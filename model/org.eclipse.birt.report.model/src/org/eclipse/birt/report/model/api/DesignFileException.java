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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ThreadResources;

/**
 * Signals that a fatal error occurred when opening a design file. It includes
 * the error list. Each item in the list is an instance of
 * <code>ErrorDetail</code>.
 * <p>
 * Four types of error are defined:
 * <dl>
 * <dn><code>FILE_NOT_FOUND</code></dn>
 * <dd>Design file is not found. <dn><code>INVALID_XML</code></dn>
 * <dd>Design file is not a valid xml file. <dn><code>SYNTAX_ERROR</code></dn>
 * <dd>Design file has something conflicting MetaData definition. <dn>
 * <code>SEMANTIC_ERROR</code></dn>
 * <dd>Design file is opened with semantic error.
 * <ul></ul>
 * 
 * @see ErrorDetail
 */

public class DesignFileException extends BirtException
{

	/**
	 * The list containing errors encountered when opening the design file.
	 */

	private List errorList = new ArrayList( );

	/**
	 * The file name with the error.
	 */

	protected String fileName = null;

	/**
	 * Exception thrown by SAX.
	 */

	protected Exception e = null;

	/**
	 * The input file was not found.
	 */

	public static final String DESIGN_EXCEPTION_FILE_NOT_FOUND = MessageConstants.DESIGN_FILE_EXCEPTION_FILE_NOT_FOUND;

	/**
	 * The syntax error, when design file doesn't conform metadata definition.
	 */

	public static final String DESIGN_EXCEPTION_SYNTAX_ERROR = MessageConstants.DESIGN_FILE_EXCEPTION_SYNTAX_ERROR;

	/**
	 * The semantic error, when element doesn't conform semantic check.
	 */

	public static final String DESIGN_EXCEPTION_SEMANTIC_ERROR = MessageConstants.DESIGN_FILE_EXCEPTION_SEMANTIC_ERROR;

	/**
	 * The semantic warning, when element doesn't conform semantic check.
	 * However, the level of this error is warning.
	 */

	public static final String DESIGN_EXCEPTION_SEMANTIC_WARNING = MessageConstants.DESIGN_FILE_EXCEPTION_SEMANTIC_WARNING;

	/**
	 * Other exceptions thrown by SAX. Generally, it's caused when design file
	 * is not a valid xml file.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_XML = MessageConstants.DESIGN_FILE_EXCEPTION_INVALID_XML;

	/**
	 * Constructs a <code>DesignFileException</code> with the given design
	 * filename and the specified cause. It is for the exception thrown by SAX.
	 * 
	 * @param fileName
	 *            design file name.
	 * @param e
	 *            exception to wrap.
	 */

	public DesignFileException( String fileName, Exception e )
	{
		super( DESIGN_EXCEPTION_INVALID_XML, null );
		this.fileName = fileName;
		this.e = e;
		errorList.add( new ErrorDetail( e ) );
	}

	/**
	 * Constructs a <code>DesignFileException</code> with the given design
	 * filename. Used when design file is not found.
	 * 
	 * @param fileName
	 *            design file name.
	 */

	public DesignFileException( String fileName )
	{
		super( DESIGN_EXCEPTION_FILE_NOT_FOUND, null );
		this.fileName = fileName;
	}

	/**
	 * Constructs a <code>DesignFileException</code> with the given design
	 * filename and a list of errors. Used when syntax error is found when
	 * parsing.
	 * 
	 * @param fileName
	 *            design file name.
	 * @param errList
	 *            exception list, each of them is the syntax error.
	 */

	public DesignFileException( String fileName, List errList )
	{
		super( DESIGN_EXCEPTION_SYNTAX_ERROR, null );
		this.fileName = fileName;

		Iterator iter = errList.iterator( );
		while ( iter.hasNext( ) )
		{
			Exception e = (Exception) iter.next( );

			this.errorList.add( new ErrorDetail( e ) );
		}
	}

	/**
	 * Constructs a <code>DesignFileException</code> with the given design
	 * filename, a list of errors and the new exception to add. Used when syntax
	 * error is found when parsing.
	 * 
	 * 
	 * @param fileName
	 *            design file name.
	 * @param errList
	 *            exception list, each of which is the syntax error.
	 * @param ex
	 *            the exception to add
	 *  
	 */

	public DesignFileException( String fileName, List errList, Exception ex )
	{
		super( DESIGN_EXCEPTION_INVALID_XML, null );
		this.fileName = fileName;

		Iterator iter = errList.iterator( );
		while ( iter.hasNext( ) )
		{
			Exception e = (Exception) iter.next( );

			this.errorList.add( new ErrorDetail( e ) );
		}
		this.errorList.add( new ErrorDetail( ex ) );
	}
	
	/**
	 * Returns the error list. Each item in the list is an instance of
	 * <code>ErrorDetail</code>.
	 * 
	 * @return the error list.
	 */

	public List getErrorList( )
	{
		return errorList;
	}

	/**
	 * Returns the design file name.
	 * 
	 * @return the design file name.
	 */

	public String getFileName( )
	{
		return fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	public String getLocalizedMessage( )
	{
		if ( sResourceKey == null )
			return ""; //$NON-NLS-1$

		if ( sResourceKey == DESIGN_EXCEPTION_FILE_NOT_FOUND )
		{
			return ThreadResources.getMessage( sResourceKey,
					new String[]{fileName} );
		}

		return ThreadResources.getMessage( sResourceKey );

	}
	
	/* (non-Javadoc)
	 * @see java.lang.Throwable#getMessage()
	 */
	
	public String getMessage( )
	{
		return getLocalizedMessage( );
	}

	/**
	 * Returns a string representation of the exception. If the exception type
	 * is SYNTAX_ERROR or INVALID_XML, this method checks all errors in the
	 * <code>errorList</code> and assemble them into a string. The return
	 * string is assembled in the ways:
	 * 
	 * <table border="1">
	 * <th width="20%">Error Type</th>
	 * <th width="40%">Message</th>
	 * 
	 * <tr>
	 * <td>FILE_NOT_FOUND</td>
	 * <td><code>[errorType]</code>- The design file ([fileName]) is not
	 * found.</td>
	 * </tr>
	 * 
	 * <tr>
	 * <td>SYNTAX_ERROR and INVALID_XML</td>
	 * <td><code>[errorType]</code>- [numOfErrors] errors found. <br>
	 * 1.) [detail messages.] <br>
	 * 2.) [detail messages.] <br>
	 * ... <br>
	 * </td>
	 * </tr>
	 * 
	 * <tr>
	 * <td>SEMANTIC_ERROR</td>
	 * <td>Impossible to occur.</td>
	 * </tr>
	 * 
	 * </table>
	 * 
	 * Note output message are locale independent. ONLY for debugging, not
	 * user-visible. Debugging messages are defined to be in English.
	 * 
	 * @see java.lang.Object#toString()
	 * @see ErrorDetail#toString()
	 * @see #getLocalizedMessage()
	 *  
	 */

	public String toString( )
	{
		StringBuffer sb = new StringBuffer( );

		sb.append( sResourceKey );
		sb.append( " - " ); //$NON-NLS-1$
		if ( sResourceKey == DESIGN_EXCEPTION_FILE_NOT_FOUND )
		{
			sb.append( "The design file (" ); //$NON-NLS-1$
			sb.append( fileName );
			sb.append( ") is not found ! \n" ); //$NON-NLS-1$
		}
		else if ( sResourceKey == DESIGN_EXCEPTION_SYNTAX_ERROR || sResourceKey == DESIGN_EXCEPTION_INVALID_XML )
		{
			if ( errorList != null )
			{
				sb.append( errorList.size( ) );
				sb.append( " errors found! \n" ); //$NON-NLS-1$

				int i = 1;
				Iterator iter = errorList.iterator( );
				while ( iter.hasNext( ) )
				{
					ErrorDetail e = (ErrorDetail) iter.next( );

					sb.append( i++ );
					sb.append( ".) " ); //$NON-NLS-1$
					sb.append( e );
					sb.append( "\n" ); //$NON-NLS-1$
				}
			}
		}
		else
		{
			// SEMANTIC_ERROR does not occurs here.

			assert false;
			return super.toString( );
		}

		return sb.toString( );
	}
}