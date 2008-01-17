/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.util.List;

import javax.olap.OLAPException;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.olap.api.ICubeCursor;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * Data expression evaluator for chart in xtab.
 */

public final class BIRTChartXtabResultSetEvaluator
		extends
			BIRTCubeResultSetEvaluator
{

	private final ExtendedItemHandle handle;
	private boolean bSubCursor = false;

	public BIRTChartXtabResultSetEvaluator( ICubeResultSet rs,
			ExtendedItemHandle handle )
	{
		super( rs );
		this.handle = handle;
	}

	protected void initCubeCursor( ) throws OLAPException
	{
		ICubeCursor parent = (ICubeCursor) rs.getCubeCursor( );
		cubeCursor = parent;
		try
		{
			AggregationCellHandle cellHandle = ChartReportItemUtil.getXtabContainerCell( handle );
			LevelHandle levelAggColumn = cellHandle.getAggregationOnColumn( );
			LevelHandle levelAggRow = cellHandle.getAggregationOnRow( );
			if ( cellHandle.getSpanOverOnColumn( ) != null )
			{
				// Horizontal span
				if ( levelAggColumn != null && levelAggRow != null )
				{
					// cubeCursor = parent.getSubCubeCursor( null,
					// ChartReportItemUtil.createDimensionExpression(
					// levelAggRow ),
					// null,
					// null );

					// row cursor is the main
					List edges = cubeCursor.getOrdinateEdge( );
					this.mainEdgeCursor = (EdgeCursor) edges.get( 1 );
					this.subEdgeCursor = (EdgeCursor) edges.get( 0 );

					bSubCursor = true;
				}
				else
				{
					cubeCursor = parent;
				}

			}
			else if ( cellHandle.getSpanOverOnRow( ) != null )
			{
				// Vertical span
				if ( levelAggColumn != null && levelAggRow != null )
				{
					// cubeCursor = parent.getSubCubeCursor(
					// ChartReportItemUtil.createDimensionExpression(
					// levelAggColumn ),
					// null,
					// null,
					// null );

					// column cursor is the main
					List edges = cubeCursor.getOrdinateEdge( );
					this.mainEdgeCursor = (EdgeCursor) edges.get( 0 );
					this.subEdgeCursor = (EdgeCursor) edges.get( 1 );

					bSubCursor = true;
				}
				else
				{
					cubeCursor = parent;
				}
			}
			else
			{
				cubeCursor = parent;
			}
		}
		catch ( BirtException e )
		{
			logger.log( e );
			cubeCursor = parent;
		}

		if ( !bSubCursor )
		{
			List edges = cubeCursor.getOrdinateEdge( );
			this.mainEdgeCursor = (EdgeCursor) edges.get( 0 );
			this.subEdgeCursor = null;
		}
	}

	public boolean first( )
	{
		try
		{
			initCubeCursor( );

			if ( !bSubCursor )
			{
				return super.first( );
			}

			 mainEdgeCursor.first( );
			// ChartReportItemImpl item = getReportItem( );
			// for ( int cursorIndex = item.getIndexOfChartInXtab( );
			// cursorIndex > 0; cursorIndex-- )
			// {
			// // mainEdgeCursor.next( );
			// }
			// item.nextChartInXtab( );

			return subEdgeCursor.first( );
		}
		catch ( OLAPException e )
		{
			logger.log( e );
		}
		return false;
	}

	public boolean next( )
	{
		if ( !bSubCursor )
		{
			return super.next( );
		}

		try
		{
			return subEdgeCursor.next( );
		}
		catch ( OLAPException e )
		{
			logger.log( e );
		}
		return false;
	}

	// private ChartReportItemImpl getReportItem( )
	// throws ExtendedElementException
	// {
	// return (ChartReportItemImpl) handle.getReportItem( );
	// }
}
