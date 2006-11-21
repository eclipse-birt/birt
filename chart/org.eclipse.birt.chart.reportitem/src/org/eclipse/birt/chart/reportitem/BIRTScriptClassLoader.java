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

package org.eclipse.birt.chart.reportitem;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.script.ScriptClassLoaderAdapter;

/**
 * A BIRT implementation for IScriptClassLoader
 */
public class BIRTScriptClassLoader extends ScriptClassLoaderAdapter
{

	public static final String PROPERTYSEPARATOR = ";"; //$NON-NLS-1$

	public static final String WEBAPP_CLASSPATH_KEY = "webapplication.projectclasspath"; //$NON-NLS-1$

	public static final String WORKSPACE_CLASSPATH_KEY = "workspace.projectclasspath"; //$NON-NLS-1$

	public static final String PROJECT_CLASSPATH_KEY = "user.projectclasspath"; //$NON-NLS-1$
	
	private ClassLoader classLoader;

	public BIRTScriptClassLoader( ClassLoader classLoader )
	{
		this.classLoader = classLoader;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.script.IScriptClassLoader#loadClass(java.lang.String,
	 *      java.lang.ClassLoader)
	 */
	public Class loadClass( String className, ClassLoader parentLoader )
			throws ClassNotFoundException
	{
		if ( className == null )
			return null;

		Class c = null;
		ClassNotFoundException ex = null;

		// Use built-in classLoader to load class first
		if ( this.classLoader != null )
		{
			c = this.classLoader.loadClass( className );
			if ( c != null )
			{
				return c;
			}
		}		

		try
		{
			// If not found in the cache, try creating one
			c = Class.forName( className );
		}
		catch ( ClassNotFoundException e )
		{
			ex = e;
			// Try using web application's webapplication.projectclasspath
			// to load it.
			// This would be the case where the application is deployed on
			// web server.
			c = getClassUsingCustomClassPath( className,
					WEBAPP_CLASSPATH_KEY,
					parentLoader );
			if ( c == null )
			{
				// Try using the user.projectclasspath property to load it
				// using the classpath specified. This would be the case
				// when debugging is used
				c = getClassUsingCustomClassPath( className,
						PROJECT_CLASSPATH_KEY,
						parentLoader );
				if ( c == null )
				{
					// The class is not on the current classpath.
					// Try using the workspace.projectclasspath property
					c = getClassUsingCustomClassPath( className,
							WORKSPACE_CLASSPATH_KEY,
							parentLoader );
				}
			}
		}

		if ( c == null )
		{
			// Didn't find the class using any method, so throw the
			// exception
			throw ex;
		}

		return c;
	}

	private static Class getClassUsingCustomClassPath( String className,
			String classPathKey, ClassLoader parentLoader )
	{
		String classPath = System.getProperty( classPathKey );
		if ( classPath == null || classPath.length( ) == 0 || className == null )
			return null;
		String[] classPathArray = classPath.split( PROPERTYSEPARATOR, -1 );
		URL[] urls = null;
		if ( classPathArray.length != 0 )
		{
			List l = new ArrayList( );
			for ( int i = 0; i < classPathArray.length; i++ )
			{
				String cpValue = classPathArray[i];
				File file = new File( cpValue );
				try
				{
					l.add( file.toURL( ) );
				}
				catch ( MalformedURLException e )
				{
					e.printStackTrace( );
				}
			}
			urls = (URL[]) l.toArray( new URL[l.size( )] );
		}

		if ( urls != null )
		{
			ClassLoader cl = new URLClassLoader( urls, parentLoader );
			try
			{
				return cl.loadClass( className );
				// Note: If the class can
				// not even be loadded by this
				// loader either, null will be returned
			}
			catch ( ClassNotFoundException e )
			{
				// Ignore
			}
		}
		return null;
	}

}
