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

public class Query1 {

	public static void main(String[] args) {

		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient("localhost", 27017);
			DB db = mongoClient.getDB("mongo");

			DBCollection line_item = db.getCollection("line_item");

			// Query 1
			/*
			 * 
			 * 
			 * SELECT l_returnflag, l_linestatus, sum(l_quantity) as sum_qty,

				sum(l_extendedprice) as sum_base_price, sum(l_extendedprice*(1-l_discount)) as 
				
				sum_disc_price, sum(l_extendedprice*(1-l_discount)*(1+l_tax)) as sum_charge,
				
				avg(l_quantity) as avg_qty, avg(l_extendedprice) as avg_price, avg(l_discount) 
				
				as avg_disc, count(*) as count_order
				
				FROM lineitem
				
				WHERE l_shipdate <= '[date]' 
				
				GROUP BY l_returnflag, l_linestatus
				
				ORDER BY l_returnflag, l_linestatus;
			 * 
			 * */
			// Create our pipeline operations 
			// Build the $match operation
			DBObject match = new BasicDBObject("$match", 
					new BasicDBObject("ship_date", new BasicDBObject("$lt", new Date())));

			// Build the $projection operation
			BasicDBObject fields = new BasicDBObject("_id", 0);
			fields.append("line_status", 1);
			fields.append("return_flag", 1);
			fields.append("quantity", 1);
			fields.append("extended_price", 1);
			fields.append("tax", 1);

	        
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
	        fields.append("discount", finalResult);
	        // END sum_disc_price
	        
	        // THIS IS FOR sum(l_extendedprice*(1-l_discount)*(1+l_tax)) as sum_charge
	        BasicDBList justTaxPlusOne = new BasicDBList();
	        justTaxPlusOne.add("$tax");
	        justTaxPlusOne.add(1);
	        BasicDBObject firstResult = new BasicDBObject("$add",justTaxPlusOne);
	        BasicDBList finalCalculation = new BasicDBList();
	        finalCalculation.add(firstResult);
	        finalCalculation.add(justMultiplyToExtendedPrice);
	        finalCalculation.add("$extended_price");
	        BasicDBObject finalResult2 = new BasicDBObject("$multiply",finalCalculation);
	        fields.append("charge", finalResult2);
            // END sum_charge
	        
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

			// Build the $projection operation for rename
			BasicDBObject renameBasic = new BasicDBObject("_id", 0);
			renameBasic.append("grouped_by", "$_id");
			renameBasic.append("count_order", 1);
			renameBasic.append("sum_qty", 1);
			renameBasic.append("sum_base_price", 1);
			renameBasic.append("sum_disc_price", 1);
			renameBasic.append("sum_charge", 1);
			renameBasic.append("avg_qty", 1);
			renameBasic.append("avg_price", 1);
			renameBasic.append("avg_disc", 1);
			
			DBObject rename = new BasicDBObject("$project", renameBasic);
			
			// Build the $sort operation
			DBObject sortByFields = new BasicDBObject("grouped_by",1);
			
			DBObject sort = new BasicDBObject("$sort", sortByFields);
			
			// Run aggregation
			List<DBObject> pipeline = Arrays
					.asList(match, project, group, rename, sort);
			AggregationOutput output = line_item.aggregate(pipeline);
			
			//Show results
			for (DBObject result : output.results()) {
				System.out.println(result);
			}

		
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

}
