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

package org.eclipse.birt.report.presentation.aggregation.layout;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis.AxisFault;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.context.BirtContext;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.presentation.aggregation.BirtBaseFragment;
import org.eclipse.birt.report.presentation.aggregation.control.ToolbarFragment;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.ReportEngineService;
import org.eclipse.birt.report.service.actionhandler.BirtGetReportletActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtRenderReportActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtRunReportActionHandler;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.utility.BirtUtility;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Root fragment for web viewer composite.
 * <p>
 *
 * @see BaseFragment
 */
public class FramesetFragment extends BirtBaseFragment {

	/**
	 * Override build method.
	 */
	@Override
	protected void build() {
		addChild(new ToolbarFragment());
		addChild(new ReportFragment());
	}

	/**
	 * Service provided by the fragment. This is the entry point of engine framgent.
	 * It generally includes a JSP page to render a certain part of web viewer.
	 *
	 * @param request  incoming http request
	 * @param response http response
	 * @exception ServletException
	 * @exception IOException
	 */
	@Override
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, BirtException {
		BaseAttributeBean attrBean = (BaseAttributeBean) request.getAttribute(IBirtConstants.ATTRIBUTE_BEAN);
		if (attrBean != null && !attrBean.isShowParameterPage() && !this.__checkHTMLFormat(request)) {
			this.doPreService(request, response);
			this.doService(request, response);
			this.doPostService(request, response);
		} else {
			super.doPreService(request, response);
			super.doService(request, response);
			String target = super.doPostService(request, response);

			if (target != null && target.length() > 0) {
				RequestDispatcher rd = request.getRequestDispatcher(target);
				rd.include(request, response);
			}
		}
	}

	/**
	 * Check if use html format
	 *
	 * @param request
	 * @return
	 */
	protected boolean __checkHTMLFormat(HttpServletRequest request) {
		BaseAttributeBean bean = (BaseAttributeBean) request.getAttribute(IBirtConstants.ATTRIBUTE_BEAN);
		assert bean != null;

		return ParameterAccessor.PARAM_FORMAT_HTML.equalsIgnoreCase(bean.getFormat());
	}

	/**
	 * Anything before do service.
	 *
	 * @param request  incoming http request
	 * @param response http response
	 * @exception ServletException
	 * @exception IOException
	 */
	@Override
	protected void doPreService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BaseAttributeBean attrBean = (BaseAttributeBean) request.getAttribute(IBirtConstants.ATTRIBUTE_BEAN);
		String format = attrBean.getFormat();
		String emitterId = attrBean.getEmitterId();
		String openType = ParameterAccessor.getOpenType(request);
		if (ParameterAccessor.PARAM_FORMAT_PDF.equalsIgnoreCase(format)) {
			response.setContentType("application/pdf"); //$NON-NLS-1$
		} else {
			String mimeType = ParameterAccessor.getEmitterMimeType(emitterId);
			if (mimeType == null) {
				mimeType = ReportEngineService.getInstance().getMIMEType(format);
			}
			if (mimeType != null && mimeType.length() > 0) {
				response.setContentType(mimeType);
			} else {
				response.setContentType("application/octet-stream"); //$NON-NLS-1$
			}
		}

		String filename = ParameterAccessor.getExportFilename(new BirtContext(request, response), format, emitterId);
		response.setHeader("Content-Disposition", //$NON-NLS-1$
				ParameterAccessor.htmlHeaderValueEncode(openType) + "; filename=\"" //$NON-NLS-1$
						+ ParameterAccessor.htmlHeaderValueEncode(filename) + "\""); //$NON-NLS-1$
	}

	/**
	 * Render the report in html/pdf format by calling frameset service.
	 *
	 * @param request  incoming http request
	 * @param response http response
	 * @exception ServletException
	 * @exception IOException
	 */
	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, BirtException {
		BaseAttributeBean attrBean = (BaseAttributeBean) request.getAttribute(IBirtConstants.ATTRIBUTE_BEAN);
		assert attrBean != null;

		OutputStream out = response.getOutputStream();
		IContext context = new BirtContext(request, response);
		GetUpdatedObjectsResponse upResponse = new GetUpdatedObjectsResponse();
		Operation op = null;
		try {
			File file = new File(attrBean.getReportDocumentName());
			if (!file.exists()) {
				BirtRunReportActionHandler runReport = new BirtRunReportActionHandler(context, op, upResponse);
				runReport.execute();
			}

			// If document isn't completed, throw Exception
			if (attrBean.isDocumentProcessing()) {
				AxisFault fault = new AxisFault();
				fault.setFaultReason(
						BirtResources.getMessage(ResourceConstants.GENERAL_EXCEPTION_DOCUMENT_FILE_PROCESSING));
				throw fault;
			}

			// Print report on server
			boolean isPrint = false;
			if (IBirtConstants.ACTION_PRINT.equalsIgnoreCase(attrBean.getAction())) {
				isPrint = true;
				out = new ByteArrayOutputStream();
			}

			if (ParameterAccessor.isGetReportlet(request)) {
				// render reportlet
				BirtGetReportletActionHandler renderReportlet = new BirtGetReportletActionHandler(context, op,
						upResponse, out);
				renderReportlet.execute();
			} else {
				BirtRenderReportActionHandler renderReport = new BirtRenderReportActionHandler(context, op, upResponse,
						out);
				renderReport.execute();
			}

			if (isPrint) {
				InputStream inputStream = new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
				BirtUtility.doPrintAction(inputStream, request, response);
			}
		} catch (RemoteException e) {
			response.setContentType("text/html; charset=utf-8"); //$NON-NLS-1$
			BirtUtility.appendErrorMessage(response.getOutputStream(), e);
		}

	}

	/**
	 * Override implementation of doPostService.
	 */
	@Override
	protected String doPostService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		return null;
	}
}
