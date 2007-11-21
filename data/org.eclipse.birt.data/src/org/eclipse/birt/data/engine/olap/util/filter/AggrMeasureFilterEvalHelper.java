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

package org.eclipse.birt.data.engine.olap.util.filter;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.util.DataJSObjectPopulator;
import org.eclipse.birt.data.engine.olap.util.DimensionJSEvalHelper;
import org.eclipse.birt.data.engine.olap.util.IJSObjectPopulator;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */

public class AggrMeasureFilterEvalHelper extends DimensionJSEvalHelper
		implements
			IAggrMeasureFilterEvalHelper
{
	/**
	 * 
	 * @param scope
	 * @param queryDefn
	 * @param cubeFilter
	 * @throws DataException
	 */
	public AggrMeasureFilterEvalHelper( Scriptable scope,ICubeQueryDefinition queryDefn, IFilterDefinition cubeFilter) throws DataException
	{
		assert cubeFilter != null;
		Context cx = Context.enter( );
		try
		{
			super.init( scope, queryDefn, cx, cubeFilter.getExpression( ) );
		}
		finally
		{
			Context.exit( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IAggrMeasureFilterEvalHelper#evaluateFilter(org.eclipse.birt.data.engine.olap.util.filter.IResultRow)
	 */
	public boolean evaluateFilter( IResultRow resultRow ) throws DataException
	{
		super.setData( resultRow );

		Context cx = Context.enter( );
		try
		{
			Object result = ScriptEvalUtil.evalExpr( expr, cx, scope, null, 0 );
			return DataTypeUtil.toBoolean( result ).booleanValue( );
		}
		catch ( IJSObjectPopulator.InMatchDimensionIndicator e )
		{
			return true;
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
	 * @see org.eclipse.birt.data.engine.olap.util.DimensionJSEvalHelper#registerJSObjectPopulators()
	 */
	protected void registerJSObjectPopulators( ) throws DataException
	{
		super.registerJSObjectPopulators( );
		register( new DataJSObjectPopulator( scope,
				queryDefn.getBindings( ),
				true ) );
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IAggrMeasureFilterEvalHelper#getExpression()
	 */
	public IBaseExpression getExpression( )
	{
		return this.expr;
	}
	
}
