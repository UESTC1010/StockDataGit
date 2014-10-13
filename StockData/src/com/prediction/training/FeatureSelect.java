//package com.prediction.training;
//
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.Calendar;
//import java.util.Date;
//
//import org.neuroph.core.learning.SupervisedTrainingElement;
//import org.neuroph.core.learning.TrainingSet;
//
//import com.prediction.crawler.DBControl;
//
//public class FeatureSelect {
//	static String path = "C:/Users/rushshi/Desktop/cpu.arff";	
//	
//	public static TrainingSet<SupervisedTrainingElement> getdataset(String code, Date start, Date end){
//		TrainingSet<SupervisedTrainingElement> trainingSet = new TrainingSet<SupervisedTrainingElement>(7, 1);
//		Date tempday = start;
//		Date nextday = start;
//		do{
//			tempday = nextday;
//			nextday = Tool.nextday(tempday);
//			//获取input向量
//			double[] feature = FeatureSelect.getFeature(code,tempday);
//			//获取output向量
//			Double label = getLabel(code, nextday);
//			if(feature!=null && label != null){
//				double[] Label = new double[]{label};
//				trainingSet.addElement(new SupervisedTrainingElement(feature, Label));
//				Tool.writetotxt("C:/Users/rushshi/Desktop/X.txt",feature,Label);
//			}
//		}while(nextday.before(end));
//		return trainingSet;	
//	}
//	
//	public static double[] getFeature(String code, Date start){
//		double[] s = DBControl.GetFeatureFromDB(code, start);
//		return s != null? s:null;
//	}
//	
//	public static Double getLabel(String code, Date start){
//		Double label = DBControl.GetZhangfu(code, start);
//		if(label != null)
//			return label;
//		else
//			return null;
//	}
//}
