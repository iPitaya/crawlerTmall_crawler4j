package my.crawler.extract;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;



public class ProxyTest {
	public static void main(String[] args) throws Exception {
		//commonProxy();
		System.out.println("--------------------------------------");
		notProxy();
		System.out.println("--------------------------------------");
		useProxy();
	}

	/***
	 * 未使用 httpclient 代理
	 * 
	 * @throws Exception
	 */
	public static void notProxy() throws Exception {
		HttpClient client = new DefaultHttpClient();
		HttpGet post = new HttpGet("http://china.makepolo.com/");
		HttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		//System.out.println("Response content: "
		//		+ EntityUtils.toString(entity, "UTF-8"));
		client.getConnectionManager().shutdown();
	}

	/**
	 * 使用 httpclient 代理
	 * 
	 * @throws Exception
	 */
	public static void useProxy() throws Exception {
		HttpHost proxy = new HttpHost("118.85.208.193", 80);
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

		HttpGet post = new HttpGet("http://china.makepolo.com");
		HttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		System.out.println(entity == null);
		System.out.println(entity.getContentLength());
		System.out.println("Response content: "
				+ EntityUtils.toString(entity, "utf-8"));
		client.getConnectionManager().shutdown();
	}

	/***
	 * 普通代理
	 * 
	 * @throws Exception
	 */
	public static void commonProxy() throws Exception {
		String strUrl = "http://www.ziroom.com/z/nl/sub/o5-s40001.html";
		URL url = new URL(strUrl);
		URLConnection conn = url.openConnection();

		Properties systemProperties = System.getProperties();
		systemProperties.setProperty("http.proxyHost", "118.186.34.12");
		systemProperties.setProperty("http.proxyPort", "80");

		String ss = "";

		BufferedReader rd = new BufferedReader(new InputStreamReader(
				conn.getInputStream(), "gb2312"));

		while ((ss = rd.readLine()) != null) {
			System.out.println(ss);
		}

		rd.close();
	}
}
