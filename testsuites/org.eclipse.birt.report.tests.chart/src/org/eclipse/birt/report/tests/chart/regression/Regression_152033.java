/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.chart.regression;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Date;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.DateTimeDataSet;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.DateTimeDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.report.tests.chart.ChartTestCase;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;

/**
 * Run mode: StandAlone Regression description:
 * </p>
 * Can't change the label of the X-Axis of a scatter chart
 * </p>
 * Test description:
 * <p>
 * Change the label of the X-Axis of a scatter chart, verify if it can be take
 * effect.
 * </p>
 */

public class Regression_152033 extends ChartTestCase
{

	private static String OUTPUT = "Regression_152033.jpg"; //$NON-NLS-1$
	/**
	 * A chart model instance
	 */
	private Chart cm = null;

	/**
	 * The jpg rendering device
	 */
	private IDeviceRenderer dRenderer = null;

	private GeneratedChartState gcs = null;

	/**
	 * execute application
	 * 
	 * @param args
	 */
	public static void main( String[] args )
	{
		new Regression_152033( );
	}

	/**
	 * Constructor
	 */
	public Regression_152033( )
	{
		final PluginSettings ps = PluginSettings.instance( );
		try
		{
			dRenderer = ps.getDevice( "dv.JPG" );//$NON-NLS-1$

		}
		catch ( ChartException ex )
		{
			ex.printStackTrace( );
		}
		cm = createDateTimeScatter( );
		BufferedImage img = new BufferedImage(
				600,
				600,
				BufferedImage.TYPE_INT_ARGB );
		Graphics g = img.getGraphics( );

		Graphics2D g2d = (Graphics2D) g;
		dRenderer.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, g2d );
		dRenderer.setProperty( IDeviceRenderer.FILE_IDENTIFIER, this
				.getClassFolder2( )
				  ); //$NON-NLS-1$
		Bounds bo = BoundsImpl.create( 0, 0, 600, 600 );
		bo.scale( 72d / dRenderer.getDisplayServer( ).getDpiResolution( ) );

		Generator gr = Generator.instance( );

		try
		{
			gcs = gr.build(
					dRenderer.getDisplayServer( ),
					cm,
					bo,
					null,
					null,
					null );
			gr.render( dRenderer, gcs );
		}
		catch ( ChartException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}

	}

	/**
	 * Creates a scatter chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createDateTimeScatter( )
	{
		ChartWithAxes cwaScatter = ChartWithAxesImpl.create( );

		// TODO: research running script under plugin test.

		cwaScatter
				.setScript( "function beforeDrawAxisLabel(axis, label, context)" //$NON-NLS-1$
						+ "{label.getCaption().setValue(\"ABC\"); " //$NON-NLS-1$
						+ "axis.setLabel(label);}" //$NON-NLS-1$
				);

		// Plot

		cwaScatter.getPlot( ).getClientArea( ).getOutline( ).setVisible( true );

		// Title
		cwaScatter.getTitle( ).setVisible( false );

		// X-Axis
		Axis xAxisPrimary = ( (ChartWithAxesImpl) cwaScatter )
				.getPrimaryBaseAxes( )[0];
		xAxisPrimary.getTitle( ).getCaption( ).setValue( "Time" );
		xAxisPrimary.setType( AxisType.DATE_TIME_LITERAL );
		xAxisPrimary.getTitle( ).setVisible( true );
		xAxisPrimary.getOrigin( ).setType( IntersectionType.MIN_LITERAL );

		// Y-Axis
		Axis yAxisPrimary = ( (ChartWithAxesImpl) cwaScatter )
				.getPrimaryOrthogonalAxis( xAxisPrimary );
		yAxisPrimary.getTitle( ).getCaption( ).setValue( "Score" );

		yAxisPrimary.getTitle( ).setBackground( ColorDefinitionImpl.WHITE( ) );
		yAxisPrimary.getTitle( ).setVisible( true );
		yAxisPrimary.setType( AxisType.LINEAR_LITERAL );
		yAxisPrimary.getOrigin( ).setType( IntersectionType.MIN_LITERAL );

		// create chart data
		int seriesLen = 10;
		double[] yseries = new double[seriesLen];
		Calendar[] datetime = new Calendar[seriesLen];
		for ( int i = 0; i < seriesLen; i++ )
		{
			yseries[i] = ( (double) i );
			datetime[i] = new GregorianCalendar( );
			datetime[i].setTime( new Date( i ) );
			// uncomment the following line and chart works ok
			// datetime[i].setTime(new Date(i*1000));
		}
		NumberDataSet dsNumericValues2 = NumberDataSetImpl.create( yseries );
		DateTimeDataSet dsDateTime = DateTimeDataSetImpl.create( datetime );

		// X-Series
		Series seBase = SeriesImpl.create( );
		seBase.setDataSet( dsDateTime );
		SeriesDefinition sdX = SeriesDefinitionImpl.create( );
		xAxisPrimary.getSeriesDefinitions( ).add( sdX );
		sdX.getSeries( ).add( seBase );

		// Y-Series
		ScatterSeries ss = (ScatterSeries) ScatterSeriesImpl.create( );
		ss.setDataSet( dsNumericValues2 );
		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		yAxisPrimary.getSeriesDefinitions( ).add( sdY );
		sdY.getSeries( ).add( ss );

		return cwaScatter;
	}

}
