package com.prediction.semantic;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.wltea.analyzer.cfg.Configuration;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;


public class WordFren {
	public static Map getTextDef(String text) throws IOException {
        Map<String, Integer> wordsFren=new HashMap<String, Integer>();
        IKSegmenter ikSegmenter = new IKSegmenter(new StringReader(text), true);
        Lexeme lexeme;
        while ((lexeme = ikSegmenter.next()) != null) {
            if(lexeme.getLexemeText().length()>0){
                if(wordsFren.containsKey(lexeme.getLexemeText())){
                    wordsFren.put(lexeme.getLexemeText(),wordsFren.get(lexeme.getLexemeText())+1);
                }else {
                    wordsFren.put(lexeme.getLexemeText(),1);
                }
            }
        }
        return wordsFren;
    }
	
	public static HashMap<String,Integer[]> getWordMap(String text) {
		HashMap<String,Integer[]> wordMap=new HashMap<String,Integer[]>();
        IKSegmenter ikSegmenter = new IKSegmenter(new StringReader(text), true);
        Lexeme lexeme;
        int i = 1;
        try {
			while ((lexeme = ikSegmenter.next()) != null) {
				wordMap.put(lexeme.getLexemeText(),new Integer[]{lexeme.getBeginPosition(),i++});
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wordMap;
    }
	
	
	/**
	 * @param args
	 */
	public static void sortSegmentResult(Map<String,Integer> wordsFrenMaps,int topWordsCount){
        
        List<Map.Entry<String, Integer>> wordFrenList = new ArrayList<Map.Entry<String, Integer>>(wordsFrenMaps.entrySet());
        Collections.sort(wordFrenList, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> obj1, Map.Entry<String, Integer> obj2) {
                return obj2.getValue() - obj1.getValue();
            }
        });
        System.out.println("排序后:================");
        for(int i=0;i<topWordsCount&&i<wordFrenList.size();i++){
            Map.Entry<String,Integer> wordFrenEntry=wordFrenList.get(i);
            if(wordFrenEntry.getValue()>1){
                System.out.println(wordFrenEntry.getKey()+"             的次数为"+wordFrenEntry.getValue());
            }
        }
    }
	public static int findUniqueWord(Map<String,Integer> wordsFrenMaps,String word){
		if( wordsFrenMaps.containsKey(word) ){
			return wordsFrenMaps.get(word);
		}
		else 
			return 0;
	}
	
	
	public static void main(String[] args) {
		ArrayList<String> array = WordFren.getWordList("简单说说今天参加苏宁众包大会的观感吧");
		for(String txt:array){
			System.out.println(txt);
		}
	}

	public static ArrayList<String> getWordList(String string) {
		ArrayList<String> wordlist=new ArrayList<String>();
        IKSegmenter ikSegmenter = new IKSegmenter(new StringReader(string), true);
        Lexeme lexeme;
        try {
			while ((lexeme = ikSegmenter.next()) != null) {
				wordlist.add(lexeme.getLexemeText());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
//		for (String x:wordlist){
//			System.out.println(x);
//		}
		return wordlist;
	}
	
}
