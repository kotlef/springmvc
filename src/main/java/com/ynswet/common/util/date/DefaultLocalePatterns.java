/**
* Copyright 2007 - 2011 Skyway Software, Inc.
*/
package com.ynswet.common.util.date;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;

/**
 * The default patterns for a specific {@link Locale} that are used to parse date/time strings.
 *
 * @author Michael Weaver
 * @author Jack Kennedy
 *
 * @see DateParser
 */
public class DefaultLocalePatterns {
	public static final String SHORT_TIME_WITH_TIMEZONE = "h:mm a z";
	public static final String SHORT_TIME_WITHOUT_AMPM = "h:mm";
	public static final String TWENTY_FOUR_HOUR_TIME_WITHOUT_AMPM = "H:mm";

	private static final Map<Locale, DefaultLocalePatterns> cache = new HashMap<Locale, DefaultLocalePatterns>();

	private final Locale locale;
	private final String[] allPatterns;
	private final String[] datePatterns;
	private final String[] timePatterns;
	private final String[] dateTimePatterns;
	private final Map<String, String> internalPatterns;

	public static DefaultLocalePatterns get(Locale locale) {
		synchronized (cache) {
			DefaultLocalePatterns lp = cache.get(locale);
			if (lp == null) {
				lp = new DefaultLocalePatterns(locale);
				cache.put(locale, lp);
			}
			return lp;
		}
	}

	private DefaultLocalePatterns(Locale locale) {
		this.locale = locale;

		this.datePatterns = toArray(buildDatePatterns(locale));
		this.timePatterns = toArray(buildTimePatterns(locale));
		this.dateTimePatterns = toArray(buildDateTimePatterns(locale));

		ArrayList<String> allPatterns = new ArrayList<String>();
		addAll(allPatterns, datePatterns);
		addAll(allPatterns, timePatterns);
		addAll(allPatterns, dateTimePatterns);
		this.allPatterns = toArray(allPatterns);

		this.internalPatterns = buildInternalPatternMap(locale);
	}

	public Locale getLocale() {
		return locale;
	}

	/**
	 * Returns the standard short, medium and long formats for the Locale. This method will actually
	 * generate the formats for all possible combinations of SHORT, MEDIUM, Long, and FULl for Date,
	 * Date Time, and Time. For Date Time formats, it will generate a format for each possible
	 * combination of date and time i.e. DATE=SHORT, TIME=FULL, etc
	 *
	 * The goal of this method is to create a set of formats that give the parser a fighting chance
	 * of successfully parsing a users hand written input in their own locale for dates and times
	 *
	 * By default will return the ISO formats as well
	 *
	 * @param locale
	 * @param includeISOs
	 * @return
	 */
	public String[] getAllPatterns() {
		return allPatterns;
	}

	/**
	 * Returns the list of patterns that can be used to parse a string that represents a date
	 * (without any time components)
	 */
	public String[] getDatePatterns() {
		return datePatterns;
	}

	/**
	 * Returns the list of patterns that can be used to parse a string that represents a time
	 * (without any date components)
	 */
	public String[] getTimePatterns() {
		return timePatterns;
	}

	/**
	 * Returns the list of patterns that can be used to parse a string that represents a date and
	 * time (includes both components).
	 */
	public String[] getDateTimeFormats() {
		return dateTimePatterns;
	}

	/**
	 * Returns a String representing the pattern to format the text value returned by this Editor
	 * This method will check for well known values representing the generic SHORT, MEDIUM, LONG
	 * formats for the current users local
	 *
	 * @see DATE_LONG
	 * @return
	 */
	public String convertInternalPattern(String pattern) {
		String s = internalPatterns.get(pattern);
		if (s != null) {
			return s;
		}
		return StringUtils.remove(pattern, " (ISO-8601)");
	}

	public String[] convertInternalPattern(String[] patterns) {
		String[] s = new String[patterns.length];
		for (int x=0; x < patterns.length; x++) {
			s[x] = convertInternalPattern(patterns[x]);
		}
		return s;
	}

	private static String[] toArray(List<String> list) {
		return list.toArray(new String[list.size()]);
	}

	private static void addAll(List<String> list, String[] array) {
		for (String s : array)
			list.add(s);
	}

	private static Map<String, String> buildInternalPatternMap(Locale locale) {
		Map<String, String> map = new HashMap<String, String>();
		// Match the formatting "Key" with an Actual Format, basically SHORT, MEDIUM, LONG, and
		// FULL for each type
		map.put(InternalPatterns.DATE_FULL, FastDateFormat.getDateInstance(FastDateFormat.FULL, locale).getPattern());
		map.put(InternalPatterns.DATE_LONG, FastDateFormat.getDateInstance(FastDateFormat.LONG, locale).getPattern());
		map.put(InternalPatterns.DATE_SHORT, FastDateFormat.getDateInstance(FastDateFormat.SHORT, locale).getPattern());
		map.put(InternalPatterns.DATE_MEDIUM, FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale).getPattern());
		map.put(InternalPatterns.DATETIME_SHORT, FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, locale).getPattern());
		map.put(InternalPatterns.DATETIME_MEDIUM, FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, locale).getPattern());
		map.put(InternalPatterns.DATETIME_LONG, FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, locale).getPattern());
		map.put(InternalPatterns.DATETIME_FULL, FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, locale).getPattern());
		map.put(InternalPatterns.TIME_SHORT, FastDateFormat.getTimeInstance(FastDateFormat.SHORT, locale).getPattern());
		map.put(InternalPatterns.TIME_MEDIUM, FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, locale).getPattern());
		map.put(InternalPatterns.TIME_LONG, FastDateFormat.getTimeInstance(FastDateFormat.LONG, locale).getPattern());
		map.put(InternalPatterns.TIME_FULL, FastDateFormat.getTimeInstance(FastDateFormat.FULL, locale).getPattern());
		return map;
	}

	private static List<String> buildDatePatterns(Locale locale) {
		ArrayList<String> formats = new ArrayList<String>();

		// M/d/yy
		formats.add(FastDateFormat.getDateInstance(FastDateFormat.SHORT, locale).getPattern());
		// MMM d, yyyy
		formats.add(FastDateFormat.getDateInstance(FastDateFormat.MEDIUM, locale).getPattern());
		// MMMM d, yyyy
		formats.add(FastDateFormat.getDateInstance(FastDateFormat.LONG, locale).getPattern());
		// EEEE, MMMM d, yyyy
		formats.add(FastDateFormat.getDateInstance(FastDateFormat.FULL, locale).getPattern());

		// Add ISO patterns
		formats.add(ISOFormats.ISO_DATE_FORMAT.getPattern());

		return formats;
	}

	private static List<String> buildTimePatterns(Locale locale) {
		ArrayList<String> formats = new ArrayList<String>();

		// h:mm a
		formats.add(FastDateFormat.getTimeInstance(FastDateFormat.SHORT, locale).getPattern());
		// h:mm:ss a
		formats.add(FastDateFormat.getTimeInstance(FastDateFormat.MEDIUM, locale).getPattern());
		// h:mm:ss a z
		formats.add(FastDateFormat.getTimeInstance(FastDateFormat.LONG, locale).getPattern());
		// h:mm:ss a z
		formats.add(FastDateFormat.getTimeInstance(FastDateFormat.FULL, locale).getPattern());

		// Add other patterns that makes sense
		formats.add(SHORT_TIME_WITH_TIMEZONE);
		formats.add(SHORT_TIME_WITHOUT_AMPM);
		formats.add(TWENTY_FOUR_HOUR_TIME_WITHOUT_AMPM);

		// Add ISO patterns
		formats.add(ISOFormats.ISO_TIME_TIME_ZONE_FORMAT.getPattern());
		formats.add(ISOFormats.ISO_TIME_NO_T_FORMAT.getPattern());
		formats.add(ISOFormats.ISO_TIME_NO_T_TIME_ZONE_FORMAT.getPattern());

		return formats;
	}

	private static List<String> buildDateTimePatterns(Locale locale) {
		ArrayList<String> formats = new ArrayList<String>();

		// M/d/yy h:mm a
		formats.add(FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.SHORT, locale).getPattern());
		// MMM d, yyyy h:mm:ss a
		formats.add(FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.MEDIUM, locale).getPattern());
		// MMMM d, yyyy h:mm:ss a z
		formats.add(FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.LONG, locale).getPattern());
		// EEEE, MMMM d, yyyy h:mm:ss a z
		formats.add(FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.FULL, locale).getPattern());
		// M/d/yy h:mm:ss a
		formats.add(FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.MEDIUM, locale).getPattern());
		// M/d/yy h:mm:ss a z
		formats.add(FastDateFormat.getDateTimeInstance(FastDateFormat.SHORT, FastDateFormat.FULL, locale).getPattern());
		// MMM d, yyyy h:mm a
		formats.add(FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.SHORT, locale).getPattern());
		// MMM d, yyyy h:mm:ss a z
		formats.add(FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.LONG, locale).getPattern());
		// MMM d, yyyy h:mm:ss a z
		formats.add(FastDateFormat.getDateTimeInstance(FastDateFormat.MEDIUM, FastDateFormat.FULL, locale).getPattern());
		// MMMM d, yyyy h:mm a
		formats.add(FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.SHORT, locale).getPattern());
		// MMMM d, yyyy h:mm:ss a
		formats.add(FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.MEDIUM, locale).getPattern());
		// MMMM d, yyyy h:mm:ss a z
		formats.add(FastDateFormat.getDateTimeInstance(FastDateFormat.LONG, FastDateFormat.FULL, locale).getPattern());
		// EEEE, MMMM d, yyyy h:mm a
		formats.add(FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.SHORT, locale).getPattern());
		// EEEE, MMMM d, yyyy h:mm:ss a
		formats.add(FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.MEDIUM, locale).getPattern());
		// EEEE, MMMM d, yyyy h:mm:ss a z
		formats.add(FastDateFormat.getDateTimeInstance(FastDateFormat.FULL, FastDateFormat.LONG, locale).getPattern());

		// Add ISO patterns
		formats.add(ISOFormats.ISO_DATETIME_FORMAT.getPattern());
		formats.add(ISOFormats.ISO_DATETIME_TIME_ZONE_FORMAT_SANS_T.getPattern());
		formats.add(ISOFormats.ISO_DATETIME_TIME_ZONE_FORMAT.getPattern());

		return formats;
	}
}
