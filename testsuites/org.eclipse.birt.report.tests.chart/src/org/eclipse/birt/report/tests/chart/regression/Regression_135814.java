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
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
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
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.report.tests.chart.ChartTestCase;

/**
 * Regression description:
 * </p>
 * Set legend separator line properties, can not take effect
 * </p>
 * Test description:
 * </p>
 * Create a bar chart with to series, set its legend separator style to
 * dash-dotted and color to green
 * </p>
 */

public class Regression_135814 extends ChartTestCase
{

	private static String GOLDEN = "Regression_135814.jpg"; //$NON-NLS-1$
	private static String OUTPUT = "Regression_135814.jpg"; //$NON-NLS-1$

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

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
		new Regression_135814( );
	}

	/**
	 * Constructor
	 */
	public Regression_135814( )
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
		cm = createBarChart( );
		BufferedImage img = new BufferedImage(
				500,
				500,
				BufferedImage.TYPE_INT_ARGB );
		Graphics g = img.getGraphics( );

		Graphics2D g2d = (Graphics2D) g;
		dRenderer.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, g2d );
		dRenderer.setProperty( IDeviceRenderer.FILE_IDENTIFIER, this
				.genOutputFile( OUTPUT ) ); //$NON-NLS-1$

		Bounds bo = BoundsImpl.create( 0, 0, 500, 500 );
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

	public void test_regression_135814( ) throws Exception
	{
		Regression_135814 st = new Regression_135814( );
		assertTrue( st.compareImages( GOLDEN, OUTPUT ) );
	}

	/**
	 * Creates a bar chart model as a reference implementation
	 * 
	 * @return An instance of the simulated runtime chart model (containing
	 *         filled datasets)
	 */
	public static final Chart createBarChart( )
	{
		ChartWithAxes cwaBar = ChartWithAxesImpl.create( );

		// Chart Type
		cwaBar.setType( "Bar Chart" );
		cwaBar.setSubType( "Side-by-side" );

		// Title
		cwaBar.getTitle( ).getLabel( ).getCaption( ).setValue(
				"Computer Hardware Sales" ); //$NON-NLS-1$
		cwaBar.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );
		cwaBar.getTitle( ).setVisible( true );

		// Legend
		Legend lg = cwaBar.getLegend( );
		lg.setVisible( true );
		lg.getSeparator( ).setVisible( true );
		lg.getSeparator( ).setStyle( LineStyle.DASH_DOTTED_LITERAL );
		lg.getSeparator( ).setColor( ColorDefinitionImpl.GREEN( ) );

		// X-Axis
		Axis xAxisPrimary = ( (ChartWithAxesImpl) cwaBar ).getPrimaryBaseAxes( )[0];
		xAxisPrimary.getTitle( ).setVisible( false );

		xAxisPrimary.setType( AxisType.TEXT_LITERAL );
		xAxisPrimary.getOrigin( ).setType( IntersectionType.VALUE_LITERAL );
		xAxisPrimary.getLabel( ).getCaption( ).setColor(
				ColorDefinitionImpl.GREEN( ).darker( ) );

		// Y-Axis
		Axis yAxisPrimary = ( (ChartWithAxesImpl) cwaBar )
				.getPrimaryOrthogonalAxis( xAxisPrimary );
		yAxisPrimary.getLabel( ).getCaption( ).setValue( "Sales Growth" ); //$NON-NLS-1$
		yAxisPrimary.getLabel( ).getCaption( ).setColor(
				ColorDefinitionImpl.BLUE( ) );
		xAxisPrimary.getLabel( ).getCaption( ).getFont( ).setRotation( 75 );

		yAxisPrimary.getTitle( ).setVisible( false );
		yAxisPrimary.setType( AxisType.LINEAR_LITERAL );
		yAxisPrimary.getOrigin( ).setType( IntersectionType.VALUE_LITERAL );

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl.create( new String[]{
				"Keyboards", "Moritors", "Printers", "Mortherboards"} );
		NumberDataSet dsNumericValues1 = NumberDataSetImpl
				.create( new double[]{143.26, 156.55, 95.25, 47.56} );
		NumberDataSet dsNumericValues2 = NumberDataSetImpl
				.create( new double[]{15.29, -14.53, -47.05, 32.55} );

		// X-Series
		Series seBase = SeriesImpl.create( );
		seBase.setDataSet( dsStringValue );

		SeriesDefinition sdX = SeriesDefinitionImpl.create( );
		xAxisPrimary.getSeriesDefinitions( ).add( sdX );
		sdX.getSeries( ).add( seBase );

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create( );
		bs.setSeriesIdentifier( "Actuate" ); //$NON-NLS-1$
		bs.getLabel( ).getCaption( ).setColor( ColorDefinitionImpl.RED( ) );
		bs.getLabel( ).setBackground( ColorDefinitionImpl.CYAN( ) );
		bs.getLabel( ).setVisible( true );
		bs.setDataSet( dsNumericValues1 );

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		yAxisPrimary.getSeriesDefinitions( ).add( sdY );
		sdY.getSeriesPalette( ).update( ColorDefinitionImpl.GREEN( ) );
		sdY.getSeries( ).add( bs );

		BarSeries bs2 = (BarSeries) BarSeriesImpl.create( );
		bs2.setSeriesIdentifier( "Micorsoft" ); //$NON-NLS-1$
		bs2.getLabel( ).getCaption( ).setColor( ColorDefinitionImpl.RED( ) );
		bs2.getLabel( ).setBackground( ColorDefinitionImpl.CYAN( ) );
		bs2.getLabel( ).setVisible( true );
		bs2.setDataSet( dsNumericValues2 );

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create( );
		yAxisPrimary.getSeriesDefinitions( ).add( sdY2 );
		sdY2.getSeriesPalette( ).update( ColorDefinitionImpl.YELLOW( ) );
		sdY2.getSeries( ).add( bs2 );

		return cwaBar;
	}
}
