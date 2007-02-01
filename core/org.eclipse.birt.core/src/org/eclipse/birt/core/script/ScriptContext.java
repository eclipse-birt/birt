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

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * Wraps around the Rhino Script context
 * 
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

	protected ImporterTopLevel global;

	protected Scriptable sharedScope;
	/**
	 * The JavaScript scope used for script execution
	 */
	protected Scriptable scope;

	/**
	 * a cache used to store the precompiled scripts the key is the source code
	 * and the value is the compiled script.
	 */
	protected HashMap compiledScripts = new HashMap( );

	/**
	 * for BIRT globel varible "params"
	 */
	protected NativeObject params;

	/**
	 * constructor
	 */
	public ScriptContext( )
	{
		this( null );
	}

	public ScriptContext( ScriptableObject root )
	{
		try
		{
			this.context = Context.enter( );
			global = new ImporterTopLevel( );
			if ( root != null )
			{
				global.setPrototype( root );
			}
			global.initStandardObjects( context, true );
			this.scope = global;
			sharedScope = context.newObject( scope );

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
			compiledScripts.clear( );
		}
	}

	/**
	 * creates a new scripting scope
	 */
	public Scriptable enterScope( )
	{
		return enterScope( null );
	}

	/**
	 * Use a new scope in the script context. The following script is evaluated
	 * in the new scope. You must call exitScope to return to the parent scope.
	 * The new scope is created automatically if the newScope is null.
	 * 
	 * @param newScope,
	 *            scope used for following evaluation. null means create a scope
	 *            automatically.
	 * @return the scope used for following evaluation.
	 */
	public Scriptable enterScope( Scriptable newScope )
	{
		if ( newScope == null )
		{
			newScope = context.newObject( scope );
		}
		newScope.setPrototype( scope );
		scope = newScope;
		sharedScope.setPrototype( scope );
		return newScope;
	}

	/**
	 * exits from the current scripting scope. Must couple with the enterScope.
	 */
	public void exitScope( )
	{
		Scriptable protoScope = scope.getPrototype( );
		if ( protoScope != null )
			scope = protoScope;
		sharedScope.setPrototype( scope );
	}

	/**
	 * @return the current scope
	 */
	public Scriptable getScope( )
	{
		return scope;
	}

	public Scriptable getSharedScope( )
	{
		return sharedScope;
	}

	public Scriptable getRootScope( )
	{
		return global;
	}

	public Context getContext( )
	{
		return context;
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
		Script script = (Script) compiledScripts.get( source );
		if ( script == null )
		{
			script = context.compileString( source, name, lineNo, null );
			compiledScripts.put( source, script );
		}
		Object value = script.exec( context, scope );
		return jsToJava( value );
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
		return JavascriptEvalUtil.convertJavascriptValue( jsValue );
	}

	public Object javaToJs( Object value )
	{
		return Context.javaToJS( value, scope );
	}

	/**
	 * register a intializer which is called when construct a new script
	 * context. You can't reigster the same initializer more than once,
	 * otherwise the initailzier will be called multiple times.
	 * 
	 * @param initializer
	 *            initializer.
	 * @deprecated BIRT 1.0.1
	 */
	public static synchronized void registerInitializer(
			IJavascriptInitializer initializer )
	{
	}

	/**
	 * remove a intialzier.
	 * 
	 * @param initializer
	 *            to be removed.
	 * @deprecated BIRT 1.0.1
	 */
	public static synchronized void unregisterInitializer(
			IJavascriptInitializer initializer )
	{
	}

	/**
	 * register a wrapper which should be called in WapperFactory.
	 * 
	 * @param wrapper
	 *            new wrapper.
	 * @deprecated BIRT 1.0.1
	 */
	public static synchronized void registerWrapper( IJavascriptWrapper wrapper )
	{
	}

	/**
	 * remove the wapper.
	 * 
	 * @param wrapper
	 *            to be removed.
	 * @deprecated BIRT 1.0.1
	 */
	public static synchronized void unregisterWrapper(
			IJavascriptWrapper wrapper )
	{
	}

}