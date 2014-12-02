package my.crawler.extract;

import java.sql.*;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import my.crawler.loadimage.DownloadImage;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import com.sleepycat.je.tree.Key;

public class ExtractHtml {
	public HtmlCleaner cleaner = null;
	public GetRuleFromSql rules = null;
	private DownloadImage dlimg = null;

	public ExtractHtml() {
		super();
		this.cleaner = new HtmlCleaner();
		this.rules = new GetRuleFromSql();
		this.dlimg = new DownloadImage("data/crawl/image/test");
		rules.init();
	}

	public HtmlCleaner getCleaner() {
		return cleaner;
	}

	public void setCleaner(HtmlCleaner cleaner) {
		this.cleaner = cleaner;
	}

	public GetRuleFromSql getRules() {
		return rules;
	}

	public void setRules(GetRuleFromSql rules) {
		this.rules = rules;
	}

	public void manageHtml(String content, String weburl) {
		// cleaner = new HtmlCleaner();
		// rules = new GetRuleFromSql();
		Map<String, String> maprules = null;
		Vector<Map<String, String>> vcr = null;
		URL aURL;
		try {
			aURL = new URL(weburl);

			String url = aURL.getHost();
			System.out.println(url);
			vcr = rules.GetRules(url);
			String newCharset = "gb2312";
			TagNode node = cleaner.clean(content);
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
			if (ns.length != 0) {
				String chart = (((TagNode) ns[0]).getAttributeByName("content"))
						.toString();
				// System.out.println(chart);
				Matcher mhh = Pattern.compile("=(.*)").matcher(chart);
				Boolean result1 = mhh.find();
				if (result1) {
					// System.out.println(mhh.group(1));
					newCharset = mhh.group(1);
				}
			}

			// 开始xpath解析 需要的数据
			Vector<Map<String, String>> tempvcr = new Vector<Map<String, String>>();

			for (int i = 0; i < vcr.size(); i++) {
				Map<String, String> tempmap = new HashMap<String, String>();
				
				maprules = (Map) vcr.get(i);

				Iterator it = maprules.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry entry = (Map.Entry) it.next();
					Object key = entry.getKey();
					Object value = entry.getValue();

					if (!value.equals("")) {
						try {
							ns = node.evaluateXPath(value.toString());

							if (ns.length > 0) {
								String text = null;
								TagNode tagnode = (TagNode) ns[0];
								if (key.equals("picture")) {
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
											.replaceAll("&gt;", ">");
								}
								if (key.equals("contact")) {
									if (text.contains("(")) {
										text = text.split("\\(")[0];
									}

								}
								tempmap.put(key.toString(), text);
							}
						} catch (XPatherException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}
				tempvcr.add(tempmap);
			}
			getBestResult(tempvcr);

		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	// 获得最佳结果
	public void getBestResult(Vector vcr) {
		
		Map map = null;
		int num = 0, num2 = 0, which = 0;
		for (int i = 0; i < vcr.size(); i++) {
			num = 0;
			map = (Map) vcr.get(i);
			// System.out.println(map.size());

			/*
			 * Iterator it = map.entrySet().iterator();
			 * System.out.println("kkkkkkkfffffffff"); while (it.hasNext()) {
			 * Map.Entry entry = (Map.Entry) it.next(); Object key =
			 * entry.getKey(); System.out.println(key.toString()+"fffffffff");
			 * if (!key.toString().equals("")){ num +=1; }
			 * 
			 * }
			 */
			num = map.size();
			if (num >= num2) {
				which = i;
				num2 = num;
			}
		}
		// System.out.println(which);
		if (vcr.size() > 0) {
			map = (Map) vcr.get(which);
			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				System.out.println(key.toString() + " : " + value.toString());
			}
		}
	}

	// 将 UTF-8 编码的字符串转换为 GB2312 编码格式：

	public static String utf8Togb2312(String str) {

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < str.length(); i++) {

			char c = str.charAt(i);

			switch (c) {

			case '+':

				sb.append(' ');

				break;

			case '%':

				try {

					sb.append((char) Integer.parseInt(

					str.substring(i + 1, i + 3), 16));

				}

				catch (NumberFormatException e) {

					throw new IllegalArgumentException();

				}

				i += 2;

				break;

			default:

				sb.append(c);

				break;

			}

		}

		String result = sb.toString();

		String res = null;

		try {

			byte[] inputBytes = result.getBytes("8859_1");

			res = new String(inputBytes, "UTF-8");

		}

		catch (Exception e) {
		}

		return res;

	}

	// 将 GB2312 编码格式的字符串转换为 UTF-8 格式的字符串：

	public static String gb2312ToUtf8(String str) {

		String urlEncode = "";

		try {

			urlEncode = URLEncoder.encode(str, "UTF-8");

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();

		}

		return urlEncode;

	}

}
