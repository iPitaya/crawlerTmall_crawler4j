package cn.b2b.crawler.resultdata;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;


public class DataSiteMsg {
	
	private final String lineflagstr = "!!";
	private final String keysplit = ":";
	private List list;
	
	
	
	public List getList() {
		return list;
	}

	public void setList(List list) {
		this.list = list;
	}

	public DataSiteMsg(){
		list = new ArrayList();
		readDataFile();
	}
	
	public void readDataFile(){
		SiteFile sitefile = new SiteFile();
		try {
			BufferedReader br = new BufferedReader(new FileReader("conf/resultdata/sitemsg.txt"));
			String line;
			while ((line = br.readLine()) != null){
				
				if ( !lineflagstr.equals(line) ){
					int index = line.indexOf(keysplit);
					if(index > 0){
						String key = line.substring(0, index);
						String value = line.substring(index+1);
						
						if (key.equals("domain")){
							sitefile.setDomain(value);
						}
						
						if (key.equals("siteurl")){
							sitefile.setSiteurl(value);
						}
						
						if (key.equals("site")){
							sitefile.setSite(value);
						}
						
					}
				}else{
					if (!sitefile.getDomain().equals("") && !sitefile.getSiteurl().equals("") && !sitefile.getSite().equals("")){
					list.add(sitefile);
					}
					sitefile = new SiteFile();
					//sitefile.cleaner();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public SiteFile getSiteFromUrl(String url){
		SiteFile sf = null;
		for (int i = 0;i < list.size(); i++){
			if (url.contains(((SiteFile)list.get(i)).getDomain())){
				sf = ((SiteFile)list.get(i));
				break;
			}
		}
		return sf;
	}
	
	public BasicDBObject getSiteMsg(BasicDBObject mginfo,String url){
		
		SiteFile sf = null;
		sf = getSiteFromUrl(url);
		if (sf != null){
			mginfo.put("siteurl", sf.getSiteurl());
			mginfo.put("site", sf.getSite());
			
		}
		return mginfo;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataSiteMsg dsm = new DataSiteMsg();
		dsm.readDataFile();
		List ss = dsm.getList();
		System.out.println(ss.size());
		if (dsm.getSiteFromUrl("http://www.1688.com/sss/fggg/ss") != null){
			System.out.println(dsm.getSiteFromUrl("http://www.1688.com/sss/fggg/ss").getDomain());
		}
	}

}
