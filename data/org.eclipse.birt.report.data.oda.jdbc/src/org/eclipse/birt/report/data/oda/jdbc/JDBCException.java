/*******************************************************************************
 * Copyright (c) 2004,2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc;

import java.util.Locale;
import java.util.logging.Logger;
import java.sql.SQLException;

import org.eclipse.birt.report.data.oda.i18n.JdbcResourceHandle;
import org.eclipse.birt.data.oda.OdaException;

/**
 * JDBCException is thrown when a JDBC call results in a java.sql.SQLException
 * being thrown. Error code and SQLState are copied from the SQLException, and
 * the caught SQLException is set as the initCause of the new exception.
 * 
 */
public class JDBCException extends OdaException
{

	/** Error code for all JDBCException instances. */
	public final static int ERROR_JDBC = 101;

	private static Logger logger = Logger.getLogger( Connection.class.getName( ) );

	private String errorCode;
	private Object argv[];
	private static JdbcResourceHandle resourceHandle = new JdbcResourceHandle( Locale.getDefault( ) );

	/**
	 * 
	 * @param errorCode
	 * @param cause
	 */
	public JDBCException( String errorCode, SQLException cause )
	{
		super( "A JDBC Exception occured: "
				+ cause.getLocalizedMessage( ), cause
				.getSQLState( ), ERROR_JDBC );
		initCause( cause );
		this.errorCode = errorCode;
	}

	/**
	 * 
	 * @param errorCode
	 * @param cause
	 * @param argv
	 */
	public JDBCException( String errorCode, SQLException cause, Object argv )
	{
		super( "A JDBC Exception occured: "
				+ cause.getLocalizedMessage( ), cause
				.getSQLState( ), ERROR_JDBC );
		initCause( cause );
		this.errorCode = errorCode;
		this.argv = new Object[]{
			argv
		};
	}

	/**
	 * 
	 * @param errorCode
	 * @param cause
	 * @param argv
	 */
	public JDBCException( String errorCode, SQLException cause, Object argv[] )
	{
		super( "A JDBC Exception occured: "
				+ cause.getLocalizedMessage( ), cause
				.getSQLState( ), ERROR_JDBC );
		initCause( cause );
		this.errorCode = errorCode;
		this.argv = argv;
	}

	/*
	 * @see java.lang.Throwable#getMessage()
	 */
	public String getMessage( )
	{
		if ( argv == null )
		{
			return resourceHandle.getMessage( errorCode );
		}
		else
		{
			return resourceHandle.getMessage( errorCode, argv );
		}
	}

}