package cn.b2b.crawler.rulefile;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class RuleMap {
	private String domain;
	private Map<String, List<String>> patternMap;
	
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public Map<String, List<String>> getPatternMap() {
		return patternMap;
	}
	public void setPatternMap(Map<String, List<String>> patternMap) {
		this.patternMap = patternMap;
	}
	
	public void process(Map  strmap){
		
	}
		
}
