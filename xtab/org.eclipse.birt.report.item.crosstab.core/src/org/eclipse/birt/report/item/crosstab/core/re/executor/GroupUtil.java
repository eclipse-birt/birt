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

package org.eclipse.birt.report.item.crosstab.core.re.executor;

import java.util.ArrayList;
import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;

/**
 * GroupUtil
 */
public class GroupUtil implements ICrosstabConstants
{

	/**
	 * Prevent from instantiation
	 */
	private GroupUtil( )
	{
	}

	/**
	 * Returns the accumulated group index for current level element.
	 * 
	 * @param crosstabItem
	 * @param axisType
	 * @param dimensionIndex
	 * @param levelIndex
	 *            If this is negative(<0), means the last level index in given
	 *            dimension.
	 * @return
	 */
	public static int getGroupIndex( CrosstabReportItemHandle crosstabItem,
			int axisType, int dimensionIndex, int levelIndex )
	{
		List groups = getGroups( crosstabItem, axisType );

		if ( levelIndex < 0 )
		{
			for ( int i = groups.size( ) - 1; i >= 0; i-- )
			{
				EdgeGroup gp = (EdgeGroup) groups.get( i );

				if ( gp.dimensionIndex == dimensionIndex )
				{
					return i;
				}
			}
		}
		else
		{
			for ( int i = 0; i < groups.size( ); i++ )
			{
				EdgeGroup gp = (EdgeGroup) groups.get( i );

				if ( gp.dimensionIndex == dimensionIndex
						&& gp.levelIndex == levelIndex )
				{
					return i;
				}
			}
		}

		return -1;
	}

	/**
	 * Returns the accumulated group index for current level element from given
	 * group list.
	 * 
	 * @param groups
	 * @param dimensionIndex
	 * @param levelIndex
	 *            If this is negative(<0), means the last level index in given
	 *            dimension.
	 * @return
	 */
	public static int getGroupIndex( List groups, int dimensionIndex,
			int levelIndex )
	{
		if ( levelIndex < 0 )
		{
			for ( int i = groups.size( ) - 1; i >= 0; i-- )
			{
				EdgeGroup gp = (EdgeGroup) groups.get( i );

				if ( gp.dimensionIndex == dimensionIndex )
				{
					return i;
				}
			}
		}
		else
		{
			for ( int i = 0; i < groups.size( ); i++ )
			{
				EdgeGroup gp = (EdgeGroup) groups.get( i );

				if ( gp.dimensionIndex == dimensionIndex
						&& gp.levelIndex == levelIndex )
				{
					return i;
				}
			}
		}

		return -1;
	}

	/**
	 * Returns the span between current group to last group.
	 */
	public static int computeGroupSpan( List groups, int dimensionIndex,
			int levelIndex )
	{
		int currentGroup = -1;

		for ( int i = 0; i < groups.size( ); i++ )
		{
			EdgeGroup gp = (EdgeGroup) groups.get( i );

			if ( gp.dimensionIndex == dimensionIndex
					&& gp.levelIndex == levelIndex )
			{
				currentGroup = i;
				break;
			}
		}

		if ( currentGroup == -1 )
		{
			return 1;
		}

		return groups.size( ) - currentGroup - 1;
	}

	/**
	 * Checks if the crosstab has any corresponding aggregation cell defined for
	 * specific level.
	 */
	public static boolean hasTotalContent(
			CrosstabReportItemHandle crosstabItem, int axisType, int dimX,
			int levelX, int meaX )
	{
		if ( !IColumnWalker.IGNORE_TOTAL_COLUMN_WITHOUT_AGGREGATION )
		{
			return true;
		}

		// TODO skip invisible levels

		int mCount = crosstabItem.getMeasureCount( );
		int rdCount = crosstabItem.getDimensionCount( ROW_AXIS_TYPE );
		int cdCount = crosstabItem.getDimensionCount( COLUMN_AXIS_TYPE );

		boolean checkAllMeasure = meaX < 0 || meaX >= mCount;

		int startMeasure = meaX;
		int endMeasure = meaX + 1;

		if ( checkAllMeasure )
		{
			startMeasure = 0;
			endMeasure = mCount;
		}

		if ( startMeasure >= endMeasure )
		{
			return false;
		}

		if ( axisType == COLUMN_AXIS_TYPE )
		{
			if ( rdCount == 0 )
			{
				// default as grand total
				String colDimName = null;
				String colLevelName = null;

				if ( dimX >= 0 && levelX >= 0 )
				{
					// sub total
					DimensionViewHandle cdv = crosstabItem.getDimension( COLUMN_AXIS_TYPE,
							dimX );
					LevelViewHandle clv = cdv.getLevel( levelX );

					colDimName = cdv.getCubeDimensionName( );
					colLevelName = clv.getCubeLevelName( );
				}

				return hasAggregationCell( startMeasure,
						endMeasure,
						crosstabItem,
						null,
						null,
						colDimName,
						colLevelName );
			}
			else
			{
				for ( int i = 0; i < rdCount; i++ )
				{
					DimensionViewHandle rdv = crosstabItem.getDimension( ROW_AXIS_TYPE,
							i );

					for ( int j = 0; j < rdv.getLevelCount( ); j++ )
					{
						LevelViewHandle rlv = rdv.getLevel( j );

						// default as grand total
						String colDimName = null;
						String colLevelName = null;

						if ( dimX >= 0 && levelX >= 0 )
						{
							// sub total
							DimensionViewHandle cdv = crosstabItem.getDimension( COLUMN_AXIS_TYPE,
									dimX );
							LevelViewHandle clv = cdv.getLevel( levelX );

							colDimName = cdv.getCubeDimensionName( );
							colLevelName = clv.getCubeLevelName( );
						}

						if ( hasAggregationCell( startMeasure,
								endMeasure,
								crosstabItem,
								rdv.getCubeDimensionName( ),
								rlv.getCubeLevelName( ),
								colDimName,
								colLevelName ) )
						{
							return true;
						}
					}
				}
			}
		}
		else
		{
			if ( cdCount == 0 )
			{
				// default as grand total
				String rowDimName = null;
				String rowLevelName = null;

				if ( dimX >= 0 && levelX >= 0 )
				{
					// sub total
					DimensionViewHandle rdv = crosstabItem.getDimension( ROW_AXIS_TYPE,
							dimX );
					LevelViewHandle rlv = rdv.getLevel( levelX );

					rowDimName = rdv.getCubeDimensionName( );
					rowLevelName = rlv.getCubeLevelName( );
				}

				return hasAggregationCell( startMeasure,
						endMeasure,
						crosstabItem,
						rowDimName,
						rowLevelName,
						null,
						null );
			}
			else
			{
				for ( int i = 0; i < cdCount; i++ )
				{
					DimensionViewHandle cdv = crosstabItem.getDimension( COLUMN_AXIS_TYPE,
							i );

					for ( int j = 0; j < cdv.getLevelCount( ); j++ )
					{
						LevelViewHandle clv = cdv.getLevel( j );

						// default as grand total
						String rowDimName = null;
						String rowLevelName = null;

						if ( dimX >= 0 && levelX >= 0 )
						{
							// sub total
							DimensionViewHandle rdv = crosstabItem.getDimension( ROW_AXIS_TYPE,
									dimX );
							LevelViewHandle rlv = rdv.getLevel( levelX );

							rowDimName = rdv.getCubeDimensionName( );
							rowLevelName = rlv.getCubeLevelName( );
						}

						if ( hasAggregationCell( startMeasure,
								endMeasure,
								crosstabItem,
								rowDimName,
								rowLevelName,
								cdv.getCubeDimensionName( ),
								clv.getCubeLevelName( ) ) )
						{
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	private static boolean hasAggregationCell( int startMeasure,
			int endMeasure, CrosstabReportItemHandle crosstabItem,
			String rowDimensionName, String rowLevelName,
			String columnDimensionName, String columnLevelName )
	{
		for ( int k = startMeasure; k < endMeasure; k++ )
		{
			AggregationCellHandle cell = crosstabItem.getMeasure( k )
					.getAggregationCell( rowDimensionName,
							rowLevelName,
							columnDimensionName,
							columnLevelName );

			if ( cell != null )
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns a list of groups on specific axis.
	 */
	public static List getGroups( CrosstabReportItemHandle crosstabItem,
			int axisType )
	{
		List groups = new ArrayList( );

		int dimCount = crosstabItem.getDimensionCount( axisType );

		if ( dimCount > 0 )
		{
			// TODO filter invisible levels
			for ( int i = 0; i < dimCount; i++ )
			{
				DimensionViewHandle dv = crosstabItem.getDimension( axisType, i );

				for ( int j = 0; j < dv.getLevelCount( ); j++ )
				{
					groups.add( new EdgeGroup( i, j ) );
				}
			}
		}

		return groups;
	}

	/**
	 * Check if this group is a leaf group, e.g. the innerest non-dummy group.
	 */
	public static boolean isLeafGroup( List groupCursors, int groupIndex )
			throws OLAPException
	{
		for ( int i = groupIndex + 1; i < groupCursors.size( ); i++ )
		{
			DimensionCursor dc = (DimensionCursor) groupCursors.get( i );

			if ( !isDummyGroup( dc ) )
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks if given dimension cusor is associated with a dummy group
	 */
	public static boolean isDummyGroup( DimensionCursor dc )
			throws OLAPException
	{
		// check special edge start/end value for dummy group
		return dc.getEdgeStart( ) == -1 && dc.getEdgeEnd( ) == -1;
	}

	/**
	 * Returns the previous group on specific axis
	 */
	public static EdgeGroup getPreviousGroup( List groups,
			int currentDimensionIndex, int currentLevelIndex )
	{
		int currentGroup = -1;

		for ( int i = 0; i < groups.size( ); i++ )
		{
			EdgeGroup gp = (EdgeGroup) groups.get( i );

			if ( gp.dimensionIndex == currentDimensionIndex
					&& gp.levelIndex == currentLevelIndex )
			{
				currentGroup = i;
				break;
			}
		}

		if ( currentGroup > 0 && currentGroup < groups.size( ) )
		{
			return (EdgeGroup) groups.get( currentGroup - 1 );
		}

		return null;
	}

	/**
	 * Returns the next group on given group list.
	 */
	public static EdgeGroup getNextGroup( List groups,
			int currentDimensionIndex, int currentLevelIndex )
	{
		int currentGroup = -1;

		for ( int i = 0; i < groups.size( ); i++ )
		{
			EdgeGroup gp = (EdgeGroup) groups.get( i );

			if ( gp.dimensionIndex == currentDimensionIndex
					&& gp.levelIndex == currentLevelIndex )
			{
				currentGroup = i;
				break;
			}
		}

		if ( currentGroup >= 0 && currentGroup < groups.size( ) - 1 )
		{
			return (EdgeGroup) groups.get( currentGroup + 1 );
		}

		return null;
	}

	/**
	 * Returns the next group index on given group list.
	 */
	public static int getNextGroupIndex( List groups,
			int currentDimensionIndex, int currentLevelIndex )
	{
		int currentGroup = -1;

		for ( int i = 0; i < groups.size( ); i++ )
		{
			EdgeGroup gp = (EdgeGroup) groups.get( i );

			if ( gp.dimensionIndex == currentDimensionIndex
					&& gp.levelIndex == currentLevelIndex )
			{
				currentGroup = i;
				break;
			}
		}

		if ( currentGroup >= 0 && currentGroup < groups.size( ) - 1 )
		{
			return currentGroup + 1;
		}

		return -1;
	}

	/**
	 * Checks if current group is the first group
	 */
	public static boolean isFirstGroup( List groups, int dimensionIndex,
			int levelIndex )
	{
		if ( groups.size( ) > 0 )
		{
			EdgeGroup eg = (EdgeGroup) groups.get( 0 );

			return dimensionIndex == eg.dimensionIndex
					&& levelIndex == eg.levelIndex;
		}

		return false;
	}

	/**
	 * Compute the row span include data span and subtotal span, this doesn't
	 * consider for multiple vertical measures.
	 */
	public static int computeRowSpan( CrosstabReportItemHandle crosstabItem,
			List rowGroups, int dimensionIndex, int levelIndex,
			EdgeCursor rowEdgeCursor, boolean isLayoutDownThenOver )
			throws OLAPException
	{
		if ( rowEdgeCursor == null )
		{
			return 1;
		}

		long startPosition = rowEdgeCursor.getPosition( );

		int groupIndex = -1;

		for ( int i = 0; i < rowGroups.size( ); i++ )
		{
			EdgeGroup gp = (EdgeGroup) rowGroups.get( i );

			if ( gp.dimensionIndex == dimensionIndex
					&& gp.levelIndex == levelIndex )
			{
				groupIndex = i;
				break;
			}
		}

		boolean verticalHeader = MEASURE_DIRECTION_VERTICAL.equals( crosstabItem.getMeasureDirection( ) );
		int factor = verticalHeader ? Math.max( crosstabItem.getMeasureCount( ),
				1 )
				: 1;

		if ( groupIndex != -1
				&& !isLeafGroup( rowEdgeCursor.getDimensionCursor( ),
						groupIndex ) )
		{
			long currentPosition = startPosition;

			DimensionCursor dc = (DimensionCursor) rowEdgeCursor.getDimensionCursor( )
					.get( groupIndex );

			long edgeEndPosition = dc.getEdgeEnd( );

			assert currentPosition == dc.getEdgeStart( );

			int span = 0;

			int startGroupIndex = isLayoutDownThenOver ? ( groupIndex + 1 )
					: groupIndex;

			while ( currentPosition <= edgeEndPosition )
			{
				span += factor;

				for ( int i = rowGroups.size( ) - 2; i >= startGroupIndex; i-- )
				{
					dc = (DimensionCursor) rowEdgeCursor.getDimensionCursor( )
							.get( i );

					// skip dummy groups
					if ( isDummyGroup( dc ) )
					{
						continue;
					}

					// check for each group end
					if ( currentPosition == dc.getEdgeEnd( ) )
					{
						EdgeGroup gp = (EdgeGroup) rowGroups.get( i );

						DimensionViewHandle dv = crosstabItem.getDimension( ROW_AXIS_TYPE,
								gp.dimensionIndex );
						LevelViewHandle lv = dv.getLevel( gp.levelIndex );

						// consider vertical measure case
						if ( lv.getAggregationHeader( ) != null )
						{
							span += getTotalRowSpan( crosstabItem,
									gp.dimensionIndex,
									gp.levelIndex,
									verticalHeader );
						}
					}
					else
					{
						break;
					}
				}

				rowEdgeCursor.next( );

				currentPosition = rowEdgeCursor.getPosition( );
			}

			// restore original position
			rowEdgeCursor.setPosition( startPosition );

			return span;
		}

		return factor;
	}

	/**
	 * Returns the first visible total row index
	 */
	public static int getFirstTotalRowIndex(
			CrosstabReportItemHandle crosstabItem, int dimensionIndex,
			int levelIndex, boolean isVerticalMeasure )
	{
		int totalMeasureCount = crosstabItem.getMeasureCount( );

		if ( totalMeasureCount > 0 && isVerticalMeasure )
		{
			for ( int k = 0; k < totalMeasureCount; k++ )
			{
				if ( hasTotalContent( crosstabItem,
						ROW_AXIS_TYPE,
						dimensionIndex,
						levelIndex,
						k ) )
				{
					return k;
				}
			}
		}

		return 0;
	}

	/**
	 * Computes the necessary row span for specific subtotal/grandtotal cell
	 */
	public static int getTotalRowSpan( CrosstabReportItemHandle crosstabItem,
			int dimensionIndex, int levelIndex, boolean isVerticalMeasure )
	{
		int totalMeasureCount = crosstabItem.getMeasureCount( );

		if ( totalMeasureCount == 0 )
		{
			return !IColumnWalker.IGNORE_TOTAL_COLUMN_WITHOUT_MEASURE ? 1 : 0;
		}

		if ( !isVerticalMeasure )
		{
			return hasTotalContent( crosstabItem,
					ROW_AXIS_TYPE,
					dimensionIndex,
					levelIndex,
					-1 ) ? 1 : 0;
		}
		else
		{
			int span = 0;

			for ( int k = 0; k < totalMeasureCount; k++ )
			{
				if ( hasTotalContent( crosstabItem,
						ROW_AXIS_TYPE,
						dimensionIndex,
						levelIndex,
						k ) )
				{
					span++;
				}
			}

			return span;
		}
	}

	/**
	 * Checks if the given crosstab has measure header in specified axis
	 */
	public static boolean hasMeasureHeader(
			CrosstabReportItemHandle crosstabItem, int axisType )
	{
		int mc = crosstabItem.getMeasureCount( );

		if ( mc > 0 )
		{
			if ( axisType == COLUMN_AXIS_TYPE )
			{
				if ( MEASURE_DIRECTION_HORIZONTAL.equals( crosstabItem.getMeasureDirection( ) ) )
				{
					for ( int i = 0; i < mc; i++ )
					{
						if ( crosstabItem.getMeasure( i ).getHeader( ) != null )
						{
							return true;
						}
					}
				}
			}
			else
			{
				if ( MEASURE_DIRECTION_VERTICAL.equals( crosstabItem.getMeasureDirection( ) ) )
				{
					for ( int i = 0; i < mc; i++ )
					{
						if ( crosstabItem.getMeasure( i ).getHeader( ) != null )
						{
							return true;
						}
					}
				}
			}
		}

		return false;
	}

}
