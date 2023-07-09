/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.utility;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.exception.CoreException;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.engine.api.IScalarParameterDefn;
import org.eclipse.birt.report.exception.ViewerValidationException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.metadata.ValidationValueException;
import org.eclipse.birt.report.model.api.util.ParameterValidationUtil;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.ParameterDataTypeConverter;

import com.ibm.icu.util.ULocale;

/**
 * Provides data convert and format services
 *
 */
public class DataUtil {

	/**
	 * Convert Object to String
	 *
	 * @param object
	 * @return String
	 */
	public static String getString(Object object) {
		if (object == null) {
			return null;
		}

		return object.toString();
	}

	/**
	 * Returns trim string, not null
	 *
	 * @param str
	 * @return
	 */
	public static String trimString(String str) {
		if (str == null) {
			return ""; //$NON-NLS-1$
		}

		return str.trim();
	}

	/**
	 * Trim the first/end separator
	 *
	 * @param path
	 * @return
	 */
	public static String trimSep(String path) {
		return trimSepFirst(trimSepEnd(path));
	}

	/**
	 * Trim the end separator
	 *
	 * @param path
	 * @return
	 */
	public static String trimSepEnd(String path) {
		path = trimString(path);
		if (path.endsWith(File.separator)) {
			path = path.substring(0, path.length() - 1);
		}

		return path;
	}

	/**
	 * Trim the end separator
	 *
	 * @param path
	 * @return
	 */
	public static String trimSepFirst(String path) {
		path = trimString(path);
		if (path.startsWith(File.separator)) {
			path = path.substring(1);
		}

		return path;
	}

	/**
	 * Returns the default date/time format
	 *
	 * @param dataType
	 * @return
	 */
	private static String getDefaultDateFormat(String dataType) {
		String defFormat = null;
		if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equalsIgnoreCase(dataType)) {
			defFormat = ParameterValidationUtil.DEFAULT_DATETIME_FORMAT;
		} else if (DesignChoiceConstants.PARAM_TYPE_DATE.equalsIgnoreCase(dataType)) {
			defFormat = ParameterValidationUtil.DEFAULT_DATE_FORMAT;
		} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equalsIgnoreCase(dataType)) {
			defFormat = ParameterValidationUtil.DEFAULT_TIME_FORMAT;
		}

		return defFormat;
	}

	/**
	 *
	 * Convert parameter to Object
	 *
	 * @param paramName
	 * @param dataType
	 * @param format
	 * @param value
	 * @param locale
	 * @param isLocale  indicate whether it is a locale string
	 * @return Object
	 * @throws ViewerValidationException
	 */
	public static Object validate(String paramName, String dataType, String format, String value, Locale locale,
			TimeZone timeZone, boolean isLocale) throws ViewerValidationException {
		if (paramName == null || value == null || IBirtConstants.NULL_VALUE.equals(value)) {
			return null;
		}

		if (!DesignChoiceConstants.PARAM_TYPE_STRING.equalsIgnoreCase(dataType) && value.trim().length() <= 0) {
			throw new ViewerValidationException(BirtResources
					.getMessage(ResourceConstants.GENERAL_ERROR_PARAMETER_NOTBLANK, new String[] { paramName }));
		}

		try {
			return validate(dataType, format, value, locale, timeZone, isLocale);
		} catch (ValidationValueException e) {
			throw new ViewerValidationException(
					BirtResources.getMessage(ResourceConstants.GENERAL_ERROR_PARAMETER_INVALID,
							new String[] { paramName }) + " " + e.getLocalizedMessage(), //$NON-NLS-1$
					e);
		}
	}

	/**
	 *
	 * Convert parameter to Object with pattern
	 *
	 * @param paramName
	 * @param dataType
	 * @param format
	 * @param value
	 * @param locale
	 * @param isLocale  indicate whether it is a locale string
	 * @return Object
	 * @throws ViewerValidationException
	 */
	public static Object validateWithPattern(String paramName, String dataType, String format, String value,
			Locale locale, TimeZone timeZone, boolean isLocale) throws ViewerValidationException {
		if (paramName == null || IBirtConstants.NULL_VALUE.equals(value)) {
			return null;
		}

		try {
			return validateWithPattern(dataType, format, value, locale, timeZone, isLocale);
		} catch (ValidationValueException e) {
			throw new ViewerValidationException(
					BirtResources.getMessage(ResourceConstants.GENERAL_ERROR_PARAMETER_INVALID,
							new String[] { paramName }) + " " + e.getLocalizedMessage(), //$NON-NLS-1$
					e);
		}
	}

	/**
	 *
	 * Convert parameter to Object
	 *
	 * @param dataType
	 * @param format
	 * @param value
	 * @param locale
	 * @param isLocale indicate whether it is a locale string
	 * @return Object
	 * @throws ValidationValueException
	 */
	public static Object validate(String dataType, String format, String value, Locale locale, TimeZone timeZone,
			boolean isLocale) throws ValidationValueException {
		Object obj = null;
		if (value == null || IBirtConstants.NULL_VALUE.equals(value)) {
			return null;
		}

		// if parameter value equals display text, should use local/format to
		// format parameter value first
		if (isLocale) {
			obj = validateWithLocale(dataType, format, value, locale, timeZone);
		} else {
			// Convert string to object using default format/local
			obj = ParameterValidationUtil.validate(dataType, getDefaultDateFormat(dataType), value,
					BirtUtility.toICUTimeZone(timeZone));
		}

		return obj;
	}

	/**
	 *
	 * Convert parameter to Object with pattern
	 *
	 * @param dataType
	 * @param format
	 * @param value
	 * @param locale
	 * @param isLocale indicate whether it is a locale string
	 * @return Object
	 * @throws ValidationValueException
	 */
	public static Object validateWithPattern(String dataType, String format, String value, Locale locale,
			TimeZone timeZone, boolean isLocale) throws ValidationValueException {
		Object obj = null;
		if (value == null || IBirtConstants.NULL_VALUE.equals(value)) {
			return null;
		}

		// if parameter value equals display text, should use local/format to
		// format parameter value first
		if (isLocale) {
			obj = validateWithLocale(dataType, format, value, locale, timeZone);
		} else {
			// Default format
			if (format == null) {
				format = getDefaultDateFormat(dataType);
			}

			// Convert string to object using default locale
			obj = ParameterValidationUtil.validate(dataType, format, value, BirtUtility.toICUTimeZone(timeZone));
		}

		return obj;
	}

	/**
	 * Convert parameter to Object with locale setting
	 *
	 * @param dataType
	 * @param format
	 * @param value
	 * @param locale
	 * @return
	 * @throws ValidationValueException
	 */
	public static Object validateWithLocale(String dataType, String format, String value, Locale locale,
			TimeZone timeZone) throws ValidationValueException {
		Object obj = null;
		if (value == null || IBirtConstants.NULL_VALUE.equals(value)) {
			return null;
		}

		try {
			if (format == null) {
				if (DesignChoiceConstants.PARAM_TYPE_DATE.equalsIgnoreCase(dataType)) {
					format = ParameterValidationUtil.DISPLAY_DATE_FORMAT;
				} else if (DesignChoiceConstants.PARAM_TYPE_TIME.equalsIgnoreCase(dataType)) {
					format = ParameterValidationUtil.DISPLAY_TIME_FORMAT;
				} else if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equalsIgnoreCase(dataType)) {
					format = DesignChoiceConstants.DATETIME_FORMAT_TYPE_UNFORMATTED;
				}
			}

			// Convert locale string to object
			obj = ParameterValidationUtil.validate(dataType, format, value, locale,
					BirtUtility.toICUTimeZone(timeZone));
		} catch (Exception e) {
			// Convert string to object using default format/local
			if (DesignChoiceConstants.PARAM_TYPE_DATETIME.equalsIgnoreCase(dataType)) {
				// Make the consistent with designer
				try {
					if (timeZone != null) {
						obj = DataTypeUtil.toDate(value, BirtUtility.toICUTimeZone(timeZone));
					} else {
						obj = DataTypeUtil.toDate(value);
					}
				} catch (BirtException el) {
					throw new ValidationValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
							DesignChoiceConstants.PARAM_TYPE_DATETIME);
				}
			} else if (DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equalsIgnoreCase(dataType)) {
				try {
					obj = ParameterValidationUtil.validate(dataType, format,
							String.valueOf(DataUtil.convert((Object) value,
									ParameterDataTypeConverter.convertDataType(dataType))),
							locale, BirtUtility.toICUTimeZone(timeZone));
				} catch (Exception e2) {
					throw new ValidationValueException(value, PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE,
							DesignChoiceConstants.PARAM_TYPE_BOOLEAN);
				}
			} else {

				obj = ParameterValidationUtil.validate(dataType, getDefaultDateFormat(dataType), value,
						BirtUtility.toICUTimeZone(timeZone));
			}
		}

		return obj;
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
	 * @param timeZone the time zone information
	 * @return the formatted string
	 */
	public static String getDisplayValue(String dataType, String format, Object value, Locale locale,
			TimeZone timeZone) {
		return ParameterValidationUtil.getDisplayValue(dataType, format, value, ULocale.forLocale(locale),
				BirtUtility.toICUTimeZone(timeZone));
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

	public static String getDisplayValue(Object value) {
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
	 * @param value the input value to validate
	 * @param time  zone the time zone to use for the output
	 * @return the formatted string
	 */

	public static String getDisplayValue(Object value, TimeZone timeZone) {
		if (value == null) {
			return null;
		}

		if (value instanceof Float || value instanceof Double) {
			return value.toString();
		} else if (value instanceof BigDecimal || value instanceof com.ibm.icu.math.BigDecimal) {
			return value.toString().replaceFirst("E\\+", "E"); //$NON-NLS-1$//$NON-NLS-2$
		}

		return ParameterValidationUtil.getDisplayValue(value, BirtUtility.toICUTimeZone(timeZone));
	}

	/**
	 * Convert object to be exported as CSV
	 *
	 * @param value
	 * @return
	 * @throws BirtException
	 */
	public static String getCSVDisplayValue(Object value) throws BirtException {
		if (value == null) {
			return null;
		}

		if (value instanceof Integer || value instanceof Long || value instanceof Float || value instanceof Double
				|| value instanceof BigDecimal || value instanceof com.ibm.icu.math.BigDecimal) {
			return value.toString();
		}

		return DataTypeUtil.toLocaleNeutralString(value);
	}

	/**
	 * Try convert an object to given type Types supported:
	 * <p>
	 * <ul>
	 * <li>IScalarParameterDefn.TYPE_INTEGER</li>
	 * <li>IScalarParameterDefn.TYPE_DECIMAL</li>
	 * <li>IScalarParameterDefn.TYPE_BOOLEAN</li>
	 * <li>IScalarParameterDefn.TYPE_DATE_TIME</li>
	 * <li>IScalarParameterDefn.TYPE_FLOAT</li>
	 * <li>IScalarParameterDefn.TYPE_STRING</li>
	 * <li>IScalarParameterDefn.TYPE_DATE</li>
	 * <li>IScalarParameterDefn.TYPE_TIME</li>
	 * <ul>
	 * </p>
	 *
	 * @param source
	 * @param toType
	 * @return
	 * @throws BirtException
	 */
	public static Object convert(Object source, int toType) throws BirtException {
		if (source == null) {
			return null;
		}

		// if any type, return directly.
		if (toType == IScalarParameterDefn.TYPE_ANY) {
			return source;
		}

		switch (toType) {
		case IScalarParameterDefn.TYPE_INTEGER:
			return DataTypeUtil.toInteger(source);
		case IScalarParameterDefn.TYPE_DECIMAL:
			return DataTypeUtil.toBigDecimal(source);
		case IScalarParameterDefn.TYPE_BOOLEAN:
			return DataTypeUtil.toBoolean(source);
		case IScalarParameterDefn.TYPE_DATE_TIME:
			return DataTypeUtil.toDate(source);
		case IScalarParameterDefn.TYPE_FLOAT:
			return DataTypeUtil.toDouble(source);
		case IScalarParameterDefn.TYPE_STRING:
			return DataTypeUtil.toString(source);
		case IScalarParameterDefn.TYPE_DATE:
			return DataTypeUtil.toSqlDate(source);
		case IScalarParameterDefn.TYPE_TIME:
			return DataTypeUtil.toSqlTime(source);
		default:
			throw new CoreException("Invalid type."); //$NON-NLS-1$
		}
	}

	/**
	 * Convert to UTF-8 bytes
	 *
	 * @param bytes
	 * @return
	 */
	public static String toUTF8(byte[] bytes) {
		assert bytes != null;
		String str = null;
		try {
			str = new String(bytes, "utf-8"); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e) {
		}
		return str;
	}

	/**
	 * Returns oda type name
	 *
	 * @param odaTypeCode
	 * @return
	 */
	public static String getOdaTypeName(int odaTypeCode) {
		switch (odaTypeCode) {
		case Types.INTEGER:
			return "INT"; //$NON-NLS-1$
		case Types.DOUBLE:
		case Types.FLOAT:
			return "DOUBLE"; //$NON-NLS-1$
		case Types.VARCHAR:
			return "STRING"; //$NON-NLS-1$
		case Types.DATE:
			return "DATE"; //$NON-NLS-1$
		case Types.TIME:
			return "TIME"; //$NON-NLS-1$
		case Types.TIMESTAMP:
			return "TIMESTAMP"; //$NON-NLS-1$
		case Types.NUMERIC:
		case Types.DECIMAL:
			return "BIGDECIMAL"; //$NON-NLS-1$
		case Types.BLOB:
			return "BLOB"; //$NON-NLS-1$
		case Types.CLOB:
			return "CLOB"; //$NON-NLS-1$
		case Types.BOOLEAN:
			return "BOOLEAN"; //$NON-NLS-1$
		default:
			return "STRING"; //$NON-NLS-1$
		}
	}

	/**
	 * Check the passed String whether be contained in list
	 *
	 * @param values
	 * @param value
	 * @param ifDelete
	 * @return
	 */
	public static boolean contain(List values, String value, boolean ifDelete) {
		if (values == null) {
			return false;
		}

		for (Iterator it = values.iterator(); it.hasNext();) {
			Object obj = it.next();
			if (obj == null) {
				if (value == null) {
					if (ifDelete) {
						values.remove(obj);
					}
					return true;
				}

				continue;
			}

			if (obj instanceof String && ((String) obj).equals(value)) {
				if (ifDelete) {
					values.remove(obj);
				}
				return true;
			}
		}

		return false;
	}

	/**
	 * Compare two strings that could be null and return true if they are equal.
	 *
	 * @param s1 first string
	 * @param s2 second string
	 * @return true if the strings are equal, or both are null
	 */
	public static boolean equals(String s1, String s2) {
		// s2 is null or equal to s1
		if (s1 != null) {
			return s1.equals(s2);
		}
		// s1 is null or equal to s2
		else if (s2 != null) {
			// s1 is null, but not s2
			return s2.equals(s1);
		}
		// both are null
		else {
			return true;
		}
	}
}
