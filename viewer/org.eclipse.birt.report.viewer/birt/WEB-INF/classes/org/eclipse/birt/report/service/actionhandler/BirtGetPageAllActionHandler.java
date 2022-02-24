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

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.service.api.IViewerReportDesignHandle;
import org.eclipse.birt.report.service.api.IViewerReportService;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.soapengine.api.Data;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Page;
import org.eclipse.birt.report.soapengine.api.Update;
import org.eclipse.birt.report.soapengine.api.UpdateContent;
import org.eclipse.birt.report.soapengine.api.UpdateData;
import org.eclipse.birt.report.utility.BirtUtility;
import org.eclipse.birt.report.utility.DataUtil;
import org.eclipse.birt.report.utility.ParameterAccessor;

public class BirtGetPageAllActionHandler extends AbstractBaseActionHandler {

	/**
	 * Constructor.
	 * 
	 * @param context
	 * @param operation
	 */
	public BirtGetPageAllActionHandler(IContext context, Operation operation, GetUpdatedObjectsResponse response) {
		super(context, operation, response);
	}

	/**
	 * Get report service
	 */
	public IViewerReportService getReportService() {
		return BirtReportServiceFactory.getReportService();
	}

	/**
	 * implement __execute method
	 */
	protected void __execute() throws Exception {
		// get attribute bean
		ViewerAttributeBean attrBean = (ViewerAttributeBean) context.getBean();
		assert attrBean != null;

		boolean svgFlag = getSVGFlag(operation.getOprand());
		String docName = attrBean.getReportDocumentName();

		// get bookmark
		String bookmark = getBookmark(operation.getOprand(), context.getBean());

		// input options
		InputOptions options = createInputOptions(attrBean, svgFlag);

		// output as byte array
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		if (ParameterAccessor.isGetImageOperator(context.getRequest())) {
			// render image
			BirtRenderImageActionHandler renderImageHandler = new BirtRenderImageActionHandler(context, operation,
					response);
			renderImageHandler.__execute();
		} else if (ParameterAccessor.isGetReportlet(context.getRequest())) {
			// render reportlet
			BirtGetReportletActionHandler getReportletHandler = new BirtGetReportletActionHandler(context, operation,
					response, out);
			getReportletHandler.execute();
		} else if (context.getBean().isDocumentInUrl()) {
			// Bookmark is a TOC name, then find TOC id by name
			if (isToc(operation.getOprand(), attrBean)) {
				bookmark = getReportService().findTocByName(docName, bookmark, options);
			}

			// render document file
			getReportService().renderReport(docName, attrBean.getReportPage(), attrBean.getReportPageRange(), options,
					out);
		} else {
			// run and render report design
			IViewerReportDesignHandle reportDesignHandle = attrBean.getReportDesignHandle(context.getRequest());

			Map parameterMap = attrBean.getParameters();
			if (parameterMap == null)
				parameterMap = new HashMap();

			Map displayTexts = attrBean.getDisplayTexts();
			if (displayTexts == null)
				displayTexts = new HashMap();

			// handle operation
			BirtUtility.handleOperation(operation, attrBean, parameterMap, displayTexts);

			getReportService().runAndRenderReport(reportDesignHandle, docName, options, parameterMap, out,
					new ArrayList(), displayTexts);
		}

		Page pageObj = new Page();
		pageObj.setPageNumber("1"); //$NON-NLS-1$
		pageObj.setTotalPage("1"); //$NON-NLS-1$
		pageObj.setRtl(attrBean.isReportRtl());
		Data pageData = new Data();
		pageData.setPage(pageObj);

		// Update response.
		UpdateContent content = new UpdateContent();
		content.setContent(DataUtil.toUTF8(out.toByteArray()));
		content.setTarget("Document"); //$NON-NLS-1$
		if (bookmark != null)
			content.setBookmark(bookmark);

		UpdateData updateDocumentData = new UpdateData();
		updateDocumentData.setTarget("birtReportDocument"); //$NON-NLS-1$
		updateDocumentData.setData(pageData);

		Update updateDocument = new Update();
		updateDocument.setUpdateContent(content);
		updateDocument.setUpdateData(updateDocumentData);

		response.setUpdate(new Update[] { updateDocument });
	}
}
