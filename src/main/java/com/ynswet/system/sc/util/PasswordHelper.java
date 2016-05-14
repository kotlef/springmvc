package com.ynswet.system.sc.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

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
				new SimpleByteSource(getCredentialsSalt(userlogin)),
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

//		try {
			// FileReader fr = new FileReader("E:\\1.txt");
			// BufferedReader bufferedreader = new BufferedReader(fr);
			// String instring;
			// while ((instring = bufferedreader.readLine().trim()) != null) {
			// if (0 != instring.length()) {
			// System.out.println(instring);
			// }
			// }
			// fr.close();

//			BufferedReader br = new BufferedReader(
//					new InputStreamReader(new FileInputStream("E:\\1.txt")));
//
//			for (String line = br.readLine(); line != null; line = br
//					.readLine()) {
//				String[] str = line.split("\\s+");
//				//System.out.println(str[0] + "--|--" + str[1]);
//				String password = new SimpleHash(ph.algorithmName, "123",
//						ByteSource.Util.bytes(str[1] + salt), ph.hashIterations)
//								.toHex();
//				System.out.println("INSERT INTO `d1`.`userlogin` (`LoginString`, `UID`, `Password`, `Salt`, `Status`) VALUES  ('"+str[1]+"','"+str[0]+"','"+password+"','"+salt+"','0');");
//			}
//			br.close();
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		//test账户
		String[] testUserloginname = new String[]{"18812345678", "18800001234",
				"18800008888", "18800006666","18800007777","18812345679", "18800001235",
				"18800008889", "18800006667","18800008899", "18800006688","18800007766","18800007755"};
		//运营账户

		String[] userloginname = new String[]{"18687524490", "18208868807",
				"18087064716", "18988078987","18788491638","13378851254", "18208750024",
				"13658891364"};

		String[] appUsername = new String[]{"0000"};
		for (String name : appUsername) {
			String password = new SimpleHash(ph.algorithmName, "yuantian",
					ByteSource.Util.bytes(name + salt), ph.hashIterations)
							.toHex();
			System.out.println(password);
		}
	}
}
