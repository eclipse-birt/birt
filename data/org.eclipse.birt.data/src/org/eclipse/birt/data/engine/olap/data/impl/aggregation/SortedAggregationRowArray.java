
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
import java.util.Comparator;

import org.eclipse.birt.data.engine.cache.Constants;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.DiskSortedStack;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;

/**
 * The class provide the function of sorting a aggregation result set.
 */

public class SortedAggregationRowArray {
	IAggregationResultSet aggregationResultSet;
	private IDiskArray sortedRows = null;
	int[] sortTypes = null;

	public SortedAggregationRowArray(IAggregationResultSet aggregationResultSet, DimLevel[] keyLevels, int[] sortTypes)
			throws IOException {
		this.aggregationResultSet = aggregationResultSet;
		this.sortTypes = sortTypes;
		if (keyLevels != null && needReSort(keyLevels)) {
			sort(keyLevels);
		}
	}

	public SortedAggregationRowArray(IAggregationResultSet aggregationResultSet) throws IOException {
		this.aggregationResultSet = aggregationResultSet;
		sort(aggregationResultSet.getAllLevels());
	}

	/**
	 * 
	 * @param keyLevels
	 * @throws IOException
	 */
	private void sort(DimLevel[] keyLevels) throws IOException {
		int[] keyLevelIndexes = getKeyLevelIndexs(keyLevels);

		Comparator<IAggregationResultRow> comparator = new AggregationResultRowComparator(keyLevelIndexes, sortTypes);
		DiskSortedStack diskSortedStack = new DiskSortedStack(Constants.FACT_TABLE_BUFFER_SIZE, false, comparator,
				AggregationResultRow.getCreator());

		for (int i = 0; i < aggregationResultSet.length(); i++) {
			aggregationResultSet.seek(i);
			IAggregationResultRow row = aggregationResultSet.getCurrentRow();
			AggregationResultRow newRow = new AggregationResultRow();
			newRow.setLevelMembers(row.getLevelMembers());
			newRow.setAggregationValues(row.getAggregationValues());
			diskSortedStack.push(newRow);
		}

		sortedRows = new BufferedStructureArray(AggregationResultRow.getCreator(), Constants.LIST_BUFFER_SIZE);
		AggregationResultRow row = (AggregationResultRow) diskSortedStack.pop();
		while (row != null) {
			sortedRows.add(row);
			row = (AggregationResultRow) diskSortedStack.pop();
		}
	}

	/**
	 * 
	 * @param keyLevels
	 * @return
	 */
	private int[] getKeyLevelIndexs(DimLevel[] keyLevels) {
		int[] keyLevelIndexes = new int[keyLevels.length];
		DimLevel[] allLevels = aggregationResultSet.getAllLevels();
		for (int i = 0; i < keyLevels.length; i++) {
			for (int j = 0; j < allLevels.length; j++) {
				if (keyLevels[i].equals(allLevels[j]))
					keyLevelIndexes[i] = j;
			}
		}
		return keyLevelIndexes;
	}

	/**
	 * 
	 * @param keyLevels
	 * @return
	 */
	private boolean needReSort(DimLevel[] keyLevels) {
		DimLevel[] allLevels = aggregationResultSet.getAllLevels();
		for (int i = 0; i < keyLevels.length; i++) {
			if (!keyLevels[i].equals(allLevels[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param index
	 * @return
	 * @throws IOException
	 */
	public IAggregationResultRow get(int index) throws IOException {
		if (sortedRows != null) {
			return (IAggregationResultRow) sortedRows.get(index);
		} else {
			aggregationResultSet.seek(index);
			return aggregationResultSet.getCurrentRow();
		}
	}

	/**
	 * 
	 * @return
	 */
	public int size() {
		if (sortedRows != null) {
			return sortedRows.size();
		} else {
			return aggregationResultSet.length();
		}
	}

	public IDiskArray getSortedRows() {
		return sortedRows;
	}
}
