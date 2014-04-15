
public class GetAllData implements Runnable {
	String code = null;
	public GetAllData(String code) {
		super();
		this.code = code;
	}
	@Override
	public void run() {
		int pagenum = 1;
		String url = "http://xueqiu.com/statuses/search.json?count=10&comment=0&symbol="+code+"&hl=0&source=user&page="+pagenum;
		String json = PageHandle.downloadpage(url);
		if(json != null){
			int maxpage = PageHandle.GetMaxPageNum(json.substring(1, 100));
			System.out.println(code+"股票的页数为"+maxpage);
			if(maxpage != 0){
				for(int i=1;i<=maxpage;i++){
					url = "http://xueqiu.com/statuses/search.json?count=10&comment=0&symbol="+code+"&hl=0&source=user&page="+i;
					System.out.println("code="+code +"      page="+ i);
					XqCrawler.tt.execute(new GetPageThread(url));
				}
			}
		}
	}

}
