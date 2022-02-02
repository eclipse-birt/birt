/*******************************************************************************
 * Copyright (c) 2004, 2012 Actuate Corporation.
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
package org.eclipse.birt.data.engine.expression;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.data.ExpressionHelper;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IExpressionCollection;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.BaseExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.DataSetRuntime;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.script.JSResultSetRow;
import org.eclipse.birt.data.engine.script.NEvaluator;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Scriptable;

/**
 * None-static version of <code> ExprEvaluateUtil </code>. The expression
 * evaluator optimizes the expression evaluation by reusing parsed expression.
 * 
 */
public class ExprEvaluator {

	private ExpressionHelper exprHelper;

	public ExprEvaluator() {
		exprHelper = new ExpressionHelper();
	}

	public void close() {
		exprHelper.close();
	}

	/**
	 * @param dataExpr
	 * @param odiResult
	 * @param scope
	 * @param logger
	 * @return
	 * @throws BirtException
	 */
	public Object evaluateExpression(IBaseExpression dataExpr, IResultIterator odiResult, Scriptable scope,
			ScriptContext cx) throws BirtException {
		return ExprEvaluateUtil.evaluateExpression(dataExpr, odiResult, scope, cx);
	}

	public Object evaluateCompiledExpression(CompiledExpression expr, IResultObject ro, int currentIndex,
			Scriptable scope, ScriptContext cx) throws DataException {
		return ExprEvaluateUtil.evaluateCompiledExpression(expr, ro, currentIndex, scope, cx);
	}

	/**
	 * @param expr
	 * @param odiResult
	 * @param scope
	 * @return
	 * @throws DataException
	 */
	public Object evaluateCompiledExpression(CompiledExpression expr, IResultIterator odiResult, Scriptable scope,
			ScriptContext cx) throws DataException {
		return ExprEvaluateUtil.evaluateCompiledExpression(expr, odiResult, scope, cx);
	}

	/**
	 * Evaluate non-compiled expression
	 * 
	 * @param dataExpr
	 * @param scope
	 * @return the value of raw data type, Java or Java Script
	 * @throws BirtException
	 */
	public Object evaluateRawExpression(IBaseExpression dataExpr, Scriptable scope, ScriptContext cx)
			throws BirtException {
		return ExprEvaluateUtil.evaluateRawExpression(dataExpr, scope, cx);
	}

	/**
	 * @param dataExpr
	 * @param scope
	 * @return the value of Java data type
	 * @throws BirtException
	 */
	public Object evaluateRawExpression2(IBaseExpression dataExpr, Scriptable scope, ScriptContext cx,
			DataSetRuntime dataSet) throws BirtException {
		return doEvaluateRawExpression(dataExpr, scope, true, cx, dataSet);
	}

	/**
	 * @param dataExpr
	 * @param scope
	 * @return
	 * @throws BirtException
	 */
	private Object doEvaluateRawExpression(IBaseExpression dataExpr, Scriptable scope, boolean javaType,
			ScriptContext cx) throws BirtException {
		return ExprEvaluateUtil.doEvaluateRawExpression(dataExpr, scope, javaType, cx);
	}

	/**
	 * 
	 * @param dataExpr
	 * @param cx
	 * @param isRow    true:row["xxx"]; false:dataSetRow["xxx"]
	 * @return
	 * @throws BirtException
	 */
	private String extractDirectColumn(IBaseExpression dataExpr, ScriptContext cx, boolean isRow) throws BirtException {
		if (dataExpr instanceof IScriptExpression && !BaseExpression.constantId.equals(dataExpr.getScriptId())) {
			String exprText = ((IScriptExpression) dataExpr).getText();
			if (isRow) {
				return exprHelper.getColumnBindingName(exprText);
			} else {
				return exprHelper.getColumnName(exprText);
			}
		}
		return null;
	}

	/**
	 * @param dataExpr
	 * @param scope
	 * @return
	 * @throws BirtException
	 */
	private Object doEvaluateRawExpression(IBaseExpression dataExpr, Scriptable scope, boolean javaType,
			ScriptContext cx, DataSetRuntime dataSet) throws BirtException {
		if (dataSet == null) {
			return doEvaluateRawExpression(dataExpr, scope, javaType, cx);
		}
		String dataSetColumn = extractDirectColumn(dataExpr, cx, false);
		if (dataSetColumn != null) {
			if (dataSet.getCurrentRow() != null
					&& dataSet.getCurrentRow().getResultClass().getFieldIndex(dataSetColumn) >= 0) {
				Object value = dataSet.getCurrentRow().getFieldValue(dataSetColumn);
				return DataTypeUtil.convert(value, dataExpr.getDataType());
			}
		}

		String rowName = extractDirectColumn(dataExpr, cx, true);
		if (rowName != null) {
			Scriptable scriptable = dataSet.getJSResultRowObject();
			if (scriptable instanceof JSResultSetRow) {
				JSResultSetRow resultSetRow = (JSResultSetRow) scriptable;
				IBinding b = resultSetRow.getBinding(rowName);

				if (b != null && b.getAggrFunction() == null) {
					IBaseExpression expr = b.getExpression();
					dataSetColumn = extractDirectColumn(expr, cx, false);
					if (dataSetColumn != null) {
						// binding "xxx" expression is just dataSetRow["xxx"]
						if (dataSet.getCurrentRow() != null
								&& dataSet.getCurrentRow().getResultClass().getFieldIndex(dataSetColumn) >= 0) {
							Object value = dataSet.getCurrentRow().getFieldValue(dataSetColumn);
							return DataTypeUtil.convert(value, b.getDataType());
						}
					}
				}
			} else {
				// row["xxx"] is added on data set level
				if (dataSet.getCurrentRow() != null
						&& dataSet.getCurrentRow().getResultClass().getFieldIndex(rowName) >= 0) {
					Object value = dataSet.getCurrentRow().getFieldValue(rowName);
					return DataTypeUtil.convert(value, dataExpr.getDataType());
				}
			}
		}

		return doEvaluateRawExpression(dataExpr, scope, javaType, cx);
	}

	/**
	 * 
	 * @param dataExpr
	 * @param scope
	 * @param javaType
	 * @param cx
	 * @return
	 * @throws DataException
	 * @throws BirtException
	 */
	public Object evaluateConditionExpression(IConditionalExpression dataExpr, Scriptable scope, boolean javaType,
			ScriptContext cx, CompareHints filterHints) throws DataException, BirtException {
		return ExprEvaluateUtil.evaluateConditionExpression(dataExpr, scope, javaType, cx, filterHints);
	}

	public Object evaluateConditionExpression(IConditionalExpression dataExpr, Scriptable scope, boolean javaType,
			ScriptContext cx, CompareHints filterHints, DataSetRuntime dataSet) throws DataException, BirtException {
		if (dataExpr.getHandle() != null)
			return Boolean.valueOf(((NEvaluator) dataExpr.getHandle()).evaluate(cx, scope, dataSet));

		IScriptExpression opr = ((IConditionalExpression) dataExpr).getExpression();
		int oper = ((IConditionalExpression) dataExpr).getOperator();
		IBaseExpression operand1 = ((IConditionalExpression) dataExpr).getOperand1();
		IBaseExpression operand2 = ((IConditionalExpression) dataExpr).getOperand2();

		if (operand1 instanceof IExpressionCollection) {
			Object[] expr = ((IExpressionCollection) operand1).getExpressions().toArray();
			Object[] result = new Object[expr.length];
			for (int i = 0; i < result.length; i++) {
				result[i] = doEvaluateRawExpression((IBaseExpression) expr[i], scope, javaType, cx, dataSet);
			}
			return ScriptEvalUtil.evalConditionalExpr(doEvaluateRawExpression(opr, scope, javaType, cx, dataSet), oper,
					ExprEvaluateUtil.flatternMultipleValues(result), filterHints);
		} else {
			return ScriptEvalUtil.evalConditionalExpr(doEvaluateRawExpression(opr, scope, javaType, cx, dataSet), oper,
					doEvaluateRawExpression(operand1, scope, javaType, cx, dataSet),
					doEvaluateRawExpression(operand2, scope, javaType, cx, dataSet), filterHints);
		}
	}

	/**
	 * 
	 * @param dataExpr
	 * @return
	 * @throws BirtException
	 */
	public Object evaluateValue(IBaseExpression dataExpr, int index, IResultObject roObject, Scriptable scope,
			ScriptContext cx) throws BirtException {
		return ExprEvaluateUtil.evaluateValue(dataExpr, index, roObject, scope, cx);
	}

	/**
	 * @param roObject
	 * @param index
	 * @param colref
	 * @return
	 * @throws DataException
	 */
	public Object evaluateColumnReferenceExpression(IResultObject roObject, int index, ColumnReferenceExpression colref)
			throws DataException {
		return ExprEvaluateUtil.evaluateColumnReferenceExpression(roObject, index, colref);
	}

}
