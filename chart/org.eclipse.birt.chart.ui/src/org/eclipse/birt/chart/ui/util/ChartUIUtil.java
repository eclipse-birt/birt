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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.ui.i18n.Messages;
import org.eclipse.emf.common.util.EList;
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
			query.eAdapters( ).addAll( seriesDefn.eAdapters( ) );
			seriesDefn.getDesignTimeSeries( ).getDataDefinition( ).add( query );
			return query;
		}
		return (Query) seriesDefn.getDesignTimeSeries( )
				.getDataDefinition( )
				.get( queryIndex );
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
	public static List getOrthogonalSeriesDefinitions( Chart chart,
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

			List seriesList = new ArrayList( );
			for ( int i = 0; i < axisList.size( ); i++ )
			{
				seriesList.addAll( ( (Axis) axisList.get( i ) ).getSeriesDefinitions( ) );
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

	public static int getFontSize( FontDefinition font )
	{
		return font.isSetSize( ) ? (int) font.getSize( ) : 9;
	}

	public static int getFontRotation( FontDefinition font )
	{
		return font.isSetRotation( ) ? (int) font.getRotation( ) : 0;
	}

	public static String getFontName( FontDefinition font )
	{
		return font.getName( ) == null ? "Default" : font.getName( ); //$NON-NLS-1$
	}

	public static TextAlignment getFontTextAlignment( FontDefinition font )
	{
		return font.getAlignment( ) == null ? TextAlignmentImpl.create( )
				: font.getAlignment( );
	}

	/**
	 * Checks all data definitions are bound
	 * 
	 * @param chart
	 *            chart model
	 */
	public static boolean checkDataBinding( Chart chart )
	{
		List sdList = ChartUIUtil.getBaseSeriesDefinitions( chart );
		if ( !checkDataDefinition( sdList ) )
		{
			return false;
		}
		for ( int i = 0; i < ChartUIUtil.getOrthogonalAxisNumber( chart ); i++ )
		{
			sdList = ChartUIUtil.getOrthogonalSeriesDefinitions( chart, i );
			if ( !checkDataDefinition( sdList ) )
			{
				return false;
			}
		}
		return true;
	}

	private static boolean checkDataDefinition( List sdList )
	{
		for ( int i = 0; i < sdList.size( ); i++ )
		{
			EList ddList = ( (SeriesDefinition) sdList.get( i ) ).getDesignTimeSeries( )
					.getDataDefinition( );
			for ( int j = 0; j < ddList.size( ); j++ )
			{
				String query = ( (Query) ddList.get( j ) ).getDefinition( );
				if ( query == null || query.length( ) == 0 )
				{
					return false;
				}
			}
		}
		return true;
	}

}
