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

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import jakarta.servlet.ServletOutputStream;

import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.utility.ParameterAccessor;

public class BirtExtractDataActionHandler extends AbstractBaseActionHandler {

	public BirtExtractDataActionHandler(IContext context, Operation operation, GetUpdatedObjectsResponse response) {
		super(context, operation, response);
	}

	@Override
	protected void __execute() throws Exception {
		ViewerAttributeBean attrBean = (ViewerAttributeBean) context.getBean();
		String docName = attrBean.getReportDocumentName();
		String resultSetName = ParameterAccessor.getResultSetName(context.getRequest());
		Collection columns = ParameterAccessor.getSelectedColumns(context.getRequest());
		Set filters = Collections.EMPTY_SET;
		InputOptions options = new InputOptions();
		options.setOption(InputOptions.OPT_REQUEST, context.getRequest());
		options.setOption(InputOptions.OPT_LOCALE, attrBean.getLocale());
		options.setOption(InputOptions.OPT_TIMEZONE, attrBean.getTimeZone());

		ServletOutputStream out = context.getResponse().getOutputStream();
		getReportService().extractResultSet(docName, resultSetName, columns, filters, options, out);
	}

	@Override
	protected IViewerReportService getReportService() {
		return BirtReportServiceFactory.getReportService();
	}
}
