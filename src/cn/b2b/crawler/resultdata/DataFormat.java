package cn.b2b.crawler.resultdata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.b2b.crawler.config.test;

public class DataFormat {

	private String CITYFILE = "conf/city.txt";
	private String PROVINCEFILE = "conf/province.txt";
	private String SIZEFILE = "conf/companysize.txt";
	private String MANAGEFILE = "conf/managemodel.txt";
	private Map citymap = new HashMap();
	private Map provincemap = new HashMap();
	private Map sizemap = new HashMap();
	private Map managemap = new HashMap();

	public DataFormat() {
		initCity(citymap, CITYFILE);
		initCity(provincemap, PROVINCEFILE);
		initSize(sizemap, SIZEFILE);
		initManage(managemap, MANAGEFILE);
	}

	public Map getCitymap() {
		return citymap;
	}

	public void setCitymap(Map citymap) {
		this.citymap = citymap;
	}

	public Map getProvincemap() {
		return provincemap;
	}

	public void setProvincemap(Map provincemap) {
		this.provincemap = provincemap;
	}

	public void initCity(Map mapdata, String filename) {

		BufferedReader reader = null;
		String cityname;
		try {
			reader = new BufferedReader(new FileReader(new File(filename)));
			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				cityname = line.split("\t")[1];
				if (cityname.length() > 2) {
					cityname = cityname.substring(0, cityname.length() - 1);
				}
				// System.out.println(city.length());
				// System.out.println(cityname);
				// cityname = line.split("\t")[1].replaceAll("锟斤拷",
				// "").replaceAll("锟斤拷", "");
				mapdata.put(cityname, line.split("\t")[0]);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initSize(Map mapdata, String filename) {

		BufferedReader reader = null;
		String cityname;
		try {
			reader = new BufferedReader(new FileReader(new File(filename)));
			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				cityname = line.split("\t")[1];
				if (cityname.length() > 2) {
					cityname = cityname.substring(0, cityname.length() - 1);
				}
				// System.out.println(city.length());
				// System.out.println(city);
				// cityname = line.split("\t")[1].replaceAll("锟斤拷",
				// "").replaceAll("锟斤拷", "");
				mapdata.put(cityname, line.split("\t")[0]);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void initManage(Map mapdata, String filename) {

		BufferedReader reader = null;
		String cityname;
		try {
			reader = new BufferedReader(new FileReader(new File(filename)));
			String line = null;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				cityname = line.split("\t")[1];
				if (cityname.length() > 2) {
					cityname = cityname.substring(0, cityname.length() - 1);
				}
				// System.out.println(city.length());
				// System.out.println(cityname);
				// cityname = line.split("\t")[1].replaceAll("锟斤拷",
				// "").replaceAll("锟斤拷", "");
				mapdata.put(cityname, line.split("\t")[0]);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getCityId(String msg, Map mapdata) {
		Iterator iter = mapdata.entrySet().iterator();
		boolean flag = false;
		String id = "";
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			if (msg.contains(key.toString())) {
				id = value.toString();

				flag = true;
				break;
			}
		}

		return id;
	}

	public String getResult(String key, String msg) {
		String data = msg;
		if (!key.equals("content") && !key.equals("title")){
			msg = getProcessStr(msg);
		}else{
			msg = msg.trim();
		}

		if (key.equals("title")){
			msg = titleStr(msg);

		}
		
		if (key.equals("url")){
        	msg = msg.toLowerCase();
        }
		data = msg;
		// System.out.println(msg + "dddddddddddddddddddddddddddddddddd" + key);
		switch (key) {
		case "city":
			// System.out.println(msg + "dddddddddddddddddddddddddddddddddd");
			data = getCityId(msg, getCitymap());
			// System.out.println(data + "dddddddddddddddddddddddddddddddddd");
			if (data == "") {
				data = String.valueOf(0);
			}
			break;
		case "province":
			data = getCityId(msg, getProvincemap());
			if (data == "") {
				data = String.valueOf(-1);
			}
			break;
		case "size":
			data = getCityId(msg, sizemap);
			if (data == "") {
				data = String.valueOf(6);
			}
			break;
		case "operation":
			data = getCityId(msg, managemap);
			if (data == "") {
				data = String.valueOf(6);
			}
			break;
		case "del":
			data = String.valueOf(0);
			break;
		case "memlevel":
			data = String.valueOf(0);
			break;
		case "state":
			data = String.valueOf(1);
			break;
		case "poslong":
			data = String.valueOf(0);
			break;
		case "poslati":
			data = String.valueOf(0);
			break;
		case "license":
			data = String.valueOf(0);
			break;
		}

		return data;
	}

	public boolean isChangeInt(String key) {
		boolean flag = false;

		switch (key) {
		case "city":
		case "province":
		case "size":
		case "operation":
		case "del":
		case "memlevel":
		case "state":
		case "poslong":
		case "poslati":
		case "license":
			flag = true;
			break;
		}
		return flag;
	}

	public String getTimeData(String key, String value) {
		if (key.equals("rdate") && value.equals("")) {
			value = "19700101 08:00:00";
		}

		if (key.equals("sdate") && value.equals("")) {
			value = "19700101 08:00:00";
		}
		if (key.equals("edate") && value.equals("")) {
			value = "19700101 08:00:00";
		}
		if (key.equals("udate") && value.equals("")) {
			value = "19700101 08:00:00";
		}

		if (key.equals("address")) {
			value = value.replaceAll("［已认证］查看地图", "");
			value = value.replaceAll("查看地图>>", "");
		}

		return value;
	}

	// 字符串处理
	public String getProcessStr(String msg) {
		msg = msg.replace('\n', ' ').replace('\r', ' ').replaceAll("\\?", " ")
				.replaceAll("&nbsp;", " ").replaceAll("&nbsp", " ")
				.replaceAll("&gt;", ">").trim();

		if (msg.contains("：")) {
			if (msg.split("：").length > 1) {
				msg = msg.split("：")[1];
			} else {
				msg = "";
			}
		}

		if (msg.contains(":") && !msg.startsWith("http:")) {
			if (msg.split("：").length > 1) {
				msg = msg.split(":")[1];
			} else {
				msg = "";
			}
		}

		if (msg.contains("(")) {
			msg = msg.split("\\(")[0];
		}

		msg = msg.replaceAll("\"", " ");
		msg = msg.replaceAll("'", " ");

		return msg;
	}
	
	public String titleStr(String msg){
		if (msg.contains("：")) {
			if (msg.split("：").length > 1) {
				msg = msg.split("：")[1];
			} else {
				msg = "";
			}
		}

		if (msg.contains(":") && !msg.startsWith("http:")) {
			if (msg.split("：").length > 1) {
				msg = msg.split(":")[1];
			} else {
				msg = "";
			}
		}
		return msg;
	} 

	public static void main(String[] args) {
		DataFormat ts = new DataFormat();
		String msg = "重庆市永川区永川新区凤凰城查看地图";
		System.out.println(msg.contains("重庆"));
		System.out.println(ts.getCityId(msg, ts.getCitymap()));
	}
}
