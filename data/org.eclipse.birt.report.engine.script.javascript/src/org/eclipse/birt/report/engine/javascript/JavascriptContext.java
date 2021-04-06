/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.javascript;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.script.IScriptContext;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.core.script.ScriptableParameters;
import org.eclipse.birt.report.engine.executor.PageVariable;
import org.eclipse.birt.report.engine.executor.ScriptablePageVariables;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

public class JavascriptContext implements IScriptContext {

	/**
	 * for logging
	 */
	protected static Logger logger = Logger.getLogger(JavascriptContext.class.getName());

	/**
	 * The JavaScript scope used for script execution
	 */
	protected Scriptable scope;

	private ScriptContext scriptContext;

	public JavascriptContext(ScriptContext scriptContext, Scriptable scope) {
		if (scope == null) {
			throw new IllegalArgumentException("Scope can not be null.");
		}
		this.scope = scope;
		this.scriptContext = scriptContext;
	}

	/**
	 * @param name  the name of a property
	 * @param value the value of a property
	 */
	public void setAttribute(String name, Object value) {
		value = wrap(scope, name, value);
		Object jsValue = Context.javaToJS(value, scope);
		scope.put(name, scope, jsValue);
	}

	public void removeAttribute(String name) {
		scope.delete(name);
	}

	public Scriptable getScope() {
		return scope;
	}

	private Object wrap(Scriptable scope, String name, Object value) {
		if (scriptContext.getParent() != null) {
			return value;
		}
		if (ExpressionUtil.PARAMETER_INDICATOR.equals(name) && value instanceof HashMap<?, ?>) {
			return new ScriptableParameters((Map<String, Object>) value, scope);
		} else if (ExpressionUtil.VARIABLE_INDICATOR.equals(name)) {
			return new ScriptablePageVariables((Map<String, PageVariable>) value, scope);
		}
		return value;
	}

	private Object javaToJs(Object value, Scriptable scope) {
		return Context.javaToJS(value, scope);
	}
}
