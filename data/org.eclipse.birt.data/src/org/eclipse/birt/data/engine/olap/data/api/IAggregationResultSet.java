
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
	public int getLevelCount();

	/**
	 * 
	 * @return
	 */
	public DimLevel[] getAllLevels();

	/**
	 * 
	 * @param level
	 * @return
	 */
	public int getLevelIndex(DimLevel level);

	/**
	 * 
	 * @param levelIndex
	 * @return
	 */
	public int getLevelKeyColCount(int levelIndex);

	/**
	 * 
	 * @param levelIndex
	 * @return
	 */
	public int getLevelAttributeColCount(int levelIndex);

	/**
	 * 
	 * @param level
	 * @return
	 */
	public int getLevelKeyDataType(DimLevel level, String keyName);

	/**
	 * 
	 * @param levelName
	 * @return
	 */
	public int getLevelKeyDataType(int levelIndex, String keyName);

	/**
	 * 
	 * @param levelIndex
	 * @param attributeName
	 * @return
	 */
	public int getLevelAttributeIndex(int levelIndex, String attributeName);

	/**
	 * 
	 * @param level
	 * @param attributeName
	 * @return
	 */
	public int getLevelAttributeIndex(DimLevel level, String attributeName);

	/**
	 * 
	 * @param levelIndex
	 * @param attributeName
	 * @return
	 */
	public int getLevelKeyIndex(int levelIndex, String keyName);

	/**
	 * 
	 * @param level
	 * @param attributeName
	 * @return
	 */
	public int getLevelKeyIndex(DimLevel level, String keyName);

	/**
	 * 
	 * @param levelIndex
	 * @return
	 */
	public String[] getLevelAttributes(int levelIndex);

	/**
	 * 
	 * @param levelIndex
	 * @return
	 */
	public Object[] getLevelAttributesValue(int levelIndex);

	/**
	 * 
	 * @param level
	 * @param attributeName
	 * @return
	 */
	public int getLevelAttributeDataType(DimLevel level, String attributeName);

	/**
	 * 
	 * @param levelName
	 * @param attributeName
	 * @return
	 */
	public int getLevelAttributeDataType(int levelIndex, String attributeName);

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	public int getAggregationDataType(int aggregationIndex) throws IOException;

	/**
	 * 
	 * @param index
	 * @throws IOException
	 */
	public void seek(int index) throws IOException;

	/**
	 * 
	 * @return
	 */
	public int length();

	/**
	 * 
	 * @param levelIndex
	 * @return
	 */
	public Object[] getLevelKeyValue(int levelIndex);

	/**
	 * 
	 * @param levelIndex
	 * @param attributeIndex
	 * @return
	 */
	public Object getLevelAttribute(int levelIndex, int attributeIndex);

	/**
	 * 
	 * @param name
	 * @return
	 * @throws IOException
	 */
	public int getAggregationIndex(String name) throws IOException;

	/**
	 * 
	 * @param index
	 * @return
	 */
	public String getAggregationName(int index);

	/**
	 * 
	 * @param aggregationIndex
	 * @return
	 * @throws IOException
	 */
	public Object getAggregationValue(int aggregationIndex) throws IOException;

	/**
	 * 
	 * @param levelIndex
	 * @return
	 */
	public int getSortType(int levelIndex);

	public IAggregationResultRow getCurrentRow() throws IOException;

	public DimLevel getLevel(int levelIndex);

	public String getLevelKeyName(int levelIndex, int keyIndex);

	public AggregationDefinition getAggregationDefinition();

	public String[][] getAttributeNames();

	public String[][] getKeyNames();

	/**
	 * 
	 * @return
	 */
	public int getPosition();

	public int getAggregationCount();

	public String[][] getLevelKeys();

	public int[][] getLevelKeyDataType();

	public String[][] getLevelAttributes();

	public int[][] getLevelAttributeDataType();

	public int[] getSortType();

	public int[] getAggregationDataType();

	/**
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException;

	/**
	 * 
	 * @throws IOException
	 */
	public void clear() throws IOException;
}
