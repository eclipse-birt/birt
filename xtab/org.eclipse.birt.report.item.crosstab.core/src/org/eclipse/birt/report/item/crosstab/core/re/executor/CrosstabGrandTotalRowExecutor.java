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

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;

/**
 * CrosstabGrandTotalRowExecutor
 */
public class CrosstabGrandTotalRowExecutor extends BaseCrosstabExecutor
{

	private static final Logger logger = Logger.getLogger( CrosstabGrandTotalRowExecutor.class.getName( ) );

	private int rowIndex;

	private long currentEdgePosition;

	private int rowSpan, colSpan;
	private int currentChangeType;
	private int currentColIndex;
	private int lastMeasureIndex;
	private int lastDimensionIndex;
	private int lastLevelIndex;
	private int totalMeasureCount;

	private boolean hasLast;

	public CrosstabGrandTotalRowExecutor( BaseCrosstabExecutor parent,
			int rowIndex )
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

	private AggregationCellHandle getRowGrandTotalCell( int dimensionIndex,
			int levelIndex, int measureIndex )
	{
		if ( measureIndex >= 0 && measureIndex < totalMeasureCount )
		{
			if ( dimensionIndex < 0 || levelIndex < 0 )
			{
				return crosstabItem.getMeasure( measureIndex )
						.getAggregationCell( null, null, null, null );
			}
			else
			{
				DimensionViewHandle dv = crosstabItem.getDimension( COLUMN_AXIS_TYPE,
						dimensionIndex );
				LevelViewHandle lv = dv.getLevel( levelIndex );

				return crosstabItem.getMeasure( measureIndex )
						.getAggregationCell( null,
								null,
								dv.getCubeDimensionName( ),
								lv.getCubeLevelName( ) );
			}
		}
		return null;
	}

	public IReportItemExecutor getNextChild( )
	{
		IReportItemExecutor nextExecutor = null;

		try
		{
			while ( walker.hasNext( ) )
			{
				ColumnEvent ev = walker.next( );

				switch ( currentChangeType )
				{
					case ColumnEvent.ROW_EDGE_CHANGE :

						if ( ev.type != ColumnEvent.ROW_EDGE_CHANGE
								&& rowIndex == 0 )
						{
							nextExecutor = new CrosstabCellExecutor( this,
									crosstabItem.getGrandTotal( ROW_AXIS_TYPE ),
									rowSpan,
									colSpan,
									currentColIndex - colSpan + 1 );
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
								getRowGrandTotalCell( lastDimensionIndex,
										lastLevelIndex,
										mx ),
								rowSpan,
								colSpan,
								currentColIndex - colSpan + 1 );

						( (CrosstabCellExecutor) nextExecutor ).setPosition( currentEdgePosition );

						hasLast = false;
						break;
				}

				if ( ev.type == ColumnEvent.MEASURE_CHANGE
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
				else if ( ev.type == ColumnEvent.ROW_EDGE_CHANGE
						&& rowIndex == 0 )
				{
					rowSpan = hasMeasureHeader( ROW_AXIS_TYPE ) ? Math.max( totalMeasureCount,
							1 )
							: 1;

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
					return nextExecutor;
				}
			}

		}
		catch ( OLAPException e )
		{
			logger.log( Level.SEVERE,
					Messages.getString( "CrosstabGrandTotalRowExecutor.error.generate.child.executor" ), //$NON-NLS-1$
					e );
		}

		if ( hasLast )
		{
			hasLast = false;

			// handle last column
			switch ( currentChangeType )
			{
				case ColumnEvent.ROW_EDGE_CHANGE :

					if ( rowIndex == 0 )
					{
						nextExecutor = new CrosstabCellExecutor( this,
								crosstabItem.getGrandTotal( ROW_AXIS_TYPE ),
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
							getRowGrandTotalCell( lastDimensionIndex,
									lastLevelIndex,
									mx ),
							rowSpan,
							colSpan,
							currentColIndex - colSpan + 1 );

					( (CrosstabCellExecutor) nextExecutor ).setPosition( currentEdgePosition );

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
			logger.log( Level.SEVERE,
					Messages.getString( "CrosstabGrandTotalRowExecutor.error.check.child.executor" ), //$NON-NLS-1$
					e );
		}
		return false;
	}

}
