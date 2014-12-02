package cn.b2b.crawler.filter;

//import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlFilter {
	//url域名
	private String domain;
	//Pattern列表映射，用于进行正则匹配
	private Map<String, List<Pattern>> patternMap;
	//定义了使用patternMap中哪些键的Pattern进行正则匹配
	private String[] defaultCmpKeySeq;
		
	public UrlFilter(String domain, Map<String, List<Pattern>> patternMap,
			String[] defaultCmpKeySeq) {
		super();
		this.domain = domain;
		this.patternMap = patternMap;
		this.defaultCmpKeySeq = defaultCmpKeySeq;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Map<String, List<Pattern>> getPatternMap() {
		return patternMap;
	}

	public void setPatternMap(Map<String, List<Pattern>> patternMap) {
		this.patternMap = patternMap;
	}

	public String[] getDefaultCmpKeySeq() {
		return defaultCmpKeySeq;
	}

	public void setDefaultKeySeq(String[] defaultCmpKeySeq) {
		this.defaultCmpKeySeq = defaultCmpKeySeq;
	}

	/**
	 * 匹配上一个则返回真，一个都不匹配则返回假
	 * @param url
	 * @param patList
	 * @return
	 */
	private boolean matchOne(String url, List<Pattern> patList) {
		boolean result = false;
		for (Pattern pat : patList) {
			Matcher mat = pat.matcher(url);
			result = mat.find();			
			if (result) {
				break;
			}
		}
		return result;
	}

	/**
	 * 对url进行过滤
	 * @param url
	 * @param domain
	 * @param subDomain
	 * @param depth url的深度
	 * @param parentUrl 
	 * @param cmpKeySeq 定义用来获取Pattern的键；如果传递null,则使用defaultCmpKeySeq
	 * @return 非空的FilterResult对象
	 */
	public FilterResult filtering(String url, String domain, String subDomain,
			short depth, String parentUrl, String[] cmpKeySeq) {
		FilterResult filteResult = null;
		boolean result = false;
		String falseReason = "unknown";
		String trueReason = "match";
		filteResult = new FilterResult(result, falseReason, trueReason);
		if (!this.domain.equals(domain)) {
			falseReason = "domain_not_match";
			filteResult.setFalseReason(falseReason);
			return filteResult;
		} else {
			String[] useCmpKeySeq = defaultCmpKeySeq;
			if(null != cmpKeySeq){
				useCmpKeySeq = cmpKeySeq;
			}
			for(String key:useCmpKeySeq){
				List<Pattern> patternList = patternMap.get(key);
				if(null != patternList){
					result = matchOne(url, patternList);
					if(result){
						trueReason = "match_" + key;
						filteResult = new FilterResult(result, falseReason, trueReason);
						break;
					}
				}
			}
			return filteResult;			
		}
	}

	/**
	 * 匹配所有Pattern才返回真，否则返回假
	 * @param url
	 * @param patList
	 * @return
	 */
	private boolean matchAll(String text, List<Pattern> patList) {
		boolean result = true;
		for (Pattern pat : patList) {
			Matcher mat = pat.matcher(text);
			result = mat.find();			
			if (!result) {
				break;
			}
		}
		return result;
	}
	
	/**
	 * 在过滤url后，若果result为真，则可能还需进一步从html上进行过滤，两者过滤皆为真时，result才为真
	 * @param url
	 * @param domain
	 * @param subDomain
	 * @param depth
	 * @param parentUrl
	 * @param cmpKeySeq
	 * @param html
	 * @return
	 */
	public FilterResult filtering(String url, String domain, String subDomain,
			short depth, String parentUrl, String[] cmpKeySeq, String html) {
		FilterResult filteResult = filtering(url, domain, subDomain, depth, parentUrl, cmpKeySeq);
		//System.out.println("--------------------1");
		if(filteResult.isResult() && html != null && patternMap.containsKey("text")){
			boolean result = matchAll(html, patternMap.get("text"));
			//System.out.println("--------------------2");
			if(!result){
				//System.out.println("--------------------3");
				filteResult.setResult(result);
				filteResult.setFalseReason("not_match_text");
			}
		}
		return filteResult;
	}
}
