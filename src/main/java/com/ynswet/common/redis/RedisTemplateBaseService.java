package com.ynswet.common.redis;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 
 * 类功能说明  单个对象存储 redis 基类
 * <p>Title: RedisTemplateBaseService.java</p>
 * @author 原勇
 * @date 2015年5月10日 下午3:55:52
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public abstract class RedisTemplateBaseService<K extends Serializable, V extends Serializable> {
	@Autowired
	protected RedisTemplate<K, V> redisTemplate;  
	  
    public RedisTemplate<K, V> getRedisTemplate() {  
        return redisTemplate;  
    }  
  
    public void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {  
        this.redisTemplate = redisTemplate;  
    }
    
   /**
    * 
    * 函数功能说明  存储对象类型
    * @author 原勇 
    * @date 2015年5月10日
    * 修改者名字 修改日期
    * 修改内容
    * @param @param key
    * @param @param objKey
    * @param @param value    
    * @return void   
    * @throws
    */
    public void put(K key, final String objKey, Object value) {  
        redisTemplate.opsForHash().put(key, objKey, value);  
    }  
  
    /**
     * 
     * 函数功能说明  删除缓存
     * @author 原勇 
     * @date 2015年5月10日
     * 修改者名字 修改日期
     * 修改内容
     * @param @param key
     * @param @param objKey    
     * @return void   
     * @throws
     */
    public void delete(K key, final String objKey) {  
        redisTemplate.opsForHash().delete(key, objKey);  
    }  
  
    /**
     * 
     * 函数功能说明  获取redis 缓存
     * @author 原勇 
     * @date 2015年5月10日
     * 修改者名字 修改日期
     * 修改内容
     * @param @param key
     * @param @param objKey
     * @param @return    
     * @return Object   
     * @throws
     */
    public Object get(K key,final String objKey) {  
        return redisTemplate.opsForHash().get(key, objKey);  
    } 
}
