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

package org.eclipse.birt.report.model.api;

import java.util.ResourceBundle;

import org.eclipse.birt.core.exception.BirtException;

/**
 * Implementation of BirtException in model project.
 */

public abstract class ModelException extends BirtException
{

	private static final long serialVersionUID = 1L;
	/**
	 * The plugin id of all the model exceptions.
	 */

	public static final String PLUGIN_ID = "org.eclipse.birt.report.model"; //$NON-NLS-1$

	/**
	 * Constructs a new model exception with the error code.
	 * 
	 * @param errCode
	 *            used to retrieve a piece of externalized message displayed to
	 *            end user
	 */

	public ModelException( String errCode )
	{
		super( PLUGIN_ID, errCode, null );
	}

	/**
	 * Constructs a new model exception with the error code, string arguments
	 * used to format error messages and the nested exception.
	 * 
	 * @param errCode
	 *            used to retrieve a piece of externalized message displayed to
	 *            end user
	 * @param args
	 *            string arguments used to format error messages
	 * @param cause
	 *            the nested exception
	 */

	public ModelException( String errCode, String[] args, Throwable cause )
	{
		super( PLUGIN_ID, errCode, args, null, cause );
	}

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

	public ModelException( String pluginId, String errorCode,
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

	public ModelException( String pluginId, String errorCode,
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

	public ModelException( String pluginId, String errorCode, Object[] args,
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

	public ModelException( String pluginId, String errorCode, Object arg0,
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

	public ModelException( String pluginId, String errorCode, Object[] args,
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

	public ModelException( String pluginId, String errorCode, Object arg0,
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
	
	public ModelException( String pluginId, String errorCode, Object[] args,
			Throwable cause )
	{
		super( pluginId, errorCode, args, cause );
	}
}