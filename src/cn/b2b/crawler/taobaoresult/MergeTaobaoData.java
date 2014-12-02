package cn.b2b.crawler.taobaoresult;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import cn.b2b.crawler.config.ConfigXML;
import cn.b2b.crawler.db.ManageMysql;

public class MergeTaobaoData {
	private ManageMysql mgsql = null;
	private ConfigXML config = null;

	public MergeTaobaoData() {
		config = new ConfigXML();
		mgsql = new ManageMysql(config);
		mgsql.init();
	}
	
	public void dataChange(){
		String sql = "delete from taobaodata  where url like '%htm?n=1&sid%'  and  isflag = 0";
		mgsql.writeData(sql);
		sql = "UPDATE taobaodata  set url = REPLACE(url,'htm?sid','htm?n=1&sid') where url like '%htm?sid%' and isflag = 0";
		mgsql.writeData(sql);
	}

	public void mergeData() {
		dataChange();
		String sql = "SELECT * from taobaodata  where url like '%com/i%' and isflag = 0";
		ResultSet rs = mgsql.readData(sql);
		try {
			while (rs.next()) {
				String url = rs.getString("url");
				System.out.println(url);
				String regstr = "(\\d)+";
				Pattern p = Pattern.compile(regstr);
				Matcher m = p.matcher(url);
				if (m.find()) {
					String id = m.group();
					String urlstr = "http://a.m.taobao.com/da" + id +".htm";
					String jsondata = getDetailData(id);
					System.out.println(urlstr);
					System.out.println(jsondata);
					sql = "update taobaodata set isflag = 1 where id = " + rs.getString("id");
					mgsql.writeData(sql);
					if (!jsondata.equals("{}")){
						writeResultData(rs.getString("jsondata"),url,jsondata,rs.getString("onlyflag"));
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeResultData(String data,String url,String damsg,String onlyflag){
		String sql = "insert into taobaoresult(imsg,url,damsg,onlyflag) values('"+ data +"','"+ url +"','"+ damsg +"','"+onlyflag+"')";
		mgsql.writeData(sql);
	}

	public String getDetailData(String idstr) {
		String sql = "select * from taobaodata  where isflag = 0 and url like '%da" + idstr
				+ "%' ORDER BY url";
		ResultSet rs = mgsql.readData(sql);
		String result = null;
		Map<String, String> mapdata = new HashMap<String, String>();
		try {
			while (rs.next()) {
				System.out.println(rs.getString("jsondata"));
				JSONObject jsonObj = new JSONObject(rs.getString("jsondata"));
				Iterator it = jsonObj.keys();
				while (it.hasNext()) {
					Object key = it.next();
					// System.out.println(key.toString());
					if (mapdata.containsKey(key)) {
						mapdata.put(
								key.toString(),
								mapdata.get(key)
										+ jsonObj.getString(key.toString()));
					} else {
						mapdata.put(key.toString(),
								jsonObj.getString(key.toString()));
					}
				}
				String upsql = "update taobaodata set isflag = 1 where id = " + rs.getString("id");
				mgsql.writeData(upsql);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//String urlstr = "http://a.m.taobao.com/da" + idstr +".htm";
		result = (new JSONObject(mapdata)).toString();
		/*
		if (!result.equals("{}")){
			writeResultData(result,urlstr);
		}
		*/
		return result;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MergeTaobaoData mtd = new MergeTaobaoData();
		mtd.mergeData();
	}

}
