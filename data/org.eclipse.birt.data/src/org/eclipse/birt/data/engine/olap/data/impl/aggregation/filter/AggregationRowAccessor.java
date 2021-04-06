/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IBindingValueFetcher;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;

/**
 * This class is responsible for adapt the current row of the result set so that
 * the filter or sort helpers can access it directly.
 */

public class AggregationRowAccessor extends AbstractRowAccessor {

	private IAggregationResultSet resultSet;
	private IBindingValueFetcher fetcher;
	private Map<String, Object> currentAxisValue;

	/**
	 * 
	 * @param resultSet
	 */
	public AggregationRowAccessor(IAggregationResultSet resultSet, IBindingValueFetcher fetcher) {
		this.resultSet = resultSet;
		this.fetcher = fetcher;
		this.currentAxisValue = new HashMap<String, Object>();
		populateFieldIndexMap();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.
	 * AbstractRowAccessor#populateFieldIndexMap()
	 */
	protected void populateFieldIndexMap() {
		for (int i = 0; i < resultSet.getLevelCount(); i++) {
			DimLevel level = resultSet.getAllLevels()[i];
			for (int j = 0; j < resultSet.getLevelKeyColCount(i); j++) {
				String levelKeyName = resultSet.getLevelKeyName(i, j);
				String name = OlapExpressionUtil.getAttrReference(level.getDimensionName(), level.getLevelName(),
						levelKeyName);
				fieldIndexMap.put(name, new AggregationKeyIndex(i, j));
			}
			String[] attrNames = resultSet.getLevelAttributes(i);
			if (attrNames != null) {
				for (int j = 0; j < attrNames.length; j++) {
					String attrName = parseAttributeName(attrNames[j]);
					String name = OlapExpressionUtil.getAttrReference(level.getDimensionName(), level.getLevelName(),
							attrName);
					fieldIndexMap.put(name, new AggregationAttrIndex(i, j));
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.util.filter.IResultRow#getAggrValue(java.
	 * lang.String)
	 */
	public Object getAggrValue(String aggrName) throws DataException {
		try {
			int aggrIndex = resultSet.getAggregationIndex(aggrName);
			if (aggrIndex == -1)
				return this.getFieldValue(aggrName);
			return resultSet.getAggregationValue(aggrIndex);
		} catch (IOException e) {
			throw new DataException("", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.util.filter.IResultRow#getFieldValue(java.
	 * lang.String)
	 */
	public Object getFieldValue(String fieldName) throws DataException {
		FieldIndex index = (FieldIndex) fieldIndexMap.get(fieldName);
		if (index != null)
			return index.getValue();
		if (this.currentAxisValue.containsKey(fieldName))
			return this.currentAxisValue.get(fieldName);
		if (fetcher != null)
			return fetcher.getValue(fieldName, this, this.resultSet.getPosition());
		return null;
	}

	/**
	 * 
	 */
	class AggregationKeyIndex extends KeyIndex {

		/**
		 * 
		 * @param levelIndex
		 * @param keyIndex
		 */
		AggregationKeyIndex(int levelIndex, int keyIndex) {
			super(levelIndex, keyIndex);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.
		 * AbstractRowAccessor.FieldIndex#getValue()
		 */
		Object getValue() {
			return resultSet.getLevelKeyValue(levelIndex)[keyIndex];
		}
	}

	/**
	 * 
	 */
	class AggregationAttrIndex extends AttributeIndex {

		/**
		 * 
		 * @param levelIndex
		 * @param keyIndex
		 */
		AggregationAttrIndex(int levelIndex, int keyIndex) {
			super(levelIndex, keyIndex);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.
		 * AbstractRowAccessor.FieldIndex#getValue()
		 */
		Object getValue() {
			return resultSet.getLevelAttribute(levelIndex, attrIndex);
		}
	}

	public boolean isTimeDimensionRow() {
		return false;
	}

	public boolean isAxisLevel(String name) {
		return this.currentAxisValue.containsKey(name);
	}

	public void setCurrentAxisValue(Map<String, Object> currentAxisValue) {
		if (currentAxisValue != null)
			this.currentAxisValue = currentAxisValue;
		else
			this.currentAxisValue.clear();
	}
}
