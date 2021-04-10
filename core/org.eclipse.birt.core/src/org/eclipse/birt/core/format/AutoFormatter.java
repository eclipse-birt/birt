/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.format;

import org.eclipse.birt.core.exception.BirtException;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * 
 */

public class AutoFormatter implements IFormatter {

	private String pattern;
	private ULocale locale;
	private TimeZone timeZone;

	private IFormatter directFormatter = null;
	private DateFormatter defaultDateFormatter;

	public AutoFormatter(String pattern, ULocale locale, TimeZone timeZone) {
		this.pattern = pattern;
		this.locale = locale;
		this.timeZone = timeZone;
	}

	public AutoFormatter(String pattern, ULocale locale, TimeZone timeZone, DateFormatter defaultDateFormatter) {
		this(pattern, locale, timeZone);
		this.defaultDateFormatter = defaultDateFormatter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.core.format.IFormatter#formatValue(java.lang.Object)
	 */
	public String formatValue(Object value) throws BirtException {
		if (directFormatter == null) {
			// implicitly includes its child classes java.sql.Date,
			// java.sql.Time and java.sql.Timestamp
			if (value instanceof java.util.Date) {
				if (pattern != null || defaultDateFormatter == null) {
					directFormatter = new DateFormatter(pattern, this.locale, this.timeZone);
				} else {
					directFormatter = defaultDateFormatter;
				}
			} else if (value instanceof Number) {
				directFormatter = new NumberFormatter(pattern, this.locale);
			} else if (pattern != null && value instanceof String) {
				directFormatter = new StringFormatter(pattern, this.locale);
			} else {
				directFormatter = new DefaultFormatter(locale);
			}
		}
		return directFormatter.formatValue(value);
	}
}
