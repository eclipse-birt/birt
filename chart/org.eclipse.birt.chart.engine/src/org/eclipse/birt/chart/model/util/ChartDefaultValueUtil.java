/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.model.util;

import java.util.Collection;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.emf.common.notify.Adapter;

/**
 * This class provides methods to set default value into chart elements.
 * 
 */

public class ChartDefaultValueUtil
{

	/**
	 * Removes all series palettes.
	 * 
	 * @param chart
	 */
	public static void removeSerlesPalettes( Chart chart )
	{
		ChartUtil.getCategorySeriesDefinition( chart )
				.getSeriesPalette( )
				.getEntries( )
				.clear( );
		for ( SeriesDefinition sd : ChartUtil.getValueSeriesDefinitions( chart ) )
		{
			sd.getSeriesPalette( ).getEntries( ).clear( );
		}
	}

	/**
	 * Updates series palettes to default values.
	 * 
	 * @param chart
	 */
	public static void updateSeriesPalettes( Chart chart )
	{
		updateSeriesPalettes( chart, null );
	}
	
	/**
	 * Updates series palettes to default values.
	 * 
	 * @param chart
	 * @param adapters
	 */
	public static void updateSeriesPalettes( Chart chart,
			Collection<? extends Adapter> adapters )
	{
		// Set series palette for category series definition.
		ChartUtil.getCategorySeriesDefinition( chart )
				.setSeriesPalette( DefaultValueProvider.defSeriesDefinition( 0 )
						.getSeriesPalette( )
						.copyInstance( ) );
		if ( adapters != null )
		{
			ChartUtil.getCategorySeriesDefinition( chart )
					.getSeriesPalette( )
					.eAdapters( )
					.addAll( adapters );
		}
		// Set series palettes for value series definitions.
		if ( ChartUtil.hasMultipleYAxes( chart ) )
		{
			int axesNum = ChartUtil.getOrthogonalAxisNumber( chart );
			for ( int i = 0; i < axesNum; i++ )
			{
				int pos = i;
				SeriesDefinition[] seriesDefns = ChartUtil.getOrthogonalSeriesDefinitions( chart,
						i )
						.toArray( new SeriesDefinition[]{} );
				for ( int j = 0; j < seriesDefns.length; j++ )
				{
					pos += j;
					seriesDefns[j].setSeriesPalette( DefaultValueProvider.defSeriesDefinition( pos )
							.getSeriesPalette( )
							.copyInstance( ) );
					if ( adapters != null )
					{
						seriesDefns[j].getSeriesPalette( )
								.eAdapters( )
								.addAll( adapters );
					}
				}
			}
		}
		else
		{
			int i = 0;
			for ( SeriesDefinition sd : ChartUtil.getValueSeriesDefinitions( chart ) )
			{
				sd.setSeriesPalette( DefaultValueProvider.defSeriesDefinition( i )
						.getSeriesPalette( )
						.copyInstance( ) );
				if ( adapters != null )
				{
					sd.getSeriesPalette( ).eAdapters( ).addAll( adapters );
				}
				i++;
			}
		}
	}
}
