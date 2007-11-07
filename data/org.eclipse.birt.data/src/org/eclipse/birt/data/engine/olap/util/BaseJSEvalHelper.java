/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.script.OLAPExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.filter.IResultRow;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */

public abstract class BaseJSEvalHelper
{

	protected Scriptable scope;
	protected ICubeQueryDefinition queryDefn;
	protected IBaseExpression expr;
	private List jsObjectPopulators;

	/**
	 * 
	 * @param parentScope
	 * @param queryDefn
	 * @param cx
	 * @param expr
	 * @throws DataException
	 */
	protected void init( Scriptable parentScope,
			ICubeQueryDefinition queryDefn, Context cx, IBaseExpression expr )
			throws DataException
	{
		this.scope = cx.initStandardObjects( );
		this.scope.setParentScope( parentScope );
		this.queryDefn = queryDefn;
		this.expr = expr;
		jsObjectPopulators = new ArrayList( );
		registerJSObjectPopulators( );
		OLAPExpressionCompiler.compile( cx, this.expr );
	}

	/**
	 * Overwrite this method if other Javascript objects are needed to
	 * registered. By default, the dimension Javascript object will be
	 * registered.
	 * 
	 * @throws DataException
	 */
	protected abstract void registerJSObjectPopulators( ) throws DataException;

	/**
	 * 
	 * @param populator
	 * @throws DataException
	 */
	protected void register( IJSObjectPopulator populator )
			throws DataException
	{
		populator.doInit( );
		this.jsObjectPopulators.add( populator );
	}

	/**
	 * 
	 * @param resultRow
	 */
	protected void setResultRow( IResultRow resultRow )
	{
		for ( Iterator i = jsObjectPopulators.iterator( ); i.hasNext( ); )
		{
			IJSObjectPopulator populator = (IJSObjectPopulator) i.next( );
			populator.setResultRow( resultRow );
		}
	}

	/**
	 * clear all initialized javascript objects from the scope.
	 */
	public void close( )
	{
		for ( Iterator i = jsObjectPopulators.iterator( ); i.hasNext( ); )
		{
			IJSObjectPopulator populator = (IJSObjectPopulator) i.next( );
			populator.cleanUp( );
		}
		jsObjectPopulators = null;
	}
}
