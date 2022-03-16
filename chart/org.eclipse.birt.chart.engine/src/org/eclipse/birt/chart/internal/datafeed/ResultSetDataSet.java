/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.internal.datafeed;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.datafeed.IResultSetDataSet;

/**
 * Maintains a subset of a resultset by defining a selective list of columns and
 * a row range to be extracted from a full resultset. An instance of this class
 * is provided to a custom data set processor that is capable of converting the
 * resultset subset content into the expected chart dataset format.
 */
public class ResultSetDataSet implements IResultSetDataSet {

	/**
	 * Indexes of the columns extracted from the parent resultset
	 */
	protected final int[] iaColumnIndexes;

	/**
	 * Bounds of the subset with respect to the parent resultset
	 */
	protected final long lStartRow, lEndRow;

	/**
	 * An internal iterator capable of visiting each row in the resultset subset
	 */
	protected Iterator<?> it;

	/**
	 * The current row number being visited
	 */
	protected long lRow = 0;

	/**
	 * The parent resultset wrapper of which this instance is a subset
	 */
	protected final ResultSetWrapper rsw;
	protected final List<?> lst;

	/**
	 * Temporary variable used in conjunction with the iterator
	 */
	protected final Object[] oaTuple;

	/**
	 * Number of columns in this resultset subset
	 */
	protected final int iColumnCount;

	protected final boolean listMode;

	protected final int listDataType;

	/**
	 * The constructor that creates an instance of a resultset subset by extracting
	 * appropriate columns and a row range from a resultset
	 *
	 * @param liResultSet
	 * @param iColumnIndex
	 * @param lStartRow
	 * @param lEndRow
	 */
	public ResultSetDataSet(ResultSetWrapper rsw, int[] iaColumnIndexes, long lStartRow, long lEndRow) {
		this.rsw = rsw;
		this.lst = null;
		this.iColumnCount = iaColumnIndexes.length;
		this.iaColumnIndexes = iaColumnIndexes;
		this.lStartRow = lStartRow;
		this.lEndRow = lEndRow;
		this.oaTuple = new Object[iaColumnIndexes.length];

		this.listMode = false;
		this.listDataType = IConstants.UNDEFINED;

		this.reset();
	}

	/**
	 * Creates the resultset using a given list.
	 *
	 * @param lst
	 */
	public ResultSetDataSet(List<?> lst, int dataType) {
		this.rsw = null;
		this.lst = lst;
		this.iColumnCount = 1;
		this.iaColumnIndexes = new int[] { 0 };
		this.lStartRow = 0;
		this.lEndRow = lst.size();
		this.oaTuple = new Object[iColumnCount];

		this.listMode = true;
		this.listDataType = dataType;

		this.reset();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.datafeed.IResultSetDataSet#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return (lRow < lEndRow);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.datafeed.IResultSetDataSet#next()
	 */
	@Override
	public Object[] next() {
		lRow++;
		if (lRow > lEndRow) {
			return null;
		}

		if (listMode) {
			oaTuple[0] = it.next();
		} else {
			final Object[] oaResultSet = (Object[]) it.next();
			for (int i = 0; i < iColumnCount; i++) {
				if (iaColumnIndexes[i] != -1) {
					// ignore the column if the column index is -1.
					oaTuple[i] = oaResultSet[iaColumnIndexes[i]];
				}
			}
		}
		return oaTuple;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.datafeed.IResultSetDataSet#getDataType()
	 */
	@Override
	public int getDataType() {
		if (listMode) {
			return listDataType;
		}

		if (iaColumnIndexes.length >= 1) {
			return rsw.getColumnDataType(iaColumnIndexes[0]);
		}
		return IConstants.UNDEFINED;
	}

	@Override
	public int getDataType(int columnIndex) {
		if (listMode) {
			return listDataType;
		}

		if (columnIndex < iaColumnIndexes.length) {
			return rsw.getColumnDataType(iaColumnIndexes[columnIndex]);
		}
		return IConstants.UNDEFINED;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.datafeed.IResultSetDataSet#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return iColumnCount;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.datafeed.IResultSetDataSet#getSize()
	 */
	@Override
	public long getSize() {
		return lEndRow - lStartRow;
	}

	@Override
	public void reset() {
		this.lRow = 0;
		if (this.rsw != null) {
			this.it = rsw.iterator();
			// SCROLL TO START ROW
			if (lRow < lStartRow) {
				while (lRow < lStartRow) {
					lRow++;
					it.next();
				}
			}
		} else {
			this.it = lst.iterator();
		}
	}
}
