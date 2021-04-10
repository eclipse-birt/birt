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

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Action handler for invoke RunAndRenderTask to retrieve report content.
 * 
 */
public class BirtRunAndRenderActionHandler extends AbstractBaseActionHandler {

	/**
	 * Output stream to store the report.
	 */
	private OutputStream os = null;

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param operation
	 * @param response
	 * @param os
	 */
	public BirtRunAndRenderActionHandler(IContext context, Operation operation, GetUpdatedObjectsResponse response,
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
	public void __execute() throws Exception {
		ViewerAttributeBean attrBean = (ViewerAttributeBean) context.getBean();
		Map params = attrBean.getParameters();
		Map displayTexts = attrBean.getDisplayTexts();
		IViewerReportDesignHandle reportDesignHandle = attrBean.getReportDesignHandle(context.getRequest());
		boolean svgFlag = ParameterAccessor.getSVGFlag(context.getRequest());
		String outputDocName = attrBean.getReportDocumentName();

		InputOptions options = createInputOptions(attrBean, svgFlag);

		getReportService().runAndRenderReport(reportDesignHandle, outputDocName, options, params, os, new ArrayList(),
				displayTexts);
	}

	protected IViewerReportService getReportService() {
		return BirtReportServiceFactory.getReportService();
	}
}
