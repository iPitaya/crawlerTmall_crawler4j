package cn.b2b.crawler.startfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class good{
	private Vector vc = new Vector() ;
	public void setdata(String str){
		vc.add(str);
	}
	
	public void readdata(){
		for ( int i = 0; i < vc.size() ; i++){
			System.out.println(vc.get(i));
		}
	}
}

public class test {
	private good gg;
	public void listdata(String str){
		Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher matcher = pattern.matcher(str);
        String tempstr;
        
        while(matcher.find()){
        	String str1 = matcher.group(1);
            System.out.println(str1);
            String[] nums = str1.split(",");
            if (nums.length > 2){
            	System.out.println("error");
            }
            
            for (int i = Integer.valueOf(nums[0]);i <= Integer.valueOf(nums[1]);i++){
            	//System.out.println(String.valueOf(i));
            	String num = String.valueOf(i);
            	tempstr = str.replaceAll(str1,num);
                tempstr = tempstr.replaceAll("\\(", "").replaceAll("\\)", "");
                System.out.println(tempstr);
                
            }
            
            
        }
	}
	
	public void keyworddata(String str,String filename){
		BufferedReader  reader = null;
		String tempstr = null;
		try {
			reader = new BufferedReader(new FileReader(new File(filename)));
			String line = null;
			
			Pattern pattern1 = Pattern.compile("\\[(.*?)\\]");
	        Matcher matcher1 = pattern1.matcher(str);
	        while(matcher1.find()){
	        	String str2 = matcher1.group(1);
	            //System.out.println(str2);
	            while ( (line = reader.readLine()) != null ){
					tempstr = str.replaceAll(str2, line);
					tempstr = tempstr.replaceAll("\\[", "").replaceAll("\\]", "");
					System.out.println(tempstr);
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
	
	public void koko(good gd){
		gg = gd;
		gg.setdata("ssss");
		gg.setdata("llll");
	}
	
	public void start(){
		BufferedReader  reader = null;
		String filename = "conf/startfile/start.txt";
		try {
			reader = new BufferedReader(new FileReader(new File(filename)));
			String line = null;
			while ( (line = reader.readLine()) != null ){
				//System.out.println(line);
				selecttype(line);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void selecttype(String str){
		String[] strs = str.split("\\|");
		//System.out.println(strs[0]);
		if (strs.length < 2){
			System.out.println("格式错误: " +  str );
		}
		
		if (strs[0].equals("url")){
			
		}
		
		if (strs[0].equals("list")){
			listdata(strs[1]);
		}
		
		if (strs[0].equals("keytype")){
			keyworddata(strs[1], strs[2]);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		test ts = new test();
		ts.start();
		
		/*
		//String str = "list|http://www.abc.html/sss/dd(1,100)/dd[key]";
		String str = "keytype|http://www.abc.html/sss/dd(1,100)/dd[key]|conf/startfile/key.txt";
		String[] strs = str.split("\\|");
		System.out.println(strs[0]);
		if (strs.length < 2){
			System.out.println("格式错误: " +  str );
		}
		
		if (strs[0].equals("url")){
			
		}
		
		if (strs[0].equals("list")){
			ts.listdata(strs[1]);
		}
		
		if (strs[0].equals("keytype")){
			ts.keyworddata(strs[1], strs[2]);
		}
		
		
		
		good gd = new good();
		
		ts.koko(gd);
		
		gd.readdata();
		
		*/

	}

}
