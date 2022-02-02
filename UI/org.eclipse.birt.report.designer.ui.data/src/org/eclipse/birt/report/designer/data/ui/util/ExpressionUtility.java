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
package org.eclipse.birt.report.designer.data.ui.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.IRFactory;
import org.mozilla.javascript.Node;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.ScriptNode;

/**
 * The utility class of expression, if the expression is column, return true,
 * else return false. The column format should like row.aaa , row["aaa"] or
 * row[index], dataSetRow.aaa, dataSetRow["xxx"] or dataSetRow[index]
 */
public class ExpressionUtility {
	private final static String STRING_ROW = "row"; //$NON-NLS-1$
	private final static String STRING_DATASET_ROW = "dataSetRow"; //$NON-NLS-1$
	// the default cache size
	private final static int EXPR_CACHE_SIZE = 20;
	/**
	 * Use the LRU cache for the compiled expression.For performance reasons, The
	 * compiled expression put in a cache. Repeated compile of the same expression
	 * will then used the cached value.
	 */
	private static Map compiledExprCacheInRowMode = Collections
			.synchronizedMap(new LinkedHashMap(EXPR_CACHE_SIZE, (float) 0.75, true) {

				private static final long serialVersionUID = 54331232145454L;

				protected boolean removeEldestEntry(Map.Entry eldest) {
					return size() > EXPR_CACHE_SIZE;
				}
			});
	private static Map compiledExprCacheInDataSetRowMode = Collections
			.synchronizedMap(new LinkedHashMap(EXPR_CACHE_SIZE, (float) 0.75, true) {

				private static final long serialVersionUID = 54331232145454L;

				protected boolean removeEldestEntry(Map.Entry eldest) {
					return size() > EXPR_CACHE_SIZE;
				}
			});

	/**
	 * whether the expression is column reference
	 * 
	 * @param expression
	 * @return
	 */
	public static boolean isColumnExpression(String expression, boolean mode) {
		boolean isColumn = false;
		if (expression == null || expression.trim().length() == 0)
			return isColumn;
		if (getCompiledExpCacheMap(mode).containsKey(expression)) {
			return ((Boolean) getCompiledExpCacheMap(mode).get(expression)).booleanValue();
		}
		Context context = Context.enter();
		ScriptNode tree;
		try {
			CompilerEnvirons m_compilerEnv = new CompilerEnvirons();
			m_compilerEnv.initFromContext(context);
			Parser p = new Parser(m_compilerEnv, context.getErrorReporter());
			AstRoot root = p.parse(expression, null, 0);
			IRFactory ir = new IRFactory(m_compilerEnv);
			tree = ir.transformTree(root);
		} catch (Exception e) {
			getCompiledExpCacheMap(mode).put(expression, Boolean.valueOf(false));
			return false;
		} finally {
			Context.exit();
		}

		if (tree.getFirstChild() == tree.getLastChild()) {
			// A single expression
			if (tree.getFirstChild().getType() != Token.EXPR_RESULT && tree.getFirstChild().getType() != Token.EXPR_VOID
					&& tree.getFirstChild().getType() != Token.BLOCK) {
				isColumn = false;
			}
			Node exprNode = tree.getFirstChild();
			Node child = exprNode.getFirstChild();
			assert (child != null);
			if (child.getType() == Token.GETELEM || child.getType() == Token.GETPROP)
				isColumn = getDirectColRefExpr(child, mode);
			else
				isColumn = false;
		} else {
			isColumn = false;
		}
		getCompiledExpCacheMap(mode).put(expression, Boolean.valueOf(isColumn));
		return isColumn;
	}

	/**
	 * 
	 * @param mode
	 * @return
	 */
	private static Map getCompiledExpCacheMap(boolean mode) {
		return mode ? compiledExprCacheInRowMode : compiledExprCacheInDataSetRowMode;
	}

	/**
	 * replace the row[], row.xx with dataSetRow[],dataSetRow.xx
	 * 
	 * @param refNode
	 * @return
	 */
	public static String getReplacedColRefExpr(String columnStr) {
		if (isColumnExpression(columnStr, true)) {
			return columnStr.replaceFirst("\\Qrow\\E", "dataSetRow"); //$NON-NLS-1$ //$NON-NLS-2$
		} else
			return columnStr;
	}

	/**
	 * if the Node is row Node, return true
	 * 
	 * @param refNode
	 * @return
	 */
	private static boolean getDirectColRefExpr(Node refNode, boolean mode) {
		assert (refNode.getType() == Token.GETPROP || refNode.getType() == Token.GETELEM);

		Node rowName = refNode.getFirstChild();
		assert (rowName != null);
		if (rowName.getType() != Token.NAME)
			return false;

		String str = rowName.getString();
		assert (str != null);
		if (mode && !str.equals(STRING_ROW))
			return false;
		else if (!mode && !str.equals(STRING_DATASET_ROW))
			return false;

		Node rowColumn = rowName.getNext();
		assert (rowColumn != null);

		if (refNode.getType() == Token.GETPROP && rowColumn.getType() == Token.STRING) {
			return true;
		} else if (refNode.getType() == Token.GETELEM) {
			if (rowColumn.getType() == Token.NUMBER || rowColumn.getType() == Token.STRING)
				return true;
		}

		return false;
	}
}
