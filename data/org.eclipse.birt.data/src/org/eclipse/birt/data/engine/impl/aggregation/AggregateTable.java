/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.engine.impl.aggregation;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * A registry of aggregate expressions. Stores all aggregate expressions that appears
 * in a report query or subquery
 */
public final class AggregateTable
{
	/** Array of AggrExprInfo objects to record all aggregates */
	private List aggrExprInfoList;
	
	/** */
	private List groupDefns;
	private Scriptable scope;
	
	/** the base query contains aggregate */
	private BaseQuery baseQuery;
		
	/**
	 * Used for de-serialization
	 */
	public AggregateTable( )
	{
		this.aggrExprInfoList = new ArrayList( );
	}

	/**
	 * construct the aggregateTable from preparedQuery
	 * 
	 * @param query
	 */
	public AggregateTable( Scriptable scope, List groupDefns )
	{
		this( );
		
		this.groupDefns = groupDefns;
		this.scope = scope;
	}

	/**
	 * construct the aggregateTable from baseQuery
	 * 
	 * @param query
	 */
	public AggregateTable( BaseQuery query )
	{
		this( );
		
		this.baseQuery = query;
	}
	
	/**
	 * Returns an implementation of the AggregateRegistry interface used by
	 * ExpressionCompiler, to register aggregate expressions at the specified
	 * grouping level
	 * 
	 * @param groupLevel
	 * @param afterGroup
	 * @param cx
	 * @return
	 */
	public AggregateRegistry getAggrRegistry( int groupLevel,
			boolean isDetailedRow, Context cx )
	{
		AggrRegistry aggrRegistry = new AggrRegistry( groupLevel,
				isDetailedRow,
				cx );
		aggrRegistry.prepare( groupDefns, scope, baseQuery, aggrExprInfoList );
		return aggrRegistry;
	}
	
	/**
	 * @return
	 */
	List getAggrExprInfoList( )
	{
		return this.aggrExprInfoList;
	}
	
}