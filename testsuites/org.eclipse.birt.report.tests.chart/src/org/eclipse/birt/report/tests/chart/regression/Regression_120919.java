/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.tests.chart.regression;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.GeneratedChartState;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.report.tests.chart.ChartTestCase;

/**
 * Regression description:
 * </p>
 * If you create a bar chart with date type for the X Axis and use the group by
 * day and set interval to 1, the result is not correct
 * </p>
 * Test description:
 * </p>
 * If you create a bar chart with date type for the X Axis and use the group by
 * day and set interval to 1, there will be only one group left
 * </p>
 */

public class Regression_120919 extends ChartTestCase
{

	private static String GOLDEN = "Regression_120919.jpg"; //$NON-NLS-1$
	private static String OUTPUT = "Regression_120919.jpg"; //$NON-NLS-1$

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
		Regression_120919 st = new Regression_120919( );
	}

	/**
	 * Constructor
	 */
	public Regression_120919( )
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
		cm = createChart( );
		bindGroupingData( cm );
		BufferedImage img = new BufferedImage( 500, 500,
				BufferedImage.TYPE_INT_ARGB );
		Graphics g = img.getGraphics( );

		Graphics2D g2d = (Graphics2D) g;
		dRenderer.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, g2d );
		dRenderer.setProperty( IDeviceRenderer.FILE_IDENTIFIER, this
				.getClassFolder( )
				+ OUTPUT_FOLDER + OUTPUT );

		Bounds bo = BoundsImpl.create( 0, 0, 500, 500 );
		bo.scale( 72d / dRenderer.getDisplayServer( ).getDpiResolution( ) );

		Generator gr = Generator.instance( );

		try
		{
			gcs = gr.build( dRenderer.getDisplayServer( ), cm, null, bo, null );
			gr.render( dRenderer, gcs );
		}
		catch ( ChartException e )
		{
			e.printStackTrace( );
		}
	}

	public void test_regression_120919( ) throws Exception
	{
		Regression_120919 st = new Regression_120919( );
		assertTrue( this.compareBytes( GOLDEN, OUTPUT ) );
	}

	// /**
	// * Creates a bar chart model as a reference implementation
	// *
	// * @return An instance of the simulated runtime chart model (containing
	// * filled datasets)
	// */
	// public static final Chart createBarChart( )
	// {
	// ChartWithAxes cwaBar = ChartWithAxesImpl.create( );
	//
	// // Chart Type
	// cwaBar.setType( "Bar Chart" );
	//
	// // Title
	// cwaBar.getTitle( ).getLabel( ).getCaption( ).setValue(
	// "Computer Hardware Sales" ); //$NON-NLS-1$
	// cwaBar.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );
	//
	// // Legend
	// Legend lg = cwaBar.getLegend( );
	// lg.setVisible( false );
	//
	// // X-Axis
	// Axis xAxisPrimary = ( (ChartWithAxesImpl) cwaBar ).getPrimaryBaseAxes(
	// )[0];
	// xAxisPrimary.getTitle( ).setVisible( false );
	//
	// xAxisPrimary.setType( AxisType.TEXT_LITERAL );
	// xAxisPrimary.getOrigin( ).setType( IntersectionType.VALUE_LITERAL );
	// xAxisPrimary.getLabel( ).getCaption( ).setColor(
	// ColorDefinitionImpl.GREEN( ).darker( ) );
	//
	// xAxisPrimary.getLabel( ).getCaption( ).getFont( ).setRotation( 45 );
	//
	// // Y-Axis
	// Axis yAxisPrimary = ( (ChartWithAxesImpl) cwaBar )
	// .getPrimaryOrthogonalAxis( xAxisPrimary );
	// yAxisPrimary.getLabel( ).getCaption( ).setValue( "Sales Growth" );
	// //$NON-NLS-1$
	// yAxisPrimary.getLabel( ).getCaption( ).setColor(
	// ColorDefinitionImpl.BLUE( ) );
	//
	// yAxisPrimary.getTitle( ).setVisible( false );
	// yAxisPrimary.setType( AxisType.LINEAR_LITERAL );
	// yAxisPrimary.getOrigin( ).setType( IntersectionType.VALUE_LITERAL );
	//
	// // Data Set
	// DateTimeDataSet dsDateValues = DateTimeDataSetImpl
	// .create( new Calendar[]{new CDateTime( 2005, 4, 1, 10, 12 ),
	// new CDateTime( 2005, 4, 1, 11, 15 ),
	// new CDateTime( 2005, 4, 1, 7, 15 )} );
	// NumberDataSet dsNumericValues1 = NumberDataSetImpl
	// .create( new double[]{143.26, 156.55, 95.25} );
	//
	// // X-Series
	// Series seBase = SeriesImpl.create( );
	// seBase.setDataSet( dsDateValues );
	//
	// SeriesDefinition sdX = SeriesDefinitionImpl.create( );
	// sdX.getQuery( ).setDefinition( "" ); //$NON-NLS-1$
	// sdX.setSorting( SortOption.DESCENDING_LITERAL );
	// xAxisPrimary.getSeriesDefinitions( ).add( sdX );
	// sdX.getSeries( ).add( seBase );
	//
	// sdX.getGrouping( ).setEnabled( true );
	// sdX.getGrouping( ).setGroupType( DataType.get( 1 ) );
	// sdX.getGrouping( ).setGroupingUnit( GroupingUnitType.get( 3 ) );
	// sdX.getGrouping( ).setGroupingInterval( 1 );
	// sdX.getGrouping( ).setAggregateExpression( "Sum" );
	//
	// // Y-Series
	// BarSeries bs = (BarSeries) BarSeriesImpl.create( );
	// bs.getLabel( ).getCaption( ).setColor( ColorDefinitionImpl.RED( ) );
	// bs.getLabel( ).setBackground( ColorDefinitionImpl.CYAN( ) );
	// bs.getLabel( ).setVisible( true );
	// bs.setDataSet( dsNumericValues1 );
	// bs.setStacked( true );
	//
	// SeriesDefinition sdY = SeriesDefinitionImpl.create( );
	// yAxisPrimary.getSeriesDefinitions( ).add( sdY );
	// sdY.getSeriesPalette( ).update( ColorDefinitionImpl.GREEN( ) );
	// sdY.getSeries( ).add( bs );
	//
	// return cwaBar;
	//
	// }

	private void bindGroupingData( Chart chart )

	{

		// Data Set

		final Object[][] data = new Object[][]{
				{new CDateTime( 2005, 4, 1, 10, 12 ), new Double( 143.26 )},
				{new CDateTime( 2005, 4, 1, 11, 15 ), new Double( 156.55 )},
				{new CDateTime( 2005, 4, 1, 7, 15 ), new Double( 95.25 )},};
		try
		{
			Generator gr = Generator.instance( );
			gr.bindData( new IDataRowExpressionEvaluator( ) {

				int idx = 0;

				public void close( )
				{
				}

				public Object evaluate( String expression )
				{
					if ( "X".equals( expression ) )
					{
						return data[idx][0];
					}
					else if ( "Y".equals( expression ) )
					{
						return data[idx][1];
					}
					else if ( "G".equals( expression ) )
					{
						return data[idx][0];
					}
					return null;
				}

				public Object evaluateGlobal( String expression )
				{
					return evaluate( expression );
				}

				public boolean first( )
				{
					idx = 0;
					return true;
				}

				public boolean next( )
				{
					idx++;
					return ( idx < 2 );
				}
			}, chart, new RunTimeContext( ) );

		}

		catch ( ChartException e )
		{
			e.printStackTrace( );
		}
	}

	private Chart createChart( )

	{

		ChartWithAxes cwaBar = ChartWithAxesImpl.create( );

		// X-Axis

		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes( )[0];

		xAxisPrimary.setType( AxisType.TEXT_LITERAL );

		// Y-Axis

		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis( xAxisPrimary );

		yAxisPrimary.setType( AxisType.LINEAR_LITERAL );

		// X-Series

		Series seCategory = SeriesImpl.create( );

		Query xQ = QueryImpl.create( "G" );

		seCategory.getDataDefinition( ).add( xQ );

		SeriesDefinition sdX = SeriesDefinitionImpl.create( );

		xAxisPrimary.getSeriesDefinitions( ).add( sdX );

		sdX.getSeries( ).add( seCategory );

		// -------------------------------------------------------------

		// sdX.setSorting( SortOption.ASCENDING_LITERAL );

		sdX.getGrouping( ).setEnabled( true );

		sdX.getGrouping( ).setGroupType( DataType.DATE_TIME_LITERAL );

		sdX.getGrouping( ).setAggregateExpression( "Sum" );
		sdX.getGrouping( ).setGroupingUnit( GroupingUnitType.get( 3 ) );

		sdX.getGrouping( ).setGroupingInterval( 1 );

		// -------------------------------------------------------------

		// Y-Series

		BarSeries bs = (BarSeries) BarSeriesImpl.create( );

		bs.getLabel( ).setVisible( false );

		Query yQ = QueryImpl.create( "Y" );

		bs.getDataDefinition( ).add( yQ );

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );

		yAxisPrimary.getSeriesDefinitions( ).add( sdY );

		sdY.getSeriesPalette( ).update( 0 );

		sdY.getSeries( ).add( bs );

		return cwaBar;

	}
}
