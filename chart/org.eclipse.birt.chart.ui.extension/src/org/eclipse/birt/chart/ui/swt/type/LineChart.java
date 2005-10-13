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

import java.util.Collection;
import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.Angle3DImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.Rotation3DImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.AxisImpl;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.impl.LineSeriesImpl;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.DefaultChartSubTypeImpl;
import org.eclipse.birt.chart.ui.swt.DefaultChartTypeImpl;
import org.eclipse.birt.chart.ui.swt.HelpContentImpl;
import org.eclipse.birt.chart.ui.swt.interfaces.IHelpContent;
import org.eclipse.birt.chart.ui.util.UIHelper;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.graphics.Image;

/**
 * LineChart
 */
public class LineChart extends DefaultChartTypeImpl
{

	/**
	 * Comment for <code>TYPE_LITERAL</code>
	 */
	public static final String TYPE_LITERAL = "Line Chart"; //$NON-NLS-1$

	private static final String sStackedDescription = Messages.getString( "LineChart.Txt.StackedDescription" ); //$NON-NLS-1$

	private static final String sPercentStackedDescription = Messages.getString( "LineChart.Txt.PercentStackedDescription" ); //$NON-NLS-1$

	private static final String sOverlayDescription = Messages.getString( "LineChart.Txt.OverlayDescription" ); //$NON-NLS-1$

	private transient Image imgIcon = null;

	private transient Image imgStacked = null;

	private transient Image imgStackedWithDepth = null;

	private transient Image imgPercentStacked = null;

	private transient Image imgPercentStackedWithDepth = null;

	private transient Image imgSideBySide = null;

	private transient Image imgSideBySideWithDepth = null;

	private transient Image imgSideBySide3D = null;

	private static final String[] saDimensions = new String[]{
			TWO_DIMENSION_TYPE,
			TWO_DIMENSION_WITH_DEPTH_TYPE,
			THREE_DIMENSION_TYPE
	};

	public LineChart( )
	{
		imgIcon = UIHelper.getImage( "icons/obj16/linecharticon.gif" ); //$NON-NLS-1$
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
				Messages.getString( "LineChart.Txt.HelpText" ) ); //$NON-NLS-1$
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
				imgStacked = UIHelper.getImage( "icons/wizban/stackedlinechartimage.gif" ); //$NON-NLS-1$
				imgPercentStacked = UIHelper.getImage( "icons/wizban/percentstackedlinechartimage.gif" ); //$NON-NLS-1$
				imgSideBySide = UIHelper.getImage( "icons/wizban/sidebysidelinechartimage.gif" ); //$NON-NLS-1$
			}
			else
			{
				imgStacked = UIHelper.getImage( "icons/wizban/horizontalstackedlinechartimage.gif" ); //$NON-NLS-1$
				imgPercentStacked = UIHelper.getImage( "icons/wizban/horizontalpercentstackedlinechartimage.gif" ); //$NON-NLS-1$
				imgSideBySide = UIHelper.getImage( "icons/wizban/horizontalsidebysidelinechartimage.gif" ); //$NON-NLS-1$
			}

			vSubTypes.add( new DefaultChartSubTypeImpl( "Stacked", imgStacked, sStackedDescription ) ); //$NON-NLS-1$
			vSubTypes.add( new DefaultChartSubTypeImpl( "Percent Stacked", imgPercentStacked, sPercentStackedDescription ) ); //$NON-NLS-1$
			vSubTypes.add( new DefaultChartSubTypeImpl( "Overlay", imgSideBySide, sOverlayDescription ) ); //$NON-NLS-1$
		}
		else if ( sDimension.equals( TWO_DIMENSION_WITH_DEPTH_TYPE )
				|| sDimension.equals( ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL.getName( ) ) )
		{
			if ( orientation.equals( Orientation.VERTICAL_LITERAL ) )
			{
				imgStackedWithDepth = UIHelper.getImage( "icons/wizban/stackedlinechartwithdepthimage.gif" ); //$NON-NLS-1$
				imgPercentStackedWithDepth = UIHelper.getImage( "icons/wizban/percentstackedlinechartwithdepthimage.gif" ); //$NON-NLS-1$
				imgSideBySideWithDepth = UIHelper.getImage( "icons/wizban/sidebysidelinechartwithdepthimage.gif" ); //$NON-NLS-1$
			}
			else
			{
				imgStackedWithDepth = UIHelper.getImage( "icons/wizban/horizontalstackedlinechartwithdepthimage.gif" ); //$NON-NLS-1$
				imgPercentStackedWithDepth = UIHelper.getImage( "icons/wizban/horizontalpercentstackedlinechartwithdepthimage.gif" ); //$NON-NLS-1$
				imgSideBySideWithDepth = UIHelper.getImage( "icons/wizban/horizontalsidebysidelinechartwithdepthimage.gif" ); //$NON-NLS-1$

			}

			vSubTypes.add( new DefaultChartSubTypeImpl( "Stacked", imgStackedWithDepth, sStackedDescription ) ); //$NON-NLS-1$
			vSubTypes.add( new DefaultChartSubTypeImpl( "Percent Stacked", imgPercentStackedWithDepth, //$NON-NLS-1$
					sPercentStackedDescription ) );
			vSubTypes.add( new DefaultChartSubTypeImpl( "Overlay", imgSideBySideWithDepth, sOverlayDescription ) ); //$NON-NLS-1$
		}
		else if ( sDimension.equals( THREE_DIMENSION_TYPE )
				|| sDimension.equals( ChartDimension.THREE_DIMENSIONAL_LITERAL.getName( ) ) )
		{
			imgSideBySide3D = UIHelper.getImage( "icons/wizban/sidebysidelinechart3dimage.gif" ); //$NON-NLS-1$

			vSubTypes.add( new DefaultChartSubTypeImpl( "Overlay", imgSideBySide3D, sOverlayDescription ) ); //$NON-NLS-1$
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
		( (Axis) newChart.getAxes( ).get( 0 ) ).setType( AxisType.TEXT_LITERAL );
		( (Axis) newChart.getAxes( ).get( 0 ) ).setCategoryAxis( true );

		SeriesDefinition sdX = SeriesDefinitionImpl.create( );
		Series categorySeries = SeriesImpl.create( );
		sdX.getSeries( ).add( categorySeries );
		( (Axis) newChart.getAxes( ).get( 0 ) ).getSeriesDefinitions( )
				.add( sdX );

		newChart.getTitle( )
				.getLabel( )
				.getCaption( )
				.setValue( Messages.getString( "LineChart.Txt.DefaultLineChartTitle" ) ); //$NON-NLS-1$

		if ( sSubType.equalsIgnoreCase( "Stacked" ) ) //$NON-NLS-1$
		{
			( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
					.get( 0 ) ).setOrientation( Orientation.VERTICAL_LITERAL );
			( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
					.get( 0 ) ).setType( AxisType.LINEAR_LITERAL );

			SeriesDefinition sdY = SeriesDefinitionImpl.create( );
			sdY.getSeriesPalette( ).update( 0 );
			Series valueSeries = LineSeriesImpl.create( );
			valueSeries.getLabel( ).setVisible( true );
			( (LineSeries) valueSeries ).getMarker( ).setVisible( true );
			( (LineSeries) valueSeries ).setStacked( true );
			sdY.getSeries( ).add( valueSeries );
			( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
					.get( 0 ) ).getSeriesDefinitions( ).add( sdY );
		}
		else if ( sSubType.equalsIgnoreCase( "Percent Stacked" ) ) //$NON-NLS-1$
		{
			( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
					.get( 0 ) ).setOrientation( Orientation.VERTICAL_LITERAL );
			( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
					.get( 0 ) ).setType( AxisType.LINEAR_LITERAL );
			( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
					.get( 0 ) ).setPercent( true );

			SeriesDefinition sdY = SeriesDefinitionImpl.create( );
			sdY.getSeriesPalette( ).update( 0 );
			Series valueSeries = LineSeriesImpl.create( );
			valueSeries.getLabel( ).setVisible( true );
			( (LineSeries) valueSeries ).getMarker( ).setVisible( true );
			( (LineSeries) valueSeries ).setStacked( true );
			sdY.getSeries( ).add( valueSeries );
			( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
					.get( 0 ) ).getSeriesDefinitions( ).add( sdY );
		}
		else if ( sSubType.equalsIgnoreCase( "Overlay" ) ) //$NON-NLS-1$
		{
			( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
					.get( 0 ) ).setOrientation( Orientation.VERTICAL_LITERAL );
			( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
					.get( 0 ) ).setType( AxisType.LINEAR_LITERAL );

			SeriesDefinition sdY = SeriesDefinitionImpl.create( );
			sdY.getSeriesPalette( ).update( 0 );
			Series valueSeries = LineSeriesImpl.create( );
			valueSeries.getLabel( ).setVisible( true );
			( (LineSeries) valueSeries ).getMarker( ).setVisible( true );
			( (LineSeries) valueSeries ).setStacked( false );
			sdY.getSeries( ).add( valueSeries );
			( (Axis) ( (Axis) newChart.getAxes( ).get( 0 ) ).getAssociatedAxes( )
					.get( 0 ) ).getSeriesDefinitions( ).add( sdY );
		}

		if ( sDimension.equals( THREE_DIMENSION_TYPE ) )
		{
			newChart.setRotation( Rotation3DImpl.create( new Angle3D[]{
					Angle3DImpl.createY( 45 ), Angle3DImpl.createX( -20 ),
			} ) );

			newChart.setUnitSpacing( 50 );

			newChart.getPrimaryBaseAxes( )[0].getAncillaryAxes( ).clear( );

			Axis zAxisAncillary = AxisImpl.create( Axis.ANCILLARY_BASE );
			zAxisAncillary.setTitlePosition( Position.BELOW_LITERAL );
			zAxisAncillary.getTitle( )
					.getCaption( )
					.setValue( Messages.getString( "ChartWithAxesImpl.Z_Axis.title" ) ); //$NON-NLS-1$
			zAxisAncillary.getTitle( ).setVisible( true );
			zAxisAncillary.setPrimaryAxis( true );
			zAxisAncillary.setLabelPosition( Position.BELOW_LITERAL );
			zAxisAncillary.setOrientation( Orientation.HORIZONTAL_LITERAL );
			zAxisAncillary.getOrigin( ).setType( IntersectionType.MIN_LITERAL );
			zAxisAncillary.getOrigin( )
					.setValue( NumberDataElementImpl.create( 0 ) );
			zAxisAncillary.getTitle( ).setVisible( false );
			zAxisAncillary.setType( AxisType.TEXT_LITERAL );
			newChart.getPrimaryBaseAxes( )[0].getAncillaryAxes( )
					.add( zAxisAncillary );

			newChart.getPrimaryOrthogonalAxis( newChart.getPrimaryBaseAxes( )[0] )
					.getTitle( )
					.getCaption( )
					.getFont( )
					.setRotation( 0 );

			SeriesDefinition sdZ = SeriesDefinitionImpl.create( );
			sdZ.getSeriesPalette( ).update( 0 );
			sdZ.getSeries( ).add( SeriesImpl.create( ) );
			zAxisAncillary.getSeriesDefinitions( ).add( sdZ );
		}

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
		sdBase.setDataSetRepresentation( "A, B, C" ); //$NON-NLS-1$
		sd.getBaseSampleData( ).add( sdBase );

		// Create Orthogonal Sample Data (with simulation count of 2)
		OrthogonalSampleData oSample = DataFactory.eINSTANCE.createOrthogonalSampleData( );
		oSample.setDataSetRepresentation( "5,-4,12" ); //$NON-NLS-1$
		oSample.setSeriesDefinitionIndex( 0 );
		sd.getOrthogonalSampleData( ).add( oSample );

		if ( newChart.getDimension( ) == ChartDimension.THREE_DIMENSIONAL_LITERAL )
		{
			BaseSampleData sdAncillary = DataFactory.eINSTANCE.createBaseSampleData( );
			sdAncillary.setDataSetRepresentation( "Series 1" ); //$NON-NLS-1$
			sd.getAncillarySampleData( ).add( sdAncillary );
		}

		newChart.setSampleData( sd );
	}

	private Chart getConvertedChart( Chart currentChart, String sNewSubType,
			Orientation newOrientation, String sNewDimension )
	{
		Chart helperModel = (Chart) EcoreUtil.copy( currentChart );
		ChartDimension oldDimension = currentChart.getDimension( );
		if ( ( currentChart instanceof ChartWithAxes ) )
		{
			if ( currentChart.getType( ).equals( TYPE_LITERAL ) ) // Original
			// chart is
			// of this type
			// (LineChart)
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
						if ( sNewSubType.equalsIgnoreCase( "Percent Stacked" ) ) //$NON-NLS-1$
						{
							( (Axis) axes.get( i ) ).setPercent( true );
						}
						else
						{
							( (Axis) axes.get( i ) ).setPercent( false );
						}
						EList seriesdefinitions = ( (Axis) axes.get( i ) ).getSeriesDefinitions( );
						for ( int j = 0; j < seriesdefinitions.size( ); j++ )
						{
							Series series = ( (SeriesDefinition) seriesdefinitions.get( j ) ).getDesignTimeSeries( );
							if ( ( sNewSubType.equalsIgnoreCase( "Stacked" ) || sNewSubType //$NON-NLS-1$
									.equalsIgnoreCase( "Percent Stacked" ) ) ) //$NON-NLS-1$
							{
								series.setStacked( true );
							}
							else
							{
								series.setStacked( false );
							}
						}
					}
				}
			}
			else if ( currentChart.getType( ).equals( BarChart.TYPE_LITERAL )
					|| currentChart.getType( ).equals( AreaChart.TYPE_LITERAL )
					|| currentChart.getType( ).equals( StockChart.TYPE_LITERAL )
					|| currentChart.getType( )
							.equals( ScatterChart.TYPE_LITERAL ) )
			{
				if ( !currentChart.getType( ).equals( BarChart.TYPE_LITERAL ) ) //$NON-NLS-1$
				{
					currentChart.setSampleData( getConvertedSampleData( currentChart.getSampleData( ) ) );
					( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
							.get( 0 ) ).setType( AxisType.TEXT_LITERAL );
				}
				currentChart.setType( TYPE_LITERAL );
				currentChart.setSubType( sNewSubType );
				EList axes = ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
						.get( 0 ) ).getAssociatedAxes( );
				for ( int i = 0; i < axes.size( ); i++ )
				{
					if ( sNewSubType.equalsIgnoreCase( "Percent Stacked" ) ) //$NON-NLS-1$
					{
						( (Axis) axes.get( i ) ).setPercent( true );
					}
					else
					{
						( (Axis) axes.get( i ) ).setPercent( false );
					}
					EList seriesdefinitions = ( (Axis) axes.get( i ) ).getSeriesDefinitions( );
					for ( int j = 0; j < seriesdefinitions.size( ); j++ )
					{
						Series series = ( (SeriesDefinition) seriesdefinitions.get( j ) ).getDesignTimeSeries( );
						series = getConvertedSeries( series );
						if ( ( sNewSubType.equalsIgnoreCase( "Stacked" ) || sNewSubType.equalsIgnoreCase( "Percent Stacked" ) ) ) //$NON-NLS-1$ //$NON-NLS-2$
						{
							series.setStacked( true );
						}
						else
						{
							series.setStacked( false );
						}
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
			( (Axis) ( (ChartWithAxes) currentChart ).getAxes( ).get( 0 ) ).setType( AxisType.TEXT_LITERAL );
			( (Axis) ( (ChartWithAxes) currentChart ).getAxes( ).get( 0 ) ).setCategoryAxis( true );

			( (Axis) ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
					.get( 0 ) ).getAssociatedAxes( ).get( 0 ) ).setOrientation( Orientation.VERTICAL_LITERAL );
			( (Axis) ( (Axis) ( (ChartWithAxes) currentChart ).getAxes( )
					.get( 0 ) ).getAssociatedAxes( ).get( 0 ) ).setType( AxisType.LINEAR_LITERAL );

			// Copy generic chart properties from the old chart
			currentChart.setBlock( helperModel.getBlock( ) );
			currentChart.setDescription( helperModel.getDescription( ) );
			currentChart.setGridColumnCount( helperModel.getGridColumnCount( ) );
			currentChart.setSampleData( helperModel.getSampleData( ) );
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
					if ( ( sNewSubType.equalsIgnoreCase( "Stacked" ) || sNewSubType.equalsIgnoreCase( "Percent Stacked" ) ) ) //$NON-NLS-1$ //$NON-NLS-2$
					{
						series.setStacked( true );
					}
					else
					{
						series.setStacked( false );
					}
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

		if ( sNewDimension.equals( THREE_DIMENSION_TYPE )
				&& getDimensionFor( sNewDimension ) != oldDimension )
		{
			( (ChartWithAxes) currentChart ).setRotation( Rotation3DImpl.create( new Angle3D[]{
					Angle3DImpl.createY( 45 ), Angle3DImpl.createX( -20 ),
			} ) );

			( (ChartWithAxes) currentChart ).setUnitSpacing( 50 );

			( (ChartWithAxes) currentChart ).getPrimaryBaseAxes( )[0].getAncillaryAxes( )
					.clear( );

			Axis zAxisAncillary = AxisImpl.create( Axis.ANCILLARY_BASE );
			zAxisAncillary.setTitlePosition( Position.BELOW_LITERAL );
			zAxisAncillary.getTitle( )
					.getCaption( )
					.setValue( Messages.getString( "ChartWithAxesImpl.Z_Axis.title" ) ); //$NON-NLS-1$
			zAxisAncillary.getTitle( ).setVisible( true );
			zAxisAncillary.setPrimaryAxis( true );
			zAxisAncillary.setLabelPosition( Position.BELOW_LITERAL );
			zAxisAncillary.setOrientation( Orientation.HORIZONTAL_LITERAL );
			zAxisAncillary.getOrigin( ).setType( IntersectionType.MIN_LITERAL );
			zAxisAncillary.getOrigin( )
					.setValue( NumberDataElementImpl.create( 0 ) );
			zAxisAncillary.getTitle( ).setVisible( false );
			zAxisAncillary.setType( AxisType.TEXT_LITERAL );
			( (ChartWithAxes) currentChart ).getPrimaryBaseAxes( )[0].getAncillaryAxes( )
					.add( zAxisAncillary );

			SeriesDefinition sdZ = SeriesDefinitionImpl.create( );
			sdZ.getSeriesPalette( ).update( 0 );
			sdZ.getSeries( ).add( SeriesImpl.create( ) );
			zAxisAncillary.getSeriesDefinitions( ).add( sdZ );

			if ( currentChart.getSampleData( )
					.getAncillarySampleData( )
					.isEmpty( ) )
			{
				BaseSampleData sdAncillary = DataFactory.eINSTANCE.createBaseSampleData( );
				sdAncillary.setDataSetRepresentation( "Series 1" ); //$NON-NLS-1$
				currentChart.getSampleData( )
						.getAncillarySampleData( )
						.add( sdAncillary );
			}
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
		LineSeries lineseries = (LineSeries) LineSeriesImpl.create( );
		lineseries.getLineAttributes( ).setVisible( true );
		lineseries.getLineAttributes( ).setColor( ColorDefinitionImpl.BLACK( ) );
		if ( !( series instanceof ScatterSeries ) )
		{
			Marker marker = AttributeFactory.eINSTANCE.createMarker( );
			marker.setSize( 5 );
			marker.setType( MarkerType.BOX_LITERAL );
			marker.setVisible( true );
			lineseries.setMarker( marker );
		}
		else
		{
			lineseries.setMarker( ( (ScatterSeries) series ).getMarker( ) );
		}

		// Copy generic series properties
		lineseries.setLabel( series.getLabel( ) );
		if ( series.getLabelPosition( ).equals( Position.INSIDE_LITERAL )
				|| series.getLabelPosition( ).equals( Position.OUTSIDE_LITERAL ) )
		{
			lineseries.setLabelPosition( Position.ABOVE_LITERAL );
		}
		else
		{
			lineseries.setLabelPosition( series.getLabelPosition( ) );
		}

		lineseries.setVisible( series.isVisible( ) );
		if ( series.eIsSet( ComponentPackage.eINSTANCE.getSeries_Triggers( ) ) )
		{
			lineseries.getTriggers( ).addAll( series.getTriggers( ) );
		}
		if ( series.eIsSet( ComponentPackage.eINSTANCE.getSeries_DataPoint( ) ) )
		{
			lineseries.setDataPoint( series.getDataPoint( ) );
		}
		if ( series.eIsSet( ComponentPackage.eINSTANCE.getSeries_DataDefinition( ) ) )
		{
			lineseries.getDataDefinition( ).add( series.getDataDefinition( )
					.get( 0 ) );
		}

		// Copy series specific properties
		if ( series instanceof StockSeries )
		{
			lineseries.getLineAttributes( )
					.setColor( ( (StockSeries) series ).getLineAttributes( )
							.getColor( ) );
		}
		return lineseries;
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
		while ( strtok.hasMoreTokens( ) )
		{
			String sElement = strtok.nextToken( ).trim( );
			if ( !sElement.startsWith( "'" ) ) //$NON-NLS-1$
			{
				sbNewRepresentation.append( "'" ); //$NON-NLS-1$
				sbNewRepresentation.append( sElement );
				sbNewRepresentation.append( "'" ); //$NON-NLS-1$
			}
			else
			{
				sbNewRepresentation.append( sElement );
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
		StringBuffer sbNewRepresentation = new StringBuffer( "" ); //$NON-NLS-1$
		while ( strtok.hasMoreTokens( ) )
		{
			String sElement = strtok.nextToken( ).trim( );
			if ( sElement.startsWith( "H" ) ) //$NON-NLS-1$ 
			// Orthogonal sample data is for
			// a stock chart (Orthogonal
			// sample data CANNOT
			// be text
			{
				StringTokenizer strStockTokenizer = new StringTokenizer( sElement );
				sbNewRepresentation.append( strStockTokenizer.nextToken( )
						.trim( )
						.substring( 1 ) );
			}
			else
			{
				sbNewRepresentation.append( sElement );
			}
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#supportsTransposition(java.lang.String)
	 */
	public boolean supportsTransposition( String dimension )
	{
		if ( getDimensionFor( dimension ) == ChartDimension.THREE_DIMENSIONAL_LITERAL )
		{
			return false;
		}

		return supportsTransposition( );
	}

	private ChartDimension getDimensionFor( String sDimension )
	{
		if ( sDimension == null || sDimension.equals( TWO_DIMENSION_TYPE ) )
		{
			return ChartDimension.TWO_DIMENSIONAL_LITERAL;
		}
		if ( sDimension.equals( THREE_DIMENSION_TYPE ) )
		{
			return ChartDimension.THREE_DIMENSIONAL_LITERAL;
		}
		return ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL;
	}
}