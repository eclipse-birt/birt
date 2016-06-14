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
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineDecorator;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.NumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.component.DialRegion;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.DialRegionImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.DialChartImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.impl.DialSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.report.tests.chart.ChartTestCase;

/**
 * Regression description:
 * </p>
 * Create a meter chart with two series with data labels (by default), the dial
 * becomes very small to leave space for the data labels
 * </p>
 * Test description:
 * <p>
 * Create a meter chart with two series with data labels (by default), view the
 * generated image
 * </p>
 */

public class Regression_119411 extends ChartTestCase
{

	private static String GOLDEN = "Reg_119411.jpg"; //$NON-NLS-1$
	private static String OUTPUT = "Reg_119411.jpg"; //$NON-NLS-1$	

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
		new Regression_119411( );
	}

	/**
	 * Constructor
	 */
	public Regression_119411( )
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
		cm = createMeterChart( );
		BufferedImage img = new BufferedImage(
				600,
				600,
				BufferedImage.TYPE_INT_ARGB );
		Graphics g = img.getGraphics( );

		Graphics2D g2d = (Graphics2D) g;
		dRenderer.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, g2d );
		dRenderer.setProperty( IDeviceRenderer.FILE_IDENTIFIER, this
				.genOutputFile( OUTPUT )
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

	public void test_regression_119411( ) throws Exception
	{
		Regression_119411 st = new Regression_119411( );
		assertTrue( st.compareImages( GOLDEN, OUTPUT ) );
	}

	/**
	 * Creates a meter chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createMeterChart( )
	{
		DialChart dChart = (DialChart) DialChartImpl.create( );
		dChart.setDialSuperimposition( false );
		dChart.setGridColumnCount( 2 );
		dChart.setSeriesThickness( 25 );

		// Title/Plot
		dChart.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );
		Plot p = dChart.getPlot( );
		p.getClientArea( ).setBackground( ColorDefinitionImpl.CREAM( ) );
		p.getClientArea( ).getOutline( ).setVisible( false );
		p.getOutline( ).setVisible( false );

		dChart.getTitle( ).getLabel( ).getCaption( ).setValue( "Meter Chart" );
		dChart.getTitle( ).getOutline( ).setVisible( false );

		// Legend
		Legend lg = dChart.getLegend( );
		LineAttributes lia = lg.getOutline( );
		lg.getText( ).getFont( ).setSize( 16 );
		lia.setStyle( LineStyle.SOLID_LITERAL );
		lg.getInsets( ).setLeft( 10 );
		lg.getInsets( ).setRight( 10 );
		lg.setBackground( null );
		lg.getOutline( ).setVisible( false );
		lg.setShowValue( true );
		lg.getClientArea( ).setBackground( ColorDefinitionImpl.PINK( ) );

		lg.getClientArea( ).getOutline( ).setVisible( true );
		lg.getTitle( ).getCaption( ).getFont( ).setSize( 20 );
		lg.getTitle( ).setInsets( InsetsImpl.create( 10, 10, 10, 10 ) );
		lg.setTitlePosition( Position.ABOVE_LITERAL );
		lg.setPosition( Position.BELOW_LITERAL );
		lg.setItemType( LegendItemType.SERIES_LITERAL );

		TextDataSet categoryValues = TextDataSetImpl
				.create( new String[]{"Moto"} );

		SeriesDefinition sd = SeriesDefinitionImpl.create( );
		dChart.getSeriesDefinitions( ).add( sd );
		Series seCategory = (Series) SeriesImpl.create( );

		seCategory.setDataSet( categoryValues );
		sd.getSeries( ).add( seCategory );

		SeriesDefinition sdCity = SeriesDefinitionImpl.create( );

		final Fill[] fiaOrth = {ColorDefinitionImpl.PINK( ),
				ColorDefinitionImpl.ORANGE( ), ColorDefinitionImpl.WHITE( )};
		sdCity.getSeriesPalette( ).getEntries( ).clear( );
		for ( int i = 0; i < fiaOrth.length; i++ )
		{
			sdCity.getSeriesPalette( ).getEntries( ).add( fiaOrth[i] );
		}

		// Dial 1
		DialSeries seDial1 = (DialSeries) DialSeriesImpl.create( );
		seDial1.setDataSet( NumberDataSetImpl.create( new double[]{20} ) );
		NumberFormatSpecifier nb = NumberFormatSpecifierImpl.create( );
		nb.setPrefix( "abcdefga" );
		seDial1.getDial( ).setFormatSpecifier( nb );
		seDial1.getDial( ).setFill(
				GradientImpl.create(
						ColorDefinitionImpl.create( 225, 255, 225 ),
						ColorDefinitionImpl.create( 225, 225, 255 ),
						45,
						false ) );
		seDial1.setSeriesIdentifier( "Temperature" );
		seDial1.getNeedle( ).setDecorator( LineDecorator.CIRCLE_LITERAL );
		seDial1.getDial( ).setStartAngle( -45 );
		seDial1.getDial( ).setStopAngle( 225 );
		seDial1.getDial( ).getMinorGrid( ).getTickAttributes( ).setVisible(
				true );
		seDial1.getDial( ).getMinorGrid( ).getTickAttributes( ).setColor(
				ColorDefinitionImpl.BLACK( ) );
		seDial1.getDial( ).getMinorGrid( ).setTickStyle(
				TickStyle.BELOW_LITERAL );
		seDial1
				.getDial( )
				.getScale( )
				.setMin( NumberDataElementImpl.create( 0 ) );
		seDial1.getDial( ).getScale( ).setMax(
				NumberDataElementImpl.create( 90 ) );
		seDial1.getDial( ).getScale( ).setStep( 10 );
		seDial1.getLabel( ).setOutline(
				LineAttributesImpl.create(
						ColorDefinitionImpl.GREY( ).darker( ),
						LineStyle.SOLID_LITERAL,
						1 ) );
		seDial1.getLabel( ).setBackground(
				ColorDefinitionImpl.GREY( ).brighter( ) );

		DialRegion dregion1 = DialRegionImpl.create( );
		dregion1.setFill( ColorDefinitionImpl.GREEN( ) );
		dregion1.setOutline( LineAttributesImpl.create( ColorDefinitionImpl
				.BLACK( )
				.darker( ), LineStyle.SOLID_LITERAL, 1 ) );
		dregion1.setStartValue( NumberDataElementImpl.create( 70 ) );
		dregion1.setEndValue( NumberDataElementImpl.create( 90 ) );
		dregion1.setInnerRadius( 40 );
		dregion1.setOuterRadius( -1 );
		seDial1.getDial( ).getDialRegions( ).add( dregion1 );

		DialRegion dregion2 = DialRegionImpl.create( );
		dregion2.setFill( ColorDefinitionImpl.YELLOW( ) );
		dregion2.setOutline( LineAttributesImpl.create( ColorDefinitionImpl
				.BLACK( )
				.darker( ), LineStyle.SOLID_LITERAL, 1 ) );
		dregion2.setStartValue( NumberDataElementImpl.create( 40 ) );
		dregion2.setEndValue( NumberDataElementImpl.create( 70 ) );
		dregion2.setOuterRadius( 70 );
		seDial1.getDial( ).getDialRegions( ).add( dregion2 );

		DialRegion dregion3 = DialRegionImpl.create( );
		dregion3.setFill( ColorDefinitionImpl.RED( ) );
		dregion3.setOutline( LineAttributesImpl.create( ColorDefinitionImpl
				.BLACK( )
				.darker( ), LineStyle.SOLID_LITERAL, 1 ) );
		dregion3.setStartValue( NumberDataElementImpl.create( 0 ) );
		dregion3.setEndValue( NumberDataElementImpl.create( 40 ) );
		dregion3.setInnerRadius( 40 );
		dregion3.setOuterRadius( 90 );
		seDial1.getDial( ).getDialRegions( ).add( dregion3 );

		// Dial 2
		DialSeries seDial2 = (DialSeries) DialSeriesImpl.create( );
		seDial2.setDataSet( NumberDataSetImpl.create( new double[]{58} ) );
		seDial2.getDial( ).setFill(
				GradientImpl.create(
						ColorDefinitionImpl.create( 225, 255, 225 ),
						ColorDefinitionImpl.create( 225, 225, 255 ),
						45,
						false ) );
		seDial2.setSeriesIdentifier( "Wind Speed" );
		seDial2.getNeedle( ).setDecorator( LineDecorator.CIRCLE_LITERAL );
		seDial2.getDial( ).setStartAngle( -45 );
		seDial2.getDial( ).setStopAngle( 225 );
		seDial2.getDial( ).getMinorGrid( ).getTickAttributes( ).setVisible(
				true );
		seDial2.getDial( ).getMinorGrid( ).getTickAttributes( ).setColor(
				ColorDefinitionImpl.BLACK( ) );
		seDial2.getDial( ).getMinorGrid( ).setTickStyle(
				TickStyle.BELOW_LITERAL );
		seDial2
				.getDial( )
				.getScale( )
				.setMin( NumberDataElementImpl.create( 0 ) );
		seDial2.getDial( ).getScale( ).setMax(
				NumberDataElementImpl.create( 90 ) );
		seDial2.getDial( ).getScale( ).setStep( 10 );
		seDial2.getLabel( ).setOutline(
				LineAttributesImpl.create(
						ColorDefinitionImpl.GREY( ).darker( ),
						LineStyle.SOLID_LITERAL,
						1 ) );
		seDial2.getLabel( ).setBackground(
				ColorDefinitionImpl.GREY( ).brighter( ) );

		seDial2.getDial( ).getDialRegions( ).add( dregion1 );
		seDial2.getDial( ).getDialRegions( ).add( dregion2 );
		seDial2.getDial( ).getDialRegions( ).add( dregion3 );

		// dChart.setDialSuperimposition(true);
		sd.getSeriesDefinitions( ).add( sdCity );
		sdCity.getSeries( ).add( seDial1 );
		sdCity.getSeries( ).add( seDial2 );

		return dChart;

	}
}
