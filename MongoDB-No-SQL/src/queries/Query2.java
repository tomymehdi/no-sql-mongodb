package queries;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Query2 {

	public static void main(String[] args) {

		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient("localhost", 27017);
			DB db = mongoClient.getDB("mongo");

			DBCollection nation = db.getCollection("nation");
			DBCollection supplier = db.getCollection("supplier");
			DBCollection order = db.getCollection("order");
			DBCollection line_item = db.getCollection("line_item");
			DBCollection partsupp = db.getCollection("partsupp");
			DBCollection part = db.getCollection("part");
			String type = "Type";
			String region = "Region";
			String size = "Size";
			/*
			 * 
			 * SELECT s_acctbal, s_name, n_name, p_partkey, p_mfgr, s_address, s_phone,

				s_comment
				
				FROM part, supplier, partsupp, nation, region
				
				WHERE p_partkey = ps_partkey AND s_suppkey = ps_suppkey AND p_size = [SIZE] 
				
				AND p_type like '%[TYPE]' AND s_nationkey = n_nationkey AND n_regionkey = 
				
				r_regionkey AND r_name = '[REGION]' AND ps_supplycost = (SELECT 
				
				min(ps_supplycost) FROM partsupp, supplier, nation, region WHERE p_partkey = 
				
				ps_partkey AND s_suppkey = ps_suppkey AND s_nationkey = n_nationkey AND 
				
				n_regionkey = r_regionkey AND r_name = '[REGION]') 
				
				ORDER BY s_acctbal desc, n_name, s_name, p_partkey;
			 * 
			 * */
			// Query
			// create our pipeline operations, first with the $match
			

			
			DBObject matchType = new BasicDBObject("$match", new BasicDBObject(
					"type", java.util.regex.Pattern.compile(type) ));
			
			DBObject matchRegion = new BasicDBObject("$match", new BasicDBObject(
					"nation.region.name", new BasicDBObject("$eq", region)));

			BasicDBList matchList = new BasicDBList();
			matchList.add(matchRegion);
			matchList.add(matchType);
			DBObject match = new BasicDBObject("$match", matchList);

			// build the $projection operation
			DBObject fields = new BasicDBObject("_id", 0);
			fields.put("acctbal", 1);
			fields.put("supplier.name", 1);
			fields.put("nation.name", 1);
			fields.put("part._id", 1);
			fields.put("supplier.address", 1);
			fields.put("part.mfgr", 1);
			fields.put("supplier.phone", 1);
			fields.put("supplier.comment", 1);

	        
			DBObject project = new BasicDBObject("$project", fields);

			DBObject groupByFields = new BasicDBObject( "return_flag", "$return_flag" );
			groupByFields.put( "line_status", "$line_status" );


			DBObject group = new BasicDBObject(); // EMPTY GROUP ??

			// Finally the $sort operation
			DBObject sort = new BasicDBObject("$sort", new BasicDBObject(
					"line_status", -1));
			sort.put("$sort", new BasicDBObject("return_flag", 1));
			sort.put("$sort", new BasicDBObject("nation.name", -1));
			sort.put("$sort", new BasicDBObject("supplier.name", -1));
			sort.put("$sort", new BasicDBObject("part._id", -1));
			// run aggregation
			List<DBObject> pipeline = Arrays
					.asList(match, project, group, sort);
			AggregationOutput output = line_item.aggregate(pipeline);

			for (DBObject result : output.results()) {
				System.out.println(result);
			}


		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

}
