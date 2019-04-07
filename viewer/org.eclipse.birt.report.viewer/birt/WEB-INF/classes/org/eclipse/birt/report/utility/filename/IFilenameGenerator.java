/*************************************************************************************
 * Copyright (c) 2008 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/
package org.eclipse.birt.report.utility.filename;

import java.util.Map;

public interface IFilenameGenerator
{
	/**
	 * Reference to an instance of ServletContext.
	 * @see ServletContext
	 */
	public static final String OPTIONS_SERVLET_CONTEXT = "servletContext";
	
	/**
	 * Reference to the http request
	 * @see HttpServletRequest
	 */
	public static final String OPTIONS_HTTP_REQUEST = "httpRequest";	

	/**
	 * Reference to the viewer attributes bean.
	 * @see ViewerAttributeBean
	 */
	public static final String OPTIONS_VIEWER_ATTRIBUTES_BEAN = "viewerAttributesBean";	
	
	/**
	 * Reference to the report design file name as String.
	 */
	public static final String OPTIONS_REPORT_DESIGN = "reportDesign";
	
	/**
	 * Reference to the report document file name as String.
	 */	
	public static final String OPTIONS_REPORT_DOCUMENT = "reportDocument";

	/**
	 * Reference to a target file extension as String.
	 */
	public static final String OPTIONS_TARGET_FILE_EXTENSION = "targetFileExtension";
	
	/**
	 * Reference to an instance of EmitterInfo.
	 * Only specified for IFilenameGenerator.getExportFilename().
	 * @see EmitterInfo
	 * @see IFilenameGenerator#getExportFilename(Map)
	 */
	public static final String OPTIONS_EMITTER_INFO = "emitterInfo";

	/**
	 * Reference to the extraction extension name.
	 * Only specified for IFilenameGenerator.getExtractionFilename()
	 * @see IFilenameGenerator#getExtractionFilename(Map)
	 */
	public static final String OPTIONS_EXTRACTION_EXTENSION = "extractionExtension";
	
	/**
	 * Specifies a file name for a report export operation.
	 */
	public static final String OUTPUT_TYPE_EXPORT = "export";

	/**
	 * Specifies a file name for a report data extraction operation.
	 */
	public static final String OUTPUT_TYPE_DATA_EXTRACTION = "dataExtraction";

	/**
	 * Specifies a file name for a report document generation operation.
	 */
	public static final String OUTPUT_TYPE_REPORT_DOCUMENT = "reportDocument";
	
	/**
	 * Generates a file name for the given output type.
	 * @param baseName base file name without extension
	 * @param fileExtension file extension
	 * @param outputType output type, one of the OUTPUT_TYPE_* constants.
	 * @param options options map
	 */	
	public String getFilename( String baseName, String fileExtension, String outputType, Map options);
}
