/**
* Copyright 2007 - 2011 Skyway Software, Inc.
*/
package com.ynswet.common.util.databinding;

import java.beans.PropertyEditorSupport;

public class StringEditor extends PropertyEditorSupport {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyEditorSupport#setAsText(java.lang.String)
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(text);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyEditorSupport#getAsText()
	 */
	@Override
	public String getAsText() {
		if (getValue() == null)
			return "";
		return getValue().toString();
	}
}
