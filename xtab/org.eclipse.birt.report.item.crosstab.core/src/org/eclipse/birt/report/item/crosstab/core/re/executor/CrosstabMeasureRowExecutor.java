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

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;

/**
 * CrosstabMeasureRowExecutor
 */
public class CrosstabMeasureRowExecutor extends BaseCrosstabExecutor
{

	private int rowIndex;

	private int rowSpan, colSpan;
	private int currentChangeType;
	private int currentColIndex;
	private int lastMeasureIndex;
	private int lastDimensionIndex;
	private int lastLevelIndex;
	private int totalMeasureCount;

	private boolean hasLast;

	public CrosstabMeasureRowExecutor( BaseCrosstabExecutor parent, int rowIndex )
	{
		super( parent );

		this.rowIndex = rowIndex;
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

		rowSpan = 1;
		colSpan = 0;
		lastMeasureIndex = -1;
		totalMeasureCount = crosstabItem.getMeasureCount( );

		hasLast = false;

		walker.reload( );
	}

	private AggregationCellHandle getRowSubTotalCell( int colDimensionIndex,
			int colLevelIndex, int measureIndex )
	{
		if ( measureIndex >= 0 && measureIndex < totalMeasureCount )
		{
			if ( colDimensionIndex < 0 || colLevelIndex < 0 )
			{
				return crosstabItem.getMeasure( measureIndex )
						.getAggregationCell( null, null, null, null );
			}
			else
			{
				DimensionViewHandle cdv = crosstabItem.getDimension( COLUMN_AXIS_TYPE,
						colDimensionIndex );
				LevelViewHandle clv = cdv.getLevel( colLevelIndex );

				return crosstabItem.getMeasure( measureIndex )
						.getAggregationCell( null,
								null,
								cdv.getCubeDimensionName( ),
								clv.getCubeLevelName( ) );
			}
		}
		return null;
	}

	public IReportItemExecutor getNextChild( )
	{
		IReportItemExecutor nextExecutor = null;
		int mx;

		try
		{
			while ( walker.hasNext( ) )
			{
				ColumnEvent ev = walker.next( );

				switch ( currentChangeType )
				{
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
								crosstabItem.getMeasure( mx ).getCell( ),
								rowSpan,
								colSpan,
								currentColIndex - colSpan + 1 );
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
						hasLast = false;
						break;
				}

				if ( ev.type == ColumnEvent.MEASURE_CHANGE
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
				colSpan++;
				currentColIndex++;

				if ( nextExecutor != null )
				{
					return nextExecutor;
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
							crosstabItem.getMeasure( mx ).getCell( ),
							rowSpan,
							colSpan,
							currentColIndex - colSpan + 1 );
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
					break;
			}
		}

		return nextExecutor;
	}

	public boolean hasNextChild( )
	{
		try
		{
			return walker.hasNext( ) || hasLast;
		}
		catch ( OLAPException e )
		{
			e.printStackTrace( );
		}
		return false;
	}

}
