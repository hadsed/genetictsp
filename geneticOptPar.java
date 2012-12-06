/******************************************************************************
 *
 * File: geneticOptPar.java
 * Author: Hadayat Seddiqi
 * Description: Part of AI term project. Implement traveling salesman
 *              problem with genetic algorithm in parallel. This is the
 *              actual parallel version.
 *
 ******************************************************************************/


import java.util.*;
import java.util.concurrent.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class geneticOptPar extends Frame {
    public static final boolean outputFlag = true;
    public static final boolean guiFlag = true;
    public static final boolean threadedCitiesFlag = true;
    public static final boolean threadedEvolveFlag = true;
    
    public static final int width = 1000;
    public static final int height = 1000;
    public static int numIter = 500;
    public static int numCities = 100;
    public static int popSize = 100*numCities;
    public static int numThreads = Runtime.getRuntime().availableProcessors();
    public static CyclicBarrier barrier;

    private static int[] cities, x, y;
    private static chromosome current, population[];
    private static Random rand;
    private static int generation = 0;
    private static long startTime, endTime;

    public class citySetupThread implements Runnable {
	int start, end;

	public citySetupThread(int s, int e) {
	    start = s;
	    end = e;
	}
	public void run() {
	    // Do the work
	    for (int j = start; j < end; j++) {
		x[j] = ThreadLocalRandom.current().nextInt(0, width - 100) + 40;
		y[j] = ThreadLocalRandom.current().nextInt(0, height - 100) + 40;
	    }

	    // Wait for our brethren
	    try {
		barrier.await();
	    } catch (InterruptedException ie) {
		return;
	    } catch (BrokenBarrierException bbe) {
		return;
	    }

	}
    }

    public class evolveThread implements Runnable {
	int start, end;

	public evolveThread(int s, int e) {
	    start = s;
	    end = e;
	}
	public void run() {
	    // Get midpoint
	    int n = population.length/2, m;

	    for (m = start; m > end; m--) {
		int i, j;
		i = ThreadLocalRandom.current().nextInt(0, n);

		do {
		    j = ThreadLocalRandom.current().nextInt(0, n);
		} while(i == j);

		population[m].crossover(population[i], population[j]);
		population[m].mutate(numCities);
	    }
	    
	    // Chillax, wait for your hombres
	    try {
		barrier.await();
	    } catch (InterruptedException ie) {
		return;
	    } catch (BrokenBarrierException bbe) {
		return;
	    }

	}
    }

    public static void main(String args[]) {
	// We're timing this
	startTime = System.currentTimeMillis();

	// Read in number of iterations
	if (args.length > 0) {
	    try {
		numIter = Integer.parseInt(args[0]);
		numCities = Integer.parseInt(args[1]);
		popSize = Integer.parseInt(args[2]);
		numThreads = Integer.parseInt(args[3]);
	    } catch (NumberFormatException e) {
		System.err.println("Argument" + " must be an int.");
		System.exit(1);
	    }
	}

	// Some bug with TimSort causes error with Comparator--revert to legacy MergeSort
	System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");

	// Set up thread pool
	final ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(numThreads*numIter);
	ThreadPoolExecutor tpool = new ThreadPoolExecutor(numThreads, numThreads, 10, 
							  TimeUnit.SECONDS,queue);

	// Create a barrier for the threads
	barrier = new CyclicBarrier(numThreads + 1);

	// It's game time, people
	geneticOptPar k = new geneticOptPar();
	k.init(k, tpool);

	// Done here, let's go home
	tpool.shutdown();

	// Stop timing
	endTime = System.currentTimeMillis();

	// Output time data to file
	if (outputFlag) {
	    try {
		String spacer = "    ";
		PrintWriter outStream = new PrintWriter(new BufferedWriter(new FileWriter("parout.dat", true)));
		outStream.printf("%-8d %-8d %-8d %-8d %-8d %n", numIter, numCities, popSize, 
				 numThreads, (endTime - startTime));
		outStream.close();
	    } catch (IOException ioe) {
		System.err.println("IOException: " + ioe.getMessage());
	    }
	}

	// Doesn't like exiting.. so slap it with a large trout
	if (!guiFlag) System.exit(0);
    }

    public void init(geneticOptPar k, ThreadPoolExecutor tpool) {
	// Initialize data
	cities = new int[numCities];
	x = new int[numCities];
	y = new int[numCities];
	population = new chromosome[popSize];
	
	// Seed for deterministic output by putting a constant arg
	rand = new Random(8);

	if (threadedCitiesFlag) {
	    // Threaded city setup
	    int errorCities = 0, stepCities = 0;
	    stepCities = numCities/numThreads;
	    errorCities = numCities - stepCities*numThreads;
           
	    // Split up work, assign to threads
	    for (int i = 1; i <= numThreads; i++) {
		int startCities = (i-1)*stepCities;
		int endCities = startCities + stepCities;

		// This is a bit messy...
		if(i <= numThreads) endCities += errorCities;
		tpool.execute(new citySetupThread(startCities, endCities));
	    }

	    // Wait for our comrades
	    try {
		barrier.await();
	    } catch (InterruptedException ie) {
		return;
	    } catch (BrokenBarrierException bbe) {
		return;
	    }

	} else {
	    // Set up city topology, make sure no one falls off the edge
	    for (int i = 0; i < numCities; i++) {
		x[i] = rand.nextInt(width - 100) + 40;
		y[i] = rand.nextInt(height - 100) + 40;
	    }
	}

	// Set up population
	for (int i = 0; i < population.length; i++) {
	    population[i] = k.new chromosome();
	    population[i].mutate(numCities);
	}

	// Pick out the strongest
	Arrays.sort(population, population[0]);
	current = population[0];

	if (guiFlag) {
	    // Windowing stuff
	    k.setTitle("Parallel Traveling Salesman via Genetic Algorithm");
	    k.setBackground(Color.black);
	    k.setSize(width, height);
	    k.addWindowListener(new WindowAdapter(){
		    public void windowClosing(WindowEvent we){
			System.exit(0);
		    }
		});
	    k.setVisible(true);
	}

	// Loop through -- Yes, it shouldn't be here, but clearly I'm an OOP noob. Sorry.
	for (int p = 0; p < numIter; p++) evolve(p, tpool);

	// Final paint job
	if (guiFlag) repaint();
    }

    public void evolve(int p, ThreadPoolExecutor tpool) {
	if (threadedEvolveFlag) {
	    // Threaded inner loop
	    int startEvolve = popSize - 1,
		endEvolve = (popSize - 1) - (popSize - 1)/numThreads;

	    // Split up work, assign to threads
	    for (int i = 0; i < numThreads; i++) {
		endEvolve = (popSize - 1) - (popSize - 1)*(i + 1)/numThreads + 1;
		tpool.execute(new evolveThread(startEvolve, endEvolve));
		startEvolve = endEvolve;
	    }

	    // Wait for our comrades
	    try {
		barrier.await();
	    } catch (InterruptedException ie) {
		return;
	    } catch (BrokenBarrierException bbe) {
		return;
	    }

	} else {
	    // Get top half for random number bounds
	    int n = population.length/2, m;

	    // Go through entire population backwards, replace them with children of top half parents
	    for (m = population.length - 1; m > 1; m--) {
		// Two random parents, i and j
		int i = rand.nextInt(n), j;
		do {
		    j = rand.nextInt(n);
		} while(i == j);

		// Assign child genes from parents i and j, then mutate
		population[m].crossover(population[i], population[j]);
		population[m].mutate(numCities);
	    }
	}

	// Strongest child, supposedly
	population[1].crossover(population[0], population[1]);
	population[1].mutate(numCities);
	population[0].mutate(numCities);

	// Pick out the real strongest
	Arrays.sort(population, population[0]);
	current = population[0];
	generation++;

	// Redo our paint-job if needed
      	if (guiFlag) repaint();
    }

    public void paint(Graphics g) {
	// Line color
	g.setColor(Color.gray);

	// Fill in node graphic
	for (int i = 0; i < cities.length; i++) g.fillOval(x[i] - 5, y[i] - 5, 7, 7);

	// Set up edges
	for (int i = 0; i < cities.length; i++) {
	    int icity = current.genes[i];
	    if (i != 0) {
		int last = current.genes[i - 1];
		g.drawLine(x[icity], y[icity], x[last], y[last]);
	    }
	}

	// Printed information
	FontMetrics fm = g.getFontMetrics();
	g.drawString("Generation: " + generation 
		     + "    Time: " + (System.currentTimeMillis() - startTime) + " ms"
		     + "    Overall Cost: " + current.cost,
		     8, height - fm.getHeight());
    }

    // Find distance between two cities
    public int distance(int m, int n) {
	if (m >= numCities) m = 0;
	if (n >= numCities) n = 0;

	int xdiff = x[m] - x[n];
	int ydiff = y[m] - y[n];

	return (int)Math.sqrt(xdiff*xdiff + ydiff*ydiff);
    }

    public class chromosome implements Comparator {
	int genes[];
	int cost;

	public chromosome() {
	    genes = new int[numCities];
	    b = new BitSet(numCities);
	    for (int i = 0; i < numCities; i++) genes[i] = i;
	    cost = cost();
	}

	public int cost() {
	    int d = 0;

	    // Calculate distance to walk along path
	    for (int i = 1; i < genes.length; i++)
		d += distance(genes[i], genes[i - 1]);
	    return d;
	}

	public void mutate(int n) {
	    // Loop through numCities
	    while (--n >= 0) {
		int p, q, r, s;

		// Pick two random cities
		if (threadedEvolveFlag) p = ThreadLocalRandom.current().nextInt(0, numCities - 1);
		else p = rand.nextInt(numCities - 1);
		do { 
		    if (threadedEvolveFlag) q = ThreadLocalRandom.current().nextInt(0, numCities - 1);
		    else q = rand.nextInt(numCities - 1); 
		} while(q == p);

		// Scramble the cost function (distances initialize from p --> p + 1 == 1 shown inside
		// chromosome constructor)
		int old = distance(genes[p], genes[p + 1]) + distance(genes[q], genes[q + 1]);
		int guess = distance(genes[p], genes[q]) + distance(genes[p + 1], genes[q + 1]);

		// Negative feedback selection
		if (guess >= old) continue;

		// Adjust cost
		cost -= old - guess;

		// p must be less than q
		if (q < p) {
		    r = p;
		    p = q;
		    q = r;
		}

		// Start from random points, converge inward while swapping symmetrically
		for (; q > p; q--, p++) {
		    s = genes[p + 1];
		    genes[p + 1] = genes[q];
		    genes[q] = s;
		}
	    }
	}

	public int compare(Object a, Object b) {
	    // Use for sorting
	    return ((chromosome)a).cost - ((chromosome)b).cost;
	}

	public void crossover(chromosome dad, chromosome mom) {
	    int i;

	    if (threadedEvolveFlag) i = ThreadLocalRandom.current().nextInt(0, numCities);
	    else i = rand.nextInt(numCities);

	    // Start at a random city, find a closer city or march to the end
	    while (i < numCities - 1) {
		// Pick our child well
		int child = distance(dad.genes[i], mom.genes[i+1]);

		// See if he's any good
		if (child < distance(dad.genes[i], dad.genes[i+1]) &&
		    child < distance(mom.genes[i], mom.genes[i+1])) {
		    mate(dad, mom, i);
		    break;
		}

		// Guess not.. keep looking
		i++;
	    }
	}

	BitSet b;

	private void mate(chromosome dad, chromosome mom, int i) {
	    b.clear();

	    if (this == mom) {
		chromosome temp = mom;
		mom = dad;
		dad = temp;
	    }

	    // Assign father's part to child
	    for (int j = 0; j <= i; j++) {
		genes[j] = dad.genes[j];
		b.set(genes[j]);
	    }

	    int j, k = i + 1;

	    // Assign mother's part to child
	    for (j = i + 1; j < genes.length; j++) {
		if (b.get(mom.genes[j])) continue;
		genes[k] = mom.genes[j];
		b.set(genes[k++]);
	    }

	    j = 0;

	    // Iterate over till we hit a "zero" in the bitfield for mom, then 
	    // replace that one. Rinse and repeat.
	    while(k < genes.length && 
		  j < mom.genes.length) {
		while(j < mom.genes.length - 1 && 
		      b.get(mom.genes[j])) 
		    j++;
		genes[k] = mom.genes[j];
		k++;
		j++;
	    }

	    // Update cost for walking the path
	    cost = cost();
	}
    }
}
