/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.viewer.sample;

import org.eclipse.birt.chart.extension.datafeed.DifferenceEntry;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.integrate.SimpleDataRowExpressionEvaluator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendBehaviorType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.birt.chart.model.attribute.Style;
import org.eclipse.birt.chart.model.attribute.StyleMap;
import org.eclipse.birt.chart.model.attribute.StyledComponent;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.attribute.impl.MultipleFillImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.DifferenceDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.DifferenceDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.impl.AreaSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.DifferenceSeriesImpl;
import org.eclipse.birt.chart.style.BaseStyleProcessor;
import org.eclipse.birt.chart.style.IStyle;
import org.eclipse.birt.chart.style.SimpleStyle;

import com.ibm.icu.util.ULocale;

/**
 * Utility for sample viewer
 */

public class SampleHelper
{

	private static final String EXPR_CATEGORY = "row[\"Category\"]";//$NON-NLS-1$
	private static final String EXPR_VALUE = "row[\"Value\"]";//$NON-NLS-1$

	private static StyleProcessor instance;

	static final class StyleProcessor extends BaseStyleProcessor
	{

		private static final SimpleStyle sstyle;

		static
		{
			TextAlignment ta = TextAlignmentImpl.create( );
			ta.setHorizontalAlignment( HorizontalAlignment.CENTER_LITERAL );
			ta.setVerticalAlignment( VerticalAlignment.BOTTOM_LITERAL );
			FontDefinition font = FontDefinitionImpl.create( "BookAntique", //$NON-NLS-1$
					14,
					false,
					true,
					true,
					false,
					true,
					0,
					ta );

			sstyle = new SimpleStyle( font,
					ColorDefinitionImpl.create( 10, 100, 200 ),
					ColorDefinitionImpl.CREAM( ),
					null,
					InsetsImpl.create( 1.0, 1.0, 1.0, 1.0 ) );
		}

		/**
		 * The constructor.
		 */
		private StyleProcessor( )
		{
			super( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.chart.style.IStyleProcessor#getStyle(org.eclipse
		 * .birt.chart.model.attribute.StyledComponent)
		 */
		public IStyle getStyle( Chart model, StyledComponent name )
		{
			if ( model != null && model.getStyles( ).size( ) > 0 )
			{
				for ( StyleMap sm : model.getStyles( ) )
				{
					if ( sm.getComponentName( ).equals( name ) )
					{
						Style style = sm.getStyle( );

						SimpleStyle rt = new SimpleStyle( sstyle );

						if ( style.getFont( ) != null )
						{
							rt.setFont( style.getFont( ).copyInstance( ) );
						}
						if ( style.getColor( ) != null )
						{
							rt.setColor( style.getColor( ).copyInstance( ) );
						}
						if ( style.getBackgroundColor( ) != null )
						{
							rt.setBackgroundColor( style.getBackgroundColor( )
									.copyInstance( ) );
						}
						if ( style.getBackgroundImage( ) != null )
						{
							rt.setBackgroundImage( style.getBackgroundImage( )
									.copyInstance( ) );
						}
						if ( style.getPadding( ) != null )
						{
							rt.setPadding( style.getPadding( ).copyInstance( ) );
						}

						return rt;
					}
				}
			}

			// Always return the default value.
			return sstyle.copy( );
		}
	}

	/**
	 * Creates sample evaluators for data binding. The chart evaluated this
	 * sample evaluator must have queries row["Category"] and row["Value"]
	 * 
	 * @return evaluator
	 */
	public static IDataRowExpressionEvaluator createSampleHeaderEvaluator( )
	{
		String[] set = {
				EXPR_CATEGORY, EXPR_VALUE
		};
		Object[][] data = {
				{
						"Chart", //$NON-NLS-1$
						"Data", //$NON-NLS-1$
						"Report", //$NON-NLS-1$
						"ReportEngine", //$NON-NLS-1$
						"ReportDesigner" //$NON-NLS-1$
				},
				{
						Integer.valueOf( 100 ),
						Integer.valueOf( 80 ),
						Integer.valueOf( 60 ),
						Integer.valueOf( 90 ),
						Integer.valueOf( 120 )
				}
		};
		return new SimpleDataRowExpressionEvaluator( set, data );
	}

	/**
	 * Creates sample evaluators for data binding. The chart evaluated this
	 * sample evaluator must have queries row["Category"] and row["Value"]
	 * 
	 * @return evaluator
	 */
	public static IDataRowExpressionEvaluator createSampleDetailsEvaluator(
			String component )
	{
		int totalValue = 0;
		IDataRowExpressionEvaluator headerEvaluator = createSampleHeaderEvaluator( );
		if ( headerEvaluator.first( ) )
		{
			do
			{
				Object category = headerEvaluator.evaluate( EXPR_CATEGORY );
				if ( component.equals( category ) )
				{
					Object value = headerEvaluator.evaluate( EXPR_VALUE );
					if ( value instanceof Integer )
					{
						totalValue = ( (Integer) value ).intValue( );
					}
					break;
				}
			} while ( headerEvaluator.next( ) );
		}

		String[] set = {
				EXPR_CATEGORY, EXPR_VALUE
		};
		Object[][] data = {
				{
						"2005", //$NON-NLS-1$
						"2006", //$NON-NLS-1$
						"2007", //$NON-NLS-1$
						"2008" //$NON-NLS-1$
				},
				{
						totalValue * 0.3,
						Math.round( totalValue * 0.24 ),
						Math.round( totalValue * 0.26 ),
						totalValue * 0.2
				}
		};
		return new SimpleDataRowExpressionEvaluator( set, data );
	}

	/**
	 * Gets a sample Style processor
	 * 
	 * @return style processor
	 */
	synchronized public static StyleProcessor getSampleStyleProcessor( )
	{
		if ( instance == null )
		{
			instance = new StyleProcessor( );
		}

		return instance;
	}

	/**
	 * Creates a sample RunTimeContext
	 * 
	 * @param locale
	 * @return runtime context
	 */
	public static RunTimeContext createSampleRuntimeContext( ULocale locale )
	{
		RunTimeContext rtc = new RunTimeContext( );
		rtc.setULocale( locale );
		return rtc;
	}

	/**
	 * Creates a sample chart model with runtime data. This chart model includes
	 * data and can output without data feeding.
	 * 
	 * @return chart model
	 */
	public static Chart createSampleRuntimeChart( )
	{
		ChartWithAxes cwaLine = ChartWithAxesImpl.create( );

		// Plot
		cwaLine.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );
		Plot p = cwaLine.getPlot( );
		p.getClientArea( ).setBackground( ColorDefinitionImpl.create( 255,
				255,
				225 ) );

		// Title
		cwaLine.getTitle( )
				.getLabel( )
				.getCaption( )
				.setValue( "Price difference among years" );//$NON-NLS-1$

		// Legend
		cwaLine.getLegend( ).setVisible( true );


		// Legend Interactivity
		cwaLine.getInteractivity( ).setEnable( true );
		cwaLine.getInteractivity( )
				.setLegendBehavior( LegendBehaviorType.HIGHLIGHT_SERIE_LITERAL );

		// X-Axis
		Axis xAxisPrimary = cwaLine.getPrimaryBaseAxes( )[0];
		xAxisPrimary.setType( AxisType.TEXT_LITERAL );
		xAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.BELOW_LITERAL );
		xAxisPrimary.getOrigin( ).setType( IntersectionType.VALUE_LITERAL );
		xAxisPrimary.getTitle( ).setVisible( true );
		xAxisPrimary.getTitle( ).getCaption( ).setValue( "Year" ); //$NON-NLS-1$

		// Y-Axis
		Axis yAxisPrimary = cwaLine.getPrimaryOrthogonalAxis( xAxisPrimary );
		yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );
		yAxisPrimary.getTitle( ).setVisible( true );
		yAxisPrimary.getTitle( ).getCaption( ).setValue( "Price" );//$NON-NLS-1$

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create( new String[]{
				"2005", "2006", "2007", "2008"} );//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		DifferenceDataSet orthoValues = DifferenceDataSetImpl.create( new DifferenceEntry[]{
				new DifferenceEntry( 50, 60 ),
				new DifferenceEntry( 70, 70 ),
				new DifferenceEntry( 15, 30 ),
				new DifferenceEntry( 65, 20 )
		} );

		// X-Series
		Series seCategory = SeriesImpl.create( );
		seCategory.setDataSet( categoryValues );
		SeriesDefinition sdX = SeriesDefinitionImpl.create( );

		xAxisPrimary.getSeriesDefinitions( ).add( sdX );
		sdX.getSeries( ).add( seCategory );

		// Y-Sereis
		DifferenceSeries ls = (DifferenceSeries) DifferenceSeriesImpl.create( );
		ls.setSeriesIdentifier( "Diff" ); //$NON-NLS-1$
		ls.setDataSet( orthoValues );
		ls.getLineAttributes( ).setColor( ColorDefinitionImpl.BLUE( ) );
		for ( int i = 0; i < ls.getMarkers( ).size( ); i++ )
		{
			ls.getMarkers( ).get( i ).setType( MarkerType.TRIANGLE_LITERAL );
		}
		ls.setCurve( true );

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		sdY.getSeriesPalette( ).shift( -2 );
		yAxisPrimary.getSeriesDefinitions( ).add( sdY );
		MultipleFill fill = MultipleFillImpl.create( );
		fill.getFills( ).add( ColorDefinitionImpl.CYAN( ) );
		fill.getFills( ).add( ColorDefinitionImpl.RED( ) );
		sdY.getSeriesPalette( ).getEntries( ).add( 0, fill );
		sdY.getSeries( ).add( ls );

		return cwaLine;
	}

	/**
	 * Creates a sample chart model without runtime data. This chart binds
	 * row["Category"]" and "row[Value]" as category and value expression.
	 * Application must bind data first, and then output it.
	 * 
	 * @return chart model
	 */
	public static final Chart createSampleDesignTimeChart( String chartTitle,
			String xAxisTitle, String yAxisTitle )
	{
		ChartWithAxes cwaArea = ChartWithAxesImpl.create( );

		// Plot/Title
		cwaArea.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );
		Plot p = cwaArea.getPlot( );
		p.getClientArea( ).setBackground( ColorDefinitionImpl.create( 225,
				225,
				225 ) );
		cwaArea.getTitle( ).getLabel( ).getCaption( ).setValue( chartTitle );
		cwaArea.getTitle( ).setVisible( true );

		// Legend
		Legend lg = cwaArea.getLegend( );
		lg.getText( ).getFont( ).setSize( 16 );
		lg.getInsets( ).set( 10, 5, 0, 0 );
		lg.getOutline( ).setVisible( false );
		lg.setItemType( LegendItemType.CATEGORIES_LITERAL );

		// X-Axis
		Axis xAxisPrimary = cwaArea.getPrimaryBaseAxes( )[0];
		xAxisPrimary.setType( AxisType.TEXT_LITERAL );
		xAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.BELOW_LITERAL );
		xAxisPrimary.getMajorGrid( )
				.setLineAttributes( LineAttributesImpl.create( ColorDefinitionImpl.BLUE( ),
						LineStyle.SOLID_LITERAL,
						1 ) );
		xAxisPrimary.getMinorGrid( ).getLineAttributes( ).setVisible( true );
		xAxisPrimary.getOrigin( ).setType( IntersectionType.MIN_LITERAL );
		xAxisPrimary.getTitle( ).getCaption( ).setValue( xAxisTitle );
		xAxisPrimary.getTitle( ).setVisible( true );
		xAxisPrimary.getTitle( ).getCaption( ).getFont( ).setRotation( 0 );
		xAxisPrimary.getLabel( ).setVisible( true );

		// Y-Axis
		Axis yAxisPrimary = cwaArea.getPrimaryOrthogonalAxis( xAxisPrimary );
		yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );
		yAxisPrimary.getMajorGrid( )
				.setLineAttributes( LineAttributesImpl.create( ColorDefinitionImpl.BLACK( ),
						LineStyle.SOLID_LITERAL,
						1 ) );
		yAxisPrimary.getMinorGrid( ).getLineAttributes( ).setVisible( true );
		yAxisPrimary.setPercent( false );
		yAxisPrimary.getTitle( ).getCaption( ).setValue( yAxisTitle );
		yAxisPrimary.getTitle( ).setVisible( true );
		yAxisPrimary.getTitle( ).getCaption( ).getFont( ).setRotation( 90 );
		yAxisPrimary.getLabel( ).setVisible( true );

		// X-Series
		Series seCategory = SeriesImpl.create( );
		seCategory.getDataDefinition( ).add( QueryImpl.create( EXPR_CATEGORY ) );

		SeriesDefinition sdX = SeriesDefinitionImpl.create( );
		sdX.getSeriesPalette( ).shift( 0 );
		xAxisPrimary.getSeriesDefinitions( ).add( sdX );
		sdX.getSeries( ).add( seCategory );

		// Y-Series
		AreaSeries as = (AreaSeries) AreaSeriesImpl.create( );
		as.setTranslucent( true );
		as.getLineAttributes( ).setColor( ColorDefinitionImpl.BLUE( ) );
		as.getLabel( ).setVisible( true );
		as.setCurve( true );
		as.getDataDefinition( ).add( QueryImpl.create( EXPR_VALUE ) );

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		sdY.getSeriesPalette( ).shift( -1 );
		yAxisPrimary.getSeriesDefinitions( ).add( sdY );
		sdY.getSeries( ).add( as );

		return cwaArea;

	}

}
