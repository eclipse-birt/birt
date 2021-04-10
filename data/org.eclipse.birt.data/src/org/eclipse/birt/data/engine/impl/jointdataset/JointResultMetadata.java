/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl.jointdataset;

import org.eclipse.birt.data.engine.odi.IResultClass;

/**
 * The result meta data of two data set join.
 */
public class JointResultMetadata {
	public static final int COLUMN_TYPE_COMPUTED = 0;
	public static final int COLUMN_TYPE_LEFT = 1;
	public static final int COLUMN_TYPE_RIGHT = 2;
	//
	private int[] columnSource;
	private int[] columnIndex;
	private IResultClass resultClass;

	/**
	 * Constructor
	 * 
	 * @param resultClass
	 * @param isFromLeftResultSet
	 * @param index
	 */
	public JointResultMetadata(IResultClass resultClass, int[] columnSource, int[] index) {
		this.resultClass = resultClass;
		this.columnSource = columnSource;
		this.columnIndex = index;
	}

	/**
	 * Return the IResultClass instance of this JointResultMetadata.
	 * 
	 * @return
	 */
	public IResultClass getResultClass() {
		return this.resultClass;
	}

	/**
	 * Return whether the column with given index is from left join result set or
	 * right.
	 * 
	 * @param index
	 * @return
	 */
	int getColumnSource(int index) {
		return columnSource[index - 1];
	}

	/**
	 * Return the index of column, marked by given index, in its "from" data set,
	 * that is, either left one or right one.
	 * 
	 * @param index
	 * @return
	 */
	int getSourceIndex(int index) {
		return columnIndex[index - 1];
	}
}
