/***********************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.view.models;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.NumberDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;

public class ColoredByCategorySeries
{

	public static final Chart createColoredByCategorySeries( )
	{
		ChartWithAxes cwaBar = ChartWithAxesImpl.create( );

		// Plot
		cwaBar.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );
		cwaBar.getBlock( ).getOutline( ).setVisible( true );
		Plot p = cwaBar.getPlot( );
		p.getClientArea( ).setBackground( ColorDefinitionImpl.create( 255,
				255,
				225 ) );
		p.getOutline( ).setVisible( false );
		cwaBar.getTitle( )
				.getLabel( )
				.getCaption( )
				.setValue( "Bar Chart Colored by Category" );//$NON-NLS-1$

		// Legend
		Legend lg = cwaBar.getLegend( );
		lg.getText( ).getFont( ).setBold( true );
		lg.setItemType( LegendItemType.CATEGORIES_LITERAL );

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes( )[0];
		xAxisPrimary.setType( AxisType.TEXT_LITERAL );
		xAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.BELOW_LITERAL );
		xAxisPrimary.getOrigin( ).setType( IntersectionType.MIN_LITERAL );

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis( xAxisPrimary );
		yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );
		yAxisPrimary.setType( AxisType.LINEAR_LITERAL );
		yAxisPrimary.getLabel( ).getCaption( ).getFont( ).setRotation( 90 );

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create( new String[]{
				"Item 1", "Item 2", "Item 3"} );//$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create( new double[]{
				14.3, 20.9, 7.6
		} );

		// X-Series
		Series seCategory = SeriesImpl.create( );
		seCategory.setDataSet( categoryValues );
		SeriesDefinition sdX = SeriesDefinitionImpl.create( );
		sdX.getSeriesPalette( ).update( 0 );
		xAxisPrimary.getSeriesDefinitions( ).add( sdX );
		sdX.getSeries( ).add( seCategory );

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create( );
		bs.setDataSet( orthoValues );
		bs.setSeriesIdentifier( "Series" ); //$NON-NLS-1$
		bs.getLabel( ).setVisible( true );
		bs.setLabelPosition( Position.INSIDE_LITERAL );
		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		sdY.getSeriesPalette( ).update( -1 );
		yAxisPrimary.getSeriesDefinitions( ).add( sdY );
		sdY.getSeries( ).add( bs );

		return cwaBar;
	}
}
