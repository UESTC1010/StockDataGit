
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;



public class XqCrawler{
//	static final private String xueqiuweb = "http://xueqiu.com/S/";
	private static final int ProxyNumber = 10;
	public static ExecutorService tt;
	public static String[] ProxyPool = new String[]{
		"127.0.0.1:8080",
		"116.112.66.102:808",
		"112.45.121.35:8123",
		"222.39.152.34:8080",
		"113.122.215.38:8088",
		"120.71.36.117:8088",
		"223.166.248.92:8088",
		"60.21.136.22:8080",
		"123.148.69.89:8088",	//
		"113.122.215.38:8088"
	};
	public static boolean[] ProxyStatus = new boolean[]{
		true,true,true,true,true,true,true,true,true,true};
	
	public static void main(String args[]){
		//所有股票代码集合
		Set<String> sadd = GetStockRealData.GetAllStock();
		Iterator<String> iter = sadd.iterator();
		
		while(true){
			//初始化mongodb
			DBControl db = new DBControl("xueqiutest");
			Iterator<String> itw = iter;
			int count = 0;
			tt = Executors.newCachedThreadPool();
			while(itw.hasNext()){
				String stockinfo = itw.next();
				String code = stockinfo.substring(1, 7);
				if(code.startsWith("60")||code.startsWith("90"))
					code = "SH"+code;
				else
					code = "SZ"+code;
				
				for(int i = 0; i<ProxyNumber+1 ; i++){
					if(i == 10){
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						i = 0;
					}
					
					if(ProxyStatus[i]){
						ProxyStatus[i] = false;
						tt.execute(new GetAllData(code,i));
						count++;
						break;
					}
				}
				System.out.println("-----------------"+count+"抓取ing------------");
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
		}
	}
	
}
