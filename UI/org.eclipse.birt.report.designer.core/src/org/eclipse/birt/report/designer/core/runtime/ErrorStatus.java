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

package org.eclipse.birt.report.designer.core.runtime;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;

/**
 * The multi-status to store the information about error.The class contains the
 * error message and detailed information such as the plug-in where the error
 * happens.
 */

public class ErrorStatus extends MultiStatus
{

	/**
	 * Creates a new instance of Error Status with given reason.
	 * 
	 * @param pluginId
	 *            the unique identifier of the relevant plug-in
	 * @param code
	 *            the plug-in-specific status code
	 * @param reason
	 *            the error reason
	 * @param exception
	 *            a low-level exception, or <code>null</code> if not
	 *            applicable
	 */
	public ErrorStatus( String pluginId, int code, String reason,
			Throwable exception )
	{
		super( pluginId, code, reason, exception );
	}

	/**
	 * Add a warning status with given message
	 * 
	 * @param message
	 *            the status message
	 */
	public void addWarning( String message )
	{
		merge( new Status( IStatus.WARNING,
				getPlugin( ),
				getCode( ),
				message,
				getException( ) ) );
	}

	/**
	 * Add a error status with given message
	 * 
	 * @param message
	 *            the status message
	 */
	public void addError( String message )
	{
		merge( new Status( IStatus.ERROR,
				getPlugin( ),
				getCode( ),
				message,
				getException( ) ) );
	}

	/**
	 * Add an information status with given message
	 * 
	 * @param message
	 *            the status message
	 */
	public void addInformation( String message )
	{
		merge( new Status( IStatus.INFO,
				getPlugin( ),
				getCode( ),
				message,
				getException( ) ) );
	}

	/**
	 * Add cause of error.
	 * @param e
	 */
	public void addCause( Throwable e )
	{
		String message = e.getLocalizedMessage( );
		if ( message == null )
		{
			message = e.getClass( ).getName( );
		}
		merge( new Status( IStatus.ERROR, getPlugin( ), getCode( ), message, e ) );
	}

	/**
	 * Gets the error code
	 * 
	 * @return Returns the error code
	 */
	public int getErrorCode( )
	{
		return getCode( );
	}

	/**
	 * Gets the plug-in provider
	 * 
	 * @return Returns the plug-in provider, or null if the plug-in is not found
	 */
	public String getPluginProvider( )
	{
		Bundle bundle = Platform.getBundle( getPlugin( ) );
		if ( bundle != null )
		{
			return (String) bundle.getHeaders( )
					.get( org.osgi.framework.Constants.BUNDLE_VENDOR );
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IStatus#getSeverity()
	 */
	public int getSeverity( )
	{
		if ( getChildren( ).length == 0 )
		{//Default value
			return IStatus.ERROR;
		}
		return super.getSeverity( );
	}
}