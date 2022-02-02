/**************************************************************************
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
 *  
 **************************************************************************/

package org.eclipse.birt.data.engine.expression;

import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Scriptable;

/**
 * An expression that translates to a direct column access on the "row"
 * javascript object.
 */
public final class ColumnReferenceExpression extends CompiledExpression {
	// Field name of the Rowset
	private String m_columnName;
	private int m_columnIndex;
	private String rowIndicator = "row";
	private int dataType;
	protected static Logger logger = Logger.getLogger(ColumnReferenceExpression.class.getName());

	ColumnReferenceExpression(String rowInd, String columnName) {
		logger.entering(ColumnReferenceExpression.class.getName(), "ColumnReferenceExpression", columnName);
		assert (columnName != null && columnName.length() != 0);
		m_columnName = columnName;
		m_columnIndex = -1;
		rowIndicator = rowInd;
		logger.exiting(ColumnReferenceExpression.class.getName(), "ColumnReferenceExpression");
		this.dataType = DataType.UNKNOWN_TYPE;
	}

	ColumnReferenceExpression(String rowInd, int columnIndex) {
		logger.entering(ColumnReferenceExpression.class.getName(), "ColumnReferenceExpression",
				Integer.valueOf(columnIndex));
		assert (columnIndex >= 0);
		m_columnIndex = columnIndex;
		rowIndicator = rowInd;
		logger.exiting(ColumnReferenceExpression.class.getName(), "ColumnReferenceExpression");
	}

	public int getType() {
		return TYPE_DIRECT_COL_REF;
	}

	public String getColumnName() {
		return m_columnName;
	}

	public int getColumnindex() {
		return m_columnIndex;
	}

	public boolean isIndexed() {
		return (m_columnIndex != -1);
	}

	public boolean equals(Object other) {
		if (other == null || !(other instanceof ColumnReferenceExpression))
			return false;

		ColumnReferenceExpression expr2 = (ColumnReferenceExpression) other;

		if (dataType != expr2.dataType)
			return false;
		if (m_columnName != null)
			return (m_columnName.equals(expr2.m_columnName));
		else
			return m_columnIndex == expr2.m_columnIndex;
	}

	public int hashCode() {
		if (m_columnName != null)
			return m_columnName.hashCode();
		else
			return m_columnIndex;
	}

	/**
	 * @see org.eclipse.birt.data.engine.expression.CompiledExpression#evaluate(org.mozilla.javascript.Context,
	 *      org.mozilla.javascript.Scriptable)
	 */
	public Object evaluate(ScriptContext context, Scriptable scope) throws DataException {
		// This method should not normally be called.

		// Assume the JS "row" variable has been correctly set up in scope.
		// Evaluate the expression row[index] or row["name"]
		StringBuffer expr = new StringBuffer(this.rowIndicator + "[");
		if (isIndexed()) {
			expr.append(m_columnIndex);
		} else {
			expr.append('"');
			expr.append(JavascriptEvalUtil.transformToJsConstants(m_columnName));
			expr.append('"');
		}
		expr.append(']');
		try {
			return DataTypeUtil.convert(
					ScriptEvalUtil.evaluateJSAsExpr(context, scope, expr.toString(), ScriptExpression.defaultID, 0),
					this.dataType);
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}

	/**
	 * 
	 * @param type
	 */
	public void setDataType(int type) {
		this.dataType = type;
	}

	/**
	 * 
	 * @return
	 */
	public int getDataType() {
		return this.dataType;
	}
}
