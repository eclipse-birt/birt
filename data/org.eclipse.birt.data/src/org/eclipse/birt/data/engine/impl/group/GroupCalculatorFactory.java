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

package org.eclipse.birt.data.engine.impl.group;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * A factory of group calculator.
 */
public class GroupCalculatorFactory {
	/**
	 *
	 * @param interval
	 * @param intervalStart
	 * @param intervalRange
	 * @return
	 * @throws DataException
	 */
	public static ICalculator getGroupCalculator(int interval, Object intervalStart, double intervalRange, int dataType,
			ULocale locale, TimeZone timeZone) throws DataException {
		validateInterval(interval, dataType);
		validateIntervalRange(intervalRange);
		try {
			switch (interval) {
			case IGroupDefinition.YEAR_INTERVAL:
				return new YearGroupCalculator(intervalStart, intervalRange, locale, timeZone);

			case IGroupDefinition.MONTH_INTERVAL:
				return new MonthGroupCalculator(intervalStart, intervalRange, locale, timeZone);

			case IGroupDefinition.QUARTER_INTERVAL:
				return new QuarterGroupCalculator(intervalStart, intervalRange, locale, timeZone);

			case IGroupDefinition.WEEK_INTERVAL:
				return new WeekGroupCalculator(intervalStart, intervalRange, locale, timeZone);

			case IGroupDefinition.DAY_INTERVAL:
				return new DayGroupCalculator(intervalStart, intervalRange, locale, timeZone);

			case IGroupDefinition.HOUR_INTERVAL:
				return new HourGroupCalculator(intervalStart, intervalRange, locale, timeZone);

			case IGroupDefinition.MINUTE_INTERVAL:
				return new MinuteGroupCalculator(intervalStart, intervalRange, locale, timeZone);

			case IGroupDefinition.SECOND_INTERVAL:
				return new SecondGroupCalculator(intervalStart, intervalRange, locale, timeZone);

			case IGroupDefinition.NUMERIC_INTERVAL:
				if (dataType == DataType.DECIMAL_TYPE) {
					return new DecimalGroupCalculator(intervalStart, intervalRange);
				} else {
					return new NumericGroupCalculator(intervalStart, intervalRange);
				}

			case IGroupDefinition.STRING_PREFIX_INTERVAL:
				return new StringGroupCalculator(intervalStart, intervalRange);
			default:
				throw new DataException(ResourceConstants.BAD_GROUP_INTERVAL_INVALID);
			}
		} catch (BirtException be) {
			throw DataException.wrap(be);
		}
	}

	/**
	 *
	 * @param interval
	 * @param dataType
	 * @return
	 * @throws DataException
	 */
	private static void validateInterval(int interval, int dataType) throws DataException {
		if (interval != IGroupDefinition.NO_INTERVAL && interval != IGroupDefinition.NUMERIC_INTERVAL
				&& interval != IGroupDefinition.STRING_PREFIX_INTERVAL && interval != IGroupDefinition.SECOND_INTERVAL
				&& interval != IGroupDefinition.MINUTE_INTERVAL && interval != IGroupDefinition.HOUR_INTERVAL
				&& interval != IGroupDefinition.DAY_INTERVAL && interval != IGroupDefinition.WEEK_INTERVAL
				&& interval != IGroupDefinition.MONTH_INTERVAL && interval != IGroupDefinition.QUARTER_INTERVAL
				&& interval != IGroupDefinition.YEAR_INTERVAL) {
			throw new DataException(ResourceConstants.BAD_GROUP_INTERVAL_INVALID);
		}
		if (dataType == DataType.ANY_TYPE || dataType == DataType.UNKNOWN_TYPE) {
			return;
		}

		switch (interval) {
		case IGroupDefinition.NO_INTERVAL:
			return;
		case IGroupDefinition.NUMERIC_INTERVAL:
			if (isNumber(dataType)) {
				return;
			} else {
				throw new DataException(ResourceConstants.BAD_GROUP_INTERVAL_TYPE,
						new Object[] { "numeric", DataType.getName(dataType) });
			}
		case IGroupDefinition.STRING_PREFIX_INTERVAL:
			if (isString(dataType)) {
				return;
			} else {
				throw new DataException(ResourceConstants.BAD_GROUP_INTERVAL_TYPE,
						new Object[] { "string prefix", DataType.getName(dataType) });
			}
		default:
			if (canBeConvertToDate(dataType)) {
			} else {
				throw new DataException(ResourceConstants.BAD_GROUP_INTERVAL_TYPE,
						new Object[] { "date", DataType.getName(dataType) });
			}
		}
	}

	/**
	 *
	 * @param interval
	 * @param dataType
	 * @return
	 * @throws DataException
	 */
	private static void validateIntervalRange(double intervalRange) throws DataException {
		if (intervalRange < 0) {
			throw new DataException(ResourceConstants.BAD_GROUP_INTERVAL_RANGE, new Double(intervalRange));
		}
	}

	/**
	 *
	 * @param dataType
	 * @return
	 */
	private static boolean isNumber(int dataType) {
		return (dataType == DataType.DECIMAL_TYPE || dataType == DataType.DOUBLE_TYPE
				|| dataType == DataType.INTEGER_TYPE);
	}

	/**
	 *
	 * @param dataType
	 * @return
	 */
	private static boolean canBeConvertToDate(int dataType) {
		return (dataType == DataType.DATE_TYPE || dataType == DataType.SQL_DATE_TYPE
				|| dataType == DataType.SQL_TIME_TYPE || dataType == DataType.STRING_TYPE);
	}

	/**
	 *
	 * @param dataType
	 * @return
	 */
	private static boolean isString(int dataType) {
		return (dataType == DataType.STRING_TYPE);
	}
}
