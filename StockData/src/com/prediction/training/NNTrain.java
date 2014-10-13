//package com.prediction.training;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.swing.text.html.HTMLDocument.Iterator;
//
//import org.neuroph.core.NeuralNetwork;
//import org.neuroph.core.learning.SupervisedTrainingElement;
//import org.neuroph.core.learning.TrainingSet;
//import org.neuroph.nnet.MultiLayerPerceptron;
//import org.neuroph.nnet.learning.BackPropagation;
//import org.neuroph.util.TransferFunctionType;
//
//import weka.core.Instances;
//
//import com.prediction.crawler.DBControl;
//import com.prediction.semantic.AnalysizeText;
//import com.prediction.semantic.WordFren;
//
//public class NNTrain {
//	public static HashMap<String, Double> ss = new HashMap<String, Double>();
//	private static final Double[] score = new Double[]{-1.0,-1.1,0.5,1.0,1.5,1.75,2.0};
//	private static final String path = "C:/Users/rushshi/Desktop/word/";
//	private static final String[] fileName = new String[]{
//		path+"privative.txt",
//		path+"negative.txt",
//		path+"adv0.5.txt",
//		path+"positive.txt",
//		path+"adv1.5.txt",
//		path+"adv1.75.txt",
//		path+"adv2.txt"
//	};
//	NeuralNetwork neuralNet;
//	TrainingSet<SupervisedTrainingElement> trainingSet;
//	TrainingSet<SupervisedTrainingElement> testingSet;
//	static int inputnum = 65;
//	static String[] keyword = new String[inputnum];
//	static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//	
//	public static HashMap<String,Double> loadKWMap() {
//		HashMap<String,Double> kwmap = new HashMap<String, Double>();
//		int i = 0;
//		for(String filename:fileName){
//			File file = new File(filename);
//			BufferedReader reader = null; 
//			try {
//				reader = new BufferedReader(new FileReader(file));
//				String tempString = null;
//				while ((tempString = reader.readLine()) != null) {
//					kwmap.put(tempString, score[i]);
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e1) {
//                }
//				}
//			}
//			i++;
//		}
//		return kwmap;
//	}
//	
//	private double[] handleText(String text) {
//		//need to improve search speed
//		double[] x = new double[inputnum];
//		try {
//			Map wordfrenmap = WordFren.getTextDef(text);
//			for(int i = 0; i<inputnum ; i++){
//				if(wordfrenmap.containsKey(keyword[i]))
//					x[i] = ((Integer)wordfrenmap.get(keyword[i])).doubleValue();
//				else 
//					x[i] = 0;
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return x;
//	}
//	
//	public double[] getInputX(String code, Date start, Date end){
//		String text = DBControl.GetText(code, start, end);
//		double[] inputX = handleText(text);
//		return inputX;
//	}
//
//	public double getInputY(String code, Date start){
//		double inputY = DBControl.GetZhangfu(code, start);
//		return inputY;
//	}
//	
//	public TrainingSet<SupervisedTrainingElement> getdataset(String code, Date start, Date end){
//		TrainingSet<SupervisedTrainingElement> trainingSet = new TrainingSet<SupervisedTrainingElement>(65, 1);
//		Date tempday = start;
//		Date nextday = start;
//		do{
//			tempday = nextday;
//			nextday = nextday(tempday);
//			double[] inputX = getInputX(code,tempday,nextday);
////			int x=0;
////			for(double i:inputX){
////				if(i == 1) x++;
////			}
//			double inputy = getInputY(code, nextday);
//			if(inputy != 2){
//				double[] inputY = new double[]{inputy};
//				trainingSet.addElement(new SupervisedTrainingElement(inputX, inputY));
//				writetotxt(inputX,inputY);
//			}
//		}while(nextday.before(end));
//		return trainingSet;	
//	}
//	
//	private void writetotxt(double[] inputX, double[] inputY) {
//		File file = new File("C:/Users/rushshi/Desktop/X.txt");
//		try {
//			FileWriter fileWriter=new FileWriter(file,true);
//			for(double i:inputX){
//				fileWriter.write(i+" ");
//			}
//			fileWriter.write(inputY[0]+"\r\n");
//			fileWriter.flush();
//			fileWriter.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	public Date nextday(Date date){
//		Date nextday = new Date();
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date); 
//		cal.add(Calendar.DAY_OF_YEAR, 1);
//		nextday = cal.getTime(); 
//		return nextday;
//	}
//	public void nntrain(String code, Date start, Date end){
//		neuralNet = new MultiLayerPerceptron(TransferFunctionType.TANH, inputnum, 3,1);
//		BackPropagation learningRule = (BackPropagation) neuralNet
//				.getLearningRule();
//		learningRule.setLearningRate(0.3);
//		learningRule.setMaxError(0.01);
//
//		// create training set
//		trainingSet = getdataset(code,start, end);
//		System.out.println("--------------------get the training data------------------");
////		testingSet = getdataset(code,start, end);
//		// train the network with training set
//		neuralNet.learn(trainingSet);
//		neuralNet.save("or_perceptron.nnet");
//	}
//	
//	public void testvalidate() throws ParseException{
//		// load the saved network
//		NeuralNetwork neuralNetwork = NeuralNetwork.load("or_perceptron.nnet");
//		Date start = format.parse("2014-04-01 00:00:00");
//		Date nextday = start;
//		for(int i =0;i<20;i++){
//			Date tempdate = nextday;
//			nextday = nextday(tempdate);
//			
//			double[] inputx = getInputX("SH600756",start, nextday);
//			// set network input
//			neuralNetwork.setInput(inputx);
//			// calculate network
//			neuralNetwork.calculate();
//			// get network output
//			double[] networkOutput = neuralNetwork.getOutput();
//			
//			System.out.println("trainresult = "+networkOutput[0]+" realdata = "+DBControl.GetZhangfu("SZ300027", nextday));
//		}
//	}
//	public static Instances getFileInstances( String fileName ) throws Exception
//    {
//        FileReader frData = new FileReader( fileName );
//          Instances data = new Instances( frData );
//        
//          return data;
//    }
//	public static void main(String args[]){
//		DBControl.init("xueqiu");
//		
////		NNTrain nn = new NNTrain();
////		
//		ss =loadKWMap();
//		
//		System.out.println(ss.size());
////		java.util.Iterator<String> it = ss.keySet().iterator();
////		while(it.hasNext()){
////			System.out.println(it.next());
////		}
////		if(ss.containsKey("¿√")){
////			System.out.println("hanyoulan");
////		}
//
//		try {
//			Date start = format.parse("2014-05-01 00:00:00");
//			Date end = format.parse("2014-05-10 00:00:00");
//			ArrayList<Double> scoreList = AnalysizeText.getSentiment("SH600756", start, 10);
//			java.util.Iterator<Double> it = scoreList.iterator();
//			while(it.hasNext()){
//				System.out.println(it.next());
//			}
////			TopicofOneday.getAllTopics("SZ300027", start, end);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//	}
//
//	
//}
