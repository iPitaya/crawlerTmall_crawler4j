package cn.b2b.crawler.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class test {

	private String CITYFILE = "conf/city.txt";
	private String PROVINCEFILE = "conf/province.txt";
	private String SIZEFILE = "conf/companysize.txt";
	private String MANAGEFILE = "conf/managemodel.txt";
	private Map citymap = new HashMap();
	private Map provincemap = new HashMap();
	private Map sizemap = new HashMap();
	private Map managemap = new HashMap();

	public test() {
		initCity(citymap, CITYFILE);
		initCity(provincemap, PROVINCEFILE);
		initSize(sizemap,SIZEFILE);
		initManage(managemap,MANAGEFILE);
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
				// System.out.println(city);
				// cityname = line.split("\t")[1].replaceAll("ï¿½ï¿½",
				// "").replaceAll("ï¿½ï¿½", "");
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
				// cityname = line.split("\t")[1].replaceAll("ï¿½ï¿½",
				// "").replaceAll("ï¿½ï¿½", "");
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
				 System.out.println(cityname);
				// cityname = line.split("\t")[1].replaceAll("ï¿½ï¿½",
				// "").replaceAll("ï¿½ï¿½", "");
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

	public void getCityId(String msg, Map mapdata) {
		Iterator iter = mapdata.entrySet().iterator();
		boolean  flag = false;
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			Object key = entry.getKey();
			Object value = entry.getValue();
			if (msg.contains(key.toString())) {
				System.out.println(value);
				flag = true;
				break;
			}
		}
		
		if ( flag == false){
			
		}
	}
	


	public static void main(String[] args) {
		test ts = new test();
		String msg = "ÖØÇìÖØÇìÊÐÓÀ´¨ÇøÖØÇìÓÀ´¨ÐÂÇø·ï»Ë³Ç²é¿´µØÍ¼";
		System.out.println(msg.contains("±±¾©"));
		ts.getCityId(msg, ts.getProvincemap());
	}
}
