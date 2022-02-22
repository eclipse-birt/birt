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
package org.eclipse.birt.report.data.adapter.internal.adapter;

import java.util.List;

import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ParamBindingHandle;

/**
 * Adaptor for Input Parameter Binding
 */
public class InputParamBindingAdapter extends InputParameterBinding {
	/**
	 * Constructs instance based on Model ParamBindingHandle
	 *
	 * @throws AdapterException
	 */
	public InputParamBindingAdapter(IModelAdapter adapter, ParamBindingHandle modelHandle) throws AdapterException {
		this(adapter, modelHandle.getParamName(), modelHandle.getExpressionListHandle().getListValue());
	}

	/**
	 * Constructs instance based on param name and expression
	 *
	 * @throws AdapterException
	 */
	public InputParamBindingAdapter(IModelAdapter adapter, String paramName, List<Expression> expr)
			throws AdapterException {
		super(paramName, expr.size() > 0 ? adapter.adaptExpression(expr.get(0)) : null);
	}

	/**
	 * Constructs instance base on param name, expression, and type.
	 *
	 * @param paramName
	 * @param bindingExpr
	 * @param type
	 */
	public InputParamBindingAdapter(String paramName, ExpressionAdapter bindingExpr) {
		super(paramName, bindingExpr);
	}
}
