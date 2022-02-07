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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.elements.Cell;

/**
 * Represents a row in table layout.
 */

public class LayoutRow {

	/**
	 * 0-based row index.
	 */

	private int rowId;

	/**
	 * Cells in the row.
	 */

	private List cells;

	/**
	 * The slot in which the row resides.
	 */

	private LayoutSlot container = null;

	/**
	 * Constructs a <code>LayoutRow</code> with the give index.
	 * 
	 * @param container the layout slot that this layout row resides
	 * @param rowId     the row index
	 */

	LayoutRow(LayoutSlot container, int rowId) {
		this.container = container;
		this.rowId = rowId;
		cells = new ArrayList();
	}

	/**
	 * Returns the cell with the given index.
	 * 
	 * @param colId the 0-based column index
	 * @return the cell
	 */

	public LayoutCell getLayoutCell(int colId) {
		if (colId < 0 || colId > cells.size() - 1)
			return null;

		return (LayoutCell) cells.get(colId);
	}

	/**
	 * Returns the layout cell with the given cell element.
	 * 
	 * @param cell the cell handle
	 * @return the layout cell
	 */

	protected LayoutCell getLayoutCell(CellHandle cell) {
		for (int i = 0; i < cells.size(); i++) {
			LayoutCell layoutCell = (LayoutCell) cells.get(i);
			if (layoutCell.getContent() == cell.getElement())
				return layoutCell;
		}
		return null;
	}

	/**
	 * Addes a cell to the current row.
	 * 
	 * @param cell the cell
	 */

	protected void addCell(LayoutCell cell) {
		cells.add(cell);
	}

	/**
	 * Tests whether cells are occupied within the space <code>colPos</code> and
	 * <code>colPos + colSpan - 1</code>
	 * 
	 * @param colId   the 0-based column position
	 * @param colSpan the column span
	 * @return a list containing <code>LayoutCells</code>s that are overlapped with
	 *         the check area.
	 */

	protected List checkOverlappedLayoutCells(int colId, int colSpan) {
		List retValue = new ArrayList();

		for (int i = 0; i < colSpan; i++) {
			LayoutCell cell = getLayoutCell(colId + i);
			if (cell.isUsed())
				retValue.add(cell);
		}

		return retValue;
	}

	/**
	 * Occupies cells within space <code>colId</code> and
	 * <code>colId + colSpan - 1</code>.
	 * 
	 * @param cellId          the unique cell id
	 * @param colId           the 0-based column position
	 * @param colSpan         the column span
	 * @param rowSpanOffset   the offset of the column span
	 * @param content         the cell element
	 * @param isEffectualDrop indicates whether the drop property of the cell can
	 *                        take effects.
	 */

	protected void fillCells(int cellId, int colId, int colSpan, int rowSpanOffset, Cell content,
			boolean isEffectualDrop) {
		for (int i = 0; i < colSpan; i++)
			cells.set(colId + i, new LayoutCell(this, cellId, content, rowSpanOffset, i, isEffectualDrop));
	}

	/**
	 * Occupies cells within space <code>colId</code> and
	 * <code>colId + colSpan - 1</code>.
	 * 
	 * @param cellId        the unique cell id
	 * @param colId         the 0-based column position
	 * @param colSpan       the column span
	 * @param rowSpanOffset the offset of the column span
	 * @param content       the cell element
	 */

	protected void fillDropSpannedCells(int cellId, int colId, int colSpan, int rowSpanOffset, Cell content) {
		for (int i = 0; i < colSpan; i++)
			cells.set(colId + i, new LayoutCell(this, cellId, content, rowSpanOffset, i));
	}

	/**
	 * Finds the column position for the given cell element.
	 * 
	 * @param cell the cell element
	 * @return 1-based column position
	 */

	protected int findCellColumnPos(Cell cell) {
		for (int i = 0; i < cells.size(); i++) {
			LayoutCell tmpCell = (LayoutCell) cells.get(i);
			if (tmpCell.isUsed() && cell == tmpCell.getContent()) {
				assert tmpCell.isCellStartPosition();
				return i + 1;
			}
		}
		return 0;
	}

	/**
	 * Returns the column count of the row.
	 * 
	 * @return the column count
	 */

	protected int getColumnCount() {
		return cells.size();
	}

	/**
	 * Returns the count of columns that has cell elements in the row.
	 * 
	 * @return the column count
	 */

	protected int getOccupiedColumnCount() {
		int retCount = 0;
		for (int i = 0; i < cells.size(); i++) {
			if (getLayoutCell(i).isUsed())
				retCount++;
		}
		return retCount;
	}

	/**
	 * Returns the string that shows the layout. Mainly for the debug.
	 * 
	 * @return the string that shows the layout
	 */

	public String getLayoutString() {
		if (cells.isEmpty())
			return ""; //$NON-NLS-1$

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < cells.size(); i++) {
			LayoutCell cell = (LayoutCell) cells.get(i);
			sb.append(cell.getLayoutString());
		}
		sb.append("\r\n"); //$NON-NLS-1$
		return sb.toString();
	}

	/**
	 * Returns the corresponding handle of the row element.
	 * 
	 * @return the corresponding handle of the row element
	 */

	public RowHandle getRow() {
		int rowCount = container.getRowCount();
		if (rowId + 1 > rowCount)
			return null;

		SlotHandle slot = container.getSlot();
		return (RowHandle) slot.get(rowId);
	}

	/**
	 * Returns the layout slot in which the layout row resides.
	 * 
	 * @return the layout slot
	 */

	protected LayoutSlot getContainer() {
		return container;
	}

	/**
	 * Returns <code>LayoutCell</code>s in the row. Note that modifications on the
	 * return iterator do not affect the table layout.
	 * 
	 * @return an iterator containing <code>LayoutCell</code>s.
	 */

	public Iterator layoutCellsIterator() {
		return new ArrayList(cells).iterator();
	}

	/**
	 * Returns handles of <code>Cell</code>s in the row. Note that modifications on
	 * the return iterator do not affect the table layout.
	 * 
	 * @return an iterator containing <code>CellHandle</code>s.
	 */

	public Iterator cellsIterator() {
		Set retValue = new LinkedHashSet();

		for (int i = 0; i < cells.size(); i++) {
			LayoutCell cell = (LayoutCell) cells.get(i);
			if (cell.isUsed() && cell.isCellStartPosition())
				retValue.add(cell.getCell());

		}
		return retValue.iterator();
	}

	/**
	 * Returns the row position in the its container.
	 * 
	 * @return 1-based row position
	 */

	protected int getRowPosn() {
		for (int i = 0; i < container.getRowCount(); i++) {
			if (container.getLayoutRow(i) == this)
				return i + 1;
		}

		assert false;
		return -1;
	}
}
