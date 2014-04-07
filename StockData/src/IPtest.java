import java.util.Iterator;
import java.util.Set;


public class IPtest implements Runnable {
	Iterator<String> iter = null;
	static final private String xueqiuweb = "http://xueqiu.com/S/";
	public IPtest(Iterator<String> iter) {
		super();
		this.iter = iter;
	}

	@Override
	public void run() {
		while(true){
			Iterator<String> itw = iter;
		int count = 0;
		while(itw.hasNext()){
			String stockinfo = itw.next();
			String code = stockinfo.substring(1, 7);
			if(code.startsWith("60")||code.startsWith("90"))
				code = "SH"+code;
			else
				code = "SZ"+code;
			String xqstock = xueqiuweb + code; 
			if( PageHandle.downloadpage(xqstock) == null){
				System.out.println("返回内容为空");
				System.out.println(xqstock);
			}
			count++;
			
			//System.out.println(count);
			if (count%50 == 0){
				System.out.println("第"+count/50+"个50页");
			}
		}}
	}
	public static void main(String args[]){
		Set<String> sadd = GetStockRealData.GetAllStock();
		Iterator<String> iter = sadd.iterator();
		
	    new Thread(new IPtest(iter)).start();
	}
}
