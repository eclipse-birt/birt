
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
package org.eclipse.birt.report.data.adapter.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.eclipse.birt.data.engine.olap.util.OlapQueryUtil;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DimensionLevel;
import org.eclipse.birt.report.data.adapter.api.IBindingMetaInfo;
import org.eclipse.birt.report.data.adapter.api.ICubeQueryUtil;
import org.eclipse.birt.report.data.adapter.api.IDimensionLevel;
import org.eclipse.birt.report.data.adapter.impl.DataSetIterator.ColumnMeta;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.birt.report.model.elements.interfaces.ITabularCubeModel;

/**
 *
 */

public class CubeQueryUtil implements ICubeQueryUtil {

	private DataRequestSessionImpl sessionImpl;

	private static Pattern pattern = Pattern.compile("(\\d+(\\.\\d*)?)*|(\\.\\d*)*");

	public CubeQueryUtil(DataRequestSessionImpl session) {
		this.sessionImpl = session;
	}

	/*
	 * @see
	 * org.eclipse.birt.report.data.adapter.api.ICubeQueryUtil#getReferableBindings(
	 * java.lang.String,
	 * org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition, boolean)
	 */
	@Override
	public List getReferableBindings(String targetLevel, ICubeQueryDefinition cubeDefn, boolean isSort)
			throws AdapterException {
		try {
			List bindings = cubeDefn.getBindings();
			if (bindings == null) {
				return new ArrayList();
			}
			DimLevel target = OlapExpressionUtil.getTargetDimLevel(targetLevel);

			List result = new ArrayList();
			Set<String> derivedBindings = OlapExpressionUtil.getDerivedMeasureNames(bindings);

			for (int i = 0; i < bindings.size(); i++) {
				IBinding binding = (IBinding) bindings.get(i);
				if (isNestAggregation(binding)) {
					result.add(new BindingMetaInfo(binding.getBindingName(), IBindingMetaInfo.MEASURE_TYPE));
					continue;
				}
				if (!OlapExpressionUtil.isDirectRerenrence(binding.getExpression(), bindings)
						|| derivedBindings.contains(binding.getBindingName())
						|| !ExpressionCompilerUtil.extractColumnExpression(binding.getExpression(), "data").isEmpty()) {
					result.add(new BindingMetaInfo(binding.getBindingName(), IBindingMetaInfo.OTHER_TYPE));
					continue;
				}
				Set refDimLevel = OlapExpressionCompiler.getReferencedDimLevel(binding.getExpression(), bindings,
						isSort);
				if (refDimLevel.size() > 1) {
					continue;
				}
				if (!refDimLevel.contains(target)) {
					List aggrOns = binding.getAggregatOns();
					if (isGrandTotal(binding) && isSort) {
						continue;
					}
					if (this.getReferencedMeasureName(binding.getExpression()) != null) {
						if (this.isMeasureBinding(cubeDefn, binding)) {
							result.add(new BindingMetaInfo(binding.getBindingName(), IBindingMetaInfo.MEASURE_TYPE));
							continue;
						} else if (isGrandTotal(binding)) {
							result.add(
									new BindingMetaInfo(binding.getBindingName(), IBindingMetaInfo.GRAND_TOTAL_TYPE));
							continue;
						}
					}

					for (int j = 0; j < aggrOns.size(); j++) {
						DimLevel dimLevel = OlapExpressionUtil.getTargetDimLevel(aggrOns.get(j).toString());
						if (dimLevel.equals(target)) {
							// Only add to result list if the target dimLevel is the leaf level
							// of its own edge that referenced in aggrOns list.
							if (j == aggrOns.size() - 1) {
								if (fromSameEdge(aggrOns, cubeDefn)) {
									result.add(new BindingMetaInfo(binding.getBindingName(),
											IBindingMetaInfo.GRAND_TOTAL_TYPE));
								} else {
									result.add(new BindingMetaInfo(binding.getBindingName(),
											IBindingMetaInfo.SUB_TOTAL_TYPE));
								}
							} else {
								DimLevel next = OlapExpressionUtil.getTargetDimLevel(aggrOns.get(j + 1).toString());

								int candidateEdge = getAxisQualifierEdgeType(dimLevel, cubeDefn);
								if (candidateEdge != -1) {
									if (getAxisQualifierLevel(next, cubeDefn.getEdge(candidateEdge)) != null) {
										result.add(new BindingMetaInfo(binding.getBindingName(),
												IBindingMetaInfo.SUB_TOTAL_TYPE));
									}
								}
							}
							break;
						}
					}
					continue;
				}
				result.add(new BindingMetaInfo(binding.getBindingName(), IBindingMetaInfo.DIMENSION_TYPE));
			}

			return result;
		} catch (DataException e) {
			throw new AdapterException(e.getLocalizedMessage(), e);
		}
	}

	private static boolean isNestAggregation(IBinding b) throws DataException {
		if (b.getAggrFunction() == null) {
			return false;
		}
		List<String> referencedBindings = ExpressionCompilerUtil.extractColumnExpression(b.getExpression(),
				ExpressionUtil.DATA_INDICATOR);
		if (referencedBindings == null || referencedBindings.size() == 0) {
			return false;
		}
		return true;

	}

	/**
	 *
	 * @param queryDefn
	 * @param binding
	 * @return
	 * @throws DataException
	 */
	private boolean isMeasureBinding(ICubeQueryDefinition queryDefn, IBinding binding) throws DataException {
		if (binding.getAggrFunction() == null) {
			return true;
		} else {

			List aggrs = binding.getAggregatOns();

			int coveredLeafLevel = 0;
			for (int i = 0; i < aggrs.size(); i++) {
				DimLevel dimLevel = OlapExpressionUtil.getTargetDimLevel(aggrs.get(i).toString());
				if (this.isLeafLevel(queryDefn, dimLevel)) {
					coveredLeafLevel++;
				}
			}

			if (coveredLeafLevel != this.getEdgeNumber(queryDefn)) {
				return false;
			}
			return true;
		}
	}

	/**
	 *
	 * @param cubeQuery
	 * @return
	 */
	private int getEdgeNumber(ICubeQueryDefinition cubeQuery) {
		int edgeNumber = 0;
		if (cubeQuery.getEdge(ICubeQueryDefinition.COLUMN_EDGE) != null) {
			edgeNumber++;
		}
		if (cubeQuery.getEdge(ICubeQueryDefinition.ROW_EDGE) != null) {
			edgeNumber++;
		}
		return edgeNumber;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.data.adapter.api.ICubeQueryUtil#
	 * getReferableMeasureBindings(java.lang.String,
	 * org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition)
	 */
	@Override
	public List getReferableMeasureBindings(String measureName, ICubeQueryDefinition cubeDefn) throws DataException {
		List result = new ArrayList();
		List bindings = cubeDefn.getBindings();
		for (int i = 0; i < bindings.size(); i++) {
			IBinding binding = (IBinding) bindings.get(i);
			final String referencedMeasureName = getReferencedMeasureName(binding.getExpression());
			if (measureName.equals(referencedMeasureName)) {
				List aggrOns = binding.getAggregatOns();
				if (this.isMeasureBinding(cubeDefn, binding)) {
					result.add(new BindingMetaInfo(binding.getBindingName(), IBindingMetaInfo.MEASURE_TYPE));
				} else if (fromSameEdge(aggrOns, cubeDefn)) {
					result.add(new BindingMetaInfo(binding.getBindingName(), IBindingMetaInfo.GRAND_TOTAL_TYPE));
				} else {
					result.add(new BindingMetaInfo(binding.getBindingName(), IBindingMetaInfo.SUB_TOTAL_TYPE));
				}
			}
		}
		return result;
	}

	@Override
	public List getReferableBindingsForLinkedDataSetCube(String targetLevel, ICubeQueryDefinition cubeQueryDefn,
			boolean isSort) throws AdapterException {
		return getReferableBindings(targetLevel, cubeQueryDefn, isSort);
	}

	@Override
	public List getReferableMeasureBindingsForLinkedDataSetCube(String measureName, ICubeQueryDefinition cubeDefn)
			throws DataException {
		return getReferableMeasureBindings(measureName, cubeDefn);
	}

	/**
	 * Indicate whether the binding stands for an OVERALL grand total.
	 *
	 * @param binding
	 * @return
	 * @throws DataException
	 */
	private boolean isGrandTotal(IBinding binding) throws DataException {
		return binding.getAggregatOns().size() == 0;
	}

	/**
	 *
	 * @param aggrOns
	 * @return
	 * @throws DataException
	 */
	private boolean fromSameEdge(List aggrOns, ICubeQueryDefinition cubeDefn) throws DataException {
		int candidateEdge = -1;
		for (int i = 0; i < aggrOns.size(); i++) {
			DimLevel dimLevel = OlapExpressionUtil.getTargetDimLevel(aggrOns.get(i).toString());
			if (candidateEdge == -1) {
				candidateEdge = getAxisQualifierEdgeType(dimLevel, cubeDefn);
			} else if (candidateEdge != getAxisQualifierEdgeType(dimLevel, cubeDefn)) {
				return false;
			}
		}
		return true;
	}

	/**
	 *
	 * @param query
	 * @param target
	 * @return
	 */
	private boolean isLeafLevel(ICubeQueryDefinition query, DimLevel target) {
		return isLeafLevel(query.getEdge(ICubeQueryDefinition.COLUMN_EDGE), target)
				|| isLeafLevel(query.getEdge(ICubeQueryDefinition.ROW_EDGE), target);

	}

	/**
	 *
	 * @param edge
	 * @param target
	 * @return
	 */
	private boolean isLeafLevel(IEdgeDefinition edge, DimLevel target) {
		if (edge == null) {
			return false;
		}
		IDimensionDefinition dim = (IDimensionDefinition) edge.getDimensions().get(edge.getDimensions().size() - 1);
		if (dim.getName().equals(target.getDimensionName())) {
			IHierarchyDefinition hier = (IHierarchyDefinition) dim.getHierarchy().get(0);
			ILevelDefinition level = (ILevelDefinition) hier.getLevels().get(hier.getLevels().size() - 1);
			if (target.getLevelName().equals(level.getName())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.data.adapter.api.DataRequestSession#
	 * getReferencedLevels(java.lang.String, java.lang.String,
	 * org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition)
	 */
	@Override
	public List getReferencedLevels(String targetLevel, String bindingExpr, ICubeQueryDefinition queryDefn)
			throws AdapterException {
		try {
			List result = new ArrayList();
			DimLevel target = OlapExpressionUtil.getTargetDimLevel(targetLevel);

			String bindingName = OlapExpressionCompiler.getReferencedScriptObject(bindingExpr, "data");
			if (bindingName == null) {
				return result;
			}
			IBinding binding = null;
			List bindings = queryDefn.getBindings();
			for (int i = 0; i < bindings.size(); i++) {
				IBinding bd = (IBinding) bindings.get(i);
				if (bd.getBindingName().equals(bindingName)) {
					binding = bd;
					break;
				}
			}

			if (binding == null) {
				return result;
			}

			List aggrOns = binding.getAggregatOns();
			boolean isMeasure = false;
			if (aggrOns.size() == 0) {
				isMeasure = this.getReferencedMeasureName(binding.getExpression()) != null;
			}

			int candidateEdge = this.getAxisQualifierEdgeType(target, queryDefn);

			if (candidateEdge == -1) {
				return result;
			}

			IEdgeDefinition axisQualifierEdge = queryDefn.getEdge(candidateEdge);
			if (isMeasure) {
				for (int i = 0; i < axisQualifierEdge.getDimensions().size(); i++) {
					IHierarchyDefinition hier = (IHierarchyDefinition) ((IDimensionDefinition) axisQualifierEdge
							.getDimensions().get(i)).getHierarchy().get(0);
					result.addAll(hier.getLevels());
				}
			} else {
				for (int i = 0; i < aggrOns.size(); i++) {
					DimLevel dimLevel = OlapExpressionUtil.getTargetDimLevel(aggrOns.get(i).toString());
					ILevelDefinition lvl = getAxisQualifierLevel(dimLevel, axisQualifierEdge);
					if (lvl != null) {
						result.add(lvl);
					}
				}
			}
			return result;
		} catch (BirtException e) {
			throw new AdapterException(e.getLocalizedMessage(), e);
		}
	}

	/**
	 *
	 * @param dimLevel
	 * @param edge
	 * @return
	 */
	private ILevelDefinition getAxisQualifierLevel(DimLevel dimLevel, IEdgeDefinition edge) {
		if (edge == null) {
			return null;
		}
		List dims = edge.getDimensions();
		for (int i = 0; i < dims.size(); i++) {
			IDimensionDefinition dim = (IDimensionDefinition) dims.get(i);
			if (!dim.getName().equals(dimLevel.getDimensionName())) {
				return null;
			}
			IHierarchyDefinition hier = (IHierarchyDefinition) dim.getHierarchy().get(0);
			List levels = hier.getLevels();
			for (int j = 0; j < levels.size(); j++) {
				ILevelDefinition level = (ILevelDefinition) levels.get(j);
				if (level.getName().equals(dimLevel.getLevelName())) {
					return level;
				}
			}
		}
		return null;
	}

	/**
	 *
	 * @param dimLevel
	 * @param queryDefn
	 * @return
	 */
	private int getAxisQualifierEdgeType(DimLevel dimLevel, ICubeQueryDefinition queryDefn) {
		IEdgeDefinition edge = queryDefn.getEdge(ICubeQueryDefinition.COLUMN_EDGE);
		if (edge != null) {
			List dims = edge.getDimensions();
			for (int i = 0; i < dims.size(); i++) {
				IDimensionDefinition dim = (IDimensionDefinition) dims.get(i);
				if (dim.getName().equals(dimLevel.getDimensionName())) {
					return ICubeQueryDefinition.ROW_EDGE;
				}
			}
		}

		edge = queryDefn.getEdge(ICubeQueryDefinition.ROW_EDGE);
		if (edge != null) {
			List dims = edge.getDimensions();
			for (int i = 0; i < dims.size(); i++) {
				IDimensionDefinition dim = (IDimensionDefinition) dims.get(i);
				if (dim.getName().equals(dimLevel.getDimensionName())) {
					return ICubeQueryDefinition.COLUMN_EDGE;
				}
			}
		}
		return -1;
	}

	/**
	 *
	 * @param expr
	 * @return
	 * @throws AdapterException
	 */
	@Override
	public String getReferencedMeasureName(String expr) throws AdapterException {
		return OlapExpressionCompiler.getReferencedScriptObject(expr, "measure");
	}

	/**
	 *
	 * @param expr
	 * @return
	 */
	private String getReferencedMeasureName(IBaseExpression expr) {
		return OlapExpressionCompiler.getReferencedScriptObject(expr, "measure");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.data.adapter.api.ICubeQueryUtil#
	 * getMemberValueIterator(org.eclipse.birt.report.model.api.olap.
	 * TabularCubeHandle, java.lang.String,
	 * org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition)
	 */
	@Override
	public Iterator getMemberValueIterator(TabularCubeHandle cubeHandle, String dataBindingExpr,
			ICubeQueryDefinition queryDefn) throws AdapterException {
		return this.getMemberValueIterator(cubeHandle, dataBindingExpr, queryDefn, null);
	}

	@Override
	public Iterator getMemberValueIterator(CubeHandle cubeHandle, String dataBindingExpr,
			ICubeQueryDefinition queryDefn) throws AdapterException {
		return this.getMemberValueIterator(cubeHandle, dataBindingExpr, queryDefn, null);
	}

	@Override
	public Iterator getMemberValueIterator(CubeHandle cubeHandle, String dataBindingExpr,
			ICubeQueryDefinition queryDefn, Map appContext) throws AdapterException {
		try {
			if (cubeHandle == null || dataBindingExpr == null || queryDefn == null) {
				return null;
			}

			List bindings = queryDefn.getBindings();
			Set dimLevels = OlapExpressionCompiler.getReferencedDimLevel(new ScriptExpression(dataBindingExpr),
					bindings, true);
			if (dimLevels.size() == 0 || dimLevels.size() > 1) {
				return null;
			}

			DimLevel target = (DimLevel) dimLevels.iterator().next();
			int targetDataType = getTargetDataType(bindings, dataBindingExpr);

			TabularHierarchyHandle hierHandle = (TabularHierarchyHandle) (cubeHandle
					.getDimension(target.getDimensionName()).getContent(TabularDimensionHandle.HIERARCHIES_PROP, 0));
			if (hierHandle.getDataSet() != null) {
				DefineDataSourceSetUtil.defineDataSourceAndDataSet(hierHandle.getDataSet(), this.sessionImpl);
			} else {
				DefineDataSourceSetUtil.defineDataSourceAndDataSet(
						(DataSetHandle) cubeHandle.getElementProperty(ITabularCubeModel.DATA_SET_PROP),
						this.sessionImpl);
			}

			Map levelValueMap = new HashMap();

			DataSetIterator it = createDataSetIterator(appContext, cubeHandle.getDimension(target.getDimensionName()),
					String.valueOf(cubeHandle.getElement().getID()));
			return new MemberValueIterator(it, levelValueMap, target.getLevelName(), target.getAttrName(),
					targetDataType);
		} catch (BirtException e) {
			throw new AdapterException(e.getLocalizedMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.data.adapter.api.ICubeQueryUtil#
	 * getMemberValueIterator(org.eclipse.birt.report.model.api.olap.
	 * TabularCubeHandle, java.lang.String,
	 * org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition)
	 */
	@Override
	public Iterator getMemberValueIterator(TabularCubeHandle cubeHandle, String dataBindingExpr,
			ICubeQueryDefinition queryDefn, Map appContext) throws AdapterException {
		try {
			if (cubeHandle == null || dataBindingExpr == null || queryDefn == null) {
				return null;
			}

			List bindings = queryDefn.getBindings();
			Set dimLevels = OlapExpressionCompiler.getReferencedDimLevel(new ScriptExpression(dataBindingExpr),
					bindings, true);
			if (dimLevels.size() == 0 || dimLevels.size() > 1) {
				return null;
			}

			DimLevel target = (DimLevel) dimLevels.iterator().next();
			int targetDataType = getTargetDataType(bindings, dataBindingExpr);
			TabularHierarchyHandle hierHandle = (TabularHierarchyHandle) (cubeHandle
					.getDimension(target.getDimensionName()).getContent(TabularDimensionHandle.HIERARCHIES_PROP, 0));
			if (hierHandle.getDataSet() != null) {
				DefineDataSourceSetUtil.defineDataSourceAndDataSet(hierHandle.getDataSet(), this.sessionImpl);
			} else {
				DefineDataSourceSetUtil.defineDataSourceAndDataSet(cubeHandle.getDataSet(), this.sessionImpl);
			}
			Map levelValueMap = new HashMap();

			DataSetIterator it = createDataSetIterator(appContext, cubeHandle.getDimension(target.getDimensionName()),
					String.valueOf(cubeHandle.getElement().getID()));
			return new MemberValueIterator(it, levelValueMap, target.getLevelName(), target.getAttrName(),
					targetDataType);
		} catch (BirtException e) {
			throw new AdapterException(e.getLocalizedMessage(), e);
		}
	}

	/**
	 *
	 * @param bindings
	 * @param dataBindingExpr
	 * @return
	 * @throws BirtException
	 */
	private int getTargetDataType(List bindings, String dataBindingExpr) throws BirtException {
		String bindingName = OlapExpressionCompiler.getReferencedScriptObject(dataBindingExpr, "data");
		if (bindingName == null) {
			return DataType.UNKNOWN_TYPE;
		}
		for (int i = 0; i < bindings.size(); i++) {
			IBinding binding = (IBinding) bindings.get(i);
			if (binding.getBindingName().equals(bindingName)) {
				return binding.getDataType();
			}
		}
		return DataType.UNKNOWN_TYPE;
	}

	@Override
	public Iterator getMemberValueIterator(CubeHandle cubeHandle, String targetLevel, DimensionLevel[] dimensionLevels,
			Object[] values) throws AdapterException {
		return this.getMemberValueIterator(cubeHandle, targetLevel, dimensionLevels, values, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.data.adapter.api.ICubeQueryUtil#
	 * getMemberValueIterator(org.eclipse.birt.report.model.api.olap.
	 * TabularCubeHandle, java.lang.String,
	 * org.eclipse.birt.report.data.adapter.api.DimensionLevel[],
	 * java.lang.Object[])
	 */
	@Override
	public Iterator getMemberValueIterator(TabularCubeHandle cubeHandle, String targetLevel,
			DimensionLevel[] dimensionLevels, Object[] values) throws AdapterException {
		return this.getMemberValueIterator(cubeHandle, targetLevel, dimensionLevels, values, null);
	}

	@Override
	public Iterator getMemberValueIterator(CubeHandle cubeHandle, String targetLevel, DimensionLevel[] dimensionLevels,
			Object[] values, Map appContext) throws AdapterException {
		try {
			if ((dimensionLevels == null && values != null) || (dimensionLevels != null && values == null)
					|| cubeHandle == null || targetLevel == null) {
				return null;
			}
			DimLevel target = OlapExpressionUtil.getTargetDimLevel(targetLevel);
			TabularHierarchyHandle hierHandle = (TabularHierarchyHandle) (cubeHandle
					.getDimension(target.getDimensionName()).getContent(TabularDimensionHandle.HIERARCHIES_PROP, 0));
			if (hierHandle.getDataSet() != null) {
				DefineDataSourceSetUtil.defineDataSourceAndDataSet(hierHandle.getDataSet(), this.sessionImpl);
			} else {
				DefineDataSourceSetUtil.defineDataSourceAndDataSet(
						(DataSetHandle) cubeHandle.getElementProperty(ITabularCubeModel.DATA_SET_PROP),
						this.sessionImpl);
			}
			Map levelValueMap = new HashMap();
			if (dimensionLevels != null) {
				for (int i = 0; i < dimensionLevels.length; i++) {
					if (target.getDimensionName().equals(dimensionLevels[i].getDimensionName())) {
						levelValueMap.put(dimensionLevels[i].getLevelName(), values[i]);
					}
				}
			}
			DataSetIterator it = createDataSetIterator(appContext, cubeHandle.getDimension(target.getDimensionName()),
					String.valueOf(cubeHandle.getElement().getID()));

			return new MemberValueIterator(it, levelValueMap, target.getLevelName(), target.getAttrName());
		} catch (BirtException e) {
			throw new AdapterException(e.getLocalizedMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.data.adapter.api.ICubeQueryUtil#
	 * getMemberValueIterator(org.eclipse.birt.report.model.api.olap.
	 * TabularCubeHandle, java.lang.String,
	 * org.eclipse.birt.report.data.adapter.api.DimensionLevel[],
	 * java.lang.Object[], java.util.Map)
	 */
	@Override
	public Iterator getMemberValueIterator(TabularCubeHandle cubeHandle, String targetLevel,
			DimensionLevel[] dimensionLevels, Object[] values, Map appContext) throws AdapterException {
		try {
			if ((dimensionLevels == null && values != null) || (dimensionLevels != null && values == null)
					|| cubeHandle == null || targetLevel == null) {
				return null;
			}
			DimLevel target = OlapExpressionUtil.getTargetDimLevel(targetLevel);
			TabularHierarchyHandle hierHandle = (TabularHierarchyHandle) (cubeHandle
					.getDimension(target.getDimensionName()).getContent(TabularDimensionHandle.HIERARCHIES_PROP, 0));
			if (hierHandle.getDataSet() != null) {
				DefineDataSourceSetUtil.defineDataSourceAndDataSet(hierHandle.getDataSet(), this.sessionImpl);
			} else {
				DefineDataSourceSetUtil.defineDataSourceAndDataSet(cubeHandle.getDataSet(), this.sessionImpl);
			}
			Map levelValueMap = new HashMap();
			if (dimensionLevels != null) {
				for (int i = 0; i < dimensionLevels.length; i++) {
					if (target.getDimensionName().equals(dimensionLevels[i].getDimensionName())) {
						levelValueMap.put(dimensionLevels[i].getLevelName(), values[i]);
					}
				}
			}
			DataSetIterator it = createDataSetIterator(appContext, cubeHandle.getDimension(target.getDimensionName()),
					String.valueOf(cubeHandle.getElement().getID()));

			return new MemberValueIterator(it, levelValueMap, target.getLevelName(), target.getAttrName());
		} catch (BirtException e) {
			throw new AdapterException(e.getLocalizedMessage(), e);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.data.adapter.api.ICubeQueryUtil#
	 * getMemberValueIterator(org.eclipse.birt.report.model.api.olap.
	 * TabularCubeHandle, java.lang.String,
	 * org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition[],
	 * java.lang.Object[])
	 */
	@Override
	public Iterator getMemberValueIterator(TabularCubeHandle cubeHandle, String targetLevel,
			ILevelDefinition[] higherLevelDefns, Object[] values) throws AdapterException {
		return this.getMemberValueIterator(cubeHandle, targetLevel, higherLevelDefns, values, null);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.data.adapter.api.ICubeQueryUtil#
	 * getMemberValueIterator(org.eclipse.birt.report.model.api.olap.
	 * TabularCubeHandle, java.lang.String,
	 * org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition[],
	 * java.lang.Object[])
	 */
	@Override
	public Iterator getMemberValueIterator(TabularCubeHandle cubeHandle, String targetLevel,
			ILevelDefinition[] higherLevelDefns, Object[] values, Map appContext) throws AdapterException {
		try {
			if ((higherLevelDefns == null && values != null) || (higherLevelDefns != null && values == null)
					|| cubeHandle == null || targetLevel == null) {
				return null;
			}
			DimLevel target = OlapExpressionUtil.getTargetDimLevel(targetLevel);
			TabularHierarchyHandle hierHandle = (TabularHierarchyHandle) (cubeHandle
					.getDimension(target.getDimensionName()).getContent(TabularDimensionHandle.HIERARCHIES_PROP, 0));
			if (hierHandle.getDataSet() != null) {
				DefineDataSourceSetUtil.defineDataSourceAndDataSet(hierHandle.getDataSet(), this.sessionImpl);
			} else {
				DefineDataSourceSetUtil.defineDataSourceAndDataSet(cubeHandle.getDataSet(), this.sessionImpl);
			}
			Map levelValueMap = new HashMap();
			if (higherLevelDefns != null) {
				for (int i = 0; i < higherLevelDefns.length; i++) {
					if (target.getDimensionName().equals(higherLevelDefns[i].getHierarchy().getDimension().getName())) {
						levelValueMap.put(higherLevelDefns[i].getName(), values[i]);
					}
				}
			}
			DataSetIterator it = createDataSetIterator(appContext, cubeHandle.getDimension(target.getDimensionName()),
					String.valueOf(cubeHandle.getElement().getID()));

			return new MemberValueIterator(it, levelValueMap, target.getLevelName(), target.getAttrName());
		} catch (BirtException e) {
			throw new AdapterException(e.getLocalizedMessage(), e);
		}

	}

	/**
	 * Checks whether the given string is valid to be the name for a level/dimension
	 *
	 * @param name
	 * @return
	 */
	@Override
	public boolean isValidDimensionName(String name) {
		Matcher isNum = pattern.matcher(name);
		return !isNum.matches();
	}

	/**
	 *
	 * @param appContext
	 * @param hierHandle
	 * @return
	 * @throws AdapterException
	 * @throws BirtException
	 */
	private DataSetIterator createDataSetIterator(Map appContext, DimensionHandle dim, String cubeName)
			throws AdapterException, BirtException {
		List<ColumnMeta> metaList = new ArrayList<>();
		IQueryDefinition defn = sessionImpl.createDimensionQuery(sessionImpl, dim,
				(TabularHierarchyHandle) dim.getContent(TabularDimensionHandle.HIERARCHIES_PROP, 0), metaList,
				cubeName);
		return new DataSetIterator(this.sessionImpl, defn, metaList, appContext);

	}

	/**
	 * @param hierHandle
	 * @throws BirtException
	 */

	/**
	 *
	 * @author Administrator
	 *
	 */
	private static class MemberValueIterator implements Iterator {
		private IDatasetIterator dataSetIterator;
		private boolean hasNext;
		private Map levelValueMap;
		private String targetLevelName;
		private Object currentValue;
		private String attribute;
		private int targetDateType;

		public MemberValueIterator(IDatasetIterator it, Map levelValueMap, String targetLevelName, String attribute) {
			this(it, levelValueMap, targetLevelName, attribute, DataType.UNKNOWN_TYPE);
		}

		public MemberValueIterator(IDatasetIterator it, Map levelValueMap, String targetLevelName, String attribute,
				int targetDataType) {
			this.dataSetIterator = it;
			this.hasNext = true;
			this.levelValueMap = levelValueMap;
			this.targetLevelName = targetLevelName;
			this.attribute = attribute;
			this.targetDateType = targetDataType;
			this.next();
		}

		@Override
		public boolean hasNext() {
			return this.hasNext;
		}

		@Override
		public Object next() {
			try {
				if (!this.hasNext) {
					return null;
				}
				Object result = this.currentValue;
				boolean accept = false;
				while (this.dataSetIterator.next()) {
					accept = true;
					Iterator it = this.levelValueMap.keySet().iterator();

					while (it.hasNext()) {
						String key = it.next().toString();
						Object value = this.levelValueMap.get(key);
						if (ScriptEvalUtil.compare(value,
								this.dataSetIterator.getValue(this.dataSetIterator.getFieldIndex(key))) != 0) {
							accept = false;
							break;
						}
					}
					if (accept) {
						this.currentValue = this.dataSetIterator.getValue(
								this.dataSetIterator.getFieldIndex(this.attribute == null ? this.targetLevelName
										: OlapExpressionUtil.getAttributeColumnName(this.targetLevelName,
												this.attribute)));
						break;
					}
				}

				this.hasNext = accept;
				return DataTypeUtil.convert(result, targetDateType);
			} catch (BirtException e) {
				return null;
			}
		}

		@Override
		public void remove() {
			throw new UnsupportedOperationException();
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.data.adapter.api.ICubeQueryUtil#getInvalidBindings(
	 * org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition)
	 */
	@Override
	public List getInvalidBindings(ICubeQueryDefinition queryDefn) throws AdapterException {
		try {
			List invalidBindingNameList = new ArrayList();
			List invalidBinding = OlapQueryUtil.validateBinding(queryDefn, true);
			for (int i = 0; i < invalidBinding.size(); i++) {
				IBinding binding = (IBinding) invalidBinding.get(i);
				invalidBindingNameList.add(binding.getBindingName());
			}
			return invalidBindingNameList;
		} catch (DataException e) {
			throw new AdapterException(e.getLocalizedMessage(), e);
		}
	}

	@Override
	public List getInvalidBindingsForLinkedDataSetCube(ICubeQueryDefinition queryDefn) throws AdapterException {
		return getInvalidBindings(queryDefn);
	}

	/**
	 *
	 *
	 */
	private static class BindingMetaInfo implements IBindingMetaInfo {
		//
		private String name;
		private int type;

		/**
		 * Constructor.
		 *
		 * @param name
		 * @param type
		 */
		BindingMetaInfo(String name, int type) {
			this.name = name;
			this.type = type;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.report.data.adapter.api.IBindingMetaInfo#getBindingType()
		 */
		@Override
		public int getBindingType() {
			return type;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.report.data.adapter.api.IBindingMetaInfo#getBindingName()
		 */
		@Override
		public String getBindingName() {
			return name;
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.data.adapter.api.ICubeQueryUtil#
	 * getReferencedDimensionLevel(java.lang.String)
	 */
	@Override
	public IDimensionLevel[] getReferencedDimensionLevel(String expression) throws AdapterException {
		try {
			IDimensionLevel[] result = {};
			Set dimLevels = OlapExpressionCompiler.getReferencedDimLevel(new ScriptExpression(expression),
					new ArrayList());
			if (dimLevels != null) {
				result = new IDimensionLevel[dimLevels.size()];
				Iterator it = dimLevels.iterator();
				int index = 0;
				while (it.hasNext()) {
					DimLevel dimLevel = (DimLevel) it.next();
					result[index] = new DimensionLevel(dimLevel);
					index++;
				}
			}
			return result;
		} catch (DataException e) {
			throw new AdapterException(e.getLocalizedMessage(), e);
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.birt.report.data.adapter.api.ICubeQueryUtil#
	 * getReferencedDimensionLevel(java.lang.String)
	 */
	@Override
	public IDimensionLevel[] getReferencedDimensionLevel(String expression, List<IBinding> bindings)
			throws AdapterException {
		try {
			IDimensionLevel[] result = {};
			Set dimLevels = OlapExpressionCompiler.getReferencedDimLevel(new ScriptExpression(expression), bindings);
			if (dimLevels != null) {
				result = new IDimensionLevel[dimLevels.size()];
				Iterator it = dimLevels.iterator();
				int index = 0;
				while (it.hasNext()) {
					DimLevel dimLevel = (DimLevel) it.next();
					result[index] = new DimensionLevel(dimLevel);
					index++;
				}
			}
			return result;
		} catch (DataException e) {
			throw new AdapterException(e.getLocalizedMessage(), e);
		}
	}
}
