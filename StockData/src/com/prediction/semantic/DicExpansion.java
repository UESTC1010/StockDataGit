package com.prediction.semantic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class DicExpansion {
	private static final String path = "C:/Users/rushshi/Desktop/word/";
	private static final String[] fileName = new String[]{
//		path+"privative.txt",
		path+"negative.txt",
//		path+"adv0.5.txt",
		path+"positive.txt"
//		path+"adv1.5.txt",
//		path+"adv1.75.txt",
//		path+"adv2.txt"
	};
	private static HashMap<String,Double> pWord = null;
	private static HashMap<String,Double> nWord = null;
	
	public static void main(String[] args){
		pWord = new HashMap<String, Double>();
		nWord = new HashMap<String, Double>();
		DicExpansion.LoadWord(nWord , fileName[0] , -1);
		DicExpansion.LoadWord(pWord , fileName[1] , 1);
		
		
	}
	public static void LoadWord(HashMap<String,Double> map, String wordpath, double score) {
			File file = new File(wordpath);
			BufferedReader reader = null; 
			try {
				reader = new BufferedReader(new FileReader(file));
				String tempString = null;
				while ((tempString = reader.readLine()) != null) {
					map.put(tempString, score);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
				}
			}
	}
	public static void expandDic(){
		
	}
}
