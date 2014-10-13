//package com.prediction.training;
//
//import java.text.ParseException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.Set;
//
//import com.prediction.crawler.AllStockCode;
//import com.prediction.crawler.DBControl;
//import com.prediction.semantic.AnalysizeText;
//
//public class TapeAnalysis {
//	final static String tapepath= "C:/Users/rushshi/Desktop/tapeEmotion.txt";
//	public static ArrayList<int[]> num = new ArrayList<int[]>();
//	
//	public static void main(String[] args) throws ParseException {
//		DBControl.init("xueqiu");
//		NNTrain.ss = NNTrain.loadKWMap();
//		Date start = Tool.format.parse("2014-02-06 00:00:00");
//		int period = 100;
//		int stocknum=0;
//		double[] sum = new double[period];
//		for(int i=0;i<period;i++)
//		{
//			sum[i] = 0;
//			int[] a = new int[3];
//			a[0] = a[1] = a[2] = 0;
//			TapeAnalysis.num.add(a);
//		}	
//		
//		Set<String> set = AllStockCode.GetAllStockcode();
//		Iterator<String> it = set.iterator();
//		while(it.hasNext()){
//			String stockinfo = it.next();
//			String code = stockinfo.substring(1, 7);
//			if(code.startsWith("60")||code.startsWith("90"))
//			{
//				code = "SH"+code;
//				ArrayList<Double> array = AnalysizeText.getSentiment(code, start,period);
//				for(int i =0;i<period;i++){
//					sum[i] += array.get(i);
//				}
//				System.out.println("now is the " + (++stocknum) +"th");
//			}
//		}
//		Tool.writetotxt(tapepath, sum ,start);
//	}
//
//}
