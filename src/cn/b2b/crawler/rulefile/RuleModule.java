package cn.b2b.crawler.rulefile;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.b2b.crawler.filter.UrlFilter;

public class RuleModule {
	private String filename = "conf/extractrule/rulefile.txt";
	private final String lineflagstr = "!!";
	private final String keysplit = ":";
	private Map<String, Map > fileruleMap = new HashMap<String, Map >();
	private List<String> domainlist = new ArrayList<String>();
	private String[] typekey = {"simple","table","photo"};
	
	public RuleModule(){
		readRuleFile();
	}
	
	public void readRuleFile(){
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String domain = null;
			String line;
			Map<String, List<String> > sameruleMap = new HashMap<String, List<String> >();
			try {
				while ((line = br.readLine()) != null){
					
					if ( !lineflagstr.equals(line) ){
						int index = line.indexOf(keysplit);
						if(index > 0){
							String key = line.substring(0, index);
							String value = line.substring(index+1);
							if (key.equals("domain")){
								domain = value;
								domainlist.add(domain);
							}
							if(!sameruleMap.containsKey(key)){
								sameruleMap.put(key, new ArrayList<String>());
							}
							sameruleMap.get(key).add(value);	
							System.out.println(key + " .. " + value);
						}
					}else{
						fileruleMap.put(domain, sameruleMap);
						sameruleMap = new HashMap<String, List<String> >();
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void printData(){
		
		/*
		Iterator it = fileruleMap.entrySet().iterator();
		Map.Entry entry;
		while (it.hasNext()) {
			entry = (Map.Entry) it.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
		}
		*/
		
		for (int i = 0;i < domainlist.size();i++){
			String key = domainlist.get(i);
			System.out.println(domainlist.get(i));
			for (String str:typekey){
				System.out.println(fileruleMap.get(key).get(str));
			}
		}
		
	}
	
	public List getDataByType(String domn,String type){
		if (fileruleMap.containsKey(domn)){
			if (fileruleMap.get(domn).containsKey(type)){
				return (List)fileruleMap.get(domn).get(type);
			}
		}
		return null;
	}
	public String getDomainByList(String url){
		for(String str:domainlist){
			//System.out.println(url.contains(str));
			Pattern  p = Pattern.compile(str);
			Matcher m = p.matcher(url);
			//System.out.println(m.find());
			if (m.find()){
				return str;
			}
		}
		return null;
	}
	
	public List getTypeList(String url,String type){
		String domn = getDomainByList(url);
		//System.out.println(domn);
		if (domn != null){
			//System.out.println(domn);
			return getDataByType(domn,type);
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		RuleModule rulemodule = new RuleModule();
		rulemodule.readRuleFile();
		rulemodule.printData();
		List lt = rulemodule.getDataByType("taobao.com", "photo");
		System.out.println(lt.get(0) + " ");
		rulemodule.getDomainByList("www.taobao.com/nkj");
		System.out.println(rulemodule.getTypeList("www.taobao.com/nkj", "table"));
	}
	
}
