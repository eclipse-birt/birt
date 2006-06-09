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

package org.eclipse.birt.report.data.adapter.api;

import java.util.ResourceBundle;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.i18n.AdapterResourceHandle;

/**
 * Implementation of BirtException in DtE project.
 */
public class AdapterException extends BirtException
{
	/** static ResourceHandle */
	private static ResourceBundle resourceBundle = AdapterResourceHandle.getInstance( )
			.getResourceBundle( );
	
	/** pluginId, probably this value should be obtained externally */
	private final static String _pluginId = "org.eclipse.birt.report.data.adapter";
	
	/** serialVersionUID */
	private static final long serialVersionUID = 8571109940669957243L;
	
	/*
	 * @see BirtException(errorCode)
	 */
	public AdapterException( String errorCode )
	{
		super( _pluginId, errorCode, resourceBundle );
	}
	
	/**
	 * Support provided additional parameter
	 * @param errorCode
	 * @param argv
	 */
	public AdapterException( String errorCode, Object argv )
	{
		super( _pluginId, errorCode, argv, resourceBundle );
	}
	
	/**
	 * Support provided additional parameter
	 * @param errorCode
	 * @param argv[]
	 */
	public AdapterException( String errorCode, Object argv[] )
	{
		super( _pluginId, errorCode, argv, resourceBundle );
	}
    
    /*
     * @see BirtException(message, errorCode)
     */
    public AdapterException( String errorCode, Throwable cause )
    {
    	super( _pluginId, errorCode, resourceBundle, cause );
    }
    
    public AdapterException( String errorCode, Throwable cause, Object argv )
    {
    	super( _pluginId, errorCode, argv, resourceBundle, cause);
    }
    
    public AdapterException( String errorCode, Throwable cause, Object argv[] )
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
	
}
