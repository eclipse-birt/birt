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

import java.io.OutputStream;

import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.utility.ParameterAccessor;

public class BirtRenderReportActionHandler extends AbstractBaseActionHandler {

	/**
	 * Output stream to store the report.
	 */
	private OutputStream os = null;

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param operation
	 */
	public BirtRenderReportActionHandler(IContext context, Operation operation, GetUpdatedObjectsResponse response,
			OutputStream os) {
		super(context, operation, response);
		assert os != null;
		this.os = os;
	}

	/**
	 * Local execution.
	 * 
	 * @exception ReportServiceException
	 * @return
	 */
	public void __execute() throws Exception {
		ViewerAttributeBean attrBean = (ViewerAttributeBean) context.getBean();
		assert attrBean != null;

		String docName = attrBean.getReportDocumentName();

		InputOptions options = createInputOptions(attrBean, ParameterAccessor.getSVGFlag(context.getRequest()));

		getReportService().renderReport(docName, attrBean.getReportPage(), attrBean.getReportPageRange(), options, os);
	}

	protected IViewerReportService getReportService() {
		return BirtReportServiceFactory.getReportService();
	}

}
