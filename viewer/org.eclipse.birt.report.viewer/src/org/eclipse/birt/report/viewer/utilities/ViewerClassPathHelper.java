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

package org.eclipse.birt.report.viewer.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Helper to handle viewer related classpath settings
 */

public class ViewerClassPathHelper
{

	private static final String WORKSPACE_CLASSPATH_KEY = "workspace.projectclasspath"; //$NON-NLS-1$

	static protected boolean inDevelopmentMode = false;
	static protected String[] devDefaultClasspath;
	static protected Properties devProperties = null;

	static
	{
		// Check the osgi.dev property to see if dev classpath entries have been
		// defined.
		String osgiDev = System.getProperty( "osgi.dev" ); //$NON-NLS-1$
		if ( osgiDev != null )
		{
			try
			{
				inDevelopmentMode = true;
				URL location = new URL( osgiDev );
				devProperties = load( location );
				if ( devProperties != null )
					devDefaultClasspath = getArrayFromList( devProperties
							.getProperty( "*" ) ); //$NON-NLS-1$
			}
			catch ( MalformedURLException e )
			{
				devDefaultClasspath = getArrayFromList( osgiDev );
			}
		}
	}

	public static String[] getDevClassPath( String id )
	{
		String[] result = null;
		if ( id != null && devProperties != null )
		{
			String entry = devProperties.getProperty( id );
			if ( entry != null )
				result = getArrayFromList( entry );
		}
		if ( result == null )
			result = devDefaultClasspath;
		return result;
	}

	/**
	 * Returns the result of converting a list of comma-separated tokens into an
	 * array
	 * 
	 * @return the array of string tokens
	 * @param prop
	 *            the initial comma-separated string
	 */
	public static String[] getArrayFromList( String prop )
	{
		if ( prop == null || prop.trim( ).equals( "" ) ) //$NON-NLS-1$
			return new String[0];
		Vector list = new Vector( );
		StringTokenizer tokens = new StringTokenizer( prop, "," ); //$NON-NLS-1$
		while ( tokens.hasMoreTokens( ) )
		{
			String token = tokens.nextToken( ).trim( );
			if ( !token.equals( "" ) ) //$NON-NLS-1$
				list.addElement( token );
		}
		return list.isEmpty( ) ? new String[0] : (String[]) list
				.toArray( new String[list.size( )] );
	}

	public static boolean inDevelopmentMode( )
	{
		return inDevelopmentMode;
	}

	/*
	 * Load the given properties file
	 */
	private static Properties load( URL url )
	{
		Properties props = new Properties( );
		try
		{
			InputStream is = null;
			try
			{
				is = url.openStream( );
				props.load( is );
			}
			finally
			{
				if ( is != null )
					is.close( );
			}
		}
		catch ( IOException e )
		{
			// TODO consider logging here
		}
		return props;
	}

	/**
	 * Set workspace classpath
	 */
	public static void setWorkspaceClassPath( )
	{
		// register workspace classpath finder
		IWorkspaceClasspathFinder finder = new WorkspaceClasspathFinder();
		WorkspaceClasspathManager.registerClassPathFinder( finder );
		
		// Set the classpath property
		String projectClassPaths = WorkspaceClasspathManager.getClassPath( );
		if ( projectClassPaths == null )
			projectClassPaths = ""; //$NON-NLS-1$

		System.setProperty( WORKSPACE_CLASSPATH_KEY, projectClassPaths );
	}
}
