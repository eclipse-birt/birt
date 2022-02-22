/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.data.engine.executor.cache.disk;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.IRowResultSet;
import org.eclipse.birt.data.engine.executor.cache.ResultObjectUtil;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * One implemenation of DataBaseExport. This class will read data from data base
 * and export to file with sort operation done.
 */
class DiskSortExport extends DiskDataExport {
	private int dataCountOfUnit;
	private int dataCountOfTotal;

	private SortDataProvider dataProvider;
	private MergeSortUtil mergeSortUti;

	/**
	 * @param dataProvider
	 */
	DiskSortExport(Map infoMap, Comparator comparator, ResultObjectUtil resultObjectUtil) {
		dataCountOfUnit = Integer.parseInt((String) infoMap.get("dataCountOfUnit"));

		dataProvider = new SortDataProvider(dataCountOfUnit, ((String) infoMap.get("tempDir")),
				((String) infoMap.get("goalFile")), resultObjectUtil);

		mergeSortUti = MergeSortUtil.getUtil(comparator);
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.resultset.DataBaseExport#
	 * exportStartDataToDisk(org.eclipse.birt.data.engine.executor.ResultObject[])
	 */
	@Override
	public void exportStartDataToDisk(IResultObject[] resultObjects) throws IOException, DataException {
		dataCountOfTotal = innerExportStartData(resultObjects);
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.DataBaseExport#
	 * exportRestDataToDisk(org.eclipse.birt.data.engine.odi.IResultObject,
	 * org.eclipse.birt.data.engine.executor.cache.RowResultSet)
	 */
	@Override
	public int exportRestDataToDisk(IResultObject resultObject, IRowResultSet rs, int maxRos)
			throws DataException, IOException {
		// sort the raw data to unit
		int dataCountOfRest = innerExportRestData(resultObject, rs, dataCountOfUnit, maxRos);
		dataCountOfTotal += dataCountOfRest;

		// prepares for merge sort
		dataProvider.initForMerge(dataCountOfTotal);

		// merge sort on sorted unit
		mergeSortOnUnits(getMergeCount());

		// do clean job after merge sort is done
		dataProvider.end();

		return dataCountOfRest;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.executor.cache.DataBaseExport#getRowIterator()
	 */
	@Override
	public IRowIterator getRowIterator() {
		return null;
	}

	/*
	 * @see org.eclipse.birt.data.engine.executor.cache.DataBaseExport#close()
	 */
	@Override
	public void close() {
		// do nothing
	}

	/*
	 * @see
	 * org.eclipse.birt.sort4.DataBaseExport#outputRowsUnit(org.eclipse.birt.sort4.
	 * RowData[], int)
	 */
	@Override
	protected void outputResultObjects(IResultObject[] resultObjects, int indexOfUnit)
			throws IOException, DataException {
		mergeSortUti.sortSelf(resultObjects);
		dataProvider.writeData(SortDataProvider.SORT_ITSELF, indexOfUnit * dataCountOfUnit, resultObjects,
				resultObjects.length);
	}

	/**
	 * @return the count of merge unit
	 */
	private int getMergeCount() {
		int mergeCount = dataCountOfTotal / dataCountOfUnit;
		if (dataCountOfTotal % dataCountOfUnit != 0) {
			mergeCount++;
		}

		if (dataCountOfUnit < mergeCount) {
			// normally this case will never happen
			// if we set the dataCountOfUnit as 10000, then only if
			// the dataCountOfTotal exceeds 10000*10000, this exception will
			// be thrown
			throw new IllegalArgumentException(
					"the dataCountOfUnit of " + dataCountOfUnit + " is less than the merge count of " + mergeCount
							+ ", and then merge sort on file can not be done");
		}

		return mergeCount;
	}

	/**
	 * Merge sort on units
	 *
	 * @param dataProvider
	 * @param startIndex
	 * @param stopSign
	 * @throws DataException
	 * @throws Exception
	 */
	private void mergeSortOnUnits(int mergeCount) throws IOException, DataException {
		int[] startIndexArray = new int[mergeCount];
		int[] endIndexArray = new int[mergeCount];

		for (int i = 0; i < mergeCount; i++) {
			if (i == 0) {
				startIndexArray[i] = 0;
			} else {
				startIndexArray[i] = endIndexArray[i - 1];
			}

			endIndexArray[i] = startIndexArray[i] + dataCountOfUnit;
			if (endIndexArray[i] > dataCountOfTotal) {
				endIndexArray[i] = dataCountOfTotal;
			}
		}

		int[] tempBeginIndex = new int[mergeCount];
		for (int i = 0; i < mergeCount; i++) {
			tempBeginIndex[i] = startIndexArray[i];
		}

		int currData = 0;
		int totalData = endIndexArray[mergeCount - 1] - startIndexArray[0];

		IResultObject[][] resultObjects = new IResultObject[mergeCount][];

		int dataCountOfMergeUnit = dataCountOfUnit / mergeCount;
		while (currData < totalData) {
			for (int i = 0; i < mergeCount; i++) {
				int tempEndIndex = tempBeginIndex[i] + dataCountOfMergeUnit;
				if (tempEndIndex > endIndexArray[i]) {
					tempEndIndex = endIndexArray[i];
				}

				resultObjects[i] = dataProvider.readData(tempBeginIndex[i], tempEndIndex);
				if (this.session.getStopSign().isStopped()) {
					return;
				}
			}

			// merge
			int length = 0;
			for (int i = 0; i < mergeCount; i++) {
				length += resultObjects[i].length;
			}
			IResultObject[] mergedRowDatas = new IResultObject[length];

			MergeSortInfo mergeInfo = mergeSortUti.mergeSort(resultObjects, mergedRowDatas, session);
			dataProvider.writeData(SortDataProvider.SORT_MERGE, currData, mergedRowDatas,
					mergeInfo.getDataCountOfTotal());

			for (int i = 0; i < mergeCount; i++) {
				tempBeginIndex[i] += mergeInfo.getDataCountOfUnit(i);
			}

			currData += mergeInfo.getDataCountOfTotal();
		}
	}

}
