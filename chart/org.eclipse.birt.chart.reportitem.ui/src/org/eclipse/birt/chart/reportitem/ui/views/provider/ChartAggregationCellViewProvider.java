/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui.views.provider;

import java.util.List;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.reportitem.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.ChartXTabUtil;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.ui.extension.AggregationCellViewAdapter;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;

/**
 * Provider for conversion between chart and text in cross tab
 */
public class ChartAggregationCellViewProvider
		extends
			AggregationCellViewAdapter
{

	private final static DimensionValue DEFAULT_COLUMN_WIDTH = new DimensionValue( 80,
			DesignChoiceConstants.UNITS_PT );
	private final static DimensionValue DEFAULT_ROW_WIDTH = new DimensionValue( 30,
			DesignChoiceConstants.UNITS_PT );

	public String getViewName( )
	{
		return ChartReportItemConstants.CHART_EXTENSION_NAME;
	}

	public boolean matchView( AggregationCellHandle cell )
	{
		return getChartHandle( cell ) != null;
	}

	public void switchView( AggregationCellHandle cell )
	{
		try
		{
			// Get the measure binding name and drop the DataItemHandle
			String exprMeasure = null;
			Object content = getFirstContent( cell );
			if ( content instanceof DataItemHandle )
			{
				DataItemHandle dataItemHandle = (DataItemHandle) content;
				exprMeasure = dataItemHandle.getResultSetColumn( );
			}
			if ( content instanceof DesignElementHandle )
			{
				( (DesignElementHandle) content ).dropAndClear( );
			}

			// Get the row dimension binding name
			String exprDimRow = null;
			content = getFirstContent( getLevelCell( cell.getCrosstab( )
					.getCrosstabView( ICrosstabConstants.ROW_AXIS_TYPE ) ) );
			if ( content instanceof DataItemHandle )
			{
				DataItemHandle dataItemHandle = (DataItemHandle) content;
				exprDimRow = dataItemHandle.getResultSetColumn( );
			}

			// Get the column dimension binding name
			String exprDimColumn = null;
			content = getFirstContent( getLevelCell( cell.getCrosstab( )
					.getCrosstabView( ICrosstabConstants.COLUMN_AXIS_TYPE ) ) );
			if ( content instanceof DataItemHandle )
			{
				DataItemHandle dataItemHandle = (DataItemHandle) content;
				exprDimColumn = dataItemHandle.getResultSetColumn( );
			}

			// Create the ExtendedItemHandle with default chart model
			ExtendedItemHandle chartHandle = cell.getCrosstabHandle( )
					.getElementFactory( )
					.newExtendedItem( null,
							ChartReportItemConstants.CHART_EXTENSION_NAME );
			ChartReportItemImpl reportItem = (ChartReportItemImpl) chartHandle.getReportItem( );
			ChartWithAxes cm = createDefaultChart( exprMeasure, new String[]{
					exprDimRow, exprDimColumn
			} );
			reportItem.setModel( cm );
			cell.addContent( chartHandle, 0 );

			// Set span and add axis cell
			if ( cm.isTransposed( ) )
			{
				cell.setSpanOverOnRow( cell.getAggregationOnRow( ) );
				CrosstabCellHandle rowCell = cell.getCrosstab( )
						.getDimension( ICrosstabConstants.ROW_AXIS_TYPE, 0 )
						.getLevel( 0 )
						.getCell( );
				if ( rowCell.getHeight( ) == null
						|| rowCell.getHeight( ).getMeasure( ) == 0 )
				{
					// Set a default height for cell to fit with chart
					cell.getCrosstab( ).setRowHeight( rowCell,
							DEFAULT_ROW_WIDTH );
				}
				rowCell.getCrosstabHandle( )
						.setProperty( StyleHandle.PADDING_TOP_PROP,
								new DimensionValue( 0,
										DesignChoiceConstants.UNITS_PT ) );
				rowCell.getCrosstabHandle( )
						.setProperty( StyleHandle.PADDING_BOTTOM_PROP,
								new DimensionValue( 0,
										DesignChoiceConstants.UNITS_PT ) );
				updateXtabForAxisChart( cell,
						chartHandle,
						ICrosstabConstants.ROW_AXIS_TYPE );
			}
			else
			{
				cell.setSpanOverOnColumn( cell.getAggregationOnColumn( ) );
				CrosstabCellHandle columnCell = cell.getCrosstab( )
						.getDimension( ICrosstabConstants.COLUMN_AXIS_TYPE, 0 )
						.getLevel( 0 )
						.getCell( );
				if ( columnCell.getWidth( ) != null
						|| columnCell.getWidth( ).getMeasure( ) == 0 )
				{
					// Set a default width for cell to fit with chart
					cell.getCrosstab( ).setColumnWidth( columnCell,
							DEFAULT_COLUMN_WIDTH );
				}
				columnCell.getCrosstabHandle( )
						.setProperty( StyleHandle.PADDING_LEFT_PROP,
								new DimensionValue( 0,
										DesignChoiceConstants.UNITS_PT ) );
				columnCell.getCrosstabHandle( )
						.setProperty( StyleHandle.PADDING_RIGHT_PROP,
								new DimensionValue( 0,
										DesignChoiceConstants.UNITS_PT ) );
				updateXtabForAxisChart( cell,
						chartHandle,
						ICrosstabConstants.COLUMN_AXIS_TYPE );
			}
		}
		catch ( BirtException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	private void updateXtabForAxisChart( AggregationCellHandle cell,
			ExtendedItemHandle chartHandle, int axisType ) throws BirtException
	{
		if ( cell.getCrosstab( ).getGrandTotal( axisType ) == null )
		{
			cell.getCrosstab( ).addGrandTotal( axisType );
		}
		ExtendedItemHandle axisChartHandle = cell.getCrosstabHandle( )
				.getElementFactory( )
				.newExtendedItem( null,
						ChartReportItemConstants.CHART_EXTENSION_NAME );
		axisChartHandle.setProperty( ChartReportItemConstants.PROPERTY_HOST_CHART,
				chartHandle );

		int otherAxisType = axisType == ICrosstabConstants.ROW_AXIS_TYPE
				? ICrosstabConstants.COLUMN_AXIS_TYPE
				: ICrosstabConstants.ROW_AXIS_TYPE;
		CrosstabCellHandle grandTotalAggCell = ( (MeasureViewHandle) cell.getContainer( ) ).getAggregationCell( cell.getDimensionName( otherAxisType ),
				cell.getLevelName( otherAxisType ),
				null,
				null );
		( (DesignElementHandle) getFirstContent( grandTotalAggCell ) ).dropAndClear( );
		grandTotalAggCell.addContent( axisChartHandle );
	}

	public void restoreView( AggregationCellHandle cell )
	{
		try
		{
			Chart cm = ChartReportItemUtil.getChartFromHandle( getChartHandle( cell ) );
			ChartXTabUtil.removeAxisChartInXTab( cm, cell.getCrosstab( ) );
		}
		catch ( BirtException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	private ChartWithAxes createDefaultChart( String exprMeasure,
			String[] exprDimensions )
	{
		ChartWithAxes cm = ChartWithAxesImpl.create( );
		cm.setType( "Bar Chart" );//$NON-NLS-1$
		cm.setSubType( "Side-by-side" );//$NON-NLS-1$
		cm.setUnits( "Points" ); //$NON-NLS-1$
		cm.setUnitSpacing( 50 );
		cm.getLegend( ).setVisible( false );
		cm.getTitle( ).setVisible( false );

		String exprCategory = null;
		if ( exprDimensions[1] != null )
		{
			exprCategory = exprDimensions[1];
		}
		else if ( exprDimensions[0] != null )
		{
			exprCategory = exprDimensions[0];
			// Transpose the chart to fit the direction
			cm.setTransposed( true );
		}

		// Add base series
		SeriesDefinition sdBase = SeriesDefinitionImpl.create( );
		sdBase.getSeriesPalette( ).shift( 0 );
		Series series = SeriesImpl.create( );
		sdBase.getSeries( ).add( series );
		cm.getBaseAxes( )[0].getSeriesDefinitions( ).add( sdBase );
		if ( exprCategory != null )
		{
			Query query = QueryImpl.create( ExpressionUtil.createJSDataExpression( exprCategory ) );
			series.getDataDefinition( ).add( query );
		}

		// Add orthogonal series
		SeriesDefinition sdOrth = SeriesDefinitionImpl.create( );
		sdOrth.getSeriesPalette( ).shift( 0 );
		series = BarSeriesImpl.create( );
		sdOrth.getSeries( ).add( series );
		cm.getOrthogonalAxes( cm.getBaseAxes( )[0], true )[0].getSeriesDefinitions( )
				.add( sdOrth );
		if ( exprMeasure != null )
		{
			Query query = QueryImpl.create( ExpressionUtil.createJSDataExpression( exprMeasure ) );
			series.getDataDefinition( ).add( query );
		}
		// if ( exprYGroup != null )
		// {
		// sdOrth.getQuery( )
		// .setDefinition( ExpressionUtil.createJSDataExpression( exprYGroup )
		// );
		// }

		// Add sample data
		SampleData sampleData = DataFactory.eINSTANCE.createSampleData( );
		sampleData.getBaseSampleData( ).clear( );
		sampleData.getOrthogonalSampleData( ).clear( );
		// Create Base Sample Data
		BaseSampleData sampleDataBase = DataFactory.eINSTANCE.createBaseSampleData( );
		sampleDataBase.setDataSetRepresentation( "A, B, C" ); //$NON-NLS-1$
		sampleData.getBaseSampleData( ).add( sampleDataBase );
		// Create Orthogonal Sample Data (with simulation count of 2)
		OrthogonalSampleData sampleDataOrth = DataFactory.eINSTANCE.createOrthogonalSampleData( );
		sampleDataOrth.setDataSetRepresentation( "5,4,12" ); //$NON-NLS-1$
		sampleDataOrth.setSeriesDefinitionIndex( 0 );
		sampleData.getOrthogonalSampleData( ).add( sampleDataOrth );
		cm.setSampleData( sampleData );

		return cm;
	}

	private Object getFirstContent( CrosstabCellHandle cell )
	{
		if ( cell != null )
		{
			List contents = cell.getContents( );
			if ( contents != null && contents.size( ) >= 1 )
			{
				return contents.get( 0 );
			}
		}
		return null;
	}

	private CrosstabCellHandle getLevelCell( CrosstabViewHandle xtab )
	{
		if ( xtab == null )
		{
			return null;
		}
		DimensionViewHandle dim = xtab.getDimension( 0 );
		if ( dim != null )
		{
			LevelViewHandle level = dim.getLevel( 0 );
			if ( level != null )
			{
				return level.getCell( );
			}
		}
		return null;
	}

	private ExtendedItemHandle getChartHandle( CrosstabCellHandle cell )
	{
		Object content = getFirstContent( cell );
		if ( content instanceof ExtendedItemHandle
				&& ChartReportItemConstants.CHART_EXTENSION_NAME.equals( ( (ExtendedItemHandle) content ).getExtensionName( ) ) )
		{
			return (ExtendedItemHandle) content;
		}
		return null;
	}
}
