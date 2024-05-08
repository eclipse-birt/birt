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

	/**
	 * Get the content
	 *
	 * @return Return the content
	 */
	public Object getContent() {
		return content;
	}

	/**
	 * Get the cell based on cell id
	 *
	 * @param cellId cell id
	 * @return Return a cell
	 */
	public Cell getCell(int cellId) {
		return cells[cellId];
	}

	/**
	 * Get the row id
	 *
	 * @return Return the row id
	 */
	public int getRowId() {
		return rowId;
	}
}
