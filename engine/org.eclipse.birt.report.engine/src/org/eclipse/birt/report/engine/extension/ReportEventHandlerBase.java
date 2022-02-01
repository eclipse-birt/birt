/*******************************************************************************
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
