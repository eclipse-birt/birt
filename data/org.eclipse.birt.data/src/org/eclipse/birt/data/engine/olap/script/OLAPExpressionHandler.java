
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
package org.eclipse.birt.data.engine.olap.script;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ICompiledScript;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.CompiledExpression;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * 
 */

public class OLAPExpressionHandler extends CompiledExpression {
	private ICompiledScript script;

	OLAPExpressionHandler(ICompiledScript script) {
		assert script != null;
		this.script = script;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.expression.CompiledExpression#evaluate(org.
	 * mozilla.javascript.Context, org.mozilla.javascript.Scriptable)
	 */
	public Object evaluate(ScriptContext context, Scriptable scope) throws DataException {
		Object temp = null;
		try {
			temp = context.evaluate(script);
			temp = JavascriptEvalUtil.convertJavascriptValue(temp);
			if (temp instanceof ScriptableObject) {
				return ((ScriptableObject) temp).getDefaultValue(null);
			}
		} catch (EvaluatorException e) {
			throw new DataException(e.details(), e);
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
		return temp;
	}

	public int getType() {
		return 0;
	}

}
