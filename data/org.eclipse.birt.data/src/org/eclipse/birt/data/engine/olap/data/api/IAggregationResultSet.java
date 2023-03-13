
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
package org.eclipse.birt.data.engine.olap.data.api;

import java.io.IOException;

import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;

/**
 * The interface used to access a set of data rows retrieved by a cube
 * aggregation.
 */

public interface IAggregationResultSet {
	/**
	 *
	 * @return
	 */
	int getLevelCount();

	/**
	 *
	 * @return
	 */
	DimLevel[] getAllLevels();

	/**
	 *
	 * @param level
	 * @return
	 */
	int getLevelIndex(DimLevel level);

	/**
	 *
	 * @param levelIndex
	 * @return
	 */
	int getLevelKeyColCount(int levelIndex);

	/**
	 *
	 * @param levelIndex
	 * @return
	 */
	int getLevelAttributeColCount(int levelIndex);

	/**
	 *
	 * @param level
	 * @return
	 */
	int getLevelKeyDataType(DimLevel level, String keyName);

	/**
	 *
	 * @param levelName
	 * @return
	 */
	int getLevelKeyDataType(int levelIndex, String keyName);

	/**
	 *
	 * @param levelIndex
	 * @param attributeName
	 * @return
	 */
	int getLevelAttributeIndex(int levelIndex, String attributeName);

	/**
	 *
	 * @param level
	 * @param attributeName
	 * @return
	 */
	int getLevelAttributeIndex(DimLevel level, String attributeName);

	/**
	 *
	 * @param levelIndex
	 * @param attributeName
	 * @return
	 */
	int getLevelKeyIndex(int levelIndex, String keyName);

	/**
	 *
	 * @param level
	 * @param attributeName
	 * @return
	 */
	int getLevelKeyIndex(DimLevel level, String keyName);

	/**
	 *
	 * @param levelIndex
	 * @return
	 */
	String[] getLevelAttributes(int levelIndex);

	/**
	 *
	 * @param levelIndex
	 * @return
	 */
	Object[] getLevelAttributesValue(int levelIndex);

	/**
	 *
	 * @param level
	 * @param attributeName
	 * @return
	 */
	int getLevelAttributeDataType(DimLevel level, String attributeName);

	/**
	 *
	 * @param levelName
	 * @param attributeName
	 * @return
	 */
	int getLevelAttributeDataType(int levelIndex, String attributeName);

	/**
	 *
	 * @return
	 * @throws IOException
	 */
	int getAggregationDataType(int aggregationIndex) throws IOException;

	/**
	 *
	 * @param index
	 * @throws IOException
	 */
	void seek(int index) throws IOException;

	/**
	 *
	 * @return
	 */
	int length();

	/**
	 *
	 * @param levelIndex
	 * @return
	 */
	Object[] getLevelKeyValue(int levelIndex);

	/**
	 *
	 * @param levelIndex
	 * @param attributeIndex
	 * @return
	 */
	Object getLevelAttribute(int levelIndex, int attributeIndex);

	/**
	 *
	 * @param name
	 * @return
	 * @throws IOException
	 */
	int getAggregationIndex(String name) throws IOException;

	/**
	 *
	 * @param index
	 * @return
	 */
	String getAggregationName(int index);

	/**
	 *
	 * @param aggregationIndex
	 * @return
	 * @throws IOException
	 */
	Object getAggregationValue(int aggregationIndex) throws IOException;

	/**
	 *
	 * @param levelIndex
	 * @return
	 */
	int getSortType(int levelIndex);

	IAggregationResultRow getCurrentRow() throws IOException;

	DimLevel getLevel(int levelIndex);

	String getLevelKeyName(int levelIndex, int keyIndex);

	AggregationDefinition getAggregationDefinition();

	String[][] getAttributeNames();

	String[][] getKeyNames();

	/**
	 *
	 * @return
	 */
	int getPosition();

	int getAggregationCount();

	String[][] getLevelKeys();

	int[][] getLevelKeyDataType();

	String[][] getLevelAttributes();

	int[][] getLevelAttributeDataType();

	int[] getSortType();

	int[] getAggregationDataType();

	/**
	 *
	 * @throws IOException
	 */
	void close() throws IOException;

	/**
	 *
	 * @throws IOException
	 */
	void clear() throws IOException;
}
