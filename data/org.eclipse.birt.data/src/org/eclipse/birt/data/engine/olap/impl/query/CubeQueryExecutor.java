/*******************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.DataEngineThreadLocal;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.ICollectionConditionalExpression;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IExpressionCollection;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.olap.api.query.CubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeFilterDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeSortDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IDimensionDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IEdgeDefinition;
import org.eclipse.birt.data.engine.olap.api.query.IHierarchyDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IComputedMeasureHelper;
import org.eclipse.birt.data.engine.olap.data.api.ISelection;
import org.eclipse.birt.data.engine.olap.data.impl.SelectionFactory;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.SimpleLevelFilter;
import org.eclipse.birt.data.engine.olap.util.ComputedMeasureHelper;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionCompiler;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.eclipse.birt.data.engine.olap.util.filter.AggrMeasureFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.BaseDimensionFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IAggrMeasureFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFacttableFilterEvalHelper;
import org.eclipse.birt.data.engine.olap.util.filter.IJSFilterHelper;
import org.eclipse.birt.data.engine.olap.util.filter.JSFacttableFilterEvalHelper;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */

public class CubeQueryExecutor {

	private ICubeQueryDefinition defn;
	private Scriptable scope;
	private DataEngineSession session;
	private DataEngineContext context;
	private String queryResultsId;
	private IBaseQueryResults outResults;

	private List<IJSFilterHelper> dimensionFilterEvalHelpers;
	private List<SimpleLevelFilter> dimensionSimpleFilter;
	private List<IAggrMeasureFilterEvalHelper> aggrMeasureFilterEvalHelpers;
	private List<IAggrMeasureFilterEvalHelper> aggrFilterEvalHelpersOnCubeOperator;
	private List<IJSFacttableFilterEvalHelper> advancedFacttableBasedFilterEvalHelper;
	private boolean populateFilter = false;

	public static final int DIMENSION_FILTER = 0;
	public static final int AGGR_MEASURE_FILTER = 1;
	public static final int FACTTABLE_FILTER = 2;
	public static final int AGGR_OPERATION_FILTER = 3;

	/**
	 * 
	 * @param outResults
	 * @param defn
	 * @param session
	 * @param scope
	 * @param context
	 * @throws DataException
	 */
	public CubeQueryExecutor(IBaseQueryResults outResults, ICubeQueryDefinition defn, DataEngineSession session,
			Scriptable scope, DataEngineContext context) throws DataException {
		this.defn = defn;
		this.scope = scope;
		this.context = context;
		this.session = session;
		DataEngineThreadLocal.getInstance().getPathManager().setTempPath(session.getTempDir());
		this.outResults = outResults;
		this.dimensionFilterEvalHelpers = new ArrayList<IJSFilterHelper>();
		this.dimensionSimpleFilter = new ArrayList<SimpleLevelFilter>();
		this.aggrMeasureFilterEvalHelpers = new ArrayList<IAggrMeasureFilterEvalHelper>();
		this.aggrFilterEvalHelpersOnCubeOperator = new ArrayList<IAggrMeasureFilterEvalHelper>();
		this.advancedFacttableBasedFilterEvalHelper = new ArrayList<IJSFacttableFilterEvalHelper>();
		if (!(context.getMode() == DataEngineContext.MODE_PRESENTATION && defn.getQueryResultsID() != null)) {
			// query execution result will be loaded directly from document
			// needless to populate filer helpers
			populateFilterHelpers();
		}
	}

	private void validateFilter(List filters, List bindings) throws DataException {
		IFilterDefinition filter = null;
		for (int i = 0; i < filters.size(); i++) {
			filter = (IFilterDefinition) filters.get(i);
			List bindingName = ExpressionCompilerUtil.extractColumnExpression(filter.getExpression(),
					ScriptConstants.DATA_BINDING_SCRIPTABLE);
			for (int j = 0; j < bindingName.size(); j++) {
				if (isAggregationBinding((String) bindingName.get(j), this.defn.getBindings())) {
					IBinding binding = getBinding((String) bindingName.get(j), this.defn.getBindings());
					validateAggregationFilterExpr(filter.getExpression(), binding);
				}
			}
		}
	}

	private void validateAggregationFilterExpr(IBaseExpression filterExpr, IBinding binding) throws DataException {
		Set targetDimLevel = OlapExpressionCompiler.getReferencedDimLevel(filterExpr, this.defn.getBindings());
		List aggregationOns = binding.getAggregatOns();
		Object[] dimLevel = targetDimLevel.toArray();
		for (int i = 0; i < dimLevel.length; i++) {
			boolean exist = false;
			for (int j = 0; j < aggregationOns.size(); j++) {
				DimLevel level = (DimLevel) dimLevel[i];
				if (aggregationOns.get(j).equals(
						ExpressionUtil.createJSDimensionExpression(level.getDimensionName(), level.getLevelName()))) {
					exist = true;
					break;
				}
			}
			if (!exist) {
				String expr = "";
				if (filterExpr instanceof IScriptExpression)
					expr = ((IScriptExpression) filterExpr).getText();
				else if (filterExpr instanceof IConditionalExpression)
					expr = ((IConditionalExpression) filterExpr).getExpression().getText();
				throw new DataException(ResourceConstants.INVALID_AGGREGATION_FILTER_EXPRESSION,
						new Object[] { expr, binding.getBindingName(), ((DimLevel) dimLevel[i]).toString() });
			}
		}
	}

	private SimpleLevelFilter createSimpleLevelFilter(IFilterDefinition filter, List bindings) {
		if (!(filter instanceof CubeFilterDefinition)
				|| ((CubeFilterDefinition) filter).getAxisQualifierValues() != null)
			return null;

		IBaseExpression expr = ((CubeFilterDefinition) filter).getExpression();
		if (!(expr instanceof IConditionalExpression))
			return null;
		IConditionalExpression condExpr = (IConditionalExpression) ((CubeFilterDefinition) filter).getExpression();

		if (isVariableOperandInDimensionFilter(condExpr))
			return null;

		Set refDimLevel;
		try {
			refDimLevel = OlapExpressionCompiler.getReferencedDimLevel(condExpr.getExpression(), bindings);

			if (refDimLevel.size() != 1)
				return null;
			DimLevel dimlevel = (DimLevel) refDimLevel.iterator().next();
			if (dimlevel.getAttrName() != null)
				return null;
			if (!(condExpr.getOperand1() instanceof IExpressionCollection)) {
				Object Op1 = ScriptEvalUtil.evalExpr((IScriptExpression) condExpr.getOperand1(),
						session.getEngineContext().getScriptContext().newContext(scope), ScriptExpression.defaultID, 0);
				if (Op1 == null)
					return null;
				ISelection[] selction = new ISelection[1];
				if (condExpr.getOperator() == IConditionalExpression.OP_EQ
						|| condExpr.getOperator() == IConditionalExpression.OP_IN) {
					selction[0] = SelectionFactory.createOneKeySelection(new Object[] { Op1 });
				} else if (condExpr.getOperator() == IConditionalExpression.OP_GT) {
					selction[0] = SelectionFactory.createRangeSelection(new Object[] { Op1 }, null, false, false);
				} else if (condExpr.getOperator() == IConditionalExpression.OP_GE) {
					selction[0] = SelectionFactory.createRangeSelection(new Object[] { Op1 }, null, true, false);
				} else if (condExpr.getOperator() == IConditionalExpression.OP_LT) {
					selction[0] = SelectionFactory.createRangeSelection(null, new Object[] { Op1 }, false, false);
				} else if (condExpr.getOperator() == IConditionalExpression.OP_LE) {
					selction[0] = SelectionFactory.createRangeSelection(null, new Object[] { Op1 }, false, true);
				} else
					return null;

				return new SimpleLevelFilter(dimlevel, selction);
			} else if (condExpr.getOperator() == IConditionalExpression.OP_IN) {
				IExpressionCollection combinedExpr = (IExpressionCollection) ((IConditionalExpression) expr)
						.getOperand1();
				Object[] exprs = combinedExpr.getExpressions().toArray();

				Object[] opValues = new Object[exprs.length];
				boolean existValue = false;
				for (int i = 0; i < opValues.length; i++) {
					opValues[i] = ScriptEvalUtil.evalExpr((IBaseExpression) exprs[i],
							session.getEngineContext().getScriptContext().newContext(scope), ScriptExpression.defaultID,
							0);
					if (opValues[i] != null) {
						existValue = true;
					}
				}
				ISelection[] selction = new ISelection[1];
				Object[][] keyValues = null;
				if (opValues.length == 1 && opValues[0] instanceof Object[]) {
					Object[] mulValue = (Object[]) opValues[0];
					keyValues = new Object[mulValue.length][1];
					for (int i = 0; i < mulValue.length; i++) {
						keyValues[i][0] = mulValue[i];
					}
				} else {
					keyValues = new Object[opValues.length][1];
					for (int i = 0; i < opValues.length; i++) {
						keyValues[i][0] = opValues[i];
					}
				}
				selction[0] = SelectionFactory.createMutiKeySelection(keyValues);
				return new SimpleLevelFilter(dimlevel, selction);
			}
			return null;
		} catch (DataException e) {
			return null;
		}
	}

	private boolean isVariableOperandInDimensionFilter(IConditionalExpression condExpr) {
		// If the filter operand is not constant, we should not use simple level filter,
		// as the operand is changing as per cursor changes.
		Object referedScriptable = OlapExpressionCompiler.getReferencedScriptObject(condExpr.getOperand1(),
				ScriptConstants.DIMENSION_SCRIPTABLE);
		return referedScriptable != null;
	}

	private boolean adjustCubeFilterUpdateAggregationFlag(IFilterDefinition filter, Set dimLevelSet) {
		if (filter instanceof CubeFilterDefinition && ((CubeFilterDefinition) filter).getTargetLevel() != null) {
			IHierarchyDefinition hierarchy = ((CubeFilterDefinition) filter).getTargetLevel().getHierarchy();
			DimLevel[] dims = (DimLevel[]) dimLevelSet.toArray(new DimLevel[0]);

			for (int j = 0; j < dims.length; j++) {
				boolean existDimLevel = false;
				for (int p = 0; p < hierarchy.getLevels().size(); p++) {
					if (dims[j].getDimensionName().equals(hierarchy.getDimension().getName())
							&& dims[j].getLevelName().equals(hierarchy.getLevels().get(p).getName()))
						existDimLevel = true;
				}
				if (!existDimLevel) {
					filter.setUpdateAggregation(true);
					break;
				}
			}
		}
		return filter.updateAggregation();
	}

	public void populateFilterHelpers() throws DataException {
		if (populateFilter)
			return;
		List filters = defn.getFilters();
		Set<DimLevel> dimLevelInCubeQuery = this.getDimLevelsDefinedInCubeQuery();
		validateFilter(filters, defn.getBindings());
		for (int i = 0; i < filters.size(); i++) {
			IFilterDefinition filter = (IFilterDefinition) filters.get(i);
			if (!filter.updateAggregation()) {
				Set dimLevelSet = OlapExpressionCompiler.getReferencedDimLevel(filter.getExpression(),
						defn.getBindings());
				if (dimLevelSet.size() <= 1) {
					// For the filter that not requires updating aggregation, we would not populate
					// them here.
					continue;
				} else {
					if (!adjustCubeFilterUpdateAggregationFlag(filter, dimLevelSet))
						continue;
				}

			}
			switch (this.getFilterType(filter, dimLevelInCubeQuery)) {
			case CubeQueryExecutor.DIMENSION_FILTER: {
				SimpleLevelFilter simpleLevelfilter = createSimpleLevelFilter(filter, defn.getBindings());
				if (simpleLevelfilter == null) {
					this.dimensionFilterEvalHelpers
							.add(BaseDimensionFilterEvalHelper.createFilterHelper(this.outResults, this.scope, defn,
									filter, this.session.getEngineContext().getScriptContext()));
				} else {
					boolean existLevelFilter = false;
					for (int j = 0; j < this.dimensionSimpleFilter.size(); j++) {
						if (dimensionSimpleFilter.get(j).getDimensionName().equals(simpleLevelfilter.getDimensionName())
								&& dimensionSimpleFilter.get(j).getLevelName()
										.equals(simpleLevelfilter.getLevelName())) {
							this.dimensionFilterEvalHelpers
									.add(BaseDimensionFilterEvalHelper.createFilterHelper(this.outResults, this.scope,
											defn, filter, this.session.getEngineContext().getScriptContext()));
							existLevelFilter = true;
							break;
						}
					}
					if (!existLevelFilter)
						this.dimensionSimpleFilter.add(simpleLevelfilter);
				}
				break;
			}
			case CubeQueryExecutor.AGGR_MEASURE_FILTER: {
				this.aggrMeasureFilterEvalHelpers.add(new AggrMeasureFilterEvalHelper(this.outResults, scope, defn,
						filter, session.getEngineContext().getScriptContext()));
				break;
			}
			case CubeQueryExecutor.AGGR_OPERATION_FILTER: {
				this.aggrFilterEvalHelpersOnCubeOperator.add(new AggrMeasureFilterEvalHelper(this.outResults, scope,
						defn, filter, session.getEngineContext().getScriptContext()));
				break;
			}
			case CubeQueryExecutor.FACTTABLE_FILTER:
			default: {
				this.advancedFacttableBasedFilterEvalHelper.add(new JSFacttableFilterEvalHelper(scope,
						this.session.getEngineContext().getScriptContext(), filter, this.outResults, this.defn));
			}
			}
		}
		populateFilter = true;
	}

	public int getFilterType(IFilterDefinition filter, Set<DimLevel> dimLevelInCubeQuery) throws DataException {
		if (!(filter instanceof ICubeFilterDefinition)) {
			if (filter.getExpression() instanceof ICollectionConditionalExpression) {
				Collection<IScriptExpression> exprs = ((ICollectionConditionalExpression) (filter.getExpression()))
						.getExpr();
				Set dimensionSet = new HashSet();
				Iterator<IScriptExpression> exprsIterator = exprs.iterator();
				while (exprsIterator.hasNext()) {
					Iterator dimLevels = OlapExpressionCompiler
							.getReferencedDimLevel(exprsIterator.next(), this.defn.getBindings()).iterator();
					while (dimLevels.hasNext())
						dimensionSet.add(((DimLevel) dimLevels.next()).getDimensionName());
				}
				// For drill up/down filter that need not update aggregation, we will treat them
				// as FACTTABLE_FILTER. This FACTTABLE_FILTER
				// will indeed apply on detailed most aggregation result set.
				// TODO: Refactor FACTTABLE_FILTER, give better naming so that can reflect its
				// new usage.
				if (dimensionSet.size() == 1 && filter.updateAggregation())
					return CubeQueryExecutor.DIMENSION_FILTER;
				else
					return CubeQueryExecutor.FACTTABLE_FILTER;
			}
			return CubeQueryExecutor.DIMENSION_FILTER;
		}
		ICubeFilterDefinition cubeFilter = (ICubeFilterDefinition) filter;
		if (cubeFilter.getTargetLevel() != null) {
			return CubeQueryExecutor.DIMENSION_FILTER;
		} else {
			String measure = OlapExpressionCompiler.getReferencedScriptObject(filter.getExpression(),
					ScriptConstants.MEASURE_SCRIPTABLE);
			if (measure != null)
				return CubeQueryExecutor.FACTTABLE_FILTER;

			List bindingName = ExpressionCompilerUtil.extractColumnExpression(filter.getExpression(),
					ScriptConstants.DATA_BINDING_SCRIPTABLE);
			if (bindingName.size() > 0) {
				List bindingList = new ArrayList();
				bindingList.addAll(this.defn.getBindings());
				List nestedCubeOperation = new ArrayList();
				if (this.defn instanceof PreparedCubeQueryDefinition) {
					nestedCubeOperation
							.addAll(((PreparedCubeQueryDefinition) this.defn).getBindingsForNestAggregation());
				}
				if (existAggregationBinding(bindingName, bindingList))
					return CubeQueryExecutor.AGGR_MEASURE_FILTER;
				if (existAggregationBinding(bindingName, nestedCubeOperation))
					return CubeQueryExecutor.AGGR_OPERATION_FILTER;

				Set targetDimLevel = OlapExpressionCompiler.getReferencedDimLevel(filter.getExpression(),
						this.defn.getBindings());
				if (!targetDimLevel.isEmpty() && targetDimLevel.size() == 1) {
					return CubeQueryExecutor.DIMENSION_FILTER;
				}

				if (!filter.updateAggregation()) {
					List derivedBindingNameList = new ArrayList();
					for (int i = 0; i < bindingName.size(); i++) {
						IBinding binding = getBinding(bindingName.get(i).toString(), this.defn.getBindings());
						if (binding != null) {
							List temp = ExpressionCompilerUtil.extractColumnExpression(binding.getExpression(),
									ScriptConstants.DATA_BINDING_SCRIPTABLE);
							if (temp != null && temp.size() > 0)
								derivedBindingNameList.addAll(temp);
						}
					}
					if (derivedBindingNameList.size() > 0) {
						if (existAggregationBinding(derivedBindingNameList, this.defn.getBindings()))
							return CubeQueryExecutor.AGGR_MEASURE_FILTER;
					}
				}

				List derivedBindingNameList = new ArrayList();
				for (int i = 0; i < bindingName.size(); i++) {
					IBinding binding = getBinding(bindingName.get(i).toString(), this.defn.getBindings());
					if (binding != null) {
						List temp = ExpressionCompilerUtil.extractColumnExpression(binding.getExpression(),
								ScriptConstants.DATA_BINDING_SCRIPTABLE);
						if (temp != null && temp.size() > 0)
							derivedBindingNameList.addAll(temp);
					}
				}
				if (derivedBindingNameList.size() > 0) {
					if (existAggregationBinding(derivedBindingNameList, this.defn.getBindings()))
						return CubeQueryExecutor.AGGR_MEASURE_FILTER;
				}

				return CubeQueryExecutor.FACTTABLE_FILTER;
			} else {
				List dimensionName = ExpressionCompilerUtil.extractColumnExpression(filter.getExpression(),
						ScriptConstants.DIMENSION_SCRIPTABLE);
				if (dimensionName.size() > 1) {
					return FACTTABLE_FILTER;
				}
				return DIMENSION_FILTER;
			}
		}
	}

	/**
	 * 
	 * @param bindingName
	 * @param bindings
	 * @return
	 * @throws DataException
	 */
	private static boolean existAggregationBinding(List bindingName, List bindings) throws DataException {
		for (int i = 0; i < bindingName.size(); i++) {
			if (isAggregationBinding((String) bindingName.get(i), bindings))
				return true;
		}
		return false;
	}

	private static boolean isAggregationBinding(String bindingName, List bindings) throws DataException {
		for (int j = 0; j < bindings.size(); j++) {
			IBinding binding = (IBinding) bindings.get(j);
			if (bindingName.equals(binding.getBindingName()) && OlapExpressionUtil.isAggregationBinding(binding)) {
				return true;
			}
		}
		return false;
	}

	private static IBinding getBinding(String bindingName, List bindings) throws DataException {
		for (int j = 0; j < bindings.size(); j++) {
			IBinding binding = (IBinding) bindings.get(j);
			if (bindingName.equals(binding.getBindingName())) {
				return binding;
			}
		}
		return null;
	}

	/**
	 * 
	 * @return
	 * @throws DataException
	 */
	public List<IJSFilterHelper> getDimensionFilterEvalHelpers() throws DataException {
		return this.dimensionFilterEvalHelpers;
	}

	public List<SimpleLevelFilter> getdimensionSimpleFilter() throws DataException {
		return this.dimensionSimpleFilter;
	}

	/**
	 * 
	 * @return
	 * @throws DataException
	 */
	public List<IAggrMeasureFilterEvalHelper> getMeasureFilterEvalHelpers() throws DataException {
		return this.aggrMeasureFilterEvalHelpers;
	}

	/**
	 * 
	 * @return
	 * @throws DataException
	 */
	public List<IAggrMeasureFilterEvalHelper> getNestAggregationFilterEvalHelpers() throws DataException {
		return this.aggrFilterEvalHelpersOnCubeOperator;
	}

	/**
	 * 
	 * @return
	 * @throws DataException
	 */
	public List<IJSFacttableFilterEvalHelper> getFacttableBasedFilterHelpers() throws DataException {
		return this.advancedFacttableBasedFilterEvalHelper;
	}

	public Set<DimLevel> getDimLevelsDefinedInCubeQuery() {
		Set<DimLevel> dimLevelDefinedInCube = new HashSet<DimLevel>();
		populateDimLevelInEdge(dimLevelDefinedInCube, ICubeQueryDefinition.COLUMN_EDGE);
		populateDimLevelInEdge(dimLevelDefinedInCube, ICubeQueryDefinition.ROW_EDGE);
		populateDimLevelInEdge(dimLevelDefinedInCube, ICubeQueryDefinition.PAGE_EDGE);
		return dimLevelDefinedInCube;
	}

	private void populateDimLevelInEdge(Set<DimLevel> dimLevelDefinedInCube, int i) {
		IEdgeDefinition edge = defn.getEdge(i);
		if (edge == null)
			return;
		List<IDimensionDefinition> dims = edge.getDimensions();
		for (IDimensionDefinition dim : dims) {
			List<ILevelDefinition> levels = ((IHierarchyDefinition) dim.getHierarchy().get(0)).getLevels();
			for (ILevelDefinition level : levels) {
				dimLevelDefinedInCube.add(new DimLevel(dim.getName(), level.getName()));
			}
		}
	}

	/**
	 * 
	 * @return
	 * @throws DataException
	 */
	public IComputedMeasureHelper getComputedMeasureHelper() throws DataException {
		if (this.defn.getComputedMeasures() != null && this.defn.getComputedMeasures().size() > 0)
			return new ComputedMeasureHelper(this.scope, session.getEngineContext().getScriptContext(),
					this.defn.getComputedMeasures());
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public ICubeQueryDefinition getCubeQueryDefinition() {
		return this.defn;
	}

	/**
	 * 
	 * @return
	 */
	public DataEngineSession getSession() {
		return this.session;
	}

	/**
	 * 
	 * @return
	 */
	public DataEngineContext getContext() {
		return this.context;
	}

	/**
	 * 
	 * @return
	 */
	public List getColumnEdgeSort() {
		return getEdgeSort(ICubeQueryDefinition.COLUMN_EDGE);
	}

	/**
	 * 
	 * @return
	 */
	public List getRowEdgeSort() {
		return getEdgeSort(ICubeQueryDefinition.ROW_EDGE);
	}

	/**
	 * 
	 * @return
	 */
	public List getPageEdgeSort() {
		return getEdgeSort(ICubeQueryDefinition.PAGE_EDGE);
	}

	/**
	 * 
	 * @return
	 */
	public String getQueryResultsId() {
		return this.queryResultsId;
	}

	/**
	 * 
	 * @param id
	 */
	public void setQueryResultsId(String id) {
		this.queryResultsId = id;
	}

	/**
	 * 
	 * @return
	 */
	public IBaseQueryResults getOuterResults() {
		return this.outResults;
	}

	public Scriptable getScope() {
		return scope;
	}

	/**
	 * 
	 * @param edgeType
	 * @return
	 */
	private List getEdgeSort(int edgeType) {
		List l = this.defn.getSorts();
		List result = new ArrayList();
		for (int i = 0; i < l.size(); i++) {
			ICubeSortDefinition sort = (ICubeSortDefinition) l.get(i);
			if (this.defn.getEdge(edgeType) != null && sort.getTargetLevel() != null && this.defn.getEdge(edgeType)
					.getDimensions().contains(sort.getTargetLevel().getHierarchy().getDimension())) {
				result.add(sort);
			}
		}

		Collections.sort(result, new Comparator() {

			public int compare(Object arg0, Object arg1) {
				int level1 = ((ICubeSortDefinition) arg0).getTargetLevel().getHierarchy().getLevels()
						.indexOf(((ICubeSortDefinition) arg0).getTargetLevel());
				int level2 = ((ICubeSortDefinition) arg1).getTargetLevel().getHierarchy().getLevels()
						.indexOf(((ICubeSortDefinition) arg1).getTargetLevel());

				if (level1 == level2)
					return 0;
				else if (level1 < level2)
					return -1;
				else
					return 1;

			}
		});
		return result;
	}
}
