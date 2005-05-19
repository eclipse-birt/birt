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

import java.io.FileNotFoundException;
import java.net.UnknownHostException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.nls.Messages;

/**
 * The subclass which extends from BirtException used for GUI to wrap expected
 * internal exception
 */

public class GUIException extends BirtException
{

	private static final String MSG_FILE_NOT_FOUND_PREFIX = Messages.getString( "ExceptionHandler.Title.FileNotFound" ); //$NON-NLS-1$

	private static final String MSG_UNKNOWN_HOST = Messages.getString( "ExceptionHandler.Message.UnknownHost" ); //$NON-NLS-1$

	/**
	 * Creates a new instance of GUI exception with the specified error code
	 * 
	 * @param errorCode
	 *            the error code of the exception
	 * @param cause
	 *            the cause which invoked the exception
	 */
	public GUIException( String errorCode, Throwable cause )
	{
		super( errorCode, null );
		initCause( cause );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */
	public String getLocalizedMessage( )
	{
		String message = getCause( ).getLocalizedMessage( );
		if ( getCause( ) instanceof UnknownHostException )
		{
			message = MSG_UNKNOWN_HOST + message;
		}
		else if ( getCause( ) instanceof FileNotFoundException )
		{
			message = MSG_FILE_NOT_FOUND_PREFIX + ":" + message; //$NON-NLS-1$
		}
		return message;
	}
}
