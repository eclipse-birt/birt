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
import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IGroupContent;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.TableArea;
import org.eclipse.birt.report.engine.layout.pdf.emitter.TableLayout.TableContext;

public class TableGroupLayout extends RepeatableLayout {
	protected TableLayout tableLM = null;

	public TableGroupLayout(LayoutEngineContext context, ContainerLayout parent, IContent content) {
		super(context, parent, content);
		tableLM = getTableLayoutManager();
		bandStatus = IBandContent.BAND_GROUP_HEADER;
	}

	protected void repeatHeader() throws BirtException {
		if (bandStatus == IBandContent.BAND_GROUP_HEADER) {
			return;
		}
		if (!((IGroupContent) content).isHeaderRepeat()) {
			return;
		}
		IBandContent header = context.getWrappedGroupHeader(content.getInstanceID());
		if (header == null || header.getChildren().isEmpty()) {
			return;
		}
		TableRegionLayout rLayout = tableLM.getTableRegionLayout();
		rLayout.initialize(header);
		rLayout.layout();
		TableArea tableRegion = (TableArea) header.getExtension(IContent.LAYOUT_EXTENSION);
		if (tableRegion != null && tableRegion.getAllocatedHeight() < getCurrentMaxContentHeight()) {
			TableContext tableContext = (TableContext) tableLM.contextList.getLast();
			tableContext.layout.addRows(rLayout.getTableAreaLayout().getRows());

			// add to root
			Iterator iter = tableRegion.getChildren();
			while (iter.hasNext()) {
				AbstractArea area = (AbstractArea) iter.next();
				addArea(area);
			}
		}
		content.setExtension(IContent.LAYOUT_EXTENSION, null);
	}

	public boolean addArea(AbstractArea area) {
		return addArea(area, false);
	}

}
