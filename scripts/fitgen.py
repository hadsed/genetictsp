'''

File: fitgen.py
Author: Hadayat Seddiqi
Description: Generate a gnuplot script to fit the
             data to the given fitting functions.

'''

import numpy as np

itrvec = np.loadtxt('par_1_itr_time.dat', usecols=[0], delimiter=' ', unpack=True)
citvec = np.loadtxt('par_1_cit_time.dat', usecols=[0], delimiter=' ', unpack=True)
popvec = np.loadtxt('par_1_pop_time.dat', usecols=[0], delimiter=' ', unpack=True)

itrmax = itrvec[-1]
citmax = citvec[-1]
popmax = popvec[-1]

threads = [1, 4, 8, 12]

header = "clear\nset terminal png size 800,600 font 'Verdana, 14'\nset term png\nset key left\n\n"
itr = "set output 'fit_time_itr.png' \nset title 'Fitted Number of Iterations vs. Runtime' \n" + \
      "set xlabel 'Number of Iterations'\nset ylabel 'Runtime (seconds)'\n"
cit = "set output 'fit_time_cit.png' \nset title 'Fitted Number of Cities vs. Runtime' \n" + \
      "set xlabel 'Number of Cities'\nset ylabel 'Runtime (seconds)'\n"
pop = "set output 'fit_time_pop.png' \nset title 'Fitted Number of Population Size vs. Runtime' \n" + \
      "set xlabel 'Population Size'\nset ylabel 'Runtime (seconds)'\n"

# Fill out for iterations
for k in range(len(threads)) :
    thr = str(threads[k])
    itr += "f1_" + thr + "(x) = a_" + thr + "*x + b_" + thr + "\na_" + thr + " = 1; b_" + thr + " = 1;\n"
    itr += "fit f1_" + thr + "(x) 'par_" + thr + "_itr_time.dat' using 1:($2/1000)" + \
        "via a_" + thr + ",b_" + thr +"\n"

itr += "plot [0:" + str(itrmax) + "] " 
for k in range(len(threads)) :
    thr = str(threads[k])
    itr += "f1_" + thr + "(x) title '" + thr + " Threads'"
    if ((k + 1) < len(threads)) : itr += ", "
    else : itr += "\n\n"


# Fill out for cities
for k in range(len(threads)) :
    thr = str(threads[k])
    cit += "f2_" + thr + "(x) = a_" + thr + "*(x + b_" + thr + ") + c_" + thr + "\n" + \
        "a_" + thr + " = 1; b_" + thr + " = 1; c_" + thr + " = 1;\n"
    cit += "fit f2_" + thr + "(x) 'par_" + thr + "_cit_time.dat' using 1:($2/1000)" + \
        "via a_" + thr + ",b_" + thr + ",c_" + thr + "\n"

cit += "plot [0:" + str(citmax) + "] " 
for k in range(len(threads)) :
    thr = str(threads[k])
    cit += "f2_" + thr + "(x) title '" + thr + " Threads'"
    if ((k + 1) < len(threads)) : cit += ", "
    else : cit += "\n\n"


# Fill out for population size
for k in range(len(threads)) :
    thr = str(threads[k])
    itr += "f3_" + thr + "(x) = a_" + thr + "*x + b_" + thr + "\na_" + thr + " = 1; b_" + thr + " = 1;\n"
    itr += "fit f3_" + thr + "(x) 'par_" + thr + "_pop_time.dat' using 1:($2/1000)" + \
        "via a_" + thr + ",b_" + thr +"\n"

pop += "plot [0:" + str(popmax) + "] " 
for k in range(len(threads)) :
    thr = str(threads[k])
    pop += "f3_" + thr + "(x) title '" + thr + " Threads'"
    if ((k + 1) < len(threads)) : pop += ", "
    else : pop += "\n\n"

script = header + itr + cit + pop

with open("curvescript.gnu", "w") as plotfile : plotfile.write(script)
