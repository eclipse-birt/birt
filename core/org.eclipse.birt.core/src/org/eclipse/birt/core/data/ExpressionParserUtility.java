/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.exception.CoreException;
import org.eclipse.birt.core.i18n.ResourceConstants;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.IRFactory;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.ScriptNode;

/**
 * This utility class is to compile expression to get a list of column
 * expression. The returned column expression is marked as dataSetRow["name"] or
 * dataSetRow[index]
 */
public class ExpressionParserUtility {
	private final String pluginId = "org.eclipse.birt.core"; //$NON-NLS-1$

	private final static String ROWS_0_INDICATOR = "rows"; //$NON-NLS-1$
	private final static String TOTAL = "Total"; //$NON-NLS-1$

	private String ROW_INDICATOR = "row"; //$NON-NLS-1$
	private boolean hasAggregation = false;
	private boolean isDirectColumnRef = false;

	/**
	 * compile the expression
	 * 
	 * @param expression
	 * @return List contains all column reference
	 * @throws BirtException
	 */
	public static List compileColumnExpression(String expression) throws BirtException {
		return compileColumnExpression(new ExpressionParserUtility(), expression, ExpressionUtil.ROW_INDICATOR);
	}

	/**
	 * compile the expression
	 * 
	 * @param expression
	 * @return List contains all column reference
	 * @throws BirtException
	 */
	public static List compileColumnExpression(ExpressionParserUtility util, String expression, String indicator)
			throws BirtException {
		if (expression == null || expression.trim().length() == 0)
			return new ArrayList();
		util.ROW_INDICATOR = indicator;
		List columnExprList = new ArrayList();
		columnExprList.clear();
		Context context = Context.enter();
		try {
			ScriptNode tree = util.parse(expression, context);
			util.CompiledExprFromTree(expression, context, tree, columnExprList);
		} catch (Exception ex) {
			throw new CoreException(ResourceConstants.INVALID_EXPRESSION, expression, ex);
		} finally {
			Context.exit();
		}
		return columnExprList;
	}

	/**
	 * 
	 * @return
	 * @throws BirtException
	 */
	public static boolean hasAggregation(String expression) throws BirtException {
		return hasAggregation(expression, ExpressionUtil.ROW_INDICATOR);
	}

	/**
	 * 
	 * @return
	 * @throws BirtException
	 */
	static boolean hasAggregation(String expression, String indicator) throws BirtException {
		ExpressionParserUtility util = new ExpressionParserUtility();
		util.hasAggregation = false;
		compileColumnExpression(util, expression, indicator);
		return util.hasAggregation;
	}

	public static boolean isDirectColumnRef(String expression, String indicator) throws BirtException {
		ExpressionParserUtility util = new ExpressionParserUtility();
		util.isDirectColumnRef = false;
		compileColumnExpression(util, expression, indicator);
		return util.isDirectColumnRef;
	}

	/**
	 * compile the expression from a script tree
	 * 
	 * @param expression
	 * @param context
	 * @param tree
	 * @param columnExprList
	 * @throws BirtException
	 */
	private void CompiledExprFromTree(String expression, Context context, ScriptNode tree, List columnExprList)
			throws BirtException {
		if (tree.getFirstChild() == tree.getLastChild()) {
			if (tree.getFirstChild().getType() == Token.FUNCTION) {
				int index = getFunctionIndex(tree.getFirstChild().getString(), tree);
				compileFunctionNode(tree.getFunctionNode(index), tree, columnExprList);
			} else {
				// A single expression
				if (tree.getFirstChild().getType() != Token.EXPR_RESULT
						&& tree.getFirstChild().getType() != Token.EXPR_VOID
						&& tree.getFirstChild().getType() != Token.BLOCK
						&& tree.getFirstChild().getType() != Token.SCRIPT) {
					// This should never happen?
					throw new CoreException(pluginId, ResourceConstants.INVALID_EXPRESSION);
				}
				Node exprNode = tree.getFirstChild();
				processChild(exprNode, tree, columnExprList);
			}
		} else {
			compileComplexExpr(tree, tree, columnExprList);
		}
	}

	/**
	 * parse the expression into a script tree
	 * 
	 * @param expression
	 * @param cx
	 * @return
	 */
	private ScriptNode parse(String expression, Context cx) {
		CompilerEnvirons compilerEnv = new CompilerEnvirons();
		Parser p = new Parser(compilerEnv, cx.getErrorReporter());
		AstRoot root = p.parse(expression, null, 0);
		IRFactory ir = new IRFactory(compilerEnv);
		return ir.transformTree(root);
	}

	/**
	 * process child node
	 * 
	 * @param parent
	 * @param child
	 * @param tree
	 * @param columnExprList
	 * @throws BirtException
	 */
	private void processChild(Node child, ScriptNode tree, List columnExprList) throws BirtException {
		switch (child.getType()) {
		case Token.NUMBER:
		case Token.STRING:
		case Token.NULL:
		case Token.TRUE:
		case Token.FALSE:
			break;

		case Token.GETPROP:
		case Token.GETELEM:
		case Token.SETPROP:
		case Token.SETELEM: {
			compileDirectColRefExpr(child, tree, columnExprList);
			break;
		}
		case Token.CALL:
			compileAggregateExpr(child, tree, columnExprList);
			break;
		default:
			compileComplexExpr(child, tree, columnExprList);
		}

	}

	/**
	 * compile column reference expression
	 * 
	 * @param refNode
	 * @throws BirtException
	 */
	private void compileDirectColRefExpr(Node refNode, ScriptNode tree, List columnExprList) throws BirtException {
		assert (refNode.getType() == Token.GETPROP || refNode.getType() == Token.GETELEM
				|| refNode.getType() == Token.SETELEM || refNode.getType() == Token.SETPROP);

		Node rowName = refNode.getFirstChild();
		assert (rowName != null);
		if (rowName.getType() != Token.NAME) {
			if (refNode.getType() == Token.GETPROP || refNode.getType() == Token.GETELEM
					|| refNode.getType() == Token.SETELEM || refNode.getType() == Token.SETPROP) {
				compileOuterColRef(refNode, tree, columnExprList);
				compileRowPositionRef(refNode, tree, columnExprList);
				return;
			}
			compileComplexExpr(refNode, tree, columnExprList);
			return;
		} else
			compileSimpleColumnRefExpr(refNode, tree, columnExprList);
	}

	/**
	 * compile outer column expression
	 * 
	 * @param refNode
	 * @param tree
	 * @param columnExprList
	 * @throws BirtException
	 */
	private void compileOuterColRef(Node refNode, ScriptNode tree, List columnExprList) throws BirtException {
		int level = compileOuterColRefExpr(refNode);
		if (level == -1) {
			compileComplexExpr(refNode, tree, columnExprList);
		} else {
			Node nextNode = refNode.getLastChild();
			if (nextNode.getType() == Token.STRING) {
				ColumnBinding info = new ColumnBinding(nextNode.getString(), "", level);
				columnExprList.add(info);
			}
		}
		return;
	}

	/**
	 * compile row position expression
	 * 
	 * @param refNode
	 * @param tree
	 * @param columnExprList
	 * @throws BirtException
	 */
	private void compileRowPositionRef(Node refNode, ScriptNode tree, List columnExprList) throws BirtException {
		Node rowFirstNode = refNode.getFirstChild();

		if (rowFirstNode.getType() == Token.GETELEM || rowFirstNode.getType() == Token.SETELEM) {
			Node rowNode = rowFirstNode.getFirstChild();
			if (rowNode != null && rowNode.getType() == Token.NAME && rowNode.getString().equals(ROWS_0_INDICATOR)) {
				Node rowColumn = rowNode.getNext();
				if (rowColumn.getDouble() == 0.0) {
					rowColumn = rowFirstNode.getNext();
					if (rowColumn.getType() == Token.STRING
							&& (refNode.getType() == Token.GETELEM || refNode.getType() == Token.SETELEM)) {
						ColumnBinding binding = new ColumnBinding(rowColumn.getString(),
								ExpressionUtil.createJSDataSetRowExpression(rowColumn.getString()), 1);
						columnExprList.add(binding);
						;
					}
				}
			}
		}
	}

	/**
	 * compile simple column ref expression
	 * 
	 * @param refNode
	 * @param rowName
	 * @param columnExprList
	 * @throws BirtException
	 */
	private void compileSimpleColumnRefExpr(Node refNode, ScriptNode tree, List columnExprList) throws BirtException {
		Node rowName = refNode.getFirstChild();
		String str = rowName.getString();
		assert (str != null);

		Node rowColumn = rowName.getNext();
		assert (rowColumn != null);

		if (!str.equals(ROW_INDICATOR)) {
			if (rowColumn != null && rowColumn.getNext() != null)
				processChild(rowColumn.getNext(), tree, columnExprList);
			return;
		}
		if ((refNode.getType() == Token.GETPROP || refNode.getType() == Token.SETPROP)
				&& rowColumn.getType() == Token.STRING) {
			int outer_count = 0;
			if ("__rownum".equals(rowColumn.getString()) || "0".equals(rowColumn.getString()))
				return;
			if ("_outer".equals(rowColumn.getString())) {
				outer_count++;

				Node outer_Node = refNode.getNext();
				Node before_Node = outer_Node;
				while (outer_Node != null && outer_Node.getString() != null
						&& "_outer".equals(outer_Node.getString())) {
					outer_count++;
					before_Node = outer_Node;
					outer_Node = outer_Node.getNext();
				}
				ColumnBinding info = new ColumnBinding(before_Node.getString(), "", outer_count);
				columnExprList.add(info);
				return;
			}
			if (tree.getFirstChild().getFirstChild() == refNode && refNode.getNext() == null) {
				isDirectColumnRef = true;
			}
			ColumnBinding binding = new ColumnBinding(rowColumn.getString(),
					ExpressionUtil.createDataSetRowExpression(rowColumn.getString()));
			columnExprList.add(binding);
		}

		if (refNode.getType() == Token.GETELEM || refNode.getType() == Token.SETELEM) {
			if (rowColumn.getType() == Token.NUMBER) {
				if (0 == rowColumn.getDouble())
					return;
				// columnExprList.add( DATASETROW_INDICATOR
				// + "[" + (int) rowColumn.getDouble( ) + "]" );
			} else if (rowColumn.getType() == Token.STRING) {
				if ("_rownum".equals(rowColumn.getString()))
					return;
				if (tree.getFirstChild().getFirstChild() == refNode && refNode.getNext() == null) {
					isDirectColumnRef = true;
				}
				ColumnBinding binding = new ColumnBinding(rowColumn.getString(),
						ExpressionUtil.createJSDataSetRowExpression(rowColumn.getString()));
				columnExprList.add(binding);
			}
		}
		if (rowColumn != null && rowColumn.getNext() != null)
			processChild(rowColumn.getNext(), tree, columnExprList);
	}

	/**
	 * 
	 * @param refNode
	 * @return
	 */
	private int compileOuterColRefExpr(Node refNode) {
		int count = 0;
		Node rowFirstNode = refNode.getFirstChild();
		if (refNode.getType() == Token.GETPROP || refNode.getType() == Token.GETELEM
				|| refNode.getType() == Token.SETPROP || refNode.getType() == Token.SETELEM) {
			if (rowFirstNode.getType() == Token.NAME && rowFirstNode.getString().equals(ROW_INDICATOR)) {
				Node rowColumn = rowFirstNode.getNext();
				if (rowColumn.getType() == Token.STRING) {
					if ("_outer".equals(rowColumn.getString()))
						count++;
				}
				return count;
			} else if (rowFirstNode.getType() == Token.GETPROP || rowFirstNode.getType() == Token.SETPROP) {
				if (compileOuterColRefExpr(rowFirstNode) == -1)
					return -1;
				else
					count = count + compileOuterColRefExpr(rowFirstNode);
				Node nextChild = rowFirstNode.getNext();
				if (nextChild.getType() == Token.STRING) {
					if ("_outer".equals(nextChild.getString()))
						count++;
				}
			} else
				return -1;
			return count;
		} else
			return -1;
	}

	/**
	 * compile aggregate expression
	 * 
	 * @param context
	 * @param parent
	 * @param callNode
	 * @throws BirtException
	 */
	private void compileAggregateExpr(Node callNode, ScriptNode tree, List columnExprList) throws BirtException {
		assert (callNode.getType() == Token.CALL);
		compileAggregationFunction(callNode, tree, columnExprList);
		extractArguments(callNode, tree, columnExprList);
	}

	/**
	 * 
	 * @param callNode
	 * @param tree
	 * @param columnExprList
	 * @throws BirtException
	 */
	private void compileAggregationFunction(Node callNode, ScriptNode tree, List columnExprList) throws BirtException {
		Node firstChild = callNode.getFirstChild();
		if (firstChild.getType() != Token.GETPROP)
			return;

		Node getPropLeftChild = firstChild.getFirstChild();
		if (getPropLeftChild.getType() == Token.NAME && getPropLeftChild.getString().equals(TOTAL))
			hasAggregation = true;

		compileComplexExpr(firstChild, tree, columnExprList);
	}

	/**
	 * extract arguments from aggregation expression
	 * 
	 * @param context
	 * @param callNode
	 * @throws BirtException
	 */
	private void extractArguments(Node callNode, ScriptNode tree, List columnExprList) throws BirtException {
		Node arg = callNode.getFirstChild().getNext();

		while (arg != null) {
			// need to hold on to the next argument because the tree extraction
			// will cause us to lose the reference otherwise
			Node nextArg = arg.getNext();
			processChild(arg, tree, columnExprList);

			arg = nextArg;
		}
	}

	/**
	 * compile the complex expression
	 * 
	 * @param complexNode
	 * @throws BirtException
	 */
	private void compileComplexExpr(Node complexNode, ScriptNode tree, List columnExprList) throws BirtException {
		Node child = complexNode.getFirstChild();
		while (child != null) {
			if (child.getType() == Token.FUNCTION) {
				int index = getFunctionIndex(child.getString(), tree);
				compileFunctionNode(tree.getFunctionNode(index), tree, columnExprList);
			}
			// keep reference to next child, since subsequent steps could
			// lose
			// the reference to it
			Node nextChild = child.getNext();

			// do not include constants into the sub-expression list
			if (child.getType() == Token.NUMBER || child.getType() == Token.STRING || child.getType() == Token.TRUE
					|| child.getType() == Token.FALSE || child.getType() == Token.NULL) {
				processChild(child, tree, columnExprList);
				child = nextChild;
				continue;
			}

			processChild(child, tree, columnExprList);
			child = nextChild;
		}
	}

	/**
	 * compile the function expression
	 * 
	 * @param node
	 * @param tree
	 * @param columnExprList
	 * @throws BirtException
	 */
	private void compileFunctionNode(FunctionNode node, ScriptNode tree, List columnExprList) throws BirtException {
		compileComplexExpr(node, tree, columnExprList);
	}

	/**
	 * get the function node index
	 * 
	 * @param functionName
	 * @param tree
	 * @return
	 */
	private int getFunctionIndex(String functionName, ScriptNode tree) {
		int index = -1;
		for (int i = 0; i < tree.getFunctionCount(); i++) {
			if (tree.getFunctionNode(i).getFunctionName().getString().equals(functionName)) {
				index = i;
				break;
			}
		}
		return index;
	}

	public boolean hasAggregation() {
		return this.hasAggregation;
	}

	public boolean isDirectColumnRef() {
		return this.isDirectColumnRef;
	}

	public void reset() {
		this.hasAggregation = false;
		this.isDirectColumnRef = false;
	}
}
