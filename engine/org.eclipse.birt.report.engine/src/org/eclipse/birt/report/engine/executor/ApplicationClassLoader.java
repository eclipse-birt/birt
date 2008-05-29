/*******************************************************************************
 * Copyright (c)2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.executor;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ScriptLibHandle;

/**
 * The application class loader.
 * 
 * The class loader first try to the load the class as following sequence:
 * <li>1. standard java class loader,
 * <li>2. classloader setted through the appContext.
 * <li>3. CLASSPATH setted by WEBAPP_CLASSPATH_KEY
 * <li>4. PROJECT_CLASSPATH_KEY
 * <li>5. WORKSAPCE_CLASSPATH_KEY
 * <li>6. JARs define in the report design
 */
public class ApplicationClassLoader extends ClassLoader
{

	/**
	 * the logger
	 */
	protected static Logger logger = Logger.getLogger( ClassLoader.class
			.getName( ) );

	private ClassLoader loader = null;
	private IReportRunnable runnable;
	private ExecutionContext executionContext = null;

	private ReportEngine engine;

	public ApplicationClassLoader( ReportEngine engine,
			IReportRunnable reportRunnable, ExecutionContext executionContext )
	{
		this.runnable = reportRunnable;
		this.engine = engine;
		this.executionContext = executionContext;
	}

	public Class loadClass( String className ) throws ClassNotFoundException

	{
		try
		{
			if ( loader == null )
			{
				createWrappedClassLoaders( );
			}
			return loader.loadClass( className );
		}
		catch ( ClassNotFoundException ex )
		{
			return IReportEngine.class.getClassLoader( ).loadClass( className );
		}
	}

	public URL getResource( String name )
	{
		URL url = IReportEngine.class.getClassLoader( ).getResource(
				name );
		if ( url == null )
		{
			if ( loader == null )
			{
				createWrappedClassLoaders( );
			}
			return loader.getResource( name );
		}
		return null;
	}

	protected void createWrappedClassLoaders( )
	{
		loader = createClassLoaderFromDesign( runnable, engine.getClassLoader( ), executionContext );
	}

	public static ClassLoader createClassLoaderFromDesign(
			IReportRunnable runnable, ClassLoader parent, ExecutionContext executionContext )
	{
		if ( runnable != null )
		{
			ModuleHandle module = (ModuleHandle) runnable.getDesignHandle( );
			ArrayList urls = new ArrayList( );
			Iterator iter = module.scriptLibsIterator( );
			while ( iter.hasNext( ) )
			{
				ScriptLibHandle lib = (ScriptLibHandle) iter.next( );
				String libPath = lib.getName( );
				URL url = module.findResource( libPath,
						IResourceLocator.LIBRARY );
				if ( url != null )
				{
					urls.add( url );
				}
				else
				{
					if (executionContext != null) {
						executionContext.addException(new EngineException(
								MessageConstants.JAR_NOT_FOUND_ERROR, libPath));
					}
					logger.log( Level.SEVERE, "Can not find specified jar: " + libPath); //$NON-NLS-1$
				}
			}
			if ( urls.size( ) != 0 )
			{
				URL[] jarUrls = (URL[]) urls.toArray( new URL[]{} );
				return new URLClassLoader( jarUrls, parent );
			}
		}
		return parent;
	}
}
