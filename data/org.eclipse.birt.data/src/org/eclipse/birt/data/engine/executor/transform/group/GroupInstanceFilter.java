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
import org.eclipse.birt.data.engine.cache.CachedList;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.FilterUtil;
import org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor;
import org.eclipse.birt.data.engine.executor.transform.OrderingInfo;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.script.FilterPassController;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;

/**
 * The class which is used to do group instance filterings.
 */
class GroupInstanceFilter {
	private ResultSetPopulator populator;

	private FilterPassController filterPass = new FilterPassController();

	private GroupProcessorManager groupProcessor;

	private String tempDir;

	GroupInstanceFilter(GroupProcessorManager gp) {
		this.populator = gp.getResultSetPopulator();
		this.groupProcessor = gp;
		this.tempDir = this.populator.getSession().getTempDir();
	}

	/**
	 * Do group filtering job.
	 *
	 * @param cx
	 * @param stopSign
	 * @throws DataException
	 */
	public void doGroupFiltering(ScriptContext cx) throws DataException {
		List groupLevels = new ArrayList();
		List expressionList = new ArrayList();
		populateGroupFilteringExpressions(expressionList, groupLevels);
		if (expressionList.size() > 0) {
			this.groupProcessor.calculateExpressionList(expressionList, groupLevels,
					IExpressionProcessor.FILTER_ON_GROUP_EXPR);
			// The groupBoundaryInfos gotten here has not be sorted yet.
			List[] groupBoundaryInfos = this.groupProcessor.getGroupCalculationUtil().getGroupInformationUtil()
					.getGroupBoundaryInfos();

			populateFiltersInGroupBoundaryInfoSets(cx, groupBoundaryInfos);

			groupBoundaryInfos = this.groupProcessor.getGroupCalculationUtil()
					.filterGroupBoundaryInfos(groupBoundaryInfos);

			OrderingInfo odInfo = this.groupProcessor.getGroupCalculationUtil().getGroupInformationUtil()
					.getOrderingInfo(groupBoundaryInfos);

			this.populator.reSetSmartCacheUsingOrderingInfo(odInfo);
		}
	}

	/**
	 * Populate the Group filter expression list and its corresponding group level
	 * list that being used in IExpressionProcessor.calculate() method.
	 *
	 * @return
	 */
	private void populateGroupFilteringExpressions(List expressionList, List groupLevels) {
		for (int i = 0; i < this.populator.getQuery().getGrouping().length; i++) {
			List groupFilters = this.populator.getQuery().getGrouping()[i].getFilters();
			String name = this.populator.getQuery().getGrouping()[i].getName();
			if (groupFilters == null) {
				continue;
			}
			for (int j = 0; j < groupFilters.size(); j++) {
				if (((IFilterDefinition) groupFilters.get(j)).updateAggregation()) {
					IBaseExpression expr = ((IFilterDefinition) groupFilters.get(j)).getExpression();
					expr.setGroupName(name);
					expressionList.add(expr);
					groupLevels.add(Integer.valueOf(i + 1));
				}
			}
		}
	}

	/**
	 * Populate the "accept" fields in GroupBoundaryInfos. If one GroupBoundaryInfo
	 * would be filtered out then the "accept" field is set to false else ture.
	 *
	 * @param cx
	 * @param groupBoundaryInfos
	 * @throws DataException
	 */
	private void populateFiltersInGroupBoundaryInfoSets(ScriptContext cx, List[] groupBoundaryInfos)
			throws DataException {
		for (int i = 1; i <= this.populator.getQuery().getGrouping().length; i++) {
			// The sorts of certain group
			List filters = this.populator.getQuery().getGrouping()[i - 1].getFilters();
			if (filters == null || filters.size() == 0) {
				continue;
			}

			// Return to first record.
			this.populator.getResultIterator().first(0);

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
					populateGroupBoundaryInfoFilters(cx, groupBoundaryInfos[i - 1], singlePassFilter, i, true);
				}

				populateGroupBoundaryInfoFilterValues(cx, groupBoundaryInfos, i, multiPassFilter);
			} else {
				populateGroupBoundaryInfoFilters(cx, groupBoundaryInfos[i - 1], filters, i, true);
			}
			groupBoundaryInfos = this.groupProcessor.getGroupCalculationUtil()
					.filterGroupBoundaryInfos(groupBoundaryInfos);
			for (int j = 0; j < filters.size(); j++) {
				IFilterDefinition fd = (IFilterDefinition) filters.get(j);
				if (FilterUtil.isFilterNeedMultiPass(fd)) {
					fd.getExpression().setHandle(null);
				}
			}
		}
	}

	/**
	 * @param cx
	 * @param groupBoundaryInfos
	 * @param i
	 * @param groupedFilters
	 * @param j
	 * @throws DataException
	 */
	private void populateGroupBoundaryInfoFilterValues(ScriptContext cx, List[] groupBoundaryInfos, int i,
			List groupedFilters) throws DataException {
		if (i > 1) {
			int passedGroups = 0;
			for (int k = 0; k < groupBoundaryInfos[i - 2].size(); k++) {
				List currentGroupArray = new CachedList(tempDir, DataEngineSession.getCurrentClassLoader(),
						GroupBoundaryInfo.getCreator());
				for (int n = 0; n < groupBoundaryInfos[i - 1].size(); n++) {
					if ((((GroupBoundaryInfo) groupBoundaryInfos[i - 2].get(k))
							.isInBoundary(((GroupBoundaryInfo) groupBoundaryInfos[i - 1].get(n))))) {
						currentGroupArray.add(groupBoundaryInfos[i - 1].get(n));
					}
				}
				makeAGroupFilteringMultiPass(cx, i, groupedFilters, currentGroupArray, passedGroups);
				passedGroups += currentGroupArray.size();
			}
		} else {
			makeAGroupFilteringMultiPass(cx, i, groupedFilters, groupBoundaryInfos[i - 1], 0);
		}
	}

	/**
	 * Dealing with filters that needs multi-pass.
	 *
	 * @param cx
	 * @param groupLevel
	 * @param groupedFilters
	 * @param j
	 * @param currentGroupArray
	 * @param startingGroupInstanceIndex
	 * @throws DataException
	 */
	private void makeAGroupFilteringMultiPass(ScriptContext cx, int groupLevel, List filters, List currentGroupArray,
			int startingGroupInstanceIndex) throws DataException {
		advanceResultIteratorCursor(groupLevel, startingGroupInstanceIndex);

		// Make first pass
		filterPass.setPassLevel(FilterPassController.FIRST_PASS);
		filterPass.setRowCount(currentGroupArray.size());

		populateGroupBoundaryInfoFilters(cx, currentGroupArray, filters, groupLevel, false);

		advanceResultIteratorCursor(groupLevel, startingGroupInstanceIndex);
		// Make second pass
		filterPass.setPassLevel(FilterPassController.SECOND_PASS);

		populateGroupBoundaryInfoFilters(cx, currentGroupArray, filters, groupLevel, true);

		filterPass.setPassLevel(FilterPassController.DEFAULT_PASS);
		filterPass.setRowCount(FilterPassController.DEFAULT_ROW_COUNT);
		filterPass.setSecondPassRowCount(0);
	}

	/**
	 * @param groupLevel
	 * @param startingGroupInstanceIndex
	 * @throws DataException
	 */
	private void advanceResultIteratorCursor(int groupLevel, int startingGroupInstanceIndex) throws DataException {
		this.populator.getResultIterator().first(0);
		for (int i = 0; i < startingGroupInstanceIndex; i++) {
			this.populator.getResultIterator().last(groupLevel);
			this.populator.getResultIterator().next();
		}
	}

	/**
	 * Add filter infos to a group of GroupBoundaryInfo instances.
	 *
	 * @param cx
	 * @param currentGroupArray
	 * @param filters
	 * @param groupLevel
	 * @param setUpValue        if true then set the value to GroupBoundaryInfos,
	 *                          else not.
	 * @throws DataException
	 */
	private void populateGroupBoundaryInfoFilters(ScriptContext cx, List currentGroupArray, List filters,
			int groupLevel, boolean setUpValue) throws DataException {
		for (int m = 0; m < currentGroupArray.size(); m++) {
			GroupBoundaryInfo currentGBI = (GroupBoundaryInfo) currentGroupArray.get(m);
			this.populator.getResultIterator().last(groupLevel);
			if (!currentGBI.isAccpted()) {
				this.populator.getResultIterator().next();
				continue;
			}

			boolean accept = evaluateFilters(cx, filters);
			if (setUpValue) {
				currentGBI.setAccepted(currentGBI.isAccpted() && accept);
			}
			this.populator.getResultIterator().next();
		}
	}

	/**
	 * Evaluate the value of a series of filters
	 *
	 * @param cx
	 * @param groupFilters
	 * @return
	 * @throws DataException
	 */
	private boolean evaluateFilters(ScriptContext cx, List groupFilters) throws DataException {
		for (int j = 0; j < groupFilters.size(); j++) {
			Object result = evaluteFilterExpression(cx, (IFilterDefinition) (groupFilters.get(j)));

			try {
				if (!DataTypeUtil.toBoolean(result).booleanValue()) {
					return false;
				}
			} catch (BirtException e) {
				DataException e1 = new DataException(ResourceConstants.DATATYPEUTIL_ERROR, e);
				throw e1;
			}

		}
		return true;
	}

	/**
	 * Evaluate a filter expression.
	 *
	 * @param cx
	 * @param filter
	 * @return
	 * @throws DataException
	 */
	private Object evaluteFilterExpression(ScriptContext cx, IFilterDefinition filter) throws DataException {
		IBaseExpression expr = filter.getExpression();
		FilterUtil.prepareFilterExpression(tempDir, expr, filterPass,
				this.populator.getEventHandler().getExecutorHelper());

		Object result = ScriptEvalUtil.evalExpr(expr,
				cx.newContext(this.groupProcessor.getExpressionProcessor().getScope()), ScriptExpression.defaultID, 0);

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
