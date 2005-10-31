/*******************************************************************************
 * Copyright (c) Oct 22, 2004 Actuate Corporation {ADD OTHER COPYRIGHT OWNERS}.
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation {ADD
 * SUBSEQUENT AUTHOR & CONTRIBUTION}
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.type;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.impl.StockSeriesImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.DefaultChartSubTypeImpl;
import org.eclipse.birt.chart.ui.swt.DefaultChartTypeImpl;
import org.eclipse.birt.chart.ui.swt.HelpContentImpl;
import org.eclipse.birt.chart.ui.swt.interfaces.IHelpContent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.data.DefaultBaseSeriesComponent;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.graphics.Image;

/**
 * StockChart
 */
public class StockChart extends DefaultChartTypeImpl
{

	/**
	 * Comment for <code>TYPE_LITERAL</code>
	 */
	public static final String TYPE_LITERAL = "Stock Chart"; //$NON-NLS-1$

	private static final String sStandardDescription = Messages.getString( "StockChart.Txt.Description" ); //$NON-NLS-1$

	private transient Image imgIcon = null;

	private transient Image img2D = null;

	private static final String[] saDimensions = new String[]{
		TWO_DIMENSION_TYPE
	};

	public StockChart( )
	{
		imgIcon = UIHelper.getImage( "icons/obj16/stockcharticon.gif" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IChartType#getName()
	 */
	public String getName( )
	{
		return TYPE_LITERAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IChartType#getImage()
	 */
	public Image getImage( )
	{
		return imgIcon;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IChartType#getHelp()
	 */
	public IHelpContent getHelp( )
	{
		return new HelpContentImpl( TYPE_LITERAL,
				Messages.getString( "StockChart.Txt.HelpText" ) ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getChartSubtypes(java.lang.String)
	 */
	public Collection getChartSubtypes( String sDimension,
			Orientation orientation )
	{
		Vector vSubTypes = new Vector( );
		if ( sDimension.equals( TWO_DIMENSION_TYPE )
				|| sDimension.equals( ChartDimension.TWO_DIMENSIONAL_LITERAL.getName( ) ) )
		{
			if ( orientation.equals( Orientation.VERTICAL_LITERAL ) )
			{
				img2D = UIHelper.getImage( "icons/wizban/stockchartimage.gif" ); //$NON-NLS-1$
			}
			else
			{
				img2D = UIHelper.getImage( "icons/wizban/horizontalstockchartimage.gif" ); //$NON-NLS-1$
			}
			vSubTypes.add( new DefaultChartSubTypeImpl( "Standard Stock Chart", img2D, sStandardDescription ) ); //$NON-NLS-1$
		}
		return vSubTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getModel(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public Chart getModel( String sSubType, Orientation orientation,
			String sDimension, Chart currentChart )
	{
		ChartWithAxes newChart = null;
		if ( currentChart != null )
		{
			newChart = (ChartWithAxes) getConvertedChart( currentChart,
					sSubType,
					orientation,
					sDimension );
			if ( newChart != null )
			{
				return newChart;
			}
		}
		newChart = ChartWithAxesImpl.create( );
		newChart.setType( TYPE_LITERAL );
		newChart.setSubType( sSubType );
		newChart.setOrientation( orientation );
		newChart.setDimension( getDimensionFor( sDimension ) );
		newChart.setUnits( "Points" ); //$NON-NLS-1$

		( (Axis) newChart.getAxes( ).get( 0 ) ).setOrientation( Orientation.HORIZONTAL_LITERAL );
		( (Axis) newChart.getAxes( ).get( 0 ) ).setType( AxisType.DATE_TIME_LITERAL );
		( (Axis) newChart.getAxes( ).get( 0 ) ).setCategoryAxis( true );

		SeriesDefinition sdX = SeriesDefinitionImpl.create( );
		Series categorySeries = SeriesImpl.create( );
		sdX.getSeries( ).add( categorySeries );
		sdX.getSeriesPalette( ).update( 0 );
		( (Axis) newChart.getAxes( ).get( 0 ) ).getSeriesDefinitions( )
				.add( sdX );

		( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
				.get( 0 ) ).setOrientation( Orientation.VERTICAL_LITERAL );
		( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
				.get( 0 ) ).setType( AxisType.LINEAR_LITERAL );

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		sdY.getSeriesPalette( ).update( 0 );
		Series valueSeries = StockSeriesImpl.create( );
		valueSeries.getLabel( ).setVisible( true );
		sdY.getSeries( ).add( valueSeries );
		( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
				.get( 0 ) ).getSeriesDefinitions( ).add( sdY );

		addSampleData( newChart );
		return newChart;
	}

	private void addSampleData( Chart newChart )
	{
		SampleData sd = DataFactory.eINSTANCE.createSampleData( );
		sd.getBaseSampleData( ).clear( );
		sd.getOrthogonalSampleData( ).clear( );

		// Create Base Sample Data
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData( );
		sdBase.setDataSetRepresentation( "01/25/2005,01/26/2005" ); //$NON-NLS-1$
		sd.getBaseSampleData( ).add( sdBase );

		// Create Orthogonal Sample Data (with simulation count of 2)
		OrthogonalSampleData oSample = DataFactory.eINSTANCE.createOrthogonalSampleData( );
		oSample.setDataSetRepresentation( "H5.3 L1.3 O4.5 C3.4,H4.2 L3.1 O3.4 C4.1" ); //$NON-NLS-1$
		oSample.setSeriesDefinitionIndex( 0 );
		sd.getOrthogonalSampleData( ).add( oSample );

		newChart.setSampleData( sd );
	}

	private Chart getConvertedChart( Chart currentChart, String sNewSubType,
			Orientation newOrientation, String sNewDimension )
	{
		Chart helperModel = (Chart) EcoreUtil.copy( currentChart );
		if ( ( currentChart instanceof ChartWithAxes ) )
		{
			if ( currentChart.getType( ).equals( TYPE_LITERAL ) ) // Original
			// chart is
			// of this type
			// (BarChart)
			{
				if ( !currentChart.getSubType( ).equals( sNewSubType ) ) // Original
				// chart
				// is
				// of
				// the
				// required
				// subtype
				{
					currentChart.setSubType( sNewSubType );
					EList axes = ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
							.get( 0 ) ).getAssociatedAxes( );
					for ( int i = 0; i < axes.size( ); i++ )
					{
						( (Axis) axes.get( i ) ).setPercent( false );
						EList seriesdefinitions = ( (Axis) axes.get( i ) ).getSeriesDefinitions( );
						for ( int j = 0; j < seriesdefinitions.size( ); j++ )
						{
							Series series = ( (SeriesDefinition) seriesdefinitions.get( j ) ).getDesignTimeSeries( );
							series.setStacked( false );
						}
					}
				}
			}
			else if ( currentChart.getType( ).equals( LineChart.TYPE_LITERAL )
					|| currentChart.getType( ).equals( AreaChart.TYPE_LITERAL )
					|| currentChart.getType( ).equals( BarChart.TYPE_LITERAL )
					|| currentChart.getType( )
							.equals( ScatterChart.TYPE_LITERAL ) )
			{
				if ( !currentChart.getType( ).equals( TYPE_LITERAL ) )
				{
					currentChart.setSampleData( getConvertedSampleData( currentChart.getSampleData( ) ) );
				}
				currentChart.setType( TYPE_LITERAL );
				( (Axis) ( (ChartWithAxes) currentChart ).getAxes( ).get( 0 ) ).setType( AxisType.DATE_TIME_LITERAL );
				( (Axis) ( (ChartWithAxes) currentChart ).getAxes( ).get( 0 ) ).setCategoryAxis( true );

				currentChart.setSubType( sNewSubType );
				EList axes = ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
						.get( 0 ) ).getAssociatedAxes( );
				for ( int i = 0; i < axes.size( ); i++ )
				{
					( (Axis) axes.get( i ) ).setType( AxisType.LINEAR_LITERAL );
					( (Axis) axes.get( i ) ).setPercent( false );
					EList seriesdefinitions = ( (Axis) axes.get( i ) ).getSeriesDefinitions( );
					for ( int j = 0; j < seriesdefinitions.size( ); j++ )
					{
						Series series = ( (SeriesDefinition) seriesdefinitions.get( j ) ).getDesignTimeSeries( );
						series = getConvertedSeries( series );
						series.setStacked( false );
						( (SeriesDefinition) seriesdefinitions.get( j ) ).getSeries( )
								.clear( );
						( (SeriesDefinition) seriesdefinitions.get( j ) ).getSeries( )
								.add( series );
					}
				}
			}
			else
			{
				return null;
			}
			( (Axis) ( (ChartWithAxes) currentChart ).getAxes( ).get( 0 ) ).setCategoryAxis( true );
		}
		else
		{
			// Create a new instance of the correct type and set initial
			// properties
			currentChart = ChartWithAxesImpl.create( );
			currentChart.setType( TYPE_LITERAL );
			currentChart.setSubType( sNewSubType );
			( (ChartWithAxes) currentChart ).setOrientation( newOrientation );
			currentChart.setDimension( getDimensionFor( sNewDimension ) );

			( (Axis) ( (ChartWithAxes) currentChart ).getAxes( ).get( 0 ) ).setOrientation( Orientation.HORIZONTAL_LITERAL );
			( (Axis) ( (ChartWithAxes) currentChart ).getAxes( ).get( 0 ) ).setType( AxisType.DATE_TIME_LITERAL );
			( (Axis) ( (ChartWithAxes) currentChart ).getAxes( ).get( 0 ) ).setCategoryAxis( true );

			( (Axis) ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
					.get( 0 ) ).getAssociatedAxes( ).get( 0 ) ).setOrientation( Orientation.VERTICAL_LITERAL );
			( (Axis) ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
					.get( 0 ) ).getAssociatedAxes( ).get( 0 ) ).setType( AxisType.LINEAR_LITERAL );

			// Copy generic chart properties from the old chart
			currentChart.setBlock( helperModel.getBlock( ) );
			currentChart.setDescription( helperModel.getDescription( ) );
			currentChart.setGridColumnCount( helperModel.getGridColumnCount( ) );
			currentChart.setSampleData( getConvertedSampleData( helperModel.getSampleData( ) ) );
			currentChart.setScript( helperModel.getScript( ) );
			currentChart.setSeriesThickness( helperModel.getSeriesThickness( ) );
			currentChart.setUnits( helperModel.getUnits( ) );

			if ( helperModel.getType( ).equals( PieChart.TYPE_LITERAL )
					|| helperModel.getType( ).equals( MeterChart.TYPE_LITERAL ) )
			{
				// Clear existing series definitions
				( (Axis) ( (ChartWithAxes) currentChart ).getAxes( ).get( 0 ) ).getSeriesDefinitions( )
						.clear( );

				// Copy base series definitions
				( (Axis) ( (ChartWithAxes) currentChart ).getAxes( ).get( 0 ) ).getSeriesDefinitions( )
						.add( ( (ChartWithoutAxes) helperModel ).getSeriesDefinitions( )
								.get( 0 ) );

				// Clear existing series definitions
				( (Axis) ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
						.get( 0 ) ).getAssociatedAxes( ).get( 0 ) ).getSeriesDefinitions( )
						.clear( );

				// Copy orthogonal series definitions
				( (Axis) ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
						.get( 0 ) ).getAssociatedAxes( ).get( 0 ) ).getSeriesDefinitions( )
						.addAll( ( (SeriesDefinition) ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
								.get( 0 ) ).getSeriesDefinitions( ).get( 0 ) ).getSeriesDefinitions( ) );

				// Update the base series
				Series series = ( (SeriesDefinition) ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
						.get( 0 ) ).getSeriesDefinitions( ).get( 0 ) ).getDesignTimeSeries( );
				series = getConvertedSeries( series );

				// Clear existing series
				( (SeriesDefinition) ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
						.get( 0 ) ).getSeriesDefinitions( ).get( 0 ) ).getSeries( )
						.clear( );

				// Add converted series
				( (SeriesDefinition) ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
						.get( 0 ) ).getSeriesDefinitions( ).get( 0 ) ).getSeries( )
						.add( series );

				// Update the orthogonal series
				EList seriesdefinitions = ( (Axis) ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
						.get( 0 ) ).getAssociatedAxes( ).get( 0 ) ).getSeriesDefinitions( );
				for ( int j = 0; j < seriesdefinitions.size( ); j++ )
				{
					series = ( (SeriesDefinition) seriesdefinitions.get( j ) ).getDesignTimeSeries( );
					series = getConvertedSeries( series );
					series.setStacked( false );
					// Clear any existing series
					( (SeriesDefinition) seriesdefinitions.get( j ) ).getSeries( )
							.clear( );
					// Add the new series
					( (SeriesDefinition) seriesdefinitions.get( j ) ).getSeries( )
							.add( series );
				}
			}
			else
			{
				return null;
			}
		}
		if ( currentChart instanceof ChartWithAxes
				&& !( (ChartWithAxes) currentChart ).getOrientation( )
						.equals( newOrientation ) )
		{
			( (ChartWithAxes) currentChart ).setOrientation( newOrientation );
		}
		if ( !currentChart.getDimension( )
				.equals( getDimensionFor( sNewDimension ) ) )
		{
			currentChart.setDimension( getDimensionFor( sNewDimension ) );
		}
		return currentChart;
	}

	private Series getConvertedSeries( Series series )
	{
		// Do not convert base series
		if ( series.getClass( )
				.getName( )
				.equals( "org.eclipse.birt.chart.model.component.impl.SeriesImpl" ) ) //$NON-NLS-1$
		{
			return series;
		}
		StockSeries stockseries = (StockSeries) StockSeriesImpl.create( );
		stockseries.getLineAttributes( ).setVisible( true );
		stockseries.getLineAttributes( )
				.setColor( ColorDefinitionImpl.BLACK( ) );

		// Copy generic series properties
		stockseries.setLabel( series.getLabel( ) );
		if ( series.getLabelPosition( ).equals( Position.INSIDE_LITERAL )
				|| series.getLabelPosition( ).equals( Position.OUTSIDE_LITERAL ) )
		{
			stockseries.setLabelPosition( series.getLabelPosition( ) );
		}
		else
		{
			stockseries.setLabelPosition( Position.OUTSIDE_LITERAL );
		}
		stockseries.setVisible( series.isVisible( ) );
		if ( series.eIsSet( ComponentPackage.eINSTANCE.getSeries_Triggers( ) ) )
		{
			stockseries.getTriggers( ).addAll( series.getTriggers( ) );
		}
		if ( series.eIsSet( ComponentPackage.eINSTANCE.getSeries_DataPoint( ) ) )
		{
			stockseries.setDataPoint( series.getDataPoint( ) );
		}
		if ( series.eIsSet( ComponentPackage.eINSTANCE.getSeries_DataDefinition( ) ) )
		{
			if ( series.getDataDefinition( ).size( ) != 4 )
			{
				// For High value
				stockseries.getDataDefinition( )
						.add( EcoreUtil.copy( (Query) series.getDataDefinition( )
								.get( 0 ) ) );
				// For Low value
				stockseries.getDataDefinition( )
						.add( EcoreUtil.copy( (Query) series.getDataDefinition( )
								.get( 0 ) ) );
				// For Open value
				stockseries.getDataDefinition( )
						.add( EcoreUtil.copy( (Query) series.getDataDefinition( )
								.get( 0 ) ) );
				// For Close value
				stockseries.getDataDefinition( )
						.add( series.getDataDefinition( ).get( 0 ) );
			}
			stockseries.getDataDefinition( )
					.addAll( series.getDataDefinition( ) );
		}

		// Copy series specific properties
		if ( series instanceof BarSeries )
		{
			// Stock series with transparent fillCandle cannot display
			// propertly. Fix it during conversion if needed.
			ColorDefinition colorDefinition = ( (BarSeries) series ).getRiserOutline( );
			if ( colorDefinition != null
					&& ( !colorDefinition.isSetTransparency( ) || colorDefinition.getTransparency( ) != 255 ) )
				stockseries.getLineAttributes( ).setColor( colorDefinition );
		}
		else if ( series instanceof LineSeries )
		{
			stockseries.setLineAttributes( ( (LineSeries) series ).getLineAttributes( ) );
		}

		return stockseries;
	}

	private SampleData getConvertedSampleData( SampleData currentSampleData )
	{
		// Convert base sample data
		EList bsdList = currentSampleData.getBaseSampleData( );
		Vector vNewBaseSampleData = new Vector( );
		for ( int i = 0; i < bsdList.size( ); i++ )
		{
			BaseSampleData bsd = (BaseSampleData) bsdList.get( i );
			bsd.setDataSetRepresentation( getConvertedBaseSampleDataRepresentation( bsd.getDataSetRepresentation( ) ) );
			vNewBaseSampleData.add( bsd );
		}
		currentSampleData.getBaseSampleData( ).clear( );
		currentSampleData.getBaseSampleData( ).addAll( vNewBaseSampleData );

		// Convert orthogonal sample data
		EList osdList = currentSampleData.getOrthogonalSampleData( );
		Vector vNewOrthogonalSampleData = new Vector( );
		for ( int i = 0; i < osdList.size( ); i++ )
		{
			OrthogonalSampleData osd = (OrthogonalSampleData) osdList.get( i );
			osd.setDataSetRepresentation( getConvertedOrthogonalSampleDataRepresentation( osd.getDataSetRepresentation( ) ) );
			vNewOrthogonalSampleData.add( osd );
		}
		currentSampleData.getOrthogonalSampleData( ).clear( );
		currentSampleData.getOrthogonalSampleData( )
				.addAll( vNewOrthogonalSampleData );
		return currentSampleData;
	}

	private String getConvertedBaseSampleDataRepresentation(
			String sOldRepresentation )
	{
		StringTokenizer strtok = new StringTokenizer( sOldRepresentation, "," ); //$NON-NLS-1$
		StringBuffer sbNewRepresentation = new StringBuffer( "" ); //$NON-NLS-1$
		SimpleDateFormat sdf = new SimpleDateFormat( "MM/dd/yyyy", Locale.getDefault( ) ); //$NON-NLS-1$
		int iValueCount = 0;
		while ( strtok.hasMoreTokens( ) )
		{
			String sElement = strtok.nextToken( ).trim( );
			if ( !sElement.startsWith( "'" ) ) //$NON-NLS-1$
			{
				Calendar cal = Calendar.getInstance( );
				// Increment the date once for each entry so that you get a
				// sequence of dates
				cal.set( Calendar.DATE, cal.get( Calendar.DATE ) + iValueCount );
				sbNewRepresentation.append( sdf.format( cal.getTime( ) ) );
				iValueCount++;
			}
			else
			{
				sElement = sElement.substring( 1, sElement.length( ) - 1 );
				try
				{
					sdf.parse( sElement );
					sbNewRepresentation.append( sElement );
				}
				catch ( ParseException e )
				{
					Calendar cal = Calendar.getInstance( );
					// Increment the date once for each entry so that you get a
					// sequence of dates
					cal.set( Calendar.DATE, cal.get( Calendar.DATE )
							+ iValueCount );
					sbNewRepresentation.append( sdf.format( cal.getTime( ) ) );
					iValueCount++;
				}
			}
			sbNewRepresentation.append( "," ); //$NON-NLS-1$
		}
		return sbNewRepresentation.toString( ).substring( 0,
				sbNewRepresentation.length( ) - 1 );
	}

	private String getConvertedOrthogonalSampleDataRepresentation(
			String sOldRepresentation )
	{
		StringTokenizer strtok = new StringTokenizer( sOldRepresentation, "," ); //$NON-NLS-1$
		NumberFormat nf = NumberFormat.getNumberInstance( );
		StringBuffer sbNewRepresentation = new StringBuffer( "" ); //$NON-NLS-1$
		int iValueCount = 0;
		while ( strtok.hasMoreTokens( ) )
		{
			String sElement = strtok.nextToken( ).trim( );
			double value;
			try
			{
				value = nf.parse( sElement ).doubleValue( );
				if ( value < 0 )
				{
					// If the value is negative, use an arbitrary positive value
					value = 4.0 + iValueCount;
					iValueCount++;
				}
			}
			catch ( ParseException e )
			{
				value = 4.0 + iValueCount;
				iValueCount++;
			}

			sbNewRepresentation.append( "H" ); //$NON-NLS-1$
			sbNewRepresentation.append( String.valueOf( value + 2 ) );
			sbNewRepresentation.append( " " ); //$NON-NLS-1$

			sbNewRepresentation.append( " L" ); //$NON-NLS-1$
			sbNewRepresentation.append( String.valueOf( value - 1 ) );
			sbNewRepresentation.append( " " ); //$NON-NLS-1$

			sbNewRepresentation.append( " O" ); //$NON-NLS-1$
			sbNewRepresentation.append( String.valueOf( value ) );
			sbNewRepresentation.append( " " ); //$NON-NLS-1$

			sbNewRepresentation.append( " C" ); //$NON-NLS-1$
			sbNewRepresentation.append( String.valueOf( value + 1 ) );
			sbNewRepresentation.append( "," ); //$NON-NLS-1$
		}
		return sbNewRepresentation.toString( ).substring( 0,
				sbNewRepresentation.length( ) - 1 );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getSupportedDimensions()
	 */
	public String[] getSupportedDimensions( )
	{
		return saDimensions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getDefaultDimension()
	 */
	public String getDefaultDimension( )
	{
		return saDimensions[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#supportsTransposition()
	 */
	public boolean supportsTransposition( )
	{
		return true;
	}

	private ChartDimension getDimensionFor( String sDimension )
	{
		// Other types are not supported.
		return ChartDimension.TWO_DIMENSIONAL_LITERAL;
	}
	
	public ISelectDataComponent getBaseUI( Chart chart,
			ISelectDataCustomizeUI selectDataUI, IUIServiceProvider builder,
			Object oContext, String sTitle )
	{
		return new DefaultBaseSeriesComponent( (SeriesDefinition) ChartUIUtil.getBaseSeriesDefinitions( chart )
				.get( 0 ),
				builder,
				oContext,
				sTitle );
	}
}