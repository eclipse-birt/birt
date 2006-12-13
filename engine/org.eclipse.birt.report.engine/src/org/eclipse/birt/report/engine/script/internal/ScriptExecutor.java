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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * A class used to create script event handlers
 */
public class ScriptExecutor
{

	public static final String PROPERTYSEPARATOR = EngineConstants.PROPERTYSEPARATOR;

	public static final String WEBAPP_CLASSPATH_KEY = EngineConstants.WEBAPP_CLASSPATH_KEY;

	public static final String WORKSPACE_CLASSPATH_KEY = EngineConstants.WORKSPACE_CLASSPATH_KEY;

	public static final String PROJECT_CLASSPATH_KEY = EngineConstants.PROJECT_CLASSPATH_KEY;

	protected static Logger log = Logger.getLogger( ScriptExecutor.class
			.getName( ) );


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
			if ( !( js instanceof String ) )
				return JSScriptStatus.NO_RUN;
			try
			{
				if ( scope != null )
					context.newScope( scope );
				Object result = null;
				if ( js instanceof String )
					result = context.evaluate( ( String ) js );
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
		if ( element == null )
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
		
		try
		{
			ClassLoader classLoader = context.getApplicationClassLoader( );
			c = classLoader.loadClass( className );
			o = c.newInstance( );
		}
		catch ( ClassNotFoundException e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
			if ( context != null )
				context.addException( new EngineException(
						MessageConstants.SCRIPT_CLASS_NOT_FOUND_ERROR,
						new Object[]{className}, e ) ); //$NON-NLS-1$
		}
		catch ( IllegalAccessException e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
			if ( context != null )
				context.addException( new EngineException(
						MessageConstants.SCRIPT_CLASS_ILLEGAL_ACCESS_ERROR,
						new Object[]{className}, e ) ); //$NON-NLS-1$
		}
		catch ( InstantiationException e )
		{
			log.log( Level.WARNING, e.getMessage( ), e );
			if ( context != null )
				context.addException( new EngineException(
						MessageConstants.SCRIPT_CLASS_INSTANTIATION_ERROR,
						new Object[]{className}, e ) ); //$NON-NLS-1$
		}
		return o;
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
