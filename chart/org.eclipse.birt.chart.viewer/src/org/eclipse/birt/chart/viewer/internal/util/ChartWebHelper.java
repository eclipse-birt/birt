/*******************************************************************************
 * Copyright (c) 2006, 2007 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.viewer.internal.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import jakarta.servlet.ServletContext;

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
	 * Parses a XML file to chart model instance
	 * 
	 * @param strPath
	 *            chart XML file path
	 * @return chart model
	 * @throws ChartException
	 */
	public static Chart parseChart( String strPath ) throws ChartException
	{
		if ( strPath == null )
		{
			return null;
		}
		Chart chartModel = null;
		final File chartFile = new File( strPath );
		// Reads the chart model
		InputStream is = null;
		try
		{
			if ( chartFile.exists( ) )
			{
				Serializer serializer = SerializerImpl.instance( );
				is = new FileInputStream( chartFile );
				chartModel = serializer.read( is );
			}
		}
		catch ( Exception e )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.NOT_FOUND,
					e );
		}
		finally
		{
			if ( is != null )
			{
				try
				{
					is.close( );
				}
				catch ( IOException e )
				{

				}
			}
		}
		return chartModel;
	}

	/**
	 * Checks if the output type is supported
	 * 
	 * @param type
	 *            output type
	 * @return supported or not
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
	 * Checks if current chart has runtime data sets.
	 * 
	 * @param cm
	 *            chart model
	 * @return has runtime data or not
	 */
	public static boolean isChartInRuntime( Chart cm )
	{
		if ( cm instanceof ChartWithAxes )
		{
			Axis bAxis = ( (ChartWithAxes) cm ).getAxes( ).get( 0 );
			EList<Axis> oAxes = bAxis.getAssociatedAxes( );
			for ( int i = 0; i < oAxes.size( ); i++ )
			{
				Axis oAxis = oAxes.get( i );
				EList<SeriesDefinition> oSeries = oAxis.getSeriesDefinitions( );
				for ( int j = 0; j < oSeries.size( ); j++ )
				{
					SeriesDefinition sd = oSeries.get( j );
					if ( sd.getRunTimeSeries( ).size( ) > 0 )
					{
						return true;
					}
				}
			}
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			SeriesDefinition bsd = ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
					.get( 0 );
			EList<SeriesDefinition> osds = bsd.getSeriesDefinitions( );
			for ( int i = 0; i < osds.size( ); i++ )
			{
				SeriesDefinition osd = osds.get( i );
				if ( osd.getRunTimeSeries( ).size( ) > 0 )
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns the real path of the file in the web folder
	 * 
	 * @param context
	 *            servlet context
	 * @param fileName
	 *            the relative path of the file
	 */
	public static String getRealPath( ServletContext context, String fileName )
	{
		String path = context.getRealPath( "/" ); //$NON-NLS-1$

		if ( path == null )
		{
			// resources are in a .war (JBoss, WebLogic)
			java.net.URL url;
			try
			{
				url = context.getResource( "/" ); //$NON-NLS-1$
				path = url.getPath( );
			}
			catch ( MalformedURLException e )
			{
				e.printStackTrace( );
			}
		}
		// In WebSphere, path may not end with separator
		if ( path != null && path.length( ) > 0 )
		{
			if ( path.charAt( path.length( ) - 1 ) != '\\'
					|| path.charAt( path.length( ) - 1 ) != '/' )
			{
				path += File.separator;
			}
		}
		return path + fileName;
	}
}
