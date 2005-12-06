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

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIPlugin;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * ChartUIUtil
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
			if ( ddList.size( ) == 0 )
			{
				return false;
			}
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

	/**
	 * Synchronize runtime series with design series.
	 * 
	 * @param chart
	 */
	public static void syncRuntimeSeries( Chart chart )
	{
		if ( chart instanceof ChartWithAxes )
		{
			ChartWithAxes cwa = (ChartWithAxes) chart;

			// !NO NEED TO SYNC BASE SERIES
			// SeriesDefinition sdBase = (SeriesDefinition) cwa.getBaseAxes(
			// )[0].getSeriesDefinitions( )
			// .get( 0 );
			// Series seDesignBase = sdBase.getDesignTimeSeries( );

			// dss = new DesignSeriesSynchronizer( seDesignBase,
			// sdBase.getRunTimeSeries( ) );
			// seDesignBase.eAdapters( ).add(0, dss );
			// synchronizers.add( dss );

			Axis[] axa = cwa.getOrthogonalAxes( cwa.getPrimaryBaseAxes( )[0],
					true );
			int iOrthogonalSeriesDefinitionCount = 0;

			for ( int i = 0; i < axa.length; i++ )
			{
				EList elSD = axa[i].getSeriesDefinitions( );
				for ( int j = 0; j < elSD.size( ); j++ )
				{
					SeriesDefinition sd = (SeriesDefinition) elSD.get( j );
					Query qy = sd.getQuery( );
					if ( qy == null )
					{
						continue;
					}
					String sExpression = qy.getDefinition( );
					if ( sExpression == null || sExpression.length( ) == 0 )
					{
						continue;
					}
					iOrthogonalSeriesDefinitionCount++;
				}
			}

			// SYNC ALL ORTHOGONAL SERIES
			for ( int i = 0; i < axa.length; i++ )
			{
				for ( Iterator itr = axa[i].getSeriesDefinitions( ).iterator( ); itr.hasNext( ); )
				{
					SeriesDefinition sdOrthogonal = (SeriesDefinition) itr.next( );
					Series seDesignOrthogonal = sdOrthogonal.getDesignTimeSeries( );

					ArrayList seRuntimes = sdOrthogonal.getRunTimeSeries( );

					sdOrthogonal.getSeries( ).removeAll( seRuntimes );

					for ( int j = 0; j < seRuntimes.size( ); j++ )
					{
						Series seRuntimeOrthogonal = (Series) EcoreUtil.copy( seDesignOrthogonal );
						seRuntimeOrthogonal.setDataSet( ( (Series) seRuntimes.get( j ) ).getDataSet( ) );
						if ( iOrthogonalSeriesDefinitionCount < 1 )
						{
							seRuntimeOrthogonal.setSeriesIdentifier( seDesignOrthogonal.getSeriesIdentifier( ) );
						}
						else
						{
							seRuntimeOrthogonal.setSeriesIdentifier( ( (Series) seRuntimes.get( j ) ).getSeriesIdentifier( ) );
						}
						sdOrthogonal.getSeries( ).add( seRuntimeOrthogonal );
					}
				}
			}
		}
		else if ( chart instanceof ChartWithoutAxes )
		{
			ChartWithoutAxes cwoa = (ChartWithoutAxes) chart;

			final SeriesDefinition sdBase = (SeriesDefinition) cwoa.getSeriesDefinitions( )
					.get( 0 );
			int iOrthogonalSeriesDefinitionCount = 0;

			EList elSD = sdBase.getSeriesDefinitions( );
			for ( int j = 0; j < elSD.size( ); j++ )
			{
				SeriesDefinition sd = (SeriesDefinition) elSD.get( j );
				Query qy = sd.getQuery( );
				if ( qy == null )
				{
					continue;
				}
				String sExpression = qy.getDefinition( );
				if ( sExpression == null || sExpression.length( ) == 0 )
				{
					continue;
				}
				iOrthogonalSeriesDefinitionCount++;
			}

			// SYNC ALL ORTHOGONAL SERIES
			for ( Iterator itr = elSD.iterator( ); itr.hasNext( ); )
			{
				SeriesDefinition sdOrthogonal = (SeriesDefinition) itr.next( );
				Series seDesignOrthogonal = sdOrthogonal.getDesignTimeSeries( );

				ArrayList seRuntimes = sdOrthogonal.getRunTimeSeries( );

				sdOrthogonal.getSeries( ).removeAll( seRuntimes );

				for ( int j = 0; j < seRuntimes.size( ); j++ )
				{
					Series seRuntimeOrthogonal = (Series) EcoreUtil.copy( seDesignOrthogonal );
					seRuntimeOrthogonal.setDataSet( ( (Series) seRuntimes.get( j ) ).getDataSet( ) );
					if ( iOrthogonalSeriesDefinitionCount < 1 )
					{
						seRuntimeOrthogonal.setSeriesIdentifier( seDesignOrthogonal.getSeriesIdentifier( ) );
					}
					else
					{
						seRuntimeOrthogonal.setSeriesIdentifier( ( (Series) seRuntimes.get( j ) ).getSeriesIdentifier( ) );
					}
					sdOrthogonal.getSeries( ).add( seRuntimeOrthogonal );
				}
			}
		}

	}

	/**
	 * Does Live Priview. Need to check all series data binding complete before
	 * invoking
	 * 
	 * @param chart
	 *            chart model
	 * @param dataProvider
	 *            data service provider
	 * @throws ChartException
	 */
	public static void doLivePreview( Chart chart,
			IDataServiceProvider dataProvider ) throws ChartException
	{
		final List expressions;
		final Object[] columnData;
		expressions = Generator.instance( ).getRowExpressions( chart );
		columnData = dataProvider.getDataForColumns( (String[]) expressions.toArray( new String[0] ),
				-1,
				false );

		final Map map = new HashMap( );
		for ( int i = 0; i < expressions.size( ); i++ )
		{
			map.put( expressions.get( i ), columnData[i] );
		}
		IDataRowExpressionEvaluator evaluator = new IDataRowExpressionEvaluator( ) {

			private int i;
			private Object[] column;

			public Object evaluate( String expression )
			{
				column = (Object[]) map.get( expression );
				if ( i >= column.length )
				{
					throw new RuntimeException( new ChartException( ChartUIPlugin.ID,
							ChartException.DATA_SET,
							Messages.getString( "ChartUIUtil.Exception.NoValueReturned" ) ) ); //$NON-NLS-1$
				}
				return column[i];
			}

			public void first( )
			{
				i = 0;
			}

			public boolean next( )
			{
				if ( column != null && i < column.length - 1 )
				{
					i++;
					return true;
				}
				return false;
			}

			public void close( )
			{
				// no-op
			}
		};

		RunTimeContext context = new RunTimeContext( );
		context.setLocale( Locale.getDefault( ) );
		Generator.instance( ).bindData( evaluator, chart, context );

		// Original live preview code: use sample data. See TaskSelectData
		// oldSample = (SampleData) EcoreUtil.copy( getChartModel(
		// ).getSampleData( ) );
		// SampleData newSample = updateSampleData( oldSample );
		// ADD ALL ADAPTERS...AND REFRESH PREVIEW
		// newSample.eAdapters( ).addAll( getChartModel( ).eAdapters( ) );
		// getChartModel( ).setSampleData( newSample );
	}

	/**
	 * Convers sample data according to AxisType
	 * 
	 * @param axisType
	 *            axis type
	 * @param sOldRepresentation
	 *            old sample data representatio
	 * @return new sample data representation
	 */
	public static String getConvertedSampleDataRepresentation(
			AxisType axisType, String sOldRepresentation )
	{
		StringTokenizer strtok = new StringTokenizer( sOldRepresentation, "," ); //$NON-NLS-1$
		NumberFormat nf = NumberFormat.getNumberInstance( );
		SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy", Locale.getDefault( ) ); //$NON-NLS-1$
		StringBuffer sbNewRepresentation = new StringBuffer( "" ); //$NON-NLS-1$
		while ( strtok.hasMoreTokens( ) )
		{
			String sElement = strtok.nextToken( ).trim( );
			if ( sElement.startsWith( "'" ) ) //$NON-NLS-1$
			{
				sElement = sElement.substring( 1, sElement.length( ) - 1 );
			}
			try
			{
				if ( axisType.equals( AxisType.DATE_TIME_LITERAL ) )
				{
					sdf.parse( sElement );
				}
				else if ( axisType.equals( AxisType.TEXT_LITERAL ) )
				{
					if ( !sElement.startsWith( "'" ) ) //$NON-NLS-1$
					{
						sElement = "'" + sElement + "'"; //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
				else
				{
					double dbl = nf.parse( sElement ).doubleValue( );
					sElement = String.valueOf( dbl );
				}
			}
			catch ( ParseException e )
			{
				// Use the orginal sample data if parse exception encountered

				// if ( axisType.equals( AxisType.DATE_TIME_LITERAL ) )
				// {
				// Calendar cal = Calendar.getInstance( Locale.getDefault( ) );
				// StringBuffer sbNewDate = new StringBuffer( "" );
				// //$NON-NLS-1$
				// sbNewDate.append( cal.get( Calendar.MONTH ) + 1 );
				// sbNewDate.append( "/" ); //$NON-NLS-1$
				// // Increasing the date beyond the last date for the month
				// // causes the month to roll over
				// sbNewDate.append( cal.get( Calendar.DATE ) + iValueCount );
				// sbNewDate.append( "/" ); //$NON-NLS-1$
				// sbNewDate.append( cal.get( Calendar.YEAR ) );
				// sElement = sbNewDate.toString( );
				// }
				// else
				// {
				// sElement = String.valueOf( 6.0 + iValueCount );
				// }
			}
			sbNewRepresentation.append( sElement );
			sbNewRepresentation.append( "," ); //$NON-NLS-1$
		}
		return sbNewRepresentation.toString( ).substring( 0,
				sbNewRepresentation.length( ) - 1 );
	}

}
