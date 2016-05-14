package com.ynswet.common.util;


public class LogUtils {

	/**
	 * 在原有的备注信息上进行换行和添加新的备注
	 * 
	 * @param oldStr	原备注
	 *            
	 * @param newStr	新增的内容
	 *            
	 * @return 更新后的备注
	 */
	public static String addStr(String oldStr, String aStr) {
		oldStr=null==oldStr?"":oldStr;
		String date = DateTimeUtils.dateTimeFormat.format(System.currentTimeMillis());
		String note = oldStr + date +" "+ aStr  + "\r\n";
		return note;
	}
}
