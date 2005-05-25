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
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.birt.report.data.oda.jdbc.ui.dialogs.JdbcDriverManagerDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;

/**
 * TODO
 */
public class JdbcToolKit
{
	// class path used for obtaining a connection using a jdbc driver
	private static File[] jdbcDriverFiles = null;
	
	// Name of all the configured jdbc drivers
	private static ArrayList jdbcDriverInfos = null;

	private static final Class DriverClass = Driver.class;

	/**
	 * Resets cached jdbc driver list to null, force reget the infomation when
	 * required next time.
	 */
	public static void resetJdbcDriverNames( )
	{
		jdbcDriverInfos = null;
	}

	/**
	 * Returns a List jdbc Drivers. The Drivers are searched from predefined
	 * directories in the DTE plug-in. Currently it is expected that the jdbc
	 * drivers are in the "drivers" directory of the DTE oda.jdbc plug-in.
	 */
	public static ArrayList getJdbcDriverNames( String driverName )
	{
		if ( jdbcDriverInfos == null )
		{
			jdbcDriverInfos = new ArrayList( );
			
			// add JdbcOdbc first
			JDBCDriverInformation odbcInfo = JDBCDriverInformation.newInstance( sun.jdbc.odbc.JdbcOdbcDriver.class );
			if ( odbcInfo != null )
			{
				// Adding the odbc-jdbc driver
				odbcInfo.setUrlFormat( "jdbc:odbc:<data source name>" );
				odbcInfo.setDisplayName( "Sun JDBC-ODBC Bridge Driver" );
				jdbcDriverInfos.add( odbcInfo );
			}

			jdbcDriverFiles = JdbcDriverConfigUtil.getDriverFiles( );
			if ( jdbcDriverFiles != null )
			{	
				// Create a URL Array for the class loader to use
				URL[] urlList = new URL[jdbcDriverFiles.length];
				for ( int i = 0; i < jdbcDriverFiles.length; i++ )
				{
					try
					{
						urlList[i] = new URL( "file", null, jdbcDriverFiles[i].getAbsolutePath( ) ); //$NON-NLS-1$
					}
					catch ( MalformedURLException e )
					{
						ExceptionHandler.handle( e );
					}
				}
				URLClassLoader urlClassLoader = new URLClassLoader( urlList,
						ClassLoader.getSystemClassLoader( ) );
				
				for ( int i = 0; i < jdbcDriverFiles.length; i++ )
				{
					if ( jdbcDriverFiles[i].getName( ).endsWith( ".jar" ) ) //$NON-NLS-1$
					{
						String[] resourceNames = getAllResouceNames( jdbcDriverFiles[i] );
						for ( int j = 0; j < resourceNames.length; j++ )
						{
							String resourceName = resourceNames[j];
							if ( resourceName.endsWith( ".class" ) ) //$NON-NLS-1$
							{
								resourceName = ( resourceName.replaceAll( "/", //$NON-NLS-1$
										"." ) ).substring( 0, //$NON-NLS-1$
										resourceName.length( ) - 6 );
								
								Class aClass = null;
								try
								{
									aClass = urlClassLoader.loadClass( resourceName );
								}
								catch ( Throwable e )
								{
									// here throwable is used to catch exception and error
								}

								if ( aClass != null )
								{
									if ( implementsSQLDriverClass( aClass ) )
									{
										// Do not add it, if it is a
										// Abstract class
										boolean isAbstract = Modifier.isAbstract( aClass.getModifiers( ) );
										if ( !isAbstract )
										{
											JDBCDriverInformation info = JDBCDriverInformation.newInstance( aClass );
											if ( info != null )
											{
												info.setUrlFormat( JdbcDriverConfigUtil.getURLFormat( info.getDriverClassName( ) ) );
												info.setDisplayName( JdbcDriverConfigUtil.getDisplayName( info.getDriverClassName( ) ) );
												jdbcDriverInfos.add( info );
											}
										}
									}
								}
							}
						} // end of resourceNames
					}
				} // end of jdbcDriverFiles
			}

			//read user setting from the preference store and update.
			Map preferenceMap = JdbcDriverManagerDialog.getPreferenceDriverInfo( );

			for ( Iterator itr = jdbcDriverInfos.iterator( ); itr.hasNext( ); )
			{
				JDBCDriverInformation info = (JDBCDriverInformation) itr.next( );
				
				Object ob = preferenceMap.get( info.toString( ) );
				if ( ob != null )
				{
					String[] vals = (String[]) ob;
					if ( vals[1] != null && vals[1].length( ) > 0 )
					{
						info.setUrlFormat( vals[1] );
					}
				}
			}
		}
		
		return jdbcDriverInfos;
	}

	/**
	 * Get all resouces included in a jar file
	 * @param jarFile
	 * @return
	 */
	private static String[] getAllResouceNames( File jarFile )
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
	 * Determin aClass implements java.sql.Driver interface
	 * @param aClass
	 * @return
	 */
	private static boolean implementsSQLDriverClass( Class aClass )
	{
		if ( DriverClass.isAssignableFrom( aClass ) )
		{
			return true;
		}
		return false;
	}

	/**
	 * Get driver class path of specified driverName
	 * @param driverName
	 * @return
	 */
	public static String getJdbcDriverClassPath( String driverName )
	{
		String driverLocation = ""; //$NON-NLS-1$
		if ( jdbcDriverFiles == null )
		{
			getJdbcDriverNames( driverName );
		}

		// Generate a class path from the entries
		if ( jdbcDriverFiles != null )
		{
			for ( int i = 0; i < jdbcDriverFiles.length; i++ )
				driverLocation += jdbcDriverFiles[i].toString( ) + ";"; //$NON-NLS-1$
		}

		return driverLocation;
	}
	
}