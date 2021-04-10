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

package org.eclipse.birt.data.engine.executor.transform;

import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.FilterByRow;
import org.eclipse.birt.data.engine.impl.IExecutorHelper;
import org.eclipse.birt.data.engine.script.FilterPassController;
import org.eclipse.birt.data.engine.script.NEvaluator;

/**
 * The utiliy class which serves for filtering purpose.
 */
public final class FilterUtil {

	/**
	 * No instance
	 */
	private FilterUtil() {
	}

	/**
	 * Exam whether one filter need multipass. A filter that need multipass is the
	 * Top/Bottom N filters
	 * 
	 * @param filter
	 * @return
	 */
	public static boolean isFilterNeedMultiPass(IFilterDefinition filter) {
		return (filter.getExpression() instanceof IConditionalExpression
				&& isMultiPassFilterOperator(((IConditionalExpression) filter.getExpression()).getOperator()));
	}

	/**
	 * Exam whether one operatorId refer to a multipass filter operator
	 * 
	 * @param operatorId
	 * @return
	 */
	private static boolean isMultiPassFilterOperator(int operatorId) {
		if (operatorId == IConditionalExpression.OP_BOTTOM_N || operatorId == IConditionalExpression.OP_BOTTOM_PERCENT
				|| operatorId == IConditionalExpression.OP_TOP_N || operatorId == IConditionalExpression.OP_TOP_PERCENT)
			return true;
		return false;
	}

	/**
	 * Prepares a filter expression for top(n)/bottom(n) evaluation Each
	 * top(n)/bottom(n) expression is tagged with a NEvaluator handle TODO: this
	 * code is temporary
	 * 
	 * @param expr
	 */
	public static void prepareFilterExpression(String tempDir, IBaseExpression expr, FilterPassController filterPass,
			IExecutorHelper helper) throws DataException {
		// Check if this is a top/bottom(n) expressions
		if (expr instanceof IConditionalExpression) {
			ConditionalExpression ce = (ConditionalExpression) expr;
			int operator = ce.getOperator();
			if (operator == IConditionalExpression.OP_TOP_N || operator == IConditionalExpression.OP_TOP_PERCENT
					|| operator == IConditionalExpression.OP_BOTTOM_N
					|| operator == IConditionalExpression.OP_BOTTOM_PERCENT) {
				// Check if we have already prepared an NEvaluator for this
				// expression
				Object handle = expr.getHandle();
				if (handle instanceof NEvaluator) {
					// Already prepared; no-op
					return;
				}

				// Tag expression with NEvaluator
				NEvaluator evaluator = NEvaluator.newInstance(tempDir, operator, ce.getExpression(),
						(IScriptExpression) ce.getOperand1(), filterPass);
				expr.setHandle(evaluator);
			}
		}

	}

	/**
	 * Return whether there are multipass filters in the query.
	 * 
	 * @param fetchEventsList
	 * @return
	 * @throws DataException
	 */
	public static boolean hasMultiPassFilters(FilterByRow filterByRow) throws DataException {
		if (filterByRow == null)
			return false;
		if (filterByRow.isFilterSetExist(FilterByRow.GROUP_FILTER))
			return true;
		if (filterByRow.isFilterSetExist(FilterByRow.QUERY_FILTER))
			return true;
		List list = filterByRow.getFilterList(FilterByRow.ALL_ROW_FILTER);
		if (list == null)
			return false;
		for (int j = 0; j < list.size(); j++) {
			if (FilterUtil.isFilterNeedMultiPass((IFilterDefinition) (list.get(j))))
				return true;
		}
		return false;
	}

	/**
	 * @param filterByRow
	 * @param needMultiPass
	 * @return
	 */
	public static boolean hasMutipassFilters(List list) {
		for (int j = 0; j < list.size(); j++) {
			if (FilterUtil.isFilterNeedMultiPass((IFilterDefinition) (list.get(j))))
				return true;
		}
		return false;
	}
}
