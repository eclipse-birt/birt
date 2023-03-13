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
 *
 */
public class Table {

	/**
	 * should we increase the columns size dynamically.
	 */
	protected boolean dynamicColumns = true;
	/**
	 * should we omit the empty cells when the cell conflict with drop areas.
	 */
	protected boolean omitEmptyCell = true;

	/**
	 * rows in the table layout
	 */
	Row[] rows;

	int rowCount;
	int colCount;

	int nextColId;

	int rowBufferSize;
	int colBufferSize;

	public Table(int rowSize, int colSize) {
		nextColId = -1;
		rowCount = 0;
		colCount = colSize;
		ensureSize(rowSize, colSize);
	}

	public Table() {
		nextColId = -1;
		rowCount = 0;
		colCount = 0;
		ensureSize(10, 10);
	}

	/**
	 * reset the table model.
	 *
	 */
	public void reset() {
		fillEmptyCells(0, 0, rowBufferSize, colBufferSize);
		nextColId = -1;
		rowCount = 0;
		colCount = 0;
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColCount() {
		return colCount;
	}

	/**
	 * create a row in the table model
	 *
	 * @param content row content
	 */
	public void createRow(Object content) {
		ensureSize(rowCount + 1, colCount);

		Row row = rows[rowCount];
		assert (row.rowId == rowCount);
		row.content = content;

		if (rowCount > 0) {
			Cell[] cells = row.cells;
			Cell[] lastCells = rows[rowCount - 1].cells;
			for (int cellId = 0; cellId < colCount; cellId++) {
				Cell cell = lastCells[cellId];
				if (cell.status == Cell.CELL_SPANED) {
					cell = cell.getCell();
				}
				if (cell.status == Cell.CELL_USED) {
					if (cell.rowSpan < 0 || cell.rowId + cell.rowSpan > rowCount) {
						cells[cellId] = Cell.createSpanCell(rowCount, cellId, cell);
					}
				}
			}
		}
		rowCount++;
		nextColId = 0;
	}

	/**
	 * create a cell in the current row.
	 *
	 * if the cell content is not empty put it into the table if the cell is empty:
	 * if the cell has been used, drop the cell else, put it into the table.
	 *
	 * @param cellId  column index of the cell.
	 * @param rowSpan row span of the cell
	 * @param colSpan col span of the cell
	 * @param content cell content
	 */
	public void createCell(int cellId, int rowSpan, int colSpan, Cell.Content content) {
		if (cellId == -1) {
			cellId = getNextEmptyCell();
		}

		int rowId = rowCount - 1;

		ensureSize(rowId + 1, cellId + 1);

		Cell cell = rows[rowId].cells[cellId];
		int status = cell.getStatus();

		if (status != Cell.CELL_EMPTY) {
			if (omitEmptyCell && (content == null || content.isEmpty())) {
				// content is empty, and the cell is used by others,
				// omit empty cell is set to true, so just skip
				// the empty one.
				return;
			}
			if (status == Cell.CELL_USED) {
				removeCell(cell);
			} else if (status == Cell.CELL_SPANED) {
				Cell used = cell.getCell();
				assert (used.status == Cell.CELL_USED);
				if (used.rowId < rowId) {
					// the conflict cell is above current row, so reduce the row
					// span.
					resizeCell(used, rowId - used.rowId, used.colSpan);
				} else {
					// the confict cell in the same row, so reduce the column
					// span.
					assert used.rowId == rowId;
					assert used.colId < cellId;
					resizeCell(used, used.rowSpan, cellId - used.colId);
				}
			}
		}
		// now the cell is empty
		colSpan = getMaxColSpan(cellId, colSpan);
		ensureSize(rowCount, cellId + colSpan);
		Cell newCell = Cell.createCell(rowId, cellId, rowSpan, colSpan, content);

		Cell[] cells = rows[rowId].cells;
		rows[rowId].cells[cellId] = newCell;
		nextColId = cellId + colSpan;
		for (cellId = cellId + 1; cellId < nextColId; cellId++) {
			cells[cellId] = Cell.createSpanCell(rowId, cellId, newCell);
		}
		if (nextColId > colCount) {
			colCount = nextColId;
		}
	}

	public void resolveDropCells() {
		if (rowCount <= 0) {
			return;
		}
		Cell[] cells = rows[rowCount - 1].cells;
		for (int cellId = 0; cellId < colCount; cellId++) {
			Cell cell = cells[cellId];
			if (cell.status == Cell.CELL_SPANED) {
				cell = cell.getCell();
			}
			if (cell.status == Cell.CELL_USED) {
				cell.rowSpan = rowCount - cell.rowId;
			}
		}
	}

	public void resolveDropCells(int bandId) {
		if (rowCount <= 0) {
			return;
		}
		Cell[] cells = rows[rowCount - 1].cells;

		for (int cellId = 0; cellId < colCount; cellId++) {
			Cell cell = cells[cellId];
			if (cell.status == Cell.CELL_SPANED) {
				cell = cell.getCell();
			}
			if (cell.status == Cell.CELL_USED) {
				if (cell.rowSpan == bandId) {
					cell.rowSpan = rowCount - cell.rowId;
				}
			}
		}
	}

	public boolean hasDropCell() {
		if (rowCount <= 0) {
			return false;
		}
		Cell[] cells = rows[rowCount - 1].cells;
		for (int cellId = 0; cellId < colCount; cellId++) {
			Cell cell = cells[cellId];
			if (cell.status == Cell.CELL_SPANED) {
				cell = cell.getCell();
			}
			if (cell.status == Cell.CELL_USED) {
				if (cell.rowSpan < 0 || cell.rowSpan > rowCount - cell.rowId) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * get the next empty cell.
	 *
	 * @return
	 */
	protected int getNextEmptyCell() {
		assert rowCount > 0;

		Cell[] cells = rows[rowCount - 1].cells;
		for (int colId = nextColId; colId < colCount; colId++) {
			if (cells[colId].status == Cell.CELL_EMPTY) {
				return colId;
			}
		}
		if (dynamicColumns) {
			return colCount;
		}
		return colCount - 1;
	}

	protected int getMaxColSpan(int colId, int colSpan) {

		int checkSize = colCount - colId;
		if (checkSize > colSpan) {
			checkSize = colSpan;
		}

		Cell[] cells = rows[rowCount - 1].cells;
		for (int i = 1; i < checkSize; i++) {
			if (cells[colId + i].status != Cell.CELL_EMPTY) {
				return i;
			}
		}

		if (dynamicColumns) {
			return colSpan;
		}
		return checkSize;
	}

	protected void ensureSize(int newRowBufferSize, int newColBufferSize) {
		if (newRowBufferSize > rowBufferSize) {
			Row[] newRows = new Row[newRowBufferSize];
			if (rows != null) {
				System.arraycopy(rows, 0, newRows, 0, rowCount);
			}
			for (int rowId = rowBufferSize; rowId < newRowBufferSize; rowId++) {
				Row row = new Row(rowId);
				Cell[] cells = new Cell[colBufferSize];
				for (int colId = 0; colId < colBufferSize; colId++) {
					cells[colId] = Cell.EMPTY_CELL;
				}
				row.cells = cells;
				newRows[rowId] = row;
			}
			rows = newRows;
			rowBufferSize = newRowBufferSize;
		}

		if (newColBufferSize > colBufferSize) {
			for (int rowId = 0; rowId < rowBufferSize; rowId++) {
				Row row = rows[rowId];
				Cell[] newCells = new Cell[newColBufferSize];
				if (row.cells != null) {
					System.arraycopy(row.cells, 0, newCells, 0, colBufferSize);
				}
				for (int colId = colBufferSize; colId < newColBufferSize; colId++) {
					newCells[colId] = Cell.EMPTY_CELL;
				}
				row.cells = newCells;
			}
			colBufferSize = newColBufferSize;
		}
	}

	/**
	 * remove the cell from table layout buffer. The grid cell used by this cell
	 * fills EMPTY_CELL.
	 *
	 * @param rowId row index
	 * @param colId column index
	 */
	protected void removeCell(Cell cell) {
		int rowId = cell.rowId;
		int colId = cell.colId;
		int rowSpan = cell.rowSpan;
		int colSpan = cell.colSpan;
		if (rowSpan < 0) {
			rowSpan = rowCount - rowId;
		}
		if (colId + colSpan > colCount) {
			colSpan = colCount - colId;
		}
		fillEmptyCells(rowId, colId, rowSpan, colSpan);
	}

	/**
	 * fill empty cells in the table.
	 *
	 * @param rowId   row index
	 * @param colId   col index
	 * @param rowSize fill area size
	 * @param colSize fill area size
	 */
	protected void fillEmptyCells(int rowId, int colId, int rowSize, int colSize) {
		int lastRowId = rowId + rowSize;
		int lastColId = colId + colSize;
		if (lastRowId > rowCount) {
			lastRowId = rowCount;
		}
		if (lastColId > colCount) {
			lastColId = colCount;
		}

		for (int i = rowId; i < lastRowId; i++) {
			Cell[] cells = rows[i].cells;
			for (int j = colId; j < lastColId; j++) {

				cells[j] = Cell.EMPTY_CELL;
			}
		}
	}

	/**
	 * we never change both the row span and col span at the same time.
	 *
	 * @param cell       the cell to be changed
	 * @param newRowSpan new row span
	 * @param newColSpan new col span
	 */
	protected void resizeCell(Cell cell, int newRowSpan, int newColSpan) {
		assert cell.status == Cell.CELL_USED;

		int rowId = cell.rowId;
		int colId = cell.colId;
		int rowSpan = cell.rowSpan;
		if (rowSpan <= 0) {
			rowSpan = rowCount - rowId;
		}

		int colSpan = cell.colSpan;

		assert rowSpan >= newRowSpan && colSpan >= newColSpan;
		fillEmptyCells(rowId, colId + newColSpan, rowSpan, colSpan - newColSpan);
		fillEmptyCells(rowId + newRowSpan, colId, rowSpan - newRowSpan, newColSpan);

		cell.colSpan = newColSpan;
		cell.rowSpan = newRowSpan;
	}

	public Cell getCell(int rowId, int colId) {
		return rows[rowId].cells[colId];
	}

	public Row getRow(int rowId) {
		return rows[rowId];
	}
}
