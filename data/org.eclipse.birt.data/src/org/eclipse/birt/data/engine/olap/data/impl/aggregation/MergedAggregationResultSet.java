
/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.DrilledInfo;

/**
 * The 2 merged IAggregationResultSet must have equal DimLevel[]
 *
 */
public class MergedAggregationResultSet implements IAggregationResultSet {

	private IAggregationResultSet rs1;
	private IAggregationResultSet rs2;

	public MergedAggregationResultSet(IAggregationResultSet rs1, IAggregationResultSet rs2) {
		assert Arrays.deepEquals(rs1.getAllLevels(), rs2.getAllLevels());
		this.rs1 = rs1;
		this.rs2 = rs2;
	}

	@Override
	public void clear() throws IOException {
		rs1.clear();
		rs2.clear();
	}

	@Override
	public void close() throws IOException {
		rs1.close();
		rs2.close();

	}

	@Override
	public int getAggregationCount() {
		return rs1.getAggregationCount() + rs2.getAggregationCount();
	}

	@Override
	public int getAggregationDataType(int aggregationIndex) throws IOException {
		if (aggregationIndex < rs1.getAggregationCount()) {
			return rs1.getAggregationDataType(aggregationIndex);
		}
		return rs2.getAggregationDataType(aggregationIndex - rs1.getAggregationCount());
	}

	@Override
	public AggregationDefinition getAggregationDefinition() {
		AggregationDefinition ad1 = rs1.getAggregationDefinition();
		AggregationDefinition ad2 = rs2.getAggregationDefinition();

		AggregationFunctionDefinition[] afds1 = ad1.getAggregationFunctions();
		AggregationFunctionDefinition[] afds2 = ad2.getAggregationFunctions();

		AggregationFunctionDefinition[] afds = new AggregationFunctionDefinition[afds1.length + afds2.length];
		System.arraycopy(afds1, 0, afds, 0, afds1.length);
		System.arraycopy(afds2, 0, afds, afds1.length, afds2.length);

		AggregationDefinition aggr = new AggregationDefinition(ad1.getLevels(), ad1.getSortTypes(), afds);
		if (ad1.getDrilledInfo() != null) {
			DrilledInfo info = ad1.getDrilledInfo().copy();
			if (ad2.getDrilledInfo() != null) {
				info.getOriginalAggregation().addAll(ad2.getDrilledInfo().getOriginalAggregation());
			}
			aggr.setDrilledInfo(info);
		}
		return aggr;
	}

	@Override
	public int getAggregationIndex(String name) throws IOException {
		int index = rs1.getAggregationIndex(name);
		if (index < 0) {
			index = rs2.getAggregationIndex(name);
			if (index >= 0) {
				index += rs1.getAggregationCount();
			}
		}
		return index;
	}

	@Override
	public String getAggregationName(int index) {
		if (index < rs1.getAggregationCount()) {
			return rs1.getAggregationName(index);
		}
		return rs2.getAggregationName(index - rs1.getAggregationCount());
	}

	@Override
	public Object getAggregationValue(int index) throws IOException {
		if (index < rs1.getAggregationCount()) {
			return rs1.getAggregationValue(index);
		}
		return rs2.getAggregationValue(index - rs1.getAggregationCount());
	}

	@Override
	public String[][] getAttributeNames() {
		return rs1.getAttributeNames();
	}

	@Override
	public DimLevel[] getAllLevels() {
		return rs1.getAllLevels();
	}

	@Override
	public IAggregationResultRow getCurrentRow() throws IOException {
		IAggregationResultRow arr1 = rs1.getCurrentRow();
		IAggregationResultRow arr2 = rs2.getCurrentRow();
		Object[] values1 = arr1.getAggregationValues();
		Object[] values2 = arr2.getAggregationValues();
		Object[] values = new Object[values1.length + values2.length];
		System.arraycopy(values1, 0, values, 0, values1.length);
		System.arraycopy(values2, 0, values, values1.length, values2.length);
		AggregationResultRow arr = new AggregationResultRow();
		arr.setLevelMembers(arr1.getLevelMembers());
		arr.setAggregationValues(values);
		return arr;
	}

	@Override
	public String[][] getKeyNames() {
		return rs1.getKeyNames();
	}

	@Override
	public DimLevel getLevel(int levelIndex) {
		return rs1.getLevel(levelIndex);
	}

	@Override
	public Object getLevelAttribute(int levelIndex, int attributeIndex) {
		return rs1.getLevelAttribute(levelIndex, attributeIndex);
	}

	@Override
	public int getLevelAttributeColCount(int levelIndex) {
		return rs1.getLevelAttributeColCount(levelIndex);
	}

	@Override
	public int getLevelAttributeDataType(DimLevel level, String attributeName) {
		return rs1.getLevelAttributeDataType(level, attributeName);
	}

	@Override
	public int getLevelAttributeDataType(int levelIndex, String attributeName) {
		return rs1.getLevelAttributeDataType(levelIndex, attributeName);
	}

	@Override
	public int getLevelAttributeIndex(int levelIndex, String attributeName) {
		return rs1.getLevelAttributeIndex(levelIndex, attributeName);
	}

	@Override
	public int getLevelAttributeIndex(DimLevel level, String attributeName) {
		return rs1.getLevelAttributeIndex(level, attributeName);
	}

	@Override
	public String[] getLevelAttributes(int levelIndex) {
		return rs1.getLevelAttributes(levelIndex);
	}

	@Override
	public int getLevelCount() {
		return rs1.getLevelCount();
	}

	@Override
	public int getLevelIndex(DimLevel level) {
		return rs1.getLevelIndex(level);
	}

	@Override
	public int getLevelKeyColCount(int levelIndex) {
		return rs1.getLevelKeyColCount(levelIndex);
	}

	@Override
	public int getLevelKeyDataType(DimLevel level, String keyName) {
		return rs1.getLevelKeyDataType(level, keyName);
	}

	@Override
	public int getLevelKeyDataType(int levelIndex, String keyName) {
		return rs1.getLevelKeyDataType(levelIndex, keyName);
	}

	@Override
	public int getLevelKeyIndex(int levelIndex, String keyName) {
		return rs1.getLevelKeyIndex(levelIndex, keyName);
	}

	@Override
	public int getLevelKeyIndex(DimLevel level, String keyName) {
		return rs1.getLevelKeyIndex(level, keyName);
	}

	@Override
	public String getLevelKeyName(int levelIndex, int keyIndex) {
		return rs1.getLevelKeyName(levelIndex, keyIndex);
	}

	@Override
	public Object[] getLevelKeyValue(int levelIndex) {
		return rs1.getLevelKeyValue(levelIndex);
	}

	@Override
	public int getPosition() {
		return rs1.getPosition();
	}

	@Override
	public int getSortType(int levelIndex) {
		return rs1.getSortType(levelIndex);
	}

	@Override
	public int length() {
		return rs1.length();
	}

	@Override
	public void seek(int index) throws IOException {
		rs1.seek(index);
		if (index < rs2.length()) {
			rs2.seek(index);
		}
	}

	@Override
	public int[] getAggregationDataType() {
		int[] types1 = rs1.getAggregationDataType();
		int[] types2 = rs2.getAggregationDataType();
		int[] types = new int[types1.length + types2.length];
		System.arraycopy(types1, 0, types, 0, types1.length);
		System.arraycopy(types2, 0, types, types1.length, types2.length);
		return types;
	}

	@Override
	public int[][] getLevelAttributeDataType() {
		return rs1.getLevelAttributeDataType();
	}

	@Override
	public String[][] getLevelAttributes() {
		return rs1.getLevelAttributes();
	}

	@Override
	public int[][] getLevelKeyDataType() {
		return rs1.getLevelKeyDataType();
	}

	@Override
	public String[][] getLevelKeys() {
		return rs1.getLevelKeys();
	}

	@Override
	public int[] getSortType() {
		return rs1.getSortType();
	}

	@Override
	public Object[] getLevelAttributesValue(int levelIndex) {
		return rs1.getLevelAttributesValue(levelIndex);
	}

}
