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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.HorizontalAlignment;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.RiserType;
import org.eclipse.birt.chart.model.attribute.Style;
import org.eclipse.birt.chart.model.attribute.StyleMap;
import org.eclipse.birt.chart.model.attribute.StyledComponent;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.VerticalAlignment;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.FontDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.InsetsImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
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
import org.eclipse.birt.chart.style.IStyle;
import org.eclipse.birt.chart.style.IStyleProcessor;
import org.eclipse.birt.chart.style.SimpleStyle;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.ibm.icu.util.ULocale;

/**
 * 
 */

public class SampleHelper
{

	static final class SampleDataRowEvaluator
			implements
				IDataRowExpressionEvaluator
	{

		private int k = 0;
		private Object[] column;
		private Map map;

		public SampleDataRowEvaluator( String[] set, Object[][] data )
		{
			if ( set == null )
			{
				throw new IllegalArgumentException( );
			}

			map = new HashMap( );
			for ( int i = 0; i < set.length; i++ )
			{
				map.put( set[i], data[i] );
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#evaluate(java.lang.String)
		 */
		public Object evaluate( String expression )
		{
			column = (Object[]) map.get( expression );
			return column[k];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#first()
		 */
		public boolean first( )
		{
			k = 0;

			if ( map.size( ) > 0 )
			{
				column = (Object[]) map.values( ).iterator( ).next( );

				if ( column != null && k < column.length )
				{
					return true;
				}
			}

			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#next()
		 */
		public boolean next( )
		{
			if ( column != null && k < ( column.length - 1 ) )
			{
				k++;
				return true;
			}
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#close()
		 */
		public void close( )
		{
			// Doing nothing.
		}

		public Object evaluateGlobal( String expression )
		{
			// TODO Auto-generated method stub
			return null;
		}

	}

	private static StyleProcessor instance;

	static final class StyleProcessor implements IStyleProcessor
	{

		private static final SimpleStyle sstyle;

		static
		{
			TextAlignment ta = TextAlignmentImpl.create( );
			ta.setHorizontalAlignment( HorizontalAlignment.RIGHT_LITERAL );
			ta.setVerticalAlignment( VerticalAlignment.BOTTOM_LITERAL );
			FontDefinition font = FontDefinitionImpl.create( "BookAntique", //$NON-NLS-1$
					14,
					true,
					true,
					true,
					true,
					true,
					2.0,
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
						new Integer( 10 ),
						new Integer( 2 ),
						new Integer( 25 ),
						new Integer( 15 ),
						new Integer( 10 )
				},
				{
						new Integer( 7 ),
						new Integer( 9 ),
						new Integer( 5 ),
						new Integer( 4 ),
						new Integer( 2 )
				}
		};
		return new SampleDataRowEvaluator( set, data );
	}

	/**
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

	public static RunTimeContext createSampleRuntimeContext( ULocale locale )
	{
		RunTimeContext rtc = new RunTimeContext( );
		rtc.setULocale( locale );
		rtc.setRightToLeft( true );
		return rtc;
	}

	public static Chart createSampleChart( )
	{
		ChartWithAxes cwaBar = ChartWithAxesImpl.create( );
		cwaBar.setDimension( ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL );

		// Plot
		cwaBar.getBlock( ).setBackground( ColorDefinitionImpl.WHITE( ) );
		cwaBar.getBlock( ).getOutline( ).setVisible( true );
		Plot p = cwaBar.getPlot( );
		p.getClientArea( ).setBackground( ColorDefinitionImpl.create( 255,
				255,
				225 ) );
		p.getOutline( ).setVisible( false );

		// Title
		cwaBar.getTitle( ).getLabel( ).getCaption( ).setValue( "Sample Chart" ); //$NON-NLS-1$

		// Legend
		Legend lg = cwaBar.getLegend( );
		lg.getText( ).getFont( ).setSize( 16 );
		lg.setItemType( LegendItemType.CATEGORIES_LITERAL );

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes( )[0];

		xAxisPrimary.setType( AxisType.TEXT_LITERAL );
		xAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.BELOW_LITERAL );
		xAxisPrimary.getOrigin( ).setType( IntersectionType.MIN_LITERAL );
		xAxisPrimary.getTitle( ).setVisible( true );

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis( xAxisPrimary );
		yAxisPrimary.getMajorGrid( ).setTickStyle( TickStyle.LEFT_LITERAL );
		yAxisPrimary.setType( AxisType.LINEAR_LITERAL );
		yAxisPrimary.getLabel( ).getCaption( ).getFont( ).setRotation( 90 );

		// Data Set
		TextDataSet categoryValues = TextDataSetImpl.create( new String[]{
				"Item 1", "Item 2", "Item 3"} ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		NumberDataSet orthoValues = NumberDataSetImpl.create( new double[]{
				25, 35, 15
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
		bs.setRiser( RiserType.TUBE_LITERAL );
		bs.setRiserOutline( null );
		bs.getLabel( ).setVisible( true );
		bs.setLabelPosition( Position.INSIDE_LITERAL );

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		yAxisPrimary.getSeriesDefinitions( ).add( sdY );
		sdY.getSeries( ).add( bs );

		return cwaBar;
	}

}
