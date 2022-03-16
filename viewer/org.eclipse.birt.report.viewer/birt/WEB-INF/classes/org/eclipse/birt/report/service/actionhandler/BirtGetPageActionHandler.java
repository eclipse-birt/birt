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

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;

public class BirtGetPageActionHandler extends AbstractGetPageActionHandler {

	/**
	 * Constructor.
	 *
	 * @param context
	 * @param operation
	 */
	public BirtGetPageActionHandler(IContext context, Operation operation, GetUpdatedObjectsResponse response) {
		super(context, operation, response);
	}

	/**
	 * Returns report document name
	 */
	@Override
	protected String __getReportDocument() {
		return __bean.getReportDocumentName();
	}

	/**
	 * Check whether document existed
	 */
	@Override
	protected void __checkDocumentExists() throws Exception {
		File file = new File(__docName);
		if (!file.exists()) {
			BirtRunReportActionHandler handler = new BirtRunReportActionHandler(context, operation, response);
			handler.__execute();
		}

		file = new File(__docName);
		if (!file.exists()) {
			AxisFault fault = new AxisFault();
			fault.setFaultReason(BirtResources.getMessage(ResourceConstants.ACTION_EXCEPTION_NO_REPORT_DOCUMENT));
			throw fault;
		} else {
			// If document isn't completed, throw Exception
			BaseAttributeBean bean = (BaseAttributeBean) context.getBean();
			if (bean.isDocumentProcessing()) {
				AxisFault fault = new AxisFault();
				fault.setFaultReason(
						BirtResources.getMessage(ResourceConstants.GENERAL_EXCEPTION_DOCUMENT_FILE_PROCESSING));
				throw fault;
			}
		}
	}

	/**
	 *
	 */
	@Override
	public IViewerReportService getReportService() {
		return BirtReportServiceFactory.getReportService();
	}
}
