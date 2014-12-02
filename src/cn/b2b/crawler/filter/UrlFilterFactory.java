package cn.b2b.crawler.filter;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class UrlFilterFactory { 
	public static final String DefaultCmpKeySeq[] = {"pass","list","cont"}; 
	public static final String DefaultPatternKeyList[] = {"pass","list","cont", "text"};
	
	private  final int MinKeyNum = 2;
	private  final String MustKey = "main";
	private String[] patternKeyList;
	private String[] defaultCmpKeySeq;
	
	public UrlFilterFactory(String[] patternKeyList, String[] defaultCmpKeySeq) {
		super();
		this.patternKeyList = patternKeyList;
		this.defaultCmpKeySeq = defaultCmpKeySeq;
	}

	/**
	 * 生成过滤器对象
	 * @param strMap 
	 * @return
	 */
	public UrlFilter produce(Map<String, List<String>> strMap){
		UrlFilter filter = null;	
		if(strMap.size() > MinKeyNum && strMap.containsKey(MustKey)){
			Map<String,List<Pattern>> patMap = new HashMap<String, List<Pattern>>();
			for(String key:patternKeyList){
				List<String> strList = strMap.get(key);
				if(null == strList) continue;
				for(String str:strList){
					if(null != str && str.length() > 0){
						int flags = Pattern.CASE_INSENSITIVE|Pattern.DOTALL; //take care
						try{
							Pattern pat = Pattern.compile(str, flags);
							if(!patMap.containsKey(key)){
								patMap.put(key, new ArrayList<Pattern>());
							}
							patMap.get(key).add(pat);
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
			if(patMap.size() > 0){
				filter = new UrlFilter(strMap.get(MustKey).get(0), patMap, defaultCmpKeySeq);
			}
		}
		return filter;
	}
}
