/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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
package org.eclipse.birt.report.data.adapter.api.timeFunction;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.data.adapter.i18n.Message;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;

import com.ibm.icu.util.ULocale;

public class TimeFunctionHandle {
	private static TimeFunctionHandle instance;
	private static Map<ULocale, TimeFunctionHandle> handleCache;

	private Map<String, BaseTimeFunction> baseTimeFunctionMap;

	public static TimeFunctionHandle getInstance(ULocale locale) {
		synchronized (TimeFunctionHandle.class) {
			if (handleCache == null) {
				handleCache = new HashMap<>();
				instance = new TimeFunctionHandle(locale);
				handleCache.put(locale, instance);
			} else if (handleCache.containsKey(locale)) {
				instance = handleCache.get(locale);
			} else {
				instance = new TimeFunctionHandle(locale);
				handleCache.put(locale, instance);
			}
		}

		return instance;
	}

	private TimeFunctionHandle(ULocale local) {
		baseTimeFunctionMap = new HashMap<>();
		buildTimeFunctionMap(local);
	}

	private void buildTimeFunctionMap(ULocale local) {
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.CURRENT_QUARTER,
				new BaseTimeFunction(IBuildInBaseTimeFunction.CURRENT_QUARTER,
						Message.getMessage(ResourceConstants.TIMEFUNCITON_CURRENT_QUARTER, local),
						Message.getMessage(ResourceConstants.TIMEFUNCITON_CURRENT_QUARTER_DES, local)));
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.CURRENT_MONTH,
				new BaseTimeFunction(IBuildInBaseTimeFunction.CURRENT_MONTH,
						Message.getMessage(ResourceConstants.TIMEFUNCITON_CURRENT_MONTH, local),
						Message.getMessage(ResourceConstants.TIMEFUNCITON_CURRENT_MONTH_DES, local)));
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.PREVIOUS_MONTH,
				new BaseTimeFunction(IBuildInBaseTimeFunction.PREVIOUS_MONTH,
						Message.getMessage(ResourceConstants.TIMEFUNCITON_PREVIOUS_MONTH, local),
						Message.getMessage(ResourceConstants.TIMEFUNCITON_PREVIOUS_MONTH_DES, local)));
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.PREVIOUS_WEEK_TO_DATE,
				new BaseTimeFunction(IBuildInBaseTimeFunction.PREVIOUS_WEEK_TO_DATE,
						Message.getMessage(ResourceConstants.TIMEFUNCITON_PREVIOUS_WEEK_TO_DATE, local),
						Message.getMessage(ResourceConstants.TIMEFUNCITON_PREVIOUS_WEEK_TO_DATE_DES, local)));
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.PREVIOUS_MONTH_TO_DATE,
				new BaseTimeFunction(IBuildInBaseTimeFunction.PREVIOUS_MONTH_TO_DATE,
						Message.getMessage(ResourceConstants.TIMEFUNCITON_PREVIOUS_MONTH_TO_DATE, local),
						Message.getMessage(ResourceConstants.TIMEFUNCITON_PREVIOUS_MONTH_TO_DATE_DES, local)));
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.PREVIOUS_QUARTER,
				new BaseTimeFunction(IBuildInBaseTimeFunction.PREVIOUS_QUARTER,
						Message.getMessage(ResourceConstants.TIMEFUNCITON_PREVIOUS_QUARTER, local),
						Message.getMessage(ResourceConstants.TIMEFUNCITON_PREVIOUS_QUARTER_DES, local)));
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.PREVIOUS_QUARTER_TO_DATE,
				new BaseTimeFunction(IBuildInBaseTimeFunction.PREVIOUS_QUARTER_TO_DATE,
						Message.getMessage(ResourceConstants.TIMEFUNCITON_PREVIOUS_QUARTER_TO_DATE, local),
						Message.getMessage(ResourceConstants.TIMEFUNCITON_PREVIOUS_QUARTER_TO_DATE_DES, local)));
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.PREVIOUS_YEAR,
				new BaseTimeFunction(IBuildInBaseTimeFunction.PREVIOUS_YEAR,
						Message.getMessage(ResourceConstants.TIMEFUNCITON_PREVIOUS_YEAR, local),
						Message.getMessage(ResourceConstants.TIMEFUNCITON_PREVIOUS_YEAR_DES, local)));
//		baseTimeFunctionMap.put( IBuildInBaseTimeFunction.TRAILING_30_DAYS,
//				new BaseTimeFunction( IBuildInBaseTimeFunction.TRAILING_30_DAYS,
//						Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_30_DAYS,
//								local ),
//						Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_30_DAYS_DES,
//								local ) ) );
//		baseTimeFunctionMap.put( IBuildInBaseTimeFunction.TRAILING_60_DAYS,
//				new BaseTimeFunction( IBuildInBaseTimeFunction.TRAILING_60_DAYS,
//						Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_60_DAYS,
//								local ),
//						Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_60_DAYS_DES,
//								local ) ) );
//		baseTimeFunctionMap.put( IBuildInBaseTimeFunction.TRAILING_90_DAYS,
//				new BaseTimeFunction( IBuildInBaseTimeFunction.TRAILING_90_DAYS,
//						Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_90_DAYS,
//								local ),
//						Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_90_DAYS_DES,
//								local ) ) );
//		baseTimeFunctionMap.put( IBuildInBaseTimeFunction.TRAILING_120_DAYS,
//				new BaseTimeFunction( IBuildInBaseTimeFunction.TRAILING_120_DAYS,
//						Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_120_DAYS,
//								local ),
//						Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_120_DAYS_DES,
//								local ) ) );
//		baseTimeFunctionMap.put( IBuildInBaseTimeFunction.TRAILING_12_MONTHS,
//				new BaseTimeFunction( IBuildInBaseTimeFunction.TRAILING_12_MONTHS,
//						Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_12_MONTHS,
//								local ),
//						Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_12_MONTHS_DES,
//								local ) ) );
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.YEAR_TO_DATE,
				new BaseTimeFunction(IBuildInBaseTimeFunction.YEAR_TO_DATE,
						Message.getMessage(ResourceConstants.TIMEFUNCITON_YEAR_TO_DATE, local),
						Message.getMessage(ResourceConstants.TIMEFUNCITON_YEAR_TO_DATE_DES, local)));
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.QUARTER_TO_DATE,
				new BaseTimeFunction(IBuildInBaseTimeFunction.QUARTER_TO_DATE,
						Message.getMessage(ResourceConstants.TIMEFUNCITON_QUARTER_TO_DATE, local),
						Message.getMessage(ResourceConstants.TIMEFUNCITON_QUARTER_TO_DATE_DES, local)));
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.MONTH_TO_DATE,
				new BaseTimeFunction(IBuildInBaseTimeFunction.MONTH_TO_DATE,
						Message.getMessage(ResourceConstants.TIMEFUNCITON_MONTH_TO_DATE, local),
						Message.getMessage(ResourceConstants.TIMEFUNCITON_MONTH_TO_DATE_DES, local)));
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.PREVIOUS_YEAR_TO_DATE,
				new BaseTimeFunction(IBuildInBaseTimeFunction.PREVIOUS_YEAR_TO_DATE,
						Message.getMessage(ResourceConstants.TIMEFUNCITON_PREVIOUS_YEAR_TO_DATE, local),
						Message.getMessage(ResourceConstants.TIMEFUNCITON_PREVIOUS_YEAR_TO_DATE_DES, local)));
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.WEEK_TO_DATE_LAST_YEAR,
				new BaseTimeFunction(IBuildInBaseTimeFunction.WEEK_TO_DATE_LAST_YEAR,
						Message.getMessage(ResourceConstants.TIMEFUNCITON_WEEK_TO_DATE_LAST_YEAR, local),
						Message.getMessage(ResourceConstants.TIMEFUNCITON_WEEK_TO_DATE_LAST_YEAR_DES, local)));
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.MONTH_TO_DATE_LAST_YEAR,
				new BaseTimeFunction(IBuildInBaseTimeFunction.MONTH_TO_DATE_LAST_YEAR,
						Message.getMessage(ResourceConstants.TIMEFUNCITON_MONTH_TO_DATE_LAST_YEAR, local),
						Message.getMessage(ResourceConstants.TIMEFUNCITON_MONTH_TO_DATE_LAST_YEAR_DES, local)));
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.QUARTER_TO_DATE_LAST_YEAR,
				new BaseTimeFunction(IBuildInBaseTimeFunction.QUARTER_TO_DATE_LAST_YEAR,
						Message.getMessage(ResourceConstants.TIMEFUNCITON_QUARTER_TO_DATE_LAST_YEAR, local),
						Message.getMessage(ResourceConstants.TIMEFUNCITON_QUARTER_TO_DATE_LAST_YEAR_DES, local)));
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.CURRENT_YEAR,
				new BaseTimeFunction(IBuildInBaseTimeFunction.CURRENT_YEAR,
						Message.getMessage(ResourceConstants.TIMEFUNCITON_CURRENT_YEAR, local),
						Message.getMessage(ResourceConstants.TIMEFUNCITON_CURRENT_YEAR_DES, local)));
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.WEEK_TO_DATE,
				new BaseTimeFunction(IBuildInBaseTimeFunction.WEEK_TO_DATE,
						Message.getMessage(ResourceConstants.TIMEFUNCITON_WEEK_TO_DATE, local),
						Message.getMessage(ResourceConstants.TIMEFUNCITON_WEEK_TO_DATE_DES, local)));

		// complex time function
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO, new BaseTimeFunction(
				IBuildInBaseTimeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO,
				Message.getMessage(ResourceConstants.TIMEFUNCITON_CURRENT_PERIOD_FROM_N_PERIOD_AGO, local),
				Message.getMessage(ResourceConstants.TIMEFUNCITON_CURRENT_PERIOD_FROM_N_PERIOD_AGO_DES, local)));
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO, new BaseTimeFunction(
				IBuildInBaseTimeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO,
				Message.getMessage(ResourceConstants.TIMEFUNCITON_PERIOD_TO_DATE_FROM_N_PERIOD_AGO, local),
				Message.getMessage(ResourceConstants.TIMEFUNCITON_PERIOD_TO_DATE_FROM_N_PERIOD_AGO_DES, local)));
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.TRAILING_N_MONTHS,
				new BaseTimeFunction(IBuildInBaseTimeFunction.TRAILING_N_MONTHS,
						Message.getMessage(ResourceConstants.TIMEFUNCITON_TRAILING_N_MONTHS, local),
						Message.getMessage(ResourceConstants.TIMEFUNCITON_TRAILING_N_MONTHS, local)));
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.TRAILING_N_DAYS,
				new BaseTimeFunction(IBuildInBaseTimeFunction.TRAILING_N_DAYS,
						Message.getMessage(ResourceConstants.TIMEFUNCITON_TRAILING_N_DAYS, local),
						Message.getMessage(ResourceConstants.TIMEFUNCITON_TRAILING_N_DAYS, local)));
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO, new BaseTimeFunction(
				IBuildInBaseTimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO,
				Message.getMessage(ResourceConstants.TIMEFUNCITON_TRAILING_N_PERIOD_FROM_N_PERIOD_AGO, local),
				Message.getMessage(ResourceConstants.TIMEFUNCITON_TRAILING_N_PERIOD_FROM_N_PERIOD_AGO_DES, local)));
		baseTimeFunctionMap.put(IBuildInBaseTimeFunction.NEXT_N_PERIODS,
				new BaseTimeFunction(IBuildInBaseTimeFunction.NEXT_N_PERIODS,
						Message.getMessage(ResourceConstants.TIMEFUNCITON_NEXT_N_PERIODS, local),
						Message.getMessage(ResourceConstants.TIMEFUNCITON_NEXT_N_PERIODS_DES, local)));
	}

	public BaseTimeFunction getFunction(String functionName) {
		return baseTimeFunctionMap.get(functionName);
	}
}
