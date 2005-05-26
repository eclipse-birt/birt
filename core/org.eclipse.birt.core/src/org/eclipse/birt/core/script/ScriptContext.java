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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrapFactory;

/**
 * Wraps around the Rhino Script context
 * 
 * @version $Revision: 1.13 $ $Date: 2005/05/20 15:11:13 $
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
		this( null );
	}

	public ScriptContext( ScriptableObject root )
	{
		try
		{
			this.context = Context.enter( );
			if ( root == null )
			{
				this.scope = this.context.initStandardObjects( );
			}
			else
			{
				this.scope = this.context.newObject( root );
				this.scope.setParentScope( root );
			}

			//set the wrapper factory
			context.setWrapFactory( new BIRTWrapper( ) );

			//register BIRT defined static objects into script context

			new BIRTInitializer( ).intialize( context, scope );
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
	public Scriptable enterScope( )
	{
		return enterScope( null );
	}

	public Scriptable enterScope( Scriptable newScope )
	{
		if ( newScope == null )
		{
			newScope = context.newObject( scope );
		}
		newScope.setParentScope( scope );
		scope = newScope;
		return newScope;
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
	public Scriptable getScope( )
	{
		return scope;
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
		Object value = context.evaluateString( scope, source, name, lineNo,
				null );
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
		if(jsValue instanceof Integer)
		{
			return jsValue;
		}
		return Context.toType( jsValue, Object.class );
	}

	/**
	 * the registed intializers.
	 */
	protected static ArrayList initializers = new ArrayList( );

	/**
	 * register a intializer which is called when construct a new script
	 * context. You can't reigster the same initializer more than once,
	 * otherwise the initailzier will be called multiple times.
	 * 
	 * @param initializer
	 *            initializer.
	 */
	public static synchronized void registerInitializer(
			IJavascriptInitializer initializer )
	{
		initializers.add( initializer );
	}

	/**
	 * remove a intialzier.
	 * 
	 * @param initializer
	 *            to be removed.
	 */
	public static synchronized void unregisterInitializer(
			IJavascriptInitializer initializer )
	{
		initializers.remove( initializer );
	}

	/**
	 * any wapper instance.
	 */
	protected static ArrayList wrappers = new ArrayList( );

	/**
	 * register a wrapper which should be called in WapperFactory.
	 * 
	 * @param wrapper
	 *            new wrapper.
	 */
	public static synchronized void registerWrapper( IJavascriptWrapper wrapper )
	{
		wrappers.add( wrapper );
	}

	/**
	 * remove the wapper.
	 * 
	 * @param wrapper
	 *            to be removed.
	 */
	public static synchronized void unregisterWrapper(
			IJavascriptWrapper wrapper )
	{
		wrappers.remove( wrapper );
	}

	/**
	 * wapper factory to wrap the java object to java script object.
	 * 
	 * In this wapper factory, it calls registed wapper one by one to see if any
	 * wapper can handle the object. If no wapper is used, the default wapper is
	 * used.
	 * 
	 * @version $Revision: 1.13 $ $Date: 2005/05/20 15:11:13 $
	 */
	private class BIRTWrapper extends WrapFactory
	{

		/**
		 * wrapper an java object to javascript object.
		 */
		public Object wrap( Context cx, Scriptable scope, Object obj,
				Class staticType )
		{
			int wrapperCount = wrappers.size( );
			for ( int i = 0; i < wrapperCount; i++ )
			{
				IJavascriptWrapper wrapper = (IJavascriptWrapper) wrappers
						.get( i );
				if ( wrapper != null )
				{
					Object object = wrapper.wrap( cx, scope, obj, staticType );
					if ( object != obj )
					{
						return object;
					}
				}
			}
			if ( obj instanceof LinkedHashMap )
			{
				return new NativeJavaLinkedHashMap( scope, obj, staticType );
			}
			if ( obj instanceof Map )
			{
				return new NativeJavaMap( scope, obj, staticType );
			}
			if ( obj instanceof List )
			{
				return new NativeJavaList( scope, obj, staticType );
			}
			return super.wrap( cx, scope, obj, staticType );
		}
	}

	/**
	 * initailzier used to initalize the script context.
	 * 
	 * @version $Revision: 1.13 $ $Date: 2005/05/20 15:11:13 $
	 */
	private class BIRTInitializer
	{

		void intialize( Context cx, Scriptable scope ) throws Exception
		{
			int initializerCount = initializers.size( );
			for ( int i = 0; i < initializerCount; i++ )
			{
				IJavascriptInitializer initalizer = (IJavascriptInitializer) initializers
						.get( i );
				if ( initalizer != null )
				{
					initalizer.initialize( cx, scope );
				}
			}
			ScriptableObject.defineClass( scope, NativeFinance.class );
			ScriptableObject.defineClass( scope, NativeDateTimeSpan.class );
		}
	}

}