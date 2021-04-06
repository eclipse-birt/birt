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

package org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.cache.Constants;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IBindingValueFetcher;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.AggregationRowAccessor;
import org.eclipse.birt.data.engine.olap.data.util.BufferedPrimitiveDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.CompareUtil;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.util.sort.IJSSortHelper;

/**
 * Helper class to sort on aggregations.
 */

public class AggregationSortHelper {

	/**
	 * 
	 * @param base
	 * @param targetSorts
	 * @param targetResultSets
	 * @return
	 */
	public static IAggregationResultSet sort(IAggregationResultSet base, ITargetSort[] targetSorts,
			IAggregationResultSet[] targetResultSets, IBindingValueFetcher fetcher) throws IOException, DataException {
		IDiskArray baseDiskArray = getRowsFromBaseResultSet(base);
		IDiskArray[] keyDiskArrays = populateKeyDiskArray(base, targetSorts, targetResultSets, fetcher);
		CompareUtil.sort(new WrapperedDiskArray(baseDiskArray, keyDiskArrays),
				new AggrResultRowComparator(base, targetSorts), AggregationResultRow.getCreator());
		releaseDiskArrays(keyDiskArrays);
		return new AggregationResultSet(base.getAggregationDefinition(), baseDiskArray, base.getKeyNames(),
				base.getAttributeNames());
	}

	/**
	 * release unnecessary disk arrays.
	 * 
	 * @param keyDiskArrays
	 * @throws IOException
	 */
	private static void releaseDiskArrays(IDiskArray[] keyDiskArrays) throws IOException {
		for (int i = 0; i < keyDiskArrays.length; i++) {
			keyDiskArrays[i].close();
		}
	}

	/**
	 * 
	 * @param base
	 * @return
	 * @throws IOException
	 */
	private static IDiskArray getRowsFromBaseResultSet(IAggregationResultSet base) throws IOException {
		int bufferSize = 4096;
		if (Constants.isAggressiveMemoryUsage()) {
			bufferSize = base.length();
		}
		IDiskArray diskArray = new BufferedStructureArray(AggregationResultRow.getCreator(), bufferSize);
		for (int j = 0; j < base.length(); j++) {
			base.seek(j);
			IAggregationResultRow temp = base.getCurrentRow();
			diskArray.add(temp);
		}
		return diskArray;
	}

	/**
	 * 
	 * @param base
	 * @param targetSorts
	 * @param targetResultSets
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	private static IDiskArray[] populateKeyDiskArray(IAggregationResultSet base, ITargetSort[] targetSorts,
			IAggregationResultSet[] targetResultSets, IBindingValueFetcher fetcher) throws DataException, IOException {
		IDiskArray[] keyDiskArrays = new IDiskArray[targetSorts.length];
		for (int i = 0; i < keyDiskArrays.length; i++) {
			keyDiskArrays[i] = new BufferedPrimitiveDiskArray();
		}
		// classify the target sorts according to their target level
		Map indexMap = new HashMap();
		List sortHelperIndex = new ArrayList();
		for (int i = 0; i < targetSorts.length; i++) {
			if (targetSorts[i] instanceof AggrSortDefinition) {
				DimLevel targetLevel = targetSorts[i].getTargetLevel();
				List sortDefnIndex = (List) indexMap.get(targetLevel);
				if (sortDefnIndex == null) {
					sortDefnIndex = new ArrayList();
					indexMap.put(targetLevel, sortDefnIndex);
				}
				sortDefnIndex.add(Integer.valueOf(i));
			} else {
				sortHelperIndex.add(Integer.valueOf(i));
			}
		}

		// populate the key values from the aggregation result set.
		for (Iterator itr = indexMap.values().iterator(); itr.hasNext();) {
			final int[] sortIndex = toIntArray((List) itr.next());
			if (sortIndex.length > 0) {
				populateAggrKeysForTargetLevel(base, sortIndex, targetSorts, targetResultSets, keyDiskArrays);
			}
		}

		// populate the key values evaluated by the expression helpers
		final int[] sortIndex = toIntArray(sortHelperIndex);
		if (sortIndex.length > 0) {
			populateExprKeyDiskArray(base, targetSorts, sortIndex, keyDiskArrays, fetcher);
		}
		return keyDiskArrays;
	}

	/**
	 * populate aggregation values to disk arrays for the specified
	 * AggrSortDefinitions who share the same target level.
	 * 
	 * @param base
	 * @param sortIndex
	 * @param targetSorts
	 * @param targetResultSets
	 * @param keyDiskArrays
	 * @throws IOException
	 */
	private static void populateAggrKeysForTargetLevel(IAggregationResultSet base, int[] sortIndex,
			ITargetSort[] targetSorts, IAggregationResultSet[] targetResultSets, IDiskArray[] keyDiskArrays)
			throws IOException {
		// all the target sorts located with sortIndex shared the same
		// target level, but they should be classified according to their target
		// result set since they may be defined in different aggregation levels
		Map map = new HashMap();
		for (int i = 0; i < sortIndex.length; i++) {
			int index = sortIndex[i];
			List list = (List) map.get(targetResultSets[index]);
			if (list == null) {
				list = new ArrayList();
				map.put(targetResultSets[index], list);
			}
			list.add(Integer.valueOf(index));
		}
		//
		for (Iterator itr = map.keySet().iterator(); itr.hasNext();) {
			IAggregationResultSet resultSet = (IAggregationResultSet) itr.next();
			int[] targetSortIndex = toIntArray((List) map.get(resultSet));
			populateAggrKeysForTargetResultSet(base, resultSet, targetSortIndex, targetSorts, keyDiskArrays);
		}
	}

	/**
	 * aggregation values to disk arrays for the specified AggrSortDefinitions who
	 * share the same target aggregation result set.
	 * 
	 * @param base
	 * @param targetResultSet
	 * @param sortIndex
	 * @param targetSorts
	 * @param keyDiskArrays
	 * @throws IOException
	 */
	private static void populateAggrKeysForTargetResultSet(IAggregationResultSet base,
			IAggregationResultSet targetResultSet, int[] sortIndex, ITargetSort[] targetSorts,
			IDiskArray[] keyDiskArrays) throws IOException {
		AggrSortDefinition sortDefinition = (AggrSortDefinition) targetSorts[sortIndex[0]];
		DimLevel targetLevel = sortDefinition.getTargetLevel();

		final AggregationDefinition aggrDefinition = targetResultSet.getAggregationDefinition();
		if (aggrDefinition == null || aggrDefinition.getAggregationFunctions() == null) {// populate dimension level key
																							// values
			int levelIndex = targetResultSet.getLevelIndex(targetLevel);
			for (int i = 0; i < base.length(); i++) {
				base.seek(i);
				Object key = base.getLevelKeyValue(levelIndex)[0];
				for (int j = 0; j < sortIndex.length; j++) {
					keyDiskArrays[sortIndex[j]].add(key);
				}
			}
			return;
		}
		// populate aggregation values
		DimLevel[] axisQualifierLevel = sortDefinition.getAxisQualifierLevel();
		int[] levelIndex = new int[axisQualifierLevel.length];
		for (int i = 0; i < levelIndex.length; i++) {
			levelIndex[i] = targetResultSet.getLevelIndex(axisQualifierLevel[i]);
		}
		Object[] axisQualifierValue = sortDefinition.getAxisQualifierValue();
		int[] aggrIndex = new int[sortIndex.length];
		for (int i = 0; i < sortIndex.length; i++) {
			AggrSortDefinition sortDefn = (AggrSortDefinition) targetSorts[sortIndex[i]];
			aggrIndex[i] = targetResultSet.getAggregationIndex(sortDefn.getAggrName());
		}

		int indexInBase = base.getLevelIndex(targetLevel);

		CompareIndex compareIndex = getCompareIndex(base, targetResultSet, indexInBase);

		int baseRowIndex = 0;
		for (int i = 0; i < targetResultSet.length(); i++) {
			targetResultSet.seek(i);
			Object[] values = new Object[levelIndex.length];
			for (int j = 0; j < levelIndex.length; j++) {
				if (levelIndex[j] == -1)
					values[j] = axisQualifierValue[j];
				else
					values[j] = targetResultSet.getLevelKeyValue(levelIndex[j])[0];
			}

			if (CompareUtil.compare(values, axisQualifierValue) == 0) {
				IAggregationResultRow targetRow = targetResultSet.getCurrentRow();
				boolean found = false;
				while (baseRowIndex < base.length()) {
					base.seek(baseRowIndex);
					IAggregationResultRow baseRow = base.getCurrentRow();
					if (shareLevelKey(baseRow, targetRow, compareIndex)) {
						for (int j = 0; j < sortIndex.length; j++) {
							keyDiskArrays[sortIndex[j]].add(targetRow.getAggregationValues()[aggrIndex[j]]);
						}
						baseRowIndex++;
						found = true;
					} else if (!found) {
						fillNullValues(sortIndex, keyDiskArrays);
						baseRowIndex++;
					} else {
						break;
					}
				}
			}
		}

		for (; baseRowIndex < base.length(); baseRowIndex++) {
			fillNullValues(sortIndex, keyDiskArrays);
		}
	}

	/**
	 * 
	 * @param base
	 * @param targetResultSet
	 * @param indexInBase
	 * @return
	 */
	private static CompareIndex getCompareIndex(IAggregationResultSet base, IAggregationResultSet targetResultSet,
			int indexInBase) {
		DimLevel[] baseLevels = base.getAllLevels();
		DimLevel[] targetAllLevels = targetResultSet.getAllLevels();
		List baseMemberIndex = new ArrayList();
		List targetMemberIndex = new ArrayList();
		for (int i = 0; i < targetAllLevels.length; i++) {
			for (int j = 0; j <= indexInBase; j++) {
				if (baseLevels[j].equals(targetAllLevels[i])) {
					baseMemberIndex.add(Integer.valueOf(j));
					targetMemberIndex.add(Integer.valueOf(i));
					break;
				}
			}
		}
		CompareIndex compareIndex = new CompareIndex();
		compareIndex.memberIndex1 = toIntArray(baseMemberIndex);
		compareIndex.memberIndex2 = toIntArray(targetMemberIndex);
		return compareIndex;
	}

	/**
	 * 
	 * @param currentRow
	 * @param targetRow
	 * @param compareIndex
	 * @return
	 */
	private static boolean shareLevelKey(IAggregationResultRow currentRow, IAggregationResultRow targetRow,
			CompareIndex compareIndex) {
		final int[] index1 = compareIndex.memberIndex1;
		final int[] index2 = compareIndex.memberIndex2;
		for (int i = 0; i < index1.length; i++) {
			int ret = currentRow.getLevelMembers()[index1[i]].compareTo(targetRow.getLevelMembers()[index2[i]]);
			if (ret != 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param sortIndex
	 * @param keyDiskArrays
	 * @throws IOException
	 */
	private static void fillNullValues(int[] sortIndex, IDiskArray[] keyDiskArrays) throws IOException {
		for (int j = 0; j < sortIndex.length; j++) {
			keyDiskArrays[sortIndex[j]].add(null);
		}
	}

	/**
	 * 
	 * @param list
	 * @return
	 */
	private static int[] toIntArray(List list) {
		int[] index = new int[list.size()];
		for (int i = 0; i < index.length; i++) {
			index[i] = ((Integer) list.get(i)).intValue();
		}
		return index;
	}

	/**
	 * 
	 * @param base
	 * @param targetSorts
	 * @param sortHelperIndex
	 * @param keyDiskArrays
	 * @throws IOException
	 * @throws DataException
	 */
	private static void populateExprKeyDiskArray(IAggregationResultSet base, ITargetSort[] targetSorts,
			int[] sortHelperIndex, IDiskArray[] keyDiskArrays, IBindingValueFetcher fetcher)
			throws IOException, DataException {
		AggregationRowAccessor rowAccessor = new AggregationRowAccessor(base, fetcher);
		for (int i = 0; i < base.length(); i++) {
			base.seek(i);
			for (int j = 0; j < sortHelperIndex.length; j++) {
				final int index = sortHelperIndex[j];
				IJSSortHelper sortHelper = (IJSSortHelper) targetSorts[index];
				Object keyValue = sortHelper.evaluate(rowAccessor);
				keyDiskArrays[index].add(keyValue);
			}
		}
	}
}

/**
 *
 */
class CompareIndex {
	int[] memberIndex1;
	int[] memberIndex2;
}

/**
 * 
 * 
 */
class WrapperedDiskArray implements IDiskArray {

	private int index;
	private IDiskArray baseArray;
	private IDiskArray[] keyValueArrays;

	/**
	 * 
	 * @param base
	 * @param keyValueArrays
	 */
	WrapperedDiskArray(IDiskArray base, IDiskArray[] keyValueArrays) {
		this.baseArray = base;
		this.keyValueArrays = keyValueArrays;
		this.index = 0;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private Object getCurrentBaseRow() throws IOException {
		return this.baseArray.get(this.index);
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private Object[] getCurrentKeyRow() throws IOException {
		Object[] keys = new Object[keyValueArrays.length];
		for (int i = 0; i < keyValueArrays.length; i++) {
			keys[i] = keyValueArrays[i].get(index);
		}
		return keys;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.util.IDiskArray#add(java.lang.Object)
	 */
	public boolean add(Object o) throws IOException {
		assert o instanceof AggregationResultRow;
		AggregationResultRow obj = (AggregationResultRow) o;
		AggregationResultRow baseRow = new AggregationResultRow();
		baseRow.setLevelMembers(obj.getLevelMembers());
		this.baseArray.add(baseRow);
		Object[] aggrValues = obj.getAggregationValues();
		for (int i = 0; i < keyValueArrays.length; i++) {
			keyValueArrays[i].add(aggrValues[i]);
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.util.IDiskArray#clear()
	 */
	public void clear() throws IOException {
		this.baseArray.clear();
		for (int i = 0; i < keyValueArrays.length; i++) {
			keyValueArrays[i].clear();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.util.IDiskArray#close()
	 */
	public void close() throws IOException {
		this.baseArray.close();
		for (int i = 0; i < keyValueArrays.length; i++) {
			keyValueArrays[i].close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.util.IDiskArray#get(int)
	 */
	public Object get(int index) throws IOException {
		this.index = index;
		return new AggregationResultRow(((IAggregationResultRow) this.getCurrentBaseRow()).getLevelMembers(),
				getCurrentKeyRow());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.util.IDiskArray#size()
	 */
	public int size() {
		return this.baseArray.size();
	}
}

/**
 *
 */
class AggrResultRowComparator implements Comparator {

	private int[] valueIndexs;
	private boolean[] sortDirections;

	/**
	 * 
	 * @param base
	 * @param targetSorts
	 */
	public AggrResultRowComparator(IAggregationResultSet base, ITargetSort[] targetSorts) {
		// The keys for sorting, which should include every level key in the
		// base result set and all the aggregation/evaluated keys for the
		// targetSorts.
		final int length = base.getLevelCount() + targetSorts.length;
		valueIndexs = new int[length];
		sortDirections = new boolean[length];
		List[] indicesForSort = new List[base.getLevelCount()];
		for (int i = 0; i < targetSorts.length; i++) {
			DimLevel targetLevel = targetSorts[i].getTargetLevel();
			int levelIndex = base.getLevelIndex(targetLevel);
			if (indicesForSort[levelIndex] == null) {
				indicesForSort[levelIndex] = new ArrayList();
			}
			// index in the targetSorts with 2's complement encoding
			indicesForSort[levelIndex].add(Integer.valueOf(~i));
		}
		// populate value indices and sort directions
		int index = 0;
		for (int i = 0; i < indicesForSort.length; i++) {
			if (indicesForSort[i] != null) {
				for (Iterator j = indicesForSort[i].iterator(); j.hasNext();) {
					Integer aggrIndex = (Integer) j.next();
					valueIndexs[index] = aggrIndex.intValue();
					ITargetSort targetSort = targetSorts[~valueIndexs[index]];
					sortDirections[index] = toSortDirection(targetSort.getSortDirection());
					index++;
				}
			}
			valueIndexs[index] = i;
			sortDirections[index] = toSortDirection(base.getSortType(i));
			index++;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(T, T)
	 */
	public int compare(Object arg0, Object arg1) {
		IAggregationResultRow row1 = (IAggregationResultRow) arg0;
		IAggregationResultRow row2 = (IAggregationResultRow) arg1;
		Object[] keyValues1 = new Object[valueIndexs.length];
		Object[] keyValues2 = new Object[valueIndexs.length];
		for (int i = 0; i < valueIndexs.length; i++) {
			final int index = valueIndexs[i];
			if (index >= 0) {
				keyValues1[i] = row1.getLevelMembers()[index].getKeyValues()[0];
				keyValues2[i] = row2.getLevelMembers()[index].getKeyValues()[0];
			} else {
				keyValues1[i] = row1.getAggregationValues()[~index];
				keyValues2[i] = row2.getAggregationValues()[~index];
			}
		}
		return CompareUtil.compare(keyValues1, keyValues2, sortDirections);
	}

	/**
	 * 
	 * @param sortType
	 * @return
	 */
	private boolean toSortDirection(int sortType) {
		return sortType == IDimensionSortDefn.SORT_DESC ? false : true;
	}
}
