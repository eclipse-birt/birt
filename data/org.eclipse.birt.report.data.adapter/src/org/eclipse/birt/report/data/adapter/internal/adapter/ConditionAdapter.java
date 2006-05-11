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

import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

/**
 * A boolean expression defined with a main expression, an operator, and 0 to 2
 * operands
 */
public class ConditionAdapter extends ConditionalExpression
{
	/**
	 * Constructs an instance, setting main expression and the operator (which takes no operands)
	 * The operator parameter contains a String operator defined in Model
	 */
	public ConditionAdapter( String mainExpr, String operator  )
	{
		super( mainExpr, operatorFromModel(operator) );
	}
	
	/**
	 * Constructs an instance, setting main expression, a unary operator, and its operand
	 * The operator parameter contains a String operator defined in Model
	 */
	public ConditionAdapter( String mainExpr, String operator, String operand1  )
	{
		super( mainExpr, operatorFromModel(operator), operand1 );
	}
	
	/**
	 * Constructs an instance, setting main expression, a binary operator, and its two operands
	 * The operator parameter contains a String operator defined in Model
	 */
	public ConditionAdapter( String mainExpr, String operator, String operand1, String operand2 )
	{
		super( mainExpr, operatorFromModel(operator), operand1, operand2 );
	}
	
	/**
	 * Converts a Model filter operator (a string) to corresponding Data Engine
	 * operator constant
	 */
	public static int operatorFromModel( String modelOpr )
	{
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_EQ ) )
			return IConditionalExpression.OP_EQ;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_NE ) )
			return IConditionalExpression.OP_NE;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_LT ) )
			return IConditionalExpression.OP_LT;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_LE ) )
			return IConditionalExpression.OP_LE;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_GE ) )
			return IConditionalExpression.OP_GE;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_GT ) )
			return IConditionalExpression.OP_GT;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_BETWEEN ) )
			return IConditionalExpression.OP_BETWEEN;
		if ( modelOpr
				.equals( DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN ) )
			return IConditionalExpression.OP_NOT_BETWEEN;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_NULL ) )
			return IConditionalExpression.OP_NULL;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_NOT_NULL ) )
			return IConditionalExpression.OP_NOT_NULL;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_TRUE ) )
			return IConditionalExpression.OP_TRUE;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_FALSE ) )
			return IConditionalExpression.OP_FALSE;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_LIKE ) )
			return IConditionalExpression.OP_LIKE;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_TOP_N ) )
			return IConditionalExpression.OP_TOP_N;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_N ) )
			return IConditionalExpression.OP_BOTTOM_N;
		if ( modelOpr
				.equals( DesignChoiceConstants.FILTER_OPERATOR_TOP_PERCENT ) )
			return IConditionalExpression.OP_TOP_PERCENT;
		if ( modelOpr
				.equals( DesignChoiceConstants.FILTER_OPERATOR_BOTTOM_PERCENT ) )
			return IConditionalExpression.OP_BOTTOM_PERCENT;
		
		/*		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_ANY ) )
			return IConditionalExpression.OP_ANY;*/
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_MATCH ) )
			return IConditionalExpression.OP_MATCH;
		
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_NOT_LIKE ))
			return IConditionalExpression.OP_NOT_LIKE;
		if ( modelOpr.equals( DesignChoiceConstants.FILTER_OPERATOR_NOT_MATCH ))
			return IConditionalExpression.OP_NOT_MATCH;
		assert false; // unknown filter operator
		return IConditionalExpression.OP_NONE;
	}
	
}
