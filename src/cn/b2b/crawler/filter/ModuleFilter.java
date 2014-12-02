package cn.b2b.crawler.filter;

import java.util.List;
import java.util.Map;

public class ModuleFilter {
	private String name;
	//key为域名，value为过滤器列表
	private Map<String, List<UrlFilter> > filterMap;                                                           

	public ModuleFilter(String name, Map<String, List<UrlFilter> > filterMap) {
		super();
		this.name = name;
		this.filterMap = filterMap;
	}
	
	/**
	 * 对url进行过滤
	 * @param url
	 * @param domain
	 * @param subDomain
	 * @param depth
	 * @param parentUrl
	 * @param cmpKeySeq 决定了使用filterMap中的那些过滤器列表进行过滤
	 * @return
	 */
	public ModuleFilterResult filtering(String url, String domain, String subDomain,
			short depth, String parentUrl, String[] cmpKeySeq) {
		ModuleFilterResult filterResult = null;
		List<UrlFilter> filters = filterMap.get(domain);
		if(null == filters){
			filterResult = new ModuleFilterResult("none", false, null);
		}else{
			boolean change = false;
			for(UrlFilter filter:filters){
				FilterResult result = filter.filtering(url, domain, subDomain, depth, parentUrl, cmpKeySeq);
				if(result != null && result.isResult()){
					change = true;
					filterResult = new ModuleFilterResult(name, true, result);
					break; 
				}
			}
			if(!change){
				filterResult = new ModuleFilterResult("not_change", false, null);
			}
		}
		return filterResult;
	}

	/**
	 * 先对url进行过滤，如果有配置text过滤器，则在对url过滤结果为真的text进行过滤
	 * @param url
	 * @param domain
	 * @param subDomain
	 * @param depth
	 * @param parentUrl
	 * @param cmpKeySeq 
	 * @param html
	 * @return
	 */
	public ModuleFilterResult filtering(String url, String domain, String subDomain,
			short depth, String parentUrl, String[] cmpKeySeq, String html) {
		ModuleFilterResult filterResult = null;
		FilterResult result = null;
		List<UrlFilter> filters = filterMap.get(domain);
		if(null == filters){
			filterResult = new ModuleFilterResult(name, false, null);
		}else{
			boolean change = false;
			for(UrlFilter filter:filters){
				result = filter.filtering(url, domain, subDomain, depth, parentUrl, cmpKeySeq, html);
				if(result != null && result.isResult()){
					change = true;
					filterResult = new ModuleFilterResult(name, true, result);
					break; 
				}
			}
			if(!change){
				filterResult = new ModuleFilterResult(name, false, result);
			}
		}
		return filterResult;
	}
}
