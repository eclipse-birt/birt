/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.adapter.api.timeFunction;

import org.eclipse.birt.report.data.adapter.i18n.Message;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;

public interface IBuildInBaseTimeFunction
{

	public static final String CURRENT_QUARTER = "CURRENT QUARTER";
	public static final String CURRENT_MONTH = "CURRENT MONTH";
	public static final String TRAILING_30_DAYS = "TRAILING 30 DAYS";
	public static final String TRAILING_60_DAYS = "TRAILING 60 DAYS";
	public static final String TRAILING_90_DAYS = "TRAILING 90 DAYS";
	public static final String TRAILING_12_MONTHS = "TRAILING 12 MONTHS";
	public static final String YEAR_TO_DATE = "YEAR TO DATE";
	public static final String QUARTER_TO_DATE = "QUARTER TO DATE";
	public static final String MONTH_TO_DATE = "MONTH TO DATE";
	public static final String CURRENT_YEAR = "CURRENT YEAR";
	public static final String WEEK_TO_DATE = "WEEK TO DATE";

	public static final String PREVIOUS_MONTH = "PREVIOUS MONTH";
	public static final String PREVIOUS_QUARTER = "PREVIOUS QUARTER";
	public static final String PREVIOUS_YEAR = "PREVIOUS YEAR";
	public static final String MONTH_TO_DATE_LAST_YEAR = "MONTH TO DATE LAST YEAR";
	public static final String QUARTER_TO_DATE_LAST_YEAR = "QUARTER TO DATE LAST YEAR";
	public static final String PREVIOUS_MONTH_TO_DATE = "PREVIOUS MONTH TO DATE";
	public static final String PREVIOUS_QUARTER_TO_DATE = "PREVIOUS QUARTER TO DATE";
	public static final String PREVIOUS_YEAR_TO_DATE = "PREVIOUS YEAR TO DATE";
	public static final String CURRENT_PERIOD_FROM_N_PERIOD_AGO = "CURRENT PERIOD FROM N PERIODS AGO";
	public static final String PERIOD_TO_DATE_FROM_N_PERIOD_AGO = "PERIOD TO DATE FROM N PERIODS AGO";
	public static final String TRAILING_N_PERIOD_FROM_N_PERIOD_AGO = "TRAILING N PERIODS FROM N PERIODS AGO";
	public static final String NEXT_N_PERIODS = "NEXT_N_PERIODS";
	

	public static BaseTimeFunction CURRENT_QUARTER_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.CURRENT_QUARTER,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_CURRENT_QUARTER ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_CURRENT_QUARTER_DES ) );
	public static BaseTimeFunction CURRENT_MONTH_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.CURRENT_MONTH,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_CURRENT_MONTH ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_CURRENT_MONTH_DES ) );
	public static BaseTimeFunction PREVIOUS_MONTH_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.PREVIOUS_MONTH,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_MONTH ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_MONTH_DES ) );
	public static BaseTimeFunction PREVIOUS_MONTH_TO_DATE_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.PREVIOUS_MONTH_TO_DATE,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_MONTH_TO_DATE ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_MONTH_TO_DATE_DES ) );
	public static BaseTimeFunction PREVIOUS_QUARTER_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.PREVIOUS_QUARTER,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_QUARTER ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_QUARTER_DES ) );
	public static BaseTimeFunction PREVIOUS_QUARTER_TO_DATE_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.PREVIOUS_QUARTER_TO_DATE,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_QUARTER_TO_DATE ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_QUARTER_TO_DATE_DES ) );
	public static BaseTimeFunction PREVIOUS_YEAR_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.PREVIOUS_YEAR,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_YEAR ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_YEAR_DES ) );
	public static BaseTimeFunction TRAILING_30_DAYS_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.TRAILING_30_DAYS,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_30_DAYS ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_30_DAYS_DES ) );
	public static BaseTimeFunction TRAILING_60_DAYS_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.TRAILING_60_DAYS,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_60_DAYS ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_60_DAYS_DES ) );
	public static BaseTimeFunction TRAILING_90_DAYS_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.TRAILING_90_DAYS,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_90_DAYS ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_90_DAYS_DES ) );
	public static BaseTimeFunction TRAILING_12_MONTHS_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.TRAILING_12_MONTHS,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_12_MONTHS ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_12_MONTHS_DES ) );
	public static BaseTimeFunction YEAR_TO_DATE_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.YEAR_TO_DATE,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_YEAR_TO_DATE ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_YEAR_TO_DATE_DES ) );
	public static BaseTimeFunction QUARTER_TO_DATE_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.QUARTER_TO_DATE,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_QUARTER_TO_DATE ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_QUARTER_TO_DATE_DES ) );
	public static BaseTimeFunction MONTH_TO_DATE_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.MONTH_TO_DATE,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_MONTH_TO_DATE ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_MONTH_TO_DATE_DES ) );
	public static BaseTimeFunction PREVIOUS_YEAR_TO_DATE_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.PREVIOUS_YEAR_TO_DATE,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_YEAR_TO_DATE ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_PREVIOUS_YEAR_TO_DATE_DES ) );
	public static BaseTimeFunction MONTH_TO_DATE_LAST_YEAR_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.MONTH_TO_DATE_LAST_YEAR,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_MONTH_TO_DATE_LAST_YEAR ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_MONTH_TO_DATE_LAST_YEAR_DES ) );
	public static BaseTimeFunction QUARTER_TO_DATE_LAST_YEAR_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.QUARTER_TO_DATE_LAST_YEAR,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_QUARTER_TO_DATE_LAST_YEAR ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_QUARTER_TO_DATE_LAST_YEAR_DES ) );
	public static BaseTimeFunction CURRENT_YEAR_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.CURRENT_YEAR,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_CURRENT_YEAR ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_CURRENT_YEAR_DES ) );
	public static BaseTimeFunction WEEK_TO_DATE_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.WEEK_TO_DATE,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_WEEK_TO_DATE ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_WEEK_TO_DATE_DES ) );

	// complex time function
	public static BaseTimeFunction CURRENT_PERIOD_FROM_N_PERIOD_AGO_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.CURRENT_PERIOD_FROM_N_PERIOD_AGO,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_CURRENT_PERIOD_FROM_N_PERIOD_AGO ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_CURRENT_PERIOD_FROM_N_PERIOD_AGO_DES ) );
	public static BaseTimeFunction PERIOD_TO_DATE_FROM_N_PERIOD_AGO_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.PERIOD_TO_DATE_FROM_N_PERIOD_AGO,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_PERIOD_TO_DATE_FROM_N_PERIOD_AGO ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_PERIOD_TO_DATE_FROM_N_PERIOD_AGO_DES ) );
	public static BaseTimeFunction TRAILING_N_PERIOD_FROM_N_PERIOD_AGO_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.TRAILING_N_PERIOD_FROM_N_PERIOD_AGO,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_N_PERIOD_FROM_N_PERIOD_AGO ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_TRAILING_N_PERIOD_FROM_N_PERIOD_AGO_DES ) );
	public static BaseTimeFunction NEXT_N_PERIODS_FUNCTION = new BaseTimeFunction( IBuildInBaseTimeFunction.NEXT_N_PERIODS,
			Message.getMessage( ResourceConstants.TIMEFUNCITON_NEXT_N_PERIODS ),
			Message.getMessage( ResourceConstants.TIMEFUNCITON_NEXT_N_PERIODS_DES ) );
}
