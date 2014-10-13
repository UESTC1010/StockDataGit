package com.prediction.training;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Tool {
	public static DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static DecimalFormat df = new DecimalFormat("000.00");
	
	public static void main(String[] args){
	}
	
	//write topic to txt
	public  static void writetotxt(String txtpath,double[] inputX, double[] inputY) {
		File file = new File(txtpath);
		try {
			FileWriter fileWriter=new FileWriter(file,true);
//			for(double i:inputX){
//				fileWriter.write(i+" ");
//			}
			fileWriter.write(df.format(inputX[6])+" ");
			fileWriter.write(inputY[0]+"\r\n");
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writetotxt(String txtpath, ArrayList<String> topics){
		File file = new File(txtpath);
		try {
			FileWriter fileWriter=new FileWriter(file,true);
			int i = 0;
			for(String s:topics){
				fileWriter.write(i+"."+s+"\r\n");
				i++;
			}
			
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writetotxt(String txtpath, String topic){
		File file = new File(txtpath);
		try {
			FileWriter fileWriter=new FileWriter(file,true);
			fileWriter.write("----"+topic+"\r\n");
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	public  static void writetotxt(String txtpath,double[] x, Date date) {
//		File file = new File(txtpath);
//		try {
//			FileWriter fileWriter=new FileWriter(file,true);
//			for(int i =0;i<x.length;i++){
//				Date nextday = addday(date,i+1);
//				if( checkMonday(nextday) )
//				{
//					float weekendpos = TapeAnalysis.num.get(i)[0]+TapeAnalysis.num.get(i-1)[0];
//					float weekendneg = TapeAnalysis.num.get(i)[1]+TapeAnalysis.num.get(i-1)[1];
//					float weekendsum = TapeAnalysis.num.get(i)[2]+TapeAnalysis.num.get(i-1)[2];
//					fileWriter.write(nextday + "  "+ df.format(x[i])+"   "+ weekendpos +"   "
//						+ weekendneg +"   "+ weekendsum + "   " + weekendpos/(weekendpos + weekendneg)+  "\r\n");
//				}
//				
//				else if( checkWeekend(nextday) )  ;
//				else{
//					float pos = TapeAnalysis.num.get(i)[0];
//					float neg = TapeAnalysis.num.get(i)[1];
//					float sum = TapeAnalysis.num.get(i)[2];
//					fileWriter.write(nextday +"  "+ df.format(x[i])+"   "+pos+"   "
//						+ neg +"   " + sum + "   " + pos/(pos+neg) + "\r\n");
//				}					
//			}
//			fileWriter.flush();
//			fileWriter.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	
	public static Date nextday(Date date){
		Date nextday = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date); 
		cal.add(Calendar.DAY_OF_YEAR, 1);
//		cal.add(Calendar.HOUR_OF_DAY, 9);
		nextday = cal.getTime(); 
		return nextday;
	}
	
	public static Date addday(Date date,int i){
		Date nextday = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date); 
		cal.add(Calendar.DAY_OF_YEAR, i);
//		cal.add(Calendar.HOUR_OF_DAY, 9);
		nextday = cal.getTime(); 
		return nextday;
	}
	
	public static boolean checkMonday(Date date){
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY)
	    	return true;
	    else
	    	return false;
	}
	public static boolean checkWeekend(Date date){
		Calendar cal = Calendar.getInstance();
	    cal.setTime(date);
	    if(cal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY||cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY)
	    	return true;
	    else
	    	return false;
	}
}
