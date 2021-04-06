/**
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.adapter.api.timeFunction;

import java.util.Date;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.i18n.AdapterResourceHandle;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import com.ibm.icu.util.Calendar;

public class DateTimeUtility {
	/**
	 * Return date portion according the expected time type
	 * 
	 * @param date
	 * @param timeType see DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_MONTH
	 *                 etc portions.
	 * @param calendar
	 * @return
	 * @throws AdapterException
	 */
	public static int getPortion(Object date, String timeType, Calendar calendar) throws AdapterException {
		if (calendar == null) {
			calendar = Calendar.getInstance();
		}
		if (date == null)
			return new Integer(0);
		if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_MONTH.equals(timeType)) {
			return new Integer(getCalendar(calendar, date).get(Calendar.DAY_OF_MONTH));
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_WEEK.equals(timeType)) {
			return new Integer(getCalendar(calendar, date).get(Calendar.DAY_OF_WEEK));
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_YEAR.equals(timeType)) {
			return new Integer(getCalendar(calendar, date).get(Calendar.DAY_OF_YEAR));
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_MONTH.equals(timeType)) {
			return new Integer(getCalendar(calendar, date).get(Calendar.WEEK_OF_MONTH));
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_YEAR.equals(timeType)) {
			return new Integer(getCalendar(calendar, date).get(Calendar.WEEK_OF_YEAR));
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MONTH.equals(timeType)) {
			return new Integer(getCalendar(calendar, date).get(Calendar.MONTH) + 1);
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_QUARTER.equals(timeType)) {
			int month = -1;
			month = getCalendar(calendar, date).get(Calendar.MONTH);
			int quarter = -1;
			switch (month) {
			case Calendar.JANUARY:
			case Calendar.FEBRUARY:
			case Calendar.MARCH:
				quarter = 1;
				break;
			case Calendar.APRIL:
			case Calendar.MAY:
			case Calendar.JUNE:
				quarter = 2;
				break;
			case Calendar.JULY:
			case Calendar.AUGUST:
			case Calendar.SEPTEMBER:
				quarter = 3;
				break;
			case Calendar.OCTOBER:
			case Calendar.NOVEMBER:
			case Calendar.DECEMBER:
				quarter = 4;
				break;
			default:
				quarter = -1;
			}
			return new Integer(quarter);
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_WEEK_OF_QUARTER.equals(timeType)) {
			int[] dayCountPerQtr_NLY = { 90, 91, 92, 92 };
			int[] dayCountPerQtr_LY = { 91, 91, 92, 92 };
			int month = getCalendar(calendar, date).get(Calendar.MONTH);
			int dayOfYear = getCalendar(calendar, date).get(Calendar.DAY_OF_YEAR);
			int year = getCalendar(calendar, date).get(Calendar.YEAR);
			int quarter = -1;
			boolean isLeapYear = false;
			int[] dayCountPerQtr = null;
			switch (month) {
			case Calendar.JANUARY:
			case Calendar.FEBRUARY:
			case Calendar.MARCH:
				quarter = 1;
				break;
			case Calendar.APRIL:
			case Calendar.MAY:
			case Calendar.JUNE:
				quarter = 2;
				break;
			case Calendar.JULY:
			case Calendar.AUGUST:
			case Calendar.SEPTEMBER:
				quarter = 3;
				break;
			case Calendar.OCTOBER:
			case Calendar.NOVEMBER:
			case Calendar.DECEMBER:
				quarter = 4;
				break;
			default:
				quarter = -1;
			}
			if ((year % 4 == 0) && year % 100 != 0) {
				isLeapYear = true;
			} else if (year % 400 == 0) {
				isLeapYear = true;
			} else {
				isLeapYear = false;
			}

			if (isLeapYear) {
				dayCountPerQtr = dayCountPerQtr_LY;
			} else {
				dayCountPerQtr = dayCountPerQtr_NLY;
			}
			int totalDaysTillQtr = 0;
			for (int i = 0; i < quarter - 1; i++) {
				totalDaysTillQtr += dayCountPerQtr[i];
			}
			int dayOfQtr = dayOfYear - totalDaysTillQtr;
			int dayOfWeek = getCalendar(calendar, date).get(Calendar.DAY_OF_WEEK);
			// Finding last nearest sunday
			int dayCountTillLastSunday = dayOfQtr - dayOfWeek + 1;
			int weekOfQuarter = 1;
			while (dayCountTillLastSunday > 1) {
				dayCountTillLastSunday -= 7;
				weekOfQuarter++;
			}
			// Using a formula to represent each Week of a Year uniquely.
			// To handle aggregations done across years which includes a Leap Year
			weekOfQuarter = (quarter * 100) + weekOfQuarter;
			return new Integer(weekOfQuarter);
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_DAY_OF_QUARTER.equals(timeType)) {
			int[] dayCountPerQtr_NLY = { 90, 91, 92, 92 };
			int[] dayCountPerQtr_LY = { 91, 91, 92, 92 };
			int month = getCalendar(calendar, date).get(Calendar.MONTH);
			int dayOfYear = getCalendar(calendar, date).get(Calendar.DAY_OF_YEAR);
			int year = getCalendar(calendar, date).get(Calendar.YEAR);
			int quarter = -1;
			boolean isLeapYear = false;
			int[] dayCountPerQtr = null;
			switch (month) {
			case Calendar.JANUARY:
			case Calendar.FEBRUARY:
			case Calendar.MARCH:
				quarter = 1;
				break;
			case Calendar.APRIL:
			case Calendar.MAY:
			case Calendar.JUNE:
				quarter = 2;
				break;
			case Calendar.JULY:
			case Calendar.AUGUST:
			case Calendar.SEPTEMBER:
				quarter = 3;
				break;
			case Calendar.OCTOBER:
			case Calendar.NOVEMBER:
			case Calendar.DECEMBER:
				quarter = 4;
				break;
			default:
				quarter = -1;
			}
			if ((year % 4 == 0) && year % 100 != 0) {
				isLeapYear = true;
			} else if (year % 400 == 0) {
				isLeapYear = true;
			} else {
				isLeapYear = false;
			}

			if (isLeapYear) {
				dayCountPerQtr = dayCountPerQtr_LY;
			} else {
				dayCountPerQtr = dayCountPerQtr_NLY;
			}
			int dayOfQtr = 0;
			int totalDaysTillQtr = 0;
			for (int i = 0; i < quarter - 1; i++) {
				totalDaysTillQtr += dayCountPerQtr[i];
			}
			dayOfQtr = dayOfYear - totalDaysTillQtr;
			// Using a formula to represent each day of a year uniquely.
			// To handle aggregations done across years which includes a Leap Year
			dayOfQtr = (quarter * 100) + dayOfQtr;
			return new Integer(dayOfQtr);
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR.equals(timeType)) {
			return new Integer(getCalendar(calendar, date).get(Calendar.YEAR));
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_HOUR.equals(timeType)) {
			return new Integer(getCalendar(calendar, date).get(Calendar.HOUR_OF_DAY));
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_MINUTE.equals(timeType)) {
			return new Integer(getCalendar(calendar, date).get(Calendar.MINUTE));
		} else if (DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_SECOND.equals(timeType)) {
			return Integer.valueOf(getCalendar(calendar, date).get(Calendar.SECOND));
		} else
			throw new AdapterException(ResourceConstants.INVALID_DATE_TIME_TYPE, timeType);
	}

	private static Calendar getCalendar(Calendar calendar, Object d) {
		assert d != null;
		Date date;
		try {
			if (d instanceof java.sql.Date) {
				calendar.setTime((Date) d);
				return calendar;
			}
			date = DataTypeUtil.toDate(d);
			calendar.setTime(date);
			return calendar;
		} catch (BirtException e) {
			throw new java.lang.IllegalArgumentException(
					AdapterResourceHandle.getInstance().getMessage(ResourceConstants.INVALID_DATETIME_VALUE));
		}
	}
}
