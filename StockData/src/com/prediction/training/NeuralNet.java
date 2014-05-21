package com.prediction.training;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingElement;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.norm.MaxMinNormalizer;
import org.neuroph.util.norm.Normalizer;

import com.prediction.crawler.DBControl;

public class NeuralNet {
	static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static void main(String[] args){
		DBControl.init("xueqiu");
		NNTrain.ss = NNTrain.loadKWMap();
		
		Date start = null;
		Date end = null;
		try {
			start = format.parse("2014-04-15 00:00:00");
			end  = format.parse("2014-04-29 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		NeuralNetwork neuralNet = new MultiLayerPerceptron(7, 10,1);
		BackPropagation learningRule = (BackPropagation) neuralNet
				.getLearningRule();
		learningRule.setLearningRate(0.3);
		learningRule.setMaxError(0.1);

		// create training set
		TrainingSet<SupervisedTrainingElement> trainingSet = FeatureSelect.getdataset("SZ002024",start, end);
		trainingSet.normalize(new MaxMinNormalizer());
		for (TrainingElement trainingElement : trainingSet.elements()) {
			FeatureSelect.writetotxt(trainingElement.getInput(),new double[]{1});
		}
		System.out.println("--------------------get the training data------------------");
		// train the network with training set
		neuralNet.learn(trainingSet);
		neuralNet.save("or_perceptron.nnet");
		NeuralNet.testvalidate();
	}
	
	public static void testvalidate(){
		// load the saved network
		NeuralNetwork neuralNetwork = NeuralNetwork.load("or_perceptron.nnet");
		Date start = null;
		Date end = null;
		try {
			start = format.parse("2014-05-01 00:00:00");
			end  = format.parse("2014-05-19 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		TrainingSet<SupervisedTrainingElement> trainingSet = FeatureSelect.getdataset("SZ002024",start, end);
		trainingSet.normalize();
		Iterator<SupervisedTrainingElement> it = trainingSet.iterator();
		while(it.hasNext()){
			neuralNetwork.setInput(it.next().getInput());
			neuralNetwork.calculate();
			double[] networkOutput = neuralNetwork.getOutput();
			System.out.println("trainresult = "+networkOutput[0]+" realdata = "+it.next().getLabel());
		}
	}
}
