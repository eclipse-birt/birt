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

package org.eclipse.birt.data.aggregation.impl.rank;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.aggregation.calculator.ICalculator;
import org.eclipse.birt.data.aggregation.impl.SummaryAccumulator;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Accumulator that is used by Percentile and Quartile. The formula to calculate
 * the Percentile is not of standard one. It follows Microsoft excel convention.
 *
 * Say, if you want pct-th percentile from acading array a[], the pseudocodes of
 * calculation looks like follows:
 *
 * k=Math.floor((pct/4)*(n-1))+1) f=(pct/4)*(n-1))+1 - k; // We also need to
 * calculate fraction: ad = a[k]+(f*(a[k+1]-a[k])) //Then we can calculate out
 * the adjustment: result = a[k] + ad;
 *
 */
abstract class PercentileAccumulator extends SummaryAccumulator {

	//
	private Double pct;
	private List cachedValues;

	public PercentileAccumulator(ICalculator calc) {
		super(calc);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.aggregation.SummaryAccumulator#start()
	 */
	@Override
	public void start() {
		super.start();

		pct = -1D;
		cachedValues = new ArrayList();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.aggregation.Accumulator#onRow(java.lang.Object[]
	 * )
	 */
	@Override
	public void onRow(Object[] args) throws DataException {
		assert (args.length == 2);
		if (args[0] != null) {
			Number d = calculator.add(calculator.getTypedObject(0), calculator.getTypedObject(args[0]));
			if (d != null) {
				cachedValues.add(d);
			}
		}
		if (pct == -1) {
			Double pctValue = RankAggregationUtil.getNumericValue(args[1]);
			pct = getPctValue(pctValue);
		}
	}

	protected abstract double getPctValue(Double d) throws DataException;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.aggregation.SummaryAccumulator#getSummaryValue()
	 */
	@Override
	public Object getSummaryValue() throws DataException {
		Object[] sortedObjs = this.cachedValues.toArray();
		if (sortedObjs.length == 0) {
			return null;
		}
		RankAggregationUtil.sortArray(sortedObjs);
		double n = pct * (sortedObjs.length - 1) + 1;
		int k = (int) Math.floor(n);
		double fraction = n - k;

		Number adjustment = 0;
		if (fraction != 0) {
			adjustment = calculator.multiply(calculator.getTypedObject(fraction), calculator
					.subtract(calculator.getTypedObject(sortedObjs[k]), calculator.getTypedObject(sortedObjs[k - 1])));
		}

		return calculator.add(calculator.getTypedObject(sortedObjs[k - 1]), calculator.getTypedObject(adjustment));
	}

}
