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

			DBCollection line_item = db.getCollection("line_item");
			DBCollection partsupp = db.getCollection("partsupp");

			// Insertion examples to nation
			BasicDBObject regionObject = new BasicDBObject("name",
					"America del sur");
			BasicDBObject nationObject = new BasicDBObject("name", "Argentina")
					.append("comment", "otro Commentario").append("region", regionObject);

			BasicDBObject nationObject2 = new BasicDBObject("name", "Bolivia")
					.append("comment", "Comentario para BOlivia").append("region", regionObject);

			BasicDBObject nationObject3 = new BasicDBObject("name", "Chile")
					.append("comment", "Comentario para chile").append("region", regionObject);

			// Insertion example supplier
			BasicDBObject supplierObject = new BasicDBObject("name",
					"Supplier1").append("address", "Cordoba 1000").append("_id", 1)
					.append("nation", nationObject).append("phone", "123456789")
					.append("acctbal", "10")
					.append("comment", "Un Comment");
			
			BasicDBObject supplierObject2 = new BasicDBObject("name",
					"Supplier2").append("address", "Cabildo 3554").append("_id", 2)
					.append("nation", nationObject2).append("phone", "1134122223")
					.append("acctbal", "18")
					.append("comment", "Comment Supplier 2");

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
			
			
			// Insertion example PART-SUPPLIER N-N
			
			BasicDBObject partSupplierObject1 = new BasicDBObject("avail_qty",
					100).append("supply_cost", 500).append("_id", 1).append("supplier",supplierObject)
					.append("comment", "Comment Part-Supplier 1 - 1 ")
					.append("part", partObject2);
			BasicDBObject partSupplierObject2 = new BasicDBObject("avail_qty",
					250).append("supply_cost", 1300).append("_id", 2).append("supplier",supplierObject2)
					.append("comment", "Comment Part-Supplier 2 - 1 ")
					.append("part", partObject1);
			BasicDBObject partSupplierObject3 = new BasicDBObject("avail_qty",
					1250).append("supply_cost", 1800).append("_id", 3).append("supplier",supplierObject2)
					.append("comment", "Comment Part-Supplier 2 - 2 ")
					.append("part", partObject2);
			
			partsupp.insert(partSupplierObject1);
			partsupp.insert(partSupplierObject2);
			partsupp.insert(partSupplierObject3);
		
			// CustomerObjects
			BasicDBObject customerObject1 = new BasicDBObject("_id", 1)
			.append("name", "nombre del primero")
			.append("address", "riobamba 32444")
			.append("phone", "123456876")
			.append("mkt_segment", "test1")
			.append("comment", "lmfaoo")
			.append("nation", nationObject2);

			BasicDBObject customerObject2 = new BasicDBObject("_id", 1)
			.append("name", "nombre del segundo")
			.append("address", "suipacha 32444")
			.append("phone", "987654")
			.append("mkt_segment", "test1")
			.append("comment", "kateioasdf")
			.append("nation", nationObject3);
			
			BasicDBObject customerObject3 = new BasicDBObject("_id", 1)
			.append("name", "nombre del tercero")
			.append("address", "cabrera 32444")
			.append("phone", "12423")
			.append("mkt_segment", "test1")
			.append("comment", "comment3")
			.append("nation", nationObject);
			// OrderObjects
			BasicDBObject orderObject1 = new BasicDBObject("_id", 1)
			.append("order_status", "s")
			.append("total_price", 11.10)
			.append("order_date", new Date())
			.append("order_priority", "3")
			.append("clerk", "clerk")
			.append("ship_priority", 10)
			.append("comment", "comment1")
			.append("customer", customerObject1);
			BasicDBObject orderObject2 = new BasicDBObject("_id", 2)
			.append("order_status", "s")
			.append("total_price", 132.10)
			.append("order_date", new Date())
			.append("order_priority", "3")
			.append("clerk", "clerk")
			.append("ship_priority", 10)
			.append("comment", "comment2")
			.append("customer", customerObject3);
			BasicDBObject orderObject3 = new BasicDBObject("_id", 3)
			.append("order_status", "s")
			.append("total_price", 57.10)
			.append("order_date", new Date())
			.append("order_priority", "3")
			.append("clerk", "clerk")
			.append("ship_priority", 10)
			.append("comment", "comment3")
			.append("customer", customerObject2);
			BasicDBObject orderObject4 = new BasicDBObject("_id", 4)
			.append("order_status", "s")
			.append("total_price", 11332.10)
			.append("order_date", new Date())
			.append("order_priority", "3")
			.append("clerk", "clerk")
			.append("ship_priority", 10)
			.append("comment", "comment4")
			.append("customer", customerObject2);
			// Insertion example line_item
			BasicDBObject line_itemObject = new BasicDBObject("quantity", 1)
					 .append("line_number", "1")
					.append("discount", 0.16)
					.append("extended_price", 5)
					.append("tax", 0.8)
					.append("return_flag", "a").append("line_status", "b")
					.append("ship_date", new Date())
					.append("ship_mode", "lala")
					.append("discount", 0.4)
					.append("order", orderObject1)
					.append("supplier", supplierObject);
			line_item.insert(line_itemObject);
			line_itemObject = new BasicDBObject("quantity", 4)
			 .append("line_number", "1")
			.append("discount", 0.15)
			.append("extended_price", 9)
			.append("tax", 0.6)
			.append("return_flag", "a").append("line_status", "b")
			.append("ship_date", new Date())
			.append("ship_mode", "lala")
			.append("discount", 0.4)
			.append("order", orderObject2)
			.append("supplier", supplierObject);
			line_item.insert(line_itemObject);
			
			line_itemObject = new BasicDBObject("quantity", 7)
			.append("line_number", "1")
			.append("discount", 0.1)
			.append("extended_price", 19)
			.append("tax", 0.5)
			.append("return_flag", "b").append("line_status", "c")
			.append("ship_date", new Date())
			.append("ship_mode", "lala")
			.append("discount", 0.4)
			.append("order", orderObject3)
			.append("supplier", supplierObject);
			line_item.insert(line_itemObject);
			
			line_itemObject = new BasicDBObject("quantity", 7)
			.append("line_number", "1")
			.append("discount", 0.1)
			.append("extended_price", 19)
			.append("tax", 0.5)
			.append("return_flag", "b").append("line_status", "d")
			.append("ship_date", new Date())
			.append("ship_mode", "lala")
			.append("discount", 0.4)
			.append("order", orderObject4)
			.append("supplier", supplierObject);
			line_item.insert(line_itemObject);
			
			
			
			// Insertion example partsupp
			// Insertion example part
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
}
