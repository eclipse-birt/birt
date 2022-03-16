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

package org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.cache.Constants;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IBindingValueFetcher;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.ISelection;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.Cube;
import org.eclipse.birt.data.engine.olap.data.impl.SelectionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.util.BufferedPrimitiveDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.CompareUtil;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.ObjectArrayUtil;
import org.eclipse.birt.data.engine.olap.data.util.OrderedDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.SetUtil;
import org.eclipse.birt.data.engine.olap.util.filter.IJSDimensionFilterHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFilterHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IJSTopBottomFilterHelper;

/**
 *
 */

public class AggregationFilterHelper {

	private Map dimensionMap;
	private List aggrFilters;
	private List topbottomFilters;
	private boolean isEmptyXtab;
	private IBindingValueFetcher fetcher;

	/**
	 *
	 * @param cube
	 * @param jsFilterHelpers
	 */
	public AggregationFilterHelper(Cube cube, List jsFilterHelpers, IBindingValueFetcher bindingValueFetcher) {
		this.fetcher = bindingValueFetcher;
		populateDimensionLevels(cube);
		// populate the filter helpers to aggrFilters and topbottomFilters
		populateFilters(jsFilterHelpers);
	}

	/**
	 * transform the specified filter helpers to level filters for another
	 * aggregation calculation. Note: if the returned list is null, which means the
	 * final aggregation result will be empty, and no more calculations are needed.
	 *
	 * @param aggregations
	 * @param resultSet
	 * @return
	 * @throws DataException
	 */
	public List generateLevelFilters(AggregationDefinition[] aggregations, IAggregationResultSet[] resultSet)
			throws DataException {
		List levelFilterList = new ArrayList();
		try {
			applyAggrFilters(aggregations, resultSet, levelFilterList);
			if (isEmptyXtab) {
				return null;
			}
			applyTopBottomFilters(aggregations, resultSet, levelFilterList);

			if (levelFilterList.size() == 0) {
				return null;
			}
		} catch (IOException e) {
			throw new DataException("", e);//$NON-NLS-1$
		}
		return levelFilterList;
	}

	/**
	 *
	 * @param jsFilterHelpers
	 */
	private void populateFilters(List jsFilterHelpers) {
		aggrFilters = new ArrayList();
		topbottomFilters = new ArrayList();
		for (Iterator i = jsFilterHelpers.iterator(); i.hasNext();) {
			IJSFilterHelper filterHelper = (IJSFilterHelper) i.next();
			if (filterHelper instanceof IJSDimensionFilterHelper) {
				IJSDimensionFilterHelper dimFilterHelper = (IJSDimensionFilterHelper) filterHelper;
				aggrFilters.add(new AggrFilterDefinition(dimFilterHelper));
			} else if (filterHelper instanceof IJSTopBottomFilterHelper) {
				IJSTopBottomFilterHelper tbFilterHelper = (IJSTopBottomFilterHelper) filterHelper;
				topbottomFilters.add(new TopBottomFilterDefinition(tbFilterHelper));
			}
		}
	}

	/**
	 *
	 * @param cube
	 */
	private void populateDimensionLevels(Cube cube) {
		dimensionMap = new HashMap();
		if (cube == null) {
			return;
		}
		IDimension[] dimensions = cube.getDimesions();
		for (int i = 0; i < dimensions.length; i++) {
			dimensionMap.put(dimensions[i].getName(), dimensions[i].getHierarchy().getLevels());
		}
	}

	/**
	 *
	 * @param aggregations
	 * @param resultSet
	 * @param levelFilterList
	 * @throws DataException
	 * @throws IOException
	 */
	private void applyAggrFilters(AggregationDefinition[] aggregations, IAggregationResultSet[] resultSet,
			List levelFilterList) throws DataException, IOException {
		for (Iterator i = aggrFilters.iterator(); i.hasNext();) {
			AggrFilterDefinition filter = (AggrFilterDefinition) i.next();
			for (int j = 0; !isEmptyXtab && j < aggregations.length; j++) {
				if (aggregations[j].getAggregationFunctions() != null
						&& isMatch(aggregations[j], resultSet[j], filter)) {
					applyAggrFilter(resultSet[j], filter, levelFilterList);
				}
			}
		}
	}

	private boolean isMatchResultSet(AggregationDefinition aggregation, IAggregationResultSet resultSet,
			AggrFilterDefinition filter) {
		return filter.getAggrLevels() == null && resultSet.getLevelIndex(filter.getTargetLevel()) >= 0;
	}

	/**
	 *
	 * @param aggregation
	 * @param resultSet
	 * @param filter
	 * @return
	 */
	private boolean isMatch(AggregationDefinition aggregation, IAggregationResultSet resultSet,
			AggrFilterDefinition filter) {
		return filter.getAggrLevels() == null && resultSet.getLevelIndex(filter.getTargetLevel()) >= 0
				|| FilterUtil.isEqualLevels(aggregation.getLevels(), filter.getAggrLevels());
	}

	private void applyNoUpdateAggrFilter(IAggregationResultSet resultSet, AggrFilterDefinition filter,
			List levelFilters) throws DataException, IOException {
		DimLevel targetLevel = filter.getTargetLevel();
		int targetIndex = resultSet.getLevelIndex(targetLevel);
		// template key values' list that have been filtered in the same
		// qualified level
		List selKeyValueList = new ArrayList();
		// to remember the members of the dimension that consists of the
		// previous aggregation result's target level
		Member[] preMembers = null;
		IJSDimensionFilterHelper filterHelper = (IJSDimensionFilterHelper) filter.getFilterHelper();
		AggregationRowAccessor row4filter = new AggregationRowAccessor(resultSet, fetcher);
		for (int k = 0; k < resultSet.length(); k++) {
			resultSet.seek(k);
			boolean isSelect = filterHelper.evaluateFilter(row4filter);
			if (isSelect) {// generate level filter here
				Member[] members = resultSet.getCurrentRow().getLevelMembers();// getTargetDimMembers(
																				// targetLevel.getDimensionName( ),
				// resultSet );
				if (preMembers != null && !FilterUtil.shareParentLevels(members, preMembers, targetIndex)) {
					LevelFilter levelFilter = toLevelFilter(targetLevel, selKeyValueList, preMembers, filterHelper);
					levelFilters.add(levelFilter);
					selKeyValueList.clear();
				}
				int levelIndex = resultSet.getLevelIndex(targetLevel);
				// select aggregation row
				Object[] levelKeyValue = resultSet.getLevelKeyValue(levelIndex);
				if (levelKeyValue != null && levelKeyValue[0] != null) {
					selKeyValueList.add(levelKeyValue);
				}
				preMembers = members;
			}
		}
		// ---------------------------------------------------------------------------------
		if (preMembers == null) {// filter is empty, so that the final x-Tab will be empty
			isEmptyXtab = true;
			return;
		}
		// generate the last level filter
		if (!selKeyValueList.isEmpty()) {
			LevelFilter levelFilter = toLevelFilter(targetLevel, selKeyValueList, preMembers, filterHelper);
			levelFilters.add(levelFilter);
		}
	}

	/**
	 * @param resultSet
	 * @param filter
	 * @param levelFilters
	 * @throws IOException
	 * @throws DataException
	 */
	private void applyAggrFilter(IAggregationResultSet resultSet, AggrFilterDefinition filter, List levelFilters)
			throws DataException, IOException {
		DimLevel targetLevel = filter.getTargetLevel();
		ILevel[] levelsOfDimension = getLevelsOfDimension(targetLevel.getDimensionName());
		int targetIndex = FilterUtil.getTargetLevelIndex(levelsOfDimension, targetLevel.getLevelName());
		// template key values' list that have been filtered in the same
		// qualified level
		List selKeyValueList = new ArrayList();
		// to remember the members of the dimension that consists of the
		// previous aggregation result's target level
		Member[] preMembers = null;
		IJSDimensionFilterHelper filterHelper = (IJSDimensionFilterHelper) filter.getFilterHelper();
		AggregationRowAccessor row4filter = new AggregationRowAccessor(resultSet, fetcher);
		for (int k = 0; k < resultSet.length(); k++) {
			resultSet.seek(k);
			boolean isSelect = filterHelper.evaluateFilter(row4filter);
			if (isSelect) {// generate level filter here
				Member[] members = getTargetDimMembers(targetLevel.getDimensionName(), resultSet);
				if (preMembers != null && !FilterUtil.shareParentLevels(members, preMembers, targetIndex)) {
					LevelFilter levelFilter = toLevelFilter(targetLevel, selKeyValueList, preMembers, filterHelper);
					levelFilters.add(levelFilter);
					selKeyValueList.clear();
				}
				int levelIndex = resultSet.getLevelIndex(targetLevel);
				// select aggregation row
				Object[] levelKeyValue = resultSet.getLevelKeyValue(levelIndex);
				if (levelKeyValue != null && levelKeyValue[0] != null) {
					selKeyValueList.add(levelKeyValue);
				}
				preMembers = members;
			}
		}
		// ---------------------------------------------------------------------------------
		if (preMembers == null) {// filter is empty, so that the final x-Tab will be empty
			isEmptyXtab = true;
			return;
		}
		// generate the last level filter
		if (!selKeyValueList.isEmpty()) {
			LevelFilter levelFilter = toLevelFilter(targetLevel, selKeyValueList, preMembers, filterHelper);
			levelFilters.add(levelFilter);
		}
	}

	private boolean hasFiltersOnEdgeAggrResultSet(IAggregationResultSet rs) {
		boolean result = false;

		for (int k = 0; k < this.topbottomFilters.size(); k++) {
			TopBottomFilterDefinition filterDefinition = (TopBottomFilterDefinition) topbottomFilters.get(k);
			if (isMatchResultSet(rs.getAggregationDefinition(), rs, filterDefinition)) {
				return true;
			}
		}

		for (int k = 0; k < this.aggrFilters.size(); k++) {
			AggrFilterDefinition filterDefinition = ((AggrFilterDefinition) aggrFilters.get(k));
			if (isMatchResultSet(rs.getAggregationDefinition(), rs, filterDefinition)) {
				return true;
			}
		}

		return result;
	}

	private List populateDistinctLevelKeyList(AggregationDefinition aggregation, IAggregationResultSet resultSet,
			TopBottomFilterDefinition filter) throws DataException {
		IJSTopBottomFilterHelper filterHelper = (IJSTopBottomFilterHelper) filter.getFilterHelper();
		int n = -1;
		if (!filterHelper.isPercent()) {
			n = (int) filterHelper.getN();
		}

		IDiskArray aggrValueArray = new OrderedDiskArray(n, filterHelper.isTop());

		String dimensionName = filter.getTargetLevel().getDimensionName();
		Object preValue = null;
		try {
			AggregationRowAccessor row4filter = new AggregationRowAccessor(resultSet, fetcher);
			for (int k = 0; k < resultSet.length(); k++) {
				resultSet.seek(k);
				int levelIndex = resultSet.getLevelIndex(filter.getTargetLevel());
				Object[] levelKey = resultSet.getLevelKeyValue(levelIndex);
				Object aggrValue = filterHelper.evaluateFilterExpr(row4filter);
				if (levelKey != null && filterHelper.isQualifiedRow(row4filter)
						&& (CompareUtil.compare(preValue, aggrValue) != 0)) {
					aggrValueArray.add(aggrValue);
				}
				preValue = aggrValue;
			}
			return fetchDistictLevelKeys(aggrValueArray, filterHelper);
		} catch (IOException e) {
			throw new DataException("", e);//$NON-NLS-1$
		}

	}

	private List fetchDistictLevelKeys(IDiskArray aggrValueArray, IJSTopBottomFilterHelper filterHelper)
			throws IOException {
		int start = 0; // level key start index in aggrValueArray
		int end = aggrValueArray.size(); // level key end index (not
		// including) in aggrValueArray
		if (filterHelper.isPercent()) {// top/bottom percentage filter
			int size = aggrValueArray.size(); // target level member size
			int n = FilterUtil.getTargetN(size, filterHelper.getN());
			if (filterHelper.isTop()) {
				start = size - n;
			} else {
				end = n;
			}
		}
		List resultList = new ArrayList();
		for (int i = start; i < end; i++) {
			Object aggrValue = aggrValueArray.get(i);
			resultList.add(aggrValue);
		}
		return resultList;
	}

	public IAggregationResultSet[] generateFilteredAggregationResultSet(IAggregationResultSet[] rs,
			List<Integer> affectedAggrResultSetIndex) throws IOException, DataException {
		IAggregationResultSet[] result = new IAggregationResultSet[rs.length];
		List levelFilterList = new ArrayList();
		for (int i = 0; i < rs.length; i++) {
			if (rs[i].getAggregationDefinition().getAggregationFunctions() == null
					&& hasFiltersOnEdgeAggrResultSet(rs[i])) {
				AggregationRowAccessor row4filter = new AggregationRowAccessor(rs[i], fetcher);
				IDiskArray validRows = new BufferedStructureArray(AggregationResultRow.getCreator(), rs[i].length());
				List[] topBottomNFilterResultList = null;
				int[] targetLevelIndex = null;
				if (this.topbottomFilters.size() > 0) {
					topBottomNFilterResultList = new List[this.topbottomFilters.size()];
					targetLevelIndex = new int[this.topbottomFilters.size()];
				}

				for (int k = 0; k < this.topbottomFilters.size(); k++) {
					IJSTopBottomFilterHelper filterHelper = (IJSTopBottomFilterHelper) ((TopBottomFilterDefinition) topbottomFilters
							.get(k)).getFilterHelper();
					topBottomNFilterResultList[k] = populateDistinctLevelKeyList(rs[i].getAggregationDefinition(),
							rs[i], (TopBottomFilterDefinition) topbottomFilters.get(k));
					targetLevelIndex[k] = rs[i]
							.getLevelIndex(((TopBottomFilterDefinition) topbottomFilters.get(k)).getTargetLevel());
				}

				for (int j = 0; j < rs[i].length(); j++) {
					rs[i].seek(j);
					boolean isFilterByAll = true;

					for (int k = 0; k < this.aggrFilters.size(); k++) {
						IJSDimensionFilterHelper filterHelper = (IJSDimensionFilterHelper) ((AggrFilterDefinition) aggrFilters
								.get(k)).getFilterHelper();
						if (isMatch(rs[i].getAggregationDefinition(), rs[i], (AggrFilterDefinition) aggrFilters.get(k))
								&& (!filterHelper.evaluateFilter(row4filter))) {
							isFilterByAll = false;
							break;
						}
					}

					for (int k = 0; k < this.topbottomFilters.size(); k++) {
						IAggregationResultRow currentRow = rs[i].getCurrentRow();
						if (targetLevelIndex[k] >= 0) {
							Member m = currentRow.getLevelMembers()[targetLevelIndex[k]];
							if (!topBottomNFilterResultList[k].contains(m.getKeyValues()[0])) {
								isFilterByAll = false;
								break;
							}
						}
					}

					if (isFilterByAll) {
						validRows.add(rs[i].getCurrentRow());
					}
				}

				IAggregationResultSet newAggrResultSet = new AggregationResultSet(rs[i].getAggregationDefinition(),
						rs[i].getAllLevels(), validRows, rs[i].getKeyNames(), rs[i].getAttributeNames());
				result[i] = newAggrResultSet;
			} else {
				boolean filtered = false;
				for (Iterator k = aggrFilters.iterator(); k.hasNext();) {
					AggrFilterDefinition filter = (AggrFilterDefinition) k.next();
					if (rs[i].getAggregationDefinition().getAggregationFunctions() != null
							&& isMatch(rs[i].getAggregationDefinition(), rs[i], filter)
							&& filter.getAggrLevels() != null) {
						applyNoUpdateAggrFilter(rs[i], filter, levelFilterList);
						filtered = true;
					}
				}

				for (Iterator k = topbottomFilters.iterator(); k.hasNext();) {
					TopBottomFilterDefinition filter = (TopBottomFilterDefinition) k.next();
					if (rs[i].getAggregationDefinition().getAggregationFunctions() != null
							&& (isMatch(rs[i].getAggregationDefinition(), rs[i], filter)
									&& filter.getAggrLevels() != null)) {
						applyNoUpdateTopBottomFilters(rs[i].getAggregationDefinition(), rs[i], levelFilterList);
						filtered = true;
					}
				}

				if (filtered && levelFilterList.size() > 0) {
					IDiskArray validRows = new BufferedStructureArray(AggregationResultRow.getCreator(),
							rs[i].length());
					for (int k = 0; k < levelFilterList.size(); k++) {
						LevelFilter f = (LevelFilter) levelFilterList.get(k);
						ISelection[] selections = f.getSelections();

						for (int p = 0; p < selections.length; p++) {
							ISelection select = selections[p];
							DimLevel dim = new DimLevel(f.getDimensionName(), f.getLevelName());
							int levelIndex = rs[i].getLevelIndex(dim);
							if (levelIndex >= 0) {
								for (int m = 0; m < rs[i].length(); m++) {
									rs[i].seek(m);
									Object[] obj = rs[i].getCurrentRow().getLevelMembers()[levelIndex].getKeyValues();
									if (select.isSelected(obj)) {
										validRows.add(rs[i].getCurrentRow());
									}
								}
							}
						}
					}
					IAggregationResultSet newAggrResultSet = new AggregationResultSet(rs[i].getAggregationDefinition(),
							rs[i].getAllLevels(), validRows, rs[i].getKeyNames(), rs[i].getAttributeNames());
					result[i] = newAggrResultSet;
					affectedAggrResultSetIndex.add(i);
				} else if (filtered && levelFilterList.size() == 0) {
					IAggregationResultSet newAggrResultSet = new AggregationResultSet(rs[i].getAggregationDefinition(),
							rs[i].getAllLevels(),
							new BufferedStructureArray(AggregationResultRow.getCreator(), rs[i].length()),
							rs[i].getKeyNames(), rs[i].getAttributeNames());
					result[i] = newAggrResultSet;
					affectedAggrResultSetIndex.add(i);
				} else {
					result[i] = rs[i];
				}
			}
		}

		return result;
	}

	/**
	 * get the members of the specified dimension from the aggregation result set.
	 * Note: only the members of the levels which reside in the result set will be
	 * fetched, otherwise the corresponding member value is null.
	 *
	 *
	 * @param dimensionName
	 * @param resultSet
	 * @return
	 */
	private Member[] getTargetDimMembers(String dimensionName, IAggregationResultSet resultSet) {
		ILevel[] levels = getLevelsOfDimension(dimensionName);
		Member[] members = new Member[levels.length];
		for (int i = 0; i < levels.length; i++) {
			int levelIndex = resultSet.getLevelIndex(new DimLevel(dimensionName, levels[i].getName()));
			if (levelIndex >= 0) {
				Object[] values = resultSet.getLevelKeyValue(levelIndex);
				Object[] fieldValues = ObjectArrayUtil.convert(new Object[][] { values, null });
				members[i] = (Member) Member.getCreator().createInstance(fieldValues);
			}
		}
		return members;
	}

	/**
	 * @param targetLevel
	 * @param selKeyValueList
	 * @param dimMembers
	 * @param filterHelper
	 * @return
	 */
	private LevelFilter toLevelFilter(DimLevel targetLevel, List selKeyValueList, Member[] dimMembers,
			IJSFilterHelper filterHelper) {
		Object[][] keyValues = new Object[selKeyValueList.size()][];
		for (int i = 0; i < selKeyValueList.size(); i++) {
			keyValues[i] = (Object[]) selKeyValueList.get(i);
		}
		ISelection selection = SelectionFactory.createMutiKeySelection(keyValues);
		LevelFilter levelFilter = new LevelFilter(targetLevel, new ISelection[] { selection });
		levelFilter.setDimMembers(dimMembers);
		levelFilter.setFilterHelper(filterHelper);
		return levelFilter;
	}

	private void applyNoUpdateTopBottomFilters(AggregationDefinition aggregation, IAggregationResultSet resultSet,
			List levelFilterList) throws DataException, IOException {
		if (aggregation.getAggregationFunctions() == null) {
			return;
		}
		Map levelFilterMap = new HashMap();
		for (Iterator j = topbottomFilters.iterator(); j.hasNext();) {
			TopBottomFilterDefinition filter = (TopBottomFilterDefinition) j.next();
			if (filter.getFilterHelper().isAggregationFilter()) {
				if (FilterUtil.isEqualLevels(aggregation.getLevels(), filter.getAggrLevels())) {
					IDiskArray levelKeyList = populateNoUpdateLevelKeyList(aggregation, resultSet, filter);
					IDiskArray selectedLevelKeys = null;
					if (levelFilterMap.containsKey(filter.getTargetLevel())) {
						Object[] valueObjs = (Object[]) levelFilterMap.get(filter.getTargetLevel());
						selectedLevelKeys = (IDiskArray) valueObjs[0];
						selectedLevelKeys = SetUtil.getIntersection(selectedLevelKeys, levelKeyList);
					} else {
						selectedLevelKeys = levelKeyList;
					}
					levelFilterMap.put(filter.getTargetLevel(),
							new Object[] { selectedLevelKeys, filter.getFilterHelper() });
				}
			}
		}
		// generate level filters according to the selected level keys
		for (Iterator j = levelFilterMap.keySet().iterator(); j.hasNext();) {
			DimLevel target = (DimLevel) j.next();
			Object[] valueObjs = (Object[]) levelFilterMap.get(target);
			IDiskArray selectedKeyArray = (IDiskArray) valueObjs[0];
			IJSFilterHelper filterHelper = (IJSFilterHelper) valueObjs[1];
			if (selectedKeyArray.size() == 0) {
				continue;
			}

			int index = resultSet.getLevelIndex(target);
			Map keyMap = new HashMap();
			for (int k = 0; k < selectedKeyArray.size(); k++) {
				MultiKey multiKey = (MultiKey) selectedKeyArray.get(k);
				String parentKey = getParentKey(multiKey.dimMembers, index);
				List keyList = (List) keyMap.get(parentKey);
				if (keyList == null) {
					keyList = new ArrayList();
					keyMap.put(parentKey, keyList);
				}
				keyList.add(multiKey);
			}
			for (Iterator keyItr = keyMap.values().iterator(); keyItr.hasNext();) {
				List keyList = (List) keyItr.next();
				ISelection selections = toMultiKeySelection(keyList);
				LevelFilter levelFilter = new LevelFilter(target, new ISelection[] { selections });
				// use the first key's dimension members since all them
				// share same parent levels (with the same parent key)
				levelFilter.setDimMembers(((MultiKey) keyList.get(0)).dimMembers);
				levelFilter.setFilterHelper(filterHelper);
				levelFilterList.add(levelFilter);
			}
		}
	}

	/**
	 *
	 * @param aggregations
	 * @param resultSet
	 * @param levelFilterList
	 * @throws IOException
	 * @throws DataException
	 * @throws IOException
	 */
	private void applyTopBottomFilters(AggregationDefinition[] aggregations, IAggregationResultSet[] resultSet,
			List levelFilterList) throws DataException, IOException {
		for (int i = 0; i < aggregations.length; i++) {
			if (aggregations[i].getAggregationFunctions() == null) {
				continue;
			}
			Map levelFilterMap = new HashMap();
			for (Iterator j = topbottomFilters.iterator(); j.hasNext();) {
				TopBottomFilterDefinition filter = (TopBottomFilterDefinition) j.next();
				if (filter.getFilterHelper().isAggregationFilter()) {// aggregation top/bottom filter
					if (FilterUtil.isEqualLevels(aggregations[i].getLevels(), filter.getAggrLevels())) {
						IDiskArray levelKeyList = populateLevelKeyList(aggregations[i], resultSet[i], filter);
						IDiskArray selectedLevelKeys = null;
						if (levelFilterMap.containsKey(filter.getTargetLevel())) {
							Object[] valueObjs = (Object[]) levelFilterMap.get(filter.getTargetLevel());
							selectedLevelKeys = (IDiskArray) valueObjs[0];
							selectedLevelKeys = SetUtil.getIntersection(selectedLevelKeys, levelKeyList);
						} else {
							selectedLevelKeys = levelKeyList;
						}
						levelFilterMap.put(filter.getTargetLevel(),
								new Object[] { selectedLevelKeys, filter.getFilterHelper() });
					}
				}
			}
			// generate level filters according to the selected level keys
			for (Iterator j = levelFilterMap.keySet().iterator(); j.hasNext();) {
				DimLevel target = (DimLevel) j.next();
				Object[] valueObjs = (Object[]) levelFilterMap.get(target);
				IDiskArray selectedKeyArray = (IDiskArray) valueObjs[0];
				IJSFilterHelper filterHelper = (IJSFilterHelper) valueObjs[1];
				if (selectedKeyArray.size() == 0) {
					continue;
				}
				ILevel[] levels = getLevelsOfDimension(target.getDimensionName());
				int index = FilterUtil.getTargetLevelIndex(levels, target.getLevelName());
				Map keyMap = new HashMap();
				for (int k = 0; k < selectedKeyArray.size(); k++) {
					MultiKey multiKey = (MultiKey) selectedKeyArray.get(k);
					String parentKey = getParentKey(multiKey.dimMembers, index);
					List keyList = (List) keyMap.get(parentKey);
					if (keyList == null) {
						keyList = new ArrayList();
						keyMap.put(parentKey, keyList);
					}
					keyList.add(multiKey);
				}
				for (Iterator keyItr = keyMap.values().iterator(); keyItr.hasNext();) {
					List keyList = (List) keyItr.next();
					ISelection selections = toMultiKeySelection(keyList);
					LevelFilter levelFilter = new LevelFilter(target, new ISelection[] { selections });
					// use the first key's dimension members since all them
					// share same parent levels (with the same parent key)
					levelFilter.setDimMembers(((MultiKey) keyList.get(0)).dimMembers);
					levelFilter.setFilterHelper(filterHelper);
					levelFilterList.add(levelFilter);
				}
			}
		}
	}

	/**
	 *
	 * @param dimensionName
	 * @return
	 */
	private ILevel[] getLevelsOfDimension(String dimensionName) {
		return (ILevel[]) dimensionMap.get(dimensionName);
	}

	/**
	 *
	 * @param aggregation
	 * @param resultSet
	 * @param filter
	 * @param levelFilters
	 * @return
	 */
	private IDiskArray populateLevelKeyList(AggregationDefinition aggregation, IAggregationResultSet resultSet,
			TopBottomFilterDefinition filter) throws DataException {
		IJSTopBottomFilterHelper filterHelper = (IJSTopBottomFilterHelper) filter.getFilterHelper();
		int n = -1;
		if (!filterHelper.isPercent()) {
			n = (int) filterHelper.getN();
		}

		IDiskArray aggrValueArray = new OrderedDiskArray(n, filterHelper.isTop());

		String dimensionName = filter.getTargetLevel().getDimensionName();
		try {
			AggregationRowAccessor row4filter = new AggregationRowAccessor(resultSet, fetcher);
			for (int k = 0; k < resultSet.length(); k++) {
				resultSet.seek(k);
				int levelIndex = resultSet.getLevelIndex(filter.getTargetLevel());
				Object[] levelKey = resultSet.getLevelKeyValue(levelIndex);
				if (levelKey != null && filterHelper.isQualifiedRow(row4filter)) {
					Object aggrValue = filterHelper.evaluateFilterExpr(row4filter);
					Member[] members = getTargetDimMembers(dimensionName, resultSet);
					aggrValueArray.add(new ValueObject(aggrValue, new MultiKey(levelKey, members)));
				}
			}
			return fetchLevelKeys(aggrValueArray, filterHelper);
		} catch (IOException e) {
			throw new DataException("", e);//$NON-NLS-1$
		}

	}

	private IDiskArray populateNoUpdateLevelKeyList(AggregationDefinition aggregation, IAggregationResultSet resultSet,
			TopBottomFilterDefinition filter) throws DataException {
		IJSTopBottomFilterHelper filterHelper = (IJSTopBottomFilterHelper) filter.getFilterHelper();
		int n = -1;
		if (!filterHelper.isPercent()) {
			n = (int) filterHelper.getN();
		}

		IDiskArray aggrValueArray = new OrderedDiskArray(n, filterHelper.isTop());

		String dimensionName = filter.getTargetLevel().getDimensionName();
		try {
			AggregationRowAccessor row4filter = new AggregationRowAccessor(resultSet, fetcher);
			for (int k = 0; k < resultSet.length(); k++) {
				resultSet.seek(k);
				int levelIndex = resultSet.getLevelIndex(filter.getTargetLevel());
				Object[] levelKey = resultSet.getLevelKeyValue(levelIndex);
				if (levelKey != null && filterHelper.isQualifiedRow(row4filter)) {
					Object aggrValue = filterHelper.evaluateFilterExpr(row4filter);
					Member[] members = resultSet.getCurrentRow().getLevelMembers();
					aggrValueArray.add(new ValueObject(aggrValue, new MultiKey(levelKey, members)));
				}
			}
			return fetchLevelKeys(aggrValueArray, filterHelper);
		} catch (IOException e) {
			throw new DataException("", e);//$NON-NLS-1$
		}

	}

	/**
	 * @param aggrValueArray
	 * @param filterHelper
	 * @return
	 * @throws IOException
	 */
	private IDiskArray fetchLevelKeys(IDiskArray aggrValueArray, IJSTopBottomFilterHelper filterHelper)
			throws IOException {
		int start = 0; // level key start index in aggrValueArray
		int end = aggrValueArray.size(); // level key end index (not
		// including) in aggrValueArray
		if (filterHelper.isPercent()) {// top/bottom percentage filter
			int size = aggrValueArray.size(); // target level member size
			int n = FilterUtil.getTargetN(size, filterHelper.getN());
			if (filterHelper.isTop()) {
				start = size - n;
			} else {
				end = n;
			}
		}
		IDiskArray levelKeyArray = new BufferedPrimitiveDiskArray(
				Math.min((end - start + 1), Constants.LIST_BUFFER_SIZE));
		for (int i = start; i < end; i++) {
			ValueObject aggrValue = (ValueObject) aggrValueArray.get(i);
			levelKeyArray.add(aggrValue.index);
		}
		return levelKeyArray;
	}

	/**
	 *
	 * @param members
	 * @param index
	 * @return
	 */
	private String getParentKey(Member[] members, int index) {
		assert index >= 0 && index < members.length;
		StringBuilder buf = new StringBuilder();
		for (int i = 0; i < index; i++) {
			if (members[i] == null) {
				buf.append('?');
			} else {
				Object[] keyValues = members[i].getKeyValues();
				if (keyValues != null && keyValues.length > 0) {
					for (int j = 0; j < keyValues.length; j++) {
						buf.append(keyValues[j].toString());
						buf.append(',');
					}
					buf.deleteCharAt(buf.length() - 1);
				}
			}
			buf.append('-');
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	/**
	 * @param keyList
	 * @return
	 */
	private ISelection toMultiKeySelection(List keyList) {
		Object[][] keys = new Object[keyList.size()][];
		for (int i = 0; i < keyList.size(); i++) {
			MultiKey multiKey = (MultiKey) keyList.get(i);
			keys[i] = multiKey.values;
		}
		return SelectionFactory.createMutiKeySelection(keys);
	}
}

/**
 *
 */
class MultiKey implements Comparable {

	Object[] values;
	Member[] dimMembers; // dimension members associate with this level key

	MultiKey(Object[] values, Member[] dimensionMembers) {
		this.values = values;
		this.dimMembers = dimensionMembers;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Comparable#compareTo(T)
	 */
	@Override
	public int compareTo(Object obj) {
		if (obj == null) {
			return -1;
		} else if (obj instanceof MultiKey) {
			MultiKey key = (MultiKey) obj;
			return CompareUtil.compare(values, key.values);
		}
		return -1;
	}

	/**
	 * @return the dimensionMembers
	 */
	Member[] getDimMembers() {
		return dimMembers;
	}

	/**
	 * @param dimensionMembers the dimensionMembers to set
	 */
	void setDimMembers(Member[] dimensionMembers) {
		this.dimMembers = dimensionMembers;
	}
}

/**
 *
 *
 */
class AggrFilterDefinition {

	protected DimLevel[] aggrLevels;
	protected IJSFilterHelper filterHelper;
	protected DimLevel targetLevel;
	protected DimLevel[] axisQualifierLevels;
	protected Object[] axisQualifierValues;

	AggrFilterDefinition(IJSFilterHelper filterEvalHelper) {
		filterHelper = filterEvalHelper;
		ICubeFilterDefinition cubeFilter = filterEvalHelper.getCubeFilterDefinition();
		if (cubeFilter.getTargetLevel() != null) {
			targetLevel = new DimLevel(cubeFilter.getTargetLevel());
		}
		aggrLevels = filterEvalHelper.getAggrLevels();
		ILevelDefinition[] axisLevels = cubeFilter.getAxisQualifierLevels();
		if (axisLevels != null) {
			axisQualifierLevels = new DimLevel[axisLevels.length];
			for (int i = 0; i < axisLevels.length; i++) {
				axisQualifierLevels[i] = new DimLevel(axisLevels[i]);
			}
		}
		axisQualifierValues = cubeFilter.getAxisQualifierValues();
	}

	/**
	 * @return the axisQualifierLevelNames
	 */
	DimLevel[] getAxisQualifierLevels() {
		return axisQualifierLevels;
	}

	/**
	 * @return the axisQualifierLevelValues
	 */
	Object[] getAxisQualifierValues() {
		return axisQualifierValues;
	}

	/**
	 * @return the aggrLevels
	 */
	DimLevel[] getAggrLevels() {
		return aggrLevels;
	}

	/**
	 * @return the aggrFilter
	 */
	IJSFilterHelper getFilterHelper() {
		return filterHelper;
	}

	/**
	 * @return the targetLevel
	 */
	DimLevel getTargetLevel() {
		return targetLevel;
	}
}

/**
 *
 */
class TopBottomFilterDefinition extends AggrFilterDefinition {

	double n;
	int filterType;

	/**
	 *
	 * @param filterHelper
	 */
	TopBottomFilterDefinition(IJSFilterHelper filterHelper) {
		super(filterHelper);
		this.filterHelper = filterHelper;
		IJSTopBottomFilterHelper topBottomFilterHelper = ((IJSTopBottomFilterHelper) filterHelper);
		this.filterType = topBottomFilterHelper.getFilterType();
		this.n = topBottomFilterHelper.getN();
	}

	/**
	 * @return the n, which will be greater than zero.
	 */
	double getN() {
		return n;
	}

	/**
	 * @return the filterType
	 */
	int getFilterType() {
		return filterType;
	}
}
