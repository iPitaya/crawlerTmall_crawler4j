package cn.b2b.crawler.resultdata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import cn.b2b.crawler.config.ConfigResultData;

public class DataMySQL {
	
	public Connection conn = null;
	public ResultSet rs;
	
	private String url = "jdbc:mysql://localhost/mycrawler"; // URL指向要访问的数据库名scutcs

	private String user = "root"; // MySQL配置时的用户名

	private String password = ""; // MySQL配置时的密码

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}

	public ResultSet getRs() {
		return rs;
	}

	public void setRs(ResultSet rs) {
		this.rs = rs;
	}

	public DataMySQL() {
		super();
		
		//init();

	}
	
	public void initMysql(ConfigResultData config){
		url = config.getJdbcmysql();
		user = config.getMysqluser();
		password = config.getMysqlpass();
		init();
	}

	public void init() {
		String driver = "com.mysql.jdbc.Driver"; // 驱动程序名

		//String url = "jdbc:mysql://localhost/mycrawler"; // URL指向要访问的数据库名scutcs

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

	// 返回的是结果集
	public ResultSet readData(String sqlstr) {
		Statement statement = null;
		try {
			
			statement = conn.createStatement(); // statement用来执行SQL语句

			String sql = sqlstr; // 要执行的SQL语句

			rs = statement.executeQuery(sql); // 结果集
		} catch (SQLException e) {
			try {
				conn.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			init();
		}
		return rs;
	}

	public boolean writeData(String sqlstr) {
		try {
			Statement statement = null;
			
			statement = conn.createStatement();
			// statement用来执行SQL语句

			// System.out.println(sql);

			statement.execute(sqlstr); // 结果集
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean updateData(String sqlstr) {
		try {
			Statement statement = null;
			
			statement = conn.createStatement();
			// statement用来执行SQL语句

			// System.out.println(sql);

			statement.execute(sqlstr); // 结果集
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public void checkConn(){
		if ( conn == null ){
			init();
		}
	}
	
	public static void main(String args[]){
		
	}
}
