/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.api;

/**
 * This interface describes a specified conditional expression which returns a
 * Boolean value, and used for Joint Data Set only.
 */
public interface IJoinCondition {

	/**
	 * The integer value stands for an equality operator. It is evaluated to true if
	 * the result of first expression is equal to that of second expression
	 */
	public static final int OP_EQ = 1;

	/**
	 * This method returns the IScriptExpression instance which will be evaluated
	 * against the data set which servers as left operand of a joint.
	 * 
	 * @return the expression servers as first operand of a joint.
	 */
	public IScriptExpression getLeftExpression();

	/**
	 * This method returns the IScriptExpression instance which will be evaluated
	 * against the data set which servers as right operand of a joint.
	 * 
	 * @return the expression servers as second operand of a joint.
	 */
	public IScriptExpression getRightExpression();

	/**
	 * This method returns the integer standing for a compare operator. All
	 * supported compare operators are defined in this interface.
	 * 
	 * @return The operator specified using one of the OP_xxx enumeration values
	 *         defined in this interface.
	 */
	public int getOperator();
}
