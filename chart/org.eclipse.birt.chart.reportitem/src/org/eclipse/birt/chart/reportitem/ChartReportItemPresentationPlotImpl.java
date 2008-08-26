/***********************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Presentation implementation for Chart Plot in Cross tab
 */
public final class ChartReportItemPresentationPlotImpl extends
		ChartReportItemPresentationBase
{

	protected Bounds computeBounds( ) throws ChartException
	{
		final Bounds originalBounds = cm.getBlock( ).getBounds( );

		// we must copy the bounds to avoid that setting it on one object
		// unsets it on its precedent container
		Bounds bounds = (Bounds) EcoreUtil.copy( originalBounds );

		try
		{
			AggregationCellHandle xtabCell = ChartXTabUtil.getXtabContainerCell( handle );
			if ( xtabCell != null )
			{
				if ( xtabCell.getSpanOverOnColumn( ) != null )
				{
					// Horizontal direction
					CrosstabCellHandle columnCell = ChartXTabUtil.getInnermostLevelCell( xtabCell.getCrosstab( ),
							ICrosstabConstants.COLUMN_AXIS_TYPE );
					// Get the column width plus border
					double dWidth = ChartReportItemUtil.convertToPoints( xtabCell.getCrosstab( )
							.getColumnWidth( columnCell ),
							dpi );
					if ( dWidth == 0 )
					{
						dWidth = ChartXTabUtil.DEFAULT_COLUMN_WIDTH.getMeasure( );
					}
					// Set negative size to be replaced by actual size
					// Cell size includes border. In IE, cell size doesn't
					// include padding, but FF and PDF includes padding. To
					// avoid this computation conflict, set 0 padding in design
					// time.
					bounds.setWidth( -dWidth );
				}
				else if ( xtabCell.getSpanOverOnRow( ) != null )
				{
					// Vertical direction plus border
					CrosstabCellHandle rowCell = ChartXTabUtil.getInnermostLevelCell( xtabCell.getCrosstab( ),
							ICrosstabConstants.ROW_AXIS_TYPE );
					double dHeight = ChartReportItemUtil.convertToPoints( xtabCell.getCrosstab( )
							.getRowHeight( rowCell ),
							dpi );
					if ( dHeight == 0 )
					{
						dHeight = ChartXTabUtil.DEFAULT_ROW_HEIGHT.getMeasure( );
					}
					// Set negative size to be replaced by actual size
					// Cell size includes border. In IE, cell size doesn't
					// include padding, but FF and PDF includes padding. To
					// avoid this computation conflict, set 0 padding in design
					// time.
					bounds.setHeight( -dHeight );
				}
			}
		}
		catch ( BirtException e )
		{
			throw new ChartException( ChartReportItemPlugin.ID,
					ChartException.GENERATION,
					e );
		}

		return bounds;
	}

	protected void updateChartModel( )
	{
		// Update runtime model to render plot only
		ChartXTabUtil.updateModelToRenderPlot( cm, rtc.isRightToLeft( ) );
	}

}
