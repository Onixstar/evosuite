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
import org.evosuite.utils.Randomness;

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
        int point1 = 0;

        // Clone for crossover
        T t1 = parent1.clone();
        T t2 = parent2.clone();

        // Calculate ratio for iteration count based on fitness
        // Higher differences result in more copies of the chromosome with higher fitness value
        // The ratio will always be >= 1
        double ratio = 0;
        T fittest_parent = null;
        if(fitness_1 > fitness_2){
            ratio = fitness_1 / fitness_2;
            fittest_parent = parent1.clone();
        } 
        else {
            ratio = fitness_2 / fitness_1;
            fittest_parent = parent1.clone();
        }

        // Set iteration length based on ratio, which is cut off at a maximum iteration
        // Capping iteration limit at 1000 iterations
        int max_len = (int) Math.min(Math.ceil(ratio * fittest_parent.size()), 1000);

        // Threshold influences points also in each iteration, so that more genes from
        // a chromosome wit higher fitness are inherited
        // An additional random factor makes sure we have different crossover points each iteration ()
        int max_genes = Math.min(parent1.size(), parent2.size());
        for(int i = 0; i < max_len; i++){
            point1 = (int) ((Randomness.nextInt(max_genes) - 1) + ((max_genes * (int) Math.min((Math.ceil((fitness_1 * fitness_2)/(fitness_1 + fitness_2))),1)) - 1))/2;
            // Using Single-point-crossover function provided by chromosome class
            parent1.crossOver(t2, point1);
            parent2.crossOver(t1, point1);
        } 
    }
}
