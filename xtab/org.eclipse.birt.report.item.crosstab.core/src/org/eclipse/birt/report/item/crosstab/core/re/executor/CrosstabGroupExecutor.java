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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.olap.OLAPException;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.report.engine.content.IBandContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ITableGroupContent;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;

/**
 * CrosstabGroupExecutor
 */
public class CrosstabGroupExecutor extends BaseCrosstabExecutor
{

	private static final Logger logger = Logger.getLogger( CrosstabGroupExecutor.class.getName( ) );

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

	boolean notifyNextGroupPageBreak;

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
				handleGroupPageBreakAfter( lastLevel, rowCursor );
			}
			catch ( OLAPException e )
			{
				logger.log( Level.SEVERE,
						Messages.getString( "CrosstabGroupExecutor.error.close.executor" ), //$NON-NLS-1$
						e );
			}
		}

		super.close( );

		groupCursors = null;
		lastLevel = null;
		elements = null;
		rowCursor = null;
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

				handleGroupPageBreakBefore( lastLevel, rowCursor );

				collectExecutable( );
			}
			catch ( OLAPException e )
			{
				logger.log( Level.SEVERE,
						Messages.getString( "CrosstabGroupExecutor.error.prepare.group" ), //$NON-NLS-1$
						e );
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

	private int getStartingGroupLevel( ) throws OLAPException
	{
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

		// check group start on previous group, to show header on
		// previous group
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

		// check group end on previous group, to show footer on
		// previous group
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

					// check group end on previous group, to show footer on
					// previous group
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
				logger.log( Level.SEVERE,
						Messages.getString( "CrosstabGroupExecutor.error.check.child.executor" ), //$NON-NLS-1$
						e );
			}
		}

		return false;
	}
}
