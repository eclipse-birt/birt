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

package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.util.DataType;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;

/**
 * Default implement class of the interface IAggregationResultSet.
 */

public class AggregationResultSet implements IAggregationResultSet {
	private AggregationDefinition aggregation;
	private DimLevel[] levels;
	private Map<String, Integer> aggregationResultNameMap = null;
	private IDiskArray aggregationResultRows;
	List<TimeResultRow> timeResultSet;
	private int currentPosition;
	private String[][] keyNames;
	private String[][] attributeNames;
	private int[][] keyDataTypes;
	private int[][] attributeDataTypes;
	private int[] aggregationDataType;
	private IAggregationResultRow resultObject;
	private static Logger logger = Logger.getLogger(AggregationResultSet.class.getName());

	/**
	 *
	 * @param aggregation
	 * @param aggregationResultRow
	 * @param keyNames
	 * @param attributeNames
	 * @throws IOException
	 */
	public AggregationResultSet(AggregationDefinition aggregation, IDiskArray aggregationResultRow, String[][] keyNames,
			String[][] attributeNames) throws IOException {
		Object[] params = { aggregation, aggregationResultRow, keyNames, attributeNames };
		logger.entering(AggregationResultSet.class.getName(), "AggregationResultSet", params);
		this.aggregation = aggregation;
		this.levels = aggregation.getLevels();
		this.aggregationResultRows = aggregationResultRow;
		produceaggregationNameMap();
		this.keyNames = keyNames;
		this.attributeNames = attributeNames;
		int aggrCount = 0;
		if (aggregation.getAggregationFunctions() != null) {
			aggrCount = aggregation.getAggregationFunctions().length;
		}
		aggregationDataType = new int[aggrCount];
		Arrays.fill(aggregationDataType, DataType.UNKNOWN_TYPE);
		if (aggregationResultRow.size() == 0) {
			return;
		}
		this.resultObject = (IAggregationResultRow) aggregationResultRow.get(0);
		if (resultObject.getLevelMembers() != null) {
			keyDataTypes = new int[resultObject.getLevelMembers().length][];
			attributeDataTypes = new int[resultObject.getLevelMembers().length][];

			for (int i = 0; i < resultObject.getLevelMembers().length; i++) {
				// only for drill operation, the member key value will be null
				if (resultObject.getLevelMembers()[i] == null) {
					continue;
				}
				keyDataTypes[i] = new int[resultObject.getLevelMembers()[i].getKeyValues().length];
				for (int j = 0; j < resultObject.getLevelMembers()[i].getKeyValues().length; j++) {
					keyDataTypes[i][j] = DataType
							.getDataType(resultObject.getLevelMembers()[i].getKeyValues()[j].getClass());
				}
				if (resultObject.getLevelMembers()[i].getAttributes() != null) {
					attributeDataTypes[i] = new int[resultObject.getLevelMembers()[i].getAttributes().length];

					for (int j = 0; j < attributeDataTypes[i].length; j++) {
						if (resultObject.getLevelMembers()[i].getAttributes()[j] != null) {
							attributeDataTypes[i][j] = DataType
									.getDataType(resultObject.getLevelMembers()[i].getAttributes()[j].getClass());
						}
					}
				}
			}
		}
		setAggregationDataType();
		logger.exiting(AggregationResultSet.class.getName(), "AggregationResultSet");
	}

	public AggregationResultSet(AggregationDefinition aggregation, DimLevel[] levels, IDiskArray aggregationResultRow,
			String[][] keyNames, String[][] attributeNames) throws IOException {
		this(aggregation, aggregationResultRow, keyNames, attributeNames);
		this.levels = levels;
	}

	/**
	 * @throws IOException
	 *
	 */
	private void setAggregationDataType() throws IOException {
		IAggregationResultRow resultObject = null;

		for (int i = 0; i < this.aggregationResultRows.size(); i++) {
			resultObject = (IAggregationResultRow) aggregationResultRows.get(i);
			if (resultObject.getAggregationValues() == null) {
				continue;
			}
			boolean existUnknown = false;

			for (int j = 0; j < resultObject.getAggregationValues().length; j++) {
				if (aggregationDataType[j] == DataType.UNKNOWN_TYPE) {
					if (resultObject.getAggregationValues()[j] != null) {
						aggregationDataType[j] = DataType
								.getDataType(resultObject.getAggregationValues()[j].getClass());
					} else {
						existUnknown = true;
					}
				}
			}
			if (!existUnknown) {
				return;
			}
		}
	}

	/**
	 * @throws IOException
	 *
	 */
	private void setTimeDataType() throws IOException {
		TimeResultRow timeResultRow = null;
		int[] timeAggregationDataType = null;
		for (int i = 0; i < this.timeResultSet.size(); i++) {
			timeResultRow = (TimeResultRow) timeResultSet.get(0);
			if (timeResultRow.getValue() == null) {
				continue;
			}
			boolean existUnknown = false;

			if (timeAggregationDataType == null) {
				timeAggregationDataType = new int[timeResultRow.getValue().length];
			}

			for (int j = 0; j < timeResultRow.getValue().length; j++) {
				if (timeAggregationDataType[j] == DataType.UNKNOWN_TYPE) {
					if (timeResultRow.getValue()[j] != null) {
						timeAggregationDataType[j] = DataType.getDataType(timeResultRow.getValue()[j].getClass());
					} else {
						existUnknown = true;
					}
				}
			}
			if (!existUnknown) {
				break;
			}
		}
	}

	/**
	 *
	 */
	private void produceaggregationNameMap() {
		AggregationFunctionDefinition[] functions = aggregation.getAggregationFunctions();
		aggregationResultNameMap = new HashMap<>();
		if (functions == null) {
			return;
		}
		for (int i = 0; i < functions.length; i++) {
			if (functions[i].getName() != null) {
				aggregationResultNameMap.put(functions[i].getName(), i);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#
	 * getAggregationDataType(int)
	 */
	@Override
	public int getAggregationDataType(int aggregationIndex) throws IOException {
		if (aggregationDataType == null || aggregationIndex < 0) {
			return DataType.UNKNOWN_TYPE;
		}
		return aggregationDataType[aggregationIndex];
	}

	/**
	 *
	 * @return
	 */
	@Override
	public int[] getAggregationDataType() {
		return aggregationDataType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getAggregationValue
	 * (int)
	 */
	@Override
	public Object getAggregationValue(int aggregationIndex) throws IOException {
		if (resultObject.getAggregationValues() == null || aggregationIndex < 0) {
			return null;
		}
		return resultObject.getAggregationValues()[aggregationIndex];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelAttribute(
	 * int, int)
	 */
	@Override
	public Object getLevelAttribute(int levelIndex, int attributeIndex) {
		if (resultObject.getLevelMembers() == null || levelIndex < 0
				|| resultObject.getLevelMembers()[levelIndex].getAttributes() == null) {
			return null;
		}
		return resultObject.getLevelMembers()[levelIndex].getAttributes()[attributeIndex];
	}

	@Override
	public Object[] getLevelAttributesValue(int levelIndex) {
		if (resultObject.getLevelMembers() == null || levelIndex < 0
				|| resultObject.getLevelMembers()[levelIndex].getAttributes() == null) {
			return null;
		}
		return resultObject.getLevelMembers()[levelIndex].getAttributes();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#
	 * getLevelAttributeDataType(java.lang.String, java.lang.String)
	 */
	@Override
	public int getLevelAttributeDataType(DimLevel level, String attributeName) {
		int levelIndex = getLevelIndex(level);
		if (attributeDataTypes == null || attributeDataTypes[levelIndex] == null) {
			return DataType.UNKNOWN_TYPE;
		}
		return this.attributeDataTypes[levelIndex][getLevelAttributeIndex(level, attributeName)];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#
	 * getLevelAttributeIndex(java.lang.String, java.lang.String)
	 */
	@Override
	public int getLevelAttributeIndex(DimLevel level, String attributeName) {
		int levelIndex = getLevelIndex(level);
		if (attributeNames == null || attributeNames[levelIndex] == null) {
			return -1;
		}
		for (int i = 0; i < attributeNames[levelIndex].length; i++) {
			if (attributeNames[levelIndex][i].equals(attributeName)) {
				return i;
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#
	 * getLevelAttributeIndex(int, java.lang.String)
	 */
	@Override
	public int getLevelAttributeIndex(int levelIndex, String attributeName) {
		if (attributeNames == null || levelIndex < 0 || attributeNames[levelIndex] == null) {
			return -1;
		}
		for (int i = 0; i < attributeNames[levelIndex].length; i++) {
			if (attributeNames[levelIndex][i].equals(attributeName)) {
				return i;
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelIndex(java.
	 * lang.String)
	 */
	@Override
	public int getLevelIndex(DimLevel level) {
		if (levels == null) {
			return -1;
		}
		for (int i = 0; i < levels.length; i++) {
			if (levels[i].equals(level)) {
				return i;
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelKeyDataType
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public int getLevelKeyDataType(DimLevel level, String keyName) {
		if (keyDataTypes == null) {
			return DataType.UNKNOWN_TYPE;
		}
		return getLevelKeyDataType(getLevelIndex(level), keyName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelKeyValue(
	 * int)
	 */
	@Override
	public Object[] getLevelKeyValue(int levelIndex) {
		if (resultObject.getLevelMembers() == null || levelIndex < 0
				|| levelIndex > resultObject.getLevelMembers().length - 1
				|| resultObject.getLevelMembers()[levelIndex] == null) {
			return null;
		}
		return resultObject.getLevelMembers()[levelIndex].getKeyValues();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#length()
	 */
	@Override
	public int length() {
		return aggregationResultRows.size();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#seek(int)
	 */
	@Override
	public void seek(int index) throws IOException {
		if (index >= aggregationResultRows.size()) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + aggregationResultRows.size());
		}
		currentPosition = index;
		resultObject = (IAggregationResultRow) aggregationResultRows.get(index);
		if (this.timeResultSet != null) {
			Object[] aggrValues = resultObject.getAggregationValues();
			Object[] tAggrValues = this.timeResultSet.get(currentPosition).getValue();
			System.arraycopy(tAggrValues, 0, aggrValues, aggrValues.length - tAggrValues.length, tAggrValues.length);
		}
	}

	/**
	 *
	 * @return
	 */
	@Override
	public IAggregationResultRow getCurrentRow() {
		return this.resultObject;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet#getPosition(
	 * )
	 */
	@Override
	public int getPosition() {
		return currentPosition;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getSortType(int)
	 */
	@Override
	public int getSortType(int levelIndex) {
		if (aggregation.getSortTypes() == null) {
			return -100;
		}
		return aggregation.getSortTypes()[levelIndex];
	}

	/**
	 *
	 * @return
	 */
	@Override
	public int[] getSortType() {
		return aggregation.getSortTypes();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#
	 * getLevelAttributeDataType(int, java.lang.String)
	 */
	@Override
	public int getLevelAttributeDataType(int levelIndex, String attributeName) {
		if (attributeDataTypes == null || levelIndex < 0 || attributeDataTypes[levelIndex] == null) {
			return DataType.UNKNOWN_TYPE;
		}
		return attributeDataTypes[levelIndex][getLevelAttributeIndex(levelIndex, attributeName)];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getAllAttributes(
	 * int)
	 */
	@Override
	public String[] getLevelAttributes(int levelIndex) {
		if (attributeNames == null) {
			return null;
		}
		return attributeNames[levelIndex];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelKeyDataType
	 * (int, java.lang.String)
	 */
	@Override
	public int getLevelKeyDataType(int levelIndex, String keyName) {
		if (keyDataTypes == null || levelIndex < 0 || keyDataTypes[levelIndex] == null) {
			return DataType.UNKNOWN_TYPE;
		}
		return keyDataTypes[levelIndex][getLevelKeyIndex(levelIndex, keyName)];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelKeyIndex(
	 * int, java.lang.String)
	 */
	@Override
	public int getLevelKeyIndex(int levelIndex, String keyName) {
		if (keyNames == null || levelIndex < 0 || keyNames[levelIndex] == null) {
			return -1;
		}
		for (int i = 0; i < keyNames[levelIndex].length; i++) {
			if (keyNames[levelIndex][i].equals(keyName)) {
				return i;
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelKeyIndex(
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public int getLevelKeyIndex(DimLevel level, String keyName) {
		if (keyNames == null) {
			return -1;
		}
		return getLevelKeyIndex(getLevelIndex(level), keyName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.olap.data.api.IAggregationResultSet#
	 * getLevelAttributeColCount(int)
	 */
	@Override
	public int getLevelAttributeColCount(int levelIndex) {
		if (attributeNames == null || attributeNames[levelIndex] == null) {
			return 0;
		}
		return attributeNames[levelIndex].length;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelKeyColCount
	 * (int)
	 */
	@Override
	public int getLevelKeyColCount(int levelIndex) {
		if (keyNames == null || keyNames[levelIndex] == null) {
			return 0;
		}
		return keyNames[levelIndex].length;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public String[][] getLevelKeys() {
		return keyNames;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public int[][] getLevelKeyDataType() {
		return keyDataTypes;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public String[][] getLevelAttributes() {
		return attributeNames;
	}

	/**
	 *
	 * @return
	 */
	@Override
	public int[][] getLevelAttributeDataType() {
		return this.attributeDataTypes;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.olap.data.api.IAggregationResultSet#getLevelCount()
	 */
	@Override
	public int getLevelCount() {
		if (keyNames == null) {
			return 0;
		}
		return keyNames.length;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet#
	 * getLevelKeyName(int, int)
	 */
	@Override
	public String getLevelKeyName(int levelIndex, int keyIndex) {
		return keyNames[levelIndex][keyIndex];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet#getLevelName
	 * (int)
	 */
	@Override
	public DimLevel getLevel(int levelIndex) {
		return levels[levelIndex];
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet#
	 * getAggributeNames()
	 */
	@Override
	public String[][] getAttributeNames() {
		return this.attributeNames;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet#getKeyNames(
	 * )
	 */
	@Override
	public String[][] getKeyNames() {
		return this.keyNames;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet#
	 * getAggregationIndex(java.lang.String)
	 */
	@Override
	public int getAggregationIndex(String name) throws IOException {
		Object index = aggregationResultNameMap.get(name);
		if (index == null) {
			return -1;
		}
		return ((Integer) index).intValue();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet#getAllLevels
	 * ()
	 */
	@Override
	public DimLevel[] getAllLevels() {
		return levels;
	}

	/**
	 *
	 */
	@Override
	public AggregationDefinition getAggregationDefinition() {
		return this.aggregation;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet#close()
	 */
	@Override
	public void close() throws IOException {
		aggregationResultRows.close();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet#clear()
	 */
	@Override
	public void clear() throws IOException {
		aggregationResultRows.clear();
	}

	public void addTimeFunctionResultSet(List<TimeResultRow> timeResultSet) throws IOException {
		this.timeResultSet = timeResultSet;
		setTimeDataType();
	}

	@Override
	public int getAggregationCount() {
		return aggregation.getAggregationFunctions() == null ? 0 : aggregation.getAggregationFunctions().length;
	}

	@Override
	public String getAggregationName(int index) {
		if (aggregation.getAggregationFunctions() != null) {
			return aggregation.getAggregationFunctions()[index].getName();
		}
		return null;
	}

	public IDiskArray getAggregationResultRows() {
		return this.aggregationResultRows;
	}

	public void setAggregationResultRows(IDiskArray rows) {
		this.aggregationResultRows = rows;
	}
}
