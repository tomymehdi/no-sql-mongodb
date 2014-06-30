line_item = {
	"_id": 1,
	"line_number": 1,
	"quantity": 1,
	"extended_price": 123098.213,
	"tax": 123.123,
	"return_flag": "s",
	"line_status": "d",
	"ship_date": ISODate("2010-09-24"),
	"commit_date": ISODate("2010-09-24"),
	"receip_date": ISODate("2010-09-24"),
	"shiping_struct": "text",
	"ship_mode": "text",
	"discount": 121.12,
	"comment": "text",
	"order": {
		"_id": 1,
		"order_status": "s",
		"total_price": 112.123,
		"order_date": ISODate("2010-09-24"),
		"order_priority": "order_priority",
		"clerk": "clerk",
		"ship_priority": 10,
		"comment": "comment",
		"customer": {
			"_id": 1,
			"name": "name",
			"address": "address",
			"phone": "1234577",
			"acctbal": 10,
			"mkt_segment": "text",
			"comment": "comment",
			"nation": {
				"_id": 1,
				"name": "name",
				"comment": "comment",
				"region": {
					"_id": 1, 
					"name": "name", 
					"comment": "comment"
				}
			}
		},
	},
	"supplier": {
		"_id": 1,
		"name": "name",
		"address": "address",
		"phone": "2944798178",
		"acctbal": 10,
		"comment": "comment",
		"nation": {
			"_id": 1,
			"name": "name",
			"comment": "comment",
			"region": {
				"_id": 1, 
				"name": "name", 
				"comment": "comment"
			}
		}
	}
},

partsupp = {
	"_id": 1,
	"avail_qty": 100,
	"supply_cost": 1000,
	"comment": "comment",
	"part":{
		"_id": 1,
		"name": "text",
		"mfgr": "text",
		"brand": "text",
		"type": "text",
		"size": 1,
		"container": "text",
		"retail_price": 1,
		"comment": "text"
	},
	"supplier": {
		"_id": 1,
		"name": "name",
		"address": "address",
		"phone": "2944798178",
		"acctbal": 10,
		"comment": "comment",
		"nation": {
			"_id": 1,
			"name": "name",
			"comment": "comment",
			"region": {
				"_id": 1, 
				"name": "name", 
				"comment": "comment"
			}
		}
	}
}