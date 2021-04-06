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

package org.eclipse.birt.data.engine.olap.util.filter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.api.query.CubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.impl.query.PreparedCubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.query.view.CubeQueryDefinitionUtil;
import org.eclipse.birt.data.engine.olap.util.DataJSObjectPopulator;
import org.eclipse.birt.data.engine.olap.util.DimensionJSEvalHelper;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */

public abstract class BaseDimensionFilterEvalHelper extends DimensionJSEvalHelper implements IJSFilterHelper {
	protected DimLevel[] aggrLevels;
	protected ICubeFilterDefinition cubeFilter;
	protected ILevelDefinition[] axisLevels;
	protected Object[] axisValues;
	protected boolean isAxisFilter;

	/**
	 * 
	 * @param outResults
	 * @param parentScope
	 * @param queryDefn
	 * @param cubeFilter
	 * @return
	 * @throws DataException
	 */
	public static IJSFilterHelper createFilterHelper(IBaseQueryResults outResults, Scriptable parentScope,
			ICubeQueryDefinition queryDefn, IFilterDefinition cubeFilter, ScriptContext cx) throws DataException {
		if (cubeFilter.getExpression() instanceof IConditionalExpression) {
			IConditionalExpression expr = (IConditionalExpression) cubeFilter.getExpression();
			if (expr.getOperator() == IConditionalExpression.OP_TOP_N
					|| expr.getOperator() == IConditionalExpression.OP_BOTTOM_N
					|| expr.getOperator() == IConditionalExpression.OP_TOP_PERCENT
					|| expr.getOperator() == IConditionalExpression.OP_BOTTOM_PERCENT)
				return new TopBottomDimensionFilterEvalHelper(outResults, parentScope, queryDefn, cubeFilter, cx);
		}

		return new DimensionFilterEvalHelper(outResults, parentScope, cx, queryDefn, cubeFilter);
	}

	/**
	 * 
	 * @param parentScope
	 * @param queryDefn
	 * @param cubeFilter
	 * @param cx
	 * @throws DataException
	 */
	protected void initialize(IBaseQueryResults outResults, Scriptable parentScope, ICubeQueryDefinition queryDefn,
			IFilterDefinition cubeFilter, ScriptContext cx) throws DataException {
		populaterFilterDefinition(cubeFilter);
		super.init(outResults, parentScope, queryDefn, cx, cubeFilter.getExpression());
	}

	private void populaterFilterDefinition(IFilterDefinition cubeFilter) throws DataException {
		if (cubeFilter instanceof ICubeFilterDefinition) {
			this.cubeFilter = (ICubeFilterDefinition) cubeFilter;
		} else {
			this.cubeFilter = new CubeFilterDefinition(cubeFilter.getExpression());
		}
		axisLevels = this.cubeFilter.getAxisQualifierLevels();
		axisValues = this.cubeFilter.getAxisQualifierValues();
		if (axisLevels == null || axisValues == null || axisLevels.length != axisValues.length) {
			this.isAxisFilter = false;
		} else {
			for (int i = 0; i < axisLevels.length; i++) {
				if (axisLevels[i] == null)
					throw new DataException(ResourceConstants.AXIS_LEVEL_CANNOT_BE_NULL);
				if (axisValues[i] == null)
					throw new DataException(ResourceConstants.AXIS_VALUE_CANNOT_BE_NULL, axisLevels[i].getName());
			}
		}
		this.isAxisFilter = (axisLevels != null && axisValues != null && axisLevels.length == axisValues.length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.util.BaseJSEvalHelper#
	 * registerJSObjectPopulators()
	 */
	protected void registerJSObjectPopulators() throws DataException {
		super.registerJSObjectPopulators();
		this.aggrLevels = populateAggrLevels();
		register(new DataJSObjectPopulator(this.outResults, scope, queryDefn.getBindings(),
				this.aggrLevels != null && this.aggrLevels.length > 0, cx));
	}

	/**
	 * @return
	 * @throws DataException
	 */
	protected DimLevel[] populateAggrLevels() throws DataException {
		// get aggregation level names from the query definition related with
		// the query expression
		String bindingName = OlapExpressionCompiler.getReferencedScriptObject(expr,
				ScriptConstants.DATA_BINDING_SCRIPTABLE);// $NON-NLS-1$
		if (bindingName == null) {
			bindingName = OlapExpressionCompiler.getReferencedScriptObject(expr,
					ScriptConstants.DATA_SET_BINDING_SCRIPTABLE);// $NON-NLS-1$
		}
		if (bindingName == null)
			return null;

		return getAggregateOnLevels(bindingName);
	}

	private DimLevel[] getAggregateOnLevels(String bindingName) throws DataException {
		List bindingList = new ArrayList();
		List bindingForNestList = new ArrayList();
		bindingList.addAll(queryDefn.getBindings());
		if (this.queryDefn instanceof PreparedCubeQueryDefinition) {
			bindingForNestList.addAll(((PreparedCubeQueryDefinition) this.queryDefn).getBindingsForNestAggregation());
		}

		for (Iterator it = bindingList.iterator(); it.hasNext();) {
			IBinding binding = (IBinding) it.next();
			if (binding.getBindingName().equals(bindingName)) {
				return getAggregationOnsForBinding(binding);
			}
		}
		for (Iterator it = bindingForNestList.iterator(); it.hasNext();) {
			IBinding binding = (IBinding) it.next();
			if (binding.getBindingName().equals(bindingName)) {
				if (CubeQueryDefinitionUtil.isRunnnigAggr(binding.getAggrFunction())) {
					List fullAggrOnLevels = OlapExpressionUtil.getFullLevelsForRunningAggregation(binding, bindingList);
					return (DimLevel[]) fullAggrOnLevels.toArray(new DimLevel[] {});
				} else {
					getAggregationOnsForBinding(binding);
				}
			}
		}
		return null;
	}

	private DimLevel[] getAggregationOnsForBinding(IBinding binding) throws DataException {
		List aggrs = binding.getAggregatOns();
		if (aggrs.size() == 0) {
			if (OlapExpressionCompiler.getReferencedScriptObject(binding.getExpression(),
					ScriptConstants.DIMENSION_SCRIPTABLE) != null)
				return null;

			IBinding directReferenceBinding = OlapExpressionUtil.getDirectMeasureBinding(binding,
					queryDefn.getBindings());
			if (directReferenceBinding != null && directReferenceBinding != binding) {
				return getAggregateOnLevels(directReferenceBinding.getBindingName());
			}
			// get all level names in the query definition
			List levelList = new ArrayList();
			// get all levels from the row edge and column edge
			IEdgeDefinition rowEdge = queryDefn.getEdge(ICubeQueryDefinition.ROW_EDGE);
			populateDimLevel(levelList, rowEdge);
			IEdgeDefinition colEdge = queryDefn.getEdge(ICubeQueryDefinition.COLUMN_EDGE);
			populateDimLevel(levelList, colEdge);
			DimLevel[] levels = new DimLevel[levelList.size()];
			levelList.toArray(levels);
			return levels;
		} else {
			DimLevel[] levels = new DimLevel[aggrs.size()];
			for (int i = 0; i < aggrs.size(); i++) {
				levels[i] = OlapExpressionUtil.getTargetDimLevel(aggrs.get(i).toString());
			}
			return levels;
		}
	}

	/**
	 * 
	 * @param levelList
	 * @param edge
	 */
	private void populateDimLevel(List levelList, IEdgeDefinition edge) {
		if (edge == null)
			return;
		List rowDims = edge.getDimensions();
		for (Iterator i = rowDims.iterator(); i.hasNext();) {
			IDimensionDefinition dim = (IDimensionDefinition) i.next();
			IHierarchyDefinition hirarchy = (IHierarchyDefinition) dim.getHierarchy().get(0);
			for (Iterator j = hirarchy.getLevels().iterator(); j.hasNext();) {
				ILevelDefinition level = (ILevelDefinition) j.next();
				levelList.add(new DimLevel(dim.getName(), level.getName()));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.util.filter.IJSFilterHelper#getAggrLevels()
	 */
	public DimLevel[] getAggrLevels() {
		return this.aggrLevels;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IJSFilterHelper#
	 * getDimensionName()
	 */
	public String getDimensionName() {
		return this.dimName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IJSFilterHelper#
	 * getCubeFiterDefinition()
	 */
	public ICubeFilterDefinition getCubeFilterDefinition() {
		return cubeFilter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IJSFilterHelper#
	 * isAggregationFilter()
	 */
	public boolean isAggregationFilter() {
		if (this.isAxisFilter)
			return true;
		return this.dimName == null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.util.DimensionJSEvalHelper#
	 * getTargetDimension()
	 */
	protected IDimensionDefinition getTargetDimension() throws DataException {
//		if ( isAggregationFilter( ) )
//		{
//			ILevelDefinition targetLevel = cubeFilter.getTargetLevel( );
//			if ( targetLevel == null )
//			{
//				throw new DataException( ResourceConstants.REFERENCED_LEVEL_NOT_FOUND );
//			}
//			IDimensionDefinition dimDefn = targetLevel.getHierarchy( )
//					.getDimension( );
//			if ( dimDefn == null )
//			{
//				throw new DataException( ResourceConstants.REFERENCED_DIMENSION_NOT_FOUND,
//						dimName );
//			}
//			return dimDefn;
//		}
//		else
		{
			return super.getTargetDimension();
		}
	}
}