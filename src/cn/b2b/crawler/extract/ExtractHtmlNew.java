package cn.b2b.crawler.extract;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import my.crawler.extract.GetRuleFromSql;
import my.crawler.loadimage.DownloadImage;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import com.mongodb.BasicDBObject;

import cn.b2b.crawler.config.ConfigXML;

public class ExtractHtmlNew {
	//public HtmlCleaner cleaner = null;
	public GetRuleFromDb rules = null;
	public DownloadImage dlimg = null;
	public ConfigXML config = null;
	private BasicDBObject mginfo = new BasicDBObject();
	private String dataresult;
	//private Vector<Map<String, String>> tempvcr = new Vector<Map<String, String>>();  //存放抽取数据

	public String getDataresult() {
		return dataresult;
	}

	public void setDataresult(String dataresult) {
		this.dataresult = dataresult;
	}

	public BasicDBObject getMginfo() {
		return mginfo;
	}

	public void setMginfo(BasicDBObject mginfo) {
		this.mginfo = mginfo;
	}

	public ExtractHtmlNew() {
		super();
		this.config = new ConfigXML();
		//this.cleaner = new HtmlCleaner();
		this.rules = new GetRuleFromDb(config.getUrldb());
		this.dlimg = new DownloadImage(config.getPicpath());
		rules.setUser(config.getUsername());
		rules.setPassword(config.getPasswd());
		rules.init();
		rules.setDatatypes(config.getDatatypes());

	}
	/*
	public HtmlCleaner getCleaner() {
		return cleaner;
	}

	public void setCleaner(HtmlCleaner cleaner) {
		this.cleaner = cleaner;
	}
	*/

	public GetRuleFromDb getRules() {
		return rules;
	}

	public void setRules(GetRuleFromDb rules) {
		this.rules = rules;
	}
	
	

	public void manageHtml(String content, String weburl) {
		HtmlCleaner cleaner = new HtmlCleaner();
		// rules = new GetRuleFromSql();
		System.out.println(weburl);
		Map<String, String> maprules = null;
		Vector<Map<String, String>> vcr = null;
		URL aURL;
		try {
			aURL = new URL(weburl);

			String url = aURL.getHost();
			System.out.println(url);
			vcr = rules.getRules(url);
			TagNode node = cleaner.clean(content);
			//content = null;
			Object[] ns = node.getElementsByName("title", true);
			if (ns.length > 0) {
				System.out.println("title=" + ((TagNode) ns[0]).getText());
			}
			try {
				ns = node.evaluateXPath("//head/meta[@http-equiv]");
			} catch (XPatherException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// System.out.println(ns.length);
			/*
			if (ns.length != 0) {
				String chart = (((TagNode) ns[0]).getAttributeByName("content"))
						.toString();
				// System.out.println(chart);
				Matcher mhh = Pattern.compile("=(.*)").matcher(chart);
				Boolean result1 = mhh.find();
			}
			*/

			// 锟斤拷始xpath锟斤拷锟斤拷 锟斤拷要锟斤拷锟斤拷锟�
			Vector<Map<String, String>> tempvcr = new Vector<Map<String, String>>();
			//tempvcr.clear();
			/*
			for (int i=0;i<tempvcr.size();i++){
				tempvcr.remove(i);
			}
			*/
			
			
			for (int i = 0; i < vcr.size(); i++) {
				Map<String, String> tempmap = new HashMap<String, String>();

				maprules = (Map) vcr.get(i);

				Iterator it = maprules.entrySet().iterator();
				Map.Entry entry;
				while (it.hasNext()) {
					entry = (Map.Entry) it.next();
					Object key = entry.getKey();
					Object value = entry.getValue();

					//System.out.println(key.toString() + value.toString());
					if (value.toString().contains("|")){
						tempmap = getDataByTag(tempmap,value.toString(),key.toString(),content,weburl);
					}else{
						tempmap = getDataByXpath(tempmap,value.toString(),key.toString(),node,weburl);
					}
				}
				tempvcr.add(tempmap);
			}
			getBestResult(tempvcr);
			//getBestResult(vcr);
			tempvcr = null;
			

		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		cleaner = null;

	}
	
	public Map getDataByTag(Map tempmap,String value,String key,String content,String weburl){
		//String tag = "<span>电&nbsp;&nbsp;&nbsp;&nbsp;话：</span>|</li>";
		
		//System.out.println(tag2[0] +" : "+ value);
		System.out.println(value + "  tag");
		if (!value.equals("")) {
			String[] tag2 =value.split("\\|");
			if (tag2.length == 2){
			String starttag = tag2[0];
			String endtag = tag2[1];
			int fromIndex = content.indexOf(starttag);
			System.out.println(fromIndex+"   ");
			if ( fromIndex > -1){
			fromIndex = fromIndex + starttag.length();
			int toIndex = content.indexOf(endtag, fromIndex);
			System.out.println(fromIndex+"   "+toIndex);
			String datastr = content.substring(fromIndex, toIndex).trim();
			datastr = datastr.replaceAll("<.*?>", "");
			System.out.println(datastr);
			tempmap.put(key.toString(), datastr);
			}
			}
		}else {
			String text2 = null;
			text2 = value.toString();
			if (key.equals("url")) {
				text2 = weburl;
			}
			tempmap.put(key.toString(), text2);
		}
		return tempmap;
	}
	
	public Map getDataByXpath(Map tempmap,String value,String key,TagNode node,String weburl){
		Object[] ns;
		if (!value.equals("")) {
			try {
				ns = node.evaluateXPath(value.toString());

				if (ns.length > 0) {

					String text = null;
					TagNode tagnode = (TagNode) ns[0];
					//System.out.println(tagnode.getText().toString());
					if (key.equals("keyword")) {
						try {
							ns = node
									.evaluateXPath("//head/meta[@name='keywords']");
							String keyword = (((TagNode) ns[0])
									.getAttributeByName("content"))
									.toString();
							text = keyword;

						} catch (XPatherException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (key.equals("picture")) {
						text = tagnode.getAttributeByName("src")
								.toString();
						if (!text.equals("")) {
							dlimg.processUrl(text);
						}
					} else {
						text = tagnode.getText().toString()
								.replace('\n', ' ')
								.replace('\r', ' ')
								.replaceAll("\\?", " ")
								.replaceAll("&nbsp;", " ")
								.replaceAll("&gt;", ">").trim();
					}
					if (key.equals("contact")) {
						if (text.contains("(")) {
							text = text.split("\\(")[0];
						}
						if (text.contains("（")){
							text = text.split("（")[0];
						}

					}
					if (!key.equals("content") && !key.equals("title")) {
						text = getProcessStr(text);
					} else {
						text = getContentStr(text);
					}
					tempmap.put(key.toString(), text);
					//maprules.put(key.toString(), "");
					//maprules.put(key.toString(), text);
					System.out.println(key.toString() + "  : "
							+ text);
				}
			} catch (XPatherException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			String text2 = null;
			text2 = value.toString();
			if (key.equals("url")) {
				text2 = weburl;
			}
			tempmap.put(key.toString(), text2);
			//maprules.put(key.toString(), "");
			//maprules.put(key.toString(), text2);
		}
		return tempmap;
	}

	// 处理内容中的标签
	public String getContentStr(String msg) {
		Pattern pattern = Pattern.compile("\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(msg);
		String temp = "";
		if (matcher.find()) {
			//System.out.println(matcher.group(0));
			temp = matcher.group(0);
		}
		//msg = msg.replaceAll(temp, "");
		int index = msg.indexOf("});");
		if(index > 0){
			msg = msg.substring(index+3);
		}
		msg = msg.replaceAll("该公司没有上传图片", "");
		msg = msg.replaceAll("\"", " ");
		msg = msg.replaceAll("'", " ");
		msg = msg.trim();
		return msg;
	}

	// 字符串处理
	public String getProcessStr(String msg) {
		msg = msg.replace('\n', ' ').replace('\r', ' ').replaceAll("\\?", " ")
				.replaceAll("&nbsp;", " ").replaceAll("&nbsp", " ")
				.replaceAll("&gt;", ">").trim();

		if (msg.contains("：")) {
			if (msg.split("：").length > 1){
				msg = msg.split("：")[1];
			}else{
				msg = "";
			}
		}

		if (msg.contains(":") && !msg.startsWith("http:")) {
			if ( msg.split("：").length > 1 ){
				msg = msg.split(":")[1];
			}else{
				msg = "";
			}
		}
		
		
		if (msg.contains("(")) {
			msg = msg.split("\\(")[0];
		}
		
		msg = msg.replaceAll("\"", " ");
		msg = msg.replaceAll("'", " ");

		return msg;
	}

	// 锟斤拷锟斤拷锟窖斤拷锟�
	public void getBestResult(Vector vcr) {

		mginfo.clear();
		dataresult = "";
		Map map = null;
		int num = 0, num2 = 0, which = 0;
		//System.out.println(vcr.size() + "kkkkkkkkkkkkkkkkkkkkk");
		for (int i = 0; i < vcr.size(); i++) {
			num = 0;
			map = (Map) vcr.get(i);
			// System.out.println(map.size());

			Iterator it = map.entrySet().iterator();
			//System.out.println("kkkkkkkfffffffff");
			Map.Entry entry;
			while (it.hasNext()) {
				entry = (Map.Entry) it.next();
				Object key = entry.getKey();
				//System.out.println(key.toString() + "fffffffff");
				if (!key.toString().equals("")) {
					num += 1;
				}

			}

			// num = map.size();
			if (num >= num2) {
				which = i;
				num2 = num;
			}
		}
		// System.out.println(which);
		String result = "";
		String key = "";
		String value = "";
		if (vcr.size() > 0) {
			map = (Map) vcr.get(which);
			List<String> list = rules.getTypeList();
			for (int i = 0; i < list.size(); i++) {
				if (i == 0) {
					result = "{";
				}

				key = list.get(i);
				try {
					if (map.containsKey(key)) {
						value = map.get(list.get(i)).toString();
					} else {
						value = "";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				result = result + "\"" + key + "\":\"" + value + "\"";
				if (i == list.size() - 1) {
					result = result + "}";
				} else {
					result = result + ",";
				}
				// System.out.println(value);
				if (value.equals("null")) {
					value = "";
				}
				mginfo = addData(mginfo, key, value);

			}

		}
		
		//System.out.println(mginfo.toString());
		dataresult = result;
		System.out.println(result);
		result = null;
	}

	public BasicDBObject addData(BasicDBObject info, String key, String value) {
		info.put(key, value);
		return info;
	}

}
