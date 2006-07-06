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

package org.eclipse.birt.data.engine.executor.transform.group;

import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.executor.cache.ResultSetCache;
import org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Context;

/**
 * 
 */

public class GroupProcessorManager
{

	/** GroupCalculationUtil that is used by this CachedResultSet */
	private GroupCalculationUtil groupCalculationUtil;

	// private ResultSetCache smartCache;

	private ResultSetPopulator populator;

	// private IResultIterator ri;

	private IExpressionProcessor exprProcessor;

	/**
	 * 
	 * @param query
	 * @param ri
	 * @param rsMeta
	 * @param populator
	 * @throws DataException
	 */
	public GroupProcessorManager( BaseQuery query, IResultIterator ri,
			IResultClass rsMeta, ResultSetPopulator populator )
			throws DataException
	{
		this.populator = populator;
		this.groupCalculationUtil = new GroupCalculationUtil(query, populator
				.getResultSetMetadata(), this.populator);
	}

	/**
	 * Do group filtering and Sorting job.
	 * 
	 * @throws DataException
	 */
	public void doGroupFiltering( ResultSetCache rsCache,
			IExpressionProcessor exprProc )
			throws DataException
	{
		this.populator.setCache( rsCache );

		this.exprProcessor = exprProc;
		exprProcessor.setResultIterator( this.populator.getResultIterator( ) );

		Context cx = Context.enter( );
		try
		{
			new GroupInstanceFilter( this ).doGroupFiltering( cx );
			//new GroupInstanceSorter( this ).doGroupSorting( cx );
		}
		finally
		{
			Context.exit( );
		}
	}
	
	/**
	 * Do group filtering and Sorting job.
	 * 
	 * @throws DataException
	 */
	public void doGroupSorting( ResultSetCache rsCache,
			IExpressionProcessor exprProc )
			throws DataException
	{
		this.populator.setCache( rsCache );

		this.exprProcessor = exprProc;
		exprProcessor.setResultIterator( this.populator.getResultIterator( ) );

		Context cx = Context.enter( );
		try
		{
			new GroupInstanceSorter( this ).doGroupSorting( cx );
		}
		finally
		{
			Context.exit( );
		}
	}

	/**
	 * Calculate the expression list.
	 * 
	 * @param expressionList
	 * @throws DataException
	 */
	void calculateExpressionList( List expressionList,
			List groupLevelList, int type ) throws DataException
	{
		int[] groupLevelArray = new int[groupLevelList.size( )];
		for ( int i = 0; i < groupLevelList.size( ); i++ )
		{
			groupLevelArray[i] = ( (Integer) groupLevelList.get( i ) ).intValue( );
		}
		this.exprProcessor.evaluateMultiPassExprOnGroup( expressionList.toArray( ),
				groupLevelArray,
				type );
	}

	/**
	 * 
	 * @return
	 */
	public IExpressionProcessor getExpressionProcessor( )
	{
		return this.exprProcessor;
	}

	/**
	 * 
	 * @return
	 */
	public ResultSetPopulator getResultSetPopulator( )
	{
		return this.populator;
	}

	/**
	 * 
	 * @return
	 */
	public GroupCalculationUtil getGroupCalculationUtil( )
	{
		return this.groupCalculationUtil;
	}
}
