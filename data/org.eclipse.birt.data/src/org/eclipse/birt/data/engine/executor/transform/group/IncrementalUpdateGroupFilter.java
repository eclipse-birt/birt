/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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
package org.eclipse.birt.data.engine.executor.transform.group;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.cache.SimpleSmartCache;
import org.eclipse.birt.data.engine.executor.transform.FilterUtil;
import org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IQuery.GroupSpec;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.script.FilterPassController;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;

/**
 * Filter groups from outermost level to innermost level based on current group
 * info.
 * <p>
 * While filtering groups, the group info and aggregation values are
 * incrementally updated.
 *
 * @author fengbin
 *
 */
public class IncrementalUpdateGroupFilter extends IncrementalUpdateCaculator {
	private GroupSpec[] groupSpecs;
	private FilterPassController filterPassCtrl;
	private ScriptContext scriptCtx;
	private SimpleSmartCache resultSetCache;

	public IncrementalUpdateGroupFilter(ResultSetPopulator populator) throws DataException {
		super(populator);
		this.scriptCtx = this.populator.getSession().getEngineContext().getScriptContext();
		this.groupSpecs = populator.getQuery().getGrouping();

		populateFilterExpression();
	}

	@SuppressWarnings("unchecked")
	public void doFilters() throws DataException {
		if (!doFiltering) {
			return;
		}

		for (int i = 1; i <= this.groupSpecs.length; i++) {
			List<IFilterDefinition> filters = this.groupSpecs[i - 1].getFilters();
			if (filters == null || filters.size() == 0) {
				continue;
			}

			populator.getResultIterator().first(0);
			resultSetCache = new SimpleSmartCache(populator.getSession(), populator.getEventHandler(),
					populator.getResultSetMetadata());

			if (FilterUtil.hasMutipassFilters(filters)) {
				List singlePassFilter = new ArrayList<IFilterDefinition>();
				List multiPassFilter = new ArrayList<IFilterDefinition>();

				for (Object filter : filters) {
					if (FilterUtil.isFilterNeedMultiPass(((IFilterDefinition) filter))) {
						multiPassFilter.add(filter);
					} else {
						singlePassFilter.add(filter);
					}
				}

				if (singlePassFilter.size() > 0) {
					doSinglePassFilter(i, singlePassFilter);
					setFilteringResults();
					resultSetCache = new SimpleSmartCache(populator.getSession(), populator.getEventHandler(),
							populator.getResultSetMetadata());
				}

				doMultiPassFilter(i, multiPassFilter);
			} else {
				doSinglePassFilter(i, filters);
			}

			setFilteringResults();
			restFilters(filters);
		}

		// Rewind result set cursor to the first row.
		this.populator.getResultIterator().first(0);
	}

	private void restFilters(List<IFilterDefinition> filters) {
		for (int j = 0; j < filters.size(); j++) {
			IFilterDefinition fd = filters.get(j);
			if (FilterUtil.isFilterNeedMultiPass(fd)) {
				fd.getExpression().setHandle(null);
			}
		}
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	private void populateFilterExpression() throws DataException {
		ArrayList<Integer> levelList = new ArrayList<>();
		ArrayList<IBaseExpression> exprsList = new ArrayList<>();

		for (int i = 0; i < this.groupSpecs.length; i++) {
			List<IFilterDefinition> groupFilters = this.groupSpecs[i].getFilters();
			if (groupFilters == null) {
				continue;
			}

			String name = this.groupSpecs[i].getName();
			for (int j = 0; j < groupFilters.size(); j++) {
				if (!groupFilters.get(j).updateAggregation()) {
					IBaseExpression expr = ((IFilterDefinition) groupFilters.get(j)).getExpression();
					expr.setGroupName(name);
					exprsList.add(expr);
					levelList.add(Integer.valueOf(i + 1));
				}
			}
		}

		if (exprsList.size() > 0) {
			doFiltering = true;
			populator.getGroupProcessorManager().setExpressionProcessor(populator.getExpressionProcessor());
			populator.getGroupProcessorManager().calculateExpressionList(exprsList, levelList,
					IExpressionProcessor.FILTER_ON_GROUP_EXPR);
			filterPassCtrl = new FilterPassController();
		}
	}

	private void seekToGroup(int groupLevel, int groupIdx) throws DataException {
		this.populator.getResultIterator().first(0);
		for (int i = 0; i < groupIdx; i++) {
			this.populator.getResultIterator().last(groupLevel);
			this.populator.getResultIterator().next();
		}
	}

	private void doSinglePassFilter(int level, List<IFilterDefinition> filters) throws DataException {
		doFilterPass(level, filters, groupSize[level - 1]);
	}

	private void doMultiPassFilter(int level, List<IFilterDefinition> filters) throws DataException {
		if (level > 1) {
			int currentGroupIndex = 0;
			for (int i = 0; i < groupSize[level - 2]; i++) // Outter level
			{
				int count = this.groupUpdators[level - 2].getChildCount(i);
				// First pass
				filterPassCtrl.setPassLevel(FilterPassController.FIRST_PASS);
				filterPassCtrl.setRowCount(count);
				doEvaluatePass(level, filters, count);

				// Second pass
				filterPassCtrl.setPassLevel(FilterPassController.SECOND_PASS);
				seekToGroup(level, currentGroupIndex);
				doFilterPass(level, filters, count);

				filterPassCtrl.setPassLevel(FilterPassController.DEFAULT_PASS);
				filterPassCtrl.setRowCount(FilterPassController.DEFAULT_ROW_COUNT);
				filterPassCtrl.setSecondPassRowCount(0);
				currentGroupIndex += count;
			}
		} else {
			filterPassCtrl.setPassLevel(FilterPassController.FIRST_PASS);
			filterPassCtrl.setRowCount(groupSize[level - 1]);
			doEvaluatePass(level, filters, groupSize[level - 1]);

			filterPassCtrl.setPassLevel(FilterPassController.SECOND_PASS);
			seekToGroup(level, 0);
			doFilterPass(level, filters, groupSize[level - 1]);
			filterPassCtrl.setPassLevel(FilterPassController.DEFAULT_PASS);
			filterPassCtrl.setRowCount(FilterPassController.DEFAULT_ROW_COUNT);
			filterPassCtrl.setSecondPassRowCount(0);
		}
	}

	private void doEvaluatePass(int level, List<IFilterDefinition> filters, int count) throws DataException {
		for (int i = 0; i < count; i++) {
			populator.getResultIterator().last(level);
			evaluateFilters(filters);
			populator.getResultIterator().next();
		}
	}

	private void doFilterPass(int level, List<IFilterDefinition> filters, int count) throws DataException {
		for (int i = 0; i < count; i++) {
			populator.getResultIterator().first(level);
			boolean accept = evaluateFilters(filters);
			int gIdx = getCurrentGroupIndex(level);
			if (accept) {
				// GroupUpdator add accept groups from current level to
				// innermost level
				// Put all rows to result set cache
				acceptGroup(level, gIdx);
			} else {
				// GroupUpdator report sublevel group removed, update groups
				// from current level to outtermost level
				filterGroup(level, gIdx);
			}
		}
	}

	private void setFilteringResults() throws DataException {
		List<GroupInfo>[] currentGroupInfo = getGroups();
		// Reset populator with new result set.
		populator.setCache(resultSetCache);
		populator.getGroupProcessorManager().getGroupCalculationUtil().setResultSetCache(resultSetCache);
		populator.getGroupProcessorManager().getGroupCalculationUtil().getGroupInformationUtil()
				.setGroups(currentGroupInfo);
		populator.getGroupProcessorManager().setExpressionProcessor(populator.getExpressionProcessor());
		resultSetCache = null;

		// Prepare for next filtering round.
		populator.getResultIterator().clearAggrValueHolder();
		AggrValuesUpdator[] aggrs = aggrValuesUpdators;
		aggrValuesUpdators = new AggrValuesUpdator[aggrs.length];
		for (int i = 0; i < aggrs.length; i++) {
			populator.getResultIterator().addAggrValueHolder(aggrs[i]);
			aggrValuesUpdators[i] = new AggrValuesUpdator(aggrs[i], populator);
		}

		originGroups = currentGroupInfo;
		groupSize = new int[originGroups.length];
		for (int i = 0; i < originGroups.length; i++) {
			groupUpdators[i] = new GroupInfoUpdator(i, tempDir, originGroups[i], getLastGroupIndex(i),
					aggrValuesUpdators);
			groupSize[i] = originGroups[i].size();
		}

		filterPassCtrl.setPassLevel(FilterPassController.DEFAULT_PASS);
		filterPassCtrl.setRowCount(FilterPassController.DEFAULT_ROW_COUNT);
		filterPassCtrl.setSecondPassRowCount(0);
	}

	private void acceptGroup(int level, int groupIndex) throws DataException {
		groupUpdators[level - 1].onGroup(groupIndex);
		GroupRange childChunck = groupUpdators[level - 1].getChildRange();

		// Update high levels
		for (int i = level - 2; i >= 0; i--) {
			groupUpdators[i].onGroup(getCurrentGroupIndex(i + 1));
		}

		// Update lower levels.
		for (int i = level; i < groupUpdators.length; i++) {
			childChunck = groupUpdators[i].acceptGroupRange(childChunck);
		}

		// Accept result set rows.
		for (int i = childChunck.first; i < childChunck.length + childChunck.first; i++) {
			assert i == populator.getResultIterator().getCurrentResultIndex();
			IResultObject res = populator.getResultIterator().getCurrentResult();
			resultSetCache.add(res);
			acceptAggrValues(i); // Accept running aggregations.
			populator.getResultIterator().next();
		}
	}

	private void filterGroup(int level, int groupIndex) throws DataException {
		// Filter current group
		groupUpdators[level - 1].filterGroup(groupIndex);
		GroupRange chunk = groupUpdators[level - 1].getChildRange();
		int pRemoved = 1;

		// Update lower level
		for (int i = level; i < groupUpdators.length; i++) {
			groupUpdators[i].increaseParentIndex(pRemoved);
			pRemoved = chunk.length;
			chunk = groupUpdators[i].filterGroupRange(chunk);
		}

		for (int i = level - 2; i >= 0; i--) {
			if (groupUpdators[i].notOnGroup(getCurrentGroupIndex(i + 1)) < 0) {
				break;
			}

			if (i < groupUpdators.length - 1) {
				groupUpdators[i + 1].increaseParentIndex();
			}
		}

		// Skip rows in current group.
		populator.getResultIterator().last(level);
		populator.getResultIterator().next();
	}

	private boolean evaluateFilters(List<IFilterDefinition> filters) throws DataException {
		for (int j = 0; j < filters.size(); j++) {
			Object result = evaluteFilterExpression(filters.get(j));

			try {
				if (!DataTypeUtil.toBoolean(result).booleanValue()) {
					return false;
				}
			} catch (BirtException e) {
				throw new DataException(ResourceConstants.DATATYPEUTIL_ERROR, e);
			}
		}
		return true;
	}

	private Object evaluteFilterExpression(IFilterDefinition filter) throws DataException {
		IBaseExpression expr = filter.getExpression();
		FilterUtil.prepareFilterExpression(tempDir, expr, filterPassCtrl,
				this.populator.getEventHandler().getExecutorHelper());

		Object result = ScriptEvalUtil.evalExpr(expr,
				scriptCtx.newContext(this.populator.getGroupProcessorManager().getExpressionProcessor().getScope()),
				ScriptExpression.defaultID, 0);

		if (result == null) {
			Object info = null;
			if (expr instanceof IScriptExpression) {
				info = ((IScriptExpression) expr).getText();
			} else {
				info = expr;
			}
			throw new DataException(ResourceConstants.INVALID_EXPRESSION_IN_FILTER, info);
		}
		return result;
	}
}
