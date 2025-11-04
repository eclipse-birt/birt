/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.core.data;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import jakarta.sql.rowset.serial.SerialBlob;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.exception.CoreException;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.i18n.ResourceConstants;
import org.eclipse.birt.core.i18n.ResourceHandle;
import org.eclipse.birt.core.script.JavascriptEvalUtil;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * A utility function The convert method converts the source object, which can
 * be any supported data type, into an object given specified type. If no
 * reasonable conversion can be made, throw a BirtException.
 */
public final class DataTypeUtil {

	// Defalult Locale, if we have any problem parse string to date for
	// Locale.getDefault()
	// we will try to parse it for Locale.US
	private static ULocale DEFAULT_LOCALE = ULocale.US;
	private static ULocale JRE_DEFAULT_LOCALE = ULocale.getDefault();
	private static SimpleDateFormat MysqlUSDateFormatter = new SimpleDateFormat("M/d/yyyy HH:mm");

	private static Pattern p1 = Pattern.compile(".*[0-9]+:[0-9]+:[0-9]+.*");
	private static Pattern p2 = Pattern.compile(".*[0-9]+:[0-9]+.*");

	// cache DateFormatter of ICU
	private static Map dfMap = new HashMap();
	private static Map nfMap = new HashMap();
	// Default Date/Time Style
	private static int DEFAULT_DATE_STYLE = DateFormat.FULL;

	// resource bundle for exception messages
	public static ResourceBundle resourceBundle = (new ResourceHandle(JRE_DEFAULT_LOCALE)).getUResourceBundle();

	public static long count = 0;

	/**
	 * convert an object to given type Types supported: DataType.INTEGER_TYPE
	 * DataType.DECIMAL_TYPE DataType.BOOLEAN_TYPE DataType.DATE_TYPE
	 * DataType.DOUBLE_TYPE DataType.STRING_TYPE DataType.BLOB_TYPE
	 * DataType.SQL_DATE_TYPE DataType.SQL_TIME_TYPE
	 *
	 * @param source
	 * @param toType
	 * @return
	 * @throws BirtException
	 */
	public static Object convert(Object source, int toType) throws BirtException {
		source = JavascriptEvalUtil.convertJavascriptValue(source);

		// here we assume the efficiency of if else is higher than switch case
		if (toType == DataType.UNKNOWN_TYPE || toType == DataType.ANY_TYPE) {
			return source;
		}

		switch (toType) {
		case DataType.INTEGER_TYPE:
			return toInteger(source);
		case DataType.DECIMAL_TYPE:
			return toBigDecimal(source);
		case DataType.BOOLEAN_TYPE:
			return toBoolean(source);
		case DataType.DATE_TYPE:
			return toDate(source);
		case DataType.DOUBLE_TYPE:
			return toDouble(source);
		case DataType.STRING_TYPE:
			return toString(source);
		case DataType.BLOB_TYPE:
			return toBytes(source);
		case DataType.BINARY_TYPE:
			return toBytes(source);
		case DataType.SQL_DATE_TYPE:
			return toSqlDate(source);
		case DataType.SQL_TIME_TYPE:
			return toSqlTime(source);
		case DataType.JAVA_OBJECT_TYPE:
			return source;
		default:
			throw new CoreException(ResourceConstants.INVALID_TYPE, resourceBundle);
		}
	}

	/**
	 * convert a object to given class Classes supported: Integer.class
	 * BigDecimal.class Boolean.class Time.class Date.class Double.class
	 * String.class Blob.class
	 *
	 * @param source
	 * @param toTypeClass
	 * @return
	 * @throws BirtException
	 */
	public static Object convert(Object source, Class toTypeClass) throws BirtException {
		if ((source != null && source.getClass() == toTypeClass) || (toTypeClass == DataType.getClass(DataType.ANY_TYPE))) {
			return source;
		}
		if (toTypeClass == Integer.class) {
			return toInteger(source);
		}
		if (toTypeClass == BigDecimal.class) {
			return toBigDecimal(source);
		}
		if (toTypeClass == Boolean.class) {
			return toBoolean(source);
		}
		if (toTypeClass == Time.class) {
			return toSqlTime(source);
		}
		if (toTypeClass == java.sql.Date.class) {
			return toSqlDate(source);
		}
		if (toTypeClass == java.sql.Timestamp.class) {
			return toTimestamp(source);
		}
		if (toTypeClass == Date.class) {
			return toDate(source);
		}
		if (toTypeClass == Double.class) {
			return toDouble(source);
		}
		if (toTypeClass == String.class) {
			return toString(source);
		}
		if (toTypeClass == Blob.class) {
			if (source instanceof byte[]) {
				return source;
			} else {
				return toBlob(source);
			}
		}
		if (toTypeClass == byte[].class) {
			return source;
		}
		if (toTypeClass == Object.class) {
			return source;
		}

		throw new CoreException(ResourceConstants.INVALID_TYPE, resourceBundle);
	}

	/**
	 * Boolean -> Integer true -> 1 others -> 0 Date -> Integer Date.getTime();
	 * String -> Integer Integer.valueOf();
	 *
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static Integer toInteger(Object source) throws BirtException {
		return toInteger(source, JRE_DEFAULT_LOCALE);
	}

	/**
	 * Boolean -> Integer true -> 1 others -> 0 Date -> Integer Date.getTime();
	 * String -> Integer Integer.valueOf();
	 *
	 * @param source
	 * @param locale Locale
	 * @return
	 * @throws BirtException
	 */
	public static Integer toInteger(Object source, ULocale locale) throws BirtException {
		if (source == null) {
			return null;
		}

		if (source instanceof Integer) {
			return (Integer) source;
		} else if (source instanceof Number) {
			// This takes care of BigDecimal, BigInteger, Byte, Double,
			// Float, Long, Short
			if (!isConvertableToInteger((Number) source)) {
				throw new CoreException(ResourceConstants.CONVERT_FAILS, new Object[] { source.toString(), "Integer" });
			}
			int intValue = ((Number) source).intValue();
			return intValue;
		} else if (source instanceof Boolean) {
			if (((Boolean) source).booleanValue()) {
				return 1;
			}
			return 0;
		} else if (source instanceof Date) {
			long longValue = ((Date) source).getTime();
			if (!isConvertableToInteger(Long.valueOf(longValue))) {
				throw new CoreException(ResourceConstants.CONVERT_FAILS, new Object[] { source.toString(), "Integer" });
			}
			return (int) longValue;
		} else if (source instanceof CharSequence) {
			source = source.toString();
			try {
				return Integer.valueOf((String) source);
			} catch (NumberFormatException e) {
				try {
					Number number = NumberFormat.getInstance(locale).parse((String) source);
					if (number != null) {
						if (!isConvertableToInteger(number)) {
							throw new CoreException(ResourceConstants.CONVERT_FAILS,
									new Object[] { source.toString(), "Integer" });
						}
						return number.intValue();
					}

					throw new CoreException(ResourceConstants.CONVERT_FAILS,
							new Object[] { source.toString(), "Integer" });
				} catch (ParseException e1) {
					throw new CoreException(ResourceConstants.CONVERT_FAILS,
							new Object[] { source.toString(), "Integer" });
				}
			}
		} else {
			throw new CoreException(ResourceConstants.CONVERT_FAILS, new Object[] { source.toString(), "Integer" });
		}
	}

	/**
	 * Boolean -> BigDecimal true -> 1 others -> 0 Date -> BigDecimal
	 * Date.getTime(); String -> BigDecimal new BigDecimal(String);
	 *
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static BigDecimal toBigDecimal(Object source) throws BirtException {
		if (source == null) {
			return null;
		}

		if (source instanceof BigDecimal) {
			return (BigDecimal) source;
		} else if (source instanceof Number) {
			// This takes care of BigDecimal, BigInteger, Byte, Double,
			// Float, Long, Short, Integer
			// An intermediate conversion using String is preferrable per JavaDoc
			// comment in BigDecimal(String) constructor
			if (source instanceof Double && (((Double) source).isInfinite() || ((Double) source).isNaN())) {
				return null;
			} else if (source instanceof Float && (((Float) source).isInfinite() || ((Float) source).isNaN())) {
				return null;
			}

			String str = ((Number) source).toString();
			try {
				return new BigDecimal(str);
			} catch (NumberFormatException e) {
				throw new CoreException(ResourceConstants.CONVERT_FAILS, new Object[] { str, "BigDecimal" });
			}
		} else if (source instanceof Boolean) {
			if (((Boolean) source).booleanValue()) {
				return BigDecimal.ONE;
			}
			return BigDecimal.ZERO;
		} else if (source instanceof Date) {
			long longValue = ((Date) source).getTime();
			return new BigDecimal(longValue);
		} else if (source instanceof CharSequence) {
			// if empty string, return null
			source = source.toString();
			if (((String) source).length() == 0) {
				return null;
			}
			try {
				return new BigDecimal((String) source);
			} catch (NumberFormatException e) {
				try {
					Number number = NumberFormat.getInstance(JRE_DEFAULT_LOCALE).parse((String) source);
					if (number != null) {
						return new BigDecimal(number.toString());
					}

					throw new CoreException(ResourceConstants.CONVERT_FAILS,
							new Object[] { source.toString(), "BigDecimal" });
				} catch (ParseException e1) {
					throw new CoreException(ResourceConstants.CONVERT_FAILS,
							new Object[] { source.toString(), "BigDecimal" });
				}
			}
		} else {
			throw new CoreException(ResourceConstants.CONVERT_FAILS, new Object[] { source.toString(), "BigDecimal" });
		}
	}

	/**
	 * Number -> Boolean 0 -> false others -> true String -> Boolean "true" -> true
	 * (ignore case) "false" -> false (ignore case) other string will throw an
	 * exception Date -> Boolean throw exception
	 *
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static Boolean toBoolean(Object source) throws BirtException {
		if (source == null) {
			return null;
		}

		if (source instanceof Boolean) {
			return (Boolean) source;
		} else if (source instanceof Number) {
			// Takes care of all numeric types
			if (((Number) source).doubleValue() == 0) {
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		} else if (source instanceof CharSequence) {
			source = source.toString();
			if (((String) source).equalsIgnoreCase("true")) {
				return Boolean.TRUE;
			} else if (((String) source).equalsIgnoreCase("false")) {
				return Boolean.FALSE;
			} else {
				try {
					if (Double.parseDouble((String) source) == 0) {
						return Boolean.FALSE;
					} else {
						return Boolean.TRUE;
					}
				} catch (NumberFormatException e) {
					throw new CoreException(ResourceConstants.CONVERT_FAILS,
							new Object[] { source.toString(), "Boolean" });
				}
			}
		} else {
			throw new CoreException(ResourceConstants.CONVERT_FAILS, new Object[] { source.toString(), "Boolean" });
		}
	}

	/**
	 * Number -> Date new Date((long)Number) String -> Date toDate(String)
	 *
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static Date toDate(Object source) throws BirtException {
		if (source == null) {
			return null;
		}

		if (source instanceof Date) {
			return new Date(((Date) source).getTime());
		} else if (source instanceof CharSequence) {
			return toDate(source.toString());
		} else if (source instanceof Double) {
			// Rounding Double to the nearest Long.
			// This should be a relatively safe operation since this type
			// of conversion is usually done for representing aggregate
			// function results as Date.
			return new Date(Math.round((Double) source));
		} else {
			throw new CoreException(ResourceConstants.CONVERT_FAILS, new Object[] { source.toString(), "Date" });
		}
	}

	/**
	 *
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static Timestamp toTimestamp(Object source) throws BirtException {
		Date date = toDate(source);
		if (date == null) {
			return null;
		}
		return new Timestamp(date.getTime());
	}

	/**
	 * Date -> Time String -> Time
	 *
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static Time toSqlTime(Object source) throws BirtException {
		if (source == null) {
			return null;
		}

		if (source instanceof Date) {
			return toSqlTime((Date) source);
		} else if (source instanceof CharSequence) {
			source = source.toString();
			try {
				return toSqlTime(toDate((String) source));
			} catch (Exception e) {
				try {
					return toSqlTime((String) source);
				} catch (Exception e1) {

				}
			}
		} else if (source instanceof Double) {
			// Rounding to the nearest Long is safe here since the Long value
			// represents milliseconds
			return toSqlTime(new Date(Math.round((Double) source)));
		}

		throw new CoreException(ResourceConstants.CONVERT_FAILS, new Object[] { source.toString(), "Time" });
	}

	/**
	 *
	 * @param value
	 * @return
	 */
	private static Time toSqlTime(String s) {
		int hour;
		int addHour;
		int minute;
		int second;
		int firstColon;
		int secondColon;
		int marker;

		if (s == null) {
			throw new java.lang.IllegalArgumentException();
		}

		firstColon = s.indexOf(':');
		secondColon = s.indexOf(':', firstColon + 1);
		for (marker = secondColon + 1; marker < s.length(); marker++) {
			if (!isDigitTen(s.charAt(marker))) {
				break;
			}
		}
		addHour = 0;
		String markerValue = null;
		String aMarker = null;
		if (marker < s.length()) {
			markerValue = s.substring(marker).trim();
			if ("am".compareToIgnoreCase(markerValue) == 0) {
				addHour = 0;
				aMarker = "am";
			} else if ("pm".compareToIgnoreCase(markerValue) == 0) {
				addHour = 12;
				aMarker = "pm";
			} else {
				throw new java.lang.IllegalArgumentException();
			}
		}
		if (firstColon <= 0 || secondColon <= 0 || secondColon >= s.length() - 1) {
			throw new java.lang.IllegalArgumentException();
		}
		hour = Integer.parseInt(s.substring(0, firstColon));
		minute = Integer.parseInt(s.substring(firstColon + 1, secondColon));
		if (minute < 0 || minute > 60) {
			throw new java.lang.IllegalArgumentException();
		}
		if (marker < s.length()) {
			second = Integer.parseInt(s.substring(secondColon + 1, marker));
		} else {
			second = Integer.parseInt(s.substring(secondColon + 1));
		}
		if (second < 0 || second > 60) {
			throw new java.lang.IllegalArgumentException();
		}
		if (hour == 12 && minute == 0 && second == 0 && aMarker != null) {
			if ("am".equals(aMarker)) {
				hour = 24;
			} else {
				hour = 12;
			}
		} else {
			if (hour < 0 || (hour > 12 && markerValue != null && markerValue.length() > 0)) {
				throw new java.lang.IllegalArgumentException();
			}
			hour += addHour;
			if (hour > 24) {
				throw new java.lang.IllegalArgumentException();
			}
		}

		return toSqlTime(hour, minute, second);
	}

	/**
	 *
	 * @param hour
	 * @param minute
	 * @param second
	 * @return
	 */
	private static Time toSqlTime(int hour, int minute, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.HOUR_OF_DAY, hour);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, second);
		return new java.sql.Time(calendar.getTimeInMillis());
	}

	/**
	 *
	 * @param c
	 * @return
	 */
	private static boolean isDigitTen(char c) {
		if (c <= '9' && c >= '0') {
			return true;
		}
		return false;
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	private static java.sql.Time toSqlTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTimeInMillis(date.getTime());
		calendar.set(Calendar.YEAR, 1970);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		return new java.sql.Time(calendar.getTimeInMillis());
	}

	/**
	 * Date -> Time String -> Time
	 *
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static java.sql.Date toSqlDate(Object source) throws BirtException {
		if (source == null) {
			return null;
		}

		if (source instanceof java.sql.Date) {
			return maskSQLDate((java.sql.Date) source);
		} else if (source instanceof Date) {
			return toSqlDate((Date) source);
		} else if (source instanceof CharSequence) {
			source = source.toString();
			try {
				return toSqlDate(toDate((String) source));
			} catch (Exception e) {
				try {
					return java.sql.Date.valueOf((String) source);
				} catch (Exception e1) {

				}
			}
		} else if (source instanceof Double) {
			// Rounding to the nearest Long is safe here since the Long value
			// represents milliseconds
			return toSqlDate(new Date(Math.round((Double) source)));
		}

		throw new CoreException(ResourceConstants.CONVERT_FAILS, new Object[] { source.toString(), "java.sql.Date" });
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	private static java.sql.Date toSqlDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTimeInMillis(date.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new java.sql.Date(calendar.getTimeInMillis());
	}

	/**
	 * mask out time info for sql Date
	 *
	 * @param date
	 * @return
	 */
	private static java.sql.Date maskSQLDate(java.sql.Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.setTimeInMillis(date.getTime());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return new java.sql.Date(calendar.getTimeInMillis());
	}

	/**
	 * A temp solution to the adoption of ICU4J to BIRT. Simply delegate toDate(
	 * String, Locale) method.
	 *
	 * @param source the String to be convert
	 * @param locate the locate of the string
	 * @return result Date
	 */
	public static Date toDate(String source, Locale locale) throws BirtException {
		return toDate(source, ULocale.forLocale(locale));
	}

	/**
	 * convert String with the specified locale to java.util.Date
	 *
	 * @param source the String to be convert
	 * @param locate the locate of the string
	 * @return result Date
	 */
	public static Date toDate(String source, ULocale locale) throws BirtException {
		return toDate(source, locale, null);
	}

	/**
	 * Parses a date/time string
	 *
	 * @param source
	 * @param locale
	 * @param timeZone
	 * @return
	 * @throws BirtException
	 */
	public static Date toDate(String source, ULocale locale, TimeZone timeZone) throws BirtException {
		DateFormat dateFormat = (DateFormat) getDateFormatObject(source, locale, timeZone).clone();
		Date resultDate = null;
		try {
			if (timeZone != null) {
				dateFormat.setTimeZone(timeZone);
			}
			resultDate = dateFormat.parse(source);
			return resultDate;
		} catch (ParseException e) {
			throw new CoreException(ResourceConstants.CONVERT_FAILS, new Object[] { source.toString(), "Date" });
		}
	}

	/**
	 * @deprecated use getDateFormatObject instead
	 */
	@Deprecated
	public static DateFormat getDateFormat(String source, ULocale locale, TimeZone timeZone) throws BirtException {
		return getDateFormatObject(source, locale, timeZone);
	}

	/**
	 * Retrieve date format object that matches the given date/time string
	 *
	 * @since 4.8
	 *
	 * @param source
	 * @param locale
	 * @param timeZone
	 * @return
	 * @throws BirtException
	 */
	public static DateFormat getDateFormatObject(String source, ULocale locale, TimeZone timeZone)
			throws BirtException {
		if (source == null) {
			return null;
		}

		DateFormat dateFormat = null;
		Date resultDate = null;

		boolean existTime = p1.matcher(source).matches() || p2.matcher(source).matches();

		for (int i = DEFAULT_DATE_STYLE; i <= DateFormat.SHORT; i++) {
			for (int j = DEFAULT_DATE_STYLE; j <= DateFormat.SHORT; j++) {
				dateFormat = DateFormatFactory.getDateTimeInstance(i, j, locale);
				TimeZone savedTimeZone = null;
				if (timeZone != null) {
					savedTimeZone = dateFormat.getTimeZone();
					dateFormat.setTimeZone(timeZone);
				}
				try {
					resultDate = dateFormat.parse(source);
					return dateFormat;
				} catch (ParseException e1) {
				} finally {
					if (savedTimeZone != null) {
						dateFormat.setTimeZone(savedTimeZone);
					}
				}
			}

			// only Date, no Time
			if (!existTime) {
				dateFormat = DateFormatFactory.getDateInstance(i, locale);
				TimeZone savedTimeZone = null;
				if (timeZone != null) {
					savedTimeZone = dateFormat.getTimeZone();
					dateFormat.setTimeZone(timeZone);
				}
				try {
					resultDate = dateFormat.parse(source);
					return dateFormat;
				} catch (ParseException e1) {
				} finally {
					if (savedTimeZone != null) {
						dateFormat.setTimeZone(savedTimeZone);
					}
				}
			}
		}

		// for the String can not be parsed, throws a BirtException
		if (resultDate == null) {
			throw new CoreException(ResourceConstants.CONVERT_FAILS, new Object[] { source.toString(), "Date" });
		}

		// never access here
		return dateFormat;
	}

	/**
	 * Convert a string to a Date instance according to the TimeZone value
	 *
	 * @param source
	 * @param timeZone
	 * @return
	 * @throws BirtException
	 */
	public static Date toDate(String source, TimeZone timeZone) throws BirtException {
		assert timeZone != null;
		try {
			return toDateISO8601(source, timeZone);
		} catch (BirtException e) {
			try {
				// format the String for JRE default locale
				return toDate(source, JRE_DEFAULT_LOCALE, timeZone);
			} catch (BirtException use) {
				// format the String for Locale.US
				return toDate(source, DEFAULT_LOCALE, timeZone);
			}
		}
	}

	/**
	 * A temp solution to the adoption of ICU4J in BIRT. It is a simple delegation
	 * to toDateWithCheck( String, Locale ).
	 *
	 * @param source
	 * @param locale
	 * @return Date
	 * @throws BirtException
	 */
	public static Date toDateWithCheck(String source, Locale locale) throws BirtException {
		return toDateWithCheck(source, ULocale.forLocale(locale));
	}

	/**
	 * Convert string to date with check. JDK may do incorrect converse, for
	 * example: 2005/1/1 Local.US, format pattern is MM/dd/YY. Above conversion can
	 * be done without error, but obviously the result is not right. This method
	 * will do such a simple check, in DateFormat.SHORT case instead of all cases.
	 * Year is not lower than 0. Month is from 1 to 12. Day is from 1 to 31.
	 *
	 * @param source
	 * @param locale
	 * @return Date
	 * @throws BirtException
	 */
	public static Date toDateWithCheck(String source, ULocale locale) throws BirtException {
		DateFormat dateFormat = DateFormatFactory.getDateInstance(DateFormat.SHORT, locale);
		Date resultDate = null;
		try {
			resultDate = dateFormat.parse(source);
		} catch (ParseException e) {
			return toDate(source, locale);
		}

		// check whether conversion is correct
		if (!DateUtil.checkValid(dateFormat, source)) {
			throw new CoreException(ResourceConstants.CONVERT_FAILS, new Object[] { source.toString(), "Date" });
		}

		return resultDate;
	}

	public static Double toDouble(Object source, Locale locale) throws CoreException {
		return toDouble(source, ULocale.forLocale(locale));
	}

	public static Double toDouble(Object source, ULocale locale) throws CoreException {
		if (source == null) {
			return null;
		}

		if (source instanceof Double) {
			return (Double) source;
		} else if (source instanceof Number) {
			if (!isConvertableToDouble((Number) source)) {
				throw new CoreException(ResourceConstants.CONVERT_FAILS, new Object[] { source.toString(), "Double" });
			}
			double doubleValue = ((Number) source).doubleValue();
			return new Double(doubleValue);
		} else if (source instanceof Boolean) {
			if (((Boolean) source).booleanValue()) {
				return new Double(1d);
			}
			return new Double(0d);
		} else if (source instanceof Date) {
			double doubleValue = ((Date) source).getTime();
			return new Double(doubleValue);
		} else if (source instanceof CharSequence) {
			source = source.toString();
			try {
				return Double.valueOf((String) source);
			} catch (NumberFormatException e) {
				try {
					Number number = NumberFormat.getInstance(locale == null ? JRE_DEFAULT_LOCALE : locale)
							.parse((String) source);
					if (number != null) {
						if (!isConvertableToDouble(number)) {
							throw new CoreException(ResourceConstants.CONVERT_FAILS,
									new Object[] { source.toString(), "Double" });
						}
						return new Double(number.doubleValue());
					}

					throw new CoreException(ResourceConstants.CONVERT_FAILS,
							new Object[] { source.toString(), "Double" });
				} catch (ParseException e1) {
					throw new CoreException(ResourceConstants.CONVERT_FAILS,
							new Object[] { source.toString(), "Double" });
				}
			}
		} else {
			throw new CoreException(ResourceConstants.CONVERT_FAILS, new Object[] { source.toString(), "Double" });
		}
	}

	/**
	 * Boolean -> Double true -> 1 others -> 0 Date -> Double Date.getTime(); String
	 * -> Double Double.valueOf(String);
	 *
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static Double toDouble(Object source) throws BirtException {
		return toDouble(source, JRE_DEFAULT_LOCALE);
	}

	/**
	 * Number -> String Number.toString() Boolean -> String Boolean.toString() Date
	 * -> String toString(Date)
	 *
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static String toString(Object source) throws BirtException {
		return toString(source, JRE_DEFAULT_LOCALE);
	}

	/**
	 * A temp solution to the adoption of ICU4J. It is a simple delegation to
	 * toString( Object, Locale ).
	 *
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static String toString(Object source, Locale locale) throws BirtException {
		return toString(source, ULocale.forLocale(locale));
	}

	/**
	 * Convert an object to an locale neutral String value. For Date values we will
	 * convert to ISO8601 format. User can specify the time zone to output.
	 *
	 * @param source
	 * @param zone
	 * @return
	 * @throws BirtException
	 */
	public static String toLocaleNeutralString(Object source, TimeZone zone) throws BirtException {
		if (source == null) {
			return null;
		}
		if (source instanceof Time) {
			return toLocaleNeutralString(source);
		} else if (source instanceof java.sql.Date) {
			return toLocaleNeutralString(source);
		} else if (source instanceof Timestamp) {
			return toLocaleNeutralString(source);
		} else if (source instanceof Date) {
			return DateFormatISO8601.format((Date) source, zone);
		} else {
			return toLocaleNeutralString(source);
		}
	}

	/**
	 * Convert an object to an locale neutral String value. For Date values we will
	 * convert to ISO8601 format. This will always output default(current) time
	 * zone.
	 *
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static String toLocaleNeutralString(Object source) throws BirtException {
		if (source == null) {
			return null;
		}
		if (source instanceof Time) {
			return ((Time) source).toString();
		} else if (source instanceof java.sql.Date) {
			return ((java.sql.Date) source).toString();
		} else if (source instanceof Timestamp) {
			return ((java.sql.Timestamp) source).toString();
		} else if (source instanceof Date) {
			return DateFormatISO8601.format((Date) source);
		} else if (source instanceof Number) {
			return ((Number) source).toString();
		} else {
			return toLimitedSizeString(source);
		}
	}

	/**
	 * Number -> String Number.toString() Boolean -> String Boolean.toString() Date
	 * -> String toString(Date,locale)
	 *
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static String toString(Object source, ULocale locale) throws BirtException {
		if (source == null) {
			return null;
		}
		if (source instanceof Time) {
			return toString((Date) source, locale);
		} else if (source instanceof java.sql.Date) {
			return ((java.sql.Date) source).toString();
		} else if (source instanceof Timestamp) {
			return ((java.sql.Timestamp) source).toString();
		} else if (source instanceof Date) {
			return toString((Date) source, locale);
		} else if (source instanceof Number) {
			return toString((Number) source, locale);
		} else {
			return toLimitedSizeString(source);
		}
	}

	/**
	 *
	 * @param source
	 * @return
	 */
	private static String toLimitedSizeString(Object source) {
		if (source instanceof byte[]) {
			StringBuilder buf = new StringBuilder();
			final int strLength = 8;

			byte[] sourceValue = (byte[]) source;
			int length = Math.min(sourceValue.length, strLength);
			for (int i = 0; i < length; i++) {
				buf.append(Integer.toHexString(sourceValue[i]).toUpperCase());
				buf.append(" ");
			}
			if (sourceValue.length > strLength) {
				buf.append("...");
			}
			return buf.toString();
		} else {
			return source.toString();
		}
	}

	/**
	 *
	 * @param source
	 * @param locale
	 * @return
	 */
	private static String toString(Number source, ULocale locale) {
		NumberFormat nf = (NumberFormat) nfMap.get(locale);
		if (nf == null) {
			synchronized (nfMap) {
				nf = (NumberFormat) nfMap.get(locale);
				if (nf == null) {
					nf = NumberFormat.getInstance(locale);
					nfMap.put(locale, nf);
				}
			}
		}
		return nf.format(source);
	}

	/**
	 * Converting Blob to/from other types is not currently supported
	 *
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static Blob toBlob(Object source) throws BirtException {
		// Converting Blob to/from other types is not currently supported
		if (source == null) {
			return null;
		}

		if (source instanceof Blob) {
			return (Blob) source;
		} else if (source instanceof byte[]) {
			try {
				return new SerialBlob((byte[]) source);
			} catch (Exception e) {
				throw new CoreException(ResourceConstants.CONVERT_FAILS, new Object[] { source.toString(), "Blob" });
			}
		} else {
			throw new CoreException(ResourceConstants.CONVERT_FAILS, new Object[] { source.toString(), "Blob" });
		}
	}

	/**
	 * @param source
	 * @return byte array
	 * @throws BirtException
	 */
	public static byte[] toBytes(Object source) throws BirtException {
		// Converting Blob to/from other types is not currently supported
		if (source == null) {
			return null;
		}

		if (source instanceof byte[]) {
			return (byte[]) source;
		} else if (source instanceof Blob) {
			try {
				return ((Blob) source).getBytes((long) 1, (int) ((Blob) source).length());
			} catch (SQLException e) {
				throw new CoreException(ResourceConstants.CONVERT_FAILS, new Object[] { source.toString(), "Binary" });

			}
		} else {
			throw new CoreException(ResourceConstants.CONVERT_FAILS, new Object[] { source.toString(), "Binary" });
		}
	}

	/**
	 * Converts a Java class to its corresponding data type constant defined in
	 * DataType
	 */
	public static int toApiDataType(Class clazz) {
		if (clazz == null) {
			return DataType.UNKNOWN_TYPE;
		}

		if (clazz == DataType.AnyType.class) {
			return DataType.ANY_TYPE;
		} else if (Integer.class.isAssignableFrom(clazz)) {
			return DataType.INTEGER_TYPE;
		} else if (Double.class.isAssignableFrom(clazz)) {
			return DataType.DOUBLE_TYPE;
		} else if (String.class.isAssignableFrom(clazz)) {
			return DataType.STRING_TYPE;
		} else if (BigDecimal.class.isAssignableFrom(clazz)) {
			return DataType.DECIMAL_TYPE;
		} else if (clazz == java.sql.Date.class) {
			return DataType.SQL_DATE_TYPE;
		} else if (clazz == java.sql.Time.class) {
			return DataType.SQL_TIME_TYPE;
		} else if (Date.class.isAssignableFrom(clazz)) {
			return DataType.DATE_TYPE;
		} else if (byte[].class.isAssignableFrom(clazz)) {
			return DataType.BINARY_TYPE;
		} else if (Clob.class.isAssignableFrom(clazz)
				|| clazz.getName().equals("org.eclipse.datatools.connectivity.oda.IClob")) {
			return DataType.STRING_TYPE;
		} else if (Blob.class.isAssignableFrom(clazz)
				|| clazz.getName().equals("org.eclipse.datatools.connectivity.oda.IBlob")) {
			return DataType.BLOB_TYPE;
		} else if (clazz == Boolean.class) {
			return DataType.BOOLEAN_TYPE;
		} else if (clazz == Object.class) {
			return DataType.JAVA_OBJECT_TYPE;
		}

		// any other types are not recognized nor supported;
		return DataType.UNKNOWN_TYPE;
	}

	public static Class fromApiDataTypeToJavaClass(int apiDataType) {
		switch (apiDataType) {
		case DataType.ANY_TYPE:
			return DataType.AnyType.class;
		case DataType.INTEGER_TYPE:
			return Integer.class;
		case DataType.DOUBLE_TYPE:
			return Double.class;
		case DataType.STRING_TYPE:
			return String.class;
		case DataType.DECIMAL_TYPE:
			return BigDecimal.class;
		case DataType.SQL_DATE_TYPE:
			return java.sql.Date.class;
		case DataType.SQL_TIME_TYPE:
			return java.sql.Time.class;
		case DataType.DATE_TYPE:
			return java.util.Date.class;
		case DataType.BINARY_TYPE:
			return byte[].class;
		case DataType.BOOLEAN_TYPE:
			return Boolean.class;
		case DataType.JAVA_OBJECT_TYPE:
			return Object.class;
		case DataType.BLOB_TYPE:
			try {
				Class c = Class.forName("org.eclipse.datatools.connectivity.oda.IBlob");
				return c;
			} catch (Exception exp) {

			}
		default:
			return DataType.AnyType.class;
		}

	}

	/**
	 * Converts an ODA data type code to its corresponding Data Engine API data type
	 * constant defined in DataType.
	 *
	 * @param odaDataTypeCode an ODA data type code
	 * @throws BirtException if the specified ODA data type code is not a supported
	 *                       type
	 */
	public static int toApiDataType(int odaDataTypeCode) throws BirtException {
		Class odiTypeClass = toOdiTypeClass(odaDataTypeCode);
		return toApiDataType(odiTypeClass);
	}

	/**
	 * Convert object to a suitable type from its value Object -> Integer -> Double
	 * -> BigDecimal -> Date -> String
	 */
	public static Object toAutoValue(Object evaValue) {
		if (evaValue == null) {
			return null;
		}

		Object value = null;
		if (evaValue instanceof CharSequence) {
			// 1: to Integer
			String stringValue = evaValue.toString();
			value = toIntegerValue(evaValue);
			if (value == null) {
				try {
					// 2: to Double
					value = Double.valueOf(stringValue);
				} catch (NumberFormatException e1) {
					try {
						// 3: to BigDecimal
						value = new BigDecimal(stringValue);
					} catch (NumberFormatException e2) {
						try {
							// 4: to Date
							value = toDate(stringValue);
						} catch (BirtException e3) {
							value = evaValue;
						}
					}
				}
			}
		}
		return value;
	}

	/**
	 * convert object to Integer. If fails, return null. Object -> Integer
	 */
	public static Integer toIntegerValue(Object evaValue) {
		// to Integer
		Integer value = null;
		if (evaValue instanceof CharSequence) {
			String stringValue = evaValue.toString();
			try {
				// 1: to Integer
				value = Integer.valueOf(stringValue);
			} catch (NumberFormatException e1) {
				try {
					Double ddValue = Double.valueOf(stringValue);
					int intValue = ddValue.intValue();
					double doubleValue = ddValue.doubleValue();
					// TODO: improve this implementation
					// here examine whether the two values are equal.1.0e-5
					if (Math.abs(intValue - doubleValue) < 0.0000001) {
						value = Integer.valueOf(String.valueOf(intValue));
					} else {
						value = null;
					}
				} catch (NumberFormatException e2) {
					value = null;
				}
			}
		}
		return value;
	}

	/**
	 * Convert String without specified locale to java.util.Date Try to format the
	 * given String for JRE default Locale, if it fails, try to format the String
	 * for Locale.US
	 *
	 * @param source the String to be convert
	 * @param locate the locate of the string
	 * @return result Date
	 */
	private static Date toDate(String source) throws BirtException {
		source = source.trim();
		try {
			return toDateISO8601(source, null);
		} catch (BirtException e) {
			try {
				// format the String for JRE default locale
				return toDate(source, JRE_DEFAULT_LOCALE);
			} catch (BirtException use) {
				try {
					// format the String for Locale.US
					return toDate(source, DEFAULT_LOCALE);
				} catch (BirtException de) {
					return toDateForSpecialFormat(source);
				}
			}
		}
	}

	/**
	 * convert String with ISO8601 date format to java.util.Date
	 *
	 * @param source the String to be convert
	 * @param locate the locate of the string
	 * @return result Date
	 */
	private static Date toDateISO8601(String source, TimeZone timeZone) throws BirtException {
		Date resultDate = null;

		try {
			resultDate = DateFormatISO8601.parse(source, timeZone);
			return resultDate;
		} catch (ParseException e1) {
			throw new CoreException(ResourceConstants.CONVERT_FAILS, new Object[] { source.toString(), "Date" });
		}
	}

	private static Date toDateForSpecialFormat(String source) throws BirtException {
		try {
			return MysqlUSDateFormatter.parse(source);
		} catch (ParseException e1) {
			throw new CoreException(ResourceConstants.CONVERT_FAILS, new Object[] { source.toString(), "Date" });
		}
	}

	/**
	 * Find the date format pattern string for a given datetime string without
	 * specified locale. If a suitable date format cannot be found or the pattern
	 * string cannot be retrieved, returns null
	 *
	 * @since 4.8
	 *
	 * @param source
	 * @return
	 * @throws BirtException
	 */
	public static String getDateFormat(String source) throws BirtException {
		source = source.trim();
		SimpleDateFormat sdf = null;
		try {
			sdf = DateFormatISO8601.getSimpleDateFormat(source, null);
		} catch (BirtException e) {
			try {
				DateFormat dateformat = getDateFormatObject(source, JRE_DEFAULT_LOCALE, null);
				sdf = (SimpleDateFormat) dateformat;
			} catch (BirtException use) {
				try {
					DateFormat dateformat = getDateFormatObject(source, DEFAULT_LOCALE, null);
					sdf = (SimpleDateFormat) dateformat;
				} catch (BirtException de) {
					try {
						MysqlUSDateFormatter.parse(source);
						return "M/d/yyyy HH:mm";
					} catch (ParseException e1) {
					}
				}
			} catch (ClassCastException ce) {
				// If a DateFormat cannot be cast to SimpleDateFormat, then
				// it will not be able to return its format pattern string
			}
		}

		if (sdf != null) {
			return sdf.toPattern();
		}
		return null;
	}

	/**
	 * Call org.eclipse.birt.core.format.DateFormatter
	 *
	 * @param source
	 * @return
	 */
	private static String toString(Date source, ULocale locale) {
		DateFormatter df;

		// avoid any multi-thread issue
		df = (DateFormatter) (dfMap.get(locale));
		if (df == null) {
			synchronized (dfMap) {
				df = (DateFormatter) (dfMap.get(locale));
				if (df == null) {
					df = new DateFormatter(locale);
					dfMap.put(locale, df);
				}
			}
		}

		return df.format((Date) source);
	}

	/**
	 * Converts an ODA data type code to the Java class of its corresponding Data
	 * Engine ODI data type. <br>
	 * <br>
	 * <b>ODA Data Type -> ODI Type Class</b><br>
	 * <i>Integer -> java.lang.Integer<br>
	 * Double -> java.lang.Double<br>
	 * Character -> java.lang.String<br>
	 * Decimal -> java.math.BigDecimal<br>
	 * Date -> java.sql.Date<br>
	 * Time -> java.sql.Time<br>
	 * Timestamp -> java.sql.Timestamp<br>
	 * Blob -> java.sql.Blob<br>
	 * Clob -> java.sql.Clob<br>
	 * Boolean -> java.lang.Boolean<br>
	 * JavaObject -> java.lang.Object<br>
	 * </i>
	 *
	 * @param odaDataTypeCode an ODA data type code
	 * @return the ODI type class that corresponds with the specified ODA data type
	 * @throws BirtException if the specified ODA data type is not a supported type
	 */
	public static Class toOdiTypeClass(int odaDataTypeCode) throws BirtException {
		if (odaDataTypeCode != Types.CHAR && odaDataTypeCode != Types.INTEGER && odaDataTypeCode != Types.DOUBLE
				&& odaDataTypeCode != Types.DECIMAL && odaDataTypeCode != Types.DATE && odaDataTypeCode != Types.TIME
				&& odaDataTypeCode != Types.TIMESTAMP && odaDataTypeCode != Types.BLOB && odaDataTypeCode != Types.CLOB
				&& odaDataTypeCode != Types.BOOLEAN && odaDataTypeCode != Types.JAVA_OBJECT
				&& odaDataTypeCode != Types.NULL) {
			throw new CoreException(ResourceConstants.INVALID_TYPE);
		}

		Class fieldClass = null;
		switch (odaDataTypeCode) {
		case Types.CHAR:
			fieldClass = String.class;
			break;

		case Types.INTEGER:
			fieldClass = Integer.class;
			break;

		case Types.DOUBLE:
			fieldClass = Double.class;
			break;

		case Types.DECIMAL:
			fieldClass = BigDecimal.class;
			break;

		case Types.DATE:
			fieldClass = java.sql.Date.class;
			break;

		case Types.TIME:
			fieldClass = Time.class;
			break;

		case Types.TIMESTAMP:
			fieldClass = Timestamp.class;
			break;

		case Types.BLOB:
			fieldClass = Blob.class;
			break;

		case Types.CLOB:
			fieldClass = Clob.class;
			break;

		case Types.BOOLEAN:
			fieldClass = Boolean.class;
			break;

		case Types.JAVA_OBJECT:
			fieldClass = Object.class;
			break;

		case Types.NULL:
			fieldClass = null;
			break;
		}

		return fieldClass;
	}

	/**
	 * Converts an ODI type class to its corresponding ODA data type code. <br>
	 * <b>ODI Type Class -> ODA Data Type</b><br>
	 * <i>java.lang.Integer -> Integer<br>
	 * java.lang.Double -> Double<br>
	 * java.lang.String -> Character<br>
	 * java.math.BigDecimal -> Decimal<br>
	 * java.util.Date -> Timestamp<br>
	 * java.sql.Date -> Date<br>
	 * java.sql.Time -> Time<br>
	 * java.sql.Timestamp -> Timestamp<br>
	 * java.sql.Blob -> Blob<br>
	 * java.sql.Clob -> Clob<br>
	 * java.lang.Boolean -> Boolean<br>
	 * java.lang.Object -> JavaObject<br>
	 * </i><br>
	 * All other type classes are mapped to the ODA String data type.
	 *
	 * @param odiTypeClass a type class used by the Data Engine ODI component
	 * @return the ODA data type that maps to the ODI type class.
	 */
	public static int toOdaDataType(Class odiTypeClass) {
		int odaType = Types.CHAR; // default

		if (odiTypeClass == null) {
			odaType = Types.CHAR;
		} else if (odiTypeClass == String.class) {
			odaType = Types.CHAR;
		} else if (odiTypeClass == Integer.class) {
			odaType = Types.INTEGER;
		} else if (odiTypeClass == Double.class) {
			odaType = Types.DOUBLE;
		} else if (odiTypeClass == BigDecimal.class) {
			odaType = Types.DECIMAL;
		} else if (odiTypeClass == Time.class) {
			odaType = Types.TIME;
		} else if (odiTypeClass == Timestamp.class) {
			odaType = Types.TIMESTAMP;
		} else if (odiTypeClass == java.sql.Date.class) {
			odaType = Types.DATE;
		} else if (odiTypeClass == java.util.Date.class) {
			odaType = Types.TIMESTAMP;
		} else if (odiTypeClass == Blob.class) {
			odaType = Types.BLOB;
		} else if (odiTypeClass == Clob.class) {
			odaType = Types.CLOB;
		} else if (odiTypeClass == Boolean.class) {
			odaType = Types.BOOLEAN;
		} else if (odiTypeClass == Object.class) {
			odaType = Types.JAVA_OBJECT;
		}

		return odaType;
	}

	private static boolean isConvertableToInteger(Number n) {
		assert n != null;

		long longValue = n.longValue();
		return longValue >= Integer.MIN_VALUE && longValue <= Integer.MAX_VALUE;

	}

	private static boolean isConvertableToDouble(Number n) {
		assert n != null;

		double doubleValue = n.doubleValue();
		return !Double.isInfinite(doubleValue);

	}
}
