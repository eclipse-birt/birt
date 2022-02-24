/*
 *************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
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
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api;

import java.util.Collection;

import org.eclipse.birt.data.engine.api.querydefn.BaseExpression;

public class CollectionConditionalExpression extends BaseExpression implements ICollectionConditionalExpression {
	private Collection<IScriptExpression> expr;
	private Collection<Collection<IScriptExpression>> operand;
	private int operator;

	public CollectionConditionalExpression(Collection<IScriptExpression> expr, int operator,
			Collection<Collection<IScriptExpression>> operand) {
		this.expr = expr;
		this.operand = operand;
		this.operator = operator;
	}

	public Collection<IScriptExpression> getExpr() {
		return this.expr;
	}

	public Collection<Collection<IScriptExpression>> getOperand() {
		return this.operand;
	}

	public int getOperator() {
		return this.operator;
	}
}
