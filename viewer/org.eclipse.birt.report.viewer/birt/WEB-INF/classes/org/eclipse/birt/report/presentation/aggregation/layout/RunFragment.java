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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.context.BirtContext;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.service.actionhandler.BirtRenderImageActionHandler;
import org.eclipse.birt.report.service.actionhandler.BirtRunAndRenderActionHandler;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.utility.BirtUtility;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Root fragment for web viewer 'run' pattern.
 * <p>
 *
 * @see BaseFragment
 */
public class RunFragment extends FramesetFragment {

	/**
	 * Override build method.
	 */
	@Override
	protected void build() {
		addChild(new ReportDialogFragment());
		addChild(new DocumentFragment());
	}

	/**
	 * Check if use html format
	 *
	 * @param request
	 * @return
	 */
	@Override
	protected boolean __checkHTMLFormat(HttpServletRequest request) {
		BaseAttributeBean bean = (BaseAttributeBean) request.getAttribute(IBirtConstants.ATTRIBUTE_BEAN);
		assert bean != null;

		// if don't set report or document parameter, return false
		if (!ParameterAccessor.isReportParameterExist(request, ParameterAccessor.PARAM_REPORT)
				&& !ParameterAccessor.isReportParameterExist(request, ParameterAccessor.PARAM_REPORT_DOCUMENT)) {
			return false;
		}

		return super.__checkHTMLFormat(request);
	}

	/**
	 * Handle image operate.
	 *
	 * @param request  incoming http request
	 * @param response http response
	 * @exception ServletException
	 * @exception IOException
	 */
	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BaseAttributeBean attrBean = (BaseAttributeBean) request.getAttribute(IBirtConstants.ATTRIBUTE_BEAN);

		OutputStream out = response.getOutputStream();
		GetUpdatedObjectsResponse upResponse = new GetUpdatedObjectsResponse();
		IContext context = new BirtContext(request, response);
		Operation op = null;
		try {
			// if render image
			if (ParameterAccessor.isGetImageOperator(request)) {
				BirtRenderImageActionHandler renderImageHandler = new BirtRenderImageActionHandler(context, op,
						upResponse);
				renderImageHandler.execute();
			} else {
				// Print report on server
				boolean isPrint = false;
				if (IBirtConstants.ACTION_PRINT.equalsIgnoreCase(attrBean.getAction())) {
					isPrint = true;
					out = new ByteArrayOutputStream();
				}

				BirtRunAndRenderActionHandler runAndRenderHandler = new BirtRunAndRenderActionHandler(context, op,
						upResponse, out);
				runAndRenderHandler.execute();

				if (isPrint) {
					InputStream inputStream = new ByteArrayInputStream(((ByteArrayOutputStream) out).toByteArray());
					BirtUtility.doPrintAction(inputStream, request, response);
				}
			}
		} catch (RemoteException e) {
			// if get image, don't write exception into output stream.
			if (!ParameterAccessor.isGetImageOperator(request)) {
				response.setContentType("text/html; charset=utf-8"); //$NON-NLS-1$
				BirtUtility.appendErrorMessage(response.getOutputStream(), e);
			}
		}
	}
}
