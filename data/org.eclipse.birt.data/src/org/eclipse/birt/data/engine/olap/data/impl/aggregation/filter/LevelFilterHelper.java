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

package org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.CubeQueryExecutorHelper;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.ISelection;
import org.eclipse.birt.data.engine.olap.data.impl.Constants;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.DimensionRow;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.util.BufferedPrimitiveDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.CompareUtil;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.OrderedDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.SetUtil;
import org.eclipse.birt.data.engine.olap.util.filter.IJSDimensionFilterHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFilterHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IJSTopBottomFilterHelper;

/**
 * 
 */

public class LevelFilterHelper
{

	private Dimension dimension;
	private IDiskArray dimPosition;
	private List levelFilters;

	/**
	 * @throws IOException
	 * @throws DataException
	 * 
	 */
	public LevelFilterHelper( Dimension dimension, List levelFilters )
			throws DataException, IOException
	{
		this.dimension = dimension;
		this.levelFilters = levelFilters;
		populatePositions( );
	}

	/**
	 * 
	 * @param jsFilters
	 * @param isBreakHierarchy
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	public IDiskArray getJSFilterResult( List jsFilters,
			boolean isBreakHierarchy ) throws DataException, IOException
	{
		List dimFilterList = new ArrayList( );
		List topBottomfilterList = new ArrayList( );
		for ( int j = 0; j < jsFilters.size( ); j++ )
		{
			Object filterHelper = jsFilters.get( j );
			if ( filterHelper instanceof IJSDimensionFilterHelper )
			{
				dimFilterList.add( filterHelper );
			}
			else if ( filterHelper instanceof IJSTopBottomFilterHelper )
			{
				topBottomfilterList.add( filterHelper );
			}
		}

		IDiskArray result = getDimFilterPositions( dimFilterList );
		if ( topBottomfilterList.isEmpty( ) )
		{
			return result;
		}
		else
		{// top/bottom dimension filters
			IDiskArray result2 = getTopbottomFilterPositions( topBottomfilterList,
					isBreakHierarchy );
			return SetUtil.getIntersection( result, result2 );
		}
	}

	/**
	 * @param dimFilterList
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	private IDiskArray getDimFilterPositions( List dimFilterList )
			throws IOException, DataException
	{
		IDiskArray result = new BufferedPrimitiveDiskArray( Math.min( dimPosition.size( ),
				Constants.LIST_BUFFER_SIZE ) );
		for ( int i = 0; i < dimPosition.size( ); i++ )
		{
			Integer pos = (Integer) dimPosition.get( i );
			if ( isDimPositionSelected( pos.intValue( ), dimFilterList ) )
				result.add( pos );
		}
		return result;
	}

	/**
	 * get the top/bottom filter selected dimension positions.
	 * 
	 * @param topBottomfilterList
	 * @param isBreakHierarchy
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	private IDiskArray getTopbottomFilterPositions( List filterList,
			boolean isBreakHierarchy ) throws IOException, DataException
	{
		IDiskArray result = null;
		ILevel[] levels = dimension.getHierarchy( ).getLevels( );
		for ( int i = 0; i < filterList.size( ); i++ )
		{
			IJSTopBottomFilterHelper filter = (IJSTopBottomFilterHelper) filterList.get( i );
			List dimValueArrayList = evaluateFilter( levels,
					filter,
					isBreakHierarchy );
			IDiskArray dimPositionArray = fetchDimPositions( dimValueArrayList,
					filter );
			if ( result == null )
			{
				result = dimPositionArray;
			}
			else
			{
				result = SetUtil.getIntersection( result, dimPositionArray );
			}
		}
		return result == null ? dimPosition : result;
	}

	/**
	 * get all selected dimension positions.
	 * 
	 * @param dimValueArrayList
	 * @param filterHelper
	 * @param dimPositionArray
	 * @return
	 * @throws IOException
	 */
	private IDiskArray fetchDimPositions( List dimValueArrayList,
			IJSTopBottomFilterHelper filterHelper ) throws IOException
	{
		// final selection positions
		IDiskArray dimPositionArray = new OrderedDiskArray( );
		for ( Iterator itr = dimValueArrayList.iterator( ); itr.hasNext( ); )
		{
			IDiskArray dimValues = (IDiskArray) itr.next( );
			int size = dimValues.size( );
			int start = 0;
			int end = size;
			if ( filterHelper.isPercent( ) )
			{
				int n = FilterUtil.getTargetN( size, filterHelper.getN( ) );
				if ( filterHelper.isTop( ) )
					start = size - n;
				else
					end = n;
			}
			for ( int j = start; j < end; j++ )
			{
				ValueObject aggrValue = (ValueObject) dimValues.get( j );
				IntRange range = (IntRange) aggrValue.index;
				for ( int k = range.start; k <= range.end; k++ )
				{
					dimPositionArray.add( new Integer( k ) );
				}
			}
		}
		return dimPositionArray;
	}

	/**
	 * 
	 * @param pos
	 * @param dimFilterList
	 * @throws IOException
	 * @throws DataException
	 */
	private boolean isDimPositionSelected( int pos, List dimFilterList )
			throws IOException, DataException
	{
		DimensionRow dimRow = dimension.getRowByPosition( pos );
		RowForFilter rowForFilter = getRowForFilter( dimRow );
		for ( int j = 0; j < dimFilterList.size( ); j++ )
		{
			IJSDimensionFilterHelper filterHelper = (IJSDimensionFilterHelper) dimFilterList.get( j );
			if ( !filterHelper.evaluateFilter( rowForFilter ) )
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * evaluate the filter to dimension positions in <code>dimPosition</code>
	 * and store the evaluate result to <code>dimValueArrayList</code>, which
	 * contains one or multiple IDiskArray instances.
	 * 
	 * @param levels
	 * @param filter
	 * @param isBreakHierarchy
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private List evaluateFilter( ILevel[] levels,
			IJSTopBottomFilterHelper filter, boolean isBreakHierarchy )
			throws DataException, IOException
	{
		List dimValueArrayList = new ArrayList( ); // 
		// get target level index, also equals to the length of parent levels
		int index = FilterUtil.getTargetLevelIndex( levels,
				filter.getTargetLevel( ).getLevelName( ) );

		Member[] preMembers = null;
		Object[] preValue = null;
		IDiskArray dimValueArray = null;
		int n = -1;
		if ( filter.isPercent( ) == false )
		{
			n = (int) filter.getN( );
		}
		// when using break hierarchy mode, it applies top/bottom filters to the
		// whole
		// dimension values of specified level; otherwise, it should apply them
		// to separate
		// parent levels
		if ( isBreakHierarchy )
		{
			dimValueArray = new OrderedDiskArray( n, filter.isTop( ) );
			dimValueArrayList.add( dimValueArray );
		}
		IntRange range = null;
		// first-pass filter evaluation, store the qualified values
		// and position index to dimValueArrayList
		for ( int j = 0; j < dimPosition.size( ); j++ )
		{
			Integer pos = (Integer) dimPosition.get( j );
			DimensionRow dimRow = dimension.getRowByPosition( pos.intValue( ) );

			boolean shareParentLevels = preMembers != null
					&& FilterUtil.shareParentLevels( dimRow.getMembers( ),
							preMembers,
							index );
			if ( isBreakHierarchy == false )
			{// maintain the dimension hierarchy
				if ( shareParentLevels )
				{
					dimValueArray = (IDiskArray) dimValueArrayList.get( dimValueArrayList.size( ) - 1 );
				}
				else
				{
					dimValueArray = new OrderedDiskArray( n, filter.isTop( ) );
					dimValueArrayList.add( dimValueArray );
				}
			}
			preMembers = dimRow.getMembers( );
			Object[] levelValue = dimRow.getMembers( )[index].getKeyValues( );
			if ( preValue == null
					|| shareParentLevels == false
					|| CompareUtil.compare( preValue, levelValue ) != 0 )
			{
				RowForFilter rowForFilter = getRowForFilter( dimRow );
				Object value = filter.evaluateFilterExpr( rowForFilter );
				range = new IntRange( pos.intValue( ), pos.intValue( ) );
				dimValueArray.add( new ValueObject( value, range ) );
			}
			else
			{
				range.end = pos.intValue( );
			}
			preValue = levelValue;
		}
		return dimValueArrayList;
	}

	/**
	 * generate a RowForFilter instance for IJsFilter to evaluate.
	 * 
	 * @param dimRow
	 * @return
	 */
	private RowForFilter getRowForFilter( DimensionRow dimRow )
	{
		String[] fieldNames = getAllFieldNames( );
		RowForFilter rowForFilter = new RowForFilter( fieldNames );
		// fill values for this row
		List fields = new ArrayList( );
		for ( int i = 0; i < dimRow.getMembers( ).length; i++ )
		{
			if ( dimRow.getMembers( )[i].getKeyValues( ) != null )
			{
				for ( int j = 0; j < dimRow.getMembers( )[i].getKeyValues( ).length; j++ )
				{
					fields.add( dimRow.getMembers( )[i].getKeyValues( )[j] );
				}
			}
			if ( dimRow.getMembers( )[i].getAttributes( ) != null )
			{
				for ( int j = 0; j < dimRow.getMembers( )[i].getAttributes( ).length; j++ )
				{
					fields.add( dimRow.getMembers( )[i].getAttributes( )[j] );
				}
			}
		}
		rowForFilter.setFieldValues( fields.toArray( ) );
		return rowForFilter;
	}

	/**
	 * 
	 * @param dimension
	 * @return
	 */
	private String[] getAllFieldNames( )
	{
		ILevel[] levels = dimension.getHierarchy( ).getLevels( );
		List fieldNameList = new ArrayList( );
		for ( int i = 0; i < levels.length; i++ )
		{
			String[] keyNames = levels[i].getKeyNames( );
			if ( keyNames != null )
			{
				for ( int j = 0; j < keyNames.length; j++ )
				{
					fieldNameList.add( CubeQueryExecutorHelper.getAttrReference( dimension.getName( ),
							levels[i].getName( ),
							keyNames[j] ) );
				}
			}

			String[] attrNames = levels[i].getAttributeNames( );
			if ( attrNames != null )
			{
				for ( int j = 0; j < attrNames.length; j++ )
				{
					fieldNameList.add( CubeQueryExecutorHelper.getAttrReference( dimension.getName( ),
							levels[i].getName( ),
							attrNames[j] ) );
				}
			}
		}
		String[] fieldNames = new String[fieldNameList.size( )];
		fieldNameList.toArray( fieldNames );
		return fieldNames;
	}

	/**
	 * @param validFilterMap
	 * @return
	 * @throws IOException
	 */
	private IDiskArray populateValidPositions( Map validFilterMap )
			throws IOException
	{
		IDiskArray selectedPositions = new BufferedPrimitiveDiskArray( );
		ILevel[] levels = dimension.getHierarchy( ).getLevels( );
		for ( int i = 0; i < dimension.length( ); i++ )
		{
			DimensionRow row = (DimensionRow) dimension.getRowByPosition( i );
			Member[] curMembers = row.getMembers( );
			// the filters in different level will be intersected in our
			// definition, so that if current position i is selected by all
			// filters, it will be put into the final disk array.
			boolean isSelectedByAll = true;
			for ( Iterator levelItr = validFilterMap.keySet( ).iterator( ); levelItr.hasNext( ); )
			{
				String levelName = (String) levelItr.next( );
				boolean isSelectedByAny = false;
				// filters with the same level name will be united
				List filterList = (List) validFilterMap.get( levelName );
				assert filterList.size( ) > 0;
				LevelFilter firstFilter = (LevelFilter) filterList.get( 0 );
				int targetIndex = FilterUtil.getTargetLevelIndex( levels,
						firstFilter.getLevelName( ) );
				assert targetIndex >= 0;
				for ( Iterator filterItr = filterList.iterator( ); filterItr.hasNext( ); )
				{
					LevelFilter filter = (LevelFilter) filterItr.next( );
					Member[] dimMembers = filter.getDimMembers( );
					if ( dimMembers == null
							|| FilterUtil.shareParentLevels( curMembers,
									dimMembers,
									targetIndex ) )
					{
						ISelection[] selectins = filter.getSelections( );
						for ( int k = 0; k < selectins.length; k++ )
						{
							if ( selectins[k].isSelected( curMembers[targetIndex].getKeyValues( ) ) )
							{
								isSelectedByAny = true;
								break;
							}
						}
					}
					if ( isSelectedByAny )
						break;
				}
				if ( isSelectedByAny == false )
				{
					isSelectedByAll = false;
					break;
				}
			}
			if ( isSelectedByAll )
			{
				selectedPositions.add( new Integer( i ) );
			}
		}
		return selectedPositions;
	}

	/**
	 * 
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private void populatePositions( ) throws DataException, IOException
	{
		if ( levelFilters.isEmpty( ) )
		{
			this.dimPosition = dimension.findAll( );
		}

		Map validFilterMap = getValidFilterMap( );

		if ( validFilterMap.isEmpty( ) )
		{// no filter for this dimension
			this.dimPosition = dimension.findAll( );
		}

		this.dimPosition = populateValidPositions( validFilterMap );
	}

	/**
	 * @return
	 */
	private Map getValidFilterMap( )
	{
		Map validFilterMap = new HashMap( );
		for ( Iterator i = levelFilters.iterator( ); i.hasNext( ); )
		{// just collect current dimension's filters
			LevelFilter filter = (LevelFilter) i.next( );
			if ( filter.getDimensionName( ).equals( dimension.getName( ) ) )
			{
				String keyName = createLevelKey( filter, filter.getLevelName( ) );
				// put level filters with different IJSFilterHelpers into
				// different group so that they can be intersected later.
				// For level filters in the same group (one entry in the map),
				// they will be united.

				addFilter( validFilterMap, filter, keyName );
			}
		}
		return validFilterMap;
	}

	/**
	 * @param filter
	 * @param levelName
	 * @return
	 */
	private String createLevelKey( LevelFilter filter, String levelName )
	{
		IJSFilterHelper filterHelper = filter.getFilterHelper( );
		if ( filterHelper != null )
		{
			levelName = levelName + '_' + filterHelper.hashCode( );
		}
		return levelName;
	}

	/**
	 * @param validFilterMap
	 * @param filter
	 * @param levelName
	 */
	private void addFilter( Map validFilterMap, LevelFilter filter,
			String levelName )
	{
		List filterList = null;
		if ( validFilterMap.containsKey( levelName ) )
		{
			filterList = (List) validFilterMap.get( levelName );
		}
		else
		{
			filterList = new ArrayList( );
			validFilterMap.put( levelName, filterList );
		}
		filterList.add( filter );
	}

}

/**
 * 
 */
class IntRange
{

	int start;
	int end;

	IntRange( int start, int end )
	{
		this.start = start;
		this.end = end;
	}
}
