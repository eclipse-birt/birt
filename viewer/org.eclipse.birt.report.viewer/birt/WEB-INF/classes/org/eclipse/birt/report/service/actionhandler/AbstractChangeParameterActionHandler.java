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

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.BaseAttributeBean;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.OutputOptions;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Oprand;

/**
 * The abstract action handler to handle "ChangeParameter" action.
 */
public abstract class AbstractChangeParameterActionHandler extends AbstractBaseActionHandler {

	public AbstractChangeParameterActionHandler(IContext context, Operation operation,
			GetUpdatedObjectsResponse response) {
		super(context, operation, response);
	}

	protected void __execute() throws Exception {
		BaseAttributeBean attrBean = (BaseAttributeBean) context.getBean();
		boolean svgFlag = getSVGFlag(operation.getOprand());

		// First generate report document.
		runReport();

		String bookmark = null;
		boolean useBookmark = false;

		String docName = attrBean.getReportDocumentName();

		long pageNumber = getPageNumber(context.getRequest(), operation.getOprand(), docName);

		if (!isValidPageNumber(context.getRequest(), pageNumber, docName)) {
			bookmark = getBookmark(operation.getOprand(), attrBean);
			if (bookmark != null && bookmark.length() > 0) {
				InputOptions options = new InputOptions();
				options.setOption(InputOptions.OPT_REQUEST, context.getRequest());
				options.setOption(InputOptions.OPT_LOCALE, attrBean.getLocale());
				options.setOption(InputOptions.OPT_TIMEZONE, attrBean.getTimeZone());

				// Bookmark is a TOC name, then find TOC id by name
				if (isToc(operation.getOprand(), attrBean)) {
					bookmark = (getReportService()).findTocByName(docName, bookmark, options);
				}

				pageNumber = getReportService().getPageNumberByBookmark(docName, bookmark, options);

				if (!isValidPageNumber(context.getRequest(), pageNumber, docName)) {
					AxisFault fault = new AxisFault();
					fault.setFaultReason(BirtResources.getMessage(ResourceConstants.ACTION_EXCEPTION_INVALID_BOOKMARK,
							new String[] { getBookmark(operation.getOprand(), attrBean) }));
					throw fault;
				}
				useBookmark = true;
			}
		}

		InputOptions options = createInputOptions(attrBean, svgFlag);
		doRenderPage(options, docName, pageNumber, useBookmark, bookmark);
	}

	protected abstract void runReport() throws Exception;

	protected abstract void doRenderPage(InputOptions options, String docName, long pageNumber, boolean useBookmark,
			String bookmark) throws ReportServiceException, RemoteException;

	/**
	 * Check whether the page number is valid or not.
	 * 
	 * @param pageNumber
	 * @param document
	 * @return
	 * @throws RemoteException
	 * @throws ReportServiceException
	 */
	protected boolean isValidPageNumber(HttpServletRequest request, long pageNumber, String documentName)
			throws RemoteException, ReportServiceException {
		InputOptions options = new InputOptions();
		options.setOption(InputOptions.OPT_REQUEST, request);
		return pageNumber > 0
				&& pageNumber <= getReportService().getPageCount(documentName, options, new OutputOptions());
	}

	/**
	 * Get page number from incoming soap request.
	 * 
	 * @param params
	 * @param document
	 * @return
	 * @throws RemoteException
	 * @throws ReportServiceException
	 */
	protected long getPageNumber(HttpServletRequest request, Oprand[] params, String documentName)
			throws RemoteException, ReportServiceException {
		long pageNumber = -1;
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				if (IBirtConstants.OPRAND_PAGENO.equalsIgnoreCase(params[i].getName())) {
					try {
						pageNumber = Integer.parseInt(params[i].getValue());
					} catch (NumberFormatException e) {
						AxisFault fault = new AxisFault();
						fault.setFaultCode(new QName("DocumentProcessor.getPageNumber( )")); //$NON-NLS-1$
						fault.setFaultString(
								BirtResources.getMessage(ResourceConstants.ACTION_EXCEPTION_PAGE_NUMBER_PARSE_ERROR,
										new Object[] { params[i].getValue() }));
						throw fault;
					}
					InputOptions options = new InputOptions();
					options.setOption(InputOptions.OPT_REQUEST, request);
					long totalPageNumber = getReportService().getPageCount(documentName, options, new OutputOptions());
					if (pageNumber <= 0 || pageNumber > totalPageNumber) {
						AxisFault fault = new AxisFault();
						fault.setFaultCode(new QName("DocumentProcessor.getPageNumber( )")); //$NON-NLS-1$
						fault.setFaultString(
								BirtResources.getMessage(ResourceConstants.ACTION_EXCEPTION_INVALID_PAGE_NUMBER,
										new Object[] { Long.valueOf(pageNumber), Long.valueOf(totalPageNumber) }));
						throw fault;
					}

					break;
				}
			}
		}

		return pageNumber;
	}
}
