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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;

/**
 * 
 * @version $Revision: #1 $ $Date: 2005/01/25 $
 */
public class ScriptContext
{

	protected static Log logger = LogFactory.getLog( ScriptContext.class );

	/**
	 * JavaScript Context
	 */
	protected Context context;
	/**
	 * Scope used by current context
	 */
	protected Scriptable scope;
	/**
	 * Globel varible "params" 
	 */
	protected NativeObject params;

	public ScriptContext( )
	{
		try
		{
			this.context = Context.enter( );
			this.scope = this.context.initStandardObjects( );
			//set the wrapper class
			context.setWrapFactory(new BIRTWrapFactory());
			//register NativeFinance into script context
			NativeFinance.init( context, scope, true );
			//register DateTimeSpan into script context
			NativeDateTimeSpan.init( context, scope, true );
		}
		catch ( Exception ex )
		{
			Context.exit( );
			this.scope = null;
			this.context = null;
			logger.error( ex );
		}
	}

	public void registerBean( String name, Object value )
	{
		assert ( this.context != null );
		Object sObj = Context.javaToJS( value, scope );
		scope.put( name, scope, sObj );
	}

	public void terminate( )
	{
		if ( context != null )
		{
			Context.exit( );
			context = null;
		}
	}

	public void newScope()
	{
	    Scriptable newScope = new NativeObject();
	    newScope.setPrototype(scope);
	    newScope.setParentScope(scope);
	    scope = newScope;
	}
	
	public void exitScope()
	{
		Scriptable parentScope = scope.getParentScope();
		if (parentScope != null)
		{
			scope = parentScope;
		}
	}
	
	public Scriptable getCurrentScope()
	{
		return scope;
	}
	
	public Object lookupBean( String name )
	{
		assert ( this.context != null );
		return this.scope.get( name, this.scope );
	}

	public Object eval( String source )
	{
		Object value = eval( source, "<inline>", 1 );
		return jsToJava( value );
	}

	public Object eval( String source, String name, int lineNo )
	{
		assert ( this.context != null );
		try
		{
			Object value = context.evaluateString( this.scope, source, name,
					lineNo, null );
			return jsToJava( value );
		}
		catch ( Exception ex )
		{
			logger.error( source, ex );
			return null;
		}
	}

	public Object jsToJava( Object jsValue )
	{
		if ( jsValue instanceof Scriptable )
		{
			if ( "Date".equals( ( (Scriptable) jsValue ).getClassName( ) ) )
			{
				return Context.toType( jsValue, java.util.Date.class );
			}
		}
		return Context.toType( jsValue, Object.class );
	}
}