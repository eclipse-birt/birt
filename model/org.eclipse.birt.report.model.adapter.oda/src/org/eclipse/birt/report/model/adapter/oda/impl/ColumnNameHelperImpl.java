/*******************************************************************************
 * Copyright (c) 2004 - 2010 Actuate Corporation.
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

package org.eclipse.birt.report.model.adapter.oda.impl;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.simpleapi.IExpressionType;
import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * Class to help resolve column name conversion.
 */
public class ColumnNameHelperImpl {

	/**
	 * Constructor
	 */
	protected ColumnNameHelperImpl() {
	}

	/**
	 * Extracts the column name from the given column.
	 *
	 * @param column the column to extract
	 * @return the column name, or null if it cannot be extracted.
	 */
	public String extractColumnName(Object column) {
		if (column instanceof String) {
			return extractColumnName((String) column);
		}

		if (column instanceof Expression) {
			return extractColumnName((Expression) column);
		}

		return null;
	}

	/**
	 * Extracts the column name from the given column expression.
	 *
	 * @param columnExpr the column expression to extract
	 * @return the column name, or null if it cannot be extracted.
	 */
	public String extractColumnName(Expression columnExpr) {
		if (columnExpr != null) {
			String type = columnExpr.getType();
			String value = columnExpr.getStringExpression();
			if (IExpressionType.JAVASCRIPT.equalsIgnoreCase(type)) {
				return extractColumnName(value);
			}
			return value;
		}
		return null;
	}

	/**
	 * Converts the ROM column expression to ODA column
	 *
	 * @param columnExpr the column expression to convert
	 * @return the ODA column converted.
	 */
	public String extractColumnName(String columnExpr) {
		if (!StringUtil.isBlank(columnExpr)) {
			String columnName = checkColumnName(columnExpr);
			if (StringUtil.isBlank(columnName)) {
				return columnExpr;
			}
			return columnName;
		}
		return null;
	}

	/**
	 * Checks if column name can extract from the given column expression.
	 *
	 * @param columnExpr the column expression
	 * @return the column name, or null if cannot extract.
	 */
	protected String checkColumnName(String columnExpr) {
		String columnName = null;
		if (!StringUtil.isBlank(columnExpr)) {
			try {
				columnName = ExpressionUtil.getColumnName(columnExpr);
			} catch (BirtException e) {
			}
			if (columnName == null) {
				try {
					columnName = ExpressionUtil.getColumnBindingName(columnExpr);
				} catch (BirtException e) {
				}
			}
		}
		return columnName;
	}

	/**
	 * Creates a column expression with given expression type.
	 *
	 * @param columnName the column name
	 * @param type       the expression type
	 * @return the column expression created.
	 */
	public Expression createColumnExpression(String column, String type) {
		if (StringUtil.isBlank(column)) { // empty check
			return null;
		}
		if (StringUtil.isBlank(type)) {
			type = IExpressionType.JAVASCRIPT;
		}
		if (!IExpressionType.JAVASCRIPT.equals(type)) {
			return null;
		}
		String columnName = checkColumnName(column);
		if (StringUtil.isBlank(columnName)) {
			columnName = column;
		}

		return new Expression(ExpressionUtil.createDataSetRowExpression(columnName), type);
	}
}
