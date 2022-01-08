/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.engine.util;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.ir.Expression;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;

public class ExpressionUtil {

	public static Expression createUserProperty(DesignElementHandle handle, UserPropertyDefn userDef) {

		String propName = userDef.getName();
		String valueExpr = handle.getStringProperty(propName);
		switch (userDef.getTypeCode()) {
		case IPropertyType.SCRIPT_TYPE:
		case IPropertyType.EXPRESSION_TYPE:
			ExpressionHandle property = handle.getExpressionProperty(propName);
			if (property == null) {
				return null;
			}
			Object expression = property.getValue();
			if (expression == null) {
				expression = userDef.getDefault();
			}
			if (expression instanceof org.eclipse.birt.report.model.api.Expression) {
				return createExpression((org.eclipse.birt.report.model.api.Expression) expression);
			}
			return null;
		case IPropertyType.NUMBER_TYPE:
		case IPropertyType.INTEGER_TYPE:
		case IPropertyType.FLOAT_TYPE:
			return createConstant(DataType.DOUBLE_TYPE, valueExpr);
		case IPropertyType.BOOLEAN_TYPE:
			return createConstant(DataType.BOOLEAN_TYPE, valueExpr);

		case IPropertyType.DATE_TIME_TYPE:
			return createConstant(DataType.DATE_TYPE, valueExpr);

		default:
			return createConstant(DataType.STRING_TYPE, valueExpr);
		}
	}

	public static Expression createExpression(org.eclipse.birt.report.model.api.Expression expr) {
		if (expr != null) {
			String type = expr.getType();
			if (ExpressionType.CONSTANT.equals(type)) {
				String text = expr.getStringExpression();
				return Expression.newConstant(-1, text);
			} else {
				String text = expr.getStringExpression();
				if (text != null) {
					text = text.trim();
					if (text.length() > 0) {
						return Expression.newScript(type, text);
					}
				}
			}
		}
		return null;
	}

	public static Expression createConstant(int type, String expr) {
		// we can't trim the expression as the white space has means in
		// constant
		if (expr != null) {
			return Expression.newConstant(type, expr);
		}
		return null;
	}

	public static Expression createExpression(String expr) {
		if (expr != null) {
			expr = expr.trim();
			if (expr.length() > 0) {
				return Expression.newScript("javascript", expr);
			}
		}
		return null;
	}

	public static Object evaluate(ExecutionContext context, Expression expr) {
		try {
			return context.evaluate(expr);
		} catch (BirtException ex) {
			context.addException(ex);
		}
		return null;
	}
}
