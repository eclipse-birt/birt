/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.timefunction.IPeriodsFunction;
import org.eclipse.birt.data.engine.api.timefunction.TimeMember;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;

/**
 * The Trailing N Period function returns a set of consecutive members that
 * include a specified member and can be located by the member and the Offset
 * number.Now we support trailing in 5 level, Year, Quarter, Month, Week, Day
 * 
 * 
 */
public class TrailingFunction extends AbstractMDX implements IPeriodsFunction {

	private String offsetLevel = "";
	private int offset = 0;
	private boolean isMoveForward = false;

	public TrailingFunction(String levelType, int offset) {
		this.offsetLevel = levelType;
		this.offset = offset;
		isMoveForward = offset > 0 ? true : false;
	}

	private Calendar adjustStartDate(Calendar curDate, String calculateUnit) {
		Calendar startDate = (Calendar) curDate.clone();

		if (calculateUnit.equals(YEAR)) {
			if (isMoveForward)
				setToYearStart(startDate);
			else
				setToYearEnd(startDate);
		} else if (calculateUnit.equals(QUARTER)) {
			if (isMoveForward)
				setToQuarterStart(startDate);
			else
				setToQuarterEnd(startDate);
		} else if (calculateUnit.equals(MONTH)) {
			if (isMoveForward)
				setToMonthStart(startDate);
			else
				setToMonthEnd(startDate);
		}

		return startDate;
	}

	/**
	 * Move to get end date. Note: If the input is 2011 May, and the offset is 3
	 * months. The passed-in startDate will be May 1st, 2011, and calculateUnit is
	 * MONTH, that means start from May, move 3 months, to August, the endDate to be
	 * returned is August 31st, 2011.
	 * 
	 * @param startDate
	 * @param calculateUnit
	 * @return
	 */
	private Calendar getEndDate(Calendar startDate, String calculateUnit) {
		Calendar endDate = (Calendar) startDate.clone();

		if (calculateUnit.equals(YEAR)) {
			if (isMoveForward)
				setToYearEnd(endDate);
			else
				setToYearStart(endDate);
		} else if (calculateUnit.equals(QUARTER)) {
			if (isMoveForward)
				setToQuarterEnd(endDate);
			else
				setToQuarterStart(endDate);
		} else if (calculateUnit.equals(MONTH)) {
			if (isMoveForward)
				setToMonthEnd(endDate);
			else
				setToMonthStart(endDate);
		} else if (calculateUnit.equals(WEEK)) {
			if (isMoveForward)
				setToWeekEnd(endDate);
			else
				setToWeekStart(endDate);
		}

		if (offsetLevel.equals(TimeMember.TIME_LEVEL_TYPE_YEAR))
			endDate.add(Calendar.YEAR, offset);
		else if (offsetLevel.equals(TimeMember.TIME_LEVEL_TYPE_QUARTER))
			endDate.add(Calendar.MONTH, offset * 3);
		else if (offsetLevel.equals(TimeMember.TIME_LEVEL_TYPE_MONTH))
			endDate.add(Calendar.MONTH, offset);
		else if (offsetLevel.equals(TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH))
			endDate.add(Calendar.WEEK_OF_YEAR, offset);
		else if (offsetLevel.equals(TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR))
			endDate.add(Calendar.WEEK_OF_YEAR, offset);
		else if (offsetLevel.equals(TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH))
			endDate.add(Calendar.DATE, offset);
		else if (offsetLevel.equals(TimeMember.TIME_LEVEL_TYPE_DAY_OF_WEEK))
			endDate.add(Calendar.DATE, offset);
		else if (offsetLevel.equals(TimeMember.TIME_LEVEL_TYPE_DAY_OF_YEAR))
			endDate.add(Calendar.DATE, offset);

		return endDate;
	}

	/**
	 * Get timeMember/s for current date, then make a move
	 * 
	 * @param curDate
	 * @param startDate
	 * @param endDate
	 * @param unit
	 * @param step
	 * @return
	 */
	private List<Calendar> moveAStep(Calendar curDate, Calendar endDate, String unit, String extraWeekLevel) {
		List<Calendar> times = new ArrayList<Calendar>();
		int step = isMoveForward ? 1 : -1;

		times.add((Calendar) curDate.clone());

		if (unit.equals(YEAR)) {
			if (isMoveForward)
				setToYearStart(curDate);
			else
				setToYearEnd(curDate);
			curDate.add(Calendar.YEAR, step);
		} else if (unit.equals(QUARTER)) {
			if (isMoveForward)
				setToQuarterStart(curDate);
			else
				setToQuarterEnd(curDate);
			curDate.add(Calendar.MONTH, step * 3);
		} else if (unit.equals(MONTH)) {
			if (isMoveForward)
				setToMonthStart(curDate);
			else
				setToMonthEnd(curDate);
			curDate.add(Calendar.MONTH, step);
		} else if (unit.equals(WEEK)) {
			Calendar tmpDate = (Calendar) curDate.clone();
			if (isMoveForward) {
				if (extraWeekLevel != null) {
					tmpDate.set(Calendar.DAY_OF_WEEK, 7);
					if (tmpDate.after(endDate))
						tmpDate = endDate;
					tmpDate = getExtraWeek(tmpDate, curDate, extraWeekLevel);
					if (tmpDate != null)
						times.add(tmpDate);
				}
				setToWeekStart(curDate);
				curDate.set(Calendar.DAY_OF_WEEK, 1);
			} else {
				if (extraWeekLevel != null) {
					tmpDate.set(Calendar.DAY_OF_WEEK, 1);
					if (tmpDate.before(endDate))
						tmpDate = endDate;
					tmpDate = getExtraWeek(tmpDate, curDate, extraWeekLevel);
					if (tmpDate != null)
						times.add(tmpDate);
				}
				setToWeekEnd(curDate);
				curDate.set(Calendar.DAY_OF_WEEK, 7);
			}
			curDate.add(Calendar.WEEK_OF_YEAR, step);
		} else if (unit.equals(DAY)) {
			curDate.add(Calendar.DATE, step);
		}

		return times;
	}

	/**
	 * Check if date1 is in the same time period ( month/ year ) with date2. If not,
	 * return date1, else return null.
	 * 
	 * @param date1
	 * @param date2
	 * @param extraWeekLevel
	 * @return
	 */
	private Calendar getExtraWeek(Calendar date1, Calendar date2, String extraWeekLevel) {

		if (extraWeekLevel.equals(TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH)) {
			if (date1.get(Calendar.MONTH) != date2.get(Calendar.MONTH))
				return date1;
		}
		if (extraWeekLevel.equals(TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR)) {
			if (date1.get(Calendar.YEAR) != date2.get(Calendar.YEAR))
				return date1;
		}

		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.data.impl.aggregation.function.
	 * IPeriodsFunction#getResult(org.eclipse.birt.data.engine.olap.data.impl.
	 * aggregation.function.TimeMember)
	 */
	public List<TimeMember> getResult(TimeMember member) {
		List<TimeMember> timeMembers = new ArrayList<TimeMember>();

		String[] levelTypes = member.getLevelType();
		int[] startValues = member.getMemberValue();

		Calendar curDate = new GregorianCalendar(TimeMemberUtil.getTimeZone(), TimeMemberUtil.getDefaultLocale());
		// Get start date
		curDate.clear();
		String calculateUnit = translateToCal(curDate, levelTypes, startValues);
		Calendar startDate = adjustStartDate(curDate, calculateUnit);

		// Get end date
		Calendar endDate = getEndDate(startDate, calculateUnit);

		curDate = (Calendar) startDate.clone();
		int[] fillDateTmp;
		List<Calendar> times;
		TimeMember timeMember;
		boolean end = false;
		String extraWeekLevel = calculateUnit.equals(WEEK) ? getExtraWeekLevel(levelTypes) : null;

		while (!end) {
			times = moveAStep(curDate, endDate, calculateUnit, extraWeekLevel);
			for (int i = 0; i < times.size(); i++) {
				fillDateTmp = getValueFromCal(times.get(i), levelTypes);
				timeMember = new TimeMember(fillDateTmp, levelTypes);
				timeMembers.add(timeMember);
			}
			if (isMoveForward) {
				end = !curDate.before(endDate);
			} else {
				end = !curDate.after(endDate);
			}
		}

		return timeMembers;
	}

	private String getExtraWeekLevel(String[] levelTypes) {
		String result = TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR;

		for (String level : levelTypes) {
			if (level.equals(TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH)
					|| level.equals(TimeMember.TIME_LEVEL_TYPE_QUARTER)
					|| level.equals(TimeMember.TIME_LEVEL_TYPE_MONTH))
				result = TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH;
		}

		return result;
	}

	private void setToYearStart(Calendar cal) {
		cal.set(Calendar.DAY_OF_YEAR, 1);
	}

	private void setToYearEnd(Calendar cal) {
		setToYearStart(cal);
		cal.add(Calendar.YEAR, 1);
		cal.add(Calendar.DATE, -1);
	}

	private void setToQuarterStart(Calendar cal) {
		int quarter = cal.get(Calendar.MONTH) / 3;
		cal.set(Calendar.MONTH, quarter * 3);
		cal.set(Calendar.DAY_OF_MONTH, 1);
	}

	private void setToQuarterEnd(Calendar cal) {
		setToQuarterStart(cal);
		cal.add(Calendar.MONTH, 3);
		cal.add(Calendar.DATE, -1);
	}

	private void setToMonthStart(Calendar cal) {
		cal.set(Calendar.DAY_OF_MONTH, 1);
	}

	private void setToMonthEnd(Calendar cal) {
		setToMonthStart(cal);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DATE, -1);
	}

	private void setToWeekStart(Calendar cal) {
		cal.set(Calendar.DAY_OF_WEEK, 1);
	}

	private void setToWeekEnd(Calendar cal) {
		cal.set(Calendar.DAY_OF_WEEK, 7);
	}
}
