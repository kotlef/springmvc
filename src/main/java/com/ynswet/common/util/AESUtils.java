/**       
 * @Title: AESUtils.java
 * @Package com.ynswet.system.sc.encryption
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年10月10日 下午1:46:34
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.common.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {
	
	/** 
	 * 加密 
	 *  
	 * @param content 需要加密的内容 
	 * @param password  加密密码 
	 * @return 
	 */  
	public static String encrypt(String content,String key) {  
		try {
			//String aesKey = "keep_on_fighting";
			String aesKey = key;
			
			String aesIV = "****************";

			SecretKeySpec skeySpec = new SecretKeySpec(aesKey.getBytes("UTF-8"), "AES");
			IvParameterSpec iv = new IvParameterSpec(aesIV.getBytes("UTF-8"));
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] byteContent = content.getBytes("utf-8");
			byte[] result = cipher.doFinal(byteContent);

			return parseByte2HexStr(result); // Encoding to hex
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**解密 
	 * @param content  待解密内容 
	 * @param password 解密密钥 
	 * @return 
	 */  
	public static String decrypt(String content,String key) {  
		try {
			//String aesKey = "keep_on_fighting";
			String aesKey = key;
			String aesIV = "****************";

			SecretKeySpec skeySpec = new SecretKeySpec(aesKey.getBytes("UTF-8"), "AES");
			IvParameterSpec iv = new IvParameterSpec(aesIV.getBytes("UTF-8"));
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

			byte[] contents = parseHexStr2Byte(content); // Decoding from hex
			byte[] result = cipher.doFinal(contents);

			return new String(result); 

		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		}
		return null;
	} 
	
  /**将二进制转换成16进制 
   * @param buf 
   * @return 
   */  
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

  /**将16进制转换为二进制 
   * @param hexStr 
   * @return 
   */  
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2),
					16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}
	
	public static void main(String[] args){
		String content = "888888";  
		//加密   
		System.out.println("加密前：" + content);  
		String encryptResult = AESUtils.encrypt(content,"ynlxSwet98341278");  
		System.out.println("加密后：" + encryptResult);  
		//解密   
		String decryptResult = AESUtils.decrypt(encryptResult,"ynlxSwet98341278");  
		System.out.println("解密后：" + decryptResult); 
	}
}


