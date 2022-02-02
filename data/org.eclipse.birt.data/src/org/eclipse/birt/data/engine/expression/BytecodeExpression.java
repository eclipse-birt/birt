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

import java.util.logging.Logger;

import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;

public abstract class BytecodeExpression extends CompiledExpression {
	private Script m_script;

	protected static Logger logger = Logger.getLogger(BytecodeExpression.class.getName());

	/**
	 * Sets the compiled Javascript bytecode for this
	 * <code>BytecodeExpression</code>.
	 * 
	 * @param script the compiled Javascript bytecode.
	 */
	void setScript(Script script) {
		assert script != null;
		m_script = script;
	}

	/**
	 * Evaluates the compiled byte code
	 */
	public Object evaluate(ScriptContext context, Scriptable scope) throws DataException {
		try {
			Object result = JavascriptEvalUtil
					.convertJavascriptValue(m_script.exec(Context.getCurrentContext(), scope));
			return result;
		} catch (RhinoException e) {
			throw DataException.wrap(JavascriptEvalUtil.wrapRhinoException(e, "<compiled script>", null, 0));
		}
	}

	/**
	 * Return the group level of the bytecode expression.
	 * 
	 * @return
	 */
	public abstract int getGroupLevel();

}
