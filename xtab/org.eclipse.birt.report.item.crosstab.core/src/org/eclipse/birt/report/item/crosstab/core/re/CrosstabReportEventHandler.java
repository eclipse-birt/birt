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
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.extension.IOnCreateEvent;
import org.eclipse.birt.report.engine.extension.IOnRenderEvent;
import org.eclipse.birt.report.engine.extension.IReportEventContext;
import org.eclipse.birt.report.engine.extension.ReportEventHandlerBase;
import org.eclipse.birt.report.engine.script.internal.instance.RunningState;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.script.internal.handler.CrosstabCreationHandler;
import org.eclipse.birt.report.item.crosstab.core.script.internal.handler.CrosstabRenderingHandler;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * CrosstabReportEventHandler
 */
public class CrosstabReportEventHandler extends ReportEventHandlerBase {

	@Override
	public void onCreate(IOnCreateEvent event) throws BirtException {
		DesignElementHandle modelHandle = event.getHandle();

		if (!(modelHandle instanceof ExtendedItemHandle)) {
			return;
		}

		CrosstabReportItemHandle crosstab = (CrosstabReportItemHandle) ((ExtendedItemHandle) modelHandle)
				.getReportItem();

		IReportEventContext context = event.getContext();

		CrosstabCreationHandler handler = new CrosstabCreationHandler((ExtendedItemHandle) modelHandle,
				context.getApplicationClassLoader());

		handler.handleCrosstab(crosstab, (ITableContent) event.getContent(), context, RunningState.CREATE);
	}

	@Override
	public void onRender(IOnRenderEvent event) throws BirtException {
		DesignElementHandle modelHandle = event.getHandle();

		if (!(modelHandle instanceof ExtendedItemHandle)) {
			return;
		}

		CrosstabReportItemHandle crosstab = (CrosstabReportItemHandle) ((ExtendedItemHandle) modelHandle)
				.getReportItem();

		IReportEventContext context = event.getContext();

		CrosstabRenderingHandler handler = new CrosstabRenderingHandler((ExtendedItemHandle) modelHandle,
				context.getApplicationClassLoader());

		handler.handleCrosstab(crosstab, (ITableContent) event.getContent(), context, RunningState.RENDER);
	}

}
