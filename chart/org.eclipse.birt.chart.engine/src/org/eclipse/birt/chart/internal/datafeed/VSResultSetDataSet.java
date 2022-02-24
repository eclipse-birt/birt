/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.chart.internal.datafeed;

import java.util.List;

import org.eclipse.birt.chart.computation.IConstants;

/**
 * The class extends ResultSetDataSet and provides internal function to convert
 * data type of value from boolean to int, it is just used for the data
 * populating of value series.
 * 
 * @since 2.5.1
 */

class VSResultSetDataSet extends ResultSetDataSet {
	/**
	 * The constructor that creates an instance of a resultset subset by extracting
	 * appropriate columns and a row range from a resultset
	 * 
	 * @param liResultSet
	 * @param iColumnIndex
	 * @param lStartRow
	 * @param lEndRow
	 */
	public VSResultSetDataSet(ResultSetWrapper rsw, int[] iaColumnIndexes, long lStartRow, long lEndRow) {
		super(rsw, iaColumnIndexes, lStartRow, lEndRow);
	}

	/**
	 * Creates the resultset using a given list.
	 * 
	 * @param lst
	 */
	public VSResultSetDataSet(List<?> lst, int dataType) {
		super(lst, dataType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.IResultSetDataSet#getDataType()
	 */
	public int getDataType() {
		if (listMode) {
			if (listDataType == IConstants.BOOLEAN)
				return IConstants.NUMERICAL;
			return listDataType;
		}

		if (iaColumnIndexes.length >= 1) {
			int type = rsw.getColumnDataType(iaColumnIndexes[0]);
			if (type == IConstants.BOOLEAN)
				return IConstants.NUMERICAL;
			return type;
		}
		return IConstants.UNDEFINED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.internal.datafeed.ResultSetDataSet#getDataType(int)
	 */
	public int getDataType(int columnIndex) {
		if (listMode) {
			if (listDataType == IConstants.BOOLEAN)
				return IConstants.NUMERICAL;
			return listDataType;
		}

		if (columnIndex < iaColumnIndexes.length) {
			int type = rsw.getColumnDataType(iaColumnIndexes[columnIndex]);
			if (type == IConstants.BOOLEAN)
				return IConstants.NUMERICAL;
			return type;
		}
		return IConstants.UNDEFINED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.datafeed.IResultSetDataSet#next()
	 */
	public Object[] next() {
		lRow++;
		if (lRow > lEndRow) {
			return null;
		}

		if (listMode) {
			oaTuple[0] = it.next();
			if (oaTuple[0] instanceof Boolean) {
				oaTuple[0] = asInteger((Boolean) oaTuple[0]);
			}
		} else {
			final Object[] oaResultSet = (Object[]) it.next();
			for (int i = 0; i < iColumnCount; i++) {
				if (iaColumnIndexes[i] != -1) {
					// ignore the column if the column index is -1.
					oaTuple[i] = oaResultSet[iaColumnIndexes[i]];
					if (oaTuple[i] instanceof Boolean) {
						oaTuple[i] = asInteger((Boolean) oaTuple[i]);
					}
				}
			}
		}
		return oaTuple;
	}

	/**
	 * @param o
	 * @return
	 */
	private Integer asInteger(Boolean o) {
		if (o.booleanValue()) {
			return Integer.valueOf(1);
		} else {
			return Integer.valueOf(0);
		}
	}
}
