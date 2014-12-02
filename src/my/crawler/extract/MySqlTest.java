package my.crawler.extract;

import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MySqlTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String driver = "com.mysql.jdbc.Driver";         // 驱动程序名

        
        String url = "jdbc:mysql://localhost/mycrawler";     // URL指向要访问的数据库名scutcs          
        
        String user = "root";       // MySQL配置时的用户名

        String password = "";      // MySQL配置时的密码

        try { 
         
         Class.forName(driver);    // 加载驱动程序

        
         Connection conn = DriverManager.getConnection(url, user, password);      // 连续数据库

         if(!conn.isClosed()) 
          System.out.println("Succeeded connecting to the Database!");     //验证是否连接成功

         
         Statement statement = conn.createStatement();               // statement用来执行SQL语句

        
         String sql = "select * from productRules";                  // 要执行的SQL语句

        
         ResultSet rs = statement.executeQuery(sql);       // 结果集

         System.out.println("-----------------------------------------");
         System.out.println("执行结果如下所示:");
         System.out.println("-----------------------------------------");
         System.out.println(" 学号" + "\t" + " 姓名" + "\t\t" + "性别");
         System.out.println("-----------------------------------------");

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
         
         Map<String, String> map = new HashMap<String, String>();
         
         while(rs.next()) {
 
         
          name = rs.getString("name");                           // 选择sname这列数据
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
          
          Iterator it = map.entrySet().iterator();
          while (it.hasNext()){
        	  Map.Entry entry = (Map.Entry)it.next();
        	  Object key = entry.getKey();
        	  Object value = entry.getValue();
        	  System.out.println(key + "  :  " + value);
          }
         
          System.out.println(rs.getString("domain") + "\t" + name + "\t" + rs.getString("place"));        // 输出结果
         }

         rs.close();
         conn.close();
         //测试URL
         URL aURL = new URL("http://java.sun.com:80/docs/books/tutorial" + "/index.html?name=networking#DOWNLOADING");
         System.out.println(aURL.getHost());

        } catch(ClassNotFoundException e) {


         System.out.println("Sorry,can`t find the Driver!"); 
         e.printStackTrace();


        } catch(SQLException e) {


         e.printStackTrace();


        } catch(Exception e) {


         e.printStackTrace();


        }
	}

}
