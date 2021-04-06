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

package org.eclipse.birt.report.designer.internal.ui.expressions;

/**
 * The adapter class for {@link IExpressionConverter}.
 */
public abstract class AbstractExpressionConverter implements IExpressionConverter {

	public String getBindingExpression(String bindingName) {
		return null;
	}

	public String getCubeBindingExpression(String bindingName) {
		return null;
	}

	public String getDimensionExpression(String dimensionName, String levelName, String attributeName) {
		return null;
	}

	public String getMeasureExpression(String measureName) {
		return null;
	}

	public String getParameterExpression(String paramName) {
		return null;
	}

	public String getBinding(String expression) {
		return null;
	}

	public String getResultSetColumnExpression(String columnName) {
		return null;
	}

	public String getConstantExpression(String value, String dataType) {
		return value;
	}

}