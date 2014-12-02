package cn.b2b.crawler.test;

import java.util.List;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.frontier.DocIDServer;
import edu.uci.ics.crawler4j.frontier.Frontier;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyFrontier extends Frontier{

	public MyFrontier(Environment env, CrawlConfig config,
			DocIDServer docIdServer) {
		super(env, config, docIdServer);
		// TODO Auto-generated constructor stub
	}
	
}
