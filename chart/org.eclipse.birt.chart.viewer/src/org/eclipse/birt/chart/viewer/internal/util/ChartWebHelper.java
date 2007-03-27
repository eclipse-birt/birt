/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.viewer.internal.util;

import java.io.File;
import java.io.FileInputStream;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.Serializer;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.impl.SerializerImpl;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.emf.common.util.EList;

/**
 * Utility class for web component
 */

public class ChartWebHelper
{

	/**
	 * Parses a xml file to chart model instance
	 * 
	 * @param strPath
	 *            chart xml file path
	 * @return
	 * @throws ChartException
	 */
	public static Chart parseChart( String strPath ) throws ChartException
	{
		Chart chartModel = null;
		final File chartFile = new File( strPath );
		// Reads the chart model
		try
		{
			if ( chartFile.exists( ) )
			{
				Serializer serializer = SerializerImpl.instance( );
				chartModel = serializer.read( new FileInputStream( chartFile ) );
			}
		}
		catch ( Exception e )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.NOT_FOUND,
					e );
		}
		return chartModel;
	}

	/**
	 * Checks if the output type is supported
	 * 
	 * @param type
	 *            output type
	 * @return
	 */
	public static boolean checkOutputType( String type )
	{
		try
		{
			return ChartUtil.isOutputFormatSupport( type );
		}
		catch ( ChartException e )
		{
			return false;
		}
	}

	/**
	 * Checks if current chart has runtime datasets.
	 * 
	 * @param cm
	 *            chart model
	 * @return
	 */
	public static boolean isChartInRuntime( Chart cm )
	{
		if ( cm instanceof ChartWithAxes )
		{
			Axis bAxis = (Axis) ( (ChartWithAxes) cm ).getAxes( ).get( 0 );
			EList oAxes = bAxis.getAssociatedAxes( );
			for ( int i = 0; i < oAxes.size( ); i++ )
			{
				Axis oAxis = (Axis) oAxes.get( i );
				EList oSeries = oAxis.getSeriesDefinitions( );
				for ( int j = 0; j < oSeries.size( ); j++ )
				{
					SeriesDefinition sd = (SeriesDefinition) oSeries.get( j );
					if ( sd.getRunTimeSeries( ).size( ) > 0 )
					{
						return true;
					}
				}
			}
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			SeriesDefinition bsd = (SeriesDefinition) ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
					.get( 0 );
			EList osds = bsd.getSeriesDefinitions( );
			for ( int i = 0; i < osds.size( ); i++ )
			{
				SeriesDefinition osd = (SeriesDefinition) osds.get( i );
				if ( osd.getRunTimeSeries( ).size( ) > 0 )
				{
					return true;
				}
			}
		}
		return false;
	}
}
