package com.prediction.semantic;

import java.util.ArrayList;
import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.prediction.crawler.DBControl;

public class TopicofOneday {
	public static ArrayList<String> getAllTopics(String code,Date startdate, Date enddate){
		ArrayList<String> strlist = new ArrayList<String>();
		BasicDBObject keys = new BasicDBObject();
		keys.put("created_at", new BasicDBObject("$gte", startdate).append("$lte", enddate));
		DBCursor cursor = DBControl.db.getCollection(code).find(keys);
		long x = DBControl.db.getCollection(code).count(keys);		

		while(cursor.hasNext()){
			//clear the topic.	
			String text = DeleteNoise(String.valueOf(cursor.next().get("text")));
			//save one topic to list.
			strlist.add( new String(text) );
		}
		//System.out.println(x);
		return strlist;
	}
	private static String DeleteNoise(String text) {
		//delete some link.
		String noHtmlContent = text.replaceAll("<[^>]*>","").replaceAll("&nbsp", "").replaceAll("\\$.*\\$", "");
		//do more about noise.
		//
		return noHtmlContent;
	}
}
