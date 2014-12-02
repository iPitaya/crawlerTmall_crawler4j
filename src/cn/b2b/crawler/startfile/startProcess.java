package cn.b2b.crawler.startfile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.MyCrawlController;

public class startProcess {
	
	private MyCrawlController controller;
	
	
	
	public MyCrawlController getController() {
		return controller;
	}

	public void setController(MyCrawlController controller) {
		this.controller = controller;
	}

	public startProcess(){
		
	}
	
	public void processUrl(String url){
		if(controller != null){
			controller.addSeed(url);
		}else{
			System.out.println("controller 没有初始化");
		}
	}
	//处理列表页
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
                processUrl(tempstr);
            }
            
            
        }
	}
	//处理按照关键词搜索过滤的网页
	public void keyworddata(String str,String filename,String flag ){
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
	            	if (line.trim().equals("")){
	            		continue;
	            	}
	            	line = URLEncoder.encode(line, "gbk");
	            	if (str.contains("ch.gongchang.com")){
	            		line = line.replaceAll("%", "");
	            		line = line.toLowerCase();
	            	}
					tempstr = str.replaceAll(str2, line);
					tempstr = tempstr.replaceAll("\\[", "").replaceAll("\\]", "");
					if (flag.equals("keyword")){
						System.out.println(tempstr);
						processUrl(tempstr);
					}else{
						
						listdata(tempstr);
					}
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
	//处理普通url 种子url
	public void urldata(String str){
		System.out.println("urlseed "+str);
		processUrl(str);
	}
	//关键字和列表页同时存在
	public void keywordlistdata(String str,String filename){
		keyworddata(str,filename,"keywordlist");
	}
	
	public void start(){
		BufferedReader  reader = null;
		String filename = "conf/startfile/start.txt";
		try {
			reader = new BufferedReader(new FileReader(new File(filename)));
			String line = null;
			while ( (line = reader.readLine()) != null ){
				
				if (!line.trim().equals("")){
					selecttype(line);
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
	//判定是哪种类型的url
	public void selecttype(String str){
		String[] strs = str.split("\\|");
		//System.out.println(strs[0]);
		if (strs.length < 2){
			System.out.println("格式错误: " +  str );
			return;
		}
		
		if (strs[0].equals("url")){
			urldata(strs[1]);
		}
		
		if (strs[0].equals("list")){
			listdata(strs[1]);
		}
		
		if (strs[0].equals("keytype")){
			if (strs.length <= 2){
				System.out.println("格式错误: " +  str );
				return;
			}else{
				keyworddata(strs[1], strs[2],"keyword");
			}
		}
		
		if (strs[0].equals("listkeytype")){
			keywordlistdata(strs[1], strs[2]);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		startProcess ts = new startProcess();
		ts.start();
	}

}
