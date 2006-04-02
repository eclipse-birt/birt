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

/**
 * 
 */
public class ExprManager
{
	//private Map exprsMap;
	private Map allExprsMap;
	
	/**
	 *
	 */
	ExprManager( )
	{
		//exprsMap = new HashMap( );
		allExprsMap = new HashMap( );
	}
	
	/**
	 * @param resultsExprMap
	 * @param groupLevel
	 */
	void addExpr( Map resultsExprMap, int groupLevel )
	{
		//exprsMap.put( new Integer( groupLevel ), resultsExprMap );
		
		if ( resultsExprMap != null )
			allExprsMap.putAll( resultsExprMap );	
	}
	
	/**
	 * @param name
	 * @return
	 */
	public IBaseExpression getExpr( String name )
	{
		return (IBaseExpression) allExprsMap.get( name );
	}
	
}
