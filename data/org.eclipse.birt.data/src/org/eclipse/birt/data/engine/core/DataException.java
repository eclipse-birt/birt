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
	private String errorCode;
	private Object argv[];

	/** static ResourceHandle */
	private static DataResourceHandle resourceHandle = new DataResourceHandle( Locale.getDefault( ) );
	
	private static String ERROR_CODE_PREFIX = "DATA_EXCEPTION_";
	
	/*
	 * @see BirtException(errorCode)
	 */
	public DataException( String errorCode )
	{
		super( errorCode, resourceHandle.getResourceBundle( ) );
		this.errorCode = errorCode;
	}
	
	/**
	 * Support provided additional parameter
	 * @param errorCode
	 * @param argv
	 */
	public DataException( String errorCode, Object argv )
	{
		super( errorCode, argv, resourceHandle.getResourceBundle( ) );
		this.errorCode = errorCode;
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
		this.errorCode = errorCode;
		this.argv = argv;
	}
    
    /*
     * @see BirtException(message, errorCode)
     */
    public DataException( String errorCode, Throwable cause )
    {
    	super( errorCode, resourceHandle.getResourceBundle( ), cause );
    	this.errorCode = errorCode;
    }
    
    public DataException( String errorCode, Throwable cause, Object argv )
    {
    	super( errorCode, argv, resourceHandle.getResourceBundle( ), cause);
    	this.errorCode = errorCode;
		this.argv = new Object[]{
				argv
			};
    }
    
    public DataException( String errorCode, Throwable cause, Object argv[] )
    {
    	super( errorCode, argv, resourceHandle.getResourceBundle( ), cause );
    	this.errorCode = errorCode;
    	this.argv = argv;
    }
    
    /**
     * @return errrorCode
     */
    public String getErrorCode( )
    {
    	return ERROR_CODE_PREFIX + errorCode;
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
		if ( argv == null )
		{
			return resourceHandle.getMessage( errorCode );
		}
		else
		{
			return resourceHandle.getMessage( errorCode, argv );
		}
	}
	
}
