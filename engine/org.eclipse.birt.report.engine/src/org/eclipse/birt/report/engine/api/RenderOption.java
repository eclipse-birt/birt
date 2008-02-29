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
public class RenderOption implements IRenderOption
{

	/**
	 * a hash map that stores the rendering options
	 */
	protected HashMap options = new HashMap( );

	/**
	 * constructor
	 */
	public RenderOption( )
	{
		options = new HashMap( );
	}

	public RenderOption( HashMap options )
	{
		this.options = options;
	}

	public RenderOption( IRenderOption options )
	{
		this.options = options.getOptions( );
	}

	/**
	 * set value for one rendering option
	 * 
	 * @param name
	 *            the option name
	 * @param value
	 *            value for the option
	 */
	public void setOption( String name, Object value )
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
	public Object getOption( String name )
	{
		return options.get( name );
	}

	/**
	 * Check if an option is defined.
	 */
	public boolean hasOption( String name )
	{
		return options.containsKey( name );
	}

	public HashMap getOptions( )
	{
		return options;
	}

	/**
	 * returns the output settings
	 * 
	 * @return the output settings
	 * @deprecated user should always use the get/set to change the setting.
	 */
	public HashMap getOutputSetting( )
	{
		return options;
	}

	protected String getStringOption( String name )
	{
		Object value = options.get( name );
		if ( value instanceof String )
		{
			return (String) value;
		}
		return null;
	}

	protected boolean getBooleanOption( String name, boolean defaultValue )
	{
		Object value = options.get( name );
		if ( value instanceof Boolean )
		{
			return ( (Boolean) value ).booleanValue( );
		}
		else if ( value instanceof String )
		{
			return "true".equalsIgnoreCase( (String) value ); //$NON-NLS-1$
		}
		return defaultValue;
	}

	/**
	 * returns the output format, i.e., html, pdf, etc.
	 * 
	 * @return Returns the output format
	 */
	public String getOutputFormat( )
	{
		return getStringOption( OUTPUT_FORMAT );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IOutputSetting#setOutputFormat(java.lang.String)
	 */
	public void setOutputFormat( String format )
	{
		setOption( OUTPUT_FORMAT, format );
	}

	public String getEmitterID( )
	{
		return getStringOption( EMITTER_ID );
	}

	public void setEmitterID( String emitterId )
	{
		setOption( EMITTER_ID, emitterId );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IOutputSetting#setOutputStream(java.io.OutputStream)
	 */
	public void setOutputStream( OutputStream ostream )
	{
		setOption( OUTPUT_STREAM, ostream );
	}

	public OutputStream getOutputStream( )
	{
		Object out = getOption( OUTPUT_STREAM );
		if ( out instanceof OutputStream )
		{
			return (OutputStream) out;
		}
		return null;
	}

	public void setOutputFileName( String outputFileName )
	{
		setOption( OUTPUT_FILE_NAME, outputFileName );
	}

	public String getOutputFileName( )
	{
		return getStringOption( OUTPUT_FILE_NAME );
	}

	/**
	 * @param formats -
	 *            the image format supported by the browser
	 */
	public void setSupportedImageFormats( String formats )
	{
		setOption( SUPPORTED_IMAGE_FORMATS, formats );
	}

	/**
	 * @return the image format supported by the browser
	 */
	public String getSupportedImageFormats( )
	{
		return getStringOption( SUPPORTED_IMAGE_FORMATS );
	}

	/**
	 * Returns the base URL for creating an Action URL
	 * 
	 * @return the baseURL.
	 */
	public String getBaseURL( )
	{
		return getStringOption( BASE_URL );
	}

	/**
	 * sets the base url for action handling
	 * 
	 * @param baseURL
	 *            sets the base URL used for action handling
	 */
	public void setBaseURL( String baseURL )
	{
		setOption( BASE_URL, baseURL );
	}

	/**
	 * @deprecated
	 * @param handler
	 */
	public void setActionHandle( IHTMLActionHandler handler )
	{
		setActionHandler( handler );
	}

	public void setActionHandler( IHTMLActionHandler handler )
	{
		setOption( ACTION_HANDLER, handler );
	}

	/**
	 * @deprecated
	 * @return
	 */
	public IHTMLActionHandler getActionHandle( )
	{
		return getActionHandler( );
	}

	public IHTMLActionHandler getActionHandler( )
	{
		Object handler = getOption( ACTION_HANDLER );
		if ( handler instanceof IHTMLActionHandler )
		{
			return (IHTMLActionHandler) handler;
		}
		return null;
	}

	/**
	 * @deprecated
	 * @param handler
	 */
	public void setImageHandle( IHTMLImageHandler handler )
	{
		setImageHandler( handler );
	}

	public void setImageHandler( IHTMLImageHandler handler )
	{
		setOption( IMAGE_HANDLER, handler );
	}

	/**
	 * @deprecated
	 * @return
	 */
	public IHTMLImageHandler getImageHandle( )
	{
		return getImageHandler( );
	}

	public IHTMLImageHandler getImageHandler( )
	{
		Object handler = getOption( IMAGE_HANDLER );
		if ( handler instanceof IHTMLImageHandler )
		{
			return (IHTMLImageHandler) handler;
		}
		return null;
	}
	
	public boolean needCloseOutputStreamOnExit()
	{
		return getBooleanOption( CLOSE_OUTPUTSTREAM_ON_EXIT, true );
	}
	
	public void closeOutputStreamOnExit( boolean closeOnExit )
	{
		setOption( CLOSE_OUTPUTSTREAM_ON_EXIT, Boolean.valueOf( closeOnExit ) );
	}
}