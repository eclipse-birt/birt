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

	/**
	 * Content interface
	 *
	 * @since 3.3
	 *
	 */
	public interface Content {

		/**
		 * Check if the content is empty
		 *
		 * @return Return the check result if the content is empty
		 */
		boolean isEmpty();

		/**
		 * Reset the content
		 */
		void reset();
	}

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

	/**
	 * Get the status
	 *
	 * @return Return the status
	 */
	public int getStatus() {
		return this.status;
	}

	/**
	 * Get the cell content
	 *
	 * @return Return the cell content
	 */
	public Content getContent() {
		Cell cell = getCell();
		return (Content) cell.content;
	}

	/**
	 * Get the row id
	 *
	 * @return Return the row id
	 */
	public int getRowId() {
		if (status == CELL_SPANED) {
			Cell cell = getCell();
			if (cell != null) {
				return cell.getRowId();

			}
		}
		return rowId;
	}

	/**
	 * Get the column id
	 *
	 * @return Return the column id
	 */
	public int getColId() {
		if (status == CELL_SPANED) {
			Cell cell = getCell();
			if (cell != null) {
				return cell.getColId();

			}
		}
		return colId;
	}

	/**
	 * Get the left row span
	 *
	 * @return Return the left row span
	 */
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

	/**
	 * Get the row span
	 *
	 * @return Return the row span
	 */
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

	/**
	 * Get the column span
	 *
	 * @return Return the column span
	 */
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
