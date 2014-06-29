package queries;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;


public class Queries {
	
	public static void main(String[] args) {

		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "no-sql" );
			
			DBCollection nation = db.getCollection("nation");
			DBCollection supplier = db.getCollection("supplier");
			DBCollection order = db.getCollection("order");
			DBCollection line_item = db.getCollection("line_item");
			DBCollection partsupp = db.getCollection("partsupp");
			DBCollection part = db.getCollection("part");
			
			
			//http://hmkcode.com/mongodb-java-simple-example/
			//http://docs.mongodb.org/ecosystem/tutorial/use-aggregation-framework-with-java-driver/
			
			//Query
			// create our pipeline operations, first with the $match
			DBObject match = new BasicDBObject("$match", new BasicDBObject("ship_date", new BasicDBObject("$lt", new Date())));

			// build the $projection operation
			DBObject fields = new BasicDBObject("_id", 0);
			fields.put("line_status", 1);
			fields.put("return_flag", 1);
			fields.put("quantity", 1);
			//fields.put("sum_qty", 1);
			//fields.put("sum_base_price", new BasicDBObject("$sum", "extended_price"));
			//fields.put("sum_disc_price", new BasicDBObject("$sum", "discount"));
			//fields.put("avg_price", new BasicDBObject("$avg","extended_price"));
			DBObject project = new BasicDBObject("$project", fields );

			// Now the $group operation
			DBObject groupFields = new BasicDBObject("_id", "$line_status");
			groupFields.put("_id","$return_flag");
			groupFields.put("sum_qty", new BasicDBObject("$sum", "$quantity"));
			DBObject group = new BasicDBObject("$group", groupFields);

			// Finally the $sort operation
			DBObject sort = new BasicDBObject("$sort", new BasicDBObject( "line_status", -1));
			sort.put("$sort", new BasicDBObject("return_flag", -1));
			// run aggregation
			List<DBObject> pipeline = Arrays.asList(match, project, group, sort);
			AggregationOutput output = line_item.aggregate(pipeline);
			
			
			
			for (DBObject result : output.results()) {
			    System.out.println(result);
			}
			
//			line_item.aggregate({ $group:
//									{
//										_id: { return_flag:"$return_flag", line_status:"$line_status"},
//										sum_base_price : { $sum : "$extended_price" },
//										sum_dic_price : { $sum : {"$extended_price" * (1 - "$discount") } },
//										sum_qty : {$sum : "$quantity" } } }
//										
//									}
//									
//								});
			
			
			//Query2
			
			//Query3
			
			//Query4
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

	}

}
