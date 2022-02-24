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

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;

public class CellLayout extends BlockStackingLayout {

	/**
	 * table layout manager of current cell
	 */
	protected TableLayout tableLayout;

	protected int columnWidth = 0;

	/**
	 * cell content
	 */
	private ICellContent cellContent;

	public CellLayout(LayoutEngineContext context, ContainerLayout parent, IContent content) {
		super(context, parent, content);
		tableLayout = getTableLayoutManager();

		cellContent = (ICellContent) content;
		// tableLM.startCell( cellContent );

		// set max width constraint
		int startColumn = cellContent.getColumn();
		int endColumn = startColumn + cellContent.getColSpan();
		columnWidth = tableLayout.getCellWidth(startColumn, endColumn);

		boolean isLastColumn = (endColumn == tableLayout.getColumnCount());
		if (tableLayout.isInBlockStacking && isLastColumn) {
			isInBlockStacking = true;
		} else {
			isInBlockStacking = false;
		}
		isInline = true;
	}

	protected void createRoot() {
		CellArea cell = AreaFactory.createCellArea(cellContent);
		cell.setRowSpan(cellContent.getRowSpan());
		currentContext.root = cell;
		int startColumn = cellContent.getColumn();
		int endColumn = startColumn + cellContent.getColSpan();
		columnWidth = tableLayout.getCellWidth(startColumn, endColumn);
		tableLayout.resolveBorderConflict((CellArea) currentContext.root, true);
		removeMargin(currentContext.root.getStyle());
		currentContext.root.setWidth(columnWidth);
	}

	protected void initialize() {
		currentContext = new ContainerContext();
		contextList.add(currentContext);
		createRoot();
		validateBoxProperty(currentContext.root.getStyle(), columnWidth, context.getMaxHeight());
		offsetX = currentContext.root.getContentX();
		offsetY = currentContext.root.getContentY();
		currentContext.maxAvaWidth = currentContext.root.getContentWidth();
		currentContext.root.setAllocatedHeight(parent.getCurrentMaxContentHeight());
		currentContext.maxAvaHeight = currentContext.root.getContentHeight();
	}

	protected void closeLayout(ContainerContext currentContext, int index, boolean finished) {
		currentContext.root.setHeight(currentContext.currentBP + offsetY
				+ getDimensionValue(currentContext.root.getStyle().getProperty(StyleConstants.STYLE_PADDING_BOTTOM)));
		parent.addToRoot(currentContext.root, index);
	}

	protected void align(ContainerArea container) {
		// Do nothing, this is handled by Tablelayout.
	}

	protected void closeLayout(int size, boolean finished) {
		for (int i = 0; i < size; i++) {
			closeLayout(contextList.removeFirst(), i, finished && i == (size - 1));
		}
		if (contextList.size() > 0) {
			currentContext = contextList.getFirst();
		}
	}

}
