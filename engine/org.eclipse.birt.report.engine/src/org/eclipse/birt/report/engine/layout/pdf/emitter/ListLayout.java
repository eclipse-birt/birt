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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IListBandContent;
import org.eclipse.birt.report.engine.content.IListContent;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

public class ListLayout extends RepeatableLayout {

	public ListLayout(LayoutEngineContext context, ContainerLayout parent, IContent content) {
		super(context, parent, content);
		isInline = PropertyUtil.isInlineElement(content);
		isInBlockStacking &= !isInline;
	}

	@Override
	protected void repeatHeader() throws BirtException {
		if (bandStatus == IBandContent.BAND_HEADER) {
			return;
		}
		IListContent listContent = (IListContent) content;
		if (!listContent.isHeaderRepeat()) {
			return;
		}
		IListBandContent band = listContent.getHeader();
		if (band == null || band.getChildren().isEmpty()) {
			return;
		}
		ContainerArea headerArea = (ContainerArea) AreaFactory.createLogicContainer(content.getReportContent());
		headerArea.setAllocatedWidth(parent.getCurrentMaxContentWidth());
		Layout regionLayout = new RegionLayout(context, band, headerArea);
		regionLayout.layout();

		if (headerArea.getAllocatedHeight() < getCurrentMaxContentHeight())// FIXME need check
		{
			addArea(headerArea);
			repeatCount++;
		}
	}

	@Override
	protected void initialize() throws BirtException {
		checkInlineBlock();
		super.initialize();
	}

	/*
	 * protected void closeLayout( ) { super.closeLayout();
	 * if(PropertyUtil.isInlineElement(content)&&parent!=null) {
	 * parent.gotoFirstPage(); } }
	 */

	protected void checkInlineBlock() throws BirtException {
		if (PropertyUtil.isInlineElement(content)) {
			if (parent instanceof IInlineStackingLayout) {
				int avaWidth = parent.getCurrentMaxContentWidth();
				calculateSpecifiedWidth();
				if (avaWidth < specifiedWidth && specifiedWidth > 0 && specifiedWidth < parent.getMaxAvaWidth()) {
					((IInlineStackingLayout) parent).endLine();
				}
			}
		}
	}

}
