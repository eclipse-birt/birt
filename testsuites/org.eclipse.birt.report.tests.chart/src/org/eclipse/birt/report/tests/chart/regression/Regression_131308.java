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
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.impl.AreaSeriesImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.report.tests.chart.ChartTestCase;

/**
 * Regression description:
 * </p>
 * On a 2 series area chart. Colors seems to be invert when one of value is
 * equal to 0.
 * </p>
 * Test description:
 * <p>
 * Create two series area chart, the datas contain 0 values, verify if it can be
 * displayed correctly.
 * </p>
 */

public class Regression_131308 extends ChartTestCase
{

	private static String GOLDEN = "Regression_131308.jpg"; //$NON-NLS-1$
	private static String OUTPUT = "Regression_131308.jpg"; //$NON-NLS-1$

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
		new Regression_131308( );
	}

	/**
	 * Constructor
	 */
	public Regression_131308( )
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
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}
	}

	public void test_regression_131308( ) throws Exception
	{
		Regression_131308 st = new Regression_131308( );
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
				"Computer Hardware Sales" ); //$NON-NLS-1$
		cwaArea.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );

		// X-Axis
		Axis xAxisPrimary = ( (ChartWithAxesImpl) cwaArea )
				.getPrimaryBaseAxes( )[0];
		xAxisPrimary.getTitle( ).setVisible( false );

		xAxisPrimary.setType( AxisType.TEXT_LITERAL );
		xAxisPrimary.getOrigin( ).setType( IntersectionType.VALUE_LITERAL );
		xAxisPrimary.getLabel( ).getCaption( ).setColor(
				ColorDefinitionImpl.GREEN( ).darker( ) );
		xAxisPrimary.getLabel( ).getCaption( ).getFont( ).setRotation( 75 );

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
				"Keyboards", "Moritors", "Printers", "Mortherboards",
				"Telephones", "Mouse", "NetCards"} );
		NumberDataSet dsNumericValues1 = NumberDataSetImpl
				.create( new double[]{143.26, 156.55, 95.25, 47.56, 0, 88.9,
						93.25} );
		NumberDataSet dsNumericValues2 = NumberDataSetImpl
				.create( new double[]{143.26, 0, 95.25, 47.56, 35.8, 0, 123.45} );

		// X-Series
		Series seBase = SeriesImpl.create( );
		seBase.setDataSet( dsStringValue );

		SeriesDefinition sdX = SeriesDefinitionImpl.create( );
		xAxisPrimary.getSeriesDefinitions( ).add( sdX );
		sdX.getSeries( ).add( seBase );

		// Y-Series-1
		AreaSeries as1 = (AreaSeries) AreaSeriesImpl.create( );
		as1.setSeriesIdentifier( "Actuate" ); //$NON-NLS-1$
		as1.getLabel( ).getCaption( ).setColor( ColorDefinitionImpl.GREEN( ) );
		as1.getLabel( ).setBackground( ColorDefinitionImpl.CYAN( ) );
		as1.getLabel( ).setVisible( true );
		as1.setLineAttributes( LineAttributesImpl.create( ColorDefinitionImpl
				.create( 207, 41, 207 ), LineStyle.SOLID_LITERAL, 1 ) );
		as1.setDataSet( dsNumericValues2 );
		as1.setStacked( true );

		SeriesDefinition sdY1 = SeriesDefinitionImpl.create( );
		yAxisPrimary.getSeriesDefinitions( ).add( sdY1 );
		sdY1.getSeriesPalette( ).update( ColorDefinitionImpl.GREEN( ) );
		sdY1.getSeries( ).add( as1 );

		// Y-Series-2
		AreaSeries as2 = (AreaSeries) AreaSeriesImpl.create( );
		as2.setSeriesIdentifier( "Microsoft" ); //$NON-NLS-1$
		as2.getLabel( ).getCaption( ).setColor( ColorDefinitionImpl.RED( ) );
		as2.getLabel( ).setBackground( ColorDefinitionImpl.CYAN( ) );
		as2.getLabel( ).setVisible( true );
		as2.setLineAttributes( LineAttributesImpl.create( ColorDefinitionImpl
				.create( 207, 41, 207 ), LineStyle.SOLID_LITERAL, 1 ) );
		as2.setDataSet( dsNumericValues1 );
		as2.setStacked( true );

		SeriesDefinition sdY2 = SeriesDefinitionImpl.create( );
		yAxisPrimary.getSeriesDefinitions( ).add( sdY2 );
		sdY2.getSeriesPalette( ).update( ColorDefinitionImpl.RED( ) );
		sdY2.getSeries( ).add( as2 );

		return cwaArea;
	}
}
