
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;



public class XqCrawler{
	
	static final private String xueqiuweb = "http://xueqiu.com/S/";
	private static final int stocknumber = 200;
	public static ExecutorService tt;
	
	
	public static void main(String args[]){
		Set<String> sadd = GetStockRealData.GetAllStock();
		Iterator<String> iter = sadd.iterator();
		
		while(true){
			DBControl db = new DBControl("xueqiu");
			Iterator<String> itw = iter;
			int count = 0;
			while(itw.hasNext()){
				tt = Executors.newCachedThreadPool();
					for(int j=0; j<stocknumber; j++){
						if(itw.hasNext()){
							String stockinfo = itw.next();
							String code = stockinfo.substring(1, 7);
							if(code.startsWith("60")||code.startsWith("90"))
								code = "SH"+code;
							else
								code = "SZ"+code;
							tt.execute(new GetAllData(code));
						}
					}
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
		}
	}
	
}
