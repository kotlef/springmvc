/**
* Copyright 2007 - 2011 Skyway Software, Inc.
*/
package com.ynswet.common.util.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;

/**
 * Provides conversion routines for date objects.
 *
 * @author Jack Kennedy
 * @author Michael Weaver
 */
public abstract class DateParser {
	public final static String DEFAULT_DATE_FORMAT_FROM_DATE_CONTROLS = "yyyy-MM-dd"; //$NON-NLS-1$

	public static Date convertToDate(String text, Locale locale, String... patterns) {
		Date d = null;
		DefaultLocalePatterns lp = null;

		if (StringUtils.isNotBlank(text)) {
			// first try the default format from the DOJO calendar widget
			// if we can parse the date with this value with a DateFormat rather than
			// relying on DateUtils, we will get the benefit of a parser that also
			// knows how to shift dates (for example, from Thai to Gregorian
			SimpleDateFormat defaultControlForm = new SimpleDateFormat (DEFAULT_DATE_FORMAT_FROM_DATE_CONTROLS, locale);
			try {
				d = defaultControlForm.parse(text);
			} catch (ParseException e) {
				// nop
			}

			// next try the default patters for the users locale
			if (d == null) {
				try {
					lp = DefaultLocalePatterns.get(locale);

					if (patterns != null) {
						d = DateUtils.parseDate(text, lp.convertInternalPattern(patterns));
					}
				} catch (ParseException e) {
					// nop
				}
			}

			// finally, attempt to parse the date using all of the available patterns
			if (d == null) {
				try {
					d = DateUtils.parseDate(text, lp.getAllPatterns());
				} catch (ParseException e) {
					throw new RuntimeException("Could not parse string into a Calendar: " + text, e);
				}
			}
		}

		return d;
	}

	/**
	 * Parses the text to create a Calendar object. This method delegates to
	 * {@link DateUtils#parseDate(String, String[])} from Apache.
	 * <p>
	 * Apache's method iterates a set of patterns, attempting to parse the text with each pattern
	 * until one succeeds. We invoke Apache's method with the specified set of patterns first. If
	 * none of those succeed, we try the default patterns for the specified locale. If none of those
	 * succeed, a {@link RuntimeException} is thrown.
	 *
	 * @throws RuntimeException if the text cannot be parsed into a Calender
	 *
	 * @see org.apache.commons.lang.time.DateUtils#parseDate(String, String[])
	 * @see DefaultLocalePatterns
	 */
	public static Calendar convertToCalendar(String text, Locale locale, String... patterns) {
		Date d = convertToDate(text, locale, patterns);
		Calendar c = null;

		if (d != null){
			c = Calendar.getInstance(locale);
			c.setTime(d);
		}

		return c;
	}

	/**
	 * Converts the current value to Text using the pattern that has been set or a default pattern
	 * if no pattern has been set
	 */
	public static String convertToString(Calendar c, String pattern, Locale locale) {
		FastDateFormat formatter = getFormatter(pattern, locale);

		return formatter.format(c);
	}

	public static String convertToString(Date date, String pattern, Locale locale) {
		FastDateFormat formatter = getFormatter(pattern, locale);

		return formatter.format(date);
	}

	public static FastDateFormat getFormatter(String pattern, Locale locale){
		FastDateFormat formatter = null;

		if (StringUtils.isBlank(pattern)) {
			// If the pattern being requested is empty, then use a default formatter
			formatter = FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT, locale);
		} else {
			formatter = FastDateFormat.getInstance(DefaultLocalePatterns.get(locale).convertInternalPattern(pattern), locale);
		}

		return formatter;
	}

	/**
	 * Answers whether or not the given string can be parsed into a Calendar using the default
	 * pattern and the Locale specific patterns as parsing options. This method uses the
	 * DateUtils.parseDate method from Apache.
	 *
	 * @see org.apache.commons.lang.time.DateUtils
	 * @see getFormatsForLocale
	 */
	public static final boolean isValidCalendar(String text, Locale locale) {
		try {
			Calendar c = convertToCalendar(text, locale);
			if (c != null) {
				return true;
			}
		} catch (Exception e) {
			// nop
		}
		return false;
	}

	/**
	 * Answers whether or not the given string represents the date portion (not including time
	 * portions) of a Calendar according to the default pattern and the Locale specific patterns as
	 * parsing options. This method uses the DateUtils.parseDate method from Apache.
	 *
	 * @see org.apache.commons.lang.time.DateUtils
	 * @see getFormatsForLocale
	 */
	public static final boolean isValidDate(String text, Locale locale) {
		try {
			DateUtils.parseDate(text, DefaultLocalePatterns.get(locale).getDatePatterns());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Answers whether or not the given string represents the time portion (not including date
	 * portions) of a Calendar according to the default pattern and the Locale specific patterns as
	 * parsing options. This method uses the DateUtils.parseDate method from Apache.
	 *
	 * @see org.apache.commons.lang.time.DateUtils
	 * @see getFormatsForLocale
	 */
	public static final boolean isValidTime(String text, Locale locale) {
		try {
			DateUtils.parseDate(text, DefaultLocalePatterns.get(locale).getTimePatterns());
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Answers whether or not the given string represents a complete date and time (including both
	 * date and time portions) of a Calendar according to the default pattern and the Locale
	 * specific patterns as parsing options. This method uses the DateUtils.parseDate method from
	 * Apache.
	 *
	 * @see org.apache.commons.lang.time.DateUtils
	 * @see getFormatsForLocale
	 */
	public static final boolean isValidDateTime(String text, Locale locale) {
		try {
			DateUtils.parseDate(text, DefaultLocalePatterns.get(locale).getDateTimeFormats());
		} catch (Exception e) {
			return false;
		}
		return true;
	}
}