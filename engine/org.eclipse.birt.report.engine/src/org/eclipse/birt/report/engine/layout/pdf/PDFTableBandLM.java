
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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IElement;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.dom.DOMReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;

public class PDFTableBandLM extends PDFBlockStackingLM implements IBlockStackingLayoutManager {

	protected PDFTableLM tbl;
	protected int groupLevel;
	protected int type;
	protected boolean repeatHeader = false;

	public PDFTableBandLM(PDFLayoutEngineContext context, PDFStackingLM parent, IContent content,
			IReportItemExecutor executor) throws BirtException {
		super(context, parent, content, executor);
		tbl = getTableLayoutManager();
		IElement pContent = content.getParent();

		if (pContent instanceof IGroupContent) {
			IGroupContent groupContent = (IGroupContent) pContent;
			groupLevel = groupContent.getGroupLevel();
			repeatHeader = groupContent.isHeaderRepeat();
			type = ((IBandContent) content).getBandType();
			if (type == IBandContent.BAND_GROUP_HEADER && !(executor instanceof DOMReportItemExecutor)
					&& repeatHeader) {
				execute(executor, content);
				executor.close();
				groupContent.getChildren().add(content);
				this.executor = new DOMReportItemExecutor(content);
				this.executor.execute();
			}
		} else if (pContent instanceof ITableContent) {
			ITableContent tableContent = (ITableContent) pContent;
			repeatHeader = tableContent.isHeaderRepeat();
			type = ((IBandContent) content).getBandType();
			if (type == IBandContent.BAND_HEADER && !(executor instanceof DOMReportItemExecutor) && repeatHeader) {
				execute(executor, content);
				executor.close();
				tableContent.getChildren().add(content);
				this.executor = new DOMReportItemExecutor(content);
				this.executor.execute();
			}
		}

	}

	/*
	 * protected boolean checkAvailableSpace( ) { boolean availableSpace =
	 * super.checkAvailableSpace( ); if(availableSpace && tbl != null) {
	 * tbl.setTableCloseStateAsForced( ); } return availableSpace; }
	 */

	@Override
	protected boolean traverseChildren() throws BirtException {
		if (isFirst && groupLevel >= 0 && type == IBandContent.BAND_GROUP_FOOTER) {
			int height;
			height = tbl.updateUnresolvedCell(groupLevel, false);
			if (0 != height) {
				((PDFTableGroupLM) parent).updateHeight(height);
			}
		}
		isFirst = false;
		boolean childBreak = super.traverseChildren();
		if (!childBreak && groupLevel >= 0 && type == IBandContent.BAND_GROUP_FOOTER) {
			int height;
			height = tbl.updateUnresolvedCell(groupLevel, true);
			if (0 != height) {
				((PDFTableGroupLM) parent).updateHeight(height);
			}
		}
		return childBreak;
	}

	@Override
	public int getCurrentBP() {
		return parent.getCurrentBP();
	}

	protected boolean submitRoot(boolean childBreak) {
		return true;
	}

	@Override
	public int getCurrentIP() {
		return parent.getCurrentIP();
	}

	@Override
	public int getCurrentMaxContentHeight() {
		return parent.getCurrentMaxContentHeight();
	}

	@Override
	public int getCurrentMaxContentWidth() {
		return parent.getCurrentMaxContentWidth();
	}

	@Override
	public int getOffsetX() {
		return parent.getOffsetX();
	}

	@Override
	public int getOffsetY() {
		return parent.getOffsetY();
	}

	@Override
	public void setCurrentBP(int bp) {
		parent.setCurrentBP(bp);
	}

	@Override
	public void setCurrentIP(int ip) {
		parent.setCurrentIP(ip);
	}

	@Override
	public void setOffsetX(int x) {
		parent.setOffsetX(x);
	}

	@Override
	public int getMaxAvaHeight() {
		return parent.getMaxAvaHeight();
	}

	@Override
	public void setOffsetY(int y) {
		parent.setOffsetY(y);
	}

	@Override
	public boolean addArea(IArea area, boolean keepWithPrevious, boolean keepWithNext) {
		return parent.addArea(area, false, false);
	}

	@Override
	protected void createRoot() {
		// do nothing
	}

	@Override
	protected void initialize() {

	}

	@Override
	protected IReportItemExecutor createExecutor() {
		return executor;
	}

	// support page-break on header band at the first layout.
	/*
	 * protected boolean canPageBreak( ) { if(!allowPageBreak()) { return false; }
	 * return super.canPageBreak( ); }
	 */

	@Override
	protected boolean allowPageBreak() {
		if (type == IBandContent.BAND_GROUP_HEADER || type == IBandContent.BAND_HEADER) {
			return !repeatHeader;
		}
		return true;
	}

	@Override
	public void submit(AbstractArea area) {
		parent.submit(area);
	}

	@Override
	protected boolean addToRoot(AbstractArea area) {
		if (getCurrentBP() + area.getAllocatedHeight() <= getMaxAvaHeight()) {
			parent.addArea(area, false, false);
			return true;
		} else {
			return false;
		}
	}

}
