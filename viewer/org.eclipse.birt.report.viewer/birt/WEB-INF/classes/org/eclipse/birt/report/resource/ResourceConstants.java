/*******************************************************************************
 * Copyright (c) 2004, 2025 Actuate Corporation and others
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.resource;

/**
 * Constant values of resource labels
 *
 * @since 3.3
 *
 */
public interface ResourceConstants {

	// dialog title
	/** property: exception dialog title */
	String EXCEPTION_DIALOG_TITLE = "birt.viewer.dialog.exception.title"; //$NON-NLS-1$

	/** property: export dialog title */
	String EXPORT_REPORT_DIALOG_TITLE = "birt.viewer.dialog.exportReport.title"; //$NON-NLS-1$

	/** property: parameter dialog title */
	String PARAMETER_DIALOG_TITLE = "birt.viewer.dialog.parameter.title"; //$NON-NLS-1$

	/** property: export data dialog title */
	String SIMPLE_EXPORT_DATA_DIALOG_TITLE = "birt.viewer.dialog.simpleExportData.title"; //$NON-NLS-1$

	/** property: print report server dialog title */
	String PRINT_REPORTSERVER_DIALOG_TITLE = "birt.viewer.dialog.printReportServer.title"; //$NON-NLS-1$

	/** property: print report dialog title */
	String PRINT_REPORT_DIALOG_TITLE = "birt.viewer.dialog.printReport.title"; //$NON-NLS-1$

	/** property: confirmation dialog title */
	String CONFIRMATION_DIALOG_TITLE = "birt.viewer.dialog.confirmation.title"; //$NON-NLS-1$

	/** property: message dialog title */
	String MESSAGE_DIALOG_TITLE = "birt.viewer.dialog.message.title"; //$NON-NLS-1$

	/**
	 * Page title for the "web viewer", "html" preview.
	 */
	String BIRT_VIEWER_TITLE = "birt.viewer.title"; //$NON-NLS-1$


	// errors
	/** property: general error parameter is invalid */
	String GENERAL_ERROR_PARAMETER_INVALID = "birt.viewer.error.parameter.invalid"; //$NON-NLS-1$

	/** property: general error parameter cannot be blank */
	String GENERAL_ERROR_PARAMETER_NOTBLANK = "birt.viewer.error.parameternotallowblank"; //$NON-NLS-1$

	/** property: general error parameter is invalid */
	String GENERAL_ERROR_NO_VIEWING_SESSION = "birt.viewer.error.noviewingsession"; //$NON-NLS-1$

	/** property: general error viewer session expired */
	String GENERAL_ERROR_VIEWING_SESSION_EXPIRED = "birt.viewer.error.viewingsessionexpired"; //$NON-NLS-1$

	/** property: general error viewer session locked */
	String GENERAL_ERROR_VIEWING_SESSION_LOCKED = "birt.viewer.error.viewingsessionlocked"; //$NON-NLS-1$

	/** property: general error viewer maximum session reached */
	String GENERAL_ERROR_VIEWING_SESSION_MAX_REACHED = "birt.viewer.error.viewingsessionmaxreached"; //$NON-NLS-1$

	/** property: error trace copy */
	String ERROR_STACK_TRACE_COPY = "birt.viewer.error.stacktrace.copy"; //$NON-NLS-1$

	/** property: error trace copied */
	String ERROR_STACK_TRACE_COPIED = "birt.viewer.error.stacktrace.copy.done"; //$NON-NLS-1$

	/** property: error invalid extension for document parameter */
	String ERROR_INVALID_EXTENSION_FOR_DOCUMENT_PARAMETER = "birt.viewer.error.invalidextfordocumentparam"; //$NON-NLS-1$


	// general exception

	/** property: general exception of document file error */
	String GENERAL_EXCEPTION_DOCUMENT_FILE_ERROR = "birt.viewer.generalException.DOCUMENT_FILE_ERROR"; //$NON-NLS-1$

	/** property: general exception of document file access error */
	String GENERAL_EXCEPTION_DOCUMENT_ACCESS_ERROR = "birt.viewer.generalException.DOCUMENT_ACCESS_ERROR"; //$NON-NLS-1$

	/** property: general exception of report file error */
	String GENERAL_EXCEPTION_REPORT_FILE_ERROR = "birt.viewer.generalException.REPORT_FILE_ERROR"; //$NON-NLS-1$

	/** property: general exception of report file access error */
	String GENERAL_EXCEPTION_REPORT_ACCESS_ERROR = "birt.viewer.generalException.REPORT_ACCESS_ERROR"; //$NON-NLS-1$

	/** property: general exception of document file processing */
	String GENERAL_EXCEPTION_DOCUMENT_FILE_PROCESSING = "birt.viewer.generalException.DOCUMENT_FILE_PROCESSING"; //$NON-NLS-1$

	/** property: general exception no report design file available */
	String GENERAL_EXCEPTION_NO_REPORT_DESIGN = "birt.viewer.generalException.NO_REPORT_DESIGN"; //$NON-NLS-1$

	/** property: general exception of multiple exceptions */
	String GENERAL_EXCEPTION_MULTIPLE_EXCEPTIONS = "birt.viewer.generalException.MULTIPLE_EXCEPTIONS"; //$NON-NLS-1$


	// report service exception

	/** property: report service exception, not data of the document */
	String REPORT_SERVICE_EXCEPTION_EXTRACT_DATA_NO_DOCUMENT = "birt.viewer.reportServiceException.EXTRACT_DATA_NO_DOCUMENT"; //$NON-NLS-1$

	/** property: report service exception, not result set selected */
	String REPORT_SERVICE_EXCEPTION_EXTRACT_DATA_NO_RESULT_SET = "birt.viewer.reportServiceException.EXTRACT_DATA_NO_RESULT_SET"; //$NON-NLS-1$

	/** property: report service exception, invalid TOC */
	String REPORT_SERVICE_EXCEPTION_INVALID_TOC = "birt.viewer.reportServiceException.INVALID_TOC"; //$NON-NLS-1$

	/** property: report service exception, invalid parameter */
	String REPORT_SERVICE_EXCEPTION_INVALID_PARAMETER = "birt.viewer.reportServiceException.INVALID_PARAMETER"; //$NON-NLS-1$

	/** property: report service exception, error at startup of report engine */
	String REPORT_SERVICE_EXCEPTION_STARTUP_REPORTENGINE_ERROR = "birt.viewer.reportServiceException.STARTUP_REPORTENGINE_ERROR"; //$NON-NLS-1$


	// data extraction exception

	/** property: report service exception, invalid extraction format */
	String REPORT_SERVICE_EXCEPTION_INVALID_EXTRACTFORMAT = "birt.viewer.reportServiceException.INVALID_EXTRACTFORMAT"; //$NON-NLS-1$

	/** property: report service exception, invalid extraction extension */
	String REPORT_SERVICE_EXCEPTION_INVALID_EXTRACTEXTENSION = "birt.viewer.reportServiceException.INVALID_EXTRACTEXTENSION"; //$NON-NLS-1$


	// birt action exception

	/** property: action exception, no report document */
	String ACTION_EXCEPTION_NO_REPORT_DOCUMENT = "birt.viewer.actionException.NO_REPORT_DOCUMENT"; //$NON-NLS-1$

	/** property: action exception, invalid bookmark */
	String ACTION_EXCEPTION_INVALID_BOOKMARK = "birt.viewer.actionException.INVALID_BOOKMARK"; //$NON-NLS-1$

	/** property: action exception, invalid page number */
	String ACTION_EXCEPTION_INVALID_PAGE_NUMBER = "birt.viewer.actionException.INVALID_PAGE_NUMBER"; //$NON-NLS-1$

	/** property: action exception, page number parse error */
	String ACTION_EXCEPTION_PAGE_NUMBER_PARSE_ERROR = "birt.viewer.actionException.PAGE_NUMBER_PARSE_ERROR"; //$NON-NLS-1$

	/** property: action exception, invalid id format */
	String ACTION_EXCEPTION_INVALID_ID_FORMAT = "birt.viewer.actionException.INVALID_ID_FORMAT"; //$NON-NLS-1$

	/** property: action exception, document file not exists */
	String ACTION_EXCEPTION_DOCUMENT_FILE_NO_EXIST = "birt.viewer.actionException.DOCUMENT_FILE_NO_EXIST"; //$NON-NLS-1$


	// birt soap binding exception

	/** property: SOAP bining exception, no handler for target */
	String SOAP_BINDING_EXCEPTION_NO_HANDLER_FOR_TARGET = "birt.viewer.soapBindingException.NO_HANDLER_FOR_TARGET"; //$NON-NLS-1$


	// component processor exception

	/** property: component processor exception, missing operator */
	String COMPONENT_PROCESSOR_EXCEPTION_MISSING_OPERATOR = "birt.viewer.componentProcessorException.MISSING_OPERATOR"; //$NON-NLS-1$


	// stack trace title

	/** property: exception dialog title of stack trace */
	String EXCEPTION_DIALOG_STACK_TRACE = "birt.viewer.exceptionDialog.stackTrace"; //$NON-NLS-1$

	/** property: exception dialog label to show stack trace */
	String EXCEPTION_DIALOG_SHOW_STACK_TRACE = "birt.viewer.exceptionDialog.showStackTrace"; //$NON-NLS-1$

	/** property: exception dialog label to hide stack trace */
	String EXCEPTION_DIALOG_HIDE_STACK_TRACE = "birt.viewer.exceptionDialog.hideStackTrace"; //$NON-NLS-1$


	// viewer taglib excepton

	/** property: tag library, no attribute id defined */
	String TAGLIB_NO_ATTR_ID = "birt.viewer.taglib.NO_ATTR_ID"; //$NON-NLS-1$

	/** property: tag library, invalid attribute id */
	String TAGLIB_INVALID_ATTR_ID = "birt.viewer.taglib.INVALID_ATTR_ID"; //$NON-NLS-1$

	/** property: tag library, duplicate of attribute id */
	String TAGLIB_ATTR_ID_DUPLICATE = "birt.viewer.taglib.ATTR_ID_DUPLICATE"; //$NON-NLS-1$

	/** property: tag library, duplicate of parameter name */
	String TAGLIB_PARAM_NAME_DUPLICATE = "birt.viewer.taglib.PARAM_NAME_DUPLICATE"; //$NON-NLS-1$

	/** property: tag library, no report source found */
	String TAGLIB_NO_REPORT_SOURCE = "birt.viewer.taglib.NO_REPORT_SOURCE"; //$NON-NLS-1$

	/** property: tag library, no report document found */
	String TAGLIB_NO_REPORT_DOCUMENT = "birt.viewer.taglib.NO_REPORT_DOCUMENT"; //$NON-NLS-1$

	/** property: tag library, no requestor name defined */
	String TAGLIB_NO_REQUESTER_NAME = "birt.viewer.taglib.NO_REQUESTER_NAME"; //$NON-NLS-1$


	// birt general exception

	/** property: exception of disabled cookies */
	String EXCEPTION_MAYBE_DISABLED_COOKIES = "birt.viewer.exception.maybe_disabled_cookies"; //$NON-NLS-1$

}
