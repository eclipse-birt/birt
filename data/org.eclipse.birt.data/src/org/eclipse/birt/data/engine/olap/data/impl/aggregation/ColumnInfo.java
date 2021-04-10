
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

/**
 * 
 */

public class ColumnInfo {
	private int dimIndex;
	private int levelIndex;
	private int columnIndex;
	private int dataType;
	private boolean isKey;

	public ColumnInfo(int dimIndex, int levelIndex, int columnIndex, int dataType, boolean isKey) {
		this.dimIndex = dimIndex;
		this.levelIndex = levelIndex;
		this.columnIndex = columnIndex;
		this.dataType = dataType;
		this.isKey = isKey;

	}

	int getDimIndex() {
		return dimIndex;
	}

	int getLevelIndex() {
		return levelIndex;
	}

	int getColumnIndex() {
		return columnIndex;
	}

	int getDataType() {
		return dataType;
	}

	boolean isKey() {
		return isKey;
	}
}
