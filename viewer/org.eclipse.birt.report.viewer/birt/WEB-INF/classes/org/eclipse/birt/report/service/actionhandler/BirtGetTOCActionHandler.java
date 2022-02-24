/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.service.actionhandler;

import java.io.File;
import java.rmi.RemoteException;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;

/**
 * Implement action handler for GetToc event.
 * <P>
 */
public class BirtGetTOCActionHandler extends AbstractGetTOCActionHandler {

	/**
	 * Constructor.
	 *
	 * @param context
	 * @param operation
	 * @param response
	 */
	public BirtGetTOCActionHandler(IContext context, Operation operation, GetUpdatedObjectsResponse response) {
		super(context, operation, response);
	}

	/**
	 * Get Report Service Object
	 *
	 * @return IViewerReportService
	 */
	@Override
	protected IViewerReportService getReportService() {
		return BirtReportServiceFactory.getReportService();
	}

	/**
	 * Implement to get document file path
	 *
	 * @return String
	 */
	@Override
	protected String __getReportDocument() {
		return __bean.getReportDocumentName();
	}

	/**
	 * Implement to check if document file exists
	 *
	 * @throws RemoteException
	 */
	@Override
	protected void __checkDocumentExists() throws RemoteException {
		File file = new File(__docName);
		if (!file.exists()) {
			// if document file doesn't exist, throw exception
			AxisFault fault = new AxisFault();
			fault.setFaultReason(BirtResources.getMessage(ResourceConstants.ACTION_EXCEPTION_DOCUMENT_FILE_NO_EXIST));
			throw fault;
		}
	}
}
