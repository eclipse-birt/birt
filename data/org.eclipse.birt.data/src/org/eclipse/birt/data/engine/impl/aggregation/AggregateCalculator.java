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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.data.engine.cache.BasicCachedList;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExprEvaluateUtil;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Scriptable;

/**
 * Calculation engine for aggregate expressions. Does runtime calculation of
 * aggregate expressions that appear in a report query or subquery. Stores
 * values of aggregate expressions.
 */
class AggregateCalculator {

	/**
	 * Array to store all calculated aggregate values. aggrValue[i] is a list of
	 * values calculated for expression #i in the associated aggregate table. The
	 * aggrgate values are stored in each list as the cursor advances for the
	 * associated ODI result set.
	 */
	private List[] aggrValues;

	/**
	 * Array to store current argument values to all aggregates argrArgs[i] is the
	 * argument array to aggregate expression #i
	 */
	private Object[][] aggrArgs;

	/**
	 * The table contains all aggregate expression
	 */
	private List aggrExprInfoList;

	// The count of aggregate expression
	private int aggrCount;

	// The Odi result
	private IResultIterator odiResult;

	private AccumulatorManager[] accumulatorManagers;

	private Set invalidAggrSet;
	private Map invalidAggrMsg;

	private static Logger logger = Logger.getLogger(AggregateCalculator.class.getName());

	/**
	 * For the given odi resultset, calcaulate the value of aggregate from
	 * aggregateTable
	 *
	 * @param aggrTable
	 * @param odiResult
	 */
	AggregateCalculator(String tempDir, List aggrExprInfoList, IResultIterator odiResult) {
		Object[] params = { aggrExprInfoList, odiResult };
		logger.entering(AggregateCalculator.class.getName(), "AggregateCalculator", params);
		assert aggrExprInfoList != null;
		assert odiResult != null;

		this.aggrExprInfoList = aggrExprInfoList;
		this.odiResult = odiResult;

		aggrCount = aggrExprInfoList.size();

		if (aggrCount > 0) {
			aggrValues = new List[aggrCount];
			aggrArgs = new Object[aggrCount][];
			for (int i = 0; i < aggrCount; i++) {
				aggrValues[i] = new BasicCachedList(tempDir, DataEngineSession.getCurrentClassLoader());
				AggrExprInfo aggrInfo = getAggrInfo(i);

				// Initialize argument array for this aggregate expression
				aggrArgs[i] = new Object[aggrInfo.aggregation.getParameterDefn().length];
			}
			accumulatorManagers = new AccumulatorManager[aggrCount];
		}
		logger.exiting(AggregateCalculator.class.getName(), "AggregateCalculator");
	}

	/**
	 * Makes one pass over the odiResult and calculates values for all aggregate
	 * expressions. odiResult must be open, and cursor placed at first row. Upon
	 * return, odiResult is rewinded to first row. Before calling this method, a
	 * Javascript "row" object must be set up in the passed-in JS context and bound
	 * to the passed in odiResult.
	 */
	void calculate(Scriptable scope, ScriptContext cx) throws DataException {
		List validAggregations = new ArrayList();
		boolean[] populateAggrValue = new boolean[this.aggrCount];
		int count = 1;
		for (int i = 0; i < this.aggrCount; i++) {
			validAggregations.add(Integer.valueOf(i));
			if (this.getAggrInfo(i).aggregation.getNumberOfPasses() > 1) {
				populateAggrValue[i] = false;
			} else {
				populateAggrValue[i] = true;
			}
			accumulatorManagers[i] = new AccumulatorManager(this.getAggrInfo(i).aggregation);
		}

		while (validAggregations.size() > 0) {
			int[] validAggregationArray = new int[validAggregations.size()];
			for (int i = 0; i < validAggregations.size(); i++) {
				validAggregationArray[i] = ((Integer) validAggregations.get(i)).intValue();
			}
			assert odiResult.getCurrentResultIndex() == 0;
			if (odiResult.getCurrentResult() == null) {
				// Empty result set; nothing to do
				return;
			}

			pass(scope, cx, populateAggrValue, validAggregationArray);

			// Rewind to first row
			odiResult.first(0);

			count++;
			prepareNextIteration(validAggregations, populateAggrValue, count);
		}
	}

	/**
	 * Make a pass to all aggregations. Iterator over entire result set. At each
	 * row, call each aggregate aggregationtion.
	 *
	 * @param scope
	 * @param populateAggrValue
	 * @param validAggregationArray
	 * @throws DataException
	 */
	private void pass(Scriptable scope, ScriptContext cx, boolean[] populateAggrValue, int[] validAggregationArray)
			throws DataException {
		do {
			int startingGroupLevel = odiResult.getStartingGroupLevel();
			int endingGroupLevel = odiResult.getEndingGroupLevel();

			for (int i = 0; i < validAggregationArray.length; i++) {
				int index = validAggregationArray[i];
				if (invalidAggrSet != null && invalidAggrSet.contains(Integer.valueOf(index))) {
					addInvalidAggrMsg(index, endingGroupLevel);
					continue;
				}

				if (!onRow(index, startingGroupLevel, endingGroupLevel, scope, cx, populateAggrValue[index])) {
					addInvalidAggrMsg(index, endingGroupLevel);

					if (invalidAggrSet == null) {
						invalidAggrSet = new HashSet();
					}
					invalidAggrSet.add(Integer.valueOf(index));
				}
			}
		} while (odiResult.next());
	}

	/**
	 *
	 * @param index
	 * @param endingGroupLevel
	 */
	private void addInvalidAggrMsg(int index, int endingGroupLevel) {
		assert invalidAggrMsg != null;

		if (getAggrInfo(index).aggregation.getType() == IAggrFunction.RUNNING_AGGR
				|| endingGroupLevel <= getAggrInfo(index).groupLevel) {
			aggrValues[index].add(invalidAggrMsg.get(Integer.valueOf(index)));
		}
	}

	/**
	 * Calculate the value by row
	 *
	 * @param aggrIndex
	 * @param startingGroupLevel
	 * @param endingGroupLevel
	 * @param context
	 * @param scope
	 * @throws DataException
	 */
	private boolean onRow(int aggrIndex, int startingGroupLevel, int endingGroupLevel, Scriptable scope,
			ScriptContext cx, boolean populateValue) throws DataException {
		AggrExprInfo aggrInfo = getAggrInfo(aggrIndex);
		Accumulator acc = null;

		boolean newGroup = false;
		if (startingGroupLevel <= aggrInfo.groupLevel) {
			// A new group starts for this aggregate; call start() on
			// accumulator
			newGroup = true;
			acc = accumulatorManagers[aggrIndex].next();
			acc.start();
		} else {
			acc = accumulatorManagers[aggrIndex].getCurrentAccumulator();
		}

		// Apply filtering on row
		boolean accepted = true;
		if (aggrInfo.filter != null) {
			try {
				Object filterResult = ExprEvaluateUtil.evaluateCompiledExpression(aggrInfo.filter, odiResult, scope,
						cx);
				if (filterResult == null) {
					accepted = true;
				} else {
					accepted = DataTypeUtil.toBoolean(filterResult).booleanValue();
				}
			} catch (BirtException e) {
				if (invalidAggrMsg == null) {
					invalidAggrMsg = new HashMap();
				}
				invalidAggrMsg.put(Integer.valueOf(aggrIndex), e);

				return false;
			}
		}

		if (aggrInfo.calculateLevel > 0) {
			if (startingGroupLevel > aggrInfo.calculateLevel) {
				accepted = false;
			}
		}

		if (accepted) {
			// Calculate arguments to the aggregate aggregationtion
			IParameterDefn[] argDefs = aggrInfo.aggregation.getParameterDefn();
			assert argDefs.length == aggrArgs[aggrIndex].length;
			try {
				calculateArguments(aggrIndex, scope, cx, aggrInfo, newGroup, argDefs);

				acc.onRow(aggrArgs[aggrIndex]);
			} catch (DataException e) {
				if (invalidAggrMsg == null) {
					invalidAggrMsg = new HashMap();
				}
				invalidAggrMsg.put(Integer.valueOf(aggrIndex), e);

				return false;
			}
		}

		// If this is a running aggregate, get value for current row
		boolean isRunning = (aggrInfo.aggregation.getType() == IAggrFunction.RUNNING_AGGR);

		if (isRunning && populateValue) {
			Object value = acc.getValue();
			aggrValues[aggrIndex].add(value);
//			assert aggrValues[aggrIndex].size( ) == odiResult.getCurrentResultIndex( ) + 1;
		}

		if (endingGroupLevel <= aggrInfo.groupLevel) {
			// Current group ends for this aggregate; call finish() on
			// accumulator
			acc.finish();

			// For non-running aggregates, this is the time to call getValue
			if ((!isRunning) && populateValue) {
				Object value = acc.getValue();
				aggrValues[aggrIndex].add(value);
//				assert aggrInfo.groupLevel == 0
//						? ( aggrValues[aggrIndex].size( ) == 1 )
//						: ( aggrValues[aggrIndex].size( ) == odiResult.getCurrentGroupIndex( aggrInfo.groupLevel ) + 1 );
			}
		}
		return true;
	}

	private void calculateArguments(int aggrIndex, Scriptable scope, ScriptContext cx, AggrExprInfo aggrInfo,
			boolean newGroup, IParameterDefn[] argDefs) throws DataException {
		if (aggrInfo.args == null || aggrInfo.args.length == 0) {
			aggrArgs[aggrIndex] = null;
		} else {
			for (int i = 0; i < argDefs.length; i++) {
				// Note that static arguments only need to be calculated
				// once at the start of the iteration
				if (!argDefs[i].isOptional() || newGroup) {
					aggrArgs[aggrIndex][i] = ExprEvaluateUtil.evaluateCompiledExpression(aggrInfo.args[i], odiResult,
							scope, cx);
				}
			}
		}
	}

	/**
	 * Prepare next run of aggregation pass.
	 *
	 * @param validAggregations
	 * @param populateAggrValue
	 * @param count
	 */
	private void prepareNextIteration(List validAggregations, boolean[] populateAggrValue, int count) {
		validAggregations.clear();
		for (int i = 0; i < this.aggrCount; i++) {
			this.accumulatorManagers[i].restart();
			IAggrFunction temp = this.getAggrInfo(i).aggregation;
			populateAggrValue[i] = false;
			int passesNumber = temp.getNumberOfPasses();
			if (count <= passesNumber) {
				validAggregations.add(Integer.valueOf(i));
				if (count == passesNumber) {
					populateAggrValue[i] = true;
				}
			}
		}
	}

	/**
	 * Gets information about one aggregate expression in the table, given its index
	 */
	private AggrExprInfo getAggrInfo(int i) {
		return (AggrExprInfo) aggrExprInfoList.get(i);
	}

	/**
	 * Returns a scriptable object that implements the JS "_aggr_value" internal
	 * object
	 */
	Scriptable getJSAggrValueObject() {
		return new JSAggrValueObject(this.aggrExprInfoList, this.odiResult, this.aggrValues);
	}

	/**
	 * A helper class that is used to manage the Accumulators of aggregations.
	 *
	 */
	private static class AccumulatorManager {
		//
		private IAggrFunction aggregation;
		private int cursor;
		private List cachedAcc;
		private Accumulator accumulator;

		/**
		 * Constructor.
		 *
		 * @param aggregation
		 */
		AccumulatorManager(IAggrFunction aggregation) {
			this.aggregation = aggregation;
			this.cursor = -1;

			int passNum = aggregation.getNumberOfPasses();
			if (passNum < 2) {
				this.accumulator = aggregation.newAccumulator();
			} else {
				this.cachedAcc = new ArrayList();
			}
		}

		/**
		 * Get the current accumulator.
		 *
		 * @return
		 */
		Accumulator getCurrentAccumulator() {
			if (this.accumulator != null) {
				return this.accumulator;
			}
			if (cachedAcc.size() == 0) {
				cachedAcc.add(aggregation.newAccumulator());
			}
			return (Accumulator) cachedAcc.get(cursor);
		}

		/**
		 * Get the next accumulator. If there is no next accumulator, populate one.
		 *
		 * @return
		 */
		Accumulator next() {
			if (this.accumulator != null) {
				return this.accumulator;
			}
			cursor++;
			if (cachedAcc.size() > cursor) {
				return (Accumulator) cachedAcc.get(cursor);
			} else {
				cachedAcc.add(aggregation.newAccumulator());
				return (Accumulator) cachedAcc.get(cursor);
			}
		}

		/**
		 * Reset the cursor to unstart state ( = -1)
		 *
		 */
		void restart() {
			this.cursor = -1;
		}
	}
}
