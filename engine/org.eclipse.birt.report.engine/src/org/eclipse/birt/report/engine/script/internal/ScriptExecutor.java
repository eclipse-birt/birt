/*******************************************************************************
 * Copyright (c) 2005, 2007 Actuate Corporation.
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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
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


	protected static JSScriptStatus handleJS( Object scope,
			ScriptExpression expr, ExecutionContext context ) throws BirtException
	{
		return handleJSInternal( scope, expr, context );
	}

	private static JSScriptStatus handleJSInternal( Object scope,
			ScriptExpression expr, ExecutionContext context ) throws BirtException
	{
		if ( expr != null )
		{
			try
			{
				if ( scope != null )
					context.newScope( scope );
				Object result = null;
				result = context.evaluate( expr );
				return new JSScriptStatus( true, result );
			} finally
			{
				if ( scope != null )
					context.exitScope( );
			}
		}
		return JSScriptStatus.NO_RUN;
	}
	
	protected static boolean needOnCreate( ReportItemDesign design )
	{
		if ( design == null )
		{
			return false;
		}
		return design.getOnCreate( ) != null || design.getJavaClass( ) != null;
	}
	
	protected static boolean needOnRender( ReportItemDesign design )
	{
		if ( design == null )
		{
			return false;
		}
		return design.getOnRender( ) != null || design.getJavaClass( ) != null;
	}
	
	protected static boolean needOnPageBreak( ReportItemDesign design )
	{
		if ( design == null )
		{
			return false;
		}
		return design.getOnPageBreak( ) != null || design.getJavaClass( ) != null;
	}

	protected static Object getInstance( DesignElementHandle element,
			ExecutionContext context ) throws EngineException
	{
		if ( element == null )
			return null;
		String className = element.getEventHandlerClass( );
		return getInstance( className, context );
	}
	
	protected static Object getInstance( ReportItemDesign element,
			ExecutionContext context ) throws EngineException
	{
		if ( element == null )
			return null;
		String className = element.getJavaClass( );
		return getInstance( className, context );
	}

	protected static Object getInstance( String className,
			ExecutionContext context ) throws EngineException
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
			throw new EngineException(
					MessageConstants.SCRIPT_CLASS_NOT_FOUND_ERROR,
					new Object[]{className}, e ); //$NON-NLS-1$
		}
		catch ( IllegalAccessException e )
		{
			throw new EngineException(
					MessageConstants.SCRIPT_CLASS_ILLEGAL_ACCESS_ERROR,
					new Object[]{className}, e ); //$NON-NLS-1$
		}
		catch ( InstantiationException e )
		{
			throw new EngineException(
					MessageConstants.SCRIPT_CLASS_INSTANTIATION_ERROR,
					new Object[]{className}, e ); //$NON-NLS-1$
		}
		return o;
	}
/*
	protected static void addClassCastException( ExecutionContext context,
			ClassCastException e, String className, Class requiredInterface )
	{
		addException( context, e, MessageConstants.SCRIPT_CLASS_CAST_ERROR,
				new Object[] { className, requiredInterface.getName( ) } );
	}
*/
	protected static void addClassCastException( ExecutionContext context,
			Exception e, DesignElementHandle handle, Class requiredInterface )
	{
		EngineException ex = new EngineException(
				MessageConstants.SCRIPT_CLASS_CAST_ERROR, new Object[]{
						handle.getEventHandlerClass( ),
						requiredInterface.getName( )}, e );
		
		log.log( Level.WARNING, e.getMessage( ), e );
		if ( context == null )
			return;
		
		context.addException( handle, ex );
	}

	protected static void addException( ExecutionContext context, Exception e )
	{
		addException( context, e, null );
	}
	
	protected static void addException( ExecutionContext context, Exception e,
			DesignElementHandle handle )
	{
		EngineException eex = null;
		if ( e instanceof EngineException )
			eex = (EngineException) e;
		else if ( e instanceof BirtException )
		{
			eex = new EngineException( (BirtException) e );
		}
		else
		{
			eex = new EngineException( MessageConstants.UNHANDLED_SCRIPT_ERROR,
					e );
		}
		
		log.log( Level.WARNING, eex.getMessage( ), eex );
		if ( context == null )
			return;
		
		if( handle == null )
			context.addException( eex );
		else
			context.addException( handle, eex );
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
