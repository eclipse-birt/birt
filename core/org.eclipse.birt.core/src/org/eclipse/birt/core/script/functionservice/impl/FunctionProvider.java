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
import org.eclipse.birt.core.script.functionservice.IScriptFunction;
import org.eclipse.birt.core.script.functionservice.IScriptFunctionCategory;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */

public class FunctionProvider
{
	private static IFunctionProvider provider;

	/**
	 * Set the current function provider impl.
	 * @param provider
	 */
	public static void setFunctionProvider( IFunctionProvider provider )
	{
		FunctionProvider.provider = provider;
	}
	/**
	 * Return all the categories defined by extensions.
	 * 
	 * @return
	 * @throws BirtException
	 */
	public static IScriptFunctionCategory[] getCategories( )
			throws BirtException
	{
		if( provider!= null )
			return provider.getCategories();
		return new IScriptFunctionCategory[]{};
	}

	/**
	 * Return the functions that defined in a category.
	 * 
	 * @param categoryName
	 * @return
	 * @throws BirtException
	 */
	public static IScriptFunction[] getFunctions( String categoryName )
			throws BirtException
	{
		if( provider!= null )
			return provider.getFunctions( categoryName );
		return new IScriptFunction[0];
	}

	/**
	 * Register script functions to scope.
	 * 
	 * @param cx
	 * @param scope
	 * @throws BirtException
	 */
	public static void registerScriptFunction( Context cx, Scriptable scope )
			throws BirtException
	{
		if( provider != null )
			FunctionProvider.provider.registerScriptFunction( cx, scope);
	}
}
