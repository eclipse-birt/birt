/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.mozilla.javascript.Scriptable;

/**
 * Used for these invalid expressions input by users.
 */
public class InvalidExpression extends CompiledExpression {
	private DataException cause;
	protected static Logger logger = Logger.getLogger(InvalidExpression.class.getName());

	InvalidExpression(DataException cause) {
		assert cause != null;
		this.cause = cause;
		logger.logp(Level.FINER, InvalidExpression.class.getName(), "InvalidExpression", "InvalidExpression starts up");
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.CompiledExpression#getType()
	 */
	public int getType() {
		return TYPE_INVALID_EXPR;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.impl.CompiledExpression#evaluate(org.mozilla.
	 * javascript.Context, org.mozilla.javascript.Scriptable)
	 */
	public Object evaluate(ScriptContext context, Scriptable scope) throws DataException {
		throw cause;
	}

}
