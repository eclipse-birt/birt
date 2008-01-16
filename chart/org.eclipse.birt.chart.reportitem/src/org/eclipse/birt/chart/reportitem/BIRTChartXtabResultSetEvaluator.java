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

	public BIRTChartXtabResultSetEvaluator( ICubeResultSet rs,
			ExtendedItemHandle handle )
	{
		super( rs );
		this.handle = handle;
	}

	protected ICubeCursor getCubeCursor( )
	{
		if ( cursor == null )
		{
			ICubeCursor parent = (ICubeCursor) rs.getCubeCursor( );
			// Chart cm = ChartReportItemUtil.getChartFromHandle( handle );
			// boolean bTransposed = false;
			// if ( cm instanceof ChartWithAxes
			// && ( (ChartWithAxes) cm ).isTransposed( ) )
			// {
			// bTransposed = true;
			// }
			try
			{
				// CubeHandle cube = ChartReportItemUtil.getBindingCube( handle
				// );
				AggregationCellHandle cellHandle = ChartReportItemUtil.getXtabContainerCell( handle );
				LevelHandle levelAggColumn = cellHandle.getAggregationOnColumn( );
				LevelHandle levelAggRow = cellHandle.getAggregationOnRow( );
				if ( cellHandle.getSpanOverOnColumn( ) != null )
				{
					// Horizontal span
					if ( levelAggColumn != null && levelAggRow != null )
					{
						cursor = parent.getSubCubeCursor( null,
								ChartReportItemUtil.createDimensionExpression( levelAggRow ),
								null,
								null );
					}
					else
					{
						cursor = parent;
					}

				}
				else if ( cellHandle.getSpanOverOnRow( ) != null )
				{
					// Vertical span
					if ( levelAggColumn != null && levelAggRow != null )
					{
						cursor = parent.getSubCubeCursor( ChartReportItemUtil.createDimensionExpression( levelAggColumn ),
								null,
								null,
								null );
					}
					else
					{
						cursor = parent;
					}
				}
				else
				{
					cursor = parent;
				}
			}
			catch ( BirtException e )
			{
				logger.log( e );
				cursor = parent;
			}
		}
		return cursor;
	}
}
