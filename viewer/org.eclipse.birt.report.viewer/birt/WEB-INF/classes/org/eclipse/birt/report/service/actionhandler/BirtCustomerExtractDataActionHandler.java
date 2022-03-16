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

import javax.servlet.ServletOutputStream;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * This action handle is used to support user extended data extraction
 *
 */
public class BirtCustomerExtractDataActionHandler extends AbstractBaseActionHandler {

	/**
	 * Default constructor
	 *
	 * @param context
	 * @param operation
	 * @param response
	 */
	public BirtCustomerExtractDataActionHandler(IContext context, Operation operation,
			GetUpdatedObjectsResponse response) {
		super(context, operation, response);
	}

	/**
	 * Execute action
	 */
	@Override
	protected void __execute() throws Exception {
		ViewerAttributeBean attrBean = (ViewerAttributeBean) context.getBean();
		String docName = attrBean.getReportDocumentName();
		InputOptions options = new InputOptions();
		options.setOption(InputOptions.OPT_REQUEST, context.getRequest());
		options.setOption(InputOptions.OPT_LOCALE, attrBean.getLocale());
		options.setOption(InputOptions.OPT_TIMEZONE, attrBean.getTimeZone());

		String extractFormat = ParameterAccessor.getExtractFormat(context.getRequest());
		String extractExtension = ParameterAccessor.getExtractExtension(context.getRequest());

		if (extractExtension != null) {
			// check extract extension
			boolean flag = ParameterAccessor.validateExtractExtension(extractExtension);
			if (!flag) {
				AxisFault fault = new AxisFault();
				fault.setFaultReason(
						BirtResources.getMessage(ResourceConstants.REPORT_SERVICE_EXCEPTION_INVALID_EXTRACTEXTENSION));
				throw fault;
			}

			extractFormat = ParameterAccessor.getExtractFormat(extractExtension);
		}

		// check extract format
		boolean flag = ParameterAccessor.validateExtractFormat(extractFormat);
		if (!flag) {
			AxisFault fault = new AxisFault();
			fault.setFaultReason(
					BirtResources.getMessage(ResourceConstants.REPORT_SERVICE_EXCEPTION_INVALID_EXTRACTFORMAT));
			throw fault;
		}

		ServletOutputStream out = context.getResponse().getOutputStream();
		getReportService().extractData(docName, options, out);
	}

	/**
	 * Returns Viewer Report Service
	 */
	@Override
	protected IViewerReportService getReportService() {
		return BirtReportServiceFactory.getReportService();
	}
}
