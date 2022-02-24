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

import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.expression.AggregateExpression;
import org.eclipse.birt.data.engine.expression.AggregationConstantsUtil;
import org.eclipse.birt.data.engine.expression.BytecodeExpression;
import org.eclipse.birt.data.engine.expression.CompiledExpression;
import org.eclipse.birt.data.engine.expression.ConstantExpression;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IQuery.GroupSpec;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

/**
 * Aggregation registry implemenation
 */
final class AggrRegistry implements AggregateRegistry {
	private int groupLevel; // current group level
	private boolean isDetailedRow;
	private int calculationLevel;
	private ScriptContext cx;

	private BaseQuery baseQuery;

	private List groupDefns;
	private int groupCount;
	private Scriptable scope;

	private int runStates;

	private List aggrExprInfoList;

	private static final String TOTAL_COUNT_FUNC = "COUNT";//$NON-NLS-1$
	private static final String TOTAL_RUNNINGCOUNT_FUNC = "RUNNINGCOUNT";//$NON-NLS-1$
	private static int PREPARED_QUERY = 1;
	private static int BASE_QUERY = 2;
	private static Logger logger = Logger.getLogger(AggrRegistry.class.getName());

	/**
	 * @param groupLevel
	 * @param isDetailedRow
	 * @param cx
	 * @throws DataException
	 */
	AggrRegistry(int groupLevel, int calculationLevel, boolean isDetailedRow, ScriptContext cx) throws DataException {
		Object[] params = { Integer.valueOf(groupLevel), Integer.valueOf(calculationLevel),
				Boolean.valueOf(isDetailedRow), cx };
		logger.entering(AggrRegistry.class.getName(), "AggrRegistry", params);
		this.groupLevel = groupLevel;
		this.isDetailedRow = isDetailedRow;
		this.calculationLevel = calculationLevel;
		this.cx = cx;
		if (this.calculationLevel < this.groupLevel && this.calculationLevel > 0)
			throw new DataException(ResourceConstants.INVALID_TOTAL_EXPRESSION);
		logger.exiting(AggrRegistry.class.getName(), "AggrRegistry");
	}

	/**
	 * @param groupDefns
	 * @param scope
	 * @param baseQuery
	 * @param aggrExprInfoList
	 */
	void prepare(List groupDefns, Scriptable scope, BaseQuery baseQuery, List aggrExprInfoList) {
		this.groupDefns = groupDefns;
		this.scope = scope;
		this.baseQuery = baseQuery;
		this.aggrExprInfoList = aggrExprInfoList;

		if (baseQuery == null)
			this.runStates = PREPARED_QUERY;
		else
			this.runStates = BASE_QUERY;

		if (groupDefns != null)
			this.groupCount = groupDefns.size();
	}

	/**
	 * Register the aggregate expression into aggregate table, get the only
	 * aggregate id.
	 */
	public int register(AggregateExpression expr) throws DataException {
		return registerExpression(expr, groupLevel, calculationLevel, isDetailedRow, cx);
	}

	/**
	 * Registers one aggregate expression. Returns an ID for the registered
	 * aggregate. If an equivalent aggregate expression had been previously
	 * registered, the ID of the existing expression is returned.
	 * 
	 * @param expr       aggregate expression
	 * @param groupLevel
	 * @param cx
	 * @return
	 * @throws DataException
	 */
	private int registerExpression(AggregateExpression expr, int groupLevel, int calculationLevel,
			boolean isDetailedRow, ScriptContext cx) throws DataException {
		AggrExprInfo info = newAggrExprInfo(expr, groupLevel, calculationLevel, isDetailedRow, cx);

		// See if an existing aggregate expression is equivalent to this one
		int id;
		for (id = 0; id < aggrExprInfoList.size(); id++) {
			if (info.equals(aggrExprInfoList.get(id)))
				break;
		}

		if (id == aggrExprInfoList.size()) {
			aggrExprInfoList.add(info);
		}

		expr.setRegId(id);

		return id;
	}

	/**
	 * Creates a AggrExprInfo structure from the compiler's AggregateExpression
	 * output class
	 * 
	 * @param expr
	 * @param currentGroupLevel
	 * @param cx
	 * @return
	 * @throws DataException
	 */
	private AggrExprInfo newAggrExprInfo(AggregateExpression expr, int currentGroupLevel, int calculationLevel,
			boolean isDetailedRow, ScriptContext cx) throws DataException {
		AggrExprInfo aggr = new AggrExprInfo();
		assert expr != null;
		assert currentGroupLevel >= 0;

		aggr.aggregation = expr.getAggregation();
		aggr.calculateLevel = calculationLevel;
		List exprArgs = expr.getArguments();

		boolean isTotalCountOrRunningCount = isTotalCountOrRunningCount(aggr);
		// Find out how many fixed arguments this aggregate function takes
		// Optional filter and group arguments follow the fixed arguments
		int nFixedArgs = aggr.aggregation.getParameterDefn().length;

		// Verify that the expression has the right # of arguments
		int nArgs = exprArgs.size();
		if (!isValidArgumentNum(aggr, nFixedArgs, nArgs, isTotalCountOrRunningCount)) {
			DataException e = new DataException(ResourceConstants.INVALID_AGGR_PARAMETER, aggr.aggregation.getName());
			throw e;
		}

		// Determine grouping level for this aggregate
		// Look at the group level argument. If it is not present, or is null, the group
		// level
		// is the same group in which the aggregate expression is defined.
		aggr.groupLevel = currentGroupLevel;
		if (containsGroupLevel(aggr, nFixedArgs, nArgs, isTotalCountOrRunningCount)) {
			CompiledExpression groupExpr = (CompiledExpression) exprArgs.get(nArgs - 1);
			if (!(groupExpr instanceof ConstantExpression))

			{
				DataException e = new DataException(ResourceConstants.INVALID_AGGR_GROUP_EXPRESSION,
						aggr.aggregation.getName());
				throw e;
			}

			// Note that we use the data engine's shared scope for this
			// evaluation. Group expression
			// is not expected to depend on any query execution. In fact it
			// should just be a constant
			// expression most of the case
			Object groupLevelObj = groupExpr.evaluate(cx,
					runStates == BASE_QUERY ? Context.getCurrentContext().initStandardObjects() : scope);
			if (groupLevelObj == null) {
				// null argument; use default level
			} else if (groupLevelObj instanceof String) {
				int innerMostGroup = 0;
				if (runStates == PREPARED_QUERY) {
					innerMostGroup = groupCount;
				} else {
					innerMostGroup = baseQuery.getGrouping() != null ? baseQuery.getGrouping().length : 0;
				}
				int groupLevel = AggregationConstantsUtil.getGroupLevel((String) groupLevelObj, currentGroupLevel,
						innerMostGroup, isDetailedRow);
				// When the groupLevelObj can be recognized, it will return a non-negative
				// value.Else return -1.
				if (groupLevel != -1) {
					aggr.groupLevel = groupLevel;
				} else {
					aggr.groupLevel = (runStates == BASE_QUERY) ? getGroupIndex(groupLevelObj.toString())
							: getGroupIndexFromPreparedQuery((String) groupLevelObj);
				}
			} else if (groupLevelObj instanceof Number) {
				int offset = ((Number) groupLevelObj).intValue();
				if (offset < 0)
					aggr.groupLevel = currentGroupLevel + offset;
				else
					aggr.groupLevel = offset;
			}

			if (aggr.groupLevel < 0 || aggr.groupLevel > (runStates == BASE_QUERY
					? (baseQuery.getGrouping() == null ? 0 : baseQuery.getGrouping().length)
					: groupCount)) {
				DataException e = new DataException(ResourceConstants.INVALID_GROUP_LEVEL, aggr.aggregation.getName());
				throw e;
			}
		}

		// Extract filter parameter
		if (containsFilter(aggr, nFixedArgs, nArgs, isTotalCountOrRunningCount)) {
			if (isTotalCountOrRunningCount) {
				aggr.filter = (CompiledExpression) exprArgs.get(nFixedArgs - getOptionalArgNum(aggr, nFixedArgs));
			} else {
				aggr.filter = (CompiledExpression) exprArgs.get(nFixedArgs);
			}
			// If filter expression is a constant "null", ignore it
			if (aggr.filter instanceof ConstantExpression && ((ConstantExpression) aggr.filter).getValue() == null) {
				aggr.filter = null;
			} else if (aggr.filter instanceof BytecodeExpression) {
				boolean isValid = false;
				int groupLevel = ((BytecodeExpression) aggr.filter).getGroupLevel();
				if (aggr.calculateLevel == -1) {
					if (groupLevel == 0 || groupLevel == -1)
						isValid = true;
				} else if (groupLevel == -1) {
					if (aggr.calculateLevel == 0)
						isValid = true;
				} else {

					if (aggr.calculateLevel == groupLevel)
						isValid = true;
				}
				if (!isValid)
					throw new DataException(ResourceConstants.INVALID_TOTAL_EXPRESSION);
			}

		}

		if ((nFixedArgs > 0 && !aggr.aggregation.getParameterDefn()[0].isOptional()) || (nArgs == nFixedArgs + 2)) {
			aggr.args = new CompiledExpression[nFixedArgs];
			exprArgs.subList(0, nFixedArgs).toArray(aggr.args);
		} else {
			aggr.args = new CompiledExpression[0];
		}

		return aggr;
	}

	/**
	 * To see whether the function is Total.COUNT or Total.RUNNINGCOUNT
	 * 
	 * @param aggr
	 * @return
	 */
	private boolean isTotalCountOrRunningCount(AggrExprInfo aggr) {
		return TOTAL_COUNT_FUNC.equalsIgnoreCase(aggr.aggregation.getName())
				|| TOTAL_RUNNINGCOUNT_FUNC.equalsIgnoreCase(aggr.aggregation.getName());
	}

	/**
	 * Get the optional arguments' number
	 * 
	 * @param aggr
	 * @param nFixedArgs
	 * @return
	 */
	private int getOptionalArgNum(AggrExprInfo aggr, int nFixedArgs) {
		int optionalArgNum = 0;
		for (int i = 0; i < nFixedArgs; i++) {
			if (aggr.aggregation.getParameterDefn()[i].isOptional()) {
				optionalArgNum++;
			}
		}
		return optionalArgNum;
	}

	/**
	 * Check whether the number of the aggregation expression arguments is valid.
	 * 
	 * @param aggr
	 * @return
	 */
	private boolean isValidArgumentNum(AggrExprInfo aggr, int nFixedArgs, int nArgs,
			boolean isTotalCountOrRunningCount) {
		if (isTotalCountOrRunningCount) {
			return nArgs <= 2;
		}
		return nArgs >= nFixedArgs && (nArgs <= (nFixedArgs + 2));
	}

	/**
	 * Check whether the number of the aggregation expression arguments is valid.
	 * 
	 * @param aggr
	 * @param nFixedArgs
	 * @param nArgs
	 * @return
	 */
	private boolean containsFilter(AggrExprInfo aggr, int nFixedArgs, int nArgs, boolean isTotalCountOrRunningCount) {
		if (isTotalCountOrRunningCount) {
			return nArgs > 0;
		}
		return nArgs > nFixedArgs;
	}

	/**
	 * Check whether the input expression contains group level
	 * 
	 * @param aggr
	 * @param nFixedArgs
	 * @param nArgs
	 * @return
	 */
	private boolean containsGroupLevel(AggrExprInfo aggr, int nFixedArgs, int nArgs,
			boolean isTotalCountOrRunningCount) {
		if (isTotalCountOrRunningCount) {
			return nArgs == 2;
		}
		return nArgs == (nFixedArgs + 2);
	}

	/**
	 * 
	 * Finds a group given a text identifier of a group. Returns index of group
	 * found (1 = outermost group, 2 = second level group etc.). The text identifier
	 * can be the group name, the group key column name, or the group key expression
	 * text. Returns -1 if no matching group is found
	 * 
	 * @param groupText
	 * @return
	 */
	private int getGroupIndexFromPreparedQuery(String groupText) {
		assert groupText != null;
		assert groupDefns != null;

		for (int i = 0; i < groupDefns.size(); i++) {
			IGroupDefinition group = (IGroupDefinition) groupDefns.get(i);
			if (groupText.equals(group.getName()) || groupText.equals(group.getKeyColumn())
					|| groupText.equals(group.getKeyExpression())) {
				return i + 1; // Note that group index is 1-based
			}
		}
		return -1;
	}

	/**
	 * Return the index of group according to the given group text.
	 * 
	 * @param groupText
	 * @return The index of group
	 */
	private int getGroupIndex(String groupText) {
		assert groupText != null;

		GroupSpec[] groups = baseQuery.getGrouping();
		for (int i = 0; i < groups.length; i++) {
			GroupSpec group = groups[i];
			if (groupText.equals(group.getName()) || groupText.equals(group.getKeyColumn())) {
				return i + 1; // Note that group index is 1-based
			}
		}
		return -1;
	}

}
