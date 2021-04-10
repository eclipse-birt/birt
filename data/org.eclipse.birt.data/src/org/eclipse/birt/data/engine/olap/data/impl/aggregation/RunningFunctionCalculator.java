
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import java.io.IOException;

import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.cache.Constants;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;

/**
 * This class can be used to calculate a cube aggregation with running
 * functions.
 */

public class RunningFunctionCalculator extends BaseAggregationCalculator {
	boolean needMultiplePass;

	RunningFunctionCalculator(AggregationDefinition aggregation, IAggregationResultSet aggrResultSet)
			throws DataException, IOException {
		super(aggregation, aggrResultSet);
		if (aggrResultSet.getAllLevels() != null) {
			keyLevelIndex = getKeyLevelIndexs(aggrResultSet.getAllLevels());
		} else {
			keyLevelIndex = null;
		}
		facttableRow = new FacttableRow(getMeasureInfo(), null, null);
		needMultiplePass = needMultiplePass(aggregation);
		this.sortTypes = aggregation.getSortTypes();
	}

	/**
	 * 
	 * @param aggrDef
	 * @return
	 * @throws DataException
	 */
	private static boolean needMultiplePass(AggregationDefinition aggrDef) throws DataException {
		AggregationFunctionDefinition[] aggregationFunction = aggrDef.getAggregationFunctions();

		if (aggregationFunction != null) {
			IAggrFunction aggregation = AggregationManager.getInstance()
					.getAggregation(aggregationFunction[0].getFunctionName());
			if (aggregation == null) {
				throw new DataException(
						DataResourceHandle.getInstance().getMessage(ResourceConstants.UNSUPPORTED_FUNCTION)
								+ aggregationFunction[0].getFunctionName());
			}
			if (aggregation.getNumberOfPasses() > 1) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.impl.aggregation.
	 * IAggregationCalculator#execute(org.eclipse.birt.data.engine.impl.StopSign)
	 */
	public IAggregationResultSet execute(StopSign stopSign) throws IOException, DataException {
		AggregationResultRowComparator comparator = null;
		if (aggregation.getLevels() != null) {
			comparator = new AggregationResultRowComparator(getKeyLevelIndexs(aggregation.getLevels()), sortTypes);
		}
		SortedAggregationRowArray sortedRows = new SortedAggregationRowArray(aggrResultSet, aggregation.getLevels(),
				sortTypes);

		IDiskArray result = new BufferedStructureArray(AggregationResultRow.getCreator(), Constants.LIST_BUFFER_SIZE);

		if (aggrResultSet.length() <= 0) {
			return getAggregationResultSet(result);
		}
		IAggregationResultRow lastRow = sortedRows.get(0);
		IAggregationResultRow currentRow = null;
		int lastIndex = 0;

		onRow(lastRow);
		if (!needMultiplePass) {
			addOneResultRow(result, lastRow);
		}

		for (int i = 1; !stopSign.isStopped() && i < sortedRows.size(); i++) {
			currentRow = sortedRows.get(i);
			if (comparator != null && comparator.compare(currentRow, lastRow) != 0) {
				if (needMultiplePass) {
					secondPass(sortedRows, result, lastIndex, i);
					lastIndex = i;
				}

				// recreate running accumulators
				createAccumulators();
			}
			onRow(currentRow);
			if (!needMultiplePass) {
				addOneResultRow(result, currentRow);
			}
			lastRow = currentRow;
		}
		if (needMultiplePass) {
			secondPass(sortedRows, result, lastIndex, sortedRows.size());
		}
		return getAggregationResultSet(result);
	}

	/**
	 */
	private void secondPass(SortedAggregationRowArray sortedRows, IDiskArray result, int startIndex, int endIndex)
			throws DataException, IOException {
		if (accumulators != null) {
			for (int j = 0; j < accumulators.length; j++) {
				accumulators[j].finish();
				accumulators[j].start();
			}
		}
		for (int i = startIndex; i < endIndex; i++) {
			IAggregationResultRow row = sortedRows.get(i);
			onRow(row);
			addOneResultRow(result, row);
		}
	}

	/**
	 * 
	 * @param result
	 * @param lastRow
	 * @throws DataException
	 * @throws IOException
	 */
	private void addOneResultRow(IDiskArray result, IAggregationResultRow lastRow) throws DataException, IOException {
		AggregationResultRow resultRow = newAggregationResultRow(lastRow);
		if (accumulators != null) {
			for (int j = 0; j < accumulators.length; j++) {
				resultRow.getAggregationValues()[j] = accumulators[j].getValue();
			}
		}
		result.add(resultRow);
	}

	/**
	 * 
	 * @param result
	 * @return
	 * @throws IOException
	 */
	private IAggregationResultSet getAggregationResultSet(IDiskArray result) throws IOException {
		return new AggregationResultSet(aggregation, aggrResultSet.getAllLevels(), result, getKeyNames(),
				getAttributeNames());
	}
}
