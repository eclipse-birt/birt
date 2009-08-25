/*******************************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.Serializer;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.impl.SerializerImpl;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.core.runtime.Platform;

/**
 * Utility class for Chart integration as report item
 */

public class ChartReportItemUtil extends ChartItemUtil
{


	/**
	 * Checks if shared scale is needed when computation
	 * 
	 * @param eih
	 *            handle
	 * @param cm
	 *            chart model
	 * @return shared binding needed or not
	 * @since 2.3
	 */
	public static boolean canScaleShared( ReportItemHandle eih, Chart cm )
	{
		return cm instanceof ChartWithAxes
				&& eih.getDataSet( ) == null
				&& getBindingHolder( eih ) != null
				&& ChartXTabUtil.isInXTabMeasureCell( eih );
	}

	/**
	 * Creates the default bounds for chart model.
	 * 
	 * @param eih
	 *            chart handle
	 * @param cm
	 *            chart model
	 * @return default bounds
	 * @since 2.3
	 */
	public static Bounds createDefaultChartBounds( ExtendedItemHandle eih,
			Chart cm )
	{
		// Axis chart case
		if ( ChartXTabUtil.isAxisChart( eih ) )
		{
			// Axis chart must be ChartWithAxes
			ChartWithAxes cmWA = (ChartWithAxes) cm;
			if ( cmWA.isTransposed( ) )
			{
				return BoundsImpl.create( 0,
						0,
						DEFAULT_CHART_BLOCK_WIDTH,
						DEFAULT_AXIS_CHART_BLOCK_SIZE );
			}
			else
			{
				return BoundsImpl.create( 0,
						0,
						DEFAULT_AXIS_CHART_BLOCK_SIZE,
						DEFAULT_CHART_BLOCK_HEIGHT );
			}
		}
		// Plot or ordinary chart case
		else
		{
			return BoundsImpl.create( 0,
					0,
					DEFAULT_CHART_BLOCK_WIDTH,
					DEFAULT_CHART_BLOCK_HEIGHT );
		}
	}

	/**
	 * Checks if result set is empty
	 * 
	 * @param set
	 *            result set
	 * @throws BirtException
	 * @since 2.3
	 */
	public static boolean isEmpty( IBaseResultSet set ) throws BirtException
	{
		if ( set instanceof IQueryResultSet )
		{
			return ( (IQueryResultSet) set ).isEmpty( );
		}
		// TODO add code to check empty for ICubeResultSet
		return false;
	}
	

	public static <T> T getAdapter( Object adaptable, Class<T> adapterClass )
	{
		IAdapterManager adapterManager = Platform.getAdapterManager( );
		return adapterClass.cast( adapterManager.loadAdapter( adaptable,
				adapterClass.getName( ) ) );
	}

	public static Serializer instanceSerializer( ExtendedItemHandle handle )
	{

		IChartReportItemFactory factory = getAdapter( handle,
				IChartReportItemFactory.class );

		if ( factory != null )
		{
			return factory.createSerializer( handle );
		}
		else
		{
			return SerializerImpl.instance( );
		}
	}
}
