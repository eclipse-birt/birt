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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefn;
import org.eclipse.birt.data.engine.api.IJSExpression;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IFilter;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;

/**
 * Implementation of IFilter, which will do filtering on row data.
 */
class FilterByRow implements IFilter
{

	protected List filters;
	protected Scriptable scope;
	protected JSRowObject scriptObj;
	
	FilterByRow( List filters, Scriptable scope, JSRowObject scriptObj )
	{
		this.filters = filters;
		this.scope = scope;
		this.scriptObj = scriptObj;
	}
	
	public boolean accept( IResultObject row ) throws DataException
	{
		boolean isAccepted = true;
		Iterator filterIt = filters.iterator( );
		scriptObj.setRowObject( row );
		while ( filterIt.hasNext( ) )
		{
			try
			{
				IFilterDefn filter = (IFilterDefn) filterIt.next( );

				IBaseExpression expr = filter.getExpression( );

				Object result = evaluateExpression( expr );
				// filter in
				if ( DataTypeUtil.toBoolean( result ).booleanValue( ) == false )
				{
					isAccepted = false;
					break;
				}
			}

			catch ( DataException e )
			{

				throw new DataException( ResourceConstants.EVALUATE_ERROR, e );
			}

		}
		return isAccepted;
	}

	public Object evaluateExpression( IBaseExpression expr )
			throws DataException, DataException
	{
		if ( expr == null )
		{
			return null;
		}
		else if ( expr instanceof IConditionalExpression )
		{
			ConditionalExpression ConditionalExpr = (ConditionalExpression) expr;
			Object expression = evaluateExpression( ConditionalExpr
					.getExpression( ) );
			Object Op1 = evaluateExpression( ConditionalExpr.getOperand1( ) );
			Object Op2 = evaluateExpression( ConditionalExpr.getOperand2( ) );
			return JSExprCalculator.getResult( expression, ConditionalExpr
					.getOperator( ), Op1, Op2 );
		}
		else
		{
			IJSExpression jsExpr = (IJSExpression) expr;
			Context cx = Context.enter( );
			try
			{
				return cx.evaluateString( scope, jsExpr.getText( ), "MySource",
						1, null );
			}
			catch ( JavaScriptException e )
			{
				throw new DataException( ResourceConstants.EVALUATE_ERROR, e );
			}
			finally
			{
				Context.exit( );
			}
		}
	}
}
