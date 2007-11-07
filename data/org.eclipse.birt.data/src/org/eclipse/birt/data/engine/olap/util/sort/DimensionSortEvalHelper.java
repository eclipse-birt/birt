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

package org.eclipse.birt.data.engine.olap.util.sort;

import java.util.Set;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.util.DimensionJSEvalHelper;
import org.eclipse.birt.data.engine.olap.util.IJSObjectPopulator;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.filter.IResultRow;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */

public class DimensionSortEvalHelper extends DimensionJSEvalHelper
		implements
			IJSSortHelper
{

	protected ISortDefinition sortDefinition;
	private DimLevel targetLevel;

	public DimensionSortEvalHelper( Scriptable parentScope,
			ICubeQueryDefinition queryDefn, ISortDefinition sortDefinition )
			throws DataException
	{
		assert sortDefinition != null;
		Context cx = Context.enter( );
		try
		{
			initialize( parentScope, queryDefn, sortDefinition, cx );
		}
		finally
		{
			Context.exit( );
		}
	}

	/**
	 * 
	 * @param parentScope
	 * @param queryDefn
	 * @param sortDefinition
	 * @param cx
	 * @throws DataException
	 */
	protected void initialize( Scriptable parentScope,
			ICubeQueryDefinition queryDefn, ISortDefinition sortDefinition,
			Context cx ) throws DataException
	{
		super.init( parentScope, queryDefn, cx, sortDefinition.getExpression( ) );
		this.sortDefinition = sortDefinition;
	}

	/**
	 * 
	 */
	public Object evaluate( IResultRow resultRow ) throws DataException
	{
		super.setResultRow( resultRow );

		Context cx = Context.enter( );
		try
		{
			return ScriptEvalUtil.evalExpr( expr, cx, scope, null, 0 );
		}
		catch ( IJSObjectPopulator.InMatchDimensionIndicator e )
		{
			return null;
		}
		catch ( BirtException e )
		{
			throw DataException.wrap( e );
		}
		finally
		{
			Context.exit( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.util.sort.IJSSortHelper#getSortDefinition()
	 */
	public ISortDefinition getSortDefinition( )
	{
		return this.sortDefinition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.util.sort.IJSSortHelper#getTargetLevel()
	 */
	public DimLevel getTargetLevel( )
	{
		if ( this.targetLevel == null )
		{
			Set set = null;
			try
			{
				set = OlapExpressionCompiler.getReferencedDimLevel( expr, queryDefn.getBindings( ) );
			}
			catch ( DataException e )
			{
				return null;
			}
			if ( set != null && !set.isEmpty( ) )
			{
				this.targetLevel = (DimLevel) set.iterator( ).next( );
			}
		}
		return this.targetLevel;
	}
}
