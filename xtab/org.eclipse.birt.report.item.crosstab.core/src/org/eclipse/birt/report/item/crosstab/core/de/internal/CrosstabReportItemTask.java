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
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.MessageConstants;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;

/**
 * CrosstabReportItemTask
 */
public class CrosstabReportItemTask extends AbstractCrosstabModelTask
{

	/**
	 * 
	 * @param focus
	 */
	public CrosstabReportItemTask( CrosstabReportItemHandle focus )
	{
		super( focus );
		this.crosstab = focus;
	}

	// /**
	// * Returns the mirrored starting level in specific axis.
	// *
	// * @return
	// */
	// public LevelHandle getMirroredStartingLevel( int axisType )
	// {
	// CrosstabViewHandle crosstabView = crosstab.getCrosstabView( axisType );
	//
	// if ( crosstabView != null )
	// {
	// return crosstabView.getMirroredStartingLevel( );
	// }
	//
	// return null;
	// }
	//
	// /**
	// * Sets mirrrored starting level property for specific axis.
	// */
	// public void setMirroredStartingLevel( int axisType, LevelHandle value )
	// throws SemanticException
	// {
	// if ( crosstab == null || !CrosstabModelUtil.isValidAxisType( axisType ) )
	// {
	// return;
	// }
	//
	// CommandStack stack = crosstab.getCommandStack( );
	// stack.startTrans( Messages.getString(
	// "CrosstabReportItemTask.msg.set.mirroredStartingLevel" ) ); //$NON-NLS-1$
	//
	// try
	// {
	// CrosstabViewHandle crosstabView = crosstab.getCrosstabView( axisType );
	//
	// if ( crosstabView == null )
	// {
	// crosstabView = crosstab.addCrosstabView( axisType );
	// }
	//
	// crosstabView.setMirroredStartingLevel( value );
	// }
	// catch ( SemanticException e )
	// {
	// stack.rollback( );
	// throw e;
	// }
	//
	// stack.commit( );
	// }

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

		CommandStack stack = crosstab.getCommandStack( );
		stack.startTrans( Messages.getString( "CrosstabReportItemTask.msg.add.grandtotal" ) ); //$NON-NLS-1$

		CrosstabCellHandle grandTotal = null;

		try
		{
			CrosstabViewHandle crosstabView = crosstab.getCrosstabView( axisType );

			if ( crosstabView == null )
			{
				crosstabView = crosstab.addCrosstabView( axisType );
			}

			grandTotal = new CrosstabViewTask( crosstabView ).addGrandTotal( measureList,
					functionList,
					false );
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}

		stack.commit( );

		return grandTotal;
	}

	/**
	 * Removes row/column grand total from crosstab if it is not empty,
	 * otherwise do nothing. The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>.
	 * 
	 * @param axisType
	 */
	public void removeGrandTotal( int axisType ) throws SemanticException
	{
		CrosstabViewHandle crosstabView = crosstab.getCrosstabView( axisType );

		if ( crosstabView != null )
		{
			crosstabView.removeGrandTotal( );
		}
	}

	/**
	 * Removes row/column grand total from crosstab on particular measure,
	 * otherwise do nothing. The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>.
	 * 
	 * @param axisType
	 */
	public void removeGrandTotal( int axisType, int measureIndex )
			throws SemanticException
	{
		CrosstabViewHandle crosstabView = crosstab.getCrosstabView( axisType );

		if ( crosstabView != null )
		{
			crosstabView.removeGrandTotal( measureIndex );
		}
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
			if ( CrosstabModelUtil.isAggregationOn( measureView, null, axisType ) )
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
		if ( crosstab == null
				|| crosstab.getGrandTotal( axisType ) == null
				|| measureView == null
				|| crosstab != measureView.getCrosstab( ) )
			return null;

		for ( int j = 0; j < measureView.getAggregationCount( ); j++ )
		{
			AggregationCellHandle cell = measureView.getAggregationCell( j );
			if ( ( axisType == COLUMN_AXIS_TYPE && cell.getAggregationOnColumn( ) == null )
					|| ( axisType == ROW_AXIS_TYPE && cell.getAggregationOnRow( ) == null ) )
			{
				String function = CrosstabModelUtil.getAggregationFunction( crosstab,
						cell );
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
		if ( crosstab == null
				|| crosstab.getGrandTotal( axisType ) == null
				|| measureView == null
				|| crosstab != measureView.getCrosstab( ) )
			return;

		CommandStack stack = crosstab.getCommandStack( );
		stack.startTrans( Messages.getString( "CrosstabReportItemTask.msg.set.aggregate.function" ) ); //$NON-NLS-1$

		try
		{
			for ( int j = 0; j < measureView.getAggregationCount( ); j++ )
			{
				AggregationCellHandle cell = measureView.getAggregationCell( j );
				if ( ( axisType == COLUMN_AXIS_TYPE && cell.getAggregationOnColumn( ) == null )
						|| ( axisType == ROW_AXIS_TYPE && cell.getAggregationOnRow( ) == null ) )
				{
					CrosstabModelUtil.setAggregationFunction( crosstab,
							cell,
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
					.getElement( ), new String[]{
					name,
					crosstab.getModelHandle( ).getElement( ).getIdentifier( )
			}, MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND );
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

		// record existing subtotal aggregation info from source dimension
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
				MeasureViewHandle measureView = (MeasureViewHandle) measureList.get( j );
				String function = levelView.getAggregationFunction( measureView );
				if ( function == null )
				{
					functionList.add( DEFAULT_MEASURE_FUNCTION );
				}
				else
				{
					functionList.add( function );
				}
			}
			functionListMap.put( name, functionList );
			measureListMap.put( name, measureList );
		}

		// record existing grandtotal aggregation info on target view, we need
		// to keep the grandtotal, but when remove dimension on source view, it
		// could be removed
		List grandMeasureList = getAggregationMeasures( targetAxisType );
		List grandFunctionList = new ArrayList( );
		for ( int j = 0; j < grandMeasureList.size( ); j++ )
		{
			MeasureViewHandle measureView = (MeasureViewHandle) grandMeasureList.get( j );
			String function = getAggregationFunction( targetAxisType,
					measureView );
			if ( function == null )
			{
				grandFunctionList.add( DEFAULT_MEASURE_FUNCTION );
			}
			else
			{
				grandFunctionList.add( function );
			}
		}

		// have a copy for source dimension
		DimensionViewHandle clonedDimensionView = (DimensionViewHandle) CrosstabUtil.getReportItem( dimensionView.getModelHandle( )
				.copy( )
				.getHandle( dimensionView.getModelHandle( ).getModule( ) ) );

		CommandStack stack = crosstab.getCommandStack( );
		stack.startTrans( Messages.getString( "CrosstabReportItemTask.msg.pivot.dimension" ) ); //$NON-NLS-1$

		try
		{
			CrosstabViewHandle srcCrosstabView = (CrosstabViewHandle) dimensionView.getContainer( );
			new CrosstabViewTask( srcCrosstabView ).removeDimension( dimensionView,
					false );

			// if target crosstab view is null, add it first
			CrosstabViewHandle targetCrosstabView = crosstab.getCrosstabView( targetAxisType );
			if ( targetCrosstabView == null )
			{
				targetCrosstabView = crosstab.addCrosstabView( targetAxisType );
			}

			List transferMeasureList = new ArrayList( );
			List transferFunctionList = new ArrayList( );

			// check if target view is empty and no grandtotal defined, then
			// remove dummy grandtotal from original view
			if ( targetCrosstabView.getDimensionCount( ) == 0
					&& targetCrosstabView.getGrandTotal( ) == null )
			{
				// remove dummy grandtotal cells on remained subtotal from
				// source view
				for ( int i = 0; i < srcCrosstabView.getDimensionCount( ); i++ )
				{
					DimensionViewHandle dv = srcCrosstabView.getDimension( i );

					for ( int j = 0; j < dv.getLevelCount( ); j++ )
					{
						LevelViewHandle lv = dv.getLevel( j );

						if ( lv.getAggregationHeader( ) != null )
						{
							for ( int k = 0; k < crosstab.getMeasureCount( ); k++ )
							{
								MeasureViewHandle mv = crosstab.getMeasure( k );

								String rowDimension = null;
								String rowLevel = null;
								String colDimension = dv.getCubeDimensionName( );
								String colLevel = lv.getCubeLevelName( );

								if ( srcCrosstabView.getAxisType( ) == ROW_AXIS_TYPE )
								{
									rowDimension = colDimension;
									rowLevel = colLevel;
									colDimension = null;
									colLevel = null;
								}

								AggregationCellHandle aggCell = mv.getAggregationCell( rowDimension,
										rowLevel,
										colDimension,
										colLevel );

								if ( aggCell != null )
								{
									aggCell.getModelHandle( ).drop( );
								}
							}
						}
					}
				}

				// transfer dummy grandtotal cells on source grandtotal to
				// target view
				if ( srcCrosstabView.getGrandTotal( ) != null )
				{

					for ( int i = 0; i < crosstab.getMeasureCount( ); i++ )
					{
						MeasureViewHandle mv = crosstab.getMeasure( i );

						AggregationCellHandle aggCell = mv.getAggregationCell( null,
								null,
								null,
								null );

						if ( aggCell != null )
						{
							String function = getAggregationFunction( srcCrosstabView.getAxisType( ),
									mv );

							aggCell.getModelHandle( ).drop( );

							// record the grandtotal cell need to be transfered
							transferMeasureList.add( mv );
							transferFunctionList.add( function );
						}
					}
				}
			}

			targetCrosstabView.getViewsProperty( )
					.add( clonedDimensionView.getModelHandle( ), targetIndex );

			// transfer pervious recorded grandtotal for target view
			if ( transferMeasureList.size( ) > 0
					&& clonedDimensionView.getLevelCount( ) > 0 )
			{
				addMeasureAggregations( clonedDimensionView.getLevel( clonedDimensionView.getLevelCount( ) - 1 ),
						transferMeasureList,
						transferFunctionList,
						false );
			}

			// add all the level aggregations
			for ( int i = 0; i < clonedDimensionView.getLevelCount( ); i++ )
			{
				LevelViewHandle levelView = clonedDimensionView.getLevel( i );
				String levelName = levelView.getCubeLevelName( );
				if ( levelName == null )
				{
					continue;
				}

				if ( levelView.isInnerMost( ) )
				{
					// remove aggregatio header on new innermost level if
					// existed
					if ( levelView.getAggregationHeaderProperty( )
							.getContentCount( ) > 0 )
					{
						levelView.getAggregationHeaderProperty( ).drop( 0 );
					}
				}
				else
				{
					// try restore original subtotal
					List measureList = (List) measureListMap.get( levelName );
					List functionList = (List) functionListMap.get( levelName );
					new LevelViewTask( levelView ).addSubTotal( measureList,
							functionList,
							false );
				}
			}

			// restore all grandtotal aggregations on target view
			if ( grandMeasureList.size( ) > 0 )
			{
				addMeasureAggregations( targetAxisType,
						grandMeasureList,
						grandFunctionList,
						false );
			}

			validateCrosstab( );
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
			crosstab.getLogger( ).log( Level.INFO,
					MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND,
					new Object[]{
							String.valueOf( srcAxisType ),
							String.valueOf( srcIndex )
					} );
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
					crosstab.getModelHandle( ).getElement( ).getIdentifier( )
			}, MessageConstants.CROSSTAB_EXCEPTION_DUPLICATE_DIMENSION );
		}

		DimensionViewHandle dimensionView = null;

		CommandStack stack = crosstab.getCommandStack( );
		stack.startTrans( Messages.getString( "CrosstabReportItemTask.msg.insert.dimension" ) ); //$NON-NLS-1$

		try
		{
			CrosstabViewHandle crosstabView = crosstab.getCrosstabView( axisType );

			if ( crosstabView == null )
			{
				// if the crosstab view is null, then create and add a crosstab
				// view
				// first, and then add the dimension to it second;
				crosstabView = crosstab.addCrosstabView( axisType );
			}

			// add the dimension to crosstab view directly
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
					.getElement( ), new String[]{
					name,
					crosstab.getModelHandle( ).getElement( ).getIdentifier( )
			}, MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND );
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

}
