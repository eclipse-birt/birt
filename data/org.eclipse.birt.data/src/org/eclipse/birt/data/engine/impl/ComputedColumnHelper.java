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

package org.eclipse.birt.data.engine.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.TransformationConstants;
import org.eclipse.birt.data.engine.expression.CompiledExpression;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;

/**
 * One of implementation of IResultObjectEvent interface. Used to calculate the
 * computed columns value in the time window from fetching data to do
 * grouping/sorting data.
 */

public class ComputedColumnHelper implements IResultObjectEvent
{
	private ComputedColumnHelperInstance dataSetInstance;
	private ComputedColumnHelperInstance resultSetInstance;
	private ComputedColumnHelperInstance availableModeInstance;
	private int currentModel;
	private List allCC;

	private static Logger logger = Logger.getLogger( ComputedColumnHelper.class.getName( ) );
	// private Object groupMethod.

	/**
	 * 
	 * @param dataSet
	 * @param dataSetCCList
	 * @param resultSetCCList
	 * @throws  
	 */
	ComputedColumnHelper( DataSetRuntime dataSet, List dataSetCCList,
			List resultSetCCList ) throws DataException
	{
		Object[] params = { dataSet, dataSetCCList, resultSetCCList };
		logger.entering( ComputedColumnHelper.class.getName( ),
				"ComputedColumnHelper",
				params );
		
		this.dataSetInstance = new ComputedColumnHelperInstance( dataSet,
				dataSetCCList );
		this.resultSetInstance = new ComputedColumnHelperInstance( dataSet,
				resultSetCCList );
		List availableCCList = new ArrayList( );
		getAvailableComputedList( getComputedNameList( dataSetCCList ),
				dataSetCCList,
				availableCCList );
		this.availableModeInstance = new ComputedColumnHelperInstance( dataSet,
				availableCCList );
		this.currentModel = TransformationConstants.DATA_SET_MODEL;
		this.allCC = new ArrayList( );
		this.allCC.addAll( dataSetCCList );
		this.allCC.addAll( resultSetCCList );
		logger.exiting( ComputedColumnHelper.class.getName( ), "ComputedColumnHelper" );
	}

	/**
	 * 
	 * @return
	 */
	private ComputedColumnHelperInstance getCurrentInstance( )
	{
		if ( this.currentModel == TransformationConstants.DATA_SET_MODEL )
			return this.dataSetInstance;
		else if ( this.currentModel == TransformationConstants.RESULT_SET_MODEL )
			return this.resultSetInstance;
		else if ( this.currentModel == TransformationConstants.PRE_CALCULATE_MODEL )
			return this.availableModeInstance;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.odi.IResultObjectEvent#process(org.eclipse.birt.data.engine.odi.IResultObject,
	 *      int)
	 */
	public boolean process( IResultObject resultObject, int rowIndex )
			throws DataException
	{
		if ( this.getCurrentInstance( ) != null )
			return this.getCurrentInstance( ).process( resultObject, rowIndex );
		else
			return true;
	}

	/**
	 * Return whether the computed column set with given model exists
	 * 
	 * @param model
	 * @return
	 */
	public boolean isComputedColumnExist( int model )
	{
		if ( model == TransformationConstants.DATA_SET_MODEL )
			return this.dataSetInstance.getComputedColumnList( ).size( ) > 0;
		else if ( model == TransformationConstants.RESULT_SET_MODEL )
			return this.resultSetInstance.getComputedColumnList( ).size( ) > 0;
		else if ( model == TransformationConstants.ALL_MODEL )
			return this.allCC.size( ) > 0;
		return false;
	}

	/**
	 * Return a list of computed column of current instance.
	 * 
	 * @return
	 */
	public List getComputedColumnList( )
	{
		if ( this.getCurrentInstance( ) != null )
			return this.getCurrentInstance( ).getComputedColumnList( );
		else
			return this.allCC;
	}

	/**
	 * 
	 * @param rePrepare
	 */
	public void setRePrepare( boolean rePrepare )
	{
		if ( this.getCurrentInstance( ) != null )
			this.getCurrentInstance( ).setRePrepare( rePrepare );
	}

	/**
	 * 
	 * @param model
	 */
	public void setModel( int model )
	{
		this.currentModel = model;
	}
		
	/**
	 * 
	 * @param dataSetCCList
	 * @return
	 */
	private List getComputedNameList( List dataSetCCList )
	{
		List result = new ArrayList( );
		for ( int i = 0; i < dataSetCCList.size( ); i++ )
		{
			IComputedColumn column = (IComputedColumn) dataSetCCList.get( i );
			result.add( column.getName( ) );
		}
		return result;
	}	
	/**
	 * 
	 * @param dataSetCCList
	 * @return
	 * @throws DataException 
	 */
	private void getAvailableComputedList( List refernceNameList,
			List dataSetCCList, List result ) throws DataException
	{
		try
		{
			for ( int i = 0; i < dataSetCCList.size( ); i++ )
			{
				IComputedColumn column = (IComputedColumn) dataSetCCList.get( i );
				if ( !refernceNameList.contains( column.getName( ) ) )
				{
					continue;
				}

				if ( ExpressionCompilerUtil.hasAggregationInExpr( column.getExpression( ) ) )
				{
					continue;
				}
				else
				{
					List referedList = ExpressionUtil.extractColumnExpressions( ( (IScriptExpression) column.getExpression( ) ).getText( ) );
					if ( referedList.size( ) == 0 )
					{
						result.add( column );
					}
					else
					{
						List newList = new ArrayList( );
						for ( int j = 0; j < referedList.size( ); j++ )
						{
							IColumnBinding binding = (IColumnBinding) referedList.get( j );
							String name = binding.getResultSetColumnName( );
							newList.add( name );
						}
						if ( !hasAggregation( newList, dataSetCCList ) )
						{
							result.add( column );
						}
					}
				}
			}
		}
		catch ( BirtException e )
		{
			throw DataException.wrap( e );
		}
	}

	/**
	 * 
	 * @param nameList
	 * @param dataSetCCList
	 * @return
	 * @throws DataException
	 */
	private boolean hasAggregation( List nameList, List dataSetCCList )
			throws DataException
	{
		try
		{
			for ( int k = 0; k < nameList.size( ); k++ )
			{
				IComputedColumn column = null;
				for ( int i = 0; i < dataSetCCList.size( ); i++ )
				{
					column = (IComputedColumn) dataSetCCList.get( i );
					if ( column.getName( ) != null &&
							column.getName( ).equals( nameList.get( k ) ) )
						break;
					else
						column = null;
				}
				if ( column != null )
				{
					if ( ExpressionCompilerUtil.hasAggregationInExpr( column.getExpression( ) ) )
					{
						return true;
					}
					else
					{
						List referedList = ExpressionUtil.extractColumnExpressions( ( (IScriptExpression) column.getExpression( ) ).getText( ) );
						List newList = new ArrayList( );
						for ( int j = 0; j < referedList.size( ); j++ )
						{
							IColumnBinding binding = (IColumnBinding) referedList.get( j );
							String name = binding.getResultSetColumnName( );
							newList.add( name );
						}
						return hasAggregation( newList, dataSetCCList );
					}
				}
				else
				{
					continue;
				}
			}
			return false;
		}
		catch ( BirtException e )
		{
			throw DataException.wrap( e );
		}
	}
}

class ComputedColumnHelperInstance
{

	private DataSetRuntime dataSet;

	// computed column list passed from external caller
	private List ccList;

	// computed column array which will be evaluated
	private IComputedColumn[] computedColumn;

	// computed column position index array
	private int[] columnIndexArray;

	// prepared flag
	private boolean isPrepared;

	protected static Logger logger = Logger.getLogger( ComputedColumnHelper.class.getName( ) );

	public ComputedColumnHelperInstance( DataSetRuntime dataSet,
			List computedColumns )
	{
		// Do not change the assignment of array
		// TODO enhance.
		this.ccList = new ArrayList( );
		for ( int i = 0; i < computedColumns.size( ); i++ )
			this.ccList.add( computedColumns.get( i ) );
		this.isPrepared = false;
		this.dataSet = dataSet;
	}

	public List getComputedColumnList( )
	{
		return this.ccList;
	}

	/*
	 * @see org.eclipse.birt.data.engine.odi.IResultObjectEvent#process(org.eclipse.birt.data.engine.odi.IResultObject)
	 */
	public boolean process( IResultObject resultObject, int rowIndex )
			throws DataException
	{
		logger.entering( ComputedColumnHelper.class.getName( ), "process" );
		assert resultObject != null;

		IResultClass resultClass = resultObject.getResultClass( );
		if ( isPrepared == false )
			prepare( resultClass );

		// check if no computed columns are found as custom fields in the result
		// set
		if ( computedColumn.length == 0 )
		{
			logger.exiting( ComputedColumnHelper.class.getName( ), "process" );
			return true; // done
		}

		// bind new object to row script object
		dataSet.setRowObject( resultObject, true );
		dataSet.setCurrentRowIndex( rowIndex );
		// now assign the computed value to each of its projected computed
		// columns
		Context cx = Context.enter( );
		try
		{
			// iterate through each projected computed column,
			// and assign it the computed value
			for ( int i = 0; i < computedColumn.length; i++ )
			{
				if ( computedColumn[i].getExpression( ) != null )
				{
					Object value = null;
					try
					{
						if ( computedColumn[i].getExpression( ).getHandle( ) != null )
							value = ( (CompiledExpression) computedColumn[i].getExpression( )
									.getHandle( ) ).evaluate( cx,
									dataSet.getScriptScope( ) );
						else
						{
							String exprText = ( (IScriptExpression) computedColumn[i].getExpression( ) ).getText( );
							if ( exprText != null )
								value = ScriptEvalUtil.evaluateJSAsExpr( cx,
										dataSet.getJSDataSetObject( ),
										exprText,
										"ComputedColumn",
										0 );
						}
						if ( computedColumn[i] instanceof GroupComputedColumn )
						{
							try
							{
								value = ( (GroupComputedColumn) computedColumn[i] ).calculate( value );
							}
							catch ( BirtException e )
							{
								throw DataException.wrap( e );
							}
						}

						value = DataTypeUtil.convert( value,
								resultClass.getFieldValueClass( columnIndexArray[i] ) );
					}
					catch ( BirtException e )
					{
						if ( resultClass.wasAnyType( columnIndexArray[i] ) )
							throw new DataException( ResourceConstants.POSSIBLE_MIXED_DATA_TYPE_IN_COLUMN );
						
						String fieldName = resultClass.getFieldName( columnIndexArray[i] );
						if ( fieldName != null
								&& fieldName.startsWith( "_{$TEMP_" ) )
						{
							// Data Type of computed column is not correct
							throw new DataException( ResourceConstants.WRONG_DATA_TYPE_SCRIPT_RESULT,
									new Object[]{
											resultClass.getFieldValueClass( columnIndexArray[i] )
													.getName( ),
											value == null ? value
													: value.toString( ),
									} );
						}
						// Data Type of computed column is not correct
						throw new DataException( ResourceConstants.WRONG_DATA_TYPE_COMPUTED_COLUMN,
								new Object[]{
										resultClass.getFieldName( columnIndexArray[i] ),
										resultClass.getFieldValueClass( columnIndexArray[i] )
												.getName( ),
										value == null ? value
												: value.toString( ),
								} );
					}

					resultObject.setCustomFieldValue( columnIndexArray[i],
							value );
				}
				else
				{
					throw new DataException( ResourceConstants.EXPR_INVALID_COMPUTED_COLUMN,
							resultObject.getResultClass( )
									.getFieldName( columnIndexArray[i] ) );
				}
			}
		}
		finally
		{
			Context.exit( );
		}
		logger.exiting( ComputedColumnHelper.class.getName( ), "process" );
		return true;
	}

	/**
	 * Indicate the ComputedColumnHelper to reprepare.
	 * 
	 * @param rePrepare
	 */
	public void setRePrepare( boolean rePrepare )
	{
		this.isPrepared = !rePrepare;
	}

	/**
	 * Convert ccList to projComputedColumns, only prepare once.
	 */
	private void prepare( IResultClass resultClass ) throws DataException
	{
		assert resultClass != null;

		// identify those computed columns that are projected
		// in the result set by checking the result metadata
		List cmptList = new ArrayList( );
		for ( int i = 0; i < ccList.size( ); i++ )
		{
			IComputedColumn cmptdColumn = (IComputedColumn) ccList.get( i );

			int cmptdColumnIdx = resultClass.getFieldIndex( cmptdColumn.getName( ) );
			// check if given field name is found in result set metadata, and
			// is indeed declared as a custom field
			if ( cmptdColumnIdx >= 1
					&& resultClass.isCustomField( cmptdColumnIdx ) )
				cmptList.add( new Integer( i ) );
			// else computed column is not projected, skip to next computed
			// column
		}

		int size = cmptList.size( );
		columnIndexArray = new int[size];
		computedColumn = new IComputedColumn[size];

		for ( int i = 0; i < size; i++ )
		{
			int pos = ( (Integer) cmptList.get( i ) ).intValue( );
			IComputedColumn cmptdColumn = (IComputedColumn) ccList.get( pos );
			computedColumn[i] = cmptdColumn;
			columnIndexArray[i] = resultClass.getFieldIndex( cmptdColumn.getName( ) );
		}

		isPrepared = true;
	}
}