/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.extension;

import org.eclipse.birt.core.exception.BirtException;

/*
 * A sample implementation of IReportEventHandler. Customer could over-write onPrepare, onCreate,
 * onRender to process java scripts for extended items
 * 
 */
public class ReportEventHandlerBase implements IReportEventHandler {

	public void handle(IReportEvent event) throws BirtException {
		if (event == null)
			return;

		int eventType = event.getEventType();
		switch (eventType) {
		case IReportEvent.ON_PREPARE_EVENT:
			onPrepare((IOnPrepareEvent) event);
			break;
		case IReportEvent.ON_CREATE_EVENT:
			onCreate((IOnCreateEvent) event);
			break;
		case IReportEvent.ON_RENDER_EVENT:
			onRender((IOnRenderEvent) event);
			break;
		case IReportEvent.ON_PAGEBREAK_EVENT:
			onPagebreak(event);
			break;
		default:

		}
	}

	public void onPrepare(IOnPrepareEvent event) throws BirtException {

	}

	public void onCreate(IOnCreateEvent event) throws BirtException {

	}

	public void onRender(IOnRenderEvent event) throws BirtException {

	}

	public void onPagebreak(IReportEvent event) throws BirtException {

	}
}
