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
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Presentation implementation for Chart Plot in Cross tab
 */
public final class ChartReportItemPresentationPlotImpl
		extends
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
							.getColumnWidth( columnCell ) );
					double dLeftBorder = ChartReportItemUtil.convertToPoints( xtabCell.getCrosstabHandle( )
							.getDimensionProperty( StyleHandle.BORDER_LEFT_WIDTH_PROP ) );
					double dRightBorder = ChartReportItemUtil.convertToPoints( xtabCell.getCrosstabHandle( )
							.getDimensionProperty( StyleHandle.BORDER_RIGHT_WIDTH_PROP ) );
					// Set negative size to be replaced by actual size
					bounds.setWidth( -dWidth - dLeftBorder - dRightBorder );
				}
				else if ( xtabCell.getSpanOverOnRow( ) != null )
				{
					// Vertical direction plus border
					CrosstabCellHandle rowCell = ChartXTabUtil.getInnermostLevelCell( xtabCell.getCrosstab( ),
							ICrosstabConstants.ROW_AXIS_TYPE );
					double dHeight = ChartReportItemUtil.convertToPoints( xtabCell.getCrosstab( )
							.getRowHeight( rowCell ) );
					double dTopBorder = ChartReportItemUtil.convertToPoints( xtabCell.getCrosstabHandle( )
							.getDimensionProperty( StyleHandle.BORDER_TOP_WIDTH_PROP ) );
					double dBottomBorder = ChartReportItemUtil.convertToPoints( xtabCell.getCrosstabHandle( )
							.getDimensionProperty( StyleHandle.BORDER_BOTTOM_WIDTH_PROP ) );
					// Set negative size to be replaced by actual size
					bounds.setHeight( -dHeight - dTopBorder - dBottomBorder );
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
		ChartXTabUtil.updateModelToRenderPlot( cm );
	}

}
