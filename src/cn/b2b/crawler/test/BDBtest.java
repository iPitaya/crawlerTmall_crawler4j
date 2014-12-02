package cn.b2b.crawler.test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.frontier.DocIDServer;
import edu.uci.ics.crawler4j.frontier.Frontier;
import edu.uci.ics.crawler4j.url.WebURL;

public class BDBtest {

	protected Frontier frontier;
	protected DocIDServer docIdServer;
	
	public void test() throws Exception{
		CrawlConfig config = new CrawlConfig();
		config.setCrawlStorageFolder("data/crawl");
		boolean resumable = config.isResumableCrawling();
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setAllowCreate(true);
		envConfig.setTransactional(resumable);
		envConfig.setLocking(resumable);
		
		File envHome = new File(config.getCrawlStorageFolder() + "/frontier");
		if (!envHome.exists()) {
			if (!envHome.mkdir()) {
				throw new Exception("Couldn't create this folder: " + envHome.getAbsolutePath());
			}
		}
		
		Environment env = new Environment(envHome, envConfig);
		docIdServer = new DocIDServer(env, config);
		frontier = new Frontier(env, config, docIdServer);
		//System.out.println("jjj");
		
		while (true) {
			
			List<WebURL> assignedURLs = new ArrayList<>(50);
			frontier.getNextURLs(50, assignedURLs);
			if (assignedURLs.size() == 0) {	
					frontier.close();
					docIdServer.close();
					System.exit(0);
					return;
			}
			for (WebURL curURL : assignedURLs) {
				//System.out.println("kkk");
				System.out.println(curURL.getURL());
			}
		}
			
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		BDBtest db = new BDBtest();
		db.test();
	}

}
