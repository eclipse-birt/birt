/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout.area.impl;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.layout.pdf.cache.CursorableList;
import org.eclipse.birt.report.engine.nLayout.area.impl.TableArea.TableLayoutInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.BorderInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.BoxStyle;
import org.eclipse.birt.report.engine.util.BidiAlignmentResolver;
import org.w3c.dom.css.CSSValue;

public class TableLayout {

	protected CursorableList rows = new CursorableList();

	/**
	 * Border conflict resolver
	 */
	protected BorderConflictResolver bcr = new BorderConflictResolver();

	protected TableLayoutInfo layoutInfo = null;

	protected ITableContent tableContent;

	protected int startCol;

	protected int endCol;

	protected RowArea unresolvedRow;

	private RowArea currentRow;

	private boolean isRTL = false;

	public TableLayout(ITableContent tableContent, TableLayoutInfo layoutInfo, int startCol, int endCol) {
		this.tableContent = tableContent;
		this.layoutInfo = layoutInfo;
		this.startCol = startCol;
		this.endCol = endCol;
		if (tableContent != null) {
			isRTL = tableContent.isRTL();
			// bcr.setRTL( isRTL );
		}
	}

	public void setUnresolvedRow(RowArea row) {
		if (row != null && isUnresolved(row)) {
			unresolvedRow = row;
		}
	}

	public RowArea getNextRow(RowArea row) {
		int index = rows.indexOf(row);
		if (index >= 0 && index <= (rows.size() - 2)) {
			return (RowArea) rows.get(index + 1);
		}
		return null;
	}

	protected int resolveBottomBorder(CellArea cell) {
		IStyle tableStyle = tableContent.getComputedStyle();
		IContent cellContent = cell.getContent();
		IStyle columnStyle = getColumnStyle(cell.getColumnID());
		IStyle cellContentStyle = cellContent.getComputedStyle();
		IStyle rowStyle = ((IContent) cellContent.getParent()).getComputedStyle();
		if (tableStyle != null && rowStyle != null && columnStyle != null && cellContentStyle != null) {
			return 0;
		}
		BorderInfo border = bcr.resolveTableBottomBorder(tableStyle, rowStyle, columnStyle, cellContentStyle);
		if (border != null) {
			if (cell.getBoxStyle() == BoxStyle.DEFAULT) {
				cell.setBoxStyle(new BoxStyle(BoxStyle.DEFAULT));
			}
			cell.getBoxStyle().setBottomBorder(border);
			return border.getWidth();
		}
		return 0;
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
		ArrayList rowCollection = new ArrayList();
		add(table, rowCollection);
		Iterator iter = rows.iterator();
		while (iter.hasNext()) {
			RowArea row = (RowArea) iter.next();
			if (rowCollection.contains(row)) {
				iter.remove();
			}
		}
		rows.resetCursor();
	}

	public void clear() {
		rows.clear();
		rows.resetCursor();
	}

	protected IStyle getLeftCellContentStyle(RowArea lastRow, CellArea currentCell) {
		RowArea currentRow = (RowArea) currentCell.getParent();
		int columnID = currentCell.getColumnID();
		CellArea cell = null;
//		if ( isRTL )
//		{
//			cell = currentRow.getCell( columnID + 1 );
//			if ( cell == null && lastRow != null )
//			{
//				cell = lastRow.getCell( columnID + 1 );
//			}
//			if ( cell == null )
//			{
//				// FIXME:
//				// For RtL reports, the left cell of current cell is not
//				// initialized yet. Assume left cell has the same border style
//				// as current style, although the assumption is not always
//				// correct.
//				return currentCell.getContent( ).getComputedStyle( );
//			}
//		}
//		else
		{
			cell = currentRow.getCell(columnID - 1);
			if (cell == null && lastRow != null) {
				cell = lastRow.getCell(columnID - 1);
			}
		}

		if (cell != null) {
			return cell.getContent().getComputedStyle();
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
		IStyle tableStyle = tableContent.getComputedStyle();
		IStyle rowStyle = row.getComputedStyle();
		IStyle columnStyle = getColumnStyle(columnID);
		IStyle preRowStyle = null;
		IStyle preColumnStyle = /*
								 * isRTL ? getColumnStyle( columnID + 1 ) // TODO need valid columnID when
								 * getColumnStyle gets supported :
								 */getColumnStyle(columnID - 1);
		IStyle leftCellContentStyle = null; // for RTL it will be right cell content style
		IStyle topCellStyle = null;

		RowArea lastRow = null;

		if (rows.size() > 0) {
			lastRow = (RowArea) rows.getCurrent();
		}

		if (lastRow != null) {
			preRowStyle = lastRow.getContent().getComputedStyle();
			CellArea cell = lastRow.getCell(columnID);
			if (cell != null && cell.getContent() != null) {
				topCellStyle = cell.getContent().getComputedStyle();
			}
		}
//		if ( ( !isRTL && columnID > startCol ) || ( isRTL && columnID + colSpan - 1 < endCol ) )
		if (columnID > startCol) {
			leftCellContentStyle = getLeftCellContentStyle(lastRow, cellArea);
		}
		// FIXME
		if (rows.size() == 0 && lastRow == null) {
			// resolve top border
			if (isFirst) {
				if (tableStyle != null || rowStyle != null || cellContentStyle != null) {
					BorderInfo border = bcr.resolveTableTopBorder(tableStyle, rowStyle, columnStyle, cellContentStyle);
					if (border != null) {
						if (cellArea.getBoxStyle() == BoxStyle.DEFAULT) {
							cellArea.setBoxStyle(new BoxStyle(BoxStyle.DEFAULT));
						}
						cellArea.getBoxStyle().setTopBorder(border);
					}
				}
			} else {
				if (tableStyle != null) {
					BorderInfo border = bcr.resolveTableTopBorder(tableStyle, null, columnStyle, null);
					if (border != null) {
						if (cellArea.getBoxStyle() == BoxStyle.DEFAULT) {
							cellArea.setBoxStyle(new BoxStyle(BoxStyle.DEFAULT));
						}
						cellArea.getBoxStyle().setTopBorder(border);
					}
				}
			}

			// resolve left border
			if ((columnID == startCol && !isRTL) || (columnID + colSpan - 1 == endCol && isRTL)) {
				if (tableStyle != null || rowStyle != null || cellContentStyle != null) {
					BorderInfo border = bcr.resolveTableLeftBorder(tableStyle, rowStyle, columnStyle, cellContentStyle);
					if (border != null) {
						if (cellArea.getBoxStyle() == BoxStyle.DEFAULT) {
							cellArea.setBoxStyle(new BoxStyle(BoxStyle.DEFAULT));
						}
						cellArea.getBoxStyle().setLeftBorder(border);
					}
				}
			} else if (!isRTL) {
				if (leftCellContentStyle != null || cellContentStyle != null) {
					BorderInfo border = bcr.resolveCellLeftBorder(preColumnStyle, columnStyle, leftCellContentStyle,
							cellContentStyle);
					if (border != null) {
						if (cellArea.getBoxStyle() == BoxStyle.DEFAULT) {
							cellArea.setBoxStyle(new BoxStyle(BoxStyle.DEFAULT));
						}
						cellArea.getBoxStyle().setLeftBorder(border);
					}
				}
			}

			// resolve right border
			if ((columnID + colSpan - 1 == endCol && !isRTL) || (columnID == startCol && isRTL)) {
				if (tableStyle != null || rowStyle != null || cellContentStyle != null) {
					BorderInfo border = bcr.resolveTableRightBorder(tableStyle, rowStyle, columnStyle,
							cellContentStyle);
					if (border != null) {
						if (cellArea.getBoxStyle() == BoxStyle.DEFAULT) {
							cellArea.setBoxStyle(new BoxStyle(BoxStyle.DEFAULT));
						}
						cellArea.getBoxStyle().setRightBorder(border);
					}
				}
			} else if (isRTL && columnID > startCol && (leftCellContentStyle != null || cellContentStyle != null)) {
				// Resolve left border for the previous (logically) cell and set it
				// as the right border for the current cell
				BorderInfo border = bcr.resolveCellLeftBorder(columnStyle, preColumnStyle, cellContentStyle,
						leftCellContentStyle);
				if (border != null) {
					if (cellArea.getBoxStyle() == BoxStyle.DEFAULT) {
						cellArea.setBoxStyle(new BoxStyle(BoxStyle.DEFAULT));
					}
					cellArea.getBoxStyle().setRightBorder(border);
				}
			}

		} else {
			if (isFirst) {
				if (preRowStyle != null || rowStyle != null || topCellStyle != null || cellContentStyle != null) {
					BorderInfo border = bcr.resolveCellTopBorder(preRowStyle, rowStyle, topCellStyle, cellContentStyle);
					if (border != null) {
						if (cellArea.getBoxStyle() == BoxStyle.DEFAULT) {
							cellArea.setBoxStyle(new BoxStyle(BoxStyle.DEFAULT));
						}
						cellArea.getBoxStyle().setTopBorder(border);
					}
				}
			} else {
				if (preRowStyle != null || topCellStyle != null) {
					BorderInfo border = bcr.resolveCellTopBorder(preRowStyle, null, topCellStyle, null);
					if (border != null) {
						if (cellArea.getBoxStyle() == BoxStyle.DEFAULT) {
							cellArea.setBoxStyle(new BoxStyle(BoxStyle.DEFAULT));
						}
						cellArea.getBoxStyle().setTopBorder(border);
					}
				}
			}
			// resolve left border
			if ((columnID == startCol && !isRTL) || (columnID + colSpan - 1 == endCol && isRTL)) {
				// first column
				if (tableStyle != null || rowStyle != null || cellContentStyle != null) {
					BorderInfo border = bcr.resolveTableLeftBorder(tableStyle, rowStyle, columnStyle, cellContentStyle);
					if (border != null) {
						if (cellArea.getBoxStyle() == BoxStyle.DEFAULT) {
							cellArea.setBoxStyle(new BoxStyle(BoxStyle.DEFAULT));
						}
						cellArea.getBoxStyle().setLeftBorder(border);
					}
				}
			} else if (!isRTL) {
				// TODO fix row span conflict
				if (leftCellContentStyle != null || cellContentStyle != null) {
					BorderInfo border = bcr.resolveCellLeftBorder(preColumnStyle, columnStyle, leftCellContentStyle,
							cellContentStyle);
					if (border != null) {
						if (cellArea.getBoxStyle() == BoxStyle.DEFAULT) {
							cellArea.setBoxStyle(new BoxStyle(BoxStyle.DEFAULT));
						}
						cellArea.getBoxStyle().setLeftBorder(border);
					}
				}
			}
			// resolve right border
			if ((columnID + colSpan - 1 == endCol && !isRTL) || (columnID == startCol && isRTL)) {
				if (tableStyle != null || rowStyle != null || cellContentStyle != null) {
					BorderInfo border = bcr.resolveTableRightBorder(tableStyle, rowStyle, columnStyle,
							cellContentStyle);
					if (border != null) {
						if (cellArea.getBoxStyle() == BoxStyle.DEFAULT) {
							cellArea.setBoxStyle(new BoxStyle(BoxStyle.DEFAULT));
						}
						cellArea.getBoxStyle().setRightBorder(border);
					}
				}
			} else if (isRTL && columnID > startCol && (leftCellContentStyle != null || cellContentStyle != null)) {
				// Resolve left border for the previous (logically) cell and set it
				// as the right border for the current cell
				BorderInfo border = bcr.resolveCellLeftBorder(columnStyle, preColumnStyle, cellContentStyle,
						leftCellContentStyle);
				if (border != null) {
					if (cellArea.getBoxStyle() == BoxStyle.DEFAULT) {
						cellArea.setBoxStyle(new BoxStyle(BoxStyle.DEFAULT));
					}
					cellArea.getBoxStyle().setRightBorder(border);
				}
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

	private int getAllocatedHeight(AbstractArea area) {
		if (area instanceof ContainerArea) {
			return ((ContainerArea) area).getAllocatedHeight();
		}
		return area.getHeight();
	}

	private int getAllocatedWidth(AbstractArea area) {
		if (area instanceof ContainerArea) {
			return ((ContainerArea) area).getAllocatedWidth();
		}
		return area.getWidth();
	}

	protected void align(CellArea cell) {
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
				totalHeight += getAllocatedHeight(child);
			}
			int offset = cell.getContentHeight() - totalHeight;
			if (offset > 0) {
				if (IStyle.BOTTOM_VALUE.equals(verticalAlign)) {
					iter = cell.getChildren();
					int y = cell.getOffsetY() + offset;
					while (iter.hasNext()) {
						AbstractArea child = (AbstractArea) iter.next();
						child.setAllocatedY(y);
						y += child.getAllocatedHeight();
					}
				} else if (IStyle.MIDDLE_VALUE.equals(verticalAlign)) {
					iter = cell.getChildren();
					int y = cell.getOffsetY() + offset / 2;
					while (iter.hasNext()) {
						AbstractArea child = (AbstractArea) iter.next();
						child.setAllocatedY(y);
						y += child.getAllocatedHeight();
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
				int spacing = cell.getContentWidth() - getAllocatedWidth(area);
				if (spacing > 0) {
					if (isRightAligned) {
						area.setAllocatedX(spacing + cell.getOffsetX());
					} else if (IStyle.CENTER_VALUE.equals(align)) {
						area.setAllocatedX(spacing / 2 + cell.getOffsetX());
					}
				}
			}
		}
	}

	public void reset(TableArea table) {
		Iterator iter = rows.iterator();
		while (iter.hasNext()) {
			RowArea row = (RowArea) iter.next();
			if (table.contains(row)) {
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
	public int resolveAll(RowArea row) {
		if (row == null || rows.size() == 0) {
			return 0;
		}
		int originalRowHeight = row.getHeight();
		int height = originalRowHeight;

		for (int i = startCol; i <= endCol; i++) {
			CellArea cell = row.getCell(i);
			if (null == cell) {
				// After padding empty cell and dummy cell, the cell should not
				// be null.
				continue;
			}
			if (cell instanceof DummyCell) {
				DummyCell dummyCell = (DummyCell) cell;
				int delta = dummyCell.getDelta();
				// FIXME
				// height = Math.max( height, dummyCell.getCell( ).getHeight( ) - delta );
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
				int delta = ((DummyCell) cell).getDelta();
				if (cell.getRowSpan() == 1)
				// this dummyCell and it reference cell height have already been
				// updated.
				{
					CellArea refCell = ((DummyCell) cell).getCell();
					refCell.setHeight(delta + height);
					align(refCell);
				} else {
					CellArea refCell = ((DummyCell) cell).getCell();
					refCell.setHeight(delta + height);
					align(refCell);
				}
			} else {
				int oh = cell.getHeight();
				cell.setHeight(height);
				if (oh != height) {
					align(cell);
				}
			}
			i = i + cell.getColSpan() - 1;
		}
		row.setHeight(height);
		return dValue;
	}

	public int resolveBottomBorder() {
		if (rows.size() == 0) {
			return 0;
		}
		RowArea row = (RowArea) rows.getCurrent();
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
			row.setHeight(row.getHeight() + result);
			for (int i = startCol; i <= endCol; i++) {
				CellArea cell = row.getCell(i);
				if (cell != null) {
					if (cell instanceof DummyCell) {
						CellArea oc = ((DummyCell) cell).getCell();
						oc.setHeight(oc.getHeight() + result);
					} else {
						cell.setHeight(cell.getHeight() + result);
					}
					i = i + cell.getColSpan() - 1;
				}
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
	public void addRow(RowArea rowArea, boolean isFixedLayout) {
		updateRow(rowArea, isFixedLayout);
		rows.add(rowArea);
	}

	protected boolean isUnresolved(RowArea row) {
		if (!row.finished) {
			return true;
		}
		for (int i = startCol; i <= endCol; i++) {
			CellArea cell = row.getCell(i);
			if (cell != null && cell.getRowSpan() > 1) {
				return true;
			}
		}
		return false;
	}

	public void setMergeUnresolvedRowHint() {

		/**
		 * this case should only exist in add first detail row in new page, the repeated
		 * group header should be the last row. the row span in repeated head should be
		 * updated by unresolved row
		 */
		RowArea lastRow = (RowArea) rows.getCurrent();
		if (unresolvedRow != null && lastRow != null) {
			if (isUnresolved(unresolvedRow)) {
				for (int i = startCol; i <= endCol; i++) {
					CellArea cell = lastRow.getCell(i);
					if (cell != null) {
						CellArea unresolvedCell = unresolvedRow.getCell(i);
						if (unresolvedCell != null) {
							cell.setRowSpan(unresolvedCell.getRowSpan() - (unresolvedRow.finished ? 0 : -1));
						}
					}
				}
				unresolvedRow = null;
			}
		}
	}

	protected boolean isSpecifiedHeight(RowArea area) {
		IContent content = area.getContent();
		if (content != null) {
			DimensionType cHeight = content.getHeight();
			if (cHeight != null && !cHeight.getUnits().equals(DimensionType.UNITS_PERCENTAGE)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 1) Creates row wrapper. 2) For the null cell in the row wrapper, fills the
	 * relevant position with dummy cell or empty cell. 3) Updates the height of the
	 * row and the cells in the row.
	 * 
	 * @param rowArea current rowArea.
	 */
	private void updateRow(RowArea rowArea, boolean isFixedLayout) {
		if (unresolvedRow != null) {
			if (!unresolvedRow.finished) {
				for (int i = startCol; i <= endCol; i++) {
					CellArea cell = unresolvedRow.getCell(i);
					if (cell != null) {
						cell.setRowSpan(cell.getRowSpan() + 1);
						i = i + cell.getColSpan();
					}
				}
			}
		}
		RowArea lastRow = null;
		if (rows.size() > 0) {
			lastRow = (RowArea) rows.getCurrent();
		}
		currentRow = rowArea;
		int sheight = rowArea.getSpecifiedHeight();
		int height = sheight;
		/*
		 * In this case, the row should be the first row of new page. 1. this row is the
		 * first row of grid in new page 2. this row is the first row of repeated header
		 * of new page
		 */
		if (lastRow == null) {
			for (int i = startCol; i <= endCol; i++) {
				CellArea upperCell = null;
				if (unresolvedRow != null) {
					upperCell = unresolvedRow.getCell(i);
				}
				CellArea cell = currentRow.getCell(i);
				if (cell == null) {
					// should create a new cell, set rowspan according to the
					// upperCell
					cell = createEmptyCell(upperCell, i, rowArea);
				} else {
					if (cell.getRowSpan() == 1) {
						if (!isFixedLayout || sheight == 0) {
							height = Math.max(height, cell.getHeight());
						}
					}
					i = i + cell.getColSpan() - 1;
				}
			}
		}
		/**
		 * Not the first row
		 */
		else {
			for (int i = startCol; i <= endCol; i++) {
				CellArea upperCell = lastRow.getCell(i);
				CellArea cell = currentRow.getCell(i);
				// upperCell has row span, or is a drop cell.
				if (upperCell != null && (upperCell.getRowSpan() > 1)) {
					if (rowArea.cells[i] != null) {
						rowArea.removeChild(rowArea.cells[i]);
					}
					DummyCell dummyCell = createDummyCell(upperCell, lastRow);
					rowArea.setCell(dummyCell);
					int delta = dummyCell.getDelta();
					if (dummyCell.getRowSpan() == 1) {
						if (!isFixedLayout || sheight == 0) {
							height = Math.max(height, dummyCell.getCell().getHeight() - delta);
						}
					}
					i = i + upperCell.getColSpan() - 1;
				} else {
					if (cell == null) {
						if (unresolvedRow != null) {
							upperCell = unresolvedRow.getCell(i);
							cell = createEmptyCell(upperCell, i, rowArea);
						} else {
							cell = createEmptyCell(null, i, rowArea);
						}

					}
					if (cell != null) {
						if (cell.getRowSpan() == 1) {
							if (!isFixedLayout || sheight == 0 && !isSpecifiedHeight(rowArea)) {
								height = Math.max(height, cell.getHeight());
							}
						}
						i = i + cell.getColSpan() - 1;
					}
				}

			}
		}
		if (unresolvedRow != null) {
			unresolvedRow = null;
		}
		updateRowHeight(rowArea, height, isFixedLayout);
	}

	private CellArea createEmptyCell(CellArea upperCell, int columnId, RowArea row) {
		ICellContent cellContent = null;
		int rowSpan = 1;

		if (upperCell != null) {
			cellContent = (ICellContent) upperCell.getContent();
			rowSpan = upperCell.getRowSpan() - 1;
		}

		if (cellContent == null) {
			cellContent = tableContent.getReportContent().createCellContent();
			cellContent.setColumn(columnId);
			cellContent.setColSpan(1);
			cellContent.setRowSpan(1);
			cellContent.setParent(row.getContent());
		}
		int emptyCellColID = cellContent.getColumn();
		int emptyCellColSpan = cellContent.getColSpan();
		CellArea emptyCell = null;
		if (upperCell != null) {
			emptyCell = upperCell.cloneArea();
		} else {
			emptyCell = new CellArea();
			emptyCell.content = cellContent;
		}

		// clear border
		BoxStyle bs = emptyCell.getBoxStyle();
		if (bs != BoxStyle.DEFAULT) {
			bs.setRightBorder(null);
			bs.setBottomBorder(null);
		}

		emptyCell.setHeight(0);
		emptyCell.setRowSpan(rowSpan);

		CellArea originalCell = upperCell;
		if (upperCell instanceof DummyCell) {
			originalCell = ((DummyCell) upperCell).getCell();
		}

		CellArea leftSideCellArea = null;
		if (emptyCellColID > startCol) {
			leftSideCellArea = row.getCell(emptyCellColID - 1);
			if (leftSideCellArea == null) {
				// the left-side cell is a dummy cell which will be
				// created in addRow()
				int k = emptyCellColID - 1;
				while (leftSideCellArea == null && k > startCol) {
					k--;
					leftSideCellArea = row.getCell(k);
				}
			}
		} else {
			leftSideCellArea = null;
		}
		emptyCell.setParent(row);
		row.setCell(emptyCell);
		resolveBorderConflict(emptyCell, true);
		emptyCell.setWidth(getCellWidth(emptyCellColID, emptyCellColID + emptyCellColSpan));
		emptyCell.setPosition(layoutInfo.getXPosition(columnId), 0);
		if (leftSideCellArea != null) {
			int index = row.indexOf(leftSideCellArea);
			row.addChild(index + 1, emptyCell);
		} else {
			row.addChild(0, emptyCell);
		}
		emptyCell.isDummy = true;
		return emptyCell;
	}

	/**
	 * Creates dummy cell and updates its delta value.
	 * 
	 * @param upperCell the upper cell.
	 * @return the created dummy cell.
	 */
	private DummyCell createDummyCell(CellArea upperCell, RowArea lastRow) {
		DummyCell dummyCell = null;
		CellArea refCell = null;
		int lastRowHeight = lastRow.getHeight();
		int delta = 0;
		if (upperCell instanceof DummyCell) {
			refCell = ((DummyCell) upperCell).getCell();
			dummyCell = new DummyCell(refCell);
			delta = ((DummyCell) upperCell).getDelta() + lastRowHeight;
			dummyCell.setDelta(delta);
		} else {
			refCell = upperCell;
			dummyCell = new DummyCell(upperCell);
			dummyCell.setDelta(lastRowHeight);
		}
		int rowSpan = upperCell.getRowSpan() - 1;
		if (unresolvedRow != null) {
			CellArea cell = unresolvedRow.getCell(upperCell.getColumnID());
			rowSpan = cell.getRowSpan() - (unresolvedRow.finished ? 1 : 0);
		}
		dummyCell.setRowSpan(rowSpan);
		dummyCell.setColSpan(upperCell.getColSpan());
		dummyCell.isDummy = true;
		return dummyCell;
	}

	/**
	 * Updates the row height and the height of the cells in the row.
	 * 
	 * @param rowArea
	 * @param height
	 */
	private void updateRowHeight(RowArea row, int height, boolean isFixedLayout) {
		if (height < 0)
			return;
		row.setHeight(height);
		for (int i = startCol; i <= endCol; i++) {
			CellArea cell = row.getCell(i);
			if (cell != null) {
				if (cell.getRowSpan() == 1) {
					if (cell instanceof DummyCell) {
						CellArea refCell = ((DummyCell) cell).getCell();
						int delta = ((DummyCell) cell).getDelta();
						refCell.setHeight(delta + height);
						align(refCell);
					} else {
						int cellHeight = cell.getHeight();
						cell.setHeight(height);
						align(cell);
						if (isFixedLayout && cellHeight > height) {
							cell.setNeedClip(true);
						}
					}
				}
				i = i + cell.getColSpan() - 1;
			}
		}
	}

	public int getCellWidth(int startColumn, int endColumn) {
		if (layoutInfo != null) {
			return layoutInfo.getCellWidth(startColumn, endColumn);
		}
		return 0;
	}

	/*
	 * public RowArea getArea( ) { return row; } }
	 */
	public CursorableList getRows() {
		return rows;
	}

	/*
	 * //---------debug
	 * 
	 * 
	 * // a method for debugging.
	 * 
	 * public static void getInfo( IArea area, int offsetX, int offsetY ) { if( area
	 * instanceof CellArea ) { System.out.println(
	 * "------------------Cell------------------" ); //top border int x = offsetX +
	 * area.getX( ); int y = offsetY + area.getY( ); int rx = offsetX + area.getX( )
	 * + area.getWidth(); int by = offsetY + area.getY( ) + area.getHeight();
	 * System.out.print("Top border:"); System.out.print("(" + x+", " + y + ")\t"
	 * +"(" + rx +", " + y + ")\n" ); // System.out.println("style: " +
	 * area.getStyle( ).getProperty( StyleConstants.STYLE_BORDER_TOP_STYLE ) ); //
	 * System.out.println("color: " + area.getStyle( ).getProperty(
	 * StyleConstants.STYLE_BORDER_TOP_COLOR ) ); // System.out.println("width: " +
	 * area.getStyle( ).getProperty( StyleConstants.STYLE_BORDER_TOP_WIDTH ) );
	 * //left border System.out.print("Left border:"); System.out.print("(" + x+", "
	 * + y + ")\t" +"(" + x +", " + by + ")\n" ); // System.out.println("style: " +
	 * area.getStyle( ).getProperty( StyleConstants.STYLE_BORDER_LEFT_STYLE ) ); //
	 * System.out.println("color: " + area.getStyle( ).getProperty(
	 * StyleConstants.STYLE_BORDER_LEFT_COLOR ) ); // System.out.println("width: " +
	 * area.getStyle( ).getProperty( StyleConstants.STYLE_BORDER_LEFT_WIDTH ) );
	 * traverse( area, offsetX, offsetY ); System.out.println(
	 * "------------------Cell end---------------" ); } else if ( area instanceof
	 * TextArea ) { TextArea textArea = (TextArea )area; System.out.println(
	 * "$$text$$" + textArea.getText( ) ); } else if ( area instanceof ContainerArea
	 * ) { traverse( area, offsetX, offsetY ); } }
	 * 
	 * private static void traverse( IArea area, int offsetX, int offsetY ) {
	 * ContainerArea container = (ContainerArea) area; offsetX = offsetX +
	 * area.getX( ); offsetY = offsetY + area.getY( ); for ( Iterator i =
	 * container.getChildren( ); i.hasNext( ); ) { getInfo( (IArea) i.next( ),
	 * offsetX, offsetY ); } offsetX = offsetX - area.getX( ); offsetY = offsetY -
	 * area.getY( ); }
	 * 
	 */

}
