'''

File: analysis.py
Author Hadayat Seddiqi
Description: Get all 'converted' data files, generate
             three different plots based on iterations,
             cities, and population size versus run time
             for all thread numbers.

'''

import numpy as np
from operator import itemgetter

num = [1, 4, 8, 12]

# Extract data for a Runtime vs. Number of Iterations graph
for k in range(len(num)) :
    fstr = 'par_' + str(num[k]) +'.dat'

    # Load in data file
    itr, cit, pop, thr, time = np.loadtxt(fstr, usecols=[0,1,2,3,4], delimiter=' ', unpack=True)

    # Extract data for a Num. of Iterations vs. Runtime graph
    outstr = 'par_' + str(num[k]) + '_itr_time.dat'
    data = []

    print ("Blarb:")
    print (cit[-1])
    print (pop[-1])
    print (itr[-1])

    for i in range(len(itr)) :
        if ( (cit[i] == cit[-1]) & (pop[i] == pop[-1]) ) :
            data.append([itr[i], time[i]])

    data.sort(key=itemgetter(0))
    np.savetxt(outstr, data)

    # Extract data for a Num. of Cities vs. Runtime graph
    outstr = 'par_' + str(num[k]) + '_cit_time.dat'
    data = []

    for i in range(len(itr)) :
        if ( (itr[i] == itr[-1]) & (pop[i] == pop[-1]) ) :
            data.append([cit[i], time[i]])
            
    data.sort(key=itemgetter(0))
    np.savetxt(outstr, data)
    
    # Extract data for a Population Size vs. Runtime graph
    outstr = 'par_' + str(num[k]) + '_pop_time.dat'
    data = []

    for i in range(len(itr)) :
        if ( (cit[i] == cit[-1]) & (itr[i] == itr[-1]) ) :
            data.append([pop[i], time[i]])
            
    data.sort(key=itemgetter(0))
    np.savetxt(outstr, data)
