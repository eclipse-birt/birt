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
	private IServiceForResultSet serviceForResultSet;
	
	private final static int MANUAL_BINDING = 1;
	private final static int AUTO_BINDING = 2;
	
	/**
	 * @param ri
	 * @param scope
	 * @param saveUtil
	 * @param serviceForResultSet
	 */
	BindingColumnsEvalUtil( IResultIterator ri, Scriptable scope,
			RDSaveUtil saveUtil, IServiceForResultSet serviceForResultSet )
	{
		this.odiResult = ri;
		this.scope = scope;
		this.saveUtil = saveUtil;
		this.serviceForResultSet = serviceForResultSet;
	}

	/**
	 * @return
	 * @throws DataException save error
	 */
	Map getColumnsValue( ) throws DataException
	{
		Map exprValueMap = new HashMap( );

		List groupBindingColsList = serviceForResultSet.getAllBindingExprs( );

		for ( int i = 0; i < groupBindingColsList.size( ); i++ )
		{
			GroupBindingColumn gbc = (GroupBindingColumn) groupBindingColsList.get( i );
			Iterator it = gbc.getColumnNames( ).iterator( );
			while ( it.hasNext( ) )
			{
				String exprName = (String) it.next( );
				IBaseExpression baseExpr = gbc.getExpression( exprName );
				evaluateValue( exprName, baseExpr, exprValueMap, MANUAL_BINDING );
			}
		}

		Map autoBindingExprs = serviceForResultSet.getAllAutoBindingExprs( );
		Iterator itr = autoBindingExprs.entrySet( ).iterator( );
		while ( itr.hasNext( ) )
		{
			Map.Entry entry = (Entry) itr.next( );
			String exprName = (String) entry.getKey( );
			IBaseExpression baseExpr = (IBaseExpression) entry.getValue( );
			evaluateValue( exprName, baseExpr, exprValueMap, AUTO_BINDING );
		}

		return exprValueMap;
	}
	
	/**
	 * @param baseExpr
	 * @param exprType
	 * @param valueMap
	 * @throws DataException 
	 */
	private void evaluateValue( String exprName, IBaseExpression baseExpr,
			Map valueMap, int exprType ) throws DataException
	{
		Object exprValue;
		try
		{
			if ( exprType == MANUAL_BINDING )
				exprValue = ExprEvaluateUtil.evaluateExpression( baseExpr,
						odiResult,
						scope );
			else
				exprValue = ExprEvaluateUtil.evaluateRawExpression( baseExpr,
						scope );
		}
		catch ( BirtException e )
		{
			exprValue = e;
		}
		valueMap.put( exprName, exprValue );
		
		if ( exprValue instanceof BirtException == false )
			saveUtil.doSaveExpr( exprName, exprValue );
	}
	
}
