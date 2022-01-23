/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.RowArea;
import org.eclipse.birt.report.engine.layout.area.impl.TableArea;
import org.eclipse.birt.report.engine.layout.pdf.BorderConflictResolver;
import org.eclipse.birt.report.engine.layout.pdf.cache.CursorableList;
import org.eclipse.birt.report.engine.layout.pdf.cache.DummyCell;
import org.eclipse.birt.report.engine.layout.pdf.emitter.TableLayout.TableLayoutInfo;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.util.BidiAlignmentResolver;
import org.w3c.dom.css.CSSValue;

public class TableAreaLayout {

	protected CursorableList rows = new CursorableList();

	/**
	 * Border conflict resolver
	 */
	protected BorderConflictResolver bcr = new BorderConflictResolver();

	protected TableLayoutInfo layoutInfo = null;

	protected ITableContent tableContent;

	protected ICellContent[] cellCache = new ICellContent[2];

	protected int startCol;

	protected int endCol;

	protected Row unresolvedRow;

	public TableAreaLayout(ITableContent tableContent, TableLayoutInfo layoutInfo, int startCol, int endCol) {
		this.tableContent = tableContent;
		this.layoutInfo = layoutInfo;
		this.startCol = startCol;
		this.endCol = endCol;
		if (tableContent != null)
			bcr.setRTL(tableContent.isRTL());
	}

	public void setUnresolvedRow(Row row) {
		this.unresolvedRow = row;
	}

	public Row getUnresolvedRow() {
		return (Row) rows.getCurrent();
	}

	public Row createUnresolvedRow(RowArea rowArea) {
		Row lastRow = (Row) rows.getCurrent();
		Row row = new Row(rowArea, startCol, endCol);
		boolean usedResolvedRow = false;

		for (int i = startCol; i <= endCol; i++) {
			CellArea upperCell = null;
			if (lastRow != null) {
				upperCell = lastRow.getCell(i);
			}
			// upperCell has row span, or is a drop cell.
			if (upperCell != null && (upperCell.getRowSpan() > 1)) {
				DummyCell dummyCell = createDummyCell(upperCell);
				row.addArea(dummyCell);
				i = i + upperCell.getColSpan() - 1;
			}
			// upperCell has NO row span, and is NOT a drop cell.
			// or upperCell is null. In this case, we need not care about
			// the upperCell.
			else {
				CellArea cell = row.getCell(i);
				if (cell == null) {
					if (unresolvedRow != null) {
						upperCell = unresolvedRow.getCell(i);
						usedResolvedRow = true;
					}
					cell = createEmptyCell(upperCell, i, row, lastRow);
				}
				i = i + cell.getColSpan() - 1;
			}
		}
		if (usedResolvedRow) {
			unresolvedRow = null;
		}
		return row;
	}

	protected int resolveBottomBorder(CellArea cell) {
		IStyle tableStyle = tableContent.getComputedStyle();
		IContent cellContent = cell.getContent();
		IStyle columnStyle = getColumnStyle(cell.getColumnID());
		IStyle cellAreaStyle = cell.getStyle();
		IStyle cellContentStyle = cellContent.getComputedStyle();
		IStyle rowStyle = ((IContent) cellContent.getParent()).getComputedStyle();
		bcr.resolveTableBottomBorder(tableStyle, rowStyle, columnStyle, cellContentStyle, cellAreaStyle);
		return PropertyUtil.getDimensionValue(cellAreaStyle.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH));
	}

	protected void add(ContainerArea area, ArrayList rows) {
		if (area instanceof RowArea) {
			rows.add(area);
		} else {
			Iterator iter = area.getChildren();
			while (iter.hasNext()) {
				ContainerArea container = (ContainerArea) iter.next();
				add(container, rows);
			}
		}
	}

	public void remove(TableArea table) {
		ArrayList rowColloection = new ArrayList();
		add(table, rowColloection);
		Iterator iter = rows.iterator();
		while (iter.hasNext()) {
			Row row = (Row) iter.next();
			if (rowColloection.contains(row.getArea())) {
				iter.remove();
			}
		}
		rows.resetCursor();
	}

	protected IStyle getLeftCellContentStyle(Row lastRow, int columnID) {
		if (cellCache[1] != null && cellCache[1].getColumn() + cellCache[1].getColSpan() <= columnID) {
			if (lastRow != null && columnID > 0) {
				CellArea cell = lastRow.getCell(columnID - 1);
				if (cell != null && cell.getRowSpan() < 0 && cell.getContent() != null) {
					return cell.getContent().getComputedStyle();
				}

			}
			return cellCache[1].getComputedStyle();

		}
		if (lastRow != null && columnID > 0) {
			CellArea cell = lastRow.getCell(columnID - 1);
			if (cell != null && cell.getRowSpan() > 1 && cell.getContent() != null) {
				return cell.getContent().getComputedStyle();
			}
		}
		return null;
	}

	/**
	 * resolve cell border conflict
	 * 
	 * @param cellArea
	 */
	public void resolveBorderConflict(CellArea cellArea, boolean isFirst) {
		IContent cellContent = cellArea.getContent();
		int columnID = cellArea.getColumnID();
		int colSpan = cellArea.getColSpan();
		IRowContent row = (IRowContent) cellContent.getParent();
		IStyle cellContentStyle = cellContent.getComputedStyle();
		IStyle cellAreaStyle = cellArea.getStyle();
		IStyle tableStyle = tableContent.getComputedStyle();
		IStyle rowStyle = row.getComputedStyle();
		IStyle columnStyle = getColumnStyle(columnID);
		IStyle preRowStyle = null;
		IStyle preColumnStyle = getColumnStyle(columnID - 1);
		IStyle leftCellContentStyle = null;
		IStyle topCellStyle = null;

		Row lastRow = null;

		if (cellCache[0] != cellContent) {
			cellCache[1] = cellCache[0];
			cellCache[0] = (ICellContent) cellContent;
		}

		if (rows.size() > 0) {
			lastRow = (Row) rows.getCurrent();
		}

		leftCellContentStyle = getLeftCellContentStyle(lastRow, columnID);

		if (lastRow != null) {
			preRowStyle = lastRow.getContent().getComputedStyle();
			CellArea cell = lastRow.getCell(columnID);
			if (cell != null && cell.getContent() != null) {
				topCellStyle = cell.getContent().getComputedStyle();
			}
		}
		// FIXME
		if (rows.size() == 0 && lastRow == null) {
			// resolve top border
			if (isFirst) {
				bcr.resolveTableTopBorder(tableStyle, rowStyle, columnStyle, cellContentStyle, cellAreaStyle);
			} else {
				bcr.resolveTableTopBorder(tableStyle, null, columnStyle, null, cellAreaStyle);
			}

			// resolve left border
			if (columnID == startCol) {
				bcr.resolveTableLeftBorder(tableStyle, rowStyle, columnStyle, cellContentStyle, cellAreaStyle);
			} else {
				bcr.resolveCellLeftBorder(preColumnStyle, columnStyle, leftCellContentStyle, cellContentStyle,
						cellAreaStyle);
			}

			// resolve right border

			if (columnID + colSpan - 1 == endCol) {
				bcr.resolveTableRightBorder(tableStyle, rowStyle, columnStyle, cellContentStyle, cellAreaStyle);
			}

		} else {
			if (isFirst) {
				bcr.resolveCellTopBorder(preRowStyle, rowStyle, topCellStyle, cellContentStyle, cellAreaStyle);
			} else {
				bcr.resolveCellTopBorder(preRowStyle, null, topCellStyle, null, cellAreaStyle);
			}
			// resolve left border
			if (columnID == startCol) {
				// first column
				bcr.resolveTableLeftBorder(tableStyle, rowStyle, columnStyle, cellContentStyle, cellAreaStyle);
			} else {
				// TODO fix row span conflict
				bcr.resolveCellLeftBorder(preColumnStyle, columnStyle, leftCellContentStyle, cellContentStyle,
						cellAreaStyle);
			}
			// resolve right border
			if (columnID + colSpan - 1 == endCol) {
				bcr.resolveTableRightBorder(tableStyle, rowStyle, columnStyle, cellContentStyle, cellAreaStyle);
			}
		}
	}

	/**
	 * get column style
	 * 
	 * @param columnID
	 * @return
	 */
	private IStyle getColumnStyle(int columnID) {
		// current not support column style
		return null;
	}

	protected void verticalAlign(CellArea c) {
		CellArea cell;
		if (c instanceof DummyCell) {
			cell = ((DummyCell) c).getCell();
		} else {
			cell = c;
		}
		IContent content = cell.getContent();
		if (content == null) {
			return;
		}
		CSSValue verticalAlign = content.getComputedStyle().getProperty(IStyle.STYLE_VERTICAL_ALIGN);
		if (IStyle.BOTTOM_VALUE.equals(verticalAlign) || IStyle.MIDDLE_VALUE.equals(verticalAlign)) {
			int totalHeight = 0;
			Iterator iter = cell.getChildren();
			while (iter.hasNext()) {
				AbstractArea child = (AbstractArea) iter.next();
				totalHeight += child.getAllocatedHeight();
			}
			int offset = cell.getContentHeight() - totalHeight;
			if (offset > 0) {
				if (IStyle.BOTTOM_VALUE.equals(verticalAlign)) {
					iter = cell.getChildren();
					while (iter.hasNext()) {
						AbstractArea child = (AbstractArea) iter.next();
						child.setAllocatedPosition(child.getAllocatedX(), child.getAllocatedY() + offset);
					}
				} else if (IStyle.MIDDLE_VALUE.equals(verticalAlign)) {
					iter = cell.getChildren();
					while (iter.hasNext()) {
						AbstractArea child = (AbstractArea) iter.next();
						child.setAllocatedPosition(child.getAllocatedX(), child.getAllocatedY() + offset / 2);
					}
				}

			}
		}

		CSSValue align = content.getComputedStyle().getProperty(IStyle.STYLE_TEXT_ALIGN);

		// bidi_hcg: handle empty or justify align in RTL direction as right
		// alignment
		boolean isRightAligned = BidiAlignmentResolver.isRightAligned(content, align, false);

		// single line
		if (isRightAligned || IStyle.CENTER_VALUE.equals(align)) {

			Iterator iter = cell.getChildren();
			while (iter.hasNext()) {
				AbstractArea area = (AbstractArea) iter.next();
				int spacing = cell.getContentWidth() - area.getAllocatedWidth();
				if (spacing > 0) {
					if (isRightAligned) {
						area.setAllocatedPosition(spacing + area.getAllocatedX(), area.getAllocatedY());
					} else if (IStyle.CENTER_VALUE.equals(align)) {
						area.setAllocatedPosition(spacing / 2 + area.getAllocatedX(), area.getAllocatedY());
					}
				}
			}
		}
	}

	public void reset(TableArea table) {
		Iterator iter = rows.iterator();
		while (iter.hasNext()) {
			Row row = (Row) iter.next();
			if (table.contains(row.getArea())) {
				iter.remove();
			}
		}

		rows.resetCursor();
	}

	/**
	 * When pagination happens, if drop cells should be finished by force, we need
	 * to end these cells and vertical align for them.
	 * 
	 */
	public int resolveAll() {
		if (rows.size() == 0) {
			return 0;
		}
		Row row = (Row) rows.getCurrent();
		int originalRowHeight = row.getArea().getHeight();
		int height = originalRowHeight;

		for (int i = startCol; i <= endCol; i++) {
			CellArea cell = row.getCell(i);
			if (null == cell) {
				// After padding empty cell and dummy cell, the cell should not be null.
				continue;
			}
			if (cell instanceof DummyCell) {
				DummyCell dummyCell = (DummyCell) cell;
				int delta = dummyCell.getDelta();
				height = Math.max(height, delta);
			} else {
				height = Math.max(height, cell.getHeight());
			}
			i = i + cell.getColSpan() - 1;
		}

		int dValue = height - originalRowHeight;
		for (int i = startCol; i <= endCol; i++) {
			CellArea cell = row.getCell(i);
			if (cell == null) {
				// this should NOT happen.
				continue;
			}
			if (cell instanceof DummyCell) {
				if (cell.getRowSpan() == 1)
				// this dummyCell and it reference cell height have already been
				// updated.
				{
					if (dValue != 0) {
						CellArea refCell = ((DummyCell) cell).getCell();
						refCell.setHeight(refCell.getHeight() + height - originalRowHeight);
						verticalAlign(refCell);
					}
				} else {
					CellArea refCell = ((DummyCell) cell).getCell();
					int delta = ((DummyCell) cell).getDelta();
					if (delta < height) {
						refCell.setHeight(refCell.getHeight() - delta + height);
					}
					verticalAlign(refCell);
				}
			} else {
				if (dValue != 0) {
					cell.setHeight(height);
					verticalAlign(cell);
				}
			}
			i = i + cell.getColSpan() - 1;
		}
		row.getArea().setHeight(height);
		return dValue;
	}

	public int resolveBottomBorder() {
		if (rows.size() == 0) {
			return 0;
		}
		Row row = (Row) rows.getCurrent();
		int result = 0;
		int width = 0;
		for (int i = startCol; i <= endCol; i++) {
			CellArea cell = row.getCell(i);
			if (cell == null) {
				// this should NOT happen.
				continue;
			}
			if (cell instanceof DummyCell) {
				width = resolveBottomBorder(((DummyCell) cell).getCell());
			} else {
				width = resolveBottomBorder(cell);
			}

			if (width > result)
				result = width;
			i = i + cell.getColSpan() - 1;
		}

		// update cell height
		if (result > 0) {
			row.getArea().setHeight(row.getArea().getHeight() + result);
			for (int i = startCol; i <= endCol; i++) {
				CellArea cell = row.getCell(i);
				if (cell instanceof DummyCell) {
					CellArea refCell = ((DummyCell) cell).getCell();
					refCell.setHeight(refCell.getHeight() + result);
				} else {
					cell.setHeight(cell.getHeight() + result);
				}
				i = i + cell.getColSpan() - 1;
			}
		}
		return result;
	}

	/**
	 * Adds a list of rows to current rows.
	 */
	public void addRows(CursorableList rs) {
		Iterator iter = rs.iterator();
		while (iter.hasNext()) {
			rows.add(iter.next());
		}
	}

	/**
	 * Adds the updated row wrapper to rows.
	 */
	public void addRow(RowArea rowArea, int specifiedHeight) {
		Row row = updateRow(rowArea, specifiedHeight);
		rows.add(row);
	}

	/**
	 * 1) Creates row wrapper. 2) For the null cell in the row wrapper, fills the
	 * relevant position with dummy cell or empty cell. 3) Updates the height of the
	 * row and the cells in the row.
	 * 
	 * @param rowArea current rowArea.
	 */
	private Row updateRow(RowArea rowArea, int specifiedHeight) {
		int height = specifiedHeight;
		Row lastRow = (Row) rows.getCurrent();
		Row row = new Row(rowArea, startCol, endCol);
		boolean usedResolvedRow = false;

		for (int i = startCol; i <= endCol; i++) {
			CellArea upperCell = null;
			if (lastRow != null) {
				upperCell = lastRow.getCell(i);
			}
			// upperCell has row span, or is a drop cell.
			if (upperCell != null && (upperCell.getRowSpan() > 1)) {
				DummyCell dummyCell = createDummyCell(upperCell);
				row.addArea(dummyCell);

				int delta = dummyCell.getDelta();
				if (dummyCell.getRowSpan() == 1) {
					height = Math.max(height, delta);
				}
				i = i + upperCell.getColSpan() - 1;
			}
			// upperCell has NO row span, and is NOT a drop cell.
			// or upperCell is null. In this case, we need not care about
			// the upperCell.
			else {
				CellArea cell = row.getCell(i);
				if (cell == null) {
					if (unresolvedRow != null) {
						upperCell = unresolvedRow.getCell(i);
						usedResolvedRow = true;
					}
					cell = createEmptyCell(upperCell, i, row, lastRow);
				}
				if (cell.getRowSpan() == 1) {
					height = Math.max(height, cell.getHeight());
				}
				i = i + cell.getColSpan() - 1;
			}
		}
		if (usedResolvedRow) {
			unresolvedRow = null;
		}
		updateRowHeight(row, height);
		return row;
	}

	/**
	 * Creates dummy cell and updates its delta value.
	 * 
	 * @param upperCell the upper cell.
	 * @return the created dummy cell.
	 */
	private DummyCell createDummyCell(CellArea upperCell) {
		DummyCell dummyCell = null;
		CellArea refCell = null;
		Row lastRow = (Row) rows.getCurrent();
		int lastRowHeight = lastRow.getArea().getHeight();
		int delta = 0;
		if (upperCell instanceof DummyCell) {
			refCell = ((DummyCell) upperCell).getCell();
			dummyCell = new DummyCell(refCell);
			delta = ((DummyCell) upperCell).getDelta() - lastRowHeight;
			dummyCell.setDelta(delta);
		} else {
			refCell = upperCell;
			dummyCell = new DummyCell(upperCell);
			delta = refCell.getHeight() - lastRowHeight;
			dummyCell.setDelta(delta);
		}
		dummyCell.setRowSpan(upperCell.getRowSpan() - 1);
		dummyCell.setColSpan(upperCell.getColSpan());
		return dummyCell;
	}

	private CellArea createEmptyCell(CellArea upperCell, int columnId, Row row, Row lastRow) {
		ICellContent cellContent = null;
		int rowSpan = 1;

		if (upperCell != null) {
			cellContent = (ICellContent) upperCell.getContent();
			rowSpan = upperCell.getRowSpan();
		}

		if (cellContent == null) {
			cellContent = tableContent.getReportContent().createCellContent();
			cellContent.setColumn(columnId);
			cellContent.setColSpan(1);
			cellContent.setRowSpan(1);
			cellContent.setParent(row.getArea().getContent());
		}
		int emptyCellColID = cellContent.getColumn();
		int emptyCellColSpan = cellContent.getColSpan();
		CellArea emptyCell = AreaFactory.createCellArea(cellContent);
		emptyCell.setRowSpan(rowSpan);
		row.addArea(emptyCell);

		CellArea leftSideCellArea = null;
		if (emptyCellColID > startCol) {
			leftSideCellArea = row.getCell(emptyCellColID - 1);
			if (leftSideCellArea == null) {
				// the left-side cell is a dummy cell which will be
				// created in addRow()
				cellCache[0] = (ICellContent) lastRow.getCell(emptyCellColID - 1).getContent();
				int k = emptyCellColID - 1;
				while (leftSideCellArea == null && k > startCol) {
					k--;
					leftSideCellArea = row.getCell(k);
				}
			} else {
				cellCache[0] = (ICellContent) leftSideCellArea.getContent();
			}
		} else {
			leftSideCellArea = null;
		}
		resolveBorderConflict(emptyCell, true);
		IStyle areaStyle = emptyCell.getStyle();
		areaStyle.setProperty(IStyle.STYLE_PADDING_TOP, IStyle.NUMBER_0);
		areaStyle.setProperty(IStyle.STYLE_MARGIN_TOP, IStyle.NUMBER_0);
		emptyCell.setWidth(getCellWidth(emptyCellColID, emptyCellColID + emptyCellColSpan));
		emptyCell.setPosition(layoutInfo.getXPosition(columnId), 0);
		if (leftSideCellArea != null) {
			int index = row.getArea().indexOf(leftSideCellArea);
			row.getArea().addChild(index + 1, emptyCell);
		} else {
			row.getArea().addChild(0, emptyCell);
		}
		return emptyCell;
	}

	/**
	 * Updates the row height and the height of the cells in the row.
	 * 
	 * @param rowArea
	 * @param height
	 */
	private void updateRowHeight(Row row, int height) {
		if (height < 0)
			return;
		row.getArea().setHeight(height);
		for (int i = startCol; i <= endCol; i++) {
			CellArea cell = row.getCell(i);
			if (cell.getRowSpan() == 1) {
				if (cell instanceof DummyCell) {
					CellArea refCell = ((DummyCell) cell).getCell();
					int delta = ((DummyCell) cell).getDelta();
					if (delta < height) {
						refCell.setHeight(refCell.getHeight() - delta + height);
					}
					((DummyCell) cell).setDelta(0);
					verticalAlign(refCell);
				} else {
					cell.setHeight(height);
					verticalAlign(cell);
				}
			}
			i = i + cell.getColSpan() - 1;
		}
	}

	private int getCellWidth(int startColumn, int endColumn) {
		if (layoutInfo != null) {
			return layoutInfo.getCellWidth(startColumn, endColumn);
		}
		return 0;
	}

	public static class Row {
		protected int start;
		protected int length;
		protected int end;
		protected RowArea row;
		protected CellArea[] cells;

		Row(RowArea row, int start, int end) {
			this.row = row;
			this.start = start;
			this.end = end;
			this.length = end - start + 1;
			cells = new CellArea[length];

			Iterator iter = row.getChildren();
			while (iter.hasNext()) {
				CellArea cell = (CellArea) iter.next();
				int colId = cell.getColumnID();
				int colSpan = cell.getColSpan();
				if ((colId >= start) && (colId + colSpan - 1 <= end)) {
					int loopEnd = Math.min(colSpan, end - colId + 1);
					for (int j = 0; j < loopEnd; j++) {
						cells[colId - start + j] = cell;
					}
				}
			}
		}

		public IContent getContent() {
			return row.getContent();
		}

		public void remove(int colId) {
			// FIXME
			row.removeChild(getCell(colId));
		}

		public void remove(IArea area) {
			if (area != null) {
				if (!(area instanceof DummyCell)) {
					row.removeChild(area);
				}
				CellArea cell = (CellArea) area;
				int colId = cell.getColumnID();
				int colSpan = cell.getColSpan();
				if ((colId >= start) && (colId + colSpan - 1 <= end)) {
					int loopEnd = Math.min(colSpan, end - colId + 1);
					for (int j = 0; j < loopEnd; j++) {
						cells[colId - start + j] = null;
					}
				}
			}
		}

		public CellArea getCell(int colId) {
			if (colId < start || colId > end) {
				assert (false);
				return null;
			}
			return cells[colId - start];
		}

		public void addArea(IArea area) {
			CellArea cell = (CellArea) area;
			int colId = cell.getColumnID();
			int colSpan = cell.getColSpan();

			if ((colId >= start) && (colId + colSpan - 1 <= end)) {
				int loopEnd = Math.min(colSpan, end - colId + 1);
				for (int j = 0; j < loopEnd; j++) {
					cells[colId - start + j] = cell;
				}
			}
		}

		/**
		 * row content
		 */
		public RowArea getArea() {
			return row;
		}
	}

	public CursorableList getRows() {
		return rows;
	}

}
