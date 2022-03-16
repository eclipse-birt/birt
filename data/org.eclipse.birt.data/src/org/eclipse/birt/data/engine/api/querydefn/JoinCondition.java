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
		if (!(operator == IJoinCondition.OP_EQ)) {
			throw new DataException(ResourceConstants.INVALID_JOIN_OPERATOR);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.IJoinConditionExpression#getLeftExpression()
	 */
	@Override
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
	@Override
	public IScriptExpression getRightExpression() {
		return right;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IJoinConditionExpression#getOperator()
	 */
	@Override
	public int getOperator() {
		return operator;
	}

}
