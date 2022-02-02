/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.expression;

/**
 * <code>AggregateObject</code> represents aggregate object that might need
 * multi pass process.
 */

class AggregateObject {
	// the aggregate expression
	private AggregateExpression aggregateExp;
	// pass level of aggregate
	private int passLevel;
	// can be calculated
	private boolean isEvaluable = false;
	// the register id
	private int id = -1;

	/**
	 * 
	 * @param aggr  aggregate exprssion
	 * @param level pass level of this aggregateobject
	 */
	AggregateObject(AggregateExpression aggr, int level) {
		this.aggregateExp = aggr;
		this.passLevel = level;
	}

	/**
	 * @param aggr aggregate expression
	 */
	public AggregateObject(AggregateExpression aggr) {
		this.aggregateExp = aggr;
	}

	/**
	 * 
	 * @return aggregate expression
	 */
	public AggregateExpression getAggregateExpr() {
		return aggregateExp;
	}

	/**
	 * set the aggregate expression
	 * 
	 * @param expr
	 */
	void setAggreateExpr(AggregateExpression expr) {
		this.aggregateExp = expr;
	}

	/**
	 * get the aggregate pass level
	 * 
	 * @return
	 */
	public int getPassLevel() {
		return passLevel;
	}

	/**
	 * set the aggregate pass level
	 * 
	 * @param passLevel
	 */
	public void setPassLevel(int passLevel) {
		this.passLevel = passLevel;
	}

	/**
	 * if the aggregate can be calculated, it is available,return true
	 * 
	 * @return
	 */
	public boolean isAvailable() {
		return this.isEvaluable;
	}

	/**
	 * set the aggregate available state
	 * 
	 * @param canEvaluate
	 */
	public void setAvailable(boolean canEvaluate) {
		this.isEvaluable = canEvaluate;
	}

	/**
	 * 
	 * @param id
	 */
	public void setRegisterId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return
	 */
	public int getRegisterId() {
		return this.id;
	}

	/**
	 * Compares equivalency of two aggregate expressions
	 * 
	 * @param
	 * @return
	 */
	public boolean equals(Object other) {
		if (other == null || !(other instanceof AggregateObject))
			return false;
		AggregateObject rhs = (AggregateObject) other;
		if (!aggregateExp.getAggregation().getName().equals(rhs.getAggregateExpr().getAggregation().getName())
				|| aggregateExp.getType() != rhs.getAggregateExpr().getType())
			return false;
		if (!this.getAggregateExpr().equals(rhs.getAggregateExpr()))
			return false;
		return true;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int result = 17;
		result = 37 * result + aggregateExp.getAggregation().getName().hashCode();
		result = 37 * result + this.getAggregateExpr().hashCode();
		return result;
	}
}
