EXECUTING THE SF100-BENCHMARK WITH SPECIFIC PROJECTS

Prerequisites:
	- Working Java JRE and SDK (v.11.0.15)
	- Apache Maven (v.3.8.6)
	
Setup:
	1. Download EvoSuite using: git clone https://github.com/Onixstar/evosuite.git
	2. Go into the evosuite folder
	3. Package EvoSuite mvn package
	4. Get SF100 Benchmark: wget http://www.evosuite.org/files/SF100-EvoSuite-20120316.tar.gz
	5. Unzip SF100 with: tar -Xcf SF100-EvoSuite-20120316.tar.gz
	6. Go into evosuite/master/target
	7. Set there evosuite binary path: export EVOSUITE="java -jar $(pwd)/evosuite-master-1.2.1-SNAPSHOT.jar"


Executing Tests:
	1. Copy loop.sh to th target project (inside extracted SF100 directory)
	2. Open up loop.sh
		2.1 Take a look at the statement inside the loop that calls EvoSuite
	3. Now set the "-target" option to "-target <name>.jar"
	4. You can proceed using different configurations by configuring the parameters as follows:
		4.1 Breeder Algorithm
			- Standard Breeder: "-Dalgorithm=STANDARD_GA"
			- Fitness Breeder: "-Dalgorithm=FITNESS_GA"
		4.2 Crossover Algorithm
			- Single Point Relative Crossover: "-Dcrossover_function=SPR"
			- Fitness Crossover: "-Dcrossover_function=FIT"
			- Gene Crossover: "-Dcrossover_function=GENE"
	5. Save and exit the script now.
	6. Execute the script with "./loop.sh" (no sudo necessary)

Aquire Results:
	- Results of the executed test will be generated inside <target_project>/evosuite-reports/statistics.csv
	- Note that if the "statistics.csv" file already exists, new results will always be appeneded to the end of the file