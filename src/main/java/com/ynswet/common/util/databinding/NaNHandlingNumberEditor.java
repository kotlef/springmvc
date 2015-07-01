/**
* Copyright 2007 - 2011 Skyway Software, Inc.
*/
package com.ynswet.common.util.databinding;

import java.text.NumberFormat;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.CustomNumberEditor;

/**
 * Number editor that sets the value to null when the value received
 * is NaN.  DOJO likes to pass up NaN for numbers even if you just tab
 * through a field without making any changes.
 * 
 * @author jperkins
 *
 */
@SuppressWarnings("unchecked")
public class NaNHandlingNumberEditor extends CustomNumberEditor {
	private static final String NaN = "NaN"; //$NON-NLS-1$
	
	public NaNHandlingNumberEditor(Class numberClass, boolean allowEmpty) throws IllegalArgumentException {
		super(numberClass, allowEmpty);
	}

	public NaNHandlingNumberEditor(Class numberClass, NumberFormat numberFormat, boolean allowEmpty) throws IllegalArgumentException {
		super(numberClass, numberFormat, allowEmpty);
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (StringUtils.equals(text, NaN)){ 
			setValue(null);
			return;
		}else{
			super.setAsText(text);
		}
	}
}
