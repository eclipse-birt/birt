/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.re;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.extension.IOnCreateEvent;
import org.eclipse.birt.report.engine.extension.IOnRenderEvent;
import org.eclipse.birt.report.engine.extension.ReportEventHandlerBase;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.script.internal.handler.CrosstabCreationHandler;
import org.eclipse.birt.report.item.crosstab.core.script.internal.handler.CrosstabHandlerCache;
import org.eclipse.birt.report.item.crosstab.core.script.internal.handler.CrosstabRenderingHandler;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * CrosstabCellReportEventHandler
 */
public class CrosstabCellReportEventHandler extends ReportEventHandlerBase {

	private CrosstabHandlerCache handlerCache;

	CrosstabCellReportEventHandler(CrosstabHandlerCache handlerCache) {
		this.handlerCache = handlerCache;
	}

	@Override
	public void onCreate(IOnCreateEvent event) throws BirtException {
		DesignElementHandle modelHandle = event.getHandle();

		if (!(modelHandle instanceof ExtendedItemHandle)) {
			return;
		}

		String script = handlerCache.getOnCreateScript(modelHandle);

		if (script == null || script.length() == 0) {
			return;
		}

		CrosstabCreationHandler handler = handlerCache.getCreateHandler(modelHandle,
				event.getContext().getApplicationClassLoader());

		handler.handleCell((CrosstabCellHandle) ((ExtendedItemHandle) modelHandle).getReportItem(),
				(ICellContent) event.getContent(), event.getContext());
	}

	@Override
	public void onRender(IOnRenderEvent event) throws BirtException {
		DesignElementHandle modelHandle = event.getHandle();

		if (!(modelHandle instanceof ExtendedItemHandle)) {
			return;
		}

		String script = handlerCache.getOnRenderScript(modelHandle);

		if (script == null || script.length() == 0) {
			return;
		}

		CrosstabRenderingHandler handler = handlerCache.getRenderHandler(modelHandle,
				event.getContext().getApplicationClassLoader());

		handler.handleCell((CrosstabCellHandle) ((ExtendedItemHandle) modelHandle).getReportItem(),
				(ICellContent) event.getContent(), event.getContext());
	}

}