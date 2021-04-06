/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.toc;

import java.util.Date;
import java.util.HashMap;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

public class TOCFormatUtil {

	private HashMap<String, NumberFormatter> cachedNumberFormats = new HashMap<String, NumberFormatter>();
	private HashMap<String, DateFormatter> cachedDateFormats = new HashMap<String, DateFormatter>();
	private HashMap<String, StringFormatter> cachedStringFormats = new HashMap<String, StringFormatter>();

	private TimeZone timeZone;
	private ULocale locale;

	public TOCFormatUtil(ULocale locale, TimeZone timeZone) {
		this.timeZone = timeZone;
		this.locale = locale;
	}

	private NumberFormatter getNumberFormatter(String format) {
		NumberFormatter formatter = cachedNumberFormats.get(format);
		if (formatter == null) {
			if (format == null) {
				formatter = new NumberFormatter(locale);
			} else {
				formatter = new NumberFormatter(format, locale);
			}
			cachedNumberFormats.put(format, formatter);
		}
		return formatter;
	}

	private StringFormatter getStringFormatter(String format) {
		StringFormatter formatter = cachedStringFormats.get(format);
		if (formatter == null) {
			if (format == null) {
				formatter = new StringFormatter(locale);
			} else {
				formatter = new StringFormatter(format, locale);
			}
			cachedStringFormats.put(format, formatter);
		}
		return formatter;
	}

	private DateFormatter getDateFormatter(String format) {
		DateFormatter formatter = cachedDateFormats.get(format);
		if (formatter == null) {
			if (format == null) {
				formatter = new DateFormatter(locale, timeZone);
			} else {
				formatter = new DateFormatter(format, locale, timeZone);
			}
			cachedDateFormats.put(format, formatter);
		}
		return formatter;
	}

	private String getStringFormat(IScriptStyle style) {
		if (style == null) {
			return null;
		}
		return style.getStringFormat();
	}

	private String getNumberFormat(IScriptStyle style) {
		if (style == null) {
			return null;
		}
		return style.getNumberFormat();
	}

	private String getDateFormat(IScriptStyle style, Date value) {
		if (style != null) {
			return style.getDateFormat();
		}
		return null;
	}

	/**
	 * localize the value to a string.
	 * 
	 * @param value
	 * @param style
	 * @param locale
	 * @return
	 */
	protected String localizeValue(Object value, IScriptStyle style) {
		if (value == null) {
			return null;
		}
		if (value instanceof Number) {
			String format = getNumberFormat(style);
			NumberFormatter fmt = getNumberFormatter(format);
			;
			return fmt.format((Number) value);
		}
		if (value instanceof java.util.Date) {
			String format = getDateFormat(style, (java.util.Date) value);
			DateFormatter fmt = getDateFormatter(format);
			return fmt.format((Date) value);
		} else if (value instanceof String) {
			String format = getStringFormat(style);
			StringFormatter fmt = getStringFormatter(format);
			return fmt.format((String) value);
		}
		return value.toString();
	}
}
