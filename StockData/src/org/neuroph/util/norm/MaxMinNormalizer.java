/**
 * Copyright 2010 Neuroph Project http://neuroph.sourceforge.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.neuroph.util.norm;

import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;

/**
 * MaxMin normalization method, which normalize data in regard to min and max elements in training set (by columns)
 * Normalization is done according to formula:
 * normalizedVector[i] = (vector[i] - min[i]) / (max[i] - min[i])
 * @author Zoran Sevarac <sevarac@gmail.com>
 */
public class MaxMinNormalizer implements Normalizer {
    double[] max; // contains max values for all columns
    double[] min; // contains min values for all columns

    @Override
    public void normalize(TrainingSet<? extends TrainingElement> trainingSet) {
        // find min i max vectors
        findMaxinAndMinVectors(trainingSet);
        System.out.println("max---"+max[2]+" "+max[4]);
        System.out.println("min---"+min[2]+" "+min[4]);
        // izvrsi normalizciju / deljenje
        for (TrainingElement trainingElement : trainingSet.elements()) {
            double[] input = trainingElement.getInput();
            double[] normalizedInput = normalizeMaxMin(input);
            
            trainingElement.setInput(normalizedInput);
        }

    }
    
    private void findMaxinAndMinVectors(TrainingSet<? extends TrainingElement> trainingSet) {
        int inputSize = trainingSet.getInputSize();
        max = new double[inputSize];
        min = new double[inputSize];
       
        int isfirstex=1;
        for (TrainingElement te : trainingSet.elements()) {
            double[] input = te.getInput();
            for (int i = 0; i < inputSize; i++) {
            	if(isfirstex == 1){
            		max[i] = input[i];
            		min[i] = input[i];
            	}
            	else{
            		if (input[i] > max[i]) {
                        max[i] = input[i];
                    }
                    if (input[i] < min[i]) {
                        min[i] = input[i];
                    }
            	}
            }
            isfirstex++;
        }        
    }

    private double[] normalizeMaxMin(double[] vector) {
        double[] normalizedVector = new double[vector.length];

        for (int i = 0; i < vector.length; i++) {
            normalizedVector[i] = (vector[i] - min[i]) / (max[i] - min[i]);
        }

        return normalizedVector;
    }
}