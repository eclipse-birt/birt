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
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Oprand;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Action handler for get reportlet content.
 * 
 */
public class BirtGetReportletActionHandler extends AbstractBaseActionHandler {

	protected BaseAttributeBean __bean;

	protected String __reportDesignName;

	protected String __docName;

	protected String __reportletId;

	/**
	 * Output stream to store the report.
	 */
	OutputStream os = null;

	/**
	 * 
	 * @param context
	 * @param operation
	 * @param response
	 */
	public BirtGetReportletActionHandler(IContext context, Operation operation, GetUpdatedObjectsResponse response,
			OutputStream os) {
		super(context, operation, response);
		this.os = os;
	}

	/**
	 * Do execution.
	 * 
	 * @exception ReportServiceException
	 * @return
	 */
	protected void __execute() throws Exception {
		prepareParameters();
		doExecution();
		prepareResponse();
	}

	protected void prepareParameters() throws Exception, RemoteException {
		__bean = context.getBean();
		__reportDesignName = __bean.getReportDesignName();
		__docName = __bean.getReportDocumentName();
		__reportletId = __bean.getReportletId();

		// note: __docName and __reportDesignName can't be null
		// at the same time (already checked by ViewerAttributeBean.__init()
		if (__docName == null) {
			if (__reportDesignName != null) {
				// generate the document name
				__docName = ParameterAccessor.getReportDocument(context.getRequest(), null, true);
				__bean.setReportDocumentName(__docName);
			}
		}

		__checkDocumentExists();
	}

	protected void doExecution() throws ReportServiceException, RemoteException {
		Oprand[] operand = null;
		if (operation != null) {
			operand = operation.getOprand();
		}
		boolean svgFlag = getSVGFlag(operand);

		InputOptions options = createInputOptions(__bean, svgFlag);

		List activeIds = new ArrayList();
		getReportService().renderReportlet(__docName, __reportletId, options, activeIds, os);
	}

	/**
	 * 
	 */
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
		}
	}

	protected void prepareResponse() throws ReportServiceException, RemoteException {
	}

	/**
	 * 
	 */
	public IViewerReportService getReportService() {
		return BirtReportServiceFactory.getReportService();
	}
}
