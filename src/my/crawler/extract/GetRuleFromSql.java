package my.crawler.extract;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class GetRuleFromSql {

	public Connection conn = null;

	public GetRuleFromSql() {
		super();

	}

	public void init() {
		String driver = "com.mysql.jdbc.Driver"; // 驱动程序名

		String url = "jdbc:mysql://127.0.0.1:3306/mycrawler"; // URL指向要访问的数据库名scutcs

		String user = "root"; // MySQL配置时的用户名

		String password = ""; // MySQL配置时的密码

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
	public boolean writeUrl(String url, String type) {
		//if (isExitinDb(url,type)){
		//	return false;
		//}
		try {
			Statement statement = null;
			statement = conn.createStatement();
			// statement用来执行SQL语句
			String sql = "insert into  urltable(url,type)  values ( '" + url + "','" + type + "')"; // 要执行的SQL语句
			//System.out.println(sql);

			statement.execute(sql); // 结果集
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public Vector GetRules(String site) {
		Statement statement = null;

		Vector<Map<String, String>> vcr = new Vector<Map<String, String>>();
		try {
			statement = conn.createStatement(); // statement用来执行SQL语句

			String sql = "select * from productRules  where domain = '" + site
					+ "'"; // 要执行的SQL语句

			ResultSet rs;
			rs = statement.executeQuery(sql); // 结果集

			String name = null;
			String domain = null;
			String picture = null;
			String place = null;
			String contact = null;
			String mobile = null;
			String telephone = null;
			String email = null;
			String address = null;
			String summry = null;
			String website = null;
			String price = null;
			String brands = null;
			String company = null;

			while (rs.next()) {
				Map<String, String> map = new HashMap<String, String>();
				name = rs.getString("name"); // 选择sname这列数据
				domain = rs.getString("domain");
				picture = rs.getString("picture");
				place = rs.getString("place");
				contact = rs.getString("contact");
				mobile = rs.getString("mobile");
				telephone = rs.getString("telephone");
				email = rs.getString("email");
				address = rs.getString("address");
				summry = rs.getString("summry");
				website = rs.getString("website");
				price = rs.getString("price");
				brands = rs.getString("brands");
				company = rs.getString("company");

				map.put("name", name);
				map.put("domain", domain);
				map.put("picture", picture);
				map.put("place", place);
				map.put("contact", contact);
				map.put("mobile", mobile);
				map.put("telephone", telephone);
				map.put("email", email);
				map.put("address", address);
				map.put("summry", summry);
				map.put("website", website);
				map.put("price", price);
				map.put("brands", brands);
				map.put("company", company);
				vcr.add(map);
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return vcr;
	}
}
