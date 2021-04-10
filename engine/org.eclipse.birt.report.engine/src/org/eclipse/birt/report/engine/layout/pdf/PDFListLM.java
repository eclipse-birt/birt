/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.internal.executor.dom.DOMReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.content.ListContainerExecutor;

public class PDFListLM extends PDFBlockStackingLM implements IBlockStackingLayoutManager {
	boolean needRepeat;
	boolean repeat = false;
	int repeatCount = 0;

	public PDFListLM(PDFLayoutEngineContext context, PDFStackingLM parent, IContent content,
			IReportItemExecutor executor) {
		super(context, parent, content, executor);
		repeat = isRepeatHeader();
	}

	protected void initialize() throws BirtException {
		if (root == null && keepWithCache.isEmpty() && !isFirst) {
			repeatCount = 0;
			needRepeat = true;
		}
		super.initialize();

	}

	protected IListBandContent getHeader() {
		return ((IListContent) content).getHeader();
	}

	protected boolean isRepeatHeader() {
		return ((IListContent) content).isHeaderRepeat();
	}

	protected boolean isRootEmpty() {
		return !(root != null && root.getChildrenCount() > repeatCount);
	}

	protected void createRoot() {
		if (root == null) {
			root = (ContainerArea) AreaFactory.createBlockContainer(content);
		}
	}

	protected void repeatHeader() throws BirtException {
		if (isFirst) {
			return;
		}
		if (!needRepeat || !isRepeatHeader()) {
			return;
		}
		IListBandContent band = getHeader();
		if (band == null) {
			return;
		}
		IReportItemExecutor headerExecutor = new DOMReportItemExecutor(band);
		headerExecutor.execute();
		ContainerArea headerArea = (ContainerArea) AreaFactory.createLogicContainer(content.getReportContent());
		headerArea.setAllocatedWidth(parent.getCurrentMaxContentWidth());
		PDFRegionLM regionLM = new PDFRegionLM(context, headerArea, band, headerExecutor);
		boolean allowPB = context.allowPageBreak();
		context.setAllowPageBreak(false);
		regionLM.layout();
		context.setAllowPageBreak(allowPB);
		if (headerArea.getAllocatedHeight() < getCurrentMaxContentHeight())// FIXME need check
		{
			addArea(headerArea, false, pageBreakAvoid);
			repeatCount++;
		}
		needRepeat = false;
	}

	protected boolean traverseChildren() throws BirtException {
		repeatHeader();
		return super.traverseChildren();

	}

	protected IReportItemExecutor createExecutor() {
		return new ListContainerExecutor(content, executor);
	}

}
