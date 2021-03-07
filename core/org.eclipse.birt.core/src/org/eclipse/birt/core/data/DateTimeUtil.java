/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.data;

import java.util.Date;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 *
 */

public class DateTimeUtil {
	private ULocale locale;
	private TimeZone timeZone;

	public DateTimeUtil(ULocale locale, TimeZone timeZone) {
		this.locale = locale == null ? ULocale.getDefault() : locale;
		this.timeZone = timeZone == null ? TimeZone.getDefault() : timeZone;
	}

	/**
	 * 4-digit year number of date/time value d
	 *
	 * @param d
	 * @return
	 */
	public int year(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException("date value is null!");
		}

		return getCalendar(d).get(Calendar.YEAR);
	}

	/**
	 * Quarter number (1 to 4) of date/time value d
	 *
	 * @param d
	 * @return
	 */
	public int quarter(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException("date value is null!");
		}

		int month = getCalendar(d).get(Calendar.MONTH);
		switch (month) {
		case Calendar.JANUARY:
		case Calendar.FEBRUARY:
		case Calendar.MARCH:
			return 1;
		case Calendar.APRIL:
		case Calendar.MAY:
		case Calendar.JUNE:
			return 2;
		case Calendar.JULY:
		case Calendar.AUGUST:
		case Calendar.SEPTEMBER:
			return 3;
		case Calendar.OCTOBER:
		case Calendar.NOVEMBER:
		case Calendar.DECEMBER:
			return 4;
		default:
			return -1;
		}
	}

	/**
	 * Month of date/time value d. Return month number (1-12)
	 *
	 * @param d
	 * @return
	 */
	public int month(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException("date value is null!");
		}

		return getCalendar(d).get(Calendar.MONTH) + 1;
	}

	/**
	 * Day the week. Return a number 1 (Sunday) to 7 (Saturday).
	 *
	 * @param d
	 * @return
	 */
	public String weekDay(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException("date value is null!");
		}

		return String.valueOf(getCalendar(d).get(Calendar.DAY_OF_WEEK));
	}

	/**
	 * Return difference in number of years
	 *
	 * @param d1
	 * @param d2
	 * @return
	 */
	public int diffYear(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			throw new java.lang.IllegalArgumentException("date value is null!");
		}
		int startYear = year(d1);
		int endYear = year(d2);

		return endYear - startYear;
	}

	/**
	 * Return difference in number of months
	 *
	 * @param d1
	 * @param d2
	 * @return
	 */
	public int diffMonth(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			throw new java.lang.IllegalArgumentException("date value is null!");
		}

		int startMonth = year(d1) * 12 + month(d1);
		int endMonth = year(d2) * 12 + month(d2);

		return endMonth - startMonth;
	}

	/**
	 * Return difference in number of quarters
	 *
	 * @param d1
	 * @param d2
	 * @return
	 */
	public int diffQuarter(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			throw new java.lang.IllegalArgumentException("date value is null!");
		}

		int startQuter = year(d1) * 4 + quarter(d1);
		int endQuter = year(d2) * 4 + quarter(d2);

		return endQuter - startQuter;
	}

	/**
	 * Return difference in number of weeks
	 *
	 * @param d1
	 * @param d2
	 * @return
	 */
	public long diffWeek(Date d1, Date d2) {
		Calendar calendar = getCalendarOfStartingTime();

		Date baseDay = calendar.getTime();

		int diffDay = 1 - Integer.parseInt(weekDay(baseDay));

		baseDay = addDay(baseDay, diffDay);

		return (diffSecond(baseDay, d2) + 3000 * 60 * 60 * 24 * 7) / (60 * 60 * 24 * 7)
				- (diffSecond(baseDay, d1) + 3000 * 60 * 60 * 24 * 7) / (60 * 60 * 24 * 7);
	}

	/**
	 * Return difference in number of days
	 *
	 * @param d1
	 * @param d2
	 * @return
	 */
	public long diffDay(Date d1, Date d2) {
		Calendar calendar = getCalendarOfStartingTime();

		return (diffSecond(calendar.getTime(), d2) + 3000 * 60 * 60 * 24 * 7) / (60 * 60 * 24)
				- (diffSecond(calendar.getTime(), d1) + 3000 * 60 * 60 * 24 * 7) / (60 * 60 * 24);
	}

	/**
	 *
	 * @return
	 */

	private Calendar getCalendarOfStartingTime() {
		Calendar calendar = Calendar.getInstance(locale);
		calendar.setTimeZone(timeZone);
		calendar.clear();
		return calendar;
	}

	/**
	 * Return difference in number of hours
	 *
	 * @param d1
	 * @param d2
	 * @return
	 */
	public long diffHour(Date d1, Date d2) {
		Calendar calendar = getCalendarOfStartingTime();
		return (diffSecond(calendar.getTime(), d2) + 3000 * 60 * 60 * 24 * 7) / (60 * 60)
				- (diffSecond(calendar.getTime(), d1) + 3000 * 60 * 60 * 24 * 7) / (60 * 60);
	}

	/**
	 * Return difference in number of minutes
	 *
	 * @param d1
	 * @param d2
	 * @return
	 */
	public long diffMinute(Date d1, Date d2) {
		Calendar calendar = getCalendarOfStartingTime();

		return (diffSecond(calendar.getTime(), d2) + 3000 * 60 * 60 * 24 * 7) / 60
				- (diffSecond(calendar.getTime(), d1) + 3000 * 60 * 60 * 24 * 7) / 60;
	}

	/**
	 * Return difference in number of seconds
	 *
	 * @param d1
	 * @param d2
	 * @return
	 */
	public long diffSecond(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			throw new java.lang.IllegalArgumentException("date value is null!");
		}
		long diff = d2.getTime() - d1.getTime();

		if (timeZone.inDaylightTime(d1)) {
			diff -= timeZone.getDSTSavings();
		}
		if (timeZone.inDaylightTime(d2)) {
			diff += timeZone.getDSTSavings();
		}

		return Long.valueOf(diff / 1000);
	}

	/**
	 * Add num days
	 *
	 * @param date
	 * @param num
	 * @return
	 */
	public Date addDay(Date date, int num) {
		Calendar startCal = getCalendar(date);

		startCal.add(Calendar.DATE, num);

		return startCal.getTime();
	}

	/**
	 *
	 * @param d
	 * @return
	 */
	private Calendar getCalendar(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException("date value is null!");
		}
		Calendar c = Calendar.getInstance(locale);
		if (d instanceof java.sql.Date) {
			c.setTimeZone(TimeZone.getDefault());
		} else {
			c.setTimeZone(timeZone);
		}
		c.setTime(d);
		return c;
	}
}
