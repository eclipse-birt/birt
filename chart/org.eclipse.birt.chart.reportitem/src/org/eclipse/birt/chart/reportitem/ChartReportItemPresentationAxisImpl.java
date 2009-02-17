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
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.ibm.icu.util.Calendar;

/**
 * Presentation implementation for Chart Axis in Cross tab
 */
public final class ChartReportItemPresentationAxisImpl extends
		ChartReportItemPresentationBase
{

	public void setModelObject( ExtendedItemHandle eih )
	{
		// Get the host chart handle from host chart
		handle = (ExtendedItemHandle) eih.getElementProperty( ChartReportItemConstants.PROPERTY_HOST_CHART );
		IReportItem item = getReportItem( handle );
		if ( item == null )
		{
			return;
		}
		cm = (Chart) ( (ChartReportItemImpl) item ).getProperty( ChartReportItemConstants.PROPERTY_CHART );

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
		ChartXTabUtil.updateModelToRenderAxis( cm, rtc.isRightToLeft( ) );
	}

	@Override
	protected IDataRowExpressionEvaluator createEvaluator( IBaseResultSet set )
			throws ChartException
	{
		// If no shared scale, to get evaluator from query.
		if ( rtc.getSharedScale( ) == null || !rtc.getSharedScale( ).isShared( ) )
		{
			return super.createEvaluator( set );
		}

		// Check the axis type to return the dummy data with correct type
		final boolean bDatetypeAxis;
		if ( cm instanceof ChartWithAxes )
		{
			ChartWithAxes cwa = (ChartWithAxes) cm;
			Axis yAxis = cwa.getOrthogonalAxes( cwa.getBaseAxes( )[0], true )[0];
			bDatetypeAxis = yAxis.getType( ) == AxisType.DATE_TIME_LITERAL;
		}
		else
		{
			bDatetypeAxis = false;
		}

		// Return a dummy data set since axis chart can render without data
		return new IDataRowExpressionEvaluator( ) {

			private int count = 1;

			public void close( )
			{

			}

			public Object evaluate( String expression )
			{
				return bDatetypeAxis ? Calendar.getInstance( )
						: Integer.valueOf( 1 );
			}

			public Object evaluateGlobal( String expression )
			{
				return evaluate( expression );
			}

			public boolean first( )
			{
				return true;
			}

			public boolean next( )
			{
				return count-- > 0;
			}
		};
	}

}
