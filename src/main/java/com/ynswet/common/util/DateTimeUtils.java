package com.ynswet.common.util;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateTimeUtils implements Serializable {

	private static final long serialVersionUID = -3098985139095632110L;

	private static Date date = new Date();

	private static Calendar now = Calendar.getInstance();

	private static SimpleDateFormat formatter = new SimpleDateFormat();

	public final static SimpleDateFormat timeFormat = new SimpleDateFormat(
			"HH:mm:ss");

	public final static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");

	public final static SimpleDateFormat ymdhmFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");

	public final static SimpleDateFormat dateTimeFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public final static SimpleDateFormat yearMonthNow = new SimpleDateFormat(
			"yyyy-MM");

	/**
	 *
	 *
	 * @return 明日是周几 星期一到七(1-7)
	 */
	public static int getNextDayOfweek() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new java.util.Date(date.getTime() + 24 * 60 * 60
				* 1000));
		if (calendar.get(Calendar.DAY_OF_WEEK) == 1)
			return 7;
		else
			return calendar.get(Calendar.DAY_OF_WEEK) - 1;
	}

	/**
	 *
	 *
	 * @return 明天的日期
	 * @throws ParseException
	 */
	public static String getNextDay() {
		String operationtime = dateFormat.format(new java.util.Date(date
				.getTime() + 24 * 60 * 60 * 1000));
		return operationtime;
	}

	/**
	 *
	 *
	 * @return 今天的日期
	 */
	public static String getToday() {
		String operationtime = dateFormat.format(new java.util.Date(date
				.getTime()));
		return operationtime;

	}

	/**
	 *
	 * @return 现在的时间 默认的系统格式
	 */
	public static Date getNowTime() {
		return new Date();
	}

	/**
	 *
	 * @param date
	 * @return "yyyy-MM-dd" 格式的字符串
	 *
	 */
	public static String validateDate(Date date) {
		return date == null ? null : DateTimeUtils.dateFormat.format(date);
	}

	/**
	 *
	 * @param time
	 * @return "yyyy-MM" 格式的字符串
	 *
	 */
	public static String valiYearMonthDate(long time) {
		String operationtime = yearMonthNow.format(new java.util.Date(time));
		return operationtime;
	}

	/**
	 *
	 * @param date
	 * @return "yyyy-MM" 格式的字符串
	 *
	 */
	public static String valiYearMonthDate() {
		String operationtime = yearMonthNow.format(new java.util.Date(date
				.getTime()));
		return operationtime;
	}

	/**
	 *
	 * @param date
	 * @return "HH:mm" 格式的字符串
	 */
	public static String validateTime(Date date) {
		return date == null ? null : DateTimeUtils.timeFormat.format(date);
	}

	/**
	 *
	 * @param date
	 * @return "yyyy-MM-dd HH:mm" 格式的字符串
	 */
	public static String validateYmhmTime(Date date) {
		return date == null ? null : DateTimeUtils.ymdhmFormat.format(date);
	}

	/**
	 *
	 * @param date
	 * @return "yyyy-MM-dd HH:mm:ss" 格式的字符串
	 */
	public static String validateDateTime(Date date) {
		return date == null ? null : DateTimeUtils.dateTimeFormat.format(date);
	}

	/**
	 *
	 * @param dateStr
	 *            String 格式为"yyyy-MM-dd"
	 * @return 日期
	 *
	 */
	public static Date validateDate(String dateStr) {
		try {
			return dateStr == null || dateStr.equals("") ? null
					: DateTimeUtils.dateFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *
	 * @param dateStr
	 *            String 格式为"HH:mm"
	 * @return 日期
	 */
	public static Date validateTime(String dateStr) {
		try {
			return dateStr == null || dateStr.equals("") ? null
					: DateTimeUtils.timeFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *
	 * @param dateStr
	 *            String 格式为"yyyy-MM-dd HH:mm:ss"
	 * @return 日期
	 */
	public static Date validateDateTime(String dateStr) {
		try {
			return dateStr == null || dateStr.equals("") ? null
					: DateTimeUtils.dateTimeFormat.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *
	 * 求两个日期相差天数
	 *
	 * @param sd
	 *            起始日期，格式yyyy-MM-dd
	 *
	 * @param ed
	 *            终止日期，格式yyyy-MM-dd
	 *
	 * @return 两个日期相差天数
	 *
	 */
	public static long getIntervalDays(String sd, String ed) {
		return ((java.sql.Date.valueOf(ed)).getTime() - (java.sql.Date
				.valueOf(sd)).getTime()) / (3600 * 24 * 1000);
	}

	/**
	 *
	 * 取得指定年月日的日期对象.
	 *
	 * @param year
	 *            年
	 *
	 * @param month
	 *            月注意是从1到12
	 *
	 * @param day
	 *            日
	 *
	 * @return 一个java.util.Date()类型的对象
	 *
	 */
	public static Date getDateObj(int year, int month, int day) {
		Calendar c = new GregorianCalendar();
		c.set(year, month - 1, day);
		return c.getTime();
	}

	/**
	 *
	 * 取得指定分隔符分割的年月日的日期对象.
	 *
	 * @param argsDate
	 *            格式为"yyyy-MM-dd"
	 *
	 * @param split
	 *
	 *            时间格式的间隔符，例如"-"，"/"，要和时间一致起来。
	 *
	 * @return 一个java.util.Date()类型的对象
	 *
	 */
	public static Date getDateObj(String argsDate, String split) {
		String[] temp = argsDate.split(split);
		int year = new Integer(temp[0]).intValue();
		int month = new Integer(temp[1]).intValue();
		int day = new Integer(temp[2]).intValue();
		return getDateObj(year, month, day);
	}

	/**
	 *
	 * @return 当前年上月份
	 */
	public static String getNowYearLashtMonth() {

		return now.get(Calendar.YEAR) + "-" + (getNowMonth() - 1);
	}

	/**
	 *
	 * @return 当前年月份
	 */
	public static String getNowYearMonth() {

		return now.get(Calendar.YEAR) + "-" + getNowMonth();
	}

	/**
	 *
	 * @return 当前年份
	 */
	public static int getNowYear() {

		return now.get(Calendar.YEAR);
	}

	/**
	 *
	 * @return 当前月份
	 */
	public static int getNowMonth() {

		return now.get(Calendar.MONTH) + 1;
	}

	/**
	 *
	 * @return 当前月份里面的几号
	 */
	public static int getNowDate() {

		return now.get(Calendar.DATE);
	}

	/**
	 * format 2011年05月04日
	 *
	 * @return yyyy-mm-dd hh:mm:ss
	 */
	public static String formatToTimeStamp(String s) {

		s = s.replace("年", "-");
		s = s.replace("月", "-");
		s = s.replace("日", "");
		s += " 00:00:00";
		return s;

	}

	/**
	 * 从开始日期到结束日期每月按照yyyy-MM格式输出，不能重复 不含当前年月
	 *
	 * @param yearMonth
	 * @param compareYearMonth
	 * @return
	 */
	public static List<String> compareYearMonth(String startYearMonth,
			String endYearMonth) {
		List<String> result = new ArrayList<String>();
		if (startYearMonth.indexOf("-") != -1
				&& endYearMonth.indexOf("-") != -1) {

			String[] startArr = startYearMonth.split("-");
			String[] endArr = endYearMonth.split("-");
			int startYear = Integer.parseInt(startArr[0]);
			int startMonth = Integer.parseInt(startArr[1]);
			int endYear = Integer.parseInt(endArr[0]);
			int endMonth = Integer.parseInt(endArr[1]);

			if (startYear == endYear) {
				for (int i = startMonth; i < endMonth; i++) {
					result.add(startYear + "-" + i);
				}
			} else if (startYear < endYear) {
				for (int i = startMonth; i < 13; i++) {
					result.add(startYear + "-" + i);
				}
				int startNextYear = startYear + 1;
				for (int i = startNextYear; i < endYear; i++) {
					for (int m = 1; m < 13; m++) {
						result.add(i + "-" + m);
					}
				}
				for (int i = 1; i < endMonth; i++) {
					result.add(endYear + "-" + i);
				}
			}

		}

		return result;
	}


	/**
	 *
	 * 函数功能说明   当前时间
	 * @author 原勇
	 * @date 2015年6月15日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @return   
	 * @return Date   
	 * @throws
	 */
	public static Date  getSystemCurrentTimeMillis(){

		return new Timestamp(System.currentTimeMillis());
	}
	
	/**
	 * 
	 * 函数功能说明:获取昨天的日期
	 * @author 刘培琪 
	 * @date 2015年10月23日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @return    
	 * @return String   
	 * @throws
	 */
	public static String getYesterDay(){
		String operationtime = dateFormat.format(new java.util.Date(date.getTime()
				- 24*60*60*1000));
		return operationtime;
	}




	public static void main(String[] args) {

		//System.out.println(DateTimeUtils.getNowYearLashtMonth());
		//System.out.println(DateTimeUtils.getNowYearMonth());
		List<String> ymList = DateTimeUtils
				.compareYearMonth("2010-4", "2011-5");

		List<String> sonList = new ArrayList<String>();
		for (int i = 3; i < 12; i++) {
			sonList.add("2010-" + i);

		}
		sonList.addAll(sonList);
		for (String str : ymList)
			System.out.println("A:" + str);
		for (String str : sonList)
			System.out.println("B:" + str);

		//System.out.println(ymList.removeAll(sonList));

		for (String str : ymList)
			System.out.println("C:" + str);
		// String startMonth =
		// DateTimeUtils.valiYearMonthDate(DateTimeUtils.getDateObj(2010, 4,
		// 5).getTime());
		//System.out.println(startMonth);
		// String endMonth = DateTimeUtils.valiYearMonthDate();
		//System.out.println(endMonth);
	}
}