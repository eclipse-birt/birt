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

package org.eclipse.birt.data.aggregation.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.aggregation.impl.rank.TotalDenseRank;
import org.eclipse.birt.data.aggregation.impl.rank.TotalIsBottomN;
import org.eclipse.birt.data.aggregation.impl.rank.TotalIsBottomNPercent;
import org.eclipse.birt.data.aggregation.impl.rank.TotalIsTopN;
import org.eclipse.birt.data.aggregation.impl.rank.TotalIsTopNPercent;
import org.eclipse.birt.data.aggregation.impl.rank.TotalPercentRank;
import org.eclipse.birt.data.aggregation.impl.rank.TotalPercentSum;
import org.eclipse.birt.data.aggregation.impl.rank.TotalPercentile;
import org.eclipse.birt.data.aggregation.impl.rank.TotalQuartile;
import org.eclipse.birt.data.aggregation.impl.rank.TotalRank;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.aggregation.IAggregationFactory;

/**
 *
 */

public class BuildInAggregationFactory implements IAggregationFactory {

	private Map<String, IAggrFunction> aggrMap = new HashMap<>();
	private List<IAggrFunction> aggregations = new ArrayList<>();

	/**
	 *
	 */
	private void populateAggregations() {
		final TotalCount totalCount = new TotalCount();
		aggrMap.put(IBuildInAggregation.TOTAL_COUNT_FUNC, totalCount);
		aggregations.add(totalCount);
		final TotalSum totalSum = new TotalSum();
		aggrMap.put(IBuildInAggregation.TOTAL_SUM_FUNC, totalSum);
		aggregations.add(totalSum);
		final TotalMax totalMax = new TotalMax();
		aggrMap.put(IBuildInAggregation.TOTAL_MAX_FUNC, totalMax);
		aggregations.add(totalMax);
		final TotalMin totalMin = new TotalMin();
		aggrMap.put(IBuildInAggregation.TOTAL_MIN_FUNC, totalMin);
		aggregations.add(totalMin);
		final TotalAve totalAve = new TotalAve();
		aggrMap.put(IBuildInAggregation.TOTAL_AVE_FUNC, totalAve);
		aggregations.add(totalAve);
		final TotalWeightedAve totalWeightedAve = new TotalWeightedAve();
		aggrMap.put(IBuildInAggregation.TOTAL_WEIGHTEDAVE_FUNC, totalWeightedAve);
		aggregations.add(totalWeightedAve);
		final TotalMovingAve totalMovingAve = new TotalMovingAve();
		aggrMap.put(IBuildInAggregation.TOTAL_MOVINGAVE_FUNC, totalMovingAve);
		aggregations.add(totalMovingAve);
		final TotalMedian totalMedian = new TotalMedian();
		aggrMap.put(IBuildInAggregation.TOTAL_MEDIAN_FUNC, totalMedian);
		aggregations.add(totalMedian);
		final TotalMode totalMode = new TotalMode();
		aggrMap.put(IBuildInAggregation.TOTAL_MODE_FUNC, totalMode);
		aggregations.add(totalMode);
		final TotalStdDev totalStdDev = new TotalStdDev();
		aggrMap.put(IBuildInAggregation.TOTAL_STDDEV_FUNC, totalStdDev);
		aggregations.add(totalStdDev);
		final TotalVariance totalVariance = new TotalVariance();
		aggrMap.put(IBuildInAggregation.TOTAL_VARIANCE_FUNC, totalVariance);
		aggregations.add(totalVariance);
		final TotalFirst totalFirst = new TotalFirst();
		aggrMap.put(IBuildInAggregation.TOTAL_FIRST_FUNC, totalFirst);
		aggregations.add(totalFirst);
		final TotalLast totalLast = new TotalLast();
		aggrMap.put(IBuildInAggregation.TOTAL_LAST_FUNC, totalLast);
		aggregations.add(totalLast);
		final TotalRunningSum totalRunningSum = new TotalRunningSum();
		aggrMap.put(IBuildInAggregation.TOTAL_RUNNINGSUM_FUNC, totalRunningSum);
		aggregations.add(totalRunningSum);
		final TotalIrr totalIrr = new TotalIrr();
		aggrMap.put(IBuildInAggregation.TOTAL_IRR_FUNC, totalIrr);
		aggregations.add(totalIrr);
		final TotalMirr totalMirr = new TotalMirr();
		aggrMap.put(IBuildInAggregation.TOTAL_MIRR_FUNC, totalMirr);
		aggregations.add(totalMirr);
		final TotalNpv totalNpv = new TotalNpv();
		aggrMap.put(IBuildInAggregation.TOTAL_NPV_FUNC, totalNpv);
		aggregations.add(totalNpv);
		final TotalRunningNpv totalRunningNpv = new TotalRunningNpv();
		aggrMap.put(IBuildInAggregation.TOTAL_RUNNINGNPV_FUNC, totalRunningNpv);
		aggregations.add(totalRunningNpv);
		final TotalCountDistinct totalCountDistinct = new TotalCountDistinct();
		aggrMap.put(IBuildInAggregation.TOTAL_COUNTDISTINCT_FUNC, totalCountDistinct);
		aggregations.add(totalCountDistinct);
		final TotalRank totalRank = new TotalRank();
		aggrMap.put(IBuildInAggregation.TOTAL_RANK_FUNC, totalRank);
		aggregations.add(totalRank);
		final TotalIsTopN totalIsTopN = new TotalIsTopN();
		aggrMap.put(IBuildInAggregation.TOTAL_TOP_N_FUNC, totalIsTopN);
		aggregations.add(totalIsTopN);
		final TotalIsTopNPercent totalIsTopNPercent = new TotalIsTopNPercent();
		aggrMap.put(IBuildInAggregation.TOTAL_TOP_PERCENT_FUNC, totalIsTopNPercent);
		aggregations.add(totalIsTopNPercent);
		final TotalIsBottomN totalIsBottomN = new TotalIsBottomN();
		aggrMap.put(IBuildInAggregation.TOTAL_BOTTOM_N_FUNC, totalIsBottomN);
		aggregations.add(totalIsBottomN);
		final TotalIsBottomNPercent totalIsBottomNPercent = new TotalIsBottomNPercent();
		aggrMap.put(IBuildInAggregation.TOTAL_BOTTOM_PERCENT_FUNC, totalIsBottomNPercent);
		aggregations.add(totalIsBottomNPercent);
		final TotalPercentRank totalPercentRank = new TotalPercentRank();
		aggrMap.put(IBuildInAggregation.TOTAL_PERCENT_RANK_FUNC, totalPercentRank);
		aggregations.add(totalPercentRank);
		final TotalPercentile totalPercentile = new TotalPercentile();
		aggrMap.put(IBuildInAggregation.TOTAL_PERCENTILE_FUNC, totalPercentile);
		aggregations.add(totalPercentile);
		final TotalQuartile totalQuartile = new TotalQuartile();
		aggrMap.put(IBuildInAggregation.TOTAL_QUARTILE_FUNC, totalQuartile);
		aggregations.add(totalQuartile);
		final TotalPercentSum totalPercentSum = new TotalPercentSum();
		aggrMap.put(IBuildInAggregation.TOTAL_PERCENTSUM_FUNC, totalPercentSum);
		aggregations.add(totalPercentSum);
		final TotalRunningCount totalRunningCount = new TotalRunningCount();
		aggrMap.put(IBuildInAggregation.TOTAL_RUNNINGCOUNT_FUNC, totalRunningCount);
		aggregations.add(totalRunningCount);
		final TotalConcatenate totalConcatenate = new TotalConcatenate();
		aggrMap.put(IBuildInAggregation.TOTAL_CONCATENATE_FUNC, totalConcatenate);
		aggregations.add(totalConcatenate);

		final TotalRange totalRange = new TotalRange();
		aggrMap.put(IBuildInAggregation.TOTAL_RANGE_FUNC, totalRange);
		aggregations.add(totalRange);

		final TotalDenseRank totalDenseRank = new TotalDenseRank();
		aggrMap.put(IBuildInAggregation.TOTAL_DENSERANK_FUNC, totalDenseRank);
		aggregations.add(totalDenseRank);
	}

	/**
	 *
	 */
	public BuildInAggregationFactory() {
		populateAggregations();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggregationFactory#
	 * getAggregations()
	 */
	@Override
	public List<IAggrFunction> getAggregations() {
		return aggregations;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.aggregation.IAggregationFactory#
	 * getAggregation(java.lang.String)
	 */
	@Override
	public IAggrFunction getAggregation(String name) {
		return aggrMap.get(name.toUpperCase());
	}
}
