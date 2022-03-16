/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.executor;

import java.util.Map;

import org.eclipse.birt.core.script.BaseScriptable;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Wrapper;

public class ScriptablePageVariables extends BaseScriptable {

	private Map<String, PageVariable> variables;

	private static final String JS_CLASS_NAME = "ScriptableVariables";

	public ScriptablePageVariables(Map<String, PageVariable> variables, Scriptable scope) {
		setParentScope(scope);
		this.variables = variables;
	}

	@Override
	public Object get(String name, Scriptable start) {
		PageVariable variable = variables.get(name);
		if (variable != null) {
			return variable.getValue();
		}
		String errorMessage = "Report variable\"" + name + "\" does not exist";
		throw new JavaScriptException(errorMessage, "<unknown>", -1);
	}

	@Override
	public Object get(int index, Scriptable start) {
		return get(String.valueOf(index), start);
	}

	@Override
	public boolean has(String name, Scriptable start) {
		return variables.get(name) != null;
	}

	/**
	 * Support setting parameter value by following methods:
	 */
	@Override
	public void put(String name, Scriptable start, Object value) {
		PageVariable variable = variables.get(name);
		if (variable != null) {
			if (value instanceof Wrapper) {
				value = ((Wrapper) value).unwrap();
			}
			variable.setValue(value);
			return;
		}
		String errorMessage = "Report variable\"" + name + "\" does not exist";
		throw new JavaScriptException(errorMessage, "<unknown>", -1);
	}

	@Override
	public String getClassName() {
		return JS_CLASS_NAME;
	}
}
