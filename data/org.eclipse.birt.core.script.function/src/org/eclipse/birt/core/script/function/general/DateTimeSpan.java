/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.core.script.function.general;

import java.util.Date;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.function.i18n.Messages;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionContext;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionExecutor;

import com.ibm.icu.util.Calendar;

/**
 * Provides a set of functions for working with the difference between two
 * dates.
 * 
 */
public class DateTimeSpan {
	private static final String YEARS = "years";
	private static final String ADDDATE = "addDate";
	private static final String ADDTIME = "addTime";
	private static final String DAYS = "days";
	private static final String HOURS = "hours";
	private static final String MINUTES = "minutes";
	private static final String MONTHS = "months";
	private static final String SECONDS = "seconds";
	private static final String SUBDATE = "subDate";
	private static final String SUBTIME = "subTime";

	/**
	 * logger used to log syntax errors.
	 */
	static protected Logger logger = Logger.getLogger(DateTimeSpan.class.getName());

	/**
	 * The class is static, the application cannot create an instance of this class
	 */
	private DateTimeSpan() {
	}

	/**
	 * @param startDate A date object that represents the start of the span
	 * @param endDate   A date object that represents the end of the span
	 * @return the number of years between two dates
	 */
	static int years(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return 0;
		}

		if (!validateDateArgus(startDate, endDate)) {
			return -years(endDate, startDate);
		}

		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		// Get the year of startDate
		int startYear = startCal.get(Calendar.YEAR) - 1900;
		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		// Get the year of endDate
		int endYear = endCal.get(Calendar.YEAR) - 1900;
		assert (endYear >= startYear);
		// Get the span of the endYear and the startYear, only thinking of the
		// year but month and day
		int spanYear = endYear - startYear;
		// startCal.set( Calendar.YEAR, endYear + 1900 );
		startCal.add(Calendar.YEAR, spanYear);
		startDate = startCal.getTime();
		endDate = endCal.getTime();
		/*
		 * the value 0 if the argument is a Date equal to this Date; a value less than 0
		 * if the argument is a Date after this Date; and a value greater than 0 if the
		 * argument is a Date before this Date.
		 */
		if (startDate.compareTo(endDate) > 0) {
			spanYear -= 1;
		}
		return spanYear;
	}

	private static class YearsScriptFunctionExecutor implements IScriptFunctionExecutor {

		private static final long serialVersionUID = 1L;

		public Object execute(Object[] arguments, IScriptFunctionContext context) throws BirtException {
			if (arguments == null || arguments.length != 2)
				throw new BirtException("org.eclipse.birt.core.script.general", null,
						Messages.getString("invalid.number.of.argument") + "DateTimeSpan.year()");
			return years(DataTypeUtil.toDate(arguments[0]), DataTypeUtil.toDate(arguments[1]));
		}

	}

	/**
	 * @param startDate A date object that represents the start of the span
	 * @param endDate   A date object that represents the end of the span
	 * @return the number of months between two dates
	 */
	static int months(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return 0;
		}

		if (!validateDateArgus(startDate, endDate)) {
			return -months(endDate, startDate);
		}

		Calendar startCal = Calendar.getInstance();
		startCal.setTime(startDate);

		Calendar endCal = Calendar.getInstance();
		endCal.setTime(endDate);

		int startMonth = startCal.get(Calendar.YEAR) * 12 + startCal.get(Calendar.MONTH);
		int endMonth = endCal.get(Calendar.YEAR) * 12 + endCal.get(Calendar.MONTH);

		int spanMonth = endMonth - startMonth;

		startCal.add(Calendar.MONTH, spanMonth);

		if (startCal.getTime().compareTo(endCal.getTime()) > 0) {
			spanMonth--;
		}

		return spanMonth;
	}

	private static class MonthsScriptFunctionExecutor implements IScriptFunctionExecutor {

		private static final long serialVersionUID = 1L;

		public Object execute(Object[] arguments, IScriptFunctionContext context) throws BirtException {
			if (arguments == null || arguments.length != 2)
				throw new BirtException("org.eclipse.birt.core.script.general", null,
						Messages.getString("invalid.number.of.argument") + "DateTimeSpan.months()");
			return months(DataTypeUtil.toDate(arguments[0]), DataTypeUtil.toDate(arguments[1]));
		}
	}

	/**
	 * @param startDate A date object that represents the start of the span
	 * @param endDate   A date object that represents the end of the span
	 * @return the number of days between two dates
	 */
	static int days(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return 0;
		}

		if (!validateDateArgus(startDate, endDate)) {
			return -days(endDate, startDate);
		}

		long diff = endDate.getTime() - startDate.getTime();

		return (int) (diff / (1000 * 60 * 60 * 24));

	}

	private static class DaysScriptFunctionExecutor implements IScriptFunctionExecutor {

		private static final long serialVersionUID = 1L;

		public Object execute(Object[] arguments, IScriptFunctionContext context) throws BirtException {
			if (arguments == null || arguments.length != 2)
				throw new BirtException("org.eclipse.birt.core.script.general", null,
						Messages.getString("invalid.number.of.argument") + "DateTimeSpan.days()");
			return days(DataTypeUtil.toDate(arguments[0]), DataTypeUtil.toDate(arguments[1]));
		}
	}

	/**
	 * @param startDate A date object that represents the start of the span
	 * @param endDate   A date object that represents the end of the span
	 * @return the number of hours between two dates
	 */
	static int hours(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return 0;
		}

		if (!validateDateArgus(startDate, endDate)) {
			return -hours(endDate, startDate);
		}

		long diff = endDate.getTime() - startDate.getTime();

		return (int) (diff / (1000 * 60 * 60));
	}

	private static class HoursScriptFunctionExecutor implements IScriptFunctionExecutor {

		private static final long serialVersionUID = 1L;

		public Object execute(Object[] arguments, IScriptFunctionContext context) throws BirtException {
			if (arguments == null || arguments.length != 2)
				throw new BirtException("org.eclipse.birt.core.script.general", null,
						Messages.getString("invalid.number.of.argument") + "DateTimeSpan.hours()");
			return hours(DataTypeUtil.toDate(arguments[0]), DataTypeUtil.toDate(arguments[1]));
		}
	}

	/**
	 * @param startDate A date object that represents the start of the span
	 * @param endDate   A date object that represents the end of the span
	 * @return the number of minutes between two dates
	 */
	static int minutes(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return 0;
		}

		if (!validateDateArgus(startDate, endDate)) {
			return -minutes(endDate, startDate);
		}

		long diff = endDate.getTime() - startDate.getTime();

		return (int) (diff / (1000 * 60));
	}

	private static class MinutesScriptFunctionExecutor implements IScriptFunctionExecutor {

		private static final long serialVersionUID = 1L;

		public Object execute(Object[] arguments, IScriptFunctionContext context) throws BirtException {
			if (arguments == null || arguments.length != 2)
				throw new BirtException("org.eclipse.birt.core.script.general", null,
						Messages.getString("invalid.number.of.argument") + "DateTimeSpan.minutes()");
			return minutes(DataTypeUtil.toDate(arguments[0]), DataTypeUtil.toDate(arguments[1]));
		}
	}

	/**
	 * @param startDate A date object that represents the start of the span
	 * @param endDate   A date object that represents the end of the span
	 * @return the number of seconds between two dates
	 */
	static int seconds(Date startDate, Date endDate) {
		if (startDate == null || endDate == null) {
			return 0;
		}

		if (!validateDateArgus(startDate, endDate)) {
			return -seconds(endDate, startDate);
		}

		long diff = endDate.getTime() - startDate.getTime();

		return (int) (diff / 1000);

	}

	private static class SecondsScriptFunctionExecutor implements IScriptFunctionExecutor {

		private static final long serialVersionUID = 1L;

		public Object execute(Object[] arguments, IScriptFunctionContext context) throws BirtException {
			if (arguments == null || arguments.length != 2)
				throw new BirtException("org.eclipse.birt.core.script.general", null,
						Messages.getString("invalid.number.of.argument") + "DateTimeSpan.seconds()");
			return seconds(DataTypeUtil.toDate(arguments[0]), DataTypeUtil.toDate(arguments[1]));
		}
	}

	/**
	 * @param startDate A date object that represent the bass date.
	 * @param years     The number of years to add to the date.
	 * @param months    The number of month to add to the date.
	 * @param days      The number of days to add to the date.
	 * @return A date that results from adding the years, months, and days to the
	 *         start date.
	 */
	static Date addDate(Date startDate, int years, int months, int days) {
		Calendar startCal = Calendar.getInstance();
		Date firstDate = startDate;
		startCal.setTime(firstDate);
		/*
		 * Add years first. Then, using the resulting date, add the months. Then, using
		 * the resulting date, add the days.
		 */
		startCal.add(Calendar.YEAR, years);
		startCal.add(Calendar.MONTH, months);
		startCal.add(Calendar.DATE, days);

		return startCal.getTime();
	}

	private static class AddDateScriptFunctionExecutor implements IScriptFunctionExecutor {

		private static final long serialVersionUID = 1L;

		public Object execute(Object[] arguments, IScriptFunctionContext context) throws BirtException {
			if (arguments == null || arguments.length != 4)
				throw new BirtException("org.eclipse.birt.core.script.general", null,
						Messages.getString("invalid.number.of.argument") + "DateTimeSpan.addDate()");
			return addDate(DataTypeUtil.toDate(arguments[0]), DataTypeUtil.toInteger(arguments[1]),
					DataTypeUtil.toInteger(arguments[2]), DataTypeUtil.toInteger(arguments[3]));
		}
	}

	/**
	 * @param startDate A date object that represents the base date
	 * @param hours     The number of hours to add to the date
	 * @param minutes   The number of minutes to add to the date
	 * @param seconds   The number of seconds to add to the date
	 * @return A date that results adding the hours, minutes, seconds to the start
	 *         date.
	 */
	static Date addTime(Date startDate, int hours, int minutes, int seconds) {
		Calendar startCal = Calendar.getInstance();
		Date firstDate = startDate;
		startCal.setTime(firstDate);
		/*
		 * Add years first. Then, using the resulting date, add the months. Then, using
		 * the resulting date, add the days.
		 */
		startCal.add(Calendar.HOUR_OF_DAY, hours);
		startCal.add(Calendar.MINUTE, minutes);
		startCal.add(Calendar.SECOND, seconds);

		return startCal.getTime();
	}

	private static class AddTimeScriptFunctionExecutor implements IScriptFunctionExecutor {

		private static final long serialVersionUID = 1L;

		public Object execute(Object[] arguments, IScriptFunctionContext context) throws BirtException {
			if (arguments == null || arguments.length != 4)
				throw new BirtException("org.eclipse.birt.core.script.general", null,
						Messages.getString("invalid.number.of.argument") + "DateTimeSpan.addTime()");
			return addTime(DataTypeUtil.toDate(arguments[0]), DataTypeUtil.toInteger(arguments[1]),
					DataTypeUtil.toInteger(arguments[2]), DataTypeUtil.toInteger(arguments[3]));
		}
	}

	/**
	 * @param startDate A date object that represent the bass date.
	 * @param years     The number of years to add to the date.
	 * @param months    The number of month to add to the date.
	 * @param days      The number of days to add to the date.
	 * @return A date that results from subtract the years, months, and days from
	 *         the start date.
	 */
	static Date subDate(Date startDate, int years, int months, int days) {
		Calendar startCal = Calendar.getInstance();
		Date firstDate = startDate;
		startCal.setTime(firstDate);
		return addDate(startDate, -years, -months, -days);
	}

	private static class SubDateScriptFunctionExecutor implements IScriptFunctionExecutor {

		private static final long serialVersionUID = 1L;

		public Object execute(Object[] arguments, IScriptFunctionContext context) throws BirtException {
			if (arguments == null || arguments.length != 4)
				throw new BirtException("org.eclipse.birt.core.script.general", null,
						Messages.getString("invalid.number.of.argument") + "DateTimeSpan.subDate()");
			return subDate(DataTypeUtil.toDate(arguments[0]), DataTypeUtil.toInteger(arguments[1]),
					DataTypeUtil.toInteger(arguments[2]), DataTypeUtil.toInteger(arguments[3]));
		}
	}

	/**
	 * @param startDate A date object that represents the base date
	 * @param hours     The number of hours to add to the date
	 * @param minutes   The number of minutes to add to the date
	 * @param seconds   The number of seconds to add to the date
	 * @return A date that results subtracting the hours, minutes, seconds from the
	 *         start date.
	 */
	static Date subTime(Date startDate, int hours, int minutes, int seconds) {
		return addTime(startDate, -hours, -minutes, -seconds);
	}

	private static class SubTimeScriptFunctionExecutor implements IScriptFunctionExecutor {

		private static final long serialVersionUID = 1L;

		public Object execute(Object[] arguments, IScriptFunctionContext context) throws BirtException {
			if (arguments == null || arguments.length != 4)
				throw new BirtException("org.eclipse.birt.core.script.general", null,
						Messages.getString("invalid.number.of.argument") + "DateTimeSpan.subTime()");
			return subTime(DataTypeUtil.toDate(arguments[0]), DataTypeUtil.toInteger(arguments[1]),
					DataTypeUtil.toInteger(arguments[2]), DataTypeUtil.toInteger(arguments[3]));
		}
	}

	/**
	 * validate the order of the date arguments
	 * 
	 * @param start start date
	 * @param end   end date
	 * @return whether start date is before end date
	 */
	private static boolean validateDateArgus(Date start, Date end) {
		return start.compareTo(end) <= 0;
	}

	static IScriptFunctionExecutor getExecutor(String functionName) throws BirtException {
		if (YEARS.equals(functionName))
			return new YearsScriptFunctionExecutor();
		else if (ADDDATE.equals(functionName))
			return new AddDateScriptFunctionExecutor();
		else if (ADDTIME.equals(functionName))
			return new AddTimeScriptFunctionExecutor();
		else if (DAYS.equals(functionName))
			return new DaysScriptFunctionExecutor();
		else if (HOURS.equals(functionName))
			return new HoursScriptFunctionExecutor();
		else if (MINUTES.equals(functionName))
			return new MinutesScriptFunctionExecutor();
		else if (SECONDS.equals(functionName))
			return new SecondsScriptFunctionExecutor();
		else if (SUBDATE.equals(functionName))
			return new SubDateScriptFunctionExecutor();
		else if (SUBTIME.equals(functionName))
			return new SubTimeScriptFunctionExecutor();
		else if (MONTHS.equals(functionName))
			return new MonthsScriptFunctionExecutor();

		throw new BirtException("org.eclipse.birt.core.script.function.general", null,
				Messages.getString("invalid.function.name") + "DateTimeSpan." + functionName);
	}
}
