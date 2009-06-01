/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.script;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.exception.CoreException;
import org.eclipse.birt.core.i18n.ResourceConstants;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionContext;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ImporterTopLevel;
import org.mozilla.javascript.LazilyLoadedCtor;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

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
	 * a cache storing compiled script
	 */
	protected Map<String, Script> compiledScripts = new HashMap<String, Script>( );
	
	/**
	 * a cache storing ScriptExpression
	 */
	protected HashMap<String, ScriptExpression> scriptExpressionCache = new HashMap<String, ScriptExpression>( );

	/**
	 * for BIRT globel varible "params"
	 */
	protected NativeObject params;
	
	private Map<String, Object> propertyMap = new HashMap<String, Object>();

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
				//can not put this object to root, because this object will cache package and classloader information.
				//so we need rewrite this property.
	            new LazilyLoadedCtor( global, "Packages",
						"org.mozilla.javascript.NativeJavaTopPackage", false );
				global.exportAsJSClass( 3, global, false );
				global.delete( "constructor" );
				global.setPrototype( root );
			}
			else
			{
				global.initStandardObjects( context, true );
			}
			if ( global.get( org.eclipse.birt.core.script.functionservice.IScriptFunctionContext.FUNCITON_BEAN_NAME,
					global ) == org.mozilla.javascript.UniqueTag.NOT_FOUND )
			{
				IScriptFunctionContext functionContext = new IScriptFunctionContext( ) {

					public Object findProperty( String name )
					{
						return propertyMap.get( name );
					}
				};

				Object sObj = Context.javaToJS( functionContext, global );
				global.put( org.eclipse.birt.core.script.functionservice.IScriptFunctionContext.FUNCITON_BEAN_NAME,
						global,
						sObj );
			}
			this.scope = global;
			sharedScope = context.newObject( scope );
			sharedScope.setParentScope( scope );
			
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

	public void setCompiledScripts(Map<String, Script> compiledScripts)
	{
		this.compiledScripts = compiledScripts;
	}
	
	public void setTimeZone(TimeZone zone )
	{
		propertyMap.put( IScriptFunctionContext.TIMEZONE, zone );
	}
	
	public void setLocale( Locale locale )
	{
		context.setLocale(  locale );
		propertyMap.put( IScriptFunctionContext.LOCALE, ULocale.forLocale( locale) );
	}
		
	/**
	 * @param name
	 *            the name of a property
	 * @param value
	 *            the value of a property
	 */
	public void registerBean( String name, Object value )
	{
		Object sObj = Context.javaToJS( value, global );
		global.put( name, global, sObj );
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
			compiledScripts = null;
			scriptExpressionCache.clear();
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
		newScope.setParentScope( scope );
		scope = newScope;
		sharedScope.setParentScope( scope );
		return newScope;
	}

	/**
	 * exits from the current scripting scope. Must couple with the enterScope.
	 */
	public void exitScope( )
	{
		Scriptable protoScope = scope.getParentScope( );
		if ( protoScope != null )
			scope = protoScope;
		sharedScope.setParentScope( scope );
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
	public Object eval( String source ) throws BirtException
	{
		if ( null != source && source.length( ) > 0 )
		{
			ScriptExpression scriptExpression = scriptExpressionCache
					.get( source );
			if ( scriptExpression == null )
			{
				scriptExpression = new ScriptExpression( source );
				scriptExpressionCache.put( source, scriptExpression );
			}
			return eval( scriptExpression );
		}
		return null;
	}

	/**
	 * Evaluates a String with given scope.
	 * @param source
	 * @param scope
	 * @return
	 */
	public Object eval( String source, Scriptable scope ) throws BirtException
	{
		if ( null != source && source.length( ) > 0 )
		{
			ScriptExpression expr = scriptExpressionCache
					.get( source );
			if ( expr == null )
			{
				expr = new ScriptExpression( source );
				scriptExpressionCache.put( source, expr );
			}
			

			return eval( scope, expr );
		}
		return null;
	}

	/**
	 * Evaluate expr with given scope.
	 * @param scope
	 * @param expr
	 * @return
	 */
	private Object eval( Scriptable scope, final ScriptExpression expr )
			throws BirtException
	{
		String source = expr.getScriptText( );
		Script script = expr.getCompiledScript( );
		try
		{
			if ( script == null )
			{
				String text = expr.getScriptText( );
				if ( context.getDebugger( ) != null )
				{
					source = text + expr.getLineNumber( );
				}
				script = compiledScripts.get( source );
				if ( script == null )
				{
					script = compile( expr.getScriptText( ), expr.getId( ),
							expr.getLineNumber( ) );
					compiledScripts.put( source, script );
				}
				expr.setCompiledScript( script );
			}
			Object value = script.exec( context, scope );
			return jsToJava( value );
		}
		catch ( Throwable ex )
		{
			throw new CoreException( ResourceConstants.JAVASCRIPT_COMMON_ERROR,
					new Object[]{source, ex.getMessage( )}, ex );
		}
	}

	private Script compile( final String script, final String id,
			final int lineNumber )
	{
		return AccessController.doPrivileged( new PrivilegedAction<Script>( ) {

			public Script run( )
			{
				return context.compileString( script, id, lineNumber, null );
			}
		} );
	}

	/**
	 * evaluates a script
	 */
	public Object eval( ScriptExpression expr ) throws BirtException
	{
		assert ( this.context != null );
		if ( null == expr )
		{
			return null;
		}

		return eval( this.scope, expr );
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

	public void setApplicationClassLoader( ClassLoader appLoader )
	{
		ClassLoader loader = appLoader;
		try
		{
			appLoader.loadClass( "org.mozilla.javascript.Context" );
		}
		catch ( ClassNotFoundException e )
		{
			loader = new RhinoClassLoaderDecoration( appLoader, getClass( )
					.getClassLoader( ) );
		}
		getContext( ).setApplicationClassLoader( loader );
	}
	
	private static class RhinoClassLoaderDecoration extends ClassLoader
	{

		private ClassLoader applicationClassLoader;
		private ClassLoader rhinoClassLoader;

		public RhinoClassLoaderDecoration( ClassLoader applicationClassLoader,
				ClassLoader rhinoClassLoader )
		{
			this.applicationClassLoader = applicationClassLoader;
			this.rhinoClassLoader = rhinoClassLoader;
		}

		public Class<?> loadClass( String name ) throws ClassNotFoundException
		{
			try
			{
				return applicationClassLoader.loadClass( name );
			}
			catch ( ClassNotFoundException e )
			{
				return rhinoClassLoader.loadClass( name );
			}
		}
	}
}