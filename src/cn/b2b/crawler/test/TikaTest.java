package cn.b2b.crawler.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import edu.uci.ics.crawler4j.parser.ExtractedUrlAnchorPair;
import edu.uci.ics.crawler4j.parser.HtmlContentHandler;

public class TikaTest {
	 public static String PATH = "data/test/js.html";
	 public static String OUTPATH = PATH + ".OUT";
	/**
	 * @param args
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 * @throws SAXException 
	 */
	public static void main(String[] args) throws TikaException, ClientProtocolException, IOException, SAXException {
		// TODO Auto-generated method stub
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet("http://seven-princess.taobao.com/widgetAsync.htm?ids=6616555972%2C6615964246%2C6615964251%2C6615964248&path=%2Fshop%2Fview_shop.htm&callback=callbackGetMods6616555972&site_instance_id=100732288&v=1");
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		String html = EntityUtils.toString(entity, "UTF-8");
		System.out.println(html);
		Tika tika = new Tika();
		//Reader reader = tika.parse(new URL("http://www.taobao.com"));
		Parser parser = new HtmlParser();  
		InputStream iStream = new BufferedInputStream(new FileInputStream(  
                new File(PATH)));  
        OutputStream oStream = new BufferedOutputStream(new FileOutputStream(  
                new File(OUTPATH)));  
        ByteArrayInputStream stream = new ByteArrayInputStream(html.getBytes());
        ContentHandler iHandler = new BodyContentHandler(oStream); 
        ParseContext parseContext = new ParseContext();
        HtmlContentHandler contentHandler = new HtmlContentHandler();
        Metadata meta = new Metadata();  
        meta.add(Metadata.CONTENT_ENCODING, "utf-8");  
        parser.parse(stream, contentHandler, meta, parseContext);
        for (ExtractedUrlAnchorPair urlAnchorPair : contentHandler.getOutgoingUrls()) {
			String href = urlAnchorPair.getHref();
			href = href.trim();
			System.out.println(href.replaceAll("\"", "").replace("\\", ""));
        }
		
	}

}
