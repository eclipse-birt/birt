
/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

import org.eclipse.birt.data.engine.aggregation.AggregationUtil;
import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.MeasureInfo;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.DimColumn;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFacttableFilterEvalHelper;

/**
 * This abstract class provide the common function of calculating a cube
 * aggregation
 */

public abstract class BaseAggregationCalculator implements IAggregationCalculator {
	protected Accumulator[] accumulators;
	protected int[] measureIndexes;
	protected FacttableRow facttableRow;
	protected int[] keyLevelIndex;
	protected IAggregationResultSet aggrResultSet;
	protected AggregationDefinition aggregation;
	protected ColumnInfo[] paraInfo;
	protected int[] sortTypes;

	BaseAggregationCalculator(AggregationDefinition aggregation, IAggregationResultSet aggrResultSet)
			throws DataException, IOException {
		this.aggregation = aggregation;
		this.aggrResultSet = aggrResultSet;
		getParameterColIndex();
		initParaInfo();
		createAccumulators();
	}

	/**
	 *
	 * @throws DataException
	 * @throws IOException
	 */
	protected void initParaInfo() throws DataException, IOException {
		AggregationFunctionDefinition[] aggregationFunctions = aggregation.getAggregationFunctions();

		if (aggregationFunctions != null) {
			this.measureIndexes = new int[aggregationFunctions.length];

			for (int i = 0; i < aggregationFunctions.length; i++) {
				IAggrFunction aggregationFunc = AggregationManager.getInstance()
						.getAggregation(aggregationFunctions[i].getFunctionName());
				if (aggregation == null) {
					throw new DataException(
							DataResourceHandle.getInstance().getMessage(ResourceConstants.UNSUPPORTED_FUNCTION)
									+ aggregationFunctions[i].getFunctionName());
				}
				if (!AggregationUtil.needDataField(aggregationFunc)) {
					paraInfo[i] = null;
				}

				final String measureName = aggregationFunctions[i].getMeasureName();
				this.measureIndexes[i] = aggrResultSet.getAggregationIndex(measureName);

				if (this.measureIndexes[i] == -1 && measureName != null) {
					throw new DataException(ResourceConstants.MEASURE_NAME_NOT_FOUND, measureName);
				}
			}
		}
	}

	/**
	 *
	 * @throws DataException
	 */
	protected void createAccumulators() throws DataException {
		AggregationFunctionDefinition[] aggregationFunctions = aggregation.getAggregationFunctions();

		if (aggregationFunctions != null) {
			this.accumulators = new Accumulator[aggregationFunctions.length];
			for (int i = 0; i < aggregationFunctions.length; i++) {
				IAggrFunction aggregationFunc = AggregationManager.getInstance()
						.getAggregation(aggregationFunctions[i].getFunctionName());
				if (aggregationFunc == null) {
					throw new DataException(
							DataResourceHandle.getInstance().getMessage(ResourceConstants.UNSUPPORTED_FUNCTION)
									+ aggregationFunctions[i].getFunctionName());
				}
				this.accumulators[i] = aggregationFunc.newAccumulator();
				this.accumulators[i].start();
			}
		}
	}

	/**
	 *
	 * @throws DataException
	 */
	protected void getParameterColIndex() throws DataException {
		AggregationFunctionDefinition[] functions = aggregation.getAggregationFunctions();
		if (functions == null || functions.length == 0) {
			return;
		}
		paraInfo = new ColumnInfo[functions.length];
		for (int j = 0; j < functions.length; j++) {
			DimColumn paraCol = functions[j].getParaCol();
			if (paraCol != null) {
				paraInfo[j] = findColumnIndex(paraCol);
			}
		}
	}

	/**
	 *
	 * @param paraColumn
	 * @return
	 */
	private ColumnInfo findColumnIndex(DimColumn paraColumn) {
		ColumnInfo paraInfo;
		int levelIndex = this.aggrResultSet
				.getLevelIndex(new DimLevel(paraColumn.getDimensionName(), paraColumn.getLevelName()));
		int columnIndex = this.aggrResultSet.getLevelKeyIndex(levelIndex, paraColumn.getColumnName());
		int dataType = -1;
		if (columnIndex == -1) {
			columnIndex = this.aggrResultSet.getLevelAttributeIndex(levelIndex, paraColumn.getColumnName());
			dataType = this.aggrResultSet.getLevelAttributeDataType(levelIndex, paraColumn.getColumnName());
			paraInfo = new ColumnInfo(-1, levelIndex, columnIndex, dataType, false);
		} else {
			dataType = this.aggrResultSet.getLevelKeyDataType(levelIndex, paraColumn.getColumnName());
			paraInfo = new ColumnInfo(-1, levelIndex, columnIndex, dataType, true);
		}
		return paraInfo;
	}

	/**
	 *
	 * @return
	 * @throws IOException
	 */
	protected MeasureInfo[] getMeasureInfo() throws IOException {
		MeasureInfo[] measureInfos = new MeasureInfo[aggrResultSet.getAggregationCount()];
		for (int i = 0; i < measureInfos.length; i++) {
			measureInfos[i] = new MeasureInfo(aggrResultSet.getAggregationName(i),
					aggrResultSet.getAggregationDataType(i));
		}
		return measureInfos;
	}

	/**
	 *
	 * @param keyLevels
	 * @return
	 * @throws DataException
	 */
	protected int[] getKeyLevelIndexs(DimLevel[] keyLevels) throws DataException {
		int[] keyLevelIndexes = new int[keyLevels.length];
		DimLevel[] allLevels = aggrResultSet.getAllLevels();
		for (int i = 0; i < keyLevels.length; i++) {
			keyLevelIndexes[i] = -1;
			for (int j = 0; j < allLevels.length; j++) {
				if (keyLevels[i].equals(allLevels[j])) {
					keyLevelIndexes[i] = j;
				}
			}
			if (keyLevelIndexes[i] == -1) {
				throw new DataException(DataResourceHandle.getInstance().getMessage(ResourceConstants.NONEXISTENT_LEVEL)
						+ keyLevels[i].getLevelName());
			}
		}
		return keyLevelIndexes;
	}

	/**
	 *
	 * @param row
	 * @throws DataException
	 */
	protected void onRow(IAggregationResultRow row) throws DataException {
		if (accumulators == null) {
			return;
		}
		for (int i = 0; i < accumulators.length; i++) {
			if (getFilterResult(row, i)) {
				accumulators[i].onRow(getAccumulatorParameter(aggregation.getAggregationFunctions()[i], row, i));
			}
		}
	}

	/**
	 *
	 * @param function
	 * @param row
	 * @param funcIndex
	 * @return
	 * @throws DataException
	 */
	protected Object[] getAccumulatorParameter(AggregationFunctionDefinition function, IAggregationResultRow row,
			int funcIndex) throws DataException {
		Object[] parameters = null;
		if (paraInfo[funcIndex] == null || paraInfo[funcIndex].getLevelIndex() == -1) {
			if (getParaNum(funcIndex) <= 1) {
				parameters = new Object[1];
				if (measureIndexes[funcIndex] < 0) {
					return null;
				} else {
					parameters[0] = row.getAggregationValues()[measureIndexes[funcIndex]];
				}
			} else {
				parameters = new Object[2];
				parameters[1] = function.getParaValue();
				if (measureIndexes[funcIndex] >= 0) {
					parameters[0] = row.getAggregationValues()[measureIndexes[funcIndex]];
				}
			}
		} else {
			parameters = new Object[2];
			if (measureIndexes[funcIndex] < 0) {
				parameters[0] = null;
			} else {
				parameters[0] = row.getAggregationValues()[measureIndexes[funcIndex]];
			}
			Member member = row.getLevelMembers()[paraInfo[funcIndex].getLevelIndex()];
			if (paraInfo[funcIndex].isKey()) {
				parameters[1] = member.getKeyValues()[paraInfo[funcIndex].getColumnIndex()];
			} else {
				parameters[1] = member.getAttributes()[paraInfo[funcIndex].getColumnIndex()];
			}
		}
		return parameters;
	}

	private int getParaNum(int index) throws DataException {
		AggregationFunctionDefinition[] aggregationFunctions = aggregation.getAggregationFunctions();
		IAggrFunction aggregationFunc = AggregationManager.getInstance()
				.getAggregation(aggregationFunctions[index].getFunctionName());
		if (aggregationFunc.getParameterDefn() == null) {
			return 0;
		}
		return aggregationFunc.getParameterDefn().length;
	}

	/**
	 *
	 * @param row
	 * @param functionNo
	 * @return
	 * @throws DataException
	 */
	protected boolean getFilterResult(IAggregationResultRow row, int functionNo) throws DataException {
		facttableRow.setMeasure(row.getAggregationValues());
		IJSFacttableFilterEvalHelper filterEvalHelper = (aggregation.getAggregationFunctions()[functionNo])
				.getFilterEvalHelper();
		if (filterEvalHelper == null) {
			return true;
		} else {
			return filterEvalHelper.evaluateFilter(facttableRow);
		}
	}

	/**
	 *
	 * @param row
	 * @return
	 * @throws DataException
	 * @throws IOException
	 */
	protected AggregationResultRow newAggregationResultRow(IAggregationResultRow row)
			throws DataException, IOException {
		AggregationResultRow resultObj = new AggregationResultRow();
		if (keyLevelIndex != null && keyLevelIndex.length > 0) {
			Member[] members = new Member[keyLevelIndex.length];
			for (int i = 0; i < keyLevelIndex.length; i++) {
				members[i] = row.getLevelMembers()[keyLevelIndex[i]];
			}
			resultObj.setLevelMembers(members);
		}
		resultObj.setAggregationValues(new Object[accumulators.length]);

		return resultObj;
	}

	/**
	 *
	 * @return
	 */
	protected String[][] getKeyNames() {
		if (keyLevelIndex == null) {
			return null;
		}
		String[][] result = new String[keyLevelIndex.length][];
		for (int i = 0; i < keyLevelIndex.length; i++) {
			String[][] keyNames = aggrResultSet.getKeyNames();
			if (keyNames != null && keyNames.length > keyLevelIndex[i]) {
				result[i] = keyNames[keyLevelIndex[i]];
			}
		}
		return result;
	}

	/**
	 *
	 * @return
	 */
	protected String[][] getAttributeNames() {
		if (keyLevelIndex == null) {
			return null;
		}
		String[][] result = new String[keyLevelIndex.length][];
		for (int i = 0; i < keyLevelIndex.length; i++) {
			String[][] attributeNames = aggrResultSet.getAttributeNames();
			if (attributeNames != null && attributeNames.length > keyLevelIndex[i]) {
				result[i] = attributeNames[keyLevelIndex[i]];
			}
		}
		return result;
	}
}
