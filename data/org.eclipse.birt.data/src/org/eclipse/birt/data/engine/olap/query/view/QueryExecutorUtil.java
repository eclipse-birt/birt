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
package org.eclipse.birt.data.engine.olap.query.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeSortDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.data.api.CubeQueryExecutorHelper;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IBindingValueFetcher;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.CachedAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.SortedAggregationRowArray;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.AggrSortDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.ITargetSort;
import org.eclipse.birt.data.engine.olap.impl.query.CubeOperationsExecutor;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryExecutor;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.eclipse.birt.data.engine.olap.util.sort.DimensionSortEvalHelper;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.mozilla.javascript.Scriptable;

public class QueryExecutorUtil {
	/**
	 * apply filter on nested aggregation
	 * 
	 * @param view
	 * @param stopSign
	 * @param executor
	 * @param finalAggregation
	 * @param rs
	 * @return
	 * @throws DataException
	 * @throws IOException
	 * @throws BirtException
	 */
	public static IAggregationResultSet[] applyFilterOnOperation(BirtCubeView view,
			CubeQueryExecutorHelper cubeQueryExecutorHelper, CubeQueryExecutor executor,
			AggregationDefinition[] finalAggregation, IAggregationResultSet[] rs, IBindingValueFetcher fetcher,
			StopSign stopSign) throws DataException, IOException, BirtException {
		if (!executor.getNestAggregationFilterEvalHelpers().isEmpty()) {
			// need to re-execute the query.
			// process derived measure/nested aggregation
			if (view.getCubeQueryExecutionHints() == null
					|| view.getCubeQueryExecutionHints().canExecuteCubeOperation()) {
				CubeOperationsExecutor coe = new CubeOperationsExecutor(view.getCubeQueryDefinition(),
						view.getPreparedCubeOperations(), view.getCubeQueryExecutor().getScope(),
						view.getCubeQueryExecutor().getSession().getEngineContext().getScriptContext());
				rs = coe.execute(rs, stopSign, fetcher);
				cubeQueryExecutorHelper.addAggrMeasureFilter(executor.getNestAggregationFilterEvalHelpers());
				cubeQueryExecutorHelper.applyAggrFilters(finalAggregation, rs, stopSign);
			}
		}
		return rs;
	}

	/**
	 * 
	 * @param cubeQueryDefinition
	 * @param cubeQueryExcutorHelper
	 * @throws DataException
	 */
	public static void populateAggregationSort(CubeQueryExecutor executor,
			CubeQueryExecutorHelper cubeQueryExcutorHelper, int type) throws DataException {
		List columnSort;
		switch (type) {
		case ICubeQueryDefinition.COLUMN_EDGE:
			columnSort = executor.getColumnEdgeSort();
			break;
		case ICubeQueryDefinition.ROW_EDGE:
			columnSort = executor.getRowEdgeSort();
			break;
		case ICubeQueryDefinition.PAGE_EDGE:
			columnSort = executor.getPageEdgeSort();
			break;
		default:
			return;
		}
		for (int i = 0; i < columnSort.size(); i++) {
			ICubeSortDefinition cubeSort = (ICubeSortDefinition) columnSort.get(i);
			ICubeQueryDefinition queryDefn = executor.getCubeQueryDefinition();
			String expr = cubeSort.getExpression().getText();
			ITargetSort targetSort = null;
			if ((cubeSort.getAxisQualifierLevels().length == 0 && (OlapExpressionUtil.isComplexDimensionExpr(expr)
					|| OlapExpressionUtil.isReferenceToAttribute(cubeSort.getExpression(), queryDefn.getBindings())))
					|| (!OlapExpressionUtil.isDirectRerenrence(cubeSort.getExpression(),
							executor.getCubeQueryDefinition().getBindings()))) {
				Scriptable scope = executor.getSession().getSharedScope();
				targetSort = new DimensionSortEvalHelper(executor.getOuterResults(), scope, queryDefn, cubeSort,
						executor.getSession().getEngineContext().getScriptContext());
			} else {
				String bindingName = OlapExpressionUtil.getBindingName(expr);
				if (bindingName == null)
					continue;
				List bindings = queryDefn.getBindings();
				List aggrOns = null;
				IBinding binding = null;
				for (int j = 0; j < bindings.size(); j++) {
					binding = (IBinding) bindings.get(j);
					if (binding.getBindingName().equals(bindingName)) {
						aggrOns = binding.getAggregatOns();
						break;
					}
				}

				DimLevel[] aggrOnLevels = null;

				if (aggrOns == null || aggrOns.size() == 0) {
					if (binding == null)
						continue;

					String measureName = OlapExpressionCompiler.getReferencedScriptObject(binding.getExpression(),
							ScriptConstants.MEASURE_SCRIPTABLE);
					if (measureName == null) {
						IBinding referBinding = OlapExpressionUtil.getDirectMeasureBinding(binding, bindings);
						if (referBinding != null) {
							measureName = OlapExpressionUtil.getMeasure(referBinding.getExpression());
							bindingName = referBinding.getBindingName();
							aggrOns = referBinding.getAggregatOns();

							if (referBinding.getAggrFunction() == null && (aggrOns == null || aggrOns.size() == 0)) {
								aggrOns = CubeQueryDefinitionUtil.populateMeasureAggrOns(queryDefn);
							}
						}
					}

					if (aggrOns != null && aggrOns.size() > 0) {
						aggrOnLevels = new DimLevel[aggrOns.size()];
						for (int j = 0; j < aggrOnLevels.length; j++) {
							aggrOnLevels[j] = OlapExpressionUtil.getTargetDimLevel(aggrOns.get(j).toString());
						}
					}
				} else {
					aggrOnLevels = new DimLevel[aggrOns.size()];
					for (int j = 0; j < aggrOnLevels.length; j++) {
						aggrOnLevels[j] = OlapExpressionUtil.getTargetDimLevel(aggrOns.get(j).toString());
					}
				}
				DimLevel[] axisLevels = new DimLevel[cubeSort.getAxisQualifierLevels().length];
				for (int k = 0; k < axisLevels.length; k++) {
					axisLevels[k] = new DimLevel(cubeSort.getAxisQualifierLevels()[k]);
				}
				targetSort = new AggrSortDefinition(aggrOnLevels, bindingName, axisLevels,
						cubeSort.getAxisQualifierValues(), new DimLevel(cubeSort.getTargetLevel()),
						cubeSort.getSortDirection());
			}
			switch (type) {
			case ICubeQueryDefinition.COLUMN_EDGE:
				cubeQueryExcutorHelper.addColumnSort(targetSort);
				break;
			case ICubeQueryDefinition.ROW_EDGE:
				cubeQueryExcutorHelper.addRowSort(targetSort);
				break;
			case ICubeQueryDefinition.PAGE_EDGE:
				cubeQueryExcutorHelper.addPageSort(targetSort);
			}
		}
	}

	/**
	 * 
	 * @param cube
	 * @param query
	 * @return
	 * @throws DataException
	 */
	public static AggregationDefinition[] prepareCube(ICubeQueryDefinition query, CalculatedMember[] calculatedMember,
			Scriptable scope, ScriptContext cx) throws DataException {
		IEdgeDefinition columnEdgeDefn = query.getEdge(ICubeQueryDefinition.COLUMN_EDGE);
		ILevelDefinition[] levelsOnColumn = CubeQueryDefinitionUtil.getLevelsOnEdge(columnEdgeDefn);
		IEdgeDefinition rowEdgeDefn = query.getEdge(ICubeQueryDefinition.ROW_EDGE);
		ILevelDefinition[] levelsOnRow = CubeQueryDefinitionUtil.getLevelsOnEdge(rowEdgeDefn);
		IEdgeDefinition pageEdgeDefn = query.getEdge(ICubeQueryDefinition.PAGE_EDGE);
		ILevelDefinition[] levelsOnPage = CubeQueryDefinitionUtil.getLevelsOnEdge(pageEdgeDefn);

		List<AggregationDefinition> aggregations = new ArrayList<AggregationDefinition>();

		int[] sortType;
		if (columnEdgeDefn != null) {
			DimLevel[] levelsForFilter = new DimLevel[levelsOnColumn.length + levelsOnPage.length];
			sortType = new int[levelsOnColumn.length + levelsOnPage.length];
			int index = 0;
			for (; index < levelsOnPage.length;) {
				levelsForFilter[index] = new DimLevel(levelsOnPage[index]);
				sortType[index] = CubeQueryDefinitionUtil.getSortDirection(levelsForFilter[index], query);
				index++;
			}
			for (int i = 0; i < levelsOnColumn.length; i++) {
				levelsForFilter[index] = new DimLevel(levelsOnColumn[i]);
				sortType[index] = CubeQueryDefinitionUtil.getSortDirection(levelsForFilter[i], query);
				index++;
			}
			aggregations.add(new AggregationDefinition(levelsForFilter, sortType, null));
		}
		if (rowEdgeDefn != null) {
			DimLevel[] levelsForFilter = new DimLevel[levelsOnRow.length + levelsOnPage.length];
			sortType = new int[levelsOnRow.length + levelsOnPage.length];
			int index = 0;
			for (; index < levelsOnPage.length;) {
				levelsForFilter[index] = new DimLevel(levelsOnPage[index]);
				sortType[index] = CubeQueryDefinitionUtil.getSortDirection(levelsForFilter[index], query);
				index++;
			}
			for (int i = 0; i < levelsOnRow.length; i++) {
				levelsForFilter[index] = new DimLevel(levelsOnRow[i]);
				sortType[index] = CubeQueryDefinitionUtil.getSortDirection(levelsForFilter[i], query);
				index++;
			}
			aggregations.add(new AggregationDefinition(levelsForFilter, sortType, null));
		}
		if (pageEdgeDefn != null) {
			DimLevel[] levelsForFilter = new DimLevel[levelsOnPage.length];
			sortType = new int[levelsOnPage.length];
			for (int i = 0; i < levelsOnPage.length; i++) {
				levelsForFilter[i] = new DimLevel(levelsOnPage[i]);
				sortType[i] = CubeQueryDefinitionUtil.getSortDirection(levelsForFilter[i], query);
			}
			aggregations.add(new AggregationDefinition(levelsForFilter, sortType, null));
		}

		AggregationDefinition[] fromCalculatedMembers = CubeQueryDefinitionUtil
				.createAggregationDefinitons(calculatedMember, query, scope, cx);

		aggregations.addAll(Arrays.asList(fromCalculatedMembers));

		return aggregations.toArray(new AggregationDefinition[0]);
	}

	/**
	 * If the length of edge cursor exceed the limit setting, throw exception.
	 * 
	 * @param cubeView
	 * @param rsArray
	 * @throws DataException
	 */
	public static void validateLimitSetting(BirtCubeView cubeView, IAggregationResultSet[] rsArray)
			throws DataException {
		int count = 0;
		if (cubeView.getColumnEdgeView() != null) {
			if (cubeView.getAppContext() != null) {
				int limitSize = populateFetchLimitSize(
						cubeView.getAppContext().get(DataEngine.CUBECURSOR_FETCH_LIMIT_ON_COLUMN_EDGE));
				if (limitSize > 0 && limitSize < rsArray[count].length()) {
					throw new DataException(ResourceConstants.RESULT_LENGTH_EXCEED_COLUMN_LIMIT,
							new Object[] { limitSize });
				}
			}
			count++;
		}
		if (cubeView.getRowEdgeView() != null) {
			if (cubeView.getAppContext() != null) {
				int limitSize = populateFetchLimitSize(
						cubeView.getAppContext().get(DataEngine.CUBECUSROR_FETCH_LIMIT_ON_ROW_EDGE));
				if (limitSize > 0 && limitSize < rsArray[count].length()) {
					throw new DataException(ResourceConstants.RESULT_LENGTH_EXCEED_ROW_LIMIT,
							new Object[] { limitSize });
				}
			}
			count++;
		}
	}

	/**
	 * 
	 * @param propValue
	 * @return
	 */
	private static int populateFetchLimitSize(Object propValue) {
		int fetchLimit = -1;
		String fetchLimitSize = propValue == null ? "-1" : propValue.toString();

		if (fetchLimitSize != null)
			fetchLimit = Integer.parseInt(fetchLimitSize);

		return fetchLimit;
	}

	public static IAggregationResultSet sortAggregationResultSet(IAggregationResultSet rs) throws IOException {
		SortedAggregationRowArray sarr = new SortedAggregationRowArray(rs);
		rs.close();
		return new AggregationResultSet(rs.getAggregationDefinition(), rs.getAllLevels(), sarr.getSortedRows(),
				rs.getKeyNames(), rs.getAttributeNames());
	}

	public static void initLoadedAggregationResultSets(IAggregationResultSet[] arss, AggregationDefinition[] ads) {
		assert ads.length <= arss.length;
		for (int i = 0; i < ads.length; i++) {
			CachedAggregationResultSet cars = (CachedAggregationResultSet) arss[i];
			cars.setAggregationDefinition(ads[i]);
		}
	}

}
