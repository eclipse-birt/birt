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
package org.eclipse.birt.data.engine.impl.aggregation;

import java.util.Objects;

import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.expression.CompiledExpression;

/**
 * Describes one aggregate expression This info contains the full information
 * about one aggregate expression
 */
class AggrExprInfo {
	// Aggregate function
	IAggrFunction aggregation;
	// Grouping level of the aggr expression. 0 = entire list, 1 = outermost
	// group etc.
	int groupLevel = -1;
	// Filtering condition for the aggregate
	CompiledExpression filter;
	// Arguments to the aggregate function
	CompiledExpression[] args;

	int calculateLevel;

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof AggrExprInfo)) {
			return false;
		}
		AggrExprInfo rhs = (AggrExprInfo) other;
		if (aggregation != rhs.aggregation || groupLevel != rhs.groupLevel || args.length != rhs.args.length
				|| calculateLevel != rhs.calculateLevel) {
			return false;
		}

		if (!Objects.equals(filter, rhs.filter)) {
			return false;
		}

		for (int i = 0; i < args.length; i++) {
			if (!args[i].equals(rhs.args[i])) {
				return false;
			}
		}
		return true;
	}

	/*
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + (filter == null ? 0 : filter.hashCode());
		for (int i = 0; i < args.length; i++) {
			result = 37 * result + args[i].hashCode();
		}
		return result;
	}
}
