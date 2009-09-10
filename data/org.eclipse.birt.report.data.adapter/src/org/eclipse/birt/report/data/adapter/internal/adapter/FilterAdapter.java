/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */ 
package org.eclipse.birt.report.data.adapter.internal.adapter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;

/**
 * A report filter
 */
public class FilterAdapter extends FilterDefinition
{
	protected IModelAdapter adapter;
	/**
	 * Construct a filter based on a Model filter handle
	 * @throws AdapterException 
	 */
	public FilterAdapter( IModelAdapter adapter, FilterConditionHandle modelFilter  ) throws AdapterException
	{
		super(null);
		this.adapter = adapter;
		String filterExpr = modelFilter.getExpr( );
		if ( filterExpr != null || filterExpr.length( ) > 0 )
		{
			// convert to DtE exprFilter if there is no operator
			String filterOpr = modelFilter.getOperator( );
			if ( filterOpr == null || filterOpr.length( ) == 0 )
			{
				// Standalone expression; data type must be boolean
				setExpression( adapter.adaptExpression( DataAdapterUtil.getExpression( modelFilter.getExpressionProperty( FilterCondition.EXPR_MEMBER )) ) );
			}
			else
			{
				// Condition filter with operator and operands
				if ( !filterOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_IN )
						&& !filterOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_NOT_IN ) )
				{
					String operand1 = modelFilter.getValue1( );
					String operand2 = modelFilter.getValue2( );

					setExpression( adapter.adaptConditionalExpression( DataAdapterUtil.getExpression( modelFilter.getExpressionProperty( FilterCondition.EXPR_MEMBER )),
							filterOpr,
							operand1 == null? null: modelFilter.getValue1ExpressionList( ).getListValue().get( 0 ),
							DataAdapterUtil.getExpression( modelFilter.getExpressionProperty( FilterCondition.VALUE2_MEMBER ))));
				}
				else
				{
					List<Expression> operands = modelFilter.getValue1ExpressionList( ).getListValue();
					List<IScriptExpression> adaptedExpressions = new ArrayList<IScriptExpression>();
					for( Expression expr:operands )
					{
						adaptedExpressions.add( adapter.adaptExpression( expr ) );
					}
					setExpression( new ConditionAdapter( adapter.adaptExpression( DataAdapterUtil.getExpression( modelFilter.getExpressionProperty( FilterCondition.EXPR_MEMBER ))) ,
							filterOpr,
							adaptedExpressions ) );
				}
			}
		}
	}
	
	/**
	 * Construct a filter with provided expression text
	 * @throws AdapterException 
	 */
	public FilterAdapter( IModelAdapter adapter, ExpressionHandle handle ) throws AdapterException
	{
		super( adapter.adaptExpression( DataAdapterUtil.getExpression( handle )));
		this.adapter = adapter;
	}
	
}
