package com.prediction.crawler;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import com.google.gson.Gson;

public class GetStockRealData implements Runnable{
	
	public static final String  SOHU_FINANCE_URL  = "http://q.stock.sohu.com/hisHq?";
	public static final String  stopstockpath  = "D:/stock_realdata/stopstock.txt";
	public static final String  suspendstockpath = "D:/stock_realdata/suspendstock.txt";
	public static DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
	String code = null;
	
	@Override
	public void run() {
//		
//		Set<String> s = AllStockCode.GetAllStockcode();	
//		Iterator<String> it = s.iterator();
//		
//		while(it.hasNext()){
//			String stockinfo = it.next();
//			String code = stockinfo.substring(1, 7);  
//			System.out.println(code);
			GetStockRealData.getStockData(code, "20130101", "20140611");
//		}
		
//		while(true){
//			System.out.println("----------------开始爬取今天数据------------------");
//			Date date = new Date();
//			if (date.getDay() != 0 &&date.getDay() != 6){
//				SimpleDateFormat timedf = new SimpleDateFormat("yyyyMMdd");//设置日期格式
//				String todayDate = timedf.format(new Date());
////		     	System.out.println(todayDate);
//				
//				Set<String> sadd = AllStockCode.GetAllStockcode();
//				Iterator<String> iter = sadd.iterator();
//				
//				while(iter.hasNext()){
//					String stockinfo = iter.next();
//					String code = stockinfo.substring(1, 7);  
//					GetStockRealData.getStockData(code,todayDate,todayDate);
//				}
//			  
//			}
//			try {
//				System.out.println("----------------当天真实数据爬取完成------------------");
//				Thread.sleep(24*60*60*1000-60*1000);
//				GetStockRealData.DeleteFolder(suspendstockpath);
//				GetStockRealData.DeleteFolder(stopstockpath);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	public GetStockRealData(String code) {
		this.code = code;
	}

	public static void getStockData(String code, String fromDate,String toDate) {
	 	Stockdata data = GetDataFromJson(code, fromDate, toDate);
	 	if(data==null)
	 		writestopstock(suspendstockpath, code);
	 	else{
	 		String[][] hisdata= data.getHq();
        if(hisdata==null)
        	writestopstock(stopstockpath, code);
        else{
//        	System.out.println(hisdata.length);
        	for(int i = 0;i<hisdata.length;i++){
        		StockData stockdata = new StockData();
        		
        		try {
					stockdata.setTime(dateformat.parse(hisdata[i][0]));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
        		stockdata.setKaipan(hisdata[i][1]);
        		stockdata.setShoupan(hisdata[i][2]);
        		stockdata.setZhengfu(hisdata[i][3]);
        		stockdata.setZhangfu(hisdata[i][4]);
        		stockdata.setZuidi(hisdata[i][5]);
        		stockdata.setZuigao(hisdata[i][6]);
        		stockdata.setZongshou(hisdata[i][7]);
        		stockdata.setZongjin(hisdata[i][8]);
        		stockdata.setHuanshou(hisdata[i][9]);
        		DBControl.SaveStockData(code,stockdata);
        	} 
        	}
	 	}
	}
	
	private static Stockdata GetDataFromJson(String code, String fromDate,
			String toDate) {
		 	String url = SOHU_FINANCE_URL + "code=cn_" + code + "&start=" + fromDate + "&end=" + toDate + "&order=A&period=d&rt=json";
//	        System.out.println(url);
	        URL MyURL = null;
	        URLConnection con = null;
	        String json="";
	        try {
				MyURL = new URL(url);
				con = MyURL.openConnection();
				 BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
		         String line = null;
		         while ((line = reader.readLine()) != null)
		         {
		        	 json = json+line;
		         }
		         reader.close();
		         json = json.substring(1, json.length()-1);
//				 System.out.println(json);
			        
			} catch (IOException es) {
				es.printStackTrace();
			} 
			
			Gson gson = new Gson();
			Stockdata data = gson.fromJson(json,Stockdata.class);
//			System.out.println(data.getCode()+"    "+data.getStatus() );
			return data;
	}
	
	public static void writestopstock(String pathname, String code){
		File log = new File(pathname);
		try {
			FileWriter fileWriter=new FileWriter(log,true);
			fileWriter.write(code+"\r\n");
			fileWriter.flush();
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean DeleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		
		if (!file.exists()) { 
			return flag;
		} else {
			return file.delete();
		}
	}

	 
	public class Stockdata{
		String status;
		String[][] hq;
		String code;
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public String[][] getHq() {
			return hq;
		}
		public void setHq(String[][] hq) {
			this.hq = hq;
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
	}
	
	public  static void main(String[] args) {
		DBControl.init("xueqiu");
		new Thread(new GetStockRealData("000001")).start();
	}
}
