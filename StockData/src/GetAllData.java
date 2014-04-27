import java.lang.reflect.Type;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


public class GetAllData implements Runnable {
	String code = null;
	static int restrain = 1;
	static int x = 0;
	public GetAllData(String code) {
		super();
		this.code = code;
	}
	@Override
	public void run() {
		int pagenum = 1;
		String url = "http://xueqiu.com/statuses/search.json?count=50&comment=0&symbol="+code+"&hl=0&source=user&page="+pagenum+"&access_token=CSr4RehgUt3wohdtqUTp9E";
		String json = PageHandle.downloadpage(url);
//		System.out.println(json);
		if(json != null){
			String s = json.substring(json.length()-80, json.length());
			System.out.println(s);
			int[] value = PageHandle.GetMaxPageNum(s);
//			int[] value = PageHandle.GetMaxPageNum(json.substring(1, 60));
			int newcount = value[0];
			int maxpage = value[1];
			System.out.println(code+"讨论的个数为"+newcount);
			System.out.println(code+"讨论的页数为"+maxpage);
			
			int oldcount = DBControl.GetCount(code);
			
			if(maxpage != 0 && oldcount == 0){
				for(int i=1;i<=maxpage;i++){
					url = "http://xueqiu.com/statuses/search.json?count=50&comment=0&symbol="+code+"&hl=0&source=user&page="+i+"&access_token=CSr4RehgUt3wohdtqUTp9E";
					System.out.println("code="+code +"      page="+ i);
//					if ( restrain%1000 == 0){
//						try {
//							Thread.sleep(1000*60);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
					getpage(url);
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
//					restrain++;
				}
			}
			else{
				int x = newcount - oldcount;
				if(x != 0){
					url = "http://xueqiu.com/statuses/search.json?count="+x+"&comment=0&symbol="+code+"&hl=0&source=user&page=1"+"&access_token=CSr4RehgUt3wohdtqUTp9E";
					System.out.println(url);
//					XqCrawler.tt.execute(new GetPageThread(url,code));
				}
			}
			DBControl.SaveCount(code, newcount);
		}
		else{
			System.out.println(url);
		}
	}
	public void getpage(String url){
		String json = PageHandle.downloadpage(url);
		if(json != null){
			Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter()).create();
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
//				System.out.println("-------股票"+page.symbol+"没有list-------"+url);
				System.out.println(x);
			}
		}
	}
	
	public class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date>{

		@Override
		public JsonElement serialize(Date arg0, Type arg1,
				JsonSerializationContext arg2) {
			return new JsonPrimitive(String.valueOf(arg0.getTime()));
		}

		@Override
		public Date deserialize(JsonElement arg0, Type arg1,
				JsonDeserializationContext arg2) throws JsonParseException {
			return new Date(Long.valueOf(arg0.getAsString()));
		}  
		
	}
	//main test
	public static void main(String args[]){
		DBControl db = new DBControl();
		
		Date start = new Date(100+14, 3, 24, 0, 0);
		Date end = new Date(100+14, 3, 27, 0, 0);
		System.out.println(start);
		System.out.println(end);
		db.GetText("SZ300027", start, end);
//		new Thread(new GetAllData("SZ300104")).start();
//		GetAllData aa = new GetAllData("SZ000333");
//		aa.getpage("http://xueqiu.com/statuses/search.json?count=1&comment=0&symbol=SZ000333&hl=0&source=user&page=1&access_token=CSr4RehgUt3wohdtqUTp9E");
	}
}
