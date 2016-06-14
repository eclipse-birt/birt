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
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Anchor;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.LeaderLineStyle;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.report.tests.chart.ChartTestCase;

/**
 * Regression description:
 * </p>
 * The pie chart always stretches to use the available space, whick makes it
 * difficult to have a circle or fix a ratio
 * </p>
 * Test description:
 * </p>
 * The pie chart can have a fixed ratio
 * </p>
 */

public class Regression_113536 extends ChartTestCase
{

	private static String GOLDEN = "Regression_113536.jpg"; //$NON-NLS-1$
	private static String OUTPUT = "Regression_113536.jpg"; //$NON-NLS-1$

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

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
		new Regression_113536( );
	}

	/**
	 * Constructor
	 */
	public Regression_113536( )
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
		cm = createPieChart( );
		BufferedImage img = new BufferedImage(
				600,
				600,
				BufferedImage.TYPE_INT_ARGB );
		Graphics g = img.getGraphics( );

		Graphics2D g2d = (Graphics2D) g;
		dRenderer.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, g2d );
		dRenderer.setProperty( IDeviceRenderer.FILE_IDENTIFIER, this
				.genOutputFile( OUTPUT )
				  );

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
			e.printStackTrace( );
		}
	}

	public void test_regression_113536( ) throws Exception
	{
		new Regression_113536( );
		assertTrue( this.compareImages( GOLDEN, OUTPUT ) );
	}

	/**
	 * Creates a pie chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createPieChart( )
	{
		ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create( );

		// Chart Type
		cwoaPie.setType( "Pie Chart" );

		// Title
		cwoaPie.getTitle( ).getLabel( ).getCaption( ).setValue(
				"Sample Pie Chart" );
		cwoaPie.getBlock( ).setBounds( BoundsImpl.create( 0, 0, 252, 288 ) );
		cwoaPie.getBlock( ).getOutline( ).setVisible( true );

		// Plot
		cwoaPie.getPlot( ).getClientArea( ).getOutline( ).setVisible( false );
		cwoaPie.getPlot( ).getClientArea( ).setBackground(
				ColorDefinitionImpl.create( 255, 255, 225 ) );

		// Legend
		Legend lg = cwoaPie.getLegend( );
		lg.getText( ).getFont( ).setSize( 16 );
		lg.getInsets( ).set( 10, 5, 0, 0 );

		lg.getOutline( ).setStyle( LineStyle.DASH_DOTTED_LITERAL );
		lg.getOutline( ).setColor( ColorDefinitionImpl.create( 214, 100, 12 ) );
		lg.getOutline( ).setVisible( true );

		lg
				.setBackground( GradientImpl.create( ColorDefinitionImpl
						.create( 225, 225, 255 ), ColorDefinitionImpl.create(
						255,
						255,
						225 ), -35, false ) );
		lg.setAnchor( Anchor.EAST_LITERAL );
		lg.setItemType( LegendItemType.CATEGORIES_LITERAL );

		lg.getClientArea( ).setBackground( ColorDefinitionImpl.ORANGE( ) );
		lg.setPosition( Position.LEFT_LITERAL );
		lg.setOrientation( Orientation.VERTICAL_LITERAL );

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl.create( new String[]{
				"Keyboards", "Moritors", "Printers", "Mortherboards"} );
		NumberDataSet dsNumericValues1 = NumberDataSetImpl
				.create( new double[]{143.26, 156.55, 95.25, 47.56} );
		NumberDataSet dsNumericValues2 = NumberDataSetImpl
				.create( new double[]{31.24, 56.55, 5.25, 4.56} );

		// Series-1
		Series seCategory = SeriesImpl.create( );
		seCategory.setDataSet( dsStringValue );

		SeriesDefinition series = SeriesDefinitionImpl.create( );
		series.getSeries( ).add( seCategory );
		cwoaPie.getSeriesDefinitions( ).add( series );

		PieSeries ps = (PieSeries) PieSeriesImpl.create( );
		ps.getLabel( ).getCaption( ).setColor( ColorDefinitionImpl.RED( ) );
		ps.getLabel( ).setBackground( ColorDefinitionImpl.CYAN( ) );
		ps.getLabel( ).setVisible( true );
		ps.setSeriesIdentifier( "Actuate" );
		ps.setDataSet( dsNumericValues1 );
		ps.setLeaderLineAttributes( LineAttributesImpl.create(
				ColorDefinitionImpl.create( 239, 33, 3 ),
				LineStyle.DASH_DOTTED_LITERAL,
				3 ) );
		ps.setLeaderLineStyle( LeaderLineStyle.FIXED_LENGTH_LITERAL );
		ps.setExplosion( 0 );
		ps.setSliceOutline( ColorDefinitionImpl.BLACK( ) );

		ps.setRatio( 1 );

		SeriesDefinition seGroup1 = SeriesDefinitionImpl.create( );
		series.getSeriesPalette( ).update( -2 );
		series.getSeriesDefinitions( ).add( seGroup1 );
		seGroup1.getSeries( ).add( ps );

		// Series-2
		PieSeries ps2 = (PieSeries) PieSeriesImpl.create( );
		ps2.getLabel( ).getCaption( ).setColor( ColorDefinitionImpl.RED( ) );
		ps2.getLabel( ).setBackground( ColorDefinitionImpl.CYAN( ) );
		ps2.getLabel( ).setVisible( true );
		ps2.setSeriesIdentifier( "Micorsoft" );
		ps2.setDataSet( dsNumericValues2 );
		ps2.setLeaderLineAttributes( LineAttributesImpl.create(
				ColorDefinitionImpl.create( 239, 33, 3 ),
				LineStyle.DASH_DOTTED_LITERAL,
				3 ) );
		ps2.setLeaderLineStyle( LeaderLineStyle.FIXED_LENGTH_LITERAL );
		ps2.setExplosion( 0 );
		ps2.setSliceOutline( ColorDefinitionImpl.BLACK( ) );

		ps2.setRatio( 5 );

		SeriesDefinition seGroup2 = SeriesDefinitionImpl.create( );
		series.getSeriesPalette( ).update( -2 );
		series.getSeriesDefinitions( ).add( seGroup2 );
		seGroup2.getSeries( ).add( ps2 );

		return cwoaPie;

	}
}
