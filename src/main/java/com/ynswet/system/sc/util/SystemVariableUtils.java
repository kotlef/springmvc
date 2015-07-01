package com.ynswet.system.sc.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Component;

import com.ynswet.system.sc.model.CommonVariableModel;

/**
 * 系统变量工具类
 * 
 * @author elfmatian
 */
@Component
public class SystemVariableUtils {
	


	/**
	 * 获取当前安全模型
	 * 
	 * @return {@link CommonVariableModel}
	 */
	public static CommonVariableModel getCommonVariableModel() {

		Subject subject = SecurityUtils.getSubject();

		if (subject != null && subject.getPrincipal() != null
				&& subject.getPrincipal() instanceof CommonVariableModel) {
			return (CommonVariableModel) subject.getPrincipal();
		}

		return null;
	}

	/**
	 * 判断当前会话是否登录
	 * 
	 * @return boolean
	 */
	public static boolean isAuthenticated() {
		return SecurityUtils.getSubject().isAuthenticated();
	}

	/**
	 * 退出
	 * 
	 * @return boolean
	 */
	public static void logout() {

		SecurityUtils.getSubject().logout();
	}


	
	public static void main(String[] args) {

		String password = new SimpleHash("MD5", "123456").toHex();
		//System.out.println(password);
	}

}
