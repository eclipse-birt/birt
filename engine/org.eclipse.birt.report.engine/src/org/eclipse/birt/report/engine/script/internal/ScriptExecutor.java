/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.script.internal;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

/**
 * A class used to create script event handlers
 */
public class ScriptExecutor
{

	public static final String PROPERTYSEPARATOR = ";";

	public static final String WEBAPP_CLASSPATH_KEY = "webapplication.projectclasspath";

	public static final String WORKSPACE_CLASSPATH_KEY = "workspace.projectclasspath";

	public static final String PROJECT_CLASSPATH_KEY = "user.projectclasspath";

	protected static Logger log = Logger.getLogger( ScriptExecutor.class
			.getName( ) );

	protected static JSScriptStatus handleJS( Object scope, Expression js,
			ExecutionContext context )
	{
		return handleJSInternal( scope, js, context );
	}

	protected static JSScriptStatus handleJS( Object scope, String js,
			ExecutionContext context )
	{
		return handleJSInternal( scope, js, context );
	}

	private static JSScriptStatus handleJSInternal( Object scope, Object js,
			ExecutionContext context )
	{
		if ( js != null )
		{
			if ( !( js instanceof String || js instanceof Expression ) )
				return JSScriptStatus.NO_RUN;
			try
			{
				if ( scope != null )
					context.newScope( scope );
				Object result = null;
				if ( js instanceof String )
					result = context.evaluate( ( String ) js );
				else if ( js instanceof Expression )
					result = context.evaluate( ( Expression ) js );
				return new JSScriptStatus( true, result );
			} finally
			{
				if ( scope != null )
					context.exitScope( );
			}
		}
		return JSScriptStatus.NO_RUN;
	}

	protected static Object getInstance( DesignElementHandle element,
			ExecutionContext context )
	{
		if (element == null)
			return null;
		String className = element.getEventHandlerClass( );
		return getInstance( className, context );
	}

	protected static Object getInstance( String className,
			ExecutionContext context )
	{
		if ( className == null )
			return null;
		Object o = null;

		Class c = null;
		ClassNotFoundException ex = null;
		try
		{
			try
			{
				// If not found in the cache, try creating one
				c = Class.forName( className );
			} catch ( ClassNotFoundException e )
			{
				ex = e;
				// Try using web application's webapplication.projectclasspath
				// to load it.
				// This would be the case where the application is deployed on
				// web server.
				c = getClassUsingCustomClassPath( className,
						WEBAPP_CLASSPATH_KEY );
				if ( c == null )
				{
					// Try using the user.projectclasspath property to load it
					// using the classpath specified. This would be the case
					// when debugging is used
					c = getClassUsingCustomClassPath( className,
							PROJECT_CLASSPATH_KEY );
					if ( c == null )
					{
						// The class is not on the current classpath.
						// Try using the workspace.projectclasspath property
						c = getClassUsingCustomClassPath( className,
								WORKSPACE_CLASSPATH_KEY );
					}
				}
			}

			if ( c != null )
				o = c.newInstance( );
			else
				// Didn't find the class using any method, so throw the
				// exception
				throw ex;
		} catch ( ClassNotFoundException e )
		{
			log.log( Level.WARNING, ex.getMessage( ), ex );
			if ( context != null )
				context.addException( new EngineException(
						MessageConstants.SCRIPT_CLASS_NOT_FOUND_ERROR,
						new Object[] { className }, ex ) ); //$NON-NLS-1$
		} catch ( IllegalAccessException e )
		{
			log.log( Level.WARNING, ex.getMessage( ), ex );
			if ( context != null )
				context.addException( new EngineException(
						MessageConstants.SCRIPT_CLASS_ILLEGAL_ACCESS_ERROR,
						new Object[] { className }, ex ) ); //$NON-NLS-1$
		} catch ( InstantiationException e )
		{
			log.log( Level.WARNING, ex.getMessage( ), ex );
			if ( context != null )
				context.addException( new EngineException(
						MessageConstants.SCRIPT_CLASS_INSTANTIATION_ERROR,
						new Object[] { className }, ex ) ); //$NON-NLS-1$
		}
		return o;
	}

	private static Class getClassUsingCustomClassPath( String className,
			String classPathKey )
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
				} catch ( MalformedURLException e )
				{
					e.printStackTrace( );
				}
			}
			urls = ( URL[] ) l.toArray( new URL[l.size( )] );
		}

		if ( urls != null )
		{
			ClassLoader cl = new URLClassLoader( urls, ScriptExecutor.class
					.getClassLoader( ) );
			try
			{
				return cl.loadClass( className );
				// Note: If the class can
				// not even be loadded by this
				// loader either, null will be returned
			} catch ( ClassNotFoundException e )
			{
				// Ignore
			}
		}
		return null;
	}

	protected static void addClassCastException( ExecutionContext context,
			ClassCastException e, String className, Class requiredInterface )
	{
		addException( context, e, MessageConstants.SCRIPT_CLASS_CAST_ERROR,
				new Object[] { className, requiredInterface.getName( ) } );
	}

	protected static void addException( ExecutionContext context, Exception e )
	{
		addException( context, e, MessageConstants.UNHANDLED_SCRIPT_ERROR, null );
	}

	private static void addException( ExecutionContext context, Exception e,
			String errorType, Object[] args )
	{
		log.log( Level.WARNING, e.getMessage( ), e );
		if ( context == null )
			return;
		if ( args == null )
			context.addException( new EngineException( errorType, e ) );
		else
			context.addException( new EngineException( errorType, args, e ) );
	}

	protected static class JSScriptStatus
	{
		private boolean didRun;

		private Object result;

		public static final JSScriptStatus NO_RUN = new JSScriptStatus( false,
				null );

		public JSScriptStatus( boolean didRun, Object result )
		{
			this.didRun = didRun;
			this.result = result;
		}

		public boolean didRun( )
		{
			return didRun;
		}

		public Object result( )
		{
			return result;
		}
	}
}
