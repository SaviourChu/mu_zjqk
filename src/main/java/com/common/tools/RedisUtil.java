package com.common.tools;

import java.util.List;
import java.util.Map;

import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

import redis.clients.jedis.Jedis;

public class RedisUtil {

	public static String get(String key) {
		Cache cache = Redis.use();
		Jedis jedis = cache.getJedis();
		try {
			return jedis.get(key);
		} finally {
			cache.close(jedis);
		}
	}

	public static String set(String key, String value) {
		Cache cache = Redis.use();
		Jedis jedis = cache.getJedis();
		try {
			return jedis.set(key, value);
		} finally {
			cache.close(jedis);
		}
	}

	public static void setex(String key, String value, int seconds) {
		Cache cache = Redis.use();
		Jedis jedis = cache.getJedis();
		try {
			jedis.setex(key, seconds, value);
		} finally {
			cache.close(jedis);
		}
	}

	public static Boolean hexists(String key, String field) {
		Cache cache = Redis.use();
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hexists(key, field);
		} finally {
			cache.close(jedis);
		}
	}

	public static Long hsetnx(String key, String field, String value) {
		Cache cache = Redis.use();
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hsetnx(key, field, value);
		} finally {
			cache.close(jedis);
		}
	}

	public static Long hdel(String key, String fields) {
		Cache cache = Redis.use();
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hdel(key, fields);
		} finally {
			cache.close(jedis);
		}
	}

	public static String hmset(String key, Map<String, String> hash) {
		Cache cache = Redis.use();
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hmset(key, hash);
		} finally {
			cache.close(jedis);
		}
	}

	public static Long del(String key) {
		Cache cache = Redis.use();
		Jedis jedis = cache.getJedis();
		try {
			return jedis.del(key);
		} finally {
			cache.close(jedis);
		}
	}

	public static String hget(String key, String field) {
		Cache cache = Redis.use();
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hget(key, field);
		} finally {
			cache.close(jedis);
		}
	}

	public static Map<String, String> hgetAll(String key) {
		Cache cache = Redis.use();
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hgetAll(key);
		} finally {
			cache.close(jedis);
		}
	}

	public static Long hset(String key, String field, String value) {
		Cache cache = Redis.use();
		Jedis jedis = cache.getJedis();
		try {
			return jedis.hset(key, field, value);
		} finally {
			cache.close(jedis);
		}
	}

	public static Long llen(String key) {
		Cache cache = Redis.use();
		Jedis jedis = cache.getJedis();
		try {
			return jedis.llen(key);
		} finally {
			cache.close(jedis);
		}
	}

	public static Long lpush(String key, String... strings) {
		Cache cache = Redis.use();
		Jedis jedis = cache.getJedis();
		try {
			return jedis.lpush(key, strings);
		} finally {
			cache.close(jedis);
		}
	}

	/**
	 * 设置过期时间 单位 秒
	 * 
	 * @param key
	 * @param seconds
	 */
	public static void expire(String key, int seconds) {
		Cache cache = Redis.use();
		Jedis jedis = cache.getJedis();
		try {
			jedis.expire(key, seconds);
		} finally {
			cache.close(jedis);
		}
	}

	public static List<String> lrange(String key, long start, long end) {
		Cache cache = Redis.use();
		Jedis jedis = cache.getJedis();
		try {
			return jedis.lrange(key, start, end);
		} finally {
			cache.close(jedis);
		}
	}
}
