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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
	 * @param resultSet
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	public static IAggregationResultSet sort( List sorts,
			IAggregationResultSet[] resultSet ) throws DataException
	{
		try
		{
			Map map = new LinkedHashMap( );
			for ( Iterator it = sorts.iterator( ); it.hasNext( ); )
			{
				AggrSortDefinition sort = (AggrSortDefinition) it.next( );
				if ( map.get( sort.getTargetLevel( ) ) == null )
				{
					List temp = new ArrayList( );
					temp.add( sort );
					map.put( sort.getTargetLevel( ), temp );
				}
				else
				{
					( (List) map.get( sort.getTargetLevel( ) ) ).add( sort );
				}
			}

			SortKey[] sortKeys = new SortKey[map.size( )];
			Object[] keys = map.keySet( ).toArray( );
			AxisQualifier[] qualifiers = new AxisQualifier[keys.length];
			for ( int n = 0; n < keys.length; n++ )
			{
				List aggrSorts = (List) map.get( keys[n] );
				IAggregationResultSet matchedResultSet = getMatchedResultSet( resultSet,
						( (AggrSortDefinition) aggrSorts.get( 0 ) ).getAggrLevels( ) );
				DimLevel targetLevelName = ( (AggrSortDefinition) aggrSorts.get( 0 ) ).getTargetLevel( );
				DimLevel[] levelNames = ( (AggrSortDefinition) aggrSorts.get( 0 ) ).getAxisQualifierLevel( );
				int[] levelIndex = new int[levelNames.length];
				for ( int k = 0; k < levelIndex.length; k++ )
				{
					levelIndex[k] = matchedResultSet.getLevelIndex( levelNames[k] );
				}
				qualifiers[n] = new AxisQualifier( levelIndex,
						( (AggrSortDefinition) aggrSorts.get( 0 ) ).getAxisQualifierValue( ) );
				int[] aggrIndex = new int[aggrSorts.size( )];
				boolean[] aggrDir = new boolean[aggrSorts.size( )];
				for ( int i = 0; i < aggrSorts.size( ); i++ )
				{
					AggrSortDefinition sort = (AggrSortDefinition) aggrSorts.get( i );

					aggrIndex[i] = matchedResultSet.getAggregationIndex( sort.getAggrName( ) );

					aggrDir[i] = sort.getDirection( );
				}

				int targetLevelOffset = 0;
				if ( qualifiers[n].getLevelIndex( ).length > 0 )
				{
					if ( qualifiers[n].getLevelIndex( )[0] == 0 )
						targetLevelOffset = qualifiers[n].getLevelIndex( ).length;
				}
				sortKeys[n] = new SortKey( aggrIndex,
						aggrDir,
						matchedResultSet.getLevelIndex( targetLevelName ),
						targetLevelOffset,
						matchedResultSet );
			}

			IAggregationResultSet base = null;
			for ( int i = 0; i < resultSet.length; i++ )
			{
				if ( isEdgeResultSet( resultSet[i] )
						&& ( resultSet[i].getLevelIndex( (DimLevel)keys[0] ) >= 0 ) )
				{
					base = resultSet[i];
					break;
				}
			}

			return AggregationSortHelper.sort( base, qualifiers, sortKeys );
		}
		catch ( IOException e )
		{
			throw new DataException( e.getLocalizedMessage( ) );
		}
	}

	/**
	 * A result set would come to be an edge result set only if its aggregation function is null.
	 * @param resultSet
	 * @return
	 */
	public static boolean isEdgeResultSet( IAggregationResultSet resultSet )
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
