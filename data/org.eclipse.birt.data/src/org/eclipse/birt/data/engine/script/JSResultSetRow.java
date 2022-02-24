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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExprEvaluateUtil;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.ExprManager;
import org.eclipse.birt.data.engine.impl.IExecutorHelper;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * This JS object serves for the row of binding columns.
 */
public class JSResultSetRow extends ScriptableObject {
	private IResultIterator odiResult;
	private ExprManager exprManager;
	private Scriptable scope;
	private IExecutorHelper helper;
	private ScriptContext cx;
	private int currRowIndex;
	private Map valueCacheMap;

	/** */
	private static final long serialVersionUID = 649424371394281464L;

	/**
	 * @param odiResult
	 * @param exprManager
	 * @param scope
	 * @param helper
	 */
	public JSResultSetRow(IResultIterator odiResult, ExprManager exprManager, Scriptable scope, IExecutorHelper helper,
			ScriptContext cx) {
		this.odiResult = odiResult;
		this.exprManager = exprManager;
		this.scope = scope;
		this.helper = helper;
		this.cx = cx;
		this.currRowIndex = -1;
		this.valueCacheMap = new HashMap();
	}

	public JSResultSetRow(IResultIterator odiResult, JSResultSetRow jsResult) {
		this(odiResult, jsResult.exprManager, jsResult.scope, jsResult.helper, jsResult.cx);
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#getClassName()
	 */
	@Override
	public String getClassName() {
		return "ResultSetRow";
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#has(int,
	 * org.mozilla.javascript.Scriptable)
	 */
	@Override
	public boolean has(int index, Scriptable start) {
		return this.has(String.valueOf(index), start);
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#has(java.lang.String,
	 * org.mozilla.javascript.Scriptable)
	 */
	@Override
	public boolean has(String name, Scriptable start) {
		try {
			return exprManager.getExpr(name) != null;
		} catch (DataException e) {
			return false;
		}
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#get(int,
	 * org.mozilla.javascript.Scriptable)
	 */
	@Override
	public Object get(int index, Scriptable start) {
		return this.get(String.valueOf(index), start);
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String,
	 * org.mozilla.javascript.Scriptable)
	 */
	@Override
	public Object get(String name, Scriptable start) {
		if (ScriptConstants.OUTER_RESULT_KEYWORD.equalsIgnoreCase(name)) {
			if (this.helper.getParent() != null) {
				return helper.getParent().getScriptable();
			} else {
				throw Context.reportRuntimeError(
						DataResourceHandle.getInstance().getMessage(ResourceConstants.NO_OUTER_RESULTS_EXIST));
			}
		}
		int rowIndex = -1;
		try {
			rowIndex = odiResult.getCurrentResultIndex();
		} catch (BirtException e1) {
			// impossible, ignore
		}

		if (ScriptConstants.ROW_NUM_KEYWORD.equalsIgnoreCase(name) || "0".equalsIgnoreCase(name)) {
			return Integer.valueOf(rowIndex);
		}

		if (rowIndex == currRowIndex && valueCacheMap.containsKey(name)) {
			return valueCacheMap.get(name);
		} else {
			Object value = null;
			try {
				IBinding binding = this.exprManager.getBinding(name);

				if (binding == null) {
					throw Context.reportRuntimeError(DataResourceHandle.getInstance()
							.getMessage(ResourceConstants.INVALID_BOUND_COLUMN_NAME, new String[] { name }));
				}

				if (binding.getAggrFunction() != null) {
					return JavascriptEvalUtil.convertToJavascriptValue(
							DataTypeUtil.convert(this.odiResult.getAggrValue(name), binding.getDataType()), this.scope);
				}

				IBaseExpression dataExpr = this.exprManager.getExpr(name);
				if (dataExpr == null) {
					throw Context.reportRuntimeError(DataResourceHandle.getInstance()
							.getMessage(ResourceConstants.INVALID_BOUND_COLUMN_NAME, new String[] { name }));
				}
				value = ExprEvaluateUtil.evaluateValue(dataExpr, this.odiResult.getCurrentResultIndex(),
						this.odiResult.getCurrentResult(), this.scope, this.cx);
				value = JavascriptEvalUtil.convertToJavascriptValue(DataTypeUtil.convert(value, binding.getDataType()),
						this.scope);
			} catch (BirtException e) {
				throw Context.reportRuntimeError(e.getLocalizedMessage());
			}
			if (this.currRowIndex != rowIndex) {
				this.valueCacheMap.clear();
				this.currRowIndex = rowIndex;
			}
			valueCacheMap.put(name, value);
			return value;
		}
	}

	/**
	 * @param rsObject
	 * @param index
	 * @param name
	 * @return value
	 * @throws DataException
	 */
	public Object getValue(IResultObject rsObject, int index, String name) throws DataException {
		Object value = null;
		if (name.startsWith("_{")) {

			try {
				value = rsObject.getFieldValue(name);
			} catch (DataException e) {
				// ignore
			}
		} else {
			IBaseExpression dataExpr = this.exprManager.getExpr(name);
			try {
				value = ExprEvaluateUtil.evaluateValue(dataExpr, -1, rsObject, this.scope, this.cx);
				// value = JavascriptEvalUtil.convertJavascriptValue( value );
			} catch (BirtException e) {
			}
		}
		return value;
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#put(int,
	 * org.mozilla.javascript.Scriptable, java.lang.Object)
	 */
	@Override
	public void put(int index, Scriptable scope, Object value) {
		throw new IllegalArgumentException("Put value on result set row is not supported.");
	}

	/*
	 * @see org.mozilla.javascript.ScriptableObject#put(java.lang.String,
	 * org.mozilla.javascript.Scriptable, java.lang.Object)
	 */
	@Override
	public void put(String name, Scriptable scope, Object value) {
		throw new IllegalArgumentException("Put value on result set row is not supported.");
	}

	public IResultIterator getOdiResult() {
		return odiResult;
	}

	public IBinding getBinding(String name) throws DataException {
		return exprManager.getBinding(name);
	}

}
