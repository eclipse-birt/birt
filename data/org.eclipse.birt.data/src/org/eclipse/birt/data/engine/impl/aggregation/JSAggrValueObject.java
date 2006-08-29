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
package org.eclipse.birt.data.engine.impl.aggregation;

import java.util.List;

import org.eclipse.birt.data.engine.aggregation.BuiltInAggregationFactory;
import org.eclipse.birt.data.engine.api.aggregation.IAggregation;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * JS object to retrieve aggregation value.
 */
public class JSAggrValueObject extends ScriptableObject
{
	private int aggrCount;
	private List aggrExprInfoList;
	private IResultIterator odiResult;
	private List[] aggrValues;
	
	/** serialVersionUID = 1L; */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param aggrExprInfoList
	 * @param odiResult
	 * @param aggrValues
	 * @param hasOdiResultDataRows
	 */
	JSAggrValueObject( List aggrExprInfoList, IResultIterator odiResult,
			List[] aggrValues )
	{
		this.aggrExprInfoList = aggrExprInfoList;
		this.odiResult = odiResult;
		this.aggrValues = aggrValues;

		this.aggrCount = aggrExprInfoList.size( );
	}
	
	/*
	 * @see org.mozilla.javascript.Scriptable#getClassName()
	 */
	public String getClassName( )
	{
		return "_RESERVED_AGGR_VALUE";
	}

	/*
	 * @see org.mozilla.javascript.Scriptable#has(int,
	 *      org.mozilla.javascript.Scriptable)
	 */
	public boolean has( int index, Scriptable start )
	{
		return index > 0 && index < this.aggrCount;
	}

	/*
	 * @see org.mozilla.javascript.Scriptable#get(int,
	 *      org.mozilla.javascript.Scriptable)
	 */
	public Object get( int index, Scriptable start )
	{
		if ( index < 0 || index >= this.aggrCount )
		{
			// Should never get here
			return null;
		}

		try
		{
			return getAggregateValue( index );
		}
		catch ( DataException e )
		{
			throw Context.reportRuntimeError( e.getLocalizedMessage( ) );
		}
	}
	
	/**
	 * Get the aggregate value
	 * @param aggrIndex
	 * @return
	 * @throws DataException
	 */
	private Object getAggregateValue( int aggrIndex ) throws DataException
	{
		AggrExprInfo aggrInfo = getAggrInfo( aggrIndex );
				
		if ( this.odiResult.getRowCount( ) == 0 )
		{
			if ( aggrInfo.aggregation.getName( ).equalsIgnoreCase( BuiltInAggregationFactory.TOTAL_COUNT_FUNC)
				 ||	aggrInfo.aggregation.getName( ).equalsIgnoreCase( BuiltInAggregationFactory.TOTAL_COUNTDISTINCT_FUNC))
				
				return new Integer( 0 );
			else
				return null;
		}

		try
		{
			int groupIndex;

			if ( aggrInfo.aggregation.getType( ) == IAggregation.SUMMARY_AGGR )
			{
				// Aggregate on the whole list: there is only one group
				if ( aggrInfo.groupLevel == 0 )
					groupIndex = 0;
				else
					groupIndex = this.odiResult.getCurrentGroupIndex( aggrInfo.groupLevel );
			}
			else
			{
				groupIndex = this.odiResult.getCurrentResultIndex( );
			}

			return this.aggrValues[aggrIndex].get( groupIndex );

		}
		catch ( DataException e )
		{
			throw e;
		}
	}
	
	/**
	 * Gets information about one aggregate expression in the table, given its
	 * index
	 */
	private AggrExprInfo getAggrInfo( int i )
	{
		return (AggrExprInfo) this.aggrExprInfoList.get( i );
	}
	
	/**
	 * Get aggregation's count
	 * @return
	 */
	int getAggrCount( )
	{
		return this.aggrExprInfoList.size( );
	}

	/**
	 * Get the aggregate value list.
	 * 
	 * @param i
	 * @return
	 */
	public List getAggregateValues( int i )
	{
		if( i < this.aggrCount )
			return aggrValues[i];
		else
			return null;
	}
	
}
