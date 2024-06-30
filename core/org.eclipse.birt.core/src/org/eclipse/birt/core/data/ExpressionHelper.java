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

package org.eclipse.birt.core.data;

import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;

/**
 * None-static version of <code> ExpressionParserUtility </code>, which caches
 * parsed expressions and avoid repeat parsing the same expressions.
 *
 */
public class ExpressionHelper {

	private ExpressionParserUtility exprParser;
	private HashMap<String, CompileResult> parsedRowExprs;
	private HashMap<String, CompileResult> parsedDataSetExprs;

	public ExpressionHelper() {
		exprParser = new ExpressionParserUtility();
		parsedRowExprs = new HashMap<>();
		parsedDataSetExprs = new HashMap<>();
	}

	public synchronized String getColumnName(String oldExpression) throws BirtException {
		if (oldExpression == null || oldExpression.trim().length() == 0) {
			return null;
		}

		CompileResult result = extractColumnExpressions(oldExpression, ExpressionUtil.DATASET_ROW_INDICATOR);

		if (result.getColumnReference().size() != 1 || !result.isDirectColumnRef()) {
			return null;
		}

		return ((IColumnBinding) result.getColumnReference().get(0)).getResultSetColumnName();
	}

	public synchronized String getColumnBindingName(String oldExpression) throws BirtException {
		if (oldExpression == null || oldExpression.trim().length() == 0) {
			return null;
		}

		CompileResult result = extractColumnExpressions(oldExpression, ExpressionUtil.ROW_INDICATOR);

		if (result.getColumnReference().size() != 1 || !result.isDirectColumnRef()) {
			return null;
		}

		return ((IColumnBinding) result.getColumnReference().get(0)).getResultSetColumnName();
	}

	private CompileResult extractColumnExpressions(String expression, String indicator) throws BirtException {
		CompileResult result = getParsedExpression(expression, indicator);
		if (result != null) {
			return result;
		}
		extractExpressions(expression, indicator);
		return getParsedExpression(expression, indicator);
	}

	private List extractExpressions(String expression, String indicator) throws BirtException {
		exprParser.reset();
		List result = ExpressionParserUtility.compileColumnExpression(exprParser, expression, indicator);
		cacheParsedExpression(expression, result, indicator);
		return result;
	}

	private void cacheParsedExpression(String expression, List parseResult, String indicator) {
		HashMap<String, CompileResult> cache = getExpressionCache(indicator);
		if (cache == null) {
			return;
		}
		cache.put(expression,
				new CompileResult(parseResult, exprParser.hasAggregation(), exprParser.isDirectColumnRef()));
	}

	private CompileResult getParsedExpression(String expr, String indicator) {
		HashMap<String, CompileResult> cache = getExpressionCache(indicator);
		return cache == null ? null : cache.get(expr);
	}

	private HashMap<String, CompileResult> getExpressionCache(String indicator) {
		if (ExpressionUtil.DATASET_ROW_INDICATOR.equals(indicator)) {
			return parsedDataSetExprs;
		} else if (ExpressionUtil.ROW_INDICATOR.equals(indicator)) {
			return parsedRowExprs;
		}
		return null;
	}

	public synchronized void close() {
		parsedRowExprs.clear();
		parsedDataSetExprs.clear();
	}

	class CompileResult {

		boolean hasAggregation = false;
		boolean isDirectColumnRef = false;
		List columnRefs;

		public CompileResult(List columnRefs, boolean hasAggregation, boolean isDirectColumnRef) {
			this.columnRefs = columnRefs;
			this.hasAggregation = hasAggregation;
			this.isDirectColumnRef = isDirectColumnRef;
		}

		public boolean hasAggregation() {
			return hasAggregation;
		}

		public boolean isDirectColumnRef() {
			return isDirectColumnRef;
		}

		public List getColumnReference() {
			return columnRefs;
		}
	}
}
