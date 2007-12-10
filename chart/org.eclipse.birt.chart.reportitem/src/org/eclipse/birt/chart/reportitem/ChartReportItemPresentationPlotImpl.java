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

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Presentation implementation for Chart Plot in Cross tab
 */
public final class ChartReportItemPresentationPlotImpl
		extends
			ChartReportItemPresentationBase
{

	protected Bounds computeBounds( )
	{
		final Bounds originalBounds = cm.getBlock( ).getBounds( );

		// we must copy the bounds to avoid that setting it on one object
		// unsets it on its precedent container

		Bounds bounds = (Bounds) EcoreUtil.copy( originalBounds );
		if ( cm instanceof ChartWithAxes )
		{
			// Set the dynamic size with negative value, which will be replaced by the
			// real value after computation when building chart
//			ChartWithAxes chart = (ChartWithAxes) cm;
//			if ( chart.isTransposed( ) )
//			{
//				bounds.setHeight( 0 );
//			}
//			else
//			{
//				bounds.setWidth( 0 );
//			}
		}
		return bounds;
	}

	protected void updateChartModel( )
	{
		if ( cm instanceof ChartWithAxes )
		{
			ChartWithAxes chart = (ChartWithAxes) cm;
			chart.getLegend( ).setVisible( false );
			chart.getTitle( ).setVisible( false );
			chart.getPlot( ).getOutline( ).setVisible( false );
			chart.getBlock( ).getInsets( ).set( 0, 0, 0, 0 );
			// chart.getPlot( ).getInsets( ).set( 0, 0, 0, 0 );
			// chart.getPlot( ).getClientArea( ).getInsets( ).set( 0, 0, 0, 0 );

			// boolean bTransposed = chart.isTransposed( );
			Axis xAxis = (Axis) chart.getAxes( ).get( 0 );
			Axis yAxis = (Axis) xAxis.getAssociatedAxes( ).get( 0 );

			xAxis.getTitle( ).setVisible( false );
			xAxis.getLabel( ).setVisible( false );
			xAxis.getLineAttributes( ).setVisible( false );
			xAxis.getMajorGrid( ).getTickAttributes( ).setVisible( false );
			xAxis.getMinorGrid( ).getTickAttributes( ).setVisible( false );		

			yAxis.getTitle( ).setVisible( false );
			yAxis.getLabel( ).setVisible( false );
			yAxis.getLineAttributes( ).setVisible( false );
			yAxis.getMajorGrid( ).getTickAttributes( ).setVisible( false );
			yAxis.getMinorGrid( ).getTickAttributes( ).setVisible( false );
		}
	}

}
