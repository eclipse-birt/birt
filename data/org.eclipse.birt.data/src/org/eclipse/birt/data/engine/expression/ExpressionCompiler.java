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

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateRegistry;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstRoot;

/**
 * 
 * This class handles the compilation of ROM JavaScript expressions to be
 * evaluated by the DtE at report query preparation time. Each expression is
 * compiled to generate a handle, which is an instance of CompiledExpression or
 * its derived class. The expression handle is used by the factory to evaluate
 * the expression after the report query is executed. <br>
 * ExpressionCompiler compiles the expression into Rhino byte code for faster
 * evaluation at runtime.
 */
public class ExpressionCompiler extends AbstractExpressionCompiler {
	private AggregateRegistry registry;
	private final static String AGGR_VALUE = "_aggr_value";

	/**
	 * Compiles a Javascript expression to produce a subclass of CompileExpression,
	 * which contains the compiled form of the JS expression, after suitable
	 * replacement of aggregate functin occurrences in the expression. Aggregate
	 * function calls that appear in the expression are registered with the provided
	 * AggregateRegistry instance.
	 * <p>
	 * Aggregate registry can be null, in which case the provided expression is
	 * expected NOT to contain aggregates. If it does, a DteException is thrown.
	 * 
	 * @param expression Text of expression to compile
	 * @param registry   Registry for aggregate expressions. Can be null if
	 *                   expression is not expected to contain aggregates
	 * @param context    Rhino context associated with current thread
	 * @return
	 */
	public CompiledExpression compile(String expression, AggregateRegistry registry, ScriptContext context) {
		try {
			if (expression == null || expression.trim().length() == 0)
				throw new DataException(ResourceConstants.EXPRESSION_CANNOT_BE_NULL_OR_BLANK);

			this.registry = registry;
			return super.compileExpression(expression, context);
		} catch (Exception e) {
			if (e instanceof DataException) {
				return new InvalidExpression((DataException) e);
			} else {
				// Here exception will not be thrown, since invalid
				// expression is not such a fatal error that requires
				// stop generating report.
				DataException dataException = new DataException(ResourceConstants.INVALID_JS_EXPR, e, expression);
				return new InvalidExpression(dataException);
			}
		}
	}

	/**
	 * @param context
	 * @param aggregateExpression
	 * @param callNode
	 * @throws DataException
	 */
	private void extractArguments(Context context, AggregateExpression aggregateExpression, Node callNode)
			throws DataException {
		Node arg = callNode.getFirstChild().getNext();

		while (arg != null) {
			// need to hold on to the next argument because the tree extraction
			// will cause us to lose the reference otherwise
			Node nextArg = arg.getNext();

			CompiledExpression expr = processChild(context, false, callNode, arg, null);
			if (!(expr instanceof BytecodeExpression)) {
				aggregateExpression.addArgument(expr);
				arg = nextArg;
				continue;
			}

			AstRoot tree = new AstRoot(Token.SCRIPT);
			Node exprNode = new Node(Token.EXPR_RESULT);
			exprNode.addChildToFront(arg);
			tree.addChildrenToFront(exprNode);
			compileForBytecodeExpr(context, tree, expr);
			aggregateExpression.addArgument(expr);

			arg = nextArg;
		}
	}

	/**
	 * @param aggregateExpression
	 * @param parent
	 * @param aggregateCallNode
	 * @throws DataException
	 */
	private void replaceAggregateNode(AggregateExpression aggregateExpression, Node parent, Node aggregateCallNode)
			throws DataException {
		if (registry == null)
			throw new DataException(ResourceConstants.INVALID_CALL_AGGR);

		// replace the aggregate CALL node with _aggr_value[<aggregateId>]
		int aggregateId = registry.register(aggregateExpression);
		Node newFirstChild = Node.newString(Token.NAME, AGGR_VALUE);
		Node newSecondChild = Node.newNumber(aggregateId);
		Node aggregateNode = new Node(Token.GETELEM, newFirstChild, newSecondChild);
		parent.replaceChild(aggregateCallNode, aggregateNode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.expression.AbstractExpressionParser#
	 * compileAggregateExpr(org.mozilla.javascript.Context,
	 * org.mozilla.javascript.Node, org.mozilla.javascript.Node)
	 */
	protected AggregateExpression compileAggregateExpr(Context context, Node parent, Node callNode)
			throws DataException {
		assert (callNode.getType() == Token.CALL);

		IAggrFunction aggregation = getAggregationFunction(callNode);
		// not an aggregation function being called, then it's considered
		// a complex expression
		if (aggregation == null)
			return null;

		AggregateExpression aggregateExpression = new AggregateExpression(aggregation);

		extractArguments(context, aggregateExpression, callNode);
		replaceAggregateNode(aggregateExpression, parent, callNode);

		return aggregateExpression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.expression.AbstractExpressionParser#
	 * compileDirectColRefExpr(org.mozilla.javascript.Node,
	 * org.mozilla.javascript.Node, boolean)
	 */
	protected CompiledExpression compileDirectColRefExpr(Node parent, Node refNode, Node grandFather,
			boolean customerChecked, Context context) throws DataException {
		// if it's a GETPROP or GETELEM with row on the left side,
		// and either a STRING or NUMBER on the right side, then it's
		// a direct column reference
		assert (refNode.getType() == Token.GETPROP || refNode.getType() == Token.GETELEM);

		Node rowName = refNode.getFirstChild();
		assert (rowName != null);
		if (rowName.getType() != Token.NAME)
			return null;

		String str = rowName.getString();
		assert (str != null);
		if (!str.equals(rowIndicator))
			return null;

		Node rowColumn = rowName.getNext();
		assert (rowColumn != null);

		if (refNode.getType() == Token.GETPROP && rowColumn.getType() == Token.STRING) {
			if (ScriptConstants.OUTER_RESULT_KEYWORD.equals(rowColumn.getString())
					|| ScriptConstants.ROW_NUM_KEYWORD.equals(rowColumn.getString())
					|| "0".equals(rowColumn.getString()))
				return null;

			return new ColumnReferenceExpression(getDataSetMode() ? STRING_ROW : STRING_DATASETROW,
					rowColumn.getString());
		}
		if (refNode.getType() == Token.GETELEM) {
			if (rowColumn.getType() == Token.NUMBER) {
				if (0 == rowColumn.getDouble())
					return null;
				return new ColumnReferenceExpression(getDataSetMode() ? STRING_ROW : STRING_DATASETROW,
						(int) rowColumn.getDouble());
			} else if (rowColumn.getType() == Token.STRING) {
				if ("_rownum".equals(rowColumn.getString()))
					return null;
				return new ColumnReferenceExpression(getDataSetMode() ? STRING_ROW : STRING_DATASETROW,
						rowColumn.getString());
			}
		}

		// right side is not a STRING or a NUMBER, which is what is needed for
		// a direct column reference. so it could be something
		// like row[getColumnIndex()] and that would be a complex expression
		return null;
	}

}
