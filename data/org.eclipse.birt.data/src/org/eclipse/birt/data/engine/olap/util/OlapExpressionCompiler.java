/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.ICollectionConditionalExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IExpressionCollection;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.BaseExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.util.DirectedGraph;
import org.eclipse.birt.data.engine.impl.util.DirectedGraph.CycleFoundException;
import org.eclipse.birt.data.engine.impl.util.DirectedGraphEdge;
import org.eclipse.birt.data.engine.impl.util.GraphNode;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.IRFactory;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstRoot;

/**
 * 
 */

public class OlapExpressionCompiler {

	/**
	 * Get referenced Script Object (dimension, data, measure, etc) according to
	 * given object name.
	 * 
	 * @param expr
	 * @param objectName
	 * @return
	 */
	public static String getReferencedScriptObject(IBaseExpression expr, String objectName) {
		if (expr == null || BaseExpression.constantId.equals(expr.getScriptId()))
			return null;
		if (expr instanceof IScriptExpression) {
			return getReferencedScriptObject(((IScriptExpression) expr), objectName);
		} else if (expr instanceof IConditionalExpression) {
			String dimName = null;
			IScriptExpression expr1 = ((IConditionalExpression) expr).getExpression();
			dimName = getReferencedScriptObject(expr1, objectName);
			if (dimName != null)
				return dimName;
			IBaseExpression op1 = ((IConditionalExpression) expr).getOperand1();
			dimName = getReferencedScriptObject(op1, objectName);
			if (dimName != null)
				return dimName;

			IBaseExpression op2 = ((IConditionalExpression) expr).getOperand2();
			dimName = getReferencedScriptObject(op2, objectName);
			return dimName;
		} else if (expr instanceof IExpressionCollection) {
			IExpressionCollection combinedExpr = (IExpressionCollection) expr;
			Object[] exprs = combinedExpr.getExpressions().toArray();

			for (int i = 0; i < exprs.length; i++) {
				String o = getReferencedScriptObject((IBaseExpression) exprs[i], objectName);
				if (o != null)
					return o;
			}
		}

		// In 2.6 we only consider support IInNotInFilter in single dimension filter
		// In future we may allow the filter cross dimensions.
		else if (expr instanceof ICollectionConditionalExpression) {
			Collection<IScriptExpression> testExpr = ((ICollectionConditionalExpression) expr).getExpr();
			if (testExpr.size() > 0) {
				return getReferencedScriptObject(testExpr.iterator().next(), objectName);
			}

		}

		return null;
	}

	/**
	 * 
	 * @param expr
	 * @param objectName
	 * @return
	 */
	private static String getReferencedScriptObject(IScriptExpression expr, String objectName) {
		if (expr == null)
			return null;
		else
			return getReferencedScriptObject(expr.getText(), objectName);
	}

	/**
	 * 
	 * @param expr
	 * @param objectName
	 * @return
	 */
	public static String getReferencedScriptObject(String expr, String objectName) {
		if (expr == null)
			return null;
		try {
			Context cx = Context.enter();
			CompilerEnvirons ce = new CompilerEnvirons();
			Parser p = new Parser(ce, cx.getErrorReporter());
			AstRoot tree = p.parse(expr, null, 0);
			Node root = new IRFactory(ce).transformTree(tree);

			return getScriptObjectName(root, objectName);
		} catch (Exception ex) {
			return null;
		} finally {
			Context.exit();
		}
	}

	/**
	 * 
	 * @param expr
	 * @param bindings
	 * @return
	 * @throws DataException
	 */
	public static Set getReferencedDimLevel(IBaseExpression expr, List bindings) throws DataException {
		return getReferencedDimLevel(expr, bindings, false);
	}

	/**
	 * Get set of reference DimLevels.
	 * <p>
	 * Attention: make sure no dependency cycle in <argument>bindings</argument>
	 * <p>
	 * otherwise, dead loop encountered
	 * <p>
	 * use <method>OlapExpressionCompiler.validateDependencyCycle( )</method> to
	 * check whether dependency cycle exist
	 * 
	 * @param expr
	 * @param bindings
	 * @param onlyFromDirectReferenceExpr
	 * @return
	 * @throws DataException
	 */
	public static Set getReferencedDimLevel(IBaseExpression expr, List bindings, boolean onlyFromDirectReferenceExpr)
			throws DataException {
		if (expr instanceof IScriptExpression) {
			return getReferencedDimLevel(((IScriptExpression) expr), bindings, onlyFromDirectReferenceExpr);
		} else if (expr instanceof IConditionalExpression) {
			Set result = new HashSet();
			IScriptExpression expr1 = ((IConditionalExpression) expr).getExpression();
			result.addAll(getReferencedDimLevel(expr1, bindings, onlyFromDirectReferenceExpr));

			IBaseExpression op1 = ((IConditionalExpression) expr).getOperand1();
			if (op1 != null)
				result.addAll(getReferencedDimLevel(op1, bindings, onlyFromDirectReferenceExpr));

			IBaseExpression op2 = ((IConditionalExpression) expr).getOperand2();
			if (op2 != null)
				result.addAll(getReferencedDimLevel(op2, bindings, onlyFromDirectReferenceExpr));
			return result;
		} else if (expr instanceof IExpressionCollection) {
			Set result = new HashSet();
			Object[] ops = ((IExpressionCollection) expr).getExpressions().toArray();
			for (int i = 0; i < ops.length; i++) {
				result.addAll(getReferencedDimLevel((IBaseExpression) ops[i], bindings, onlyFromDirectReferenceExpr));
			}
			return result;
		}

		return Collections.EMPTY_SET;
	}

	/**
	 * 
	 * @param expr
	 * @param bindings
	 * @param onlyFromDirectReferenceExpr
	 * @return
	 * @throws DataException
	 */
	private static Set getReferencedDimLevel(IScriptExpression expr, List bindings, boolean onlyFromDirectReferenceExpr)
			throws DataException {
		if (expr == null || expr.getText() == null || expr.getText().length() == 0
				|| BaseExpression.constantId.equals(expr.getScriptId()))
			return new HashSet();
		try {
			Set result = new HashSet();
			Context cx = Context.enter();
			CompilerEnvirons ce = new CompilerEnvirons();
			Parser p = new Parser(ce, cx.getErrorReporter());
			AstRoot tree = p.parse(expr.getText(), null, 0);
			Node root = new IRFactory(ce).transformTree(tree);

			populateDimLevels(null, root, result, bindings, onlyFromDirectReferenceExpr);
			return result;
		} finally {
			Context.exit();
		}
	}

	/**
	 * 
	 * @param n
	 * @param result
	 * @param bindings
	 * @param onlyFromDirectReferenceExpr
	 * @throws DataException
	 */
	private static void populateDimLevels(Node grandpa, Node n, Set result, List bindings,
			boolean onlyFromDirectReferenceExpr) throws DataException {
		if (n == null)
			return;
		if (onlyFromDirectReferenceExpr) {
			if (n.getType() == Token.SCRIPT) {
				if (n.getFirstChild() == null || n.getFirstChild().getType() != Token.EXPR_RESULT)
					return;
				if (n.getFirstChild().getFirstChild() == null
						|| (n.getFirstChild().getFirstChild().getType() != Token.GETPROP
								&& n.getFirstChild().getFirstChild().getType() != Token.GETELEM))
					return;
			}
		}
		if (n.getFirstChild() != null && (n.getType() == Token.GETPROP || n.getType() == Token.GETELEM)) {
			if (n.getFirstChild().getFirstChild() != null
					&& (n.getFirstChild().getFirstChild().getType() == Token.GETPROP
							|| n.getFirstChild().getFirstChild().getType() == Token.GETELEM)) {
				Node dim = n.getFirstChild().getFirstChild();
				if (ScriptConstants.DIMENSION_SCRIPTABLE.equals(dim.getFirstChild().getString())) {
					String dimName = dim.getLastChild().getString();
					String levelName = dim.getNext().getString();
					String attr = n.getLastChild().getString();

					DimLevel dimLevel = new DimLevel(dimName, levelName, attr);
					if (!result.contains(dimLevel))
						result.add(dimLevel);
				}
			} else if (n.getFirstChild() != null && n.getFirstChild().getType() == Token.NAME) {
				if (ScriptConstants.DIMENSION_SCRIPTABLE.equals(n.getFirstChild().getString())) {
					if (n.getLastChild() != null && n.getNext() != null) {
						String dimName = n.getLastChild().getString();
						String levelName = n.getNext().getString();
						String attr = null;
						if (grandpa != null && grandpa.getNext() != null
								&& grandpa.getNext().getType() == Token.STRING) {
							attr = grandpa.getNext().getString();
						}
						DimLevel dimLevel = new DimLevel(dimName, levelName, attr);
						if (!result.contains(dimLevel))
							result.add(dimLevel);
					}
				} else if (ScriptConstants.DATA_BINDING_SCRIPTABLE.equals(n.getFirstChild().getString())
						|| ScriptConstants.DATA_SET_BINDING_SCRIPTABLE.equals(n.getFirstChild().getString())) {
					if (n.getLastChild() != null) {
						String bindingName = n.getLastChild().getString();
						IBinding binding = getBinding(bindings, bindingName);
						if (binding != null) {
							result.addAll(getReferencedDimLevel(binding.getExpression(), bindings,
									onlyFromDirectReferenceExpr));
						}
					}
				}
			}
		}
		populateDimLevels(grandpa, n.getFirstChild(), result, bindings, onlyFromDirectReferenceExpr);
		if (n.getLastChild() != n.getFirstChild()) {
			populateDimLevels(grandpa, n.getLastChild(), result, bindings, onlyFromDirectReferenceExpr);
		}
		if (n.getNext() != n.getFirstChild() && n.getNext() != n.getLastChild()) {
			populateDimLevels(grandpa, n.getNext(), result, bindings, onlyFromDirectReferenceExpr);
		}
	}

	/**
	 * Get binding
	 * 
	 * @param bindings
	 * @param bindingName
	 * @return
	 * @throws DataException
	 */
	private static IBinding getBinding(List bindings, String bindingName) throws DataException {
		for (int i = 0; i < bindings.size(); i++) {
			if (((IBinding) bindings.get(i)).getBindingName().equals(bindingName))
				return (IBinding) bindings.get(i);
		}
		return null;
	}

	/**
	 * 
	 * @param n
	 * @param objectName
	 * @return
	 */
	private static String getScriptObjectName(Node n, String objectName) {
		if (n == null)
			return null;
		String result = null;
		if (n.getType() == Token.NAME) {
			if (objectName.equals(n.getString())) {
				Node dimNameNode = n.getNext();
				if (dimNameNode == null || dimNameNode.getType() != Token.STRING)
					return null;

				return dimNameNode.getString();
			}
		}

		result = getScriptObjectName(n.getFirstChild(), objectName);
		if (result == null)
			result = getScriptObjectName(n.getLastChild(), objectName);

		if (result == null)
			result = getScriptObjectName(n.getNext(), objectName);

		return result;
	}

	/**
	 * 
	 * @param bindings
	 * @return the first found binding involved in dependency cycle.
	 *         <p>
	 *         null if no dependency cycle found
	 * @throws DataException
	 */
	public static void validateDependencyCycle(Set<IBinding> bindings) throws DataException {
		Set<DirectedGraphEdge> edges = new HashSet<DirectedGraphEdge>();
		for (IBinding binding : bindings) {
			List<String> references = ExpressionCompilerUtil.extractColumnExpression(binding.getExpression(),
					ExpressionUtil.DATA_INDICATOR);
			for (String reference : references) {
				DirectedGraphEdge edge = new DirectedGraphEdge(new GraphNode(binding.getBindingName()),
						new GraphNode(reference));
				edges.add(edge);
			}
		}
		DirectedGraph dg = new DirectedGraph(edges);
		try {
			dg.validateCycle();
		} catch (CycleFoundException e) {
			throw new DataException(ResourceConstants.COLUMN_BINDING_CYCLE, e.getNode().getValue());
		}
	}
}
