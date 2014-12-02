package cn.b2b.crawler.extract;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class GetRuleFromDb {
	public Connection conn = null;
	public String url;
	public String ruletable;
	public String datatypes;
	private String user;
	private String password;
	private List<String>  vc = null; //typelist
	//private Vector<Map<String, String>> vcr = new Vector<Map<String, String>>(); //getRules

	public GetRuleFromDb(String urlname) {
		super();
		url = urlname;
		user = "root";
		password = "";
		 
		// init();
	}
	
	

	public String getUser() {
		return user;
	}



	public void setUser(String user) {
		this.user = user;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;	
	}



	public void init() {
		String driver = "com.mysql.jdbc.Driver"; // 驱动程序名

		//String url = "jdbc:mysql://127.0.0.1:3306/mycrawler"; // URL指向要访问的数据库名scutcs

		//String user = "root"; // MySQL配置时的用户名

		//String password = ""; // MySQL配置时的密码

		try {
			Class.forName(driver);// 加载驱动程序

			conn = DriverManager.getConnection(url, user, password); // 连续数据库

			if (!conn.isClosed()) {
				System.out.println("Succeeded connecting to the Database!"); // 验证是否连接成功
			} else {
				System.out.println("error connecting to the Database!");
				System.exit(1);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getDatatypes() {
		return datatypes;
	}

	public void setDatatypes(String datatypes) {
		this.datatypes = datatypes;
		initTypeList();
	}
	public  String  getTypesStr(){
		return datatypes.replace('|', ',');
	}
	
	public  void  initTypeList(){
		vc = new ArrayList<String>();
		String[]  str = datatypes.split("\\|");
		for (int i=0; i<str.length;i++){
			vc.add(str[i]);
			//System.out.println(str[i]);
		}
	}
	
	public  List<String> getTypeList(){
		return vc;
	}
	
	//判断url是否存在
		public boolean  isExitinDb(String url,String type){
			Statement statement = null;
			try {
				statement = conn.createStatement();
				// statement用来执行SQL语句
				String sql = "select  *  from  urltable   where  url  =  '" + url + "'" ;
				ResultSet rs;
				rs = statement.executeQuery(sql); // 结果集
				if (rs.next()){
					System.out.println(url + "已存在");
					return true;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return  false;
		}
		
		//把抽取的url入库
		public boolean writeUrl(String url, String type,int depth) {
			//if (isExitinDb(url,type)){
			//	return false;
			//}
			try {
				Statement statement = null;
				
				statement = conn.createStatement();
				// statement用来执行SQL语句
				String sql = "insert into  urltable(url,type,depth)  values ( '" + url + "','" + type + "',"+depth+")"; // 要执行的SQL语句
				//System.out.println(sql);

				statement.execute(sql); // 结果集
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
		}
	
	public Vector getRules(String site){
		Statement statement = null;

		Vector<Map<String, String>> vcr = new Vector<Map<String, String>>();
		//vcr.clear();
		/*
		for (int i=0;i<vcr.size();i++){
			vcr.remove(i);
		}
		*/
		
		try {
			
			statement = conn.createStatement(); // statement用来执行SQL语句
			
			String  typestr = getTypesStr();
			String[] parts = site.split("\\.");
			if (parts.length > 2) {
				site = parts[parts.length - 2] + "." + parts[parts.length - 1];
			}

			String sql = "select " +typestr + " from companyrule  where domain = '" + site+ "'"; // 要执行的SQL语句

			ResultSet rs;
			//System.out.println(sql);
			rs = statement.executeQuery(sql); // 结果集
			List vct = getTypeList();
			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				//System.out.println(vct.size());
				for(int i=0;i<vct.size();i++){
					  String type =(String) vct.get(i);
					  //System.out.println(type);
					  map.put(type, rs.getString(type));
					}
				vcr.add(map);
			}
			
			
		}catch(SQLException e1){
			e1.printStackTrace();
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			init();
		}
		return vcr;
	}
	
	//把json数据串入mysql
	public boolean writeDataStr(String str) {
		//if (isExitinDb(url,type)){
		//	return false;
		//}
		String sql = null;
		try {
			Statement statement = null;
			
			statement = conn.createStatement();
			// statement用来执行SQL语句
			sql = "insert into  dataresult(resultstr)  values ( '" + str + "')"; // 要执行的SQL语句
			//System.out.println(sql);

			statement.execute(sql); // 结果集
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(sql);
			return false;
		}
		return true;
	}
	
	
}
