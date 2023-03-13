
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
