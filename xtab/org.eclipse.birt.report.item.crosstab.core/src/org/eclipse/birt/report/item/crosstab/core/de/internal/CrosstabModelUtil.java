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

package org.eclipse.birt.report.item.crosstab.core.de.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.item.crosstab.core.IAggregationCellConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.core.ILevelViewConstants;
import org.eclipse.birt.report.item.crosstab.core.IMeasureViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.ComputedMeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.core.util.ICrosstabUpdateContext;
import org.eclipse.birt.report.item.crosstab.core.util.ICrosstabUpdateListener;
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * Provide all util methods for Model part of x-tab.
 */
public final class CrosstabModelUtil implements ICrosstabConstants
{

	private static AggregationManager manager;

	private static class DefaultCrosstabUpdateContext implements
			ICrosstabUpdateContext
	{

		public void performDefaultCreation( int type, Object model,
				Map<String, Object> extras ) throws SemanticException
		{
			// do nothing for now
		}

		public void performDefaultValidation( int type, Object model,
				Map<String, Object> extras ) throws SemanticException
		{
			if ( type == ICrosstabUpdateListener.MEASURE_DETAIL
					|| type == ICrosstabUpdateListener.MEASURE_AGGREGATION )
			{
				if ( !( model instanceof AggregationCellHandle ) )
				{
					return;
				}

				AggregationCellHandle cell = (AggregationCellHandle) model;

				MeasureViewHandle measureView = (MeasureViewHandle) cell.getContainer( );

				if ( !needUpdateMeasure( measureView ) )
				{
					// computed measure doesn't need update bindings and cell
					// content
					return;
				}

				CrosstabReportItemHandle crosstab = cell.getCrosstab( );

				LevelHandle rowLevel = cell.getAggregationOnRow( );
				LevelHandle colLevel = cell.getAggregationOnColumn( );

				String aggregateRowName = rowLevel == null ? null
						: rowLevel.getQualifiedName( );
				String aggregateColumnName = colLevel == null ? null
						: colLevel.getQualifiedName( );

				String function = null;
				if ( extras != null )
				{
					function = (String) extras.get( ICrosstabUpdateListener.EXTRA_FUNCTION_HINT );
				}

				// TODO the rowDimension/colDimension parameter is not really
				// needed here, so we pass null, but need check if future
				// implementation changes.
				addDataItem( crosstab,
						cell,
						measureView,
						function,
						null/* ! */,
						aggregateRowName,
						null/* ! */,
						aggregateColumnName );
			}
		}
	}

	private static ThreadLocal<ICrosstabModelListener> modelListener = new ThreadLocal<ICrosstabModelListener>( );

	public static void setCrosstabModelListener( ICrosstabModelListener listener )
	{
		if ( listener == null )
		{
			modelListener.remove( );
		}
		else
		{
			if ( listener instanceof ICrosstabUpdateListener )
			{
				( (ICrosstabUpdateListener) listener ).setContext( new DefaultCrosstabUpdateContext( ) );
			}
			modelListener.set( listener );
		}
	}

	public static ICrosstabModelListener getCrosstabModelListener( )
	{
		return modelListener.get( );
	}

	/**
	 * Notifies any creation event for crosstab model.
	 * 
	 * @param type
	 *            see <code>ICrosstabModelListener</code> for the type
	 *            constants.
	 * @param model
	 *            the model object associated with this event.
	 */
	public static void notifyCreation( int type, Object model,
			Map<String, Object> extras )
	{
		ICrosstabModelListener listener = modelListener.get( );
		if ( listener != null )
		{
			if ( listener instanceof ICrosstabUpdateListener )
			{
				( (ICrosstabUpdateListener) listener ).onCreated( type,
						model,
						extras );
			}
			else
			{
				listener.onCreated( type, model );
			}
		}
		else
		{
			try
			{
				// Perform default creation action
				new DefaultCrosstabUpdateContext( ).performDefaultCreation( type, model, extras );
			}
			catch ( SemanticException e )
			{}
		}
	}

	/**
	 * Notifies any validation event for crosstab model.
	 * 
	 * @param type
	 *            see <code>ICrosstabModelListener</code> for the type
	 *            constants.
	 * @param model
	 *            the model object associated with this event.
	 */
	public static void notifyValidate( int type, Object model,
			Map<String, Object> extras )
	{
		ICrosstabModelListener listener = modelListener.get( );
		if ( listener != null )
		{
			if ( listener instanceof ICrosstabUpdateListener )
			{
				( (ICrosstabUpdateListener) listener ).onValidate( type,
						model,
						extras );
			}
			else
			{
				listener.onValidate( type, model );
			}
		}
		else
		{
			try
			{
				// Perform default validation action
				new DefaultCrosstabUpdateContext( ).performDefaultValidation( type, model, extras );
			}
			catch ( SemanticException e )
			{}
		}
	}

	/**
	 * 
	 * @param elements
	 * @return
	 */
	public static List<IReportItem> getReportItems( List<?> elements )
	{
		if ( elements == null || elements.isEmpty( ) )
			return Collections.emptyList( );

		List<IReportItem> values = new ArrayList<IReportItem>( );
		for ( int i = 0; i < elements.size( ); i++ )
		{
			if ( elements.get( i ) instanceof DesignElementHandle )
				values.add( CrosstabUtil.getReportItem( (DesignElementHandle) elements.get( i ) ) );
		}
		return values;
	}

	/**
	 * Gets the opposite axis type for the given axis. If axis type is column,
	 * then return row; if axis type is row, then return column; otherwise
	 * return <code>ICrosstabConstants.NO_AXIS_TYPE</code>.
	 * 
	 * @param axisType
	 * @return
	 */
	public static int getOppositeAxisType( int axisType )
	{
		switch ( axisType )
		{
			case COLUMN_AXIS_TYPE :
				return ROW_AXIS_TYPE;
			case ROW_AXIS_TYPE :
				return COLUMN_AXIS_TYPE;
			default :
				return NO_AXIS_TYPE;
		}
	}

	/**
	 * Gets the index where this level resides in the whole crosstab.
	 * 
	 * @param levelView
	 * @return
	 */
	static int getTotalIndex( LevelViewHandle levelView )
	{
		if ( levelView == null )
			return -1;
		CrosstabReportItemHandle crosstab = levelView.getCrosstab( );
		if ( crosstab == null )
			return -1;

		int axisType = levelView.getAxisType( );
		int levelIndex = levelView.getIndex( );
		int dimensionIndex = ( (DimensionViewHandle) levelView.getContainer( ) ).getIndex( );

		int result = levelIndex;
		for ( int i = 0; i < dimensionIndex; i++ )
		{
			DimensionViewHandle dimension = crosstab.getDimension( axisType, i );
			result += dimension.getLevelCount( );
		}
		return result;

	}

	/**
	 * Justifies whether the given axis type is valid.
	 * 
	 * @param axisType
	 * @return true if axis type is valid, otherwise false
	 */
	public static boolean isValidAxisType( int axisType )
	{
		if ( axisType == COLUMN_AXIS_TYPE || axisType == ROW_AXIS_TYPE )
			return true;
		return false;
	}

	/**
	 * Gets the count of all the levels in all the dimension views at the given
	 * axis type.
	 * 
	 * @param crosstab
	 * @param axisType
	 * @return
	 */
	public static int getAllLevelCount( CrosstabReportItemHandle crosstab,
			int axisType )
	{
		if ( crosstab == null )
			return 0;
		int count = 0;
		for ( int i = 0; i < crosstab.getDimensionCount( axisType ); i++ )
		{
			DimensionViewHandle dimensionView = crosstab.getDimension( axisType,
					i );
			count += dimensionView.getLevelCount( );
		}

		return count;
	}

	/**
	 * Gets the preceding level in the crosstab.
	 * 
	 * @param levelView
	 *            the level view to search the preceding one
	 * @return the preceding leve for the given if found, otherwise null
	 */
	public static LevelViewHandle getPrecedingLevel( LevelViewHandle levelView )
	{
		if ( levelView == null )
			return null;

		// such the preceding one in the same dimension
		DimensionViewHandle dimensionView = (DimensionViewHandle) levelView.getContainer( );
		if ( dimensionView == null )
			return null;
		int index = levelView.getIndex( );
		if ( index - 1 >= 0 )
			return dimensionView.getLevel( index - 1 );

		// such the last one in the preceding dimension
		CrosstabViewHandle crosstabView = (CrosstabViewHandle) dimensionView.getContainer( );
		if ( crosstabView == null )
			return null;
		index = dimensionView.getIndex( );
		for ( int i = index - 1; i >= 0; i-- )
		{
			dimensionView = crosstabView.getDimension( i );
			int levelCount = dimensionView.getLevelCount( );
			if ( levelCount > 0 )
				return dimensionView.getLevel( levelCount - 1 );
		}

		return null;
	}

	/**
	 * Gets the innermost level view in the crosstab.
	 * 
	 * @param crosstab
	 * @param axisType
	 * @return
	 */
	public static LevelViewHandle getInnerMostLevel(
			CrosstabReportItemHandle crosstab, int axisType )
	{
		if ( crosstab == null )
			return null;

		for ( int dimensionIndex = crosstab.getDimensionCount( axisType ) - 1; dimensionIndex >= 0; dimensionIndex-- )
		{
			DimensionViewHandle dimensionView = crosstab.getDimension( axisType,
					dimensionIndex );

			int totalLevels = dimensionView.getLevelCount( );

			if ( totalLevels > 0 )
			{
				return dimensionView.getLevel( totalLevels - 1 );
			}
		}

		return null;
	}

	public static Iterator getBindingColumnIterator( DesignElementHandle handle )
	{
		if ( handle instanceof ReportItemHandle )
		{
			return ( (ReportItemHandle) handle ).columnBindingsIterator( );
		}
		return Collections.EMPTY_LIST.iterator( );
	}

	public static List getVisiableColumnBindingsList(
			DesignElementHandle handle, boolean includeSelf )
	{
		List bindingList = new ArrayList( );
		if ( includeSelf )
		{
			Iterator iterator = getBindingColumnIterator( handle );
			while ( iterator.hasNext( ) )
			{
				bindingList.add( iterator.next( ) );
			}
		}

		return bindingList;
	}

	public static ComputedColumnHandle getInputBinding( ReportItemHandle input,
			String bindingName )
	{
		List elementsList = getVisiableColumnBindingsList( input, true );
		if ( elementsList != null && elementsList.size( ) > 0 )
		{
			for ( int i = 0; i < elementsList.size( ); i++ )
			{
				if ( ( (ComputedColumnHandle) elementsList.get( i ) ).getName( )
						.equals( bindingName ) )
					return (ComputedColumnHandle) elementsList.get( i );
			}
		}
		return null;
	}
	
	public static void updateRPTMeasureAggregation(CrosstabReportItemHandle crosstab)
	{
		if (crosstab == null)
		{
			return;
		}
		int count = crosstab.getMeasureCount( );
		for (int i=0; i<count; i++)
		{
			MeasureViewHandle measureHandle = crosstab.getMeasure( i );
			
			if (measureHandle.getCell( ) != null)
			{
				List<DataItemHandle> items = getDataItems(measureHandle.getCell( ));
				for (int j=0; j<items.size( ); j++)
				{
					updateRPTAggregateOn( crosstab, items.get( j ) );
				}
			}
		}
	}

	private static List<DataItemHandle> getDataItems(AggregationCellHandle cell)
	{
		List<DataItemHandle> items = new ArrayList<DataItemHandle>();
		if (cell == null)
		{
			return items;
		}
		List list = cell.getContents( );
		for (int i=0; i<list.size( ); i++)
		{
			if (list.get( i ) instanceof DataItemHandle)
			{
				DataItemHandle handle = (DataItemHandle) list.get( i );
				String binding = handle.getResultSetColumn( );
				ComputedColumnHandle computedHnadle = getInputBinding( (ReportItemHandle) cell.getCrosstab( )
						.getModelHandle( ),
						binding );
				if ( computedHnadle != null
						&& computedHnadle.getTimeDimension( ) != null )
				{
					items.add( handle );
				}
			}
		}
		
		return items;
	}
	
	private static void updateRPTAggregateOn(CrosstabReportItemHandle crosstab, DataItemHandle dataHandle)
	{
		String binding = dataHandle.getResultSetColumn( );
		ComputedColumnHandle computedHnadle = getInputBinding( (ReportItemHandle)crosstab.getModelHandle( ),
				binding );
		
		try
		{
			computedHnadle.clearAggregateOnList( );
		
		
			LevelViewHandle levelHandle = getInnerMostLevel( crosstab, ICrosstabConstants.ROW_AXIS_TYPE );
			if (levelHandle != null)
			{
				computedHnadle.addAggregateOn( levelHandle.getCubeLevelName( ) );
			}
			
			levelHandle = getInnerMostLevel( crosstab, ICrosstabConstants.COLUMN_AXIS_TYPE );
			if (levelHandle != null)
			{
				computedHnadle.addAggregateOn( levelHandle.getCubeLevelName( ) );
			}
		}
		catch ( SemanticException e )
		{
			//do nothing now
		}
	}
	
	private static boolean needUpdateMeasure( MeasureViewHandle measureView )
	{
		if ( measureView == null )
		{
			return false;
		}
		if ( !( measureView instanceof ComputedMeasureViewHandle ) 
				|| CrosstabUtil.isLinkedDataModelMeasureView( measureView ) )
		{
			return true;
		}

		AggregationCellHandle cell = measureView.getCell( );
		List<DataItemHandle> items = getDataItems(cell);
		
		return items.size( ) > 0;
	}

	/**
	 * @param crosstab
	 * @param measureView
	 * @param function
	 * @param rowDimension
	 * @param rowLevel
	 * @param colDimension
	 * @param colLevel
	 * @throws SemanticException
	 * 
	 * 
	 */
	public static void addDataItem( CrosstabReportItemHandle crosstab,
			AggregationCellHandle cell, MeasureViewHandle measureView,
			String function, String rowDimension, String rowLevel,
			String colDimension, String colLevel ) throws SemanticException
	{
		if ( crosstab == null || !needUpdateMeasure( measureView ) )
		{
			return;
		}
		if ( measureView instanceof ComputedMeasureViewHandle
				&& !CrosstabUtil.isLinkedDataModelMeasureView( measureView ) )
		{
			List<DataItemHandle> items = getDataItems(cell);
			for (int i=0; i<items.size( ); i++)
			{
				updateRPTAggregateOn( crosstab, items.get( i ) );
			}
			return;
		}
		if ( cell == null )
		{
			// add a data-item to the measure aggregations
			cell = measureView.getAggregationCell( rowDimension,
					rowLevel,
					colDimension,
					colLevel );
		}

		if ( measureView instanceof ComputedMeasureViewHandle
				&& !CrosstabUtil.isLinkedDataModelMeasureView( measureView ) )
		{
			List<DataItemHandle> items = getDataItems(cell);
			for (int i=0; i<items.size( ); i++)
			{
				updateRPTAggregateOn( crosstab, items.get( i ) );
			}
			return;
		}

		if ( cell != null )
		{
			// create a computed column and set some properties
			String name = generateComputedColumnName( measureView,
					colLevel,
					rowLevel );
			ComputedColumn column = StructureFactory.newComputedColumn( crosstab.getModelHandle( ),
					name );
			String dataType = measureView.getDataType( );
			column.setDataType( dataType );
			
			String measureName = null;
			if( CrosstabUtil.isBoundToLinkedDataSet( crosstab ))
			{
				measureName = CrosstabUtil.getRefLinkedDataModelColumnName( measureView );
				if( measureName == null || measureName.isEmpty() )
				{
					// throw case
					return;
				}				
				column.setExpression( ExpressionUtil.createDataSetRowExpression( measureName ) );
			}
			else
			{
				measureName = measureView.getCubeMeasureName( );
				column.setExpression( ExpressionUtil.createJSMeasureExpression( measureName ) );
			}
			
			String defaultFunction = getDefaultMeasureAggregationFunction( measureView );
			// Count function should use integer data type
			if ( DesignChoiceConstants.MEASURE_FUNCTION_COUNT.equals( defaultFunction ) )
			{
				dataType = DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER;
				column.setDataType( dataType );
			}
			column.setAggregateFunction( function != null ? function
					: defaultFunction );

			// When the function is not null,set the column set the correct data
			// type
			if ( function != null && !function.equalsIgnoreCase( defaultFunction ) )
			{
				try
				{
					// reset the data type to default by the aggregatino
					// function

					IAggrFunction aggFunc = getAggregationManager( ).getAggregation( column.getAggregateFunction( ) );

					if ( aggFunc.getType( ) == IAggrFunction.RUNNING_AGGR )
					{
						// for running aggregation functions, it does not
						// support
						// direct calculation on measure, so we reset the func
						// to default func.
						column.setAggregateFunction( defaultFunction );
					}
					else
					{
						String targetType = DataAdapterUtil.adapterToModelDataType( aggFunc.getDataType( ) );

						if ( !DesignChoiceConstants.COLUMN_DATA_TYPE_ANY.equals( targetType ) )
						{
							column.setDataType( targetType );
						}
					}
				}
				catch ( BirtException e )
				{
					// do nothing;
				}
			}

			if ( rowLevel != null )
			{
				column.addAggregateOn( rowLevel );
			}

			if ( colLevel != null )
			{
				column.addAggregateOn( colLevel );
			}

			// add the computed column to crosstab
			ComputedColumnHandle columnHandle = generateAggregation( crosstab,
					cell,
					measureView,
					function,
					rowDimension,
					rowLevel,
					colDimension,
					colLevel );
			if( columnHandle == null )
			{
				return;
			}
			
			if ( cell.getContents( ).size( ) == 0 )
			{
				// set the data-item result set the the name of the column
				// handle
				DataItemHandle dataItem = crosstab.getModuleHandle( )
						.getElementFactory( )
						.newDataItem( null );
				dataItem.setResultSetColumn( columnHandle.getName( ) );
				cell.addContent( dataItem );
			}
			else if ( cell.getContents( ).size( ) == 1
					&& cell.getContents( ).get( 0 ) instanceof DataItemHandle )
			{
				DataItemHandle dataItem = (DataItemHandle) cell.getContents( )
						.get( 0 );
				dataItem.setResultSetColumn( columnHandle.getName( ) );
			}
			else
			{
				// try reset binding on first eligible data item
				// TODO should have better logic or move logic out
				for ( Object item : cell.getContents( ) )
				{
					if ( item instanceof DataItemHandle )
					{
						String bindingName = ( (DataItemHandle) item ).getResultSetColumn( );

						ComputedColumnHandle binding = ( (ReportItemHandle) crosstab.getModelHandle( ) ).findColumnBinding( bindingName );

						// TODO only update bindings already having a function.
						// this is still bad, logic should be moved outside
						// here.

						if ( binding != null
								&& binding.getAggregateFunction( ) != null )
						{
							try
							{
								IAggrFunction aggFunc = getAggregationManager( ).getAggregation( binding.getAggregateFunction( ) );

								// TODO we ignore any existing running type
								// aggregation binding here, logic need be
								// refined and moved out.
								if ( aggFunc.getType( ) != IAggrFunction.RUNNING_AGGR 
										&& isMeasureDataItem(crosstab, measureName, (DataItemHandle) item, binding) )
								{
									( (DataItemHandle) item ).setResultSetColumn( columnHandle.getName( ) );

									break;
								}
							}
							catch ( BirtException e )
							{
								// ignore
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Whether is measure data item.
	 * 
	 * @param crosstab
	 * @param measureName
	 * @param dataItemHandle
	 * @param crosstabBindingsCache
	 * @return
	 */
	public static boolean isMeasureDataItem( CrosstabReportItemHandle crosstab, String measureName,
			DataItemHandle dataItemHandle, ComputedColumnHandle binding )
	{
		if ( crosstab == null 
				|| dataItemHandle == null 
				|| measureName == null 
				|| binding == null )
		{
			return false;
		}

		String resultSetColumn = dataItemHandle.getResultSetColumn( );
		if ( resultSetColumn == null )
		{
			return false;
		}

		String measureBREExpr = "[" + escape(measureName) + "]";
		String measureJsExpr = ExpressionUtil
				.createJSMeasureExpression( measureName );		
		if ( CrosstabUtil.isBoundToLinkedDataSet( crosstab ) )
		{
			measureJsExpr = ExpressionUtil
					.createDataSetRowExpression( measureName );
		}		
		
		ExpressionHandle expr = getExpression( binding );		
		String exprStr = (expr != null) ? expr.getStringExpression( ) : null;
		if ( exprStr != null )
		{
			if( "javascript".equalsIgnoreCase( expr.getType( ) ) )
			{
				if( exprStr.contains( measureJsExpr ) )
				{
					return true;
				}
			}
			else
			{
				if( exprStr.contains( measureBREExpr ) )
				{
					return true;
				}
			}
		}

		return false;
	}
	
	private static String escape( String name )
	{
		// escape '
		name = name.replaceAll( "\\'", "''" );
		// escape [
		name = name.replaceAll( "\\[", "'['" );
		// escape ]
		name = name.replaceAll( "\\]", "']'" );
		// escape ?
		name = name.replaceAll( "\\?", "'?'" );
		return name;
	}

	private static String unescape( String name )
	{
		// unescape [
		name = name.replaceAll( "'\\['", "[" );
		// unescape ]
		name = name.replaceAll( "'\\]'", "]" );
		// unescape ?
		name = name.replaceAll( "\\'\\?\\'", "?" );
		// unescape '
		name = name.replaceAll( "\\'\\'", "'" );
		return name;
	}
	
	public static ExpressionHandle getExpression(
			ComputedColumnHandle columnBindingHandle )
	{
		if ( columnBindingHandle == null )
		{
			return null;
		}

		ExpressionHandle exprHandle = columnBindingHandle.getExpressionProperty( ComputedColumn.EXPRESSION_MEMBER );
		if ( exprHandle == null || exprHandle.getValue( ) == null )
		{
			Iterator<AggregationArgumentHandle> it = columnBindingHandle
					.argumentsIterator( );
			while ( it.hasNext( ) )
			{
				AggregationArgumentHandle ah = it.next( );
				if ( "Expression".equalsIgnoreCase( ah.getName( ) ) )
				{
					exprHandle = ah
							.getExpressionProperty( AggregationArgument.VALUE_MEMBER );
					break;
				}
			}
		}

		return exprHandle;
	}
	
	public static ComputedColumnHandle generateAggregation(
			CrosstabReportItemHandle crosstab, AggregationCellHandle cell,
			MeasureViewHandle measureView, String function,
			String rowDimension, String rowLevel, String colDimension,
			String colLevel ) throws SemanticException
	{
		// create a computed column and set some properties
		String name = generateComputedColumnName( measureView,
				colLevel,
				rowLevel );
		ComputedColumn column = StructureFactory.newComputedColumn( crosstab.getModelHandle( ),
				name );
		String dataType = measureView.getDataType( );
		column.setDataType( dataType );
		if( CrosstabUtil.isBoundToLinkedDataSet( crosstab ))
		{
			String dataField = CrosstabUtil.getRefLinkedDataModelColumnName( measureView );
			if( dataField == null || dataField.isEmpty() )
			{
				// throw case
				return null;
			}
			
			column.setExpression( ExpressionUtil.createDataSetRowExpression( dataField ) );
		}
		else
		{
			column.setExpression( ExpressionUtil.createJSMeasureExpression( measureView.getCubeMeasureName( ) ) );
		}
		
		String defaultFunction = getDefaultMeasureAggregationFunction( measureView );
		column.setAggregateFunction( function != null ? function
				: defaultFunction );
		// Count function should use integer data type
		if ( DesignChoiceConstants.MEASURE_FUNCTION_COUNT.equals( defaultFunction ) )
		{
			dataType = DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER;
			column.setDataType( dataType );
		}

		// When the function is not null,set the column set the correct data
		// type
		if ( function != null && !function.equalsIgnoreCase( defaultFunction ) )
		{
			try
			{
				// reset the data type to default by the aggregatino
				// function

				IAggrFunction aggFunc = getAggregationManager( ).getAggregation( column.getAggregateFunction( ) );

				if ( aggFunc.getType( ) == IAggrFunction.RUNNING_AGGR )
				{
					// for running aggregation functions, it does not
					// support
					// direct calculation on measure, so we reset the func
					// to default func.
					column.setAggregateFunction( defaultFunction );
				}
				else
				{
					String targetType = DataAdapterUtil.adapterToModelDataType( aggFunc.getDataType( ) );

					if ( !DesignChoiceConstants.COLUMN_DATA_TYPE_ANY.equals( targetType ) )
					{
						column.setDataType( targetType );
					}
				}
			}
			catch ( BirtException e )
			{
				// do nothing;
			}
		}

		if ( rowLevel != null )
		{
			column.addAggregateOn( rowLevel );
		}

		if ( colLevel != null )
		{
			column.addAggregateOn( colLevel );
		}

		return ( (ReportItemHandle) crosstab.getModelHandle( ) ).addColumnBinding( column,
				false );
	}

	static AggregationManager getAggregationManager( ) throws BirtException
	{
		// TODO do we need release this?

		if ( manager == null )
		{
			DataRequestSession session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION ) );
			manager = session.getAggregationManager( );
			session.shutdown( );
		}

		return manager;
	}

	/**
	 * Generates an meaningful and unique computed column name for a measure
	 * aggregation.
	 * 
	 * @param measureView
	 * @param aggregationOnColumn
	 * @param aggregationOnRow
	 * @return
	 */
	public static String generateComputedColumnName(
			MeasureViewHandle measureView, String aggregationOnColumn,
			String aggregationOnRow )
	{
		String name = ""; //$NON-NLS-1$
		String temp = measureView.getCubeMeasureName( );
		if ( temp != null && temp.length( ) > 0 )
		{
			name = name + temp;
		}
		
		if ( aggregationOnRow != null && aggregationOnRow.length( ) > 0 )
		{
			if ( name.length( ) > 0 )
			{
				name = name + "_" + aggregationOnRow; //$NON-NLS-1$
			}
			else
			{
				name = name + aggregationOnRow;
			}
		}
		if ( aggregationOnColumn != null && aggregationOnColumn.length( ) > 0 )
		{
			if ( name.length( ) > 0 )
			{
				name = name + "_" + aggregationOnColumn; //$NON-NLS-1$
			}
			else
			{
				name = name + aggregationOnColumn;
			}
		}
		if ( name.length( ) <= 0 )
		{
			name = "measure"; //$NON-NLS-1$
		}

		return name;
	}

	/**
	 * Returns the default aggregation function for specific measure view
	 */
	public static String getDefaultMeasureAggregationFunction(
			MeasureViewHandle mv )
	{		
		if ( mv != null && mv.getCubeMeasure( ) != null )
		{
			String func = null;
			if ( CrosstabUtil.isBoundToLinkedDataSet( mv.getCrosstab( ) ) )
			{				
				ComputedColumnHandle columnHandle = CrosstabUtil.getMeasureBindingColumnHandle( mv );
				if( columnHandle != null )
				{
					func = columnHandle.getAggregateFunction();
				}
				
				if( func == null )
				{
					if( !isNumeric( mv.getCubeMeasure( ).getDataType( ) ) )
					{
						func = DesignChoiceConstants.MEASURE_FUNCTION_COUNT;
					}	
					else
					{
						func = DEFAULT_MEASURE_FUNCTION;
					}	
					
				}
				
				return DataAdapterUtil.getRollUpAggregationName( func );
			}
			else
			{
				func = mv.getCubeMeasure( ).getFunction( );	
				if ( func != null )
				{
					return DataAdapterUtil.getRollUpAggregationName( func );
				}
			}
	
		}

		return DEFAULT_MEASURE_FUNCTION;
	}

	/**
	 * Whether it is numeric data type.
	 * @param dataType
	 * @return
	 */
	public static boolean isNumeric( String dataType )
	{
		return DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER.equals( dataType )
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT.equals( dataType )
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL.equals( dataType );
	}
	
	/**
	 * Gets the aggregation function for this cell.
	 * 
	 * @param crosstab
	 * @param cell
	 * @return
	 */
	public static String getAggregationFunction(
			CrosstabReportItemHandle crosstab, AggregationCellHandle cell )
	{
		assert crosstab != null;
		assert cell != null;
		assert cell.getCrosstab( ) == crosstab;

		ReportItemHandle crosstabModel = (ReportItemHandle) crosstab.getModelHandle( );
		List contents = cell.getContents( );
		for ( int index = 0; index < contents.size( ); index++ )
		{
			DesignElementHandle content = (DesignElementHandle) contents.get( index );
			if ( content instanceof DataItemHandle )
			{
				String columnName = ( (DataItemHandle) content ).getResultSetColumn( );
				ComputedColumnHandle columnHandle = crosstabModel.findColumnBinding( columnName );
				if ( columnHandle != null
						&& columnHandle.getAggregateFunction( ) != null )
					return columnHandle.getAggregateFunction( );
			}
		}
		return null;
	}

	/**
	 * Sets the aggregation function for this cell.
	 * 
	 * @param crosstab
	 * @param cell
	 * @param function
	 * @return
	 * @throws SemanticException
	 */
	public static void setAggregationFunction(
			CrosstabReportItemHandle crosstab, AggregationCellHandle cell,
			String function ) throws SemanticException
	{
		assert crosstab != null;
		assert cell != null;
		assert cell.getCrosstab( ) == crosstab;

		ReportItemHandle crosstabModel = (ReportItemHandle) crosstab.getModelHandle( );
		List contents = cell.getContents( );
		for ( int index = 0; index < contents.size( ); index++ )
		{
			DesignElementHandle content = (DesignElementHandle) contents.get( index );
			if ( content instanceof DataItemHandle )
			{
				String columnName = ( (DataItemHandle) content ).getResultSetColumn( );
				ComputedColumnHandle columnHandle = crosstabModel.findColumnBinding( columnName );

				// TODO only update bindings already having a function. this is
				// still bad, logic should be moved outside here.

				if ( columnHandle != null
						&& columnHandle.getAggregateFunction( ) != null )
				{
					columnHandle.setAggregateFunction( function );

					try
					{
						// reset the data type to default by the aggregatino
						// function

						// TODO these binding creation/modification logic should
						// be delegated to the caller context instead of
						// hard-coded here.

						String targetType = DataAdapterUtil.adapterToModelDataType( getAggregationManager( ).getAggregation( function )
								.getDataType( ) );

						if ( !DesignChoiceConstants.COLUMN_DATA_TYPE_ANY.equals( targetType ) )
						{
							columnHandle.setDataType( targetType );
						}
					}
					catch ( BirtException e )
					{
						// do nothing;
					}
				}
			}
		}
	}

	/**
	 * @return return the level view by an accumlated global index on given axis
	 */
	public static LevelViewHandle getLevel( CrosstabReportItemHandle crosstab,
			int overallIndex, int axisType )
	{
		int dimensionCount = crosstab.getDimensionCount( axisType );
		int totalLevels = 0;

		for ( int i = 0; i < dimensionCount; i++ )
		{
			DimensionViewHandle dimensionViewhandle = crosstab.getDimension( axisType,
					i );
			int levelViewHandleCount = dimensionViewhandle.getLevelCount( );

			if ( overallIndex < totalLevels + levelViewHandleCount )
			{
				return dimensionViewhandle.getLevel( overallIndex - totalLevels );
			}

			totalLevels += levelViewHandleCount;
		}

		return null;
	}

	/**
	 * Locates the cell which controls the column width for given cell
	 * 
	 * @param crosstabItem
	 * 
	 * @param cell
	 * @return
	 */
	public static CrosstabCellHandle locateColumnWidthCell(
			CrosstabReportItemHandle crosstabItem, CrosstabCellHandle cell )
	{
		if ( crosstabItem != null
				&& cell != null
				&& cell.getCrosstab( ) == crosstabItem )
		{
			// TODO valid source cell, rowSpan/columnSpan must be 1.

			boolean isMeasureHorizontal = MEASURE_DIRECTION_HORIZONTAL.equals( crosstabItem.getMeasureDirection( ) );

			if ( cell instanceof AggregationCellHandle )
			{
				AggregationCellHandle aggCell = (AggregationCellHandle) cell;

				MeasureViewHandle mv = null;

				if ( IMeasureViewConstants.DETAIL_PROP.equals( cell.getModelHandle( )
						.getContainerPropertyHandle( )
						.getPropertyDefn( )
						.getName( ) ) )
				{
					// for measure detail cell

					if ( isMeasureHorizontal )
					{
						// use detail cell from current measure
						return ( (MeasureViewHandle) cell.getContainer( ) ).getCell( );
					}
					else
					{
						// use first meausre detail cell
						return crosstabItem.getMeasure( 0 ).getCell( );
					}
				}

				if ( isMeasureHorizontal )
				{
					// for horizontal measure, use container measure
					mv = (MeasureViewHandle) aggCell.getContainer( );
				}
				else
				{
					// else use first measure
					mv = crosstabItem.getMeasure( 0 );
				}

				String colDimension = aggCell.getDimensionName( COLUMN_AXIS_TYPE );
				String colLevel = aggCell.getLevelName( COLUMN_AXIS_TYPE );

				LevelViewHandle colLevelHandle = getInnerMostLevel( crosstabItem,
						COLUMN_AXIS_TYPE );

				if ( colLevelHandle == null )
				{
					// empty column area, use measure detail cell directly
					return mv.getCell( );
				}

				DimensionViewHandle colDimHandle = (DimensionViewHandle) colLevelHandle.getContainer( );

				if ( colLevelHandle.getCubeLevelName( ).equals( colLevel )
						&& colDimHandle.getCubeDimensionName( )
								.equals( colDimension ) )
				{
					// aggregation on innerest column level
					return mv.getCell( );
				}

				String rowDimension = null;
				String rowLevel = null;

				LevelViewHandle rowLevelHandle = getInnerMostLevel( crosstabItem,
						ROW_AXIS_TYPE );
				if ( rowLevelHandle != null )
				{
					rowDimension = ( (DimensionViewHandle) rowLevelHandle.getContainer( ) ).getCubeDimensionName( );
					rowLevel = rowLevelHandle.getCubeLevelName( );
				}

				// return selected aggregation cell on measure
				return mv.getAggregationCell( rowDimension,
						rowLevel,
						colDimension,
						colLevel );
			}
			else if ( cell.getContainer( ) instanceof MeasureViewHandle )
			{
				if ( isMeasureHorizontal )
				{
					MeasureViewHandle mv = (MeasureViewHandle) cell.getContainer( );

					// must be measure header
					int headerPos = cell.getModelHandle( ).getIndex( );

					if ( headerPos > 0 )
					{
						// this header is for subtotal or grandtotal, try find
						// the corresponding aggregations cell
						List<LevelViewHandle> levels = CrosstabModelUtil.getAllAggregationLevels( crosstabItem,
								COLUMN_AXIS_TYPE );

						// we need the reversed order here to count from inner
						// most to outer most
						Collections.reverse( levels );

						String rowDimension = null;
						String rowLevel = null;

						LevelViewHandle rowLevelHandle = getInnerMostLevel( crosstabItem,
								ROW_AXIS_TYPE );
						if ( rowLevelHandle != null )
						{
							rowDimension = ( (DimensionViewHandle) rowLevelHandle.getContainer( ) ).getCubeDimensionName( );
							rowLevel = rowLevelHandle.getCubeLevelName( );
						}

						int realIndex = 1;

						// try match it for subtotal first
						for ( int i = 1; i < levels.size( ); i++ )
						{
							LevelViewHandle lv = levels.get( i );

							if ( CrosstabModelUtil.isAggregationOn( mv,
									lv.getCubeLevelName( ),
									COLUMN_AXIS_TYPE ) )
							{
								// find the real header index
								if ( headerPos == realIndex )
								{
									String colDimension = ( (DimensionViewHandle) lv.getContainer( ) ).getCubeDimensionName( );
									String colLevel = lv.getCubeLevelName( );

									// return selected aggregation cell on
									// measure
									return mv.getAggregationCell( rowDimension,
											rowLevel,
											colDimension,
											colLevel );
								}

								realIndex++;
							}
						}

						// now it must be measure header for grandtotal

						// return selected aggregation cell on
						// measure
						return mv.getAggregationCell( rowDimension,
								rowLevel,
								null,
								null );
					}

					// use detail cell from current measure
					return mv.getCell( );
				}
				else if ( IMeasureViewConstants.HEADER_PROP.equals( cell.getModelHandle( )
						.getContainerPropertyHandle( )
						.getPropertyDefn( )
						.getName( ) ) )
				{
					// in vertical case, use the first available measrue header
					// cell
					for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
					{
						MeasureViewHandle mv = crosstabItem.getMeasure( i );
						if ( mv.getHeader( ) != null )
						{
							return mv.getHeader( );
						}
					}
				}
				else
				{
					// use first meausre detail cell
					return crosstabItem.getMeasure( 0 ).getCell( );
				}
			}
			else if ( cell.getContainer( ) instanceof LevelViewHandle )
			{
				LevelViewHandle lv = (LevelViewHandle) cell.getContainer( );

				boolean isRowLevl = ICrosstabReportItemConstants.ROWS_PROP.equals( lv.getContainer( )
						.getContainer( )
						.getModelHandle( )
						.getContainerPropertyHandle( )
						.getPropertyDefn( )
						.getName( ) );

				if ( isRowLevl )
				{
					if ( ILevelViewConstants.AGGREGATION_HEADER_PROP.equals( cell.getModelHandle( )
							.getContainerPropertyHandle( )
							.getPropertyDefn( )
							.getName( ) ) )
					{
						// use innerest row level cell
						return getInnerMostLevel( crosstabItem, ROW_AXIS_TYPE ).getCell( );
					}
					// use current level cell
					return cell;
				}

				// not row level
				if ( crosstabItem.getMeasureCount( ) == 0 )
				{
					if ( ILevelViewConstants.AGGREGATION_HEADER_PROP.equals( cell.getModelHandle( )
							.getContainerPropertyHandle( )
							.getPropertyDefn( )
							.getName( ) ) )
					{
						// for subtotal cell, return itself
						return cell;
					}
					// if no measure, for level detail and subtotal, use
					// inneerest level cell
					return getInnerMostLevel( crosstabItem, COLUMN_AXIS_TYPE ).getCell( );
				}
				if ( ILevelViewConstants.AGGREGATION_HEADER_PROP.equals( cell.getModelHandle( )
						.getContainerPropertyHandle( )
						.getPropertyDefn( )
						.getName( ) ) )
				{
					// user selected aggregation cell on first measure
					String rowDimension = null;
					String rowLevel = null;

					String colDimension = ( (DimensionViewHandle) lv.getContainer( ) ).getCubeDimensionName( );
					String colLevel = lv.getCubeLevelName( );

					LevelViewHandle rowLevelHandle = getInnerMostLevel( crosstabItem,
							ROW_AXIS_TYPE );
					if ( rowLevelHandle != null )
					{
						rowDimension = ( (DimensionViewHandle) rowLevelHandle.getContainer( ) ).getCubeDimensionName( );
						rowLevel = rowLevelHandle.getCubeLevelName( );
					}

					if ( isMeasureHorizontal )
					{
						// user first available aggregation cell
						for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
						{
							CrosstabCellHandle aggCell = crosstabItem.getMeasure( i )
									.getAggregationCell( rowDimension,
											rowLevel,
											colDimension,
											colLevel );

							if ( aggCell != null )
							{
								return aggCell;
							}
						}
					}
					else
					{
						// use selected aggregation cell on first measure
						return crosstabItem.getMeasure( 0 )
								.getAggregationCell( rowDimension,
										rowLevel,
										colDimension,
										colLevel );
					}
				}
				// user first measure detail cell
				return crosstabItem.getMeasure( 0 ).getCell( );
			}
			else if ( cell.getContainer( ) instanceof CrosstabViewHandle )
			{

				boolean isRowGrandTotal = ICrosstabReportItemConstants.ROWS_PROP.equals( cell.getContainer( )
						.getModelHandle( )
						.getContainerPropertyHandle( )
						.getPropertyDefn( )
						.getName( ) );

				if ( isRowGrandTotal )
				{
					LevelViewHandle rowLevelHandle = getInnerMostLevel( crosstabItem,
							ROW_AXIS_TYPE );
					if ( rowLevelHandle != null )
					{
						// use innerest row level cell
						return rowLevelHandle.getCell( );
					}
					// user itself
					return cell;
				}

				if ( crosstabItem.getMeasureCount( ) == 0 )
				{
					// if no measure, for grand total cell, use itself
					return cell;
				}
				// user selected aggregation cell on first measure
				String rowDimension = null;
				String rowLevel = null;

				LevelViewHandle rowLevelHandle = getInnerMostLevel( crosstabItem,
						ROW_AXIS_TYPE );
				if ( rowLevelHandle != null )
				{
					rowDimension = ( (DimensionViewHandle) rowLevelHandle.getContainer( ) ).getCubeDimensionName( );
					rowLevel = rowLevelHandle.getCubeLevelName( );
				}

				if ( isMeasureHorizontal )
				{
					// user first available aggregation cell
					for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
					{
						CrosstabCellHandle aggCell = crosstabItem.getMeasure( i )
								.getAggregationCell( rowDimension,
										rowLevel,
										null,
										null );

						if ( aggCell != null )
						{
							return aggCell;
						}
					}
				}
				else
				{
					// use selected aggregation cell on first measure
					return crosstabItem.getMeasure( 0 )
							.getAggregationCell( rowDimension,
									rowLevel,
									null,
									null );
				}
			}
			else if ( cell.getContainer( ) instanceof CrosstabReportItemHandle )
			{
				// crosstab header cell

				if ( crosstabItem.getHeaderCount( ) > 1 )
				{
					int levelCount = getAllLevelCount( crosstabItem,
							ROW_AXIS_TYPE );
					int scan = ( isMeasureHorizontal ? 0 : 1 ) + levelCount;

					if ( scan == 0 )
					{
						// blank row area, use first header cell
						return crosstabItem.getHeader( );
					}

					int idx = cell.getModelHandle( ).getIndex( );
					idx = idx % scan;

					if ( idx < levelCount )
					{
						// use relevant level cell.
						LevelViewHandle rowLevelHandle = getLevel( crosstabItem,
								idx,
								ROW_AXIS_TYPE );
						if ( rowLevelHandle != null )
						{
							// use innerest row level cell
							return rowLevelHandle.getCell( );
						}
					}
					else if ( !isMeasureHorizontal )
					{
						// use first available measrue header cell
						for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
						{
							MeasureViewHandle mv = crosstabItem.getMeasure( i );
							if ( mv.getHeader( ) != null )
							{
								return mv.getHeader( );
							}
						}
					}
				}
				else
				{
					LevelViewHandle rowLevelHandle = getInnerMostLevel( crosstabItem,
							ROW_AXIS_TYPE );
					if ( rowLevelHandle != null )
					{
						// use innerest row level cell
						return rowLevelHandle.getCell( );
					}

					if ( !isMeasureHorizontal )
					{
						// use first available measrue header cell
						for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
						{
							MeasureViewHandle mv = crosstabItem.getMeasure( i );
							if ( mv.getHeader( ) != null )
							{
								return mv.getHeader( );
							}
						}
					}

				}

				// user itself
				return cell;
			}
		}

		return null;
	}

	/**
	 * Locates the cell which controls the row height for given cell
	 * 
	 * @param crosstabItem
	 * 
	 * @param cell
	 * @return
	 */
	public static CrosstabCellHandle locateRowHeightCell(
			CrosstabReportItemHandle crosstabItem, CrosstabCellHandle cell )
	{
		if ( crosstabItem != null
				&& cell != null
				&& cell.getCrosstab( ) == crosstabItem )
		{
			// TODO valid source cell, rowSpan/columnSpan must be 1.

			boolean isMeasureHorizontal = MEASURE_DIRECTION_HORIZONTAL.equals( crosstabItem.getMeasureDirection( ) );

			if ( cell instanceof AggregationCellHandle )
			{
				AggregationCellHandle aggCell = (AggregationCellHandle) cell;

				MeasureViewHandle mv = null;

				if ( IMeasureViewConstants.DETAIL_PROP.equals( cell.getModelHandle( )
						.getContainerPropertyHandle( )
						.getPropertyDefn( )
						.getName( ) ) )
				{
					if ( !isMeasureHorizontal )
					{
						// use detail cell from current measure
						return ( (MeasureViewHandle) cell.getContainer( ) ).getCell( );
					}
					else
					{
						// use first meausre detail cell
						return crosstabItem.getMeasure( 0 ).getCell( );
					}
				}

				if ( !isMeasureHorizontal )
				{
					// for horizontal measure, use container measure
					mv = (MeasureViewHandle) aggCell.getContainer( );
				}
				else
				{
					// else use first measure
					mv = crosstabItem.getMeasure( 0 );
				}

				String rowDimension = aggCell.getDimensionName( ROW_AXIS_TYPE );
				String rowLevel = aggCell.getLevelName( ROW_AXIS_TYPE );

				LevelViewHandle rowLevelHandle = getInnerMostLevel( crosstabItem,
						ROW_AXIS_TYPE );

				if ( rowLevelHandle == null )
				{
					// empty row area, use measure detail cell directly
					return mv.getCell( );
				}

				DimensionViewHandle rowDimHandle = (DimensionViewHandle) rowLevelHandle.getContainer( );

				if ( rowLevelHandle.getCubeLevelName( ).equals( rowLevel )
						&& rowDimHandle.getCubeDimensionName( )
								.equals( rowDimension ) )
				{
					// aggregation on innerest column level
					return mv.getCell( );
				}

				String colDimension = null;
				String colLevel = null;

				LevelViewHandle colLevelHandle = getInnerMostLevel( crosstabItem,
						COLUMN_AXIS_TYPE );
				if ( colLevelHandle != null )
				{
					colDimension = ( (DimensionViewHandle) colLevelHandle.getContainer( ) ).getCubeDimensionName( );
					colLevel = colLevelHandle.getCubeLevelName( );
				}

				// return selected aggregation cell on measure
				return mv.getAggregationCell( rowDimension,
						rowLevel,
						colDimension,
						colLevel );
			}
			else if ( cell.getContainer( ) instanceof MeasureViewHandle )
			{
				if ( !isMeasureHorizontal )
				{
					MeasureViewHandle mv = (MeasureViewHandle) cell.getContainer( );

					// must be measure header
					int headerPos = cell.getModelHandle( ).getIndex( );

					if ( headerPos > 0 )
					{
						// this header is for subtotal or grandtotal, try find
						// the corresponding aggregations cell
						List<LevelViewHandle> levels = CrosstabModelUtil.getAllAggregationLevels( crosstabItem,
								ROW_AXIS_TYPE );

						// we need the reversed order here to count from inner
						// most to outer most
						Collections.reverse( levels );

						String colDimension = null;
						String colLevel = null;

						LevelViewHandle colLevelHandle = getInnerMostLevel( crosstabItem,
								COLUMN_AXIS_TYPE );
						if ( colLevelHandle != null )
						{
							colDimension = ( (DimensionViewHandle) colLevelHandle.getContainer( ) ).getCubeDimensionName( );
							colLevel = colLevelHandle.getCubeLevelName( );
						}

						int realIndex = 1;

						// try match it for subtotal first
						for ( int i = 1; i < levels.size( ); i++ )
						{
							LevelViewHandle lv = levels.get( i );

							if ( CrosstabModelUtil.isAggregationOn( mv,
									lv.getCubeLevelName( ),
									ROW_AXIS_TYPE ) )
							{
								// find the real header index
								if ( headerPos == realIndex )
								{
									String rowDimension = ( (DimensionViewHandle) lv.getContainer( ) ).getCubeDimensionName( );
									String rowLevel = lv.getCubeLevelName( );

									// return selected aggregation cell on
									// measure
									return mv.getAggregationCell( rowDimension,
											rowLevel,
											colDimension,
											colLevel );
								}

								realIndex++;
							}
						}

						// now it must be measure header for grandtotal

						// return selected aggregation cell on
						// measure
						return mv.getAggregationCell( null,
								null,
								colDimension,
								colLevel );
					}

					// use detail cell from current measure
					return mv.getCell( );
				}
				else if ( IMeasureViewConstants.HEADER_PROP.equals( cell.getModelHandle( )
						.getContainerPropertyHandle( )
						.getPropertyDefn( )
						.getName( ) ) )
				{
					// in horizontal case, use first available measrue header
					// cell
					for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
					{
						MeasureViewHandle mv = crosstabItem.getMeasure( i );
						if ( mv.getHeader( ) != null )
						{
							return mv.getHeader( );
						}
					}
				}
				else
				{
					// use first meausre detail cell
					return crosstabItem.getMeasure( 0 ).getCell( );
				}
			}
			else if ( cell.getContainer( ) instanceof LevelViewHandle )
			{
				LevelViewHandle lv = (LevelViewHandle) cell.getContainer( );

				boolean isRowLevl = ICrosstabReportItemConstants.ROWS_PROP.equals( lv.getContainer( )
						.getContainer( )
						.getModelHandle( )
						.getContainerPropertyHandle( )
						.getPropertyDefn( )
						.getName( ) );

				if ( !isRowLevl )
				{
					if ( ILevelViewConstants.AGGREGATION_HEADER_PROP.equals( cell.getModelHandle( )
							.getContainerPropertyHandle( )
							.getPropertyDefn( )
							.getName( ) ) )
					{
						// use innerest column level cell
						return getInnerMostLevel( crosstabItem,
								COLUMN_AXIS_TYPE ).getCell( );
					}
					// use current level cell
					return cell;
				}
				if ( crosstabItem.getMeasureCount( ) == 0 )
				{
					if ( ILevelViewConstants.AGGREGATION_HEADER_PROP.equals( cell.getModelHandle( )
							.getContainerPropertyHandle( )
							.getPropertyDefn( )
							.getName( ) ) )
					{
						// for subtotal cell, return itself
						return cell;
					}
					// if no measure, for level detail and subtotal, use
					// inneerest level cell
					return getInnerMostLevel( crosstabItem, ROW_AXIS_TYPE ).getCell( );
				}
				if ( ILevelViewConstants.AGGREGATION_HEADER_PROP.equals( cell.getModelHandle( )
						.getContainerPropertyHandle( )
						.getPropertyDefn( )
						.getName( ) ) )
				{
					// user selected aggregation cell on first measure
					String colDimension = null;
					String colLevel = null;

					String rowDimension = ( (DimensionViewHandle) lv.getContainer( ) ).getCubeDimensionName( );
					String rowLevel = lv.getCubeLevelName( );

					LevelViewHandle colLevelHandle = getInnerMostLevel( crosstabItem,
							COLUMN_AXIS_TYPE );
					if ( colLevelHandle != null )
					{
						colDimension = ( (DimensionViewHandle) colLevelHandle.getContainer( ) ).getCubeDimensionName( );
						colLevel = colLevelHandle.getCubeLevelName( );
					}

					if ( isMeasureHorizontal )
					{
						// use selected aggregation cell on first measure
						return crosstabItem.getMeasure( 0 )
								.getAggregationCell( rowDimension,
										rowLevel,
										colDimension,
										colLevel );
					}
					else
					{
						// user first available aggregation cell
						for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
						{
							CrosstabCellHandle aggCell = crosstabItem.getMeasure( i )
									.getAggregationCell( rowDimension,
											rowLevel,
											colDimension,
											colLevel );

							if ( aggCell != null )
							{
								return aggCell;
							}
						}
					}
				}
				// user first measure detail cell
				return crosstabItem.getMeasure( 0 ).getCell( );
			}
			else if ( cell.getContainer( ) instanceof CrosstabViewHandle )
			{

				boolean isRowGrandTotal = ICrosstabReportItemConstants.ROWS_PROP.equals( cell.getContainer( )
						.getModelHandle( )
						.getContainerPropertyHandle( )
						.getPropertyDefn( )
						.getName( ) );

				if ( !isRowGrandTotal )
				{
					LevelViewHandle colLevelHandle = getInnerMostLevel( crosstabItem,
							COLUMN_AXIS_TYPE );
					if ( colLevelHandle != null )
					{
						// use innerest column level cell
						return colLevelHandle.getCell( );
					}
					// user itself
					return cell;
				}
				if ( crosstabItem.getMeasureCount( ) == 0 )
				{
					// if no measure, for grand total cell, use itself
					return cell;
				}
				// user selected aggregation cell on first measure
				String colDimension = null;
				String colLevel = null;

				LevelViewHandle colLevelHandle = getInnerMostLevel( crosstabItem,
						COLUMN_AXIS_TYPE );
				if ( colLevelHandle != null )
				{
					colDimension = ( (DimensionViewHandle) colLevelHandle.getContainer( ) ).getCubeDimensionName( );
					colLevel = colLevelHandle.getCubeLevelName( );
				}

				if ( isMeasureHorizontal )
				{
					// use selected aggregation cell on first measure
					return crosstabItem.getMeasure( 0 )
							.getAggregationCell( null,
									null,
									colDimension,
									colLevel );
				}
				else
				{
					// user first available aggregation cell
					for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
					{
						CrosstabCellHandle aggCell = crosstabItem.getMeasure( i )
								.getAggregationCell( null,
										null,
										colDimension,
										colLevel );

						if ( aggCell != null )
						{
							return aggCell;
						}
					}
				}
			}
			else if ( cell.getContainer( ) instanceof CrosstabReportItemHandle )
			{
				// crosstab header cell

				if ( crosstabItem.getHeaderCount( ) > 1 )
				{
					int colLevelCount = getAllLevelCount( crosstabItem,
							COLUMN_AXIS_TYPE );

					if ( colLevelCount == 0 && !isMeasureHorizontal )
					{
						// blank column area, use first header cell
						return crosstabItem.getHeader( );
					}

					int rowLevelCount = getAllLevelCount( crosstabItem,
							ROW_AXIS_TYPE );
					int scan = ( isMeasureHorizontal ? 0 : 1 ) + rowLevelCount;

					if ( scan == 0 )
					{
						scan = 1;
					}

					int idx = cell.getModelHandle( ).getIndex( );
					idx = idx / scan;

					if ( idx < colLevelCount )
					{
						LevelViewHandle colLevelHandle = getLevel( crosstabItem,
								idx,
								COLUMN_AXIS_TYPE );
						if ( colLevelHandle != null )
						{
							// use innerest column level cell
							return colLevelHandle.getCell( );
						}
					}
					else if ( isMeasureHorizontal )
					{
						// use first available measrue header cell
						for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
						{
							MeasureViewHandle mv = crosstabItem.getMeasure( i );
							if ( mv.getHeader( ) != null )
							{
								return mv.getHeader( );
							}
						}
					}
				}
				else
				{
					LevelViewHandle colLevelHandle = getInnerMostLevel( crosstabItem,
							COLUMN_AXIS_TYPE );
					if ( colLevelHandle != null )
					{
						// use innerest column level cell
						return colLevelHandle.getCell( );
					}

					if ( isMeasureHorizontal )
					{
						// use first available measrue header cell
						for ( int i = 0; i < crosstabItem.getMeasureCount( ); i++ )
						{
							MeasureViewHandle mv = crosstabItem.getMeasure( i );
							if ( mv.getHeader( ) != null )
							{
								return mv.getHeader( );
							}
						}
					}
				}

				// use itself
				return cell;
			}

		}

		return null;
	}

	/**
	 * Gets the list of the level views that effects the aggregation cells.
	 * 
	 * @param crosstab
	 * @param axisType
	 * @return
	 */
	public static List<LevelViewHandle> getAllAggregationLevels(
			CrosstabReportItemHandle crosstab, int axisType )
	{
		List<LevelViewHandle> result = new ArrayList<LevelViewHandle>( );
		for ( int i = 0; i < crosstab.getDimensionCount( axisType ); i++ )
		{
			DimensionViewHandle dimensionView = crosstab.getDimension( axisType,
					i );
			for ( int j = 0; j < dimensionView.getLevelCount( ); j++ )
			{
				LevelViewHandle levelView = dimensionView.getLevel( j );

				// if the level view not specify the cube level, it is
				// meaningless to do anything
				if ( levelView.getCubeLevelName( ) == null )
					continue;

				if ( levelView.isInnerMost( )
						|| levelView.getAggregationHeader( ) != null )
				{
					result.add( levelView );
				}
			}
		}

		return result;
	}

	/**
	 * Gets the property name of the aggregation on property in AggregationCell
	 * by the axis type.
	 * 
	 * @param axisType
	 * @return
	 */
	public static String getAggregationOnPropName( int axisType )
	{
		if ( axisType == COLUMN_AXIS_TYPE )
			return IAggregationCellConstants.AGGREGATION_ON_COLUMN_PROP;
		if ( axisType == ROW_AXIS_TYPE )
			return IAggregationCellConstants.AGGREGATION_ON_ROW_PROP;
		return null;
	}

	/**
	 * Justifies whether the given measure is aggregated on the level view.
	 * 
	 * @param measureView
	 * @param levelName
	 * @param axisType
	 * @return
	 */
	public static boolean isAggregationOn( MeasureViewHandle measureView,
			String levelName, int axisType )
	{
		if ( measureView == null || !isValidAxisType( axisType ) )
			return false;

		String propName = getAggregationOnPropName( axisType );
		for ( int j = 0; j < measureView.getAggregationCount( ); j++ )
		{
			AggregationCellHandle cell = measureView.getAggregationCell( j );
			String aggregationOn = cell.getModelHandle( )
					.getStringProperty( propName );
			if ( ( levelName == null && aggregationOn == null )
					|| ( levelName != null && levelName.equals( aggregationOn ) ) )
				return true;
		}
		return false;
	}

	/**
	 * Computes the total measure header cell count for a complete crosstab
	 * layout, this computation doesnt' consider the meausre header visibility
	 * setting.
	 * 
	 * @param measureView
	 * @return
	 */
	public static int computeAllMeasureHeaderCount(
			CrosstabReportItemHandle crosstab, MeasureViewHandle measureView )
	{
		if ( crosstab == null || measureView == null )
		{
			return 0;
		}

		if ( measureView instanceof ComputedMeasureViewHandle 
				&& !CrosstabUtil.isLinkedDataModelMeasureView( measureView ) )
		{
			// currently computed measure do not support subtotal or grandtotal,
			// so it can only have one header.
			return 1;
		}

		int targetAxis = MEASURE_DIRECTION_VERTICAL.equals( crosstab.getMeasureDirection( ) ) ? ROW_AXIS_TYPE
				: COLUMN_AXIS_TYPE;

		List<LevelViewHandle> levels = getAllAggregationLevels( crosstab,
				targetAxis );

		if ( levels == null || levels.size( ) == 0 )
		{
			// the target axis is empty, still return 1
			return 1;
		}

		int count = 0;

		LevelViewHandle innerMost = getInnerMostLevel( crosstab, targetAxis );

		// check subtotal and inner most
		for ( int i = 0; i < levels.size( ); i++ )
		{
			LevelViewHandle lv = levels.get( i );

			if ( lv == innerMost
					|| isAggregationOn( measureView,
							lv.getCubeLevelName( ),
							targetAxis ) )
			{
				count++;
			}
		}

		// check grandtotal
		if ( isAggregationOn( measureView, null, targetAxis ) )
		{
			count++;
		}

		assert count > 0;

		return count;
	}

	// add support multiple header
	/**
	 * Validate the crosstab header cell
	 * 
	 * @param crosstab
	 */
	public static void validateCrosstabHeader( CrosstabReportItemHandle crosstab )
	{
		int headerCellCount = crosstab.getHeaderCount( );
		List<LevelViewHandle> columnLevelList = getLevelList( crosstab,
				ICrosstabConstants.COLUMN_AXIS_TYPE );
		List<LevelViewHandle> rowLevelList = getLevelList( crosstab,
				ICrosstabConstants.ROW_AXIS_TYPE );
		PropertyHandle headerHandle = crosstab.getModelHandle( )
				.getPropertyHandle( ICrosstabReportItemConstants.HEADER_PROP );
		int rowSize = rowLevelList.size( );
		int columnSize = columnLevelList.size( );

		if ( !crosstab.isHideMeasureHeader( ) )
		{
			if ( ICrosstabConstants.MEASURE_DIRECTION_VERTICAL.equals( crosstab.getMeasureDirection( ) ) )
			{
				rowSize = rowSize + 1;
			}
			else
			{
				columnSize = columnSize + 1;
			}
		}
		int total = rowSize * columnSize - headerCellCount;
		if ( rowLevelList.size( ) == 0 )
		{
			return;
		}
		else if ( columnLevelList.size( ) == 0 && rowLevelList.size( ) > 0 )
		{
			if ( headerCellCount < rowSize )
			{
				for ( int i = 0; i < rowSize - headerCellCount; i++ )
				{
					ExtendedItemHandle cellHandle = null;
					try
					{
						cellHandle = CrosstabExtendedItemFactory.createCrosstabCell( crosstab.getModuleHandle( ) );
						headerHandle.add( cellHandle );
					}
					catch ( SemanticException e )
					{
						// do nothing
						continue;
					}
				}
			}

			return;
		}

		for ( int i = 0; i < total; i++ )
		{
			ExtendedItemHandle cellHandle = null;
			try
			{
				cellHandle = CrosstabExtendedItemFactory.createCrosstabCell( crosstab.getModuleHandle( ) );
				headerHandle.add( cellHandle );
			}
			catch ( SemanticException e )
			{
				// do nothing
				continue;
			}

		}
	}

	public static List<LevelViewHandle> getLevelList(
			CrosstabReportItemHandle crosstab, int axisType )
	{
		List retValue = new ArrayList( );
		int dimensionCount = crosstab.getDimensionCount( axisType );
		for ( int i = 0; i < dimensionCount; i++ )
		{
			DimensionViewHandle dimensionViewhandle = crosstab.getDimension( axisType,
					i );
			int leveViewHandleCount = dimensionViewhandle.getLevelCount( );
			for ( int j = 0; j < leveViewHandleCount; j++ )
			{
				retValue.add( dimensionViewhandle.getLevel( j ) );
			}
		}
		return retValue;
	}

	// pos < 0 means , is n't add or remove lever cause the update header cell,
	// (hide measer header cell)
	/**
	 * Update the header cell
	 * 
	 * @param crosstab
	 * @param pos
	 * @param axisType
	 */
	public static void updateHeaderCell( CrosstabReportItemHandle crosstab,
			int pos, int axisType )
	{
		updateHeaderCell( crosstab, pos, axisType, false, 0 );
	}

	public static void updateHeaderCell( CrosstabReportItemHandle crosstab,
			int pos, int axisType, boolean removeLevel )
	{
		updateHeaderCell( crosstab, pos, axisType, false, 0, removeLevel );
	}

	public static void updateHeaderCell( CrosstabReportItemHandle crosstab,
			int pos, int axisType, boolean isMoveDimension, int adjustCount )
	{
		updateHeaderCell( crosstab,
				pos,
				axisType,
				isMoveDimension,
				adjustCount,
				false );
	}

	public static void updateHeaderCell( CrosstabReportItemHandle crosstab,
			int pos, int axisType, boolean isMoveDimension, int adjustCount,
			boolean removeLevel )
	{

		HeaderData data = calcHeaderData( crosstab );
		List<LevelViewHandle> rowLevelList = getLevelList( crosstab,
				ICrosstabConstants.ROW_AXIS_TYPE );
		if ( isMoveDimension )
		{
			if ( ICrosstabConstants.COLUMN_AXIS_TYPE == axisType )
			{
				data.rowNumber = data.rowNumber - adjustCount;
			}
			else
			{
				data.columnNumber = data.columnNumber - adjustCount;
			}
		}

		int total = data.rowNumber * data.columnNumber;

		PropertyHandle headerHandle = crosstab.getModelHandle( )
				.getPropertyHandle( ICrosstabReportItemConstants.HEADER_PROP );
		// hide or show measere hider
		if ( pos == -1 )
		{
			if ( ICrosstabConstants.MEASURE_DIRECTION_VERTICAL.equals( crosstab.getMeasureDirection( ) ) )
			{
				pos = total - crosstab.getHeaderCount( ) > 0 ? data.columnNumber - 1
						: data.columnNumber;
				axisType = ICrosstabConstants.ROW_AXIS_TYPE;
			}
			else
			{
				pos = total - crosstab.getHeaderCount( ) > 0 ? data.rowNumber - 1
						: data.rowNumber;
				axisType = ICrosstabConstants.COLUMN_AXIS_TYPE;
			}
		}
		else if ( pos == -2
				&& !crosstab.isHideMeasureHeader( )
				&& crosstab.getMeasureCount( ) > 0 )// setHideMeasureHeader
		{
			if ( crosstab.getHeaderCount( ) <= 1 )
			{
				return;
			}
			if ( ICrosstabConstants.MEASURE_DIRECTION_VERTICAL.equals( crosstab.getMeasureDirection( ) ) )
			{

				if ( total - crosstab.getHeaderCount( ) != data.rowNumber )
				{
					for ( int i = 0; i < data.columnNumber - 1; i++ )
					{
						try
						{
							int delPos =  ( data.rowNumber - 1 ) * ( data.columnNumber - 1 );
							if( delPos < crosstab.getHeaderCount( ) )
							{
								headerHandle.removeItem( delPos );
							}
						}
						catch ( PropertyValueException e )
						{
							// do nothing now
						}
					}
				}
			}
			else
			{
				if ( total - crosstab.getHeaderCount( ) != data.columnNumber )
				{
					for ( int i = data.rowNumber - 2; i >= 0; i-- )
					{
						try
						{
							int delPos = data.columnNumber - 1;
							if ( i == data.rowNumber - 2 )
							{
								delPos = data.columnNumber;
							}
							
							delPos = i * ( data.columnNumber + 1 ) + delPos;
							if( delPos < crosstab.getHeaderCount( ) )
							{
								headerHandle.removeItem( delPos );
							}
						}
						catch ( PropertyValueException e )
						{
							// do nothing now
						}
					}
				}
			}
			if ( ICrosstabConstants.MEASURE_DIRECTION_VERTICAL.equals( crosstab.getMeasureDirection( ) ) )
			{
				pos = data.columnNumber - 1;
				axisType = ICrosstabConstants.ROW_AXIS_TYPE;
			}
			else
			{
				pos = data.rowNumber - 1;
				axisType = ICrosstabConstants.COLUMN_AXIS_TYPE;
			}
		}

		boolean isAdd = total - crosstab.getHeaderCount( ) > 0;
		if ( !isMoveDimension
				&& !needUpdateHeaderCell( crosstab,
						removeLevel ? false : isAdd,
						axisType ) )
		{
			return;
		}

		if ( total == crosstab.getHeaderCount( ) )
		{
			return;
		}

		if ( ICrosstabConstants.COLUMN_AXIS_TYPE == axisType )
		{
			for ( int i = 0; i < data.columnNumber; i++ )
			{
				try
				{
					if ( isAdd )
					{
						int insertRow = pos;
						if ( insertRow == data.rowNumber - 1
								&& rowLevelList.size( ) != 0 )
						{
							insertRow = insertRow - 1;
						}
						ExtendedItemHandle cellHandle = CrosstabExtendedItemFactory.createCrosstabCell( crosstab.getModuleHandle( ) );

						headerHandle.add( cellHandle, insertRow
								* data.columnNumber
								+ i );
					}
					else
					{
						int delPos = ( pos == data.rowNumber ? pos - 1 : pos ) * data.columnNumber;
						if( delPos < crosstab.getHeaderCount( ) )
						{
							headerHandle.removeItem( delPos );
						}
					}
				}
				catch ( SemanticException e )
				{
					// do nothing now
				}
			}
		}
		else
		{
			for ( int i = data.rowNumber - 1; i >= 0; i-- )
			{
				try
				{
					if ( isAdd )
					{
						int insertColumn = pos;
						if ( pos == data.columnNumber - 1
								&& i != data.rowNumber - 1 )
						{
							insertColumn = pos - 1;
						}
						ExtendedItemHandle cellHandle = CrosstabExtendedItemFactory.createCrosstabCell( crosstab.getModuleHandle( ) );

						headerHandle.add( cellHandle, i
								* ( data.columnNumber - 1 )
								+ insertColumn );
					}
					else
					{
						int delPos = pos;
						if ( pos == data.columnNumber
								&& i != data.rowNumber - 1 )
						{
							delPos = pos - 1;
						}
						delPos = i * ( data.columnNumber + 1 ) + delPos;						
						if( delPos < crosstab.getHeaderCount( ) )
						{
							headerHandle.removeItem( delPos );
						}
					}
				}
				catch ( SemanticException e )
				{
					// do nothing now
				}
			}
		}
	}

	private static boolean needUpdateHeaderCell(
			CrosstabReportItemHandle crosstab, boolean isAdd, int axisType )
	{
		if ( crosstab.getHeaderCount( ) > 1 )
		{
			return true;
		}

		List<LevelViewHandle> columnLevelList = getLevelList( crosstab,
				ICrosstabConstants.COLUMN_AXIS_TYPE );
		List<LevelViewHandle> rowLevelList = getLevelList( crosstab,
				ICrosstabConstants.ROW_AXIS_TYPE );
		if ( columnLevelList.size( ) == 0 && rowLevelList.size( ) == 0 )
		{
			return true;
		}
		int value = isAdd ? 2 : 1;
		if ( columnLevelList.size( ) == 0 )
		{
			if ( rowLevelList.size( ) <= value )
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		if ( rowLevelList.size( ) == 0 )
		{
			if ( columnLevelList.size( ) <= value )
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		if ( isAdd )
		{
			if ( ICrosstabConstants.COLUMN_AXIS_TYPE == axisType
					&& ( columnLevelList.size( ) == value || columnLevelList.size( ) == 1 )
					&& rowLevelList.size( ) == 1 )
			{
				return true;
			}
			else if ( ICrosstabConstants.ROW_AXIS_TYPE == axisType
					&& columnLevelList.size( ) == 1
					&& ( rowLevelList.size( ) == value || rowLevelList.size( ) == 1 ) )
			{
				return true;
			}
		}
		return false;
	}

	public static int[] getHeaderRowAndColumnCount(
			CrosstabReportItemHandle crosstab )
	{
		HeaderData data = calcHeaderData( crosstab );
		return new int[]{
				data.rowNumber, data.columnNumber
		};
	}

	private static HeaderData calcHeaderData( CrosstabReportItemHandle crosstab )
	{
		HeaderData data = new HeaderData( );
		List<LevelViewHandle> columnLevelList = getLevelList( crosstab,
				ICrosstabConstants.COLUMN_AXIS_TYPE );
		List<LevelViewHandle> rowLevelList = getLevelList( crosstab,
				ICrosstabConstants.ROW_AXIS_TYPE );

		if ( columnLevelList.size( ) == 0 && rowLevelList.size( ) == 0 )
		{
			return data;
		}
		if ( columnLevelList.size( ) == 0 )
		{
			if ( needCrosstabHeaderCellForMeasureHeader( crosstab,
					ICrosstabConstants.ROW_AXIS_TYPE ) )
			{
				data.columnNumber = rowLevelList.size( ) + 1;
			}
			else
			{
				data.columnNumber = rowLevelList.size( );
			}

			return data;
		}

		if ( rowLevelList.size( ) == 0 )
		{
			if ( needCrosstabHeaderCellForMeasureHeader( crosstab,
					ICrosstabConstants.COLUMN_AXIS_TYPE ) )
			{
				data.rowNumber = columnLevelList.size( ) + 1;
			}
			else
			{
				data.rowNumber = columnLevelList.size( );
			}

			return data;
		}

		data.rowNumber = needCrosstabHeaderCellForMeasureHeader( crosstab,
				ICrosstabConstants.COLUMN_AXIS_TYPE ) ? columnLevelList.size( ) + 1
				: columnLevelList.size( );
		data.columnNumber = needCrosstabHeaderCellForMeasureHeader( crosstab,
				ICrosstabConstants.ROW_AXIS_TYPE ) ? rowLevelList.size( ) + 1
				: rowLevelList.size( );

		return data;
	}

	private static boolean needCrosstabHeaderCellForMeasureHeader(
			CrosstabReportItemHandle crosstab, int axisType )
	{
		if ( crosstab.isHideMeasureHeader( ) )
		{
			return false;
		}

		if ( crosstab.getMeasureCount( ) == 0 )
		{
			return false;
		}

		if ( ICrosstabConstants.MEASURE_DIRECTION_VERTICAL.equals( crosstab.getMeasureDirection( ) ) )
		{
			if ( ICrosstabConstants.ROW_AXIS_TYPE == axisType )
			{
				return true;
			}
		}
		else
		{
			if ( ICrosstabConstants.COLUMN_AXIS_TYPE == axisType )
			{
				return true;
			}
		}

		return false;
	}

	private static class HeaderData
	{

		// default value all 1
		public int rowNumber = 1;
		public int columnNumber = 1;
	}

	public static int findPriorLevelCount( DimensionViewHandle viewHandle )
	{
		int count = 0;
		CrosstabReportItemHandle crosstab = viewHandle.getCrosstab( );
		int dimensionCount = crosstab.getDimensionCount( viewHandle.getAxisType( ) );
		for ( int i = 0; i < dimensionCount; i++ )
		{
			DimensionViewHandle handle = crosstab.getDimension( viewHandle.getAxisType( ),
					i );
			if ( handle == viewHandle )
			{
				break;
			}
			count = count + handle.getLevelCount( );
		}
		return count;
	}
}
