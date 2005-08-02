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

package org.eclipse.birt.report.model.api.activity;

import org.eclipse.birt.report.model.api.ModelException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Represents exceptions encountered during parsing the xml file, it will
 * include a reference to the element which causes the error.
 *  
 */

public class SemanticException extends ModelException
{
	/**
	 * The element with semantic error.
	 */

	protected DesignElement element;

	/**
	 * Constructor.
	 * 
	 * @param errCode
	 *            the error code
	 *  
	 */

	protected SemanticException( String errCode )
	{
		super( errCode );
	}

	/**
	 * Constructor.
	 * 
	 * @param element
	 *            the element which has errors
	 * @param errCode
	 *            the error code
	 */

	public SemanticException( DesignElement element, String errCode )
	{
		super( errCode );
		this.element = element;
	}

	/**
	 * Constructor.
	 * 
	 * @param element
	 *            the element which has errors
	 * @param errCode
	 *            the error code
	 * @param cause
	 *            the nested exception
	 */
	
	public SemanticException( DesignElement element, String errCode,
			Throwable cause )
	{
		super( errCode, null, cause );
		this.element = element;
	}

	/**
	 * Constructor.
	 * 
	 * @param element
	 *            the element which has errors
	 * @param values
	 *            value array used for error message
	 * @param errCode
	 *            the error code
	 */

	public SemanticException( DesignElement element, String[] values,
			String errCode )
	{
		super( errCode, values, null );
		this.element = element;
	}

	/**
	 * Returns the element having semantic error.
	 * 
	 * @return the element having semantic error
	 */

	public DesignElement getElement( )
	{
		return element;
	}

	/**
	 * Returns the element name if it exists.
	 * 
	 * @param element
	 *            the design element
	 * @return the element name if it exists. Otherwise, return empty string.
	 */

	protected static String getElementName( DesignElement element )
	{
		String name = element.getElementName( );

		if ( !StringUtil.isBlank( element.getName( ) ) )
		{
			name = element.getElementName( ) + " \"" + element.getName( ) //$NON-NLS-1$
					+ "\""; //$NON-NLS-1$
		}
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */

	public String getMessage( )
	{
		return getLocalizedMessage( );
	}
}