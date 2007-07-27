/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
 * Defines render options for emitters
 */
public interface IRenderOption
{

	public static final String OUTPUT_FORMAT = "Format"; //$NON-NLS-1$
	public static final String OUTPUT_FORMAT_HTML = "html"; //$NON-NLS-1$
	public static final String OUTPUT_FORMAT_PDF = "pdf"; //$NON-NLS-1$
	public static final String OUTPUT_FORMAT_FO = "fo"; //$NON-NLS-1$
	public static final String EMITTER_ID = "emitterID"; //$NON-NLS-1$

	public static final String OUTPUT_FILE_NAME = "outputFile"; //$NON-NLS-1$
	public static final String OUTPUT_STREAM = "outputStream"; //$NON-NLS-1$

	public static final String IMAGE_HANDLER = "imageHandler"; //$NON-NLS-1$
	public static final String ACTION_HANDLER = "actionHandler"; //$NON-NLS-1$

	public static final String LOCALE = "locale"; //$NON-NLS-1$

	public static final String SUPPORTED_IMAGE_FORMATS = "supportedImageFormats";

	public static final String BASE_URL = "baseUrl";
	
	public static final String OUTPUT_DISPLAY_NONE = "org.eclipse.birt.report.engine.api.IRenderOption.outputDisplayNone";

	/**
	 * get all the options defined in this object
	 * 
	 * @return
	 */
	public HashMap getOptions( );

	/**
	 * @return
	 * @deprecated use getOptions instead
	 */
	public HashMap getOutputSetting( );

	/**
	 * set the option value.
	 * 
	 * @param name
	 *            option name.
	 * @param value
	 *            value
	 */
	public void setOption( String name, Object value );

	/**
	 * get the option value defined by the name.
	 * 
	 * @param name
	 *            option name.
	 * @return value, null if not defined
	 */
	public Object getOption( String name );

	/**
	 * if there exits an option named by name.
	 * 
	 * @param name
	 *            option name.
	 * @return true if user has defined an option with this name, even if the
	 *         value is NULL. false otherwise.
	 */
	public boolean hasOption( String name );

	public void setOutputFormat( String format );

	public String getOutputFormat( );

	public void setOutputFileName( String outputFileName );

	public String getOutputFileName( );

	public void setOutputStream( OutputStream ostream );

	public OutputStream getOutputStream( );

	/**
	 * @param formats -
	 *            the image format supported by the browser
	 */
	public void setSupportedImageFormats( String formats );

	/**
	 * @return the image format supported by the browser
	 */
	public String getSupportedImageFormats( );

	/**
	 * Returns the base URL for creating an Action URL
	 * 
	 * @return the baseURL.
	 */
	public String getBaseURL( );

	/**
	 * sets the base url for action handling
	 * 
	 * @param baseURL
	 *            sets the base URL used for action handling
	 */
	public void setBaseURL( String baseURL );

	/**
	 * @deprecated
	 * @param handler
	 */
	public void setActionHandle( IHTMLActionHandler handler );

	public void setActionHandler( IHTMLActionHandler handler );

	/**
	 * @deprecated
	 * @return
	 */
	public IHTMLActionHandler getActionHandle( );

	public IHTMLActionHandler getActionHandler( );

	/**
	 * @deprecated
	 * @param handler
	 */
	public void setImageHandle( IHTMLImageHandler handler );

	public void setImageHandler( IHTMLImageHandler handler );

	/**
	 * @deprecated
	 * @return
	 */
	public IHTMLImageHandler getImageHandle( );

	public IHTMLImageHandler getImageHandler( );

	/**
	 * returns the emitter id
	 * 
	 * @return Returns the emitter id
	 */
	public String getEmitterID( );

	/**
	 * sets the emitter id
	 * 
	 * @param emitterID
	 */
	public void setEmitterID( String emitterID );

}