clear
set terminal gif size 800,600 font 'Verdana, 14'
set term gif
set key left

set output 'time_itr.gif' 
set title 'Number of Iterations vs. Runtime' 
set xlabel 'Number of Iterations'
set ylabel 'Runtime (ms)'
plot 'par_1_itr_time.dat' w l title '1 Threads', 'par_4_itr_time.dat' w l title '4 Threads', 'par_8_itr_time.dat' w l title '8 Threads', 'par_12_itr_time.dat' w l title '12 Threads'

set output 'time_cit.gif' 
set title 'Number of Cities vs. Runtime' 
set xlabel 'Number of Cities'
set ylabel 'Runtime (ms)'
plot 'par_1_cit_time.dat' w l title '1 Threads', 'par_4_cit_time.dat' w l title '4 Threads', 'par_8_cit_time.dat' w l title '8 Threads', 'par_12_cit_time.dat' w l title '12 Threads'

set output 'time_pop.gif' 
set title 'Number of Population Size vs. Runtime' 
set xlabel 'Population Size'
set ylabel 'Runtime (ms)'
plot 'par_1_pop_time.dat' w l title '1 Threads', 'par_4_pop_time.dat' w l title '4 Threads', 'par_8_pop_time.dat' w l title '8 Threads', 'par_12_pop_time.dat' w l title '12 Threads'

