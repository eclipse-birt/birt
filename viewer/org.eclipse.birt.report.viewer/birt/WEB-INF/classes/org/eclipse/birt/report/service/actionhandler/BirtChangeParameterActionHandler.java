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

import java.io.ByteArrayOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.OutputOptions;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.soapengine.api.Data;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Page;
import org.eclipse.birt.report.soapengine.api.Update;
import org.eclipse.birt.report.soapengine.api.UpdateContent;
import org.eclipse.birt.report.soapengine.api.UpdateData;
import org.eclipse.birt.report.utility.DataUtil;
import org.eclipse.birt.report.utility.ParameterAccessor;

public class BirtChangeParameterActionHandler extends AbstractChangeParameterActionHandler {

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param operation
	 */
	public BirtChangeParameterActionHandler(IContext context, Operation operation, GetUpdatedObjectsResponse response) {
		super(context, operation, response);
	}

	protected void runReport() throws Exception {
		BirtRunReportActionHandler handler = new BirtRunReportActionHandler(context, operation, response);
		handler.__execute();
	}

	protected void doRenderPage(InputOptions options, String docName, long pageNumber, boolean useBookmark,
			String bookmark) throws ReportServiceException, RemoteException {
		// get attribute bean
		ViewerAttributeBean attrBean = (ViewerAttributeBean) context.getBean();
		assert attrBean != null;
		ArrayList activeIds = new ArrayList();

		ByteArrayOutputStream page = null;
		if (ParameterAccessor.isGetReportlet(context.getRequest())) {

			// render reportlet
			String __reportletId = attrBean.getReportletId();
			page = getReportService().getReportlet(docName, __reportletId, options, activeIds);
		} else {
			page = getReportService().getPage(docName, pageNumber + "", options, activeIds); //$NON-NLS-1$
		}

		// Update instruction for document.
		UpdateContent content = new UpdateContent();
		content.setContent(DataUtil.toUTF8(page.toByteArray()));
		content.setTarget(operation.getTarget().getId());
		content.setInitializationId(parseReportId(activeIds));
		if (useBookmark) {
			content.setBookmark(bookmark);
		}

		Update updateDocument = new Update();
		updateDocument.setUpdateContent(content);

		// Update instruction for nav bar.
		UpdateData updateData = new UpdateData();
		updateData.setTarget("navigationBar"); //$NON-NLS-1$
		Page pageObj = new Page();
		pageObj.setPageNumber(String.valueOf(pageNumber));
		pageObj.setTotalPage(String.valueOf(getReportService().getPageCount(docName, options, new OutputOptions())));
		pageObj.setRtl(attrBean.isReportRtl());
		Data pageData = new Data();
		pageData.setPage(pageObj);
		updateData.setData(pageData);
		Update updateNavbar = new Update();
		updateNavbar.setUpdateData(updateData);

		UpdateData updateDocumentData = new UpdateData();
		updateDocumentData.setTarget("birtReportDocument");
		updateDocumentData.setData(pageData);
		updateDocument.setUpdateData(updateDocumentData);

		response.setUpdate(new Update[] { updateDocument, updateNavbar });
	}

	protected IViewerReportService getReportService() {
		return BirtReportServiceFactory.getReportService();
	}
}