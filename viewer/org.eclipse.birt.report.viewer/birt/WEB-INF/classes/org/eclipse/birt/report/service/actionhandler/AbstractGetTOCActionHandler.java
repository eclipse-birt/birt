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

import java.rmi.RemoteException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.service.api.ToC;
import org.eclipse.birt.report.soapengine.api.Data;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Oprand;
import org.eclipse.birt.report.soapengine.api.TOC;
import org.eclipse.birt.report.soapengine.api.Update;
import org.eclipse.birt.report.soapengine.api.UpdateData;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * Abstract action handler for GetToc event.
 * <P>
 * In current implementation, toc entries are found from a document object.
 * <p>
 */
public abstract class AbstractGetTOCActionHandler extends AbstractBaseActionHandler {

	/*
	 * Attribute Bean from request scope
	 */
	protected BaseAttributeBean __bean;

	/*
	 * Existed document file
	 */
	protected String __docName;

	/*
	 * TOC tree
	 */
	protected ToC __node = null;

	/**
	 * Get document file path
	 *
	 * @return String
	 */
	abstract protected String __getReportDocument();

	/**
	 * Check if document file exists.
	 *
	 * @throws Exception
	 */
	abstract protected void __checkDocumentExists() throws Exception;

	/**
	 * Constructor.
	 *
	 * @param context
	 * @param operation
	 * @param response
	 */
	public AbstractGetTOCActionHandler(IContext context, Operation operation, GetUpdatedObjectsResponse response) {
		super(context, operation, response);
	}

	/**
	 * Execute action handler.
	 *
	 * @exception RemoteException
	 * @return
	 */
	@Override
	protected void __execute() throws Exception {
		prepareParameters();
		doExecution();
		prepareResponse();
	}

	/**
	 * Prepare required parameters
	 *
	 * @throws ReportServiceException
	 * @throws RemoteException
	 */
	protected void prepareParameters() throws Exception {
		__bean = context.getBean();
		__docName = __getReportDocument();
		__checkDocumentExists();
	}

	/**
	 * Process action hander execution
	 *
	 * @throws ReportServiceException
	 * @throws RemoteException
	 */
	protected void doExecution() throws ReportServiceException, RemoteException {
		Oprand[] oprands = operation.getOprand();
		InputOptions options = new InputOptions();
		HttpServletRequest request = context.getRequest();
		options.setOption(InputOptions.OPT_REQUEST, request);
		BaseAttributeBean bean = (BaseAttributeBean) request.getAttribute(IBirtConstants.ATTRIBUTE_BEAN);
		if (bean != null) {
			options.setOption(InputOptions.OPT_LOCALE, bean.getLocale());
			options.setOption(InputOptions.OPT_TIMEZONE, bean.getTimeZone());
		}

		if (oprands != null && oprands.length > 0) {
			__node = getReportService().getTOC(__docName, oprands[0].getValue(), options);
		} else {
			__node = getReportService().getTOC(__docName, null, options);
		}
	}

	/**
	 * Prepare response
	 *
	 * @throws ReportServiceException
	 * @throws RemoteException
	 */
	protected void prepareResponse() throws ReportServiceException, RemoteException {
		TOC toc = new TOC();
		List children = __node.getChildren();
		if (children != null && children.size() > 0) {
			TOC[] childTOCNodes = new TOC[children.size()];
			for (int i = 0; i < children.size(); i++) {
				ToC child = (ToC) children.get(i);
				childTOCNodes[i] = new TOC();
				childTOCNodes[i].setId(child.getID());
				childTOCNodes[i].setDisplayName(ParameterAccessor.htmlEncode(child.getDisplayName()));
				childTOCNodes[i].setBookmark(child.getBookmark());
				childTOCNodes[i].setStyle(child.getStyle());
				childTOCNodes[i]
						.setIsLeaf(child.getChildren() == null || child.getChildren().size() <= 0);
			}
			toc.setChild(childTOCNodes);
		}

		Data data = new Data();
		data.setTOC(toc);
		UpdateData updateData = new UpdateData();
		updateData.setTarget("birtToc"); //$NON-NLS-1$
		updateData.setData(data);
		Update update = new Update();
		update.setUpdateData(updateData);
		response.setUpdate(new Update[] { update });
	}
}
