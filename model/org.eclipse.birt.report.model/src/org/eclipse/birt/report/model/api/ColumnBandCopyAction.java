/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.ColumnHelper;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.interfaces.ICellModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableColumnModel;
import org.eclipse.birt.report.model.elements.interfaces.ITableRowModel;

/**
 * Provides the copy operation to the column band in the grid/table.
 * 
 */

abstract class ColumnBandCopyAction extends ColumnBandAction {

	/**
	 * Constructs a <code>ColumnBandCopyAction</code> for the copy action.
	 * 
	 * @param adapter the adapter to work on tables and grids.
	 * 
	 */

	public ColumnBandCopyAction(ColumnBandAdapter adapter) {
		super(adapter);
	}

	/**
	 * Copies the column object and cells under it to the adapter.
	 * 
	 * @param columnNumber the column number
	 * @return the copied column band that includes the copied column and cells
	 * @throws SemanticException if the copy operation on the column
	 *                           <code>columnNumber</code> is forbidden.
	 */

	protected ColumnBandData copyColumnBand(int columnNumber) throws SemanticException {
		ColumnBandData data = new ColumnBandData();

		if (columnNumber <= 0)
			return null;

		TableColumn clonedColumn = copyColumn(adapter.getColumns(), columnNumber);
		List cells = cloneCells(adapter.getCellsUnderColumn(columnNumber), columnNumber);

		data.setColumn(clonedColumn);
		data.setCells(cells);

		if (!isRectangleArea(cells, 1))
			throw new SemanticError(adapter.getElementHandle().getElement(),
					new String[] { Integer.toString(columnNumber), adapter.getElementHandle().getName() },
					SemanticError.DESIGN_EXCEPTION_COLUMN_COPY_FORBIDDEN);

		if (adapter.hasDroppingCell(cells))
			throw new SemanticError(adapter.getElementHandle().getElement(),
					new String[] { Integer.toString(columnNumber), adapter.getElementHandle().getName() },
					SemanticError.DESIGN_EXCEPTION_COLUMN_COPY_FORBIDDEN);

		return data;
	}

	/**
	 * Makes new copies of a list of cell handles with the given column number.
	 * 
	 * @param cells        a list of cells.
	 * @param columnNumber the column number
	 * @return a list containing new cloned cells
	 */

	private List cloneCells(List cells, int columnNumber) {
		List list = new ArrayList();

		for (int i = 0; i < cells.size(); i++) {
			CellHandle originalCell = (CellHandle) cells.get(i);
			Cell clonedCell = null;

			try {
				clonedCell = (Cell) originalCell.getElement().clone();
			} catch (CloneNotSupportedException e) {
				assert false;
			}

			// clears the column property in the cell is not useful here.

			list.add(getCellContextInfo(clonedCell, (RowHandle) originalCell.getContainer()));
		}
		return list;
	}

	/**
	 * Returns the context information of a cell. The cell must reside in a valid
	 * row container.
	 * 
	 * @param cell the cell handle
	 * @param row  the row that contains the context information
	 * @return a new <code>CellContextInfo</code> object
	 */

	protected CellContextInfo getCellContextInfo(Cell cell, RowHandle row) {
		DesignElementHandle rowContainer = row.getContainer();
		int slotId = rowContainer.findContentSlot(row);
		int groupId = -1;
		SlotHandle slot = rowContainer.getSlot(slotId);

		if (rowContainer instanceof TableGroupHandle) {
			TableHandle rowGrandPa = (TableHandle) rowContainer.getContainer();
			groupId = rowGrandPa.getGroups().findPosn(rowContainer);
		}

		CellContextInfo cellInfo = new CellContextInfo(cell, cell.getRowSpan(adapter.getModule()),
				cell.getColSpan(adapter.getModule()),
				cell.getStringProperty(adapter.getModule(), ICellModel.DROP_PROP));

		int rowNumber = slot.findPosn(row);
		cellInfo.setContainerDefnName(rowContainer.getDefn().getName());
		cellInfo.setSlotId(slotId);
		cellInfo.setGroupId(groupId);
		cellInfo.setRowIndex(rowNumber);

		return cellInfo;
	}

	/**
	 * Returns insert positions of <code>copiedCells</code> if the cells are
	 * inserted to the beginning of row or at the end of the row. Each element in
	 * the return value is an integer, which can be
	 * 
	 * <ul>
	 * <li>0 -- insert to the beginning of row
	 * <li>-1 -- insert to the end of the row
	 * </ul>
	 * 
	 * And for other cases, the position is not calculated here.
	 * 
	 * @param size        the size of the array to be return.
	 * @param columnIndex the column index where copied cells are pasted
	 * @param isInsert    <code>true</code> if this is an insert and paste action.
	 *                    Otherwise <code>false</code>.
	 * 
	 * @return an array containing insert positions
	 */

	private int[] getInsertPosition(int size, int columnIndex, boolean isInsert) {
		// insert column index that is from 1.

		int[] insertPosition = null;

		int columnCount = adapter.getColumnCount();
		if (isInsert && (columnIndex == 0 || columnIndex == columnCount - 1)) {
			insertPosition = new int[size];

			if (columnIndex == 0)
				Arrays.fill(insertPosition, 0);
			else
				Arrays.fill(insertPosition, -1);
		}

		return insertPosition;
	}

	/**
	 * Performs insert and paste or paste operations. Removes cells in
	 * <code>originalCells</code> if <code>isInsert</code> is <code>true</code>.
	 * Then inserts cells in <code>copiedCells</code> to the element.
	 * 
	 * @param copiedCells   a list containing cells that is to be inserted.
	 * @param originalCells a list containing cells that is to be deleted.
	 * @param columnIndex   the column index where copied cells are pasted
	 * @param isInsert      <code>true</code> if this is an insert and paste action.
	 *                      Otherwise <code>false</code>.
	 * @throws SemanticException if any error occurs during pasting cells.
	 */

	protected void pasteCells(List copiedCells, List originalCells, int columnIndex, boolean isInsert)
			throws SemanticException {

		// get the insertion positions if the column is inserted to the head or
		// the end of the table.

		int[] insertPosition = getInsertPosition(copiedCells.size(), columnIndex, isInsert);

		// remove cells first.

		for (int i = 0; !isInsert && i < originalCells.size(); i++) {
			CellContextInfo contextInfo = (CellContextInfo) originalCells.get(i);
			CellHandle cell = contextInfo.getCell().handle(adapter.getModule());
			if (!isInsert)
				cell.getContainerSlotHandle().drop(cell);
		}

		// adds the copied cells to the destination.

		for (int i = 0; i < copiedCells.size(); i++) {

			CellContextInfo contextInfo = (CellContextInfo) copiedCells.get(i);

			// groupId is equal to -1, means this is a top slot in the table

			RowHandle row = adapter.getRow(contextInfo.getSlotId(), contextInfo.getGroupId(),
					contextInfo.getRowIndex());

			assert row != null;

			// get correct insertion position information

			int pos;
			if (insertPosition == null)
				pos = adapter.findCellPosition(row, columnIndex, isInsert);
			else
				pos = insertPosition[i];

			// to avoid duplicate names in the same name space, rename nested
			// elements here.

			CellHandle cell = contextInfo.getCell().handle(adapter.getModule());
			adapter.getModule().getModuleHandle().rename(cell);

			if (pos != -1)
				row.addElement(cell, ITableRowModel.CONTENT_SLOT, pos);
			else
				row.addElement(cell, ITableRowModel.CONTENT_SLOT);
		}
	}

	/**
	 * Copies a column with the given column slot and the column number.
	 * 
	 * @param columns     the column slot
	 * @param columnIndex the column number
	 * @return a new column instance
	 */

	protected TableColumn copyColumn(SlotHandle columns, int columnIndex) {
		TableColumn column = ColumnHelper.findColumn(adapter.getModule(), columns.getSlot(), columnIndex);

		if (column == null)
			return null;

		TableColumn clonedColumn = null;

		try {
			clonedColumn = (TableColumn) column.clone();
			clonedColumn.setProperty(ITableColumnModel.REPEAT_PROP, Integer.valueOf(1));
		} catch (CloneNotSupportedException e) {
			assert false;
		}

		return clonedColumn;
	}

}
