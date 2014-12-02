package cn.b2b.crawler.config;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class ConfigXML {
	private  String  filterfile ;
	private  String  picpath;
	private  String  dbip;
	private  String  dbport;
	private  String  needtype;
	private  String  datatypes;
	private  String  configpath = "conf/config.xml";
	private  String  urldb;
	private  String  username;
	private  String  passwd;
	
	public  ConfigXML(){
		init();
	}
	
	public  void  init(){
		try {
			File f = new File(configpath);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);
			//NodeList nl = doc.getElementsByTagName("purchase-order");
			this.filterfile = doc.getElementsByTagName("filterfile").item(0).getFirstChild().getNodeValue();
			this.picpath = doc.getElementsByTagName("pathpic").item(0).getFirstChild().getNodeValue();
			this.dbip = doc.getElementsByTagName("dbip").item(0).getFirstChild().getNodeValue();
			this.dbport = doc.getElementsByTagName("port").item(0).getFirstChild().getNodeValue();
			this.needtype = doc.getElementsByTagName("type").item(0).getFirstChild().getNodeValue();
			this.datatypes = doc.getElementsByTagName("data").item(0).getFirstChild().getNodeValue();
			this.urldb = doc.getElementsByTagName("jdbcmysql").item(0).getFirstChild().getNodeValue();
			this.username = doc.getElementsByTagName("mysqluser").item(0).getFirstChild().getNodeValue();
			this.passwd = doc.getElementsByTagName("mysqlpass").item(0).getFirstChild().getNodeValue();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public String getUrldb() {
		return urldb;
	}

	public void setUrldb(String urldb) {
		this.urldb = urldb;
	}

	public String getFilterfile() {
		return filterfile;
	}
	public void setFilterfile(String filterfile) {
		this.filterfile = filterfile;
	}
	public String getPicpath() {
		return picpath;
	}
	public void setPicpath(String picpath) {
		this.picpath = picpath;
	}
	public String getDbip() {
		return dbip;
	}
	public void setDbip(String dbip) {
		this.dbip = dbip;
	}
	public String getDbport() {
		return dbport;
	}
	public void setDbport(String dbport) {
		this.dbport = dbport;
	}
	public String getNeedtype() {
		return needtype;
	}
	public void setNeedtype(String needtype) {
		this.needtype = needtype;
	}
	public String getDatatypes() {
		return datatypes;
	}
	public void setDatatypes(String datatypes) {
		this.datatypes = datatypes;
	}
	
	
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswd() {
		return passwd.trim();
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public  void  printdata(){
		System.out.println(this.configpath);
		System.out.println(this.getFilterfile());
		System.out.println(this.getPicpath());
		System.out.println(this.getDbip());
		System.out.println(this.getDbport());
		System.out.println(this.getNeedtype());
		System.out.println(this.getDatatypes());
		System.out.println(this.getUsername());
		System.out.println(this.getPasswd());
		
		
	}
	
	public static void  main(String[] args){
		ConfigXML  config =  new  ConfigXML();
		config.printdata();
	}
	

}
