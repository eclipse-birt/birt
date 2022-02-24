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

package org.eclipse.birt.report;

import org.eclipse.birt.report.engine.api.HTMLRenderOption;

public interface IBirtConstants {

	// Oprand Name Value pair: Names
	String OPRAND_PAGENO = "__page"; //$NON-NLS-1$
	String OPRAND_BOOKMARK = "__bookmark"; //$NON-NLS-1$
	String OPRAND_SVG = "__svg"; //$NON-NLS-1$
	String OPRAND_TASKID = "__taskid"; //$NON-NLS-1$
	String OPRAND_ISTOC = "__istoc"; //$NON-NLS-1$
	String OPRAND_PARAM = "param"; //$NON-NLS-1$

	String MIME_TYPE = "text/xml"; //$NON-NLS-1$
	String HTML_RENDER_FORMAT = HTMLRenderOption.OUTPUT_FORMAT_HTML;
	String PDF_RENDER_FORMAT = HTMLRenderOption.OUTPUT_FORMAT_PDF;
	String POSTSCRIPT_RENDER_FORMAT = "postscript"; //$NON-NLS-1$
	String DOC_RENDER_FORMAT = "doc"; //$NON-NLS-1$
	String EXCEL_RENDER_FORMAT = "xls"; //$NON-NLS-1$
	String PPT_RENDER_FORMAT = "ppt"; //$NON-NLS-1$

	// frameset viewer model.
	String VIEWER_FRAMESET = "frameset"; //$NON-NLS-1$

	// run viewer model.
	String VIEWER_RUN = "run"; //$NON-NLS-1$

	// preview viewer model.
	String VIEWER_PREVIEW = "preview"; //$NON-NLS-1$

	// parameter viewer model.
	String VIEWER_PARAMETER = "parameter"; //$NON-NLS-1$

	// document viewer model.
	String VIEWER_DOCUMENT = "document"; //$NON-NLS-1$

	// output viewer model.
	String VIEWER_OUTPUT = "output"; //$NON-NLS-1$

	// download viewer model.
	String VIEWER_DOWNLOAD = "download"; //$NON-NLS-1$

	// extract viewer model.
	String VIEWER_EXTRACT = "extract"; //$NON-NLS-1$

	// Servlet path for frameset model.
	String SERVLET_PATH_FRAMESET = "/" + VIEWER_FRAMESET; //$NON-NLS-1$

	// Servlet path for running model.
	String SERVLET_PATH_RUN = "/" + VIEWER_RUN; //$NON-NLS-1$

	// Servlet path for preview model.
	String SERVLET_PATH_PREVIEW = "/" + VIEWER_PREVIEW; //$NON-NLS-1$

	// Servlet path for parameter model.
	String SERVLET_PATH_PARAMETER = "/" + VIEWER_PARAMETER; //$NON-NLS-1$

	// Servlet path for document model.
	String SERVLET_PATH_DOCUMENT = "/" + VIEWER_DOCUMENT; //$NON-NLS-1$

	// Servlet path for output model.
	String SERVLET_PATH_OUTPUT = "/" + VIEWER_OUTPUT; //$NON-NLS-1$

	// Servlet path for download model.
	String SERVLET_PATH_DOWNLOAD = "/" + VIEWER_DOWNLOAD; //$NON-NLS-1$

	// Servlet path for data extraction model.
	String SERVLET_PATH_EXTRACT = "/" + VIEWER_EXTRACT; //$NON-NLS-1$

	// request GET method.
	String REQUEST_GET = "get"; //$NON-NLS-1$

	// request POST method.
	String REQUEST_POST = "post"; //$NON-NLS-1$

	// suffix of design file
	String SUFFIX_DESIGN_FILE = "rptdesign"; //$NON-NLS-1$

	// suffix of template file
	String SUFFIX_TEMPLATE_FILE = "rpttemplate"; //$NON-NLS-1$

	// suffix of library file
	String SUFFIX_LIBRARY_FILE = "rptlibrary"; //$NON-NLS-1$

	// suffix of design document file
	String SUFFIX_DESIGN_DOCUMENT = "rptdocument"; //$NON-NLS-1$

	// suffix of design config file
	String SUFFIX_DESIGN_CONFIG = "rptconfig"; //$NON-NLS-1$

	// Attribute Bean
	String ATTRIBUTE_BEAN = "attributeBean"; //$NON-NLS-1$

	// BIRT viewing sessions
	String ATTRIBUTE_VIEWING_SESSION_MANAGER = "viewingSessionManager"; //$NON-NLS-1$

	String SESSION_ID_DATE_FORMAT = "yyyyMMdd_HHmmss_SSS"; //$NON-NLS-1$

	// If Cascade parameter
	String IS_CASCADE = "isCascade"; //$NON-NLS-1$

	// Property -- type
	String PROP_TYPE = "type"; //$NON-NLS-1$

	// Property -- value expression
	String PROP_EXPR = "expr"; //$NON-NLS-1$

	// Task Map
	String TASK_MAP = "tasks"; //$NON-NLS-1$

	// default value of viewer settings
	String DEFAULT_VIEWER_CONFIG_FILE = "WEB-INF/viewer.properties"; //$NON-NLS-1$
	String DEFAULT_DOCUMENT_FOLDER = "documents";//$NON-NLS-1$
	String DEFAULT_IMAGE_FOLDER = "report/images"; //$NON-NLS-1$
	String DEFAULT_LOGS_FOLDER = "logs"; //$NON-NLS-1$
	String DEFAULT_LOGS_LEVEL = "WARNING"; //$NON-NLS-1$
	String DEFAULT_SCRIPTLIB_FOLDER = "scriptlib"; //$NON-NLS-1$

	// Open type
	String OPEN_TYPE_INLINE = "inline"; //$NON-NLS-1$
	String OPEN_TYPE_ATTACHMENT = "attachment"; //$NON-NLS-1$

	// Action name
	String ACTION_PRINT = "print"; //$NON-NLS-1$

	// Message type
	String MSG_ERROR = "error"; //$NON-NLS-1$
	String MSG_COMPLETE = "complete"; //$NON-NLS-1$

	// Flag
	String VAR_ON = "ON"; //$NON-NLS-1$
	String VAR_OFF = "OFF"; //$NON-NLS-1$

	// Null Value
	String NULL_VALUE = "$${{((null))}}$$"; //$NON-NLS-1$
	String NULL_VALUE_DISPLAY = "Null Value"; //$NON-NLS-1$

	// System property name
	String SYS_PROP_WORKING_PATH = "birt.viewer.working.path"; //$NON-NLS-1$
	String SYS_PROP_RESOURCE_PATH = "birt.viewer.resource.path"; //$NON-NLS-1$
	String SYS_PROP_ROOT_PATH = "birt.viewer.root.path"; //$NON-NLS-1$
	String SYS_PROP_BIRT_ISDESIGNER = "birt.designer"; //$NON-NLS-1$

	// app context key
	String APPCONTEXT_BIRT_RESOURCE_PATH = "birt.viewer.resource.path"; //$NON-NLS-1$
	/**
	 * unknown line number
	 */
	int UNKNOWN_POSITION = -1;

	/**
	 * Default encoding for http request
	 */
	String DEFAULT_ENCODE = "UTF-8"; //$NON-NLS-1$

	/**
	 * Page overflow mode when render report as PDF/PS
	 */
	int PAGE_OVERFLOW_AUTO = 0;
	int PAGE_OVERFLOW_ACTUAL = 1;
	int PAGE_OVERFLOW_FITTOPAGE = 2;

	String DOC_PROPERTY_RTL = "docRtl";
	/**
	 * Suffix of report document.
	 */
	String SUFFIX_REPORT_DOCUMENT = ".rptdocument"; //$NON-NLS-1$

	String SERVLET_PATH = "BirtViewerServletPath";
}
