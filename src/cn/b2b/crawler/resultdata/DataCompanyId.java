package cn.b2b.crawler.resultdata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.mongodb.BasicDBObject;

public class DataCompanyId {
	public  int  companyid ;
	public  String filename = "conf/resultdata/idnum.txt";
	
	public DataCompanyId(){
		readdata();
	}
	
	public int getCompanyid() {
		return companyid;
	}

	public void setCompanyid(int companyid) {
		this.companyid = companyid;
	}
	
	//向mginfo中添加 companyid 和 docnum 并且把companyid加1
	public BasicDBObject addCompanyId(BasicDBObject mginfo){
		companyid += 1;
		writedata(companyid);
		mginfo.put("companyid", companyid);
		mginfo.put("docnum", companyid);
		return mginfo;
	}
	
	//从文件中读取当前编号数字
	public int readdata(){
		FileReader fr;
		try {
			fr = new FileReader(filename);
			BufferedReader filein = new BufferedReader(fr);
			String b;
			b = filein.readLine();
			companyid = Integer.valueOf(b);
			System.out.println(b);
			/*
			while ((b = filein.readLine()) != null) {
			System.out.println(b);
			}
			*/
			fr.close();
			filein.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return companyid;
	}
	//向文件中写入当前数字
	public  void  writedata(int data){
		FileWriter fw;
		try {
			fw = new FileWriter(filename);
			BufferedWriter bw=new BufferedWriter(fw);
			bw.write(String.valueOf(data));
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DataCompanyId cid = new DataCompanyId();
		int num = 0;
		num = cid.readdata();
		cid.writedata(num+1);
	}

}
