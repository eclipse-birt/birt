/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;

/**
 * TODO
 */
public class JdbcToolKit
{

	// Name of all the configured jdbc drivers
	private static ArrayList jdbcDriverName = null;

	// class path used for obtaining a connection using a jdbc driver
	private static ArrayList jdbcDriverLocations = null;

	private static boolean implementsSQLDriver( Class aClass )
	{
		try
		{
			Class[] interfaces = aClass.getInterfaces( );
			for ( int i = 0; i < interfaces.length; i++ )
			{
				Class anInterface = interfaces[i];
				if ( "java.sql.Driver".equals( anInterface.getName( ) ) ) //$NON-NLS-1$
				{
					return true;
				}
			}
		}
		catch ( Throwable e )
		{
			ExceptionHandler.handle( e );
		}
		return false;
	}

	private static boolean instanceOfSQLDriver( Class aClass )
	{
		Class superClass = aClass;

		try
		{
			if ( implementsSQLDriver( superClass ) )
			{

				return true;
			}

			while ( ( superClass = superClass.getSuperclass( ) ) != null )
			{
				if ( superClass.isInterface( ) == true
						&& "java.sql.Driver".equals( superClass.getName( ) ) ) //$NON-NLS-1$
				{
					return true;
				}
				else if ( implementsSQLDriver( superClass ) )
				{
					return true;
				}
			}
		}
		catch ( Throwable e )
		{
			e.printStackTrace( );
		}
		return false;
	}

	private static String[] getJarFileEntries( File jarFile )
	{
		ArrayList jarEntries = new ArrayList( );
		try
		{
			ZipFile zf = new ZipFile( jarFile );
			Enumeration e = zf.entries( );
			while ( e.hasMoreElements( ) )
			{
				ZipEntry ze = (ZipEntry) e.nextElement( );
				if ( !ze.isDirectory( ) )
				{
					jarEntries.add( ze.getName( ) );
				}
			}
			zf.close( );
		}
		catch ( IOException e1 )
		{
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}
		return (String[]) jarEntries.toArray( new String[jarEntries.size( )] );
	}

	/*
	 * Returns a List jdbc Drivers. The Drivers are searched from predefined
	 * directories in the DTE plug-in. Currently it is assumed that the jdbc
	 * drivers are specified in the odacondifg.xml file in the "jdbc1"
	 * sub-driectory within the "drivers" directory of the DTE plug-in
	 */
	public static ArrayList getJdbcDriverNames( String driverName )
	{
		if ( jdbcDriverName == null )
		{
			jdbcDriverName = new ArrayList( );

			ArrayList abstractDriverClassList = new ArrayList( );

			try
			{
				// Adding the odbc-jdbc driver
				jdbcDriverName.add( "sun.jdbc.odbc.JdbcOdbcDriver" ); //$NON-NLS-1$
			}
			catch ( Exception e )
			{

			}

			ArrayList driverFiles = null;
			try
			{
				JdbcDriverConfigUtil driverConfigUtil = new JdbcDriverConfigUtil( driverName );
				driverFiles = driverConfigUtil.getDriverFiles( );
			}
			catch ( Exception e )
			{
				ExceptionHandler.handle( e );
			}

			Iterator iterator = driverFiles.iterator( );

			Class aClass = null;

			jdbcDriverLocations = new ArrayList( );

			URL[] urlList = new URL[driverFiles.size( )];

			// Create a URL Array for the class loader to use
			for ( int i = 0; i < driverFiles.size( ); i++ )
			{
				File driverFile = (File) driverFiles.get( i );

				try
				{
					urlList[i] = new URL( "file:///" //$NON-NLS-1$
							+ driverFile.getAbsolutePath( ) );
					jdbcDriverLocations.add( driverFile.getAbsolutePath( ) );
				}
				catch ( MalformedURLException e )
				{
					ExceptionHandler.handle( e );
				}

			}

			URLClassLoader urlClassLoader = new URLClassLoader( urlList,
					ClassLoader.getSystemClassLoader( ) );
			while ( iterator.hasNext( ) )
			{
				File driverFile = (File) iterator.next( );

				if ( driverFile.getName( ).endsWith( ".jar" ) ) //$NON-NLS-1$
				{
					try
					{
						String[] resourceNames = getJarFileEntries( driverFile );
						for ( int j = 0; j < resourceNames.length; j++ )
						{
							String resourceName = resourceNames[j];
							if ( resourceName.endsWith( ".class" ) ) //$NON-NLS-1$
							{
								try
								{
									resourceName = ( resourceName.replaceAll( "/", //$NON-NLS-1$
											"." ) ).substring( 0, //$NON-NLS-1$
											resourceName.length( ) - 6 );
									aClass = urlClassLoader.loadClass( resourceName );
								}
								catch ( Throwable e )
								{

								}

								if ( aClass != null )
								{
									if ( instanceOfSQLDriver( aClass ) )
									{

										// Do not add it, if it is a Abstract
										// class
										int modifier = aClass.getModifiers( );
										boolean isAbstract = Modifier.isAbstract( modifier );

										if ( isAbstract )
										{

											Class storedClass = aClass;
											abstractDriverClassList.add( storedClass );
										}
										if ( !isAbstract )
										{
											jdbcDriverName.add( aClass.getName( ) );
										}
									}
								}
							}
						}
					}
					catch ( Throwable e )
					{

					}

				}
			}
		}
		return jdbcDriverName;
	}

	public static String getJdbcDriverClassPath( String driverName )
	{
		String driverLocation = ""; //$NON-NLS-1$
		if ( jdbcDriverLocations == null )
		{
			getJdbcDriverNames( driverName );
		}

		// Generate a class path from the entries

		if ( jdbcDriverLocations != null )
		{
			Iterator itor = jdbcDriverLocations.iterator( );
			while ( itor.hasNext( ) )
			{
				driverLocation = driverLocation
						+ (String) ( itor.next( ) ) + ";"; //$NON-NLS-1$
			}
		}

		return driverLocation;
	}
}