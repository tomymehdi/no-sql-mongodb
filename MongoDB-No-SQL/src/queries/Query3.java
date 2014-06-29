package queries;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Query3 {

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
			String Segment = "Type";

			
			
			/*
			 * 
			 * SELECT l_orderkey, sum(l_extendedprice*(1-l_discount)) as revenue, 

				o_orderdate, o_shippriority
				
				FROM customer, orders, lineitem
				
				WHERE c_mktsegment = '[SEGMENT]' AND c_custkey = o_custkey AND l_orderkey = 
				
				o_orderkey AND o_orderdate < '[DATE1]' AND l_shipdate > '[DATE2]'
				
				GROUP BY l_orderkey, o_orderdate, o_shippriority
				
				ORDER BY revenue desc, o_orderdate;
			 * 
			 * */
		
			// Query
			// create our pipeline operations, first with the $match
			DBObject match = new BasicDBObject("$match", new BasicDBObject(
					"customer.mkt_segment", new BasicDBObject("$eq", Segment )));
			// build the $projection operation
			DBObject fields = new BasicDBObject("_id", 1);
			fields.put("order_date", 1);
			fields.put("ship_priority", 1);

	        // THIS IS FOR sum(l_extendedprice*(1-l_discount)) as  sum_disc_price
	        BasicDBList justDiscountNegated = new BasicDBList();
            justDiscountNegated.add("$discount");
	        justDiscountNegated.add(-1);
	        BasicDBObject toSubstract = new BasicDBObject("$multiply",justDiscountNegated);
	        BasicDBList justSubstractOneToDiscount = new BasicDBList();
            justSubstractOneToDiscount.add(toSubstract);
	        justSubstractOneToDiscount.add(1);
	        BasicDBObject justMultiplyToExtendedPrice = new BasicDBObject("$add",justSubstractOneToDiscount);
	        BasicDBList justTheResult = new BasicDBList();
	        justTheResult.add(justMultiplyToExtendedPrice);
	        justTheResult.add("$extended_price");
	        BasicDBObject finalResult = new BasicDBObject("$multiply",justTheResult);
	        fields.put("discount", finalResult);
	        // END sum_disc_price

			DBObject project = new BasicDBObject("$project", fields);

			DBObject groupByFields = new BasicDBObject( "_id", "$_id" );
			groupByFields.put( "order_date", "$order_date" );
			groupByFields.put( "ship_priority", "$ship_priority" );
			
			// Now the $group operation
			DBObject groupFields = new BasicDBObject( "_id", groupByFields );
			groupFields.put("revenue", new BasicDBObject("$sum", "$discount"));
			DBObject group = new BasicDBObject("$group", groupFields);

			// Finally the $sort operation
			DBObject sort = new BasicDBObject("$sort", new BasicDBObject(
					"revenue", 1));
			sort.put("$sort", new BasicDBObject("order_date", -1));
			// run aggregation
			List<DBObject> pipeline = Arrays
					.asList(match, project, group, sort);
			AggregationOutput output = order.aggregate(pipeline);

			for (DBObject result : output.results()) {
				System.out.println(result);
			}


		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

}
