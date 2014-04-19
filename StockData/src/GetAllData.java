import java.util.concurrent.Executors;


public class GetAllData implements Runnable {
	String code = null;
	static int restrain = 1;
	public GetAllData(String code) {
		super();
		this.code = code;
	}
	@Override
	public void run() {
		int pagenum = 1;
		String url = "http://xueqiu.com/statuses/search.json?count=10&comment=0&symbol="+code+"&hl=0&source=user&page="+pagenum;
		String json = PageHandle.downloadpage(url);
		System.out.println(json);
		if(json != null){
			int[] value = PageHandle.GetMaxPageNum(json.substring(1, 60));
			int newcount = value[0];
			int maxpage = value[1];
			System.out.println(code+"讨论的个数为"+newcount);
//			System.out.println(code+"讨论的页数为"+maxpage);
			
			int oldcount = DBControl.GetCount(code);
			
			
			if(maxpage != 0 && oldcount == 0){
				for(int i=1;i<=maxpage;i++){
					url = "http://xueqiu.com/statuses/search.json?count=10&comment=0&symbol="+code+"&hl=0&source=user&page="+i;
					System.out.println("code="+code +"      page="+ i);
					if ( restrain%1000 == 0){
						try {
							Thread.sleep(1000*60);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					XqCrawler.tt.execute(new GetPageThread(url,code));
					restrain++;
				}
			}
			else{
				int x = newcount - oldcount;
				if(x != 0){
					url = "http://xueqiu.com/statuses/search.json?count="+x+"&comment=0&symbol="+code+"&hl=0&source=user&page=1";
					XqCrawler.tt.execute(new GetPageThread(url,code));
				}
			}
			DBControl.SaveCount(code, newcount);
		}
		else{
			System.out.println(url);
		}
	}
	
	//main test
	public static void main(String args[]){
		DBControl db = new DBControl();
		XqCrawler.tt =  Executors.newCachedThreadPool();
		new Thread(new GetAllData("SZ300027")).start();
	}
}
