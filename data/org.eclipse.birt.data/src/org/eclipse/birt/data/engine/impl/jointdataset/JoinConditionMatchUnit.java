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
package org.eclipse.birt.data.engine.impl.jointdataset;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Scriptable;

/**
 * Utility class which is used by IJoinConditionMatcher.
 */
public class JoinConditionMatchUnit {
	private IScriptExpression expr = null;
	private Scriptable scope = null;
	private ScriptContext context;

	/**
	 * Constructor
	 *
	 * @param expr
	 * @param scope
	 */
	public JoinConditionMatchUnit(IScriptExpression expr, Scriptable scope, ScriptContext context) {
		this.expr = expr;
		this.scope = scope;
		this.context = context;
	}

	/**
	 * Get the value of current column.
	 *
	 * @return
	 * @throws DataException
	 */
	public Object getColumnValue() throws DataException {
		Object leftValue = ScriptEvalUtil.evalExpr(this.expr, context.newContext(this.scope),
				ScriptExpression.defaultID, 0);

		return leftValue;
	}
}
