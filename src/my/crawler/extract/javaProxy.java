package my.crawler.extract;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class javaProxy {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub
		Socket socket = new Socket("118.85.208.193", 80);  
		//Socket socket = new Socket("58.83.194.110",80);
		
		socket.getOutputStream().write(new String("GET  http://china.makepolo.com  HTTP/1.0\r\n\r\n").getBytes());  

		byte[] bs = new byte[1024];  
		InputStream is = socket.getInputStream(); 
		
		int i=0;  
		i = is.read(bs);
		 while (i> 0) {  
			
		    System.out.println(new String(bs, 0, i));  
		   
		    System.out.println(i);
		    i=0;
		    i=is.read(bs);
		   
		}  
		
//		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
//		
//		String line;
//		while ((line=bufferedReader.readLine())!= null) {
//			
//			System.out.println(line);
//			System.out.println(123);
//			line=null;
//		}
		
		is.close();  
		socket.close();
	}

}
