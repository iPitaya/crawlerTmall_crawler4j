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

package edu.uci.ics.crawler4j.examples.basic;

import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.MyCrawlController;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import my.crawler.extract.ExtractHtml;
import my.crawler.extract.GetRuleFromSql;
import my.crawler.loadimage.DownloadImage;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import cn.b2b.crawler.config.ConfigXML;
import cn.b2b.crawler.db.DataToMongodb;
import cn.b2b.crawler.extract.ExtractHtmlNew;
import cn.b2b.crawler.extract.ExtractHtmlTaobao;
import cn.b2b.crawler.extract.GetRuleFromDb;
import cn.b2b.crawler.file.StoreFile;
import cn.b2b.crawler.filter.FilterResult;
import cn.b2b.crawler.filter.ModuleFilter;
import cn.b2b.crawler.filter.ModuleFilterFactory;
import cn.b2b.crawler.filter.ModuleFilterResult;
import cn.b2b.crawler.proxy.manageproxy;
import cn.b2b.crawler.proxy.proxyip;
import cn.b2b.crawler.taobaoConfig.ConfigTaobao;

/**
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public class BasicCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(css|js|bmp|gif|jpe?g"
					+ "|png|tiff?|mid|mp2|mp3|mp4"
					+ "|wav|avi|mov|mpeg|ram|m4v|pdf"
					+ "|rm|smil|wmv|swf|wma|zip|rar|gz))$");

	// private ExtractHtmlNew extracthtml = new ExtractHtmlNew();
	private ExtractHtmlTaobao extracthtml = new ExtractHtmlTaobao();
	private String filterFileName = "conf/test_filter.txt";
	// private ModuleFilter filter = new ModuleFilterFactory().produce("cp",
	// filterFileName);
	// private URL aURL = null;
	private ModuleFilter filter;
	private GetRuleFromDb seturl;
	private ConfigXML configxml;

	private manageproxy mgproxy;
	private ConfigTaobao configtaobao;

	private String cmpKeySeq[] = { "cont", "list", "pass" };
	private static int nums = 0;

	// private DataToMongodb mgdb = new DataToMongodb();

	@Override
	public void init(int id, CrawlController crawlController) {
		// TODO Auto-generated method stub
		super.init(id, crawlController);
		this.filter = ((MyCrawlController) crawlController).getFilter();
		this.configxml = ((MyCrawlController) crawlController).getConfigxml();
		this.seturl = new GetRuleFromDb(configxml.getUrldb());
		seturl.setUser(configxml.getUsername());
		seturl.setPassword(configxml.getPasswd());
		seturl.init();
		mgproxy = new manageproxy(configxml);
		mgproxy.getIpsProxy();
		configtaobao = new ConfigTaobao();
	}

	public boolean urlToSql() {
		return true;
	}

	public boolean urlFilterNew(String urls, String domain, String flag,
			WebURL url) {
		try {
			if (filter != null) {
				ModuleFilterResult result = filter.filtering(urls, domain, "",
						(short) 0, "", cmpKeySeq, "");
				// System.out.println(urls);
				// System.out.println(result);
				// getShijian("urlFilterfinish");
				if (result.isResult()) {
					/*
					 * if (seturl.isExitinDb(urls, result.getFilterResult()
					 * .getTrueReason())) { return false; }
					 */
					String webtype = result.getFilterResult().getTrueReason();
					//System.out.println(webtype + " " + urls + " "
					//		+ url.getDepth());
					if (webtype.equals("match_list")
							&& url.getDepth() >= myController.getConfig()
									.getListDepthOfCrawling()) {
						//System.out.println(webtype + " " + urls + " "
						//		+ url.getDepth() + "false");
						return false;
					} else if (webtype.equals("match_pass")
							&& url.getDepth() >= myController.getConfig()
									.getContDepthOfCrawling()) {
						//System.out.println(webtype + " " + urls + " "
						//		+ url.getDepth() + "false");
						return false;
					} else {
						if (flag.equals("visit")) {

							seturl.writeUrl(urls, result.getFilterResult()
									.getTrueReason(), url.getDepth());
						}
						return true;
					}

				}
				/*
				 * FilterResult jieguo = result.getFilterResult(); if (jieguo !=
				 * null){ if (jieguo.isResult()){ return true; } }
				 */
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public boolean urlFilter(String urls, String domain, String flag, int depth) {
		// getShijian("urlFilter");
		try {
			// aURL = new URL(urls);
			// ModuleFilter filter = new ModuleFilterFactory().produce("cp",
			// filterFileName);
			if (filter != null) {
				ModuleFilterResult result = filter.filtering(urls, domain, "",
						(short) 0, "", cmpKeySeq, "");
				// System.out.println(urls);
				//System.out.println(result);
				// getShijian("urlFilterfinish");
				if (result.isResult()) {
					/*
					 * if (seturl.isExitinDb(urls, result.getFilterResult()
					 * .getTrueReason())) { return false; }
					 */

					if (flag.equals("visit")) {

						seturl.writeUrl(urls, result.getFilterResult()
								.getTrueReason(), depth);
					}
					return true;

				}
				/*
				 * FilterResult jieguo = result.getFilterResult(); if (jieguo !=
				 * null){ if (jieguo.isResult()){ return true; } }
				 */
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	// 更换ip和端口
	public void selectIpAndPort() {
		proxyip pp;
		do {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pp = mgproxy.getOneProxy();
		} while (mgproxy.checkProxy(pp.getIp(), pp.getPort()) == false);
		pageFetcher.getConfig().setProxyHost(pp.getIp());
		pageFetcher.getConfig().setProxyPort(pp.getPort());
		HttpHost proxy = new HttpHost(pp.getIp(), pp.getPort());
		pageFetcher.getHttpClient().getParams()
				.setParameter("http.route.default-proxy", proxy);
	}

	public void taobaoLogin() {
		HttpClient client = new DefaultHttpClient();
		String url = "https://login.taobao.com/member/login.jhtml";
		HttpPost post = new HttpPost(url);
		String accont = configtaobao.SelectAccont();
		String accontname = accont.split("\\|")[0];
		String accontpass = accont.split("\\|")[1];
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// 建立一个NameValuePair数组，用于存储欲传送的参数
		params.add(new BasicNameValuePair("TPL_username", accontname));
		params.add(new BasicNameValuePair("TPL_password", accontpass));
		params.add(new BasicNameValuePair("ChuJiYuTag", "ChuJiYuTag"));
		params.add(new BasicNameValuePair("TPL_checkcode", ""));
		params.add(new BasicNameValuePair("need_check_code", ""));
		params.add(new BasicNameValuePair("loginsite", "0"));
		params.add(new BasicNameValuePair("newlogin", "1"));
		params.add(new BasicNameValuePair("TPL_redirect_url",
				"http://www.taobao.com/"));
		params.add(new BasicNameValuePair("from", "tb"));

		// 添加参数
		try {
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));

			// 设置编码
			HttpResponse response;
			response = client.execute(post);
			String cookie_data = "";
			for (int i = 0; i < response.getAllHeaders().length; i++) {
				String ck_name = response.getAllHeaders()[i].getName();
				String ck_value = response.getAllHeaders()[i].getValue();
				if (ck_name.equals("Set-Cookie"))
					if (cookie_data.equals("")) {
						cookie_data = ck_value;
					} else {
						cookie_data = cookie_data + ";" + ck_value;
					}
			}
			System.out.println(cookie_data);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 发送Post,并返回一个HttpResponse对象
		// Header header = response.getFirstHeader("Content-Length");
		// String Length=header.getValue();
		// 上面两行可以得到指定的Header

	}

	@Override
	public void processPage(WebURL curURL) {
		// TODO Auto-generated method stub

		nums += 1;
		System.out.println(nums);
		if (nums % 16 == 0) {
			selectIpAndPort();
			if (configtaobao.getIsUse().equals("true")){
			taobaoLogin();
			}
			nums = 0;
		}

		super.processPage(curURL);
	}

	public boolean isDetailHtml(String urls, String domain) {
		if (filter != null) {
			ModuleFilterResult result = filter.filtering(urls, domain, "",
					(short) 0, "", cmpKeySeq, "");
			// System.out.println(result);
			if (result.isResult()) {
				if (result.getFilterResult().getTrueReason()
						.equals("match_cont")) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean isPassAndContHtml(String urls, String domain) {
		if (filter != null) {
			ModuleFilterResult result = filter.filtering(urls, domain, "",
					(short) 0, "", cmpKeySeq, "");
			// System.out.println(result);
			if (result.isResult()) {
				if (result.getFilterResult().getTrueReason()
						.equals("match_cont")
						|| result.getFilterResult().getTrueReason()
								.equals("match_pass")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic).
	 */
	@Override
	public boolean shouldVisit(WebURL url) {
		// getShijian("shouldVisit");
		String href = url.getURL().toLowerCase();
		// return !FILTERS.matcher(href).matches() &&
		// href.startsWith("http://china.makepolo.com");
		if (href.endsWith(".com")) {
			href = href + "/";
		}
		// System.out.println(url.toString());

		return !FILTERS.matcher(href).matches()
				&& urlFilterNew(href, url.getDomain(), "shouldVisit", url);

	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program.
	 */
	@Override
	public void visit(Page page) {
		// getShijian("visit");
		// this.pageFetcher.getHttpClient().getParams().setParameter(name,
		// value);
		// pageFetcher.getConfig().setProxyHost("14.18.16.67");
		// pageFetcher.getConfig().setProxyPort(80);
		/*
		 * nums += 1; System.out.println(nums); if (nums%10 == 0){
		 * selectIpAndPort(); nums = 0; }
		 */
		int docid = page.getWebURL().getDocid();
		String url = page.getWebURL().getURL();
		String domain = page.getWebURL().getDomain();
		// String path = page.getWebURL().getPath();
		// String subDomain = page.getWebURL().getSubDomain();
		String parentUrl = page.getWebURL().getParentUrl();
		// String anchor = page.getWebURL().getAnchor();
		short depth = page.getWebURL().getDepth();

		System.out.println("Docid: " + docid);
		System.out.println("URL: " + url);
		// System.out.println("Domain: '" + domain + "'");
		// System.out.println("Sub-domain: '" + subDomain + "'");
		// System.out.println("Path: '" + path + "'");
		System.out.println("Parent page: " + parentUrl);
		// System.out.println("Anchor text: " + anchor);
		System.out.println("url Depth: " + depth);

		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			// String text = htmlParseData.getText();
			String html = htmlParseData.getHtml();
			//StoreFile storehtml = new StoreFile();
			//storehtml.writedata(html, docid);
			// List<WebURL> links = htmlParseData.getOutgoingUrls();

			// System.out.println(html);
			// ExtractHtml extracthtml = new ExtractHtml();
			try {

				if (isDetailHtml(url, domain)) {
					/*
					 * System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
					 * //extracthtml.manageHtml(html,
					 * page.getWebURL().toString());
					 * System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
					 * 
					 * if (extracthtml.getDataresult() != "") {
					 * seturl.writeDataStr(extracthtml.getDataresult()); }
					 */

					System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
					extracthtml.manageHtml(html, page.getWebURL(),
							htmlParseData);
					System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println(Thread.currentThread().getName());

			// 把处理完的网页url入库
			urlFilter(url, domain, "visit", depth);

			// System.out.println("Text length: " + text.length());
			System.out.println("Html length: " + html.length());
			// System.out.println("Number of outgoing links: " + links.size());
			html = null;
		}

		/*
		 * Header[] responseHeaders = page.getFetchResponseHeaders(); if
		 * (responseHeaders != null) { System.out.println("Response headers:");
		 * for (Header header : responseHeaders) { System.out.println("\t" +
		 * header.getName() + ": " + header.getValue()); } }
		 */

		System.out.println("=============");
	}

	public void getShijian(String strtime) {
		Date date = new Date(new Date().getTime());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = format.format(date);
		System.out.println(str + "  " + strtime);
	}
}
