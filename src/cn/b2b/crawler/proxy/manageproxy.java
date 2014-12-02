package cn.b2b.crawler.proxy;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

import cn.b2b.crawler.config.ConfigXML;
import cn.b2b.crawler.db.ManageMysql;

public class manageproxy {
	private List<proxyip>  iplist = new ArrayList<proxyip>();
	private ConfigXML config;
	
	public manageproxy(ConfigXML config){
		this.config = config;
	}
	
	public List<proxyip> getIplist() {
		return iplist;
	}

	public void setIplist(List<proxyip> iplist) {
		this.iplist = iplist;
	}

	public List<proxyip> getIpsProxy(){
		ManageMysql  mmsql = new ManageMysql(this.config);
		mmsql.init();
		
		String  sql = "select * from proxylist where  flag = 1";
		ResultSet rs = mmsql.readData(sql);
		try {
			while (rs.next()){
				proxyip pip = new proxyip();
				pip.setIp(rs.getString("proxy_ip"));
				pip.setPort(rs.getInt("proxy_port"));
				iplist.add(pip);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return iplist;
	}
	
	public boolean  checkProxy(String ip,int port){
		boolean  flag = false;
		HttpHost proxy = new HttpHost(ip, port);
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
		HttpGet post = new HttpGet("http://www.b2b.cn");
		HttpResponse response;
		try {
			response = client.execute(post);
			HttpEntity entity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();
			System.out.println(statusCode);
			if (statusCode == 200 ){
				flag = true;
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return flag;
	}
	
	public proxyip getOneProxy(){
		System.out.println(Math.random()  +  "  " + iplist.size());
		int sum = (int)(Math.random()*iplist.size());
		System.out.println("    " + iplist.get(sum).getIp() + "   " + sum);
		return  iplist.get(sum);
	}
}
