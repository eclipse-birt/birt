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

package org.eclipse.birt.core.script;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Wraps around the Rhino Script context
 * 
 * @version $Revision: 1.8 $ $Date: 2005/05/08 06:58:29 $
 */
public class ScriptContext
{

	/**
	 * for logging
	 */
	protected static Logger logger = Logger.getLogger( ScriptContext.class
			.getName( ) );

	/**
	 * the JavaScript Context
	 */
	protected Context context;

	/**
	 * The JavaScript scope used for script execution
	 */
	protected Scriptable scope;

	/**
	 * for BIRT globel varible "params"
	 */
	protected NativeObject params;

	/**
	 * constructor
	 */
	public ScriptContext( )
	{
		try
		{
			this.context = Context.enter( );
			this.scope = this.context.initStandardObjects( );

			//set the wrapper factory
			context.setWrapFactory( new BIRTWrapFactory( ) );

			//register BIRT defined static objects into script context
			ScriptableObject.defineClass(scope, NativeFinance.class);
			ScriptableObject.defineClass(scope, NativeDateTimeSpan.class);
		}
		catch ( Exception ex )
		{
			Context.exit( );
			this.scope = null;
			this.context = null;
			if ( logger.isLoggable( Level.WARNING ) )
			{
				logger.log( Level.WARNING, ex.getMessage( ) );
			}
		}
	}

	/**
	 * @param name
	 *            the name of a property
	 * @param value
	 *            the value of a property
	 */
	public void registerBean( String name, Object value )
	{
		assert ( this.context != null );
		Object sObj = Context.javaToJS( value, scope );
		scope.put( name, scope, sObj );
	}

	/**
	 * exit the scripting context
	 */
	public void exit( )
	{
		if ( context != null )
		{
			Context.exit( );
			context = null;
		}
	}

	/**
	 * creates a new scripting scope
	 */
	public void newScope( )
	{
		Scriptable newScope;
		try
		{
			newScope = context.newObject( scope );
			newScope.setParentScope( scope );
			scope = newScope;
		}
		catch ( EvaluatorException e )
		{
			if ( logger.isLoggable( Level.WARNING ) )
			{
				logger.log( Level.WARNING, e.getMessage( ) );
			}
		}
		catch ( JavaScriptException e )
		{
			if ( logger.isLoggable( Level.WARNING ) )
			{
				logger.log( Level.WARNING, e.getMessage( ) );
			}
		}
	}

	/**
	 * exits from the current scripting scope
	 */
	public void exitScope( )
	{
		Scriptable parentScope = scope.getParentScope( );
		if ( parentScope != null )
			scope = parentScope;
	}

	/**
	 * @return the current scope
	 */
	public Scriptable getCurrentScope( )
	{
		return scope;
	}

	/**
	 * checks if a property is available in the scope
	 * 
	 * @param name
	 * @return
	 */
	public Object lookupBean( String name )
	{
		assert ( context != null );
		return scope.get( name, scope );
	}

	/**
	 * evaluates a script
	 * 
	 * @param source
	 *            script to be evaluated
	 * @return the evaluated value
	 */
	public Object eval( String source )
	{
		return eval( source, "<inline>", 1 );
	}

	/**
	 * evaluates a script
	 */
	public Object eval( String source, String name, int lineNo )
	{
		assert ( this.context != null );
		try
		{
			Object value = context.evaluateString( scope, source, name, lineNo,
					null );
			return jsToJava( value );
		}
		catch ( Exception ex )
		{
			if (logger.isLoggable(Level.WARNING))
			{
				logger.log(Level.WARNING, ex.getMessage());
			}
			return null;
		}
	}

	/**
	 * converts a JS object to a Java object
	 * 
	 * @param jsValue
	 *            javascript object
	 * @return Java object
	 */
	public Object jsToJava( Object jsValue )
	{
		if ( jsValue instanceof Scriptable )
		{
			String className = ( (Scriptable) jsValue ).getClassName( );
			if ( "Date".equals( className ) )
			{
				return Context.toType( jsValue, java.util.Date.class );
			}
			else if ( "Boolean".equals( className ) )
			{
				return Boolean.valueOf( Context.toString( jsValue ) );
			}
			else if ( "String".equals( className ) )
			{
				return Context.toString( jsValue );
			}
		}
		return Context.toType( jsValue, Object.class );
	}
}