/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.report.data.adapter.api.script;

import java.util.Map;

import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.Wrapper;

/**
 * 
 * Implements the "params" scriptable object to access report parameter
 * attributes value
 * 
 */
public class ReportParameter extends ScriptableObject implements Wrapper {

	private static final long serialVersionUID = -738537100416474285L;
	public final static String FIELD_VALUE = "value";
	public final static String FIELD_DISPLAY_TEXT = "displayText";

	private Map params;
	private String parameterName;

	public static Scriptable jsStrPrototype = null;

	/**
	 * Constructor
	 * 
	 * @param params
	 */
	public ReportParameter(Map parameters, String name, Scriptable scope) {
		assert params != null;
		this.setParentScope(scope);
		this.params = parameters;
		this.parameterName = name;
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#getClassName()
	 */
	public String getClassName() {
		return "ReportParameter";
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#has(java.lang.String,
	 * org.mozilla.javascript.Scriptable)
	 */
	public boolean has(String name, Scriptable scope) {
		if (FIELD_VALUE.equals(name) || FIELD_DISPLAY_TEXT.equals(name)) {
			return true;
		}
		if (jsStrPrototype == null) {
			jsStrPrototype = Context.toObject("", scope);
			if (jsStrPrototype != null) {
				jsStrPrototype = jsStrPrototype.getPrototype();
			}
		}
		return jsStrPrototype != null && jsStrPrototype.has(name, scope);
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String,
	 * org.mozilla.javascript.Scriptable)
	 */
	public Object get(String name, Scriptable start) {
		DummyParameterAttribute attr = (DummyParameterAttribute) this.params.get(this.parameterName);
		if (attr == null) {
			return null;
		}

		if (FIELD_VALUE.equals(name)) {
			return attr.getValue();
		} else if (FIELD_DISPLAY_TEXT.equals(name)) {
			return attr.getDisplayText();
		}

		Object value = attr.getValue();
		if (value instanceof Scriptable) {
			Scriptable jsValue = (Scriptable) value;
			Object property = jsValue.getPrototype().get(name, jsValue);
			if (property instanceof Callable) {
				Callable callable = (Callable) property;
				return new JsValueCallable(callable);
			}
			return jsValue.get(name, jsValue);
		} else {
			Scriptable jsValue = Context.toObject(value, start);
			return jsValue.get(name, jsValue);
		}

	}

	static class JsValueCallable implements Callable {
		private Callable impl;

		public JsValueCallable(Callable callable) {
			this.impl = callable;
		}

		public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
			ReportParameter parameter = (ReportParameter) thisObj;
			Scriptable value = (Scriptable) parameter.unwrap();
			return impl.call(cx, scope, value, args);
		}
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#put(java.lang.String,
	 * org.mozilla.javascript.Scriptable, java.lang.Object)
	 */
	public void put(String name, Scriptable scope, Object value) {
		Object parameterValue = this.params.get(parameterName);
		if (parameterValue == null) {
			return;
		}
		if (FIELD_VALUE.equals(name)) {
			DummyParameterAttribute attr = (DummyParameterAttribute) parameterValue;
			attr.setValue(JavascriptEvalUtil.convertJavascriptValue(value));
		} else if (FIELD_DISPLAY_TEXT.equals(name)) {
			DummyParameterAttribute attr = (DummyParameterAttribute) params.get(parameterName);
			attr.setDisplayText((String) value);
		}
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#getDefaultValue(java.lang.Class)
	 */
	public Object getDefaultValue(Class hint) {
		Object value = params.get(parameterName);
		if (value == null) {
			return null;
		}
		assert value instanceof DummyParameterAttribute;
		DummyParameterAttribute parameter = (DummyParameterAttribute) value;
		return parameter.getValue();
	}

	/*
	 * @see org.mozilla.javascript.Wrapper#unwrap()
	 */
	public Object unwrap() {
		Object value = params.get(parameterName);
		if (value != null) {
			return ((DummyParameterAttribute) value).getValue();
		}
		return null;
	}
}
