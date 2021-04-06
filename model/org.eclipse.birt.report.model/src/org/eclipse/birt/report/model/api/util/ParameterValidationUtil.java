/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.metadata.ValidationValueException;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.metadata.BooleanPropertyType;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * Validates the parameter value with the given data type and format pattern
 * string. This util class can validate the parameter of the following types:
 * 
 * <ul>
 * <li><code>PARAM_TYPE_DATETIME</code></li>
 * <li><code>PARAM_TYPE_FLOAT</code></li>
 * <li><code>PARAM_TYPE_DECIMAL</code></li>
 * <li><code>PARAM_TYPE_BOOLEAN</code></li>
 * <li><code>PARAM_TYPE_STRING</code></li>
 * <li><code>PARAM_TYPE_INTEGER</code></li>
 * <li><code>PARAM_TYPE_DATE</code></li>
 * <li><code>PARAM_TYPE_TIME</code></li>
 * </ul>
 * 
 * @see org.eclipse.birt.report.model.api.elements.DesignChoiceConstants
 */

public class ParameterValidationUtil {

	/**
	 * Default locale of the validation issues. If the caller does not provide the
	 * locale information, we will use it.
	 */

	private static final ULocale DEFAULT_LOCALE = ULocale.US;

	/**
	 * Default date-time format string.
	 */

	public static final String DEFAULT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"; //$NON-NLS-1$
	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd"; //$NON-NLS-1$
	public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss"; //$NON-NLS-1$

	public static final String DISPLAY_DATE_FORMAT = "Medium Date"; //$NON-NLS-1$
	public static final String DISPLAY_TIME_FORMAT = "Medium Time"; //$NON-NLS-1$

	private static final int DATETIME_FORMAT_TYPE = 0;
	private static final int DATE_FORMAT_TYPE = 1;
	private static final int TIME_FORMAT_TYPE = 2;
	private static DateFormatter[] defaultDateFormatters = null;
	private static NumberFormatter defaultNumberFormatter = null;
	private static StringFormatter defaultStringFormatter = null;

	static {
		// initialize the default formatters

		// date-time formatter
		defaultDateFormatters = new DateFormatter[3];
		DateFormatter dateFormatter = new DateFormatter(DEFAULT_LOCALE);
		dateFormatter.applyPattern(DEFAULT_DATETIME_FORMAT);
		defaultDateFormatters[DATETIME_FORMAT_TYPE] = dateFormatter;

		// date formatter
		dateFormatter = new DateFormatter(DEFAULT_LOCALE);
		dateFormatter.applyPattern(DEFAULT_DATE_FORMAT);
		defaultDateFormatters[DATE_FORMAT_TYPE] = dateFormatter;

		// time formatter
		dateFormatter = new DateFormatter(DEFAULT_LOCALE);
		dateFormatter.applyPattern(DEFAULT_TIME_FORMAT);
		defaultDateFormatters[TIME_FORMAT_TYPE] = dateFormatter;

		// number formatter
		defaultNumberFormatter = new NumberFormatter(DEFAULT_LOCALE);

		// string formatter
		defaultStringFormatter = new StringFormatter(DEFAULT_LOCALE);
		defaultStringFormatter.setTrim(false);
	}

	/**
	 * Validates a input parameter value with the given data type. The returned
	 * value is locale and format independent. The data type can be one of the
	 * following:
	 * 
	 * <ul>
	 * <li><code>PARAM_TYPE_DATETIME</code></li>
	 * <li><code>PARAM_TYPE_FLOAT</code></li>
	 * <li><code>PARAM_TYPE_DECIMAL</code></li>
	 * <li><code>PARAM_TYPE_BOOLEAN</code></li>
	 * <li><code>PARAM_TYPE_STRING</code></li>
	 * <li><code>PARAM_TYPE_DATE</code></li>
	 * <li><code>PARAM_TYPE_TIME</code></li>
	 * </ul>
	 * 
	 * @param dataType the data type of the value
	 * @param value    the input value to validate
	 * @param locale   the locale information
	 * @return the validated value if the input value is valid for the given data
	 *         type
	 * @throws ValidationValueException if the input value is not valid with the
	 *                                  given data type
	 */

	static private Object validate(String dataType, String value, ULocale locale) throws ValidationValueException {
		return validate(dataType, value, locale, null);
	}

	/**
	 * Validates a input parameter value with the given data type. The returned
	 * value is locale and format independent. The data type can be one of the
	 * following:
	 * 
	 * <ul>
	 * <li><code>PARAM_TYPE_DATETIME</code></li>
	 * <li><code>PARAM_TYPE_FLOAT</code></li>
	 * <li><code>PARAM_TYPE_DECIMAL</code></li>
	 * <li><code>PARAM_TYPE_BOOLEAN</code></li>
	 * <li><code>PARAM_TYPE_STRING</code></li>
	 * <li><code>PARAM_TYPE_DATE</code></li>
	 * <li><code>PARAM_TYPE_TIME</code></li>
	 * </ul>
	 * 
	 * @param dataType the data type of the value
	 * @param value    the input value to validate
	 * @param locale   the locale information
	 * @param timeZone the time zone information (only for DateTime type)
	 * @return the validated value if the input value is valid for the given data
	 *         type
	 * @throws ValidationValueException if the input value is not valid with the
	 *                                  given data type
	 */

	static private Object validate(String dataType, String value, ULocale locale, TimeZone timeZone)
			throws ValidationValueException {
		if (value == null)
			return null;

		if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equalsIgnoreCase(dataType))
			return doValidateDateTime(value, locale, timeZone);
		else if (DesignChoiceConstants.PARAM_TYPE_DATE.equalsIgnoreCase(dataType)) {
			try {
				return new java.sql.Date(DataTypeUtil.toDate(value, locale).getTime());
			} catch (Exception e) {
				try {
					return java.sql.Date.valueOf(value);
				} catch (Exception err) {
					throw new ValidationValueException(value, ValidationValueException.DESIGN_EXCEPTION_INVALID_VALUE,
							dataType);
				}
			}
		} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equalsIgnoreCase(dataType)) {
			try {
				return new java.sql.Time(DataTypeUtil.toDate(value, locale).getTime());
			} catch (Exception e) {
				try {
					return java.sql.Time.valueOf(value);
				} catch (Exception err) {
					throw new ValidationValueException(value, ValidationValueException.DESIGN_EXCEPTION_INVALID_VALUE,
							dataType);
				}
			}
		} else if (DesignChoiceConstants.PARAM_TYPE_FLOAT.equalsIgnoreCase(dataType)) {
			Number number = doValidateNumber(dataType, value, locale);
			if (number == null)
				return null;
			return new Double(number.doubleValue());
		} else if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equalsIgnoreCase(dataType)) {
			Number number = doValidateNumber(dataType, value, locale);
			if (number == null)
				return null;
			if (number instanceof BigDecimal) {
				return number;
			}

			return new BigDecimal(number.toString());

		} else if (DesignChoiceConstants.PARAM_TYPE_INTEGER.equalsIgnoreCase(dataType)) {
			Number number = doValidateNumber(dataType, value, locale);
			if (number == null)
				return null;
			return Integer.valueOf(number.intValue());
		} else if (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equalsIgnoreCase(dataType)) {
			return doValidateBoolean(value, locale);
		} else if (DesignChoiceConstants.PARAM_TYPE_STRING.equalsIgnoreCase(dataType)) {
			return value;
		} else {
			assert false;
			return null;
		}

	}

	/**
	 * Validates the input value at the given locale. The format is: short date and
	 * medium time.
	 * 
	 * @param value    the value to validate
	 * @param locale   the locale information
	 * @param timeZone the time zone information
	 * @return the date value if validation is successful
	 * @throws ValidationValueException if the value is invalid
	 */

	private static final Date doValidateDateTime(String value, ULocale locale, TimeZone timeZone)
			throws ValidationValueException {
		try {
			return DataTypeUtil.toDate(value, locale, timeZone);
		} catch (BirtException e) {
			throw new ValidationValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					DesignChoiceConstants.PARAM_TYPE_DATETIME);
		}
	}

	/**
	 * Validates the input value at the given locale. The format is: general number.
	 * 
	 * @param dataType the data type
	 * @param value    the value to validate
	 * @param locale   the locale information
	 * @return the double value if validation is successful
	 * @throws ValidationValueException if the value is invalid
	 */

	static final Number doValidateNumber(String dataType, String value, ULocale locale)
			throws ValidationValueException {
		assert DesignChoiceConstants.PARAM_TYPE_FLOAT.equalsIgnoreCase(dataType)
				|| DesignChoiceConstants.PARAM_TYPE_DECIMAL.equalsIgnoreCase(dataType)
				|| DesignChoiceConstants.PARAM_TYPE_INTEGER.equalsIgnoreCase(dataType);
		value = StringUtil.trimString(value);
		if (value == null)
			return null;

		NumberFormat localeFormatter = null;

		if (DesignChoiceConstants.PARAM_TYPE_INTEGER.equalsIgnoreCase(dataType)) {
			localeFormatter = NumberFormat.getIntegerInstance(locale.toLocale());
		} else if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equalsIgnoreCase(dataType)) {
			localeFormatter = NumberFormat.getNumberInstance(locale.toLocale());

			if (localeFormatter instanceof DecimalFormat) {
				((DecimalFormat) localeFormatter).setParseBigDecimal(true);
			}
		} else {
			localeFormatter = NumberFormat.getNumberInstance(locale.toLocale());
		}

		try {
			// Parse in locale-dependent way.
			// Use the decimal separator from the locale.

			return localeFormatter.parse(value);
		} catch (ParseException e) {
			throw new ValidationValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, dataType);
		}

	}

	/**
	 * Validates a input parameter value with the given data type, format choice
	 * string. The returned value is locale and pattern dependent. The data type and
	 * the format can be one pair of the following:
	 * <p>
	 * <table border="1" cellpadding="0" cellspacing="0" style="border-collapse: *
	 * collapse" bordercolor="#111111" width="36%" id="AutoNumber1">
	 * <tr>
	 * <td width="16%">Data Type</td>
	 * <td width="84%">Format Type</td>
	 * </tr>
	 * <tr>
	 * <td width="16%">Float/Decimal</td>
	 * <td width="84%">
	 * <ul>
	 * <li>General Number</li>
	 * <li>Currency</li>
	 * <li>Fixed</li>
	 * <li>Percent</li>
	 * <li>Scientific</li>
	 * <li>Standard</li>
	 * <li>pattern string, such as "###,##0", "###,##0.00 'm/s'", "###.#\';';#" and
	 * so on.</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td width="16%">Date time</td>
	 * <td width="84%">
	 * <ul>
	 * <li>General Date</li>
	 * <li>Long Date</li>
	 * <li>Medium Date</li>
	 * <li>Short Date</li>
	 * <li>Long Time</li>
	 * <li>Medium Time</li>
	 * <li>Short Time</li>
	 * <li>pattern string, such as "MM/dd/yyyy hh:mm:ss a", "yyyy-MM-dd HH:mm:ss"
	 * and so on.</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td width="16%">String</td>
	 * <td width="84%">
	 * <ul>
	 * <li>Upper case</li>
	 * <li>Lower case</li>
	 * <li>pattern string, such as "lt!" and so on.</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * </table>
	 * 
	 * @param dataType the data type of the value
	 * @param format   the format choice string
	 * @param value    the input value to validate
	 * @param locale   the locale information
	 * @return the validated value if the input value is valid for the given data
	 *         type and format choice string
	 * @throws ValidationValueException if the input value is not valid with the
	 *                                  given data type and format string
	 */

	static public Object validate(String dataType, String format, String value, Locale locale)
			throws ValidationValueException {
		return validate(dataType, format, value, ULocale.forLocale(locale));
	}

	/**
	 * Validates a input parameter value with the given data type, format choice
	 * string. The returned value is locale and pattern dependent. The data type and
	 * the format can be one pair of the following:
	 * <p>
	 * <table border="1" cellpadding="0" cellspacing="0" style="border-collapse: *
	 * collapse" bordercolor="#111111" width="36%" id="AutoNumber1">
	 * <tr>
	 * <td width="16%">Data Type</td>
	 * <td width="84%">Format Type</td>
	 * </tr>
	 * <tr>
	 * <td width="16%">Float/Decimal</td>
	 * <td width="84%">
	 * <ul>
	 * <li>General Number</li>
	 * <li>Currency</li>
	 * <li>Fixed</li>
	 * <li>Percent</li>
	 * <li>Scientific</li>
	 * <li>Standard</li>
	 * <li>pattern string, such as "###,##0", "###,##0.00 'm/s'", "###.#\';';#" and
	 * so on.</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td width="16%">Date time</td>
	 * <td width="84%">
	 * <ul>
	 * <li>General Date</li>
	 * <li>Long Date</li>
	 * <li>Medium Date</li>
	 * <li>Short Date</li>
	 * <li>Long Time</li>
	 * <li>Medium Time</li>
	 * <li>Short Time</li>
	 * <li>pattern string, such as "MM/dd/yyyy hh:mm:ss a", "yyyy-MM-dd HH:mm:ss"
	 * and so on.</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td width="16%">String</td>
	 * <td width="84%">
	 * <ul>
	 * <li>Upper case</li>
	 * <li>Lower case</li>
	 * <li>pattern string, such as "lt!" and so on.</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * </table>
	 * 
	 * @param dataType the data type of the value
	 * @param format   the format choice string
	 * @param value    the input value to validate
	 * @param locale   the locale information
	 * @param timeZone the time zone information
	 * @return the validated value if the input value is valid for the given data
	 *         type and format choice string
	 * @throws ValidationValueException if the input value is not valid with the
	 *                                  given data type and format string
	 */

	static public Object validate(String dataType, String format, String value, Locale locale, TimeZone timeZone)
			throws ValidationValueException {
		return validate(dataType, format, value, ULocale.forLocale(locale), timeZone);
	}

	/**
	 * Validates a input parameter value with the given data type, format choice
	 * string, using the default locale. The returned value is locale and pattern
	 * dependent. The data type and the format can be one pair of the following:
	 * <p>
	 * <table border="1" cellpadding="0" cellspacing="0" style="border-collapse: *
	 * collapse" bordercolor="#111111" width="36%" id="AutoNumber1">
	 * <tr>
	 * <td width="16%">Data Type</td>
	 * <td width="84%">Format Type</td>
	 * </tr>
	 * <tr>
	 * <td width="16%">Float/Decimal</td>
	 * <td width="84%">
	 * <ul>
	 * <li>General Number</li>
	 * <li>Currency</li>
	 * <li>Fixed</li>
	 * <li>Percent</li>
	 * <li>Scientific</li>
	 * <li>Standard</li>
	 * <li>pattern string, such as "###,##0", "###,##0.00 'm/s'", "###.#\';';#" and
	 * so on.</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td width="16%">Date time</td>
	 * <td width="84%">
	 * <ul>
	 * <li>General Date</li>
	 * <li>Long Date</li>
	 * <li>Medium Date</li>
	 * <li>Short Date</li>
	 * <li>Long Time</li>
	 * <li>Medium Time</li>
	 * <li>Short Time</li>
	 * <li>pattern string, such as "MM/dd/yyyy hh:mm:ss a", "yyyy-MM-dd HH:mm:ss"
	 * and so on.</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td width="16%">String</td>
	 * <td width="84%">
	 * <ul>
	 * <li>Upper case</li>
	 * <li>Lower case</li>
	 * <li>pattern string, such as "lt!" and so on.</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * </table>
	 * 
	 * @param dataType the data type of the value
	 * @param format   the format choice string
	 * @param value    the input value to validate
	 * @param timeZone the time zone information
	 * @return the validated value if the input value is valid for the given data
	 *         type and format choice string
	 * @throws ValidationValueException if the input value is not valid with the
	 *                                  given data type and format string
	 */

	static public Object validate(String dataType, String format, String value, TimeZone timeZone)
			throws ValidationValueException {
		return validate(dataType, format, value, DEFAULT_LOCALE, timeZone);
	}

	/**
	 * Validates a input parameter value with the given data type, format choice
	 * string. The returned value is locale and pattern dependent. The data type and
	 * the format can be one pair of the following:
	 * <p>
	 * <table border="1" cellpadding="0" cellspacing="0" style="border-collapse: *
	 * collapse" bordercolor="#111111" width="36%" id="AutoNumber1">
	 * <tr>
	 * <td width="16%">Data Type</td>
	 * <td width="84%">Format Type</td>
	 * </tr>
	 * <tr>
	 * <td width="16%">Float/Decimal</td>
	 * <td width="84%">
	 * <ul>
	 * <li>General Number</li>
	 * <li>Currency</li>
	 * <li>Fixed</li>
	 * <li>Percent</li>
	 * <li>Scientific</li>
	 * <li>Standard</li>
	 * <li>pattern string, such as "###,##0", "###,##0.00 'm/s'", "###.#\';';#" and
	 * so on.</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td width="16%">Date time</td>
	 * <td width="84%">
	 * <ul>
	 * <li>General Date</li>
	 * <li>Long Date</li>
	 * <li>Medium Date</li>
	 * <li>Short Date</li>
	 * <li>Long Time</li>
	 * <li>Medium Time</li>
	 * <li>Short Time</li>
	 * <li>pattern string, such as "MM/dd/yyyy hh:mm:ss a", "yyyy-MM-dd HH:mm:ss"
	 * and so on.</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td width="16%">String</td>
	 * <td width="84%">
	 * <ul>
	 * <li>Upper case</li>
	 * <li>Lower case</li>
	 * <li>pattern string, such as "lt!" and so on.</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * </table>
	 * 
	 * @param dataType the data type of the value
	 * @param format   the format choice string
	 * @param value    the input value to validate
	 * @param locale   the locale information
	 * @return the validated value if the input value is valid for the given data
	 *         type and format choice string
	 * @throws ValidationValueException if the input value is not valid with the
	 *                                  given data type and format string
	 */

	static public Object validate(String dataType, String format, String value, ULocale locale)
			throws ValidationValueException {
		return validate(dataType, format, value, locale, null);
	}

	/**
	 * Validates a input parameter value with the given data type, format choice
	 * string. The returned value is locale and pattern dependent. The data type and
	 * the format can be one pair of the following:
	 * <p>
	 * <table border="1" cellpadding="0" cellspacing="0" style="border-collapse: *
	 * collapse" bordercolor="#111111" width="36%" id="AutoNumber1">
	 * <tr>
	 * <td width="16%">Data Type</td>
	 * <td width="84%">Format Type</td>
	 * </tr>
	 * <tr>
	 * <td width="16%">Float/Decimal</td>
	 * <td width="84%">
	 * <ul>
	 * <li>General Number</li>
	 * <li>Currency</li>
	 * <li>Fixed</li>
	 * <li>Percent</li>
	 * <li>Scientific</li>
	 * <li>Standard</li>
	 * <li>pattern string, such as "###,##0", "###,##0.00 'm/s'", "###.#\';';#" and
	 * so on.</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td width="16%">Date time</td>
	 * <td width="84%">
	 * <ul>
	 * <li>General Date</li>
	 * <li>Long Date</li>
	 * <li>Medium Date</li>
	 * <li>Short Date</li>
	 * <li>Long Time</li>
	 * <li>Medium Time</li>
	 * <li>Short Time</li>
	 * <li>pattern string, such as "MM/dd/yyyy hh:mm:ss a", "yyyy-MM-dd HH:mm:ss"
	 * and so on.</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td width="16%">String</td>
	 * <td width="84%">
	 * <ul>
	 * <li>Upper case</li>
	 * <li>Lower case</li>
	 * <li>pattern string, such as "lt!" and so on.</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * </table>
	 * 
	 * @param dataType the data type of the value
	 * @param format   the format choice string
	 * @param value    the input value to validate
	 * @param locale   the locale information
	 * @param timeZone the time zone information (only valid for DateTime type)
	 * @return the validated value if the input value is valid for the given data
	 *         type and format choice string
	 * @throws ValidationValueException if the input value is not valid with the
	 *                                  given data type and format string
	 */

	static public Object validate(String dataType, String format, String value, ULocale locale, TimeZone timeZone)
			throws ValidationValueException {
		if (value == null)
			return null;

		if (StringUtil.isBlank(format))
			return validate(dataType, value, locale, timeZone);

		String newFormat = transformDateFormat(dataType, format, value);
		try {

			if (DesignChoiceConstants.PARAM_TYPE_DATE.equalsIgnoreCase(dataType)) {
				try {
					// no time zone for java.sql.Date
					return new java.sql.Date(doValidateDateTimeByPattern(newFormat, value, locale, null).getTime());
				} catch (Exception e) {
					try {
						return java.sql.Date.valueOf(value);
					} catch (Exception err) {
						throw new ValidationValueException(value,
								ValidationValueException.DESIGN_EXCEPTION_INVALID_VALUE, dataType);
					}
				}
			} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equalsIgnoreCase(dataType)) {
				try {
					// no time zone for java.sql.Time
					return new java.sql.Time(doValidateDateTimeByPattern(newFormat, value, locale, null).getTime());
				} catch (Exception e) {
					try {
						return java.sql.Time.valueOf(value);
					} catch (Exception err) {
						throw new ValidationValueException(value,
								ValidationValueException.DESIGN_EXCEPTION_INVALID_VALUE, dataType);
					}
				}
			}
			if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equalsIgnoreCase(dataType))
				// time zone is only supported for the DataTime type
				return doValidateDateTimeByPattern(newFormat, value, locale, timeZone);
			else if (DesignChoiceConstants.PARAM_TYPE_FLOAT.equalsIgnoreCase(dataType)) {
				Number number = doValidateNumberByPattern(dataType, newFormat, value, locale);
				if (number == null)
					return null;
				return new Double(number.doubleValue());
			} else if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equalsIgnoreCase(dataType)) {
				Number number = doValidateNumberByPattern(dataType, newFormat, value, locale);
				if (number == null)
					return null;
				if (number instanceof BigDecimal) {
					return number;
				}

				return new BigDecimal(number.toString());

			} else if (DesignChoiceConstants.PARAM_TYPE_INTEGER.equalsIgnoreCase(dataType)) {
				Number number = doValidateNumberByPattern(dataType, newFormat, value, locale);
				if (number == null)
					return null;
				return Integer.valueOf(number.intValue());
			} else if (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equalsIgnoreCase(dataType)) {
				return doValidateBoolean(value, locale);
			} else if (DesignChoiceConstants.PARAM_TYPE_STRING.equalsIgnoreCase(dataType)) {
				if (StringUtil.isBlank(value))
					return value;
				else if (DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED.equalsIgnoreCase(newFormat))
					return value;
				else {
					StringFormatter formatter = new StringFormatter(locale);
					formatter.applyPattern(newFormat);
					try {
						return formatter.parser(value);
					} catch (ParseException e) {
						throw new ValidationValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
								DesignChoiceConstants.PARAM_TYPE_STRING);
					}
				}
			} else {
				assert false;
				return null;
			}
		} catch (ValidationValueException e) {
			return validate(dataType, value, locale);
		}
	}

	/**
	 * Transform date format type if format is 'Unformatted'.
	 * <ul>
	 * if date type is 'date', transform to 'dateUnformatted'.
	 * <ul>
	 * if date type is 'dateTime', transform to 'dateTimeUnformatted'.
	 * <ul>
	 * if date type is 'time', transform to 'timeUnformatted'.
	 * <ul>
	 * if value is date, transform to 'dateTimeUnformatted'.
	 * <ul>
	 * if value is sql.date, transform to 'dateUnformatted'.
	 * <ul>
	 * if value is time, transform to 'timeUnformatted'.
	 * 
	 * @param dataType
	 * @param format
	 * @param value
	 * @return formated date
	 */

	private static String transformDateFormat(String dataType, String format, Object value) {
		if (DesignChoiceConstants.DATETIEM_FORMAT_TYPE_UNFORMATTED.equalsIgnoreCase(format)) {
			if (!StringUtil.isBlank(dataType)) {
				if (DesignChoiceConstants.PARAM_TYPE_DATE.equalsIgnoreCase(dataType)) {
					return DateFormatter.DATE_UNFORMATTED;
				} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equalsIgnoreCase(dataType)) {
					return DateFormatter.TIME_UNFORMATTED;
				} else if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equalsIgnoreCase(dataType)) {
					return DateFormatter.DATETIME_UNFORMATTED;
				}
			} else {
				if (value instanceof Date) {
					return DateFormatter.DATETIME_UNFORMATTED;
				} else if (value instanceof java.sql.Date) {
					return DateFormatter.DATE_UNFORMATTED;
				} else if (value instanceof java.sql.Time) {
					return DateFormatter.TIME_UNFORMATTED;
				}
			}
		}

		return format;
	}

	/**
	 * Validates a input parameter value with the given data type, format choice
	 * string and a default locale defined by the class(Locale.US). The returned
	 * value is pattern dependent. The data type and the format can be one pair of
	 * the following:
	 * <p>
	 * <table border="1" cellpadding="0" cellspacing="0" style="border-collapse: *
	 * collapse" bordercolor="#111111" width="36%" id="AutoNumber1">
	 * <tr>
	 * <td width="16%">Data Type</td>
	 * <td width="84%">Format Type</td>
	 * </tr>
	 * <tr>
	 * <td width="16%">Float/Decimal</td>
	 * <td width="84%">
	 * <ul>
	 * <li>General Number</li>
	 * <li>Currency</li>
	 * <li>Fixed</li>
	 * <li>Percent</li>
	 * <li>Scientific</li>
	 * <li>Standard</li>
	 * <li>pattern string, such as "###,##0", "###,##0.00 'm/s'", "###.#\';';#" and
	 * so on.</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td width="16%">Date time</td>
	 * <td width="84%">
	 * <ul>
	 * <li>General Date</li>
	 * <li>Long Date</li>
	 * <li>Medium Date</li>
	 * <li>Short Date</li>
	 * <li>Long Time</li>
	 * <li>Medium Time</li>
	 * <li>Short Time</li>
	 * <li>pattern string, such as "MM/dd/yyyy hh:mm:ss a", "yyyy-MM-dd HH:mm:ss"
	 * and so on.</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * <tr>
	 * <td width="16%">String</td>
	 * <td width="84%">
	 * <ul>
	 * <li>Upper case</li>
	 * <li>Lower case</li>
	 * <li>pattern string, such as "lt!" and so on.</li>
	 * </ul>
	 * </td>
	 * </tr>
	 * </table>
	 * 
	 * @param dataType the data type of the value
	 * @param format   the format choice string
	 * @param value    the input value to validate
	 * @return the validated value if the input value is valid for the given data
	 *         type and format choice string
	 * @throws ValidationValueException if the input value is not valid with the
	 *                                  given data type and format string
	 */

	static public Object validate(String dataType, String format, String value) throws ValidationValueException {
		return validate(dataType, format, value, DEFAULT_LOCALE);
	}

	/**
	 * Validates the input boolean value with the given locale.
	 * 
	 * @param value  the input value to validate
	 * @param locale the locale information
	 * @return the <code>Boolean</code> object if the input is valid, otherwise
	 *         <code>null</code>
	 * @throws ValidationValueException
	 */

	static private Boolean doValidateBoolean(String value, ULocale locale) throws ValidationValueException {
		if (StringUtil.isBlank(value))
			return null;

		// 1. Internal boolean name.

		if (value.equalsIgnoreCase(BooleanPropertyType.TRUE))
			return Boolean.TRUE;
		else if (value.equalsIgnoreCase(BooleanPropertyType.FALSE))
			return Boolean.FALSE;

		// 2. A localized Boolean name. Convert the localized
		// Boolean name into Boolean instance.

		if (value.equalsIgnoreCase(getMessage(locale, BooleanPropertyType.BOOLEAN_TRUE_RESOURCE_KEY))) {
			return Boolean.TRUE;
		} else if (value.equalsIgnoreCase(getMessage(locale, BooleanPropertyType.BOOLEAN_FALSE_RESOURCE_KEY))) {
			return Boolean.FALSE;
		}

		throw new ValidationValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
				DesignChoiceConstants.PARAM_TYPE_BOOLEAN);
	}

	/**
	 * Gets the message with the given locale and key.
	 * 
	 * @param locale the locale information
	 * @param key    the message key
	 * @return the message if found, otherwise the message key
	 */

	static private String getMessage(ULocale locale, String key) {
		// works around bug in some J2EE server; see Bugzilla #126073
		ULocale baseLocale = ThreadResources.getLocale();
		if (locale != null)
			ThreadResources.setLocale(locale);
		String msg = ModelMessages.getMessage(key);
		ThreadResources.setLocale(baseLocale);
		return msg;
	}

	/**
	 * Validates the input date time string with the given format. The format can be
	 * pre-defined choices or the pattern string.
	 * 
	 * @param format   the format to validate
	 * @param value    the value to validate
	 * @param locale   the locale information
	 * @param timeZone the time zone information
	 * @return the date value if validation is successful
	 * @throws ValidationValueException if the value to validate is invalid
	 */

	static private Date doValidateDateTimeByPattern(String format, String value, ULocale locale, TimeZone timeZone)
			throws ValidationValueException {
		assert !StringUtil.isBlank(format);
		if (StringUtil.isBlank(value))
			return null;

		try {
			DateFormatter formatter = null;
			if (timeZone != null) {
				formatter = new DateFormatter(locale, timeZone);
			} else {
				formatter = new DateFormatter(locale);
			}
			formatter.applyPattern(format);
			return formatter.parse(value);
		} catch (ParseException e) {
			throw new ValidationValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
					DesignChoiceConstants.PARAM_TYPE_DATETIME);
		}
	}

	/**
	 * Validates the input date time string with the given format.
	 * 
	 * @param dataType the data type of the value
	 * @param format   the format pattern
	 * @param value    the value to validate
	 * @param locale   the locale information
	 * @return the double value if the validation is successful
	 * @throws ValidationValueException if the value to validate is invalid
	 */

	static private Number doValidateNumberByPattern(String dataType, String format, String value, ULocale locale)
			throws ValidationValueException {
		assert DesignChoiceConstants.PARAM_TYPE_FLOAT.equalsIgnoreCase(dataType)
				|| DesignChoiceConstants.PARAM_TYPE_DECIMAL.equalsIgnoreCase(dataType)
				|| DesignChoiceConstants.PARAM_TYPE_INTEGER.equalsIgnoreCase(dataType);
		if (DesignChoiceConstants.NUMBER_FORMAT_TYPE_UNFORMATTED.equalsIgnoreCase(format))
			return doValidateNumber(dataType, value, locale);
		assert !StringUtil.isBlank(format);
		if (StringUtil.isBlank(value))
			return null;
		NumberFormatter formatter = new NumberFormatter(locale);
		formatter.applyPattern(format);

		if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equalsIgnoreCase(dataType)) {
			formatter.setParseBigDecimal(true);
		}

		try {
			return formatter.parse(value);
		} catch (ParseException e) {
			throw new ValidationValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, dataType);
		}
	}

	/**
	 * Gets the display string for the value with the given data type, format,
	 * locale. The value must be the valid data type. That is:
	 * 
	 * <ul>
	 * <li>if data type is <code>PARAM_TYPE_DATETIME</code>, then the value must be
	 * <code>java.util.Date<code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_FLOAT</code>, then the value must be
	 * <code>java.lang.Double</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_DECIMAL</code>, then the value must
	 * be <code>java.math.BigDecimal</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_BOOLEAN</code>, then the value must
	 * be <code>java.lang.Boolean</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_STRING</code>, then the value must
	 * be <code>java.lang.String</code>.</li>
	 * </ul>
	 * 
	 * @param dataType the data type of the input value
	 * @param format   the format pattern to validate
	 * @param value    the input value to validate
	 * @param locale   the locale information
	 * @return the formatted string
	 */

	static public String getDisplayValue(String dataType, String format, Object value, Locale locale) {
		return getDisplayValue(dataType, format, value, ULocale.forLocale(locale));
	}

	/**
	 * Gets the display string for the value with default locale and default format,
	 * The value must be the valid data type. That is:
	 * 
	 * <ul>
	 * <li>if data type is <code>PARAM_TYPE_DATETIME</code>, then the value must be
	 * <code>java.util.Date<code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_FLOAT</code>, then the value must be
	 * <code>java.lang.Double</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_DECIMAL</code>, then the value must
	 * be <code>java.math.BigDecimal</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_BOOLEAN</code>, then the value must
	 * be <code>java.lang.Boolean</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_STRING</code>, then the value must
	 * be <code>java.lang.String</code>.</li>
	 * </ul>
	 * 
	 * @param value the input value to validate
	 * @return the formatted string
	 */

	static public String getDisplayValue(Object value) {
		return getDisplayValue(value, null);
	}

	/**
	 * Gets the display string for the value with default locale and default format,
	 * The value must be the valid data type. That is:
	 * 
	 * <ul>
	 * <li>if data type is <code>PARAM_TYPE_DATETIME</code>, then the value must be
	 * <code>java.util.Date<code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_FLOAT</code>, then the value must be
	 * <code>java.lang.Double</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_DECIMAL</code>, then the value must
	 * be <code>java.math.BigDecimal</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_BOOLEAN</code>, then the value must
	 * be <code>java.lang.Boolean</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_STRING</code>, then the value must
	 * be <code>java.lang.String</code>.</li>
	 * </ul>
	 * 
	 * @param value    the input value to validate
	 * @param timeZone the time zone to use (only for DateTime type)
	 * @return the formatted string
	 */

	static public String getDisplayValue(Object value, TimeZone timeZone) {
		if (value == null)
			return null;

		if (value instanceof Date && !(value instanceof java.sql.Date || value instanceof java.sql.Time)) {
			DateFormatter formatter = null;
			if (timeZone != null) {
				formatter = new DateFormatter(DEFAULT_LOCALE, timeZone);
				formatter.applyPattern(DEFAULT_DATETIME_FORMAT);
			} else {
				formatter = defaultDateFormatters[DATETIME_FORMAT_TYPE];
			}
			return formatter.format((Date) value);
		} else if (value instanceof java.sql.Date) {
			DateFormatter formatter = defaultDateFormatters[DATE_FORMAT_TYPE];
			return formatter.format(new Date(((java.sql.Date) value).getTime()));
		} else if (value instanceof java.sql.Time) {
			DateFormatter formatter = defaultDateFormatters[TIME_FORMAT_TYPE];
			return formatter.format(new Date(((java.sql.Time) value).getTime()));
		} else if (value instanceof Float) {
			NumberFormatter formatter = defaultNumberFormatter;
			return formatter.format(((Number) value).floatValue());
		} else if (value instanceof Double) {
			NumberFormatter formatter = defaultNumberFormatter;
			return formatter.format(((Number) value).doubleValue());
		} else if (value instanceof BigDecimal) {
			NumberFormatter formatter = defaultNumberFormatter;
			return formatter.format(((BigDecimal) value));
		} else if (value instanceof Integer || value instanceof Long) {
			NumberFormatter formatter = defaultNumberFormatter;
			return formatter.format(((Number) value).longValue());
		} else if (value instanceof Boolean) {
			if (((Boolean) value).booleanValue()) {
				return getMessage(DEFAULT_LOCALE, BooleanPropertyType.BOOLEAN_TRUE_RESOURCE_KEY);
			}

			return getMessage(DEFAULT_LOCALE, BooleanPropertyType.BOOLEAN_FALSE_RESOURCE_KEY);
		} else if (value instanceof String) {
			StringFormatter formatter = defaultStringFormatter;
			return formatter.format((String) value);
		} else {
			StringFormatter formatter = defaultStringFormatter;
			return formatter.format(value.toString());
		}
	}

	/**
	 * Gets the display string for the value with the given data type, format,
	 * locale. The value must be the valid data type. That is:
	 * 
	 * <ul>
	 * <li>if data type is <code>PARAM_TYPE_DATETIME</code>, then the value must be
	 * <code>java.util.Date<code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_FLOAT</code>, then the value must be
	 * <code>java.lang.Double</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_DECIMAL</code>, then the value must
	 * be <code>java.math.BigDecimal</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_BOOLEAN</code>, then the value must
	 * be <code>java.lang.Boolean</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_STRING</code>, then the value must
	 * be <code>java.lang.String</code>.</li>
	 * </ul>
	 * 
	 * @param dataType the data type of the input value
	 * @param format   the format pattern to validate
	 * @param value    the input value to validate
	 * @param locale   the locale information
	 * @return the formatted string
	 */

	static public String getDisplayValue(String dataType, String format, Object value, ULocale locale) {
		return getDisplayValue(dataType, format, value, locale, null);
	}

	/**
	 * Gets the display string for the value with the given data type, format,
	 * locale. The value must be the valid data type. That is:
	 * 
	 * <ul>
	 * <li>if data type is <code>PARAM_TYPE_DATETIME</code>, then the value must be
	 * <code>java.util.Date<code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_FLOAT</code>, then the value must be
	 * <code>java.lang.Double</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_DECIMAL</code>, then the value must
	 * be <code>java.math.BigDecimal</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_BOOLEAN</code>, then the value must
	 * be <code>java.lang.Boolean</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_STRING</code>, then the value must
	 * be <code>java.lang.String</code>.</li>
	 * </ul>
	 * 
	 * @param dataType the data type of the input value
	 * @param format   the format pattern to validate
	 * @param value    the input value to validate
	 * @param locale   the locale information
	 * @return the formatted string
	 */

	static public String getDisplayValue(String dataType, String format, Object value, ULocale locale,
			TimeZone timeZone) {
		if (value == null)
			return null;

		format = StringUtil.trimString(format);
		format = transformDateFormat(dataType, format, value);
		if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equalsIgnoreCase(dataType)
				|| (value instanceof Date && !(value instanceof java.sql.Date || value instanceof java.sql.Time))) {
			DateFormatter formatter = new DateFormatter(locale, timeZone);
			formatter.applyPattern(format);
			return formatter.format((Date) value);
		} else if (DesignChoiceConstants.PARAM_TYPE_DATE.equalsIgnoreCase(dataType) || value instanceof java.sql.Date) {
			DateFormatter formatter = new DateFormatter(locale);
			if (format == null)
				format = DISPLAY_DATE_FORMAT;
			formatter.applyPattern(format);
			return formatter.format(new Date(((java.sql.Date) value).getTime()));
		} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equalsIgnoreCase(dataType) || value instanceof java.sql.Time) {
			DateFormatter formatter = new DateFormatter(locale);
			if (format == null)
				format = DISPLAY_TIME_FORMAT;
			formatter.applyPattern(format);
			return formatter.format(new Date(((java.sql.Time) value).getTime()));
		} else if (DesignChoiceConstants.PARAM_TYPE_FLOAT.equalsIgnoreCase(dataType) || value instanceof Float
				|| value instanceof Double) {
			if (value instanceof Float) {
				NumberFormatter formatter = new NumberFormatter(locale);
				formatter.applyPattern(format);
				return formatter.format(((Number) value).floatValue());
			}
			NumberFormatter formatter = new NumberFormatter(locale);
			formatter.applyPattern(format);
			return formatter.format(((Number) value).doubleValue());
		} else if (DesignChoiceConstants.PARAM_TYPE_DECIMAL.equalsIgnoreCase(dataType) || value instanceof BigDecimal) {
			NumberFormatter formatter = new NumberFormatter(locale);
			formatter.applyPattern(format);
			return formatter.format(((BigDecimal) value));
		} else if (DesignChoiceConstants.PARAM_TYPE_INTEGER.equalsIgnoreCase(dataType) || value instanceof Integer
				|| value instanceof Long) {
			NumberFormatter formatter = new NumberFormatter(locale);
			formatter.applyPattern(format);
			return formatter.format(((Number) value).longValue());
		} else if (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equalsIgnoreCase(dataType) || value instanceof Boolean) {
			if (((Boolean) value).booleanValue()) {
				return getMessage(locale, BooleanPropertyType.BOOLEAN_TRUE_RESOURCE_KEY);
			}

			return getMessage(locale, BooleanPropertyType.BOOLEAN_FALSE_RESOURCE_KEY);
		} else if (DesignChoiceConstants.PARAM_TYPE_STRING.equalsIgnoreCase(dataType) || value instanceof String) {
			StringFormatter formatter = new StringFormatter(locale);
			formatter.applyPattern(format);
			formatter.setTrim(false);
			return formatter.format((String) value);
		} else {
			StringFormatter formatter = new StringFormatter(locale);
			formatter.applyPattern(format);
			formatter.setTrim(false);
			return formatter.format(value.toString());
		}

	}

	/**
	 * Gets the display string for the value with the given data type, format and
	 * the default locale defined by the class(Locale.US). The value must be the
	 * valid data type. That is:
	 * 
	 * <ul>
	 * <li>if data type is <code>PARAM_TYPE_DATETIME</code>, then the value must be
	 * <code>java.util.Date<code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_FLOAT</code>, then the value must be
	 * <code>java.lang.Double</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_DECIMAL</code>, then the value must
	 * be <code>java.math.BigDecimal</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_BOOLEAN</code>, then the value must
	 * be <code>java.lang.Boolean</code>.</li>
	 * <li>if the data type is <code>PARAM_TYPE_STRING</code>, then the value must
	 * be <code>java.lang.String</code>.</li>
	 * </ul>
	 * 
	 * @param dataType the data type of the input value
	 * @param format   the format pattern to validate
	 * @param value    the input value to validate
	 * @return the formatted string
	 */

	static public String getDisplayValue(String dataType, String format, Object value) {
		return getDisplayValue(dataType, format, value, DEFAULT_LOCALE);
	}
}