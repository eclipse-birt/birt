/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.ir.BandDesign;
import org.eclipse.birt.report.engine.ir.ListBandDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.w3c.dom.css.CSSValue;

public class ListBandExecutor extends StyledItemExecutor {

	protected ListBandExecutor(ExecutorManager manager) {
		super(manager, ExecutorManager.LISTBANDITEM);
	}

	ListingElementExecutor listExecutor;

	void setListingExecutor(ListingElementExecutor listExecutor) {
		this.listExecutor = listExecutor;
	}

	public IContent execute() {
		ListBandDesign bandDesign = (ListBandDesign) getDesign();

		IListBandContent bandContent = report.createListBandContent();
		setContent(bandContent);

		restoreResultSet();

		initializeContent(bandDesign, bandContent);

		startTOCEntry(bandContent);
		handlePageBreakInterval();
		// prepare to execute the children
		currentItem = 0;

		return bandContent;
	}

	protected void handlePageBreakInterval() {
		if (listExecutor.breakOnDetailBand) {
			BandDesign band = (BandDesign) design;
			if (band.getBandType() == BandDesign.BAND_DETAIL) {
				if (listExecutor.softBreakBefore) {
					IStyle style = content.getStyle();
					if (style != null) {
						CSSValue pageBreak = style.getProperty(IStyle.STYLE_PAGE_BREAK_BEFORE);
						if (pageBreak == null || IStyle.AUTO_VALUE.equals(pageBreak)) {
							style.setProperty(IStyle.STYLE_PAGE_BREAK_BEFORE, IStyle.SOFT_VALUE);
						}
					}
					listExecutor.softBreakBefore = false;
					listExecutor.addAfterBreak = true;
					listExecutor.pageRowCount = 0;
				}
				listExecutor.next();
				if (listExecutor.needSoftBreakAfter()) {
					listExecutor.softBreakBefore = true;
				}
			}
		}
	}

	public void close() throws BirtException {
		finishTOCEntry();
		super.close();
	}

	int currentItem;

	public boolean hasNextChild() {
		ListBandDesign bandDesign = (ListBandDesign) design;
		return currentItem < bandDesign.getContentCount();
	}

	public IReportItemExecutor getNextChild() {
		ListBandDesign bandDesign = (ListBandDesign) design;
		if (currentItem < bandDesign.getContentCount()) {
			ReportItemDesign itemDesign = bandDesign.getContent(currentItem++);
			ReportItemExecutor executor = manager.createExecutor(this, itemDesign);
			return executor;
		}
		return null;
	}
}
