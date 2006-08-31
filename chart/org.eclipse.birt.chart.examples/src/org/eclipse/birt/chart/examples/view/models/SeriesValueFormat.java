/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.chart.examples.view.models;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaDateFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
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
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.util.CDateTime;

import com.ibm.icu.util.Calendar;


public class SeriesValueFormat
{
	public static final Chart createSeriesValueFormat( )
	{
		ChartWithAxes cwaBar = ChartWithAxesImpl.create( );

		// Plot
		cwaBar.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );

		// Title
		cwaBar.getTitle( )
				.getLabel( )
				.getCaption( )
				.setValue( "Bar Chart with Formatted Series" );//$NON-NLS-1$

		// Legend
		cwaBar.getLegend( ).setVisible( false );
		cwaBar.getLegend( ).setItemType( LegendItemType.CATEGORIES_LITERAL );

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes( )[0];
		xAxisPrimary.setCategoryAxis( true );
		xAxisPrimary.setType( AxisType.DATE_TIME_LITERAL );
		xAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.BELOW_LITERAL );
		xAxisPrimary.getOrigin( ).setType( IntersectionType.MIN_LITERAL );

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis( xAxisPrimary );
		yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );
		yAxisPrimary.setType( AxisType.LINEAR_LITERAL );

		// Data Set
		DateTimeDataSet categoryValues = DateTimeDataSetImpl.create( new Calendar[]{
				new CDateTime( 2001, 5, 1 ),
				new CDateTime( 2001, 4, 11 ),
				new CDateTime( 2001, 8, 23 ),
				new CDateTime( 2001, 10, 15 )
		} );
		NumberDataSet orthoValues = NumberDataSetImpl.create( new double[]{
				1620, 3630, 4600, -1800
		} );

		// X-Series
		Series seCategory = SeriesImpl.create( );
		seCategory.setDataSet( categoryValues );

		SeriesDefinition sdX = SeriesDefinitionImpl.create( );
		sdX.getSeriesPalette( ).update( -1 );
		xAxisPrimary.getSeriesDefinitions( ).add( sdX );
		sdX.getSeries( ).add( seCategory );
		sdX.setFormatSpecifier( JavaDateFormatSpecifierImpl.create( "MM/dd/yyyy" ) );//$NON-NLS-1$

		//Y-Series 
		BarSeries bs = (BarSeries) BarSeriesImpl.create( );
		bs.setDataSet( orthoValues );
		bs.setTranslucent( true );
		bs.getLabel( ).setVisible( true );
		bs.setLabelPosition( Position.INSIDE_LITERAL );

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		yAxisPrimary.getSeriesDefinitions( ).add( sdY );
		sdY.getSeries( ).add( bs );

		DataPointComponent dpc = DataPointComponentImpl.create( DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create( "$###,###.00" ) );//$NON-NLS-1$
		bs.getDataPoint( ).getComponents( ).clear( );
		bs.getDataPoint( ).getComponents( ).add( dpc );

		return cwaBar;
	}

}
