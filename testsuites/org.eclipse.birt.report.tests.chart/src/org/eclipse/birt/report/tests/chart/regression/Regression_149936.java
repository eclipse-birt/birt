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

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.impl.AreaSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.report.tests.chart.ChartTestCase;

/**
 * Regression description:
 * </p>
 * Invisible Major Gridlines Appear in Stacked Area Chart
 * </p>
 * Test description:
 * </p>
 * Create a stacked area chart, set large Y-axis values and X-axis minor grid
 * and ticks visible
 * </p>
 */

public class Regression_149936 extends ChartTestCase
{

	private static String GOLDEN = "Regression_149936.jpg"; //$NON-NLS-1$
	private static String OUTPUT = "Regression_149936.jpg"; //$NON-NLS-1$	

	/**
	 * A chart model instance
	 */
	private Chart cm = null;

	/**
	 * The swing rendering device
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
		new Regression_149936( );
	}

	/**
	 * Constructor
	 */
	public Regression_149936( )
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
		cm = createAreaChart( );
		BufferedImage img = new BufferedImage(
				800,
				800,
				BufferedImage.TYPE_INT_ARGB );
		Graphics g = img.getGraphics( );

		Graphics2D g2d = (Graphics2D) g;
		dRenderer.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, g2d );
		dRenderer.setProperty( IDeviceRenderer.FILE_IDENTIFIER, this
				.genOutputFile( OUTPUT )
				  ); //$NON-NLS-1$
		Bounds bo = BoundsImpl.create( 0, 0, 800, 800 );
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
			e.printStackTrace( );
		}
	}

	public void test_regression_149936( ) throws Exception
	{
		Regression_149936 st = new Regression_149936( );
		assertTrue( st.compareImages( GOLDEN, OUTPUT ) );
	}

	/**
	 * Creates a area chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createAreaChart( )
	{
		ChartWithAxes cwaArea = ChartWithAxesImpl.create( );

		// Chart Type
		cwaArea.setType( "Area Chart" );

		// Title
		cwaArea.getTitle( ).getLabel( ).getCaption( ).setValue(
				"Area Chart With Multi-Yseries" ); //$NON-NLS-1$
		cwaArea.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );
		cwaArea.getTitle( ).getLabel( ).setVisible( true );

		// Plot
		cwaArea.getPlot( ).getClientArea( ).getOutline( ).setVisible( false );
		cwaArea.getPlot( ).getClientArea( ).setBackground(
				ColorDefinitionImpl.create( 255, 255, 225 ) );

		// Legend
		Legend lg = cwaArea.getLegend( );
		lg.getText( ).getFont( ).setSize( 16 );
		lg.getInsets( ).set( 10, 5, 0, 0 );

		lg.getOutline( ).setStyle( LineStyle.DOTTED_LITERAL );
		lg.getOutline( ).setColor( ColorDefinitionImpl.create( 214, 100, 12 ) );
		lg.getOutline( ).setVisible( true );

		lg
				.setBackground( GradientImpl.create( ColorDefinitionImpl
						.create( 225, 225, 255 ), ColorDefinitionImpl.create(
						255,
						255,
						225 ), -35, false ) );
		lg.setAnchor( Anchor.SOUTH_LITERAL );
		lg.setItemType( LegendItemType.SERIES_LITERAL );

		lg.getClientArea( ).setBackground( ColorDefinitionImpl.ORANGE( ) );
		lg.setPosition( Position.BELOW_LITERAL );
		lg.setOrientation( Orientation.VERTICAL_LITERAL );

		// X-Axis
		Axis xAxisPrimary = ( (ChartWithAxesImpl) cwaArea )
				.getPrimaryBaseAxes( )[0];
		xAxisPrimary.getTitle( ).setVisible( false );

		xAxisPrimary.setType( AxisType.TEXT_LITERAL );
		xAxisPrimary.getOrigin( ).setType( IntersectionType.VALUE_LITERAL );
		xAxisPrimary.getLabel( ).getCaption( ).setColor(
				ColorDefinitionImpl.GREEN( ).darker( ) );
		xAxisPrimary.getLabel( ).getCaption( ).getFont( ).setRotation( 90 );

		xAxisPrimary.getMinorGrid( ).setLineAttributes(
				LineAttributesImpl.create(
						ColorDefinitionImpl.BLACK( ),
						LineStyle.DASH_DOTTED_LITERAL,
						2 ) );
		xAxisPrimary.getMinorGrid( ).setTickAttributes(
				LineAttributesImpl.create(
						ColorDefinitionImpl.RED( ),
						LineStyle.SOLID_LITERAL,
						1 ) );
		xAxisPrimary.getMinorGrid( ).setTickStyle( TickStyle.ABOVE_LITERAL );
		xAxisPrimary.getScale( ).setMinorGridsPerUnit( 2 );

		// Y-Axis
		Axis yAxisPrimary = ( (ChartWithAxesImpl) cwaArea )
				.getPrimaryOrthogonalAxis( xAxisPrimary );
		yAxisPrimary.getLabel( ).getCaption( ).setValue( "Sales Growth" ); //$NON-NLS-1$
		yAxisPrimary.getLabel( ).getCaption( ).setColor(
				ColorDefinitionImpl.BLUE( ) );

		yAxisPrimary.getTitle( ).setVisible( false );
		yAxisPrimary.setType( AxisType.LINEAR_LITERAL );
		yAxisPrimary.getOrigin( ).setType( IntersectionType.VALUE_LITERAL );

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl.create( new String[]{
				"1145923200", "1145923800", "1145924400", "1145925000",
				"1145925600", "1145926200", "1145923200", "1145923800",
				"1145924400", "1145925000", "1145925600", "1145926200",
				"1145926800", "1145927400", "1145928000", "1145928600",
				"1145929200", "1145929800", "1145930400", "1145931000",
				"1145931600", "1145932200", "1145932800", "1145334000",
				"1145934000", "1145934600", "1145935200", "1145935800"} );
		NumberDataSet dsNumericValues1 = NumberDataSetImpl
				.create( new double[]{10, 10, 10, 10, 10, 10, 10, 10, 1600000,
						1800000, 1600000, 10, 10, 10, 10, 10, 10, 10, 10, 10,
						10, 10, 10, 10, 10, 10, 10, 10} );

		// X-Series
		Series seBase = SeriesImpl.create( );
		seBase.setDataSet( dsStringValue );

		SeriesDefinition sdX = SeriesDefinitionImpl.create( );
		xAxisPrimary.getSeriesDefinitions( ).add( sdX );
		sdX.getSeries( ).add( seBase );

		// Y-Series
		AreaSeries as = (AreaSeries) AreaSeriesImpl.create( );
		as.setSeriesIdentifier( "Origin" ); //$NON-NLS-1$
		as.getLabel( ).setVisible( true );
		as.setDataSet( dsNumericValues1 );
		as.setStacked( true );

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		yAxisPrimary.getSeriesDefinitions( ).add( sdY );
		sdY.getSeriesPalette( ).update( ColorDefinitionImpl.GREEN( ) );
		sdY.getSeries( ).add( as );

		return cwaArea;
	}
}