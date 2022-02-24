/**
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

package org.eclipse.birt.report.model.api;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.elements.TableColumn;

/**
 * Represents the data structure to store copied objects like the column and
 * cells.
 */

public class ColumnBandData implements Cloneable {

	/**
	 * The copied column.
	 */

	private TableColumn column = null;

	/**
	 * The copied cells.
	 */

	private List cells = null;

	/**
	 * Constructs a default <code>ColumnBandData</code>.
	 */

	ColumnBandData() {
	}

	/**
	 * Returns the copied column.
	 * 
	 * @return the copied column.
	 */

	protected TableColumn getColumn() {
		return column;
	}

	/**
	 * Saves the copied column.
	 * 
	 * @param column the copied column object
	 */

	void setColumn(TableColumn column) {
		this.column = column;
	}

	/**
	 * Returns cells after the copy operation.
	 * 
	 * @return a list containing cells. Each element in the list is a
	 *         <code>CellContextInfo</code>.
	 */

	protected List getCells() {
		return cells;
	}

	/**
	 * Saves the copied cells.
	 * 
	 * @param cells a list containing cells. Each element in the list is a
	 *              <code>CellContextInfo</code>.
	 */

	void setCells(List cells) {
		this.cells = cells;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */

	protected Object clone() throws CloneNotSupportedException {
		ColumnBandData clonedData = (ColumnBandData) super.clone();

		TableColumn clonedColumn = (TableColumn) column.clone();
		clonedData.column = clonedColumn;

		List clonedList = new ArrayList();
		for (int i = 0; cells != null && i < cells.size(); i++) {
			CellContextInfo contextInfo = (CellContextInfo) cells.get(i);
			clonedList.add(contextInfo.clone());
		}
		clonedData.cells = clonedList;

		return clonedData;
	}

	/**
	 * Deeply clones the column band data.
	 * 
	 * @return the copy of the column band data
	 */

	public ColumnBandData copy() {
		try {
			return (ColumnBandData) clone();
		} catch (CloneNotSupportedException e) {
			assert false;
		}

		return null;
	}
}
