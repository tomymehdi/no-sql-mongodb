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
					.append("comment", "otro Commentario").append("region", regionObject);

			BasicDBObject nationObject2 = new BasicDBObject("name", "Bolivia")
					.append("comment", "Comentario para BOlivia").append("region", regionObject);

			BasicDBObject nationObject3 = new BasicDBObject("name", "Chile")
					.append("comment", "Comentario para chile").append("region", regionObject);
			nation.insert(nationObject);
			nation.insert(nationObject2);
			nation.insert(nationObject3);

			// Insertion example supplier
			BasicDBObject supplierObject = new BasicDBObject("name",
					"Supplier1").append("address", "Cordoba 1000").append("_id", 1)
					.append("_nation_id", "1").append("phone", "123456789")
					.append("acctbal", "10")
					.append("comment", "Un Comment");
			supplier.insert(supplierObject);
			
			BasicDBObject supplierObject2 = new BasicDBObject("name",
					"Supplier2").append("address", "Cabildo 3554").append("_id", 2)
					.append("_nation_id", "1").append("phone", "1134122223")
					.append("acctbal", "18")
					.append("comment", "Comment Supplier 2");
			supplier.insert(supplierObject2);

			// Insertion example order
			
			
			// Insertion example part
			BasicDBObject partObject1 = new BasicDBObject("name",
					"Parte 1").append("mfgr", "MFGR Parte 1").append("_id", 1)
					.append("brand", "Marca").append("type", "type")
					.append("size", "18").append("container", "Container de part1").append("retail_price", 17)
					.append("comment", "Comment Part 1 ");
		
			BasicDBObject partObject2 = new BasicDBObject("name",
					"Parte 2").append("mfgr", "MFGR Parte 2").append("_id", 2)
					.append("brand", "Otra Marca").append("type", "Otro tipo")
					.append("size", "12").append("container", "Container de part2").append("retail_price", 13)
					.append("comment", "Comment Part 2 ");
			part.insert(partObject1);
			part.insert(partObject2);
			
			
			// Insertion example PART-SUPPLIER N-N
			
			BasicDBObject partSupplierObject1 = new BasicDBObject("avail_qty",
					100).append("supply_cost", 500).append("_id", 1).append("_supplier_id",1).append("_id", 1)
					.append("comment", "Comment Part-Supplier 1 - 1 ");
			BasicDBObject partSupplierObject2 = new BasicDBObject("avail_qty",
					250).append("supply_cost", 1300).append("_id", 1).append("_supplier_id",1).append("_id", 2)
					.append("comment", "Comment Part-Supplier 2 - 1 ");
			BasicDBObject partSupplierObject3 = new BasicDBObject("avail_qty",
					1250).append("supply_cost", 1800).append("_id", 1).append("_supplier_id",2).append("_id", 2)
					.append("comment", "Comment Part-Supplier 2 - 2 ");
			partsupp.insert(partSupplierObject1);
			partsupp.insert(partSupplierObject2);
			partsupp.insert(partSupplierObject3);
		
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
