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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IListGroupContent;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;

public class ListGroupLayout extends RepeatableLayout {

	public ListGroupLayout(LayoutEngineContext context, ContainerLayout parent, IContent content) {
		super(context, parent, content);
		bandStatus = IBandContent.BAND_GROUP_HEADER;
	}

	protected void repeatHeader() throws BirtException {
		if (bandStatus == IBandContent.BAND_GROUP_HEADER) {
			return;
		}
		IListGroupContent listGroupContent = (IListGroupContent) content;
		if (!listGroupContent.isHeaderRepeat()) {
			return;
		}
		IBandContent band = listGroupContent.getHeader();
		if (band == null || band.getChildren().isEmpty()) {
			return;
		}
		ContainerArea headerArea = (ContainerArea) AreaFactory.createLogicContainer(content.getReportContent());
		headerArea.setAllocatedWidth(parent.getCurrentMaxContentWidth());
		Layout regionLayout = new RegionLayout(context, band, headerArea);
		regionLayout.layout();

		if (headerArea.getAllocatedHeight() < getCurrentMaxContentHeight())// FIXME
																			// need
																			// check
		{
			addArea(headerArea);
			repeatCount++;
		}
	}

}
