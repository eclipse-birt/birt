
package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import java.util.List;

import org.eclipse.birt.data.engine.api.timefunction.ReferenceDate;
import org.eclipse.birt.data.engine.api.timefunction.TimeMember;

import com.ibm.icu.util.Calendar;

/**
 * This abstract class used for MDX function. MTD,YTD,trailing,etc,,, will
 * extends this class, you can add some base method here.
 * 
 * @author peng.shi
 * 
 */
abstract public class AbstractMDX {

	protected static final String YEAR = "year";
	protected static final String QUARTER = "quarter";
	protected static final String MONTH = "month";
	protected static final String WEEK = "week";
	protected static final String DAY = "day";

	protected boolean isCurrent = false;

	private ReferenceDate referenceDate = null;

	/**
	 * translate the TimeMember.values to Calendar return the base
	 * level("year","month","day"...)
	 * 
	 * @param cal
	 * @param levelTypes
	 * @param values
	 * @return
	 */
	protected String translateToCal(Calendar cal, String[] levelTypes, int[] values) {
		String type = "";
		int dayOfWeek = 1;
		int month = 1;
		for (int i = 0; i < values.length; i++) {
			if (levelTypes[i].equals(TimeMember.TIME_LEVEL_TYPE_YEAR)) {
				// cal.get(Calendar.YEAR );
				cal.set(Calendar.YEAR, values[i]);
				type = YEAR;
			}

			else if (levelTypes[i].equals(TimeMember.TIME_LEVEL_TYPE_QUARTER)) {
				// no quarter in cal,so set the corresponding month
				cal.set(Calendar.MONTH, values[i] * 3 - 1);
				type = QUARTER;
			}

			else if (levelTypes[i].equals(TimeMember.TIME_LEVEL_TYPE_MONTH)) {
				cal.set(Calendar.MONTH, values[i] - 1);
				type = MONTH;
				month = values[i] - 1;
			}

			else if (levelTypes[i].equals(TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH)) {
				if (referenceDate != null && referenceDate.getDate() != null) {
					dayOfWeek = referenceDate.getDate().getDay();
					cal.set(Calendar.DAY_OF_WEEK, dayOfWeek + 1);
				} else {
					int year_woy = cal.get(Calendar.YEAR_WOY);
					int year = cal.get(Calendar.YEAR);
					if (year_woy < year) {
						cal.set(Calendar.DAY_OF_WEEK, 7);
					} else if (year_woy > year) {
						cal.set(Calendar.DAY_OF_WEEK, 1);
					}
				}

				cal.set(Calendar.WEEK_OF_MONTH, values[i]);

				setAcrossMonthWeekDay(cal, month);
				type = WEEK;
			}

			else if (levelTypes[i].equals(TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR)) {
				if (referenceDate != null && referenceDate.getDate() != null) {
					dayOfWeek = referenceDate.getDate().getDay();
					cal.set(Calendar.DAY_OF_WEEK, dayOfWeek + 1);
				} else {
					int year_woy = cal.get(Calendar.YEAR_WOY);
					int year = cal.get(Calendar.YEAR);
					if (year_woy < year) {
						cal.set(Calendar.DAY_OF_WEEK, 7);
					} else if (year_woy > year) {
						cal.set(Calendar.DAY_OF_WEEK, 1);
					}
				}
				cal.set(Calendar.WEEK_OF_YEAR, values[i]);
				setAcrossMonthWeekDay(cal, month);
				type = WEEK;
			} else if (levelTypes[i].equals(TimeMember.TIME_LEVEL_TYPE_DAY_OF_WEEK)) {
				// here seems a com.ibm.icu.util.Calendar bug, if do not call cal.get()
				// sometimes cal.set() will change date to a wrong result.
				cal.get(Calendar.DAY_OF_WEEK);
				cal.set(Calendar.DAY_OF_WEEK, values[i]);
				type = DAY;
			}

			else if (levelTypes[i].equals(TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH)) {
				cal.set(Calendar.DAY_OF_MONTH, values[i]);
				type = DAY;
			}

			else if (levelTypes[i].equals(TimeMember.TIME_LEVEL_TYPE_DAY_OF_YEAR)) {
				cal.set(Calendar.DAY_OF_YEAR, values[i]);
				type = DAY;
			}
		}

		this.referenceDate = new ReferenceDate(cal.getTime());
		return type;
	}

	private void setAcrossMonthWeekDay(Calendar cal, int monthBase) {
		int month = cal.get(Calendar.MONTH);
		if (month == monthBase) {
			return;
		}

		cal.set(Calendar.DAY_OF_WEEK, 1);
		int monthStart = cal.get(Calendar.MONTH);

		if (monthStart == monthBase) {
			return;
		}

		cal.set(Calendar.DAY_OF_WEEK, 7);

	}

	/**
	 * get the TimeMember.values from Calendar
	 * 
	 * @param cal
	 * @param levelTypes
	 * @return
	 */
	protected int[] getValueFromCal(Calendar cal, String[] levelTypes) {
		int[] tmp = new int[levelTypes.length];

		for (int i = 0; i < levelTypes.length; i++) {
			if (levelTypes[i].equals(TimeMember.TIME_LEVEL_TYPE_YEAR)) {
				tmp[i] = cal.get(Calendar.YEAR);
			}

			else if (levelTypes[i].equals(TimeMember.TIME_LEVEL_TYPE_QUARTER)) {
				tmp[i] = cal.get(Calendar.MONTH) / 3 + 1;
			}

			else if (levelTypes[i].equals(TimeMember.TIME_LEVEL_TYPE_MONTH)) {
				tmp[i] = cal.get(Calendar.MONTH) + 1;
			}

			else if (levelTypes[i].equals(TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH)) {
				tmp[i] = cal.get(Calendar.WEEK_OF_MONTH);
			}

			else if (levelTypes[i].equals(TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR)) {
				tmp[i] = cal.get(Calendar.WEEK_OF_YEAR);
			}

			else if (levelTypes[i].equals(TimeMember.TIME_LEVEL_TYPE_DAY_OF_WEEK)) {
				tmp[i] = cal.get(Calendar.DAY_OF_WEEK);
			}

			else if (levelTypes[i].equals(TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH)) {
				tmp[i] = cal.get(Calendar.DAY_OF_MONTH);
			}

			else if (levelTypes[i].equals(TimeMember.TIME_LEVEL_TYPE_DAY_OF_YEAR)) {
				tmp[i] = cal.get(Calendar.DAY_OF_YEAR);
			}
		}

		return tmp;
	}

	protected void retrieveWeek(List<TimeMember> list, Calendar cal, String[] levels, String type) {
		int endWeek;
		int startWeek = 1;
		int startMonth = 1;
		Calendar startCal = (Calendar) cal.clone();

		/*
		 * Special case for week across year, etc the last week of 2011. Year will be
		 * 2011, Year_woy will be 2012, and week_of_year will be 1, which means the
		 * first week of 2012 rather than the 53rd week of 2011. Hence, in this case, we
		 * get the number of the last week but one, and plus one manually.
		 */

		if (cal.get(Calendar.YEAR) != cal.get(Calendar.YEAR_WOY)) {
			cal.add(Calendar.WEEK_OF_YEAR, -1);
			endWeek = cal.get(Calendar.WEEK_OF_YEAR) + 1;
			cal.add(Calendar.WEEK_OF_YEAR, 1);
		} else {
			endWeek = cal.get(Calendar.WEEK_OF_YEAR);
		}
		if (type.equals("yearToDate")) {
			startCal.set(Calendar.MONTH, 0);
			startCal.set(Calendar.DAY_OF_MONTH, 1);
			// week of year > 1, this week should also be added
			if (startCal.get(Calendar.WEEK_OF_YEAR) > 1) {
				int[] newValues = getValueFromCal(startCal, levels);
				TimeMember newMember = new TimeMember(newValues, levels);
				list.add(newMember);
				startCal.add(Calendar.WEEK_OF_YEAR, 1);
			}
			startWeek = 1;
		} else if (type.equals("quarterToDate")) {
			int quarter = cal.get(Calendar.MONTH) / 3 + 1;
			startMonth = quarter * 3 - 3;
			startCal.set(Calendar.MONTH, startMonth);
			startCal.set(Calendar.DAY_OF_MONTH, 1);
			if (startMonth == 0 && startCal.get(Calendar.WEEK_OF_YEAR) > 1) {
				int[] newValues = getValueFromCal(startCal, levels);
				TimeMember newMember = new TimeMember(newValues, levels);
				list.add(newMember);
				startCal.add(Calendar.WEEK_OF_YEAR, 1);
			}
			startWeek = startCal.get(Calendar.WEEK_OF_YEAR);
		} else if (type.equals("monthToDate")) {
			startMonth = cal.get(Calendar.MONTH);
			startCal.set(Calendar.MONTH, startMonth);
			startCal.set(Calendar.DAY_OF_MONTH, 1);
			if (startMonth == 0 && startCal.get(Calendar.WEEK_OF_YEAR) > 1) {
				int[] newValues = getValueFromCal(startCal, levels);
				TimeMember newMember = new TimeMember(newValues, levels);
				list.add(newMember);
				startCal.add(Calendar.WEEK_OF_YEAR, 1);
			}
			startWeek = startCal.get(Calendar.WEEK_OF_YEAR);
		}

		TimeMember newMember = null;
		for (int i = startWeek; i <= endWeek; i++) {
			int[] newValues = getValueFromCal(startCal, levels);
			newMember = new TimeMember(newValues, levels);
			list.add(newMember);
			if (i != startWeek && isAddExtraWeek(type, startCal)) {
				addExtraWeek(list, startCal, newMember, levels);
			}

			startCal.add(Calendar.WEEK_OF_YEAR, 1);
			startCal.set(Calendar.DAY_OF_WEEK, 1);
		}

	}

	/**
	 * If the week across month,quarter,year, this week will be divided into 2
	 * parts, for example, 2011.11month,week of month 5, this week will be divided
	 * into 2011,11,week of month 5 and 2011,12,week of month 0. So when we do year
	 * to date(level week), when visit the 2011.11month,week of month 5, we should
	 * also add the extra week, 2011,12,week of month 0
	 */
	protected void addExtraWeek(List<TimeMember> timeMemberList, Calendar cal, TimeMember srcMember, String[] levels) {
		int weekStart = 1;
		int week = 1;
		int weekEnd = 1;
		int[] newValues = null;
		TimeMember newMember = null;
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		week = cal.get(Calendar.WEEK_OF_MONTH);
		cal.set(Calendar.DAY_OF_WEEK, 1);
		weekStart = cal.get(Calendar.WEEK_OF_MONTH);

		// if the weekofmonth in a week is not the same, this week must across month,
		// may need add extra week.
		if (weekStart != week) {
			newValues = getValueFromCal(cal, levels);
			newMember = new TimeMember(newValues, levels);

		} else {
			cal.set(Calendar.DAY_OF_WEEK, 7);
			weekEnd = cal.get(Calendar.WEEK_OF_MONTH);
			if (weekEnd != week) {
				newValues = getValueFromCal(cal, levels);
				newMember = new TimeMember(newValues, levels);
			}
		}
		if (newMember != null && !newMember.equals(srcMember)) {
			timeMemberList.add(newMember);
		}
		cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);

	}

	private boolean isAddExtraWeek(String type, Calendar cal) {
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
		if (type.equals("monthToDate")) {
			return false;
		}

		else if (type.equals("quarterToDate")) {
			cal.set(Calendar.DAY_OF_WEEK, 7);
			int quarterEnd = cal.get(Calendar.MONTH) / 3 + 1;
			cal.set(Calendar.DAY_OF_WEEK, 1);
			int quarterStart = cal.get(Calendar.MONTH) / 3 + 1;
			if (quarterEnd != quarterStart) {
				return false;
			}
		}

		else if (type.equals("yearToDate")) {
			cal.set(Calendar.DAY_OF_WEEK, 7);
			int yearEnd = cal.get(Calendar.YEAR);
			cal.set(Calendar.DAY_OF_WEEK, 1);
			int yearStart = cal.get(Calendar.YEAR);
			if (yearStart != yearEnd) {
				return false;
			}
		}
		cal.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		return true;
	}

	public void setIsCurrent(boolean isCurrent) {
		this.isCurrent = isCurrent;
	}

	public void setReferenceDate(ReferenceDate referenceDate) {
		this.referenceDate = referenceDate;
	}

	public ReferenceDate getReferenceDate() {
		return this.referenceDate;
	}
}
