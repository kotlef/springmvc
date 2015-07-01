/**
* Copyright 2007 - 2011 Skyway Software, Inc.
*/
package com.ynswet.common.util.databinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ynswet.common.util.date.DateParser;
import com.ynswet.common.util.date.ISOFormats;

public class CalendarConverter {
	public static final FastDateFormat DEFAULT = ISOFormats.ISO_DATETIME_TIME_ZONE_FORMAT_SANS_T;
	private static transient Log log = LogFactory.getLog(CalendarConverter.class);

	private String pattern;
	private Locale locale;
	private Collection<String> wellKnownPatterns = null;

	/**
	 * Creates a Custom Calendar Editor with a default format of date time with timezone in ISO
	 * format
	 *
	 * @see DateFormatUtils.ISO_DATETIME_TIME_ZONE_FORMAT
	 */
	public CalendarConverter() {
		this(DEFAULT.getPattern());
	}

	/**
	 * Returns a Custom Calendar Editor with the pattern specified and the default Locale
	 *
	 * @param pattern
	 */
	public CalendarConverter(String pattern) {
		this(pattern, null);
	}

	/**
	 * Returns a CustomCalendarEditor with the Locale specified, and the defult short Date Format
	 *
	 * @param locale
	 */
	public CalendarConverter(Locale locale) {
		this(FastDateFormat.getDateInstance(FastDateFormat.SHORT, locale).getPattern(), locale);
	}

	/**
	 * Returns the Locale associated with this Calendar Editor If no Locale was specified, this
	 * method will return the Locale from TypeConversionUtils
	 *
	 * @see TypeConversionUtils.getLocale()
	 * @return
	 */
	protected Locale getLocale() {
		if (locale == null)
			return TypeConversionUtils.getLocale();

		return locale;
	}

	/**
	 * Creates a custom Calendar Editor using the pattern and Locale passed The pattern will be used
	 * without modification as one of the parsing patterns The Locale will be used to generate a set
	 * of other patterns that may also be used to parse If no Locale is passed, the default Locale
	 * is used
	 *
	 * @param dateFormat
	 * @param allowEmpty
	 */
	public CalendarConverter(String pattern, Locale locale) {
		super();
		this.pattern = pattern;
		this.locale = locale;
	}

	private String[] getPatterns(String pattern)	{
		if (wellKnownPatterns == null)	{
			wellKnownPatterns = new ArrayList<String> ();
			wellKnownPatterns.add(pattern);
			wellKnownPatterns.add(ISOFormats.ISO_TIME_NO_ZONE_FORMAT.getPattern());
			wellKnownPatterns.add(ISOFormats.ISO_TIME_NO_T_FORMAT.getPattern());
		}
		return wellKnownPatterns.toArray(new String[wellKnownPatterns.size()]);
	}

	public Calendar getCalendarFromText (String text)	{
		Calendar cValue = null;
		try {
			if (StringUtils.isNotBlank(text)) {
				cValue = DateParser.convertToCalendar(text, getLocale(), getPatterns(pattern));
			}
		} catch (Exception e) {
			log.warn("Exception parsing Calendar from text:" + text + " :" + e); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return cValue;
	}

	public String getTextFromCalendar (Calendar calendar)	{
		return DateParser.convertToString(calendar, pattern, getLocale());
	}
}
