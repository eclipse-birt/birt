
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
package org.eclipse.birt.data.engine.executor.aggregation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.data.engine.cache.BasicCachedList;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.expression.ExprEvaluateUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.odi.IAggrDefnManager;
import org.eclipse.birt.data.engine.odi.IAggrInfo;
import org.eclipse.birt.data.engine.odi.IAggrValueHolder;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * 
 */

public class AggregationHelper implements IAggrValueHolder {
	private IAggrDefnManager manager;

	private ResultSetPopulator populator;

	/**
	 * Array to store all calculated aggregate values. aggrValue[i] is a list of
	 * values calculated for expression #i in the associated aggregate table. The
	 * aggrgate values are stored in each list as the cursor advances for the
	 * associated ODI result set.
	 */
	private List[] currentRoundAggrValue;

	/**
	 * Array to store current argument values to all aggregates argrArgs[i] is the
	 * argument array to aggregate expression #i
	 */
	private Object[][] aggrArgs;

	// The count of aggregate expression
	private int currentAggrCount;

	private AccumulatorManager[] accumulatorManagers;

	private Set invalidAggrSet;
	private Map invalidAggrMsg;
	private Set<String> aggrNames;

	/**
	 * For the given odi resultset, calcaulate the value of aggregate from
	 * aggregateTable
	 * 
	 * @param aggrTable
	 * @param odiResult
	 * @throws DataException
	 */
	public AggregationHelper(IAggrDefnManager manager, ResultSetPopulator populator) throws DataException {
		this.populator = populator;
		this.manager = manager;
		this.currentRoundAggrValue = new List[0];
		this.populateAggregations(populator.getSession().getTempDir());
	}

	private void populateAggregations(String tempDir) throws DataException {
		this.aggrNames = new HashSet<String>();
		this.currentAggrCount = manager.getAggrCount();
		if (currentAggrCount > 0) {
			currentRoundAggrValue = new List[currentAggrCount];
			aggrArgs = new Object[currentAggrCount][];
			for (int i = 0; i < this.currentAggrCount; i++) {
				currentRoundAggrValue[i] = new BasicCachedList(tempDir, DataEngineSession.getCurrentClassLoader());
				IAggrInfo aggrInfo = this.manager.getAggrDefn(i);

				// Initialize argument array for this aggregate expression
				aggrArgs[i] = new Object[aggrInfo.getAggregation().getParameterDefn().length];
				this.aggrNames.add(this.manager.getAggrDefn(i).getName());
			}
			accumulatorManagers = new AccumulatorManager[currentAggrCount];

		}
		this.calculate();
	}

	/**
	 * Makes one pass over the odiResult and calculates values for all aggregate
	 * expressions. odiResult must be open, and cursor placed at first row. Upon
	 * return, odiResult is rewinded to first row. Before calling this method, a
	 * Javascript "row" object must be set up in the passed-in JS context and bound
	 * to the passed in odiResult.
	 */
	private void calculate() throws DataException {
		List validAggregations = new ArrayList();
		boolean[] populateAggrValue = new boolean[this.currentAggrCount];
		int count = 1;
		for (int i = 0; i < this.currentAggrCount; i++) {
			validAggregations.add(Integer.valueOf(i));
			if (this.getAggrInfo(i).getAggregation().getNumberOfPasses() > 1)
				populateAggrValue[i] = false;
			else
				populateAggrValue[i] = true;
			accumulatorManagers[i] = new AccumulatorManager(this.getAggrInfo(i).getAggregation());
		}

		while (validAggregations.size() > 0) {
			int[] validAggregationArray = new int[validAggregations.size()];
			for (int i = 0; i < validAggregations.size(); i++) {
				validAggregationArray[i] = ((Integer) validAggregations.get(i)).intValue();
			}
			assert (this.getCurrentResult() == null || this.getCurrentResultIndex() == 0);
			if (this.getCurrentResult() == null) {
				for (int i = 0; i < accumulatorManagers.length; i++) {
					Accumulator a = accumulatorManagers[i].getCurrentAccumulator();
					a.start();
					a.finish();
					currentRoundAggrValue[i].add(a.getValue());
				}
				// Empty result set; nothing to do
				return;
			}

			pass(populateAggrValue, validAggregationArray);

			// Rewind to first row
			this.first(0);

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
	private void pass(boolean[] populateAggrValue, int[] validAggregationArray) throws DataException {
		do {
			int startingGroupLevel = this.getStartingGroupLevel();
			int endingGroupLevel = this.getEndingGroupLevel();

			for (int i = 0; i < validAggregationArray.length; i++) {
				int index = validAggregationArray[i];
				if (invalidAggrSet != null && invalidAggrSet.contains(Integer.valueOf(index))) {
					addInvalidAggrMsg(index, endingGroupLevel);
					continue;
				}

				if (onRow(index, startingGroupLevel, endingGroupLevel, populateAggrValue[index]) == false) {
					addInvalidAggrMsg(index, endingGroupLevel);

					if (invalidAggrSet == null)
						invalidAggrSet = new HashSet();
					invalidAggrSet.add(Integer.valueOf(index));
				}
			}
		} while (this.populator.getResultIterator().next());
	}

	/**
	 * 
	 * @param index
	 * @param endingGroupLevel
	 * @throws DataException
	 */
	private void addInvalidAggrMsg(int index, int endingGroupLevel) throws DataException {
		assert invalidAggrMsg != null;

		if (getAggrInfo(index).getAggregation().getType() == IAggrFunction.RUNNING_AGGR
				|| endingGroupLevel <= getAggrInfo(index).getGroupLevel() || getAggrInfo(index).getGroupLevel() == 0)
			currentRoundAggrValue[index].add(invalidAggrMsg.get(Integer.valueOf(index)));
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
	private boolean onRow(int aggrIndex, int startingGroupLevel, int endingGroupLevel, boolean populateValue)
			throws DataException {
		IAggrInfo aggrInfo = getAggrInfo(aggrIndex);
		Accumulator acc = null;
		boolean newGroup = false;
		IParameterDefn[] argDefs = aggrInfo.getAggregation().getParameterDefn();
		if (startingGroupLevel <= aggrInfo.getGroupLevel()) {
			// A new group starts for this aggregate; call start() on
			// accumulator
			acc = accumulatorManagers[aggrIndex].next();
			acc.start();
			newGroup = true;
			/*
			 * for ( int i = 0; i < aggrInfo.getArgument( ).length; i++ ) { // Note that
			 * static arguments only need to be calculated // once at // the // start of the
			 * iteration if ( argDefs.length <= i ) { throw new
			 * DataException("InvalidAggregtionExpression"); } if ( !argDefs[i] ) {
			 * IBaseExpression argExpr = aggrInfo.getArgument( )[i]; try {
			 * aggrArgs[aggrIndex][i] = ExprEvaluateUtil.evaluateValue( argExpr,
			 * this.populator.getCache( ).getCurrentIndex( ), this.populator.getCache(
			 * ).getCurrentResult( ), this.populator.getQuery( ).getExprProcessor(
			 * ).getScope( ) ); } catch ( BirtException e ) { throw DataException.wrap( e );
			 * } } }
			 */
		} else {
			acc = accumulatorManagers[aggrIndex].getCurrentAccumulator();
		}

		// Apply filtering on row
		boolean accepted = true;
		if (aggrInfo.getFilter() != null) {
			try {
				Object filterResult = ExprEvaluateUtil.evaluateValue(aggrInfo.getFilter(),
						this.populator.getCache().getCurrentIndex(), this.populator.getCache().getCurrentResult(),
						this.populator.getQuery().getExprProcessor().getScope(),
						this.populator.getSession().getEngineContext().getScriptContext());
				if (filterResult == null)
					accepted = true;
				else

					accepted = DataTypeUtil.toBoolean(filterResult).booleanValue();
			} catch (BirtException e) {
				if (invalidAggrMsg == null)
					invalidAggrMsg = new HashMap();
				invalidAggrMsg.put(Integer.valueOf(aggrIndex), e);

				return false;
			}
		}

		if (aggrInfo.getCalcualteLevel() > 0) {
			if (startingGroupLevel > aggrInfo.getCalcualteLevel())
				accepted = false;
		}

		if (accepted) {
			// Calculate arguments to the aggregate aggregationtion

			final IBaseExpression[] arguments = aggrInfo.getArgument();
			if (!isFunctionCount(aggrInfo) && arguments == null) {
				DataException e = new DataException(ResourceConstants.INVALID_AGGR_PARAMETER, aggrInfo.getName());
				wrapException(aggrIndex, e);
				return false;
			}

			try {
				int optionalAgrsNum = 0;
				for (int i = 0; i < argDefs.length; i++) {
					if (argDefs[i].isOptional()) {
						optionalAgrsNum++;
					}
					if (aggrInfo.getArgument() == null || i >= arguments.length + optionalAgrsNum) {
						throw new DataException(ResourceConstants.AGGREGATION_ARGUMENT_ERROR,
								new Object[] { argDefs[i].getName(), aggrInfo.getName() });
					}
					if (isEmptyAggrArgument(aggrInfo)) {
						aggrArgs[aggrIndex] = null;
					} else {
						evaluateArgsValue(aggrIndex, aggrInfo, i, argDefs[i]);
					}
				}

				if (aggrInfo.getArgument() == null
						|| !isValidArgumentNumber(aggrInfo.getArgument().length, argDefs.length, optionalAgrsNum)) {
					DataException e = new DataException(ResourceConstants.INVALID_AGGR_PARAMETER, aggrInfo.getName());
					wrapException(aggrIndex, e);
					return false;
				}
				acc.onRow(aggrArgs[aggrIndex]);
				newGroup = false;
			} catch (DataException e) {
				wrapException(aggrIndex, e);
				return false;
			}
		}

		// If this is a running aggregate, get value for current row
		boolean isRunning = (aggrInfo.getAggregation().getType() == IAggrFunction.RUNNING_AGGR);

		if (isRunning && populateValue) {
			Object value = acc.getValue();
			currentRoundAggrValue[aggrIndex].add(value);
//			assert aggrValues[aggrIndex].size( ) == odiResult.getCurrentResultIndex( ) + 1;
		}

		if (endingGroupLevel <= aggrInfo.getGroupLevel()) {
			// Current group ends for this aggregate; call finish() on
			// accumulator
			acc.finish();

			// For non-running aggregates, this is the time to call getValue
			if ((!isRunning) && populateValue) {
				Object value = acc.getValue();
				currentRoundAggrValue[aggrIndex].add(value);
//				assert aggrInfo.groupLevel == 0
//						? ( aggrValues[aggrIndex].size( ) == 1 )
//						: ( aggrValues[aggrIndex].size( ) == odiResult.getCurrentGroupIndex( aggrInfo.groupLevel ) + 1 );
			}
		}
		return true;
	}

	/**
	 * Checks whether the arguments number is valid
	 * 
	 * @param aggrArgNumb
	 * @param argDefsLength
	 * @param optionalNum
	 * @return
	 */
	private boolean isValidArgumentNumber(int aggrArgNumb, int argDefsLength, int optionalNum) {
		return (aggrArgNumb >= argDefsLength - optionalNum) && (aggrArgNumb <= argDefsLength);
	}

	/**
	 * Check whether the input aggregation script expression is empty
	 * 
	 * @param aggrInfo
	 * @return
	 */
	private boolean isEmptyAggrArgument(IAggrInfo aggrInfo) {
		return aggrInfo.getArgument().length == 0 || aggrInfo.getArgument()[0] == null
				|| ((IScriptExpression) aggrInfo.getArgument()[0]).getText() == null
				|| ((IScriptExpression) aggrInfo.getArgument()[0]).getText().trim().length() == 0;
	}

	/**
	 * Check whether the number of the aggregation arguments is valid
	 * 
	 * @param aggrInfo
	 * @param argDefs
	 * @return
	 */
	private boolean isInvalidArgumentNum(IAggrInfo aggrInfo, IParameterDefn[] argDefs) {
		if (aggrInfo.getArgument() == null) {

		}
		// if input argument is null or the
		return aggrInfo.getArgument() == null || ((aggrInfo.getArgument().length != argDefs.length)
				&& !((aggrInfo.getArgument().length == (argDefs.length - 1)) && argDefs[0].isOptional()));
	}

	/**
	 * Get the evaluated result by the ScriptExpression
	 * 
	 * @param aggrIndex
	 * @param aggrInfo
	 * @param i
	 * @throws DataException
	 */
	private void evaluateArgsValue(int aggrIndex, IAggrInfo aggrInfo, int i, IParameterDefn paramDefn)
			throws DataException {
		if (i >= aggrInfo.getArgument().length) {
			return;
		}
		IBaseExpression argExpr = aggrInfo.getArgument()[i];
		if (!paramDefn.isOptional()) {
			if (!isFunctionCount(aggrInfo) && isEmptyScriptExpression(argExpr)) {
				throw new DataException(ResourceConstants.AGGREGATION_ARGUMENT_CANNOT_BE_BLANK,
						new Object[] { paramDefn.getName(), aggrInfo.getName() });
			}
		} else if (argExpr == null || ((IScriptExpression) argExpr).getText() == null
				|| ((IScriptExpression) argExpr).getText().trim().length() == 0) {
			aggrArgs[aggrIndex][i] = null;
			return;
		}
		try {
			aggrArgs[aggrIndex][i] = ExprEvaluateUtil.evaluateValue(argExpr,
					this.populator.getCache().getCurrentIndex(), this.populator.getCache().getCurrentResult(),
					this.populator.getQuery().getExprProcessor().getScope(),
					this.populator.getSession().getEngineContext().getScriptContext());
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}

	/**
	 * @param aggrIndex
	 * @param e
	 */
	private void wrapException(int aggrIndex, DataException e) {
		if (invalidAggrMsg == null)
			invalidAggrMsg = new HashMap();
		invalidAggrMsg.put(Integer.valueOf(aggrIndex), e);
	}

	/**
	 * Checks whether the ScriptExpression has empty expression text
	 * 
	 * @param argExpr
	 * @return
	 */
	private boolean isEmptyScriptExpression(IBaseExpression argExpr) {
		IScriptExpression expr = (IScriptExpression) argExpr;
		return expr == null || expr.getText() == null || expr.getText().trim().length() == 0;
	}

	/**
	 * @param aggrInfo
	 * @return
	 */
	private boolean isFunctionCount(IAggrInfo aggrInfo) {
		return aggrInfo.getAggregation().getParameterDefn().length == 0;
	}

	/**
	 * Prepare next run of aggregation pass.
	 * 
	 * @param validAggregations
	 * @param populateAggrValue
	 * @param count
	 * @throws DataException
	 */
	private void prepareNextIteration(List validAggregations, boolean[] populateAggrValue, int count)
			throws DataException {
		validAggregations.clear();
		for (int i = 0; i < this.currentAggrCount; i++) {
			this.accumulatorManagers[i].restart();
			IAggrFunction aggrFunc = this.getAggrInfo(i).getAggregation();
			populateAggrValue[i] = false;
			int passesNumber = aggrFunc.getNumberOfPasses();
			if (count <= passesNumber) {
				validAggregations.add(Integer.valueOf(i));
				if (count == passesNumber) {
					populateAggrValue[i] = true;
				}
			}
		}
	}

	private IAggrInfo getAggrInfo(int i) throws DataException {
		return this.manager.getAggrDefn(i);
	}

	private int getStartingGroupLevel() throws DataException {
		return this.populator.getResultIterator().getStartingGroupLevel();
		/*
		 * return this.populator.getGroupProcessorManager( ) .getGroupCalculationUtil( )
		 * .getGroupInformationUtil( ) .getStartingGroupLevel( );
		 */
	}

	private int getEndingGroupLevel() throws DataException {
		return this.populator.getResultIterator().getEndingGroupLevel();
		/*
		 * return this.populator.getGroupProcessorManager( ) .getGroupCalculationUtil( )
		 * .getGroupInformationUtil( ) .getEndingGroupLevel( );
		 */}

	private int getCurrentGroupIndex(int groupLevel) throws DataException {
		return this.populator.getResultIterator().getCurrentGroupIndex(groupLevel);
		/*
		 * return this.populator.getGroupProcessorManager( ) .getGroupCalculationUtil( )
		 * .getGroupInformationUtil( ) .getCurrentGroupIndex( groupLevel );
		 */
	}

	private int getCurrentResultIndex() throws DataException {
		return this.populator.getResultIterator().getCurrentResultIndex();
	}

	private IResultObject getCurrentResult() throws DataException {
		return this.populator.getResultIterator().getCurrentResult();
		// return this.populator.getCache( ).getCurrentResult( );
	}

	private void first(int groupLevel) throws DataException {
		this.populator.getResultIterator().first(groupLevel);
	}

	/**
	 * Get the aggregate value
	 * 
	 * @param aggrIndex
	 * @return
	 * @throws DataException
	 */
	public Object getAggrValue(String name) throws DataException {
		IAggrInfo aggrInfo = this.manager.getAggrDefn(name);

		if (this.populator.getCache().getCount() == 0) {
			return aggrInfo.getAggregation().getDefaultValue();
		}

		try {
			int groupIndex;

			if (aggrInfo.getAggregation().getType() == IAggrFunction.SUMMARY_AGGR) {
				// Aggregate on the whole list: there is only one group
				if (aggrInfo.getGroupLevel() == 0)
					groupIndex = 0;
				else
					groupIndex = this.getCurrentGroupIndex(aggrInfo.getGroupLevel());
			} else {
				groupIndex = this.getCurrentResultIndex();
			}

			return this.currentRoundAggrValue[this.manager.getAggrDefnIndex(name)].get(groupIndex);

		} catch (DataException e) {
			throw e;
		}
	}

	public List getAggrValues(String name) throws DataException {
		return this.currentRoundAggrValue[this.manager.getAggrDefnIndex(name)];
	}

	public boolean hasAggr(String name) throws DataException {
		return this.manager.getAggrDefnIndex(name) != -1;
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
			if (passNum < 2)
				this.accumulator = aggregation.newAccumulator();
			else
				this.cachedAcc = new ArrayList();
		}

		/**
		 * Get the current accumulator.
		 * 
		 * @return
		 */
		Accumulator getCurrentAccumulator() {
			if (this.accumulator != null)
				return this.accumulator;
			if (cachedAcc.size() == 0) {
				cachedAcc.add(aggregation.newAccumulator());
				cursor++;
			}

			return (Accumulator) cachedAcc.get(cursor);
		}

		/**
		 * Get the next accumulator. If there is no next accumulator, populate one.
		 * 
		 * @return
		 */
		Accumulator next() {
			if (this.accumulator != null)
				return this.accumulator;
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

	public Set<String> getAggrNames() throws DataException {
		return this.aggrNames;
	}

	public IAggrInfo getAggrInfo(String aggrName) throws DataException {
		if (this.hasAggr(aggrName))
			return this.manager.getAggrDefn(aggrName);
		return null;
	}
}
