package cn.b2b.crawler.config;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class ConfigStart {
	private String configpath = "conf/configstart.xml";
	private String storagefolder;
	private String numberofCrawlers;
	private String daleytime;
	private String depthcrawl;
	private String pagesfetchnum;
	private String resumablecrawling;
	private String listwebdepth;
	private String contwebdepth;
	
	
	
	
	public int getListwebdepth() {
		return Integer.valueOf(listwebdepth);
	}

	public void setListwebdepth(String listwebdepth) {
		this.listwebdepth = listwebdepth;
	}

	public int getContwebdepth() {
		return Integer.valueOf(contwebdepth);
	}

	public void setContwebdepth(String contwebdepth) {
		this.contwebdepth = contwebdepth;
	}

	public String getStoragefolder() {
		return storagefolder;
	}

	public void setStoragefolder(String storagefolder) {
		this.storagefolder = storagefolder;
	}

	public int getNumberofCrawlers() {
		return Integer.valueOf(numberofCrawlers);
	}

	public void setNumberofCrawlers(String numberofCrawlers) {
		this.numberofCrawlers = numberofCrawlers;
	}

	public int getDaleytime() {
		return Integer.valueOf(daleytime);
	}

	public void setDaleytime(String daleytime) {
		this.daleytime = daleytime;
	}

	public int getDepthcrawl() {
		return Integer.valueOf(depthcrawl);
	}

	public void setDepthcrawl(String depthcrawl) {
		this.depthcrawl = depthcrawl;
	}

	public int getPagesfetchnum() {
		return Integer.valueOf(pagesfetchnum);
	}

	public void setPagesfetchnum(String pagesfetchnum) {
		this.pagesfetchnum = pagesfetchnum;
	}

	public boolean getResumablecrawling() {
		boolean flag ;
		if (resumablecrawling.equals("true")){
			flag = true;
		}else{
			flag = false;
		}
		return flag;
	}

	public void setResumablecrawling(String resumablecrawling) {
		this.resumablecrawling = resumablecrawling;
	}

	public ConfigStart(){
		init();
	}
	
	public void  init(){
		try{
			File f = new File(configpath);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(f);
			
			
			this.storagefolder = doc.getElementsByTagName("crawlStorageFolder").item(0).getFirstChild().getNodeValue();
			this.numberofCrawlers = doc.getElementsByTagName("numberOfCrawlers").item(0).getFirstChild().getNodeValue();
			this.daleytime = doc.getElementsByTagName("daleytime").item(0).getFirstChild().getNodeValue();
			this.depthcrawl = doc.getElementsByTagName("DepthOfCrawling").item(0).getFirstChild().getNodeValue();
		
			this.pagesfetchnum = doc.getElementsByTagName("MaxPagesToFetch").item(0).getFirstChild().getNodeValue();
			this.resumablecrawling = doc.getElementsByTagName("ResumableCrawling").item(0).getFirstChild().getNodeValue();
			
			this.listwebdepth = doc.getElementsByTagName("ListWebDepth").item(0).getFirstChild().getNodeValue();
			this.contwebdepth = doc.getElementsByTagName("ContWebDepth").item(0).getFirstChild().getNodeValue();
						
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void print(){
		System.out.println(getStoragefolder());
		System.out.println(getNumberofCrawlers());
		System.out.println(getDaleytime());
		System.out.println(getDepthcrawl());
		System.out.println(getPagesfetchnum());
		System.out.println(getResumablecrawling());
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ConfigStart cfs = new ConfigStart();
		cfs.print();
	}

}
