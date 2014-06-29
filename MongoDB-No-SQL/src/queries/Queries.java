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

public class Queries {

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

			// http://hmkcode.com/mongodb-java-simple-example/
			// http://docs.mongodb.org/ecosystem/tutorial/use-aggregation-framework-with-java-driver/

			// Query
			// create our pipeline operations, first with the $match
			DBObject match = new BasicDBObject("$match", new BasicDBObject(
					"ship_date", new BasicDBObject("$lt", new Date())));

			// build the $projection operation
			DBObject fields = new BasicDBObject("_id", 0);
			fields.put("line_status", 1);
			fields.put("return_flag", 1);
			fields.put("quantity", 1);
			fields.put("extended_price", 1);
			fields.put("tax", 1);

	        
	        /*THIS IS FOR sum(l_extendedprice*(1-l_discount)) as  sum_disc_price,*/
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
	        /* END */
	        
	        /*THIS IS FOR sum(l_extendedprice*(1-l_discount)*(1+l_tax)) as sum_charge*/
	        BasicDBList justTaxPlusOne = new BasicDBList();
	        justTaxPlusOne.add("$tax");
	        justTaxPlusOne.add(1);
	        BasicDBObject firstResult = new BasicDBObject("$add",justTaxPlusOne);
	        BasicDBList finalCalculation = new BasicDBList();
	        finalCalculation.add(firstResult);
	        finalCalculation.add(justMultiplyToExtendedPrice);
	        finalCalculation.add("$extended_price");
	        BasicDBObject finalResult2 = new BasicDBObject("$multiply",finalCalculation);
	        fields.put("charge", finalResult2);
            /* END */
	        
	        
			DBObject project = new BasicDBObject("$project", fields);

			DBObject groupByFields = new BasicDBObject( "return_flag", "$return_flag" );
			groupByFields.put( "line_status", "$line_status" );
			
			// Now the $group operation
			DBObject groupFields = new BasicDBObject( "_id", groupByFields );
			groupFields.put("count_order", new BasicDBObject("$sum", 1));
			groupFields.put("sum_qty", new BasicDBObject("$sum", "$quantity"));
			groupFields.put("sum_base_price", new BasicDBObject("$sum", "$extended_price"));
			groupFields.put("sum_disc_price", new BasicDBObject("$sum", "$discount"));
			groupFields.put("sum_charge", new BasicDBObject("$sum", "$charge"));
			groupFields.put("avg_qty", new BasicDBObject("$avg", "$quantity"));
			groupFields.put("avg_price", new BasicDBObject("$avg", "$extended_price"));
			groupFields.put("avg_disc", new BasicDBObject("$avg", "$discount"));
			DBObject group = new BasicDBObject("$group", groupFields);

			// Finally the $sort operation
			DBObject sort = new BasicDBObject("$sort", new BasicDBObject(
					"line_status", -1));
			sort.put("$sort", new BasicDBObject("return_flag", -1));
			// run aggregation
			List<DBObject> pipeline = Arrays
					.asList(match, project, group, sort);
			AggregationOutput output = line_item.aggregate(pipeline);

			for (DBObject result : output.results()) {
				System.out.println(result);
			}

			// line_item.aggregate({ $group:
			// {
			// _id: { return_flag:"$return_flag", line_status:"$line_status"},
			// sum_base_price : { $sum : "$extended_price" },
			// sum_dic_price : { $sum : {"$extended_price" * (1 - "$discount") }
			// },
			// sum_qty : {$sum : "$quantity" } } }
			//
			// }
			//
			// });

			// Query2

			// Query3

			// Query4

		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

}
