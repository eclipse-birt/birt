/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.nLayout.area.impl;

import java.awt.Color;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.style.BackgroundImageInfo;
import org.eclipse.birt.report.engine.nLayout.area.style.BoxStyle;
import org.eclipse.birt.report.engine.nLayout.area.style.DiagonalInfo;
import org.eclipse.birt.report.engine.util.ResourceLocatorWrapper;
import org.w3c.dom.css.CSSValue;

public class CellArea extends BlockContainerArea implements IContainerArea {

	static int DEFAULT_PADDING = 1500;
	static LocalProperties CELL_DEFAULT = new LocalProperties();
	protected int rowSpan = 1;
	protected int colSpan = 1;
	protected int columnID = 0;
	protected int rowID = 0;

	protected DiagonalInfo diagonalInfo;

	static {
		CELL_DEFAULT.setPaddingTop(DEFAULT_PADDING);
		CELL_DEFAULT.setPaddingRight(DEFAULT_PADDING);
		CELL_DEFAULT.setPaddingBottom(DEFAULT_PADDING);
		CELL_DEFAULT.setPaddingLeft(DEFAULT_PADDING);
	}

	public CellArea(ContainerArea parent, LayoutContext context, IContent content) {
		super(parent, context, content);
	}

	public CellArea() {
		super();
		localProperties = CELL_DEFAULT;
	}

	public CellArea(CellArea cell) {
		super(cell);
		rowSpan = cell.rowSpan;
		colSpan = cell.colSpan;
		columnID = cell.columnID;
		rowID = cell.rowID;
		diagonalInfo = cell.diagonalInfo;
	}

	public DiagonalInfo getDiagonalInfo() {
		return diagonalInfo;
	}

	public void setDiagonalInfo(DiagonalInfo diagonalInfo) {
		this.diagonalInfo = diagonalInfo;
	}

	public int getColumnID() {
		return columnID;
	}

	public void setColumnID(int columnID) {
		this.columnID = columnID;
	}

	public int getRowID() {
		return rowID;
	}

	public void setRowID(int rowID) {
		this.rowID = rowID;
	}

	public int getColSpan() {
		return colSpan;
	}

	public void setColSpan(int colSpan) {
		this.colSpan = colSpan;
	}

	public int getRowSpan() {
		return rowSpan;
	}

	public void setRowSpan(int rowSpan) {
		this.rowSpan = rowSpan;
	}

	@Override
	public void close() throws BirtException {
		height = currentBP + getOffsetY() + localProperties.getPaddingBottom();
		// We don't update background image here. As the row height may be
		// updated later.
		// updateBackgroundImage( );
		checkPageBreak();
		parent.update(this);
		finished = true;
		checkDisplayNone();
	}

	@Override
	public void initialize() throws BirtException {
		ICellContent cellContent = (ICellContent) content;
		rowSpan = cellContent.getRowSpan();
		columnID = cellContent.getColumn();
		colSpan = cellContent.getColSpan();
		TableArea table = getTable();
		hasStyle = true;
		width = table.getCellWidth(columnID, columnID + colSpan);
		buildProperties(cellContent, context);
		buildDiagonalInfo();
		table.resolveBorderConflict(this, true);

		maxAvaWidth = getContentWidth();

		boolean isLastColumn = (columnID + colSpan == table.getColumnCount());
		if (!table.isInInlineStacking && isLastColumn) {
			isInInlineStacking = false;
		} else {
			isInInlineStacking = true;
		}
		this.bookmark = content.getBookmark();
		this.action = content.getHyperlinkAction();
		parent.add(this);
	}

	protected void buildDiagonalInfo() {
		ICellContent cellContent = (ICellContent) content;
		if (cellContent.hasDiagonalLine()) {
			int diagonalNumber = cellContent.getDiagonalNumber();
			int diagonalWidth = PropertyUtil.getDimensionValue(cellContent, cellContent.getDiagonalWidth(), width);
			String diagonalStyle = cellContent.getDiagonalStyle();
			if (diagonalNumber > 0 && diagonalWidth > 0 && diagonalStyle != null) {
				Color dc = PropertyUtil.getColor(cellContent.getDiagonalColor());
				if (dc == null) {
					dc = PropertyUtil.getColor(cellContent.getComputedStyle().getProperty(IStyle.STYLE_COLOR));
				}
				diagonalInfo = new DiagonalInfo();
				diagonalInfo.setDiagonal(diagonalNumber, diagonalStyle, diagonalWidth, dc);
			}
			int antidiagonalNumber = cellContent.getAntidiagonalNumber();
			int antidiagonalWidth = PropertyUtil.getDimensionValue(cellContent, cellContent.getAntidiagonalWidth(),
					width);
			String antidiagonalStyle = cellContent.getAntidiagonalStyle();
			if (antidiagonalNumber > 0 && antidiagonalWidth > 0 && antidiagonalStyle != null) {
				if (diagonalInfo == null) {
					diagonalInfo = new DiagonalInfo();
				}
				Color adc = PropertyUtil.getColor(cellContent.getAntidiagonalColor());
				if (adc == null) {
					adc = PropertyUtil.getColor(cellContent.getComputedStyle().getProperty(IStyle.STYLE_COLOR));
				}

				diagonalInfo.setAntiDiagonal(antidiagonalNumber, antidiagonalStyle, antidiagonalWidth, adc);
			}
		}
	}

	@Override
	protected void buildProperties(IContent content, LayoutContext context) {
		IStyle style = content.getComputedStyle();
		boxStyle = new BoxStyle();
		Color color = PropertyUtil.getColor(style.getProperty(IStyle.STYLE_BACKGROUND_COLOR));
		if (color != null) {
			boxStyle.setBackgroundColor(color);
		}
		String url = content.getStyle().getBackgroundImage();
		if (url != null) {
			ResourceLocatorWrapper rl = null;
			ExecutionContext exeContext = ((ReportContent) content.getReportContent()).getExecutionContext();
			if (exeContext != null) {
				rl = exeContext.getResourceLocator();
			}
			BackgroundImageInfo backgroundImage = new BackgroundImageInfo(getImageUrl(url),
					style.getProperty(IStyle.STYLE_BACKGROUND_REPEAT), 0, 0, 0, 0, rl);
			boxStyle.setBackgroundImage(backgroundImage);
		}
		localProperties = new LocalProperties();
		IStyle cs = content.getStyle();
		CSSValue padding = cs.getProperty(IStyle.STYLE_PADDING_TOP);
		if (padding == null) {
			localProperties.setPaddingTop(DEFAULT_PADDING);
		} else {
			localProperties.setPaddingTop(getDimensionValue(style.getProperty(IStyle.STYLE_PADDING_TOP), width));
		}
		padding = cs.getProperty(IStyle.STYLE_PADDING_BOTTOM);
		if (padding == null) {
			localProperties.setPaddingBottom(DEFAULT_PADDING);
		} else {
			localProperties.setPaddingBottom(getDimensionValue(style.getProperty(IStyle.STYLE_PADDING_BOTTOM), width));
		}
		padding = cs.getProperty(IStyle.STYLE_PADDING_LEFT);
		if (padding == null) {
			localProperties.setPaddingLeft(DEFAULT_PADDING);
		} else {
			localProperties.setPaddingLeft(getDimensionValue(style.getProperty(IStyle.STYLE_PADDING_LEFT), width));
		}
		padding = cs.getProperty(IStyle.STYLE_PADDING_RIGHT);
		if (padding == null) {
			localProperties.setPaddingRight(DEFAULT_PADDING);
		} else {
			localProperties.setPaddingRight(getDimensionValue(style.getProperty(IStyle.STYLE_PADDING_RIGHT), width));
		}
		textAlign = content.getComputedStyle().getProperty(IStyle.STYLE_TEXT_ALIGN);
	}

	@Override
	public CellArea cloneArea() {
		CellArea cell = new CellArea(this);
		cell.setRowSpan(rowSpan);
		cell.setColSpan(colSpan);
		cell.setBoxStyle(new BoxStyle(cell.getBoxStyle()));
		return cell;
	}

	@Override
	public void update(AbstractArea area) throws BirtException {
		super.update(area);
		// width exceed the cell with or negative margin
		if (currentIP + area.getAllocatedWidth() > getContentWidth() || area.getX() < 0) {
			setNeedClip(true);
		}
	}

	@Override
	public boolean isPageBreakAfterAvoid() {
		return false;
	}

	@Override
	public boolean isPageBreakBeforeAvoid() {
		return false;
	}

	@Override
	public boolean isPageBreakInsideAvoid() {
		return false;
	}

	@Override
	public CellArea deepClone() {
		CellArea cell = (CellArea) super.deepClone();
		cell.setRowSpan(rowSpan);
		cell.setColSpan(colSpan);
		cell.setBoxStyle(new BoxStyle(cell.getBoxStyle()));
		if (getRowSpan() > 1) {
			cell.setHeight(currentBP + getOffsetY() + localProperties.getPaddingBottom());
		}
		return cell;
	}

}
