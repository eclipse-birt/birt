/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl;

import java.util.logging.Logger;

/**
 * Simple wrapper of colum information, including column index and column name.
 */
public class ColumnInfo {
	private int columnIndex;
	private String columnName;
	private static Logger logger = Logger.getLogger(ColumnInfo.class.getName());

	ColumnInfo(int columnIndex, String columnName) {
		Object[] params = { Integer.valueOf(columnIndex), columnName };
		logger.entering(ColumnInfo.class.getName(), "ColumnInfo", params);

		this.columnIndex = columnIndex;
		this.columnName = columnName;
		logger.exiting(ColumnInfo.class.getName(), "ColumnInfo");
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public String getColumnName() {
		return columnName;
	}

}
