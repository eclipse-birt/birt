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
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.integrate.SimpleDataRowExpressionEvaluator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.attribute.TickStyle;
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
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.report.tests.chart.ChartTestCase;

import com.ibm.icu.util.ULocale;

/**
 * The selector of charts in SWT.
 * 
 */
public final class Regression_285009 extends ChartTestCase
{
	private static String GOLDEN = "Regression_285009.jpg"; //$NON-NLS-1$
	private static String OUTPUT = "Regression_285009.jpg"; //$NON-NLS-1$

	private IDeviceRenderer idr = null;

	private Chart cm = null;
	
	private IDataRowExpressionEvaluator dree = null;

	/**
	 * main() method for constructing the layout.
	 * 
	 * @param args
	 */
	public static void main( String[] args )
	{
		new Regression_285009(  );
	}

	/**
	 * Get the connection with SWT device to render the graphics.
	 */
public	Regression_285009( )
	{
		final PluginSettings ps = PluginSettings.instance( );
		try
		{
			idr = ps.getDevice( "dv.JPG" );//$NON-NLS-1$

		}
		catch ( ChartException ex )
		{
			ex.printStackTrace( );
		}
		cm = createSimpleChart( );
		
		BufferedImage img = new BufferedImage(
				600,
				600,
				BufferedImage.TYPE_INT_ARGB );
		Graphics g = img.getGraphics( );

		Graphics2D g2d = (Graphics2D) g;
		idr.setProperty( IDeviceRenderer.GRAPHICS_CONTEXT, g2d );
		idr.setProperty( IDeviceRenderer.FILE_IDENTIFIER, this.genOutputFile( OUTPUT )
				  );

		Bounds bo = BoundsImpl.create( 0, 0, 600, 600 );
		bo.scale( 72d / idr.getDisplayServer( ).getDpiResolution( ) );
		RunTimeContext context = new RunTimeContext( );
		context.setULocale( ULocale.getDefault( ) );
		
		String[] set = {
				"Items", "Amounts", "New Amounts"};//$NON-NLS-1$ //$NON-NLS-2$
		Object[][] data = {
				{
						"A", "B", "C"//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}, {
						Integer.valueOf( 7 ), Integer.valueOf( 2 ), Integer.valueOf( 5 )
				}, {
						Integer.valueOf( 3 ), Integer.valueOf( 5 ), Integer.valueOf( 2 )
				}
		};
		dree = new SimpleDataRowExpressionEvaluator( set, data );
		Generator gr = Generator.instance( );
		try
		{
			gr.bindData( dree, cm, context );
			gr.render( idr, gr.build( idr.getDisplayServer( ),
					cm,
					bo,
					null,
					context,
					null ) );
		}
		catch ( ChartException ce )
		{
			ce.printStackTrace( );
		}
	}

	private static final Chart createSimpleChart( )
	{
		ChartWithAxes cwaBar = ChartWithAxesImpl.create( );
		
		cwaBar.getLegend().setItemType(LegendItemType.SERIES_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes( )[0];
		xAxisPrimary.setType( AxisType.TEXT_LITERAL );
		xAxisPrimary.getOrigin( ).setType( IntersectionType.VALUE_LITERAL );
		xAxisPrimary.getTitle( ).setVisible( true );

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis( xAxisPrimary );
		yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );
		yAxisPrimary.setType( AxisType.LINEAR_LITERAL );
		yAxisPrimary.getTitle( ).setVisible( true );

		// X-Series
		Series seCategory = SeriesImpl.create( );
		Query query = QueryImpl.create( "Items" );//$NON-NLS-1$
		seCategory.getDataDefinition( ).add( query );

		SeriesDefinition sdX = SeriesDefinitionImpl.create( );
		xAxisPrimary.getSeriesDefinitions( ).add( sdX );
		sdX.getSeries( ).add( seCategory );
		sdX.setSorting(SortOption.ASCENDING_LITERAL);
		sdX.setSortKey(QueryImpl.create("Amounts"));

		// Y-Series
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create( );
		bs1.getDataDefinition( ).add( QueryImpl.create( "Amounts" ) );
		bs1.setRiserOutline( null );
		bs1.getLabel( ).setVisible( true );

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		sdY.getSeriesPalette( ).shift( -1 );
		sdY.setQuery(QueryImpl.create("Items"));
		yAxisPrimary.getSeriesDefinitions( ).add( sdY );
		sdY.getSeries( ).add( bs1 );
		sdY.setSorting(SortOption.ASCENDING_LITERAL);
		sdY.setSortKey(QueryImpl.create("New Amounts"));  // Note: "Amounts" works just fine
		
		return cwaBar;
	}

	public void test_regression_285009( ) throws Exception
	{
		Regression_285009 st = new Regression_285009( );
		assertTrue( st.compareImages( GOLDEN, OUTPUT ) );
	}
}

