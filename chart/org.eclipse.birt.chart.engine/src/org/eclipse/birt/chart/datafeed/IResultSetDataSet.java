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

package org.eclipse.birt.chart.datafeed;

/**
 * This interface maintains a subset of a resultset by defining a selective list
 * of columns and a row range to be extracted from a full resultset. An
 * implementor of this is provided to a custom data set processor that is
 * capable of converting the resultset subset content into the expected chart
 * dataset format.
 */
public interface IResultSetDataSet {

	/**
	 * @return Returns the columns count for current resultset subset.
	 */
	int getColumnCount();

	/**
	 * Returns the data type associated with a single column resultset subset. Note
	 * the result is only valid for single column subset.
	 * 
	 * @return A data type associated with a single column resultset
	 */
	int getDataType();

	/**
	 * Returns the data type of a given column associated with a multi-column
	 * resultset subset.
	 * 
	 * @return A data type associated with a multi-column resultset
	 */
	int getDataType(int columnIndex);

	/**
	 * Returns the number of rows associated with this resultset subset instance
	 * 
	 * @return The number of rows associated with this resultset subset instance
	 */
	long getSize();

	/**
	 * Indicates whether another row of data is available without actually moving
	 * the cursor
	 * 
	 * @return 'true' if another row of data is available
	 */
	boolean hasNext();

	/**
	 * Returns a row of data and advances the resultset subset cursor
	 * 
	 * @return An 'Object[]' that represents a resultset subset tuple
	 */
	Object[] next();

	/**
	 * Resets data set if data has been iterated.
	 * 
	 * @since 3.7
	 */
	void reset();
}
