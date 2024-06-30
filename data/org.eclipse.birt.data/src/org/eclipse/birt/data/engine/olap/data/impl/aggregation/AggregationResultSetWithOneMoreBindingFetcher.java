
/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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

	@Override
	public void clear() throws IOException {
		ars.clear();

	}

	@Override
	public void close() throws IOException {
		ars.close();

	}

	@Override
	public int getAggregationCount() {
		return ars.getAggregationCount() + 1;
	}

	@Override
	public int getAggregationDataType(int aggregationIndex) throws IOException {
		return getAggregationDataType()[aggregationIndex];
	}

	@Override
	public int[] getAggregationDataType() {
		int[] types = new int[ars.getAggregationCount() + 1];
		System.arraycopy(ars.getAggregationDataType(), 0, types, 0, ars.getAggregationDataType().length);
		types[types.length - 1] = addedAggrExpression.getDataType();
		return types;
	}

	@Override
	public AggregationDefinition getAggregationDefinition() {
		return ars.getAggregationDefinition();
	}

	@Override
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

	@Override
	public String getAggregationName(int index) {
		if (index == this.getAggregationCount() - 1) {
			return addedAggrName;
		}
		return ars.getAggregationName(index);
	}

	@Override
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

	@Override
	public DimLevel[] getAllLevels() {
		return ars.getAllLevels();
	}

	@Override
	public String[][] getAttributeNames() {
		return ars.getAttributeNames();
	}

	@Override
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

	@Override
	public String[][] getKeyNames() {
		return ars.getKeyNames();
	}

	@Override
	public DimLevel getLevel(int levelIndex) {
		return ars.getLevel(levelIndex);
	}

	@Override
	public Object getLevelAttribute(int levelIndex, int attributeIndex) {
		return ars.getLevelAttribute(levelIndex, attributeIndex);
	}

	@Override
	public int getLevelAttributeColCount(int levelIndex) {
		return ars.getLevelAttributeColCount(levelIndex);
	}

	@Override
	public int getLevelAttributeDataType(DimLevel level, String attributeName) {
		return ars.getLevelAttributeDataType(level, attributeName);
	}

	@Override
	public int getLevelAttributeDataType(int levelIndex, String attributeName) {
		return ars.getLevelAttributeDataType(levelIndex, attributeName);
	}

	@Override
	public int[][] getLevelAttributeDataType() {
		return ars.getLevelAttributeDataType();
	}

	@Override
	public int getLevelAttributeIndex(int levelIndex, String attributeName) {
		return ars.getLevelAttributeIndex(levelIndex, attributeName);
	}

	@Override
	public int getLevelAttributeIndex(DimLevel level, String attributeName) {
		return ars.getLevelAttributeIndex(level, attributeName);
	}

	@Override
	public String[] getLevelAttributes(int levelIndex) {
		return ars.getLevelAttributes(levelIndex);
	}

	@Override
	public String[][] getLevelAttributes() {
		return ars.getLevelAttributes();
	}

	@Override
	public int getLevelCount() {
		return ars.getLevelCount();
	}

	@Override
	public int getLevelIndex(DimLevel level) {
		return ars.getLevelIndex(level);
	}

	@Override
	public int getLevelKeyColCount(int levelIndex) {
		return ars.getLevelKeyColCount(levelIndex);
	}

	@Override
	public int getLevelKeyDataType(DimLevel level, String keyName) {
		return ars.getLevelKeyDataType(level, keyName);
	}

	@Override
	public int getLevelKeyDataType(int levelIndex, String keyName) {
		return ars.getLevelKeyDataType(levelIndex, keyName);
	}

	@Override
	public int[][] getLevelKeyDataType() {
		return ars.getLevelKeyDataType();
	}

	@Override
	public int getLevelKeyIndex(int levelIndex, String keyName) {
		return ars.getLevelKeyIndex(levelIndex, keyName);
	}

	@Override
	public int getLevelKeyIndex(DimLevel level, String keyName) {
		return ars.getLevelKeyIndex(level, keyName);
	}

	@Override
	public String getLevelKeyName(int levelIndex, int keyIndex) {
		return ars.getLevelKeyName(levelIndex, keyIndex);
	}

	@Override
	public Object[] getLevelKeyValue(int levelIndex) {
		return ars.getLevelKeyValue(levelIndex);
	}

	@Override
	public String[][] getLevelKeys() {
		return ars.getLevelKeys();
	}

	@Override
	public int getPosition() {
		return ars.getPosition();
	}

	@Override
	public int getSortType(int levelIndex) {
		return ars.getSortType(levelIndex);
	}

	@Override
	public int[] getSortType() {
		return ars.getSortType();
	}

	@Override
	public int length() {
		return ars.length();
	}

	@Override
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
		@Override
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
		@Override
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

	@Override
	public Object[] getLevelAttributesValue(int levelIndex) {
		return ars.getLevelAttributesValue(levelIndex);
	}
}
