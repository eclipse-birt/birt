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

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.utility.ParameterAccessor;

abstract public class AbstractRenderImageActionHandler extends AbstractBaseActionHandler {

	public AbstractRenderImageActionHandler(IContext context, Operation operation, GetUpdatedObjectsResponse response) {
		super(context, operation, response);
	}

	@Override
	public void __execute() throws Exception {
		HttpServletRequest request = context.getRequest();
		HttpServletResponse response = context.getResponse();

		String docName = null;// TODO: Do we need document name?
		String imageId = request.getParameter(ParameterAccessor.PARAM_IMAGEID);

		response.setContentType(__getContentTypeByID(imageId));
		ServletOutputStream out = response.getOutputStream();

		InputOptions options = new InputOptions();
		options.setOption(InputOptions.OPT_REQUEST, request);

		getReportService().getImage(docName, imageId, out, options);
	}

	private String __getContentTypeByID(String imageId) {
		if (imageId.endsWith(".svg")) //$NON-NLS-1$
		{
			return "image/svg+xml"; //$NON-NLS-1$
		} else if (imageId.endsWith(".ico")) //$NON-NLS-1$
		{
			return "image/x-icon"; //$NON-NLS-1$
		}
		return "image"; //$NON-NLS-1$
	}
}
