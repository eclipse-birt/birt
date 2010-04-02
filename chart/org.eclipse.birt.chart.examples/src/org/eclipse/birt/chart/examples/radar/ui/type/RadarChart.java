/***********************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.examples.radar.ui.type;

import java.util.Collection;
import java.util.Vector;

import org.eclipse.birt.chart.examples.radar.i18n.Messages;
import org.eclipse.birt.chart.examples.radar.model.type.RadarSeries;
import org.eclipse.birt.chart.examples.radar.model.type.impl.RadarSeriesImpl;
import org.eclipse.birt.chart.examples.view.util.UIHelper;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.ui.swt.ChartPreviewPainter;
import org.eclipse.birt.chart.ui.swt.DefaultChartSubTypeImpl;
import org.eclipse.birt.chart.ui.swt.DefaultChartTypeImpl;
import org.eclipse.birt.chart.ui.swt.HelpContentImpl;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartSubType;
import org.eclipse.birt.chart.ui.swt.interfaces.IHelpContent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.data.DefaultBaseSeriesComponent;
import org.eclipse.birt.chart.ui.util.ChartCacheManager;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.swt.graphics.Image;

/**
 * Radar chart
 */
public class RadarChart extends DefaultChartTypeImpl
{

	/**
	 * Comment for <code>TYPE_LITERAL</code>
	 */
	public static final String TYPE_LITERAL = "Radar Chart"; //$NON-NLS-1$

	public static final String STANDARD_SUBTYPE_LITERAL = "Standard Radar Chart"; //$NON-NLS-1$

	public static final String SPIDER_SUBTYPE_LITERAL = "Spider Radar Chart"; //$NON-NLS-1$

	public static final String BULLSEYE_SUBTYPE_LITERAL = "Bullseye Radar Chart"; //$NON-NLS-1$

	private static final String CHART_TITLE = Messages.getString( "RadarChart.Txt.DefaultRadarChartTitle" ); //$NON-NLS-1$

	private static final String sStandardDescription = Messages.getString( "RadarChart.Txt.Description" ); //$NON-NLS-1$

	private static final String sSpiderDescription = Messages.getString( "RadarChart.Txt.SpiderDescription" ); //$NON-NLS-1$

	private static final String sBullseyeDescription = Messages.getString( "RadarChart.Txt.BullseyeDescription" ); //$NON-NLS-1$

	private static final String[] saDimensions = new String[]{
		TWO_DIMENSION_TYPE
	};

	public RadarChart( )
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IChartType#getTypeName()
	 */
	public String getName( )
	{
		return TYPE_LITERAL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IChartType#getTypeName()
	 */
	public Image getImage( )
	{
		return UIHelper.getImage( "icons/obj16/Radar16.gif" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.IChartType#getHelp()
	 */
	public IHelpContent getHelp( )
	{
		return new HelpContentImpl( TYPE_LITERAL,
				Messages.getString( "RadarChart.Txt.HelpText" ) ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getChartSubtypes(
	 * java.lang.String)
	 */
	public Collection<IChartSubType> getChartSubtypes( String sDimension,
			Orientation orientation )
	{
		Vector<IChartSubType> vSubTypes = new Vector<IChartSubType>( );
		// Do not respond to requests for unknown orientations
		if ( !orientation.equals( Orientation.VERTICAL_LITERAL ) )
		{
			return vSubTypes;
		}
		if ( sDimension.equals( TWO_DIMENSION_TYPE )
				|| sDimension.equals( ChartDimension.TWO_DIMENSIONAL_LITERAL.getName( ) ) )
		{

			vSubTypes.add( new DefaultChartSubTypeImpl( STANDARD_SUBTYPE_LITERAL,
					UIHelper.getImage( "icons/wizban/Radar71.gif" ), //$NON-NLS-1$
					sStandardDescription,
					Messages.getString( "RadarChart.SubType.Standard" ) ) ); //$NON-NLS-1$

			vSubTypes.add( new DefaultChartSubTypeImpl( SPIDER_SUBTYPE_LITERAL,
					UIHelper.getImage( "icons/wizban/spiderweb.gif" ), //$NON-NLS-1$
					sSpiderDescription,
					Messages.getString( "RadarChart.SubType.Spider" ) ) ); //$NON-NLS-1$

			vSubTypes.add( new DefaultChartSubTypeImpl( BULLSEYE_SUBTYPE_LITERAL,
					UIHelper.getImage( "icons/wizban/bullseye.gif" ), //$NON-NLS-1$
					sBullseyeDescription,
					Messages.getString( "RadarChart.SubType.Bullseye" ) ) ); //$NON-NLS-1$

		}
		return vSubTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getModel(java.lang
	 * .String, java.lang.String, java.lang.String)
	 */
	public Chart getModel( String sSubType, Orientation orientation,
			String sDimension, Chart currentChart )
	{

		ChartWithoutAxes newChart = null;

		if ( currentChart != null )
		{
			newChart = (ChartWithoutAxes) getConvertedChart( currentChart,
					sSubType,
					sDimension );
			if ( newChart != null )
			{
				return newChart;
			}
		}
		newChart = ChartWithoutAxesImpl.create( );
		newChart.setType( TYPE_LITERAL );
		newChart.setSubType( sSubType );
		newChart.setDimension( getDimensionFor( sDimension ) );
		newChart.setUnits( "Points" ); //$NON-NLS-1$
		if ( newChart.getDimension( )
				.equals( ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL ) )
		{
			newChart.setSeriesThickness( 15 );
		}
		newChart.getLegend( ).setItemType( LegendItemType.SERIES_LITERAL );

		SeriesDefinition sdX = SeriesDefinitionImpl.create( );
		sdX.getSeriesPalette( ).shift( 0 );
		Series categorySeries = SeriesImpl.create( );
		sdX.getSeries( ).add( categorySeries );
		sdX.getQuery( ).setDefinition( "Base Series" ); //$NON-NLS-1$

		newChart.getTitle( ).getLabel( ).getCaption( ).setValue( CHART_TITLE );

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		sdY.setZOrder( 1 );
		sdY.getSeriesPalette( ).shift( 0 );
		RadarSeries valueSeries = RadarSeriesImpl.create( );

		LineAttributes lia = LineAttributesImpl.create( ColorDefinitionImpl.GREY( ),
				LineStyle.SOLID_LITERAL,
				1 );
		valueSeries.setWebLineAttributes( lia );

		valueSeries.getLabel( ).setVisible( true );
		valueSeries.setSeriesIdentifier( "Series 1" ); //$NON-NLS-1$

		sdY.getSeries( ).add( valueSeries );

		sdX.getSeriesDefinitions( ).add( sdY );

		newChart.getSeriesDefinitions( ).add( sdX );

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
		sdBase.setDataSetRepresentation( "A, B, C, D" ); //$NON-NLS-1$
		sd.getBaseSampleData( ).add( sdBase );

		// Create Orthogonal Sample Data (with simulation count of 2)
		OrthogonalSampleData oSample = DataFactory.eINSTANCE.createOrthogonalSampleData( );
		oSample.setDataSetRepresentation( "5, 4, 12, 16" ); //$NON-NLS-1$
		oSample.setSeriesDefinitionIndex( 0 );
		sd.getOrthogonalSampleData( ).add( oSample );

		newChart.setSampleData( sd );
	}

	private Chart getConvertedChart( Chart currentChart, String sNewSubType,
			String sNewDimension )
	{
		Chart helperModel = (Chart) EcoreUtil.copy( currentChart );
		// Cache series to keep attributes during conversion
		ChartCacheManager.getInstance( )
				.cacheSeries( ChartUIUtil.getAllOrthogonalSeriesDefinitions( helperModel ) );
		if ( currentChart instanceof ChartWithAxes )
		{
			if ( !ChartPreviewPainter.isLivePreviewActive( ) )
			{
				helperModel.setSampleData( getConvertedSampleData( helperModel.getSampleData( ),
						( ( (ChartWithAxes) currentChart ).getAxes( ).get( 0 ) ).getType( ),
						AxisType.LINEAR_LITERAL ) );
			}

			// Create a new instance of the correct type and set initial
			// properties
			currentChart = ChartWithoutAxesImpl.create( );
			currentChart.setType( TYPE_LITERAL );
			currentChart.setSubType( sNewSubType );
			currentChart.setDimension( getDimensionFor( sNewDimension ) );
			// ( (DialChart) currentChart ).setDialSuperimposition( false );

			// Copy generic chart properties from the old chart
			currentChart.setBlock( helperModel.getBlock( ) );
			currentChart.setDescription( helperModel.getDescription( ) );
			currentChart.setGridColumnCount( helperModel.getGridColumnCount( ) );

			if ( helperModel.getInteractivity( ) != null )
			{
				currentChart.getInteractivity( )
						.setEnable( helperModel.getInteractivity( ).isEnable( ) );
				currentChart.getInteractivity( )
						.setLegendBehavior( helperModel.getInteractivity( )
								.getLegendBehavior( ) );
			}

			currentChart.setSampleData( helperModel.getSampleData( ) );

			currentChart.setScript( helperModel.getScript( ) );
			currentChart.setUnits( helperModel.getUnits( ) );
			if ( helperModel.getGridColumnCount( ) > 0 )
			{
				currentChart.setGridColumnCount( helperModel.getGridColumnCount( ) );
			}
			else
			{
				currentChart.setGridColumnCount( 1 );
			}

			// Copy series definitions from old chart
			( (ChartWithoutAxes) currentChart ).getSeriesDefinitions( )
					.add( ( ( (ChartWithAxes) helperModel ).getAxes( ).get( 0 ) ).getSeriesDefinitions( )
							.get( 0 ) );
			Vector<SeriesDefinition> vOSD = new Vector<SeriesDefinition>( );

			// Only convert series in primary orthogonal axis.
			Axis primaryOrthogonalAxis = ( (ChartWithAxes) helperModel ).getAxes( )
					.get( 0 )
					.getAssociatedAxes( )
					.get( 0 );
			EList<SeriesDefinition> osd = primaryOrthogonalAxis.getSeriesDefinitions( );
			for ( int j = 0; j < osd.size( ); j++ )
			{
				SeriesDefinition sd = osd.get( j );
				Series series = sd.getDesignTimeSeries( );
				sd.getSeries( ).clear( );
				sd.getSeries( ).add( getConvertedSeries( series, j ) );
				vOSD.add( sd );
			}

			( ( (ChartWithoutAxes) currentChart ).getSeriesDefinitions( ).get( 0 ) ).getSeriesDefinitions( )
					.clear( );
			( ( (ChartWithoutAxes) currentChart ).getSeriesDefinitions( ).get( 0 ) ).getSeriesDefinitions( )
					.addAll( vOSD );

			currentChart.getLegend( )
					.setItemType( LegendItemType.SERIES_LITERAL );
			currentChart.getTitle( )
					.getLabel( )
					.getCaption( )
					.setValue( CHART_TITLE );
		}
		else if ( currentChart instanceof ChartWithoutAxes )
		{
			if ( currentChart.getType( ).equals( TYPE_LITERAL ) )
			{
				currentChart.setSubType( sNewSubType );
				// ( (DialChart) currentChart ).setDialSuperimposition(
				// sNewSubType.equals( SUPERIMPOSED_SUBTYPE_LITERAL ) );
				if ( !currentChart.getDimension( )
						.equals( getDimensionFor( sNewDimension ) ) )
				{
					currentChart.setDimension( getDimensionFor( sNewDimension ) );
				}
			}
			else
			{
				// Create a new instance of the correct type and set initial
				// properties
				currentChart = ChartWithoutAxesImpl.create( );
				currentChart.setType( TYPE_LITERAL );
				currentChart.setSubType( sNewSubType );
				currentChart.setDimension( getDimensionFor( sNewDimension ) );

				// ( (DialChart) currentChart ).setDialSuperimposition(
				// sNewSubType.equals( SUPERIMPOSED_SUBTYPE_LITERAL ) );

				// Copy generic chart properties from the old chart
				currentChart.setBlock( helperModel.getBlock( ) );
				currentChart.setDescription( helperModel.getDescription( ) );
				currentChart.setGridColumnCount( helperModel.getGridColumnCount( ) );
				currentChart.setSampleData( helperModel.getSampleData( ) );
				currentChart.setScript( helperModel.getScript( ) );
				currentChart.setUnits( helperModel.getUnits( ) );

				if ( helperModel.getInteractivity( ) != null )
				{
					currentChart.getInteractivity( )
							.setEnable( helperModel.getInteractivity( )
									.isEnable( ) );
					currentChart.getInteractivity( )
							.setLegendBehavior( helperModel.getInteractivity( )
									.getLegendBehavior( ) );
				}

				// Clear existing series definitions
				( (ChartWithoutAxes) currentChart ).getSeriesDefinitions( )
						.clear( );

				// Copy series definitions
				( (ChartWithoutAxes) currentChart ).getSeriesDefinitions( )
						.add( ( (ChartWithoutAxes) helperModel ).getSeriesDefinitions( )
								.get( 0 ) );

				// Update the series
				EList<SeriesDefinition> seriesdefinitions = ( ( (ChartWithoutAxes) currentChart ).getSeriesDefinitions( ).get( 0 ) ).getSeriesDefinitions( );
				for ( int j = 0; j < seriesdefinitions.size( ); j++ )
				{
					Series series = seriesdefinitions.get( j )
							.getDesignTimeSeries( );
					series = getConvertedSeries( series, j );

					// Clear any existing series
					seriesdefinitions.get( j ).getSeries( ).clear( );
					// Add the new series
					seriesdefinitions.get( j ).getSeries( ).add( series );
				}

				currentChart.getLegend( )
						.setItemType( LegendItemType.SERIES_LITERAL );
				currentChart.getTitle( )
						.getLabel( )
						.getCaption( )
						.setValue( CHART_TITLE );
			}
		}
		else
		{
			return null;
		}
		return currentChart;
	}

	private Series getConvertedSeries( Series series, int seriesIndex )
	{
		// Do not convert base series
		if ( series.getClass( ).getName( ).equals( SeriesImpl.class.getName( ) ) )
		{
			return series;
		}

		RadarSeries radarseries = (RadarSeries) ChartCacheManager.getInstance( )
				.findSeries( RadarSeriesImpl.class.getName( ), seriesIndex );
		if ( radarseries == null )
		{
			radarseries = RadarSeriesImpl.create( );
		}

		// Copy generic series properties
		ChartUIUtil.copyGeneralSeriesAttributes( series, radarseries );

		return radarseries;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getSupportedDimensions
	 * ()
	 */
	public String[] getSupportedDimensions( )
	{
		return saDimensions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getDefaultDimension()
	 */
	public String getDefaultDimension( )
	{
		return saDimensions[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.IChartType#supportsTransposition
	 * ()
	 */
	public boolean supportsTransposition( )
	{
		return false;
	}

	private ChartDimension getDimensionFor( String sDimension )
	{
		return ChartDimension.TWO_DIMENSIONAL_LITERAL;
	}

	public ISelectDataComponent getBaseUI( Chart chart,
			ISelectDataCustomizeUI selectDataUI, ChartWizardContext context,
			String sTitle )
	{
		DefaultBaseSeriesComponent component = new DefaultBaseSeriesComponent( ChartUIUtil.getBaseSeriesDefinitions( chart )
				.get( 0 ),
				context,
				sTitle );
		component.setLabelText( Messages.getString( "RadarBaseSeriesComponent.Label.CategoryDefinition" ) ); //$NON-NLS-1$
		component.setTooltipWhenBlank( Messages.getString( "RadarChart.Tooltip.InputExpression" ) ); //$NON-NLS-1$
		return component;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.DefaultChartTypeImpl#getDisplayName()
	 */
	public String getDisplayName( )
	{
		return Messages.getString( "RadarChart.Txt.DisplayName" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IChartType#getSeries()
	 */
	public Series getSeries( )
	{
		return RadarSeriesImpl.create( );
	}
}
