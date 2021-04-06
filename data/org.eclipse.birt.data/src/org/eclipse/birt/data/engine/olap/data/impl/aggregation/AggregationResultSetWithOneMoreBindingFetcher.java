
/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IBindingValueFetcher;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.AggregationRowAccessor;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * 
 */

public class AggregationResultSetWithOneMoreBindingFetcher implements IAggregationResultSet {
	private static Logger logger = Logger.getLogger(AggregationResultSetWithOneMoreBindingFetcher.class.getName());

	private IAggregationResultSet ars;
	private String addedAggrName;
	private IBaseExpression addedAggrExpression;
	private Scriptable scope;
	private ScriptContext cx;

	public AggregationResultSetWithOneMoreBindingFetcher(IAggregationResultSet aggregationResultSet,
			String addedAggrName, IBaseExpression addedAggrExpression, IBindingValueFetcher fetcher, Scriptable scope,
			ScriptContext cx) {
		this.ars = aggregationResultSet;
		this.addedAggrName = addedAggrName;
		this.addedAggrExpression = addedAggrExpression;
		this.scope = scope;
		this.cx = cx;
		this.scope.put(ScriptConstants.DATA_BINDING_SCRIPTABLE, this.scope, new JSDataObject(fetcher));
		this.scope.put(ScriptConstants.DATA_SET_BINDING_SCRIPTABLE, this.scope, new JSDataObject(fetcher));
		this.scope.put(ScriptConstants.DIMENSION_SCRIPTABLE, this.scope, new JSDimensionObject());
	}

	public void clear() throws IOException {
		ars.clear();

	}

	public void close() throws IOException {
		ars.close();

	}

	public int getAggregationCount() {
		return ars.getAggregationCount() + 1;
	}

	public int getAggregationDataType(int aggregationIndex) throws IOException {
		return getAggregationDataType()[aggregationIndex];
	}

	public int[] getAggregationDataType() {
		int[] types = new int[ars.getAggregationCount() + 1];
		System.arraycopy(ars.getAggregationDataType(), 0, types, 0, ars.getAggregationDataType().length);
		types[types.length - 1] = addedAggrExpression.getDataType();
		return types;
	}

	public AggregationDefinition getAggregationDefinition() {
		return ars.getAggregationDefinition();
	}

	public int getAggregationIndex(String name) throws IOException {
		int index = ars.getAggregationIndex(name);
		if (index < 0) {
			if (addedAggrName.equals(name)) {
				return this.getAggregationCount() - 1; // the added aggregation
			}
			return -1;
		}
		return index;
	}

	public String getAggregationName(int index) {
		if (index == this.getAggregationCount() - 1) {
			return addedAggrName;
		}
		return ars.getAggregationName(index);
	}

	public Object getAggregationValue(int aggregationIndex) throws IOException {
		if (aggregationIndex == this.getAggregationCount() - 1) {
			try {
				return ScriptEvalUtil.evalExpr(addedAggrExpression, cx.newContext(scope), ScriptExpression.defaultID,
						0);
			} catch (DataException e) {
				logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
				throw new IOException(e.getLocalizedMessage());
			}
		}
		return ars.getAggregationValue(aggregationIndex);
	}

	public DimLevel[] getAllLevels() {
		return ars.getAllLevels();
	}

	public String[][] getAttributeNames() {
		return ars.getAttributeNames();
	}

	public IAggregationResultRow getCurrentRow() throws IOException {
		IAggregationResultRow arr = ars.getCurrentRow();

		Object[] values1 = arr.getAggregationValues();
		Object[] values = new Object[values1.length + 1];
		System.arraycopy(values1, 0, values, 0, values1.length);
		values[values.length - 1] = getAggregationValue(this.getAggregationCount() - 1);
		AggregationResultRow result = new AggregationResultRow();
		result.setLevelMembers(arr.getLevelMembers());
		result.setAggregationValues(values);
		return result;
	}

	public String[][] getKeyNames() {
		return ars.getKeyNames();
	}

	public DimLevel getLevel(int levelIndex) {
		return ars.getLevel(levelIndex);
	}

	public Object getLevelAttribute(int levelIndex, int attributeIndex) {
		return ars.getLevelAttribute(levelIndex, attributeIndex);
	}

	public int getLevelAttributeColCount(int levelIndex) {
		return ars.getLevelAttributeColCount(levelIndex);
	}

	public int getLevelAttributeDataType(DimLevel level, String attributeName) {
		return ars.getLevelAttributeDataType(level, attributeName);
	}

	public int getLevelAttributeDataType(int levelIndex, String attributeName) {
		return ars.getLevelAttributeDataType(levelIndex, attributeName);
	}

	public int[][] getLevelAttributeDataType() {
		return ars.getLevelAttributeDataType();
	}

	public int getLevelAttributeIndex(int levelIndex, String attributeName) {
		return ars.getLevelAttributeIndex(levelIndex, attributeName);
	}

	public int getLevelAttributeIndex(DimLevel level, String attributeName) {
		return ars.getLevelAttributeIndex(level, attributeName);
	}

	public String[] getLevelAttributes(int levelIndex) {
		return ars.getLevelAttributes(levelIndex);
	}

	public String[][] getLevelAttributes() {
		return ars.getLevelAttributes();
	}

	public int getLevelCount() {
		return ars.getLevelCount();
	}

	public int getLevelIndex(DimLevel level) {
		return ars.getLevelIndex(level);
	}

	public int getLevelKeyColCount(int levelIndex) {
		return ars.getLevelKeyColCount(levelIndex);
	}

	public int getLevelKeyDataType(DimLevel level, String keyName) {
		return ars.getLevelKeyDataType(level, keyName);
	}

	public int getLevelKeyDataType(int levelIndex, String keyName) {
		return ars.getLevelKeyDataType(levelIndex, keyName);
	}

	public int[][] getLevelKeyDataType() {
		return ars.getLevelKeyDataType();
	}

	public int getLevelKeyIndex(int levelIndex, String keyName) {
		return ars.getLevelKeyIndex(levelIndex, keyName);
	}

	public int getLevelKeyIndex(DimLevel level, String keyName) {
		return ars.getLevelKeyIndex(level, keyName);
	}

	public String getLevelKeyName(int levelIndex, int keyIndex) {
		return ars.getLevelKeyName(levelIndex, keyIndex);
	}

	public Object[] getLevelKeyValue(int levelIndex) {
		return ars.getLevelKeyValue(levelIndex);
	}

	public String[][] getLevelKeys() {
		return ars.getLevelKeys();
	}

	public int getPosition() {
		return ars.getPosition();
	}

	public int getSortType(int levelIndex) {
		return ars.getSortType(levelIndex);
	}

	public int[] getSortType() {
		return ars.getSortType();
	}

	public int length() {
		return ars.length();
	}

	public void seek(int index) throws IOException {
		ars.seek(index);
	}

	@SuppressWarnings("serial")
	private class JSDataObject extends ScriptableObject {
		private IBindingValueFetcher fetcher;
		private AggregationRowAccessor accessor;

		public JSDataObject(IBindingValueFetcher fetcher) {
			this.fetcher = fetcher;
			accessor = new AggregationRowAccessor(ars, fetcher);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String,
		 * org.mozilla.javascript.Scriptable)
		 */
		public Object get(String arg0, Scriptable scope) {
			try {
				int index = ars.getAggregationIndex(arg0);
				if (index < 0) {
					try {
						return this.fetcher.getValue(arg0, accessor, ars.getPosition());
					} catch (DataException e) {
						throw Context.reportRuntimeError(ResourceConstants.INVALID_NEST_AGGREGATION_EXPRESSION);
					}
				} else {
					return ars.getAggregationValue(index);
				}
			} catch (IOException e) {
				logger.log(Level.WARNING, e.getLocalizedMessage(), e);
				throw Context.reportRuntimeError(e.getLocalizedMessage());
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mozilla.javascript.ScriptableObject#getClassName()
		 */
		public String getClassName() {
			return JSDataObject.class.getName();
		}

	}

	@SuppressWarnings("serial")
	private class JSDimensionObject extends ScriptableObject {

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String,
		 * org.mozilla.javascript.Scriptable)
		 */
		@Override
		public Object get(String name, Scriptable start) {
			return new JSLevelObject(name);
		}

		@Override
		public String getClassName() {
			return JSDimensionObject.class.getName();
		}

	}

	@SuppressWarnings("serial")
	private class JSLevelObject extends ScriptableObject {
		private String dimensionName;

		public JSLevelObject(String dimensionName) {
			this.dimensionName = dimensionName;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String,
		 * org.mozilla.javascript.Scriptable)
		 */
		@Override
		public Object get(String name, Scriptable start) {
			int levelIndex = ars.getLevelIndex(new DimLevel(dimensionName, name));
			if (levelIndex < 0) {
				throw Context.reportRuntimeError(ResourceConstants.INVALID_NEST_AGGREGATION_EXPRESSION);
			}
			return new JSMemberObject(levelIndex);
		}

		@Override
		public String getClassName() {
			return JSLevelObject.class.getName();
		}

	}

	@SuppressWarnings("serial")
	private class JSMemberObject extends ScriptableObject {
		private int levelIndex;

		public JSMemberObject(int levelIndex) {
			this.levelIndex = levelIndex;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mozilla.javascript.ScriptableObject#getDefaultValue(java.lang.Class)
		 */
		@SuppressWarnings("unchecked")
		@Override
		public Object getDefaultValue(Class typeHint) {
			return ars.getLevelKeyValue(levelIndex)[0];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String,
		 * org.mozilla.javascript.Scriptable)
		 */
		@Override
		public Object get(String name, Scriptable start) {
			int keyIndex = ars.getLevelKeyIndex(levelIndex, name);
			if (keyIndex >= 0) {
				return ars.getLevelKeyValue(levelIndex)[keyIndex];
			}
			int attrIndex = ars.getLevelAttributeIndex(keyIndex, name);
			if (attrIndex < 0) {
				throw Context.reportRuntimeError(ResourceConstants.INVALID_NEST_AGGREGATION_EXPRESSION);
			}
			return ars.getLevelAttribute(levelIndex, attrIndex);
		}

		@Override
		public String getClassName() {
			return JSMemberObject.class.getName();
		}

	}

	public Object[] getLevelAttributesValue(int levelIndex) {
		return ars.getLevelAttributesValue(levelIndex);
	}
}
