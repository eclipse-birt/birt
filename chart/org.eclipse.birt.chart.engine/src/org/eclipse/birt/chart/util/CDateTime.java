/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.util;

import java.sql.Time;
import java.util.Date;
import java.util.Locale;

import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * A convenience data type provided to aid in wrapping a datetime value used
 * with datetime data elements. Refer to
 * {@link org.eclipse.birt.chart.model.data.DateTimeDataElement}
 */
public class CDateTime extends GregorianCalendar {

	private static final long serialVersionUID = 1L;

	private static final int MILLIS_IN_SECOND = 1000;

	private static final int MILLIS_IN_MINUTE = MILLIS_IN_SECOND * 60;

	private static final int MILLIS_IN_HOUR = MILLIS_IN_MINUTE * 60;

	private static final int MILLIS_IN_DAY = MILLIS_IN_HOUR * 24;

	private static final int[] iaUnitTypes = { Calendar.MILLISECOND, Calendar.SECOND, Calendar.MINUTE,
			Calendar.HOUR_OF_DAY, Calendar.DATE, Calendar.WEEK_OF_YEAR, Calendar.MONTH, Calendar.YEAR };

	public static final int QUARTER = 999;
	public static final int WEEK_OF_QUARTER = 1001;
	public static final int DAY_OF_QUARTER = 1002;

	private static int[] iaCalendarUnits = { Calendar.SECOND, Calendar.MINUTE, Calendar.HOUR_OF_DAY, Calendar.DATE,
			Calendar.MONTH, QUARTER };

	private static final SimpleDateFormat _sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss"); //$NON-NLS-1$

	private boolean bTimeOnly = false;

	private boolean bDateOnly = false;

	/**
	 * A zero-arg default constructor
	 */
	public CDateTime() {
		super();
	}

	/**
	 * A constructor that creates an instance from a given
	 * <code>java.util.Date</code> value. If it's an instance of
	 * <code>java.sql.Date</code>, it's Date only data. If it's an instance of
	 * <code>java.sql.Time</code>, it's Time only data. Otherwise, it means full
	 * date time value.
	 * 
	 * @param d A previously defined Date instance
	 */
	public CDateTime(Date d) {
		super();
		setTime(d);
		checkDateType(d);
	}

	/**
	 * A constructor that creates an instance from a given <code>Calendar</code>
	 * value
	 * 
	 * @param c A previously defined Calendar instance
	 */
	public CDateTime(Calendar c) {
		super();
		if (c != null) {
			setTime(c.getTime());
			if (c instanceof CDateTime) {
				checkDateType((CDateTime) c);
				if (((CDateTime) c).isFullDateTime()) {
					setTimeZone(c.getTimeZone());
				}
			}
		}
	}

	/**
	 * A constructor that creates an instance from a given <code>long</code> value
	 * 
	 * @param lTimeInMillis The time defined in milliseconds
	 */
	public CDateTime(long lTimeInMillis) {
		super();
		setTimeInMillis(lTimeInMillis);
	}

	/**
	 * A constructor that creates an instance for a specified year, month and date
	 * 
	 * @param year  The year associated with this instance
	 * @param month The month index (1-12) of the year (1-based)
	 * @param date  The day of the month associated with this instance
	 */
	public CDateTime(int year, int month, int date) {
		super(year, month - 1, date);
	}

	/**
	 * A constructor that creates an instance for a specified year, month, date,
	 * hour and minute
	 * 
	 * @param year   The year associated with this instance
	 * @param month  The month index (1-12) of the year (1-based)
	 * @param date   The day of the month associated with this instance
	 * @param hour   The hour (0-23) of the day (military) associated with this
	 *               instance
	 * @param minute The minute (0-59) of the hour associated with this instance
	 */
	public CDateTime(int year, int month, int date, int hour, int minute) {
		super(year, month - 1, date, hour, minute);
	}

	/**
	 * A constructor that creates an instance for a specified year, month, date,
	 * hour and minute
	 * 
	 * @param year   The year associated with this instance
	 * @param month  The month index (1-12) of the year (1-based)
	 * @param date   The day of the month associated with this instance
	 * @param hour   The hour (0-23) of the day (military) associated with this
	 *               instance
	 * @param minute The minute (0-59) of the hour associated with this instance
	 * @param second The second (0-59) of the minute associated with this instance
	 */
	public CDateTime(int year, int month, int date, int hour, int minute, int second) {
		super(year, month - 1, date, hour, minute, second);
	}

	/**
	 * A constructor that creates a default instance for a given locale
	 * 
	 * @param locale The locale for which the instance is being created
	 * @deprecated use {@link #CDateTime(ULocale)} instead.
	 */
	public CDateTime(Locale aLocale) {
		super(aLocale);
	}

	/**
	 * A constructor that creates a default instance for a given locale
	 * 
	 * @param locale The locale for which the instance is being created
	 * @since 2.1
	 */
	public CDateTime(ULocale locale) {
		super(locale);
	}

	/**
	 * A constructor that creates a default instance for a given timezone
	 * 
	 * @param tz The timezone for which the instance is being created
	 */
	public CDateTime(TimeZone tz) {
		super(tz);
	}

	/**
	 * A constructor that creates a default instance for a given timezone and locale
	 * 
	 * @param tz     The timezone for which the instance is being created
	 * @param locale The locale for which the instance is being created
	 * @deprecated use {@link #CDateTime(TimeZone, ULocale)} instead.
	 */
	public CDateTime(TimeZone tz, Locale locale) {
		super(tz, locale);
	}

	/**
	 * A constructor that creates a default instance for a given timezone and locale
	 * 
	 * @param tz     The timezone for which the instance is being created
	 * @param locale The locale for which the instance is being created
	 */
	public CDateTime(TimeZone tz, ULocale locale) {
		super(tz, locale);
	}

	/**
	 * A convenient method used in building the ticks for a datetime scale. Computes
	 * a new datetime object relative to the existing one moving back by 'step'
	 * units.
	 * 
	 * @param iUnit
	 * @param iStep
	 * 
	 * @return new instance
	 */
	public CDateTime backward(int iUnit, int iStep) {
		CDateTime cd = (CDateTime) clone();
		if (iUnit == QUARTER) {
			cd.add(Calendar.MONTH, (iStep == 0 ? 1 : iStep) * -3);
		} else {
			cd.add(iUnit, -iStep);
		}
		return cd;
	}

	/**
	 * A convenient method used in building the ticks for a datetime scale. Computes
	 * a new datetime object relative to the existing one moving forward by 'step'
	 * units.
	 * 
	 * @param iUnit
	 * @param iStep
	 * @return new instance
	 */
	public CDateTime forward(int iUnit, int iStep) {
		CDateTime cd = (CDateTime) clone();
		if (iUnit == QUARTER) {
			cd.add(Calendar.MONTH, (iStep == 0 ? 1 : iStep) * 3);
		} else {
			cd.add(iUnit, iStep);
		}
		return cd;
	}

	/**
	 * Returns the year associated with this instance
	 * 
	 * @return The year associated with this instance
	 */
	public final int getYear() {
		return get(Calendar.YEAR);
	}

	/**
	 * Returns the month (0-based) associated with this instance
	 * 
	 * @return The month associated with this instance
	 */
	public final int getMonth() {
		return get(Calendar.MONTH);
	}

	/**
	 * Returns the day of the month associated with this instance
	 * 
	 * @return The day of the month associated with this instance
	 */
	public final int getDay() {
		return get(Calendar.DATE);
	}

	/**
	 * Returns the hour (military) associated with this instance
	 * 
	 * @return The hour associated with this instance
	 */
	public final int getHour() {
		return get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * Returns the minute associated with this instance
	 * 
	 * @return The minute associated with this instance
	 */
	public final int getMinute() {
		return get(Calendar.MINUTE);
	}

	/**
	 * Returns the second associated with this instance
	 * 
	 * @return The second associated with this instance
	 */
	public final int getSecond() {
		return get(Calendar.SECOND);
	}

	/**
	 * Returns the most significant datetime unit in which there's a difference or 0
	 * if there is no difference.
	 * 
	 * @return The least significant 'Calendar' unit in which a difference occurred
	 */
	public static final int getDifference(CDateTime cdt1, CDateTime cdt2) {
		if (cdt1.getYear() != cdt2.getYear()) {
			return Calendar.YEAR;
		} else if (cdt1.getMonth() != cdt2.getMonth()) {
			return Calendar.MONTH;
		} else if (cdt1.getDay() != cdt2.getDay()) {
			return Calendar.DATE;
		} else if (cdt1.getHour() != cdt2.getHour()) {
			return Calendar.HOUR_OF_DAY;
		} else if (cdt1.getMinute() != cdt2.getMinute()) {
			return Calendar.MINUTE;
		} else if (cdt1.getSecond() != cdt2.getSecond()) {
			return Calendar.SECOND;
		} else {
			return 0;
		}
	}

	/**
	 * Returns a preferred format specifier for tick labels that represent axis
	 * values that will be computed based on the difference between cdt1 and cdt2
	 * 
	 * @param iUnit The unit for which a preferred pattern is being requested
	 * 
	 * @return A preferred datetime format pattern for the given unit
	 */
	public static final String getPreferredFormat(int iUnit) {
		if (iUnit == Calendar.YEAR) {
			return "yyyy"; //$NON-NLS-1$
		} else if (iUnit == Calendar.MONTH) {
			return "MMM yyyy"; //$NON-NLS-1$
		} else if (iUnit == Calendar.DATE) {
			return "MM-dd-yyyy"; //$NON-NLS-1$
		} else if (iUnit == Calendar.HOUR_OF_DAY) {
			return "MM-dd-yy\nHH:mm"; //$NON-NLS-1$
		} else if (iUnit == Calendar.MINUTE) {
			return "HH:mm:ss"; //$NON-NLS-1$
		} else if (iUnit == Calendar.SECOND) {
			return "HH:mm:ss"; //$NON-NLS-1$
		}
		return null;
	}

	/**
	 * Returns a preferred format specifier for given data-time values will be
	 * computed based on the difference between minDateTime and maxDateTime
	 * 
	 * @param minDateTime The minimum data-time value
	 * @param maxDateTime The maximum data-time value
	 * @return A preferred datetime unit for the given values
	 */
	public final static int getPreferredUnit(CDateTime minDateTime, CDateTime maxDateTime) {
		if (minDateTime.equals(maxDateTime)) {
			for (int i = 0; i < iaCalendarUnits.length; i++) {
				int pUnit = iaCalendarUnits[i];
				if (minDateTime.get(pUnit) > 0) {
					return pUnit;
				}
			}
			return Calendar.YEAR;
		} else {
			for (int i = 0; i < iaCalendarUnits.length; i++) {
				int pUnit = iaCalendarUnits[i];
				if (computeDifference(maxDateTime, minDateTime, pUnit) <= 95) {
					return pUnit;
				}
			}
			return Calendar.YEAR;
		}
	}

	/**
	 * Computes the difference between two given datetime values as a fraction for
	 * the requested field.
	 * 
	 * @param cdt1  The first datetime value
	 * @param cdt2  The second datetime value
	 * @param iUnit The field with respect to which the difference is being computed
	 *              as a fraction
	 * 
	 * @return The fractional difference between the two specified datetime values
	 */
	public static final double computeDifference(CDateTime cdt1, CDateTime cdt2, int iUnit) {
		return computeDifference(cdt1, cdt2, iUnit, false);
	}

	/**
	 * Computes the difference between two given datetime values as a fraction for
	 * the requested field.
	 * 
	 * @param cdt1      The first datetime value
	 * @param cdt2      The second datetime value
	 * @param iUnit     The field with respect to which the difference is being
	 *                  computed as a fraction
	 * @param trimBelow Specifies if trim the unit below the given unit.
	 * 
	 * @return The fractional difference between the two specified datetime values
	 */
	public static final double computeDifference(CDateTime cdt1, CDateTime cdt2, int iUnit, boolean trimBelow) {
		final long l1 = cdt1.getTimeInMillis();
		final long l2 = cdt2.getTimeInMillis();
		if (iUnit == Calendar.MILLISECOND) {
			double rt = (l1 - l2);
			if (trimBelow) {
				rt = Math.floor(rt);
			}
			return rt;
		} else if (iUnit == Calendar.SECOND) {
			double rt = (double) (l1 - l2) / MILLIS_IN_SECOND;
			if (trimBelow) {
				rt = Math.floor(rt);
			}
			return rt;
		} else if (iUnit == Calendar.MINUTE) {
			double rt = (double) (l1 - l2) / MILLIS_IN_MINUTE;
			if (trimBelow) {
				rt = Math.floor(rt);
			}
			return rt;
		} else if (iUnit == Calendar.HOUR_OF_DAY || iUnit == Calendar.HOUR) {
			double rt = (double) (l1 - l2) / MILLIS_IN_HOUR;
			if (trimBelow) {
				rt = Math.floor(rt);
			}
			return rt;
		} else if (iUnit == Calendar.DATE) {
			double rt = (double) (l1 - l2) / MILLIS_IN_DAY;
			if (trimBelow) {
				rt = Math.floor(rt);
			}
			return rt;
		} else if (iUnit == Calendar.WEEK_OF_YEAR) {
			final double dDays = computeDifference(cdt1, cdt2, Calendar.DATE);
			double rt = dDays / 7.0;
			if (trimBelow) {
				rt = Math.floor(rt);
			}
			return rt;
		} else if (iUnit == Calendar.MONTH) {
			if (trimBelow) {
				final double dYears = cdt1.getYear() - cdt2.getYear();
				return dYears * 12 + (cdt1.getMonth() - cdt2.getMonth());
			} else {
				// this is just an approximate result.
				final double dDays = computeDifference(cdt1, cdt2, Calendar.DATE);
				return dDays / 30.4375;
			}
		} else if (iUnit == Calendar.YEAR) {
			if (trimBelow) {
				return cdt1.getYear() - cdt2.getYear();
			} else {
				// this is just an approximate result.
				final double dDays = computeDifference(cdt1, cdt2, Calendar.DATE);
				return dDays / 365.25;
			}
		} else if (iUnit == QUARTER) {
			int maxQuarter = cdt1.getYear() * 4 + numberOfQuarter(cdt1);
			int minQuarter = cdt2.getYear() * 4 + numberOfQuarter(cdt2);
			return maxQuarter - minQuarter;
		}

		return 0;
	}

	/**
	 * Quarter number (1 to 4) of date/time value d The method is merged from DtE's
	 * API.
	 * 
	 * @param d
	 * @return
	 * @since 2.3
	 */
	private static int numberOfQuarter(CDateTime d) {
		if (d == null)
			throw new java.lang.IllegalArgumentException("date value is null!"); //$NON-NLS-1$

		int month = d.getMonth();
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

	private static final int getDefaultUnit(CDateTime c) {
		if (c.isTimeOnly()) {
			return Calendar.SECOND;
		}
		if (c.isFullDateTime()) {
			return Calendar.MILLISECOND;
		}
		return Calendar.DATE;
	}

	/**
	 * Walks through all values in a dataset and computes the least significant unit
	 * for which a difference was noted.
	 * 
	 * @param dsi The dataset iterator that facilitates visiting individual values
	 * 
	 * @return The least significant unit for which a difference in datetime values
	 *         was noted
	 */
	public static final int computeUnit(DataSetIterator dsi) {
		// If only one data, do not compute unit but get it via type directly.
		if (dsi.size() == 1) {
			dsi.reset();
			Object value = dsi.next();
			if (value instanceof CDateTime) {
				return getDefaultUnit((CDateTime) value);
			}
		}
		Calendar cCurr, cPrev;

		for (int k = 0; k < iaUnitTypes.length; k++) {
			cPrev = (Calendar) dsi.last();
			dsi.reset();
			while (dsi.hasNext()) {
				cCurr = (Calendar) dsi.next();
				if (cCurr != null && cPrev != null && cCurr.get(iaUnitTypes[k]) != cPrev.get(iaUnitTypes[k])) {
					return iaUnitTypes[k]; // THE UNIT FOR WHICH A DIFFERENCE
					// WAS NOTED
				}

				if (cCurr != null) {
					cPrev = cCurr;
				}
			}
		}

		// if all no difference, return year as default unit.
		return Calendar.DATE;
	}

	/**
	 * Walks through all values in a datetime array and computes the least
	 * significant unit for which a difference was noted.
	 * 
	 * @param cdta A datetime array for which the least significant unit difference
	 *             is to be computed
	 * 
	 * @return The least significant unit for which a difference in datetime values
	 *         was noted
	 */
	public static final int computeUnit(CDateTime[] cdta) throws ChartException {
		if (cdta.length == 1) {
			return getDefaultUnit(cdta[0]);
		}

		int j;
		for (int k = 0; k < iaUnitTypes.length; k++) {
			for (int i = 0; i < cdta.length; i++) {
				j = i + 1;
				if (j > cdta.length - 1) {
					j = 0;
				}

				if (cdta[i] == null || cdta[j] == null) {
					throw new ChartException(ChartEnginePlugin.ID, ChartException.VALIDATION,
							"exception.base.orthogonal.null.datadefinition", //$NON-NLS-1$
							Messages.getResourceBundle());
				}

				if (cdta[i].get(iaUnitTypes[k]) != cdta[j].get(iaUnitTypes[k])) {
					return iaUnitTypes[k]; // THE UNIT FOR WHICH A DIFFERENCE
					// WAS NOTED
				}
			}
		}

		// if all no difference, return year as default unit.
		return Calendar.YEAR;
	}

	/**
	 * Returns the number of days for a particular (month,year) combination
	 * 
	 * @param iMonth The month (0-11) for which the day count is to be retrieved
	 * @param iYear  The year for which the day count is to be retrieved
	 * @return number of days
	 */
	public static final int getMaximumDaysIn(int iMonth, int iYear) {
		CDateTime cdt = new CDateTime();
		cdt.set(Calendar.YEAR, iYear);
		cdt.set(Calendar.MONTH, iMonth);
		return cdt.getActualMaximum(Calendar.DATE);
	}

	/**
	 * Returns the number of days for a particular year
	 * 
	 * @param iYear The year for which the day count is to be retrieved
	 * 
	 * @return The number of days in the specified year
	 */
	public static final int getMaximumDaysIn(int iYear) {
		CDateTime cdt = new CDateTime();
		cdt.set(Calendar.YEAR, iYear);
		return cdt.getActualMaximum(Calendar.DAY_OF_YEAR);
	}

	/**
	 * A convenience method provided to return the number of milliseconds available
	 * in a given unit
	 * 
	 * @param iUnit The unit for which the number of milliseconds are to be computed
	 * 
	 * @return The number of milliseconds for the specified unit
	 */
	public static final double inMillis(int iUnit) {
		if (iUnit == Calendar.SECOND) {
			return MILLIS_IN_SECOND;
		} else if (iUnit == Calendar.MINUTE) {
			return MILLIS_IN_MINUTE;
		} else if (iUnit == Calendar.HOUR) {
			return MILLIS_IN_HOUR;
		} else if (iUnit == Calendar.DATE) {
			return MILLIS_IN_DAY;
		} else if (iUnit == Calendar.MONTH) {
			return MILLIS_IN_DAY * 30.4375;
		} else if (iUnit == Calendar.YEAR) {
			return MILLIS_IN_DAY * 365.25;
		}
		return 0;
	}

	/**
	 * Zeroes out all units for this datetime instance below a specified unit. If
	 * it's full date time, no trim because original value should be used to format
	 * using time zone.
	 * 
	 * @param iUnit The unit below which all values are to be zeroed out
	 */
	public final void clearBelow(int iUnit) {
		clearBelow(iUnit, false);
	}

	/**
	 * Zeroes out all units for this datetime instance below a specified unit.
	 * 
	 * @param iUnit  The unit below which all values are to be zeroed out
	 * @param always indicates if it's always trimmed no matter if it's full date
	 *               time
	 */
	public final void clearBelow(int iUnit, boolean always) {
		if (!always && isFullDateTime()) {
			// Do not clear below because of timezone issue
			return;
		}
		if (iUnit == YEAR) {
			set(Calendar.MILLISECOND, 0);
			set(Calendar.SECOND, 0);
			set(Calendar.MINUTE, 0);
			set(Calendar.HOUR, 0);
			set(Calendar.DATE, 1);
			set(Calendar.AM_PM, AM);
			set(Calendar.MONTH, 0);
		} else if (iUnit == MONTH || iUnit == QUARTER) {
			set(Calendar.MILLISECOND, 0);
			set(Calendar.SECOND, 0);
			set(Calendar.MINUTE, 0);
			set(Calendar.HOUR, 0);
			set(Calendar.AM_PM, AM);
			set(Calendar.DATE, 1);

			if (iUnit == QUARTER) {
				set(Calendar.MONTH, (getMonth() / 3) * 3);
			}
		} else if (iUnit == WEEK_OF_YEAR || iUnit == WEEK_OF_MONTH) {
			set(Calendar.MILLISECOND, 0);
			set(Calendar.SECOND, 0);
			set(Calendar.MINUTE, 0);
			set(Calendar.HOUR, 0);
			set(Calendar.AM_PM, AM);

			// Assume that Sunday is the first day, and other days will go back
			// to current Sunday
			int weekDay = get(DAY_OF_WEEK);
			add(DATE, 1 - weekDay);
		} else if (iUnit == WEEK_OF_QUARTER) {
			set(Calendar.MILLISECOND, 0);
			set(Calendar.SECOND, 0);
			set(Calendar.MINUTE, 0);
			set(Calendar.HOUR, 0);
			set(Calendar.AM_PM, AM);
		} else if (iUnit == DATE || iUnit == DAY_OF_MONTH || iUnit == DAY_OF_WEEK || iUnit == DAY_OF_QUARTER
				|| iUnit == DAY_OF_YEAR) {
			set(Calendar.MILLISECOND, 0);
			set(Calendar.SECOND, 0);
			set(Calendar.MINUTE, 0);
			set(Calendar.HOUR, 0);
			// Must reset AM/PM
			set(Calendar.AM_PM, AM);
		} else if (iUnit == HOUR || iUnit == HOUR_OF_DAY) {
			set(Calendar.MILLISECOND, 0);
			set(Calendar.SECOND, 0);
			set(Calendar.MINUTE, 0);
		} else if (iUnit == MINUTE) {
			set(Calendar.MILLISECOND, 0);
			set(Calendar.SECOND, 0);
		} else if (iUnit == SECOND) {
			set(Calendar.MILLISECOND, 0);
		}
	}

	/**
	 * Reset all units for this datetime instance above a specified unit.
	 * 
	 * @param iUnit The unit above which year values are to be reset
	 */
	public final void clearAbove(int iUnit) {
		clearAbove(iUnit, false);
	}

	/**
	 * Reset all units for this datetime instance above a specified unit.
	 * 
	 * @param iUnit  The unit above which year values are to be reset
	 * @param always indicates if it's always trimmed no matter if it's full date
	 *               time
	 */
	public final void clearAbove(int iUnit, boolean always) {
		if (!always && isFullDateTime()) {
			// Do not clear below because of timezone issue
			return;
		}
		if (iUnit == YEAR) {
			return;
		} else if (iUnit == MONTH || iUnit == QUARTER) {
			set(Calendar.YEAR, 2000); // No more sense here for 2000, just set 2000 as uniform year to group month,
										// quarter and so on without keeping hierarchy.
		} else if (iUnit == DATE || iUnit == DAY_OF_MONTH || iUnit == DAY_OF_WEEK || iUnit == DAY_OF_QUARTER
				|| iUnit == DAY_OF_YEAR) {
			set(Calendar.YEAR, 2000);
		} else if (iUnit == WEEK_OF_YEAR || iUnit == WEEK_OF_MONTH || iUnit == WEEK_OF_QUARTER) {
			set(Calendar.YEAR, 2000);
		} else if (iUnit == HOUR_OF_DAY || iUnit == HOUR) {
			set(Calendar.YEAR, 2000);
			set(Calendar.MONTH, 0);
			set(Calendar.DATE, 1);
		} else if (iUnit == MINUTE) {
			set(Calendar.YEAR, 2000);
			set(Calendar.MONTH, 0);
			set(Calendar.DATE, 1);
			set(Calendar.AM_PM, AM);
			set(Calendar.HOUR, 0);
		} else if (iUnit == SECOND) {
			set(Calendar.YEAR, 2000);
			set(Calendar.MONTH, 0);
			set(Calendar.DATE, 1);
			set(Calendar.AM_PM, AM);
			set(Calendar.HOUR, 0);
			set(Calendar.MINUTE, 0);
		}

		return;
	}

	/**
	 * returns a CDateTime, whose value equals to the unit start of the current
	 * instance
	 * 
	 * @param iUnit
	 * @return new instance
	 */
	public CDateTime getUnitStart(int iUnit) {
		CDateTime cd = new CDateTime(this);
		cd.clearBelow(iUnit);
		return cd;
	}

	/**
	 * Parses a value formatted as MM-dd-yyyy HH:mm:ss and attempts to create an
	 * instance of this object
	 * 
	 * @param sDateTimeValue The value to be parsed
	 * @return An instance of the datetime value created
	 */
	public static CDateTime parse(String sDateTimeValue) {
		try {
			return new CDateTime(_sdf.parse(sDateTimeValue));
		} catch (Exception ex) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public final String toString() {
		return _sdf.format(getTime());
	}

	public boolean after(Object when) {
		if (when == null) {
			return false;
		}
		return super.after(when);
	}

	public boolean before(Object when) {
		if (when == null) {
			return false;
		}
		return super.before(when);
	}

	/**
	 * The property timeOnly indicates that this instance of CDateTime only
	 * represents a Time value, the Date value will be ignored.
	 * 
	 * @return true if time only.
	 */
	public boolean isTimeOnly() {
		return bTimeOnly;
	}

	/**
	 * Represents if current value has both Date and Time. If yes, Timezone will be
	 * considered during formatting.
	 * 
	 * @return true if current value has both Date and Time
	 */
	public boolean isFullDateTime() {
		return !bTimeOnly && !bDateOnly;
	}

	/**
	 * The property timeOnly indicates that this instance of CDateTime only
	 * represents a Time value, the Date value will be ignored.
	 * 
	 * @param timeOnly The bTimeOnly to set.
	 */
	public void setTimeOnly(boolean timeOnly) {
		bTimeOnly = timeOnly;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.icu.util.Calendar#add(int, int)
	 */
	public void add(int unit, int step) {
		if (unit == QUARTER) {
			super.add(Calendar.MONTH, (step == 0 ? 1 : step) * 3);
			return;
		}
		super.add(unit, step);
	}

	/**
	 * Checks if specified is instance of java.sql.Date.
	 * 
	 * @param d
	 */
	private void checkDateType(Date d) {
		// If it's SQL Date, it's Date only
		bDateOnly = (d instanceof java.sql.Date);
		// If it's SQL Time, it's Time only
		if (d instanceof Time) {
			setTimeOnly(true);
		}
	}

	private void checkDateType(CDateTime d) {
		this.bDateOnly = d.bDateOnly;
		this.setTimeOnly(d.isTimeOnly());
	}

	public Date getDateTime() {
		if (bDateOnly) {
			return new java.sql.Date(getTime().getTime());
		}
		return getTime();
	}
}
