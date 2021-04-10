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
import org.eclipse.birt.report.model.api.DesignElementHandle;

public class ReportItemPreparationBase implements IReportItemPreparation {

	protected IPreparationContext context;

	protected DesignElementHandle handle;

	public void init(IReportItemPreparationInfo info) {
		context = info.getPreparationContext();
		handle = info.getModelObject();
	}

	public void prepare() throws BirtException {
		context.triggerEvent(handle);
		prepareChildren();
	}

	protected void prepareChildren() throws BirtException {

	}
}
