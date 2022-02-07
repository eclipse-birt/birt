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

import org.eclipse.birt.report.model.core.Module;

/**
 * Represents an object for copy/paste in Gird/Table. The copy/paste between
 * Grid/Table must follow the following rules:
 * 
 * <ul>
 * <li>Copy/paste operations must occur among the same type of elements, like
 * among grid elements. A copy/paste operation between Grid/Table is not
 * allowed.
 * <li>Current copy/paste operations do not support cells with "drop"
 * properties.
 * <li>Each time, only one column can be copied/pasted.
 * <li>Slot layouts between the source grid/table and the target grid/table must
 * be same.
 * </ul>
 * 
 */

abstract class ColumnBandAdapter {

	/**
	 * Returns the element where the copy/paste operation occurs.
	 * 
	 * @return the element
	 */

	protected abstract ReportItemHandle getElementHandle();

	/**
	 * Returns the module where the element belongs to.
	 * 
	 * @return the module
	 */

	protected Module getModule() {
		return getElementHandle().getModule();
	}

	/**
	 * Returns the column slot.
	 * 
	 * @return the column slot
	 */

	protected abstract SlotHandle getColumns();

	/**
	 * Returns the number of columns in the element.
	 * 
	 * @return the number of columns in the element
	 */

	protected abstract int getColumnCount();

	/**
	 * 
	 */

	ColumnBandAdapter() {
	}

	/**
	 * Returns the column index that is the start column index of the
	 * <code>target</code>.
	 * 
	 * @param target the column to find
	 * @return a column index
	 */

	protected static int getColumnStartPos(ColumnHandle target) {
		SlotHandle columns = target.getContainerSlotHandle();

		int colStartPos = 1;
		int colPosInSlot = columns.findPosn(target);
		for (int i = 0; i < colPosInSlot; i++) {
			ColumnHandle col = (ColumnHandle) columns.get(i);
			colStartPos += col.getRepeatCount();
		}

		return colStartPos;
	}

	/**
	 * Returns the row with the given slot id, group id and the row number.
	 * 
	 * @param slotId    the slot id
	 * @param groupId   the group id
	 * @param rowNumber the row number
	 * @return the row that matches the input parameters
	 */

	abstract protected RowHandle getRow(int slotId, int groupId, int rowNumber);

	/**
	 * Returns the position where the cell resides in the row.
	 * 
	 * @param row            the row handle
	 * @param columnToInsert the column number to insert, count from 1
	 * @param insert         whether insert mode
	 * @return the position indexing from 1
	 */

	protected int findCellPosition(RowHandle row, int columnToInsert, boolean insert) {
		SlotHandle cells = row.getCells();

		for (int i = 0; i < cells.getCount(); i++) {
			CellHandle cell = (CellHandle) cells.get(i);
			int cellPos = getCellPosition(cell);

			// found the cell
			if (columnToInsert == cellPos)
				return insert ? (i + 1) : i;
			// there was no corresponding cell on this row, should paste/insert
			// on this position.
			else if (columnToInsert < cellPos)
				return i;
		}

		// not return yet, paste/insert to the end of this row.
		return -1;
	}

	/**
	 * Returns the number of rows in the element.
	 * 
	 * @return the number or rows in the element.
	 */

	abstract protected int getRowCount();

	/**
	 * Checks whether any cell in <code>cells</code> has a value of
	 * <code>DesignChoiceConstants#DROP_TYPE_DETAIL</code> or
	 * <code>DesignChoiceConstants#DROP_TYPE_ALL</code> for the "drop" property.
	 * 
	 * @param cells a list containing cell handles
	 * @return <code>true</code> if any cell has the "drop" property, otherwise
	 *         <code>false</code>.
	 */

	abstract protected boolean hasDroppingCell(List cells);

	abstract protected boolean isDroppingCell(CellContextInfo cell);

	/**
	 * Returns copied cells with the column number.
	 * 
	 * @param columnNumber the column number
	 * @return new cell instances
	 */

	protected List getCellsUnderColumn(int columnNumber) {
		return getCellsUnderColumn(columnNumber, true);
	}

	/**
	 * Returns copied cells with the column number regardless whether the current
	 * position is where the cell element begins to span.
	 * 
	 * 
	 * @param columnNumber        the column number
	 * @param mustBeStartPosition <code>true</code> if it is. Otherwise
	 *                            <code>false</code>.
	 * @return the matched cell
	 */

	abstract protected List getCellsUnderColumn(int columnNumber, boolean mustBeStartPosition);

	/**
	 * Returns copied cells with the given slot and column number.
	 * 
	 * @param handle              the slot
	 * @param columnIndex         the column number
	 * @param mustBeStartPosition <code>true</code> if it is. Otherwise
	 *                            <code>false</code>.
	 * @return new cell instances
	 */

	protected List getCellsInSlot(SlotHandle handle, int columnIndex, boolean mustBeStartPosition) {
		List retValue = new ArrayList();

		for (int i = 0; i < handle.getCount(); i++) {
			RowHandle row = (RowHandle) handle.get(i);
			CellHandle cell = getCellsInRow(row, columnIndex, mustBeStartPosition);
			if (cell != null)
				retValue.add(cell);
		}
		return retValue;
	}

	/**
	 * Returns the column number with a given cell.
	 * 
	 * @param cell the cell to find.
	 * @return the column number of the given cell.
	 */

	abstract protected int getCellPosition(CellHandle cell);

	/**
	 * Returns a copied cell with the given row and column number.
	 * 
	 * @param row                 the row
	 * @param columnIndex         the column number
	 * @param mustBeStartPosition <code>true</code> if it is. Otherwise
	 *                            <code>false</code>.
	 * @return a new cell instance
	 */

	private CellHandle getCellsInRow(RowHandle row, int columnIndex, boolean mustBeStartPosition) {
		SlotHandle cells = row.getCells();

		for (int i = 0; i < cells.getCount(); i++) {
			CellHandle cell = (CellHandle) cells.get(i);
			int cellColumnIndex = getCellPosition(cell);

			if (cellColumnIndex == columnIndex)
				return cell;

			if (!mustBeStartPosition && cellColumnIndex < columnIndex
					&& cellColumnIndex + cell.getColumnSpan() >= columnIndex)
				return cell;
		}

		return null;
	}

	/**
	 * Returns a list containing rows.
	 * 
	 * @return a list containing rows.
	 */

	abstract protected List getRowContainerSlots();

	/**
	 * Checks element has parent or not.
	 * 
	 * @return <code>true</code>if has parent, else return <code>false</code>
	 */

	protected final boolean hasParent() {
		if (getElementHandle().getElement().isVirtualElement() || (getElementHandle().getExtends() != null)) {
			return true;
		}
		return false;
	}
}
