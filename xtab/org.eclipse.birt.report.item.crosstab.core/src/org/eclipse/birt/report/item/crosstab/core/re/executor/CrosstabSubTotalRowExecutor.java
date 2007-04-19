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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.olap.OLAPException;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;

/**
 * CrosstabSubTotalRowExecutor
 */
public class CrosstabSubTotalRowExecutor extends BaseCrosstabExecutor
{

	private static Logger logger = Logger.getLogger( CrosstabSubTotalRowExecutor.class.getName( ) );

	private int rowIndex;
	private int dimensionIndex, levelIndex;

	private int rowSpan, colSpan;
	private int currentChangeType;
	private int currentColIndex;
	private int lastMeasureIndex;
	private int lastDimensionIndex;
	private int lastLevelIndex;
	private int totalMeasureCount;

	private long currentEdgePosition;
	private boolean isLayoutDownThenOver;

	private int startTotalDimensionIndex;
	private int startTotalLevelIndex;

	private boolean rowEdgeStarted;
	private boolean rowSubTotalStarted;
	private boolean hasLast;

	private int totalRowSpan;
	private boolean isFirstTotalRow;
	private boolean isSubTotalBefore;
	private boolean isFirst;
	private IReportItemExecutor nextExecutor;

	public CrosstabSubTotalRowExecutor( BaseCrosstabExecutor parent,
			int rowIndex, int dimensionIndex, int levelIndex )
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

		isLayoutDownThenOver = PAGE_LAYOUT_DOWN_THEN_OVER.equals( crosstabItem.getPageLayout( ) );

		if ( isLayoutDownThenOver )
		{
			startTotalDimensionIndex = dimensionIndex;
			startTotalLevelIndex = levelIndex;
		}
		else
		{
			EdgeGroup nextGroup = GroupUtil.getNextGroup( rowGroups,
					dimensionIndex,
					levelIndex );
			startTotalDimensionIndex = nextGroup.dimensionIndex;
			startTotalLevelIndex = nextGroup.levelIndex;
		}

		DimensionViewHandle dv = crosstabItem.getDimension( ROW_AXIS_TYPE,
				dimensionIndex );
		LevelViewHandle lv = dv.getLevel( levelIndex );

		isSubTotalBefore = lv.getAggregationHeader( ) != null
				&& AGGREGATION_HEADER_LOCATION_BEFORE.equals( lv.getAggregationHeaderLocation( ) );

		boolean isVerticalMeasure = MEASURE_DIRECTION_VERTICAL.equals( crosstabItem.getMeasureDirection( ) );

		isFirstTotalRow = rowIndex == GroupUtil.getFirstTotalRowIndex( crosstabItem,
				dimensionIndex,
				levelIndex,
				isVerticalMeasure );
		totalRowSpan = GroupUtil.getTotalRowSpan( crosstabItem,
				dimensionIndex,
				levelIndex,
				isVerticalMeasure );

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

	private boolean isRowEdgeNeedStart( ColumnEvent ev )
	{
		if ( rowEdgeStarted
				|| ev.type != ColumnEvent.ROW_EDGE_CHANGE
				|| !isSubTotalBefore )
		{
			return false;
		}

		if ( ev.dimensionIndex > dimensionIndex
				|| ( ev.dimensionIndex == dimensionIndex && ( isLayoutDownThenOver ? ( ev.levelIndex >= levelIndex )
						: ( ev.levelIndex > levelIndex ) ) ) )
		{
			return false;
		}

		// check previous subtotal
		if ( ev.dimensionIndex != dimensionIndex || ev.levelIndex != levelIndex )
		{
			DimensionViewHandle dv = crosstabItem.getDimension( ROW_AXIS_TYPE,
					ev.dimensionIndex );
			LevelViewHandle lv = dv.getLevel( ev.levelIndex );

			if ( !isLayoutDownThenOver
					&& lv.getAggregationHeader( ) != null
					&& AGGREGATION_HEADER_LOCATION_BEFORE.equals( lv.getAggregationHeaderLocation( ) ) )
			{
				return false;
			}

			// check start edge position
			int gdx = GroupUtil.getGroupIndex( rowGroups,
					ev.dimensionIndex,
					ev.levelIndex );

			if ( gdx != -1 )
			{
				try
				{
					EdgeCursor rowEdgeCursor = getRowEdgeCursor( );

					if ( rowEdgeCursor != null )
					{
						DimensionCursor dc = (DimensionCursor) rowEdgeCursor.getDimensionCursor( )
								.get( gdx );

						if ( rowEdgeCursor.getPosition( ) != dc.getEdgeStart( ) )
						{
							return false;
						}
					}
				}
				catch ( OLAPException e )
				{
					logger.log( Level.SEVERE,
							Messages.getString( "CrosstabSubTotalRowExecutor.error.check.edge.start" ), //$NON-NLS-1$
							e );
				}
			}

		}

		return rowIndex == 0;
	}

	private void advance( )
	{
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

							rowEdgeStarted = false;
							hasLast = false;
						}
						else if ( rowSubTotalStarted
								&& ev.type != ColumnEvent.ROW_EDGE_CHANGE )
						{
							nextExecutor = new CrosstabCellExecutor( this,
									crosstabItem.getDimension( ROW_AXIS_TYPE,
											dimensionIndex )
											.getLevel( levelIndex )
											.getAggregationHeader( ),
									rowSpan,
									colSpan,
									currentColIndex - colSpan + 1 );

							rowSubTotalStarted = false;
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
					case ColumnEvent.COLUMN_TOTAL_CHANGE :
					case ColumnEvent.GRAND_TOTAL_CHANGE :

						int mx = lastMeasureIndex < 0 ? rowIndex
								: lastMeasureIndex;

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
							getRowEdgeCursor( ),
							isLayoutDownThenOver );
					colSpan = 0;
					lastDimensionIndex = ev.dimensionIndex;
					lastLevelIndex = ev.levelIndex;
					hasLast = true;
				}
				else if ( !rowSubTotalStarted
						&& ev.type == ColumnEvent.ROW_EDGE_CHANGE
						&& ev.dimensionIndex == startTotalDimensionIndex
						&& ev.levelIndex == startTotalLevelIndex
						&& isFirstTotalRow )
				{
					rowSubTotalStarted = true;

					rowSpan = totalRowSpan;
					colSpan = 0;
					hasLast = true;
				}
				else if ( ev.type == ColumnEvent.MEASURE_CHANGE
						|| ev.type == ColumnEvent.COLUMN_EDGE_CHANGE )
				{
					rowSpan = 1;
					colSpan = 0;
					lastMeasureIndex = ev.measureIndex;
					if ( columnGroups != null && columnGroups.size( ) > 0 )
					{
						EdgeGroup gp = (EdgeGroup) columnGroups.get( columnGroups.size( ) - 1 );
						lastDimensionIndex = gp.dimensionIndex;
						lastLevelIndex = gp.levelIndex;
					}
					else
					{
						lastDimensionIndex = ev.dimensionIndex;
						lastLevelIndex = ev.levelIndex;
					}
					hasLast = true;
				}
				else if ( ev.type == ColumnEvent.COLUMN_TOTAL_CHANGE
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
			logger.log( Level.SEVERE,
					Messages.getString( "CrosstabSubTotalRowExecutor.error.retrieve.child.executor" ), //$NON-NLS-1$
					e );
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

						rowEdgeStarted = false;
					}
					else if ( rowSubTotalStarted )
					{
						nextExecutor = new CrosstabCellExecutor( this,
								crosstabItem.getDimension( ROW_AXIS_TYPE,
										dimensionIndex )
										.getLevel( levelIndex )
										.getAggregationHeader( ),
								rowSpan,
								colSpan,
								currentColIndex - colSpan + 1 );
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
				case ColumnEvent.COLUMN_TOTAL_CHANGE :
				case ColumnEvent.GRAND_TOTAL_CHANGE :

					int mx = lastMeasureIndex < 0 ? rowIndex : lastMeasureIndex;

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
