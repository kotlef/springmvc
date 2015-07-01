package com.ynswet.common.redis;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 
 * 类功能说明  字符串形式存储cache 基类
 * <p>Title: StringRedisTemplateBaseService.java</p>
 * @author 原勇
 * @date 2015年5月10日 下午3:56:50
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public abstract class StringRedisTemplateBaseService<K extends Serializable, V extends Serializable> {

	private static final String CHARSET = "UTF8";

	@Autowired
	protected StringRedisTemplate stringRedisTemplate;

	public void setRedisTemplate(StringRedisTemplate stringRedisTemplate) {  
        this.stringRedisTemplate = stringRedisTemplate;  
    }  

	public boolean set(final byte[] key, final byte[] value,
			final long activeTime) {
		return stringRedisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				boolean rs = true;
				connection.set(key, value);
				if (activeTime > 0) {
					rs = connection.expire(key, activeTime);
				}
				return rs;
			}
		});
	}

	public boolean set(String key, String value, long activeTime) {
		return this.set(key.getBytes(), value.getBytes(), activeTime);
	}

	public boolean set(String key, String value) {
		return this.set(key, value, 0L);
	}

	public boolean set(byte[] key, byte[] value) {
		return this.set(key, value, 0L);
	}
	
	public String get(final String key) {
		return stringRedisTemplate.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection)
					throws DataAccessException {
				try {
					byte[] value = connection.get(key.getBytes());
					return value == null ? "" : new String(value, CHARSET);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return "";
			}
		});
	}
	
	/**
	 * 
	 * 函数功能说明  存储对象类型
	 * @author 原勇 
	 * @date 2015年5月10日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param key
	 * @param @param hashKey
	 * @param @param value    
	 * @return void   
	 * @throws
	 */
	public void put(String key, String hashKey, Object value) {
		stringRedisTemplate.opsForHash().put(key, hashKey, value);
	}

	/**
	 * 
	 * 函数功能说明  获取缓存对象
	 * @author 原勇 
	 * @date 2015年5月10日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param key
	 * @param @param hashKey
	 * @param @return    
	 * @return Object   
	 * @throws
	 */
	public Object get(final String key, Object hashKey) {
		return stringRedisTemplate.opsForHash().get(key, hashKey);
	}

	public Set<String> matchKeys(String pattern) {
		return stringRedisTemplate.keys(pattern);

	}

	public boolean exists(final String key) {
		return stringRedisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.exists(key.getBytes());
			}
		});
	}

	public boolean flushDB() {
		return stringRedisTemplate.execute(new RedisCallback<Boolean>() {
			public Boolean doInRedis(RedisConnection connection)
					throws DataAccessException {
				connection.flushDb();
				return true;
			}
		});
	}

	public long delete(final Collection<String> keys) {
		return stringRedisTemplate.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				long result = 0;
				for (String key : keys) {
					result = connection.del(key.getBytes());
				}
				return result;
			}
		});
	}

	public long delete(final String... keys) {
		Collection<String> cols = new ArrayList<String>();
		for (String key : keys) {
			cols.add(key);
		}
		return this.delete(cols);
	}

}
