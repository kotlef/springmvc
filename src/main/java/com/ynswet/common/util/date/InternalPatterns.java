/**
* Copyright 2007 - 2011 Skyway Software, Inc.
*/
package com.ynswet.common.util.date;

/**
 * Date patterns only recognized by our parser.
 *
 * @author JKennedy
 * @author Michael Weaver
 */
public interface InternalPatterns {
	// TODO - JK These strings are "well known" and not currently "shared" between the runtime code
	// and the design time code we may want to move them to a common class in the future
	public static String DATE_LONG = "(date_long)";
	public static String DATE_SHORT = "(date_short)";
	public static String DATE_MEDIUM = "(date_medium)";
	public static String DATE_FULL = "(date_full)";
	public static String DATETIME_LONG = "(datetime_long)";
	public static String DATETIME_SHORT = "(datetime_short)";
	public static String DATETIME_MEDIUM = "(datetime_medium)";
	public static String DATETIME_FULL = "(datetime_full)";
	public static String TIME_LONG = "(time_long)";
	public static String TIME_SHORT = "(time_short)";
	public static String TIME_MEDIUM = "(time_medium)";
	public static String TIME_FULL = "(time_full)";
}
