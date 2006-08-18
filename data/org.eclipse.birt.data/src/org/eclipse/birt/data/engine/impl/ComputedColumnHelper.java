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
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.TransformationConstants;
import org.eclipse.birt.data.engine.expression.CompiledExpression;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;

/**
 * One of implemenation of IResultObjectEvent interface. Used to calculate the
 * computed columns value in the time window from fetching data to do
 * grouping/sorting data.
 */

public class ComputedColumnHelper implements IResultObjectEvent
{
	private ComputedColumnHelperInstance dataSetInstance;
	private ComputedColumnHelperInstance resultSetInstance;
	private int currentModel;
	private List allCC;

	// private Object groupMethod.

	/**
	 * 
	 * @param dataSet
	 * @param dataSetCCList
	 * @param resultSetCCList
	 */
	ComputedColumnHelper( DataSetRuntime dataSet, List dataSetCCList,
			List resultSetCCList )
	{
		this.dataSetInstance = new ComputedColumnHelperInstance( dataSet,
				dataSetCCList );
		this.resultSetInstance = new ComputedColumnHelperInstance( dataSet,
				resultSetCCList );
		this.currentModel = TransformationConstants.DATA_SET_MODEL;
		this.allCC = new ArrayList( );
		this.allCC.addAll( dataSetCCList );
		this.allCC.addAll( resultSetCCList );
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
							value = ScriptEvalUtil.evaluateJSAsExpr( cx,
									dataSet.getJSDataSetObject( ),
									( (IScriptExpression) computedColumn[i].getExpression( ) ).getText( ),
									"ComputedColumn",
									0 );
						}

						value = DataTypeUtil.convert( value,
								resultClass.getFieldValueClass( columnIndexArray[i] ) );
					}
					catch ( BirtException e )
					{
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