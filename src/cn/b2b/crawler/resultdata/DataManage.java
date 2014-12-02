package cn.b2b.crawler.resultdata;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import cn.b2b.crawler.config.ConfigResultData;
import cn.b2b.crawler.db.DataToMongodb;
import cn.b2b.crawler.extract.GetRuleFromDb;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class DataManage {

	private JsonData jsondata = new JsonData();

	private BasicDBObject mginfo = new BasicDBObject();

	private ConfigResultData configresult = new ConfigResultData();
	private DataMySQL datasql = new DataMySQL();
	private DataToMongodb mgdb = new DataToMongodb();
	private String tablename;
	private DataCompanyId datacompanyid = new DataCompanyId();
	private DataSiteMsg sitemsg = new DataSiteMsg();

	public DataManage() {
		datasql.initMysql(configresult);
		tablename = configresult.getMysqltable();

		mgdb.initMongodb(configresult);

	}

	public BasicDBObject getBasicDBObject(String str) throws JSONException {
		return jsondata.getMapData(str);
	}

	// 把mongodb中的数据 按照公司id更新数据比较快
	public void updateMongoData(int companyid, BasicDBObject info) {
		info.put("companyid", companyid);
		info.put("docnum", companyid);
		DBObject updateCondition = new BasicDBObject();
		updateCondition.put("companyid", companyid);
		DBObject updateSetValue = new BasicDBObject("$set", info);
		mgdb.getColl().update(updateCondition, updateSetValue, true, false);
	}

	// redis 获得 数据 如果不存在
	public String isUrlInRedis(String url) {
		long time0 = 0;
		long time1 = 1;
		time0 = new Date().getTime();
		String val = UJedis.get("crawler:" + url);
		time1 = new Date().getTime();
		System.out.println("redis time " + (time1 - time0));
		return val;
	}

	// redis插入数据
	public void UrlInsertRedis(String key, String value) {
		UJedis.set("crawler:" + key, value);
	}

	public int useMysql() throws SQLException, JSONException {
		String sql = "select * from " + tablename + " where flag = 0 limit 50";
		String updatesql = "update  " + tablename + "  set flag = 1 where id =";
		ResultSet rs = datasql.readData(sql);
		int num = 0;
		long time0 = 0;
		long time1 = 0;

		while (rs.next()) {
			mginfo = getBasicDBObject(rs.getString("resultstr"));
			datasql.updateData(updatesql + rs.getString("id"));
			if (isBasicObjectOk(mginfo)) {
				String redisvalue = isUrlInRedis(mginfo.getString("url"));
				mginfo = sitemsg.getSiteMsg(mginfo, mginfo.getString("url"));
				if (redisvalue == null) {
					System.out.println(mginfo.getString("url") + "不存在");
					mginfo = datacompanyid.addCompanyId(mginfo);
					UrlInsertRedis(mginfo.getString("url"),
							String.valueOf(datacompanyid.getCompanyid()));
					System.out.println(mginfo.toString());
					time0 = new Date().getTime();
					mgdb.insertData(mginfo);
					time1 = new Date().getTime();
					System.out.println("mongo insert time " + (time1 - time0));
				} else {
					System.out.println(mginfo.getString("url") + " "
							+ redisvalue + "已存在");
					time0 = new Date().getTime();
					updateMongoData(Integer.valueOf(redisvalue), mginfo);
					time1 = new Date().getTime();
					System.out.println("mongo update time " + (time1 - time0));
				}

			}
			num++;
		}

		// System.out.println(num + "num");

		return num;
	}

	// 判断basicoject 保证数据有标题和联系方式 是否可以入mongodb
	public boolean isBasicObjectOk(BasicDBObject info) {
		if (info.get("title").equals("")) {
			return false;
		}

		if (info.get("telphone").equals("") && info.get("mobile").equals("")) {
			return false;
		}else{
			String telphone = info.get("telphone").toString();
			String mobile = info.get("mobile").toString();
			info.put("telphone", mobile);
			info.put("mobile", telphone);
		}

		if (info.get("address").equals("")) {
			return false;
		}

		return true;
	}

	public static void main(String args[]) throws SQLException, JSONException {
		DataManage manage = new DataManage();
		int num = 0;
		// int num = manage.useMysql();

		while (true) {
			num = manage.useMysql();
			/*
			 * if (manage.useMysql() == 0) { num++; } else { num = 0; } if (num
			 * > 5) { System.out.println("没有新数据。。。 程序停止  "); break; }
			 */
			System.out.println("num ................. ....... " +  num);
			if (num == 0) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(num);
			}
			// System.out.println(num);

			/*
			 * System.out.println(new Date().getTime()); long time=1277106667;
			 * Date date=new Date(time); SimpleDateFormat format=new
			 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); String
			 * str=format.format(date); System.out.println(str);
			 */

		}

	}
}
