/**
* Copyright 2007 - 2011 Skyway Software, Inc.
*/
package com.ynswet.common.util.databinding;

import java.beans.PropertyEditorSupport;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.propertyeditors.CustomBooleanEditor;

/**
 * A slightly modified version of {@link CustomBooleanEditor} that supports more formats.
 * 
 * @author Michael Weaver
 */
public class EnhancedBooleanEditor extends PropertyEditorSupport {
	public static final String VALUE_TRUE = "true"; //$NON-NLS-1$
	public static final String VALUE_FALSE = "false";//$NON-NLS-1$
	
	protected static final Set<String> TRUE = new HashSet<String>();
	protected static final Set<String> FALSE = new HashSet<String>();
	static {
		TRUE.add("on");//$NON-NLS-1$
		TRUE.add("true");//$NON-NLS-1$
		TRUE.add("yes");//$NON-NLS-1$
		TRUE.add("1");//$NON-NLS-1$
		TRUE.add("y");//$NON-NLS-1$
		TRUE.add("t");//$NON-NLS-1$
		FALSE.add("off");//$NON-NLS-1$
		FALSE.add("false");//$NON-NLS-1$
		FALSE.add("no");//$NON-NLS-1$
		FALSE.add("0");//$NON-NLS-1$
		FALSE.add("n");//$NON-NLS-1$
		FALSE.add("f");//$NON-NLS-1$
	}

	public static final String ON_OFF = "on/off";//$NON-NLS-1$
	public static final String YES_NO = "yes/no";//$NON-NLS-1$
	public static final String ONE_ZERO = "1/0";//$NON-NLS-1$
	public static final String TRUE_FALSE = "True/False";//$NON-NLS-1$
	public static final String Y_N = "Y/N";//$NON-NLS-1$
	public static final String T_F = "T/F";//$NON-NLS-1$

	protected final String trueString;
	protected final String falseString;
	protected final boolean allowEmpty;

	public EnhancedBooleanEditor(boolean allowEmpty) {
		this(null, null, allowEmpty);
	}

	public EnhancedBooleanEditor(String pattern, boolean allowEmpty) {
		if (ON_OFF.equals(pattern)) {
			this.trueString = "on";//$NON-NLS-1$
			this.falseString = "off";//$NON-NLS-1$
		} else if (YES_NO.equals(pattern)) {
			this.trueString = "yes";//$NON-NLS-1$
			this.falseString = "no";//$NON-NLS-1$
		} else if (ONE_ZERO.equals(pattern)) {
			this.trueString = "1";//$NON-NLS-1$
			this.falseString = "0";//$NON-NLS-1$
		} else if (TRUE_FALSE.equals(pattern)) {
			this.trueString = "True";//$NON-NLS-1$
			this.falseString = "False";//$NON-NLS-1$
		} else if (Y_N.equals(pattern)) {
			this.trueString = "Y";//$NON-NLS-1$
			this.falseString = "N";//$NON-NLS-1$
		} else if (T_F.equals(pattern)) {
			this.trueString = "T";//$NON-NLS-1$
			this.falseString = "F";//$NON-NLS-1$
		} else {
			this.trueString = "true";//$NON-NLS-1$
			this.falseString = "false";//$NON-NLS-1$
		}
		this.allowEmpty = allowEmpty;
	}

	public EnhancedBooleanEditor(String trueString, String falseString, boolean allowEmpty) {
		this.trueString = trueString;
		this.falseString = falseString;
		this.allowEmpty = allowEmpty;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		String input = (text != null ? text.trim() : null);
		String lcInput = input == null ? null : input.toLowerCase();
		if (this.allowEmpty && org.apache.commons.lang.StringUtils.isBlank(input)) {
			// Treat empty String as null value.
			setValue(null);
		} else if (this.trueString != null && input.equalsIgnoreCase(this.trueString)) {
			setValue(Boolean.TRUE);
		} else if (this.falseString != null && input.equalsIgnoreCase(this.falseString)) {
			setValue(Boolean.FALSE);
		} else if (this.trueString == null && TRUE.contains(lcInput)) {
			setValue(Boolean.TRUE);
		} else if (this.falseString == null && FALSE.contains(lcInput)) {
			setValue(Boolean.FALSE);
		} else {
			throw new IllegalArgumentException("Invalid boolean value [" + text + "]");//$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyEditorSupport#getAsText()
	 */
	@Override
	public String getAsText() {
		if (Boolean.TRUE.equals(getValue())) {
			return (this.trueString != null ? this.trueString : CustomBooleanEditor.VALUE_TRUE);
		} else if (Boolean.FALSE.equals(getValue())) {
			return (this.falseString != null ? this.falseString : CustomBooleanEditor.VALUE_FALSE);
		} else {
			return "";//$NON-NLS-1$
		}
	}

	public boolean isAllowEmpty() {
		return allowEmpty;
	}
}
