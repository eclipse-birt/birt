/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.util;

import java.util.Collection;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.ui.i18n.Messages;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * 
 */

public class ChartUIUtil
{

	public static void setBackgroundColor( Control control, boolean selected,
			Color color )
	{
		if ( selected )
		{
			control.setBackground( color );
		}
		else
		{
			control.setBackground( null );
		}
	}

	public static Composite createCompositeWrapper( Composite parent )
	{
		Composite cmp = new Composite( parent, SWT.NONE );
		GridLayout gridLayout = new GridLayout( );
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		cmp.setLayout( gridLayout );
		return cmp;
	}

	public static Query getDataQuery( SeriesDefinition seriesDefn,
			int queryIndex )
	{
		if ( seriesDefn.getDesignTimeSeries( ).getDataDefinition( ).size( ) <= queryIndex )
		{
			Query query = QueryImpl.create( "" ); //$NON-NLS-1$
			seriesDefn.getDesignTimeSeries( ).getDataDefinition( ).add( query );
			return query;
		}
		return (Query) seriesDefn.getDesignTimeSeries( )
				.getDataDefinition( )
				.get( queryIndex );
	}

	public static void addNewSeriesDefinition( Chart chart, EList seriesDefns,
			Collection adapters )
	{
		SeriesDefinition sdTmp = SeriesDefinitionImpl.create( );
		sdTmp.getSeriesPalette( ).update( -1 );
		sdTmp.getSeries( )
				.add( EcoreUtil.copy( ( (SeriesDefinition) seriesDefns.get( 0 ) ).getDesignTimeSeries( ) ) );
		sdTmp.eAdapters( ).addAll( adapters );

		SampleData sd = chart.getSampleData( );
		// Create a new OrthogonalSampleData instance from the existing one
		OrthogonalSampleData sdOrthogonal = DataFactory.eINSTANCE.createOrthogonalSampleData( );
		// TODO: This is HARD CODED sample data...this WILL FAIL for Stock
		// series. This should actually be obtained from
		// the series data feeder as 'default sample values'
		sdOrthogonal.setDataSetRepresentation( "33,51,-12" ); //$NON-NLS-1$
		sdOrthogonal.setSeriesDefinitionIndex( seriesDefns.size( ) );
		sdOrthogonal.eAdapters( ).addAll( sd.eAdapters( ) );
		// Update the SampleData
		chart.getSampleData( ).getOrthogonalSampleData( ).add( sdOrthogonal );

		seriesDefns.add( sdTmp );
	}

	public static String getExpressionString( Object colName )
	{
		if ( colName == null )
		{
			return ""; //$NON-NLS-1$
		}
		return "row[\"" + colName + "\"]";//$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Gets column name in the form of upper case
	 * 
	 * @param expression
	 *            expression with "rows" prefix
	 */
	public static String getColumnName( String expression )
	{
		if ( expression != null && expression.length( ) > 0 )
		{
			int startPoint = expression.indexOf( "[\"" ); //$NON-NLS-1$
			int endPoint = expression.indexOf( "\"]" ); //$NON-NLS-1$
			if ( startPoint >= 0 && endPoint >= 0 )
				return expression.substring( startPoint + 2, endPoint )
						.toUpperCase( );
		}
		return null;
	}

	public static EList getBaseSeriesDefinitions( Chart chart )
	{
		if ( chart instanceof ChartWithAxes )
		{
			return ( (Axis) ( (ChartWithAxes) chart ).getAxes( ).get( 0 ) ).getSeriesDefinitions( );
		}
		else if ( chart instanceof ChartWithoutAxes )
		{
			return ( (ChartWithoutAxes) chart ).getSeriesDefinitions( );
		}
		return null;
	}

	public static int getOrthogonalAxisNumber( Chart chart )
	{
		if ( chart instanceof ChartWithAxes )
		{
			EList axisList = ( (Axis) ( (ChartWithAxes) chart ).getAxes( )
					.get( 0 ) ).getAssociatedAxes( );
			return axisList.size( );
		}
		else if ( chart instanceof ChartWithoutAxes )
		{
			return 1;
		}
		return 0;
	}

	/**
	 * Return specified axis definitions or all series definitions
	 * 
	 * @param chart
	 *            chart
	 * @param axisIndex
	 *            -1 means all. If chart is without axis type, it's useless.
	 * @return specified axis definitions or all series definitions
	 */
	public static EList getOrthogonalSeriesDefinitions( Chart chart,
			int axisIndex )
	{
		if ( chart instanceof ChartWithAxes )
		{
			EList axisList = ( (Axis) ( (ChartWithAxes) chart ).getAxes( )
					.get( 0 ) ).getAssociatedAxes( );
			if ( axisIndex >= 0 )
			{
				return ( (Axis) axisList.get( axisIndex ) ).getSeriesDefinitions( );
			}

			EList seriesList = null;
			for ( int i = 0; i < axisList.size( ); i++ )
			{
				if ( seriesList == null )
				{
					seriesList = ( (Axis) axisList.get( i ) ).getSeriesDefinitions( );
				}
				else
				{
					seriesList.addAll( ( (Axis) axisList.get( i ) ).getSeriesDefinitions( ) );
				}
			}
			return seriesList;
		}
		else if ( chart instanceof ChartWithoutAxes )
		{
			return ( (SeriesDefinition) ( (ChartWithoutAxes) chart ).getSeriesDefinitions( )
					.get( 0 ) ).getSeriesDefinitions( );
		}
		return null;
	}

	public static String getStockTitle( int index )
	{
		switch ( index )
		{
			case 0 :
				return Messages.getString( "ChartUIUtil.StockExp.High" ); //$NON-NLS-1$
			case 1 :
				return Messages.getString( "ChartUIUtil.StockExp.Low" ); //$NON-NLS-1$
			case 2 :
				return Messages.getString( "ChartUIUtil.StockExp.Open" ); //$NON-NLS-1$
			case 3 :
				return Messages.getString( "ChartUIUtil.StockExp.Close" ); //$NON-NLS-1$
			default :
				return ""; //$NON-NLS-1$
		}
	}

	public static Axis getAxisXForProcessing( ChartWithAxes chartWithAxis )
	{
		return (Axis) chartWithAxis.getAxes( ).get( 0 );
	}

	public static Axis getAxisYForProcessing( ChartWithAxes chartWithAxis,
			int axisIndex )
	{
		return (Axis) getAxisXForProcessing( chartWithAxis ).getAssociatedAxes( )
				.get( axisIndex );
	}

	public static Axis getAxisZForProcessing( ChartWithAxes chartWithAxis )
	{
		return (Axis) getAxisXForProcessing( chartWithAxis ).getAncillaryAxes( )
				.get( 0 );
	}

	public static boolean is3DType( Chart chart )
	{
		return chart.getDimension( ).getValue( ) == ChartDimension.THREE_DIMENSIONAL;
	}

}
