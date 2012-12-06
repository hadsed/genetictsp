'''

File: gnuplotgen.py
Author: Hadayat Seddiqi
Description: Generate a gnuplot script for plotting data and 
             outputting to a GIF image. Do three different 
             plots: iterations, cities, and population size 
             versus runtime, plotting the differing number of 
             threads on the same graph to visualize speedup gained.

'''

threads = [1, 4, 8, 12]

header = "clear\nset terminal gif size 800,600 font 'Verdana, 14'\nset term gif\nset key left\n\n"
itr = "set output 'time_itr.gif' \nset title 'Number of Iterations vs. Runtime' \n" + \
      "set xlabel 'Number of Iterations'\nset ylabel 'Runtime (ms)'\nplot "
cit = "set output 'time_cit.gif' \nset title 'Number of Cities vs. Runtime' \n" + \
      "set xlabel 'Number of Cities'\nset ylabel 'Runtime (ms)'\nplot "
pop = "set output 'time_pop.gif' \nset title 'Number of Population Size vs. Runtime' \n" + \
      "set xlabel 'Population Size'\nset ylabel 'Runtime (ms)'\nplot "

# Fill out for iterations
for k in range(len(threads)) :
    itr += ("'par_" + str(threads[k]) + "_itr_time.dat' w l title '" + str(threads[k]) + " Threads'")
    if ((k + 1) < len(threads)) : itr += ", "
    else : itr += "\n\n"

# Fill out for cities
for k in range(len(threads)) :
    cit += ("'par_" + str(threads[k]) + "_cit_time.dat' w l title '" + str(threads[k]) + " Threads'")
    if ((k + 1) < len(threads)) : cit += ", "
    else : cit += "\n\n"

# Fill out for population size
for k in range(len(threads)) :
    pop += ("'par_" + str(threads[k]) + "_pop_time.dat' w l title '" + str(threads[k]) + " Threads'")
    if ((k + 1) < len(threads)) : pop += ", "
    else : pop += "\n\n"


script = header + itr + cit + pop

with open("plotscript.gnu", "w") as plotfile : plotfile.write(script)

