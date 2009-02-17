/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.viewer.sample;

import java.util.Iterator;

import org.eclipse.birt.chart.datafeed.DifferenceEntry;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.integrate.SimpleDataRowExpressionEvaluator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.Marker;
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
import org.eclipse.birt.chart.model.attribute.impl.MultipleFillImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.DifferenceDataSet;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.impl.DifferenceDataSetImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.data.impl.TextDataSetImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.impl.DifferenceSeriesImpl;
import org.eclipse.birt.chart.style.IStyle;
import org.eclipse.birt.chart.style.IStyleProcessor;
import org.eclipse.birt.chart.style.SimpleStyle;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.ibm.icu.util.ULocale;

/**
 * Utility for sample viewer
 */

public class SampleHelper
{

	private static StyleProcessor instance;

	static final class StyleProcessor implements IStyleProcessor
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
		 * @see org.eclipse.birt.chart.style.IStyleProcessor#getStyle(org.eclipse.birt.chart.model.attribute.StyledComponent)
		 */
		public IStyle getStyle( Chart model, StyledComponent name )
		{
			if ( model != null && model.getStyles( ).size( ) > 0 )
			{
				for ( Iterator itr = model.getStyles( ).iterator( ); itr.hasNext( ); )
				{
					StyleMap sm = (StyleMap) itr.next( );

					if ( sm.getComponentName( ).equals( name ) )
					{
						Style style = sm.getStyle( );

						SimpleStyle rt = new SimpleStyle( sstyle );

						if ( style.getFont( ) != null )
						{
							rt.setFont( (FontDefinition) EcoreUtil.copy( style.getFont( ) ) );
						}
						if ( style.getColor( ) != null )
						{
							rt.setColor( (ColorDefinition) EcoreUtil.copy( style.getColor( ) ) );
						}
						if ( style.getBackgroundColor( ) != null )
						{
							rt.setBackgroundColor( (ColorDefinition) EcoreUtil.copy( style.getBackgroundColor( ) ) );
						}
						if ( style.getBackgroundImage( ) != null )
						{
							rt.setBackgroundImage( (Image) EcoreUtil.copy( style.getBackgroundImage( ) ) );
						}
						if ( style.getPadding( ) != null )
						{
							rt.setPadding( (Insets) EcoreUtil.copy( style.getPadding( ) ) );
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
	 * @return
	 */
	public static IDataRowExpressionEvaluator createSampleEvaluator( )
	{
		String[] set = {
				"row[\"Category\"]", "row[\"Value\"]",};//$NON-NLS-1$ //$NON-NLS-2$
		Object[][] data = {
				{
						"Chart", //$NON-NLS-1$
						"Data", //$NON-NLS-1$
						"Report", //$NON-NLS-1$
						"Report Engine", //$NON-NLS-1$
						"Report Designer" //$NON-NLS-1$
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
	 * Gets a sample Style processor
	 * 
	 * @return
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
	 * @return
	 */
	public static RunTimeContext createSampleRuntimeContext( ULocale locale )
	{
		RunTimeContext rtc = new RunTimeContext( );
		rtc.setULocale( locale );
		return rtc;
	}

	/**
	 * Creates a sample chart model
	 * 
	 * @return chart model
	 */
	public static Chart createSampleChart( )
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
				.setValue( "Price difference between years" );//$NON-NLS-1$

		// Legend
		cwaLine.getLegend( ).setVisible( false );

		// X-Axis
		Axis xAxisPrimary = cwaLine.getPrimaryBaseAxes( )[0];
		xAxisPrimary.setType( AxisType.TEXT_LITERAL );
		xAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.BELOW_LITERAL );
		xAxisPrimary.getOrigin( ).setType( IntersectionType.VALUE_LITERAL );
		xAxisPrimary.getTitle( ).setVisible( true );
		xAxisPrimary.getTitle( ).getCaption( ).setValue( "Year" );

		// Y-Axis
		Axis yAxisPrimary = cwaLine.getPrimaryOrthogonalAxis( xAxisPrimary );
		yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );
		yAxisPrimary.getTitle( ).setVisible( true );
		yAxisPrimary.getTitle( ).getCaption( ).setValue( "Price" );

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
		ls.setDataSet( orthoValues );
		ls.getLineAttributes( ).setColor( ColorDefinitionImpl.BLUE( ) );
		for ( int i = 0; i < ls.getMarkers( ).size( ); i++ )
		{
			( (Marker) ls.getMarkers( ).get( i ) ).setType( MarkerType.TRIANGLE_LITERAL );
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

}
