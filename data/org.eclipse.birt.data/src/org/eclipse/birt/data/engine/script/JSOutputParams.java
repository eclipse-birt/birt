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

package org.eclipse.birt.data.engine.script;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.data.engine.impl.DataSetRuntime;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * JS object for Output parameter of prepared query.
 */
public class JSOutputParams extends ScriptableObject {
	private DataSetRuntime dataSet;

	/** */
	private static final long serialVersionUID = 2535883419826794186L;

	/**
	 * @param dataSet
	 */
	public JSOutputParams(DataSetRuntime dataSet) {
		this.dataSet = dataSet;
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#getClassName()
	 */
	@Override
	public String getClassName() {
		return "OutputParams";
	}

	/*
	 * @see org.mozilla.javascript.Scriptable#get(int,
	 * org.mozilla.javascript.Scriptable)
	 */
	@Override
	public Object get(int index, Scriptable scope) {
		// BIRT output parameters are accessible by name only
		return NOT_FOUND;
	}

	/*
	 * @see org.mozilla.javascript.Scriptable#get(java.lang.String,
	 * org.mozilla.javascript.Scriptable)
	 */
	@Override
	public Object get(String name, Scriptable scope) {
		try {
			Object paramValue = dataSet.getOutputParameterValue(name);
			return JavascriptEvalUtil.convertToJavascriptValue(paramValue, dataSet.getSharedScope());
		} catch (BirtException e) {
			throw Context.reportRuntimeError(e.getLocalizedMessage());
		}

	}

	/*
	 * @see org.mozilla.javascript.Scriptable#put(int,
	 * org.mozilla.javascript.Scriptable, java.lang.Object)
	 */
	@Override
	public void put(int index, Scriptable scope, Object value) {
		throw new IllegalArgumentException("Put value on output parameter object is not supported.");
	}

	/*
	 * @see org.mozilla.javascript.Scriptable#put(java.lang.String,
	 * org.mozilla.javascript.Scriptable, java.lang.Object)
	 */
	@Override
	public void put(String name, Scriptable scope, Object value) {
		try {
			dataSet.setOutputParameterValue(name, value);
		} catch (BirtException e) {
			// needs to log here.
		}
	}

	/**
	 * @see org.mozilla.javascript.ScriptableObject#has(int,
	 *      org.mozilla.javascript.Scriptable)
	 */
	@Override
	public boolean has(int index, Scriptable start) {
		return super.has(index, start);
	}

	/**
	 * @see org.mozilla.javascript.ScriptableObject#has(java.lang.String,
	 *      org.mozilla.javascript.Scriptable)
	 */
	@Override
	public boolean has(String name, Scriptable start) {
		if (dataSet.hasOutputParameter(name)) {
			return true;
		}
		return super.has(name, start);
	}

}
