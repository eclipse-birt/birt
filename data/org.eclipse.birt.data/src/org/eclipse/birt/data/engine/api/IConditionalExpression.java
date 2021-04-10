/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
package org.eclipse.birt.data.engine.api;

/**
 * Describes a conditional expression that produces a Boolean type result.
 * <p>
 * A conditonal expression contains up to four parts: <br>
 * <li>A main expression. This is a Javascript expression that is evaluated at
 * runtime to produce a result which is compared to the operands.<br>
 * <li>An operator. This operator is applied to the main expression and the
 * operands. <br>
 * <li>[optional] Operand 1 & Operand 2. These are Javascript expressions. They
 * are evaluated at runtime, and their results are the operator's 1st and 2nd
 * operands respectively. <br>
 * 
 */
public interface IConditionalExpression extends IBaseExpression {
	/*
	 * Enumeration constants for operator *** DO NOT change the value of any
	 * existing constant! ***
	 */

	/**
	 * No operator defined for this conditonal expression
	 */
	public static final int OP_NONE = 0;

	/**
	 * The Equals operator. Evalutes to true if result is equal to operand 1.
	 */
	public static final int OP_EQ = 1;

	/**
	 * The NotEquals operator. Evalutes to true if result is not equal to operand 1.
	 */
	public static final int OP_NE = 2;

	/**
	 * The LessThan operator. Evalutes to true if result is less than operand 1.
	 */
	public static final int OP_LT = 3;

	/**
	 * The LessThanOrEqualTo operator. Evalutes to true if result is less than or
	 * equal to operand 1.
	 */
	public static final int OP_LE = 4;

	/**
	 * The GreaterThanOrEqualTo operator. Evalutes to true if result is greater than
	 * or equal to operand 1.
	 */
	public static final int OP_GE = 5;

	/**
	 * The GreaterThan operator. Evalutes to true if result is greater than operand
	 * 1.
	 */
	public static final int OP_GT = 6;

	/**
	 * The Between operator. Evalutes to true if result is greater than or equal to
	 * operand 1, and less than or equal to operand 2.
	 */
	public static final int OP_BETWEEN = 7;

	/**
	 * The NotBetween operator. Evaluates to true if the Between operator evaluates
	 * to False for the same result and operands.
	 */
	public static final int OP_NOT_BETWEEN = 8;

	/**
	 * The isNull operator. Evalutes to true if the result is null.
	 */
	public static final int OP_NULL = 9;

	/**
	 * The isNotNull operator. Evalutes to true if the result is not null.
	 */
	public static final int OP_NOT_NULL = 10;

	/**
	 * The isTrue operator. Evalutes to true if the result is of Boolean type, and
	 * has a value of true.
	 */
	public static final int OP_TRUE = 11;

	/**
	 * The isFalse operator. Evalutes to true if the result is of Boolean type, and
	 * has a value of false.
	 */
	public static final int OP_FALSE = 12;

	/**
	 * The Like operator. Evaluates to true if the left operand is a String that
	 * matches the pattern string provided as right operand. the pattern uses "%" to
	 * match 0 or more of any characters, "_" to match exactly one character, and
	 * "/" as escape character. All other characters are matched case-sensitively.
	 */
	public static final int OP_LIKE = 13;

	/**
	 * The Top(N) aggregate operator. TODO: define this operator.
	 */
	public static final int OP_TOP_N = 14;

	/**
	 * The Bottom(N) aggregate operator. TODO: define this operator.
	 */
	public static final int OP_BOTTOM_N = 15;

	/**
	 * The TopNPercent aggregate operator. TODO: define this operator.
	 */
	public static final int OP_TOP_PERCENT = 16;

	/**
	 * The BottomNPercent aggregate operator. TODO: define this operator.
	 */
	public static final int OP_BOTTOM_PERCENT = 17;

	// public static final int OP_ANY = 18;

	/**
	 * The Match operator. Evaluates to true if the left operand is a String that
	 * matches the pattern string provided as right operand. The pattern uses
	 * ECMAScript (JavaScript) syntax, as defined in Section 15.10 of Standard
	 * ECMA-262
	 */
	public static final int OP_MATCH = 19;

	public static final int OP_NOT_LIKE = 20;

	public static final int OP_NOT_MATCH = 21;

	/**
	 * IN, NOT_IN operator. Evaluates to true if the left operand is contained or
	 * not contained in the Collection as right operand
	 */
	public static final int OP_IN = 22;
	public static final int OP_NOT_IN = 23;

	/**
	 * Internal filter condition. Not suppose to be used by Data Engine Client.
	 */
	public static final int OP_JOINT = -100;

	/**
	 * Gets the main expression
	 */
	public IScriptExpression getExpression();

	/**
	 * Gets the operator.
	 * 
	 * @return The operator specified using one of the OP_xxx enumeration values
	 *         defined in this interface.
	 */
	public int getOperator();

	/**
	 * Gets the expression for operand 1.
	 */
	public IBaseExpression getOperand1();

	/**
	 * Gets the expression for operand 2.
	 */
	public IBaseExpression getOperand2();
}
