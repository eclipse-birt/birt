/*******************************************************************************
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
 *******************************************************************************/
package org.eclipse.birt.data.engine.expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.ICollectionConditionalExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IExpressionCollection;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.BaseExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.ExprManager;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateRegistry;
import org.eclipse.birt.data.engine.impl.util.DirectedGraph;
import org.eclipse.birt.data.engine.impl.util.DirectedGraph.CycleFoundException;
import org.eclipse.birt.data.engine.impl.util.DirectedGraphEdge;
import org.eclipse.birt.data.engine.impl.util.GraphNode;
import org.eclipse.birt.data.engine.script.ScriptConstants;

/**
 * The utility class to provide method for expression compiling
 *
 */
public class ExpressionCompilerUtil {
	// private static ExpressionCompiler expressionCompiler = new
	// ExpressionCompiler( );

	/**
	 * compile the expression
	 *
	 * @param expr
	 * @param registry
	 * @param cx
	 * @return
	 */
	public static CompiledExpression compile(String expr, ScriptContext cx) {
		ExpressionCompiler expressionCompiler = new ExpressionCompiler();
		expressionCompiler.setDataSetMode(true);
		return expressionCompiler.compile(expr, null, cx);
	}

	/**
	 *
	 * @param name
	 * @param exprManager
	 * @param scope
	 * @return
	 * @throws DataException
	 */
	public static boolean hasColumnRow(String expression, ExprManager exprManager, ScriptContext cx)
			throws DataException {
		if (expression == null) {
			return false;
		}

		return compile(expression, exprManager, cx);

	}

	/**
	 * find columns used as dataSetRow[name] in the expression.
	 *
	 * It is same as extractColumnExpression(expression, "DataSetRow")
	 *
	 * @param expression
	 * @return list of reference name.
	 * @throws DataException
	 */
	public static List<String> extractDataSetColumnExpression(IBaseExpression expression) throws DataException {
		return extractColumnExpression(expression, ExpressionUtil.DATASET_ROW_INDICATOR);
	}

	/**
	 * find columns used as 'indicator'[name] in the expression.
	 *
	 * A valid indicator is 'data', 'row', 'dataSetRow' and 'measure'.
	 *
	 * @param expression
	 * @param indicator
	 * @return list of reference name.
	 * @throws DataException
	 */
	public static List<String> extractColumnExpression(IBaseExpression expression, String indicator)
			throws DataException {
		return extractColumnExpression(expression, new String[] { indicator });
	}

	/**
	 * find columns used as 'indicator'[name] in the expression.
	 *
	 * It can find multiple indicator references.
	 *
	 * @param expression
	 * @return
	 * @throws DataException
	 */
	public static List<String> extractColumnExpression(IBaseExpression expression, String[] indicators)
			throws DataException {
		if (expression == null) {
			return Collections.emptyList();
		}

		LinkedHashSet<String> bindings = new LinkedHashSet<>();
		extractColumnExpression(bindings, expression, indicators);
		return new ArrayList<>(bindings);
	}

	private static void extractColumnExpression(Set<String> existingBindings, IBaseExpression expression,
			String[] indicators) throws DataException {
		if (expression != null) {
			if (expression instanceof IScriptExpression) {
				extractColumnExpression(existingBindings, (IScriptExpression) expression, indicators);
			} else if (expression instanceof IConditionalExpression) {
				extractColumnExpression(existingBindings, (IConditionalExpression) expression, indicators);
			} else if (expression instanceof IExpressionCollection) {
				extractColumnExpression(existingBindings, (IExpressionCollection) expression, indicators);
			} else if (expression instanceof ICollectionConditionalExpression) {
				extractColumnExpression(existingBindings, (ICollectionConditionalExpression) expression, indicators);
			} else {
				throw new RuntimeException("unknown expresion type:" + expression);
			}
		}
	}

	/**
	 * The utility method is to compile expression to get a list of column
	 * expressions which is depended by given expression.
	 *
	 * @param expression
	 * @return
	 * @throws DataException
	 */
	private static void extractColumnExpression(Set<String> existingBindings, IScriptExpression expression,
			String[] indicators) throws DataException {
		if (expression != null) {
			populateColumnList(existingBindings, expression, indicators);
		}
	}

	/**
	 *
	 * This utility method is to compile expression to get a list of column
	 * expressions which is depended by given expression.
	 *
	 * @param expression
	 * @return
	 * @throws DataException
	 */
	private static void extractColumnExpression(Set<String> existingBindings, IConditionalExpression expression,
			String[] indicators) throws DataException {
		if (expression != null) {
			extractColumnExpression(existingBindings, expression.getExpression(), indicators);
			extractColumnExpression(existingBindings, expression.getOperand1(), indicators);
			extractColumnExpression(existingBindings, expression.getOperand2(), indicators);
		}
	}

	/**
	 *
	 * @param expression
	 * @return
	 * @throws DataException
	 */
	private static void extractColumnExpression(Set<String> existingBindings, IExpressionCollection expression,
			String[] indicators) throws DataException {
		if (expression != null) {
			Collection<IBaseExpression> exprs = expression.getExpressions();
			for (IBaseExpression expr : exprs) {
				extractColumnExpression(existingBindings, expr, indicators);
			}
		}
	}

	/**
	 *
	 * @param expression
	 * @return
	 * @throws DataException
	 */
	private static void extractColumnExpression(Set<String> existingBindings,
			ICollectionConditionalExpression expression, String[] indicators) throws DataException {
		if (expression != null) {
			Collection<IScriptExpression> exprs = expression.getExpr();
			for (IScriptExpression expr : exprs) {
				extractColumnExpression(existingBindings, expr, indicators);
			}
		}
	}

	/**
	 * Check whether there is columnReferenceExpression in aggregation. If so,
	 * return true. else return false;
	 *
	 * @return
	 */
	public static boolean hasAggregationInExpr(IBaseExpression expression) {
		if (expression == null || BaseExpression.constantId.equals(expression.getScriptId())) {
			return false;
		}
		if (expression instanceof IScriptExpression) {
			String text = ((IScriptExpression) expression).getText();
			return ExpressionUtil.hasAggregation(text);
		} else if (expression instanceof IConditionalExpression) {
			return hasAggregationInExpr(((IConditionalExpression) expression).getExpression())
					|| hasAggregationInExpr(((IConditionalExpression) expression).getOperand1())
					|| hasAggregationInExpr(((IConditionalExpression) expression).getOperand2());
		} else if (expression instanceof IExpressionCollection) {
			Object[] text = ((IExpressionCollection) expression).getExpressions().toArray();
			for (int i = 0; i < text.length; i++) {
				if (hasAggregationInExpr((IBaseExpression) text[i])) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check whether filter in query contains aggregation. If aggregation is
	 * TOPN,BOTTOMN,TOPPERCENT,BOTTMEPERCENT return true. else return false;
	 *
	 * @return
	 * @throws DataException
	 */
	public static boolean isValidExpressionInQueryFilter(IBaseExpression expression, ScriptContext context)
			throws DataException {
		if (expression instanceof IScriptExpression) {
			String text = ((IScriptExpression) expression).getText();
			if (text == null || text.trim().length() == 0
					|| BaseExpression.constantId.equals(expression.getScriptId())) {
				return true;
			}
			// fake a registry to register the aggregation.
			AggregateRegistry aggrReg = new AggregateRegistry() {

				@Override
				public int register(AggregateExpression aggregationExpr) {
					return -1;
				}
			};
			ExpressionCompiler expressionCompiler = new ExpressionCompiler();
			CompiledExpression expr = expressionCompiler.compile(text, aggrReg, context);
			return flattenFilterExpression(expr);
		} else if (expression instanceof IConditionalExpression) {
			IScriptExpression expr = ((IConditionalExpression) expression).getExpression();
			IBaseExpression oprand1 = ((IConditionalExpression) expression).getOperand1();
			IBaseExpression oprand2 = ((IConditionalExpression) expression).getOperand2();
			return isValidExpressionInQueryFilter(expr, context) && isValidExpressionInQueryFilter(oprand1, context)
					&& isValidExpressionInQueryFilter(oprand2, context);
		}
		return true;
	}

	/**
	 *
	 * @param expression
	 * @param exprManager
	 * @return
	 * @throws DataException
	 */
	private static boolean compile(String expression, ExprManager exprManager, ScriptContext cx) throws DataException {
		// fake a registry to register the aggregation.
		AggregateRegistry aggrReg = new AggregateRegistry() {

			@Override
			public int register(AggregateExpression aggregationExpr) {
				return -1;
			}
		};
		ExpressionCompiler expressionCompiler = new ExpressionCompiler();
		CompiledExpression expr = expressionCompiler.compile(expression, aggrReg, cx);
		return flattenExpression(expr, exprManager, cx);
	}

	/**
	 *
	 * @param expr
	 * @throws DataException
	 */
	private static boolean flattenExpression(CompiledExpression expr, ExprManager exprManager, ScriptContext cx)
			throws DataException {
		int type = expr.getType();
		switch (type) {
		case CompiledExpression.TYPE_COMPLEX_EXPR: {
			Iterator col = ((ComplexExpression) expr).getSubExpressions().iterator();
			while (col.hasNext()) {
				if (!flattenExpression((CompiledExpression) col.next(), exprManager, cx)) {
					return false;
				}
			}
			break;
		}
		case CompiledExpression.TYPE_DIRECT_COL_REF: {
			String columnName = ((ColumnReferenceExpression) expr).getColumnName();
			if (ScriptConstants.ROW_NUM_KEYWORD.equals(columnName)) {
				return true;
			}
			if (exprManager.getExpr(columnName) != null) {
				String expression = ((IScriptExpression) exprManager.getExpr(columnName)).getText();
				return compile(expression, exprManager, cx);
			} else {
				return false;
			}
		}
		case CompiledExpression.TYPE_SINGLE_AGGREGATE: {
			Iterator args = ((AggregateExpression) expr).getArguments().iterator();
			while (args.hasNext()) {
				if (!flattenExpression((CompiledExpression) args.next(), exprManager, cx)) {
					return false;
				}
			}
			break;
		}
		case CompiledExpression.TYPE_CONSTANT_EXPR:
		case CompiledExpression.TYPE_INVALID_EXPR: {
			return true;
		}
		}
		return true;
	}

	/**
	 *
	 * @param expr
	 * @return
	 * @throws DataException
	 */
	private static boolean flattenFilterExpression(CompiledExpression expr) throws DataException {
		int type = expr.getType();
		switch (type) {
		case CompiledExpression.TYPE_COMPLEX_EXPR: {
			Iterator col = ((ComplexExpression) expr).getSubExpressions().iterator();
			while (col.hasNext()) {
				if (!flattenFilterExpression((CompiledExpression) col.next())) {
					return false;
				}
			}
			break;
		}
		case CompiledExpression.TYPE_DIRECT_COL_REF: {
			break;
		}
		case CompiledExpression.TYPE_SINGLE_AGGREGATE: {
			final int numberOfPasses = ((AggregateExpression) expr).getAggregation().getNumberOfPasses();
			if (numberOfPasses <= 1) {
				return false;
			}
			break;
		}
		case CompiledExpression.TYPE_CONSTANT_EXPR: {
			break;
		}
		case CompiledExpression.TYPE_INVALID_EXPR: {
			throw (DataException) ((InvalidExpression) expr).evaluate(null, null);
		}
		}
		return true;
	}

	/**
	 *
	 * @param list
	 * @param expression
	 * @throws DataException
	 */
	private static void populateColumnList(Set<String> existingBindings, IScriptExpression expression,
			String[] indicators) throws DataException {
		if (expression != null) {
			if (BaseExpression.constantId.equals(expression.getScriptId())) {
			} else if (BaseExpression.javaScriptId.equals(expression.getScriptId())) {
				try {
					for (String indicator : indicators) {
						List<IColumnBinding> l = ExpressionUtil.extractColumnExpressions(expression.getText(),
								indicator);
						for (IColumnBinding cb : l) {
							if (cb.getOuterLevel() == 0) {
								existingBindings.add(cb.getResultSetColumnName());
							}
						}
					}
				} catch (BirtException e) {
					throw DataException.wrap(e);
				}
			} else {
				throw new RuntimeException("unsupported script type:" + expression.getScriptId());
			}
		}
	}

	/**
	 * @param namedExpressions
	 * @return the name of the first found NamedExpression which is involved in a
	 *         cycle. return null if no cycle is found
	 */
	@SuppressWarnings("unchecked")
	public static String getFirstFoundNameInCycle(Set<NamedExpression> namedExpressions, String indicator) {
		if (namedExpressions == null) {
			return null;
		}
		Set<DirectedGraphEdge> graphEdges = new HashSet<>();
		for (NamedExpression ne : namedExpressions) {
			List<String> referenceNames = null;
			try {
				referenceNames = ExpressionCompilerUtil.extractColumnExpression(ne.getExpression(), indicator);
			} catch (DataException e) {
				// don't care
			}
			if (referenceNames != null) {
				for (String reference : referenceNames) {
					graphEdges.add(new DirectedGraphEdge(new GraphNode(ne.getName()), new GraphNode(reference)));
				}
			}
		}
		DirectedGraph graph = new DirectedGraph(graphEdges);

		String foundName = null;
		try {
			graph.validateCycle();
		} catch (CycleFoundException e) {
			foundName = (String) e.getNode().getValue();
		}
		return foundName;
	}

}
