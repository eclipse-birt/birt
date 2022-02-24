/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.api.elements.table;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.elements.TableItem;

/**
 * The table model for the UI render. It encapsulates details about the unclear
 * layout of table elment in the design files.
 * 
 */

public class LayoutTableModel {

	/**
	 * The cached table handle.
	 */

	private TableHandle table;

	/**
	 * Constructs a <code>LayoutTableModel</code> with the given table element.
	 * 
	 * @param table the handle of the table element
	 */

	public LayoutTableModel(TableHandle table) {
		this.table = table;
	}

	/**
	 * Returns the underlying layout table.
	 * 
	 * @return the underlying layout table
	 */

	private LayoutTable getLayoutTable() {
		return ((TableItem) table.getElement()).getLayoutModel(table.getModule());
	}

	/**
	 * Returns the header slot.
	 * 
	 * @return the header slot
	 */

	public LayoutSlot getLayoutSlotHeader() {
		return getLayoutTable().getHeader();
	}

	/**
	 * Returns the detail slot.
	 * 
	 * @return the detail slot
	 */

	public LayoutSlot getLayoutSlotDetail() {
		return getLayoutTable().getDetail();
	}

	/**
	 * Returns the footer slot.
	 * 
	 * @return the footer slot
	 */

	public LayoutSlot getLayoutSlotFooter() {
		return getLayoutTable().getFooter();
	}

	/**
	 * Returns the table to which the layout model belongs.
	 * 
	 * @return the handle of the table element
	 */

	public TableHandle getTable() {
		return table;
	}

	/**
	 * Returns the layout group with the given group level.
	 * 
	 * @param groupLevel the 1-based group level
	 * 
	 * @return the layout group
	 */

	public LayoutGroup getLayoutGroup(int groupLevel) {
		return getLayoutTable().getLayoutGroup(groupLevel);
	}

	/**
	 * Returns the column count in the table.
	 * 
	 * @return the column count in the table.
	 */

	public int getColumnCount() {
		return getLayoutTable().getColumnCount();
	}

	/**
	 * Returns the column count in the table.
	 * 
	 * @return the column count in the table.
	 */

	public int getRowCount() {
		return LayoutUtil.getRowCount(getLayoutTable());
	}

	/**
	 * Return a cell element with the given poistion. Uses this method to find cells
	 * in Table Header, Detail and Footer slots.
	 * 
	 * @param slotId the slot index,
	 * @param rowId  the 1-based row index
	 * @param colId  the 1-based column index
	 * @return the cell element. If no cell on the position, return
	 *         <code>null</code>.
	 */

	public CellHandle getCell(int slotId, int rowId, int colId) {
		return getLayoutTable().getCell(slotId, rowId, colId);
	}

	/**
	 * Return a cell element with the given poistion. Uses this method to find cells
	 * in Table Header, Detail and Footer slots.
	 * 
	 * @param groupLevel the 1-based group level
	 * @param slotId     the slot index,
	 * @param rowId      the 1-based row index
	 * @param colId      the 1-based column index
	 * @return the cell element. If no cell on the position, return
	 *         <code>null</code>.
	 */

	public CellHandle getCell(int groupLevel, int slotId, int rowId, int colId) {
		return getLayoutTable().getCell(groupLevel, slotId, rowId, colId);
	}

	/**
	 * Returns the cell at the given position. The table is viewed as be constructed
	 * by a set of flattened rows. Each row has a set of cells. Please note that the
	 * return <code>CellHandle</code> is an element that occupies the given position
	 * in the layout rendering.
	 * <p>
	 * For example, if a cell occupies the position (1, 1) and (1, 2), return
	 * <code>CellHandle</code>s with parameters (1, 1) and (1, 2) are same.
	 * 
	 * @param rowPosn the 1-based row position
	 * @param colPosn the 1-based column position
	 * @return the cell handle at the given position
	 */

	public CellHandle getCell(int rowPosn, int colPosn) {
		return getLayoutTable().getCell(rowPosn, colPosn);
	}
}
