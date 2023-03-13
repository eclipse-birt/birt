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
package org.eclipse.birt.data.engine.olap.data.api.cube;

import java.util.Date;

import org.eclipse.birt.core.script.functionservice.IScriptFunctionContext;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;

public class TimeDimensionUtil {
	public static final String YEAR = "year";
	public static final String MONTH = "month";
	public static final String WEEK_OF_YEAR = "week_of_year";
	public static final String WEEK_OF_MONTH = "week_of_month";
	public static final String DAY_OF_MONTH = "day";
	public static final String DAY_OF_YEAR = "day_of_year";
	public static final String DAY_OF_WEEK = "day_of_week";
	public static final String HOUR = "hour";
	public static final String HOUR_OF_DAY = "hour_of_day";
	public static final String MINUTE = "minute";
	public static final String SECOND = "second";
	public static final String MILLISECOND = "millisecond";

	private static TimeZone timeZone = null;
	private static IScriptFunctionContext scriptContext = null;

	public static void setContext(IScriptFunctionContext context) {
		scriptContext = context;
		if (scriptContext != null) {
			timeZone = (TimeZone) scriptContext
					.findProperty(org.eclipse.birt.core.script.functionservice.IScriptFunctionContext.TIMEZONE);
		}
		if (timeZone == null) {
			timeZone = TimeZone.getDefault();
		}
	}

	public static int getFieldIndex(String fieldName) {
		if (fieldName.equals(YEAR)) {
			return Calendar.YEAR;
		} else if (fieldName.equals(MONTH)) {
			return Calendar.MONTH;
		} else if (fieldName.equals(WEEK_OF_YEAR)) {
			return Calendar.WEEK_OF_YEAR;
		} else if (fieldName.equals(WEEK_OF_MONTH)) {
			return Calendar.WEEK_OF_MONTH;
		} else if (fieldName.equals(DAY_OF_MONTH)) {
			return Calendar.DAY_OF_MONTH;
		} else if (fieldName.equals(DAY_OF_YEAR)) {
			return Calendar.DAY_OF_YEAR;
		} else if (fieldName.equals(DAY_OF_WEEK)) {
			return Calendar.DAY_OF_WEEK;
		} else if (fieldName.equals(HOUR)) {
			return Calendar.HOUR;
		} else if (fieldName.equals(HOUR_OF_DAY)) {
			return Calendar.HOUR_OF_DAY;
		} else if (fieldName.equals(MINUTE)) {
			return Calendar.MINUTE;
		} else if (fieldName.equals(SECOND)) {
			return Calendar.SECOND;
		} else if (fieldName.equals(MILLISECOND)) {
			return Calendar.MILLISECOND;
		}
		return -1;
	}

	public static String getFieldName(int fieldIndex) {
		switch (fieldIndex) {
		case Calendar.YEAR:
			return YEAR;
		case Calendar.MONTH:
			return MONTH;
		case Calendar.WEEK_OF_YEAR:
			return WEEK_OF_YEAR;
		case Calendar.WEEK_OF_MONTH:
			return WEEK_OF_MONTH;
		case Calendar.DAY_OF_MONTH:
			return DAY_OF_MONTH;
		case Calendar.DAY_OF_YEAR:
			return DAY_OF_YEAR;
		case Calendar.DAY_OF_WEEK:
			return DAY_OF_WEEK;
		case Calendar.HOUR:
			return HOUR;
		case Calendar.HOUR_OF_DAY:
			return HOUR_OF_DAY;
		case Calendar.MINUTE:
			return MINUTE;
		case Calendar.SECOND:
			return SECOND;
		case Calendar.MILLISECOND:
			return MILLISECOND;
		}
		return null;
	}

	public static Integer getFieldVaule(Date d, int fieldIndex) {
		Calendar c = getCalendar(d);

		return c.get(fieldIndex);
	}

	public static Integer getFieldVaule(Date d, String fieldName) {
		return getFieldVaule(d, getFieldIndex(fieldName));
	}

	/**
	 *
	 * @param d
	 * @return
	 */
	private static Calendar getCalendar(Date d) {
		Calendar c = null;
		if (timeZone == null) {
			c = Calendar.getInstance();
		} else {
			c = Calendar.getInstance(timeZone);
		}
		c.setTime(d);
		return c;
	}
}
