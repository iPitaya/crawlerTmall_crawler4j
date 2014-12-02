package cn.b2b.crawler.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class StoreFile {
	
	public  String filename = "data/html/";
	
	public  void  writedata(String data,int id){
		filename = filename + id +".html";
		FileWriter fw;
		try {
			fw = new FileWriter(filename);
			BufferedWriter bw=new BufferedWriter(fw);
			bw.write(data);
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

	}

}
