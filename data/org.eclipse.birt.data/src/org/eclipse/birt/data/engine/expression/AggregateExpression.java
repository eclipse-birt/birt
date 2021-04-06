/*
 *************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.expression;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;

/**
 * The compiled form of an expression that contains only a single aggregate
 * function call. For example, "Total.Sum(row.x)", or "Total.movingAve( row.y +
 * row.z, 30, row.z != 0 )".
 */
public final class AggregateExpression extends BytecodeExpression {
	private IAggrFunction aggregation;
	private int groupLevel;
	private List arguments;
	private int m_id; // Id of this expression in the aggregate registry

	AggregateExpression(IAggrFunction aggregation) {
		logger.entering(AggregateExpression.class.getName(), "AggregateExpression");
		this.aggregation = aggregation;
		this.arguments = new ArrayList();
		this.groupLevel = -1;
		logger.exiting(AggregateExpression.class.getName(), "AggregateExpression");
	}

	public IAggrFunction getAggregation() {
		return aggregation;
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object other) {
		if (other == null || !(other instanceof AggregateExpression))
			return false;

		AggregateExpression expr2 = (AggregateExpression) other;

		if (!aggregation.getName().equals(expr2.getAggregation().getName()))
			return false;
		if (groupLevel != expr2.getGroupLevel())
			return false;
		if (this.getCalculationLevel() != expr2.getCalculationLevel())
			return false;
		if (arguments.size() != expr2.getArguments().size())
			return false;
		for (int i = 0; i < arguments.size(); i++) {
			if (!arguments.get(i).equals(expr2.getArguments().get(i)))
				return false;
		}
		return true;
	}

	/**
	 * Returns a list of arguments for this aggregation expression. Each element is
	 * an instance of <code>CompiledExpression</code>.
	 * 
	 * @return a list of arguments for this aggregation expression.
	 */
	public List getArguments() {
		return arguments;
	}

	/**
	 * Adds the specific <code>CompiledExpression</code> as an argument to this
	 * <code>AggregateExpression</code>.
	 * 
	 * @param expr the <code>CompiledExpression</code> argument for this
	 *             <code>AggregateExpression</code>.
	 */
	void addArgument(CompiledExpression expr) {
		assert (expr != null);
		arguments.add(expr);
	}

	public int getType() {
		return TYPE_SINGLE_AGGREGATE;
	}

	// Sets the ID of this aggregate expression in the aggregate registry
	public void setRegId(int id) {
		m_id = id;
	}

	// Gets the ID of this aggregate expression in the registry
	public int getRegId() {
		return m_id;
	}

	/**
	 * Return the calculation level of this aggregation expression.
	 * 
	 * @return
	 */
	public int getCalculationLevel() {
		if (!this.isNestedAggregation())
			return 0;

		int result = this.groupLevel;

		for (int i = 0; i < this.arguments.size(); i++) {
			if (this.arguments.get(i) instanceof BytecodeExpression) {
				int level = ((BytecodeExpression) this.arguments.get(i)).getGroupLevel();
				if (level > result)
					result = level;

				if (level == 0)
					return 0;
			}
		}
		return result;
	}

	/**
	 * 
	 * @param groupLevel
	 */
	public void setGroupLevel(int groupLevel) {
		this.groupLevel = groupLevel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.expression.BytecodeExpression#getGroupLevel()
	 */
	public int getGroupLevel() {
		return this.groupLevel;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isNestedAggregation() {
		if (this.arguments != null) {
			for (int i = 0; i < this.arguments.size(); i++) {
				if (this.arguments.get(i) instanceof AggregateExpression) {
					return true;
				}
				if (this.arguments.get(i) instanceof ComplexExpression) {
					if (hasAggregationInComplexExpression((ComplexExpression) this.arguments.get(i)))
						return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param expr
	 * @return
	 */
	private boolean hasAggregationInComplexExpression(ComplexExpression expr) {
		Collection collection = expr.getSubExpressions();
		if (collection != null) {
			Iterator it = collection.iterator();
			if (it.hasNext()) {
				Object o = it.next();
				if (o instanceof AggregateExpression) {
					return true;
				}

				if (o instanceof ComplexExpression) {
					if (hasAggregationInComplexExpression((ComplexExpression) o)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int result = 17;

		result = 37 * result + aggregation.getName().hashCode();
		result = 37 * result + groupLevel;
		for (int i = 0; i < arguments.size(); i++) {
			result = 37 * result + arguments.get(i).hashCode();
		}
		return result;
	}
}
