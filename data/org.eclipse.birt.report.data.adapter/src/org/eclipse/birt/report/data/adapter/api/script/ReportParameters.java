/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

package org.eclipse.birt.report.data.adapter.api.script;

import java.util.Map;

import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Wrapper;

/**
 * Implements the "params" scriptable object to access report parameter object
 */
public class ReportParameters extends ScriptableObject {

	private Map parameters;

	private static final long serialVersionUID = 423299092113453L;
	private final static String JS_CLASS_NAME = "ReportParameters";
	private final static String LENGTH_VALUE = "length";

	/**
	 * Constructor
	 *
	 * @param module
	 */
	public ReportParameters(Map parameters, Scriptable scope) {
		assert parameters != null;
		this.setParentScope(scope);
		this.parameters = parameters;
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#getClassName()
	 */
	@Override
	public String getClassName() {
		return JS_CLASS_NAME;
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#has(java.lang.String,
	 * org.mozilla.javascript.Scriptable)
	 */
	@Override
	public boolean has(String name, Scriptable start) {
		if (parameters.containsKey(name)) {
			return true;
		}
		return false;
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String,
	 * org.mozilla.javascript.Scriptable)
	 */
	@Override
	public Object get(String name, Scriptable start) {
		if (name.equals(LENGTH_VALUE)) {
			return Integer.valueOf(parameters.size());
		}

		Object result = getScriptableParameter(name);
		if (result == null) {
			result = NOT_FOUND;
		}
		return result;
	}

	/**
	 * Support setting parameter value by following methods:
	 * <li>params["a"] = params["b"]
	 * <li>params["a"] = "value"
	 */
	@Override
	public void put(String name, Scriptable start, Object value) {
		DummyParameterAttribute attr = (DummyParameterAttribute) parameters.get(name);
		if (attr == null) {
			attr = new DummyParameterAttribute();
			parameters.put(name, attr);
		}
		if (value instanceof ReportParameter) {
			ReportParameter scriptableParameter = (ReportParameter) value;
			Object paramValue = scriptableParameter.get("value", this);
			String displayText = (String) scriptableParameter.get("displayText", this);
			attr.setValue(paramValue);
			attr.setDisplayText(displayText);
			return;
		}

		if (value instanceof Wrapper) {
			value = ((Wrapper) value).unwrap();
		}

		attr.setValue(value);

	}

	/**
	 * Get <code>ReportParameter</code> object
	 *
	 * @param name
	 * @return
	 */
	private Object getScriptableParameter(String name) {
		if (parameters.containsKey(name)) {
			return new ReportParameter(parameters, name, getParentScope());
		}
		return null;
	}
}
