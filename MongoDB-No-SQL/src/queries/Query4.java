package queries;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.bson.NewBSONDecoder;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class Query4 {

	public static void main(String[] args) {

		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient("localhost", 27017);
			DB db = mongoClient.getDB("mongo");

			DBCollection line_item = db.getCollection("line_item");
			String region = "Region";
			/*
			 * SELECT n_name, sum(l_extendedprice * (1 - l_discount)) as revenue
			 * 
			 * FROM customer, orders, lineitem, supplier, nation, region
			 * 
			 * WHERE c_custkey = o_custkey AND l_orderkey = o_orderkey AND
			 * l_suppkey =
			 * 
			 * s_suppkey AND c_nationkey = s_nationkey AND s_nationkey =
			 * n_nationkey AND
			 * 
			 * n_regionkey = r_regionkey AND r_name = '[REGION]' AND o_orderdate
			 * >= date
			 * 
			 * '[DATE]' AND o_orderdate < date '[DATE]' + interval '1' year
			 * 
			 * GROUP BY n_name
			 * 
			 * ORDER BY revenue desc;
			 */

			// Query
			// create our pipeline operations, first with the $match
			DBObject matchRegion = new BasicDBObject("$match",
					new BasicDBObject("nation.region.name", new BasicDBObject(
							"$eq", region)));

			DBObject matchOrderDate = new BasicDBObject("$match",
					new BasicDBObject("order_date", new BasicDBObject("$gt",
							new Date()).append("$lt", new Date()))); // + 1
																		// YEAR
																		// TODO
																		// !!
			BasicDBList matchList = new BasicDBList();
			matchList.add(matchRegion);
			matchList.add(matchOrderDate);
			DBObject match = new BasicDBObject("$match", matchList);

			// build the $projection operation
			DBObject fields = new BasicDBObject("_id", 0);
			fields.put("nation.name", 1);
			fields.put("extended_price", 1);

			// THIS IS FOR sum(l_extendedprice*(1-l_discount)) as sum_disc_price
			BasicDBList justDiscountNegated = new BasicDBList();
			justDiscountNegated.add("$discount");
			justDiscountNegated.add(-1);
			BasicDBObject toSubstract = new BasicDBObject("$multiply",
					justDiscountNegated);
			BasicDBList justSubstractOneToDiscount = new BasicDBList();
			justSubstractOneToDiscount.add(toSubstract);
			justSubstractOneToDiscount.add(1);
			BasicDBObject justMultiplyToExtendedPrice = new BasicDBObject(
					"$add", justSubstractOneToDiscount);
			BasicDBList justTheResult = new BasicDBList();
			justTheResult.add(justMultiplyToExtendedPrice);
			justTheResult.add("$extended_price");
			BasicDBObject finalResult = new BasicDBObject("$multiply",
					justTheResult);
			fields.put("discount", finalResult);
			// END sum_disc_price

			DBObject project = new BasicDBObject("$project", fields);

			DBObject groupByFields = new BasicDBObject("nation.name",
					"$nation.name");

			// Now the $group operation
			DBObject groupFields = new BasicDBObject("_id", groupByFields);
			groupFields.put("revenue", new BasicDBObject("$sum", "$discount"));
			DBObject group = new BasicDBObject("$group", groupFields);

			// Finally the $sort operation
			DBObject sort = new BasicDBObject("$sort", new BasicDBObject(
					"revenue", 1));
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
