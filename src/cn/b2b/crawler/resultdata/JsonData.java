package cn.b2b.crawler.resultdata;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.mongodb.BasicDBObject;

public class JsonData {
	
	private Map<String,String> datamap = new HashMap<String,String>();
	private BasicDBObject mginfo = new BasicDBObject();
	private DataFormat dataformat =  new DataFormat();
	//private DataCompanyId  datacompanyid = new DataCompanyId();
	
	
	
	public BasicDBObject getMginfo() {
		return mginfo;
	}

	public void setMginfo(BasicDBObject mginfo) {
		this.mginfo = mginfo;
	}
	//把resultstr转换成 BasicDBObject
	public BasicDBObject getMapData(String resultstr) throws JSONException{
		mginfo.clear();
		JSONObject json = new JSONObject(resultstr);
		Iterator<String> keys=json.keys(); 
		JSONObject jo=null;  
        Object o;  
        String key;  
        String value;
        while(keys.hasNext()){  
            key=keys.next(); 
            if (key.equals("city")||key.equals("province")){
            	o=json.get("address");
            }else{
            	o=json.get(key);
            }
            
            /*
            if(o instanceof JSONObject){  
                jo=(JSONObject)o;       
            }else{  
                System.out.println(o.toString().trim().replace('\r', ' ').replace('\t', ' '));
            }  
            */
            value = o.toString();
            //System.out.println(value);
            value = dataformat.getResult(key, value);
            value = value.trim();
            
            mginfo = addData(mginfo,key,value);
        }
        
        /*
        int num = datacompanyid.getCompanyid();
        datacompanyid.setCompanyid(num+1);
        datacompanyid.writedata(num + 1);
        mginfo.put("companyid", num + 1);
        mginfo.put("docnum", num + 1);
        */
        getLocation(mginfo);
        return mginfo;
        
        
	}
	
	public  void  getLocation(BasicDBObject mginfo){
		String address = mginfo.getString("address");
		address = address.replaceAll(" ", "");
		try {
			address = URLEncoder.encode(address, "gbk");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		HttpClient client = new DefaultHttpClient();
		String url = "http://api.map.baidu.com/geocoder?address="+address+"&output=json&key=kitDLOj4a6Gg5VdF8WXT512C";
		HttpGet post = new HttpGet(url);
		// post.addHeader("user-agent",
		// "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36");
		HttpResponse response;
		try {
			response = client.execute(post);
			HttpEntity entity = response.getEntity();
			String content = EntityUtils.toString(entity, "UTF-8");
			//System.out.println("Response content: " + content);
			client.getConnectionManager().shutdown();
			JSONObject jsonObj = new JSONObject(content);
			
			System.out.println(jsonObj.has("result"));
			if (jsonObj.getString("status").equals("OK") && jsonObj.has("result")) {
				if (!jsonObj.getString("result").equals("[]")){
				JSONObject result = jsonObj.getJSONObject("result");
				JSONObject location = result.getJSONObject("location");
				System.out.println(location.getString("lng"));
				System.out.println(location.getString("lat"));
				mginfo.put("poslong", location.getString("lng"));
				mginfo.put("poslati", location.getString("lat"));
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//拼成BasicDBObject数据便于插入mongodb
	public BasicDBObject addData(BasicDBObject info, String key, String value) {
		value = dataformat.getTimeData(key, value);
		if ( dataformat.isChangeInt(key)){
			info.put(key, Integer.valueOf(value));
			//System.out.println(key + " : " + info.get(key) + "basicobject");
			//System.out.println(info.get(key).equals(""));
		}else{
			info.put(key, value);
			//System.out.println(key + " : " + info.get(key) + "basicobject");
			//System.out.println(info.get(key).equals(""));
			//info.put("aaa", "ddd" + key);
		}
		return info;
	}
	
	public static void main(String[] args) {
		JsonData jsondata = new JsonData();
		
	}
	
}
