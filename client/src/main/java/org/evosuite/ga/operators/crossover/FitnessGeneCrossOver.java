/*
 * Copyright (C) 2010-2018 Gordon Fraser, Andrea Arcuri and EvoSuite
 * contributors
 *
 * This file is part of EvoSuite.
 *
 * EvoSuite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3.0 of the License, or
 * (at your option) any later version.
 *
 * EvoSuite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with EvoSuite. If not, see <http://www.gnu.org/licenses/>.
 */
package org.evosuite.ga.operators.crossover;

import org.evosuite.ga.Chromosome;
import org.evosuite.ga.ConstructionFailedException;
import java.util.Random;
// import org.evosuite.runtime.Random;

/**
 * Select parts with good fitness value multiple times
 *
 * @author Gordon Fraser
 */
public class MultDupCrossOver<T extends Chromosome<T>> extends CrossOverFunction<T> {

    private static final long serialVersionUID = 2881387570766261795L;

    /**
     * {@inheritDoc}
     * <p>
     * Test sets with higher fitness values may be inherited multiple times.
     *
     * @param parent1
     * @param parent2
     */
    @Override
    public void crossOver(T parent1, T parent2)
            throws ConstructionFailedException {
        
        // Single gene per chromosome means no possible crossover
        if (parent1.size() < 2 || parent2.size() < 2) {
            return;
        }

        // Get fitness values from chromosomes
        double fitness_1 = parent1.getFitness();
        double fitness_2 = parent2.getFitness();

        // Clone for crossover
        T t1 = parent1.clone();
        T t2 = parent2.clone();

        // Create RNG
        Random rand = new Random();

        // Calculate ratio for iteration count based on fitness
        // Higher differences result in more copies of the genes from the fitter parent
        // The ratio will always be < 1
        double ratio = 0;
        int fittest_parent = 0;
        if(fitness_1 < fitness_2){
            ratio = fitness_1 / fitness_2;
            fittest_parent = 1;
        } 
        else {
            ratio = fitness_2 / fitness_1;
            fittest_parent = 2;
        }

        // Prob influences points also in each iteration, so that more genes from
        // a chromosome with lower (better) fitness inherits its genes with a higher probability
        int max_genes = Math.min(parent1.size(), parent2.size());
        double prob = 1.0 - (ratio/2.0);
        for(int i = 0; i < max_genes-1; i++){
            double[] rnd_value = {rand.nextFloat(), rand.nextFloat()};
            if(fittest_parent == 1){
                if(rnd_value[0] > prob){
                    parent1.crossOver(t2, i);
                }
                else{
                    parent1.crossOver(t1, i);
                }

                if(rnd_value[1] > prob){
                    parent2.crossOver(t2, i);
                }
                else{
                    parent2.crossOver(t1, i);
                }
            }
            else{
                if(rnd_value[0] > prob){
                    parent1.crossOver(t1, i);
                }
                else{
                    parent1.crossOver(t2, i);
                }

                if(rnd_value[1] > prob){
                    parent2.crossOver(t1, i);
                }
                else{
                    parent2.crossOver(t2, i);
                }
            }
        }
        if(fittest_parent == 1){
            parent1.crossOver(t1, max_genes-1);
            parent2.crossOver(t1, max_genes-1);
        } 
        else{
            parent1.crossOver(t2, max_genes-1);
            parent2.crossOver(t2, max_genes-1);
        }
    }
}
