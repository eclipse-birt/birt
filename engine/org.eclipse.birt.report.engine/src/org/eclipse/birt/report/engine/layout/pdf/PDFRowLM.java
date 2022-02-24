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

package org.eclipse.birt.report.engine.layout.pdf;

import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IInlineStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.ILayoutManager;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.CellArea;
import org.eclipse.birt.report.engine.layout.area.impl.RowArea;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

public class PDFRowLM extends PDFInlineStackingLM implements IInlineStackingLayoutManager {

	protected PDFTableLM tbl;

	protected boolean hasNext = false;

	public PDFRowLM(PDFLayoutEngineContext context, PDFStackingLM parent, IContent content,
			IReportItemExecutor executor) {
		super(context, parent, content, executor);
		tbl = getTableLayoutManager();
		calculateSpecifiedHeight();
	}

	/*
	 * protected void calculateSpecifiedHeight( ) { super.calculateSpecifiedHeight(
	 * ); if ( specifiedHeight == 0 ) { IStyle style = content.getComputedStyle( );
	 * int fontSize = getDimensionValue( style .getProperty( IStyle.STYLE_FONT_SIZE
	 * ) ); specifiedHeight = fontSize; } }
	 */

	protected void createRoot() {
		root = AreaFactory.createRowArea((IRowContent) content);
	}

	protected void initialize() {
		if (root == null) {
			createRoot();
		}
		maxAvaWidth = parent.getCurrentMaxContentWidth();
		root.setWidth(getCurrentMaxContentWidth());
		root.setAllocatedHeight(parent.getCurrentMaxContentHeight());
		maxAvaHeight = root.getContentHeight();
		hasNext = false;
	}

	protected boolean traverseChildren() throws BirtException {

		// first loop
		if (children.size() == 0) {
			while (executor.hasNextChild()) {
				IReportItemExecutor childExecutor = executor.getNextChild();
				IContent childContent = childExecutor.execute();
				PDFAbstractLM childLM = getFactory().createLayoutManager(this, childContent, childExecutor);
				addChild(childLM);
				if (childLM.layout() && !hasNext) {
					hasNext = true;
				}
			}
		} else {
			if (!isRowFinished()) {
				for (int i = 0; i < children.size(); i++) {
					ILayoutManager childLM = (ILayoutManager) children.get(i);
					if (childLM.layout() && !hasNext) {
						hasNext = true;
					}
				}
			}
		}
		return hasNext;
	}

	protected void closeLayout() {
		if (root != null) {
			tbl.updateRow((RowArea) root, specifiedHeight, !hasNext);
		}
	}

	protected boolean submitRoot() {
		RowArea row = (RowArea) root;
		boolean ret = super.submitRoot();
		if (ret) {
			tbl.addRow(row, !hasNext, false);
		}
		return ret;

	}

	/*
	 * protected boolean isHidden( ) { return isHiddenByVisibility( ); }
	 */

	public boolean addArea(IArea area, boolean keepWithPrevious, boolean keepWithNext) {
		submit((AbstractArea) area);
		return true;
	}

	protected boolean isRowFinished() {
		for (int i = 0; i < children.size(); i++) {
			PDFAbstractLM lm = (PDFAbstractLM) children.get(i);
			if (lm != null) {
				if (!lm.isFinished()) {
					return false;
				}
			}
		}
		return true;
	}

	protected boolean hasNextChild() {
		if (children.size() > 0) {
			return !isRowFinished();
		}
		return true;

	}

	protected boolean isRootEmpty() {

		if (root != null) {
			Iterator iter = root.getChildren();
			while (iter.hasNext()) {
				CellArea cell = (CellArea) iter.next();
				if (cell.getChildrenCount() > 0) {
					return false;
				}
			}
			if (isRowFinished() && root.getChildrenCount() > 0) {
				return false;
			}
		}
		return true;

	}

	public void submit(AbstractArea area) {
		CellArea cArea = (CellArea) area;
		root.addChild(area);

		// bidi_hcg start
		int columnID = cArea.getColumnID();
		int colSpan = cArea.getColSpan();
		if (colSpan > 1) {
			ReportDesignHandle design = context.report.getDesign().getReportDesign();
			if (design.isDirectionRTL())
				columnID += colSpan - 1;
		}
		// bidi_hcg end

		cArea.setPosition(tbl.getXPos(columnID), 0);
	}

	protected boolean clearCache() {
		// TODO Auto-generated method stub
		return false;
	}

}
