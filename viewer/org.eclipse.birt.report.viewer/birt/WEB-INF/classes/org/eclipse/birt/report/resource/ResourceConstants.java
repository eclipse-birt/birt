/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.resource;

public interface ResourceConstants
{

	// dialog title

	public static final String EXCEPTION_DIALOG_TITLE = "birt.viewer.dialog.exception.title"; //$NON-NLS-1$
	public static final String EXPORT_REPORT_DIALOG_TITLE = "birt.viewer.dialog.exportReport.title"; //$NON-NLS-1$
	public static final String PARAMETER_DIALOG_TITLE = "birt.viewer.dialog.parameter.title"; //$NON-NLS-1$
	public static final String SIMPLE_EXPORT_DATA_DIALOG_TITLE = "birt.viewer.dialog.simpleExportData.title"; //$NON-NLS-1$

	/**
	 * Page title for the "web viewer", "html" preview.
	 */

	public static final String BIRT_VIEWER_TITLE = "birt.viewer.title"; //$NON-NLS-1$

	// report service exception

	public static final String REPORT_SERVICE_EXCEPTION_EXTRACT_DATA_NO_DOCUMENT = "birt.viewer.reportServiceException.EXTRACT_DATA_NO_DOCUMENT"; //$NON-NLS-1$
	public static final String REPORT_SERVICE_EXCEPTION_EXTRACT_DATA_NO_RESULT_SET = "birt.viewer.reportServiceException.EXTRACT_DATA_NO_RESULT_SET"; //$NON-NLS-1$
	public static final String REPORT_SERVICE_EXCEPTION_INVALID_TOC = "birt.viewer.reportServiceException.INVALID_TOC"; //$NON-NLS-1$
	public static final String REPORT_SERVICE_EXCEPTION_INVALID_PARAMETER = "birt.viewer.reportServiceException.INVALID_PARAMETER"; //$NON-NLS-1$

	// birt action exception

	public static final String ACTION_EXCEPTION_NO_REPORT_DOCUMENT = "birt.viewer.actionException.NO_REPORT_DOCUMENT"; //$NON-NLS-1$
	public static final String ACTION_EXCEPTION_INVALID_BOOKMARK = "birt.viewer.actionException.INVALID_BOOKMARK"; //$NON-NLS-1$
	public static final String ACTION_EXCEPTION_INVALID_PAGE_NUMBER = "birt.viewer.actionException.INVALID_PAGE_NUMBER"; //$NON-NLS-1$
	public static final String ACTION_EXCEPTION_INVALID_ID_FORMAT = "birt.viewer.actionException.INVALID_ID_FORMAT"; //$NON-NLS-1$

	// birt soap binding exception

	public static final String SOAP_BINDING_EXCEPTION_NO_HANDLER_FOR_TARGET = "birt.viewer.soapBindingException.NO_HANDLER_FOR_TARGET"; //$NON-NLS-1$
	
	// component processor exception
	
	public static final String COMPONENT_PROCESSOR_EXCEPTION_MISSING_OPERATOR = "birt.viewer.componentProcessorException.MISSING_OPERATOR"; //$NON-NLS-1$
	
	// stack trace title
	
	public static final String EXCEPTION_DIALOG_STACK_TRACE = "birt.viewer.exceptionDialog.stackTrace"; //$NON-NLS-1$

}
