/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.model.util;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.impl.DialChartImpl;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.GanttSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.impl.AreaSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.DialSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.DifferenceSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.GanttSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.ScatterSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.StockSeriesImpl;

/**
 * This class defines chart element instances with default values, the 'isSet'
 * flag of chart element is initialized.
 * 
 * @since 3.7
 */

public class DefaultValueProvider
{

	/**
	 * 
	 * @return
	 */
	public static ChartWithAxes defChartWithAxes( )
	{
		return defChartWithAxes;
	}

	/**
	 * 
	 * @return
	 */
	public static ChartWithoutAxes defChartWithoutAxes( )
	{
		return defChartWithoutAxes;
	}

	/**
	 * 
	 * @return
	 */
	public static DialChart defDialChart( )
	{
		return defDialChart;
	}

	/**
	 * 
	 * @return
	 */
	public static TitleBlock defTitleBlock( )
	{
		return defTitleBlock;
	}

	/**
	 * 
	 * @return
	 */
	public static Plot defPlot( )
	{
		return defPlot;
	}

	/**
	 * 
	 * @return
	 */
	public static Legend defLegend( )
	{
		return defLegend;
	}

	/**
	 * 
	 * @return
	 */
	public static SeriesDefinition defSeriesDefinition( int id )
	{
		return defSeriesDefinitions.get( id );
	}

	/**
	 * 
	 * @return
	 */
	public static Series defSeries( )
	{
		return defSeries;
	}

	/**
	 * 
	 * @return
	 */
	public static Axis defBaseAxis( )
	{
		return defBaseAxis;
	}

	/**
	 * 
	 * @return
	 */
	public static Axis defOrthogonalAxis( )
	{
		return defOrthAxis;
	}

	/**
	 * 
	 * @return
	 */
	public static Axis defAncillaryAxis( )
	{
		return defAncillaryAxis;
	}
	
	/**
	 * 
	 * @return
	 */
	public static GanttSeries defGanttSeries( )
	{
		return defGanttSeries;
	}

	/**
	 * 
	 * @return
	 */
	public static ScatterSeries defScatterSeries( )
	{
		return defScatterSeries;
	}

	/**
	 * 
	 * @return
	 */
	public static DialSeries defDialSeries( )
	{
		return defDialSeries;
	}

	/**
	 * 
	 * @return
	 */
	public static PieSeries defPieSeries( )
	{
		return defPieSeries;
	}

	/**
	 * 
	 * @return
	 */
	public static DifferenceSeries defDifferenceSeries( )
	{
		return defDifferenceSeries;
	}

	/**
	 * 
	 * @return
	 */
	public static StockSeries defStockSeries( )
	{
		return defStockSeries;
	}

	/**
	 * 
	 * @return
	 */
	public static LineSeries defLineSeries( )
	{
		return defLineSeries;
	}

	/**
	 * 
	 * @return
	 */
	public static AreaSeries defAreaSeries( )
	{
		return defAreaSeries;
	}

	/**
	 * 
	 * @return
	 */
	public static BarSeries defBarSeries( )
	{
		return defBarSeries;
	}

	/**
	 * 
	 * @return
	 */
	public static BubbleSeries defBubbleSeries( )
	{
		return defBubbleSeries;
	}

	private static final ChartWithAxes defChartWithAxes = createChartWithAxes( );

	private static ChartWithAxes createChartWithAxes( )
	{

		ChartWithAxes newChart = ChartWithAxesImpl.create( );

		Axis axBase = newChart.getAxes( ).get( 0 );
		try
		{
			ChartElementUtil.setDefaultValue( axBase, "categoryAxis", true );
		}
		catch ( ChartException e )
		{
		}
		axBase.getSeriesDefinitions( ).add( createSeriesDefinition( 0 ) );
		axBase.getLineAttributes( ).setColor( ColorDefinitionImpl.BLACK( ) );

		Axis axOrth = axBase.getAssociatedAxes( ).get( 0 );
		axOrth.getSeriesDefinitions( ).add( createSeriesDefinition( 0 ) );
		axOrth.getLineAttributes( ).setColor( ColorDefinitionImpl.BLACK( ) );
		createAncillaryAxis( newChart );
		return newChart;
	}

	private static Axis createAncillaryAxis( ChartWithAxes chart )
	{
		chart.setRotation( Rotation3DImpl.create( new Angle3D[]{
			Angle3DImpl.create( -20, 45, 0 )
		} ) );

		try
		{
			chart.setUnitSpacing( 50 );

			Axis zAxisAncillary = AxisImpl.create( Axis.ANCILLARY_BASE );
			ChartElementUtil.setDefaultValue( zAxisAncillary,
					"titlePosition",
					Position.BELOW_LITERAL );
			zAxisAncillary.getTitle( )
					.getCaption( )
					.setValue( Messages.getString( "ChartWithAxesImpl.Z_Axis.title" ) ); //$NON-NLS-1$
			ChartElementUtil.setDefaultValue( zAxisAncillary.getTitle( ),
					"visible",
					false );
			ChartElementUtil.setDefaultValue( zAxisAncillary,
					"primaryAxis",
					true );
			ChartElementUtil.setDefaultValue( zAxisAncillary,
					"labelPosition",
					Position.BELOW_LITERAL );
			ChartElementUtil.setDefaultValue( zAxisAncillary,
					"orientation",
					Orientation.HORIZONTAL_LITERAL );
			ChartElementUtil.setDefaultValue( zAxisAncillary.getOrigin( ),
					"type",
					IntersectionType.MIN_LITERAL );
			zAxisAncillary.getOrigin( )
					.setValue( NumberDataElementImpl.create( 0 ) );
			ChartElementUtil.setDefaultValue( zAxisAncillary,
					"type",
					AxisType.TEXT_LITERAL );
			chart.getPrimaryBaseAxes( )[0].getAncillaryAxes( )
					.add( zAxisAncillary );

			SeriesDefinition sdZ = SeriesDefinitionImpl.create( );
			sdZ.getSeriesPalette( ).shift( 0 );
			sdZ.getSeries( ).add( SeriesImpl.create( ) );
			zAxisAncillary.getSeriesDefinitions( ).add( sdZ );

			return zAxisAncillary;
		}
		catch ( ChartException e )
		{
			// Do nothing.
		}
		return null;
	}
	
	private static final TitleBlock defTitleBlock = getTitleBlock( defChartWithAxes );
	private static final Plot defPlot = getPlot( defChartWithAxes );
	private static final Legend defLegend = getLegend( defChartWithAxes );
	private static final Series defSeries = SeriesImpl.create( );
	private static final GanttSeries defGanttSeries = (GanttSeries) GanttSeriesImpl.create( );
	private static final ScatterSeries defScatterSeries = (ScatterSeries) ScatterSeriesImpl.create( );
	private static final DialSeries defDialSeries = (DialSeries) DialSeriesImpl.create( );
	private static final PieSeries defPieSeries = (PieSeries) PieSeriesImpl.create( );
	private static final DifferenceSeries defDifferenceSeries = (DifferenceSeries) DifferenceSeriesImpl.create( );
	private static final StockSeries defStockSeries = (StockSeries) StockSeriesImpl.create( );
	private static final LineSeries defLineSeries = (LineSeries) LineSeriesImpl.create( );
	private static final AreaSeries defAreaSeries = (AreaSeries) AreaSeriesImpl.create( );
	private static final BarSeries defBarSeries = (BarSeries) BarSeriesImpl.create( );
	private static final BubbleSeries defBubbleSeries = (BubbleSeries) BubbleSeriesImpl.create( );
	private static final Axis defBaseAxis = defChartWithAxes.getAxes( ).get( 0 );
	private static final Axis defOrthAxis = defBaseAxis.getAssociatedAxes( )
			.get( 0 );
	private static final Axis defAncillaryAxis = defBaseAxis.getAncillaryAxes( ).get( 0 );

	public static Insets defLabelInsets( )
	{
		return defLabelInsets;
	}
	
	private static final Insets defLabelInsets = InsetsImpl.create( 0, 2, 0, 3 );
	
	public static Label defLabel( )
	{
		return defLabel;
	}
	
	private static final Label defLabel = LabelImpl.create( );
	
	private static final DefSeriesDefinitionPool defSeriesDefinitions = new DefSeriesDefinitionPool( );

	private static class DefSeriesDefinitionPool
	{

		private int size = 8;

		public SeriesDefinition get( int id )
		{
			if ( id >= size )
			{
				int sizeNew = ( id / 4 + 1 ) * 4;

				SeriesDefinition[] poolNew = new SeriesDefinition[sizeNew];
				System.arraycopy( pool, 0, poolNew, 0, size );

				for ( int i = size; i < sizeNew; i++ )
				{
					poolNew[i] = createSeriesDefinition( -i );
				}

				pool = poolNew;
				size = sizeNew;
			}
			return pool[id];
		}

		private SeriesDefinition[] pool = {
				createSeriesDefinition( 0 ),
				createSeriesDefinition( -1 ),
				createSeriesDefinition( -2 ),
				createSeriesDefinition( -3 ),
				createSeriesDefinition( -4 ),
				createSeriesDefinition( -5 ),
				createSeriesDefinition( -6 ),
				createSeriesDefinition( -7 ),
		};

	}

	private static ChartWithoutAxes defChartWithoutAxes = createChartWithoutAxes( );

	private static ChartWithoutAxes createChartWithoutAxes( )
	{
		ChartWithoutAxes newChart = ChartWithoutAxesImpl.create( );
		newChart.setSubType( "Standard" ); //$NON-NLS-1$

		SeriesDefinition sdX = createSeriesDefinition( 0 );
		sdX.getQuery( ).setDefinition( "Base Series" ); //$NON-NLS-1$

		SeriesDefinition sdY = createSeriesDefinition( 0 );
		sdX.getSeriesDefinitions( ).add( sdY );
		newChart.getSeriesDefinitions( ).add( sdX );

		return newChart;
	}

	private static DialChart defDialChart = createDialChart( );

	private static DialChart createDialChart( )
	{
		DialChart newChart = (DialChart) DialChartImpl.create( );
		newChart.setSubType( "Standard Meter Chart" ); //$NON-NLS-1$
		newChart.setUnits( "Points" ); //$NON-NLS-1$
		newChart.setDialSuperimposition( false );
		newChart.getLegend( ).setItemType( LegendItemType.SERIES_LITERAL );

		SeriesDefinition sdX = createSeriesDefinition( 0 );
		sdX.getQuery( ).setDefinition( "Base Series" ); //$NON-NLS-1$

		SeriesDefinition sdY = createSeriesDefinition( 0 );
		sdX.getSeriesDefinitions( ).add( sdY );

		newChart.getSeriesDefinitions( ).add( sdX );

		return newChart;
	}

	private static TitleBlock getTitleBlock( Chart chart )
	{
		for ( Block b : chart.getBlock( ).getChildren( ) )
		{
			if ( b instanceof TitleBlock )
			{
				return (TitleBlock) b;
			}
		}
		return null;
	}

	private static Plot getPlot( Chart chart )
	{
		for ( Block b : chart.getBlock( ).getChildren( ) )
		{
			if ( b instanceof Plot )
			{
				return (Plot) b;
			}
		}
		return null;
	}

	private static Legend getLegend( Chart chart )
	{
		for ( Block b : chart.getBlock( ).getChildren( ) )
		{
			if ( b instanceof Legend )
			{
				return (Legend) b;
			}
		}
		return null;
	}

	private static SeriesDefinition createSeriesDefinition(
			int paletteShift )
	{
		SeriesDefinition sd = SeriesDefinitionImpl.create( );
		sd.getSeries( ).add( SeriesImpl.create( ) );
//		Palette p = PaletteImpl.create( ColorDefinitionImpl.GREY( ) );
//		p.shift( paletteShift );
//		sd.setSeriesPalette( p );
		sd.getSeriesPalette( ).shift( paletteShift );
		return sd;
	}

}
