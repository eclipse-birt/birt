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

import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * Util class for merge sort. The atomic sort operation is done here, which
 * includess sort an array and merge sort several arrays. There is one
 * significant difference between internal merge sort and external merge sort in
 * once merge sort.
 *
 * For instance, following internal sorting and its result in one time [1, 2],
 * [3, 4] -> [1, 2 ,3, 4]
 *
 * its external sorting, assume available memory size is 2 [1, 2], [3, 4] -> [1]
 * not [1, 3]
 *
 */
class MergeSortUtil {
	// Comparator
	private Comparator comparator;

	/**
	 * One key of MergeSortUtil is the comparator, which provides a service to
	 * compare the data of two rows.
	 *
	 * @param comparator
	 */
	static MergeSortUtil getUtil(Comparator comparator) {
		assert comparator != null;

		MergeSortUtil instance = new MergeSortUtil();
		instance.comparator = comparator;
		return instance;
	}

	/**
	 * Sort an array of ResultObjects using stored comparator.
	 *
	 * @param self, which needs to be sorted
	 */
	void sortSelf(IResultObject[] self) {
		Comparator comparator = new Comparator() {

			@Override
			public int compare(Object obj1, Object obj2) {
				IResultObject data1 = (IResultObject) obj1;
				IResultObject data2 = (IResultObject) obj2;
				return compareResultObject(data1, data2);
			}
		};

		Arrays.sort(self, comparator);
	}

	/**
	 * Get min object of a array
	 *
	 * @param objects
	 * @param length
	 * @return the postion of min object
	 */
	public int getMinResultObject(IResultObject[] objects, int length) {
		IResultObject minObject = null;
		int minObjectPos = -1;

		for (int i = 0; i < length; i++) {
			if (objects[i] != null && (minObject == null || (compareResultObject(minObject, objects[i]) > 0))) {
				minObject = objects[i];
				minObjectPos = i;
			}
		}

		return minObjectPos;
	}

	/**
	 * Sort several arrays of resultobjects of once time
	 *
	 * @param resultObjects,       the array of resultobjects needs to be sorted
	 * @param sortedResultObjects, the sorted result
	 * @param stopSign
	 * @return MergeSortInfo, the related merge info
	 */
	MergeSortInfo mergeSort(IResultObject[][] resultObjects, IResultObject[] sortedResultObjects,
			DataEngineSession session) {
		int mergeCount = resultObjects.length;
		int[] indexOfAllUnits = new int[mergeCount];
		boolean[] validUnit = new boolean[mergeCount];

		int realMergeCount = 0;
		IResultObject[][] toBeSortData = new IResultObject[mergeCount][];
		for (int i = 0; i < mergeCount; i++) {
			if (resultObjects[i].length > 0) {
				toBeSortData[realMergeCount++] = resultObjects[i];
				validUnit[i] = true;
			}
		}

		int totalCount = 0;
		int[] indexOfUnit = new int[realMergeCount];
		boolean isDone = false;
		while (!isDone) {
			int indexUnitOfMinValue = 0;
			IResultObject minSortData = toBeSortData[indexUnitOfMinValue][indexOfUnit[indexUnitOfMinValue]];
			for (int i = 1; i < realMergeCount; i++) {
				if (compareResultObject(minSortData, toBeSortData[i][indexOfUnit[i]]) > 0) {
					indexUnitOfMinValue = i;
					minSortData = toBeSortData[indexUnitOfMinValue][indexOfUnit[indexUnitOfMinValue]];
				}
			}

			sortedResultObjects[totalCount++] = minSortData;
			indexOfUnit[indexUnitOfMinValue]++;

			if (toBeSortData[indexUnitOfMinValue].length == indexOfUnit[indexUnitOfMinValue]) {
				isDone = true;
			}
			if (session.getStopSign().isStopped()) {
				break;
			}
		}

		for (int i = 0, j = 0; i < mergeCount; i++) {
			if (validUnit[i]) {
				indexOfAllUnits[i] = indexOfUnit[j++];
			} else {
				indexOfAllUnits[i] = 0;
			}
		}

		return new MergeSortInfo(totalCount, indexOfAllUnits);
	}

	/**
	 * @return the compared result of two result objects
	 */
	private int compareResultObject(IResultObject resultObject1, IResultObject resultObject2) {
		return comparator.compare(resultObject1, resultObject2);
	}

	public Comparator getComparator() {
		return comparator;
	}

}
