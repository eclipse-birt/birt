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

package org.eclipse.birt.report.model.api;

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.interfaces.ITableRowModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.util.CommandLabelFactory;

/**
 * Provides the insert and paste operation to the column band in the grid/table.
 * 
 */

class ColumnBandInsertAction extends ColumnBandCopyAction {

	/**
	 * The target position is one after specified column.
	 */

	private static final int INSERT_BEFORE = -1;

	/**
	 * The minimal value of the column span.
	 */

	private static final int MIN_ROW_SPAN = 1;

	/**
	 * 0-based column index.
	 */

	private int targetColumnIndex;

	private List originalCells = null;

	/**
	 * The row span for the last cell of which the column span increases.
	 */

	private int rowSpanForModifiedCell = MIN_ROW_SPAN;

	/**
	 * Constructs the insert action.
	 * 
	 * @param adapter the column adapter. Either table or grid.
	 */

	public ColumnBandInsertAction(ColumnBandAdapter adapter) {
		super(adapter);
	}

	/**
	 * Checks whether the paste operation can be done with the given copied column
	 * band data, the column index and the operation flag.
	 * 
	 * @param columnIndex the column index
	 * @param insertFlag  The column insert sign. 1 insert after position. -1 insert
	 *                    before position
	 * @return <code>true</code> indicates the paste operation can be done.
	 *         Otherwise <code>false</code>.
	 */

	protected boolean canInsert(int columnIndex, int insertFlag) {
		// if table has parent, its layout can't be changed. so can't do insert
		// operation.

		if (adapter.hasParent())
			return false;

		int columnCount = adapter.getColumnCount();

		targetColumnIndex = columnIndex;
		if (insertFlag == INSERT_BEFORE)
			targetColumnIndex = targetColumnIndex - 1;

		if (targetColumnIndex > columnCount)
			targetColumnIndex = columnCount;

		// must be >=, since if the columnIndex == columnCount. It means that
		// the column band is supposed to be appended at the far right-end of
		// table.

		if (targetColumnIndex >= columnCount || targetColumnIndex < 1) {
			// for this case, we only focus on the slot layout information, no
			// sense to check the row number

			originalCells = getCellsContextInfo(adapter.getCellsUnderColumn(1));
		} else {
			originalCells = getCellsContextInfo(adapter.getCellsUnderColumn(targetColumnIndex, false));

			if (!isValidInsertAndPasteArea(originalCells))
				return false;
		}

		return true;
	}

	/**
	 * Inserts a copied column to the given column index.
	 * 
	 * @param columnIndex the column index
	 * @param insertFlag  The column insert sign. 1 insert after position. -1 insert
	 *                    before position
	 * @return a list containing post-parsing errors. Each element in the list is
	 *         <code>ErrorDetail</code>.
	 * @throws SemanticException if layouts of slots are different.
	 */

	protected List insertColumnBand(int columnIndex, int insertFlag) throws SemanticException {
		boolean canDone = canInsert(columnIndex, insertFlag);

		if (!canDone)
			throw new SemanticError(adapter.getElementHandle().getElement(),
					new String[] { adapter.getElementHandle().getName() },
					SemanticError.DESIGN_EXCEPTION_COLUMN_INSERT_FORBIDDEN);

		TableColumn column = new TableColumn();

		ActivityStack as = adapter.getModule().getActivityStack();

		as.startSilentTrans(CommandLabelFactory.getCommandLabel(MessageConstants.INSERT_COLUMN_BAND_MESSAGE));
		try {
			pasteColumn(column, targetColumnIndex, true);
			insertCells(targetColumnIndex);
		} catch (SemanticException e) {
			as.rollback();
			throw e;
		}
		as.commit();

		return Collections.EMPTY_LIST;
	}

	/**
	 * Checks whether copied cells can be inserted and pasted.
	 * 
	 * @param cells cloned cells
	 * @return <code>true</code> if the row count matches the count of "rowSpans" in
	 *         <code>cells</code>, otherwise <code>false</code>.
	 * 
	 */

	private boolean isValidInsertAndPasteArea(List cells) {
		int numOfRows = adapter.getRowCount();
		int rowCount = 0;

		for (int i = 0; i < cells.size(); i++) {
			CellContextInfo contextInfo = (CellContextInfo) cells.get(i);
			rowCount += contextInfo.getRowSpan();
		}

		if (rowCount < numOfRows)
			return false;

		return true;
	}

	/**
	 * Inserts a new column band to the table/grid. If has a cell with colSpan > 1
	 * at the insert position, increases the colSpan.
	 * 
	 * @param originalCells a list containing cells that is to be deleted.
	 * @param columnIndex   the column index where copied cells are pasted
	 * @param isInsert      <code>true</code> if this is an insert and paste action.
	 *                      Otherwise <code>false</code>.
	 * @throws SemanticException if any error occurs during pasting cells.
	 */

	private void insertCells(int columnIndex) throws SemanticException {

		int slotCount = adapter.getElementHandle().getDefn().getSlotCount();

		// adds the copied cells to the destination.

		for (int i = 0; i < slotCount; i++) {
			SlotHandle slot = adapter.getElementHandle().getSlot(i);
			resetRowSpanForModifiedCell();
			for (int j = 0; j < slot.getCount(); j++) {
				DesignElementHandle content = slot.get(j);
				if (content instanceof RowHandle) {
					insertCell((RowHandle) content, columnIndex);
				}

				if (content instanceof GroupHandle) {
					insertCellsInGroup((GroupHandle) content, columnIndex);
				}
			}

		}

	}

	/**
	 * Inserts a new cell to the given column position of the given row.
	 * 
	 * @param row         the table row
	 * @param columnIndex the 0-based column number
	 * @throws SemanticException
	 */

	private void insertCell(RowHandle row, int columnIndex) throws SemanticException {
		assert row != null;

		// get correct insertion position information

		int pos;
		if (columnIndex == 0)
			pos = 0;
		else if (columnIndex == adapter.getColumnCount() - 1)
			pos = -1;
		else {
			if (rowSpanForModifiedCell > MIN_ROW_SPAN) {
				rowSpanForModifiedCell--;
				return;
			}

			CellHandle cell = findCell(row, columnIndex);
			if (cell == null)
				return;

			CellHandle nextCell = findCell(row, columnIndex + 1);

			if (cell.getColumnSpan() != 1 && nextCell == cell) {
				cell.setColumnSpan(cell.getColumnSpan() + 1);
				rowSpanForModifiedCell = cell.getRowSpan();

				return;
			}

			pos = cell.getContainerSlotHandle().findPosn(cell) + 1;
		}

		if (pos != -1)
			row.addElement(new Cell().getHandle(adapter.getModule()), ITableRowModel.CONTENT_SLOT, pos);
		else
			row.addElement(new Cell().getHandle(adapter.getModule()), ITableRowModel.CONTENT_SLOT);

	}

	/**
	 * Inserts a new cell to the given column position of rows in the given gruop.
	 * 
	 * @param group       the table group
	 * @param columnIndex the 0-based column number
	 * @throws SemanticException
	 */

	private void insertCellsInGroup(GroupHandle group, int columnIndex) throws SemanticException {
		assert group != null;

		int slotCount = group.getDefn().getSlotCount();
		for (int i = 0; i < slotCount; i++) {
			SlotHandle slot = group.getSlot(i);
			resetRowSpanForModifiedCell();

			for (int j = 0; j < slot.getCount(); j++) {
				DesignElementHandle content = slot.get(j);
				if (content instanceof RowHandle)
					insertCell((RowHandle) content, columnIndex);
			}
		}
	}

	/**
	 * 
	 */

	private void resetRowSpanForModifiedCell() {
		rowSpanForModifiedCell = MIN_ROW_SPAN;
	}

	/**
	 * Returns the cell resides in the row.
	 * 
	 * @param row            the row handle
	 * @param columnToInsert the column number to insert, count from 1
	 * @param insert         whether insert mode
	 * @return the cell in the given position
	 */

	private CellHandle findCell(RowHandle row, int columnToInsert) {
		SlotHandle cells = row.getCells();

		for (int i = 0; i < cells.getCount(); i++) {
			CellHandle cell = (CellHandle) cells.get(i);
			int cellPos = adapter.getCellPosition(cell);

			// found the cell

			if (columnToInsert == cellPos)
				return cell;

			else if (rowSpanForModifiedCell <= MIN_ROW_SPAN && columnToInsert < cellPos + cell.getColumnSpan())
				return cell;

			// there was no corresponding cell on this row, should paste/insert
			// on this position.

			else if (columnToInsert > cellPos && columnToInsert < cellPos + cell.getColumnSpan())
				return cell;
		}

		// not return yet, paste/insert to the end of this row.
		return null;
	}
}
