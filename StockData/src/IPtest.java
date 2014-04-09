import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;


import com.google.gson.Gson;


public class IPtest implements Runnable {
	String code = null;
	static final private String xueqiuweb = "http://xueqiu.com/S/";
	private static final int stocknumber = 50;
	public IPtest(String code) {
		super();
		this.code = code;
	}

	public IPtest() {
		super();
	}

	@Override
	public void run() {
		int pagenum = 1;
		String url = "http://xueqiu.com/statuses/search.json?count=15&comment=0&symbol="+code+"&hl=0&source=user&page="+pagenum;
		int maxpage = GetDataFromJson(url);
		System.out.println(code+"股票的页数为"+maxpage);
		if(maxpage != -1){
		for(int i=2;i<=maxpage;i++){
			url = "http://xueqiu.com/statuses/search.json?count=15&comment=0&symbol="+code+"&hl=0&source=user&page="+i;
			System.out.println("code="+code +"      page="+ i);
			GetDataFromJson(url);
		}}
	}
	
	public static void main(String args[]){
		Set<String> sadd = GetStockRealData.GetAllStock();
		Iterator<String> iter = sadd.iterator();
		while(true){
			Iterator<String> itw = iter;
			int count = 0;
			while(itw.hasNext()){
				ExecutorService tt = Executors.newCachedThreadPool();
					for(int j=0; j<stocknumber; j++){
						if(itw.hasNext()){
							String stockinfo = itw.next();
							String code = stockinfo.substring(1, 7);
							if(code.startsWith("60")||code.startsWith("90"))
								code = "SH"+code;
							else
								code = "SZ"+code;
							tt.execute(new IPtest(code));
						}
						
					}
					
						count+=50;
					
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					while( ((ThreadPoolExecutor) tt).getActiveCount() > 0){ 
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					System.out.println(((ThreadPoolExecutor) tt).getActiveCount());
					System.out.println(((ThreadPoolExecutor) tt).getActiveCount());
					tt.shutdown();
					System.out.println("-----------------"+count+"个抓取完成");
				}
				
//				count++;
//				
//				if (count%50 == 0){
//					System.out.println("第"+count/50+"个50页");
//				}
			
		}
	   
	}
	private int GetDataFromJson(String url) {
		 	
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
					return page.maxPage;
				}
				else {
					System.out.println("终止上市："+url);
					
				}
			} catch (IOException es) {
				es.printStackTrace();
			} 
			
			return -1;
	}
}
