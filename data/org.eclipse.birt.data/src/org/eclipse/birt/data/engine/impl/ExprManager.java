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
package org.eclipse.birt.data.engine.impl;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;

/**
 * 
 */
public class ExprManager
{
	private Map bindingExprsMap;	
	private Map autoBindingExprMap;
	
	/**
	 *
	 */
	ExprManager( )
	{
		bindingExprsMap = new HashMap( );
		autoBindingExprMap = new HashMap( );
	}
	
	/**
	 * @param resultsExprMap
	 * @param groupLevel
	 */
	void addBindingExpr( Map resultsExprMap, int groupLevel )
	{
		if ( resultsExprMap != null )
			bindingExprsMap.putAll( resultsExprMap );
	}
	
	/**
	 * @param name
	 * @param baseExpr
	 */
	void addAutoBindingExpr( String name, IBaseExpression baseExpr )
	{
		autoBindingExprMap.put( name, baseExpr );
	}
	
	/**
	 * @param name
	 * @return expression for specified name
	 */
	public IBaseExpression getExpr( String name )
	{
		IBaseExpression baseExpr = getBindingExpr( name );
		if ( baseExpr == null )
			baseExpr = getAutoBindingExpr( name );
		
		return baseExpr;
	}
	
	/**
	 * @param name
	 * @return
	 */
	public IBaseExpression getBindingExpr( String name )
	{
		return (IBaseExpression) bindingExprsMap.get( name );
	}
	
	/**
	 * @param name
	 * @return auto binding expression for specified name
	 */
	public IScriptExpression getAutoBindingExpr( String name )
	{
		return (IScriptExpression) this.autoBindingExprMap.get( name );
	}
	
}
