/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.core.script.function.bre;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.function.i18n.Messages;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionContext;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionExecutor;
import org.mozilla.javascript.UniqueTag;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 *
 */
public class BirtDateTime implements IScriptFunctionExecutor, Serializable {
	private static final long serialVersionUID = 1L;

	// TODO Change these values according to the locale.
	static private ThreadLocal<List<SimpleDateFormat>> threadSDFArray = new ThreadLocal<>();
	private static ThreadLocal<ULocale> threadLocale = new ThreadLocal<>();
	private static ThreadLocal<TimeZone> threadTimeZone = new ThreadLocal<>();

	private IScriptFunctionExecutor executor;

	// Constant is defined in: EngineConstants.PROPERTY_FISCAL_YEAR_START_DATE
	public static final String PROPERTY_FISCAL_YEAR_START_DATE = "FISCAL_YEAR_START_DATE"; //$NON-NLS-1$

	private static final DateFormat FISCAL_YEAR_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd"); //$NON-NLS-1$

	static protected Logger logger = Logger.getLogger(BirtDateTime.class.getName());

	/**
	 *
	 * @return
	 */
	private static SimpleDateFormat getAbbrMonthFormat() {
		return threadSDFArray.get().get(0);
	}

	/**
	 *
	 * @return
	 */
	private static SimpleDateFormat getMonthFormat() {
		return threadSDFArray.get().get(1);
	}

	/**
	 *
	 * @return
	 */
	private static SimpleDateFormat getAbbrWeekFormat() {
		return threadSDFArray.get().get(2);
	}

	/**
	 *
	 * @return
	 */
	private static SimpleDateFormat getWeekFormat() {
		return threadSDFArray.get().get(3);
	}

	/**
	 * @throws BirtException
	 *
	 *
	 */
	BirtDateTime(String functionName) throws BirtException {
		if ("year".equals(functionName)) {
			this.executor = new Function_Year();
		} else if ("quarter".equals(functionName)) {
			this.executor = new Function_Quarter();
		} else if ("month".equals(functionName)) {
			this.executor = new Function_Month();
		} else if ("week".equals(functionName)) {
			this.executor = new Function_Week();
		} else if ("weekOfMonth".equals(functionName)) {
			this.executor = new Function_WeekOfMonth();
		} else if ("day".equals(functionName)) {
			this.executor = new Function_Day();
		} else if ("weekDay".equals(functionName)) {
			this.executor = new Function_WeekDay();
		} else if ("dayOfWeek".equals(functionName)) {
			this.executor = new Function_DayOfWeek();
		} else if ("dayOfYear".equals(functionName)) {
			this.executor = new Function_DayOfYear();
		} else if ("today".equals(functionName)) {
			this.executor = new Function_Today();
		} else if ("now".equals(functionName)) {
			this.executor = new Function_Now();
		} else if ("diffYear".equals(functionName)) {
			this.executor = new Function_DiffYear();
		} else if ("diffMonth".equals(functionName)) {
			this.executor = new Function_DiffMonth();
		} else if ("diffQuarter".equals(functionName)) {
			this.executor = new Function_DiffQuarter();
		} else if ("diffWeek".equals(functionName)) {
			this.executor = new Function_DiffWeek();
		} else if ("diffDay".equals(functionName)) {
			this.executor = new Function_DiffDay();
		} else if ("diffHour".equals(functionName)) {
			this.executor = new Function_DiffHour();
		} else if ("diffMinute".equals(functionName)) {
			this.executor = new Function_DiffMinute();
		} else if ("diffSecond".equals(functionName)) {
			this.executor = new Function_DiffSecond();
		} else if ("addYear".equals(functionName)) {
			this.executor = new Function_AddYear();
		} else if ("addMonth".equals(functionName)) {
			this.executor = new Function_AddMonth();
		} else if ("addQuarter".equals(functionName)) {
			this.executor = new Function_AddQuarter();
		} else if ("addWeek".equals(functionName)) {
			this.executor = new Function_AddWeek();
		} else if ("addDay".equals(functionName)) {
			this.executor = new Function_AddDay();
		} else if ("addHour".equals(functionName)) {
			this.executor = new Function_AddHour();
		} else if ("addMinute".equals(functionName)) {
			this.executor = new Function_AddMinute();
		} else if ("addSecond".equals(functionName)) {
			this.executor = new Function_AddSecond();
		} else if ("firstDayOfYear".equals(functionName)) {
			this.executor = new Function_FirstDayOfYear();
		} else if ("firstDayOfQuarter".equals(functionName)) {
			this.executor = new Function_FirstDayOfQuarter();
		} else if ("firstDayOfMonth".equals(functionName)) {
			this.executor = new Function_FirstDayOfMonth();
		} else if ("firstDayOfWeek".equals(functionName)) {
			this.executor = new Function_FirstDayOfWeek();
		} else if ("date".equals(functionName)) {
			this.executor = new Function_Date();
		} else if ("fiscalYear".equals(functionName)) {
			this.executor = new Function_FiscalYear();
		} else if ("fiscalQuarter".equals(functionName)) {
			this.executor = new Function_FiscalQuarter();
		} else if ("fiscalMonth".equals(functionName)) {
			this.executor = new Function_FiscalMonth();
		} else if ("fiscalWeek".equals(functionName)) {
			this.executor = new Function_FiscalWeek();
		} else if ("fiscalDay".equals(functionName)) {
			this.executor = new Function_FiscalDay();
		} else if ("firstDayOfFiscalYear".equals(functionName)) {
			this.executor = new Function_FirstDayOfFiscalYear();
		} else if ("firstDayOfFiscalQuarter".equals(functionName)) {
			this.executor = new Function_FirstDayOfFiscalQuarter();
		} else if ("firstDayOfFiscalMonth".equals(functionName)) {
			this.executor = new Function_FirstDayOfFiscalMonth();
		} else if ("firstDayOfFiscalWeek".equals(functionName)) {
			this.executor = new Function_FirstDayOfFiscalWeek();
		} else if ("hour".equals(functionName)) {
			this.executor = new Function_Hour();
		} else if ("minute".equals(functionName)) {
			this.executor = new Function_Minute();
		} else if ("second".equals(functionName)) {
			this.executor = new Function_Second();
		} else if ("weekOfYear".equals(functionName)) {
			this.executor = new Function_WeekOfYear();
		} else if ("weekOfQuarter".equals(functionName)) {
			this.executor = new Function_WeekOfQuarter();
		} else if ("dayOfQuarter".equals(functionName)) {
			this.executor = new Function_DayOfQuarter();
		} else if ("dayOfMonth".equals(functionName)) {
			this.executor = new Function_DayOfMonth();
		} else {
			throw new BirtException("org.eclipse.birt.core.script.function.bre", null,
					Messages.getString("invalid.function.name") + "BirtDateTime." + functionName);
		}
	}

	private static class Function_Year extends Function_temp {
		Function_Year() {
			minParamCount = 1;
			maxParamCount = 1;
		}

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return Integer.valueOf(year(DataTypeUtil.toDate(args[0])));
		}
	}

	/**
	 * 4-digit year number of date/time value d
	 *
	 * @param d
	 * @return
	 */
	private static int year(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException("date value is null!");
		}

		return getCalendar(d).get(Calendar.YEAR);
	}

	private static class Function_Quarter extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_Quarter() {
			minParamCount = 1;
			maxParamCount = 1;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			if (args[0] instanceof Date) {
				return Integer.valueOf(quarter((Date) args[0]));
			} else {
				return Integer.valueOf(quarter(DataTypeUtil.toDate(args[0])));
			}
		}
	}

	/**
	 * Quarter number (1 to 4) of date/time value d
	 *
	 * @param d
	 * @return
	 */
	private static int quarter(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
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

	private static class Function_WeekOfQuarter extends Function_temp {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_WeekOfQuarter() {
			minParamCount = 1;
			maxParamCount = 1;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			if (args[0] instanceof Date) {
				return Integer.valueOf(weekOfQuarter((Date) args[0]));
			} else {
				return Integer.valueOf(weekOfQuarter(DataTypeUtil.toDate(args[0])));
			}
		}
	}

	private static int weekOfQuarter(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
		}

		int dayOfWeek = getCalendar(d).get(Calendar.DAY_OF_WEEK);
		int dayOfQtr = getDayOfQuarter(d);
		int quarter = quarter(d);
		// Finding last nearest Sunday
		int dayCountTillLastSunday = dayOfQtr - dayOfWeek + 1;
		int weekOfQuarter = 1;
		while (dayCountTillLastSunday > 1) {
			dayCountTillLastSunday -= 7;
			weekOfQuarter++;
		}
		// Using a formula to represent each Week of a Year uniquely.
		// To handle aggregations done across years which includes a Leap Year
		weekOfQuarter = (quarter * 100) + weekOfQuarter;
		return weekOfQuarter;
	}

	private static class Function_DayOfQuarter extends Function_temp {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_DayOfQuarter() {
			minParamCount = 1;
			maxParamCount = 1;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			if (args[0] instanceof Date) {
				return Integer.valueOf(dayOfQuarter((Date) args[0]));
			} else {
				return Integer.valueOf(dayOfQuarter(DataTypeUtil.toDate(args[0])));
			}
		}
	}

	private static int dayOfQuarter(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
		}

		int dayOfYear = getCalendar(d).get(Calendar.DAY_OF_YEAR);
		int quarter = quarter(d);
		int year = getCalendar(d).get(Calendar.YEAR);
		int[] dayCountPerQtr_NLY = { 90, 91, 92, 92 };
		int[] dayCountPerQtr_LY = { 91, 91, 92, 92 };
		boolean isLeapYear = false;

		int[] dayCountPerQtr = null;

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
		int dayOfQuarter;
		for (int i = 0; i < quarter - 1; i++) {
			totalDaysTillQtr += dayCountPerQtr[i];
		}
		dayOfQuarter = (dayOfYear - totalDaysTillQtr);
		// Using a formula to represent each day of a year uniquely.
		// To handle aggregations done across years which includes a Leap Year
		dayOfQuarter = (quarter * 100) + dayOfQuarter;
		return dayOfQuarter;
	}

	private static int getDayOfQuarter(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
		}

		int dayOfYear = getCalendar(d).get(Calendar.DAY_OF_YEAR);
		int quarter = quarter(d);
		int year = getCalendar(d).get(Calendar.YEAR);
		int[] dayCountPerQtr_NLY = { 90, 91, 92, 92 };
		int[] dayCountPerQtr_LY = { 91, 91, 92, 92 };
		boolean isLeapYear = false;

		int[] dayCountPerQtr = null;

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
		int dayOfQuarter;
		for (int i = 0; i < quarter - 1; i++) {
			totalDaysTillQtr += dayCountPerQtr[i];
		}
		dayOfQuarter = (dayOfYear - totalDaysTillQtr);
		return dayOfQuarter;
	}

	private static class Function_Month extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_Month() {
			minParamCount = 1;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			Date date = null;
			if (args[0] instanceof Date) {
				date = (Date) args[0];
			} else {
				date = DataTypeUtil.toDate(args[0]);
			}
			if (args.length == 1) {
				return Integer.valueOf(month(date));
			} else {
				return month(date, ((Number) args[1]).intValue());
			}
		}
	}

	/**
	 * Month of date/time value d. Return month number (1-12)
	 *
	 * @param d
	 * @return
	 */
	private static int month(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
		}

		return getCalendar(d).get(Calendar.MONTH) + 1;
	}

	/**
	 * Month of date/time value d. Option is an integer value: 1 (default): return
	 * month number (1-12) 2: return full month name as per user locale (e.g.,
	 * January to December for English locale). 3: return short month name as per
	 * user locale (e.g., Jan to Dec for English locale)
	 *
	 * @param d
	 * @param option
	 * @return
	 */
	private static String month(Date d, int option) {
		// TODO: finish me.
		if (d == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
		}

		Calendar c = getCalendar(d);
		int month = c.get(Calendar.MONTH);
		if (option == 1) {
			return String.valueOf(month + 1);
		} else if (option == 2) {
			return getMonthFormat().format(d);
		} else if (option == 3) {
			return getAbbrMonthFormat().format(d);
		}
		return null;
	}

	private static class Function_Week extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_Week() {
			minParamCount = 1;
			maxParamCount = 1;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return Integer.valueOf(week(DataTypeUtil.toDate(args[0])));
		}
	}

	/**
	 * Week number of the year (1 to 52) of date/time value d.
	 *
	 * @param d
	 * @return
	 */
	private static int week(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
		}

		return getCalendar(d).get(Calendar.WEEK_OF_YEAR);
	}

	private static class Function_WeekOfMonth extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_WeekOfMonth() {
			minParamCount = 1;
			maxParamCount = 1;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return Integer.valueOf(weekOfMonth(DataTypeUtil.toDate(args[0])));
		}
	}

	/**
	 * Week number of the year (1 to 52) of date/time value d.
	 *
	 * @param d
	 * @return
	 */
	private static int weekOfMonth(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
		}

		return getCalendar(d).get(Calendar.WEEK_OF_MONTH);
	}

	private static class Function_WeekOfYear extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_WeekOfYear() {
			minParamCount = 1;
			maxParamCount = 1;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return Integer.valueOf(weekOfYear(DataTypeUtil.toDate(args[0])));
		}
	}

	/**
	 * Week number of the year (1 to 52) of date/time value d.
	 *
	 * @param d
	 * @return
	 */
	private static int weekOfYear(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
		}

		return getCalendar(d).get(Calendar.WEEK_OF_YEAR);
	}

	private static class Function_Day extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_Day() {
			minParamCount = 1;
			maxParamCount = 1;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return Integer.valueOf(day(DataTypeUtil.toDate(args[0])));
		}
	}

	/**
	 * Day number of the month(1 to 31).
	 *
	 * @param d
	 * @return
	 */
	private static int day(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
		}

		return getCalendar(d).get(Calendar.DAY_OF_MONTH);
	}

	private static class Function_WeekDay extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_WeekDay() {
			minParamCount = 1;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			if (args.length == 1) {
				return weekDay(DataTypeUtil.toDate(args[0]));
			} else {
				return weekDay(DataTypeUtil.toDate(args[0]), ((Number) args[1]).intValue());
			}
		}
	}

	/**
	 * Day the week. Return a number 1 (Sunday) to 7 (Saturday).
	 *
	 * @param d
	 * @return
	 */
	private static String weekDay(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
		}

		return String.valueOf(getCalendar(d).get(Calendar.DAY_OF_WEEK));
	}

	/**
	 * Day the week. Option is an integer value: 1: return a number 1 (Sunday) to 7
	 * (Saturday) 2: return a number 1 (Monday) to 7 (Sunday) 3: return a number 0
	 * (Monday) to 6 (Sunday) 4: return the weekday name as per user locale (e.g.,
	 * Sunday Saturday for English) 5: return the abbreviated weekday name as per
	 * user locale (e.g., Sun Sat for English)
	 *
	 * @param d
	 * @param option
	 * @return
	 */
	private static String weekDay(Date d, int option) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
		}
		switch (option) {
		case 1:
			return String.valueOf(getWeekDay(d, Calendar.SUNDAY));
		case 2:
			return String.valueOf(getWeekDay(d, Calendar.MONDAY));
		case 3:
			return String.valueOf(getWeekDay(d, Calendar.MONDAY) - 1);
		case 4:
			return getWeekFormat().format(d);
		case 5:
			return getAbbrWeekFormat().format(d);
		}
		return null;

	}

	private static class Function_DayOfYear extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_DayOfYear() {
			minParamCount = 1;
			maxParamCount = 1;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return dayOfYear(DataTypeUtil.toDate(args[0]));
		}
	}

	/**
	 * Day of the year. Return a number 1 to 365
	 *
	 * @param d
	 * @return
	 */
	private static int dayOfYear(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
		}

		return getCalendar(d).get(Calendar.DAY_OF_YEAR);
	}

	private static class Function_DayOfMonth extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_DayOfMonth() {
			minParamCount = 1;
			maxParamCount = 1;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return dayOfMonth(DataTypeUtil.toDate(args[0]));
		}
	}

	/**
	 * Day of the Month. Return a number 1 to 31
	 *
	 * @param d
	 * @return
	 */
	private static int dayOfMonth(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
		}

		return getCalendar(d).get(Calendar.DAY_OF_MONTH);
	}

	private static class Function_Hour extends Function_temp {
		Function_Hour() {
			minParamCount = 1;
			maxParamCount = 1;
		}

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return Integer.valueOf(hour(DataTypeUtil.toDate(args[0])));
		}
	}

	private static int hour(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
		}

		return getCalendar(d).get(Calendar.HOUR);
	}

	private static class Function_Minute extends Function_temp {
		Function_Minute() {
			minParamCount = 1;
			maxParamCount = 1;
		}

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return Integer.valueOf(minute(DataTypeUtil.toDate(args[0])));
		}
	}

	private static int minute(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
		}

		return getCalendar(d).get(Calendar.MINUTE);
	}

	private static class Function_Second extends Function_temp {
		Function_Second() {
			minParamCount = 1;
			maxParamCount = 1;
		}

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return Integer.valueOf(second(DataTypeUtil.toDate(args[0])));
		}
	}

	private static int second(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
		}

		return getCalendar(d).get(Calendar.SECOND);
	}

	private static class Function_DayOfWeek extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_DayOfWeek() {
			minParamCount = 1;
			maxParamCount = 1;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return dayOfWeek(DataTypeUtil.toDate(args[0]));
		}
	}

	/**
	 * Day the week. Return a number 1 to 7.
	 *
	 * @param d
	 * @return
	 */
	private static int dayOfWeek(Date d) {
		if (d == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
		}

		return getCalendar(d).get(Calendar.DAY_OF_WEEK);
	}

	private static class Function_Today extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_Today() {
			minParamCount = 0;
			maxParamCount = 0;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return today();
		}
	}

	/**
	 * Returns a timestamp value which is midnight of the current date.
	 *
	 * @return
	 */
	private static Date today() {
		Calendar calendar = Calendar.getInstance(threadTimeZone.get());
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.clear(Calendar.MINUTE);
		calendar.clear(Calendar.SECOND);
		calendar.clear(Calendar.MILLISECOND);

		return calendar.getTime();
	}

	private static class Function_Now extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_Now() {
			minParamCount = 0;
			maxParamCount = 0;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			return now();
		}
	}

	/**
	 * Returns the current timestamp
	 *
	 * @return
	 */
	private static Timestamp now() {
		Date now = new Date();
		return new Timestamp(now.getTime());
	}

	private static class Function_DiffYear extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_DiffYear() {
			minParamCount = 2;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return Integer.valueOf(diffYear(DataTypeUtil.toDate(args[0]), DataTypeUtil.toDate(args[1])));
		}
	}

	/**
	 * Return difference in number of years
	 *
	 * @param d1
	 * @param d2
	 * @return
	 */
	private static int diffYear(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
		}
		int startYear = year(d1);
		int endYear = year(d2);

		return endYear - startYear;
	}

	private static class Function_DiffMonth extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_DiffMonth() {
			minParamCount = 2;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return Integer.valueOf(diffMonth(DataTypeUtil.toDate(args[0]), DataTypeUtil.toDate(args[1])));
		}
	}

	/**
	 * Return difference in number of months
	 *
	 * @param d1
	 * @param d2
	 * @return
	 */
	private static int diffMonth(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
		}

		int startMonth = year(d1) * 12 + month(d1);
		int endMonth = year(d2) * 12 + month(d2);

		return endMonth - startMonth;
	}

	private static class Function_DiffQuarter extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_DiffQuarter() {
			minParamCount = 2;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return Integer.valueOf(diffQuarter(DataTypeUtil.toDate(args[0]), DataTypeUtil.toDate(args[1])));
		}
	}

	/**
	 * Return difference in number of quarters
	 *
	 * @param d1
	 * @param d2
	 * @return
	 */
	private static int diffQuarter(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
		}

		int startQuter = year(d1) * 4 + quarter(d1);
		int endQuter = year(d2) * 4 + quarter(d2);

		return endQuter - startQuter;
	}

	private static class Function_DiffWeek extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_DiffWeek() {
			minParamCount = 2;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return Long.valueOf(diffWeek(DataTypeUtil.toDate(args[0]), DataTypeUtil.toDate(args[1])));
		}
	}

	/**
	 * Return difference in number of weeks
	 *
	 * @param d1
	 * @param d2
	 * @return
	 */
	private static long diffWeek(Date d1, Date d2) {
		Date sd1 = getWeekStartDay(d1);
		Date sd2 = getWeekStartDay(d2);
		return diffDay(sd1, sd2) / 7;
	}

	/**
	 *
	 * @param date
	 * @return
	 */
	static public Date getWeekStartDay(Date date) {
		int diffDay = 1 - Integer.parseInt(weekDay(date));

		return addDay(date, diffDay);
	}

	private static class Function_DiffDay extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_DiffDay() {
			minParamCount = 2;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return Long.valueOf(diffDay(DataTypeUtil.toDate(args[0]), DataTypeUtil.toDate(args[1])));
		}
	}

	/**
	 * Return difference in number of days
	 *
	 * @param d1
	 * @param d2
	 * @return
	 */
	private static long diffDay(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance(threadTimeZone.get());
		c1.setTime(d1);
		Calendar c2 = Calendar.getInstance(threadTimeZone.get());
		c2.setTime(d2);
		if (c1.after(c2)) {
			return -diffDay(c2, c1);
		} else {
			return diffDay(c1, c2);
		}
	}

	/**
	 *
	 * @param d1
	 * @param d2
	 * @return
	 */
	static private long diffDay(Calendar d1, Calendar d2) {
		int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
		int y2 = d2.get(Calendar.YEAR);
		if (d1.get(Calendar.YEAR) != y2) {
			do {
				days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
				d1.add(Calendar.YEAR, 1);
			} while (d1.get(Calendar.YEAR) != y2);
		}
		return days;
	}

	private static class Function_DiffHour extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_DiffHour() {
			minParamCount = 2;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return Long.valueOf(diffHour(DataTypeUtil.toDate(args[0]), DataTypeUtil.toDate(args[1])));
		}
	}

	/**
	 * Return difference in number of hours
	 *
	 * @param d1
	 * @param d2
	 * @return
	 */
	private static long diffHour(Date d1, Date d2) {
		return diffSecond(d1, d2) / (60 * 60);
	}

	private static class Function_DiffMinute extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_DiffMinute() {
			minParamCount = 2;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return Long.valueOf(diffMinute(DataTypeUtil.toDate(args[0]), DataTypeUtil.toDate(args[1])));
		}
	}

	/**
	 * Return difference in number of minutes
	 *
	 * @param d1
	 * @param d2
	 * @return
	 */
	private static long diffMinute(Date d1, Date d2) {
		return diffSecond(d1, d2) / 60;
	}

	/**
	 *
	 * @author xyi
	 *
	 */
	private static class Function_DiffSecond extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_DiffSecond() {
			minParamCount = 2;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return Long.valueOf(diffSecond(DataTypeUtil.toDate(args[0]), DataTypeUtil.toDate(args[1])));
		}
	}

	/**
	 * Return difference in number of seconds
	 *
	 * @param d1
	 * @param d2
	 * @return
	 */
	private static long diffSecond(Date d1, Date d2) {
		if (d1 == null || d2 == null) {
			throw new java.lang.IllegalArgumentException(
					Messages.getString("error.BirtDateTime.cannotBeNull.DateValue"));
		}
		long diff = d2.getTime() - d1.getTime();

		return diff / 1000l;
	}

	private static class Function_AddYear extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_AddYear() {
			minParamCount = 2;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return addYear(DataTypeUtil.toDate(args[0]), ((Number) args[1]).intValue());
		}
	}

	/**
	 * Add num years
	 *
	 * @param date
	 * @param num
	 * @return
	 */
	private static Date addYear(Date date, int num) {
		Calendar startCal = getCalendar(date);

		startCal.add(Calendar.YEAR, num);

		return startCal.getTime();
	}

	private static class Function_AddMonth extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_AddMonth() {
			minParamCount = 2;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return addMonth(DataTypeUtil.toDate(args[0]), ((Number) args[1]).intValue());
		}
	}

	/**
	 * Add num months
	 *
	 * @param date
	 * @param num
	 * @return
	 */
	private static Date addMonth(Date date, int num) {
		Calendar startCal = getCalendar(date);

		startCal.add(Calendar.MONTH, num);

		return startCal.getTime();
	}

	private static class Function_AddQuarter extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_AddQuarter() {
			minParamCount = 2;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return addQuarter(DataTypeUtil.toDate(args[0]), ((Number) args[1]).intValue());
		}
	}

	/**
	 * Add num quarters
	 *
	 * @param date
	 * @param num
	 * @return
	 */
	private static Date addQuarter(Date date, int num) {
		return addMonth(date, num * 3);
	}

	private static class Function_AddWeek extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_AddWeek() {
			minParamCount = 2;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return addWeek(DataTypeUtil.toDate(args[0]), ((Number) args[1]).intValue());
		}
	}

	/**
	 * Add num weeks
	 *
	 * @param date
	 * @param num
	 * @return
	 */
	private static Date addWeek(Date date, int num) {
		return addDay(date, num * 7);
	}

	private static class Function_AddDay extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_AddDay() {
			minParamCount = 2;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return addDay(DataTypeUtil.toDate(args[0]), ((Number) args[1]).intValue());
		}
	}

	/**
	 * Add num days
	 *
	 * @param date
	 * @param num
	 * @return
	 */
	private static Date addDay(Date date, int num) {
		Calendar startCal = getCalendar(date);

		startCal.add(Calendar.DATE, num);

		return startCal.getTime();
	}

	private static class Function_AddHour extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_AddHour() {
			minParamCount = 2;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return addHour(DataTypeUtil.toDate(args[0]), ((Number) args[1]).intValue());
		}
	}

	/**
	 * Add num hours
	 *
	 * @param date
	 * @param num
	 * @return
	 */
	private static Date addHour(Date date, int num) {
		Calendar startCal = getCalendar(date);

		startCal.add(Calendar.HOUR_OF_DAY, num);

		return startCal.getTime();
	}

	private static class Function_AddMinute extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_AddMinute() {
			minParamCount = 2;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return addMinute(DataTypeUtil.toDate(args[0]), ((Number) args[1]).intValue());
		}
	}

	/**
	 * Add num minutes
	 *
	 * @param date
	 * @param num
	 * @return
	 */
	private static Date addMinute(Date date, int num) {
		Calendar startCal = getCalendar(date);

		startCal.add(Calendar.MINUTE, num);

		return startCal.getTime();
	}

	private static class Function_AddSecond extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_AddSecond() {
			minParamCount = 2;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			return addSecond(DataTypeUtil.toDate(args[0]), ((Number) args[1]).intValue());
		}
	}

	private static class Function_FirstDayOfYear extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_FirstDayOfYear() {
			minParamCount = 1;
			maxParamCount = 1;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			Calendar cal = getCalendar(DataTypeUtil.toDate(args[0]));
			cal.set(Calendar.DAY_OF_YEAR, 1);
			return cal.getTime();
		}
	}

	private static class Function_FirstDayOfQuarter extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_FirstDayOfQuarter() {
			minParamCount = 1;
			maxParamCount = 1;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			Date date = DataTypeUtil.toDate(args[0]);
			Calendar cal = getCalendar(date);
			int quarter = quarter(date);

			cal.set(Calendar.MONTH, (quarter - 1) * 3);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			return cal.getTime();
		}
	}

	private static class Function_FirstDayOfMonth extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_FirstDayOfMonth() {
			minParamCount = 1;
			maxParamCount = 1;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			Calendar cal = getCalendar(DataTypeUtil.toDate(args[0]));
			cal.set(Calendar.DAY_OF_MONTH, 1);
			return cal.getTime();
		}
	}

	private static class Function_FirstDayOfWeek extends Function_temp {
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_FirstDayOfWeek() {
			minParamCount = 1;
			maxParamCount = 1;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			Calendar cal = getCalendar(DataTypeUtil.toDate(args[0]));
			cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
			return cal.getTime();
		}
	}

	private static class Function_Date extends Function_temp {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		Function_Date() {
			minParamCount = 3;
			maxParamCount = 6;
		}

		@Override
		protected Object getValue(Object[] args) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			for (int i = 0; i < args.length; i++) {
				if (args[i] instanceof String) {
					args[i] = Double.parseDouble((String) args[i]);
				}
			}
			if (args.length == 3) {
				return getDate(((Number) args[0]).intValue(), ((Number) args[1]).intValue(),
						((Number) args[2]).intValue(), 0, 0, 0);
			} else if (args.length == 4) {
				return getDate(((Number) args[0]).intValue(), ((Number) args[1]).intValue(),
						((Number) args[2]).intValue(), ((Number) args[3]).intValue(), 0, 0);
			} else if (args.length == 5) {
				return getDate(((Number) args[0]).intValue(), ((Number) args[1]).intValue(),
						((Number) args[2]).intValue(), ((Number) args[3]).intValue(), ((Number) args[4]).intValue(), 0);
			} else {
				return getDate(((Number) args[0]).intValue(), ((Number) args[1]).intValue(),
						((Number) args[2]).intValue(), ((Number) args[3]).intValue(), ((Number) args[4]).intValue(),
						((Number) args[5]).intValue());
			}
		}
	}

	private static class Function_FiscalYear extends Function_temp {

		private static final long serialVersionUID = 1L;

		Function_FiscalYear() {
			minParamCount = 1;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args, IScriptFunctionContext context) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			Calendar current = getCalendar(DataTypeUtil.toDate(args[0]));
			Calendar start = getFiscalYearStartDate(context, args);
			if (start.get(Calendar.DAY_OF_YEAR) > 1) {
				adjustFiscalYear(current, start);
				// Fiscal year should return next year of first day, except
				// Jan. 1
				return current.get(Calendar.YEAR) + 1;
			}
			return current.get(Calendar.YEAR);
		}
	}

	private static class Function_FiscalQuarter extends Function_temp {

		private static final long serialVersionUID = 1L;

		Function_FiscalQuarter() {
			minParamCount = 1;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args, IScriptFunctionContext context) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			Calendar current = getCalendar(DataTypeUtil.toDate(args[0]));
			Calendar start = getFiscalYearStartDate(context, args);
			// Quarter starts with 1
			adjustFiscalYear(current, start);
			return current.get(Calendar.MONTH) / 3 + 1;
		}
	}

	private static class Function_FiscalMonth extends Function_temp {

		private static final long serialVersionUID = 1L;

		Function_FiscalMonth() {
			minParamCount = 1;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args, IScriptFunctionContext context) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			Calendar current = getCalendar(DataTypeUtil.toDate(args[0]));
			Calendar start = getFiscalYearStartDate(context, args);
			// Month starts with 1
			adjustFiscalYear(current, start);
			return current.get(Calendar.MONTH) + 1;
		}
	}

	private static class Function_FiscalWeek extends Function_temp {

		private static final long serialVersionUID = 1L;

		Function_FiscalWeek() {
			minParamCount = 1;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args, IScriptFunctionContext context) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			Calendar current = getCalendar(DataTypeUtil.toDate(args[0]));
			int currentWeek = current.get(Calendar.WEEK_OF_YEAR);
			Calendar start = getFiscalYearStartDate(context, args);
			start.set(Calendar.YEAR, current.get(Calendar.YEAR));
			int startWeek = start.get(Calendar.WEEK_OF_YEAR);
			if (currentWeek >= startWeek) {
				return currentWeek - startWeek + 1;
			}

			// Go to last year to add weeks together
			start.set(Calendar.YEAR, current.get(Calendar.YEAR) - 1);

			Calendar lastYearLastWeek = Calendar.getInstance();
			lastYearLastWeek.set(start.get(Calendar.YEAR), 11, 31);
			// Last week may return 1 as week of year
			while (lastYearLastWeek.get(Calendar.WEEK_OF_YEAR) == 1) {
				lastYearLastWeek.add(Calendar.DAY_OF_MONTH, -1);
			}
			return lastYearLastWeek.get(Calendar.WEEK_OF_YEAR) - start.get(Calendar.WEEK_OF_YEAR) + 1 + currentWeek;
		}
	}

	private static class Function_FiscalDay extends Function_temp {

		private static final long serialVersionUID = 1L;

		Function_FiscalDay() {
			minParamCount = 1;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args, IScriptFunctionContext context) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			Calendar current = getCalendar(DataTypeUtil.toDate(args[0]));
			Calendar start = getFiscalYearStartDate(context, args);
			adjustFiscalYear(current, start);
			return current.get(Calendar.DAY_OF_YEAR);
		}
	}

	private static class Function_FirstDayOfFiscalMonth extends Function_temp {

		private static final long serialVersionUID = 1L;

		Function_FirstDayOfFiscalMonth() {
			minParamCount = 1;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args, IScriptFunctionContext context) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			Calendar current;
			if (args[0] instanceof Number) {
				current = getFiscalYearStartDate(context, args);
				// Month starts with 1
				current.add(Calendar.MONTH, ((Number) args[0]).intValue() - 1);
			} else {
				current = getCalendar(DataTypeUtil.toDate(args[0]));
				Calendar start = getFiscalYearStartDate(context, args);
				adjustFiscalMonth(current, start);
				// Do not exceed the max days of current month
				current.set(Calendar.DATE, Math.min(start.get(Calendar.DATE), current.getActualMaximum(Calendar.DATE)));
			}
			return current.getTime();
		}
	}

	private static class Function_FirstDayOfFiscalQuarter extends Function_temp {

		private static final long serialVersionUID = 1L;

		Function_FirstDayOfFiscalQuarter() {
			minParamCount = 1;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args, IScriptFunctionContext context) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			Calendar current;
			if (args[0] instanceof Number) {
				current = getFiscalYearStartDate(context, args);
				// Quarter starts with 1
				current.add(Calendar.MONTH, (((Number) args[0]).intValue() - 1) * 3);
			} else {
				current = getCalendar(DataTypeUtil.toDate(args[0]));
				Calendar start = getFiscalYearStartDate(context, args);
				adjustFiscalMonth(current, start);
				int monthRemaindary = (current.get(Calendar.MONTH) - start.get(Calendar.MONTH) + 12) % 3;
				current.add(Calendar.MONTH, -monthRemaindary);
				// Do not exceed the max days of current month
				current.set(Calendar.DATE, Math.min(start.get(Calendar.DATE), current.getActualMaximum(Calendar.DATE)));
			}
			return current.getTime();
		}
	}

	private static class Function_FirstDayOfFiscalWeek extends Function_temp {

		private static final long serialVersionUID = 1L;

		Function_FirstDayOfFiscalWeek() {
			minParamCount = 1;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args, IScriptFunctionContext context) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			Calendar current;
			if (args[0] instanceof Number) {
				current = getFiscalYearStartDate(context, args);
				// Week starts with 1
				current.add(Calendar.WEEK_OF_YEAR, ((Number) args[0]).intValue() - 1);
			} else {
				current = getCalendar(DataTypeUtil.toDate(args[0]));
			}
			current.set(Calendar.DAY_OF_WEEK, current.getFirstDayOfWeek());
			return current.getTime();
		}
	}

	private static class Function_FirstDayOfFiscalYear extends Function_temp {

		private static final long serialVersionUID = 1L;

		Function_FirstDayOfFiscalYear() {
			minParamCount = 1;
			maxParamCount = 2;
		}

		@Override
		protected Object getValue(Object[] args, IScriptFunctionContext context) throws BirtException {
			if (existNullValue(args)) {
				return null;
			}
			Calendar current = null;
			if (args[0] instanceof Number) {
				current = getFiscalYearStartDate(context, args);
				current.set(Calendar.YEAR, ((Number) args[0]).intValue());
				if (current.get(Calendar.DAY_OF_YEAR) > 1) {
					current.add(Calendar.YEAR, -1);
				}
			} else {
				current = getCalendar(DataTypeUtil.toDate(args[0]));
				Calendar start = getFiscalYearStartDate(context, args);
				adjustFiscalYear(current, start);
				current.set(Calendar.MONTH, start.get(Calendar.MONTH));
				// Do not exceed the max days of current month
				current.set(Calendar.DATE, Math.min(start.get(Calendar.DATE), current.getActualMaximum(Calendar.DATE)));
			}
			return current.getTime();
		}
	}

	private static void adjustFiscalYear(Calendar current, Object fiscalStart) throws BirtException {
		Calendar start;
		if (fiscalStart instanceof Calendar) {
			start = (Calendar) fiscalStart;
		} else {
			start = getCalendar(DataTypeUtil.toDate(fiscalStart));
		}
		start.set(Calendar.YEAR, current.get(Calendar.YEAR));
		current.add(Calendar.DAY_OF_YEAR, 1 - start.get(Calendar.DAY_OF_YEAR));
	}

	private static void adjustFiscalMonth(Calendar current, Object fiscalStart) throws BirtException {
		Calendar start;
		if (fiscalStart instanceof Calendar) {
			start = (Calendar) fiscalStart;
		} else {
			start = getCalendar(DataTypeUtil.toDate(fiscalStart));
		}
		current.add(Calendar.DAY_OF_MONTH, 1 - start.get(Calendar.DAY_OF_MONTH));
	}

	private static Date getDate(int year, int month, int day, int hours, int minutes, int seconds) {
		Date newDate = new Date();
		Calendar calendar = getCalendar(newDate);
		calendar.set(year, month, day, hours, minutes, seconds);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * Add num seconds
	 *
	 * @param date
	 * @param num
	 * @return
	 */
	private static Date addSecond(Date date, int num) {
		Calendar startCal = getCalendar(date);

		startCal.add(Calendar.SECOND, num);

		return startCal.getTime();
	}

	/**
	 *
	 * @param d
	 * @param startDay
	 * @return
	 */
	private static int getWeekDay(Date d, int startDay) {
		int dayOfWeek = getCalendar(d).get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek >= startDay) {
			return dayOfWeek - startDay + 1;
		} else {
			return (dayOfWeek - startDay + 1 + 7) % 8;
		}

	}

	/**
	 *
	 * @param d
	 * @return
	 */
	private static Calendar getCalendar(Date d) {
		Calendar c = null;
		if (d instanceof java.sql.Date) {
			c = Calendar.getInstance(TimeZone.getDefault(), threadLocale.get());
		} else {
			c = Calendar.getInstance(threadTimeZone.get(), threadLocale.get());
		}
		if (d == null) {
			c.clear();
			c.set(1970, 0, 1);
		} else {
			c.setTime(d);
		}
		return c;
	}

	/**
	 *
	 * @param args
	 * @return
	 */
	private static boolean existNullValue(Object[] args) {
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				if (args[i] == null) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public Object execute(Object[] arguments, IScriptFunctionContext scriptContext) throws BirtException {
		if (scriptContext != null) {
			Object locale = scriptContext
					.findProperty(org.eclipse.birt.core.script.functionservice.IScriptFunctionContext.LOCALE);
			if (!(locale instanceof ULocale)) {
				locale = ULocale.getDefault();
			}
			if (threadLocale.get() != locale) {
				threadLocale.set((ULocale) locale);
				List<SimpleDateFormat> sdfList = new ArrayList<>();
				sdfList.add(new SimpleDateFormat("MMM", threadLocale.get()));
				sdfList.add(new SimpleDateFormat("MMMM", threadLocale.get()));
				sdfList.add(new SimpleDateFormat("EEE", threadLocale.get()));
				sdfList.add(new SimpleDateFormat("EEEE", threadLocale.get()));
				threadSDFArray.set(sdfList);
			}

			Object timeZone = scriptContext
					.findProperty(org.eclipse.birt.core.script.functionservice.IScriptFunctionContext.TIMEZONE);
			if (!(timeZone instanceof TimeZone)) {
				timeZone = TimeZone.getDefault();
			}
			if (!timeZone.equals(threadTimeZone.get())) {
				threadTimeZone.set((TimeZone) timeZone);
			}
		}
		return this.executor.execute(arguments, scriptContext);
	}

	private static Calendar getDefaultFiscalYearStartDate(IScriptFunctionContext context) {
		// Get customized value from appContext or system
		Object property = context == null ? null : context.findProperty(PROPERTY_FISCAL_YEAR_START_DATE);
		if (property == null || property == UniqueTag.NOT_FOUND) {
			property = System.getProperty(PROPERTY_FISCAL_YEAR_START_DATE);
		}
		Calendar start = Calendar.getInstance();
		if (property != null) {
			try {
				Date date = FISCAL_YEAR_DATE_FORMAT.parse(property.toString());
				start.setTime(date);
				return start;
			} catch (ParseException e) {
				logger.log(Level.WARNING, e.getLocalizedMessage());
			}
		}

		// Default value is July 1 of current year
		start.set(Calendar.MONTH, 6);
		start.set(Calendar.DAY_OF_MONTH, 1);
		start.set(Calendar.HOUR_OF_DAY, 0);
		start.set(Calendar.MINUTE, 0);
		start.set(Calendar.SECOND, 0);
		start.set(Calendar.MILLISECOND, 0);
		return start;
	}

	private static Calendar getFiscalYearStartDate(IScriptFunctionContext context, Object[] args) throws BirtException {
		if (args.length > 1) {
			return getCalendar(DataTypeUtil.toDate(args[1]));
		}
		return getDefaultFiscalYearStartDate(context);
	}

}
