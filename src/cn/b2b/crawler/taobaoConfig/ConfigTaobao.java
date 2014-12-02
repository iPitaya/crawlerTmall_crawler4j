package cn.b2b.crawler.taobaoConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class ConfigTaobao {

	private String  filename = "conf/taobaoconf/usermsg.xml";
	private String isUse ;
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public List getUselist() {
		return uselist;
	}
	public void setUselist(List uselist) {
		this.uselist = uselist;
	}
	private List  uselist ;
	
	public ConfigTaobao(){
		init();
	}
	
	public void init(){
		try {
			File f = new File(filename);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);
			//NodeList nl = doc.getElementsByTagName("purchase-order");
			this.isUse = doc.getElementsByTagName("isUse").item(0).getFirstChild().getNodeValue();
			System.out.println(doc.getElementsByTagName("accont").getLength());
			uselist = new ArrayList();
			for (int i=0; i < doc.getElementsByTagName("accont").getLength();i++){
				this.uselist.add(doc.getElementsByTagName("accont").item(i).getFirstChild().getNodeValue());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String SelectAccont(){
		
		int sum = (int)(Math.random()*uselist.size());
		return (String)uselist.get(sum);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConfigTaobao ctb = new ConfigTaobao();
		ctb.init();
		System.out.println(ctb.SelectAccont());
		System.out.println(ctb.getIsUse());
	}

}
