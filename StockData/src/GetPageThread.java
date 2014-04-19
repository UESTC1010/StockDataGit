import java.util.Iterator;

import com.google.gson.Gson;


public class GetPageThread implements Runnable {
	String url;
	String code;
	static int x = 0;
	public GetPageThread(String url, String code) {
		super();
		this.url = url;
		this.code = code;
	}
	@Override
	public void run() {
		
		String json = PageHandle.downloadpage(url);
		if(json != null){
			Gson gson = new Gson();
			Page page = gson.fromJson(json,Page.class);
			
			if(page.list != null){
				Iterator<Topic> it = page.list.iterator();
				while(it.hasNext()){
					DBControl.SaveTopic(code, it.next());
				}
			}
			else {
				x++;
//				System.out.println(page.toString());
//				System.out.println("-------¹ÉÆ±"+page.symbol+"Ã»ÓÐlist-------"+url);
				System.out.println(x);
			}
		}
	}
	
	//main test
	public static void main(String args[]){
		
		String url = "http://xueqiu.com/statuses/search.json?count=10&comment=0&symbol=SZ000559&hl=0&source=user&page=1000";
		new Thread(new GetPageThread(url,"SZ000559")).start();
	}
	
}
