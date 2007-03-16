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

import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.executor.IReportItemExecutor;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;

/**
 * CrosstabHeaderRowExecutor
 */
public class CrosstabHeaderRowExecutor extends BaseCrosstabExecutor
{

	private LevelViewHandle levelView;
	private List columnGroups;

	private long currentEdgePosition;
	
	private int currentDimensionIndex, currentLevelIndex;
	private int lastDimensionIndex, lastLevelIndex;
	private int subTotalDimensionIndex, subTotalLevelIndex;
	private int currentChangeType;
	private int currentColIndex;
	private int rowSpan, colSpan;
	private boolean edgeStarted, subTotalStarted, grandTotalStarted,
			blankStarted;
	private boolean hasLast;

	private int currentGroupIndex;
	private int nextGroupIndex;
	private boolean isFirst;
	private IReportItemExecutor nextExecutor;

	public CrosstabHeaderRowExecutor( BaseCrosstabExecutor parent,
			LevelViewHandle levelView, List columnGroups )
	{
		super( parent );

		this.levelView = levelView;
		this.columnGroups = columnGroups;
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
		isFirst = true;

		subTotalStarted = false;
		grandTotalStarted = false;
		edgeStarted = false;
		blankStarted = false;
		hasLast = false;

		currentLevelIndex = levelView.getIndex( );
		currentDimensionIndex = ( (DimensionViewHandle) levelView.getContainer( ) ).getIndex( );

		subTotalDimensionIndex = -1;
		subTotalLevelIndex = -1;

		EdgeGroup gp = GroupUtil.getPreviousGroup( columnGroups,
				currentDimensionIndex,
				currentLevelIndex );

		if ( gp != null )
		{
			DimensionViewHandle dv = crosstabItem.getDimension( COLUMN_AXIS_TYPE,
					gp.dimensionIndex );
			LevelViewHandle lv = dv.getLevel( gp.levelIndex );

			if ( lv.getAggregationHeader( ) != null )
			{
				subTotalDimensionIndex = gp.dimensionIndex;
				subTotalLevelIndex = gp.levelIndex;
			}
		}

		currentGroupIndex = GroupUtil.getGroupIndex( columnGroups,
				currentDimensionIndex,
				currentLevelIndex );

		nextGroupIndex = GroupUtil.getNextGroupIndex( columnGroups,
				currentDimensionIndex,
				currentLevelIndex );

		rowSpan = 1;
		colSpan = 0;

		currentColIndex = -1;
		currentChangeType = ColumnEvent.UNKNOWN_CHANGE;

		walker.reload( );
	}

	private boolean isForceEmpty( )
	{
		try
		{
			EdgeCursor columnEdgeCursor = getColumnEdgeCursor( );

			columnEdgeCursor.setPosition( currentEdgePosition );

			DimensionCursor dc = (DimensionCursor) columnEdgeCursor.getDimensionCursor( )
					.get( currentGroupIndex );

			return GroupUtil.isDummyGroup( dc );
		}
		catch ( OLAPException e )
		{
			e.printStackTrace( );
		}

		return false;
	}

	private boolean isMeetEdgeEnd( ColumnEvent ev )
	{
		if ( ev.type == ColumnEvent.GRAND_TOTAL_CHANGE
				|| isStartUnderSubTotal( ev ) )
		{
			return true;
		}

		if ( nextGroupIndex != -1 )
		{
			try
			{
				EdgeCursor columnEdgeCursor = getColumnEdgeCursor( );

				columnEdgeCursor.setPosition( ev.dataPosition );

				//TODO edge
				DimensionCursor dc = (DimensionCursor) columnEdgeCursor.getDimensionCursor( )
						.get( currentGroupIndex /*nextGroupIndex*/ );

				if ( !GroupUtil.isDummyGroup( dc ) )
				{
					return currentEdgePosition < dc.getEdgeStart( );
				}
			}
			catch ( OLAPException e )
			{
				e.printStackTrace( );
			}
		}

		return ev.measureIndex <= 0;
	}

	private boolean isEdgeNeedStart( ColumnEvent ev )
	{
		return !edgeStarted
				&& !subTotalStarted
				&& ( ev.type == ColumnEvent.COLUMN_EDGE_CHANGE || isStartOverSubTotal( ev ) );
	}

	private boolean isStartOverSubTotal( ColumnEvent ev )
	{
		return !subTotalStarted
				&& ev.type == ColumnEvent.COLUMN_TOTAL_CHANGE
				&& ( ev.dimensionIndex > currentDimensionIndex || ( ev.dimensionIndex == currentDimensionIndex && ev.levelIndex >= currentLevelIndex ) );
	}

	private boolean isStartUnderSubTotal( ColumnEvent ev )
	{
		return !subTotalStarted
				&& ev.type == ColumnEvent.COLUMN_TOTAL_CHANGE
				&& ( ev.dimensionIndex < currentDimensionIndex || ( ev.dimensionIndex == currentDimensionIndex && ev.levelIndex < currentLevelIndex ) );
	}

	private boolean isSubTotalNeedStart( ColumnEvent ev )
	{
		return !subTotalStarted
				&& ev.type == ColumnEvent.COLUMN_TOTAL_CHANGE
				&& ev.dimensionIndex == subTotalDimensionIndex
				&& ev.levelIndex == subTotalLevelIndex;
	}

	private boolean isGrandTotalNeedStart( ColumnEvent ev )
	{
		return !grandTotalStarted
				&& ev.type == ColumnEvent.GRAND_TOTAL_CHANGE
				&& crosstabItem.getGrandTotal( COLUMN_AXIS_TYPE ) != null
				&& GroupUtil.isFirstGroup( columnGroups,
						currentDimensionIndex,
						currentLevelIndex );
	}

	private boolean isBlankNeedStart( ColumnEvent ev )
	{
		return !blankStarted
				&& ( ev.type == ColumnEvent.ROW_EDGE_CHANGE || ev.type == ColumnEvent.MEASURE_HEADER_CHANGE )
				&& GroupUtil.isFirstGroup( columnGroups,
						currentDimensionIndex,
						currentLevelIndex );
	}

	/**
	 * Advance until find next executor or till the end.
	 */
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
					case ColumnEvent.MEASURE_HEADER_CHANGE :

						if ( blankStarted
								&& ev.type != ColumnEvent.ROW_EDGE_CHANGE
								&& ev.type != ColumnEvent.MEASURE_HEADER_CHANGE )
						{
							nextExecutor = new CrosstabCellExecutor( this,
									null,
									rowSpan,
									colSpan,
									currentColIndex - colSpan + 1 );
							blankStarted = false;
							hasLast = false;
						}
						break;
					case ColumnEvent.COLUMN_EDGE_CHANGE :
					case ColumnEvent.COLUMN_TOTAL_CHANGE :

						if ( edgeStarted && isMeetEdgeEnd( ev ) )
						{
							nextExecutor = new CrosstabCellExecutor( this,
									levelView.getCell( ),
									rowSpan,
									colSpan,
									currentColIndex - colSpan + 1 );

							( (CrosstabCellExecutor) nextExecutor ).setPosition( currentEdgePosition );
							
							( (CrosstabCellExecutor) nextExecutor ).setForceEmpty( isForceEmpty( ) );

							edgeStarted = false;
							hasLast = false;
						}
						else if ( subTotalStarted
								&& ( ev.type != currentChangeType
										|| ev.dimensionIndex != lastDimensionIndex || ev.levelIndex != lastLevelIndex ) )
						{
							nextExecutor = new CrosstabCellExecutor( this,
									crosstabItem.getDimension( COLUMN_AXIS_TYPE,
											lastDimensionIndex )
											.getLevel( lastLevelIndex )
											.getAggregationHeader( ),
									rowSpan,
									colSpan,
									currentColIndex - colSpan + 1 );

							subTotalStarted = false;
							hasLast = false;
						}
						break;
					case ColumnEvent.GRAND_TOTAL_CHANGE :

						if ( grandTotalStarted && ev.type != currentChangeType )
						{
							nextExecutor = new CrosstabCellExecutor( this,
									crosstabItem.getGrandTotal( COLUMN_AXIS_TYPE ),
									rowSpan,
									colSpan,
									currentColIndex - colSpan + 1 );

							grandTotalStarted = false;
							hasLast = false;
						}
						break;
				}

				if ( isSubTotalNeedStart( ev ) )
				{
					subTotalStarted = true;

					lastDimensionIndex = ev.dimensionIndex;
					lastLevelIndex = ev.levelIndex;

					rowSpan = GroupUtil.computeGroupSpan( columnGroups,
							lastDimensionIndex,
							lastLevelIndex );

					colSpan = 0;
					hasLast = true;
				}
				else if ( isGrandTotalNeedStart( ev ) )
				{
					grandTotalStarted = true;

					rowSpan = GroupUtil.computeGroupSpan( columnGroups,
							currentDimensionIndex,
							currentLevelIndex ) + 1;

					colSpan = 0;
					hasLast = true;
				}
				else if ( isEdgeNeedStart( ev ) )
				{
					edgeStarted = true;
					rowSpan = 1;
					colSpan = 0;
					hasLast = true;
				}
				else if ( isBlankNeedStart( ev ) )
				{
					blankStarted = true;

					rowSpan = GroupUtil.computeGroupSpan( columnGroups,
							currentDimensionIndex,
							currentLevelIndex ) + 1;

					if ( hasMeasureHeader( COLUMN_AXIS_TYPE ) )
					{
						rowSpan++;
					}

					hasLast = true;
				}

				currentEdgePosition = ev.dataPosition;

				currentChangeType = ev.type;
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
			if ( blankStarted )
			{
				nextExecutor = new CrosstabCellExecutor( this,
						null,
						rowSpan,
						colSpan,
						currentColIndex - colSpan + 1 );
			}
			else if ( edgeStarted )
			{
				nextExecutor = new CrosstabCellExecutor( this,
						levelView.getCell( ),
						rowSpan,
						colSpan,
						currentColIndex - colSpan + 1 );

				( (CrosstabCellExecutor) nextExecutor ).setPosition( currentEdgePosition );
				
				( (CrosstabCellExecutor) nextExecutor ).setForceEmpty( isForceEmpty( ) );

				edgeStarted = false;
			}
			else if ( subTotalStarted )
			{
				nextExecutor = new CrosstabCellExecutor( this,
						crosstabItem.getDimension( COLUMN_AXIS_TYPE,
								lastDimensionIndex )
								.getLevel( lastLevelIndex )
								.getAggregationHeader( ),
						rowSpan,
						colSpan,
						currentColIndex );

				subTotalStarted = false;
			}
			else if ( grandTotalStarted )
			{
				nextExecutor = new CrosstabCellExecutor( this,
						crosstabItem.getGrandTotal( COLUMN_AXIS_TYPE ),
						rowSpan,
						colSpan,
						currentColIndex - colSpan + 1 );

				grandTotalStarted = false;
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
