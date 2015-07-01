package com.ynswet.system.sc.util;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.springframework.stereotype.Component;

import com.ynswet.system.sc.crypto.hash.SimpleHash;
import com.ynswet.system.sc.domain.Userlogin;

/**
 * <p>
 * Userlogin: elfmatian
 * <p>
 * Date: 14-4-11
 * <p>
 * Version: 1.0
 */
@Component
public class PasswordHelper {

	private RandomNumberGenerator randomNumberGenerator = new SecureRandomNumberGenerator();

	private String algorithmName = "MD5";
	private final int hashIterations = 2;

	public Userlogin encryptPassword(Userlogin userlogin) {

		userlogin.setSalt(randomNumberGenerator.nextBytes().toHex());

		String newPassword = new SimpleHash(algorithmName,
				userlogin.getPassword(),
				ByteSource.Util.bytes(getCredentialsSalt(userlogin)),
				hashIterations).toHex();

		userlogin.setPassword(newPassword);
		return userlogin;
	}

	public String getPassword(Userlogin userlogin, String oldPassword) {

		return new SimpleHash(algorithmName, oldPassword,
				ByteSource.Util.bytes(getCredentialsSalt(userlogin)),
				hashIterations).toHex();

	}

	public String getCredentialsSalt(Userlogin userlogin) {

		return userlogin.getLoginString() + userlogin.getSalt();
	}

	public static void main(String[] args) {
		PasswordHelper ph = new PasswordHelper();

		String salt = "9ab6caa34e07097524760e04d758e63c";

		// System.out.println("salt:" + salt);
		/*
		 * String[] userloginname = new String[] { "商店", "王丽丽", "原勇", "张明坤",
		 * "李玉鹏", "order", "王二", "wll", "kunoy", "admin", "李四", "张三" };
		 */
		String[] userloginname = new String[]{"18812345678","18800001234","18800008888","18800006666"};
		for (String name : userloginname) {
			String password = new SimpleHash(ph.algorithmName, "123",
					ByteSource.Util.bytes(name + salt), ph.hashIterations)
					.toHex();
			System.out.println(password);
		}
	}
}
