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
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * Presentation implementation for Chart Axis in Cross tab
 */
public final class ChartReportItemPresentationAxisImpl
		extends
			ChartReportItemPresentationBase
{

	public void setModelObject( ExtendedItemHandle eih )
	{
		IReportItem item = getReportItem( eih );
		if ( item == null )
		{
			return;
		}
		cm = (Chart) ( (ChartReportItemImpl) item ).getProperty( ChartReportItemUtil.PROPERTY_CHART );
		// Set the host chart handle from axis chart
		handle = (ExtendedItemHandle) eih.getElementProperty( ChartReportItemUtil.PROPERTY_HOST_CHART );

		setChartModelObject( item );
	}

	protected Bounds computeBounds( ) throws ChartException
	{
		final Bounds originalBounds = cm.getBlock( ).getBounds( );

		// we must copy the bounds to avoid that setting it on one object
		// unsets it on its precedent container

		Bounds bounds = (Bounds) EcoreUtil.copy( originalBounds );
		if ( cm instanceof ChartWithAxes )
		{
			// Set the dynamic size with zero, which will be replaced by the
			// real value after computation when building chart
			ChartWithAxes chart = (ChartWithAxes) cm;
			if ( chart.isTransposed( ) )
			{
				bounds.setHeight( 0 );
			}
			else
			{
				bounds.setWidth( 0 );
			}
		}
		return bounds;
	}

	protected void updateChartModel( )
	{
		// Update runtime model to render axis only
		ChartReportItemUtil.updateModelToRenderAxis( cm );
	}

}
