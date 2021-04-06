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
