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

package org.eclipse.birt.report.engine.executor.buffermgr;

/**
 * ROW in table layout
 * 
 */
public class Row {

	/**
	 * row index
	 */
	int rowId;
	/**
	 * row content
	 */
	Object content;
	/**
	 * cells in the row
	 */
	Cell[] cells;

	Row(int rowId) {
		this.rowId = rowId;
	}

	public Object getContent() {
		return content;
	}

	public Cell getCell(int cellId) {
		return cells[cellId];
	}

	public int getRowId() {
		return rowId;
	}
}