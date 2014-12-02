package cn.b2b.crawler.filter;

import java.util.Date;

public class Test {
	public static void main(String args[]){
		long timestart = new Date().getTime();
		String filterName = "test";
		String filterFileName = "conf/test_filter.txt";
		ModuleFilter filter = new ModuleFilterFactory().produce(filterName, filterFileName);
		//String url = "http://lingcaiying2010.1688.com/page/creditdetail.htm";
		//String domain = "1688.com";
		String url = "http://yunica.b2b.hc360.com/";
		url = "http://company.ch.gongchang.com/info/5939605_f04b/";
		String domain = "gongchang.com";
		String subDomain = "";
		short depth = 0;
		String parentUrl = "";
		String cmpKeySeq[] = {"cont","list","pass"};
		String html = "";
		if(null != filter){
			ModuleFilterResult result = filter.filtering(url, domain, subDomain, depth, parentUrl, cmpKeySeq, html);
			System.out.println(result);
			System.out.println(result.getFilterResult().getTrueReason());
		}else{
			System.out.println("filter is null!");
		}
		long timeafter = new Date().getTime(); 
		System.out.println(timeafter - timestart);
	}
}
