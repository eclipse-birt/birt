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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.odi.IResultObjectEvent;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;

/**
 * One of implemenation of IResultObjectEvent interface.
 * Used to calculate the computed columns value in the time window
 * from fetching data to do grouping/sorting data.
 */
public class ComputedColumnHelper implements IResultObjectEvent
{
	private DataSetRuntime dataSet;
	
	// computed column list passed from external caller
	private List ccList;
	
	// computed column string array which will be evaluated
	private IBaseExpression[] columnExprArray;
	
	// computed column position index array
	private int[] columnIndexArray;
	
	// prepared flag
	private boolean isPrepared;
	
	protected static Logger logger = Logger.getLogger( ComputedColumnHelper.class.getName( ) );

	ComputedColumnHelper( DataSetRuntime dataSet, List ccList)
	{
		logger.log( Level.FINER, "ComputedColumnHelper starts up" );
		assert ccList != null && ccList.size( ) > 0;
		this.ccList = ccList;
		this.isPrepared = false;
		this.dataSet = dataSet;
	}

	public List getComputedColumnList()
	{
		return this.ccList;
	}
	
	/*
	 * @see org.eclipse.birt.data.engine.odi.IResultObjectEvent#process(org.eclipse.birt.data.engine.odi.IResultObject)
	 */
	public boolean process( IResultObject resultObject, int rowIndex ) throws DataException
	{	
		logger.entering( ComputedColumnHelper.class.getName( ), "process" );
		assert resultObject != null;
		
		IResultClass resultClass = resultObject.getResultClass( );
		if ( isPrepared == false )
			prepare( resultClass );

		// check if no computed columns are found as custom fields in the result
		// set
		if ( columnExprArray.length == 0 )
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
			for ( int i = 0; i < columnExprArray.length; i++ )
			{
				if ( columnExprArray[i] != null )
				{
					Object value = null;
					if ( columnExprArray[i].getHandle() != null)
						value = ((CompiledExpression)columnExprArray[i].getHandle()).evaluate(cx,
									dataSet.getScriptScope() );
					else
						value = ScriptEvalUtil.evaluateJSAsExpr( cx,
								dataSet.getJSDataSetObject(),
								((IScriptExpression)columnExprArray[i]).getText(),
								"ComputedColumn",
								0 );
					try
					{
						value = DataTypeUtil.convert( value,
								resultClass.getFieldValueClass( columnIndexArray[i] ) );
					}
					catch ( BirtException e )
					{
						// Data Type of computed column is not correct
						throw new DataException( ResourceConstants.WRONG_DATA_TYPE_COMPUTED_COLUMN,
								new Object[]{
										resultClass.getFieldName( columnIndexArray[i] ),
										resultClass.getFieldValueClass( columnIndexArray[i] )
												.getName( ),
										value.toString( ),
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
	 * @param rePrepare
	 */
	public void setRePrepare( boolean rePrepare)
	{
		this.isPrepared = !rePrepare;
	}
	/*
	 * Convert ccList to projComputedColumns, only prepare once.
	 */
	private void prepare( IResultClass resultClass ) throws DataException
	{
		assert resultClass!=null;
		
		// identify those computed columns that are projected
		// in the result set by checking the result metadata
		List cmptList = new ArrayList();		
		for ( int i = 0; i < ccList.size( ); i++ )
		{
			IComputedColumn cmptdColumn = (IComputedColumn) ccList.get( i );

			int cmptdColumnIdx = resultClass.getFieldIndex( cmptdColumn.getName( ) );
			// check if given field name is found in result set metadata, and
			// is indeed declared as a custom field
			if ( cmptdColumnIdx >= 1
					&& resultClass.isCustomField( cmptdColumnIdx ) )
				cmptList.add(new Integer(i));
			// else computed column is not projected, skip to next computed
			// column
		}
		
		int size = cmptList.size( );
		columnExprArray = new IBaseExpression[size];
		columnIndexArray = new int[size];

		for ( int i = 0; i < size; i++ )
		{
			int pos = ( (Integer) cmptList.get( i ) ).intValue( );
			IComputedColumn cmptdColumn = (IComputedColumn) ccList.get( pos );
			columnExprArray[i] = cmptdColumn.getExpression( );
			columnIndexArray[i] = resultClass.getFieldIndex( cmptdColumn.getName( ) );
		}
		
		isPrepared = true;
	}
	
}