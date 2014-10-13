//package com.prediction.training;
//
//import java.text.ParseException;
//import java.util.Date;
//import java.util.Iterator;
//
//import org.neuroph.core.NeuralNetwork;
//import org.neuroph.core.learning.SupervisedTrainingElement;
//import org.neuroph.core.learning.TrainingElement;
//import org.neuroph.core.learning.TrainingSet;
//import org.neuroph.nnet.MultiLayerPerceptron;
//import org.neuroph.nnet.learning.BackPropagation;
//import org.neuroph.util.TransferFunctionType;
//import org.neuroph.util.norm.MaxMinNormalizer;
//
//import com.prediction.crawler.DBControl;
//
//public class NeuralNet {
//	
//	public static void main(String[] args){
//		DBControl.init("xueqiu");
//		NNTrain.ss = NNTrain.loadKWMap();
////		
//		Date start = null;
//		Date end = null;
//		try {
//			start = Tool.format.parse("2014-04-01 00:00:00");
//			end  = Tool.format.parse("2014-05-01 00:00:00");
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		
//		//tanh函数（-1，1），7个 input feature，10个中间层节点，1个output节点 
//		NeuralNetwork neuralNet = new MultiLayerPerceptron(TransferFunctionType.TANH,7, 10,1);
//		BackPropagation learningRule = (BackPropagation) neuralNet
//				.getLearningRule();
//		learningRule.setLearningRate(0.3);
//		learningRule.setMaxError(0.1);
//		learningRule.setMaxIterations(1000);
//
//		// create training set
//		TrainingSet<SupervisedTrainingElement> trainingSet = FeatureSelect.getdataset("SH600887",start, end);
//		trainingSet.normalize(new MaxMinNormalizer());
//		for (TrainingElement trainingElement : trainingSet.elements()) {
////			Tool.writetotxt("C:/Users/rushshi/Desktop/X.txt",trainingElement.getInput(),new double[]{1});
//		}
//		System.out.println("--------------------get the training data------------------");
//		// train the network with training set
////		neuralNet.learn(trainingSet);
////		neuralNet.save("or_perceptron.nnet");
////		NeuralNet.testvalidate();
//	}
//	
//	public static void testvalidate(){
//		// load the saved network
//		NeuralNetwork neuralNetwork = NeuralNetwork.load("or_perceptron.nnet");
//		Date start = null;
//		Date end = null;
//		try {
//			start = Tool.format.parse("2014-05-10 00:00:00");
//			end  = Tool.format.parse("2014-05-28 00:00:00");
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		TrainingSet<SupervisedTrainingElement> trainingSet = FeatureSelect.getdataset("SH600756",start, end);
//		trainingSet.normalize(new MaxMinNormalizer());
//		Iterator<SupervisedTrainingElement> it = trainingSet.iterator();
//		while(it.hasNext()){
//			neuralNetwork.setInput(it.next().getInput());
//			neuralNetwork.calculate();
//			double[] networkOutput = neuralNetwork.getOutput();
//			System.out.println("trainresult = "+networkOutput[0]);
//		}
//	}
//}
