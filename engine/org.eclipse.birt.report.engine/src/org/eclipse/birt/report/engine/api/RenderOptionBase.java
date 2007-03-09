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

package org.eclipse.birt.report.engine.api;

import java.io.OutputStream;
import java.util.HashMap;

/**
 * Settings for rendering a report to an output format. Currently supported
 * options include format of the request, whether to render HTML with style
 * sheet, etc. Potential future options includes image formats in PDF (vector or
 * bitmap), font embedding options, etc.
 * <p>
 * The predefined options strings are what BIRT supports. Other options are
 * available depending on custom extensions.
 */
public class RenderOptionBase implements IRenderOption
{

	public static final String OUTPUT_FORMAT = "Format"; //$NON-NLS-1$
	public static final String IMAGE_HANDLER = "imageHandler"; //$NON-NLS-1$
	public static final String ACTION_HANDLER = "actionHandler"; //$NON-NLS-1$

	public static final String OUTPUT_FORMAT_HTML = "html"; //$NON-NLS-1$
	public static final String OUTPUT_FORMAT_PDF = "pdf"; //$NON-NLS-1$
	public static final String OUTPUT_FORMAT_FO = "fo"; //$NON-NLS-1$

	public static final String OUTPUT_FILE_NAME = "outputFile"; //$NON-NLS-1$
	public static final String OUTPUT_STREAM = "outputStream"; //$NON-NLS-1$

	public static final String LOCALE = "locale"; //$NON-NLS-1$
	
	/**
	 * The option is used to control if page scaling applies.
	 * If this option is set to true, the whole page content will be scaled based on page size 
	 * when the content size exceeds the page size. 
	 * Default value of the option is false, and the oversized contents will overflow.
	 */
	public static final String FIT_TO_PAGE = "fitToPage";
	
	/**
	 * The option is used to control pagination of layout engine based on fixed-size. 
	 * If this option is set to true, layout engine will ignore all page-break based on fixed-size. 
	 * The default value is false.
	 */
	public static final String PAGEBREAK_PAGINATION_ONLY = "pagebreakPaginationOnly";

	/**
	 * a hash map that stores the rendering options
	 */
	protected HashMap options = new HashMap( );

	/**
	 * constructor
	 */
	public RenderOptionBase( )
	{
	}

	/**
	 * returns the output format, i.e., html, pdf, etc.
	 * 
	 * @return Returns the output format
	 */
	public String getOutputFormat( )
	{
		return (String) getOption( OUTPUT_FORMAT );
	}

	/**
	 * set value for one rendering option
	 * 
	 * @param name
	 *            the option name
	 * @param value
	 *            value for the option
	 */
	protected void setOption( String name, Object value )
	{
		options.put( name, value );
	}

	/**
	 * get option value for one rendering option
	 * 
	 * @param name
	 *            the option name
	 * @return the option value
	 */
	protected Object getOption( String name )
	{
		return options.get( name );
	}

	/**
	 * returns the output settings
	 * 
	 * @return the output settings
	 */
	public HashMap getOutputSetting( )
	{
		return options;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IOutputSetting#setOutputFormat(java.lang.String)
	 */
	public void setOutputFormat( String format )
	{

		options.put( OUTPUT_FORMAT, format );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IOutputSetting#setOutputStream(java.io.OutputStream)
	 */
	public void setOutputStream( OutputStream ostream )
	{
		options.put( OUTPUT_STREAM, ostream );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IOutputSetting#setOutputFileName(java.lang.String)
	 */
	public void setOutputFileName( String outputFileName )
	{
		options.put( OUTPUT_FILE_NAME, outputFileName );
	}
}
