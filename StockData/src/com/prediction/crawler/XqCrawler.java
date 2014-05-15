package com.prediction.crawler;

import java.util.Iterator;
import java.util.Set;

public class XqCrawler{
	public static void main(String args[]){
		//initialize mongoDB
		DBControl.init("xueqiu");
//		new Thread(new GetStockRealData()).start();
		
		while(true){
			//set of all stock_code
			Set<String> stockcode = AllStockCode.GetAllStockcode();
			Iterator<String> itw = stockcode.iterator();
			int count = 0;
			while(itw.hasNext()){	
				String stockinfo = itw.next();
				String code = stockinfo.substring(1, 7);
				if(code.startsWith("60")||code.startsWith("90"))
					code = "SH"+code;
				else
					code = "SZ"+code;
				
				if(count > 140){
					//crawling
					GetAllData.getstockdata(code);
					System.out.println("------"+code+" had been crawled--------");
				}
				System.out.println(++count+" stocks had been crawled!");
			}
		}
	}
	
}
