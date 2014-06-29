package insertions;

import java.net.UnknownHostException;
import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class Insertions {
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

			// Insertion examples to nation
			BasicDBObject regionObject = new BasicDBObject("name",
					"America del sur");
			BasicDBObject nationObject = new BasicDBObject("name", "Argentina")
					.append("comment", "hola").append("region", regionObject);
			nation.insert(nationObject);

			// Insertion example supplier
			BasicDBObject supplierObject = new BasicDBObject("name",
					"Supplier1").append("address", "Cordoba 1000")
					.append("_nation_id", "1").append("phone", "123456789")
					.append("acctbal", "10")
					.append("comment", "commental;dksfj");
			supplier.insert(supplierObject);

			// Insertion example order
			// Insertion example line_item
			BasicDBObject line_itemObject = new BasicDBObject("quantity", 1)
					 .append("_partsupp_id", "1")
					 .append("_supplier_id", 1)
					 .append("line_number", "1")
					.append("discount", 0.16)
					.append("extended_price", 5)
					.append("tax", 0.8)
					.append("return_flag", "a").append("line_status", "b")
					.append("ship_date", new Date());
			// .append("ship_mode", "lala")
			// .append("discount", 0.4);
			line_item.insert(line_itemObject);
			line_itemObject = new BasicDBObject("quantity", 4)
			 .append("_partsupp_id", "1")
			 .append("_supplier_id", 1)
			 .append("line_number", "1")
			.append("discount", 0.15)
			.append("extended_price", 9)
			.append("tax", 0.6)
			.append("return_flag", "a").append("line_status", "b")
			.append("ship_date", new Date());
			line_item.insert(line_itemObject);
			
			line_itemObject = new BasicDBObject("quantity", 7)
			.append("_partsupp_id", "1")
			.append("_supplier_id", 1)
			.append("line_number", "1")
			.append("discount", 0.1)
			.append("extended_price", 19)
			.append("tax", 0.5)
			.append("return_flag", "b").append("line_status", "c")
			.append("ship_date", new Date());
			line_item.insert(line_itemObject);
			
			line_itemObject = new BasicDBObject("quantity", 7)
			.append("_partsupp_id", "1")
			.append("_supplier_id", 1)
			.append("line_number", "1")
			.append("discount", 0.1)
			.append("extended_price", 19)
			.append("tax", 0.5)
			.append("return_flag", "b").append("line_status", "d")
			.append("ship_date", new Date());
			line_item.insert(line_itemObject);
			
			
			
			// Insertion example partsupp
			// Insertion example part
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
