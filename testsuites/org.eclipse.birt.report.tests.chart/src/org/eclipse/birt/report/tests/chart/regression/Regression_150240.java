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
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.MarkerRangeImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
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
 * All Charts: Need API to Change Colored Block in Legend
 * </p>
 * Test description:
 * </p>
 * Create a bar chart, set script function beforeDrawLegendEntry()
 * </p>
 */

public class Regression_150240 extends ChartTestCase
{

	private static String GOLDEN = "Regression_150240.jpg"; //$NON-NLS-1$
	private static String OUTPUT = "Regression_150240.jpg"; //$NON-NLS-1$	

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
		new Regression_150240( );
	}

	/**
	 * Constructor
	 */
	public Regression_150240( )
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
				600,
				600,
				BufferedImage.TYPE_INT_ARGB );
		Graphics g = img.getGraphics( );

		Graphics2D g2d = (Graphics2D) g;
		dRenderer.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, g2d );
		dRenderer.setProperty( IDeviceRenderer.FILE_IDENTIFIER, this
				.genOutputFile( OUTPUT ) );

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

	public void test_regression_150240( ) throws Exception
	{
		Regression_150240 st = new Regression_150240( );
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

		cwaBar
				.setScript( "function beforeDrawLegendEntry(label, scriptContext)" //$NON-NLS-1$
						+ "{label.getCaption( ).getColor().set( 35, 184, 245 ); " //$NON-NLS-1$
						+ "label.getCaption().getFont().setBold(true); " //$NON-NLS-1$
						+ "label.getCaption().getFont().setItalic(true); " //$NON-NLS-1$
						+ "label.getOutline().setVisible(true); " //$NON-NLS-1$
						+ "label.getOutline().getColor().set( 177, 12, 187);}" //$NON-NLS-1$

				);
		// Chart Type
		cwaBar.setType( "Bar Chart" );
		cwaBar.setSubType( "Stacked" );
		cwaBar.setDimension( ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL );
		cwaBar.setFloorFill( ColorDefinitionImpl.ORANGE( ) );
		cwaBar.setWallFill( ColorDefinitionImpl.GREY( ) );

		// Title
		cwaBar.getTitle( ).getLabel( ).getCaption( ).setValue(
				"Computer Hardware Sales" ); //$NON-NLS-1$
		cwaBar.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );

		// Plot
		cwaBar.getPlot( ).getClientArea( ).getOutline( ).setVisible( false );
		cwaBar.getPlot( ).getClientArea( ).setBackground(
				ColorDefinitionImpl.create( 255, 255, 225 ) );

		// Legend
		Legend lg = cwaBar.getLegend( );
		lg.setItemType( LegendItemType.CATEGORIES_LITERAL );

		// X-Axis
		Axis xAxisPrimary = ( (ChartWithAxesImpl) cwaBar ).getPrimaryBaseAxes( )[0];
		xAxisPrimary.getTitle( ).setVisible( false );

		xAxisPrimary.setType( AxisType.TEXT_LITERAL );
		xAxisPrimary.getOrigin( ).setType( IntersectionType.VALUE_LITERAL );
		xAxisPrimary.getLabel( ).getCaption( ).setColor(
				ColorDefinitionImpl.GREEN( ).darker( ) );
		xAxisPrimary.getLabel( ).getCaption( ).getFont( ).setRotation( 75 );

		xAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.BELOW_LITERAL );
		xAxisPrimary.getMajorGrid( ).getLineAttributes( ).setStyle(
				LineStyle.DOTTED_LITERAL );
		xAxisPrimary.getMajorGrid( ).getLineAttributes( ).setColor(
				ColorDefinitionImpl.GREY( ) );
		xAxisPrimary.getMajorGrid( ).getLineAttributes( ).setVisible( true );

		MarkerRange mr = MarkerRangeImpl.create(
				xAxisPrimary,
				NumberDataElementImpl.create( 2.0 ),
				NumberDataElementImpl.create( 3.0 ),
				null );
		mr.setOutline( LineAttributesImpl.create( ColorDefinitionImpl.create(
				239,
				33,
				3 ), LineStyle.DOTTED_LITERAL, 2 ) );

		// Y-Axis
		Axis yAxisPrimary = ( (ChartWithAxesImpl) cwaBar )
				.getPrimaryOrthogonalAxis( xAxisPrimary );
		yAxisPrimary.getLabel( ).getCaption( ).setValue( "Sales Growth" ); //$NON-NLS-1$
		yAxisPrimary.getLabel( ).getCaption( ).setColor(
				ColorDefinitionImpl.BLUE( ) );

		yAxisPrimary.getTitle( ).setVisible( false );
		yAxisPrimary.setType( AxisType.LINEAR_LITERAL );
		yAxisPrimary.getOrigin( ).setType( IntersectionType.VALUE_LITERAL );

		yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );
		yAxisPrimary.getMajorGrid( ).getLineAttributes( ).setStyle(
				LineStyle.DOTTED_LITERAL );
		yAxisPrimary.getMajorGrid( ).getLineAttributes( ).setColor(
				ColorDefinitionImpl.GREY( ) );
		yAxisPrimary.getMajorGrid( ).getLineAttributes( ).setVisible( true );

		// Data Set
		TextDataSet dsStringValue = TextDataSetImpl.create( new String[]{
				"Keyboards", "Moritors", "Printers", "Mortherboards"} );
		NumberDataSet dsNumericValues1 = NumberDataSetImpl
				.create( new double[]{143.26, 156.55, 95.25, 47.56} );

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
		bs.setStacked( true );

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		yAxisPrimary.getSeriesDefinitions( ).add( sdY );
		sdY.getSeriesPalette( ).update( ColorDefinitionImpl.GREEN( ) );
		sdY.getSeries( ).add( bs );

		return cwaBar;

	}

}
