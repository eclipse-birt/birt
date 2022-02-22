/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.dataextraction.impl;

import java.util.Locale;
import java.util.Map;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.AutoFormatter;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.IFormatter;
import org.eclipse.birt.core.format.IFormatter.DefaultFormatter;
import org.eclipse.birt.core.format.LocaleNeutralFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.report.engine.api.IDataExtractionOption;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.dataextraction.CommonDataExtractionOption;
import org.eclipse.birt.report.engine.dataextraction.ICommonDataExtractionOption;
import org.eclipse.birt.report.engine.dataextraction.i18n.Messages;
import org.eclipse.birt.report.engine.extension.DataExtractionExtensionBase;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * Base implementation for the data extraction extensions. It provides utility
 * methods which are initialized according to the data extraction options.
 */
public class CommonDataExtractionImpl extends DataExtractionExtensionBase {
	protected String PLUGIN_ID = "org.eclipse.birt.report.engine.dataextraction"; //$NON-NLS-1$

	private IReportContext context;
	private IDataExtractionOption options;
	private DateFormatter dateFormatter = null;
	private ULocale locale = null;
	private TimeZone timeZone = null;
	private boolean isLocaleNeutral;

	private IFormatter[] valueFormatters;

	private Map formatterMap;
	private Map localeNeutralFlags;

	/**
	 * @see org.eclipse.birt.report.engine.extension.IDataExtractionExtension#initialize(org.eclipse.birt.report.engine.api.script.IReportContext,
	 *      org.eclipse.birt.report.engine.api.IDataExtractionOption)
	 */
	@Override
	public void initialize(IReportContext context, IDataExtractionOption options) throws BirtException {
		this.context = context;
		this.options = options;

		if (options.getOutputStream() == null) {
			throw new BirtException(PLUGIN_ID,
					Messages.getString("exception.dataextraction.options.outputstream_required"), null); //$NON-NLS-1$
		}

		initCommonOptions(context, options);
	}

	/**
	 * Initializes the common options based on the data extraction option. If the
	 * passed option doesn't contain common options, use default values.
	 *
	 * @param context
	 * @param options options
	 */
	private void initCommonOptions(IReportContext context, IDataExtractionOption options) {
		String dateFormat;
		ICommonDataExtractionOption commonOptions;
		if (options instanceof ICommonDataExtractionOption) {
			commonOptions = (ICommonDataExtractionOption) options;
		} else {
			commonOptions = new CommonDataExtractionOption(options.getOptions());
		}

		this.isLocaleNeutral = commonOptions.isLocaleNeutralFormat();
		this.localeNeutralFlags = commonOptions.getLocaleNeutralFlags();

		dateFormat = commonOptions.getDateFormat();
		// get locale info
		Locale aLocale = null;
		if (commonOptions.getLocale() != null) {
			aLocale = commonOptions.getLocale();
		} else if (context != null) {
			aLocale = context.getLocale();
		}
		if (aLocale == null) {
			this.locale = ULocale.forLocale(Locale.getDefault());
		} else {
			this.locale = ULocale.forLocale(aLocale);
		}

		java.util.TimeZone javaTimeZone = commonOptions.getTimeZone();
		if (javaTimeZone != null) {
			// convert java time zone to ICU time zone
			this.timeZone = TimeZone.getTimeZone(javaTimeZone.getID());
		} else if (context != null) {
			timeZone = context.getTimeZone();
		} else {
			timeZone = TimeZone.getDefault();
		}

		if (!isLocaleNeutral) {
			dateFormatter = createDateFormatter(dateFormat, this.locale, this.timeZone);
		}
		formatterMap = commonOptions.getFormatter();
	}

	/**
	 * Returns the report context with which this instance has been initialized.
	 *
	 * @return report context instance
	 */
	public IReportContext getReportContext() {
		return context;
	}

	/**
	 * Returns the data extraction options with which this instance has been
	 * initialized
	 *
	 * @return instance of IDataExtractionOption
	 */
	public IDataExtractionOption getOptions() {
		return this.options;
	}

	/**
	 * Must be implemented by subclass.
	 */
	@Override
	public void output(IExtractionResults results) throws BirtException {
		throw new BirtException(PLUGIN_ID, Messages.getString("exception.dataextraction.missing_implementation"), null); //$NON-NLS-1$
	}

	protected void createFormatters(String[] columnNames, int[] columnTypes) {
		int length = columnNames.length;
		valueFormatters = new IFormatter[length];
		String[] patterns = getPatterns(columnNames);
		for (int i = 0; i < length; i++) {
			boolean flag = isColumnLocaleNeutral(columnNames, i);
			if (patterns[i] == null && (isLocaleNeutral || flag)) {
				valueFormatters[i] = new LocaleNeutralFormatter();
			} else {
				switch (columnTypes[i]) {
				case DataType.ANY_TYPE:
				case DataType.UNKNOWN_TYPE:
				case DataType.JAVA_OBJECT_TYPE:
					// auto-format at runtime
					valueFormatters[i] = new AutoFormatter(patterns[i], this.locale, this.timeZone, dateFormatter);
					break;
				case DataType.DATE_TYPE:
				case DataType.SQL_DATE_TYPE:
				case DataType.SQL_TIME_TYPE:
					if (patterns[i] != null) {
						valueFormatters[i] = createDateFormatter(patterns[i], this.locale, this.timeZone);
					} else {
						valueFormatters[i] = dateFormatter;
					}
					break;
				case DataType.DECIMAL_TYPE:
				case DataType.DOUBLE_TYPE:
				case DataType.INTEGER_TYPE:
					valueFormatters[i] = new NumberFormatter(patterns[i], this.locale);
					break;
				case DataType.STRING_TYPE:
					StringFormatter strFormatter = new StringFormatter(patterns[i], this.locale);
					if (patterns[i] == null) {
						strFormatter.setTrim(false);
					}
					valueFormatters[i] = strFormatter;
					break;
				default:
					valueFormatters[i] = new DefaultFormatter(this.locale);
					break;
				}
			}
		}
	}

	private boolean isColumnLocaleNeutral(String[] columnNames, int colIndex) {
		boolean isLocaleNeutral = false;
		if (localeNeutralFlags != null) {
			Object flag = localeNeutralFlags.get(colIndex + 1);
			if (flag == null) {
				flag = localeNeutralFlags.get(columnNames[colIndex]);
			}
			if (Boolean.TRUE.equals(flag)) {
				isLocaleNeutral = true;
			}
		}
		return isLocaleNeutral;
	}

	private String[] getPatterns(String[] columnNames) {
		String[] patterns = new String[columnNames.length];
		if (formatterMap != null && !formatterMap.isEmpty()) {
			for (int i = 0; i < columnNames.length; i++) {
				String pattern = (String) formatterMap.get(i + 1);
				if (pattern == null) {
					pattern = (String) formatterMap.get(columnNames[i]);
				}
				patterns[i] = pattern;
			}
		}
		return patterns;
	}

	/**
	 * Must be implemented by subclass.
	 *
	 * @see org.eclipse.birt.report.engine.extension.IDataExtractionExtension#release()
	 */
	@Override
	public void release() {
	}

	/**
	 * Creates a localized date formatter.
	 *
	 * @param pattern date format string or null for default
	 */
	protected DateFormatter createDateFormatter(String pattern, ULocale locale, TimeZone timeZone) {
		return new DateFormatter(pattern, locale, timeZone);
	}

	/**
	 * Returns the string value by object, according the the isLocaleNeutral option
	 * and the user specified formats(date format, number format, string format), if
	 * available.
	 *
	 * @param dataIterator
	 *
	 * @param columnNames
	 * @param index
	 * @return string representation of the object
	 * @throws BirtException
	 */
	protected String getStringValue(IDataIterator dataIterator, String[] columnNames, int index) throws BirtException {
		Object obj = dataIterator.getValue(columnNames[index]);

		return obj != null ? valueFormatters[index].formatValue(obj) : null;
	}

}
