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

package org.eclipse.birt.report.model.metadata;

/**
 * Indicates the error when loading extensions.
 */

public class ExtensionException extends MetaDataException
{

	/**
	 * Error code indicating the extension point is not found.
	 */

	public static final String EXTENSION_POINT_NOT_FOUND = "EXTENSION_POINT_NOT_FOUND"; //$NON-NLS-1$

	/**
	 * Error code indicating the instance can not be created.
	 */

	public static final String FAILED_TO_CREATE_INSTANCE = "FAILED_TO_CREATE_INSTANCE"; //$NON-NLS-1$

	/**
	 * Error code indicating the value is required.
	 */

	public static final String VALUE_REQUIRED = "VALUE_REQUIRED"; //$NON-NLS-1$

	/**
	 * Constructs an extension exception with error code.
	 * 
	 * @param params
	 *            the parameters for building error message
	 * @param errorCode
	 *            the error code
	 */

	public ExtensionException( String[] params, String errorCode )
	{
		super( params, errorCode );
	}

}