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

package org.eclipse.birt.report.engine.extension.internal;

import org.eclipse.birt.report.engine.extension.IOnPrepareEvent;
import org.eclipse.birt.report.engine.extension.IReportEventContext;
import org.eclipse.birt.report.model.api.DesignElementHandle;

public class OnPrepareEvent extends ReportEvent implements IOnPrepareEvent {

	DesignElementHandle handle;
	IReportEventContext context;

	public OnPrepareEvent(IReportEventContext context, DesignElementHandle handle) {
		super(ON_PREPARE_EVENT);

		this.context = context;
		this.handle = handle;
	}

	public DesignElementHandle getHandle() {
		return handle;
	}

	public void setHandle(DesignElementHandle handle) {
		this.handle = handle;
	}

	public IReportEventContext getContext() {
		return context;
	}

	public void setContext(IReportEventContext context) {
		this.context = context;
	}
}
