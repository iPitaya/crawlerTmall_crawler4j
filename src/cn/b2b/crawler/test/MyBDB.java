package cn.b2b.crawler.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;


public class MyBDB {
	
	protected Database urlsDB = null;
	protected Environment env;

	protected boolean resumable;
	protected final Object mutex = new Object();
	
	public void start() throws Exception{
		resumable = true;
		EnvironmentConfig envConfig = new EnvironmentConfig();
		envConfig.setAllowCreate(true);
		envConfig.setTransactional(resumable);
		envConfig.setLocking(resumable);
		
		File envHome = new File( "data/crawl/frontier");
		if (!envHome.exists()) {
			if (!envHome.mkdir()) {
				throw new Exception("Couldn't create this folder: " + envHome.getAbsolutePath());
			}
		}
		
		env = new Environment(envHome, envConfig);
		DatabaseConfig dbConfig = new DatabaseConfig();
		dbConfig.setAllowCreate(true);
		dbConfig.setTransactional(resumable);
		dbConfig.setDeferredWrite(!resumable);
		urlsDB = env.openDatabase(null, "InProcessPagesDB", dbConfig); //PendingURLsDB类似数据库名 InProcessPagesDB
	}
	
	public List<String> get(int max) throws DatabaseException {
		synchronized (mutex) {
			int matches = 0;
			List<String> results = new ArrayList<>(max);

			Cursor cursor = null;
			OperationStatus result;
			DatabaseEntry key = new DatabaseEntry();
			DatabaseEntry value = new DatabaseEntry();
			Transaction txn;
			if (resumable) {
				txn = env.beginTransaction(null, null);
			} else {
				txn = null;
			}
			try {
				cursor = urlsDB.openCursor(txn, null);
				result = cursor.getFirst(key, value, null);

				while (matches < max && result == OperationStatus.SUCCESS) {
					if (value.getData().length > 0) {
						String  url = new TupleInput(value.getData(), value.getOffset(),value.getSize()).readString();
						results.add(url);
						//System.out.println(value.toString());
						System.out.println(url);
						matches++;
					}
					result = cursor.getNext(key, value, null);
				}
			} catch (DatabaseException e) {
				if (txn != null) {
					txn.abort();
					txn = null;
				}
				throw e;
			} finally {
				if (cursor != null) {
					cursor.close();
				}
				if (txn != null) {
					txn.commit();
				}
			}
			return results;
		}
	}
	
	
	public void dataWriteFile(String filename,List<String> list) throws IOException{
		File file = new File(filename);
		BufferedWriter output = new BufferedWriter(new FileWriter(file));
		for (String url : list){
			output.write(url);
			output.write("\r\n");
		}
		output.close();
	    
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		//System.out.println(args[0] + " " + args[1]);
		if (args.length != 2){
			System.out.println("请传入两个参数    参数1：文件和路径    参数2：获得数据条数");
			return;
		}
		
		
		MyBDB mydb = new MyBDB();
		mydb.start();
		int num = Integer.valueOf(args[1]);
		mydb.dataWriteFile(args[0],mydb.get(num));
		
	}

}
