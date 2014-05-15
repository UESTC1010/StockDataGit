package com.prediction.crawler;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.Iterator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;


public class GetAllData {
	static String prefix = "http://xueqiu.com/statuses/search.json?count=50&comment=0&symbol=";
	static String para1 = "&hl=0&source=user&page=";
	static String para2 = "&access_token=KLXqxlg6wbC9U2XdWJ0Yin&_=1399549771768";
	
	public static void getstockdata(String code) {
		int pageCount = 50;
		String url = prefix+code+para1+1+para2;
		String json = PageHandle.downloadpage(url);
		System.out.println(url);
		if(json != null){
			String s = json.substring(json.length()-73, json.length());
			System.out.println(s);
			int[] value = PageHandle.GetMaxPageNum(s);
			int newcount = value[0];
			int maxpage = value[1];
			
			System.out.println(code+"讨论的个数为"+newcount);
			System.out.println(code+"讨论的页数为"+maxpage);
			
			//get the count of data from DB
			int oldcount = DBControl.GetCount(code);
			
			if(maxpage != 0 && oldcount == 0){
				for(int i=1;i<=maxpage;i++){
					url = prefix+code+para1+i+para2;
					//get data from Internet
					getpage(url,code);
					try {
						Thread.sleep(600);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(" code " +code + "  page "+ i );
				}
			}
			else if( oldcount == newcount){
				System.out.println(code+"hava no new topic");
			}
			else{
				int x = newcount - oldcount;
				System.out.println(code+" have "+x+" new topics!");
				if( x!=0 ){
				if(x > pageCount){
					int newDataPage = x/50;
					
					for( int i = 1; i<=newDataPage; i++){
						url = prefix+code+para1+pageCount+para2;
						getpage(url,code);
						try {
							Thread.sleep(600);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				int remainder = x%50;
				url = prefix+code+para1+remainder+para2;
				getpage(url,code);
				}
			}
			
			//save new topic count to DB 
			DBControl.SaveCount(code, newcount);
		}
		else{
			System.out.println(url);
		}
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	//get data from Internet
	public static void getpage(String url,String code){
		String json = PageHandle.downloadpage(url);
		if(json != null){
			//exchange json to object.
			Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter()).create();
			Page page = gson.fromJson(json,Page.class);
			
			if(page.list != null){
				Iterator<Topic> it = page.list.iterator();
				while(it.hasNext()){
					DBControl.SaveTopic(code, it.next());
				}
			}
			else {
				System.out.println(url+" list is null");
			}
		}
		else{
			System.out.println("请求过于频繁！");
			System.exit(0);
		}
	}
	
	public static class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date>{

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
		GetAllData.getstockdata("SH900908");
	}
}
