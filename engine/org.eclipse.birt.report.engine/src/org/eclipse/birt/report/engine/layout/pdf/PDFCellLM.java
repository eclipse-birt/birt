/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;

public class PDFCellLM extends PDFBlockStackingLM implements IBlockStackingLayoutManager {

	/**
	 * table layout manager of current cell
	 */
	protected PDFTableLM tableLM;

	protected int columnWidth = 0;

	/**
	 * cell content
	 */
	private ICellContent cellContent;

	public PDFCellLM(PDFLayoutEngineContext context, PDFStackingLM parent, IContent content,
			IReportItemExecutor executor) {
		super(context, parent, content, executor);
		assert (parent != null);
		tableLM = getTableLayoutManager();
		cellContent = (ICellContent) content;
	}

	protected void createRoot() {
		if (root == null) {
			// FIXME setup rowSpan
			CellArea cell = AreaFactory.createCellArea(cellContent);
			cell.setRowSpan(tableLM.getRowSpan(cellContent));
			root = cell;

			if (!isFirst) {
				IStyle areaStyle = root.getStyle();
				/*
				 * areaStyle.setProperty( IStyle.STYLE_BORDER_TOP_WIDTH, IStyle.NUMBER_0 );
				 */
				areaStyle.setProperty(IStyle.STYLE_PADDING_TOP, IStyle.NUMBER_0);
				areaStyle.setProperty(IStyle.STYLE_MARGIN_TOP, IStyle.NUMBER_0);
			}
		}
		tableLM.resolveBorderConflict((CellArea) root, isFirst);
		root.setWidth(columnWidth);
	}

	protected void initialize() {
		boolean isNewArea = (root == null);
		createRoot();
		if (isNewArea) {
			IStyle areaStyle = root.getStyle();
			removeMargin(areaStyle);
			validateBoxProperty(root.getStyle(), columnWidth, context.getMaxHeight());
			setOffsetX(root.getContentX());
			setOffsetY(root.getContentY());
			setCurrentBP(0);
			setCurrentIP(0);
		}
		maxAvaWidth = root.getContentWidth();
		root.setAllocatedHeight(parent.getCurrentMaxContentHeight());
		maxAvaHeight = root.getContentHeight();
	}

	protected void closeLayout() {
		if (root != null) {
			root.setHeight(getCurrentBP() + getOffsetY()
					+ getDimensionValue(root.getStyle().getProperty(StyleConstants.STYLE_PADDING_BOTTOM)));
		}

	}

	protected boolean isHidden() {
		int startColumn = cellContent.getColumn();
		int endColumn = startColumn + cellContent.getColSpan();
		columnWidth = tableLM.getCellWidth(startColumn, endColumn);
		if (columnWidth == 0 || !tableLM.isCellVisible(cellContent)) {
			return true;
		}
		return isHiddenByVisibility();
	}

	protected boolean isRootEmpty() {
		if (isLast) {
			return false;
		}
		if (parent.root != null && parent.root.getChildrenCount() > 0) {
			return false;
		}
		return super.isRootEmpty();
	}

	public boolean pageBreakInsideAvoid() {
		if (content == null) {
			return false;
		}
		IContent row = (IContent) content.getParent();
		if (row != null) {
			IStyle style = row.getStyle();
			String pageBreak = style.getPageBreakInside();
			if (IStyle.CSS_AVOID_VALUE == pageBreak) {
				return true;
			}
		}
		return false;
	}

	public boolean isPageEmpty() {
		if (root != null && root.getChildrenCount() > 0) {
			return false;
		} else {
			if (parent != null) {
				return parent.isPageEmpty();
			}
		}
		return true;
	}

	protected boolean needPageBreakAfter(String pageBreak) {
		return false;
	}

	protected boolean needPageBreakBefore(String pageBreak) {
		return false;
	}

}
