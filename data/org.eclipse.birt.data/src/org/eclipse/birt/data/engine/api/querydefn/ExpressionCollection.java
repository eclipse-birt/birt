/**
 *************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
package org.eclipse.birt.data.engine.api.querydefn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IExpressionCollection;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * Default implementation of
 * {@link org.eclipse.birt.data.engine.api.IExpressionCollection} interface.
 *
 */
public class ExpressionCollection extends BaseExpression implements IExpressionCollection {

	private List combinedExpression;

	/**
	 * 
	 * @param text
	 * @throws DataException
	 */
	public ExpressionCollection(List text) {
		combinedExpression = new ArrayList();
		if (text == null || text.isEmpty()) {
			return;
		}
		for (int i = 0; i < text.size(); i++) {
			Object o = text.get(i);
			if (o instanceof ScriptExpression)
				combinedExpression.add((ScriptExpression) text.get(i));
			else
				combinedExpression.add(new ScriptExpression(o == null ? null : o.toString()));
		}
	}

	/**
	 * 
	 * @param expression
	 */
	public ExpressionCollection(IBaseExpression[] expression) {
		combinedExpression = new ArrayList();
		if (expression != null) {
			for (int i = 0; i < expression.length; i++)
				this.combinedExpression.add(expression[i]);
		}

	}

	/**
	 * see org.eclipse.birt.data.engine.api.IExpressionCollection#getExpressions()
	 */
	public Collection getExpressions() {
		return combinedExpression;
	}
}
