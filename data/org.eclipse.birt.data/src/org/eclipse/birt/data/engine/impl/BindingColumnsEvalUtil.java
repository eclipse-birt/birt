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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExprEvaluateUtil;
import org.eclipse.birt.data.engine.impl.ResultIterator.RDSaveUtil;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Scriptable;

/**
 * Evaluate a row of bound columns and meantime do something related with saving
 * the value of bound columns.
 */
class BindingColumnsEvalUtil
{
	// 
	private IResultIterator odiResult;
	private Scriptable scope;
	private RDSaveUtil saveUtil;

	private List allManualBindingExprs;
	private List allAutoBindingExprs;

	private final static int MANUAL_BINDING = 1;
	private final static int AUTO_BINDING = 2;

	/**
	 * @param ri
	 * @param scope
	 * @param saveUtil
	 * @param serviceForResultSet
	 */
	BindingColumnsEvalUtil( IResultIterator ri, Scriptable scope,
			RDSaveUtil saveUtil, List manualBindingExprs, Map autoBindingExprs )
	{
		this.odiResult = ri;
		this.scope = scope;
		this.saveUtil = saveUtil;

		this.initBindingColumns( manualBindingExprs, autoBindingExprs );
	}

	/**
	 * @param serviceForResultSet
	 */
	private void initBindingColumns( List manualBindingExprs,
			Map autoBindingExprs )
	{
		// put the expressions of array into a list
		int size = manualBindingExprs.size( );
		GroupBindingColumn[] groupBindingColumns = new GroupBindingColumn[size];
		Iterator itr = manualBindingExprs.iterator( );
		while ( itr.hasNext( ) )
		{
			GroupBindingColumn temp = (GroupBindingColumn) itr.next( );
			groupBindingColumns[temp.getGroupLevel( )] = temp;
		}

		allManualBindingExprs = new ArrayList( );
		for ( int i = 0; i < size; i++ )
		{
			List groupBindingExprs = new ArrayList( );
			itr = groupBindingColumns[i].getColumnNames( ).iterator( );
			while ( itr.hasNext( ) )
			{
				String exprName = (String) itr.next( );
				IBaseExpression baseExpr = groupBindingColumns[i].getExpression( exprName );
				groupBindingExprs.add( new BindingColumn( exprName, baseExpr ) );
			}

			allManualBindingExprs.add( groupBindingExprs );
		}
		
		// put the auto binding expressions into a list
		allAutoBindingExprs = new ArrayList( );
		itr = autoBindingExprs.entrySet( ).iterator( );
		while ( itr.hasNext( ) )
		{
			Map.Entry entry = (Entry) itr.next( );
			String exprName = (String) entry.getKey( );
			IBaseExpression baseExpr = (IBaseExpression) entry.getValue( );
			
			allAutoBindingExprs.add( new BindingColumn( exprName, baseExpr ) );
		}
	}

	/**
	 * @return
	 * @throws DataException
	 *             save error
	 */
	Map getColumnsValue( ) throws DataException
	{
		Map exprValueMap = new HashMap( );

		for ( int i = 0; i < allManualBindingExprs.size( ); i++ )
		{
			List list = (List) allManualBindingExprs.get( i );
			Iterator it = list.iterator( );
			while ( it.hasNext( ) )
			{
				BindingColumn bindingColumn = (BindingColumn) it.next( );
				evaluateValue( bindingColumn, exprValueMap, MANUAL_BINDING );
			}
		}

		Iterator itr = this.allAutoBindingExprs.iterator( );
		while ( itr.hasNext( ) )
		{
			BindingColumn bindingColumn = (BindingColumn) itr.next( );
			evaluateValue( bindingColumn, exprValueMap, AUTO_BINDING );
		}

		return exprValueMap;
	}

	/**
	 * @param baseExpr
	 * @param exprType
	 * @param valueMap
	 * @throws DataException
	 */
	private void evaluateValue( BindingColumn bindingColumn, Map valueMap,
			int exprType ) throws DataException
	{
		Object exprValue;
		try
		{
			if ( exprType == MANUAL_BINDING )
				exprValue = ExprEvaluateUtil.evaluateExpression( bindingColumn.baseExpr,
						odiResult,
						scope );
			else
				exprValue = ExprEvaluateUtil.evaluateRawExpression( bindingColumn.baseExpr,
						scope );
		}
		catch ( BirtException e )
		{
			exprValue = e;
		}
		valueMap.put( bindingColumn.columnName, exprValue );

		if ( exprValue instanceof BirtException == false )
			saveUtil.doSaveExpr( bindingColumn.columnName, exprValue );
	}

	/**
	 * A simple wrapper for binding column
	 */
	private class BindingColumn
	{

		//
		private String columnName;
		private IBaseExpression baseExpr;

		private BindingColumn( String columnName, IBaseExpression baseExpr )
		{
			this.columnName = columnName;
			this.baseExpr = baseExpr;
		}
	}

}
