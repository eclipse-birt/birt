/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

package org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IBindingValueFetcher;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.cube.ICube;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Dimension;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Level;
import org.eclipse.birt.data.engine.olap.data.util.BufferedPrimitiveDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.SetUtil;
import org.eclipse.birt.data.engine.olap.impl.query.CubeQueryExecutor;
import org.eclipse.birt.data.engine.olap.impl.query.PreparedCubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.filter.AggrMeasureFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.CubePosFilter;
import org.eclipse.birt.data.engine.olap.util.filter.IAggrMeasureFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.ICubePosFilter;
import org.eclipse.birt.data.engine.olap.util.filter.InvalidCubePosFilter;
import org.eclipse.birt.data.engine.olap.util.filter.ValidCubePosFilter;
import org.eclipse.birt.data.engine.script.FilterPassController;
import org.eclipse.birt.data.engine.script.NEvaluator;
import org.eclipse.birt.data.engine.script.ScriptConstants;

/**
 *
 */

public class AggrMeasureFilterHelper {

	protected Map<String, IDimension> dimMap = null;
	protected IAggregationResultSet[] resultSet;
	private CubeQueryExecutor executor;
	protected IBindingValueFetcher fetcher;

	/**
	 *
	 * @param cube
	 * @param resultSet
	 */
	public AggrMeasureFilterHelper(ICube cube, IAggregationResultSet[] resultSet) {
		dimMap = new HashMap<>();
		IDimension[] dimension = cube.getDimesions();
		for (int i = 0; i < dimension.length; i++) {
			dimMap.put(dimension[i].getName(), dimension[i]);
		}
		this.resultSet = resultSet;
	}

	protected List<String> getAggregationName() {
		List<String> aggregationName = new ArrayList<>();
		for (int i = 0; i < resultSet.length; i++) {
			AggregationFunctionDefinition[] functions = resultSet[i].getAggregationDefinition()
					.getAggregationFunctions();
			if (functions == null) {
				continue;
			}
			for (int j = 0; j < functions.length; j++) {
				aggregationName.add(functions[j].getName());
			}
		}
		return aggregationName;
	}

	public void setQueryExecutor(CubeQueryExecutor executor) {
		this.executor = executor;
	}

	public void setBindingValueFetcher(IBindingValueFetcher fetcher) {
		this.fetcher = fetcher;
	}

	/**
	 *
	 * @param resultSet
	 * @throws DataException
	 * @throws IOException
	 */
	public List<ICubePosFilter> getCubePosFilters(List<IAggrMeasureFilterEvalHelper> jsMeasureEvalFilterHelper)
			throws DataException, IOException {
		String[] aggregationNames = populateAggregationNames(getAggregationName(), jsMeasureEvalFilterHelper);

		List<ICubePosFilter> cubePosFilterList = new ArrayList<>();
		for (int i = 0; i < resultSet.length; i++) {
			if (hasDefinition(resultSet[i], aggregationNames)) {
				//
				if (resultSet[i].getAllLevels() == null || resultSet[i].getAllLevels().length == 0) {
					if (resultSet[i].length() == 0) {
						return null;
					}
					AggregationRowAccessor rowAccessor = new AggregationRowAccessor(resultSet[i], fetcher);
					for (int j = 0; j < jsMeasureEvalFilterHelper.size(); j++) {
						if (resultSet[i].getAggregationIndex(aggregationNames[j]) >= 0) {
							IAggrMeasureFilterEvalHelper filterHelper = jsMeasureEvalFilterHelper.get(j);
							if (!isTopBottomNConditionalExpression(filterHelper.getExpression())) {
								// For grand total, the top/bottom filter is meaningless. Simply ignore.
								if (!filterHelper.evaluateFilter(rowAccessor)) {
									return null;
								}
							}
						}
					}
					continue;
				}
				Map<String, List<DimLevel>> levelMap = populateLevelMap(resultSet[i]);
				final int dimSize = levelMap.size();
				@SuppressWarnings("unchecked")
				List<DimLevel>[] levelListArray = new List[dimSize];
				levelMap.values().toArray(levelListArray);
				String[] dimensionNames = new String[dimSize];
				levelMap.keySet().toArray(dimensionNames);

				IDiskArray rowIndexArray = collectValidRowIndexArray(resultSet[i], jsMeasureEvalFilterHelper,
						aggregationNames);
				ICubePosFilter cubePosFilter = null;

				if (rowIndexArray.size() <= resultSet[i].length() / 2) {// use valid position filter
					cubePosFilter = getValidPosFilter(resultSet[i], rowIndexArray, dimensionNames, levelListArray);
				} else {// use invalid position filter
					cubePosFilter = getInvalidPosFilter(resultSet[i], rowIndexArray, dimensionNames, levelListArray);
				}
				cubePosFilterList.add(cubePosFilter);
			}
		}
		return cubePosFilterList;
	}

	/**
	 * to indicate whether the specified <code>resultSet</code> has aggregation
	 * definition for any one of the <code>aggregationNames</code>. //TODO Currently
	 * we do not support the filter on drilled aggregate result.
	 *
	 * @param resultSet
	 * @param aggregationNames
	 * @return
	 * @throws IOException
	 */
	protected boolean hasDefinition(IAggregationResultSet resultSet, String[] aggregationNames) throws IOException {
		for (int j = 0; j < aggregationNames.length; j++) {
			if (resultSet.getAggregationIndex(aggregationNames[j]) >= 0
					&& resultSet.getAggregationDefinition().getDrilledInfo() == null) {
				return true;
			}
		}
		return false;
	}

	private IBinding getBinding(String bindingName, List<IBinding> bindings) throws DataException {
		for (int j = 0; j < bindings.size(); j++) {
			IBinding binding = bindings.get(j);
			if (bindingName.equals(binding.getBindingName())) {
				return binding;
			}
		}
		return null;
	}

	/**
	 *
	 * @param jsMeasureEvalFilterHelper
	 * @return
	 * @throws DataException
	 */
	protected String[] populateAggregationNames(List<String> allAggrNames,
			List<IAggrMeasureFilterEvalHelper> jsMeasureEvalFilterHelper)
			throws DataException {
		String[] aggregationNames = new String[jsMeasureEvalFilterHelper.size()];
		for (int i = 0; i < aggregationNames.length; i++) {
			IAggrMeasureFilterEvalHelper filterHelper = jsMeasureEvalFilterHelper.get(i);
			List<String> bindingName = ExpressionCompilerUtil.extractColumnExpression(filterHelper.getExpression(),
					ScriptConstants.DATA_BINDING_SCRIPTABLE);
			aggregationNames[i] = (String) getIntersection(allAggrNames, bindingName);
			if (aggregationNames[i] == null) {
				aggregationNames[i] = OlapExpressionCompiler.getReferencedScriptObject(filterHelper.getExpression(),
						ScriptConstants.DATA_SET_BINDING_SCRIPTABLE);
				if (aggregationNames[i] == null && this.executor != null) {
					List<IBinding> bindingList = new ArrayList<>();
					ICubeQueryDefinition query = this.executor.getCubeQueryDefinition();
					bindingList.addAll(query.getBindings());
					if (query instanceof PreparedCubeQueryDefinition) {
						bindingList.addAll(((PreparedCubeQueryDefinition) query).getBindingsForNestAggregation());
					}
					List<String> referencedNames = new ArrayList<>();
					for (int j = 0; j < bindingName.size(); j++) {
						IBinding b = getBinding(bindingName.get(j).toString(), bindingList);
						if (b != null && b.getAggregatOns().size() == 0 && b.getAggrFunction() == null) {
							referencedNames.addAll(ExpressionCompilerUtil.extractColumnExpression(b.getExpression(),
									ScriptConstants.DATA_BINDING_SCRIPTABLE));
						}
					}
					aggregationNames[i] = (String) getIntersection(allAggrNames, referencedNames);
				}
			}
		}
		return aggregationNames;
	}

	private Object getIntersection(List<String> list1, List<String> list2) {
		for (int i = 0; i < list1.size(); i++) {
			for (int j = 0; j < list2.size(); j++) {
				if (list1.get(i).equals(list2.get(j))) {
					return list1.get(i);
				}
			}
		}
		return null;
	}

	/**
	 *
	 * @param resultSet
	 * @param rowIndexArray
	 * @param dimensionNames
	 * @param levelListArray
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	protected ICubePosFilter getInvalidPosFilter(IAggregationResultSet resultSet, IDiskArray rowIndexArray,
			String[] dimensionNames, List<DimLevel>[] levelListArray) throws IOException, DataException {
		CubePosFilter cubePosFilter = new InvalidCubePosFilter(dimensionNames);
		int rowIndex = 0;
		for (int i = 0; i < resultSet.length(); i++) {
			if (rowIndex < rowIndexArray.size()) {
				Integer index = (Integer) rowIndexArray.get(rowIndex);
				if (i == index.intValue()) {// ignore the valid positions
					rowIndex++;
					continue;
				}
			}
			resultSet.seek(i);

			IDiskArray[] dimPositions = new IDiskArray[dimensionNames.length];
			for (int j = 0; j < levelListArray.length; j++) {
				for (int k = 0; k < levelListArray[j].size(); k++) {
					DimLevel level = levelListArray[j].get(k);
					int levelIndex = resultSet.getLevelIndex(level);
					Object[] value = resultSet.getLevelKeyValue(levelIndex);
					IDiskArray positions = find(dimensionNames[j], level, value);
					if (dimPositions[j] == null) {
						dimPositions[j] = positions;
					} else {
						dimPositions[j] = SetUtil.getIntersection(dimPositions[j], positions);
					}
				}
			}
			cubePosFilter.addDimPositions(dimPositions);
			for (int n = 0; n < dimPositions.length; n++) {
				dimPositions[n].close();
			}
		}
		return cubePosFilter;
	}

	/**
	 *
	 * @param resultSet
	 * @param rowIndexArray
	 * @param dimensionNames
	 * @param levelListArray
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	protected ICubePosFilter getValidPosFilter(IAggregationResultSet resultSet, IDiskArray rowIndexArray,
			String[] dimensionNames, List<DimLevel>[] levelListArray) throws IOException, DataException {
		CubePosFilter cubePosFilter = new ValidCubePosFilter(dimensionNames);
		for (int i = 0; i < rowIndexArray.size(); i++) {
			Integer rowIndex = (Integer) rowIndexArray.get(i);
			resultSet.seek(rowIndex.intValue());
			IDiskArray[] dimPositions = new IDiskArray[dimensionNames.length];
			for (int j = 0; j < levelListArray.length; j++) {
				for (int k = 0; k < levelListArray[j].size(); k++) {
					DimLevel level = levelListArray[j].get(k);
					int levelIndex = resultSet.getLevelIndex(level);
					Object[] value = resultSet.getLevelKeyValue(levelIndex);
					IDiskArray positions = find(dimensionNames[j], level, value);
					if (dimPositions[j] == null) {
						dimPositions[j] = positions;
					} else {
						dimPositions[j] = SetUtil.getIntersection(dimPositions[j], positions);
					}
				}
			}
			cubePosFilter.addDimPositions(dimPositions);
			for (int n = 0; n < dimPositions.length; n++) {
				dimPositions[n].close();
			}
		}
		return cubePosFilter;
	}

	/**
	 * collect the filtered result
	 *
	 * @param resultSet
	 * @param filterHelpers
	 * @param aggregationNames
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private IDiskArray collectValidRowIndexArray(IAggregationResultSet resultSet,
			List<IAggrMeasureFilterEvalHelper> filterHelpers,
			String[] aggregationNames) throws DataException, IOException {
		IDiskArray result = new BufferedPrimitiveDiskArray();
		AggregationRowAccessor rowAccessor = new AggregationRowAccessor(resultSet, fetcher);
		List<IAggrMeasureFilterEvalHelper> firstRoundFilterHelper = new ArrayList<>();

		FilterPassController filterPassController = new FilterPassController();
		for (int j = 0; j < filterHelpers.size(); j++) {
			if (resultSet.getAggregationIndex(aggregationNames[j]) >= 0) {
				IAggrMeasureFilterEvalHelper filterHelper = filterHelpers.get(j);
				if (isTopBottomNConditionalExpression(filterHelper.getExpression())) {
					IConditionalExpression expr = (IConditionalExpression) filterHelper.getExpression();
					firstRoundFilterHelper.add(filterHelper);
					expr.setHandle(NEvaluator.newInstance(System.getProperty("java.io.tmpdir"),
							expr.getOperator(), expr.getExpression(), (IScriptExpression) expr.getOperand1(),
							filterPassController));
				}
			}
		}

		filterPassController.setPassLevel(FilterPassController.FIRST_PASS);
		filterPassController.setRowCount(resultSet.length());
		if (firstRoundFilterHelper.size() > 0) {
			for (int i = 0; i < resultSet.length(); i++) {
				resultSet.seek(i);
				for (int j = 0; j < firstRoundFilterHelper.size(); j++) {
					firstRoundFilterHelper.get(j).evaluateFilter(rowAccessor);
				}
			}
		}

		filterPassController.setPassLevel(FilterPassController.SECOND_PASS);

		for (int i = 0; i < resultSet.length(); i++) {
			resultSet.seek(i);
			boolean isFilterByAll = true;

			for (int j = 0; j < filterHelpers.size(); j++) {
				if (resultSet.getAggregationIndex(aggregationNames[j]) >= 0) {
					IAggrMeasureFilterEvalHelper filterHelper = filterHelpers.get(j);
					if (!filterHelper.evaluateFilter(rowAccessor)) {
						isFilterByAll = false;
						break;
					}
				}
			}

			if (isFilterByAll) {
				result.add(Integer.valueOf(i));
			}
		}
		return result;
	}

	public IAggregationResultSet[] removeInvalidAggrRows(List<IAggrMeasureFilterEvalHelper> jsMeasureEvalFilterHelper,
			List<Integer> affectedAggrResultSetIndex) throws DataException, IOException {
		IAggregationResultSet[] result = new IAggregationResultSet[resultSet.length];
		String[] aggregationNames = populateAggregationNames(getAggregationName(), jsMeasureEvalFilterHelper);
		for (int i = 0; i < resultSet.length; i++) {
			if (hasDefinition(resultSet[i], aggregationNames)) {
				IDiskArray validRows = collectValidAggregationResultSetRows(resultSet[i], jsMeasureEvalFilterHelper,
						aggregationNames);
				IAggregationResultSet newAggrResultSet = new AggregationResultSet(
						resultSet[i].getAggregationDefinition(), resultSet[i].getAllLevels(), validRows,
						resultSet[i].getKeyNames(), resultSet[i].getAttributeNames());
				result[i] = newAggrResultSet;
				affectedAggrResultSetIndex.add(i);
			} else {
				result[i] = resultSet[i];
			}
		}

		return result;
	}

	private IDiskArray collectValidAggregationResultSetRows(IAggregationResultSet resultSet,
			List<IAggrMeasureFilterEvalHelper> filterHelpers,
			String[] aggregationNames) throws DataException, IOException {
		IDiskArray result = new BufferedStructureArray(AggregationResultRow.getCreator(), resultSet.length());
		AggregationRowAccessor rowAccessor = new AggregationRowAccessor(resultSet, fetcher);
		List<IAggrMeasureFilterEvalHelper> firstRoundFilterHelper = new ArrayList<>();

		FilterPassController filterPassController = new FilterPassController();
		for (int j = 0; j < filterHelpers.size(); j++) {
			if (resultSet.getAggregationIndex(aggregationNames[j]) >= 0) {
				IAggrMeasureFilterEvalHelper filterHelper = filterHelpers.get(j);
				if (isTopBottomNConditionalExpression(filterHelper.getExpression())) {
					IConditionalExpression expr = (IConditionalExpression) filterHelper.getExpression();
					firstRoundFilterHelper.add(filterHelper);
					expr.setHandle(NEvaluator.newInstance(System.getProperty("java.io.tmpdir"),
							expr.getOperator(), expr.getExpression(), (IScriptExpression) expr.getOperand1(),
							filterPassController));
				}
			}
		}

		filterPassController.setPassLevel(FilterPassController.FIRST_PASS);
		filterPassController.setRowCount(resultSet.length());
		if (firstRoundFilterHelper.size() > 0) {
			for (int i = 0; i < resultSet.length(); i++) {
				resultSet.seek(i);
				for (int j = 0; j < firstRoundFilterHelper.size(); j++) {
					firstRoundFilterHelper.get(j).evaluateFilter(rowAccessor);
				}
			}
		}

		filterPassController.setPassLevel(FilterPassController.SECOND_PASS);

		for (int i = 0; i < resultSet.length(); i++) {
			resultSet.seek(i);
			boolean isFilterByAll = true;

			for (int j = 0; j < filterHelpers.size(); j++) {
				if (resultSet.getAggregationIndex(aggregationNames[j]) >= 0) {
					AggrMeasureFilterEvalHelper filterHelper = (AggrMeasureFilterEvalHelper) filterHelpers.get(j);
					if (!filterHelper.evaluateFilter(rowAccessor)) {
						isFilterByAll = false;
						break;
					}
				}
			}

			if (isFilterByAll) {
				result.add(resultSet.getCurrentRow());
			}
		}

		return result;
	}

	protected boolean isTopBottomNConditionalExpression(IBaseExpression expr) {
		if (expr == null || !(expr instanceof IConditionalExpression)) {
			return false;
		}
		switch (((IConditionalExpression) expr).getOperator()) {
		case IConditionalExpression.OP_TOP_N:
		case IConditionalExpression.OP_BOTTOM_N:
		case IConditionalExpression.OP_TOP_PERCENT:
		case IConditionalExpression.OP_BOTTOM_PERCENT:
			return true;
		default:
			return false;

		}
	}

	/**
	 *
	 * @param resultSet
	 * @return
	 */
	protected Map<String, List<DimLevel>> populateLevelMap(IAggregationResultSet resultSet) {
		final DimLevel[] dimLevels = resultSet.getAllLevels();
		Map<String, List<DimLevel>> levelMap = new HashMap<>();
		for (int j = 0; j < dimLevels.length; j++) {
			final String dimensionName = dimLevels[j].getDimensionName();
			List<DimLevel> list = levelMap.get(dimensionName);
			if (list == null) {
				list = new ArrayList<>();
				levelMap.put(dimensionName, list);
			}
			list.add(dimLevels[j]);
		}
		return levelMap;
	}

	/**
	 *
	 * @param dimensionName
	 * @param level
	 * @param keyValue
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	private IDiskArray find(String dimensionName, DimLevel level, Object[] keyValue) throws DataException, IOException {
		Dimension dimension = (Dimension) dimMap.get(dimensionName);
		ILevel[] levels = dimension.getHierarchy().getLevels();
		int i = 0;
		for (; i < levels.length; i++) {
			if (level.getLevelName().equals(levels[i].getName())) {
				break;
			}
		}
		if (i < levels.length) {
			return dimension.findPosition((Level) levels[i], keyValue);
		}
		throw new DataException("Can't find level {0} in the dimension!", level);//$NON-NLS-1$
	}
}
