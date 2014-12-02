package edu.uci.ics.crawler4j.crawler;

import cn.b2b.crawler.config.ConfigXML;
import cn.b2b.crawler.filter.ModuleFilter;
import cn.b2b.crawler.filter.ModuleFilterFactory;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class MyCrawlController extends CrawlController {

	protected ModuleFilter filter;
	protected ConfigXML   configxml;
	
	
	
	public ConfigXML getConfigxml() {
		return configxml;
	}

	public void setConfigxml(ConfigXML configxml) {
		this.configxml = configxml;
	}

	public ModuleFilter getFilter() {
		return filter;
	}

	public void setFilter(ModuleFilter filter) {
		this.filter = filter;
	}

	public MyCrawlController(CrawlConfig config, PageFetcher pageFetcher,
			RobotstxtServer robotstxtServer) throws Exception {
		super(config, pageFetcher, robotstxtServer);
		// TODO Auto-generated constructor stub
		
		//me
		configxml = new ConfigXML();
		
		String filterFileName = configxml.getFilterfile();
		String  needtype =  configxml.getNeedtype();
		this.filter = new ModuleFilterFactory().produce(needtype, filterFileName);
		
		//me
		
	}
	
	
}
