'''

Run my java programs to output data for graphs.

'''

import os

java = "/usr/lib/jvm/java-7-openjdk-amd64/bin/java"
prog = "geneticOptPar"
command = java + " " + prog
space = " "
delim = "_"

processors = [1, 4, 8, 12]

itrscale = 100
citscale = 100
popscale = 1000

itrrange = 20
citrange = 20
poprange = 20

for m in processors: # For threads
    for i in range(1, itrrange + 1) : # For iterations
        cmd = (command + space +
               str(i*itrscale) + space +
               str(citscale) + space +
               str(popscale) + space +
               str(m))
        os.system(cmd)
    for i in range(1, citrange + 1) : # For cities
        cmd = (command + space +
               str(itrscale) + space +
               str(i*citscale) + space +
               str(popscale) + space +
               str(m))
        os.system(cmd)
    for i in range(1, poprange + 1) : # For population size
        cmd = (command + space +
               str(itrscale) + space +
               str(citscale) + space +
               str(i*popscale) + space +
               str(m))
        os.system(cmd)

    os.system("mv parout.dat parout" + delim + str(m) + ".dat")
