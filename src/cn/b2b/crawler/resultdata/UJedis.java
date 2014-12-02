package cn.b2b.crawler.resultdata;

import java.util.ArrayList;
import java.util.List;

import cn.b2b.crawler.config.ConfigResultData;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

public class UJedis {
	private static ShardedJedisPool pool;
	static {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(500);
		config.setMaxIdle(50000);
		config.setMaxWait(20000);
		config.setTestOnBorrow(true);
		List<JedisShardInfo> servers = new ArrayList<JedisShardInfo>();
		ConfigResultData crd = new ConfigResultData();
		Object redis_servers = new Object();
		// redis_servers = "192.168.3.205:6379,192.168.3.206:6379";
		// redis_servers = "192.168.3.208:6379" ;
		redis_servers = crd.getRedisipport();
		if (redis_servers == null) {
			try {
				throw new Exception("not find redis_servers in db.properties");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			String[] pls = ((String) redis_servers).split(",");
			String host = null;
			String port = null;
			for (String s : pls) {
				if (s != null) {
					host = s.substring(0, s.indexOf(":"));
					port = s.substring(s.indexOf(":") + 1);
					JedisShardInfo sd = new JedisShardInfo(host, port);
					servers.add(sd);
				}
			}
		}
		pool = new ShardedJedisPool(config, servers, Hashing.MURMUR_HASH,
				Sharded.DEFAULT_KEY_TAG_PATTERN);
	}
	private static int index = 1;

	public static String generateKey() {
		return String.valueOf(Thread.currentThread().getId()) + "_" + (index++);
	}

	public static String get(String key) {
		ShardedJedis jedis = pool.getResource();
		String value = jedis.get(key);
		pool.returnResource(jedis);
		return value;

	}

	public static Object get(int ID, String key) {
		ShardedJedis jedis = pool.getResource();
		String value = jedis.get(key + "-" + String.valueOf(ID));
		pool.returnResource(jedis);
		return value;
	}

	public static Object get(String key, String subKey) {
		ShardedJedis jedis = pool.getResource();
		String value = jedis.get(key + "-" + subKey);
		pool.returnResource(jedis);
		return value;

	}

	public static int getint(int ID, String key) {
		ShardedJedis jedis = pool.getResource();
		String value = jedis.get(key + "-" + String.valueOf(ID));
		pool.returnResource(jedis);
		if (value == null || value == "") {
			return -999;
		}
		return Integer.parseInt(value);
	}

	public static void set(String key, String value) {
		if (key == null || key.length() == 0 || value == null)
			return;
		ShardedJedis jedis = pool.getResource();
		jedis.set(key, value);
		pool.returnResource(jedis);
	}

	public static void setint(int id, String key, int value) {

		if (key == null || key.length() == 0)
			return;
		ShardedJedis jedis = pool.getResource();
		jedis.set(key + "-" + String.valueOf(id), String.valueOf(value));
		pool.returnResource(jedis);
	}

	public static void delete(int ID, String key) {
		ShardedJedis jedis = pool.getResource();
		jedis.del(key + "-" + String.valueOf(ID));
		pool.returnResource(jedis);
	}

	public static void delete(String key) {
		ShardedJedis jedis = pool.getResource();
		jedis.del(key);
		pool.returnResource(jedis);
	}

	public static void main(String[] args) throws InterruptedException {
		// UJedis.set("abcd", "111");

		// while (true) {
		// Thread.sleep(100);
		/*
		for (int i = 0; i < 3; i++) {
			String key = "abcd" + i;
			// UJedis.set(key, Convert.toString(i));
			String val = UJedis.get("crawler:http://www.baidu.com");
			if (val == null) {
				System.err.println("not find values --------------");
			}
			System.out.println(val);
		}
		// }
		*/
		
		//UJedis.set(MD5.getMD5("crawler:http://www.baidu.com".getBytes()),"abcMD5");
		//String val = UJedis.get(MD5.getMD5("crawler:http://www.baidu.com".getBytes()));
		//System.out.println(val);
		UJedis.set("crawler:http://www.baidu.com","abc");
		String val = UJedis.get("crawler:http://www.baidu.com1");
		System.out.println(val == null);
		
	}
}
