/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.util;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.StringCharacterIterator;
import java.util.Enumeration;

import org.eclipse.birt.report.data.oda.jdbc.JDBCDriverManager;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;

public final class DriverLoader
{
	private DriverLoader( )
	{
	}

	public static Connection getConnection( String driverClassName,
			String connectionString, String userId,
			String password ) throws SQLException
	{
		try
		{
			return JDBCDriverManager.getInstance().getConnection( 
				driverClassName, connectionString, userId, password);
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle(e);
			return null;
		}
	}

	static String escapeCharacters( String value )
	{
		final StringCharacterIterator iterator = new StringCharacterIterator( value );
		char character = iterator.current( );
		final StringBuffer result = new StringBuffer( );

		while ( character != StringCharacterIterator.DONE )
		{
			if ( character == '\\' )
			{
				result.append( "\\" ); //$NON-NLS-1$
			}
			else
			{
				//the char is not a special one
				//add it to the result as is
				result.append( character );
			}
			character = iterator.next( );
		}
		return result.toString( );

	}
	
	/**
	 * The method which test whether the give connection properties can be used to create a connection
	 * @param driverClassName the name of driver class
	 * @param connectionString the connection URL
	 * @param userId the user id
	 * @param password the pass word
	 * @return boolean whether could the connection being created
	 */
	public static boolean testConnection( String driverClassName,
			String connectionString, String userId,
			String password ) 
	{
		try
		{
			Connection conn = JDBCDriverManager.getInstance().getConnection( 
					driverClassName, connectionString, userId, password);
			if ( conn == null )
			{
				return false;
			}
			else
			{
				
				if ( !conn.isClosed( ) )
					conn.close( );
			}		
			Enumeration enumeration = DriverManager.getDrivers( );
			while ( enumeration.hasMoreElements( ) )
			{
				Driver driver = (Driver) enumeration.nextElement( );
					if ( driver.acceptsURL( connectionString ) )
					{
						// The driver might be a wrapped driver. The toString() method of a wrapped driver is overriden 
						// so that the name of driver being wrapped is returned.
						if ( driver.toString( ).equalsIgnoreCase( driverClassName )
								|| driver.getClass( )
										.getName( )
										.equalsIgnoreCase( driverClassName ) )
							return true;
					}
			}
		
		}
		catch ( Exception e)
		{
		}
		return false;
	}
}

