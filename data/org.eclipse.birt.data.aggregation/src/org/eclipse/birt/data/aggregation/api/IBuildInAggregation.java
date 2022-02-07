/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.data.aggregation.api;

/**
 * 
 */

public interface IBuildInAggregation {

	public static final String TOTAL_COUNT_FUNC = "COUNT";//$NON-NLS-1$
	public static final String TOTAL_SUM_FUNC = "SUM";//$NON-NLS-1$
	public static final String TOTAL_MAX_FUNC = "MAX";//$NON-NLS-1$
	public static final String TOTAL_MIN_FUNC = "MIN";//$NON-NLS-1$
	public static final String TOTAL_AVE_FUNC = "AVE";//$NON-NLS-1$
	public static final String TOTAL_WEIGHTEDAVE_FUNC = "WEIGHTEDAVE";//$NON-NLS-1$
	public static final String TOTAL_MOVINGAVE_FUNC = "MOVINGAVE";//$NON-NLS-1$
	public static final String TOTAL_MEDIAN_FUNC = "MEDIAN";//$NON-NLS-1$
	public static final String TOTAL_MODE_FUNC = "MODE";//$NON-NLS-1$
	public static final String TOTAL_STDDEV_FUNC = "STDDEV";//$NON-NLS-1$
	public static final String TOTAL_VARIANCE_FUNC = "VARIANCE";//$NON-NLS-1$
	public static final String TOTAL_FIRST_FUNC = "FIRST";//$NON-NLS-1$
	public static final String TOTAL_LAST_FUNC = "LAST";//$NON-NLS-1$
	public static final String TOTAL_RUNNINGSUM_FUNC = "RUNNINGSUM";//$NON-NLS-1$
	public static final String TOTAL_IRR_FUNC = "IRR";//$NON-NLS-1$
	public static final String TOTAL_MIRR_FUNC = "MIRR";//$NON-NLS-1$
	public static final String TOTAL_NPV_FUNC = "NPV";//$NON-NLS-1$
	public static final String TOTAL_RUNNINGNPV_FUNC = "RUNNINGNPV";//$NON-NLS-1$
	public static final String TOTAL_COUNTDISTINCT_FUNC = "COUNTDISTINCT";//$NON-NLS-1$
	public static final String TOTAL_RANK_FUNC = "RANK";//$NON-NLS-1$
	public static final String TOTAL_TOP_N_FUNC = "ISTOPN";//$NON-NLS-1$
	public static final String TOTAL_TOP_PERCENT_FUNC = "ISTOPNPERCENT";//$NON-NLS-1$
	public static final String TOTAL_BOTTOM_N_FUNC = "ISBOTTOMN";//$NON-NLS-1$
	public static final String TOTAL_BOTTOM_PERCENT_FUNC = "ISBOTTOMNPERCENT";//$NON-NLS-1$
	public static final String TOTAL_PERCENT_RANK_FUNC = "PERCENTRANK";//$NON-NLS-1$
	public static final String TOTAL_PERCENTILE_FUNC = "PERCENTILE";//$NON-NLS-1$
	public static final String TOTAL_QUARTILE_FUNC = "QUARTILE";//$NON-NLS-1$
	public static final String TOTAL_PERCENTSUM_FUNC = "PERCENTSUM";//$NON-NLS-1$
	public static final String TOTAL_RUNNINGCOUNT_FUNC = "RUNNINGCOUNT";//$NON-NLS-1$
	public static final String TOTAL_CONCATENATE_FUNC = "CONCATENATE";//$NON-NLS-1$
	public static final String TOTAL_RANGE_FUNC = "RANGE";//$NON-NLS-1$

}
