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

public interface IBuildInBaseTimeFunction {
	public static final String CURRENT_QUARTER = "CURRENT QUARTER";
	public static final String CURRENT_MONTH = "CURRENT MONTH";
	public static final String TRAILING_30_DAYS = "TRAILING 30 DAYS";
	public static final String TRAILING_60_DAYS = "TRAILING 60 DAYS";
	public static final String TRAILING_90_DAYS = "TRAILING 90 DAYS";
	public static final String TRAILING_120_DAYS = "TRAILING 120 DAYS";
	public static final String TRAILING_12_MONTHS = "TRAILING 12 MONTHS";
	public static final String YEAR_TO_DATE = "YEAR TO DATE";
	public static final String QUARTER_TO_DATE = "QUARTER TO DATE";
	public static final String MONTH_TO_DATE = "MONTH TO DATE";
	public static final String CURRENT_YEAR = "CURRENT YEAR";
	public static final String WEEK_TO_DATE = "WEEK TO DATE";

	public static final String PREVIOUS_MONTH = "PREVIOUS MONTH";
	public static final String PREVIOUS_QUARTER = "PREVIOUS QUARTER";
	public static final String PREVIOUS_YEAR = "PREVIOUS YEAR";
	public static final String WEEK_TO_DATE_LAST_YEAR = "WEEK TO DATE LAST YEAR";
	public static final String MONTH_TO_DATE_LAST_YEAR = "MONTH TO DATE LAST YEAR";
	public static final String QUARTER_TO_DATE_LAST_YEAR = "QUARTER TO DATE LAST YEAR";
	public static final String PREVIOUS_WEEK_TO_DATE = "PREVIOUS WEEK TO DATE";
	public static final String PREVIOUS_MONTH_TO_DATE = "PREVIOUS MONTH TO DATE";
	public static final String PREVIOUS_QUARTER_TO_DATE = "PREVIOUS QUARTER TO DATE";
	public static final String PREVIOUS_YEAR_TO_DATE = "PREVIOUS YEAR TO DATE";
	public static final String CURRENT_PERIOD_FROM_N_PERIOD_AGO = "CURRENT PERIOD FROM N PERIODS AGO";
	public static final String PERIOD_TO_DATE_FROM_N_PERIOD_AGO = "PERIOD TO DATE FROM N PERIODS AGO";
	public static final String TRAILING_N_MONTHS = "TRAILING N MONTHS";
	public static final String TRAILING_N_DAYS = "TRAILING N DAYS";
	public static final String TRAILING_N_PERIOD_FROM_N_PERIOD_AGO = "TRAILING N PERIODS FROM N PERIODS AGO";
	public static final String NEXT_N_PERIODS = "NEXT_N_PERIODS";
}
