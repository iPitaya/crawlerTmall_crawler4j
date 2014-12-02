package cn.b2b.crawler.filter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleFilterFactory {
	private final String SplitStr = "!!";
	private final String SplitKV = ":";
	private UrlFilterFactory urlFilterFactory = new UrlFilterFactory(UrlFilterFactory.DefaultPatternKeyList,UrlFilterFactory.DefaultCmpKeySeq);
	
	/**
	 * 生成模块过滤器
	 * @param name 过滤器名称
	 * @param fileName 配置文件名
	 * @return
	 */
	public ModuleFilter produce(String name, String fileName){
		ModuleFilter modFilter = null;
		BufferedReader reader = null;
		try{
			Map<String, List<UrlFilter> > filterMap = new HashMap<String, List<UrlFilter> >();
			reader = new BufferedReader(new FileReader(new File(fileName)));
			String line = null;
			Map<String, List<String> > strMap = new HashMap<String, List<String> >();
			while((line = reader.readLine()) != null){
				line = line.trim();
				if(line.length() == 0) continue;
				if(!SplitStr.equals(line)){
					int index = line.indexOf(SplitKV);
					if(index > 0){
						String key = line.substring(0, index);
						String value = line.substring(index+1);
						if(!strMap.containsKey(key)){
							strMap.put(key, new ArrayList<String>());
						}
						strMap.get(key).add(value);				
					}
				}else{
					UrlFilter urlFilter = urlFilterFactory.produce(strMap);
					if(urlFilter != null){
						if(!filterMap.containsKey(urlFilter.getDomain())){				
							filterMap.put(urlFilter.getDomain(), new ArrayList<UrlFilter>());
						}
						if(urlFilter != null){
							filterMap.get(urlFilter.getDomain()).add(urlFilter);
						}
					}
					strMap.clear();
				}
			}
			reader.close();
			if(filterMap.size() > 0){
				modFilter = new ModuleFilter(name, filterMap);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return modFilter;
	}
}
