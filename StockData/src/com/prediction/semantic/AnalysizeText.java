package com.prediction.semantic;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.prediction.training.NNTrain;

public class AnalysizeText {
	//*
	//get the sentiment of everyday during the period
	//*
	public static ArrayList<Double> getSentiment(String code,Date start,Integer period){
		ArrayList<Double> textlistScore = new ArrayList<Double>();
		Date tempstart = start;
		Date tempend = start;
		//loop for time period
		for(int x=0;x<period;x++){
			tempstart = tempend;
			tempend = nextday(tempstart);
			ArrayList<String> textlist = TopicofOneday.getAllTopics(code, tempstart, tempend);
			double allscore = 0.0;
			//loop for grading all texts of one day
			for(int i=0; i<textlist.size(); i++){
				Double wholeclauseScore = 0.0;
				String[] textclause = textlist.get(i).split("¡£|£¬|£»");
				if(textclause.length<10){
				// loop for grading all clauses of one text
				for(int j=0;j<textclause.length;j++){
					ArrayList<String> wordlist = WordFren.getWordList(textclause[j]);
					wholeclauseScore+=getClauseSroce(wordlist);
				}
				allscore+=wholeclauseScore;
//				System.out.println(wholeclauseScore+"------" + textlist.get(i));
				writefile("C:/Users/rushshi/Desktop/score1.txt",wholeclauseScore+"------" + textlist.get(i));}
			}
			//compute sentiment of one day
			Double onedayScore = allscore/textlist.size();
			textlistScore.add(onedayScore);
		}
		return textlistScore;
	}
	
	public static void writefile(String pathname, String jj){
		File log = new File(pathname);
		try {
			FileWriter fileWriter=new FileWriter(log,true);
			fileWriter.write(jj+"\r\n");
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static Double getClauseSroce(ArrayList<String> wordlist) {
		Iterator<String> it = wordlist.iterator();
		LinkedList<Double> clauseSroceVec = new LinkedList<Double>();
		while(it.hasNext()){
			double wordsroce = getwordsroce(it.next());
			clauseSroceVec.add(wordsroce);
		}
		Double sroce = computeSroce(clauseSroceVec);
		return sroce;
	}

	private static Double computeSroce(LinkedList<Double> clauseSroceVec) {
		Double keyword = 1.0;
		if( clauseSroceVec.contains(keyword) ){
			Double score = 1.0;
			int keyindex = clauseSroceVec.indexOf(keyword);
			for(int i =0;i<keyindex;i++){
				if(clauseSroceVec.get(i) != 0.0)
					score *= clauseSroceVec.get(i);
			}
			return score;
		}
		return 0.0;
	}

	private static double getwordsroce(String string) {
		if(NNTrain.ss.containsKey(string)){
			return NNTrain.ss.get(string);
		}
		return 0;
	}

	public static LinkedList<Integer> getPuncIndex(String text){
		LinkedList<Integer> puncindex = new LinkedList<Integer>();
		String regex1 = "¡£|£¬|£»|£¡|£¿|?|!";
		Pattern pattern = Pattern.compile(regex1);
		Matcher matcher = pattern.matcher(text);
		while(matcher.find()){
			puncindex.add(matcher.start());
		}
		return puncindex;
	}
	
	public static Date nextday(Date date){
		Date nextday = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date); 
		cal.add(Calendar.DAY_OF_YEAR, 1);
		cal.add(Calendar.HOUR_OF_DAY, 9);
		nextday = cal.getTime(); 
		return nextday;
	}
}
