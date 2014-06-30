package queries;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import com.mongodb.AggregationOutput;
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

			DBCollection partsupp = db.getCollection("partsupp");
			String type = "type";
			String region = "America del sur";
			int size = 1;
			/*
			 * 
			 * SELECT s_acctbal, s_name, n_name, p_partkey, p_mfgr, s_address, s_phone,

				s_comment
				
				FROM part, supplier, partsupp, nation, region
				
				WHERE p_partkey = ps_partkey AND s_suppkey = ps_suppkey AND p_size = [SIZE] 
				
				AND p_type like '%[TYPE]' AND s_nationkey = n_nationkey AND n_regionkey = 
				
				r_regionkey AND r_name = '[REGION]' 
				
				AND ps_supplycost = 
				
					(SELECT 
				
					min(ps_supplycost) 
					
					FROM partsupp, supplier, nation, region 
					
					WHERE p_partkey = ps_partkey AND s_suppkey = ps_suppkey AND s_nationkey = n_nationkey AND 
					
					n_regionkey = r_regionkey AND r_name = '[REGION]') 
				
				ORDER BY s_acctbal desc, n_name, s_name, p_partkey;
			 * 
			 * */
			//Query
			//$match operations
			DBObject matchAux = new BasicDBObject("$match", new BasicDBObject(
					"supplier.nation.region.name", region ));
			// build the $projection operation
			DBObject fieldsAux = new BasicDBObject("supply_cost", 1);

			DBObject projectAux = new BasicDBObject("$project", fieldsAux);

			DBObject sortAux = new BasicDBObject("$sort", new BasicDBObject("supply_cost", 1));

			DBObject limit = new BasicDBObject("$limit", 1);
			// run aggregation
			List<DBObject> pipeline2 = Arrays.asList(matchAux, projectAux, sortAux, limit);
			AggregationOutput output2 = partsupp.aggregate(pipeline2);
			for (DBObject result : output2.results()) {
				System.out.println(result);
				System.out.println("The minimum Supply Cost is: " + result.get("supply_cost"));
			}
			Object minValue = output2.results().iterator().next().get("supply_cost");


			// Query
			// create our pipeline operations, first with the $match
			DBObject match = new BasicDBObject("$match", new BasicDBObject(
					"part.size", new BasicDBObject("$eq", size)))
			.append("$match", new BasicDBObject("supplier.nation.region.name", region ))
			.append("$match", new BasicDBObject("part.type", java.util.regex.Pattern.compile(type) ))
			.append("$match", new BasicDBObject("supply_cost", new BasicDBObject("$eq", minValue)));

			// build the $projection operation
			DBObject fields = new BasicDBObject("_id", 0);
			fields.put("supplier.acctbal", 1);
			fields.put("supplier.name", 1);
			fields.put("supplier.nation.name", 1);
			fields.put("part._id", 1);
			fields.put("supplier.address", 1);
			fields.put("part.mfgr", 1);
			fields.put("supplier.address", 1);
			fields.put("supplier.phone", 1);
			fields.put("supplier.comment", 1);

			DBObject project = new BasicDBObject("$project", fields);

			// Finally the $sort operation
			DBObject sort = new BasicDBObject("$sort", new BasicDBObject("supplier.acctbal", 1))
					.append("$sort", new BasicDBObject("supplier.nation.name", -1))
					.append("$sort", new BasicDBObject("supplier.name", -1))
					.append("$sort", new BasicDBObject("part._id", -1));
			// run aggregation
			List<DBObject> pipeline = Arrays
					.asList(match, project, sort);
			AggregationOutput output = partsupp.aggregate(pipeline);

			for (DBObject result : output.results()) {
				System.out.println(result);
			}


		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

}
