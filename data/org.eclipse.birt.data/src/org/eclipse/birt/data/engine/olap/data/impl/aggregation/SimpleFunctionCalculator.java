
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import java.io.IOException;

import org.eclipse.birt.data.engine.cache.Constants;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;

/**
 * This class can be used to calculate a cube aggregation with not-running
 * function.
 */

public class SimpleFunctionCalculator extends BaseAggregationCalculator {
	SimpleFunctionCalculator(AggregationDefinition aggregation, IAggregationResultSet aggrResultSet)
			throws DataException, IOException {
		super(aggregation, aggrResultSet);
		if (aggregation.getLevels() != null) {
			keyLevelIndex = getKeyLevelIndexs(aggregation.getLevels());
		} else {
			keyLevelIndex = null;
		}
		facttableRow = new FacttableRow(getMeasureInfo(), null, null);
		this.sortTypes = aggregation.getSortTypes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.impl.aggregation.
	 * IAggregationCalculator#execute(org.eclipse.birt.data.engine.impl.StopSign)
	 */
	public IAggregationResultSet execute(StopSign stopSign) throws IOException, DataException {
		AggregationResultRowComparator comparator = null;
		if (keyLevelIndex != null) {
			comparator = new AggregationResultRowComparator(keyLevelIndex, sortTypes);
		}
		SortedAggregationRowArray sortedRows = new SortedAggregationRowArray(aggrResultSet, aggregation.getLevels(),
				sortTypes);

		IDiskArray result = new BufferedStructureArray(AggregationResultRow.getCreator(), Constants.LIST_BUFFER_SIZE);
		if (aggrResultSet.length() <= 0) {
			return getAggregationResultSet(result);
		}
		IAggregationResultRow lastRow = sortedRows.get(0);
		IAggregationResultRow currentRow = null;
		AggregationResultRow resultRow = newAggregationResultRow(lastRow);

		if (accumulators != null) {
			for (int i = 0; i < accumulators.length; i++) {
				accumulators[i].start();
			}
		}
		onRow(lastRow);

		for (int i = 1; !stopSign.isStopped() && i < sortedRows.size(); i++) {
			currentRow = sortedRows.get(i);
			if (comparator != null && comparator.compare(currentRow, lastRow) != 0) {
				if (accumulators != null) {
					for (int j = 0; j < accumulators.length; j++) {
						accumulators[j].finish();
						resultRow.getAggregationValues()[j] = accumulators[j].getValue();
						accumulators[j].start();
					}
				}
				result.add(resultRow);
				resultRow = newAggregationResultRow(currentRow);
			}
			onRow(currentRow);
			lastRow = currentRow;
		}

		if (accumulators != null) {
			for (int j = 0; j < accumulators.length; j++) {
				accumulators[j].finish();
				resultRow.getAggregationValues()[j] = accumulators[j].getValue();
			}
		}
		result.add(resultRow);

		return getAggregationResultSet(result);
	}

	/*
	 * 
	 */
	private IAggregationResultSet getAggregationResultSet(IDiskArray result) throws IOException {
		return new AggregationResultSet(aggregation, result, getKeyNames(), getAttributeNames());
	}
}
