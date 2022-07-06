package org.evosuite.ga.operators.crossover;

import org.evosuite.ga.Chromosome;
import org.evosuite.ga.ConstructionFailedException;
import org.evosuite.utils.Randomness;

public class FitnessCrossOver extends CrossOverFunction {
    @Override
    public void crossOver(Chromosome parent1, Chromosome parent2) throws ConstructionFailedException {
       
        if (parent1.size() < 2 || parent2.size() < 2) {
            return;
        }

        // Clone for Crossover operation
        Chromosome t1 = parent1.clone();
        Chromosome t2 = parent2.clone();
          
        // Get Fitness values of chromosones
        double fitness1 = parent1.getFitness();
        double fitness2 = parent2.getFitness();

        double ratio;
        if(fitness1 < fitness2) {
            ratio = Math.max(0.2, fitness1 / fitness2);

            // Determine Crossover Points based on fitness values of the chromosones
            int point1 = (int) Math.round(parent1.size() * ratio / 2.0);
            int point2 = (int) Math.round(parent2.size() * (1.0 - (ratio / 2.0)));

            parent1.crossOver(t2, point1, point2);
            parent2.crossOver(t1, point2, point1);
        }
        else {
            ratio = Math.max(0.2, fitness2 / fitness1);

            // Determine Crossover Points based on fitness values of the chromosones
            int point1 = (int) Math.round(parent2.size() * ratio / 2.0);
            int point2 = (int) Math.round(parent1.size() * (1.0 - (ratio / 2.0)));

            parent2.crossOver(t1, point1, point2);
            parent1.crossOver(t2, point2, point1);
        }
    }
}
