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
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
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
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.util.CDateTime;

import com.ibm.icu.util.Calendar;

public class AxisTitleFormat
{

	public static final Chart createAxisTitleFormat( )
	{
		ChartWithAxes cwaBar = ChartWithAxesImpl.create( );

		// Plot
		cwaBar.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );
		Plot p = cwaBar.getPlot( );
		p.getClientArea( )
				.setBackground( GradientImpl.create( ColorDefinitionImpl.create( 225,
						225,
						255 ),
						ColorDefinitionImpl.create( 255, 255, 225 ),
						-35,
						false ) );
		p.getOutline( ).setVisible( true );

		// Title
		cwaBar.getTitle( )
				.getLabel( )
				.getCaption( )
				.setValue( "Bar Chart with Formatted Axis Title" );//$NON-NLS-1$

		// Legend
		Legend lg = cwaBar.getLegend( );
		lg.setVisible( false );
		lg.setItemType( LegendItemType.CATEGORIES_LITERAL );

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes( )[0];
		xAxisPrimary.setCategoryAxis( true );
		xAxisPrimary.setType( AxisType.DATE_TIME_LITERAL );
		xAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.BELOW_LITERAL );
		xAxisPrimary.getOrigin( ).setType( IntersectionType.VALUE_LITERAL );
		xAxisPrimary.getLabel( ).setVisible( false );
		xAxisPrimary.getTitle( ).getCaption( ).setValue( "Regional Markets" ); //$NON-NLS-1$	
		xAxisPrimary.getTitle( ).getOutline( ).setVisible( true );
		xAxisPrimary.setTitlePosition( Position.ABOVE_LITERAL );
		xAxisPrimary.getTitle( ).setVisible( true );

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis( xAxisPrimary );
		yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );
		yAxisPrimary.getTitle( ).getCaption( ).setValue( "Net Profit" );//$NON-NLS-1$
		yAxisPrimary.getTitle( ).setShadowColor( ColorDefinitionImpl.GREEN( )
				.brighter( ) );
		yAxisPrimary.getTitle( ).setBackground( ColorDefinitionImpl.YELLOW( )
				.brighter( ) );
		yAxisPrimary.getTitle( )
				.getCaption( )
				.setColor( ColorDefinitionImpl.RED( ).translucent( ) );
		yAxisPrimary.getTitle( ).setVisible( true );

		// Data Set
		DateTimeDataSet categoryValues = DateTimeDataSetImpl.create( new Calendar[]{
				new CDateTime( 2001, 5, 1 ),
				new CDateTime( 2001, 4, 11 ),
				new CDateTime( 2001, 8, 23 )
		} );
		NumberDataSet orthoValues = NumberDataSetImpl.create( new double[]{
				16170, 24210, -4300
		} );

		// X-Series
		Series seCategory = SeriesImpl.create( );
		seCategory.setDataSet( categoryValues );

		SeriesDefinition sdX = SeriesDefinitionImpl.create( );
		sdX.getSeriesPalette( ).update( -2 );
		xAxisPrimary.getSeriesDefinitions( ).add( sdX );
		sdX.getSeries( ).add( seCategory );

		// Y-Series
		BarSeries bs = (BarSeries) BarSeriesImpl.create( );
		bs.setDataSet( orthoValues );
		bs.getLabel( ).setVisible( true );
		bs.setLabelPosition( Position.INSIDE_LITERAL );

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		yAxisPrimary.getSeriesDefinitions( ).add( sdY );
		sdY.getSeries( ).add( bs );

		return cwaBar;
	}

}
