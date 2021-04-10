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
package org.eclipse.birt.data.engine.api.querydefn;

import org.eclipse.birt.data.engine.api.IJoinCondition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

/**
 * An implementation of IJoinConditionExpression.
 */
public class JoinCondition implements IJoinCondition {
	private IScriptExpression left;
	private IScriptExpression right;
	private int operator;

	/**
	 * Constructor.
	 * 
	 * @param left
	 * @param right
	 * @param op
	 * @throws DataException
	 */
	public JoinCondition(IScriptExpression left, IScriptExpression right, int op) throws DataException {
		validateJoinOperator(op);
		this.left = left;
		this.right = right;
		this.operator = op;
	}

	private void validateJoinOperator(int operator) throws DataException {
		if (!(operator == IJoinCondition.OP_EQ))
			throw new DataException(ResourceConstants.INVALID_JOIN_OPERATOR);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.IJoinConditionExpression#getLeftExpression()
	 */
	public IScriptExpression getLeftExpression() {
		return left;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.IJoinConditionExpression#getRightExpression(
	 * )
	 */
	public IScriptExpression getRightExpression() {
		return right;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.IJoinConditionExpression#getOperator()
	 */
	public int getOperator() {
		return operator;
	}

}
