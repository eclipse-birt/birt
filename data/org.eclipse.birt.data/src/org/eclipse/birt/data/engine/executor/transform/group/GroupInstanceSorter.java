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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor;
import org.eclipse.birt.data.engine.executor.transform.OrderingInfo;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;

/**
 * The class which is used to do group instance sortings.
 */
class GroupInstanceSorter
{

	private ResultSetPopulator populator;

	private GroupProcessorManager groupProcessor;

	GroupInstanceSorter( GroupProcessorManager gp )
	{
		this.populator = gp.getResultSetPopulator( );
		this.groupProcessor = gp;
	}

	/**
	 * Dealing with group sorting jobs.
	 * 
	 * @param cx
	 * @throws DataException
	 */
	void doGroupSorting( Context cx ) throws DataException
	{
		List groupLevels = new ArrayList( );
		List expressionList = new ArrayList( );
		populateGroupSortExpressions( expressionList, groupLevels );
		if ( expressionList.size( ) > 0 )
		{
			this.groupProcessor.calculateExpressionList( expressionList,
					groupLevels,
					IExpressionProcessor.SORT_ON_GROUP_EXPR );
			// The groupArray gotten here has not be sorted yet.
			List[] groupArray = this.groupProcessor.getGroupCalculationUtil( )
					.getGroupInformationUtil( )
					.getGroupBoundaryInfos( );
			populateGroupBoundaryInfosSortings( cx, groupArray );
			this.groupProcessor.getGroupCalculationUtil( )
					.sortGroupBoundaryInfos( groupArray );
			OrderingInfo odInfo = this.groupProcessor.getGroupCalculationUtil( )
					.getGroupInformationUtil( )
					.getOrderingInfo( groupArray );

			this.populator.reSetSmartCacheUsingOrderingInfo( odInfo );
		}
	}

	/**
	 * 
	 * @param expressionList
	 * @param groupLevels
	 */
	void populateGroupSortExpressions( List expressionList, List groupLevels )
	{
		for ( int i = 0; i < this.populator.getQuery( ).getGrouping( ).length; i++ )
		{
			List groupSorts = this.populator.getQuery( ).getGrouping( )[i].getSorts( );
			String name = this.populator.getQuery( ).getGrouping( )[i].getName( );
			if ( groupSorts == null )
				continue;
			for ( int j = 0; j < groupSorts.size( ); j++ )
			{
				IBaseExpression expr = ( (ISortDefinition) groupSorts.get( j ) ).getExpression( ); 
				expr.setGroupName( name );
				expressionList.add( expr );
				groupLevels.add( new Integer( i + 1 ) );
			}
		}
	}

	/**
	 * Populate the sortings in GroupBoundaryInfos instance. Each
	 * GroupBoundaryInfos instance consists of several GroupBoundaryInfo
	 * instances.
	 * 
	 * @param cx
	 * @param groupArray
	 * @throws DataException
	 */
	void populateGroupBoundaryInfosSortings( Context cx, List[] groupArray )
			throws DataException
	{
		for ( int i = 0; i < this.populator.getQuery( ).getGrouping( ).length; i++ )
		{
			// The sorts of certain group
			List groupSorts = this.populator.getQuery( ).getGrouping( )[i].getSorts( );
			if ( groupSorts == null || groupSorts.size( ) == 0 )
				continue;
			this.populator.getResultIterator( ).first( 0 );
			for ( int j = 0; j < groupArray[i].size( ); j++ )
			{
				populateGroupBoundaryInfoSortings( cx, groupArray, i, j );
			}
		}
	}

	/**
	 * Add sort infos to GroupBoundaryInfo in groupArray.
	 * 
	 * @param cx
	 * @param groupArray
	 * @param groupPosition
	 *            groupPosition = groupLevel - 1; it is 0-based;
	 * @param groupIndex
	 * @throws DataException
	 */
	private void populateGroupBoundaryInfoSortings( Context cx,
			List[] groupArray, int groupPosition, int groupIndex )
			throws DataException
	{
		Object[] sortKeys = new Object[this.populator.getQuery( ).getGrouping( )[groupPosition].getSorts( )
				.size( )];
		boolean[] sortDirections = new boolean[sortKeys.length];
		// populate the sortKeys
		// this.smartCache.moveTo(((GroupBoundaryInfo)groupArray[groupPosition].get(groupIndex)).getStartIndex());
		this.populator.getResultIterator( ).last( groupPosition + 1 );
		for ( int l = 0; l < sortKeys.length; l++ )
		{
			sortKeys[l] = ScriptEvalUtil.evalExpr( ( (ISortDefinition) this.populator.getQuery( )
					.getGrouping( )[groupPosition].getSorts( ).get( l ) ).getExpression( ),
					cx,
					this.groupProcessor.getExpressionProcessor( ).getScope( ),
					"GroupFilter",
					0 );
			sortDirections[l] = ( (ISortDefinition) this.populator.getQuery( )
					.getGrouping( )[groupPosition].getSorts( ).get( l ) ).getSortDirection( ) == ISortDefinition.SORT_ASC
					? true : false;
		}
		( (GroupBoundaryInfo) groupArray[groupPosition].get( groupIndex ) ).setSortCondition( sortKeys,
				sortDirections );
		this.populator.getResultIterator( ).next( );
	}

}
