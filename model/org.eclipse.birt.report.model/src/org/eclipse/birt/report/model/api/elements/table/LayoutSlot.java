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
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.TableRow;

/**
 * A slot in the table. A slot is a container in which multiple rows can reside.
 */

public class LayoutSlot {

	/**
	 * The invalid group level.
	 */

	private static final int NO_GROUP = 0;

	/**
	 * The 1-based level of the group.
	 */

	private int groupLevel;

	/**
	 * Rows in the slot.
	 */

	private List rows;

	/**
	 * The maximal number of columns of rows in the slot.
	 */

	private int colCount;

	/**
	 * The 0-based index for the row currently worked on.
	 */

	private int currentRowId;

	/**
	 * The slot id.
	 */

	private int slotId;

	/**
	 * The table in which the slot resides.
	 */

	protected LayoutTable tableContainer;

	/**
	 * Constructs a <code>Slot</code> with the given column number.
	 * 
	 * @param table         the layout table
	 * @param colBufferSize the column count
	 */

	protected LayoutSlot(LayoutTable table, int colBufferSize) {
		this(table, NO_GROUP, colBufferSize);
	}

	/**
	 * Constructs a <code>Slot</code> with the given column number and the group
	 * level.
	 * 
	 * @param table         the layout table
	 * @param groupId       the group level if the slot is Group Header or Group
	 *                      Footer
	 * @param colBufferSize the column count
	 */

	protected LayoutSlot(LayoutTable table, int groupId, int colBufferSize) {
		this.groupLevel = groupId;

		rows = new ArrayList();
		this.colCount = colBufferSize;
		tableContainer = table;
	}

	/**
	 * Occupies spaces in the slot with the given cell information if applicable.
	 * 
	 * @param cellPos         column index of the cell.
	 * @param rowSpan         row span of the cell
	 * @param colSpan         col span of the cell
	 * @param content         cell content
	 * @param isEffectualDrop <code>true</code> if the drop is effectual. Otherwise
	 *                        <code>false</code>.
	 * @param cellId          the unique id of a cell in a table. If it is less or
	 *                        equal than 0, assign a new id.
	 * @return the unique cell id
	 */

	protected int addCell(int cellPos, int rowSpan, int colSpan, Cell content, boolean isEffectualDrop, int cellId) {
		assert cellPos > 0;

		int rowId = currentRowId;

		int colId = cellPos - 1;
		ensureSize(rowId + rowSpan, colId + colSpan);

		addOverlappedCells(checkOverlappedLayoutCells(rowId, colId, rowSpan, colSpan));

		int id = cellId;

		if (id <= 0)
			id = tableContainer.getNextCellId();

		assert id > 0;

		// fills the space

		fillCells(id, rowId, colId, rowSpan, colSpan, content, isEffectualDrop);

		int nextColId = colId + colSpan;
		if (nextColId > colCount)
			colCount = nextColId;

		return id;
	}

	/**
	 * Occupies spaces in the slot with the given cell information if applicable.
	 * 
	 * @param cellPos         column index of the cell.
	 * @param rowSpan         row span of the cell
	 * @param colSpan         col span of the cell
	 * @param content         cell content
	 * @param isEffectualDrop <code>true</code> if the drop is effectual. Otherwise
	 *                        <code>false</code>.
	 * @return the unique cell id
	 * 
	 */

	protected int addCell(int cellPos, int rowSpan, int colSpan, Cell content, boolean isEffectualDrop) {
		return addCell(cellPos, rowSpan, colSpan, content, isEffectualDrop, 0);
	}

	/**
	 * Checks whether cells in the given area have been occupied.
	 * 
	 * @param rowId   the row index
	 * @param colId   the column index
	 * @param rowSpan the row span
	 * @param colSpan the column span
	 * @return a list containing <code>LayoutCells</code>s that are overlapped with
	 *         the check area.
	 */

	protected List checkOverlappedLayoutCells(int rowId, int colId, int rowSpan, int colSpan) {
		List retValue = new ArrayList();

		for (int i = 0; i < rowSpan; i++) {
			LayoutRow row = (LayoutRow) rows.get(rowId + i);
			retValue.addAll(row.checkOverlappedLayoutCells(colId, colSpan));
		}

		return retValue;
	}

	/**
	 * Occupies cells within space <code>colPos</code> and
	 * <code>colPos + colSpan - 1</code>.
	 * 
	 * @param cellId
	 * 
	 * @param rowId           the row index
	 * @param colId           the column index
	 * @param rowSpan         the row span
	 * @param colSpan         the column span
	 * @param content         the cell element
	 * @param isEffectualDrop indicates whether the drop property of the cell can
	 *                        take effects.
	 */

	private void fillCells(int cellId, int rowId, int colId, int rowSpan, int colSpan, Cell content,
			boolean isEffectualDrop) {
		for (int i = 0; i < rowSpan; i++) {
			LayoutRow row = (LayoutRow) rows.get(rowId + i);
			row.fillCells(cellId, colId, colSpan, i, content, isEffectualDrop);
		}
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

	protected void addDropSpannedCells(int cellId, int colId, int colSpan, int rowSpanOffset, Cell content) {
		// in this list, must be empty.

		addOverlappedCells(checkOverlappedLayoutCells(0, colId, getRowCount(), colSpan));

		for (int i = 0; i < rows.size(); i++) {
			LayoutRow row = (LayoutRow) rows.get(i);
			row.fillDropSpannedCells(cellId, colId, colSpan, i + 1, content);
		}
	}

	private void addOverlappedCells(List overlappedAreas) {
		if (!overlappedAreas.isEmpty()) {
			// the cell has been occupied, record the error.

			// tableContainer.addOverlappedCell( content, this, rowId + 1,
			// colId + 1 );

			for (int i = 0; i < overlappedAreas.size(); i++) {
				LayoutCell layoutCell = (LayoutCell) overlappedAreas.get(i);
				LayoutRow layoutRow = layoutCell.getLayoutContainer();

				tableContainer.addOverlappedCell(layoutCell.getContent(), this, layoutRow.getRowPosn(),
						layoutCell.getColumnPosn(), layoutCell.getRowSpanOffset(), layoutCell.getColumnSpanOffset());
			}

		}
	}

	/**
	 * Makes the slot has enough space with the given row size and the column size.
	 * 
	 * @param newRowCount    the new row size
	 * @param newColumnCount the new column size
	 */

	protected void ensureSize(int newRowCount, int newColumnCount) {
		int rowCount = rows.size();

		if (newRowCount > rowCount) {
			for (int rowId = rowCount; rowId < newRowCount; rowId++) {
				LayoutRow row = new LayoutRow(this, rowId);
				for (int colId = 0; colId < colCount; colId++)
					row.addCell(LayoutCell.EMPTY_CELL);

				rows.add(row);
			}
		}

		rowCount = rows.size();
		if (newColumnCount > colCount) {
			for (int rowId = 0; rowId < rowCount; rowId++) {
				LayoutRow row = (LayoutRow) rows.get(rowId);
				for (int colId = colCount; colId < newColumnCount; colId++) {
					row.addCell(LayoutCell.EMPTY_CELL);
				}
			}
		}
	}

	/**
	 * Creates a row in the slot.
	 * 
	 * @param row the row element
	 */

	protected void newLayoutRow(TableRow row) {
		int rowCount = rows.size();

		if (rowCount == 0 || rowCount == currentRowId + 1)
			ensureSize(rowCount + 1, colCount);

		if (rowCount != 0)
			currentRowId++;
	}

	/**
	 * Gets the column count of the slot.
	 * 
	 * @return he column count of the slot.
	 */

	protected int getColumnCount() {
		return colCount;
	}

	/**
	 * Returns the row with the give index.
	 * 
	 * @param rowId the 0-based row index
	 * @return the row
	 */

	protected LayoutRow getLayoutRow(int rowId) {
		if (rowId >= rows.size())
			return null;

		return (LayoutRow) rows.get(rowId);
	}

	/**
	 * Returns the current row worked on.
	 * 
	 * @return the current row
	 */

	protected LayoutRow getCurrentLayoutRow() {
		return getLayoutRow(currentRowId);
	}

	/**
	 * Returns the group level of the slot if this slot is a Group Header or Group
	 * Footer slot.
	 * 
	 * @return the 1-based group level. The 0 indicates the slot is not in the
	 *         group.
	 */

	protected int getGroupLevel() {
		return groupLevel;
	}

	/**
	 * Returns the row count in the slot.
	 * 
	 * @return the row count in the slot
	 */

	public int getRowCount() {
		if (rows.isEmpty())
			return 0;

		return currentRowId + 1;
	}

	/**
	 * Returns 1-based the column position with the given row index and the cell
	 * element.
	 * 
	 * @param rowId the row index
	 * @param cell  the cell to search
	 * @return 1-based the column position
	 */

	protected int getColumnPos(int rowId, Cell cell) {
		if (rowId < 0 || rowId >= rows.size())
			return 0;

		LayoutRow row = getLayoutRow(rowId);
		return row.findCellColumnPos(cell);
	}

	/**
	 * Return the layout cell with the given row and column index.
	 * 
	 * @param rowId the 0-based row index
	 * @param colId the 0-based column index
	 * @return the layout cell with the given position
	 */

	public LayoutCell getLayoutCell(int rowId, int colId) {
		if (rowId < 0 || rowId > getRowCount() - 1)
			return null;

		LayoutRow row = (LayoutRow) getLayoutRow(rowId);
		return row.getLayoutCell(colId);
	}

	/**
	 * Return the layout cell with the given row and column index.
	 * 
	 * @param rowId the 0-based row index
	 * @param cell  the cell element handle
	 * @return the layout cell with the given position
	 */

	protected LayoutCell getLayoutCell(int rowId, CellHandle cell) {
		if (rowId < 0 || rowId > getRowCount() - 1)
			return null;

		LayoutRow row = (LayoutRow) getLayoutRow(rowId);
		return row.getLayoutCell(cell);
	}

	/**
	 * Returns the string that shows the layout. Mainly for the debug.
	 * 
	 * @return the string that shows the layout
	 */

	public String getLayoutString() {
		if (rows.isEmpty())
			return ""; //$NON-NLS-1$

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < rows.size(); i++) {
			LayoutRow row = (LayoutRow) rows.get(i);
			sb.append(row.getLayoutString());
		}

		return sb.toString();
	}

	/**
	 * Returns the slot handle of the layout slot.
	 * 
	 * @return the slot handle of the layout slot
	 */

	public SlotHandle getSlot() {
		if (groupLevel == 0)
			return new SlotHandle(tableContainer.getTable(), slotId);

		SlotHandle slots = tableContainer.getTable().getGroups();
		TableGroupHandle group = (TableGroupHandle) slots.get(groupLevel);

		return group.getSlot(slotId);
	}

	/**
	 * Returns the handle of the group that contains this slot if applicable.
	 * 
	 * @return the handle of the group
	 */

	public TableGroupHandle getGroup() {
		if (groupLevel < 1)
			return null;

		SlotHandle slots = tableContainer.getTable().getGroups();
		TableGroupHandle group = (TableGroupHandle) slots.get(groupLevel);

		return group;
	}

	/**
	 * Returns the id of the slot. The return value can be one of the following:
	 * 
	 * <ul>
	 * <li><code>TableItem.HEADER_SLOT</code></li>
	 * <li><code>TableItem.DETAIL_SLOT</code></li>
	 * <li><code>TableItem.FOOTER_SLOT</code></li>
	 * <li><code>TableGroup.HEADER_SLOT</code></li>
	 * <li><code>TableGroup.FOOTER_SLOT</code></li>
	 * </ul>
	 * 
	 * @return the id of the slot
	 */

	public int getSlotId() {
		return slotId;
	}

	/**
	 * Returns <code>LayoutRow</code>s in the row. Note that modifications on the
	 * return iterator do not affect the table layout.
	 * 
	 * @return an iterator containing <code>LayoutRow</code>s.
	 */

	public Iterator layoutRowsIterator() {
		return new ArrayList(rows.subList(0, currentRowId)).iterator();
	}

	/**
	 * Returns handles of <code>Row</code>s in the row. Note that modifications on
	 * the return iterator do not affect the table layout.
	 * 
	 * @return an iterator containing <code>RowHandle</code>s.
	 */

	public Iterator rowsIterator() {
		Set retValue = new LinkedHashSet();

		for (int i = 0; i < currentRowId; i++) {
			LayoutRow row = (LayoutRow) rows.get(i);
			retValue.add(row.getRow());

		}
		return retValue.iterator();
	}

}
