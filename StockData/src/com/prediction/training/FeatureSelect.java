package com.prediction.training;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import org.neuroph.core.learning.SupervisedTrainingElement;
import org.neuroph.core.learning.TrainingSet;

import com.prediction.crawler.DBControl;

public class FeatureSelect {
	static String path = "C:/Users/rushshi/Desktop/cpu.arff";	
	
	public static TrainingSet<SupervisedTrainingElement> getdataset(String code, Date start, Date end){
		TrainingSet<SupervisedTrainingElement> trainingSet = new TrainingSet<SupervisedTrainingElement>(7, 1);
		Date tempday = start;
		Date nextday = start;
		do{
			tempday = nextday;
			nextday = nextday(tempday);
			double[] feature = FeatureSelect.getFeature(code,tempday);
//			int x=0;
//			for(double i:inputX){
//				if(i == 1) x++;
//			}
			Double label = getLabel(code, nextday);
			if(feature!=null && label != null){
				double[] Label = new double[]{label};
				trainingSet.addElement(new SupervisedTrainingElement(feature, Label));
				FeatureSelect.writetotxt(feature,Label);
			}
		}while(nextday.before(end));
		return trainingSet;	
	}
	public  static void writetotxt(double[] inputX, double[] inputY) {
		File file = new File("C:/Users/rushshi/Desktop/X.txt");
		try {
			FileWriter fileWriter=new FileWriter(file,true);
			for(double i:inputX){
				fileWriter.write(i+" ");
			}
			fileWriter.write(inputY[0]+"\r\n");
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static Date nextday(Date date){
		Date nextday = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date); 
		cal.add(Calendar.DAY_OF_YEAR, 1);
		nextday = cal.getTime(); 
		return nextday;
	}
	
	public static double[] getFeature(String code, Date start){
		double[] s = DBControl.GetFeatureFromDB(code, start);
		return s != null? s:null;
	}
	
	public static Double getLabel(String code, Date start){
		Double label = DBControl.GetZhangfu(code, start);
		if(label != null)
			return label;
		else
			return null;
	}
}
