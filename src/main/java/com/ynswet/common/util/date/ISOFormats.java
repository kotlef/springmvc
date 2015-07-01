/**
* Copyright 2007 - 2011 Skyway Software, Inc.
*/
package com.ynswet.common.util.date;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.FastDateFormat;

/**
 * The ISO patterns recognized by our framework.
 *
 * @author JKennedy
 * @author Michael Weaver
 */
public interface ISOFormats {
	// The DateFormatUtils versions of these patterns use the double Z (ZZ) time-zone string, which
	// contains a colon and does not round trip correctly. I'm replacing these with a single Z (Z)
	// version that does not contain a colon and round trips.
	// @see FastDateFormat#parsePattern
	// @see TimeZoneNumberRule.INSTANCE_NO_COLON
	public static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT = FastDateFormat.getInstance("yyyy-MM-dd'T'HH:mm:ssZ");
	public static final FastDateFormat ISO_TIME_NO_T_TIME_ZONE_FORMAT = FastDateFormat.getInstance("HH:mm:ssZ");
	public static final FastDateFormat ISO_TIME_TIME_ZONE_FORMAT = FastDateFormat.getInstance("'T'HH:mm:ssZ");
	public static final FastDateFormat ISO_TIME_NO_ZONE_FORMAT = FastDateFormat.getInstance("'T'HH:mm:ss");

	// The calendar widget can't consume the 'T' in ISO_DATETIME_TIME_ZONE_FORMAT, so I'm
	// adding a format w/out the 'T'. The 'T' is optional in the 8601 format anyway.
	public static final FastDateFormat ISO_DATETIME_TIME_ZONE_FORMAT_SANS_T = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ssZ");

	// Pass throughs
	public static FastDateFormat ISO_DATE_FORMAT = DateFormatUtils.ISO_DATE_FORMAT;
	public static FastDateFormat ISO_TIME_NO_T_FORMAT = DateFormatUtils.ISO_TIME_NO_T_FORMAT;
	public static FastDateFormat ISO_DATETIME_FORMAT = DateFormatUtils.ISO_DATETIME_FORMAT;
}
