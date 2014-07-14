package com.prediction.crawler;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.prediction.semantic.AnalysizeText;


public class DBControl {
	static Mongo mongo = null;
	static Morphia morphia = null;
	static Datastore ds = null;
	public static DB db = null;
	public static void init(String dbname) {
		try {
			mongo = new Mongo("192.168.1.109",27017);
//			mongo = new Mongo();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		db = mongo.getDB(dbname);
		morphia = new Morphia();
		morphia.map(Topic.class);
		morphia.map(StockData.class);	
	}
	
	public void close(){
		mongo.close();
	}
	public static void SaveCount(String code, int count){
		DBObject oldcount = new BasicDBObject();
		DBObject newcount = new BasicDBObject();
		oldcount.put("symbol", code);
		newcount.put("symbol", code);
		newcount.put("count", count);
		db.getCollection(code).update(oldcount, newcount,true,false);
	}
	public static int GetCount(String code){
		DBObject object = new BasicDBObject();
		object.put("symbol", code);
		DBObject stock = db.getCollection(code).findOne(object);
		if(stock != null)
			return (Integer)stock.get("count");
		else 
			return 0;
	}
	public static void SaveTopic(String code, Topic topic){
		DBObject DbObj = morphia.toDBObject(topic);
		db.getCollection(code).save(DbObj);
	}


	public static void SaveStockData(String code, StockData stockdata) {
		if(code.startsWith("60")||code.startsWith("90"))
			code = "SH"+code;
		else
			code = "SZ"+code;
		DBObject DbObj = morphia.toDBObject(stockdata);
		db.getCollection(code).save(DbObj);
	}
	
	public static String GetText(String code, Date start, Date end) {
		String alltext = "";
		BasicDBObject keys = new BasicDBObject();
		keys.put("created_at", new BasicDBObject("$gte", start).append("$lte", end));
		DBCursor cursor = db.getCollection(code).find(keys);
		long x = db.getCollection(code).count(keys);		

		while(cursor.hasNext()){
				String text = DeleteNoise(String.valueOf(cursor.next().get("text")));
				alltext += text;
		}
		System.out.println(x);
		return alltext;
	}

	private static String DeleteNoise(String text) {
		String noHtmlContent = text.replaceAll("<[^>]*>","").replaceAll("&nbsp", "");
		return noHtmlContent;
	}

	public static Double GetZhangfu(String code, Date start) {
		BasicDBObject keys = new BasicDBObject();
		keys.put("_id", start);
		DBObject DBobj = db.getCollection(code).findOne(keys);
		if(DBobj != null){
			double zhengfu = Double.valueOf(((String)DBobj.get("zhangfu")).replaceAll("%", ""));
//			if(zhengfu<-0.3)
//				return -1;
//			else if(zhengfu>0.3)
//				return 1;
//			else
//				return 0;
			return zhengfu;
		}
		else
			return null;
	}

	public static double[] GetFeatureFromDB(String code, Date start) {
		double[] feature = new double[7];
		
		BasicDBObject keys = new BasicDBObject();
		keys.put("_id", start);
		DBObject data = db.getCollection(code).findOne(keys);
		if(data != null){
			feature[0] = Double.valueOf((String) data.get("kaipan"));
			feature[1] = Double.valueOf((String) data.get("shoupan"));
			feature[2] = Double.valueOf(((String) data.get("zhangfu")).replace("%", ""));
			feature[3] = Double.valueOf((String) data.get("zongshou"));
			feature[4] = Double.valueOf((String) data.get("zhengfu"));
			feature[5] = Double.valueOf(((String) data.get("huanshou")).replace("%", ""));
			feature[6] = AnalysizeText.getSentiment(code, start, 1).get(0);
			return feature;
		}
		else
			return null;
	}

	public static ArrayList<String> GetTextArray(String code, Date start, Date end) {
		ArrayList<String> arraytext = new ArrayList<String>();
		BasicDBObject keys = new BasicDBObject();
		keys.put("created_at", new BasicDBObject("$gte", start).append("$lte", end));
		DBCursor cursor = db.getCollection(code).find(keys);
		long x = db.getCollection(code).count(keys);		

		while(cursor.hasNext()){
				String text = DeleteNoise(String.valueOf(cursor.next().get("text")));
				if(text.length()>50)
					arraytext.add(text);
		}
		System.out.println(x);
		return arraytext;
	}
}
