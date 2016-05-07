/**
* Copyright 2007 - 2011 Skyway Software, Inc.
*/
package com.ynswet.common.util.databinding;

import java.beans.PropertyEditor;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.beans.SimpleTypeConverter;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

/**
 * Utility Class that provides a wrapper around the Spring Property Editor approach. This class
 * holds a static SimpleTypeConverter that utilizes the Default Spring PropertyEditors For Type
 * conversion. This Class seeds that SimpleTypeConverter with Additional Converters for Calendar
 * This class also offers support for a few well known format types
 *
 *
 * @author JKennedy
 *
 */
public class TypeConversionUtils {
	public static final String CURRENCY = "(currency)";//$NON-NLS-1$


	/**
	 * Convert the Object passed to Text using any found CustomPropertyEditor first, or one of the
	 * Default Property Editors from Spring
	 *
	 * @param object
	 * @return
	 */
	public static String getAsText(Object object) {
		return getAsText(object, null);
	}

	/**
	 * Convert the Object passed to Text using any found CustomPropertyEditor first, or one of the
	 * Default Property Editors from Spring
	 *
	 * The Spring classes offer some level of caching/custom formatting by path and so this method
	 * will pass the path through
	 *
	 * @param object
	 * @param path
	 * @return The String representation for the Object passed
	 */
	public static String getAsText(Object object, String path) {
		return getAsText(object, path, null);
	}

	/**
	 * Convert the Object passed to Text using the pattern to format
	 *
	 * @param object
	 * @param path
	 * @param pattern
	 * @return A formatted String for the Object passed using the Pattern supplied
	 * @see getPropertyEditor ()
	 */
	public static String getAsText(Object object, String path, String pattern) {
		PropertyEditor editor = null;

		if (object == null)
			return null;

		editor = getPropertyEditor(object, path, pattern);

		if (editor != null) {
			editor.setValue(object);
			return editor.getAsText();
		}

		return object.toString();
	}

	/**
	 * Attempts to load a Property Editor from the Spring SimpleTypeConverter This method will first
	 * attempt to load a Custom Editor for the Object / Path passed If not custom editor is found,
	 * it will attempt to load a Default Editor
	 *
	 * @param object
	 * @return A Property Editor for editing the property passed on the Object passed
	 * @see SimpleTypeConverter.findCustomEditor
	 * @see SimpleTypeConverter.getDefaultEditor
	 */
	public static PropertyEditor getPropertyEditor(Object object, String propertyPath) {
		if (object == null)
			return null;

		PropertyEditor editor = getTypeConverter().findCustomEditor(object.getClass(), propertyPath);

		if (editor == null)
			editor = getTypeConverter().getDefaultEditor(object.getClass());

		return editor;

	}

	/**
	 * Returns a Property Editor for the property specified in the Object passed Attempts to return
	 * a Property Editor that has been seeded with the pattern passed to enable calls to getAsText()
	 * that would return formatted text. Since most property editors can not have their pattern set
	 * outside of their constructor, this method will attempt to create a new instance of the
	 * appropriate property editor with the pattern passed If the pattern is null, this method is
	 * equivilant to getPropertyEditor(Object, path)
	 *
	 * @param object
	 * @param path
	 * @param pattern
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static PropertyEditor getPropertyEditor(Object object, String path, String pattern) {
		if (object == null)
			return null;

		// Get the default property editor for this object/path
		PropertyEditor editor = getPropertyEditor(object, path);
		PropertyEditor formatter = null;

		// If there is no pattern, just use the default getAsText method from the Property Editor
		if (pattern == null) {
			return editor;
		}

		// If there is no editor registered, then return null
		if (editor == null)
			return null;

		// Temporary Fix - Remove any known extrinsic data from the pattern. See
		// org.skyway.common.ui.controls.Formats.
		pattern = StringUtils.remove(pattern, " (ISO-8601)");//$NON-NLS-1$

		// If there is a pattern, attempt to create a new Property Editor extension with the right
		// pattern using the type of editor returned rather than the Object type to
		// avoid recreating the logic that derives an Editor type for a given Object type
		if (editor instanceof CustomCalendarEditor) {
			formatter = new CustomCalendarEditor(pattern, getLocale());
		} else if (editor instanceof CustomNumberEditor) {
			formatter = new CustomNumberEditor((Class<? extends Number>) object.getClass(), getDecimalFormat(pattern), true);
		} else if (editor instanceof EnhancedBooleanEditor) {
			formatter = new EnhancedBooleanEditor(pattern, ((EnhancedBooleanEditor) editor).isAllowEmpty());
		}

		return formatter;
	}

	/**
	 * Creates a DecimalFormat for the pattern passed This method will consider well known
	 * formatting "KEYS" like (currency)
	 *
	 * @see CURRENCY
	 * @return
	 */
	public static DecimalFormat getDecimalFormat(String pattern) {
		NumberFormat nf = null;

		if ((pattern != null) && pattern.equals(CURRENCY)) {
			nf = NumberFormat.getCurrencyInstance(getLocale());
		} else {
			nf = NumberFormat.getNumberInstance(getLocale());
			((DecimalFormat) nf).applyPattern(pattern);
		}


		return ((DecimalFormat) nf);
	}


	/**
	 * Utilizes the Spring Infrastructure for determining the most appropriate Locale
	 *
	 * @see LocaleContextHolder.getLocale()
	 * @return
	 */
	public static Locale getLocale() {
		return LocaleContextHolder.getLocale();
	}

	/**
	 * Creates a Simple Type Converter and seeds it with Skyway Custom Editors
	 */
	private static SimpleTypeConverter getTypeConverter() {
		// TODO - This seems really expensive because we do this each time we invoke
		// getAsText(Object object, String path, String pattern). We should look at how Spring
		// optimizes around this.
		SimpleTypeConverter simpleTypeConverter = new SimpleTypeConverter();
		registerSkywayCustomEditors(simpleTypeConverter);
		return simpleTypeConverter;
	}

	/**
	 * Utility method to ensure that the PropertyEditorRegistrySupport passed is seeded with Skyway
	 * CustomPropertyEditors
	 *
	 * @param typeConverter
	 */
	public static void registerSkywayCustomEditors(PropertyEditorRegistry typeConverter) {
		// Add the CustomCalendarEditor to the TypeConverter
		typeConverter.registerCustomEditor(Calendar.class, new CustomCalendarEditor());
		typeConverter.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor ());
		typeConverter.registerCustomEditor(boolean.class, new EnhancedBooleanEditor(false));
		typeConverter.registerCustomEditor(Boolean.class, new EnhancedBooleanEditor(true));
	}

	/**
	 * Clears the date fields on the specified calendar collection.
	 *
	 * @param c
	 */
	public static void clearTimeFields(Set<Calendar> c) {
		for (Calendar cal : c) {
			clearTimeFields(cal);
		}
	}

	/**
	 * Clears the time fields on the specified calendar.
	 *
	 * @param c
	 */
	public static void clearTimeFields(Calendar c) {
		if (c != null) {
			c.clear(Calendar.AM);
			c.clear(Calendar.DST_OFFSET);
			c.clear(Calendar.HOUR);
			c.clear(Calendar.HOUR_OF_DAY);
			c.clear(Calendar.MILLISECOND);
			c.clear(Calendar.SECOND);
			c.clear(Calendar.MINUTE);
			c.clear(Calendar.ZONE_OFFSET);
		}
	}

	/**
	 * Clears the date fields on the specified calendar collection.
	 *
	 * @param c
	 */
	public static void clearDateFields(Set<Calendar> c) {
		for (Calendar cal : c) {
			clearDateFields(cal);
		}
	}

	/**
	 * Clears the date fields on the specified calendar.
	 *
	 * @param c
	 */
	public static void clearDateFields(Calendar c) {
		if (c != null) {
			c.clear(Calendar.DATE);
			c.clear(Calendar.DAY_OF_MONTH);
			c.clear(Calendar.DAY_OF_WEEK);
			c.clear(Calendar.DAY_OF_WEEK_IN_MONTH);
			c.clear(Calendar.DAY_OF_YEAR);
			c.clear(Calendar.MONTH);
			c.clear(Calendar.YEAR);
			c.clear(Calendar.WEEK_OF_MONTH);
			c.clear(Calendar.WEEK_OF_YEAR);
		}
	}
}
