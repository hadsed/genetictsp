'''

File: conversion.py
Author Hadayat Seddiqi
Description:  This reads in any arbitrary data file and converts
              it into a form that numpy can read and not bitch 
              about (somehow loadtxt() thinks some things are 
              strings.... oh well).

'''

import numpy as np

num = [1, 4, 8, 12]

for k in range(len(num)) :
    fstr = 'parout_' + str(num[k]) +'.dat'
    data = []
    
    # Load in data file
    for line in open(fstr) :
        # Put into array
        line = line.split()
        # Convert to floats, restructure data
        p = [float(line[0]), float(line[1]), float(line[2]), float(line[3]), float(line[4])]
        data.append(p)

    if 0 :
    #for i in range(len(data)) :
        print (data[i])

    # Save to file
    np.savetxt("par_" + str(num[k]) + ".dat", data)
