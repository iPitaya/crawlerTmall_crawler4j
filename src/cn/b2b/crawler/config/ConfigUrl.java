package cn.b2b.crawler.config;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class ConfigUrl {
	
		private Map<String,String>  urlmap ;
		private String configpath = "conf/configurl.xml";
		
		
		
		public Map<String, String> getUrlmap() {
			return urlmap;
		}

		public void setUrlmap(Map<String, String> urlmap) {
			this.urlmap = urlmap;
		}
		
		public String getAddUrl(String keyword){
			Iterator it = urlmap.entrySet().iterator();
			while (it.hasNext()){
				Map.Entry entry = (Map.Entry) it.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				
				//System.out.println(key.toString() +"/"+ value.toString());
				if (keyword.endsWith(key.toString())){
					return  keyword + "/" + value.toString();
				}
			}
			return urlmap.get(keyword);
		}

		public ConfigUrl(){
			urlmap = new HashMap<String,String>();
			init();
		}
		
		public void  init(){
			try{
				File f = new File(configpath);
				DocumentBuilderFactory factory = DocumentBuilderFactory
						.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document doc = builder.parse(f);
				
				//NodeList nl = doc.getElementsByTagName("purchase-order");
				//this.filterfile = doc.getElementsByTagName("filterfile").item(0).getFirstChild().getNodeValue();
				
				
				
				NodeList nl = doc.getElementsByTagName("weburl");
				
				for (int i=0; i < nl.getLength(); i++){
					String first = nl.item(0).getFirstChild().getNodeValue();

					
					String[] rules = first.split("\\|");
					if ( rules.length == 2){

						urlmap.put(rules[0], rules[1]);
					}
					
				}
				
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		public static void main(String[] args){
			ConfigUrl cu = new ConfigUrl();
			String result = cu.getAddUrl("12313221.cn.gongchang.com");
			String a = "12313221.cn.gongchang.com";
			String b = "gongchang.com";
			System.out.println(a.endsWith(b));
			System.out.println(result);
			/*
			Map<String,String> maprules = (Map<String,String>)cu.getUrlmap();
			Iterator it = maprules.entrySet().iterator();
			while (it.hasNext()){
				Map.Entry entry = (Map.Entry) it.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				
				System.out.println(key.toString() +"/"+ value.toString());
			}
			*/
		}
			
		
}
