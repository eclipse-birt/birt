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
	String CURRENT_QUARTER = "CURRENT QUARTER";
	String CURRENT_MONTH = "CURRENT MONTH";
	String TRAILING_30_DAYS = "TRAILING 30 DAYS";
	String TRAILING_60_DAYS = "TRAILING 60 DAYS";
	String TRAILING_90_DAYS = "TRAILING 90 DAYS";
	String TRAILING_120_DAYS = "TRAILING 120 DAYS";
	String TRAILING_12_MONTHS = "TRAILING 12 MONTHS";
	String YEAR_TO_DATE = "YEAR TO DATE";
	String QUARTER_TO_DATE = "QUARTER TO DATE";
	String MONTH_TO_DATE = "MONTH TO DATE";
	String CURRENT_YEAR = "CURRENT YEAR";
	String WEEK_TO_DATE = "WEEK TO DATE";

	String PREVIOUS_MONTH = "PREVIOUS MONTH";
	String PREVIOUS_QUARTER = "PREVIOUS QUARTER";
	String PREVIOUS_YEAR = "PREVIOUS YEAR";
	String WEEK_TO_DATE_LAST_YEAR = "WEEK TO DATE LAST YEAR";
	String MONTH_TO_DATE_LAST_YEAR = "MONTH TO DATE LAST YEAR";
	String QUARTER_TO_DATE_LAST_YEAR = "QUARTER TO DATE LAST YEAR";
	String PREVIOUS_WEEK_TO_DATE = "PREVIOUS WEEK TO DATE";
	String PREVIOUS_MONTH_TO_DATE = "PREVIOUS MONTH TO DATE";
	String PREVIOUS_QUARTER_TO_DATE = "PREVIOUS QUARTER TO DATE";
	String PREVIOUS_YEAR_TO_DATE = "PREVIOUS YEAR TO DATE";
	String CURRENT_PERIOD_FROM_N_PERIOD_AGO = "CURRENT PERIOD FROM N PERIODS AGO";
	String PERIOD_TO_DATE_FROM_N_PERIOD_AGO = "PERIOD TO DATE FROM N PERIODS AGO";
	String TRAILING_N_MONTHS = "TRAILING N MONTHS";
	String TRAILING_N_DAYS = "TRAILING N DAYS";
	String TRAILING_N_PERIOD_FROM_N_PERIOD_AGO = "TRAILING N PERIODS FROM N PERIODS AGO";
	String NEXT_N_PERIODS = "NEXT_N_PERIODS";
}
