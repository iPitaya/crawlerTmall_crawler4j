package my.crawler.loadimage;

import java.io.File;


import org.apache.http.HttpStatus;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.examples.imagecrawler.Cryptography;
import edu.uci.ics.crawler4j.fetcher.PageFetchResult;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.parser.Parser;
import edu.uci.ics.crawler4j.url.WebURL;
import edu.uci.ics.crawler4j.util.IO;

public class DownloadImage {
	private Parser parser;
	private PageFetcher pageFetcher;
	private static File storageFolder;

	public DownloadImage(String storageFolderName) {
		CrawlConfig config = new CrawlConfig();
		//下载图片需要设置的参数
		config.setIncludeBinaryContentInCrawling(true);
		parser = new Parser(config);
		pageFetcher = new PageFetcher(config);
		storageFolder = new File(storageFolderName);
		if (!storageFolder.exists()) {
			storageFolder.mkdirs();
		}
	}

	private Page download(String url) {
		WebURL curURL = new WebURL();
		curURL.setURL(url);
		PageFetchResult fetchResult = null;
		try {
			fetchResult = pageFetcher.fetchHeader(curURL);
			if (fetchResult.getStatusCode() == HttpStatus.SC_OK) {
				try {
					Page page = new Page(curURL);
					fetchResult.fetchContent(page);
					if (parser.parse(page, curURL.getURL())) {
						return page;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} finally {
			if (fetchResult != null)
			{
				fetchResult.discardContentIfNotConsumed();
			}			
		}
		return null;
	}

	public void processUrl(String url) {
		//System.out.println("Processing: " + url);

		Page page = download(url);
		System.out.println(page);
		if (page != null) {
			//System.out.println(page.getContentData().length);
			// get a unique name for storing this image
			String extension = url.substring(url.lastIndexOf("."));
			String hashedName = Cryptography.MD5(url) + extension;

			// store image
			IO.writeBytesToFile(page.getContentData(), storageFolder.getAbsolutePath() + "/" + hashedName);
			System.out.println(hashedName);

			System.out.println("Stored: " + url);
			
		} else {
			System.out.println("Couldn't fetch the content of the page.");
		}
		System.out.println("==============");
	}

	public static void main(String[] args) {
		DownloadImage downloader = new DownloadImage("data/crawl/image/test");
		//downloader.processUrl("http://en.wikipedia.org/wiki/Main_Page/");
		//downloader.processUrl("http://www.yahoo.com/");
		//downloader.processUrl("http://img6.makepolo.net/images/formals/medium_img/product/561/501/medium_8319f03d1e42965597a6aafaaaf8510f.jpg");
		downloader.processUrl("http://gw2.alicdn.com/bao/uploaded/i1/T1sSk3FbRiXXXXXXXX_!!0-item_pic.jpg_b.jpg");
		System.out.println("wan..................");
		System.exit(0);
		
	}
}
