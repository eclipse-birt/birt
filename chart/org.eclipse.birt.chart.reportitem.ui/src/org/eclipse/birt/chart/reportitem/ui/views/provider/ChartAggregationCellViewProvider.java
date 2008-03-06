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
import org.eclipse.birt.chart.reportitem.ui.ChartXTabUIUtil;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.IMeasureViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.ui.extension.AggregationCellViewAdapter;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * Provider for conversion between chart and text in cross tab
 */
public class ChartAggregationCellViewProvider
		extends
			AggregationCellViewAdapter
{

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
			ChartWithAxes cm = createDefaultChart( cell );

			// Get the measure binding expression and drop the DataItemHandle
			Object content = getFirstContent( cell );
			if ( content instanceof DesignElementHandle )
			{
				( (DesignElementHandle) content ).dropAndClear( );
			}

			// Create the ExtendedItemHandle with default chart model
			ExtendedItemHandle chartHandle = ChartXTabUIUtil.createChartHandle( cell.getModelHandle( ),
					ChartReportItemConstants.TYPE_PLOT_CHART,
					null );
			ChartReportItemImpl reportItem = (ChartReportItemImpl) chartHandle.getReportItem( );
			reportItem.setModel( cm );
			cell.addContent( chartHandle, 0 );

			// Set span and add axis cell
			ChartXTabUIUtil.addAxisChartInXTab( cell,
					cm.isTransposed( ),
					chartHandle );
		}
		catch ( BirtException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	public void restoreView( AggregationCellHandle cell )
	{
		try
		{
			// Set null size back
			CrosstabCellHandle levelCell = ChartXTabUtil.getInnermostLevelCell( cell.getCrosstab( ),
					ICrosstabConstants.ROW_AXIS_TYPE );
			if ( levelCell != null )
			{
				cell.getCrosstab( ).setRowHeight( levelCell, null );
			}
			levelCell = ChartXTabUtil.getInnermostLevelCell( cell.getCrosstab( ),
					ICrosstabConstants.COLUMN_AXIS_TYPE );
			if ( levelCell != null )
			{
				cell.getCrosstab( ).setColumnWidth( levelCell, null );
			}

			Chart cm = ChartReportItemUtil.getChartFromHandle( getChartHandle( cell ) );
			// Remove axis chart
			ChartXTabUIUtil.removeAxisChartInXTab( cell, cm );
			// Plot chart will be removed by designer itself
		}
		catch ( BirtException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	private ChartWithAxes createDefaultChart( AggregationCellHandle cell )
	{
		ChartWithAxes cm = ChartWithAxesImpl.create( );
		cm.setType( "Bar Chart" );//$NON-NLS-1$
		cm.setSubType( "Side-by-side" );//$NON-NLS-1$
		cm.setUnits( "Points" ); //$NON-NLS-1$
		cm.setUnitSpacing( 50 );
		cm.getLegend( ).setVisible( false );
		cm.getTitle( ).setVisible( false );

		String exprMeasure = ChartXTabUtil.generateComputedColumnName( cell );
		String exprCategory = null;

		// Compute the correct chart direction according to xtab
		if ( checkTransposed( cell ) )
		{
			cm.setTransposed( true );

			// Get the row dimension binding name as Category expression
			Object content = getFirstContent( ChartXTabUtil.getInnermostLevelCell( cell.getCrosstab( ),
					ICrosstabConstants.ROW_AXIS_TYPE ) );
			if ( content instanceof DataItemHandle )
			{
				DataItemHandle dataItemHandle = (DataItemHandle) content;
				exprCategory = dataItemHandle.getResultSetColumn( );
			}
		}
		else
		{
			// Get the column dimension binding name as Category expression
			Object content = getFirstContent( ChartXTabUtil.getInnermostLevelCell( cell.getCrosstab( ),
					ICrosstabConstants.COLUMN_AXIS_TYPE ) );
			if ( content instanceof DataItemHandle )
			{
				DataItemHandle dataItemHandle = (DataItemHandle) content;
				exprCategory = dataItemHandle.getResultSetColumn( );
			}
		}

		// Add base series
		SeriesDefinition sdBase = SeriesDefinitionImpl.create( );
		sdBase.getSeriesPalette( ).shift( 0 );
		Series series = SeriesImpl.create( );
		sdBase.getSeries( ).add( series );
		cm.getBaseAxes( )[0].setCategoryAxis( true );
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

	private void updateChartQueries( ChartWithAxes cm,
			AggregationCellHandle cell )
	{
		// Replace the query expression in chart model
		String exprMeasure = ChartXTabUtil.generateComputedColumnName( cell );
		String exprCategory = null;

		if ( cm.isTransposed( ) )
		{
			// Get the row dimension binding name as Category
			// expression
			Object content = getFirstContent( ChartXTabUtil.getInnermostLevelCell( cell.getCrosstab( ),
					ICrosstabConstants.ROW_AXIS_TYPE ) );
			if ( content instanceof DataItemHandle )
			{
				DataItemHandle dataItemHandle = (DataItemHandle) content;
				exprCategory = dataItemHandle.getResultSetColumn( );
			}
		}
		else
		{
			// Get the column dimension binding name as Category
			// expression
			Object content = getFirstContent( ChartXTabUtil.getInnermostLevelCell( cell.getCrosstab( ),
					ICrosstabConstants.COLUMN_AXIS_TYPE ) );
			if ( content instanceof DataItemHandle )
			{
				DataItemHandle dataItemHandle = (DataItemHandle) content;
				exprCategory = dataItemHandle.getResultSetColumn( );
			}
		}

		if ( exprCategory != null )
		{
			SeriesDefinition sdCategory = (SeriesDefinition) cm.getBaseAxes( )[0].getSeriesDefinitions( )
					.get( 0 );
			Query queryCategory = (Query) sdCategory.getDesignTimeSeries( )
					.getDataDefinition( )
					.get( 0 );
			queryCategory.setDefinition( ExpressionUtil.createJSDataExpression( exprCategory ) );
		}

		if ( exprMeasure != null )
		{
			SeriesDefinition sdValue = (SeriesDefinition) cm.getOrthogonalAxes( cm.getBaseAxes( )[0],
					true )[0].getSeriesDefinitions( ).get( 0 );
			Query queryValue = (Query) sdValue.getDesignTimeSeries( )
					.getDataDefinition( )
					.get( 0 );
			queryValue.setDefinition( ExpressionUtil.createJSDataExpression( exprMeasure ) );
		}
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

	private boolean checkTransposed( AggregationCellHandle cell )
	{
		if ( isDetailCell( cell ) )
		{
			// If no column area, transpose chart.
			if ( cell.getAggregationOnColumn( ) == null )
			{
				return true;
			}

			// If row total cell has chart, transpose the chart to keep the same
			// direction
			AggregationCellHandle rowTotalCell = ( (MeasureViewHandle) cell.getContainer( ) ).getAggregationCell( cell.getDimensionName( ICrosstabConstants.ROW_AXIS_TYPE ),
					cell.getLevelName( ICrosstabConstants.ROW_AXIS_TYPE ),
					null,
					null );
			Object content = ChartXTabUtil.getFirstContent( rowTotalCell );
			if ( ChartXTabUtil.isChartHandle( content )
					&& ChartXTabUtil.isPlotChart( (ExtendedItemHandle) content ) )
			{
				return true;
			}

			// If chart in measure cell, use the original direction
			content = ChartXTabUtil.getFirstContent( cell );
			if ( ChartXTabUtil.isChartHandle( content )
					&& ChartXTabUtil.isPlotChart( (ExtendedItemHandle) content ) )
			{
				return ( (ChartWithAxes) ChartXTabUtil.getChartFromHandle( (ExtendedItemHandle) content ) ).isTransposed( );
			}

			return false;
		}
		if ( isAggregationCell( cell ) )
		{
			// If aggregation is on row, i.e. to right side, transpose chart
			return cell.getAggregationOnRow( ) != null;
		}
		return false;
	}

	private boolean isDetailCell( AggregationCellHandle cell )
	{
		return IMeasureViewConstants.DETAIL_PROP.equals( cell.getModelHandle( )
				.getContainerPropertyHandle( )
				.getPropertyDefn( )
				.getName( ) );
	}

	private boolean isAggregationCell( AggregationCellHandle cell )
	{
		return IMeasureViewConstants.AGGREGATIONS_PROP.equals( cell.getModelHandle( )
				.getContainerPropertyHandle( )
				.getPropertyDefn( )
				.getName( ) );
	}

	public void updateView( AggregationCellHandle cell )
	{
		Object contentItem = ChartXTabUtil.getFirstContent( cell );

		// Test if it's plot chart handle
		if ( ChartReportItemUtil.isChartHandle( contentItem ) )
		{
			ExtendedItemHandle handle = (ExtendedItemHandle) contentItem;
			// Test if it's plot chart
			if ( ChartXTabUtil.isPlotChart( handle ) )
			{
				try
				{
					// Update plot chart
					// Reset query expressions
					ChartReportItemImpl reportItem = (ChartReportItemImpl) handle.getReportItem( );
					ChartWithAxes cm = (ChartWithAxes) reportItem.getProperty( ChartReportItemConstants.PROPERTY_CHART );
					if ( cm == null )
					{
						cm = createDefaultChart( cell );
					}
					else
					{
						updateChartQueries( cm, cell );
					}
					// Do not use setModel() to save cm since it has undo issue
					reportItem.setProperty( ChartReportItemConstants.PROPERTY_CHART,
							cm );

					// Reset cell span
					if ( cm.isTransposed( ) )
					{

						cell.setSpanOverOnRow( cell.getAggregationOnRow( ) );
					}
					else
					{
						cell.setSpanOverOnColumn( cell.getAggregationOnColumn( ) );
					}

					// Replace date item with axis chart
					ChartXTabUIUtil.updateAxisChart( cell,
							cm.isTransposed( ),
							handle );

				}
				catch ( BirtException e )
				{
					ExceptionHandler.handle( e );
				}
			}
		}
	}

	@Override
	public boolean canSwitch( AggregationCellHandle cell )
	{
		if ( cell == null )
		{
			// Do not allow switching to Chart when total cell is not created
			return false;
		}
		if ( cell.getAggregationOnRow( ) == null
				&& cell.getAggregationOnColumn( ) == null )
		{
			// Do not allow switching to Chart for no aggregation case
			return false;
		}
		return true;
	}
}
