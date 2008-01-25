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
import org.eclipse.birt.chart.model.ChartWithoutAxes;
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
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
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
			// Get the measure binding expression and drop the DataItemHandle
			Object content = getFirstContent( cell );
			String exprMeasure = getMeasureBindingExpr( content );
			if ( content instanceof DesignElementHandle )
			{
				( (DesignElementHandle) content ).dropAndClear( );
			}

			// Get the row dimension binding name
			String nameDimRow = null;
			content = getFirstContent( ChartXTabUtil.getInnermostLevelCell( cell.getCrosstab( ),
					ICrosstabConstants.ROW_AXIS_TYPE ) );
			if ( content instanceof DataItemHandle )
			{
				DataItemHandle dataItemHandle = (DataItemHandle) content;
				nameDimRow = dataItemHandle.getResultSetColumn( );
			}

			// Get the column dimension binding name
			String nameDimColumn = null;
			content = getFirstContent( ChartXTabUtil.getInnermostLevelCell( cell.getCrosstab( ),
					ICrosstabConstants.COLUMN_AXIS_TYPE ) );
			if ( content instanceof DataItemHandle )
			{
				DataItemHandle dataItemHandle = (DataItemHandle) content;
				nameDimColumn = dataItemHandle.getResultSetColumn( );
			}

			// Create the ExtendedItemHandle with default chart model
			String name = ReportPlugin.getDefault( ).getCustomName( ChartReportItemConstants.CHART_EXTENSION_NAME  );
			ExtendedItemHandle chartHandle = cell.getCrosstabHandle( )
					.getElementFactory( )
					.newExtendedItem( name,
							ChartReportItemConstants.CHART_EXTENSION_NAME );
			ChartReportItemImpl reportItem = (ChartReportItemImpl) chartHandle.getReportItem( );
			ChartWithAxes cm = createDefaultChart( exprMeasure, new String[]{
					nameDimRow, nameDimColumn
			} );
			reportItem.setModel( cm );
			cell.addContent( chartHandle, 0 );

			// Set span and add axis cell
			if ( cm.isTransposed( ) )
			{
				ChartXTabUIUtil.addAxisChartInXTab( cell,
						ICrosstabConstants.ROW_AXIS_TYPE,
						chartHandle );
			}
			else
			{
				ChartXTabUIUtil.addAxisChartInXTab( cell,
						ICrosstabConstants.COLUMN_AXIS_TYPE,
						chartHandle );
			}
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

	private ChartWithAxes createDefaultChart( String exprMeasure,
			String[] nameDimensions )
	{
		ChartWithAxes cm = ChartWithAxesImpl.create( );
		cm.setType( "Bar Chart" );//$NON-NLS-1$
		cm.setSubType( "Side-by-side" );//$NON-NLS-1$
		cm.setUnits( "Points" ); //$NON-NLS-1$
		cm.setUnitSpacing( 50 );
		cm.getLegend( ).setVisible( false );
		cm.getTitle( ).setVisible( false );

		String exprCategory = null;
		if ( nameDimensions[1] != null )
		{
			exprCategory = nameDimensions[1];
		}
		else if ( nameDimensions[0] != null )
		{
			exprCategory = nameDimensions[0];
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
			Query query = QueryImpl.create( exprMeasure );
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

	private String getMeasureBindingExpr( Object cellContent )
	{
		if ( cellContent instanceof DataItemHandle )
		{
			DataItemHandle dataItemHandle = (DataItemHandle) cellContent;
			return ExpressionUtil.createJSDataExpression( dataItemHandle.getResultSetColumn( ) );
		}
		if ( ChartReportItemUtil.isChartHandle( cellContent ) )
		{
			Chart cm = ChartReportItemUtil.getChartFromHandle( (ExtendedItemHandle) cellContent );
			SeriesDefinition sdValue;
			if ( cm instanceof ChartWithAxes )
			{
				sdValue = (SeriesDefinition) ( (ChartWithAxes) cm ).getOrthogonalAxes( ( (ChartWithAxes) cm ).getBaseAxes( )[0],
						true )[0].getSeriesDefinitions( ).get( 0 );
			}
			else
			{
				sdValue = (SeriesDefinition) ( (SeriesDefinition) ( (ChartWithoutAxes) cm ).getSeriesDefinitions( )
						.get( 0 ) ).getSeriesDefinitions( ).get( 0 );
			}
			Query query = (Query) sdValue.getDesignTimeSeries( )
					.getDataDefinition( )
					.get( 0 );
			return query.getDefinition( );
		}
		return null;
	}
}
