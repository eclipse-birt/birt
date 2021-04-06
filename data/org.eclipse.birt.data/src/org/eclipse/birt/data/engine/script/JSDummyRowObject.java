/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.script;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.expression.ExprEvaluateUtil;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.ExprManager;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * 
 */
public class JSDummyRowObject extends ScriptableObject {
	private ExprManager exprManager;
	private Scriptable scope;
	private Scriptable parent;

	private Map valueCacheMap;
	private ScriptContext cx;
	/** */
	private static final long serialVersionUID = -7841512175200620757L;

	/**
	 * @param exprManager
	 * @param scope
	 */
	public JSDummyRowObject(ExprManager exprManager, Scriptable scope, Scriptable parent, ScriptContext cx) {
		this.exprManager = exprManager;
		this.scope = scope;
		this.parent = parent;
		this.cx = cx;
		this.valueCacheMap = new HashMap();
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#getClassName()
	 */
	public String getClassName() {
		return "row";
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String,
	 * org.mozilla.javascript.Scriptable)
	 */
	public Object get(String name, Scriptable start) {
		if (ScriptConstants.OUTER_RESULT_KEYWORD.equalsIgnoreCase(name)) {
			if (parent == null) {
				throw Context.reportRuntimeError(
						DataResourceHandle.getInstance().getMessage(ResourceConstants.NO_OUTER_RESULTS_EXIST));
			} else {
				return parent;
			}
		}

		if (valueCacheMap.containsKey(name))
			return valueCacheMap.get(name);

		try {
			IBaseExpression baseExpr = exprManager.getExpr(name);

			Object value = ExprEvaluateUtil.evaluateRawExpression(baseExpr, scope, cx);
			Object obValue = JavascriptEvalUtil.convertToJavascriptValue(value, scope);
			valueCacheMap.put(name, obValue);

			return obValue;
		} catch (BirtException e) {
			return null;
		}
	}

}
