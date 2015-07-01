/**
* Copyright 2007 - 2011 Skyway Software, Inc.
*/
package com.ynswet.common.util.databinding;

import java.util.Calendar;
import java.util.Date;

import org.springframework.binding.convert.service.DefaultConversionService;

public class EnhancedConversionService extends DefaultConversionService {

	protected void addDefaultConverters() {
		super.addDefaultConverters();

		// registers a custom converter reference-able by id and applied when requested
		addConverter(new StringToCalendar());
		addConverter(new StringToDate());
	}

	protected void addDefaultAliases() {
		super.addDefaultAliases();
		addAlias("calendar", Calendar.class); //$NON-NLS-1$
		addAlias("date", Date.class); //$NON-NLS-1$
	}
}
