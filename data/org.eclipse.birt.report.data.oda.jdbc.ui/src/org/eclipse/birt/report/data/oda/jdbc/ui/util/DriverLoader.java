/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;

public final class DriverLoader
{

	private DriverLoader( )
	{
	}

	public static Connection getConnection( String driverClassName,
			String classPath, String connectionString, String userId,
			String password ) throws SQLException
	{
		try
		{
			// ASSUMPTION
			// All the directories in the driverClassName are delimited by the
			// ";" character

			ArrayList classPathEntries = null;
			if ( classPath != null )
			{
				classPathEntries = new ArrayList( );
				StringTokenizer st = new StringTokenizer( classPath, ";" ); //$NON-NLS-1$
				while ( st.hasMoreTokens( ) )
				{
					 classPathEntries.add( new URL( "file", //$NON-NLS-1$
	                        null, -1, escapeCharacters( st.nextToken( ) ) ) );
				}
			}

			URL[] urlClasspath = new URL[classPathEntries.size( )];
			
			for ( int i = 0; i < classPathEntries.size( ); i++ )
			{
				urlClasspath[i] = (URL) classPathEntries.get( i );
			}

			URLClassLoader ucl = new URLClassLoader( urlClasspath );
			if ( driverClassName != null && ucl != null )
			{
                Connection connection = null;
                try
                {
                    connection = DriverManager.getConnection( connectionString,
                            userId,
                            password );
                }
                catch(Exception ex)
                {
                    //Just ignore this
                    //this will happen if the driver is not registered
                }
                Driver d = null;
                if(connection == null)
                {
                    //if the connection is null
                    //we try instantiating the driver
                    d = (Driver) Class.forName( driverClassName, true, ucl )
                    .newInstance( );
                    
                    try
                    {
                        //Now get the connection again
                        connection = DriverManager.getConnection( connectionString,
                                userId,
                                password );
                    }
                    catch(Exception ex)
                    {
                        //Ignore this exception as well
                        //We will have to register the driver and try
                    }
                    
                }
                
                if(connection == null)
                {
                    DriverManager.registerDriver( new DriverExt( d ) );
                    connection = DriverManager.getConnection( connectionString,
                            userId,
                            password );
                }
                
				return connection;
			}
		}
		catch ( MalformedURLException e )
		{
			ExceptionHandler.handle( e );
		}
        catch (InstantiationException e)
        {
            ExceptionHandler.handle( e );
        }
        catch (IllegalAccessException e)
        {
            ExceptionHandler.handle( e );
        }
        catch (ClassNotFoundException e)
        {
            ExceptionHandler.handle( e );
        }
		return null;
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

}

