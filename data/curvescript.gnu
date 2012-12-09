clear
set terminal png size 800,600 font 'Verdana, 14'
set term png
set key left

set output 'fit_time_itr.png' 
set title 'Fitted Number of Iterations vs. Runtime' 
set xlabel 'Number of Iterations'
set ylabel 'Runtime (seconds)'
f1_1(x) = a_1*x + b_1
a_1 = 1; b_1 = 1;
fit f1_1(x) 'par_1_itr_time.dat' using 1:($2/1000)via a_1,b_1
f1_4(x) = a_4*x + b_4
a_4 = 1; b_4 = 1;
fit f1_4(x) 'par_4_itr_time.dat' using 1:($2/1000)via a_4,b_4
f1_8(x) = a_8*x + b_8
a_8 = 1; b_8 = 1;
fit f1_8(x) 'par_8_itr_time.dat' using 1:($2/1000)via a_8,b_8
f1_12(x) = a_12*x + b_12
a_12 = 1; b_12 = 1;
fit f1_12(x) 'par_12_itr_time.dat' using 1:($2/1000)via a_12,b_12
plot [0:10000.0] f1_1(x) title '1 Threads', f1_4(x) title '4 Threads', f1_8(x) title '8 Threads', f1_12(x) title '12 Threads'

f3_1(x) = a_1*x + b_1
a_1 = 1; b_1 = 1;
fit f3_1(x) 'par_1_pop_time.dat' using 1:($2/1000)via a_1,b_1
f3_4(x) = a_4*x + b_4
a_4 = 1; b_4 = 1;
fit f3_4(x) 'par_4_pop_time.dat' using 1:($2/1000)via a_4,b_4
f3_8(x) = a_8*x + b_8
a_8 = 1; b_8 = 1;
fit f3_8(x) 'par_8_pop_time.dat' using 1:($2/1000)via a_8,b_8
f3_12(x) = a_12*x + b_12
a_12 = 1; b_12 = 1;
fit f3_12(x) 'par_12_pop_time.dat' using 1:($2/1000)via a_12,b_12
set output 'fit_time_cit.png' 
set title 'Fitted Number of Cities vs. Runtime' 
set xlabel 'Number of Cities'
set ylabel 'Runtime (seconds)'
f2_1(x) = a_1*(x + b_1) + c_1
a_1 = 1; b_1 = 1; c_1 = 1;
fit f2_1(x) 'par_1_cit_time.dat' using 1:($2/1000)via a_1,b_1,c_1
f2_4(x) = a_4*(x + b_4) + c_4
a_4 = 1; b_4 = 1; c_4 = 1;
fit f2_4(x) 'par_4_cit_time.dat' using 1:($2/1000)via a_4,b_4,c_4
f2_8(x) = a_8*(x + b_8) + c_8
a_8 = 1; b_8 = 1; c_8 = 1;
fit f2_8(x) 'par_8_cit_time.dat' using 1:($2/1000)via a_8,b_8,c_8
f2_12(x) = a_12*(x + b_12) + c_12
a_12 = 1; b_12 = 1; c_12 = 1;
fit f2_12(x) 'par_12_cit_time.dat' using 1:($2/1000)via a_12,b_12,c_12
plot [0:10000.0] f2_1(x) title '1 Threads', f2_4(x) title '4 Threads', f2_8(x) title '8 Threads', f2_12(x) title '12 Threads'

set output 'fit_time_pop.png' 
set title 'Fitted Number of Population Size vs. Runtime' 
set xlabel 'Population Size'
set ylabel 'Runtime (seconds)'
plot [0:100000.0] f3_1(x) title '1 Threads', f3_4(x) title '4 Threads', f3_8(x) title '8 Threads', f3_12(x) title '12 Threads'

