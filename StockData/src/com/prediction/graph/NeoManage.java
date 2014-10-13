package com.prediction.graph;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.unsafe.batchinsert.BatchInserter;
import org.neo4j.unsafe.batchinsert.BatchInserters;

import com.prediction.crawler.DBControl;
import com.prediction.extracttheme.Cwr;
import com.prediction.extracttheme.Extractkeyword;
import com.prediction.extracttheme.Cwr.CoocNode;
import com.prediction.training.Tool;

public class NeoManage {
	private static GraphDatabaseService graphDb;
	private static Node firstNode;
	private static Node secondNode;
	private static Relationship relationship;

	private static enum RelTypes implements RelationshipType {
		COOCCURRENCE
	}

	public enum MyLabels implements Label {
		KEYWORD, COWORD;
	}

	private static void init(String DB_path) {
		graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_path);
	}

	public static void bulidKeyWord(CoocNode[] keynodes, String[] keywords) {
		for (int i = 0; i < keywords.length; i++) {
			keynodes[i] = new CoocNode(keywords[i]);
		}
	}

	public static void main(String[] args) throws ParseException, IOException {
		DBControl.init("xueqiu");
		// NeoManage.init("SZ002024");
		String code = "SZ002024";
		Date start = Tool.format.parse("2014-06-10 00:00:00");
		CoocNode[] keynodes = new CoocNode[Cwr.NUM_KEYWORDS];
		ArrayList<String> txtarray = DBControl.GetTextArray(code, Tool.addday(
				start, 26), Tool.addday(start, 26 + 7));
		String[] keywords = Extractkeyword.getkeywords(code, start, 26, 7);

		NeoManage.bulidKeyWord(keynodes, keywords);
		Cwr.buildCooc(txtarray, keynodes, keywords);

		for (CoocNode node : keynodes) {
			Set<Entry<String, Integer>> set = node.getcoocMap().entrySet();
			Iterator<Entry<String, Integer>> it = set.iterator();

			BatchInserter inserter = BatchInserters.inserter("SZ002024");
			
			//add keyword node
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put("name", node.getKeyword());
			long keyNode = inserter.createNode(properties, MyLabels.KEYWORD);
			

			while (it.hasNext()) {
				Entry<String, Integer> pair = it.next();
				properties.put("name", pair.getKey());
				long normalNode = inserter.createNode(properties,
						MyLabels.COWORD);
				Map<String, Object> roleproperties = new HashMap<String, Object>();
				roleproperties.put("fren", pair.getValue());
				inserter.createRelationship(normalNode, keyNode,
						RelTypes.COOCCURRENCE, roleproperties);
			}
			inserter.shutdown();
		}
	}

}
