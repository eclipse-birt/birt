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

import org.eclipse.birt.core.exception.BirtException;

/**
 * Implementation of BirtException in model project.
 */

public class ModelException extends BirtException
{

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
}