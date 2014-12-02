package edu.uci.ics.crawler4j.examples.localdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

public class CrawlerExtractTest {
	
	public HtmlCleaner cleaner = null;
	
	public String cutHtmlNode(String html,String nodeName)
	{
		return html.replaceAll("<"+nodeName+".*?>.*?</"+nodeName+">","");
	}

	public void extractHtml(String content){
		//content = content.replaceAll("<!--.*?-->","");
		//content = cutHtmlNode(content, "script");
		//System.out.println(content);
		cleaner = new HtmlCleaner();
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

		}
		String xpathstr = "//body/div/div/div/div/div/div/table/tbody/tr/td[2]";
		xpathstr = "//body/div/div/div/div/div/div/table/tbody/tr[1]/td[2]";
		xpathstr = "//body/div[4][@class='wrapper']/div[@class='wrap clearfix']/div[2][@class='content']/div[3][@class='cColc']/div[1][@class='companyTip']/div[@class='companyTipBox']";
		xpathstr = "//body/div[5]/div[1]/ul/li[1]";
		xpathstr = "//div[1][@class='fInfo']/ul/li[3]";
		xpathstr = "//div[1]/ul/li[3]";
		//xpathstr = "//div[@class='companyTipBox']";
		try {
			ns = node.evaluateXPath(xpathstr);
			if (ns.length > 0) {

				String text = null;
				TagNode tagnode = (TagNode) ns[0];
				System.out.println("tttt : " + tagnode.getText().toString());
			}
		} catch (XPatherException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("data/test/test.html");
		BufferedReader  reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			
			String line = null;
			while ( (line = reader.readLine()) != null ){
				//System.out.println(line);
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
