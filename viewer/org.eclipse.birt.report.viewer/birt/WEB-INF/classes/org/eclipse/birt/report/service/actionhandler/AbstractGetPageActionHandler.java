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

import javax.servlet.http.HttpServletRequest;
import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.service.api.InputOptions;
import org.eclipse.birt.report.service.api.OutputOptions;
import org.eclipse.birt.report.service.api.ReportServiceException;
import org.eclipse.birt.report.soapengine.api.Data;
import org.eclipse.birt.report.soapengine.api.GetUpdatedObjectsResponse;
import org.eclipse.birt.report.soapengine.api.Operation;
import org.eclipse.birt.report.soapengine.api.Oprand;
import org.eclipse.birt.report.soapengine.api.Page;
import org.eclipse.birt.report.soapengine.api.Update;
import org.eclipse.birt.report.soapengine.api.UpdateContent;
import org.eclipse.birt.report.soapengine.api.UpdateData;
import org.eclipse.birt.report.utility.DataUtil;
import org.eclipse.birt.report.utility.ParameterAccessor;

/**
 * The abstract action handler to handle "GetPage" action.
 */
public abstract class AbstractGetPageActionHandler extends AbstractBaseActionHandler {

	protected ViewerAttributeBean __bean;

	protected String __docName;

	protected long __pageNumber;

	protected long __totalPageNumber;

	protected boolean __isCompleted = true;

	protected boolean __useBookmark = false;

	protected String __bookmark;

	protected boolean __svgFlag;

	protected ByteArrayOutputStream __page = null;

	protected ArrayList __activeIds = null;

	/**
	 * Returns report document file name.
	 * 
	 * @param bean
	 * @return
	 */
	abstract protected String __getReportDocument();

	/**
	 * Check whether report document file existed.
	 * 
	 * @param docName
	 * @throws RemoteException
	 */
	abstract protected void __checkDocumentExists() throws Exception;

	/**
	 * default constructor
	 * 
	 * @param context
	 * @param operation
	 * @param response
	 */
	public AbstractGetPageActionHandler(IContext context, Operation operation, GetUpdatedObjectsResponse response) {
		super(context, operation, response);
	}

	/**
	 * execute action hanlder
	 */
	protected void __execute() throws Exception {
		prepareParameters();
		doExecution();
		prepareResponse();
	}

	/**
	 * Prepare parameters
	 * 
	 * @throws Exception
	 * @throws RemoteException
	 */
	protected void prepareParameters() throws Exception, RemoteException {
		__bean = (ViewerAttributeBean) context.getBean();
		__docName = __getReportDocument();
		__checkDocumentExists();

		if (ParameterAccessor.isGetReportlet(context.getRequest())) {
			// Reportlet doesn't support pagination
			__totalPageNumber = 1;
		} else {
			// Get total page count.
			InputOptions getPageCountOptions = new InputOptions();
			getPageCountOptions.setOption(InputOptions.OPT_LOCALE, __bean.getLocale());
			getPageCountOptions.setOption(InputOptions.OPT_TIMEZONE, __bean.getTimeZone());
			getPageCountOptions.setOption(InputOptions.OPT_REQUEST, context.getRequest());
			OutputOptions outputOptions = new OutputOptions();

			__totalPageNumber = getReportService().getPageCount(__docName, getPageCountOptions, outputOptions);

			Boolean isCompleted = (Boolean) outputOptions.getOption(OutputOptions.OPT_REPORT_GENERATION_COMPLETED);
			if (isCompleted != null) {
				__isCompleted = isCompleted.booleanValue();
			}
		}

		__pageNumber = getPageNumber(context.getRequest(), operation.getOprand(), __docName);

		// No valid page number check bookmark from soap message.
		if (!isValidPageNumber(context.getRequest(), __pageNumber, __docName)) {
			__bookmark = getBookmark(operation.getOprand(), __bean);
			if (__bookmark != null && __bookmark.length() > 0) {
				InputOptions options = new InputOptions();
				options.setOption(InputOptions.OPT_REQUEST, context.getRequest());
				options.setOption(InputOptions.OPT_LOCALE, __bean.getLocale());

				// Bookmark is a TOC name, then find TOC id by name
				if (isToc(operation.getOprand(), __bean)) {
					__bookmark = (getReportService()).findTocByName(__docName, __bookmark, options);
				}

				__pageNumber = getReportService().getPageNumberByBookmark(__docName, __bookmark, options);

				if (!isValidPageNumber(context.getRequest(), __pageNumber, __docName)) {
					AxisFault fault = new AxisFault();
					fault.setFaultReason(BirtResources.getMessage(ResourceConstants.ACTION_EXCEPTION_INVALID_BOOKMARK,
							new String[] { getBookmark(operation.getOprand(), __bean) }));
					throw fault;
				}
				__useBookmark = true;
			}
		}

		// Verify the page number again.
		if (!isValidPageNumber(context.getRequest(), __pageNumber, __docName)) {
			AxisFault fault = new AxisFault();
			fault.setFaultReason(BirtResources.getMessage(ResourceConstants.ACTION_EXCEPTION_INVALID_PAGE_NUMBER,
					new Object[] { Long.valueOf(__pageNumber), Long.valueOf(__totalPageNumber) }));
			throw fault;
		}

		__svgFlag = getSVGFlag(operation.getOprand());
	}

	/**
	 * Execution process
	 * 
	 * @throws ReportServiceException
	 * @throws RemoteException
	 */
	protected void doExecution() throws ReportServiceException, RemoteException {
		InputOptions options = createInputOptions(__bean, __svgFlag);
		String docName = null;

		__activeIds = new ArrayList();
		if (ParameterAccessor.isGetReportlet(context.getRequest())) {
			// get attribute bean
			ViewerAttributeBean attrBean = (ViewerAttributeBean) context.getBean();
			assert attrBean != null;

			docName = attrBean.getReportDocumentName();
			// render reportlet
			String __reportletId = attrBean.getReportletId();
			__page = getReportService().getReportlet(docName, __reportletId, options, __activeIds);
		} else {
			docName = __docName;
			__page = getReportService().getPage(docName, __pageNumber + "", //$NON-NLS-1$
					options, __activeIds);
		}
	}

	/**
	 * Prepare response
	 * 
	 * @throws ReportServiceException
	 * @throws RemoteException
	 */
	protected void prepareResponse() throws ReportServiceException, RemoteException {
		// Update instruction for document part.
		UpdateContent content = new UpdateContent();

		content.setContent(DataUtil.toUTF8(__page.toByteArray()));

		content.setTarget("Document"); //$NON-NLS-1$
		content.setInitializationId(parseReportId(__activeIds));

		if (__useBookmark) {
			content.setBookmark(__bookmark);
		}
		Update updateDocument = new Update();
		updateDocument.setUpdateContent(content);

		// Update instruction for nav bar.
		UpdateData updateData = new UpdateData();
		updateData.setTarget("navigationBar"); //$NON-NLS-1$
		Page pageObj = new Page();
		pageObj.setPageNumber(String.valueOf(__pageNumber));
		pageObj.setTotalPage(String.valueOf(__totalPageNumber));
		pageObj.setRtl(__bean.isReportRtl());
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
		return pageNumber > 0 && pageNumber <= __totalPageNumber;
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
					if (pageNumber <= 0 || pageNumber > __totalPageNumber) {
						AxisFault fault = new AxisFault();
						fault.setFaultCode(new QName("DocumentProcessor.getPageNumber( )")); //$NON-NLS-1$
						fault.setFaultString(
								BirtResources.getMessage(ResourceConstants.ACTION_EXCEPTION_INVALID_PAGE_NUMBER,
										new Object[] { Long.valueOf(pageNumber), Long.valueOf(__totalPageNumber) }));
						throw fault;
					}

					break;
				}
			}
		}

		return pageNumber;
	}
}
