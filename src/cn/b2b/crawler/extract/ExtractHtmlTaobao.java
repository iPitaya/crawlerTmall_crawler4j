package cn.b2b.crawler.extract;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import my.crawler.loadimage.DownloadImage;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;
import org.json.JSONObject;

import cn.b2b.crawler.config.ConfigXML;
import cn.b2b.crawler.db.ManageMysql;
import cn.b2b.crawler.rulefile.RuleModule;

import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class ExtractHtmlTaobao {
	public ConfigXML config = null;
	private ManageMysql mgdb = null;
	private RuleModule rulemodule = null;
	private String[] typekey = {"simple","table","photo","loadpic","innerhtml"};
	private DownloadImage dlimg = null;
	
	public ExtractHtmlTaobao(){
		config = new ConfigXML();
		mgdb = new ManageMysql(config);
		mgdb.init();
		rulemodule = new RuleModule();
		this.dlimg = new DownloadImage(config.getPicpath());
	}
	public void manageHtml(String content, WebURL weburl,HtmlParseData htmlParseData) {
		HtmlCleaner cleaner = new HtmlCleaner();
		TagNode node = cleaner.clean(content);
		Map  mapdata = new HashMap();
		
		/*
		if (weburl.contains("m.taobao.com/i")){
			String rule = "//body/div[2]/div[2]/div[1]/p";
			extractTable(node,rule,mapdata)	;
			String rule1 = "//head/title";	
			getMsgByXpath(node,rule1,"title",mapdata);
			getPicUrl(htmlParseData,mapdata);
		}else if (weburl.contains("m.taobao.com/da")){
			String rule = "//*[@id='itemProp']/div/table/tbody/tr";
			extractTable(node,rule,mapdata)	;
			String rule1 = "//*[@id='u99_rtf']/p";	
			getMsgByXpath(node,rule1,"detail",mapdata);
		}
		*/
		List temp;
		for (String typedata:typekey){
			temp = (List)rulemodule.getTypeList(weburl.toString(), typedata);
			if (temp == null){
				continue;
			}
			for(Object rules:temp){
				String[] keyrule = rules.toString().split(":");
				//System.out.println(rules + " " +  keyrule.length);
				if (keyrule.length>1){
					if(typedata.equals("simple")){
						getMsgByXpath(node,keyrule[1],keyrule[0],mapdata);
					}
				}
				if (keyrule.length == 1 ){
					if(typedata.equals("table")){
						extractTable(node,keyrule[0],mapdata);
					}
					if(typedata.equals("photo")){
						//getPicUrl(htmlParseData,mapdata);
						getPhotoUrl(node,keyrule[0],mapdata);
					}
					//if (typedata.equals("loadpic")){
					//	loadPicByXpath(node,keyrule[0]);
					//}
					if (typedata.equals("innerhtml")){
						getInnerHtmlByTag(cleaner,node,keyrule[0]);
					}
				}
			}
		}
		
		JSONObject json4 = new JSONObject(mapdata);//转化map对象  
        System.out.println(json4.toString());  
        String detaildata = getTaobaoContent(content);
        String sitein = weburl.getParentUrl();
        String sitestr = sitein.substring(0+7, sitein.indexOf(".com/")+4);
        String sqlstr = "insert into taobaodata(url,html,jsondata,content,onlyflag,sitename) values('"+weburl.toString()+"',\""+content.replaceAll("\"", "'")+"\",'"+json4.toString()+"','"+detaildata+"','"+weburl.getParentUrl()+"','"+sitestr+"')";
        //System.out.println(sqlstr);
        mgdb.writeData(sqlstr);
        
	}
	
	public String getTaobaoContent(String con){
		String content = con;
		int index = content.indexOf("\"apiItemDesc\":\"");
		if(index > 0){
			content = content.substring(index+15);
		}
		index = content.indexOf("\",");
		if(index > 0){
			content = content.substring(0, index);
		}
		System.out.println(content);
		return  (getHtmlByHttp(content));
	}
	
	public String getHtmlByHttp(String url){
		HttpClient client = new DefaultHttpClient();
		HttpGet post = new HttpGet(url);
		//post.addHeader("user-agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
		HttpResponse response;
		String result = "";
		try {
			response = client.execute(post);
			HttpEntity entity = response.getEntity();
			result = EntityUtils.toString(entity, "UTF-8");
			//System.out.println("Response content: "+ result);
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		result = result.substring(10, result.length()-3);
		//System.out.println(result);
		return result;
	}
	
	public void loadPicByXpath(TagNode node,String rule){
		Object[] ns = null;		
		try {
			ns = node.evaluateXPath(rule);
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Object tag:ns){
			String tagstr = ((TagNode)tag).getAttributeByName("src").toString();
			System.out.println(tagstr + " tagstr ");
			if ( !tagstr.equals("") ){
				dlimg.processUrl(tagstr);
			}
		}
	}
	
	public void getInnerHtmlByTag(HtmlCleaner cleaner,TagNode node,String rule){
		Object[] ns = null;	
		//System.out.println("hello ffffffffffffffffffffff");
		try {
			ns = node.evaluateXPath(rule);
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//for (Object tag:ns){
		//	System.out.println(cleaner.getInnerHtml((TagNode)tag));
		//}
	}
	
	public void extractTable(TagNode node,String rule, Map mapdata){
		Object[] ns = null;		
		try {
			ns = node.evaluateXPath(rule);
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (Object tag:ns){
			String tagstr = ((TagNode)tag).getText().toString();
			if (tagstr.contains(":")){
				String key = tagstr.split(":")[0].trim().replaceAll("\n", "");
				String value = tagstr.split(":")[1].trim().replaceAll("\n", "").replaceAll("\r", " ");
				//System.out.println("key : " + key);
				//System.out.println("value : " + value);
				mapdata.put(key, value);
			}
		}
	}
	
	public void getMsgByXpath(TagNode node,String rule,String keyname,Map mapdata){
		Object[] ns = null;
		try {
			ns = node.evaluateXPath(rule);
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(Object tag:ns){
			String tagstr = ((TagNode)tag).getText().toString();
			//System.out.println(tagstr);
			mapdata.put(keyname, tagstr.trim());
		}
	}
	
	public void  getPhotoUrl(TagNode node,String rule,Map mapdata){
		Object[] ns = null;
		try {
			ns = node.evaluateXPath(rule);
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int i = 0;
		//System.out.println(ns.length);
		for(Object tag:ns){
			//System.out.println(tag);
			String tagstr = ((TagNode)tag).getAttributeByName("data-src").toString();
			//System.out.println(tagstr);
			mapdata.put("pic"+i, tagstr.trim());
			i++;
		}
	}
	
	public void getPicUrl(HtmlParseData htmlParseData,Map mapdata){
		int i = 0;
		for (WebURL webURL :htmlParseData.getOutgoingUrls()){
			if (webURL.getURL().toLowerCase().contains(".jpg")){
				//System.out.println(webURL.getURL());
				mapdata.put("pic"+i, webURL.getURL());
				i++;
			}
		}
	}
	
}
