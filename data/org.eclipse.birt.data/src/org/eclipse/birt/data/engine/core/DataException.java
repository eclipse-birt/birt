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

package org.eclipse.birt.data.engine.core;

import java.util.Locale;
import java.util.ResourceBundle;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * Implementation of BirtException in DtE project.
 */

public class DataException extends BirtException
{
	/** static ResourceHandle */
	private static ResourceBundle resourceBundle = 
		new DataResourceHandle( Locale.getDefault( ) ).getResourceBundle();
	
	/** pluginId, probably this value should be obtained externally */
	private final static String _pluginId = "org.eclipse.birt.data";
	
	/** serialVersionUID */
	private static final long serialVersionUID = 8571109940669957243L;
	
	/*
	 * @see BirtException(errorCode)
	 */
	public DataException( String errorCode )
	{
		super( _pluginId, errorCode, resourceBundle );
	}
	
	/**
	 * Support provided additional parameter
	 * @param errorCode
	 * @param argv
	 */
	public DataException( String errorCode, Object argv )
	{
		super( _pluginId, errorCode, argv, resourceBundle );
	}
	
	/**
	 * Support provided additional parameter
	 * @param errorCode
	 * @param argv[]
	 */
	public DataException( String errorCode, Object argv[] )
	{
		super( _pluginId, errorCode, argv, resourceBundle );
	}
    
    /*
     * @see BirtException(message, errorCode)
     */
    public DataException( String errorCode, Throwable cause )
    {
    	super( _pluginId, errorCode, resourceBundle, cause );
    }
    
    public DataException( String errorCode, Throwable cause, Object argv )
    {
    	super( _pluginId, errorCode, argv, resourceBundle, cause);
    }
    
    public DataException( String errorCode, Throwable cause, Object argv[] )
    {
    	super( _pluginId, errorCode, argv, resourceBundle, cause );
    }
    /*
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	public String getLocalizedMessage( )
	{
		return getMessage( );
	}
    
	/*
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage( )
	{
		String msg = super.getMessage();
		
		// Dte frequently wraps exceptions
		// Concatenate error from initCause if available
		if ( this.getCause() != null )
		{
			String extraMsg = this.getCause().getLocalizedMessage();
			if ( extraMsg != null && extraMsg.length() > 0 )
				msg += "\n" + extraMsg; 
		}
		return msg;
	}
    
	
	/**
	 * Wraps a BirtException in a DataException
	 */
	public static DataException wrap( BirtException e )
	{
		if ( e instanceof DataException )
			return (DataException) e;
		return new DataException( ResourceConstants.WRAPPED_BIRT_EXCEPTION,  
				e, e.getMessage() );
	}
}
