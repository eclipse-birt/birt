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

import org.eclipse.birt.report.engine.extension.IPreparationContext;
import org.eclipse.birt.report.engine.extension.IReportItemPreparationInfo;
import org.eclipse.birt.report.model.api.DesignElementHandle;

public class ReportItemPreparationInfo implements IReportItemPreparationInfo {

	DesignElementHandle handle;

	IPreparationContext context;

	public ReportItemPreparationInfo(DesignElementHandle handle, IPreparationContext context) {
		this.handle = handle;
		this.context = context;
	}

	public DesignElementHandle getModelObject() {
		return handle;
	}

	public IPreparationContext getPreparationContext() {
		return context;
	}
}
