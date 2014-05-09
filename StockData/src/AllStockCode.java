import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.LinkRegexFilter;
import org.htmlparser.nodes.TagNode;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;


public class AllStockCode {
	public static final String stocklistpage = "http://guba.eastmoney.com/geguba_list.html";
	
	//get set of stock_code.
	public static Set<String> GetAllStockcode(){
		String htmlcode = PageHandle.downloadpage(stocklistpage);
		
		if(htmlcode != null){
			Parser parser = null;
			NodeList list = null;
			Set<String> s = new HashSet<String>();
			try {
				parser =Parser.createParser(htmlcode, "utf-8");
				parser.setEncoding("utf-8");
				NodeFilter frameFilter = new LinkRegexFilter("topic,(([6903]0)|(200))");
				list = parser.extractAllNodesThatMatch(frameFilter);
				System.out.println(list.size());
			}catch (ParserException e) {
				e.printStackTrace();
			}
			for (int i = 0; i < list.size(); i++) {
				TagNode tag = (TagNode) list.elementAt(i);
				String stock = tag.toPlainTextString();
			
				String regex1 = "\\(\\d{6}\\).*?";
				Pattern pattern = Pattern.compile(regex1);
				Matcher matcher = pattern.matcher(stock);
				while(matcher.find()){	
					s.add(stock);
				}
			}
			System.out.println(s.size());
			return s;
		}
		return null;
	}
}
