/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.RowArea;
import org.eclipse.birt.report.engine.layout.pdf.emitter.TableAreaLayout.Row;
import org.eclipse.birt.report.engine.layout.pdf.emitter.TableLayout.TableContext;

public class RowLayout extends ContainerLayout {
	protected TableLayout tbl;

	protected Row unresolvedRow = null;

	public RowLayout(LayoutEngineContext context, ContainerLayout parent, IContent content) {
		super(context, parent, content);
		tbl = getTableLayoutManager();
	}

	protected void createRoot() {
		currentContext.root = AreaFactory.createRowArea((IRowContent) content);
	}

	protected void initialize() throws BirtException {
		calculateSpecifiedHeight();
		if (specifiedHeight > parent.getCurrentMaxContentHeight()) {
			if (!parent.isPageEmpty()) {
				parent.autoPageBreak();
				if (isInBlockStacking) {
					if (parent.contextList.size() > 1) {
						parent.closeExcludingLast();
					}
				}
			}
		}
		currentContext = new ContainerContext();
		contextList.add(currentContext);
		createRoot();
		currentContext.maxAvaWidth = parent.getCurrentMaxContentWidth();
		currentContext.root.setWidth(getCurrentMaxContentWidth());
		currentContext.root.setAllocatedHeight(parent.getCurrentMaxContentHeight());
		currentContext.maxAvaHeight = currentContext.root.getContentHeight();
	}

	/*
	 * protected void closeLayout( ) { super.closeLayout(); parent.gotoLastPage(); }
	 */

	protected void closeLayout() throws BirtException {
		int size = contextList.size();
		for (int i = 0; i < size; i++) {
			int parentSize = parent.contextList.size();
			closeLayout(contextList.removeFirst(), parentSize - size + i, size, i, i == size - 1);
		}
		if (isInBlockStacking) {
			if (size > 1) {
				parent.closeExcludingLast();
			}
		}
		parent.gotoLastPage();
	}

	/**
	 * 
	 * @param currentContext the context containing the sub-row which is going to be
	 *                       closed.
	 * @param parentIndex    the parent(sub-table) index.
	 * @param size           after pagination, a row is split into size sub-rows.
	 * @param index          the index of the sub-row.
	 * @param finished       is it the last sub-row.
	 */
	protected void closeLayout(ContainerContext currentContext, int parentIndex, int size, int index,
			boolean finished) {
		if (currentContext.root != null) {
			int tableSize = tbl.contextList.size();
			if (unresolvedRow != null) {
				TableContext tc = (TableContext) (tbl.contextList.get(tableSize - size + index));
				tc.layout.setUnresolvedRow(unresolvedRow);
			}

			if (finished || !isRowEmpty(currentContext)) {
				tbl.addRow((RowArea) currentContext.root, specifiedHeight, index, size);
				parent.addToRoot(currentContext.root, parentIndex);
			}
			if (!finished && unresolvedRow == null) {
				TableContext tc = (TableContext) (tbl.contextList.get(tableSize - size + index));
				if (isRowEmpty(currentContext)) {
					unresolvedRow = tc.layout.createUnresolvedRow((RowArea) currentContext.root);
				} else {
					unresolvedRow = tc.layout.getUnresolvedRow();
				}
			}
		}
	}

	protected void closeExcludingLast() throws BirtException {
		// Current layout should be in block stacking.
		int size = contextList.size();
		closeFirstN(size - 1);
	}

	protected void closeFirstN(int size) throws BirtException {
		int rowSize = contextList.size();
		for (int i = 0; i < size; i++) {
			closeLayout(contextList.removeFirst(), i, rowSize, i, false);
		}
		setCurrentContext(0);
		if (parent != null) {
			parent.closeFirstN(size);
		}
	}

	protected void closeLayout(ContainerContext currentContext, int index, boolean finished) {
		/*
		 * if ( currentContext.root != null ) { if ( unresolvedRow != null ) {
		 * TableContext tc = (TableContext) ( tbl.contextList.get( index ) );
		 * tc.layout.setUnresolvedRow( unresolvedRow ); } tbl.updateRow( (RowArea)
		 * currentContext.root, specifiedHeight, index ); if ( finished || !isRowEmpty(
		 * currentContext ) ) { tbl.addRow( (RowArea) currentContext.root, index);
		 * parent.addToRoot( currentContext.root, index ); } if ( !finished &&
		 * unresolvedRow == null ) { TableContext tc = (TableContext) (
		 * tbl.contextList.get( index ) ); unresolvedRow = tc.layout.getUnresolvedRow(
		 * ); } }
		 */
	}

	protected boolean isRowEmpty(ContainerContext currentContext) {
		Iterator iter = currentContext.root.getChildren();
		while (iter.hasNext()) {
			ContainerArea area = (ContainerArea) iter.next();
			if (area.getChildrenCount() > 0) {
				return false;
			}
		}
		return true;
	}

	protected void addToRoot(AbstractArea area) {
		CellArea cArea = (CellArea) area;
		currentContext.root.addChild(area);

		int columnID = cArea.getColumnID();
		int colSpan = cArea.getColSpan();
		// Retrieve direction from the top-level content.
		if (colSpan > 1 && content.isRTL()) {
			columnID += colSpan - 1;
		}

		cArea.setPosition(tbl.getXPos(columnID), 0);
	}

}
