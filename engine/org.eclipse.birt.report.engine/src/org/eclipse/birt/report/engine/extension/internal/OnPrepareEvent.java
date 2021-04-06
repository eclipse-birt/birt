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
