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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.eclipse.birt.report.utility.BirtUtility;

public class BirtRunReportActionHandler extends AbstractBaseActionHandler {

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param operation
	 */
	public BirtRunReportActionHandler(IContext context, Operation operation, GetUpdatedObjectsResponse response) {
		super(context, operation, response);
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

		Map parameterMap = attrBean.getParameters();
		if (parameterMap == null)
			parameterMap = new HashMap();

		Map displayTexts = attrBean.getDisplayTexts();
		if (displayTexts == null)
			displayTexts = new HashMap();

		String docName = attrBean.getReportDocumentName();
		IViewerReportDesignHandle designHandle = attrBean.getReportDesignHandle(context.getRequest());

		InputOptions options = new InputOptions();
		options.setOption(InputOptions.OPT_REQUEST, context.getRequest());
		options.setOption(InputOptions.OPT_LOCALE, attrBean.getLocale());
		options.setOption(InputOptions.OPT_TIMEZONE, attrBean.getTimeZone());
		options.setOption(InputOptions.OPT_IS_DESIGNER, Boolean.valueOf(attrBean.isDesigner()));

		// handle operation
		BirtUtility.handleOperation(operation, attrBean, parameterMap, displayTexts);

		List<Exception> errorList = new ArrayList<Exception>();
		getReportService().runReport(designHandle, docName, options, parameterMap, displayTexts, errorList);
		if (errorList != null && !errorList.isEmpty()) {
			// clear document file
			File doc = new File(docName);
			if (doc != null)
				doc.delete();

			throw BirtUtility.makeAxisFault("BirtRunReportActionHandler.__execute()", errorList); //$NON-NLS-1$
		}
	}

	protected IViewerReportService getReportService() {
		return BirtReportServiceFactory.getReportService();
	}
}
