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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.device.IDisplayServer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.DataRowExpressionEvaluatorAdapter;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.attribute.impl.AxisOriginImpl;
import org.eclipse.birt.chart.model.attribute.impl.TextAlignmentImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIPlugin;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;
import com.ibm.icu.util.ULocale;

/**
 * ChartUIUtil
 */
public class ChartUIUtil
{

	public static final String FONT_AUTO = Messages.getString( "ChartUIUtil.Font.Auto" ); //$NON-NLS-1$

	private static IDisplayServer swtDisplayServer = null;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.ui/swt" ); //$NON-NLS-1$

	static
	{
		try
		{
			swtDisplayServer = PluginSettings.instance( )
					.getDisplayServer( "ds.SWT" ); //$NON-NLS-1$
		}
		catch ( ChartException e )
		{
			logger.log( e );
		}
	}

	public static IDisplayServer getDisplayServer( )
	{
		return swtDisplayServer;
	}

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

	/**
	 * Create a row expression based on column name.
	 * 
	 * @param colName
	 */
	public static String getExpressionString( String colName )
	{
		return ExpressionUtil.createJSRowExpression( colName );
	}

	/**
	 * Returns the default number format instance for default locale.
	 * 
	 * @return the default number format
	 */
	public static NumberFormat getDefaultNumberFormatInstance( )
	{
		NumberFormat numberFormat = NumberFormat.getInstance( );
		// fix icu limitation which only allow 3 fraction digits as maximum by
		// default. ?100 is enough.
		numberFormat.setMaximumFractionDigits( 100 );
		return numberFormat;
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
	 * Return specified axis definitions.
	 * 
	 * @param chart
	 *            chart
	 * @param axisIndex
	 *            If chart is without axis type, it always return all orthogonal
	 *            series definition.
	 * @return specified axis definitions or all series definitions
	 */
	public static EList getOrthogonalSeriesDefinitions( Chart chart,
			int axisIndex )
	{
		if ( chart instanceof ChartWithAxes )
		{
			EList axisList = ( (Axis) ( (ChartWithAxes) chart ).getAxes( )
					.get( 0 ) ).getAssociatedAxes( );
			return ( (Axis) axisList.get( axisIndex ) ).getSeriesDefinitions( );
		}
		else if ( chart instanceof ChartWithoutAxes )
		{
			return ( (SeriesDefinition) ( (ChartWithoutAxes) chart ).getSeriesDefinitions( )
					.get( 0 ) ).getSeriesDefinitions( );
		}
		return null;
	}

	/**
	 * Return specified axis definitions or all series definitions. Remember
	 * return type is ArrayList, not EList, no event is fired when adding or
	 * removing an element.
	 * 
	 * @param chart
	 *            chart
	 * @return specified axis definitions or all series definitions
	 */
	public static List getAllOrthogonalSeriesDefinitions( Chart chart )
	{
		List seriesList = new ArrayList( );
		if ( chart instanceof ChartWithAxes )
		{
			EList axisList = ( (Axis) ( (ChartWithAxes) chart ).getAxes( )
					.get( 0 ) ).getAssociatedAxes( );
			for ( int i = 0; i < axisList.size( ); i++ )
			{
				seriesList.addAll( ( (Axis) axisList.get( i ) ).getSeriesDefinitions( ) );
			}
		}
		else if ( chart instanceof ChartWithoutAxes )
		{
			seriesList.addAll( ( (SeriesDefinition) ( (ChartWithoutAxes) chart ).getSeriesDefinitions( )
					.get( 0 ) ).getSeriesDefinitions( ) );
		}
		return seriesList;
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

	public static String getGanttTitle( int index )
	{
		switch ( index )
		{
			case 0 :
				return Messages.getString( "ChartUIUtil.GanttExp.Start" ); //$NON-NLS-1$
			case 1 :
				return Messages.getString( "ChartUIUtil.GanttExp.End" ); //$NON-NLS-1$
			case 2 :
				return Messages.getString( "ChartUIUtil.GanttExp.Label" ); //$NON-NLS-1$
			default :
				return ""; //$NON-NLS-1$
		}
	}

	public static String getBubbleTitle( int index )
	{
		switch ( index )
		{
			case 0 :
				return Messages.getString( "ChartUIUtil.BubbleExp.Label" ); //$NON-NLS-1$
			case 1 :
				return Messages.getString( "ChartUIUtil.BubbleExp.Size" ); //$NON-NLS-1$
			default :
				return ""; //$NON-NLS-1$
		}
	}

	public static String getDifferenceTitle( int index )
	{
		switch ( index )
		{
			case 0 :
				return Messages.getString( "ChartUIUtil.DifferenceExp.Postive" ); //$NON-NLS-1$
			case 1 :
				return Messages.getString( "ChartUIUtil.DifferenceExp.Negative" ); //$NON-NLS-1$
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

	public static int getFontRotation( FontDefinition font )
	{
		return font.isSetRotation( ) ? (int) font.getRotation( ) : 0;
	}

	public static String getFontName( FontDefinition font )
	{
		return font.getName( ) == null ? FONT_AUTO : font.getName( );
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

					List seRuntimes = sdOrthogonal.getRunTimeSeries( );

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

				List seRuntimes = sdOrthogonal.getRunTimeSeries( );

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
	 * Does Live Preview. Need to check all series data binding complete before
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
		expressions = Generator.instance( ).getRowExpressions( chart, null );
		columnData = dataProvider.getDataForColumns( (String[]) expressions.toArray( new String[expressions.size( )] ),
				-1,
				false );

		final Map map = new HashMap( );
		for ( int i = 0; i < expressions.size( ); i++ )
		{
			map.put( expressions.get( i ), columnData[i] );
		}
		IDataRowExpressionEvaluator evaluator = new DataRowExpressionEvaluatorAdapter( ) {

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

			public boolean first( )
			{
				i = 0;

				if ( map.size( ) > 0 )
				{
					column = (Object[]) map.values( ).iterator( ).next( );

					if ( column != null && i <= column.length - 1 )
					{
						return true;
					}
				}

				return false;
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
		context.setULocale( ULocale.getDefault( ) );
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
	 * Converts sample data according to AxisType
	 * 
	 * @param axisType
	 *            axis type
	 * @param sOldRepresentation
	 *            old sample data representation
	 * @return new sample data representation
	 */
	public static String getConvertedSampleDataRepresentation(
			AxisType axisType, String sOldRepresentation )
	{
		// StringTokenizer strtok = new StringTokenizer( sOldRepresentation, ","
		// ); //$NON-NLS-1$
		// NumberFormat nf = NumberFormat.getNumberInstance( );
		// SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy",
		// Locale.getDefault( ) ); //$NON-NLS-1$
		// StringBuffer sbNewRepresentation = new StringBuffer( "" );
		// //$NON-NLS-1$
		// while ( strtok.hasMoreTokens( ) )
		// {
		// String sElement = strtok.nextToken( ).trim( );
		// if ( sElement.startsWith( "'" ) ) //$NON-NLS-1$
		// {
		// sElement = sElement.substring( 1, sElement.length( ) - 1 );
		// }
		// try
		// {
		// if ( axisType.equals( AxisType.DATE_TIME_LITERAL ) )
		// {
		// sdf.parse( sElement );
		// }
		// else if ( axisType.equals( AxisType.TEXT_LITERAL ) )
		// {
		// if ( !sElement.startsWith( "'" ) ) //$NON-NLS-1$
		// {
		// sElement = "'" + sElement + "'"; //$NON-NLS-1$ //$NON-NLS-2$
		// }
		// }
		// else
		// {
		// double dbl = nf.parse( sElement ).doubleValue( );
		// sElement = String.valueOf( dbl );
		// }
		// }
		// catch ( ParseException e )
		// {
		// // Use the orginal sample data if parse exception encountered
		//
		// // if ( axisType.equals( AxisType.DATE_TIME_LITERAL ) )
		// // {
		// // Calendar cal = Calendar.getInstance( Locale.getDefault( ) );
		// // StringBuffer sbNewDate = new StringBuffer( "" );
		// // //$NON-NLS-1$
		// // sbNewDate.append( cal.get( Calendar.MONTH ) + 1 );
		// // sbNewDate.append( "/" ); //$NON-NLS-1$
		// // // Increasing the date beyond the last date for the month
		// // // causes the month to roll over
		// // sbNewDate.append( cal.get( Calendar.DATE ) + iValueCount );
		// // sbNewDate.append( "/" ); //$NON-NLS-1$
		// // sbNewDate.append( cal.get( Calendar.YEAR ) );
		// // sElement = sbNewDate.toString( );
		// // }
		// // else
		// // {
		// // sElement = String.valueOf( 6.0 + iValueCount );
		// // }
		// }
		// sbNewRepresentation.append( sElement );
		// sbNewRepresentation.append( "," ); //$NON-NLS-1$
		// }
		// return sbNewRepresentation.toString( ).substring( 0,
		// sbNewRepresentation.length( ) - 1 );
		return getNewSampleData( axisType );
	}

	/**
	 * Creates new sample data according to specified axis type.
	 * 
	 * @param axisType
	 *            axis type
	 */
	public static String getNewSampleData( AxisType axisType )
	{
		if ( axisType.equals( AxisType.DATE_TIME_LITERAL ) )
		{
			SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy" ); //$NON-NLS-1$
			Calendar today = new GregorianCalendar( );
			Calendar firstDay = new GregorianCalendar( today.get( Calendar.YEAR ),
					0,
					1 );
			Calendar lastDay = new GregorianCalendar( today.get( Calendar.YEAR ),
					11,
					31 );
			return sdf.format( firstDay.getTime( ) )
					+ "," + sdf.format( today.getTime( ) ) + "," + sdf.format( lastDay.getTime( ) ); //$NON-NLS-1$//$NON-NLS-2$
		}
		else if ( axisType.equals( AxisType.TEXT_LITERAL ) )
		{
			return "'A','B','C'"; //$NON-NLS-1$
		}
		return "5,4,12"; //$NON-NLS-1$
	}

	/**
	 * Sets the query to all series grouping, except for the first which should
	 * be set manually
	 * 
	 * @param chart
	 *            chart model
	 * @param queryDefinition
	 *            group query definition
	 */
	public static void setAllGroupingQueryExceptFirst( Chart chart,
			String queryDefinition )
	{
		List sds = ChartUIUtil.getAllOrthogonalSeriesDefinitions( chart );
		for ( int i = 0; i < sds.size( ); i++ )
		{
			if ( i != 0 )
			{
				// Except for the first, which is changed manually.
				SeriesDefinition sd = (SeriesDefinition) sds.get( i );
				if ( sd.getQuery( ) != null )
				{
					sd.getQuery( ).setDefinition( queryDefinition );
				}
				else
				{
					Query query = QueryImpl.create( queryDefinition );
					query.eAdapters( ).addAll( sd.eAdapters( ) );
					sd.setQuery( query );
				}
			}
		}
	}

	/**
	 * Adds an orthogonal axis. Ensures only one event is fired.
	 * 
	 * @param chartModel
	 *            chart model
	 */
	public static void addAxis( final ChartWithAxes chartModel )
	{
		// Prevent notifications rendering preview
		ChartAdapter.beginIgnoreNotifications( );
		// --------------------------Begin
		
		// Create a clone of the existing Y Axis
		Axis yAxis = (Axis) ( (Axis) chartModel.getAxes( ).get( 0 ) ).getAssociatedAxes( )
				.get( 0 );
		Axis overlayAxis = (Axis) EcoreUtil.copy( yAxis );
		// Now update overlay axis to set the properties that are
		// different from
		// the original
		overlayAxis.setPrimaryAxis( false );
		overlayAxis.setOrigin( AxisOriginImpl.create( IntersectionType.MAX_LITERAL,
				null ) );
		overlayAxis.setLabelPosition( Position.RIGHT_LITERAL );
		overlayAxis.setTitlePosition( Position.RIGHT_LITERAL );
		overlayAxis.getTitle( )
				.getCaption( )
				.setValue( Messages.getString( "TaskSelectType.Caption.OverlayAxis1" ) ); //$NON-NLS-1$
		overlayAxis.eAdapters( ).addAll( yAxis.eAdapters( ) );

		// Retain the first series of the axis. Remove others
		if ( overlayAxis.getSeriesDefinitions( ).size( ) > 1 )
		{
			EList list = overlayAxis.getSeriesDefinitions( );
			for ( int i = list.size( ) - 1; i > 0; i-- )
			{
				list.remove( i );
			}
		}

		// Update overlay series definition(retain the group query,
		// clean the data query)
		SeriesDefinition sdOverlay = (SeriesDefinition) overlayAxis.getSeriesDefinitions( )
				.get( 0 );
		EList dds = sdOverlay.getDesignTimeSeries( ).getDataDefinition( );
		for ( int i = 0; i < dds.size( ); i++ )
		{
			( (Query) dds.get( i ) ).setDefinition( "" ); //$NON-NLS-1$
		}

		// Update the sample values for the new overlay series
		SampleData sd = chartModel.getSampleData( );
		// Create a new OrthogonalSampleData instance from the existing
		// one
		int currentSize = sd.getOrthogonalSampleData( ).size( );
		OrthogonalSampleData sdOrthogonal = (OrthogonalSampleData) EcoreUtil.copy( (EObject) chartModel.getSampleData( )
				.getOrthogonalSampleData( )
				.get( 0 ) );
		sdOrthogonal.setDataSetRepresentation( ChartUIUtil.getNewSampleData( overlayAxis.getType( ) ) );
		sdOrthogonal.setSeriesDefinitionIndex( currentSize );
		sdOrthogonal.eAdapters( ).addAll( sd.eAdapters( ) );
		sd.getOrthogonalSampleData( ).add( sdOrthogonal );
		// ------------------End
		ChartAdapter.endIgnoreNotifications( );

		( (Axis) chartModel.getAxes( ).get( 0 ) ).getAssociatedAxes( )
				.add( overlayAxis );
	}

	/**
	 * Removes the last orthogonal axes. This is a batch operation.
	 * 
	 * @param chartModel
	 *            chart model
	 * @param removedAxisNumber
	 *            the number of axes to be removed
	 */
	public static void removeLastAxes( ChartWithAxes chartModel,
			int removedAxisNumber )
	{
		for ( int i = 0; i < removedAxisNumber; i++ )
		{
			removeLastAxis( chartModel );
		}
	}

	/**
	 * Removes the last orthogonal axis. Ensures only one event is fired.
	 * 
	 * @param chartModel
	 *            chart model
	 */
	public static void removeLastAxis( ChartWithAxes chartModel )
	{
		removeAxis( chartModel, getOrthogonalAxisNumber( chartModel ) - 1 );
	}

	/**
	 * Removes an orthogonal axis in the specified position. Ensures only one
	 * event is fired.
	 * 
	 * @param chartModel
	 *            chart model
	 * @param axisIndex
	 *            the index of the axis to be removed
	 */
	public static void removeAxis( final Chart chartModel, final int axisIndex )
	{
		if ( chartModel instanceof ChartWithoutAxes )
		{
			return;
		}

		ChartAdapter.beginIgnoreNotifications( );
		// Ensure one primary axis existent
		Axis oldPrimaryAxis = getAxisYForProcessing( (ChartWithAxes) chartModel,
				axisIndex );
		if ( oldPrimaryAxis.isPrimaryAxis( ) )
		{
			int yAxisSize = getOrthogonalAxisNumber( chartModel );
			Axis newPrimaryAxis = null;
			if ( axisIndex + 1 < yAxisSize )
			{
				newPrimaryAxis = getAxisYForProcessing( (ChartWithAxes) chartModel,
						axisIndex + 1 );
			}
			else
			{
				newPrimaryAxis = getAxisYForProcessing( (ChartWithAxes) chartModel,
						0 );
			}
			// Retain the properties of primary axis
			newPrimaryAxis.setPrimaryAxis( true );
			newPrimaryAxis.setOrigin( oldPrimaryAxis.getOrigin( ) );
			newPrimaryAxis.setLabelPosition( oldPrimaryAxis.getLabelPosition( ) );
			newPrimaryAxis.setTitlePosition( oldPrimaryAxis.getTitlePosition( ) );
		}
		ChartAdapter.endIgnoreNotifications( );

		// Remove the orthogonal axis
		getAxisXForProcessing( (ChartWithAxes) chartModel ).getAssociatedAxes( )
				.remove( axisIndex );

		// Remove the sample data of the axis
		// int iSDIndexFirst = getLastSeriesIndexWithinAxis( chartModel,
		// axisIndex - 1 ) + 1;
		// int iSDIndexLast = getLastSeriesIndexWithinAxis( chartModel,
		// axisIndex );
		// EList list = chartModel.getSampleData( ).getOrthogonalSampleData( );
		// for ( int i = 0; i < list.size( ); i++ )
		// {
		// if ( i >= iSDIndexFirst && i <= iSDIndexLast )
		// {
		// list.remove( i );
		// i--;
		// iSDIndexLast--;
		// }
		// }

	}

	public static int getLastSeriesIndexWithinAxis( Chart chartModel,
			int axisIndex )
	{
		if ( chartModel instanceof ChartWithoutAxes
				|| axisIndex < 0
				|| axisIndex >= getOrthogonalAxisNumber( chartModel ) )
		{
			return -1;
		}
		int seriesIndex = -1;
		for ( int i = 0; i <= axisIndex; i++ )
		{
			seriesIndex += getOrthogonalSeriesDefinitions( chartModel, i ).size( );
		}
		return seriesIndex;
	}

	/**
	 * Reorder the index of the orthogonal sample data. If index is wrong,
	 * sample data can't display correctly.
	 * 
	 * @param chartModel
	 */
	public static void reorderOrthogonalSampleDataIndex( Chart chartModel )
	{
		EList list = chartModel.getSampleData( ).getOrthogonalSampleData( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			( (OrthogonalSampleData) list.get( i ) ).setSeriesDefinitionIndex( i );
		}
	}

	/**
	 * Copies general attributes in Series.
	 * 
	 * @param oldSeries
	 *            copied series
	 * @param newSeries
	 *            target series
	 */
	public static void copyGeneralSeriesAttributes( Series oldSeries,
			Series newSeries )
	{
		newSeries.setLabel( oldSeries.getLabel( ) );
		newSeries.setSeriesIdentifier( oldSeries.getSeriesIdentifier( ) );
		if ( oldSeries.isSetVisible( ) )
		{
			newSeries.setVisible( oldSeries.isVisible( ) );
		}
		// Attention: stacked will be set in chart conversion according to
		// sub-type.
		if ( oldSeries.isSetStacked( ) && newSeries.canBeStacked( ) )
		{
			newSeries.setStacked( oldSeries.isStacked( ) );
		}
		if ( oldSeries.isSetTranslucent( ) )
		{
			newSeries.setTranslucent( oldSeries.isTranslucent( ) );
		}
		if ( oldSeries.eIsSet( ComponentPackage.eINSTANCE.getSeries_Triggers( ) ) )
		{
			newSeries.getTriggers( ).addAll( oldSeries.getTriggers( ) );
		}
		if ( oldSeries.eIsSet( ComponentPackage.eINSTANCE.getSeries_DataPoint( ) ) )
		{
			newSeries.setDataPoint( oldSeries.getDataPoint( ) );
		}
		if ( oldSeries.eIsSet( ComponentPackage.eINSTANCE.getSeries_CurveFitting( ) ) )
		{
			newSeries.setCurveFitting( oldSeries.getCurveFitting( ) );
		}

		// Label position
		if ( oldSeries.getLabelPosition( ).equals( Position.INSIDE_LITERAL )
				|| oldSeries.getLabelPosition( )
						.equals( Position.OUTSIDE_LITERAL ) )
		{
			if ( newSeries instanceof LineSeries )
			{
				newSeries.setLabelPosition( Position.ABOVE_LITERAL );
			}
			else
			{
				newSeries.setLabelPosition( oldSeries.getLabelPosition( ) );
			}
		}
		else
		{
			if ( newSeries instanceof LineSeries )
			{
				newSeries.setLabelPosition( oldSeries.getLabelPosition( ) );
			}
			else
			{
				newSeries.setLabelPosition( Position.OUTSIDE_LITERAL );
			}
		}

		// Data definition
		if ( oldSeries.eIsSet( ComponentPackage.eINSTANCE.getSeries_DataDefinition( ) ) )
		{
			Object query = oldSeries.getDataDefinition( ).get( 0 );
			newSeries.getDataDefinition( ).clear( );
			if ( newSeries instanceof StockSeries )
			{
				if ( oldSeries.getDataDefinition( ).size( ) != 4 )
				{
					// For High value
					newSeries.getDataDefinition( )
							.add( EcoreUtil.copy( (Query) query ) );
					// For Low value
					newSeries.getDataDefinition( )
							.add( EcoreUtil.copy( (Query) query ) );
					// For Open value
					newSeries.getDataDefinition( )
							.add( EcoreUtil.copy( (Query) query ) );
					// For Close value
					newSeries.getDataDefinition( )
							.add( EcoreUtil.copy( (Query) query ) );
				}
				else
				{
					newSeries.getDataDefinition( )
							.addAll( oldSeries.getDataDefinition( ) );
				}
			}
			else if ( newSeries instanceof BubbleSeries )
			{
				if ( oldSeries.getDataDefinition( ).size( ) != 2 )
				{
					// For value
					newSeries.getDataDefinition( )
							.add( EcoreUtil.copy( (Query) query ) );
					// For Size
					newSeries.getDataDefinition( )
							.add( EcoreUtil.copy( (Query) query ) );
				}
				else
				{
					newSeries.getDataDefinition( )
							.addAll( oldSeries.getDataDefinition( ) );
				}
			}
			else if ( newSeries instanceof DifferenceSeries )
			{
				if ( oldSeries.getDataDefinition( ).size( ) != 2 )
				{
					newSeries.getDataDefinition( )
							.add( EcoreUtil.copy( (Query) query ) );
					newSeries.getDataDefinition( )
							.add( EcoreUtil.copy( (Query) query ) );
				}
				else
				{
					newSeries.getDataDefinition( )
							.addAll( oldSeries.getDataDefinition( ) );
				}
			}
			else
			{
				newSeries.getDataDefinition( ).add( query );
			}
		}
	}

	public static ChartDimension getDimensionType( String sDimension )
	{
		if ( sDimension == null
				|| sDimension.equals( IChartType.TWO_DIMENSION_TYPE ) )
		{
			return ChartDimension.TWO_DIMENSIONAL_LITERAL;
		}
		if ( sDimension.equals( IChartType.THREE_DIMENSION_TYPE ) )
		{
			return ChartDimension.THREE_DIMENSIONAL_LITERAL;
		}
		return ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL;
	}

	public static String getDimensionString( ChartDimension dimension )
	{
		if ( dimension == null
				|| dimension == ChartDimension.TWO_DIMENSIONAL_LITERAL )
		{
			return IChartType.TWO_DIMENSION_TYPE;
		}
		if ( dimension == ChartDimension.THREE_DIMENSIONAL_LITERAL )
		{
			return IChartType.THREE_DIMENSION_TYPE;
		}
		return IChartType.TWO_DIMENSION_WITH_DEPTH_TYPE;
	}

	/**
	 * Sets the given help context id on the given control's shell.
	 * 
	 * @param control
	 *            the control on which to register the context id
	 * @param contextId
	 *            the context id to use when F1 help is invoked
	 */
	public static void bindHelp( Control control, String contextId )
	{
		try
		{
			IWorkbench workbench = PlatformUI.getWorkbench( );
			workbench.getHelpSystem( ).setHelp( control.getShell( ), contextId );
		}
		catch ( RuntimeException e )
		{
			// Do nothing since there's no workbench
		}
	}

	/**
	 * Returns whether wall/floor has been set if it's 3D chart.
	 * 
	 * @param chart
	 *            Chart model
	 * @return Returns whether wall/floor has been set if it's 3D chart. Returns
	 *         true if chart is ChartWithoutAxes or not 3D
	 */
	public static boolean is3DWallFloorSet( Chart chart )
	{
		if ( !ChartUIUtil.is3DType( chart )
				|| chart instanceof ChartWithoutAxes )
		{
			return true;
		}
		ChartWithAxes chartWithAxes = (ChartWithAxes) chart;
		ColorDefinition wall = (ColorDefinition) chartWithAxes.getWallFill( );
		ColorDefinition floor = (ColorDefinition) chartWithAxes.getFloorFill( );
		return wall != null
				&& wall.getTransparency( ) > 0
				|| floor != null
				&& floor.getTransparency( ) > 0;
	}

}
