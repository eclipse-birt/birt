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
import java.sql.Driver;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.birt.report.data.oda.jdbc.ui.dialogs.JdbcDriverManagerDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;

/**
 * TODO
 */
public class JdbcToolKit
{

	// Name of all the configured jdbc drivers
	private static ArrayList jdbcDrivers = null;

	// class path used for obtaining a connection using a jdbc driver
	private static ArrayList jdbcDriverLocations = null;

	private static Class driverClass = Driver.class;

	private static boolean implementsSQLDriver( Class aClass )
	{
		if ( driverClass.isAssignableFrom( aClass ) )
		{
			return true;
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

	/**
	 * Resets cached jdbc driver list to null, force reget the infomation when
	 * required next time.
	 */
	public static void resetJdbcDriverNames( )
	{
		jdbcDrivers = null;
	}

	/*
	 * Returns a List jdbc Drivers. The Drivers are searched from predefined
	 * directories in the DTE plug-in. Currently it is assumed that the jdbc
	 * drivers are specified in the odacondifg.xml file in the "jdbc1"
	 * sub-driectory within the "drivers" directory of the DTE plug-in
	 */
	public static ArrayList getJdbcDriverNames( String driverName )
	{
		if ( jdbcDrivers == null )
		{
			LogInfo( 1000, "Enter getJdbcDriverNames" );
			
			jdbcDrivers = new ArrayList( );
			URL[] classPathURLs = getJdbcDriverClassPathURLS( driverName );
			try
			{
				JDBCDriverInformation info = JDBCDriverInformation.getInstance( "sun.jdbc.odbc.JdbcOdbcDriver",
						classPathURLs );
				// Adding the odbc-jdbc driver
				info.setUrlFormat( "jdbc:odbc:<data source name>" );
				info.setDisplayName( "Sun JDBC-ODBC Bridge Driver" );
				jdbcDrivers.add( info ); //$NON-NLS-1$
			}
			catch ( Exception e )
			{

			}
			JdbcDriverConfigUtil driverConfigUtil = null;

			File[] driverFiles = null;
			try
			{
				driverConfigUtil = new JdbcDriverConfigUtil( driverName );
				driverFiles = driverConfigUtil.getDriverFiles( );
				
				LogInfo( 1111, new String( "FILES number is " + driverFiles.length ) );
				for ( int i = 0; i < driverFiles.length; i++ )
				{
					LogInfo( 2222, driverFiles[i].toString( ) );
				}
			}
			catch ( Exception e )
			{
				ExceptionHandler.handle( e );
			}

			Class aClass = null;

			jdbcDriverLocations = new ArrayList( );

			if ( driverFiles != null )
			{
				URL[] urlList = new URL[driverFiles.length];

				// Create a URL Array for the class loader to use
				for ( int i = 0; i < driverFiles.length; i++ )
				{
					try
					{
						urlList[i] = new URL( "file:///" //$NON-NLS-1$
								+ driverFiles[i].getAbsolutePath( ) );
						jdbcDriverLocations.add( driverFiles[i].getAbsolutePath( ) );
					}
					catch ( MalformedURLException e )
					{
						ExceptionHandler.handle( e );
					}

				}

				URLClassLoader urlClassLoader = new URLClassLoader( urlList,
						ClassLoader.getSystemClassLoader( ) );
				for ( int i = 0; i < driverFiles.length; i++ )
				{
					if ( driverFiles[i].getName( ).endsWith( ".jar" ) ) //$NON-NLS-1$
					{
						try
						{
							String[] resourceNames = getJarFileEntries( driverFiles[i] );
							
							LogInfo( 3333, driverFiles[i].toString() );
							
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
										if ( implementsSQLDriver( aClass ) )
										{
											LogInfo( 4444, aClass.getName() );
											
											// Do not add it, if it is a
											// Abstract
											// class
											int modifier = aClass.getModifiers( );
											boolean isAbstract = Modifier.isAbstract( modifier );

											if ( !isAbstract )
											{
												JDBCDriverInformation info = JDBCDriverInformation.getInstance( aClass,
														classPathURLs );
												if ( driverConfigUtil != null )
												{
													info.setUrlFormat( driverConfigUtil.getURLFormat( info.getDriverClassName( ) ) );
													info.setDisplayName( driverConfigUtil.getDisplayName( info.getDriverClassName( ) ) );
												}
												jdbcDrivers.add( info );
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

			//read user setting from the preference store and update.
			Map userMap = JdbcDriverManagerDialog.getPreferenceDriverInfo( );

			for ( Iterator itr = jdbcDrivers.iterator( ); itr.hasNext( ); )
			{
				JDBCDriverInformation info = (JDBCDriverInformation) itr.next( );

				if ( userMap.containsKey( info.toString( ) ) )
				{
					String[] vals = (String[]) userMap.get( info.toString( ) );

					if ( vals[1] != null && vals[1].length( ) > 0 )
					{
						info.setUrlFormat( vals[1] );
					}
				}
			}

		}
		return jdbcDrivers;
	}

	// for debug a bug
	private static Logger logger = Logger.getLogger( JdbcToolKit.class.getName( ) );
	private static void LogInfo( int index, String logInfo )
	{
		logger.log( Level.INFO, "******************PLACE"
				+ index + ": " + logInfo );
	}
	
	public static URL[] getJdbcDriverClassPathURLS( String driverName )
	{
		if ( jdbcDriverLocations == null )
		{
			getJdbcDriverNames( driverName );
		}

		try
		{
			if ( jdbcDriverLocations != null )
			{
				URL[] urls = new URL[jdbcDriverLocations.size( )];
				int n = 0;
				Iterator iter = jdbcDriverLocations.iterator( );
				while ( iter.hasNext( ) )
				{
					urls[n++] = new URL( "file:///" + DriverLoader.escapeCharacters( (String) iter.next( ) ) ); //$NON-NLS-1$
				}
				return urls;
			}
		}
		catch ( MalformedURLException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}

		return new URL[]{};
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
						+ (String) ( itor.next( ) )
						+ ";"; //$NON-NLS-1$
			}
		}

		return driverLocation;
	}
}