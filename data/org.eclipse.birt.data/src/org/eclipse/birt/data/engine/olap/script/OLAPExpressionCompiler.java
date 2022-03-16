
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
package org.eclipse.birt.data.engine.olap.script;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IExpressionCollection;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.BaseExpression;
import org.eclipse.birt.data.engine.core.DataException;

/**
 *
 */

public class OLAPExpressionCompiler {
	/**
	 *
	 * @param cx
	 * @throws DataException
	 */
	public static void compile(ScriptContext cx, IBaseExpression expr) throws DataException {
		if (expr instanceof IConditionalExpression) {
			prepareScriptExpression(cx, ((IConditionalExpression) expr).getExpression());
			prepareScriptExpression(cx, ((IConditionalExpression) expr).getOperand1());
			prepareScriptExpression(cx, ((IConditionalExpression) expr).getOperand2());
		} else if (expr instanceof IScriptExpression) {
			prepareScriptExpression(cx, (IScriptExpression) expr);
		}
	}

	/**
	 *
	 * @param cx
	 * @param expr1
	 * @throws DataException
	 */
	private static void prepareScriptExpression(ScriptContext cx, IBaseExpression expr1) throws DataException {
		try {

			if (expr1 == null) {
				return;
			}
			if (expr1 instanceof IScriptExpression) {
				String exprText = ((IScriptExpression) expr1).getText();
				if (expr1.getHandle() == null && !(BaseExpression.constantId.equals(expr1.getScriptId()))) {
					expr1.setHandle(new OLAPExpressionHandler(cx.compile(expr1.getScriptId(), null, 0, exprText)));
				}
			} else if (expr1 instanceof IExpressionCollection) {
				Object[] exprs = ((IExpressionCollection) expr1).getExpressions().toArray();
				for (int i = 0; i < exprs.length; i++) {
					prepareScriptExpression(cx, (IBaseExpression) exprs[i]);
				}
			}
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}
}
