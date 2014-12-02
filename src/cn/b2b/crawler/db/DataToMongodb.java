package cn.b2b.crawler.db;

import java.net.UnknownHostException;

import cn.b2b.crawler.config.ConfigResultData;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;

public class DataToMongodb {
		private String mgip = "192.168.3.182";
		private int  mgport = 30000;
		private String  mgdatabase = "crawler_wrl";
		private String  mgtable = "test";
		
		private DBCollection  coll = null ;
		
		
		
		public DBCollection getColl() {
			return coll;
		}

		public void setColl(DBCollection coll) {
			this.coll = coll;
		}

		public DataToMongodb() {
			try {
				init();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void initMongodb(ConfigResultData config){
			mgip = config.getMongoip();
			mgport = Integer.valueOf(config.getMongoport());
			mgdatabase = config.getMongodatabase();
			mgtable = config.getMongotable();
			
			try {
				init();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void init() throws UnknownHostException{
			Mongo mg = new Mongo(mgip,mgport);

	        DB db = mg.getDB(mgdatabase);

	        coll = db.getCollection(mgtable);
		}
		
		public BasicDBObject  addData(BasicDBObject info,String key,String value){
			info.put(key, value);
			return info;
		}
		
		public void  insertData(BasicDBObject info){
			if (coll != null){
				coll.insert(info);
			}
		}
		
		
		
}
