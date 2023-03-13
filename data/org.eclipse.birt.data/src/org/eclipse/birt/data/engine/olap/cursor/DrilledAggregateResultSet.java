/**
 * Copyright (c) 2010 Actuate Corporation.
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

package org.eclipse.birt.data.engine.olap.cursor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDrillFilter;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.CompareUtil;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.query.view.CubeQueryDefinitionUtil;
import org.eclipse.birt.data.engine.olap.query.view.DrillOnDimensionHierarchy;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;

/**
 *
 * @author Administrator
 *
 */
public class DrilledAggregateResultSet implements IAggregationResultSet {

	private IDiskArray bufferedStructureArray;
	private DimLevel[] dimLevel;
	private IAggregationResultRow resultObject;
	private IAggregationResultSet aggregationRsFromCube;
	private int currentPosition;
	private Map<IEdgeDrillFilter, List<DimLevel>> drillFilterTargetLevels;

	public DrilledAggregateResultSet(IAggregationResultSet aggregationRsFromCube,
			IAggregationResultSet[] aggregationRsFromDrill, List<DrillOnDimensionHierarchy> drillFilters)
			throws IOException, DataException {
		bufferedStructureArray = new BufferedStructureArray(AggregationResultRow.getCreator(), 2000);

		this.dimLevel = aggregationRsFromCube.getAllLevels();
		this.aggregationRsFromCube = aggregationRsFromCube;

		drillFilterTargetLevels = new HashMap<>();
		for (int i = 0; i < drillFilters.size(); i++) {
			IEdgeDrillFilter[] filters = drillFilters.get(i).getDrillByDimension();
			for (int t = 0; t < filters.length; t++) {
				drillFilterTargetLevels.put(filters[t], CubeQueryDefinitionUtil.getDrilledTargetLevels(filters[t]));
			}
		}
		// initial the drilled aggregation result
		if (aggregationRsFromDrill != null) {
			for (int i = 0; i < aggregationRsFromDrill.length; i++) {
				aggregationRsFromDrill[i].seek(0);
			}
		}

		for (int k = 0; k < aggregationRsFromCube.length(); k++) {
			aggregationRsFromCube.seek(k);
			IEdgeDrillFilter targetDrill = getTargetDrillOperation(aggregationRsFromCube.getCurrentRow(), drillFilters);

			if (targetDrill == null) {
				bufferedStructureArray.add(aggregationRsFromCube.getCurrentRow());
				continue;
			}

			List<IAggregationResultRow> tempBufferArray = populateResultSet(aggregationRsFromCube, targetDrill);

			List<IEdgeDrillFilter[]> drills = this.getRemainingDrillOperation(targetDrill, drillFilters);

			if (!drills.isEmpty()) {
				tempBufferArray = populateNextResultSet(tempBufferArray, drills);
			}

			if (aggregationRsFromCube.getAggregationCount() == 0) {
				removeDuplictedRow(tempBufferArray);
			} else {
				recalculateAggregation(tempBufferArray, aggregationRsFromDrill);
			}

			if (!this.isResultForRunningAggregation(aggregationRsFromCube)) {
				sortAggregationRow(tempBufferArray);
			}

			Iterator<IAggregationResultRow> iter = tempBufferArray.iterator();
			while (iter.hasNext()) {
				bufferedStructureArray.add(iter.next());
			}
			k = aggregationRsFromCube.getPosition();
		}

		this.resultObject = (IAggregationResultRow) bufferedStructureArray.get(0);
	}

	private void sortAggregationRow(List<IAggregationResultRow> aggregationRows) {
		final int[] sortType = this.aggregationRsFromCube.getSortType();

		final boolean[] sorts = new boolean[sortType.length];
		for (int i = 0; i < sortType.length; i++) {
			if (ISortDefinition.SORT_ASC == sortType[i]) {
				sorts[i] = true;
			} else if (ISortDefinition.SORT_DESC == sortType[i]) {
				sorts[i] = false;
			} else {
				sorts[i] = true;
			}
		}
		Collections.sort(aggregationRows, new Comparator() {

			@Override
			public int compare(Object o1, Object o2) {
				IAggregationResultRow row1 = (IAggregationResultRow) o1;
				IAggregationResultRow row2 = (IAggregationResultRow) o2;
				Object[] keyValues1 = new Object[row1.getLevelMembers().length];
				Object[] keyValues2 = new Object[row2.getLevelMembers().length];
				for (int i = 0; i < row1.getLevelMembers().length; i++) {
					if (row1.getLevelMembers()[i] != null) {
						keyValues1[i] = row1.getLevelMembers()[i].getKeyValues()[0];
					} else {
						keyValues1[i] = null;
					}
					if (row2.getLevelMembers()[i] != null) {
						keyValues2[i] = row2.getLevelMembers()[i].getKeyValues()[0];
					} else {
						keyValues2[i] = null;
					}
				}
				return CompareUtil.compare(keyValues1, keyValues2, sorts);
			}
		});
	}

	private void removeDuplictedRow(List<IAggregationResultRow> aggregationRows) {
		for (int i = 0; i < aggregationRows.size(); i++) {
			IAggregationResultRow rows = aggregationRows.get(i);
			for (int k = i + 1; k < aggregationRows.size();) {
				if (rows.compareTo(aggregationRows.get(k)) == 0) {
					aggregationRows.remove(k);
				} else {
					k++;
				}
			}
		}
	}

	private void recalculateAggregation(List<IAggregationResultRow> aggregationRows,
			IAggregationResultSet[] aggregationRsFromDrill) throws DataException, IOException {
		Set<Integer> duplicatedIndex = new LinkedHashSet<>();
		for (int i = 0; i < aggregationRows.size(); i++) {
			List<Integer> positions = getRowsPositionInAggregationRows(i, aggregationRows);
			Object[] aggregationValues = null;

			for (int k = 0; k < positions.size(); k++) {
				if (k == 0) {
					aggregationValues = findAggregationValue(aggregationRows.get(positions.get(k)),
							aggregationRsFromDrill);
				} else {
					duplicatedIndex.add(positions.get(k));
				}
			}
			aggregationRows.get(i).setAggregationValues(aggregationValues);

			int baseIndex = 0;
			Iterator<Integer> iter = duplicatedIndex.iterator();
			while (iter.hasNext()) {
				int index = iter.next().intValue();
				aggregationRows.remove(index - baseIndex);
				baseIndex++;
			}
			duplicatedIndex.clear();
		}
	}

	private Object[] findAggregationValue(IAggregationResultRow iAggregationResultRow,
			IAggregationResultSet[] aggregationRsFromDrill) throws IOException, DataException {
		Map<DimLevel, Object> targetValueMap = new HashMap<>();
		List<DimLevel> levels = new ArrayList<>();
		for (int i = 0; i < this.dimLevel.length; i++) {
			if (iAggregationResultRow.getLevelMembers()[i] != null
					&& iAggregationResultRow.getLevelMembers()[i].getKeyValues() != null) {
				targetValueMap.put(this.dimLevel[i], iAggregationResultRow.getLevelMembers()[i].getKeyValues()[0]);
				levels.add(this.dimLevel[i]);
			}
		}
		IAggregationResultSet targetRs = null;
		for (int i = 0; i < aggregationRsFromDrill.length; i++) {
			if (aggregationRsFromDrill[i].getAllLevels().length != targetValueMap.size()) {
				continue;
			}
			DimLevel[] dimLevels = aggregationRsFromDrill[i].getAllLevels();
			for (int j = 0; j < dimLevels.length; j++) {
				if (!targetValueMap.containsKey(dimLevels[j])) {
					break;
				}
				if (j == dimLevels.length - 1) {
					targetRs = aggregationRsFromDrill[i];
					break;
				}
			}
			if (targetRs != null) {
				break;
			}
		}
		if (targetRs == null) {
			return iAggregationResultRow.getAggregationValues();
		}

		boolean find = false;
		if (isResultForRunningAggregation(targetRs)) {
			find = findValueOneByOne(targetRs, targetValueMap, levels);
		} else {
			find = findValueMatcher(targetRs, targetValueMap, levels);
		}
		if (find) {
			Object[] value = new Object[aggregationRsFromCube.getAggregationCount()];
			for (int i = 0; i < value.length; i++) {
				String name = aggregationRsFromCube.getAggregationDefinition().getAggregationFunctions()[i].getName();
				int index = targetRs.getAggregationIndex(name);
				value[i] = targetRs.getAggregationValue(index);
			}
			return value;
		} else {
			return null;
		}
	}

	private static boolean isResultForRunningAggregation(IAggregationResultSet ars) throws DataException {
		AggregationDefinition ad = ars.getAggregationDefinition();
		if (ad != null) {
			AggregationFunctionDefinition[] afds = ad.getAggregationFunctions();
			if (afds != null && afds.length == 1) {
				String functionName = afds[0].getFunctionName();
				IAggrFunction af = AggregationManager.getInstance().getAggregation(functionName);
				return af != null && af.getType() == IAggrFunction.RUNNING_AGGR;
			}
		}
		return false;
	}

	/**
	 * The same lookup logic with AggregationAccessor
	 *
	 * @param targetRs
	 * @param targetLevelValue
	 * @param levels
	 * @return
	 */
	private boolean findValueMatcher(IAggregationResultSet targetRs, Map<DimLevel, Object> targetLevelValue,
			List levels) {
		if (targetLevelValue.isEmpty()) {
			return true;
		}
		int start = 0, state = 0;
		boolean find = false;

		int position = targetRs.getPosition();
		for (; start < levels.size();) {
			DimLevel level = (DimLevel) levels.get(start);

			Object value1 = targetLevelValue.get(level);
			Object value2 = null;
			int index = targetRs.getLevelIndex(level);
			Object[] keyValues = targetRs.getLevelKeyValue(index);
			if (keyValues != null) {
				value2 = keyValues[targetRs.getLevelKeyColCount(index) - 1];
			}
			int sortType = targetRs.getSortType(index) == IDimensionSortDefn.SORT_DESC ? -1 : 1;
			int direction = sortType * compare(value1, value2) < 0 ? -1 : compare(value1, value2) == 0 ? 0 : 1;
			if (direction < 0 && position > 0 && (state == 0 || state == direction)) {
				state = direction;
				try {
					targetRs.seek(--position);
				} catch (IOException e) {
					find = false;
				}
				start = 0;
				continue;
			} else if (direction > 0 && position < targetRs.length() - 1 && (state == 0 || state == direction)) {
				state = direction;
				try {
					targetRs.seek(++position);
				} catch (IOException e) {
					find = false;
				}
				start = 0;
				continue;
			} else if (direction == 0) {
				if (start == levels.size() - 1) {
					find = true;
					break;
				} else {
					start++;
					continue;
				}
			} else if (position < 0 || position >= targetRs.length()) {
				return false;
			} else {
				return false;
			}
		}
		return find;
	}

	private boolean findValueOneByOne(IAggregationResultSet targetRs, Map<DimLevel, Object> targetLevelValue,
			List levels) throws IOException {
		int position = 0;
		if (targetRs.length() <= 0 || levels.isEmpty()) {
			return true;
		}
		while (position < targetRs.length()) {
			targetRs.seek(position);
			boolean match = true;
			for (int i = 0; i < levels.size(); i++) {
				DimLevel level = (DimLevel) levels.get(i);
				Object value1 = targetLevelValue.get(level);
				Object value2 = null;
				int index = targetRs.getLevelIndex(level);
				Object[] keyValues = targetRs.getLevelKeyValue(index);
				if (keyValues != null) {
					value2 = keyValues[targetRs.getLevelKeyColCount(index) - 1];
				}

				if (value1 == value2) {
					continue;
				}
				if (value1 == null || value2 == null || !value1.equals(value2)) {
					match = false;
					break;
				}
			}
			if (match) {
				return true;
			} else {
				++position;
			}
		}

		return false;
	}

	static int compare(Object value1, Object value2) {
		if (value1 == value2) {
			return 0;
		}
		if (value1 == null) {
			return -1;
		}
		if (value2 == null) {
			return 1;
		}
		if (value1 instanceof Comparable) {
			return ((Comparable) value1).compareTo(value2);
		}
		return value1.toString().compareTo(value2.toString());
	}

	private List<Integer> getRowsPositionInAggregationRows(int index, List<IAggregationResultRow> aggregationRows) {
		List<Integer> position = new ArrayList<>();
		position.add(index);
		IAggregationResultRow row = (IAggregationResultRow) aggregationRows.get(index);
		for (int i = index + 1; i < aggregationRows.size(); i++) {
			if (row.compareTo(aggregationRows.get(i)) == 0) {
				position.add(i);
			}
		}
		return position;
	}

	private List<IAggregationResultRow> populateNextResultSet(List<IAggregationResultRow> tempBufferArray,
			List<IEdgeDrillFilter[]> nextDrills) throws IOException {
		List finalBufferArray = new ArrayList<IAggregationResultRow>();
		for (int i = 0; i < tempBufferArray.size(); i++) {
			IAggregationResultRow rows = tempBufferArray.get(i);
			for (int k = 0; k < nextDrills.size(); k++) {
				IEdgeDrillFilter[] filters = nextDrills.get(k);
				for (int t = 0; t < filters.length; t++) {
					IEdgeDrillFilter targetFilter = filters[t];
					rows = this.populateResultSet(rows, targetFilter);
				}
			}
			finalBufferArray.add(rows);
		}
		return finalBufferArray;
	}

	private IAggregationResultRow populateResultSet(IAggregationResultRow aggregationRow, IEdgeDrillFilter targetDrill)
			throws IOException {
		IAggregationResultRow row = aggregationRow;
		if (isDrilledElement(aggregationRow, targetDrill)) {
			Member[] drillMember = new Member[this.dimLevel.length];
			for (int i = 0; i < drillMember.length; i++) {
				drillMember[i] = new Member();
				if (!this.dimLevel[i].getDimensionName()
						.equals(targetDrill.getTargetHierarchy().getDimension().getName())) {
					drillMember[i] = aggregationRow.getLevelMembers()[i];
				} else {
					List comparableLevels = this.drillFilterTargetLevels.get(targetDrill);
					if (comparableLevels != null && comparableLevels.contains(this.dimLevel[i])) {
						drillMember[i] = aggregationRow.getLevelMembers()[i];
					} else {
						drillMember[i] = null;
					}
					continue;
				}
			}
			row = new AggregationResultRow(drillMember, aggregationRow.getAggregationValues());
		}
		return row;
	}

	private List<IAggregationResultRow> populateResultSet(IAggregationResultSet aggregationRsFromCube,
			IEdgeDrillFilter targetDrill) throws IOException {
		List<IAggregationResultRow> drillResultSet = new ArrayList<>();
		int k = aggregationRsFromCube.getPosition();
		for (; k < aggregationRsFromCube.length(); k++) {
			aggregationRsFromCube.seek(k);

			if (isDrilledElement(aggregationRsFromCube.getCurrentRow(), targetDrill)) {
				IAggregationResultRow row = this.populateResultSet(aggregationRsFromCube.getCurrentRow(), targetDrill);
				drillResultSet.add(row);
			} else {
				aggregationRsFromCube.seek(k - 1);
				break;
			}
		}

		return drillResultSet;
	}

	private IEdgeDrillFilter getTargetDrillOperation(IAggregationResultRow row,
			List<DrillOnDimensionHierarchy> drillFilters) {
		for (int i = 0; i < drillFilters.size(); i++) {
			DrillOnDimensionHierarchy filters = drillFilters.get(i);
			Iterator<List<IEdgeDrillFilter>> iter = filters.getDrillFilterIterator();
			while (iter.hasNext()) {
				List<IEdgeDrillFilter> drills = iter.next();
				for (int j = 0; j < drills.size(); j++) {
					if (isDrilledElement(row, drills.get(j))) {
						return drills.get(j);
					}
				}
			}
		}
		return null;
	}

	private List<IEdgeDrillFilter[]> getRemainingDrillOperation(IEdgeDrillFilter targetDrill,
			List<DrillOnDimensionHierarchy> drillFilters) {
		List list = new ArrayList();
		for (int i = 0; i < drillFilters.size(); i++) {
			DrillOnDimensionHierarchy filters = drillFilters.get(i);
			if (filters.contains(targetDrill)) {
				continue;
			}
			list.add(filters.getDrillByDimension());
		}
		return list;
	}

	private boolean isDrilledElement(IAggregationResultRow row, IEdgeDrillFilter drill) {
		List comparableLevels = drillFilterTargetLevels.get(drill);

		boolean matched = true;
		Object[] tuple = drill.getTuple().toArray();
		for (int j = 0; j < tuple.length; j++) {
			if (tuple[j] == null) {
				continue;
			}
			int levelIndex = -1;
			for (int t = 0; t < this.dimLevel.length; t++) {
				if (this.dimLevel[t].equals(comparableLevels.get(j))) {
					levelIndex = t;
					break;
				}
			}
			if (levelIndex == -1) {
				return false;
			}
			if (!containMember(row.getLevelMembers()[levelIndex].getKeyValues(), (Object[]) tuple[j])) {
				matched = false;
				break;
			}
		}
		return matched;
	}

	private boolean containMember(Object[] levelkey, Object[] key) {
		Object[] memberKeys = levelkey;
		for (Object obj : key) {
			try {
				if (ScriptEvalUtil.compare(obj, memberKeys[0]) == 0) {
					return true;
				}
			} catch (DataException e) {
				// ignore it.
			}
		}
		return false;
	}

	@Override
	public void clear() throws IOException {
		this.bufferedStructureArray.clear();
	}

	@Override
	public void close() throws IOException {
		this.bufferedStructureArray.close();
	}

	@Override
	public int getAggregationCount() {
		return this.aggregationRsFromCube.getAggregationCount();
	}

	@Override
	public int getAggregationDataType(int aggregationIndex) throws IOException {
		return this.aggregationRsFromCube.getAggregationDataType(aggregationIndex);
	}

	@Override
	public int[] getAggregationDataType() {
		return this.aggregationRsFromCube.getAggregationDataType();
	}

	@Override
	public AggregationDefinition getAggregationDefinition() {
		return this.aggregationRsFromCube.getAggregationDefinition();
	}

	@Override
	public int getAggregationIndex(String name) throws IOException {
		return this.aggregationRsFromCube.getAggregationIndex(name);
	}

	@Override
	public String getAggregationName(int index) {
		return this.aggregationRsFromCube.getAggregationName(index);
	}

	@Override
	public Object getAggregationValue(int aggregationIndex) throws IOException {
		return this.resultObject.getAggregationValues()[aggregationIndex];
	}

	@Override
	public DimLevel[] getAllLevels() {
		return this.dimLevel;
	}

	@Override
	public String[][] getAttributeNames() {
		return this.aggregationRsFromCube.getAttributeNames();
	}

	@Override
	public IAggregationResultRow getCurrentRow() throws IOException {
		return this.resultObject;
	}

	@Override
	public String[][] getKeyNames() {
		return this.aggregationRsFromCube.getKeyNames();
	}

	@Override
	public DimLevel getLevel(int levelIndex) {
		return this.aggregationRsFromCube.getLevel(levelIndex);
	}

	@Override
	public Object getLevelAttribute(int levelIndex, int attributeIndex) {
		if (resultObject.getLevelMembers() == null || levelIndex < 0
				|| levelIndex > resultObject.getLevelMembers().length - 1
				|| resultObject.getLevelMembers()[levelIndex] == null) {
			return null;
		}
		return resultObject.getLevelMembers()[levelIndex].getAttributes()[attributeIndex];
	}

	@Override
	public int getLevelAttributeColCount(int levelIndex) {
		return this.aggregationRsFromCube.getLevelAttributeColCount(levelIndex);
	}

	@Override
	public int getLevelAttributeDataType(DimLevel level, String attributeName) {
		return this.aggregationRsFromCube.getLevelAttributeDataType(level, attributeName);
	}

	@Override
	public int getLevelAttributeDataType(int levelIndex, String attributeName) {
		return this.aggregationRsFromCube.getLevelAttributeDataType(levelIndex, attributeName);
	}

	@Override
	public int[][] getLevelAttributeDataType() {
		return this.aggregationRsFromCube.getLevelAttributeDataType();
	}

	@Override
	public int getLevelAttributeIndex(int levelIndex, String attributeName) {
		return this.aggregationRsFromCube.getLevelAttributeIndex(levelIndex, attributeName);
	}

	@Override
	public int getLevelAttributeIndex(DimLevel level, String attributeName) {
		return this.aggregationRsFromCube.getLevelAttributeIndex(level, attributeName);
	}

	@Override
	public String[] getLevelAttributes(int levelIndex) {
		return this.aggregationRsFromCube.getLevelAttributes(levelIndex);
	}

	@Override
	public String[][] getLevelAttributes() {
		return this.aggregationRsFromCube.getLevelAttributes();
	}

	@Override
	public Object[] getLevelAttributesValue(int levelIndex) {
		return this.aggregationRsFromCube.getLevelAttributesValue(levelIndex);
	}

	@Override
	public int getLevelCount() {
		return this.aggregationRsFromCube.getLevelCount();
	}

	@Override
	public int getLevelIndex(DimLevel level) {
		return this.aggregationRsFromCube.getLevelIndex(level);
	}

	@Override
	public int getLevelKeyColCount(int levelIndex) {
		return this.aggregationRsFromCube.getLevelKeyColCount(levelIndex);
	}

	@Override
	public int getLevelKeyDataType(DimLevel level, String keyName) {
		return this.aggregationRsFromCube.getLevelKeyDataType(level, keyName);
	}

	@Override
	public int getLevelKeyDataType(int levelIndex, String keyName) {
		return this.aggregationRsFromCube.getLevelKeyDataType(levelIndex, keyName);
	}

	@Override
	public int[][] getLevelKeyDataType() {
		return this.aggregationRsFromCube.getLevelKeyDataType();
	}

	@Override
	public int getLevelKeyIndex(int levelIndex, String keyName) {
		return this.aggregationRsFromCube.getLevelKeyIndex(levelIndex, keyName);
	}

	@Override
	public int getLevelKeyIndex(DimLevel level, String keyName) {
		return this.aggregationRsFromCube.getLevelKeyIndex(level, keyName);
	}

	@Override
	public String getLevelKeyName(int levelIndex, int keyIndex) {
		return this.aggregationRsFromCube.getLevelKeyName(levelIndex, keyIndex);
	}

	@Override
	public Object[] getLevelKeyValue(int levelIndex) {
		if (resultObject.getLevelMembers()[levelIndex] == null || levelIndex < 0
				|| levelIndex > resultObject.getLevelMembers().length - 1) {
			return null;
		}
		return resultObject.getLevelMembers()[levelIndex].getKeyValues();
	}

	@Override
	public String[][] getLevelKeys() {
		return this.aggregationRsFromCube.getLevelKeys();
	}

	@Override
	public int getPosition() {
		return this.currentPosition;
	}

	@Override
	public int getSortType(int levelIndex) {
		return this.aggregationRsFromCube.getSortType(levelIndex);
	}

	@Override
	public int[] getSortType() {
		return this.aggregationRsFromCube.getSortType();
	}

	@Override
	public int length() {
		return bufferedStructureArray.size();
	}

	@Override
	public void seek(int index) throws IOException {
		if (index >= bufferedStructureArray.size()) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + bufferedStructureArray.size());
		}
		currentPosition = index;
		resultObject = (IAggregationResultRow) bufferedStructureArray.get(index);
	}

}
