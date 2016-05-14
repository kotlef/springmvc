package com.ynswet.system.sc.encryption;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * RSA算法加密/解密工具类。
 * 
 * @author elfmatian
 * @version 1.0.0, 2014-04-14
 */
public class EncryptionBean extends AbstractRsaEncryption {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(EncryptionBean.class);

	public EncryptionBean() {

	}

	public static void main(String[] args) {
		//
		// EncryptionBean ere = new EncryptionBean();
		//
		// KeyPair kp1 = ere.generateKeyPair();
		//System.out.println(kp1.getPrivate());
		//System.out.println(kp1.getPublic());
		// KeyPair kp2 = ere.getKeyPair();
		//System.out.println(kp2.getPrivate());
		//System.out.println(kp2.getPublic());
		//
		// String passwd = "123456";
		//
		// RSAPublicKey oldRsaPublickey = (RSAPublicKey) kp2.getPublic();
		//
		// RSAPrivateKey oldRsaPrivatekey = (RSAPrivateKey) kp2.getPrivate();
		//
		// String modulus = oldRsaPublickey.getModulus().toString(16);
		//
		//System.out.println("modulus:" + modulus);
		//System.out.println("modulus1:"
		// + Hex.encodeHexString(oldRsaPublickey.getModulus()
		// .toByteArray()));
		//
		// String publicExponen = oldRsaPublickey.getModulus().toString(16);
		//
		//System.out.println("PublicExponen:" + publicExponen);
		//
		//System.out.println("PublicExponen1:"
		// + Hex.encodeHexString(oldRsaPublickey.getPublicExponent()
		// .toByteArray()));
		//
		// RSAPublicKey newRsaPublickey = ere
		// .getRSAPublidKey(Hex.encodeHexString(oldRsaPublickey
		// .getModulus().toByteArray()), "010001");
		//
		//
		// String rsaPassword = ere.encryptString(newRsaPublickey, passwd);
		//
		// String password = ere.decryptString(kp2, rsaPassword);
		//
		//System.out.println(password);

		/*
		 * String encrypString=ere.encryptString(kp1,passwd);
		 * //System.out.println("encrypString:"+encrypString); String
		 * passwd0=ere.decryptString(kp1,encrypString);
		 * //System.out.println("passwd:"+passwd0); String
		 * passwhealth_fd=ere.decryptString(kp1,encrypString);
		 * //System.out.println("passwd:"+passwhealth_fd); String
		 * passwd2=ere.decryptStringByJs(kp1,encrypString);
		 * //System.out.println("passwd:"+passwd2);
		 */
//		Properties properties = new Properties();
//		properties.put("rejoin", "true");
//		try {
//			Toolkit toolkit = ToolkitFactory.createToolkit(
//					"toolkit:terracotta://127.0.0.1:9510", properties);
//			//System.out.println("2");
//		} catch (IllegalArgumentException
//				| ToolkitInstantiationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}
}