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

import cn.b2b.crawler.config.ConfigStart;
import cn.b2b.crawler.startfile.startProcess;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.MyCrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

/**
 * @author Yasser Ganjisaffar <lastname at gmail dot com>
 */
public class BasicCrawlController {

	public static void main(String[] args){
		
		
		
		/*
		if (args.length != 2) {
			System.out.println("Needed parameters: ");
			System.out.println("\t rootFolder (it will contain intermediate crawl data)");
			System.out.println("\t numberOfCralwers (number of concurrent threads)");
			return;
		}
		*/
		
		ConfigStart cfs = new ConfigStart();

		/*
		 * crawlStorageFolder is a folder where intermediate crawl data is
		 * stored.
		 */
		//String crawlStorageFolder = args[0];
		String crawlStorageFolder = cfs.getStoragefolder();

		/*
		 * numberOfCrawlers shows the number of concurrent threads that should
		 * be initiated for crawling.
		 */
		//int numberOfCrawlers = Integer.parseInt(args[1]);
		int numberOfCrawlers = cfs.getNumberofCrawlers();

		CrawlConfig config = new CrawlConfig();

		config.setCrawlStorageFolder(crawlStorageFolder);

		/*
		 * Be polite: Make sure that we don't send more than 1 request per
		 * second (1000 milliseconds between requests).
		 */
		//config.setPolitenessDelay(1000);
		config.setPolitenessDelay(cfs.getDaleytime());

		/*
		 * You can set the maximum crawl depth here. The default value is -1 for
		 * unlimited depth
		 */
		//config.setMaxDepthOfCrawling(5);
		config.setMaxDepthOfCrawling(cfs.getDepthcrawl());
		
		config.setListDepthOfCrawling(cfs.getListwebdepth());
		config.setContDepthOfCrawling(cfs.getContwebdepth());

		/*
		 * You can set the maximum number of pages to crawl. The default value
		 * is -1 for unlimited number of pages
		 */
		//config.setMaxPagesToFetch(-1);
		config.setMaxPagesToFetch(cfs.getPagesfetchnum());

		/*
		 * Do you need to set a proxy? If so, you can use:
		 * config.setProxyHost("proxyserver.example.com");
		 * config.setProxyPort(8080);
		 * 
		 * If your proxy also needs authentication:
		 * config.setProxyUsername(username); config.getProxyPassword(password);
		 */
		//config.setProxyHost("221.130.17.38");
		//config.setProxyHost("118.186.34.10");
		//config.setProxyPort(81);

		/*
		 * This config parameter can be used to set your crawl to be resumable
		 * (meaning that you can resume the crawl from a previously
		 * interrupted/crashed crawl). Note: if you enable resuming feature and
		 * want to start a fresh crawl, you need to delete the contents of
		 * rootFolder manually.
		 */
		//config.setResumableCrawling(true);
		config.setResumableCrawling(cfs.getResumablecrawling());

		/*
		 * Instantiate the controller for this crawl.
		 */
		PageFetcher pageFetcher = new PageFetcher(config);
		RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
		RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
		MyCrawlController controller = null;
		try {
			controller = new MyCrawlController(config, pageFetcher, robotstxtServer);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		 * For each crawl, you need to add some seed urls. These are the first
		 * URLs that are fetched and then the crawler starts following links
		 * which are found in these pages
		 */
		
		//种子url初始化
		startProcess startpro = new startProcess();
		startpro.setController(controller);
		startpro.start();

		//controller.addSeed("http://www.ics.uci.edu/");
		//controller.addSeed("http://www.ics.uci.edu/~lopes/");
		//controller.addSeed("http://www.ics.uci.edu/~welling/");
		
		//controller.addSeed("http://www.54pc.com/");
		//controller.addSeed("http://www.cn5135.com/product/");
		//controller.addSeed("http://www.hc360.com/cp/");
		//controller.addSeed("http://china.makepolo.com/");
		//controller.addSeed("http://page.1688.com/cp/cp1.html?keywords=&button_click=top&n=y");
		//controller.addSeed("http://s.1688.com/company/-CBC4BCBEC7E0B7FED7B0.html");
		
		//controller.addSeed("http://info.b2b.hc360.com/list/company.shtml");
		//controller.addSeed("http://www.baidu.com");

		/*
		 * Start the crawl. This is a blocking operation, meaning that your code
		 * will reach the line after this only when crawling is finished.
		 */
		controller.start(BasicCrawler.class, numberOfCrawlers);
		System.exit(0);
	}
}
