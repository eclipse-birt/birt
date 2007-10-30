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

import java.util.List;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * A report filter
 */
public class FilterAdapter extends FilterDefinition
{
	/**
	 * Construct a filter based on a Model filter handle
	 */
	public FilterAdapter( FilterConditionHandle modelFilter  )
	{
		super(null);
		
		String filterExpr = modelFilter.getExpr( );
		if ( filterExpr != null || filterExpr.length( ) > 0 )
		{
			// convert to DtE exprFilter if there is no operator
			String filterOpr = modelFilter.getOperator( );
			if ( filterOpr == null || filterOpr.length( ) == 0 )
			{
				// Standalone expression; data type must be boolean
				setExpression( new ExpressionAdapter( filterExpr, DataType.BOOLEAN_TYPE ) );
			}
			else
			{
				// Condition filter with operator and operands
				if ( !filterOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_IN )
						&& !filterOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_NOT_IN ) )
				{
					String operand1 = modelFilter.getValue1( );
					String operand2 = modelFilter.getValue2( );

					setExpression( new ConditionAdapter( filterExpr,
							filterOpr,
							operand1,
							operand2 ) );
				}
				else
				{
					List operands = modelFilter.getValue1List( );
					setExpression( new ConditionAdapter( filterExpr,
							filterOpr,
							operands ) );
				}
			}
		}
	}
	
	/**
	 * Construct a filter with provided expression text
	 */
	public FilterAdapter( String exprText )
	{
		super( new ExpressionAdapter( exprText, DataType.BOOLEAN_TYPE));
	}
	
}
