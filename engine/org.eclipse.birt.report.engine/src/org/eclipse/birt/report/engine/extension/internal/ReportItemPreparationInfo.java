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

	@Override
	public DesignElementHandle getModelObject() {
		return handle;
	}

	@Override
	public IPreparationContext getPreparationContext() {
		return context;
	}
}
