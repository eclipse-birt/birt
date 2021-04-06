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

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;

public class ExpressionAdapter extends ScriptExpression {
	// private IModelAdapter.ExpressionLocation el =
	// IModelAdapter.ExpressionLocation.TABLE;

	public ExpressionAdapter(Expression expr) {
		this(expr, DataType.ANY_TYPE);
	}

	public ExpressionAdapter(Expression expr, IModelAdapter.ExpressionLocation el) {
		this(expr, DataType.ANY_TYPE);
		// this.el = el;
	}

	public ExpressionAdapter(String expr, String returnType) {
		super(expr, org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelDataType(returnType));

	}

	/**
	 * Constructs an expression with provided text and return data type Data type is
	 * defined as Dte enumeration value
	 */
	public ExpressionAdapter(Expression expr, int returnType) {
		super(expr.getStringExpression(), returnType);
		this.setScriptId(expr.getType());
	}

	/**
	 * Constructs an expression with provided text and return data type Data type is
	 * defined as a Model data type string
	 */
	public ExpressionAdapter(Expression expr, String returnType) {
		super(expr.getStringExpression(),
				org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelDataType(returnType));
		this.setScriptId(expr.getType());
	}

	/**
	 * Constructs an expression based on Model computed column handle
	 */
	public ExpressionAdapter(ComputedColumnHandle ccHandle) {
		super(ccHandle.getExpression(),
				org.eclipse.birt.report.data.adapter.api.DataAdapterUtil.adaptModelDataType(ccHandle.getDataType()));
		this.setScriptId(ccHandle.getExpressionProperty(ComputedColumn.EXPRESSION_MEMBER).getType());
	}

}
