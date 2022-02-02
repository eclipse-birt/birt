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

package org.eclipse.birt.report.data.adapter.group;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.report.data.adapter.api.AdapterException;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * A factory of group calculator.
 */
public class GroupCalculatorFactory {
	/**
	 * 
	 * @param interval
	 * @param dataType
	 * @param intervalStart
	 * @param intervalRange
	 * @return
	 * @throws DataException
	 */
	public static ICalculator getGroupCalculator(int interval, int dataType, Object intervalStart, double intervalRange,
			ULocale locale, TimeZone timeZone) throws AdapterException {
		if (!isValidInterval(interval, dataType)) {
			return null;
		}
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
				return new NumericGroupCalculator(intervalStart, intervalRange);

			case IGroupDefinition.STRING_PREFIX_INTERVAL:
				return new StringGroupCalculator(intervalStart, intervalRange);
			default:
				throw new DataException(ResourceConstants.BAD_GROUP_INTERVAL_INVALID);
			}
		} catch (BirtException be) {
			throw new AdapterException(be.getLocalizedMessage(), be);
		}
	}

	/**
	 * 
	 * @param interval
	 * @param dataType
	 * @return
	 * @throws DataException
	 */
	private static boolean isValidInterval(int interval, int dataType) throws AdapterException {
		if (dataType == DataType.ANY_TYPE || dataType == DataType.UNKNOWN_TYPE) {
			return true;
		}

		switch (interval) {
		case IGroupDefinition.NO_INTERVAL:
			return true;
		case IGroupDefinition.NUMERIC_INTERVAL:
			if (isNumber(dataType))
				return true;
			else
				throw new AdapterException("The group interval is invalid");
		case IGroupDefinition.STRING_PREFIX_INTERVAL:
			if (isString(dataType))
				return true;
			else
				throw new AdapterException("The group interval is invalid");
		default:
			if (isDate(dataType))
				return true;
			else
				throw new AdapterException("The group interval is invalid");
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
	private static boolean isDate(int dataType) {
		return (dataType == DataType.DATE_TYPE || dataType == DataType.SQL_DATE_TYPE
				|| dataType == DataType.SQL_TIME_TYPE);
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
