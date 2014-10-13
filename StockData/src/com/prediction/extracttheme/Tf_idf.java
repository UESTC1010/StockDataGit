package com.prediction.extracttheme;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.prediction.semantic.WordFren;

public class Tf_idf {
	
	//term frequency in a file, times for each word
    public static HashMap<String, Integer> normalTF(ArrayList<String> cutwords){
        HashMap<String, Integer> intTF = new HashMap<String, Integer>();
        
        for(String word : cutwords){
            if(intTF.get(word) == null){
            	intTF.put(word, 1);
            }else{
            	intTF.put(word, intTF.get(word) + 1);
            }
        }
        return intTF;
    }
    
	//term frequency in a file, frequency of each word
    public static HashMap<String, Float> tf(String txt){
        HashMap<String, Float> resTF = new HashMap<String, Float>();
        ArrayList<String> wordlist = WordFren.getWordList(txt);
        HashMap<String, Integer> intTF = normalTF(wordlist);
        
        int wordcount = wordlist.size();
        //iterator for that get from TF
        Iterator<Entry<String, Integer>>  iter = intTF.entrySet().iterator(); 
        while(iter.hasNext()){
        	Entry<String, Integer> entry = iter.next();
            resTF.put(entry.getKey().toString(), Float.parseFloat(entry.getValue().toString())/wordcount);
//            System.out.println(entry.getKey().toString() + " = "+  Float.parseFloat(entry.getValue().toString()) / wordcount);
        }
        return resTF;
    } 
    
    //tf for all file
    public static HashMap<Integer,HashMap<String, Float>> tfOfAllTopics(ArrayList<String> topics) throws IOException{
        HashMap<Integer, HashMap<String, Float>> allTF = new HashMap<Integer, HashMap<String, Float>>();
        
        for(int i = 0; i < topics.size(); i++){
            HashMap<String, Float> dict = new HashMap<String, Float>();
            dict = Tf_idf.tf(topics.get(i));
            allTF.put(i, dict);
        }
        return allTF;
    }
    
    //idf of every word
    public static HashMap<String, Float> idf(HashMap<Integer,HashMap<String, Float>> all_tf){
        HashMap<String, Float> resIdf = new HashMap<String, Float>();
        HashMap<String, Integer> dict = new HashMap<String, Integer>();
        int docNum = all_tf.size();
        
        for(int i = 0; i < docNum; i++){
            HashMap<String, Float> temp = all_tf.get(i);
            Iterator iter = temp.entrySet().iterator();
            while(iter.hasNext()){
                Map.Entry entry = (Map.Entry)iter.next();
                String word = entry.getKey().toString();
                if(dict.get(word) == null){
                    dict.put(word, 1);
                }else {
                    dict.put(word, dict.get(word) + 1);
                }
            }
        }
//        System.out.println("IDF for every word is:");
        Iterator iter_dict = dict.entrySet().iterator();
        while(iter_dict.hasNext()){
            Map.Entry entry = (Map.Entry)iter_dict.next();
            float value = (float)Math.log(docNum / Float.parseFloat(entry.getValue().toString()));
            resIdf.put(entry.getKey().toString(), value);
//            System.out.println(entry.getKey().toString() + " = " + value);
        }
        return resIdf;
    }
    
    //compute tf_idf of every word of every topic
    public static HashMap<Integer, HashMap<String, Float>> tf_idf(HashMap<Integer,HashMap<String, Float>> all_tf,HashMap<String, Float> idfs){
        HashMap<Integer, HashMap<String, Float>> resTfIdf = new HashMap<Integer, HashMap<String, Float>>();
            
        int docNum = all_tf.size();
        for(int i = 0; i < docNum; i++){
            HashMap<String, Float> tfidf = new HashMap<String, Float>();
            HashMap<String, Float> temp = all_tf.get(i);
            Iterator iter = temp.entrySet().iterator();
            while(iter.hasNext()){
                Map.Entry entry = (Map.Entry)iter.next();
                String word = entry.getKey().toString();
                Float value = (float)Float.parseFloat(entry.getValue().toString()) * idfs.get(word); 
                tfidf.put(word, value);
            }
            resTfIdf.put(i, tfidf);
        }
        System.out.println("TF-IDF for Every file is :");
        return resTfIdf;
//        DisTfIdf(resTfIdf);
    }
    
    //print tf_idf value
    public static void DisTfIdf(HashMap<Integer, HashMap<String, Float>> tfidf){
    	for(int i = tfidf.size()-1; i< tfidf.size(); i++){
    		List<Map.Entry<String, Float>> tf_idf = new ArrayList<Map.Entry<String,Float>>(tfidf.get(i).entrySet());
    		
    		Collections.sort(tf_idf, new Comparator<Map.Entry<String, Float>>() {   
    		    public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {      
    		    	if((o2.getValue() - o1.getValue()) > 0){
    		    		return 1;
    		    	}
    		    	else
    		    		return -1;
    		    }
    		}); 
    		
    		for (int j = 0; j < 50; j++) {
    			Map.Entry entry = tf_idf.get(j); 
    			System.out.print(i+entry.getKey().toString() + " = " + entry.getValue().toString() + ", ");
    		}
    		System.out.println(" ");
    	}
    }
    
    //return keywords
    public static String[] keywords(HashMap<Integer,HashMap<String, Float>> all_tf,HashMap<String, Float> idfs){
    	HashMap<Integer, HashMap<String, Float>> tfidf = tf_idf(all_tf,idfs);
    	String[] keywords = new String[Cwr.NUM_KEYWORDS];
    	for(int i = tfidf.size()-1; i< tfidf.size(); i++){
    		List<Map.Entry<String, Float>> tf_idf = new ArrayList<Map.Entry<String,Float>>(tfidf.get(i).entrySet());
    		
    		Collections.sort(tf_idf, new Comparator<Map.Entry<String, Float>>() {   
    		    public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {      
    		    	if((o2.getValue() - o1.getValue()) > 0){
    		    		return 1;
    		    	}
    		    	else
    		    		return -1;
    		    }
    		}); 
    		
    		for (int j = 0; j < Cwr.NUM_KEYWORDS; j++) {
    			Map.Entry entry = tf_idf.get(j);
    			keywords[j] = entry.getKey().toString();
    			System.out.print(i+entry.getKey().toString() + " = " + entry.getValue().toString() + ", ");
    		}
    		System.out.println(" ");
    	}
    	return keywords;
    }
}
