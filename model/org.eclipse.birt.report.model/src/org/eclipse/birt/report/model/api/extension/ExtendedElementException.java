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

package org.eclipse.birt.report.model.api.extension;

import java.util.ResourceBundle;

import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * Base class for all peer-provided exceptions. The easiest implementation is to
 * simply wrap the specialized peer implementation inside one of these
 * exceptions.
 */

public class ExtendedElementException extends SemanticException
{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */

	private static final long serialVersionUID = 1L;	
	
	/**
	 * Constructs a new model exception with no cause object.
	 * 
	 * @param pluginId
	 *            Returns the unique identifier of the plug-in associated with
	 *            this exception
	 * @param errorCode
	 *            used to retrieve a piece of externalized message displayed to
	 *            end user.
	 * @param bundle
	 *            the resourceBundle used to translate the message.
	 */

	public ExtendedElementException( String pluginId, String errorCode,
			ResourceBundle bundle )
	{
		super( pluginId, errorCode, bundle );
	}

	/**
	 * Constructs a new model exception.
	 * 
	 * @param pluginId
	 *            Returns the unique identifier of the plug-in associated with
	 *            this exception
	 * @param errorCode
	 *            used to retrieve a piece of externalized message displayed to
	 *            end user.
	 * @param bundle
	 *            the resourceBundle used to translate the message.
	 * @param cause
	 *            the nested exception
	 */

	public ExtendedElementException( String pluginId, String errorCode,
			ResourceBundle bundle, Throwable cause )
	{
		super( pluginId, errorCode, bundle, cause );
	}

	/**
	 * Constructs a new model exception.
	 * 
	 * @param pluginId
	 *            Returns the unique identifier of the plug-in associated with
	 *            this exception
	 * @param errorCode
	 *            used to retrieve a piece of externalized message displayed to
	 *            end user.
	 * @param bundle
	 *            the resourceBundle used to translate the message.
	 * @param args
	 *            string arguments used to format error messages
	 * @param cause
	 *            the nested exception
	 */

	public ExtendedElementException( String pluginId, String errorCode, Object[] args,
			ResourceBundle bundle, Throwable cause )
	{
		super( pluginId, errorCode, args, bundle, cause );
	}

	/**
	 * Constructs a new model exception.
	 * 
	 * @param pluginId
	 *            Returns the unique identifier of the plug-in associated with
	 *            this exception
	 * @param errorCode
	 *            used to retrieve a piece of externalized message displayed to
	 *            end user.
	 * @param bundle
	 *            the resourceBundle used to translate the message.
	 * @param cause
	 *            the nested exception
	 * @param arg0
	 *            first argument used to format error messages
	 */

	public ExtendedElementException( String pluginId, String errorCode, Object arg0,
			ResourceBundle bundle, Throwable cause )
	{
		super( pluginId, errorCode, arg0, bundle, cause );
	}

	/**
	 * Constructs a new model exception.
	 * 
	 * @param pluginId
	 *            Returns the unique identifier of the plug-in associated with
	 *            this exception
	 * @param errorCode
	 *            used to retrieve a piece of externalized message displayed to
	 *            end user.
	 * @param bundle
	 *            the resourceBundle used to translate the message.
	 * @param args
	 *            string arguments used to format error messages
	 */

	public ExtendedElementException( String pluginId, String errorCode, Object[] args,
			ResourceBundle bundle )
	{
		super( pluginId, errorCode, args, bundle );
	}

	/**
	 * Constructs a new model exception.
	 * 
	 * @param pluginId
	 *            Returns the unique identifier of the plug-in associated with
	 *            this exception
	 * @param errorCode
	 *            used to retrieve a piece of externalized message displayed to
	 *            end user.
	 * @param bundle
	 *            the resourceBundle used to translate the message.
	 * @param arg0
	 *            first argument used to format error messages
	 */

	public ExtendedElementException( String pluginId, String errorCode, Object arg0,
			ResourceBundle bundle )
	{
		super( pluginId, errorCode, arg0, bundle );
	}

	/**
	 * Constructs a new model exception.
	 * 
	 * @param pluginId
	 *            Returns the unique identifier of the plug-in associated with
	 *            this exception
	 * @param errorCode
	 *            used to retrieve a piece of externalized message displayed to
	 *            end user.
	 * @param cause
	 *            the nested exception
	 * @param args
	 *            string arguments used to format error messages
	 */
	
	public ExtendedElementException( String pluginId, String errorCode, Object[] args,
			Throwable cause )
	{
		super( pluginId, errorCode, args, cause );
	}
}