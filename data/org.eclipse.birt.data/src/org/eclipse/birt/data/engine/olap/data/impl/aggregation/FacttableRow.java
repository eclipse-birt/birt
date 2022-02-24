
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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.data.api.MeasureInfo;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.util.filter.IFacttableRow;

/**
 * 
 */

public class FacttableRow implements IFacttableRow {
	private MeasureInfo[] measureInfo;
	private Object[] measureValues;
	private ICubeDimensionReader cubeDimensionReader;
	private IDataSet4Aggregation.MetaInfo metaInfo;
	private int[] dimPos;

	/**
	 * 
	 * @param measureInfo
	 */
	FacttableRow(MeasureInfo[] measureInfo, ICubeDimensionReader cubeDimensionReader,
			IDataSet4Aggregation.MetaInfo metaInfo) {
		this.measureInfo = measureInfo;
		this.cubeDimensionReader = cubeDimensionReader;
		this.metaInfo = metaInfo;
	}

	/**
	 * 
	 * @param measureValues
	 */
	void setMeasure(Object[] measureValues) {
		this.measureValues = measureValues;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.util.filter.IFacttableRow#getMeasureValue(
	 * java.lang.String)
	 */
	public Object getMeasureValue(String measureName) throws DataException {
		for (int i = 0; i < measureInfo.length; i++) {
			if (measureInfo[i].getMeasureName().equals(measureName)) {
				return measureValues[i];
			}
		}
		return null;
	}

	public void setDimPos(int[] dimPos) {
		this.dimPos = dimPos;
	}

	public Object getLevelAttributeValue(String dimensionName, String levelName, String attribute)
			throws IOException, DataException {
		if (cubeDimensionReader == null || metaInfo == null)
			return null;
		try {
			Member member = getLevelMember(dimensionName, levelName);
			int attributeIndex = getAttributeIndex(dimensionName, levelName, attribute);
			if (member != null && attributeIndex >= 0)
				return member.getAttributes()[attributeIndex];
			return null;
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}

	public Object[] getLevelKeyValue(String dimensionName, String levelName) throws IOException, DataException {
		try {
			if (cubeDimensionReader == null || metaInfo == null)
				return null;
			Member member = getLevelMember(dimensionName, levelName);
			if (member != null)
				return member.getKeyValues();
			return null;
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}

	private Member getLevelMember(String dimensionName, String levelName) throws BirtException, IOException {
		int dimIndex = getDimensionIndex(dimensionName);
		if (dimIndex < 0)
			throw new DataException(ResourceConstants.DIMENSION_NOT_EXIST, dimensionName);
		int levelIndex = getLevelIndex(dimensionName, levelName);
		if (levelIndex < 0)
			throw new DataException(ResourceConstants.LEVEL_NAME_NOT_FOUND, dimensionName + "." + levelName);
		return cubeDimensionReader.getLevelMember(dimIndex, levelIndex, dimPos[dimIndex]);
	}

	private int getAttributeIndex(String dimensionName, String levelName, String attributeName) throws DataException {
		int dimIndex = getDimensionIndex(dimensionName);
		if (dimIndex < 0)
			throw new DataException(
					DataResourceHandle.getInstance().getMessage(ResourceConstants.DIMENSION_NOT_EXIST) + dimensionName);
		int levelIndex = getLevelIndex(dimensionName, levelName);
		if (levelIndex < 0)
			throw new DataException(ResourceConstants.LEVEL_NAME_NOT_FOUND, dimensionName + "." + levelName);
		String[] attributeNames = metaInfo.getAttributeNames(dimIndex, levelIndex);
		if (attributeNames == null || attributeNames.length == 0)
			return -1;
		for (int i = 0; i < attributeNames.length; i++) {
			if (attributeNames[i] != null && attributeNames[i].equals(attributeName))
				return i;
		}
		return -1;
	}

	private int getDimensionIndex(String dimensionName) {
		return metaInfo.getDimensionIndex(dimensionName);
	}

	private int getLevelIndex(String dimensionName, String levelName) {
		return metaInfo.getLevelIndex(dimensionName, levelName);
	}
}
