#!/bin/bash

for i in {1..10} 
do 
    $EVOSUITE -generateSuite -target ext4j.jar -Dalgorithm=FITNESS_GA -Dcrossover_function=GENE -Doutput_variables=configuration_id,TARGET_CLASS,Coverage,Fitness,Generations,Statements_Executed,BranchCoverage,Total_Goals 
done 

