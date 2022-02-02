/**************************************************************************
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
 *  
 **************************************************************************/

package org.eclipse.birt.data.engine.expression;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.mozilla.javascript.Scriptable;

/**
 * <code>ConstantExpression</code> represents Javascript string, number, true,
 * false, and null constant expressions. <code>getValue()</code> will return a
 * Java String, Double, Boolean, or null value.
 */
public final class ConstantExpression extends CompiledExpression {
	private Object m_value;

	protected static Logger logger = Logger.getLogger(ConstantExpression.class.getName());

	public ConstantExpression() {
		m_value = null;
		logger.logp(Level.FINER, ConstantExpression.class.getName(), "ConstantExpression",
				"ConstantExpression  starts up");
	}

	ConstantExpression(double d) {
		m_value = new Double(d);
	}

	public ConstantExpression(boolean b) {
		m_value = Boolean.valueOf(b);
	}

	public ConstantExpression(String s) {
		assert (s != null);
		m_value = s;
	}

	/**
	 * Returns the value associated with this <code>ConstantExpression</code>, which
	 * can be one of the following: a String, Double, Boolean, or <code>null</code>.
	 * 
	 * @return the value associated with this <code>ConstantExpression</code>.
	 */
	public Object getValue() {
		return m_value;
	}

	public int getType() {
		return TYPE_CONSTANT_EXPR;
	}

	public boolean equals(Object other) {
		if (other == null || !(other instanceof ConstantExpression))
			return false;

		ConstantExpression c2 = (ConstantExpression) other;
		if (m_value == null)
			return c2.m_value == null;
		else
			return m_value.equals(c2.m_value);
	}

	public int hashCode() {
		if (m_value == null)
			return 0;
		else
			return m_value.hashCode();
	}

	/**
	 * @see org.eclipse.birt.data.engine.expression.CompiledExpression#evaluate(org.mozilla.javascript.Context,
	 *      org.mozilla.javascript.Scriptable)
	 */
	public Object evaluate(ScriptContext context, Scriptable scope) throws DataException {
		return m_value;
	}
}
