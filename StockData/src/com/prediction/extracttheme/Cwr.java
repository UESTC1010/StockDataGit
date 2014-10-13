package com.prediction.extracttheme;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.prediction.crawler.DBControl;
import com.prediction.semantic.WordFren;
import com.prediction.training.Tool;

// Co-occurrence word retrieval 
public class Cwr {
	public static final int NUM_KEYWORDS = 20;

	public static class CoocNode {
		private String keyword;
		private HashMap<String, Integer> coocMap;

		public CoocNode(String keyword) {
			this.keyword = keyword;
			this.coocMap = new HashMap<String, Integer>();
		}

		public void addcoocword(String word) {
			if(!word.equals(keyword)){
				if (coocMap.containsKey(word))
					coocMap.put(word, coocMap.get(word) + 1);
				else
					coocMap.put(word, 1);
			}
		}

		public HashMap<String, Integer> getcoocMap() {
			return coocMap;
		}

		public String getKeyword() {
			return keyword;
		}
		
	}

	public static void buildCooc(ArrayList<String> txtarray, CoocNode[] keynodes, String[] keywords){
		// every topic
		for (int i = 0; i < txtarray.size(); i++) {
			String[] textclause = txtarray.get(i).split("¡£");
			// every clause of a topic
			for (int j = 0; j < textclause.length; j++) {
				// word segmentation
				ArrayList<String> wordlist = WordFren
						.getWordList(textclause[j]);
				// add word to a specified word Node
				for (int k = 0; k < NUM_KEYWORDS; k++) {
					if (textclause[j].contains(keywords[k])) {
						for (String word : wordlist)
							keynodes[k].addcoocword(word);
					}
				}
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		DBControl.init("xueqiu");
		String code = "SZ002024";
		Date start = Tool.format.parse("2014-06-10 00:00:00");
		CoocNode[] keynodes = new CoocNode[NUM_KEYWORDS];
		ArrayList<String> txtarray = DBControl.GetTextArray(code, Tool.addday(
				start, 26), Tool.addday(start, 26 + 7));
		String[] keywords = Extractkeyword.getkeywords(code, start, 26, 7);

		for (int i = 0; i < keywords.length; i++) {
			keynodes[i] = new CoocNode(keywords[i]);
		}
		Cwr.buildCooc(txtarray, keynodes, keywords);
		

		for (CoocNode node : keynodes) {
			List<Map.Entry<String, Integer>> relatedwords = new ArrayList<Map.Entry<String, Integer>>(
					node.getcoocMap().entrySet());
			System.out.print(node.keyword + " related words:");

			Collections.sort(relatedwords,
					new Comparator<Map.Entry<String, Integer>>() {
						public int compare(Map.Entry<String, Integer> o1,
								Map.Entry<String, Integer> o2) {
							if (o2.getValue() > o1.getValue()) {
								return 1;
							} else
								return -1;
						}
					});
			for (int j = 0; j < 30; j++) {
				Map.Entry entry = relatedwords.get(j);
				System.out.print(entry.getKey().toString() + "("
						+ entry.getValue().toString() + ") ");
			}
			System.out.println(" ");
		}
	}
}
