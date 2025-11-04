
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
package org.eclipse.birt.data.engine.olap.impl.query;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.olap.OLAPException;
import jakarta.olap.cursor.Blob;
import jakarta.olap.cursor.Clob;
import jakarta.olap.cursor.CubeCursor;
import jakarta.olap.cursor.Date;
import jakarta.olap.cursor.DimensionCursor;
import jakarta.olap.cursor.RowDataMetaData;
import jakarta.olap.cursor.Time;
import jakarta.olap.cursor.Timestamp;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.api.ICubeCursor;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.eclipse.birt.data.engine.olap.query.view.CubeQueryDefinitionUtil;
import org.eclipse.birt.data.engine.olap.script.JSCubeBindingObject;
import org.eclipse.birt.data.engine.olap.script.OLAPExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Scriptable;

/**
 *
 */

public class CubeCursorImpl implements ICubeCursor {
	private CubeCursor cursor;
	private Scriptable scope;
	private ICubeQueryDefinition queryDefn;
	private HashMap bindingMap, dataTypeMap;
	private Map<String, DimLevel> dimLevelMap;
	private Set validBindingSet;
	private Scriptable outerResults;
	private BirtCubeView cubeView;
	private ScriptContext cx;
	private Map<DimLevel, DimensionCursor> dimensionCursorMap;

	public CubeCursorImpl(IBaseQueryResults outerResults, CubeCursor cursor, Scriptable scope, ScriptContext cx,
			ICubeQueryDefinition queryDefn, BirtCubeView view) throws DataException {
		this.cursor = cursor;
		this.scope = scope;
		this.queryDefn = queryDefn;
		this.cubeView = view;
		this.cx = cx;

		this.dimensionCursorMap = new HashMap<>();
		populateDimensionCursor();
		this.outerResults = OlapExpressionUtil.createQueryResultsScriptable(outerResults);

		this.bindingMap = new HashMap();
		this.dimLevelMap = new HashMap<>();
		this.validBindingSet = new HashSet();
		this.dataTypeMap = new HashMap();
		List<IBinding> allBindings = CubeQueryDefinitionUtil.getAllBindings(queryDefn);
		for (int i = 0; i < allBindings.size(); i++) {
			IBinding binding = (IBinding) allBindings.get(i);
			final String bindingName = binding.getBindingName();
			validBindingSet.add(bindingName);
			final IBaseExpression expr = binding.getExpression();
			if (binding.getAggrFunction() == null) {
				this.bindingMap.put(bindingName, expr);
				if (expr instanceof IScriptExpression) {
					if (!isSimpleDimensionExpression(((IScriptExpression) expr).getText())) {
						OLAPExpressionCompiler.compile(cx.newContext(this.scope), expr);
					}
				} else {
					OLAPExpressionCompiler.compile(cx.newContext(this.scope), expr);
				}
			}
			dataTypeMap.put(bindingName, Integer.valueOf(binding.getDataType()));
		}

		this.scope.put(ScriptConstants.DATA_BINDING_SCRIPTABLE, this.scope, new JSCubeBindingObject(this));
		this.scope.put(ScriptConstants.DATA_SET_BINDING_SCRIPTABLE, this.scope, new JSCubeBindingObject(this));
	}

	private static boolean isSimpleDimensionExpression(String expr) throws DataException {
		if (expr != null && expr.matches("\\Qdimension[\"\\E.*\\Q\"][\"\\E.*\\Q\"]\\E")) {
			Set<DimLevel> s = OlapExpressionCompiler.getReferencedDimLevel(new ScriptExpression(expr),
					Collections.EMPTY_LIST);
			if (s.size() > 1) {
				return false;
			}
			return true;
		}
		return false;
	}

	public boolean nextMeasure() throws OLAPException, IOException {
		if (cursor instanceof org.eclipse.birt.data.engine.olap.cursor.CubeCursorImpl) {
			return ((org.eclipse.birt.data.engine.olap.cursor.CubeCursorImpl) cursor).nextMeasure();
		}
		return false;
	}

	@Override
	public List getOrdinateEdge() throws OLAPException {
		return this.cursor.getOrdinateEdge();
	}

	@Override
	public Collection getPageEdge() throws OLAPException {
		return this.cursor.getPageEdge();
	}

	@Override
	public void synchronizePages() throws OLAPException {
		this.cursor.synchronizePages();
	}

	@Override
	public void close() throws OLAPException {
		this.cursor.close();
	}

	@Override
	public InputStream getAsciiStream(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getAsciiStream(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BigDecimal getBigDecimal(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getBinaryStream(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getBinaryStream(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob getBlob(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Blob getBlob(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean getBoolean(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getBoolean(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public byte getByte(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte getByte(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] getBytes(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] getBytes(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getCharacterStream(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Reader getCharacterStream(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob getClob(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Clob getClob(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(int arg0, Calendar arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Date getDate(String arg0, Calendar arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getDouble(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDouble(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getFloat(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public float getFloat(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInt(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInt(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLong(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getLong(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public RowDataMetaData getMetaData() throws OLAPException {
		return this.cursor.getMetaData();
	}

	@Override
	public Object getObject(int arg0) throws OLAPException {
		return null;
	}

	@Override
	public Object getObject(String arg0) throws OLAPException {
		Object result = null;

		if (!validBindingSet.contains(arg0)) {
			if (arg0.equals(ScriptConstants.OUTER_RESULT_KEYWORD)) {
				if (this.outerResults != null) {
					return this.outerResults;
				} else {
					throw new OLAPException(ResourceConstants.NO_OUTER_RESULTS_EXIST);
				}
			} else {
				throw new OLAPException(DataResourceHandle.getInstance()
						.getMessage(ResourceConstants.COLUMN_BINDING_NOT_EXIST, new String[] { arg0 }));
			}
		} else if (this.bindingMap.get(arg0) == null) {
			result = this.cursor.getObject(arg0);
		} else {
			try {
				IBaseExpression expr = (IBaseExpression) this.bindingMap.get(arg0);
				DimLevel dimLevel;
				try {
					if (dimLevelMap.containsKey(arg0)) {
						dimLevel = dimLevelMap.get(arg0);
					} else {
						dimLevel = OlapExpressionUtil.getTargetDimLevel(((ScriptExpression) expr).getText());
						dimLevelMap.put(arg0, dimLevel);
					}
				} catch (Exception ex) {
					dimLevel = null;
					dimLevelMap.put(arg0, null);
				}

				if (dimLevel != null) {
					DimensionCursor dimCursor = this.dimensionCursorMap.get(dimLevel);
					if (dimCursor != null) {
						try {
							if (dimLevel.getAttrName() != null) {
								result = dimCursor.getObject(OlapExpressionUtil
										.getAttributeColumnName(dimLevel.getLevelName(), dimLevel.getAttrName()));
							} else {
								result = dimCursor.getObject(dimLevel.getLevelName());
							}
						} catch (Exception e) {
							result = null;
						}
					} else {
						result = ScriptEvalUtil.evalExpr(expr, cx.newContext(scope),
								org.eclipse.birt.core.script.ScriptExpression.defaultID, 0);
					}
				} else {
					result = ScriptEvalUtil.evalExpr(expr, cx.newContext(scope),
							org.eclipse.birt.core.script.ScriptExpression.defaultID, 0);
				}
			} catch (Exception e) {
				throw new OLAPException(e.getLocalizedMessage());
			}

		}

		if (result instanceof DataException) {
			throw new OLAPException(((DataException) result).getLocalizedMessage());
		}

		if (this.dataTypeMap.containsKey(arg0)) {
			try {
				result = DataTypeUtil.convert(JavascriptEvalUtil.convertJavascriptValue(result),
						((Integer) this.dataTypeMap.get(arg0)).intValue());
			} catch (BirtException e) {
				throw new OLAPException(e.getLocalizedMessage());
			}
		}
		return result;
	}

	private void populateDimensionCursor() throws OLAPException {
		if (this.cubeView.getPageEdgeView() != null && this.queryDefn.getEdge(ICubeQueryDefinition.PAGE_EDGE) != null) {
			populateDimensionObjects(this.queryDefn.getEdge(ICubeQueryDefinition.PAGE_EDGE).getDimensions(),
					cubeView.getPageEdgeView().getEdgeCursor().getDimensionCursor().iterator());
		}

		/*
		 * Populate Row Edge dimension objects.
		 */
		if (cubeView.getRowEdgeView() != null && this.queryDefn.getEdge(ICubeQueryDefinition.ROW_EDGE) != null) {
			populateDimensionObjects(this.queryDefn.getEdge(ICubeQueryDefinition.ROW_EDGE).getDimensions(),
					cubeView.getRowEdgeView().getEdgeCursor().getDimensionCursor().iterator());
		}

		/*
		 * Populate Column Edge dimension objects.
		 */
		if (cubeView.getColumnEdgeView() != null && this.queryDefn.getEdge(ICubeQueryDefinition.COLUMN_EDGE) != null) {
			populateDimensionObjects(this.queryDefn.getEdge(ICubeQueryDefinition.COLUMN_EDGE).getDimensions(),
					cubeView.getColumnEdgeView().getEdgeCursor().getDimensionCursor().iterator());
		}
	}

	private void populateDimensionObjects(List<IDimensionDefinition> dimensions, Iterator iterator) {
		for (int i = 0; i < dimensions.size(); i++) {
			IDimensionDefinition dimDefn = (IDimensionDefinition) dimensions.get(i);
			IHierarchyDefinition hier = (IHierarchyDefinition) dimDefn.getHierarchy().get(0);
			for (int j = 0; j < hier.getLevels().size(); j++) {
				dimensionCursorMap.put(new DimLevel(dimDefn.getName(), hier.getLevels().get(j).getName()),
						(DimensionCursor) iterator.next());
			}
		}
	}

	@Override
	public Object getObject(int arg0, Map arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getObject(String arg0, Map arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public short getShort(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public short getShort(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getString(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getString(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(int arg0, Calendar arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Time getTime(String arg0, Calendar arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(int arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(String arg0) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(int arg0, Calendar arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Timestamp getTimestamp(String arg0, Calendar arg1) throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() throws OLAPException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(String value) throws OLAPException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setName(String value) throws OLAPException {
		// TODO Auto-generated method stub

	}

	@Override
	public Scriptable getScope() {
		return this.scope;
	}

	@Override
	public java.lang.Object clone() {
		return null;
	}

	public BirtCubeView getCubeView() {
		return this.cubeView;
	}
}
