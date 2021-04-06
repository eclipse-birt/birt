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

package org.eclipse.birt.data.engine.expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IDataScriptEngine;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.querydefn.BaseExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.IRFactory;
import org.mozilla.javascript.Interpreter;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.ScriptNode;

/**
 * This class provides default implementations for the compilation of ROM
 * JavaScript expressions interface.
 */
abstract class AbstractExpressionCompiler {

	private CompilerEnvirons m_compilerEnv;
	private final static String TOTAL = "Total";
	protected final static String STRING_ROW = "row";
	protected final static String STRING_DATASETROW = "dataSetRow";
	protected String rowIndicator = STRING_ROW;
	private boolean isDataSetMode = true;

	private IScriptExpression scriptExpr;

	public void compile(IBaseExpression expr, ScriptContext context) throws DataException {
		if (expr instanceof IScriptExpression) {
			compile((IScriptExpression) expr, context);
		} else if (expr instanceof IConditionalExpression) {
			IConditionalExpression ce = (IConditionalExpression) expr;
			compile(ce.getExpression(), context);
			compile(ce.getOperand1(), context);
			compile(ce.getOperand2(), context);
		}
	}

	/**
	 * Compile the script expression.
	 * 
	 * @param baseExpr
	 * @param context
	 * @return
	 */
	private void compile(IScriptExpression baseExpr, ScriptContext context) throws DataException {
		if (baseExpr == null)
			return;
		CompiledExpression handle = this.compileExpression(baseExpr, context);
		baseExpr.setHandle(handle);
	}

	/**
	 * compile the scriptExpresion to generate a subclass of compiledExpression.
	 * 
	 * @param exp
	 * @param context
	 * @return
	 */
	protected CompiledExpression compileExpression(IScriptExpression baseExpr, ScriptContext context)
			throws DataException {
		String exp = "";
		try {
			this.scriptExpr = baseExpr;
			exp = baseExpr.getText();
			if (exp == null || BaseExpression.constantId.equals(baseExpr.getScriptId()))
				return null;
			IDataScriptEngine engine = (IDataScriptEngine) context.getScriptEngine(IDataScriptEngine.ENGINE_NAME);

			ScriptNode tree = parse(exp, engine.getJSContext(context));
			return processScriptTree(exp, tree, engine.getJSContext(context));
		} catch (Exception e) {
			DataException dataException = new DataException(ResourceConstants.INVALID_JS_EXPR, e, exp);
			throw dataException;
		}
	}

	/**
	 * compile the string expression
	 * 
	 * @param expression
	 * @param context
	 * @return
	 * @throws DataException
	 */
	protected CompiledExpression compileExpression(String expression, ScriptContext context) throws DataException {
		String exp = "";
		try {
			exp = expression;
			if (exp == null)
				return null;

			IDataScriptEngine engine = (IDataScriptEngine) context.getScriptEngine(IDataScriptEngine.ENGINE_NAME);

			ScriptNode tree = parse(exp, engine.getJSContext(context));
			return processScriptTree(exp, tree, engine.getJSContext(context));
		} catch (Exception e) {
			DataException dataException = new DataException(ResourceConstants.INVALID_JS_EXPR, e, exp);
			throw dataException;
		}
	}

	/**
	 * 
	 * @return scriptExpression
	 */
	protected IScriptExpression getScriptExpression() {
		return scriptExpr;
	}

	/**
	 * get compiled environment
	 * 
	 * @param context
	 * @return
	 */
	private CompilerEnvirons getCompilerEnv(Context context) {
		if (m_compilerEnv == null) {
			m_compilerEnv = new CompilerEnvirons();
			m_compilerEnv.initFromContext(context);
		}
		return m_compilerEnv;
	}

	/**
	 * process the script tree to produce a <code>CompiledExpression</code>
	 * 
	 * @param expression
	 * @param tree
	 * @param context
	 * @return @throws DataException
	 */
	private CompiledExpression processScriptTree(String expression, ScriptNode tree, Context context)
			throws DataException

	{
		CompiledExpression expr;
		if (tree.getFirstChild() == tree.getLastChild()) {
			if (tree.getFirstChild() == null)
				throw new DataException("Expression parse error: first child is null. The expression is " + expression,
						expression);
			// A single expression
			if (tree.getFirstChild().getType() != Token.EXPR_RESULT && tree.getFirstChild().getType() != Token.EXPR_VOID
					&& tree.getFirstChild().getType() != Token.BLOCK) {
				// This should never happen?
				throw new DataException(ResourceConstants.INVALID_JS_EXPR, expression);
			}
			Node child, parent = tree;
			Node exprNode = parent.getFirstChild();
			child = exprNode.getFirstChild();
			if (child.getNext() != null)
				child = exprNode;
			else {
				parent = exprNode;
			}
			assert (child != null && parent != null);
			expr = processChild(context, false, parent, child, tree);
		} else {
			// complex expressions
			// Multiple expressions exist; we should produce complex expressions
			// However, individual subexpressions still needs to be processed
			// to identify the interesting subexpressions
			expr = compileComplexExpr(context, tree, false);
		}
		if (expr instanceof BytecodeExpression)
			compileForBytecodeExpr(context, tree, expr);
		return expr;
	}

	/**
	 * parse the expression to script tree
	 * 
	 * @param expression
	 * @param cx
	 * @return
	 * @throws DataException
	 */
	protected ScriptNode parse(String expression, Context cx) throws DataException {
		if (expression == null)
			throw new DataException(ResourceConstants.EXPRESSION_CANNOT_BE_NULL_OR_BLANK);

		CompilerEnvirons compilerEnv = getCompilerEnv(cx);
		Parser p = new Parser(compilerEnv, cx.getErrorReporter());
		AstRoot root = p.parse(expression, null, 0);
		IRFactory ir = new IRFactory(compilerEnv);
		ScriptNode script = ir.transformTree(root);
		return script;
	}

	/**
	 * 
	 * returns the compiled expression from processing a child node
	 * 
	 * @param context
	 * @param customerChecked
	 * @param parent
	 * @param child
	 * @return
	 * @throws DataException
	 */
	protected CompiledExpression processChild(Context context, boolean customerChecked, Node parent, Node child,
			Node grandFather) throws DataException {
		CompiledExpression compiledExpr = null;
		switch (child.getType()) {
		case Token.NUMBER:
			compiledExpr = new ConstantExpression(child.getDouble());
			break;

		case Token.STRING:
			compiledExpr = new ConstantExpression(child.getString());
			break;

		case Token.NULL:
			compiledExpr = new ConstantExpression();
			break;

		case Token.TRUE:
			compiledExpr = new ConstantExpression(true);
			break;

		case Token.FALSE:
			compiledExpr = new ConstantExpression(false);
			break;

		case Token.GETPROP: {
			ConstantExpression ce = AggregationConstantsUtil.getConstantExpression(child);
			if (ce != null) {
				compiledExpr = ce;
				break;
			}
		}

		case Token.GETELEM:
			compiledExpr = compileDirectColRefExpr(parent, child, grandFather, customerChecked, context);
			break;

		case Token.CALL: {
			compiledExpr = compileAggregateExpr(context, parent, child);
			break;
		}
		}
		if (compiledExpr == null)
			compiledExpr = compileComplexExpr(context, child, customerChecked);
		return compiledExpr;
	}

	/**
	 * Check if the expression is a direct column reference type. If so, returns an
	 * instance of DirectColRefExpr that represents it; otherwise returns null.
	 * 
	 * @param parent
	 * @param refNode
	 * @param customerChecked
	 * @return
	 * @throws DataException
	 */
	protected abstract CompiledExpression compileDirectColRefExpr(Node parent, Node refNode, Node grandFather,
			boolean customerChecked, Context context) throws DataException;

	/**
	 * Check if the expression is a direct column reference type. If so, returns an
	 * instance of DirectColRefExpr that represents it; otherwise returns null.
	 * 
	 * @param refNode
	 * @param customerChecked
	 * @return
	 * @throws DataException
	 */
	protected ColumnReferenceExpression compileColRefExpr(Node refNode, boolean customerChecked) throws DataException {
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
			return new ColumnReferenceExpression(this.isDataSetMode ? STRING_ROW : STRING_DATASETROW,
					rowColumn.getString());
		}
		if (refNode.getType() == Token.GETELEM) {
			if (rowColumn.getType() == Token.NUMBER) {
				return new ColumnReferenceExpression(this.isDataSetMode ? STRING_ROW : STRING_DATASETROW,
						(int) rowColumn.getDouble());
			} else if (rowColumn.getType() == Token.STRING) {
				return new ColumnReferenceExpression(this.isDataSetMode ? STRING_ROW : STRING_DATASETROW,
						rowColumn.getString());
			}
		}
		// right side is not a STRING or a NUMBER, which is what is needed for
		// a direct column reference. so it could be something
		// like row[getColumnIndex()] and that would be a complex expression
		return null;
	}

	/**
	 * Check the expression is an aggregate expression. If so, returns an instance
	 * of AggregateExpression, otherwise return null
	 * 
	 * @param context
	 * @param parent
	 * @param callNode
	 * @return
	 * @throws DataException
	 */
	protected abstract AggregateExpression compileAggregateExpr(Context context, Node parent, Node callNode)
			throws DataException;

	/**
	 * Check the expression is a complex expression. If so, returns an instance of
	 * ComplexExpression, otherwise return null
	 * 
	 * @param context
	 * @param complexNode
	 * @return @throws DataException
	 */
	protected ComplexExpression compileComplexExpr(Context context, Node complexNode, boolean checked)
			throws DataException {
		ComplexExpression complexExpr = new ComplexExpression();
		Node child = complexNode.getFirstChild();
		complexExpr.addTokenList(Integer.valueOf(complexNode.getType()));
		while (child != null) {
			// keep reference to next child, since subsequent steps could lose
			// the reference to it
			Node nextChild = child.getNext();

			// do not include constants into the sub-expression list
			if (child.getType() == Token.NUMBER || child.getType() == Token.STRING || child.getType() == Token.TRUE
					|| child.getType() == Token.FALSE || child.getType() == Token.NULL) {
				CompiledExpression subExpr = processChild(context, false, complexNode, child, null);
				if (subExpr instanceof ConstantExpression)
					complexExpr.addContantsExpressions(subExpr);
				child = nextChild;
				continue;
			}

			CompiledExpression subExpr = processChild(context, checked, complexNode, child, null);
			complexExpr.addSubExpression(subExpr);
			complexExpr.addTokenList(Integer.valueOf(child.getType()));
			child = nextChild;
		}

		flattenNestedComplexExprs(complexExpr);

		return complexExpr;
	}

	/**
	 * compile the tree to script
	 * 
	 * @param context
	 * @param tree
	 * @param expr
	 */
	protected void compileForBytecodeExpr(Context context, ScriptNode tree, CompiledExpression expr) {
		assert (expr instanceof BytecodeExpression);
		CompilerEnvirons compilerEnv = getCompilerEnv(context);
		Interpreter compiler = new Interpreter();
		Object compiledOb = compiler.compile(compilerEnv, tree, null, false);
		Script script = (Script) compiler.createScriptObject(compiledOb, null);
		((BytecodeExpression) expr).setScript(script);
	}

	/**
	 * An aggregation expression in the form of Total.xxx for example Total.sum(
	 * row.x ) This means the first child is a GETPROP node, and its left child is
	 * "Total" and its right child is "sum"
	 * 
	 * @param callNode
	 * @return @throws DataException
	 */
	protected IAggrFunction getAggregationFunction(Node callNode) throws DataException {

		Node firstChild = callNode.getFirstChild();
		if (firstChild.getType() != Token.GETPROP)
			return null;

		Node getPropLeftChild = firstChild.getFirstChild();
		if (getPropLeftChild.getType() != Token.NAME || !getPropLeftChild.getString().equals(TOTAL))
			return null;

		Node getPropRightChild = firstChild.getLastChild();
		if (getPropRightChild.getType() != Token.STRING)
			return null;

		String aggrFuncName = getPropRightChild.getString();
		IAggrFunction agg = AggregationManager.getInstance().getAggregation(aggrFuncName);
		if (agg == null) {
			// Aggr function name after Total is invalid; this will eventuall
			// cause
			// an error. Report error now
			throw new DataException(ResourceConstants.INVALID_TOTAL_NAME, aggrFuncName);
		}
		return agg;
	}

	/**
	 * flatten out the tree when there are nested ComplexExpressions ( i.e.
	 * ComplexExpression with ComplexExpressions as sub expressions ) the nesting
	 * provides no additional information for the calculation to use. this nesting
	 * could occur depending on the Rhino parse tree that's returned by Rhino, and
	 * the limited way we can traverse down the parse tree.
	 * 
	 * @param complexExpr
	 */
	private void flattenNestedComplexExprs(ComplexExpression complexExpr) {
		ArrayList interestingSubExpr = new ArrayList();
		ArrayList interestingConstantExpr = new ArrayList();
		ArrayList tokenList = new ArrayList();

		Collection subExprs = complexExpr.getSubExpressions();
		Iterator iter = subExprs.iterator();
		while (iter.hasNext()) {
			CompiledExpression childExpr = (CompiledExpression) iter.next();
			if (childExpr instanceof ColumnReferenceExpression || childExpr instanceof AggregateExpression)
				interestingSubExpr.add(childExpr);
			else if (childExpr instanceof ComplexExpression) {
				Collection childSubExprs = ((ComplexExpression) childExpr).getSubExpressions();

				childSubExprs.addAll(((ComplexExpression) childExpr).getConstantExpressions());
				tokenList.addAll(((ComplexExpression) childExpr).getTokenList());

				Iterator childIter = childSubExprs.iterator();
				while (childIter.hasNext()) {
					CompiledExpression childChildExpr = (CompiledExpression) childIter.next();
					if (childChildExpr instanceof ColumnReferenceExpression
							|| childChildExpr instanceof AggregateExpression)
						interestingSubExpr.add(childChildExpr);
					else if (childChildExpr instanceof ConstantExpression)
						interestingConstantExpr.add(childChildExpr);
					else
						assert false;
				}

			} else
				assert false;

			iter.remove();
		}

		complexExpr.addSubExpressions(interestingSubExpr);
		complexExpr.addContantsExpressions(interestingConstantExpr);
		complexExpr.getTokenList().addAll(tokenList);
	}

	/**
	 * 
	 * @param isDataSetMode
	 */
	public void setDataSetMode(boolean isDataSetMode) {
		this.isDataSetMode = isDataSetMode;
		if (isDataSetMode) {
			this.rowIndicator = STRING_ROW;
		} else {
			this.rowIndicator = STRING_DATASETROW;
		}
	}

	/**
	 * 
	 * @return
	 */
	protected boolean getDataSetMode() {
		return this.isDataSetMode;
	}
}