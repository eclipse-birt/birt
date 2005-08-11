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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.DesignElement;

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
	 * Constructor.
	 * 
	 * @param errCode
	 *            the error code
	 *  
	 */

	public ExtendedElementException( String errCode )
	{
		super( errCode );
	}

	/**
	 * Constructor.
	 * 
	 * @param element
	 *            the extended element which has errors
	 * @param errCode
	 *            the error code
	 */

	public ExtendedElementException( DesignElement element, String errCode )
	{
		super( element, errCode );
	}

	/**
	 * Constructor.
	 * 
	 * @param element
	 *            the extended element which has errors
	 * @param errCode
	 *            the error code
	 * @param cause
	 *            the nested exception
	 */

	public ExtendedElementException( DesignElement element, String errCode,
			Throwable cause )
	{
		super( element, errCode, cause );
	}

	/**
	 * Constructor.
	 * 
	 * @param element
	 *            the extended element which has errors
	 * @param args
	 *            value array used for error message
	 * @param errCode
	 *            the error code
	 */

	public ExtendedElementException( DesignElement element, String[] args,
			String errCode )
	{
		super( element, args, errCode );
	}

	/**
	 * Constructor.
	 * 
	 * @param element
	 *            the extended element which has errors
	 * @param args
	 *            value array used for error message
	 * @param errCode
	 *            the error code
	 * @param cause
	 *            the nested exception
	 */

	public ExtendedElementException( DesignElement element, String[] args,
			String errCode, Throwable cause )
	{
		super( element, args, errCode, cause );
	}
}