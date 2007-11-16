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

package org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort;

import java.io.IOException;
import java.util.List;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;

/**
 * 
 */

public class AggrSortHelper
{

	/**
	 * 
	 * @param sorts
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	public static void sort( List sorts, IAggregationResultSet[] resultSet )
			throws DataException
	{
		assert sorts != null && sorts.size( ) > 1;
		try
		{
			ITargetSort[] targetSorts = new ITargetSort[sorts.size( )];
			sorts.toArray( targetSorts );
			int baseIndex = getBaseResultSetIndex( resultSet, targetSorts[0].getTargetLevel( ) );
			IAggregationResultSet[] targetResultSet = new IAggregationResultSet[sorts.size( )];
			for ( int i = 0; i < targetSorts.length; i++ )
			{
				if ( targetSorts[i] instanceof AggrSortDefinition )
				{
					AggrSortDefinition sortDefn = (AggrSortDefinition) targetSorts[i];
					final DimLevel[] aggrLevels = sortDefn.getAggrLevels( );
					if ( aggrLevels == null )
					{
						targetResultSet[i] = resultSet[baseIndex];
					}
					else
					{
						targetResultSet[i] = getMatchedResultSet( resultSet, aggrLevels );
					}
				}
			}
			IAggregationResultSet result = AggregationSortHelper.sort( resultSet[baseIndex],
					targetSorts,
					targetResultSet );
			resultSet[baseIndex].close( );
			resultSet[baseIndex] = result;
		}
		catch ( IOException e )
		{
			throw new DataException( e.getLocalizedMessage( ) );
		}
	}

	/**
	 * 
	 * @param resultSet
	 * @param level
	 * @return
	 * @throws DataException 
	 */
	private static int getBaseResultSetIndex(
			IAggregationResultSet[] resultSet, DimLevel level )
			throws DataException
	{
		for ( int i = 0; i < resultSet.length; i++ )
		{
			if ( isEdgeResultSet( resultSet[i] )
					&& ( resultSet[i].getLevelIndex( level ) >= 0 ) )
			{
				return i;
			}
		}
		throw new DataException( "Can't find the base aggregation result set for the target level:", level );//$NON-NLS-1$
	}

	/**
	 * A result set would come to be an edge result set only if its aggregation
	 * function is null.
	 * 
	 * @param resultSet
	 * @return
	 */
	private static boolean isEdgeResultSet( IAggregationResultSet resultSet )
	{
		return ( resultSet.getAggregationDefinition( ) == null || resultSet.getAggregationDefinition( )
				.getAggregationFunctions( ) == null );
	}

	/**
	 * 
	 * @param resultSet
	 * @param levelNames
	 * @return
	 * @throws DataException
	 */
	private static IAggregationResultSet getMatchedResultSet(
			IAggregationResultSet[] resultSet, DimLevel[] levelNames )
			throws DataException
	{
		for ( int i = 0; i < resultSet.length; i++ )
		{
			IAggregationResultSet rSet = resultSet[i];
			if ( levelNames.length != rSet.getLevelCount( ) )
				continue;
			boolean match = true;
			for ( int j = 0; j < rSet.getLevelCount( ); j++ )
			{
				if ( !levelNames[j].equals( rSet.getLevel( j ) ) )
				{
					match = false;
					break;
				}
			}
			if ( match )
			{
				if ( !isEdgeResultSet( rSet ) )
					return rSet;
			}
		}
		throw new DataException( ResourceConstants.INVALID_SORT_DEFN );
	}
}
