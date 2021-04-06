
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
package org.eclipse.birt.data.engine.olap.util.filter;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IDataScriptEngine;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.script.OLAPExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.DataJSObjectPopulator;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * 
 */

public class JSFacttableFilterEvalHelper implements IJSFacttableFilterEvalHelper {

	private ScriptableObject scope;
	private DummyMeasureObject measureObj;
	private DummyDimensionObject dimObj;
	private IBaseExpression expr;
	private ScriptContext cx;

	public JSFacttableFilterEvalHelper(Scriptable parentScope, ScriptContext cx, IFilterDefinition cubeFilter,
			IBaseQueryResults outerResults, ICubeQueryDefinition query) throws DataException {
		assert cubeFilter != null;
		initialize(parentScope, cubeFilter, cx, outerResults, query);
	}

	public IBaseExpression getFilterExpression() {
		return this.expr;
	}

	/**
	 * 
	 * @param parentScope
	 * @param cubeFilter
	 * @param cx
	 * @throws DataException
	 */
	private void initialize(Scriptable parentScope, IFilterDefinition cubeFilter, ScriptContext cx,
			IBaseQueryResults outerResults, ICubeQueryDefinition query) throws DataException {
		try {
			this.scope = ((IDataScriptEngine) (cx.getScriptEngine(IDataScriptEngine.ENGINE_NAME))).getJSContext(cx)
					.initStandardObjects();
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
		this.scope.setParentScope(parentScope);
		this.measureObj = new DummyMeasureObject();
		this.dimObj = new DummyDimensionObject();
		this.expr = cubeFilter.getExpression();
		OLAPExpressionCompiler.compile(cx.newContext(this.scope), this.expr);
		this.cx = cx;
		this.scope.put(org.eclipse.birt.data.engine.script.ScriptConstants.MEASURE_SCRIPTABLE, this.scope,
				this.measureObj);
		this.scope.put(org.eclipse.birt.data.engine.script.ScriptConstants.DIMENSION_SCRIPTABLE, this.scope,
				this.dimObj);
		if (query != null) {
			try {
				new DataJSObjectPopulator(outerResults, scope, query.getBindings(), false, cx).doInit();
			} catch (DataException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.util.filter.IJSMeasureFilterEvalHelper#
	 * evaluateFilter(org.eclipse.birt.data.engine.olap.util.filter.IFacttableRow)
	 */
	public boolean evaluateFilter(IFacttableRow facttableRow) throws DataException {
		this.measureObj.setCurrentRow(facttableRow);
		this.dimObj.setCurrentRow(facttableRow);
		try {
			Object result = ScriptEvalUtil.evalExpr(expr, cx.newContext(scope), ScriptExpression.defaultID, 0);
			return DataTypeUtil.toBoolean(result).booleanValue();
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}

	private static class DummyLevelObject extends ScriptableObject {
		private static final long serialVersionUID = 1L;
		private DummyDimensionObject host;
		private String dimName;
		private Map<String, DummyLevelAttrObject> levelAttrMap;

		public DummyLevelObject(DummyDimensionObject host, String dimName) {
			this.host = host;
			this.dimName = dimName;
			this.levelAttrMap = new HashMap<String, DummyLevelAttrObject>();
		}

		public Object get(String levelName, Scriptable scope) {
			try {
				if (this.levelAttrMap.containsKey(levelName))
					return this.levelAttrMap.get(levelName);
				else {
					this.levelAttrMap.put(levelName, new DummyLevelAttrObject(host, dimName, levelName));
					return this.levelAttrMap.get(levelName);
				}
			} catch (Exception e) {
				return null;
			}
		}

		public String getClassName() {
			return "DummyLevelObject";
		}
	}

	private static class DummyDimensionObject extends ScriptableObject {
		private static final long serialVersionUID = 1L;
		private IFacttableRow row;
		private Map<String, DummyLevelObject> dimLevMap = new HashMap<String, DummyLevelObject>();

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mozilla.javascript.ScriptableObject#getClassName()
		 */
		public String getClassName() {
			return "DummyDimensionObject";
		}

		/**
		 * Set the current row for the evaluation.
		 * 
		 * @param row
		 */
		public void setCurrentRow(IFacttableRow row) {
			this.row = row;
		}

		public Object get(String dimName, Scriptable scope) {
			try {
				if (this.dimLevMap.containsKey(dimName))
					return this.dimLevMap.get(dimName);
				else {
					this.dimLevMap.put(dimName, new DummyLevelObject(this, dimName));
					return this.dimLevMap.get(dimName);
				}
			} catch (Exception e) {
				return null;
			}
		}

	}

	private static class DummyLevelAttrObject extends ScriptableObject {
		private static final long serialVersionUID = 1L;
		private String dimName;
		private String levelName;
		private DummyDimensionObject host;

		public DummyLevelAttrObject(DummyDimensionObject host, String dimName, String levelName) {
			this.host = host;
			this.dimName = dimName;
			this.levelName = levelName;
		}

		public String getClassName() {
			return "DummyLevelAttrObject";
		}

		public Object get(String attrName, Scriptable scope) {

			try {
				if (this.levelName.equals(attrName))
					return this.getDefaultValue(null);
				return this.host.row.getLevelAttributeValue(this.dimName, this.levelName,
						OlapExpressionUtil.getAttributeColumnName(this.levelName, attrName));
			} catch (Exception e) {
				return null;
			}
		}

		public Object getDefaultValue(Class hint) {
			try {
				Object[] value = this.host.row.getLevelKeyValue(this.dimName, this.levelName);
				if (value != null && value.length > 0)
					return value[0];
				return null;
			} catch (Exception e) {
				return null;
			}
		}
	}

	/**
	 * 
	 *
	 */
	private static class DummyMeasureObject extends ScriptableObject {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		//
		private IFacttableRow row;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mozilla.javascript.ScriptableObject#getClassName()
		 */
		public String getClassName() {
			return "DummyMeasureObject";
		}

		/**
		 * Set the current row for the evaluation.
		 * 
		 * @param row
		 */
		public void setCurrentRow(IFacttableRow row) {
			this.row = row;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.mozilla.javascript.ScriptableObject#get(java.lang.String,
		 * org.mozilla.javascript.Scriptable)
		 */
		public Object get(String measureName, Scriptable scope) {
			try {
				return this.row.getMeasureValue(measureName);
			} catch (DataException e) {
				return null;
			}
		}
	}
}
