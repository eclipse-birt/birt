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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;

/**
 * Implementation of BirtException in DtE project.
 * Currently BirtException's methods are all overrided to avoid
 * mistake in test, but this way will be changed to not overridding
 * its main methods like getErrorCode and getLocalizedMessage.
 */

public class DataException extends BirtException
{
	private Object argv[];

	/** static ResourceHandle */
	private static DataResourceHandle resourceHandle = new DataResourceHandle( Locale.getDefault( ) );
	
	/*
	 * @see BirtException(errorCode)
	 */
	public DataException( String errorCode )
	{
		super( errorCode, resourceHandle.getResourceBundle( ) );
	}
	
	/**
	 * Support provided additional parameter
	 * @param errorCode
	 * @param argv
	 */
	public DataException( String errorCode, Object argv )
	{
		super( errorCode, argv, resourceHandle.getResourceBundle( ) );
		this.argv = new Object[]{
			argv
		};
	}
	
	/**
	 * Support provided additional parameter
	 * @param errorCode
	 * @param argv[]
	 */
	public DataException( String errorCode, Object argv[] )
	{
		super( errorCode, argv, resourceHandle.getResourceBundle( ) );
		this.argv = argv;
	}
    
    /*
     * @see BirtException(message, errorCode)
     */
    public DataException( String errorCode, Throwable cause )
    {
    	super( errorCode, resourceHandle.getResourceBundle( ), cause );
    }
    
    public DataException( String errorCode, Throwable cause, Object argv )
    {
    	super( errorCode, argv, resourceHandle.getResourceBundle( ), cause);
		this.argv = new Object[]{
				argv
			};
    }
    
    public DataException( String errorCode, Throwable cause, Object argv[] )
    {
    	super( errorCode, argv, resourceHandle.getResourceBundle( ), cause );
    	this.argv = argv;
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
		String msg;
		if ( argv == null )
		{
			msg = resourceHandle.getMessage( getErrorCode() );
		}
		else
		{
			msg = resourceHandle.getMessage( getErrorCode(), argv );
		}
		
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
