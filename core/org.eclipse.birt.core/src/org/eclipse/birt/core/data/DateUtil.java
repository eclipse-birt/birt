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
package org.eclipse.birt.core.data;

import java.util.regex.Pattern;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.GregorianCalendar;

/**
 * Date util class, which is used to check whether String can be correctly
 * converted to Date.
 */
public class DateUtil {
	private final static int[] DAYS_MONTH = new int[] { 31, -1, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	private static GregorianCalendar calendarInstance = null;

	/**
	 * All possible split char in differnt Locales. '/' Locale_US Locale_UK ... '-'
	 * Locale_CHINA '.' Locale_GERMAN
	 */
	private static String[] splitStrs = new String[] { "/", "-", "." };

	private static Pattern[] splitPattern = new Pattern[] { Pattern.compile("/"), Pattern.compile("-"),
			Pattern.compile(".") };

	/**
	 * Check whether dateStr can be correctly converted to Date in format of
	 * DateFormat.SHORT. Here one point must be noticed that dateStr should firstly
	 * be able to be converted to Date.
	 *
	 * @param df
	 * @param dateStr
	 * @return checkinfo
	 */
	public static boolean checkValid(DateFormat df, String dateStr) {
		assert df != null;
		assert dateStr != null;

		boolean isValid = true;
		if (df instanceof SimpleDateFormat) {
			String[] dateResult = splitDateStr(dateStr);

			SimpleDateFormat sdf = (SimpleDateFormat) df;
			String pattern = sdf.toPattern();
			String[] patternResult = splitDateStr(pattern);

			if (dateResult != null && patternResult != null) {
				isValid = isMatch(dateResult, patternResult);
			}
		}

		return isValid;
	}

	/**
	 * Split date string to 3 size of string array example: 05/04/2005 [05, 04,
	 * 2005] MM/dd/yy [MM, dd, yy]
	 *
	 * @param dateStr
	 * @return
	 */
	private static String[] splitDateStr(String dateStr) {
		Pattern pattern = null;
		for (int i = 0; i < splitStrs.length; i++) {
			if (dateStr.indexOf(splitStrs[i]) >= 0) {
				pattern = splitPattern[i];
				break;
			}
		}
		if (pattern == null) {
			return null;
		}

		String[] result = pattern.split(dateStr);
		if (result.length != 3) {
			return null;
		}

		for (int i = 0; i < result.length; i++) {
			result[i] = result[i].trim();
		}

		return result;
	}

	/**
	 * Check whether dateStr matches patterStr
	 *
	 * @param dateStr
	 * @param patternStr
	 * @return true match false does not match
	 */
	private static boolean isMatch(String[] dateStr, String[] patternStr) {
		assert dateStr != null;
		assert patternStr != null;

		int year = -1;
		int month = -1;
		int day = -1;
		for (int i = 0; i < dateStr.length; i++) {
			int value = Integer.parseInt(dateStr[i]);
			if (patternStr[i].startsWith("y") || patternStr[i].startsWith("Y")) {
				year = value;
			} else if (patternStr[i].startsWith("M") || patternStr[i].startsWith("m")) {
				month = value;
			} else if (patternStr[i].startsWith("d") || patternStr[i].startsWith("D")) {
				day = value;
			}
		}

		boolean result = true;
		if (year < 0 || month < 1 || month > 12 || isInvalidDay(day, year, month)) {
			result = false;
		}

		return result;
	}

	/**
	 * Check whether day is invalid day based on its year and month
	 *
	 * @param day   needs to be checked
	 * @param year  valid year
	 * @param month valid month
	 * @return true invalid day
	 */
	private static boolean isInvalidDay(int day, int year, int month) {
		if (calendarInstance == null) {
			calendarInstance = new GregorianCalendar();
		}

		int dayOfMonth = DAYS_MONTH[month - 1];
		if (month == 2) {
			if (calendarInstance.isLeapYear(year)) {
				dayOfMonth = 29;
			} else {
				dayOfMonth = 28;
			}
		}

		return day < 1 || day > dayOfMonth;
	}

}
