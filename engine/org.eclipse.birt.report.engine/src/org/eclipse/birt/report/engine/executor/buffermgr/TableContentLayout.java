/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.executor.buffermgr;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.impl.EngineTask;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IColumn;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.internal.content.wrap.CellContentWrapper;
import org.eclipse.birt.report.engine.internal.content.wrap.TableContentWrapper;
import org.eclipse.birt.report.engine.layout.LayoutUtil;
import org.eclipse.birt.report.engine.layout.html.HTMLLayoutContext;
import org.eclipse.birt.report.engine.layout.html.HTMLTableLayoutEmitter.CellContent;
import org.eclipse.birt.report.engine.presentation.UnresolvedRowHint;

/**
 * Table content layout
 *
 * @since 3.3
 *
 */
public class TableContentLayout {

	/** property: maximum row span */
	public final Integer MAX_ROW_SPAN = 10000;
	/**
	 * rows in the table layout
	 */
	Row[] rows;

	int rowCount;
	int colCount;
	int realColCount;

	int rowBufferSize;
	int colBufferSize;

	boolean isRowHidden;

	String format;
	ArrayList hiddenColumnIds = new ArrayList();
	ArrayList<IColumn> visibleColumns = new ArrayList<IColumn>();

	protected UnresolvedRowHint rowHint;

	protected Row lastRow = null;

	protected boolean formalized = false;

	protected HTMLLayoutContext context;

	private TableContentWrapper wrappedTable;

	private ITableContent tableContent;

	private boolean hasHiddenColumns = false;

	private int leastColumnIdToBeAjusted = 0;

	private int[] adjustedColumnIds;

	protected String keyString;

	protected boolean needFormalize = false;

	/**
	 * Constructor
	 *
	 * @param tableContent table content
	 * @param format       format
	 * @param context      HTML layout context
	 * @param keyString    key string
	 */
	public TableContentLayout(ITableContent tableContent, String format, HTMLLayoutContext context, String keyString) {
		this.format = format;
		this.context = context;
		this.tableContent = tableContent;
		this.keyString = keyString;

		this.colCount = tableContent.getColumnCount();

		String tableId = tableContent.getInstanceID().toUniqueString();
		List<?> hints = context.getPageHintManager().getTableColumnHint(tableId);

		this.adjustedColumnIds = new int[colCount];
		for (int i = 0; i < colCount; i++) {
			adjustedColumnIds[i] = -1;
		}

		if (hints.size() > 0) {
			int current = -1;
			Iterator<?> iter = hints.iterator();
			while (iter.hasNext()) {
				int[] hint = (int[]) iter.next();
				for (int i = hint[0]; i < hint[1]; i++) {
					IColumn column = tableContent.getColumn(i);
					if (!isColumnHidden(column)) {
						visibleColumns.add(column);
						current++;
					}
					adjustedColumnIds[i] = (current >= 0 ? current : 0);
				}
			}
			int maxColId = Math.max(0, current);
			current = -1;
			for (int i = 0; i < colCount; i++) {
				if (adjustedColumnIds[i] == -1) {
					adjustedColumnIds[i] = Math.min(maxColId, current + 1);
				} else {
					current = adjustedColumnIds[i];
				}
				if (!hasHiddenColumns) {
					if (i != adjustedColumnIds[i]) {
						hasHiddenColumns = true;
						leastColumnIdToBeAjusted = i;
					}
				}
			}
		} else {
			int current = -1;
			for (int i = 0; i < colCount; i++) {
				IColumn column = tableContent.getColumn(i);

				if (!isColumnHidden(column)) {
					visibleColumns.add(column);
					current++;
				} else if (!hasHiddenColumns) {
					hasHiddenColumns = true;
					leastColumnIdToBeAjusted = i;
				}
				adjustedColumnIds[i] = (current >= 0 ? current : 0);
			}

		}

		if (hasHiddenColumns) {
			this.wrappedTable = new TableContentWrapper(tableContent, visibleColumns);
		}
		this.realColCount = visibleColumns.size();
	}

	/**
	 * Get the key string
	 *
	 * @return Return the key string
	 */
	public String getKeyString() {
		return keyString;
	}

	/**
	 * Set unresolved row hint
	 *
	 * @param rowHint row hint
	 */
	public void setUnresolvedRowHint(UnresolvedRowHint rowHint) {
		this.rowHint = rowHint;
	}

	/**
	 * end row
	 *
	 * @param rowContent row content
	 */
	public void endRow(IRowContent rowContent) {
		if (isRowHidden) {
			return;
		}

		if (rowHint != null && !formalized && !LayoutUtil.isRepeatableRow(rowContent)) {
			// formalized
			Row row = rows[rowCount - 1];
			Cell[] cells = row.cells;
			for (int cellId = 0; cellId < realColCount; cellId++) {
				Cell cell = cells[cellId];
				if (cell != null) {
					// fill empty cell or remove dropped cell
					if (cell.status == Cell.CELL_EMPTY
							|| (cell.status == Cell.CELL_USED) && (rowHint.isDropColumn(cellId))) {
						IReportContent report = rowContent.getReportContent();
						ICellContent cellContent = report.createCellContent();
						rowHint.initUnresolvedCell(cellContent, rowContent.getInstanceID(), cellId);
						cellContent.setParent(rowContent);
						int rowSpan = cellContent.getRowSpan();
						int colSpan = cellContent.getColSpan();

						Cell newCell = Cell.createCell(row.rowId, cellId, rowSpan, colSpan,
								new CellContent(cellContent, null));
						row.cells[cellId] = newCell;
						int end = Math.min(realColCount, cellId + colSpan);
						for (int i = cellId + 1; i < end; i++) {
							row.cells[i] = Cell.createSpanCell(row.rowId, i, newCell);
						}
					}
				}
			}
			formalized = true;
			rowHint = null;
		}
		if (needFormalize) {
			if (hasDropCell()) {
				Row row = rows[rowCount - 1];
				Cell[] cells = row.cells;
				for (int cellId = 0; cellId < realColCount; cellId++) {
					Cell cell = cells[cellId];
					if (cell != null) {
						// fill empty cell or remove dropped cell
						if (cell.status == Cell.CELL_EMPTY) {
							IReportContent report = rowContent.getReportContent();
							ICellContent cellContent = report.createCellContent();
							cellContent.getStyle().setDisplay("none");
							cellContent.setParent(rowContent);
							Cell newCell = Cell.createCell(row.rowId, cellId, 1, 1, new CellContent(cellContent, null));
							row.cells[cellId] = newCell;
						}
					}
				}
			}
			needFormalize = false;
		}
	}

	/**
	 * reset the table model.
	 *
	 */
	public void reset() {
		// keepUnresolvedCells( );
		fillEmptyCells(0, 0, rowBufferSize, colBufferSize);
		rowCount = 0;
		isRowHidden = false;
	}

	/**
	 * Get the row count
	 *
	 * @return Return the row count
	 */
	public int getRowCount() {
		return rowCount;
	}

	/**
	 * Get the column count
	 *
	 * @return Return the column count
	 */
	public int getColCount() {
		return realColCount;
	}

	/**
	 * Check if the cache is exceeded
	 *
	 * @return Return the check result of exceeded cache
	 */
	public boolean exceedMaxCache() {
		return this.rowCount >= MAX_ROW_SPAN;
	}

	/**
	 * Set flag of need formalize
	 *
	 * @param formalize formalize is needed
	 */
	public void setNeedFormalize(boolean formalize) {
		this.needFormalize = formalize;
	}

	/**
	 * Create a row in the table model
	 *
	 * @param rowContent row content
	 * @param isHidden   hidden flag
	 * @return Return the created row
	 */
	public Row createRow(Object rowContent, boolean isHidden) {
		if (!isHidden) {
			isRowHidden = false;
			ensureSize(rowCount + 1, realColCount);
			Row row = rows[rowCount];
			row.rowId = rowCount;
			row.content = rowContent;

			if (rowCount > 0) {
				Cell[] cells = row.cells;
				// update the status of last row
				Cell[] lastCells = rows[rowCount - 1].cells;

				for (int cellId = 0; cellId < realColCount; cellId++) {
					Cell lastCell = lastCells[cellId];
					if (lastCell.status == Cell.CELL_SPANED) {
						lastCell = lastCell.getCell();
					}
					if (lastCell.status == Cell.CELL_USED) {
						if (lastCell.rowSpan < 0 || lastCell.rowId + lastCell.rowSpan > rowCount) {
							cells[cellId] = Cell.createSpanCell(rowCount, cellId, lastCell);
						}
					}
				}
			}
			rowCount++;
			return row;
		}
		isRowHidden = true;
		if (rowCount > 0) {
			// update the status of last row
			Cell[] lastCells = rows[rowCount - 1].cells;
			HashSet<Cell> updated = new HashSet<Cell>();
			for (int cellId = 0; cellId < realColCount; cellId++) {
				Cell lastCell = lastCells[cellId];
				if (lastCell.status == Cell.CELL_SPANED) {
					lastCell = lastCell.getCell();
				}
				if (lastCell.status == Cell.CELL_USED) {
					if (lastCell.rowId + lastCell.rowSpan >= rowCount + 1 && !updated.contains(lastCell)) {
						lastCell.rowSpan--;
						updated.add(lastCell);
					}
				}
			}
		}
		return null;

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
		if (isRowHidden) {
			return;
		}
		// assert(cellId>0 && cellId<=colCount);
		// resolve real columnNumber and columnSpan
		int columnNumber = cellId;
		int columnSpan = colSpan;
		if (wrappedTable != null) {
			columnNumber = getAdjustedColumnId(cellId);
			columnSpan = getAdjustedColumnSpan(cellId, colSpan);
		}
		if (columnSpan < 1) {
			return;
		}
		assert (columnNumber >= 0);
		assert (columnNumber + columnSpan <= realColCount);
		ensureSize(rowCount, columnNumber + columnSpan);

		Cell cell = rows[rowCount - 1].cells[columnNumber];
		int status = cell.getStatus();

		if (status == Cell.CELL_EMPTY) {
			Cell newCell = Cell.createCell(rows[rowCount - 1].rowId, columnNumber, rowSpan, columnSpan, content);

			Cell[] cells = rows[rowCount - 1].cells;
			rows[rowCount - 1].cells[columnNumber] = newCell;
			for (int i = columnNumber + 1; i < columnNumber + columnSpan; i++) {
				cells[i] = Cell.createSpanCell(rows[rowCount - 1].rowId, i, newCell);
			}
		} else if (status == Cell.CELL_SPANED) {
			if (rowCount > 1) {
				Cell lastCell = rows[rowCount - 2].cells[columnNumber];
				if (lastCell.getRowSpan() > 0) {
					if (lastCell.status == Cell.CELL_SPANED) {
						lastCell = lastCell.getCell();
					}
					if (lastCell.status == Cell.CELL_USED) {
						lastCell.rowSpan = rowCount - 1 - lastCell.rowId;
					}
					Cell newCell = Cell.createCell(rows[rowCount - 1].rowId, columnNumber, rowSpan, columnSpan,
							content);

					Cell[] cells = rows[rowCount - 1].cells;
					rows[rowCount - 1].cells[columnNumber] = newCell;
					for (int i = columnNumber + 1; i < columnNumber + columnSpan; i++) {
						cells[i] = Cell.createSpanCell(rows[rowCount - 1].rowId, i, newCell);
					}
				}
			}
		}

	}

	/**
	 * Resolve the dropped cells
	 *
	 * @param finished finished flag
	 *
	 */
	public void resolveDropCells(boolean finished) {
		if (!finished) {
			keepUnresolvedCells();
		}
		if (rowCount <= 0) {
			return;
		}
		Cell[] cells = rows[rowCount - 1].cells;
		for (int cellId = 0; cellId < realColCount; cellId++) {
			if (cells[cellId] != null) {
				if (cells[cellId].getRowSpan() != 1) {
					Cell cell = cells[cellId].getCell();
					cell.rowSpan = rows[rowCount - 1].rowId - cell.rowId + 1;
				}
				cellId = cellId + cells[cellId].getColSpan() - 1;
			}
		}
	}

	/**
	 * Resolve the dropped cells
	 *
	 * @param bandId   band id
	 * @param finished finished flag
	 */
	public void resolveDropCells(int bandId, boolean finished) {
		if (rowCount <= 0) {
			return;
		}
		if (!finished) {
			keepUnresolvedCells();
		}
		Cell[] cells = rows[rowCount - 1].cells;

		for (int cellId = 0; cellId < realColCount; cellId++) {
			if (cells[cellId] != null) {
				Cell cell = cells[cellId].getCell();
				if (cell.getRowSpan() == bandId) {
					cell.rowSpan = rows[rowCount - 1].rowId - cell.rowId + 1;
				}
				cellId = cellId + cells[cellId].getColSpan() - 1;
			}
		}
	}

	/**
	 * Check if unresolved rows exists
	 *
	 * @return Return the check result of unresolved rows
	 */
	public boolean hasUnResolvedRow() {
		return rowHint != null;
	}

	/**
	 * Check if dropped cells exists
	 *
	 * @return Return the check result of dropped cells
	 */
	public boolean hasDropCell() {
		if (rowCount <= 0) {
			return false;
		}

		Cell[] cells = rows[rowCount - 1].cells;
		for (int cellId = 0; cellId < realColCount; cellId++) {
			Cell cell = cells[cellId];

			if (cell != null) {
				int rowSpan = cell.getRowSpan();

				if (rowSpan < 0 || rowSpan > 1) {
					return true;
				}
			}
		}
		return false;
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

		// keep the last row for page hint
		if (lastRowId > 0 && rows[lastRowId - 1] != null) {
			lastRow = new Row(rows[lastRowId - 1].rowId);
			lastRow.content = rows[lastRowId - 1].content;
			lastRow.cells = new Cell[lastColId - colId];
			for (int i = colId; i < lastColId; i++) {
				lastRow.cells[i] = rows[lastRowId - 1].cells[i];
			}
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

	/**
	 * Get cells based on row and column index
	 *
	 * @param rowIndex row index
	 * @param colIndex column index
	 * @return Return the cell
	 */
	public Cell getCell(int rowIndex, int colIndex) {
		return rows[rowIndex].cells[colIndex];
	}

	/**
	 * Get the row based on index
	 *
	 * @param index row index
	 * @return Return the row based on index
	 */
	public Row getRow(int index) {
		assert (index >= 0 && index < rowCount);
		return rows[index];
	}

	private boolean isColumnHidden(IColumn column) {
		// For fixed layout reports and in run task, we need to emit the
		// invisible content to PDF layout engine.
		boolean hiddenMask = context.isFixedLayout()
				&& (Integer) context.getLayoutEngine().getOption(EngineTask.TASK_TYPE) == IEngineTask.TASK_RUN;
		// return LayoutUtil.isHiddenByVisibility( column, format, hiddenMask );
		return LayoutUtil.isHidden(column, format, context.getOutputDisplayNone(), hiddenMask);
	}

	/**
	 * Get the unresolved row hint
	 *
	 * @return Return the unresolved row hint
	 */
	public UnresolvedRowHint getUnresolvedRow() {
		return rowHint;

	}

	protected void keepUnresolvedCells() {
		if (rowHint == null) {
			Row row = null;
			if (rowCount > 0) {
				row = rows[rowCount - 1];
			} else if (lastRow != null) {
				row = lastRow;
			} else {
				return;
			}
			Cell[] cells = row.cells;
			IRowContent rowContent = (IRowContent) row.getContent();
			ITableContent table = rowContent.getTable();
			InstanceID tableId = table.getInstanceID();
			InstanceID rowId = rowContent.getInstanceID();
			UnresolvedRowHint hint = new UnresolvedRowHint(tableId.toUniqueString(), rowId.toUniqueString());
			for (int cellId = 0; cellId < realColCount; cellId++) {
				if (cells[cellId] != null) {
					// FIXME: Since cell maybe has a child which does not be started
					// because it has a page-break-before, we do not process its
					// style now. So we should start the cell when layout it.
					String style = null;
					CellContent cellContent = (CellContent) cells[cellId].getContent();
					if (cellContent != null) {
						ICellContent cc = cellContent.getContent();
						if (cc != null) {
							style = cc.getStyle().getCssText();
						}
					}
					hint.addUnresolvedCell(style, cells[cellId].getColId(), cells[cellId].getColSpan(),
							cells[cellId].getRowSpan());
				}
			}
			this.rowHint = hint;
		}

	}

	/**
	 * Get current row id
	 *
	 * @return Return the current row id
	 */
	public int getCurrentRowID() {
		return rowCount - 1;
	}

	/**
	 * Is the table content visible
	 *
	 * @param cell cell content
	 * @return Return the check result if the object is visible
	 */
	public boolean isVisible(ICellContent cell) {
		IElement parent = cell.getParent();
		// For fixed layout reports and in run task, we need to emit the
		// invisible content to PDF layout engine.
		boolean hiddenMask = context.isFixedLayout()
				&& (Integer) context.getLayoutEngine().getOption(EngineTask.TASK_TYPE) == IEngineTask.TASK_RUN;
		if (parent instanceof IContent) {
			if (LayoutUtil.isHidden(((IContent) parent), format, context.getOutputDisplayNone(), hiddenMask)) {
				return false;
			}
		}
		IColumn column = cell.getColumnInstance();
		if ((column == null) || isColumnHidden(column)) {
			return false;
		}

		return true;
	}

	protected static class UnresolvedRow {

		Row row;
		boolean invalidFlags[];

		public UnresolvedRow(Row row) {
			this.row = row;
			invalidFlags = new boolean[row.cells.length];
		}

		protected int getRowSpan(Row row, int originalRowSpan) {
			if (originalRowSpan > 0) {
				if (row.getContent() != this.row.getContent()) {
					return originalRowSpan - 1;

				}
			}
			return originalRowSpan;
		}

		public Cell createCell(int colId, Row row) {
			Cell[] cells = this.row.cells;
			if (colId >= 0 && colId < cells.length) {
				// FIXME need clear the content?
				if (!invalidFlags[colId]) {
					invalidFlags[colId] = true;
					return Cell.createCell(row.rowId, colId, getRowSpan(row, cells[colId].getRowSpan()),
							cells[colId].getColSpan(), cells[colId].getContent());
				}
			}
			return Cell.createCell(row.rowId, colId, 1, 1, cells[colId].getContent());
		}

	}

	/**
	 * Get the wrapped table content
	 *
	 * @return Return the wrapped table content
	 */
	public ITableContent getWrappedTableContent() {
		if (wrappedTable != null) {
			return wrappedTable;
		}
		return tableContent;
	}

	/**
	 * Get the wrapped cell content
	 *
	 * @param cellContent cell content
	 * @return Return the wrapped cell content
	 */
	public ICellContent getWrappedCellContent(ICellContent cellContent) {
		if (needWrap(cellContent)) {
			CellContentWrapper cellContentWrapper = new CellContentWrapper(cellContent);
			int columnId = cellContent.getColumn();
			int columnSpan = cellContent.getColSpan();
			cellContentWrapper.setColumn(getAdjustedColumnId(columnId));
			cellContentWrapper.setColSpan(getAdjustedColumnSpan(columnId, columnSpan));
			return cellContentWrapper;
		}
		return cellContent;
	}

	private boolean needWrap(ICellContent cellContent) {
		if (wrappedTable != null) {
			int columnId = cellContent.getColumn();
			int columnSpan = cellContent.getColSpan();
			return (columnId >= leastColumnIdToBeAjusted) || (columnId + columnSpan - 1 >= leastColumnIdToBeAjusted);
		}
		return false;
	}

	/**
	 * Get the column id with check of wrapped table
	 *
	 * @param columnId column id
	 * @return Return the column id
	 */
	public int getColumnId(int columnId) {
		if (this.wrappedTable != null) {
			return getAdjustedColumnId(columnId);
		}
		return columnId;
	}

	/**
	 * Get the column span
	 *
	 * @param columnId   column id
	 * @param columnSpan column span
	 * @return Return the column span
	 */
	public int getColunmSpan(int columnId, int columnSpan) {
		if (this.wrappedTable != null) {
			return getAdjustedColumnSpan(columnId, columnSpan);
		}
		return columnSpan;
	}

	private int getAdjustedColumnSpan(int columnId, int columnSpan) {
		if (columnSpan == 1) {
			return columnSpan;
		}
		int endColumnId = columnId + columnSpan - 1;
		return adjustedColumnIds[endColumnId] - adjustedColumnIds[columnId] + 1;
	}

	private int getAdjustedColumnId(int columnId) {
		return adjustedColumnIds[columnId];
	}
}
