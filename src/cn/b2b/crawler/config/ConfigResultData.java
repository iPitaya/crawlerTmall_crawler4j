package cn.b2b.crawler.config;

import java.io.File;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;


public class ConfigResultData {

	private String configpath = "conf/resultdataconfig.xml";
	private String jdbcmysql ;
	private String mysqltable ;
	private String mysqluser;
	private String mysqlpass;
	private String mongoip;
	private String mongoport;
	private String mongodatabase;
	private String mongotable;
	private String redisipport;
	
	
	

	public String getRedisipport() {
		return redisipport;
	}

	public void setRedisipport(String redisipport) {
		this.redisipport = redisipport;
	}

	public String getConfigpath() {
		return configpath;
	}

	public void setConfigpath(String configpath) {
		this.configpath = configpath;
	}

	public String getJdbcmysql() {
		return jdbcmysql;
	}

	public void setJdbcmysql(String jdbcmysql) {
		this.jdbcmysql = jdbcmysql;
	}

	public String getMysqltable() {
		return mysqltable;
	}

	public void setMysqltable(String mysqltable) {
		this.mysqltable = mysqltable;
	}

	public String getMysqluser() {
		return mysqluser;
	}

	public void setMysqluser(String mysqluser) {
		this.mysqluser = mysqluser;
	}

	public String getMysqlpass() {
		if (mysqlpass.equals(" "))
			mysqlpass = "";
		return mysqlpass;
	}

	public void setMysqlpass(String mysqlpass) {
		this.mysqlpass = mysqlpass;
	}

	public String getMongoip() {
		return mongoip;
	}

	public void setMongoip(String mongoip) {
		this.mongoip = mongoip;
	}

	public String getMongoport() {
		return mongoport;
	}

	public void setMongoport(String mongoport) {
		this.mongoport = mongoport;
	}

	public String getMongodatabase() {
		return mongodatabase;
	}

	public void setMongodatabase(String mongodatabase) {
		this.mongodatabase = mongodatabase;
	}

	public String getMongotable() {
		return mongotable;
	}

	public void setMongotable(String mongotable) {
		this.mongotable = mongotable;
	}

	public ConfigResultData(){
		init();
	}
	
	public void  init(){
		try{
			File f = new File(configpath);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);
			
			
			this.jdbcmysql = doc.getElementsByTagName("jdbcmysql").item(0).getFirstChild().getNodeValue();
			this.mysqltable = doc.getElementsByTagName("mysqltable").item(0).getFirstChild().getNodeValue();
			this.mysqluser = doc.getElementsByTagName("mysqluser").item(0).getFirstChild().getNodeValue();
			this.mysqlpass = doc.getElementsByTagName("mysqlpass").item(0).getFirstChild().getNodeValue();
		
			this.mongoip = doc.getElementsByTagName("mongoip").item(0).getFirstChild().getNodeValue();
			this.mongoport = doc.getElementsByTagName("mongoport").item(0).getFirstChild().getNodeValue();
			this.mongodatabase = doc.getElementsByTagName("mongodatabase").item(0).getFirstChild().getNodeValue();
			this.mongotable = doc.getElementsByTagName("mongotable").item(0).getFirstChild().getNodeValue();
			this.redisipport = doc.getElementsByTagName("redisipport").item(0).getFirstChild().getNodeValue();
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			
	}

}
