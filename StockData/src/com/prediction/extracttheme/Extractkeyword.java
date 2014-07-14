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
	
	//return topic_txt from MongDB
	public static String GetTopicTxt(String code) throws ParseException{
		Date start;
		Date end;
		start = Tool.format.parse("2014-07-01 00:00:00");
		end = Tool.format.parse("2014-07-10 00:00:00");
		String alltext = DBControl.GetText(code, start, end);
		return alltext;
	}
	
	//return topic_txt array
	public static ArrayList<String> GetTopicTxtArray(String code) throws ParseException{
		ArrayList<String> txtarray = new ArrayList<String>();
		Date start;
		Date end;
		start = Tool.format.parse("2014-07-01 00:00:00");
		end = Tool.format.parse("2014-07-10 00:00:00");
		txtarray = DBControl.GetTextArray(code, start, end);
		return txtarray;
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		DBControl.init("xueqiu");
		ArrayList<String> txtarray = GetTopicTxtArray("SZ002024");
		
		File file = new File("C:\\Users\\rushshi\\Desktop\\topictxt\\2024.txt");
		try {
			FileWriter fileWriter=new FileWriter(file,true);
			int i = 0;
			for(String s:txtarray){
				fileWriter.write(i+"."+s+"\r\n");
				i++;
			}
			
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(txtarray.size() + " valid txts");
		HashMap<Integer, HashMap<String, Float>>  alltf = Tf_idf.tfOfAllTopics(txtarray);
		System.out.println("all tf got");
		HashMap<String, Float> idfs = Tf_idf.idf(alltf);
		Tf_idf.tf_idf(alltf, idfs);
	}

}