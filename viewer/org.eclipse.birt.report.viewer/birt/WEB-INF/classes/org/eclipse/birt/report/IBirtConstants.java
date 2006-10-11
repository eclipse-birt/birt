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

package org.eclipse.birt.report;

import org.eclipse.birt.report.engine.api.HTMLRenderOption;

public interface IBirtConstants
{

	// Oprand Name Value pair: Names
	static public final String OPRAND_PAGENO = "page"; //$NON-NLS-1$
	static public final String OPRAND_BOOKMARK = "bookmark"; //$NON-NLS-1$
	static public final String OPRAND_TOC = "isToc"; //$NON-NLS-1$
	static public final String OPRAND_SVG = "svg"; //$NON-NLS-1$
	static public final String OPRAND_PARAM = "param"; //$NON-NLS-1$
	static public final String OPRAND_IID = "iid"; //$NON-NLS-1$
	
	static public String MIME_TYPE = "text/xml"; //$NON-NLS-1$
	static public String HTML_RENDER_FORMAT = HTMLRenderOption.OUTPUT_FORMAT_HTML;
	public static final String PDF_RENDER_FORMAT = HTMLRenderOption.OUTPUT_FORMAT_PDF;
	public static final String DOC_RENDER_FORMAT = HTMLRenderOption.OUTPUT_FORMAT_DOC;

	// Servlet path for parameter model.
	public static final String SERVLET_PATH_PARAMETER = "/parameter"; //$NON-NLS-1$

	// Servlet path for preview model.
	public static final String SERVLET_PATH_PREVIEW = "/preview"; //$NON-NLS-1$

	// Servlet path for frameset model.
	public static final String SERVLET_PATH_FRAMESET = "/frameset"; //$NON-NLS-1$

	// Servlet path for running model.
	public static final String SERVLET_PATH_RUN = "/run"; //$NON-NLS-1$

	// Servlet path for download model.
	public static final String SERVLET_PATH_DOWNLOAD = "/download"; //$NON-NLS-1$

	// parameter viewer model.
	public static final String VIEWER_PARAMETER = "parameter"; //$NON-NLS-1$

	// frameset viewer model.
	public static final String VIEWER_FRAMESET = "frameset"; //$NON-NLS-1$

	// running viewer model.
	public static final String VIEWER_RUN = "run"; //$NON-NLS-1$

//	 request GET method.
	public static final String REQUEST_GET = "get"; //$NON-NLS-1$

	// request POST method.
	public static final String REQUEST_POST = "post"; //$NON-NLS-1$

	// suffix of design file
	public static final String SUFFIX_DESIGN_FILE = "rptdesign"; //$NON-NLS-1$

	// suffix of template file
	public static final String SUFFIX_TEMPLATE_FILE = "rpttemplate"; //$NON-NLS-1$

	// suffix of library file
	public static final String SUFFIX_LIBRARY_FILE = "rptlibrary"; //$NON-NLS-1$

	// suffix of design document file
	public static final String SUFFIX_DESIGN_DOCUMENT = "rptdocument"; //$NON-NLS-1$

	// suffix of design config file
	public static final String SUFFIX_DESIGN_CONFIG = "rptconfig"; //$NON-NLS-1$
	
	// Attribute Bean
	public static final String ATTRIBUTE_BEAN = "attributeBean"; //$NON-NLS-1$
	
	// If Cascade parameter
	public static final String IS_CASCADE = "isCascade"; //$NON-NLS-1$
	
	// Property -- type
	public static final String PROP_TYPE = "type"; //$NON-NLS-1$
}