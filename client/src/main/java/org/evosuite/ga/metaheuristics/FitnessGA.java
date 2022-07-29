/**
 * Copyright (C) 2010-2018 Gordon Fraser, Andrea Arcuri and EvoSuite
 * contributors
 * <p>
 * This file is part of EvoSuite.
 * <p>
 * EvoSuite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3.0 of the License, or
 * (at your option) any later version.
 * <p>
 * EvoSuite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public
 * License along with EvoSuite. If not, see <http://www.gnu.org/licenses/>.
 */
package org.evosuite.ga.metaheuristics;

import org.evosuite.Properties;
import org.evosuite.ga.Chromosome;
import org.evosuite.ga.ChromosomeFactory;
import org.evosuite.ga.ConstructionFailedException;
import org.evosuite.runtime.util.AtMostOnceLogger;
import org.evosuite.utils.Randomness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Implementation of FitnessGA
 */
public class FitnessGA<T extends Chromosome<T>> extends StandardGA<T> {

    private static final long serialVersionUID = 8305884372813786175L;
    private final Logger logger = LoggerFactory.getLogger(FitnessGA.class);

    /**
     * Constructor
     *
     * @param factory a {@link org.evosuite.ga.ChromosomeFactory} object.
     */
    public FitnessGA(ChromosomeFactory<T> factory) {
        super(factory);
    }

    @Override
    protected void evolve() {

        // Elitism
        List<T> newGeneration = new ArrayList<>(elitism());

        // Truncation selection
        List<T> candidates = population.subList(0, (int) (population.size() * Properties.TRUNCATION_RATE));

        // If there are no candidates, the parameters are not set optimally,
        if (candidates.size() <= 1) {
            candidates.addAll(population);
            AtMostOnceLogger.warn(logger, "Not sufficient candidates for reproduction, consider increasing the population size, or the truncation rate");
        }

        // Determine median Fitness value of all candidates
        List<Double> FitnessValues = new ArrayList<>();
        for(int i = 0; i < candidates.size(); i++) {
            FitnessValues.add(candidates.get(i).getFitness());
        }
        
        Collections.sort(FitnessValues);
        
        double median = 0.0;
        if(Properties.MEDIAN == true) { // MEDIAN
            median = FitnessValues.get(FitnessValues.size() / 2);
        }
        else {  // MEAN
            double fitnessSum = 0.0;
            for(int i = 0; i < FitnessValues.size(); i++) {
                fitnessSum += FitnessValues.get(i);
            }

            median = fitnessSum / FitnessValues.size();
        }


        // new_generation.size() < population_size
        while (!isNextPopulationFull(newGeneration)) {

            T parent1 = Randomness.choice(candidates);
            T parent2 = Randomness.choice(candidates);

            // Self-breeding is nor allowed
            if (parent1 == parent2) {
                continue;
            }

            if ((parent1.getFitness() + parent2.getFitness()) > (median * 2.0)) {
                continue;
            }

            T offspring1 = parent1.clone();
            T offspring2 = parent2.clone();

            try {
                crossoverFunction.crossOver(offspring1, offspring2);
            } catch (ConstructionFailedException e) {
                logger.info("CrossOver/Mutation failed.");
                continue;
            }
            
            T offspring = Randomness.choice(offspring1, offspring2);

            notifyMutation(offspring);
            offspring.mutate();

            if (offspring.isChanged()) {
                offspring.updateAge(currentIteration);
            }
            if (!isTooLong(offspring)) {
                newGeneration.add(offspring);
            }
        }

        population = newGeneration;
        //archive
        updateFitnessFunctionsAndValues();
        //
        currentIteration++;
    }

}
