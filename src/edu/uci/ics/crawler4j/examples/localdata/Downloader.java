/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.uci.ics.crawler4j.examples.localdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;

import my.crawler.extract.ExtractHtml;

import org.apache.http.HttpStatus;

import cn.b2b.crawler.db.DataToMongodb;
import cn.b2b.crawler.extract.ExtractHtmlNew;
import cn.b2b.crawler.extract.ExtractHtmlTaobao;
import cn.b2b.crawler.extract.GetRuleFromDb;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.parser.ParseData;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.url.WebURL;

/**
 * This class is a demonstration of how crawler4j can be used to download a
 * single page and extract its title and text.
 */
public class Downloader {

	private Parser parser;
	private PageFetcher pageFetcher;
	
	private String dburl = "jdbc:mysql://192.168.3.17:3306/info_crawler";
	private String dbuser = "tongbao";
	private String dbpwd = "mainone%test123";
	private  DataToMongodb mgdb = new DataToMongodb();
	//private GetRuleFromDb seturl = new GetRuleFromDb(dburl);

	public Downloader() {
		CrawlConfig config = new CrawlConfig();
		parser = new Parser(config);
		pageFetcher = new PageFetcher(config);
		//seturl.setUser(dbuser);
		//seturl.setPassword(dbpwd);
		//seturl.init();
	}

	private Page download(String url) {
		WebURL curURL = new WebURL();
		curURL.setURL(url);
		PageFetchResult fetchResult = null;
		try {
			//pageFetcher.getConfig().setProxyHost("111.1.36.138");
			//pageFetcher.getConfig().setProxyPort(80);
			fetchResult = pageFetcher.fetchHeader(curURL);
			if (fetchResult.getStatusCode() == HttpStatus.SC_OK) {
				try {
					Page page = new Page(curURL);
					fetchResult.fetchContent(page);
					if (parser.parse(page, curURL.getURL())) {
						return page;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} finally {
			if (fetchResult != null)
			{
				fetchResult.discardContentIfNotConsumed();
			}			
		}
		return null;
	}

	public void processUrl(String url) {
		
        long time1=0;
        long time2=0;
        
		System.out.println("Processing: " + url);
		//me
		ExtractHtmlTaobao extracthtml = new ExtractHtmlTaobao();
		//me
		time1 = new Date().getTime();
		
		Page page = download(url);
		time2 = new Date().getTime();		
		System.out.println("时间差   ：  " + time2 +"  " + time1 + " " + (time2 - time1));
		if (page != null) {
			time1 = new Date().getTime();
			ParseData parseData = page.getParseData();
			time2 = new Date().getTime();
			System.out.println("时间差：  " + (time2 - time1));
			if (parseData != null) {
				if (parseData instanceof HtmlParseData) {
					HtmlParseData htmlParseData = (HtmlParseData) parseData;
					//System.out.println(htmlParseData.getHtml().toString());
					//CrawlerExtractTest extracttext = new CrawlerExtractTest();
					//extracttext.extractHtml(htmlParseData.getHtml().toString());
					
					time1 = new Date().getTime();
					extracthtml.manageHtml(htmlParseData.getHtml().toString(), page.getWebURL(),htmlParseData);
					time2 = new Date().getTime();
					
					System.out.println("时间差：  " + (time2 - time1));
					//System.out.println(extracthtml.getDataresult() + "ok");
					
					/*
					if (extracthtml.getDataresult() != ""){
						seturl.writeDataStr(extracthtml.getDataresult());
						//System.out.println(extracthtml.getDataresult().getBytes(Charset.forName("UTF-8")));
						String ss = extracthtml.getDataresult();
						String s1;
						try {
							s1 = new String(ss.getBytes(),"UTF-8");
							seturl.writeDataStr(ss);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					}
					*/
					/*
					if (extracthtml.getMginfo() != null){
						mgdb.insertData(extracthtml.getMginfo());
					}
					*/
					/*
					if (extracthtml.getDataresult() != "") {
						seturl.writeDataStr(extracthtml.getDataresult());
					}
					*/
					/*
					for (WebURL webURL :htmlParseData.getOutgoingUrls()){
						if (webURL.getURL().toLowerCase().contains(".jpg")){
						System.out.println(webURL.getURL());
						}
					}
					*/
					
					System.out.println("Title: " + htmlParseData.getTitle());
					System.out.println("Text length: " + htmlParseData.getText().length());
					System.out.println("Html length: " + htmlParseData.getHtml().length());
				}
			} else {
				System.out.println("Couldn't parse the content of the page.");
			}
		} else {
			System.out.println("Couldn't fetch the content of the page.");
		}
		System.out.println("==============");
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		Downloader downloader = new Downloader();
		//downloader.processUrl("http://en.wikipedia.org/wiki/Main_Page/");
		//downloader.processUrl("http://www.yahoo.com/");
		//downloader.processUrl("http://china.makepolo.com/product-detail/100103863690.html");
		//downloader.processUrl("http://shop1384880363335.1688.com/page/creditdetail.htm");
		//downloader.processUrl("http://s.hc360.com/company/%B9%AB%CB%BE.html");
		//downloader.processUrl("http://aobeisi.b2b.hc360.com/shop/show.html");
		//downloader.processUrl("http://tong0312.b2b.hc360.com/shop/show.html");
		//downloader.processUrl("http://seawind2009.cn.gongchang.com/about.html");
		//downloader.processUrl("http://company.ch.gongchang.com/info/54908317_37ef/");
		//downloader.processUrl("http://company-jsxinyin.cn.gongchang.com/about.html");
		//downloader.processUrl("http://alibo118.cn.gongchang.com/about.html");
		//downloader.processUrl("http://qcm1221.cn.gongchang.com/about.html");
		//downloader.processUrl("http://arlennurser.cn.gongchang.com/about.html");
		//downloader.processUrl("http://cdyd889.cn.gongchang.com/about.html");
		//downloader.processUrl("http://rocky-yinian.cn.gongchang.com/about.html");
		//downloader.processUrl("http://wmjbp.b2b.hc360.com/shop/show.html");
		//downloader.processUrl("http://siearo.1688.com/page/creditdetail.htm?tracelog=p4p");
		//downloader.processUrl("http://s.hc360.com/company/%B9%AB%CB%BE.html?M=%EF%BF%BD%EF%BF%BD%F0%A1%A2%B9%EF%BF%BD%EF%BF%BD%EF%BF%BD%3A%EF%BF%BD%EF%BF%BD%EF%BF%BD%CC%BC%EF%BF%BD%EF%BF%BD%EF%BF%BD%EF%BF%BD%EF%BF%BD%EF%BF%BD%D3%BC%EF%BF%BD%3A%EF%BF%BD%EF%BF%BD%EF%BF%BD%EF%BF%BD%EF%BF%BD%EF%BF%BD%CC%BC%EF%BF%BD%EF%BF%BD%EF%BF%BD%EF%BF%BD%EF%BF%BD%EF%BF%BD%D3%BC%EF%BF%BD&e=121&s=%EF%BF%BD%EF%BF%BD%D0%B5%EF%BF%BD%EF%BF%BD%EF%BF%BD%EF%BF%BD%D2%B5%EF%BF%BD%E8%B1%B8&v=4");
		//downloader.processUrl("http://s.hc360.com/company/%B9%AB%CB%BE.html?M=%CD%BF%EF%BF%BD%CF%A1%EF%BF%BD%EF%BF%BD%EF%BF%BD%EF%BF%BD%DC%A1%EF%BF%BD%EF%BF%BD%EF%BF%BD%EF%BF%BD%E6%B4%A6%EF%BF%BD%EF%BF%BD%3A%EF%BF%BD%EF%BF%BD%EF%BF%BD%CF%A1%EF%BF%BD%EF%BF%BD%EF%BF%BD%EF%BF%BD%EF%BF%BD%C6%B7%3A%EF%BF%BD%EF%BF%BD%EF%BF%BD%EF%BF%BD%EF%BF%BD%EF%BF%BD%EF%BF%BD%CF%A1%EF%BF%BD%EF%BF%BD%EF%BF%BD%EF%BF%BD%EF%BF%BD%C6%B7&s=%EF%BF%BD%EF%BF%BD%D0%B5%EF%BF%BD%EF%BF%BD%EF%BF%BD%EF%BF%BD%D2%B5%EF%BF%BD%E8%B1%B8&v=4&z=%EF%BF%BD%D0%B9%EF%BF%BD%3A%EF%BF%BD%D3%B1%EF%BF%BD%CA%A1");
		//downloader.processUrl("http://shop1362504253375.1688.com/page/offerlist.htm");
		
		String url = null;
		
		if ( args.length != 1  ){
			System.out.println("测试抽取部分，请输入正确的url");
			try {
				BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
				url = br.readLine();	
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (url.startsWith("http://")){
				downloader.processUrl(url);
			}else{
				System.out.println("url has  problems");
			}
		}else{
			downloader.processUrl(args[0]);
		}
		
		/*
		String line = "http://s.hc360.com/company/%B9%AB%CB%BE.html?I=%EF%BF%BD%EF%BF%BD%D0%B5%EF%BF%BD%EF%BF%BD%D2%B5&i=%B0%FC%D7%B0%A1%A2%D3%A1%CB%A2%A1%A2%D6%BD%D2%B5%3A%CB%DC%C1%CF%B0%FC%D7%B0%D6%C6%C6%B7%3A%CB%DC%C1%CF%B4%FC&mas=2&v=4";
		line = URLDecoder.decode(line);
		System.out.println(line);
		*/
		System.out.println("wan..................");
		System.exit(0);
		
	}
}
