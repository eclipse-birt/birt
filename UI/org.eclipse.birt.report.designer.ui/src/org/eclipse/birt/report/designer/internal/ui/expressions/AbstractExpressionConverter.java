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

package org.eclipse.birt.report.designer.internal.ui.expressions;

/**
 * The adapter class for {@link IExpressionConverter}.
 */
public abstract class AbstractExpressionConverter implements IExpressionConverter {

	@Override
	public String getBindingExpression(String bindingName) {
		return null;
	}

	@Override
	public String getCubeBindingExpression(String bindingName) {
		return null;
	}

	@Override
	public String getDimensionExpression(String dimensionName, String levelName, String attributeName) {
		return null;
	}

	@Override
	public String getMeasureExpression(String measureName) {
		return null;
	}

	@Override
	public String getParameterExpression(String paramName) {
		return null;
	}

	@Override
	public String getBinding(String expression) {
		return null;
	}

	@Override
	public String getResultSetColumnExpression(String columnName) {
		return null;
	}

	@Override
	public String getConstantExpression(String value, String dataType) {
		return value;
	}

}
