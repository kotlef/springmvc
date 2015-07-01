/**
* Copyright 2007 - 2011 Skyway Software, Inc.
*/
package com.ynswet.common.util.databinding;

import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.springframework.binding.convert.converters.StringToObject;

public class StringToDate extends StringToObject	{
	private DateConverter converter = null;

	/**
	 * Creates a CustomCalendarConverter with a default format of date time with timezone in ISO
	 * format
	 *
	 * @see DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT
	 */
	public StringToDate() {
		this(DateConverter.DEFAULT.getPattern());
	}

	/**
	 * Returns a CustomCalendarConverter with the pattern specified and the default Locale
	 *
	 * @param pattern
	 */
	public StringToDate(String pattern) {
		this(pattern, null);
	}

	/**
	 * Returns a CustomCalendarConverter with the Locale specified, and the defult short Date Format
	 *
	 * @param locale
	 */
	public StringToDate(Locale locale) {
		this(FastDateFormat.getDateInstance(FastDateFormat.SHORT, locale).getPattern(), locale);
	}

	/**
	 * Creates a CustomCalendarConverter using the pattern and Locale passed The pattern will be used
	 * without modification as one of the parsing patterns The Locale will be used to generate a set
	 * of other patterns that may also be used to parse If no Locale is passed, the default Locale
	 * is used
	 *
	 * @param dateFormat
	 * @param allowEmpty
	 */
	public StringToDate(String pattern, Locale locale) {
		super(Date.class);
		converter = new DateConverter(pattern, locale);
	}


	@SuppressWarnings("unchecked")
	@Override
	protected Object toObject(String text, Class arg1) throws Exception {
		return converter.getDateFromText(text);
	}

	@Override
	protected String toString(Object o) throws Exception {
		Date date = (Date)o;
		return converter.getTextFromDate(date);
	}

}
