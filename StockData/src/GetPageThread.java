import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import com.google.gson.Gson;


public class GetPageThread implements Runnable {
	String url;
	public GetPageThread(String url) {
		super();
		this.url = url;
	}
	@Override
	public void run() {
		 URL MyURL = null;
	        HttpURLConnection  con = null;
	        String json="";
	        try {
				MyURL = new URL(url);
				con = (HttpURLConnection) MyURL.openConnection();
				if( con.getResponseCode()==200 ){
				
				 BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));
		         String line = null;
		         while ((line = reader.readLine()) != null)
		         {
		        	 json = json+line;
//		        	 System.out.println(line);
		         }
		         reader.close();
		         
		         Gson gson = new Gson();
					Page page = gson.fromJson(json,Page.class);
//					System.out.println(page.toString());
					Iterator it = page.list.iterator();
					int i=0;
					while(it.hasNext()){
//						System.out.println(it.next().toString());
						it.next();
						i++;
					}
//					System.out.println(page.symbol+" maxpage :   "+page.maxPage);
				
				}
				else {
					System.out.println("÷’÷π…œ –£∫"+url);
					
				}
			} catch (IOException es) {
				es.printStackTrace();
			} 
			
	}

}
