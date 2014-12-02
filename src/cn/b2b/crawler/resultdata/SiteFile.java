package cn.b2b.crawler.resultdata;

public class SiteFile {
	private String domain ;
	private String siteurl;
	private String site;
	
	public SiteFile(){
		domain = "";
		siteurl = "";
		site = "";
	}
	public void cleaner(){
		domain = "";
		siteurl = "";
		site = "";
	}
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getSiteurl() {
		return siteurl;
	}

	public void setSiteurl(String siteurl) {
		this.siteurl = siteurl;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}
	
}
