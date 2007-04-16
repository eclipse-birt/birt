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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.MessageConstants;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;

/**
 * 
 */
public class CrosstabReportItemTask extends AbstractCrosstabModelTask
{

	/**
	 * 
	 * @param focus
	 */
	public CrosstabReportItemTask( AbstractCrosstabItemHandle focus )
	{
		super( focus );
		this.crosstab = (CrosstabReportItemHandle) focus;
	}

	/**
	 * 
	 * @param axisType
	 * @param measureList
	 * @param functionList
	 * @return
	 * @throws SemanticException
	 */
	public CrosstabCellHandle addGrandTotal( int axisType, List measureList,
			List functionList ) throws SemanticException
	{
		if ( crosstab == null || !CrosstabModelUtil.isValidAxisType( axisType ) )
			return null;
		CrosstabViewHandle crosstabView = crosstab.getCrosstabView( axisType );
		if ( crosstabView == null )
		{
			CommandStack stack = crosstab.getCommandStack( );
			stack.startTrans( null );

			CrosstabCellHandle grandTotal;
			try
			{
				crosstabView = crosstab.addCrosstabView( axisType );
				grandTotal = crosstabView.addGrandTotal( measureList,
						functionList );
			}
			catch ( SemanticException e )
			{
				stack.rollback( );
				throw e;
			}

			stack.commit( );

			return grandTotal;
		}
		return crosstabView.addGrandTotal( measureList, functionList );
	}

	/**
	 * Removes row/column grand total from crosstab if it is not empty,
	 * otherwise do nothing. The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>.
	 * 
	 * @param axisType
	 */
	public void removeGrandTotal( int axisType )
	{
		CrosstabViewHandle crosstabView = crosstab.getCrosstabView( axisType );
		if ( crosstabView == null || crosstabView.getGrandTotal( ) == null )
		{
			crosstab.getLogger( ).log( Level.INFO,
					"row/column grand total is not set" ); //$NON-NLS-1$
			return;
		}

		crosstabView.removeGrandTotal( );
	}

	/**
	 * Gets the measure view list that define aggregations for the row/column
	 * grand total in the crosstab. Each item in the list is instance of
	 * <code>MeasureViewHandle</code>.
	 * 
	 * @param crosstab
	 * @param axisType
	 * @return
	 */
	public List getAggregationMeasures( int axisType )
	{
		// if crosstab is null or has no grand total, then return empty
		if ( crosstab == null || crosstab.getGrandTotal( axisType ) == null )
			return Collections.EMPTY_LIST;

		List measures = new ArrayList( );
		for ( int i = 0; i < crosstab.getMeasureCount( ); i++ )
		{
			MeasureViewHandle measureView = crosstab.getMeasure( i );
			if ( measures.contains( measureView ) )
				continue;
			if ( CrosstabModelUtil
					.isAggregationOn( measureView, null, axisType ) )
				measures.add( measureView );
		}
		return measures;
	}

	/**
	 * Gets the aggregation function for the row/column grand total in the
	 * crosstab.
	 * 
	 * @param crosstab
	 * @param axisType
	 * @param measureView
	 * @return
	 */
	public String getAggregationFunction( int axisType,
			MeasureViewHandle measureView )
	{
		// if crosstab is null or not define any grand total, then return null
		if ( crosstab == null || crosstab.getGrandTotal( axisType ) == null
				|| measureView == null || crosstab != measureView.getCrosstab( ) )
			return null;

		for ( int j = 0; j < measureView.getAggregationCount( ); j++ )
		{
			AggregationCellHandle cell = measureView.getAggregationCell( j );
			if ( ( axisType == COLUMN_AXIS_TYPE && cell
					.getAggregationOnColumn( ) == null )
					|| ( axisType == ROW_AXIS_TYPE && cell
							.getAggregationOnRow( ) == null ) )
			{
				String function = CrosstabModelUtil.getAggregationFunction(
						crosstab, cell );
				if ( function != null )
					return function;
			}
		}
		return null;
	}

	/**
	 * Gets the aggregation function for the row/column grand total in the
	 * crosstab.
	 * 
	 * @param crosstab
	 * @param axisType
	 * @param measureView
	 * @param function
	 * @return
	 * @throws SemanticException
	 */
	public void setAggregationFunction( int axisType,
			MeasureViewHandle measureView, String function )
			throws SemanticException
	{
		// if crosstab is null or not define any grand total, then return null
		if ( crosstab == null || crosstab.getGrandTotal( axisType ) == null
				|| measureView == null || crosstab != measureView.getCrosstab( ) )
			return;

		CommandStack stack = crosstab.getCommandStack( );
		stack.startTrans( null );

		try
		{
			for ( int j = 0; j < measureView.getAggregationCount( ); j++ )
			{
				AggregationCellHandle cell = measureView.getAggregationCell( j );
				if ( ( axisType == COLUMN_AXIS_TYPE && cell
						.getAggregationOnColumn( ) == null )
						|| ( axisType == ROW_AXIS_TYPE && cell
								.getAggregationOnRow( ) == null ) )
				{
					CrosstabModelUtil.setAggregationFunction( crosstab, cell,
							function );
				}
			}
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );
	}

	/**
	 * Moves the dimension view with the given name to the target index in the
	 * target row/column. The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>. And index is 0-based
	 * integer.
	 * 
	 * @param name
	 *            name of the dimension view to move
	 * @param targetAxisType
	 *            row/column axis type of the move target
	 * @param targetIndex
	 *            the position index of the move target
	 * @throws SemanticException
	 */
	public void pivotDimension( String name, int targetAxisType, int targetIndex )
			throws SemanticException
	{
		DimensionViewHandle dimensionView = crosstab.getDimension( name );
		if ( dimensionView == null )
		{
			crosstab.getLogger( ).log( Level.SEVERE,
					MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND,
					name );
			throw new CrosstabException( crosstab.getModelHandle( )
					.getElement( ), new String[]{name,
					crosstab.getModelHandle( ).getElement( ).getIdentifier( )},
					MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND );
		}
		moveDimension( dimensionView, targetAxisType, targetIndex );
	}

	/**
	 * Moves the dimension view with the given name to the target index in the
	 * target row/column. The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>. And index is 0-based
	 * integer.
	 * 
	 * @param extendedItem
	 *            the dimension view extended item to move
	 * @param targetAxisType
	 *            row/column axis type of the move target
	 * @param targetIndex
	 *            the position index of the move target
	 * @throws SemanticException
	 */
	private void moveDimension( DimensionViewHandle dimensionView,
			int targetAxisType, int targetIndex ) throws SemanticException
	{
		assert dimensionView != null;

		Map functionListMap = new HashMap( );
		Map measureListMap = new HashMap( );
		for ( int i = 0; i < dimensionView.getLevelCount( ); i++ )
		{
			LevelViewHandle levelView = dimensionView.getLevel( i );
			String name = levelView.getCubeLevelName( );
			if ( name == null )
				continue;

			List measureList = levelView.getAggregationMeasures( );
			List functionList = new ArrayList( );
			for ( int j = 0; j < measureList.size( ); j++ )
			{
				MeasureViewHandle measureView = (MeasureViewHandle) measureList
						.get( j );
				String function = levelView
						.getAggregationFunction( measureView );
				if ( function == null )
					functionList.add( "" ); //$NON-NLS-1$
				else
					functionList.add( function );
			}
			functionListMap.put( name, functionList );
			measureListMap.put( name, functionList );
		}

		DimensionViewHandle clonedDimensionView = (DimensionViewHandle) CrosstabUtil
				.getReportItem( dimensionView
						.getModelHandle( )
						.copy( )
						.getHandle( dimensionView.getModelHandle( ).getModule( ) ) );
		CommandStack stack = crosstab.getCommandStack( );
		stack.startTrans( null );

		try
		{
			CrosstabViewHandle crosstabView = (CrosstabViewHandle) dimensionView
					.getContainer( );
			new CrosstabViewTask( crosstabView )
					.removeDimension( dimensionView );

			// if target crosstab view is null, generate it
			crosstabView = crosstab.getCrosstabView( targetAxisType );
			if ( crosstabView == null )
			{
				crosstabView = crosstab.addCrosstabView( targetAxisType );
			}
			crosstabView.getViewsProperty( ).add(
					clonedDimensionView.getModelHandle( ), targetIndex );

			// add all the aggregations
			for ( int i = 0; i < clonedDimensionView.getLevelCount( ); i++ )
			{
				LevelViewHandle levelView = clonedDimensionView.getLevel( i );
				String levelName = levelView.getCubeLevelName( );
				if ( levelName == null )
					continue;
				List measureList = (List) measureListMap.get( levelName );
				List functionList = (List) functionListMap.get( levelName );
				levelView.addSubTotal( measureList, functionList );
			}
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );
	}

	/**
	 * Moves the dimension view in the source position of source row/column to
	 * the target index in the target row/column. The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>. And index is 0-based
	 * integer.
	 * 
	 * @param srcAxisType
	 *            the source row/column axis type
	 * @param srcIndex
	 *            the source position index
	 * @param targetAxisType
	 *            row/column axis type of the move target
	 * @param targetIndex
	 *            the position index of the move target
	 * @throws SemanticException
	 */
	public void pivotDimension( int srcAxisType, int srcIndex,
			int targetAxisType, int targetIndex ) throws SemanticException
	{
		DimensionViewHandle dimensionView = crosstab.getDimension( srcAxisType,
				srcIndex );
		if ( dimensionView == null )
		{
			crosstab.getLogger( ).log(
					Level.INFO,
					MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND,
					new Object[]{String.valueOf( srcAxisType ),
							String.valueOf( srcIndex )} );
			return;
		}
		if ( crosstab.getCrosstabView( targetAxisType ) == null )
		{
			// TODO: throws exception
			return;
		}
		moveDimension( dimensionView, targetAxisType, targetIndex );
	}

	/**
	 * Inserts a row/column dimension into the given position. The axis type can
	 * be either <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>. And index is 0-based
	 * integer.
	 * 
	 * @param dimensionHandle
	 *            the OLAP dimension handle to use
	 * @param axisType
	 *            row/column axis type
	 * @param index
	 *            insert position, a 0-based integer
	 * @return
	 * @throws SemanticException
	 */
	public DimensionViewHandle insertDimension(
			DimensionHandle dimensionHandle, int axisType, int index )
			throws SemanticException
	{
		// if this dimension handle has referred by an existing dimension view,
		// then log error and do nothing
		if ( dimensionHandle != null
				&& crosstab.getDimension( dimensionHandle.getQualifiedName( ) ) != null )
		{
			crosstab.getLogger( ).log( Level.SEVERE,
					MessageConstants.CROSSTAB_EXCEPTION_DUPLICATE_DIMENSION,
					dimensionHandle.getQualifiedName( ) );
			throw new CrosstabException( crosstab.getModelHandle( )
					.getElement( ), new String[]{
					dimensionHandle.getQualifiedName( ),
					crosstab.getModelHandle( ).getElement( ).getIdentifier( )},
					MessageConstants.CROSSTAB_EXCEPTION_DUPLICATE_DIMENSION );
		}

		CrosstabViewHandle crosstabView = crosstab.getCrosstabView( axisType );

		if ( crosstabView == null )
		{
			// if the crosstab view is null, then create and add a crosstab view
			// first, and then add the dimension to it second;
			CommandStack stack = crosstab.getCommandStack( );
			DimensionViewHandle dimensionView = null;
			stack.startTrans( null );

			try
			{
				crosstabView = crosstab.addCrosstabView( axisType );
				dimensionView = crosstabView.insertDimension( dimensionHandle,
						index );
			}
			catch ( SemanticException e )
			{
				stack.rollback( );
				throw e;
			}
			stack.commit( );
			return dimensionView;

		}

		// add the dimension to crosstab view directly
		return crosstabView.insertDimension( dimensionHandle, index );
	}

	/**
	 * Removes a dimension view that refers a cube dimension name with the given
	 * name from the design tree.
	 * 
	 * @param name
	 *            name of the dimension view to remove
	 * @throws SemanticException
	 */
	public void removeDimension( String name ) throws SemanticException
	{
		DimensionViewHandle dimensionView = crosstab.getDimension( name );
		if ( dimensionView == null )
		{
			crosstab.getLogger( ).log( Level.SEVERE,
					MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND,
					name );
			throw new CrosstabException( crosstab.getModelHandle( )
					.getElement( ), new String[]{name,
					crosstab.getModelHandle( ).getElement( ).getIdentifier( )},
					MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND );
		}
		removeDimension( dimensionView.getAxisType( ), dimensionView.getIndex( ) );
	}

	/**
	 * Removes a row/column dimension view in the given position. The axis type
	 * can be either <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>. And index is 0-based
	 * integer.
	 * 
	 * @param axisType
	 *            row/column axis type
	 * @param index
	 *            the position index of the dimension to remove, 0-based integer
	 * @throws SemanticException
	 */
	public void removeDimension( int axisType, int index )
			throws SemanticException
	{
		CrosstabViewHandle crosstabView = crosstab.getCrosstabView( axisType );
		if ( crosstabView != null )
		{
			crosstabView.removeDimension( index );
		}
	}

	/**
	 * Locates the cell which controls the column with for given cell
	 * 
	 * @param crosstab
	 * @throws SemanticException
	 */
	public void validateCrosstab( ) throws SemanticException
	{
		if ( crosstab == null )
			return;
		String measureDirection = crosstab.getMeasureDirection( );
		int axisType = COLUMN_AXIS_TYPE;
		if ( MEASURE_DIRECTION_HORIZONTAL.equals( measureDirection ) )
		{
			// if measure is hotizontal, then do the validation according to the
			// column levels and grand-total
			axisType = COLUMN_AXIS_TYPE;
		}
		else
		{
			// if measure is vertical, then do the validtion according to the
			// row levels and grand-total
			axisType = ROW_AXIS_TYPE;
		}

		int counterAxisType = CrosstabModelUtil.getOppositeAxisType( axisType );
		// all the levels that may need add cells to be aggregated on, each in
		// the list may be an innermost in the axis type or has sub-total
		List counterAxisAggregationLevels = CrosstabModelUtil
				.getAllAggregationLevels( crosstab, counterAxisType );
		List toValidateLevelViews = CrosstabModelUtil.getAllAggregationLevels(
				crosstab, axisType );

		// validate the aggregations for sub-total
		int count = toValidateLevelViews.size( );
		for ( int i = 0; i < count; i++ )
		{
			LevelViewHandle levelView = (LevelViewHandle) toValidateLevelViews
					.get( i );

			// if the level is innermost or has sub-total, we should validate
			// the aggregations for it, otherwise need do nothing
			assert levelView.isInnerMost( )
					|| levelView.getAggregationHeader( ) != null;

			for ( int j = 0; j < crosstab.getMeasureCount( ); j++ )
			{
				MeasureViewHandle measureView = crosstab.getMeasure( j );
				validateMeasure( measureView, levelView, axisType,
						counterAxisAggregationLevels );
			}
		}

		// validate aggregations for grand-total
		if ( crosstab.getGrandTotal( axisType ) != null )
		{
			for ( int j = 0; j < crosstab.getMeasureCount( ); j++ )
			{
				MeasureViewHandle measureView = crosstab.getMeasure( j );
				validateMeasure( measureView, null, axisType,
						counterAxisAggregationLevels );
			}

		}
	}
}
