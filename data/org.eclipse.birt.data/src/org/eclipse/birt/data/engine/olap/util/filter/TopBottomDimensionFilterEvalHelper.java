
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
package org.eclipse.birt.data.engine.olap.util.filter;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.util.CompareUtil;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */

public class TopBottomDimensionFilterEvalHelper
		extends
			BaseDimensionFilterEvalHelper implements IJSTopBottomFilterHelper
{
	private double N;
	private int filterType;
	
	/**
	 * @param parentScope
	 * @param queryDefn
	 * @param cubeFilter
	 * @throws DataException
	 */
	public TopBottomDimensionFilterEvalHelper( Scriptable parentScope,
			ICubeQueryDefinition queryDefn, IFilterDefinition cubeFilter )
			throws DataException
	{
		assert cubeFilter!=null;
		Context cx = Context.enter( );
		
		try
		{
			initialize( parentScope, queryDefn, cubeFilter, cx );
			
			populateN( cx );
			popualteFilterType( );
		}
		finally
		{
			Context.exit( );
		}
	}

	/**
	 * 
	 * @param cx
	 * @throws DataException
	 */
	private void populateN( Context cx ) throws DataException
	{
		Object o =  ScriptEvalUtil.evalExpr( ( (IConditionalExpression) expr ).getOperand1( ),
					cx,
					scope,
					null,
					0 );
		this.N = Double.valueOf( o.toString( ) ).doubleValue( );
	}

	/**
	 * 
	 */
	private void popualteFilterType( )
	{
		int type = ((IConditionalExpression)this.expr).getOperator( );
		switch(type)
		{
			case IConditionalExpression.OP_TOP_N:
				this.filterType = IJSTopBottomFilterHelper.TOP_N;
				break;
			case IConditionalExpression.OP_TOP_PERCENT:
				this.filterType = IJSTopBottomFilterHelper.TOP_PERCENT;
				break;
			case IConditionalExpression.OP_BOTTOM_N:
				this.filterType = IJSTopBottomFilterHelper.BOTTOM_N;
				break;
			case IConditionalExpression.OP_BOTTOM_PERCENT:
				this.filterType = IJSTopBottomFilterHelper.BOTTOM_PERCENT;
				break;
			default:
				assert false;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IJSTopBottomFilterHelper#evaluateFilterExpr(org.eclipse.birt.data.engine.olap.util.filter.IResultRow)
	 */
	public Object evaluateFilterExpr( IResultRow resultRow ) throws DataException
	{
		this.dimObj.setResultRow( resultRow );
		this.dataObj.setResultRow( resultRow );
		Context cx = Context.enter( );
		try
		{
			Object result = ScriptEvalUtil.evalExpr( ( (IConditionalExpression) expr ).getExpression( ),
					cx,
					scope,
					null,
					0 );
			return result;
		}
		catch ( InMatchDimensionIndicator e )
		{
			throw new DataException( e.getMessage( ));
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
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IJSTopBottomFilterHelper#getFilterType()
	 */
	public int getFilterType( )
	{
		return this.filterType;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IJSTopBottomFilterHelper#getN()
	 */
	public double getN( )
	{
		return this.N;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IJSTopBottomFilterHelper#isQualifiedRow(org.eclipse.birt.data.engine.olap.util.filter.IResultRow)
	 */
	public boolean isQualifiedRow( IResultRow resultRow ) throws DataException
	{
		if ( this.isAxisFilter )
		{
			for ( int i = 0; i < axisLevels.length; i++ )
			{
				DimLevel level = new DimLevel( axisLevels[i] );
				if ( CompareUtil.compare( resultRow.getFieldValue( level.toString( ) ),
						axisValues[i] ) != 0 )
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IJSTopBottomFilterHelper#isAxisFilter()
	 */
	public boolean isAxisFilter( )
	{
		return this.isAxisFilter;
	}
}
