/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
 * CELL in table layout
 * 
 */
public class Cell {

	public interface Content {

		boolean isEmpty();

		void reset();
	};

	final static Cell EMPTY_CELL = new Cell(Cell.CELL_EMPTY);
	/**
	 * CELL is empty
	 */
	public static final int CELL_EMPTY = 0;
	/**
	 * CELL is used, it contains a CELL
	 */
	public static final int CELL_USED = 1;
	/**
	 * CELL is used, it is spaned by another CELL.
	 */
	public static final int CELL_SPANED = 2;

	int status;
	int rowId;
	int colId;
	int rowSpan = 1;
	int colSpan = 1;
	Object content;
	Cell cell;

	static Cell createCell(int rowId, int colId, int rowSpan, int colSpan, Content content) {
		Cell cell = new Cell(CELL_USED);
		cell.rowId = rowId;
		cell.colId = colId;
		cell.rowSpan = rowSpan;
		cell.colSpan = colSpan;
		cell.content = content;
		return cell;
	}

	static Cell createSpanCell(int rowId, int colId, Cell cell) {
		assert cell.status == CELL_USED;
		Cell span = new Cell(CELL_SPANED);
		span.rowId = rowId;
		span.colId = colId;
		span.content = cell;
		return span;
	}

	private Cell(int status) {
		this.status = status;
	}

	Cell getCell() {
		if (status == CELL_SPANED) {
			return (Cell) content;
		}
		return this;
	}

	public int getStatus() {
		return this.status;
	}

	public Content getContent() {
		Cell cell = getCell();
		return (Content) cell.content;
	}

	public int getRowId() {
		if (status == CELL_SPANED) {
			Cell cell = getCell();
			if (cell != null) {
				return cell.getRowId();

			}
		}
		return rowId;
	}

	public int getColId() {
		if (status == CELL_SPANED) {
			Cell cell = getCell();
			if (cell != null) {
				return cell.getColId();

			}
		}
		return colId;
	}

	public int getLeftRowSpan() {
		if (status == CELL_USED) {
			return rowSpan;
		} else if (status == CELL_SPANED) {
			Cell cell = getCell();
			if (cell != null) {
				int originalRowSpan = cell.getRowSpan();
				if (originalRowSpan > 0) {
					return originalRowSpan + cell.getRowId() - rowId;
				}
				return originalRowSpan;

			}
		}
		return rowSpan;
	}

	public int getRowSpan() {
		if (status == CELL_USED) {
			return rowSpan;
		} else if (status == CELL_SPANED) {
			Cell cell = getCell();
			if (cell != null) {
				int originalRowSpan = cell.getRowSpan();
				if (originalRowSpan > 0) {
					return originalRowSpan + cell.getRowId() - rowId;
				}
				return originalRowSpan;

			}
		}
		return rowSpan;
	}

	public int getColSpan() {
		if (status == CELL_USED) {
			return colSpan;
		} else if (status == CELL_SPANED) {
			Cell cell = getCell();
			if (cell != null) {
				return cell.getColSpan();

			}
		}
		return colSpan;
	}
}
