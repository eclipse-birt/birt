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
package org.eclipse.birt.chart.internal.datafeed;

import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.util.CDateTime;

import com.ibm.icu.util.Calendar;

/**
 * The class defines some static methods for grouping.
 *
 * @since 2.3
 */
public final class GroupingUtil {

	/**
	 * Format object into specified data format.
	 *
	 * @param obj              object will be formated as date time.
	 * @param groupingUnitType the grouping unit type.
	 * @return instance of <code>CDateTime</code>.
	 * @since 2.3
	 */
	static CDateTime formatGroupedDateTime(Object obj, GroupingUnitType groupingUnitType) {
		int cunit = groupingUnit2CDateUnit(groupingUnitType);
		// ASSIGN IT TO THE FIRST TYPLE'S GROUP EXPR VALUE
		CDateTime date = null;
		if (obj instanceof CDateTime) {
			date = (CDateTime) obj;
		} else {
			// set as the smallest Date.
			date = new CDateTime(0);
		}

		date.clearBelow(cunit);
		return date;
	}

	/**
	 * Convert GroupingUnit type to CDateUnit type.
	 *
	 * @param groupingUnitType the GroupingUnit type.
	 * @return CDateUnit type of integer.
	 * @since 2.3, it is merged from <code>DataProcessor</code>, make the method to
	 *        be a static usage.
	 */
	public static int groupingUnit2CDateUnit(GroupingUnitType groupingUnitType) {
		if (groupingUnitType != null) {
			switch (groupingUnitType.getValue()) {
			case GroupingUnitType.SECONDS:
				return Calendar.SECOND;
			case GroupingUnitType.MINUTES:
				return Calendar.MINUTE;
			case GroupingUnitType.HOURS:
				return Calendar.HOUR_OF_DAY;
			case GroupingUnitType.DAYS:
			case GroupingUnitType.DAY_OF_MONTH:
				return Calendar.DAY_OF_MONTH;
			case GroupingUnitType.DAY_OF_QUARTER:
				return CDateTime.DAY_OF_QUARTER;
			case GroupingUnitType.DAY_OF_WEEK:
				return Calendar.DAY_OF_WEEK;
			case GroupingUnitType.DAY_OF_YEAR:
				return Calendar.DAY_OF_YEAR;
			case GroupingUnitType.WEEKS:
			case GroupingUnitType.WEEK_OF_MONTH:
				return Calendar.WEEK_OF_MONTH;
			case GroupingUnitType.WEEK_OF_YEAR:
				return Calendar.WEEK_OF_YEAR;
			case GroupingUnitType.MONTHS:
				return Calendar.MONTH;
			case GroupingUnitType.QUARTERS:
				return CDateTime.QUARTER;
			case GroupingUnitType.YEARS:
				return Calendar.YEAR;
			case GroupingUnitType.WEEK_OF_QUARTER:
				return CDateTime.WEEK_OF_QUARTER;
			}
		}

		return Calendar.MILLISECOND;
	}

	/**
	 * Check if specified two strings are in same group with grouping setting.
	 *
	 * @param baseValue
	 * @param baseReference
	 * @param groupingUnit
	 * @param groupingInterval
	 * @return
	 * @since BIRT 2.3
	 */
	public static boolean isMatchedGroupingString(String baseValue, String baseReference, GroupingUnitType groupingUnit,
			int groupingInterval) {
		if (baseValue == null && baseReference == null) {
			return true;
		}

		if (baseValue == null || baseReference == null) {
			return false;
		}

		if (groupingUnit == GroupingUnitType.STRING_PREFIX_LITERAL) {
			if (groupingInterval <= 0) {
				// The "0" means all data should be in one group.
				return true;
			}

			if (baseValue.length() < groupingInterval || baseReference.length() < groupingInterval) {
				return baseValue.equals(baseReference);
			}

			return baseValue.substring(0, groupingInterval).equals(baseReference.substring(0, groupingInterval));

		} else if (groupingUnit == GroupingUnitType.STRING_LITERAL) {
			return baseValue.equals(baseReference);
		}

		return baseValue.equals(baseReference);
	}

	/**
	 * Returns grouped string of specified string on grouping setting.
	 *
	 * @param stringValue
	 * @param groupingUnit
	 * @param groupingInterval
	 * @return
	 * @since BIRT 2.3
	 */
	public static Object getGroupedString(String stringValue, GroupingUnitType groupingUnit, int groupingInterval) {
		if (stringValue == null) {
			return stringValue;
		}

		if (groupingUnit == GroupingUnitType.STRING_PREFIX_LITERAL) {
			if (groupingInterval <= 0) {
				// Always return empty string to make all data in one group.
				return ""; //$NON-NLS-1$
			}

			if (stringValue.length() < groupingInterval) {
				return stringValue;
			}
			return stringValue.substring(0, groupingInterval);
		}

		return stringValue;
	}
}
