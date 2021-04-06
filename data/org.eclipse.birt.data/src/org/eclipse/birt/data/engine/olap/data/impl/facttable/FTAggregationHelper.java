
/*******************************************************************************
 * Copyright (c) 2004, 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.impl.facttable;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */

public class FTAggregationHelper {
	private IAggrFunction[] functions;

	/**
	 * Array to store all calculated aggregate values. aggrValue[i] is a list of
	 * values calculated for expression #i in the associated aggregate table. The
	 * aggregate values are stored in each list as the cursor advances for the
	 * associated ODI result set.
	 */
	private Object[] currentRoundAggrValue;

	private List<Accumulator> accumulators;

	public FTAggregationHelper(IAggrFunction[] functions) throws DataException {
		this.functions = functions;
		this.currentRoundAggrValue = new Object[functions.length];
		this.accumulators = new ArrayList<Accumulator>();

		this.populateAggregations();
	}

	private void populateAggregations() throws DataException {
		for (int i = 0; i < functions.length; i++) {
			Accumulator acc = functions[i].newAccumulator();
			acc.start();
			this.accumulators.add(acc);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.executor.aggregation.
	 * IProgressiveAggregationHelper#onRow(int, int,
	 * org.eclipse.birt.data.engine.odi.IResultObject, int)
	 */
	public void onRow(boolean populateValue, FactTableRow factTableRow) throws DataException {
		for (int aggrIndex = 0; aggrIndex < this.functions.length; aggrIndex++) {
			Accumulator acc = this.accumulators.get(aggrIndex);

			// Calculate arguments to the aggregate aggregationtion

			acc.onRow(new Object[] { factTableRow.getMeasures()[aggrIndex] });

			if (populateValue) {
				acc.finish();
				currentRoundAggrValue[aggrIndex] = acc.getValue();
				acc.start();
			}
		}
	}

	public Object[] getCurrentValues() {
		Object[] result = new Object[this.currentRoundAggrValue.length];
		for (int i = 0; i < result.length; i++)
			result[i] = this.currentRoundAggrValue[i];
		return result;
	}
}
