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
		
		String json = PageHandle.downloadpage(url,"127.0.0.1;80");
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
//				System.out.println("-------¹ÉÆ±"+page.symbol+"Ã»ÓÐlist-------"+url);
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
		
		String url = "http://xueqiu.com/statuses/search.json?count=10&comment=0&symbol=SZ000559&hl=0&source=user&page=1000";
		new Thread(new GetPageThread(url,"SZ000559")).start();
	}
	
}
