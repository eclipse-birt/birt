/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

/**
 * A Javascript expression that cannot be reduced to other simpler types of
 * CompiledExpression. An expression of this type has been compiled to Rhino
 * byte code.
 */
public final class ComplexExpression extends BytecodeExpression {
	private ArrayList m_subExpressions;
	private ArrayList m_tokenList;
	private ArrayList m_constantExpressions;

	ComplexExpression() {
		logger.entering(ComplexExpression.class.getName(), "ComplexExpression");
		m_subExpressions = new ArrayList();
		m_tokenList = new ArrayList();
		m_constantExpressions = new ArrayList();
		logger.exiting(ComplexExpression.class.getName(), "ComplexExpression");
	}

	/**
	 * 
	 */
	public int getType() {
		return CompiledExpression.TYPE_COMPLEX_EXPR;
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (other == null || !(other instanceof ComplexExpression))
			return false;

		ComplexExpression expr2 = (ComplexExpression) other;

		if (m_subExpressions.size() != expr2.getSubExpressions().size())
			return false;
		if (m_tokenList.size() != expr2.getTokenList().size())
			return false;
		if (m_constantExpressions.size() != expr2.getConstantExpressions().size())
			return false;

		// compare the sub expression
		Iterator iter = expr2.getSubExpressions().iterator();
		for (int i = 0; i < m_subExpressions.size(); i++) {
			if (!m_subExpressions.get(i).equals(iter.next()))
				return false;
		}
		// compare the token list
		Iterator tokenIterator = expr2.getTokenList().iterator();
		for (int i = 0; i < m_tokenList.size(); i++) {
			if (!m_tokenList.get(i).equals(tokenIterator.next()))
				return false;
		}
		// compare the constant expression list
		Iterator constantIterator = expr2.getConstantExpressions().iterator();
		for (int i = 0; i < m_constantExpressions.size(); i++) {
			if (!m_constantExpressions.get(i).equals(constantIterator.next()))
				return false;
		}
		return true;
	}

	/**
	 * Adds a <code>Collection</code> of subexpressions to this
	 * <code>ComplexExpression</code>. This is a helper method to consolidate nested
	 * <code>ComplexExpression</code>.
	 * 
	 * @param subExprs the <code>Collection</code> of subexpressions.
	 */
	void addSubExpressions(Collection subExprs) {
		assert (subExprs != null);
		m_subExpressions.addAll(subExprs);
	}

	/**
	 * Adds a subexpression to this <code>ComplexExpression</code>. Each
	 * subexpression is an instance of <code>CompiledExpression</code>.
	 * 
	 * @param subExpr the subexpression to add to this
	 *                <code>CompiledExpression</code>.
	 */
	void addSubExpression(CompiledExpression subExpr) {
		assert (subExpr != null);
		m_subExpressions.add(subExpr);
	}

	/**
	 * Adds a <code>Collection</code> of <code>ConstantExpression<code> to this
	 * <code>ComplexExpression</code>. This is a helper method to consolidate nested
	 * <code>ComplexExpression</code>.
	 * 
	 * @param subExprs the <code>Collection<code> of <code>ConstantExpression<code>.
	 */
	void addContantsExpressions(Collection subExprs) {
		assert (subExprs != null);
		m_constantExpressions.addAll(subExprs);
	}

	/**
	 * Adds a <code>ConstantExpression</code> to this
	 * <code>ComplexExpression</code>. This is a helper method to consolidate nested
	 * <code>ComplexExpression</code>.
	 * 
	 * @param subExprs the <code>ConstantExpression</code>.
	 */
	void addContantsExpressions(CompiledExpression subExprs) {
		assert (subExprs != null);
		m_constantExpressions.add(subExprs);
	}

	/**
	 * add the token existing in the complex expression. These infomation can be
	 * used in comparision.
	 * 
	 * @param token
	 */
	void addTokenList(Integer token) {
		m_tokenList.add(token);
	}

	/**
	 * Returen a <code>Collection</code> of Token value.
	 * 
	 * @return
	 */
	Collection getTokenList() {
		return m_tokenList;
	}

	/**
	 * Returen a <code>Collection</code> of ConstantExpression value. These constant
	 * expression is contains in the complex expression.
	 * 
	 * @return
	 */
	public Collection getConstantExpressions() {
		return m_constantExpressions;
	}

	/**
	 * Returns a <code>Collection</code> of subexpressions in this
	 * <code>ComplexExpression</code>. Each instance in the <code>Collection</code>
	 * is a <code>CompiledExpression</code>.
	 * 
	 * @return a <code>Collection</code> of subexpression associated with this
	 *         <code>ComplexExpression</code>.
	 */
	public Collection getSubExpressions() {
		return m_subExpressions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.expression.BytecodeExpression#getGroupLevel()
	 */
	public int getGroupLevel() {
		int result = -1;
		boolean isOverall = false;
		for (int i = 0; i < m_subExpressions.size(); i++) {
			if (m_subExpressions.get(i) instanceof BytecodeExpression) {
				int level = ((BytecodeExpression) m_subExpressions.get(i)).getGroupLevel();
				if (level > result)
					result = level;
				if (level == 0)
					isOverall = true;
			} else if (m_subExpressions.get(i) instanceof ColumnReferenceExpression) {
				isOverall = true;
			}
		}
		if (result != -1 && isOverall)
			result = 0;
		return result;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int result = 17;
		for (int i = 0; i < m_subExpressions.size(); i++) {
			result = 37 * result + m_subExpressions.get(i).hashCode();
		}
		for (int i = 0; i < m_tokenList.size(); i++) {
			result = 37 * result + m_tokenList.get(i).hashCode();
		}
		for (int i = 0; i < m_constantExpressions.size(); i++) {
			result = 37 * result + m_constantExpressions.get(i).hashCode();
		}
		return result;
	}
}
