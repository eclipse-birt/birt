/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.service.actionhandler;

import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;

public class BirtRenderImageActionHandler extends AbstractRenderImageActionHandler {
	public BirtRenderImageActionHandler(IContext context, Operation operation, GetUpdatedObjectsResponse response) {
		super(context, operation, response);
	}

	protected IViewerReportService getReportService() {
		return BirtReportServiceFactory.getReportService();
	}
}
