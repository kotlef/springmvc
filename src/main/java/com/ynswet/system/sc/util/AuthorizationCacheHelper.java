package com.ynswet.system.sc.util;

import java.io.Serializable;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ynswet.system.sc.model.CommonVariableModel;
import com.ynswet.system.sc.realm.JdbcAuthenticationRealm;

/**
 * @author elfmatian
 * @time 2014年9月14日
 */
@Component
public class AuthorizationCacheHelper {

	@Autowired
	private JdbcAuthenticationRealm jdbcAuthenticationRealm;

	private static Logger log = LoggerFactory
			.getLogger(AuthorizationCacheHelper.class);

	/**
	 * 清除用户的授权信息
	 * 
	 * @param username
	 */
	public void clearAuthorizationInfo(String username, String realmName) {
		if (log.isDebugEnabled()) {
			log.debug("clear the " + username + " authorizationInfo");
		}
		
		if(null==realmName)
			realmName="shiroAuthorizationCache";
		PrincipalCollection principals = new SimplePrincipalCollection(username,realmName);
		
		jdbcAuthenticationRealm.clearCachedAuthorizationInfo(principals);
		
	}
	
	/**
	 * 清除用户的认证信息
	 * 
	 * @param username
	 */
	public void clearAuthenticationInfo(String username, String realmName) {
		if (log.isDebugEnabled()) {
			log.debug("clear the " + username + " authorizationInfo");
		}
		
		if(null==realmName)
			realmName="shiroAuthenticationCache";
		PrincipalCollection principals = new SimplePrincipalCollection(username,realmName);
		
		jdbcAuthenticationRealm.clearCachedAuthenticationInfo(principals);
		
	}



	/**
	 * 清除当前用户的授权信息
	 */
	public void clearAuthorizationInfo(String realmName) {

		if (SystemVariableUtils.isAuthenticated()) {
			clearAuthorizationInfo(SystemVariableUtils.getCommonVariableModel()
					.getUserlogin().getLoginString(), realmName);
		}
	}
	
	/**
	 * 清除所有授权信息
	 */
	public void clearAllAuthorizationInfo() {

		jdbcAuthenticationRealm.clearAllCachedAuthorizationInfo();
	}
	
	/**
	 * 清除所有认证信息
	 */
	public void clearAllAuthenticationInfo() {

		jdbcAuthenticationRealm.clearAllCachedAuthenticationInfo();
	}
	
	
	/**
	 * 清除所有认证和授权缓存
	 */
	public void clearAllCache() {

		jdbcAuthenticationRealm.clearAllCache();
	}

//	/**
//	 * 清除session(认证信息)
//	 * 
//	 * @param JSESSIONID
//	 */
//	public void clearAuthenticationInfo(Serializable JSESSIONID,
//			String cacheName) {
//		if (log.isDebugEnabled()) {
//			log.debug("clear the session " + JSESSIONID);
//		}
//
//		Cache<Object, Object> cache = cacheManager.getCache(cacheName);
//		cache.remove(JSESSIONID);
//	}

}
