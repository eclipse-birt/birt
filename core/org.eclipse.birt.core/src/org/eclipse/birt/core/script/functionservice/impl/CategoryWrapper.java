
/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.core.script.functionservice.impl;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.core.script.functionservice.IScriptFunction;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionCategory;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionContext;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.WrappedException;

/**
 * This class wraps a function that defined by user using java to a scriptable object
 * that processable by Rhino.
 * 
 */

public class CategoryWrapper extends ScriptableObject
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IScriptFunctionCategory category;
	
	/**
	 * Constructor
	 * 
	 * @param category
	 * @throws BirtException
	 */
	public CategoryWrapper( IScriptFunctionCategory category ) throws BirtException
	{
		assert category != null;
		
		this.category = category;
		IScriptFunction[] functions = category.getFunctions( );
		for ( int i = 0; i < functions.length; i++ )
		{
			final IScriptFunction function = functions[i];
			if ( "BirtMath".equals( category.getName( ) )
					&& "multiply".equals( function.getName( ) ) )
			{
				defineFunction( "multiple", function );
			}
			defineFunction( function.getName( ), function );
		}
	}

	@SuppressWarnings("serial")
	private void defineFunction( String funcName, final IScriptFunction function )
	{
		this.defineProperty( funcName, new BaseFunction( ) {

			public Object call( Context cx, Scriptable scope,
					Scriptable thisObj, java.lang.Object[] args )
			{
				Object[] convertedArgs = JavascriptEvalUtil.convertToJavaObjects( args );
				try
				{
					IScriptFunctionContext context = null;
					Object jsObj = scope.get( org.eclipse.birt.core.script.functionservice.IScriptFunctionContext.FUNCITON_BEAN_NAME,
							scope );
					if ( jsObj != org.mozilla.javascript.UniqueTag.NOT_FOUND )
					{
						context = (IScriptFunctionContext) Context.jsToJava( jsObj,
								IScriptFunctionContext.class );
					}
					return function.execute( convertedArgs, context );
				}
				catch ( BirtException e )
				{
					throw new WrappedException( e );
				}

			}
		},0 );
	}
	
	

	@Override
	public String getClassName( )
	{
		return this.category.getName( );
	}
	
	
}
