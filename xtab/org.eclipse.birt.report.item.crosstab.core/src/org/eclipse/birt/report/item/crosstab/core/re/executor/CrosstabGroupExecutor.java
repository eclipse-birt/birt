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

import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * CrosstabGroupExecutor
 */
public class CrosstabGroupExecutor extends BaseCrosstabExecutor
{

	private int currentGroupIndex;
	private EdgeCursor rowCursor;
	private List groupCursors;

	private LevelViewHandle lastLevel;
	private int lastDimensionIndex, lastLevelIndex;
	private int totalMeasureCount;

	private List elements;
	private int currentElement;
	private boolean endGroup;
	private boolean hasGroup;

	private boolean notifyNextGroupPageBreak;

	public CrosstabGroupExecutor( BaseCrosstabExecutor parent, int groupIndex,
			EdgeCursor rowCursor )
	{
		super( parent );

		this.currentGroupIndex = groupIndex;

		this.rowCursor = rowCursor;
	}

	public void close( )
	{
		if ( hasGroup )
		{
			try
			{
				handleGroupPageBreakAfter( );
			}
			catch ( OLAPException e )
			{
				e.printStackTrace( );
			}
		}

		super.close( );

		groupCursors = null;
		lastLevel = null;
		elements = null;
	}

	public IContent execute( )
	{
		ITableGroupContent content = context.getReportContent( )
				.createTableGroupContent( );

		initializeContent( content, null );

		prepareChildren( );

		return content;
	}

	private void prepareChildren( )
	{
		hasGroup = rowGroups.size( ) > 0 && rowCursor != null;

		if ( hasGroup )
		{
			try
			{
				totalMeasureCount = crosstabItem.getMeasureCount( );

				groupCursors = rowCursor.getDimensionCursor( );

				handleGroupPageBreakBefore( );

				// TODO tmp
				// ( (ITableGroupContent) getContent( ) ).setGroupLevel(
				// currentGroupIndex );

				if ( currentGroupIndex > 0 )
				{
					EdgeGroup lastGroup = (EdgeGroup) rowGroups.get( currentGroupIndex - 1 );

					lastDimensionIndex = lastGroup.dimensionIndex;
					lastLevelIndex = lastGroup.levelIndex;

					if ( lastDimensionIndex >= 0 && lastLevelIndex >= 0 )
					{
						lastLevel = crosstabItem.getDimension( ROW_AXIS_TYPE,
								lastDimensionIndex ).getLevel( lastLevelIndex );
					}
				}

				collectExecutable( );
			}
			catch ( OLAPException e )
			{
				e.printStackTrace( );
			}
		}
		else
		{
			// measure only
			elements = new ArrayList( );
			currentElement = 0;

			elements.add( new CrosstabMeasureExecutor( this ) );
		}
	}

	private boolean isLastLevelLeafGroup( ) throws OLAPException
	{
		if ( currentGroupIndex > 0 && rowCursor != null )
		{
			return GroupUtil.isLeafGroup( rowCursor.getDimensionCursor( ),
					currentGroupIndex - 1 );
		}

		return false;
	}

	private void handleGroupPageBreakBefore( ) throws OLAPException
	{
		EdgeGroup currentGroup = (EdgeGroup) rowGroups.get( currentGroupIndex );
		LevelViewHandle currentLevel = crosstabItem.getDimension( ROW_AXIS_TYPE,
				currentGroup.dimensionIndex )
				.getLevel( currentGroup.levelIndex );

		String pageBreakBefore = currentLevel.getPageBreakBefore( );
		if ( DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS.equals( pageBreakBefore )
				|| ( DesignChoiceConstants.PAGE_BREAK_BEFORE_ALWAYS_EXCLUDING_FIRST.equals( pageBreakBefore ) && !rowCursor.isFirst( ) ) )
		{
			( (ITableGroupContent) getContent( ) ).getStyle( )
					.setProperty( IStyle.STYLE_PAGE_BREAK_BEFORE,
							IStyle.ALWAYS_VALUE );
		}

		String pageBreakAfter = currentLevel.getPageBreakAfter( );
		if ( DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS.equals( pageBreakAfter ) )
		{
			( (ITableGroupContent) getContent( ) ).getStyle( )
					.setProperty( IStyle.STYLE_PAGE_BREAK_AFTER,
							IStyle.ALWAYS_VALUE );
		}

		// handle special logic for page_break_after_excluding_last
		// TODO confirm the correct behavior
		boolean hasPageBreak = false;
		IReportItemExecutor parentExecutor = getParent( );

		while ( parentExecutor instanceof CrosstabGroupExecutor )
		{
			if ( ( (CrosstabGroupExecutor) parentExecutor ).notifyNextGroupPageBreak )
			{
				( (CrosstabGroupExecutor) parentExecutor ).notifyNextGroupPageBreak = false;

				hasPageBreak = true;
			}

			parentExecutor = parentExecutor.getParent( );
		}

		if ( hasPageBreak )
		{
			( (ITableGroupContent) getContent( ) ).getStyle( )
					.setProperty( IStyle.STYLE_PAGE_BREAK_BEFORE,
							IStyle.ALWAYS_VALUE );
		}
	}

	private void handleGroupPageBreakAfter( ) throws OLAPException
	{
		EdgeGroup currentGroup = (EdgeGroup) rowGroups.get( currentGroupIndex );
		LevelViewHandle currentLevel = crosstabItem.getDimension( ROW_AXIS_TYPE,
				currentGroup.dimensionIndex )
				.getLevel( currentGroup.levelIndex );

		// handle page_break_after_excluding_last
		String pageBreakAfter = currentLevel.getPageBreakAfter( );
		IReportItemExecutor parentExecutor = getParent( );

		if ( parentExecutor instanceof CrosstabGroupExecutor
				&& DesignChoiceConstants.PAGE_BREAK_AFTER_ALWAYS_EXCLUDING_LAST.equals( pageBreakAfter )
				&& !rowCursor.isLast( ) )
		{

			// TODO confirm the correct behavior
			while ( parentExecutor instanceof CrosstabGroupExecutor )
			{
				( (CrosstabGroupExecutor) parentExecutor ).notifyNextGroupPageBreak = true;

				parentExecutor = parentExecutor.getParent( );
			}
		}
	}

	private int getStartingGroupLevel( ) throws OLAPException
	{
		// for ( int i = 0; i < groupCursors.size( ) ; i++ )
		// {
		// DimensionCursor dc = (DimensionCursor) groupCursors.get( i );
		//
		// if ( GroupUtil.isDummyGroup( dc )
		// || dc.getEdgeStart( ) == rowCursor.getPosition( ) )
		// {
		// return i;
		// }
		// }

		// TODO edge
		if ( rowCursor.isFirst( ) )
		{
			return 0;
		}

		for ( int i = 0; i < groupCursors.size( ) - 1; i++ )
		{
			DimensionCursor dc = (DimensionCursor) groupCursors.get( i );

			if ( GroupUtil.isDummyGroup( dc ) )
			{
				return i;
			}

			if ( dc.getEdgeStart( ) == rowCursor.getPosition( ) )
			{
				return i + 1;
			}
		}

		return groupCursors.size( );
	}

	private int getEndingGroupLevel( ) throws OLAPException
	{
		// for ( int i = 0; i < groupCursors.size( ); i++ )
		// {
		// DimensionCursor dc = (DimensionCursor) groupCursors.get( i );
		//
		// if ( GroupUtil.isDummyGroup( dc )
		// || dc.getEdgeEnd( ) == rowCursor.getPosition( ) )
		// {
		// return i;
		// }
		// }

		// TODO edge

		if ( rowCursor.isLast( ) )
		{
			return 0;
		}

		for ( int i = 0; i < groupCursors.size( ) - 1; i++ )
		{
			DimensionCursor dc = (DimensionCursor) groupCursors.get( i );

			if ( GroupUtil.isDummyGroup( dc ) )
			{
				return i;
			}

			if ( dc.getEdgeEnd( ) == rowCursor.getPosition( ) )
			{
				return i + 1;
			}
		}

		return groupCursors.size( );
	}

	private void collectExecutable( ) throws OLAPException
	{
		elements = new ArrayList( );
		currentElement = 0;
		endGroup = false;

		int startingGroupIndex = getStartingGroupLevel( );

		if ( startingGroupIndex <= currentGroupIndex )
		{
			if ( totalMeasureCount > 0
					|| !IColumnWalker.IGNORE_TOTAL_COLUMN_WITHOUT_MEASURE )
			{
				if ( lastLevel != null
						&& lastLevel.getAggregationHeader( ) != null
						&& AGGREGATION_HEADER_LOCATION_BEFORE.equals( lastLevel.getAggregationHeaderLocation( ) )
						&& !isLastLevelLeafGroup( ) )
				{
					// header
					CrosstabGroupBandExecutor bandExecutor = new CrosstabGroupBandExecutor( this,
							lastDimensionIndex,
							lastLevelIndex,
							IBandContent.BAND_HEADER );
					elements.add( bandExecutor );
				}
			}
		}

		if ( currentGroupIndex < rowGroups.size( ) - 1 )
		{
			// next group
			CrosstabGroupExecutor groupExecutor = new CrosstabGroupExecutor( this,
					currentGroupIndex + 1,
					rowCursor );
			elements.add( groupExecutor );
		}
		else
		{
			// detail
			EdgeGroup currentGroup = (EdgeGroup) rowGroups.get( currentGroupIndex );

			CrosstabGroupBandExecutor bandExecutor = new CrosstabGroupBandExecutor( this,
					currentGroup.dimensionIndex,
					currentGroup.levelIndex,
					IBandContent.BAND_DETAIL );
			elements.add( bandExecutor );
		}

		int endingGroupIndex = getEndingGroupLevel( );

		if ( endingGroupIndex <= currentGroupIndex )
		{
			if ( totalMeasureCount > 0
					|| !IColumnWalker.IGNORE_TOTAL_COLUMN_WITHOUT_MEASURE )
			{
				if ( lastLevel != null
						&& lastLevel.getAggregationHeader( ) != null
						&& AGGREGATION_HEADER_LOCATION_AFTER.equals( lastLevel.getAggregationHeaderLocation( ) )
						&& !isLastLevelLeafGroup( ) )
				{
					// footer
					CrosstabGroupBandExecutor bandExecutor = new CrosstabGroupBandExecutor( this,
							lastDimensionIndex,
							lastLevelIndex,
							IBandContent.BAND_FOOTER );
					elements.add( bandExecutor );
				}
			}

			endGroup = true;
		}

	}

	public IReportItemExecutor getNextChild( )
	{
		if ( currentElement < elements.size( ) )
		{
			return (IReportItemExecutor) elements.get( currentElement++ );
		}

		return null;
	}

	public boolean hasNextChild( )
	{
		if ( currentElement < elements.size( ) )
		{
			return true;
		}

		if ( hasGroup )
		{
			if ( endGroup )
			{
				return false;
			}

			try
			{
				while ( !endGroup )
				{
					int endingGroupIndex = getEndingGroupLevel( );

					if ( endingGroupIndex <= currentGroupIndex )
					{
						currentElement = 0;
						elements = new ArrayList( );

						if ( totalMeasureCount > 0
								|| !IColumnWalker.IGNORE_TOTAL_COLUMN_WITHOUT_MEASURE )
						{
							if ( lastLevel != null
									&& lastLevel.getAggregationHeader( ) != null
									&& AGGREGATION_HEADER_LOCATION_AFTER.equals( lastLevel.getAggregationHeaderLocation( ) )
									&& !isLastLevelLeafGroup( ) )
							{
								// footer
								CrosstabGroupBandExecutor bandExecutor = new CrosstabGroupBandExecutor( this,
										lastDimensionIndex,
										lastLevelIndex,
										IBandContent.BAND_FOOTER );
								elements.add( bandExecutor );
							}
						}

						endGroup = true;

						return currentElement < elements.size( );
					}

					if ( rowCursor.next( ) )
					{
						collectExecutable( );

						return currentElement < elements.size( );
					}

				}
			}
			catch ( OLAPException e )
			{
				e.printStackTrace( );
			}
		}

		return false;
	}
}
