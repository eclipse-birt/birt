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

package org.eclipse.birt.core.script;

import java.util.Map;

import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;
import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.JavaScriptException;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Wrapper;

public class ScriptableParameter extends BaseScriptable implements Wrapper {

	private Map parameters;
	private String parameterName;

	private final static String JS_CLASS_NAME = "ScriptableParameters";

	public final static String FIELD_VALUE = "value";
	public final static String FIELD_DISPLAY_TEXT = "displayText";

	public ScriptableParameter(Map parameters, String parameterName, Scriptable parent) {
		setParentScope(parent);
		this.parameters = parameters;
		this.parameterName = parameterName;
	}

	public Object get(String name, Scriptable scope) {
		ParameterAttribute parameter = getParameterAttribute(name);
		if (FIELD_VALUE.equals(name)) {
			return parameter.getValue();
		} else if (FIELD_DISPLAY_TEXT.equals(name)) {
			return parameter.getDisplayText();
		}
		Object value = parameter.getValue();
		Scriptable jsValue = Context.toObject(value, scope);
		Scriptable prototype = jsValue.getPrototype();
		if (prototype != null) {
			Object property = jsValue.getPrototype().get(name, jsValue);
			if (property instanceof Callable) {
				Callable callable = (Callable) property;
				return new JsValueCallable(callable, jsValue);
			}
			return jsValue.get(name, jsValue);
		} else {
			return jsValue.get(name, jsValue);
		}
	}

	public static class JsValueCallable implements Callable {
		private Callable impl;
		private Scriptable value;

		public JsValueCallable(Callable callable, Scriptable value) {
			this.impl = callable;
			this.value = value;
		}

		public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
			return impl.call(cx, scope, value, args);
		}
	}

	private ParameterAttribute getParameterAttribute(String name) {
		Object value = parameters.get(parameterName);
		if (value == null) {
			String errorMessage = CoreMessages.getFormattedString(ResourceConstants.JAVASCRIPT_PARAMETER_NOT_EXIST,
					name);
			throw new JavaScriptException(errorMessage, "<unknown>", -1);
		}
		assert value instanceof ParameterAttribute;
		ParameterAttribute parameter = (ParameterAttribute) value;
		return parameter;
	}

	public Object getDefaultValue(Class hint) {
		Object value = parameters.get(parameterName);
		if (value == null) {
			return null;
		}
		assert value instanceof ParameterAttribute;
		ParameterAttribute parameter = (ParameterAttribute) value;
		return parameter.getValue();
	}

	public boolean has(String name, Scriptable scope) {
		if (FIELD_VALUE.equals(name) || FIELD_DISPLAY_TEXT.equals(name)) {
			return true;
		}
		ParameterAttribute parameter = getParameterAttribute(name);
		Scriptable jsValue = Context.toObject(parameter.getValue(), scope);
		if (jsValue != null) {
			return jsValue.has(name, scope);
		}
		return false;
	}

	public void put(String name, Scriptable scope, Object value) {
		Object parameterValue = parameters.get(parameterName);
		if (parameterValue == null) {
			return;
		}
		assert parameterValue instanceof ParameterAttribute;
		ParameterAttribute parameter = (ParameterAttribute) parameterValue;

		if (value instanceof Wrapper) {
			value = ((Wrapper) value).unwrap();
		}

		if (FIELD_VALUE.equals(name)) {
			parameter.setValue(value);
		} else if (FIELD_DISPLAY_TEXT.equals(name)) {
			parameter.setDisplayText((String) value);
		}
	}

	public String getClassName() {
		return JS_CLASS_NAME;
	}

	public Object unwrap() {
		Object value = parameters.get(parameterName);
		if (value != null) {
			return ((ParameterAttribute) value).getValue();
		}
		return null;
	}
}
