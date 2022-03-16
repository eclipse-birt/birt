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

import java.util.Date;

import org.eclipse.birt.data.engine.api.timefunction.TimeMember;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

public class TimeMemberUtil {
	private static ULocale defaultLocale = ULocale.getDefault();
	private static TimeZone timeZone = TimeZone.getDefault();

	public static void setDefaultLocale(ULocale defaultLocale) {
		TimeMemberUtil.defaultLocale = defaultLocale;
	}

	public static void setTimeZone(TimeZone timeZone) {
		TimeMemberUtil.timeZone = timeZone;
	}

	public static ULocale getDefaultLocale() {
		return defaultLocale;
	}

	public static TimeZone getTimeZone() {
		return timeZone;
	}

	public static TimeMember getCurrentMember(IDimension timeDimension, TimeMember cellTimeMember) {
		return toMember(timeDimension, null, cellTimeMember);
	}

	private static int getLowestLevelIndex(IDimension timeDimension, TimeMember cellTimeMember) {
		ILevel[] levels = timeDimension.getHierarchy().getLevels();
		String[] levelType = cellTimeMember.getLevelType();
		for (int i = 0; i < levels.length; i++) {
			if (levels[i].getLeveType().equals(levelType[levelType.length - 1])) {
				return i;
			}
		}
		return -1;
	}

	public static TimeMember toMember(IDimension timeDimension, Date referenceDate, TimeMember cellTimeMember) {
		ILevel[] levels = timeDimension.getHierarchy().getLevels();
		String[] levelType = null;
		if (referenceDate != null) {
			if (levels.length > 1) {
				levelType = new String[levels.length - 1];
			} else {
				levelType = new String[levels.length];
			}
		} else {
			levelType = new String[getLowestLevelIndex(timeDimension, cellTimeMember) + 1];
		}
		int[] levelValue = new int[levelType.length];
		Calendar cal = getCalendar(referenceDate);
		int year_woy = 1;
		int year = 1;
		int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
		for (int i = 0; i < cellTimeMember.getLevelType().length; i++) {
			if (TimeMember.TIME_LEVEL_TYPE_YEAR.equals(cellTimeMember.getLevelType()[i])) {
				cal.set(Calendar.YEAR, cellTimeMember.getMemberValue()[i]);
			} else if (TimeMember.TIME_LEVEL_TYPE_QUARTER.equals(cellTimeMember.getLevelType()[i])) {
				int month = cal.get(Calendar.MONTH) % 3 + (cellTimeMember.getMemberValue()[i] - 1) * 3;
				// if level is month, and the reference date is 2011.3.31,
				// the month is 2, so the date will set to 2011.2.31, the calendar will adapt
				// the date to 2011.3
				// here we set the month to 1.
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.set(Calendar.MONTH, month);
				if (cal.getActualMaximum(Calendar.DAY_OF_MONTH) > dayOfMonth) {
					cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				} else {
					cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				}
			} else if (TimeMember.TIME_LEVEL_TYPE_MONTH.equals(cellTimeMember.getLevelType()[i])) {
				// if level is month, and the reference date is 2011.3.31,
				// the month is 2, so the date will set to 2011.2.31, the calendar will adapt
				// the date to 2011.3
				// here we set the month to 1.
				cal.set(Calendar.DAY_OF_MONTH, 1);
				cal.set(Calendar.MONTH, cellTimeMember.getMemberValue()[i] - 1);
				if (cal.getActualMaximum(Calendar.DAY_OF_MONTH) > dayOfMonth) {
					cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				} else {
					cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
				}
			} else if (TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH.equals(cellTimeMember.getLevelType()[i])) {
				cal.set(Calendar.DAY_OF_MONTH, cellTimeMember.getMemberValue()[i]);
			} else if (TimeMember.TIME_LEVEL_TYPE_DAY_OF_WEEK.equals(cellTimeMember.getLevelType()[i])) {
				cal.set(Calendar.DAY_OF_WEEK, cellTimeMember.getMemberValue()[i]);
			} else if (TimeMember.TIME_LEVEL_TYPE_DAY_OF_YEAR.equals(cellTimeMember.getLevelType()[i])) {
				cal.set(Calendar.DAY_OF_YEAR, cellTimeMember.getMemberValue()[i]);
			} else if (TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR.equals(cellTimeMember.getLevelType()[i])) {
				year_woy = cal.get(Calendar.YEAR_WOY);
				year = cal.get(Calendar.YEAR);
				// year_woy < year, means last week of previous year
				// for example. 2011/1/1, the year_woy is 2010
				if (year_woy < year) {
					cal.set(Calendar.DAY_OF_WEEK, 7);
				} else if (year_woy > year) {
					cal.set(Calendar.DAY_OF_WEEK, 1);
				}
				cal.set(Calendar.WEEK_OF_YEAR, cellTimeMember.getMemberValue()[i]);
			} else if (TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH.equals(cellTimeMember.getLevelType()[i])) {
				year_woy = cal.get(Calendar.YEAR_WOY);
				year = cal.get(Calendar.YEAR);
				// year_woy < year, means last week of previous year
				// for example. 2011/1/1, the year_woy is 2010
				if (year_woy < year) {
					cal.set(Calendar.DAY_OF_WEEK, 7);
				} else if (year_woy > year) {
					cal.set(Calendar.DAY_OF_WEEK, 1);
				}
				cal.set(Calendar.WEEK_OF_MONTH, cellTimeMember.getMemberValue()[i]);
			}
		}
		for (int i = 0; i < levelType.length; i++) {
			levelType[i] = levels[i].getLeveType();
			if (TimeMember.TIME_LEVEL_TYPE_YEAR.equals(levelType[i])) {
				levelValue[i] = cal.get(Calendar.YEAR);
			} else if (TimeMember.TIME_LEVEL_TYPE_QUARTER.equals(levelType[i])) {
				levelValue[i] = quarter(cal);
			} else if (TimeMember.TIME_LEVEL_TYPE_MONTH.equals(levelType[i])) {
				levelValue[i] = cal.get(Calendar.MONTH) + 1;
			} else if (TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH.equals(levelType[i])) {
				levelValue[i] = cal.get(Calendar.DAY_OF_MONTH);
			} else if (TimeMember.TIME_LEVEL_TYPE_DAY_OF_WEEK.equals(levelType[i])) {
				levelValue[i] = cal.get(Calendar.DAY_OF_WEEK);
			} else if (TimeMember.TIME_LEVEL_TYPE_DAY_OF_YEAR.equals(levelType[i])) {
				levelValue[i] = cal.get(Calendar.DAY_OF_YEAR);
			} else if (TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR.equals(levelType[i])) {
				levelValue[i] = cal.get(Calendar.WEEK_OF_YEAR);
			} else if (TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH.equals(levelType[i])) {
				levelValue[i] = cal.get(Calendar.WEEK_OF_MONTH);
			}
		}

		return new TimeMember(levelValue, levelType);
	}

	public static TimeMember toMember(IDimension timeDimension, Date referenceDate) {
		ILevel[] levels = timeDimension.getHierarchy().getLevels();
		String[] levelType;
		levelType = new String[levels.length - 1];

		int[] levelValue = new int[levelType.length];
		Calendar cal = getCalendar(referenceDate);

		for (int i = 0; i < levelType.length; i++) {
			levelType[i] = levels[i].getLeveType();
			if (TimeMember.TIME_LEVEL_TYPE_YEAR.equals(levelType[i])) {
				levelValue[i] = cal.get(Calendar.YEAR);
			} else if (TimeMember.TIME_LEVEL_TYPE_QUARTER.equals(levelType[i])) {
				levelValue[i] = quarter(cal);
			} else if (TimeMember.TIME_LEVEL_TYPE_MONTH.equals(levelType[i])) {
				levelValue[i] = cal.get(Calendar.MONTH) + 1;
			} else if (TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH.equals(levelType[i])) {
				levelValue[i] = cal.get(Calendar.DAY_OF_MONTH);
			} else if (TimeMember.TIME_LEVEL_TYPE_DAY_OF_WEEK.equals(levelType[i])) {
				levelValue[i] = cal.get(Calendar.DAY_OF_WEEK);
			} else if (TimeMember.TIME_LEVEL_TYPE_DAY_OF_YEAR.equals(levelType[i])) {
				levelValue[i] = cal.get(Calendar.DAY_OF_YEAR);
			} else if (TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR.equals(levelType[i])) {
				levelValue[i] = cal.get(Calendar.WEEK_OF_YEAR);
			} else if (TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH.equals(levelType[i])) {
				levelValue[i] = cal.get(Calendar.WEEK_OF_MONTH);
			}
		}

		return new TimeMember(levelValue, levelType);
	}

	private static int quarter(Calendar cal) {
		int month = cal.get(Calendar.MONTH);
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
	 * 
	 * @param d
	 * @return
	 */
	private static Calendar getCalendar(Date d) {
		Calendar c = Calendar.getInstance(timeZone, defaultLocale);

		if (d == null) {
			c.clear();
			c.set(1970, 0, 1);
		} else {
			c.setTime(d);
		}
		return c;
	}
}
