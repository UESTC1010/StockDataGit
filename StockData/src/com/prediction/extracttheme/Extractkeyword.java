package com.prediction.extracttheme;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.prediction.crawler.DBControl;
import com.prediction.semantic.WordFren;
import com.prediction.training.Tool;

public class Extractkeyword {
	
	//return topic_txt array
	public static ArrayList<String> GetTopicTxtArray(String code,Date start,int interval) throws ParseException{
		ArrayList<String> txtarray = new ArrayList<String>();
		txtarray = DBControl.GetTextArray(code, start, Tool.addday(start, interval));
		return txtarray;
	}
	
	
	
	public static void main(String[] args) throws IOException, ParseException {
		DBControl.init("xueqiu");
		String code = "SZ002024";
//		Tool.writetotxt("C:\\Users\\rushshi\\Desktop\\topictxt\\2024.txt",txtarray);
		for(int i=9;i<10;i++){
			Date start = Tool.format.parse("2014-06-10 00:00:00");
			ArrayList<String> txtarray = GetTopicTxtArray(code,start, 26);
			txtarray.add(DBControl.GetText(code, Tool.addday(start, 26),  Tool.addday(start, 33)));
			HashMap<Integer, HashMap<String, Float>>  alltf = Tf_idf.tfOfAllTopics(txtarray);
			System.out.println("all tf have got");
			HashMap<String, Float> idfs = Tf_idf.idf(alltf);
			Tf_idf.tf_idf(alltf, idfs);
		}
	}

}