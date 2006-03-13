/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.ui.swt.type;

import java.util.Collection;
import java.util.Vector;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.LeaderLineStyle;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
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
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
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

import com.ibm.icu.util.StringTokenizer;

/**
 * PieChart
 */
public class PieChart extends DefaultChartTypeImpl
{

	/**
	 * Comment for <code>TYPE_LITERAL</code>
	 */
	public static final String TYPE_LITERAL = "Pie Chart"; //$NON-NLS-1$

	private static final String STANDARD_SUBTYPE_LITERAL = "Standard Pie Chart"; //$NON-NLS-1$

	public static final String CHART_TITLE = Messages.getString( "PieChart.Txt.DefaultPieChartTitle" ); //$NON-NLS-1$

	private static final String sStandardDescription = Messages.getString( "PieChart.Txt.Description" ); //$NON-NLS-1$

	private transient Image imgIcon = null;

	private transient Image img2D = null;

	private transient Image img2DWithDepth = null;

	private static final String[] saDimensions = new String[]{
			TWO_DIMENSION_TYPE, TWO_DIMENSION_WITH_DEPTH_TYPE
	};

	public PieChart( )
	{
		imgIcon = UIHelper.getImage( "icons/obj16/piecharticon.gif" ); //$NON-NLS-1$
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
				Messages.getString( "PieChart.Txt.HelpText" ) ); //$NON-NLS-1$
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
		// Do not respond to requests for unknown orientations
		if ( !orientation.equals( Orientation.VERTICAL_LITERAL ) )
		{
			return vSubTypes;
		}
		if ( sDimension.equals( TWO_DIMENSION_TYPE )
				|| sDimension.equals( ChartDimension.TWO_DIMENSIONAL_LITERAL.getName( ) ) )
		{
			img2D = UIHelper.getImage( "icons/wizban/piechartimage.gif" ); //$NON-NLS-1$

			vSubTypes.add( new DefaultChartSubTypeImpl( STANDARD_SUBTYPE_LITERAL,
					img2D,
					sStandardDescription,
					Messages.getString( "PieChart.SubType.Standard" ) ) ); //$NON-NLS-1$
		}
		else if ( sDimension.equals( TWO_DIMENSION_WITH_DEPTH_TYPE )
				|| sDimension.equals( ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL.getName( ) ) )
		{
			img2DWithDepth = UIHelper.getImage( "icons/wizban/piechartwithdepthimage.gif" ); //$NON-NLS-1$

			vSubTypes.add( new DefaultChartSubTypeImpl( STANDARD_SUBTYPE_LITERAL,
					img2DWithDepth,
					sStandardDescription,
					Messages.getString( "PieChart.SubType.Standard" ) ) ); //$NON-NLS-1$
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

		SeriesDefinition sdX = SeriesDefinitionImpl.create( );
		sdX.getSeriesPalette( ).update( 0 );
		Series categorySeries = SeriesImpl.create( );
		sdX.getSeries( ).add( categorySeries );
		sdX.getQuery( ).setDefinition( "Base Series" ); //$NON-NLS-1$

		newChart.getTitle( ).getLabel( ).getCaption( ).setValue( CHART_TITLE );

		SeriesDefinition sdY = SeriesDefinitionImpl.create( );
		sdY.getSeriesPalette( ).update( 0 );
		Series valueSeries = PieSeriesImpl.create( );
		valueSeries.getLabel( ).setVisible( true );
		valueSeries.setSeriesIdentifier( "valueSeriesIdentifier" ); //$NON-NLS-1$
		( (PieSeries) valueSeries ).getTitle( )
				.getCaption( )
				.setValue( "valueSeries" ); //$NON-NLS-1$
		( (PieSeries) valueSeries ).setStacked( false );
		( (PieSeries) valueSeries ).setExplosion( 0 );
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
		sdBase.setDataSetRepresentation( "A, B, C" ); //$NON-NLS-1$
		sd.getBaseSampleData( ).add( sdBase );

		// Create Orthogonal Sample Data (with simulation count of 2)
		OrthogonalSampleData oSample = DataFactory.eINSTANCE.createOrthogonalSampleData( );
		oSample.setDataSetRepresentation( "5,4,12" ); //$NON-NLS-1$
		oSample.setSeriesDefinitionIndex( 0 );
		sd.getOrthogonalSampleData( ).add( oSample );

		/*
		 * OrthogonalSampleData oSample2 =
		 * DataFactory.eINSTANCE.createOrthogonalSampleData();
		 * oSample2.setDataSetRepresentation("7,22,14");
		 * oSample2.setSeriesDefinitionIndex(0);
		 * sd.getOrthogonalSampleData().add(oSample2);
		 */
		newChart.setSampleData( sd );
	}

	private Chart getConvertedChart( Chart currentChart, String sNewSubType,
			String sNewDimension )
	{
		Chart helperModel = (Chart) EcoreUtil.copy( currentChart );
		if ( ( currentChart instanceof ChartWithAxes ) )
		{
			// Create a new instance of the correct type and set initial
			// properties
			currentChart = ChartWithoutAxesImpl.create( );
			currentChart.setType( TYPE_LITERAL );
			currentChart.setSubType( sNewSubType );
			currentChart.setDimension( getDimensionFor( sNewDimension ) );

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

			if ( !currentChart.getType( ).equals( LineChart.TYPE_LITERAL )
					&& !currentChart.getType( ).equals( BarChart.TYPE_LITERAL )
					&& !currentChart.getType( ).equals( AreaChart.TYPE_LITERAL ) )
			{
				currentChart.setSampleData( getConvertedSampleData( helperModel.getSampleData( ) ) );
			}

			currentChart.setScript( helperModel.getScript( ) );
			if ( helperModel.isSetSeriesThickness( ) )
			{
				currentChart.setSeriesThickness( helperModel.getSeriesThickness( ) );
			}
			else
			{
				currentChart.setSeriesThickness( 15 );
			}
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
					.add( ( (Axis) ( (ChartWithAxes) helperModel ).getAxes( )
							.get( 0 ) ).getSeriesDefinitions( ).get( 0 ) );
			Vector vOSD = new Vector( );

			// Only convert series in primary orthogonal axis.
			Axis primaryOrthogonalAxis = ( (ChartWithAxes) helperModel ).getPrimaryOrthogonalAxis( (Axis) ( (ChartWithAxes) helperModel ).getAxes( )
					.get( 0 ) );
			EList osd = primaryOrthogonalAxis.getSeriesDefinitions( );
			for ( int j = 0; j < osd.size( ); j++ )
			{
				SeriesDefinition sd = (SeriesDefinition) osd.get( j );
				Series series = sd.getDesignTimeSeries( );
				sd.getSeries( ).clear( );
				sd.getSeries( ).add( getConvertedSeries( series ) );
				vOSD.add( sd );
			}

			( (SeriesDefinition) ( (ChartWithoutAxes) currentChart ).getSeriesDefinitions( )
					.get( 0 ) ).getSeriesDefinitions( ).clear( );
			( (SeriesDefinition) ( (ChartWithoutAxes) currentChart ).getSeriesDefinitions( )
					.get( 0 ) ).getSeriesDefinitions( ).addAll( vOSD );

			// Set the legend item type t Categories to have the chart behave as
			// expected by default.
			currentChart.getLegend( )
					.setItemType( LegendItemType.CATEGORIES_LITERAL );
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
				if ( !currentChart.getDimension( )
						.equals( getDimensionFor( sNewDimension ) ) )
				{
					currentChart.setDimension( getDimensionFor( sNewDimension ) );
				}

				if ( !currentChart.isSetSeriesThickness( ) )
				{
					currentChart.setSeriesThickness( 15 );
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

				// Copy generic chart properties from the old chart
				currentChart.setBlock( helperModel.getBlock( ) );
				currentChart.setDescription( helperModel.getDescription( ) );
				currentChart.setGridColumnCount( helperModel.getGridColumnCount( ) );
				currentChart.setSampleData( helperModel.getSampleData( ) );
				currentChart.setScript( helperModel.getScript( ) );
				currentChart.setSeriesThickness( helperModel.getSeriesThickness( ) );
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
						.addAll( ( (ChartWithoutAxes) helperModel ).getSeriesDefinitions( ) );

				// Update the series
				EList seriesdefinitions = ( (SeriesDefinition) ( (ChartWithoutAxes) currentChart ).getSeriesDefinitions( )
						.get( 0 ) ).getSeriesDefinitions( );

				Series series;

				for ( int j = 0; j < seriesdefinitions.size( ); j++ )
				{
					series = ( (SeriesDefinition) seriesdefinitions.get( j ) ).getDesignTimeSeries( );
					series = getConvertedSeries( series );

					// Clear any existing series
					( (SeriesDefinition) seriesdefinitions.get( j ) ).getSeries( )
							.clear( );
					// Add the new series
					( (SeriesDefinition) seriesdefinitions.get( j ) ).getSeries( )
							.add( series );
				}

				currentChart.getLegend( )
						.setItemType( LegendItemType.CATEGORIES_LITERAL );
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

	private Series getConvertedSeries( Series series )
	{
		// Do not convert base series
		if ( series.getClass( )
				.getName( )
				.equals( "org.eclipse.birt.chart.model.component.impl.SeriesImpl" ) ) //$NON-NLS-1$
		{
			return series;
		}
		PieSeries pieseries = (PieSeries) PieSeriesImpl.create( );
		pieseries.setExplosion( 10 );
		pieseries.getLeaderLineAttributes( ).setVisible( true );
		pieseries.getLeaderLineAttributes( )
				.setColor( ColorDefinitionImpl.BLACK( ) );
		pieseries.setLeaderLineStyle( LeaderLineStyle.STRETCH_TO_SIDE_LITERAL );

		// Copy generic series properties
		pieseries.setLabel( series.getLabel( ) );
		if ( series.getLabelPosition( ).equals( Position.INSIDE_LITERAL )
				|| series.getLabelPosition( ).equals( Position.OUTSIDE_LITERAL ) )
		{
			pieseries.setLabelPosition( series.getLabelPosition( ) );
		}
		else
		{
			pieseries.setLabelPosition( Position.OUTSIDE_LITERAL );
		}
		pieseries.setVisible( series.isVisible( ) );
		if ( series.eIsSet( ComponentPackage.eINSTANCE.getSeries_Triggers( ) ) )
		{
			pieseries.getTriggers( ).addAll( series.getTriggers( ) );
		}
		if ( series.eIsSet( ComponentPackage.eINSTANCE.getSeries_DataPoint( ) ) )
		{
			pieseries.setDataPoint( series.getDataPoint( ) );
		}
		if ( series.eIsSet( ComponentPackage.eINSTANCE.getSeries_DataDefinition( ) ) )
		{
			pieseries.getDataDefinition( ).add( series.getDataDefinition( )
					.get( 0 ) );
		}

		// Copy series specific properties
		if ( series instanceof LineSeries )
		{
			pieseries.setLeaderLineAttributes( ( (LineSeries) series ).getLineAttributes( ) );
		}
		else if ( series instanceof StockSeries )
		{
			pieseries.setLeaderLineAttributes( ( (StockSeries) series ).getLineAttributes( ) );
		}
		return pieseries;
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
				if ( sElement.startsWith( "-" ) ) //$NON-NLS-1$ // Negative Number
				{
					sElement = sElement.substring( 1 ); // Convert to positive
					// number since negative
					// values are not
					// supported for pie charts
				}
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
			if ( sElement.startsWith( "H" ) ) //$NON-NLS-1$ // Orthogonal sample data is for
			// a stock chart (Orthogonal
			// sample data CANNOT be text
			{
				StringTokenizer strStockTokenizer = new StringTokenizer( sElement );
				sbNewRepresentation.append( strStockTokenizer.nextToken( )
						.trim( )
						.substring( 1 ) );
			}
			else
			{
				// Ignore negative check
				// if ( sElement.startsWith( "-" ) ) //$NON-NLS-1$ // Negative
				// Number
				// {
				// sElement = sElement.substring( 1 ); // Convert to positive
				// // number since negative
				// // values are not
				// // supported for pie charts
				// }
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
		return false;
	}

	private ChartDimension getDimensionFor( String sDimension )
	{
		if ( sDimension == null || sDimension.equals( TWO_DIMENSION_TYPE ) )
		{
			return ChartDimension.TWO_DIMENSIONAL_LITERAL;
		}
		return ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL;
	}

	public ISelectDataComponent getBaseUI( Chart chart,
			ISelectDataCustomizeUI selectDataUI, IUIServiceProvider builder,
			Object oContext, String sTitle )
	{
		DefaultBaseSeriesComponent component = new DefaultBaseSeriesComponent( (SeriesDefinition) ChartUIUtil.getBaseSeriesDefinitions( chart )
				.get( 0 ),
				builder,
				oContext,
				sTitle );
		component.setLabelText( Messages.getString( "PieBaseSeriesComponent.Label.CategoryDefinition" ) ); //$NON-NLS-1$
		return component;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.ui.swt.DefaultChartTypeImpl#getDisplayName()
	 */
	public String getDisplayName( )
	{
		return Messages.getString( "PieChart.Txt.DisplayName" ); //$NON-NLS-1$
	}

}