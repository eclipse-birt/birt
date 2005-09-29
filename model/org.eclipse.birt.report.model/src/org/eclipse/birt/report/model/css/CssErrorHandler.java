/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.css;

import org.w3c.css.sac.*;

/**
 * Implements ErrorHandler to deal with the errors, warnings and fatal errors
 * during the parse of the CSS file.
 */

public class CssErrorHandler implements ErrorHandler
{

	/**
	 * Default constructor.
	 *  
	 */

	public CssErrorHandler( )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.css.sac.ErrorHandler#error(org.w3c.css.sac.CSSParseException)
	 */

	public void error( CSSParseException exception ) throws CSSException
	{
		StringBuffer sb = new StringBuffer( );
		sb.append( exception.getURI( ) ).append( " [" ).append( //$NON-NLS-1$
				exception.getLineNumber( ) ).append( ":" ).append( //$NON-NLS-1$
				exception.getColumnNumber( ) ).append( "] " ).append( //$NON-NLS-1$
				exception.getMessage( ) );
		System.err.println( sb.toString( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.css.sac.ErrorHandler#fatalError(org.w3c.css.sac.CSSParseException)
	 */

	public void fatalError( CSSParseException exception ) throws CSSException
	{
		StringBuffer sb = new StringBuffer( );
		sb.append( exception.getURI( ) ).append( " [" ).append( //$NON-NLS-1$
				exception.getLineNumber( ) ).append( ":" ).append( //$NON-NLS-1$
				exception.getColumnNumber( ) ).append( "] " ).append( //$NON-NLS-1$
				exception.getMessage( ) );
		System.err.println( sb.toString( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.css.sac.ErrorHandler#warning(org.w3c.css.sac.CSSParseException)
	 */

	public void warning( CSSParseException exception ) throws CSSException
	{
		StringBuffer sb = new StringBuffer( );
		sb.append( exception.getURI( ) ).append( " [" ).append( //$NON-NLS-1$
				exception.getLineNumber( ) ).append( ":" ).append( //$NON-NLS-1$
				exception.getColumnNumber( ) ).append( "] " ).append( //$NON-NLS-1$
				exception.getMessage( ) );
		System.err.println( sb.toString( ) );
	}
}