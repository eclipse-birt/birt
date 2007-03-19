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

import javax.olap.OLAPException;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;

/**
 * CrosstabRowExecutor
 */
public class CrosstabRowExecutor extends BaseCrosstabExecutor
{

	private int rowIndex;
	private int dimensionIndex;
	private int levelIndex;

	private int rowSpan, colSpan;
	private int currentChangeType;
	private int currentColIndex;
	private int lastMeasureIndex;
	private int lastDimensionIndex;
	private int lastLevelIndex;
	private int totalMeasureCount;

	private boolean rowEdgeStarted;
	private boolean hasLast;

	private long currentEdgePosition;

	private int factor;
	private boolean isFirst;
	private IReportItemExecutor nextExecutor;

	public CrosstabRowExecutor( BaseCrosstabExecutor parent, int rowIndex,
			int dimensionIndex, int levelIndex )
	{
		super( parent );

		this.rowIndex = rowIndex;
		this.dimensionIndex = dimensionIndex;
		this.levelIndex = levelIndex;
	}

	public void close( )
	{
		super.close( );

		nextExecutor = null;
	}

	public IContent execute( )
	{
		IRowContent content = context.getReportContent( ).createRowContent( );

		initializeContent( content, null );

		prepareChildren( );

		return content;
	}

	private void prepareChildren( )
	{
		currentChangeType = ColumnEvent.UNKNOWN_CHANGE;
		currentColIndex = -1;

		isFirst = true;

		rowSpan = 1;
		colSpan = 0;
		lastMeasureIndex = -1;
		totalMeasureCount = crosstabItem.getMeasureCount( );

		factor = hasMeasureHeader( ROW_AXIS_TYPE ) ? Math.max( totalMeasureCount,
				1 )
				: 1;

		hasLast = false;

		walker.reload( );
	}

	private AggregationCellHandle getRowSubTotalCell( int colDimensionIndex,
			int colLevelIndex, int measureIndex )
	{
		if ( measureIndex >= 0 && measureIndex < totalMeasureCount )
		{
			DimensionViewHandle rdv = crosstabItem.getDimension( ROW_AXIS_TYPE,
					dimensionIndex );
			LevelViewHandle rlv = rdv.getLevel( levelIndex );

			if ( colDimensionIndex < 0 || colLevelIndex < 0 )
			{
				return crosstabItem.getMeasure( measureIndex )
						.getAggregationCell( rdv.getCubeDimensionName( ),
								rlv.getCubeLevelName( ),
								null,
								null );
			}
			else
			{
				DimensionViewHandle cdv = crosstabItem.getDimension( COLUMN_AXIS_TYPE,
						colDimensionIndex );
				LevelViewHandle clv = cdv.getLevel( colLevelIndex );

				return crosstabItem.getMeasure( measureIndex )
						.getAggregationCell( rdv.getCubeDimensionName( ),
								rlv.getCubeLevelName( ),
								cdv.getCubeDimensionName( ),
								clv.getCubeLevelName( ) );
			}
		}
		return null;
	}

	private boolean isForceEmpty( )
	{
		try
		{
			EdgeCursor rowEdgeCursor = getRowEdgeCursor( );

			int groupIndex = GroupUtil.getGroupIndex( rowGroups,
					lastDimensionIndex,
					lastLevelIndex );

			DimensionCursor dc = (DimensionCursor) rowEdgeCursor.getDimensionCursor( )
					.get( groupIndex );

			return GroupUtil.isDummyGroup( dc );
		}
		catch ( OLAPException e )
		{
			e.printStackTrace( );
		}

		return false;
	}

	private boolean isRowEdgeNeedStart( ColumnEvent ev ) throws OLAPException
	{
		if ( rowEdgeStarted || ev.type != ColumnEvent.ROW_EDGE_CHANGE )
		{
			return false;
		}

		// check when previous subtotal already processed
		boolean groupFound = false;

		for ( int i = 0; i < rowGroups.size( ) - 1; i++ )
		{
			EdgeGroup gp = (EdgeGroup) rowGroups.get( i );

			if ( !groupFound
					&& gp.dimensionIndex == ev.dimensionIndex
					&& gp.levelIndex == ev.levelIndex )
			{
				groupFound = true;
			}

			// only check with non-leaf groups
			if ( groupFound
					&& !GroupUtil.isLeafGroup( getRowEdgeCursor( ).getDimensionCursor( ),
							i ) )
			{
				DimensionViewHandle dv = crosstabItem.getDimension( ROW_AXIS_TYPE,
						gp.dimensionIndex );
				LevelViewHandle lv = dv.getLevel( gp.levelIndex );

				if ( lv.getAggregationHeader( ) != null
						&& AGGREGATION_HEADER_LOCATION_BEFORE.equals( lv.getAggregationHeaderLocation( ) ) )
				{
					return false;
				}
			}
		}

		// check start edge position
		// TODO edge
		/*
		 * int gdx = GroupUtil.getNextGroupIndex( rowGroups, ev.dimensionIndex,
		 * ev.levelIndex );
		 */
		int gdx = GroupUtil.getGroupIndex( rowGroups,
				ev.dimensionIndex,
				ev.levelIndex );

		if ( gdx != -1 )
		{
			try
			{
				EdgeCursor rowEdgeCursor = getRowEdgeCursor( );
				DimensionCursor dc = (DimensionCursor) rowEdgeCursor.getDimensionCursor( )
						.get( gdx );

				if ( !GroupUtil.isDummyGroup( dc )
						&& rowEdgeCursor.getPosition( ) != dc.getEdgeStart( ) )
				{
					return false;
				}
			}
			catch ( OLAPException e )
			{
				e.printStackTrace( );
			}
		}

		return rowIndex == 0;
	}

	private void advance( )
	{
		int mx;

		try
		{
			while ( walker.hasNext( ) )
			{
				ColumnEvent ev = walker.next( );

				switch ( currentChangeType )
				{
					case ColumnEvent.ROW_EDGE_CHANGE :

						if ( rowEdgeStarted )
						{
							nextExecutor = new CrosstabCellExecutor( this,
									crosstabItem.getDimension( ROW_AXIS_TYPE,
											lastDimensionIndex )
											.getLevel( lastLevelIndex )
											.getCell( ),
									rowSpan,
									colSpan,
									currentColIndex - colSpan + 1 );

							( (CrosstabCellExecutor) nextExecutor ).setForceEmpty( isForceEmpty( ) );

							rowEdgeStarted = false;
							hasLast = false;
						}
						break;
					case ColumnEvent.MEASURE_HEADER_CHANGE :

						nextExecutor = new CrosstabCellExecutor( this,
								crosstabItem.getMeasure( rowIndex ).getHeader( ),
								rowSpan,
								colSpan,
								currentColIndex - colSpan + 1 );
						hasLast = false;
						break;
					case ColumnEvent.MEASURE_CHANGE :
					case ColumnEvent.COLUMN_EDGE_CHANGE :

						mx = lastMeasureIndex < 0 ? rowIndex : lastMeasureIndex;

						nextExecutor = new CrosstabCellExecutor( this,
								totalMeasureCount > 0 ? crosstabItem.getMeasure( mx )
										.getCell( )
										: null,
								rowSpan,
								colSpan,
								currentColIndex - colSpan + 1 );

						( (CrosstabCellExecutor) nextExecutor ).setPosition( currentEdgePosition );

						hasLast = false;
						break;
					case ColumnEvent.COLUMN_TOTAL_CHANGE :
					case ColumnEvent.GRAND_TOTAL_CHANGE :

						mx = lastMeasureIndex < 0 ? rowIndex : lastMeasureIndex;

						nextExecutor = new CrosstabCellExecutor( this,
								getRowSubTotalCell( lastDimensionIndex,
										lastLevelIndex,
										mx ),
								rowSpan,
								colSpan,
								currentColIndex - colSpan + 1 );

						( (CrosstabCellExecutor) nextExecutor ).setPosition( currentEdgePosition );

						hasLast = false;
						break;
				}

				if ( isRowEdgeNeedStart( ev ) )
				{
					rowEdgeStarted = true;
					rowSpan = GroupUtil.computeRowSpan( crosstabItem,
							rowGroups,
							ev.dimensionIndex,
							ev.levelIndex,
							getRowEdgeCursor( ) )
							* factor;
					colSpan = 0;
					lastDimensionIndex = ev.dimensionIndex;
					lastLevelIndex = ev.levelIndex;
					hasLast = true;
				}
				else if ( ev.type == ColumnEvent.MEASURE_CHANGE
						|| ev.type == ColumnEvent.COLUMN_TOTAL_CHANGE
						|| ev.type == ColumnEvent.COLUMN_EDGE_CHANGE
						|| ev.type == ColumnEvent.GRAND_TOTAL_CHANGE )
				{
					rowSpan = 1;
					colSpan = 0;
					lastMeasureIndex = ev.measureIndex;
					lastDimensionIndex = ev.dimensionIndex;
					lastLevelIndex = ev.levelIndex;
					hasLast = true;
				}
				else if ( ev.type == ColumnEvent.MEASURE_HEADER_CHANGE )
				{
					rowSpan = 1;
					colSpan = 0;
					hasLast = true;
				}

				currentChangeType = ev.type;
				currentEdgePosition = ev.dataPosition;
				colSpan++;
				currentColIndex++;

				if ( nextExecutor != null )
				{
					return;
				}
			}

		}
		catch ( OLAPException e )
		{
			e.printStackTrace( );
		}

		if ( hasLast )
		{
			hasLast = false;

			// handle last column
			switch ( currentChangeType )
			{
				case ColumnEvent.ROW_EDGE_CHANGE :

					if ( rowEdgeStarted )
					{
						nextExecutor = new CrosstabCellExecutor( this,
								crosstabItem.getDimension( ROW_AXIS_TYPE,
										lastDimensionIndex )
										.getLevel( lastLevelIndex )
										.getCell( ),
								rowSpan,
								colSpan,
								currentColIndex - colSpan + 1 );

						( (CrosstabCellExecutor) nextExecutor ).setForceEmpty( isForceEmpty( ) );

						rowEdgeStarted = false;
					}
					break;
				case ColumnEvent.MEASURE_HEADER_CHANGE :

					nextExecutor = new CrosstabCellExecutor( this,
							crosstabItem.getMeasure( rowIndex ).getHeader( ),
							rowSpan,
							colSpan,
							currentColIndex - colSpan + 1 );
					break;
				case ColumnEvent.MEASURE_CHANGE :
				case ColumnEvent.COLUMN_EDGE_CHANGE :

					mx = lastMeasureIndex < 0 ? rowIndex : lastMeasureIndex;

					nextExecutor = new CrosstabCellExecutor( this,
							totalMeasureCount > 0 ? crosstabItem.getMeasure( mx )
									.getCell( )
									: null,
							rowSpan,
							colSpan,
							currentColIndex - colSpan + 1 );

					( (CrosstabCellExecutor) nextExecutor ).setPosition( currentEdgePosition );

					break;
				case ColumnEvent.COLUMN_TOTAL_CHANGE :
				case ColumnEvent.GRAND_TOTAL_CHANGE :

					mx = lastMeasureIndex < 0 ? rowIndex : lastMeasureIndex;

					nextExecutor = new CrosstabCellExecutor( this,
							getRowSubTotalCell( lastDimensionIndex,
									lastLevelIndex,
									mx ),
							rowSpan,
							colSpan,
							currentColIndex - colSpan + 1 );

					( (CrosstabCellExecutor) nextExecutor ).setPosition( currentEdgePosition );

					break;
			}
		}
	}

	public IReportItemExecutor getNextChild( )
	{
		IReportItemExecutor childExecutor = nextExecutor;

		nextExecutor = null;

		advance( );

		return childExecutor;
	}

	public boolean hasNextChild( )
	{
		if ( isFirst )
		{
			isFirst = false;

			advance( );
		}

		return nextExecutor != null;
	}

}
