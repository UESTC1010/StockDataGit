import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
//		DBControl db = new DBControl();
//		String text =  DBControl.GetText("SZ300027");
//		try {
//			int topWordsCount=400;
//			Map wordsFrenMaps = WordFren.getTextDef(text);
//			WordFren.sortSegmentResult(wordsFrenMaps, topWordsCount);
//			System.out.println(WordFren.findUniqueWord(wordsFrenMaps, "涨"));
//			System.out.println(WordFren.findUniqueWord(wordsFrenMaps, "跌"));
////			System.out.println(WordFren.findUniqueWord(wordsFrenMaps, ""));
//			System.out.println(WordFren.findUniqueWord(wordsFrenMaps, "有望"));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}
	
}
