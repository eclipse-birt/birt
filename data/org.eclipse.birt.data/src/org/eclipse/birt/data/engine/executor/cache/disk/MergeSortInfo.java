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

/**
 * Store sort info of once merge sort. In once merge sort, following information
 * is needed to be exported. 1: how many data will be put into the merge result
 * 2: how many data is used in every array
 */
class MergeSortInfo {
	// the count of data which will is put into merge result
	private int dataCountOfTotal;

	// the count of every array which is put into merge result
	private int dataCountOfUnit[];

	/**
	 * Construction
	 * 
	 * @param dataCountOfTotal
	 * @param dataCountOfUnit
	 */
	MergeSortInfo(int dataCountOfTotal, int[] dataCountOfUnit) {
		this.dataCountOfTotal = dataCountOfTotal;
		this.dataCountOfUnit = dataCountOfUnit;
	}

	/**
	 * @return the data count of merged result
	 */
	int getDataCountOfTotal() {
		return dataCountOfTotal;
	}

	/**
	 * @param unit index
	 * @return the data count of specified array
	 */
	int getDataCountOfUnit(int index) {
		return dataCountOfUnit[index];
	}

}
