package com.ynswet.common.util;

public class StringUtils {

	/**
	 * 判断null或string.length
	 * @param string
	 * @return
	 */
	public static boolean isEmpty(String string) {
		return (string == null || string.length() == 0);
	}
	
	/**
	 * 判断null或stirng.trim()
	 * @param string
	 * @return
	 */
	public static boolean isTrimEmpty(String string) {
		return (string == null || string.trim().equals(""));
	}
	
	/**
	 * null返回空字符串
	 * @param object
	 * @return string
	 */
	public static String parseNull(String string) {
		return string == null ? "" : string;
	}
	
	/**
	 * 字符串首字母大写
	 * @param string
	 * @return
	 */
	public static String toInitialUpperCase(String string) {
		if(string == null) 
			return null;
		return string.substring(0, 1).toUpperCase() + string.substring(1, string.length());
	}
	
	/**
	 * 字符串首字母小写
	 * @param string
	 * @return
	 */
	public static String toInitialLowerCase(String string) {
		if(string == null) 
			return null;
		return string.substring(0, 1).toLowerCase() + string.substring(1, string.length());
	}
	
	/**
	 * 判断字符串是否含有大写字母
	 * @param string
	 * @return
	 */
	public static boolean hasUpperCase(String string) {
		if(string == null)
			return false;
		return string.matches("^.*[A-Z]+.*$");
	}
	
	/**
	 * 如果字符串含有大写字母
	 * 则在首尾都加上双引号
	 * @param string
	 * @return
	 */
	public static String addQuotesHasUpperCase(String string) {
		if(StringUtils.hasUpperCase(string))
			string = '"' + string + '"';
		return string;
	}
	
	/**
	 * 获取权限编码
	 * @param communityId
	 * @param houseId
	 * @return
	 */
	public static String getRightCode(int communityId,int houseId){
		
		return communityId+"-"+houseId;
	}
	
	/**
	 * 根据数组生成SQL匹配字符串
	 * @param obj
	 * @return
	 */
	public static String getSQLRegexp(int[] obj){
		if(obj.length==0){
			return null;
		}
		String result="";
		for(int i=0;i<obj.length;i++){
			result+="^"+obj[i]+"|";
		}
		if(!result.isEmpty()){
			result=result.substring(0, result.length()-1);
		}
		return result;
	}
	
	/**
	 * 根据数组生成SQL匹配字符串
	 * @param obj
	 * @return
	 */
	public static String getSQLRegexp(String[] obj){
		if(obj.length==0){
			return null;
		}
		String result="";
		for(int i=0;i<obj.length;i++){
			result+="^"+obj[i]+"|";
		}
		if(!result.isEmpty()){
			result=result.substring(0, result.length()-1);
		}
		return result;
	}
	
	public static void main(String[] args) {
		System.out.println(getRightCode(52366,223));
		System.out.println(getSQLRegexp(new String[]{"25","36"}));
	}
}
