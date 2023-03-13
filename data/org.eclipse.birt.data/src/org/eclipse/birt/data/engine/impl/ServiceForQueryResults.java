/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IExpressionCollection;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.querydefn.BaseExpression;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.aggregation.AggrInfo;
import org.eclipse.birt.data.engine.expression.CompiledExpression;
import org.eclipse.birt.data.engine.expression.ExpressionCompiler;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.DataSetRuntime.Mode;
import org.eclipse.birt.data.engine.odi.IAggrInfo;
import org.eclipse.birt.data.engine.odi.IEventHandler;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.eclipse.birt.data.engine.script.JSResultSetRow;
import org.mozilla.javascript.Scriptable;

/**
*
*/
public class ServiceForQueryResults implements IServiceForQueryResults {
	private DataEngineSession session;
	private IPreparedQueryService queryService;
	private IQueryExecutor queryExecutor;

	private PreparedDataSourceQuery reportQuery;
	private IBaseQueryDefinition queryDefn;

	private ExprManager exprManager;

	private Scriptable scope;
	private int nestedLevel;
	private int startingRawId;

	private static Logger logger = Logger.getLogger(ServiceForQueryResults.class.getName());

	/**
	 *
	 * @param context
	 * @param scope
	 * @param nestedLevel
	 * @param reportQuery
	 * @param query
	 * @param queryExecutor
	 * @param queryDefn
	 * @param exprManager
	 * @throws DataException
	 */
	public ServiceForQueryResults(DataEngineSession session, Scriptable scope, int nestedLevel,
			PreparedDataSourceQuery reportQuery, IPreparedQueryService query, IQueryExecutor queryExecutor,
			IBaseQueryDefinition queryDefn, ExprManager exprManager) throws DataException {
		Object[] params = { session, scope, Integer.valueOf(nestedLevel), reportQuery, query, queryExecutor, queryDefn,
				exprManager };
		logger.entering(ServiceForQueryResults.class.getName(), "ServiceForQueryResults", params);
		assert reportQuery != null && queryExecutor != null;

		this.session = session;
		this.scope = scope;
		this.nestedLevel = nestedLevel;
		this.reportQuery = reportQuery;
		this.queryService = query;
		this.queryExecutor = queryExecutor;
		this.queryDefn = queryDefn;
		this.exprManager = exprManager;
		this.startingRawId = calculateStartingIndex(queryExecutor);
		logger.exiting(ServiceForQueryResults.class.getName(), "ServiceForQueryResults");
	}

	/**
	 *
	 * @param queryExecutor
	 * @return
	 * @throws DataException
	 */
	private int calculateStartingIndex(IQueryExecutor queryExecutor) throws DataException {
		return (queryExecutor instanceof ISubQueryExecutor)
				? (((ISubQueryExecutor) queryExecutor).getSubQueryStartingIndex())
				: 0;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IServiceForQueryResults#getScope()
	 */
	@Override
	public Scriptable getScope() {
		return this.scope;
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.impl.IServiceForQueryResults#getNestedLevel()
	 */
	@Override
	public int getNestedLevel() {
		return this.nestedLevel;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getQueryDefn()
	 */
	@Override
	public IBaseQueryDefinition getQueryDefn() {
		return queryDefn;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getPreparedQuery()
	 */
	@Override
	public IPreparedQuery getPreparedQuery() {
		return this.reportQuery;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getGroupLevel()
	 */
	@Override
	public int getGroupLevel() {
		if (queryService instanceof PreparedSubquery) {
			PreparedSubquery subQuery = (PreparedSubquery) queryService;
			return subQuery.getGroupLevel();
		} else {
			return 0;
		}
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getDataSetRuntime(int)
	 */
	@Override
	public DataSetRuntime getDataSetRuntime() {
		return queryExecutor.getDataSet();
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getDataSetRuntimeList()
	 */
	@Override
	public DataSetRuntime[] getDataSetRuntimes(int count) {
		assert count >= 0;

		DataSetRuntime[] dsRuns = new DataSetRuntime[count];

		if (count > 1) {
			DataSetRuntime[] innerDsRuns;
			IQueryExecutor executor = queryExecutor;
			innerDsRuns = executor.getNestedDataSets(count - 1);
			for (int i = 0; i < count - 1; i++) {
				dsRuns[i] = innerDsRuns[i];
			}
		}

		dsRuns[count - 1] = queryExecutor.getDataSet();
		return dsRuns;
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getResultMetaData()
	 */
	@Override
	public IResultMetaData getResultMetaData() throws DataException {
		return queryExecutor.getResultMetaData();
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#getResultIterator()
	 */
	@Override
	public IResultIterator executeQuery() throws DataException {
		queryExecutor.execute(new EventHandler());
		return queryExecutor.getOdiResultSet();
	}

	/**
	 * The row object can have different meaning in the different context. In the
	 * phrase of data set process, the row refers to the data set row, but in the
	 * phrase of result set process, the row refers to the result set row. So in the
	 * first phrase, let the JSRowObject stands for the row, and in the second
	 * phrase, let the JSResultSetRowObject stands for the row. This event handler
	 * class will help to do such a switch.
	 */
	private class EventHandler implements IEventHandler {
		//
		private JSResultSetRow jsResultSetRow;
		private IExecutorHelper helper;

		/*
		 * @see org.eclipse.birt.data.engine.odi.IEventHandler#handleProcessEndOfDataSet
		 * (org.eclipse.birt.data.engine.odi.IResultIterator)
		 */
		@Override
		public void handleEndOfDataSetProcess(IResultIterator resultIterator) throws DataException {
			jsResultSetRow = new JSResultSetRow(resultIterator, exprManager, queryExecutor.getQueryScope(), helper,
					session.getEngineContext().getScriptContext());

			getDataSetRuntime().setJSResultSetRow(jsResultSetRow);
			getDataSetRuntime().setMode(Mode.Query);
		}

		/*
		 * @see
		 * org.eclipse.birt.data.engine.odi.IEventHandler#getValue(java.lang.String)
		 */
		@Override
		public Object getValue(IResultObject rsObject, int index, String name) throws DataException {
			if (jsResultSetRow == null) {
				return rsObject.getFieldValue(index);
			}

			return jsResultSetRow.getValue(rsObject, index, name);
		}

		/*
		 * @see org.eclipse.birt.data.engine.odi.IEventHandler#isRowID(java.lang.String)
		 */
		@Override
		public boolean isRowID(int index, String name) throws DataException {
			IBaseExpression baseExpr = exprManager.getExpr(name);
			if (baseExpr instanceof IScriptExpression) {
				String exprText = ((IScriptExpression) baseExpr).getText();
				if (exprText == null) {
					return false;
				} else if (exprText.trim().equalsIgnoreCase("dataSetRow[0]")
						|| exprText.trim().equalsIgnoreCase("dataSetRow._rowPosition")) {
					return true;
				} else {
					return false;
				}
			}
			return false;

		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.data.engine.odi.IEventHandler#getBaseExpr(java.lang.String)
		 */
		@Override
		public IBinding getBinding(String name) throws DataException {
			if (name == null) {
				return null;
			}
			return ServiceForQueryResults.this.exprManager.getBinding(name);
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.data.engine.odi.IEventHandler#getExecutorHelper()
		 */
		@Override
		public IExecutorHelper getExecutorHelper() {
			return this.helper;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.eclipse.birt.data.engine.odi.IEventHandler#setExecutorHelper(org.eclipse.
		 * birt.data.engine.impl.IExecutorHelper)
		 */
		@Override
		public void setExecutorHelper(IExecutorHelper helper) {
			this.helper = helper;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.data.engine.odi.IEventHandler#getColumnMappings()
		 */
		@Override
		public Map getColumnBindings() throws DataException {
			Map result = new HashMap();
			List groupBindingColumns = exprManager.getBindingExprs();
			for (int i = 0; i < groupBindingColumns.size(); i++) {
				GroupBindingColumn gbc = (GroupBindingColumn) groupBindingColumns.get(i);
				Iterator it = gbc.getColumnNames().iterator();
				while (it.hasNext()) {
					String name = it.next().toString();
					result.put(name, gbc.getBinding(name));
				}
			}
			return result;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.data.engine.odi.IEventHandler#getColumnMappings()
		 */
		@Override
		public List<IBinding> getAllColumnBindings() {
			return getColumnBindings(ServiceForQueryResults.this.queryDefn);
		}

		/**
		 * Get column bindings of certain query.
		 *
		 * @param defn
		 * @return
		 */
		private List<IBinding> getColumnBindings(IBaseQueryDefinition defn) {
			List<IBinding> result = new ArrayList<>();
			Iterator temp = defn.getBindings().keySet().iterator();
			while (temp.hasNext()) {
				Object key = temp.next();
				result.add((IBinding) defn.getBindings().get(key));
			}

			// Put all column bindings in subquery definitions in group
			result.addAll(populateGroupColumnBindings(defn.getGroups().iterator()));

			// Put all column bindings in subquery definition.
			result.addAll(populateSubQueryColumnBindings(defn.getSubqueries().iterator()));

			if (defn instanceof IQueryDefinition && ((IQueryDefinition) defn).needAutoBinding()) {
				Map<String, IBaseExpression> autoBindings = exprManager.getAutoBindingExprMap();
				for (Entry<String, IBaseExpression> entry : autoBindings.entrySet()) {
					result.add(new Binding(entry.getKey(), entry.getValue()));
				}
			}
			return result;
		}

		/**
		 *
		 * @param groups
		 * @return
		 */
		private List<IBinding> populateGroupColumnBindings(Iterator groups) {
			List<IBinding> result = new ArrayList<>();

			while (groups.hasNext()) {
				IGroupDefinition gd = (IGroupDefinition) groups.next();

				result.addAll(populateSubQueryColumnBindings(gd.getSubqueries().iterator()));
			}
			return result;
		}

		/**
		 *
		 * @param subs
		 * @return
		 */
		private List<IBinding> populateSubQueryColumnBindings(Iterator subs) {
			List<IBinding> result = new ArrayList<>();

			while (subs.hasNext()) {
				IBaseQueryDefinition defn1 = (IBaseQueryDefinition) subs.next();
				result.addAll(getColumnBindings(defn1));
			}

			return result;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.data.engine.odi.IEventHandler#getAppContext()
		 */
		@Override
		public Map getAppContext() {
			return queryExecutor.getAppContext();
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.eclipse.birt.data.engine.odi.IEventHandler#getAggrDefinitions()
		 */
		@Override
		public List getAggrDefinitions() throws DataException {
			List result = populateAggrDefinitions();
			sort(result);
			return result;
		}

		/**
		 * Populate a list of AggrDefinitions which is defined by user in column
		 * binding.
		 *
		 * @return
		 * @throws DataException
		 */
		private List populateAggrDefinitions() throws DataException {

			List result = new ArrayList();

			ExpressionCompiler compiler = new ExpressionCompiler();
			compiler.setDataSetMode(false);

			List groupBindingColumns = exprManager.getBindingExprs();
			for (int i = 0; i < groupBindingColumns.size(); i++) {
				GroupBindingColumn gbc = (GroupBindingColumn) groupBindingColumns.get(i);
				Iterator it = gbc.getColumnNames().iterator();
				while (it.hasNext()) {
					String name = it.next().toString();
					populateOneAggrDefinition(result, session.getEngineContext().getScriptContext(), compiler, gbc,
							name);
				}
			}
			return result;

		}

		/**
		 * Populate One AggrDefinition according to given binding name.
		 *
		 * @param result
		 * @param cx
		 * @param compiler
		 * @param gbc
		 * @param name
		 * @throws DataException
		 */
		private void populateOneAggrDefinition(List result, ScriptContext cx, ExpressionCompiler compiler,
				GroupBindingColumn gbc, String name) throws DataException {
			IBinding binding = gbc.getBinding(name);

			if (isAggregationBinding(binding)) {
				List argument = new ArrayList(binding.getArguments());
				IAggrFunction aggrFunction = AggregationManager.getInstance().getAggregation(binding.getAggrFunction());
				// Before new aggregation extension point is introduced,
				// The binding expression is serve as first argument of aggregation.
				if (binding.getExpression() != null) {
					argument.add(0, binding.getExpression());
				}
				IBaseExpression[] compiledArgu = populateAggregationArgument(cx, compiler, binding, argument,
						aggrFunction);

				compiler.compile(binding.getFilter(), cx);

				AggrInfo aggrDefn = new AggrInfo(name, gbc.getGroupLevel(), aggrFunction, compiledArgu,
						binding.getFilter());

				result.add(aggrDefn);
			}
		}

		/**
		 * Return if a binding is an Aggregation Binding.
		 *
		 * @param binding
		 * @return
		 * @throws DataException
		 */
		private boolean isAggregationBinding(IBinding binding) throws DataException {
			return binding.getAggrFunction() != null;
		}

		/**
		 * Populate the aggregation binding argument. Please note the binding expression
		 * will serve as first argument of a binding in case of necessary.
		 *
		 * @param cx
		 * @param compiler
		 * @param binding
		 * @param argument
		 * @param aggrFunction
		 * @return
		 * @throws DataException
		 */
		private IBaseExpression[] populateAggregationArgument(ScriptContext cx, ExpressionCompiler compiler,
				IBinding binding, List argument, IAggrFunction aggrFunction) throws DataException {
			IBaseExpression[] compiledArgu = new IBaseExpression[argument.size()];
			for (int j = 0; j < argument.size(); j++) {
				IScriptExpression scriptExpr = (IScriptExpression) argument.get(j);
				compiler.compile(scriptExpr, cx);
				compiledArgu[j] = scriptExpr;

			}
			return compiledArgu;
		}

		/**
		 * Sort the binding according to their calculation level.
		 *
		 * @param aggrDefns
		 * @throws DataException
		 */
		private void sort(List aggrDefns) throws DataException {
			try {
				Map nameMap = populateBindingNameMap(aggrDefns);

				Map aggrRefMap = new HashMap();
				Map aggrRefGroupLevelMap = new HashMap();
				for (int i = 0; i < aggrDefns.size(); i++) {
					IAggrInfo aggrDefn = (IAggrInfo) aggrDefns.get(i);

					List exprs = new ArrayList();
					for (int x = 0; x < aggrDefn.getArgument().length; x++) {
						exprs.add(aggrDefn.getArgument()[x]);
					}

					if (aggrDefn.getFilter() != null) {
						exprs.add(aggrDefn.getFilter());
					}

					Set aggrRefs = new HashSet();

					Set aggrRefList = new HashSet();
					boolean use0AggrLevel = this.popAggrRefFromExprs(aggrRefList, exprs, nameMap, aggrDefn);

					if (aggrRefList.size() > 0) {
						aggrRefs.addAll(aggrRefList);
					}

					int groupLevel = 0;

					int groupLevelInAggr = getGroupLevel(aggrRefs);

					if (!use0AggrLevel) {
						groupLevel = groupLevelInAggr;
					}

					aggrRefMap.put(aggrDefn.getName(), aggrRefs);

					aggrRefGroupLevelMap.put(aggrDefn.getName(), Integer.valueOf(groupLevel));
				}

				popualteCalcuateRound(aggrDefns, nameMap, aggrRefMap, aggrRefGroupLevelMap);

				sortAggrDefnsAccordingToCalLvl(aggrDefns);
			} catch (BirtException be) {
				throw DataException.wrap(be);
			}
		}

		private int getGroupLevel(Set aggrRefs) throws DataException {
			Iterator it = aggrRefs.iterator();
			int groupLevel = -1;
			while (it.hasNext()) {
				IAggrInfo aggr = (IAggrInfo) it.next();
				if (groupLevel < aggr.getGroupLevel()) {
					groupLevel = aggr.getGroupLevel();
				}
			}
			return groupLevel;
		}

		/**
		 * Sort the aggregation definition list according to their calculation level.
		 *
		 * @param aggrDefns
		 */
		private void sortAggrDefnsAccordingToCalLvl(List aggrDefns) {
			Collections.sort(aggrDefns, new Comparator() {

				@Override
				public int compare(Object o1, Object o2) {
					assert o1 instanceof IAggrInfo;
					assert o2 instanceof IAggrInfo;
					int round1 = ((IAggrInfo) o1).getRound();
					int round2 = ((IAggrInfo) o2).getRound();
					if (round1 == round2) {
						return 0;
					}
					if (round1 > round2) {
						return 1;
					} else {
						return -1;
					}
				}
			});
		}

		/**
		 * Populate the calculation level of the aggr defns.
		 *
		 * @param aggrDefns
		 * @param nameMap
		 * @param aggrRefMap
		 * @param aggrRefGroupLevelMap
		 */
		private void popualteCalcuateRound(List aggrDefns, Map nameMap, Map aggrRefMap, Map aggrRefGroupLevelMap) {
			List aggrDefnsCopy = new ArrayList(aggrDefns);
			int calculateRound = -1;
			while (aggrDefnsCopy.size() > 0) {
				calculateRound++;
				List removedNames = new ArrayList();
				for (Iterator it = aggrRefMap.keySet().iterator(); it.hasNext();) {
					String name = it.next().toString();
					IAggrInfo defn = (IAggrInfo) nameMap.get(name);
					Set aggrRefList = (Set) aggrRefMap.get(name);
					if (aggrRefList.size() == 0) {
						defn.setRound(calculateRound);
						defn.setCalculateLevel(((Integer) aggrRefGroupLevelMap.get(name)).intValue());
						aggrDefnsCopy.remove(defn);
						removedNames.add(defn);
					}
				}

				for (int i = 0; i < removedNames.size(); i++) {
					aggrRefMap.remove(((IAggrInfo) removedNames.get(i)).getName());
				}
				for (Iterator it = aggrRefMap.values().iterator(); it.hasNext();) {
					Set temp = (Set) it.next();
					temp.removeAll(removedNames);
				}
			}
		}

		/**
		 * Popualte a binding name <--> binding map.
		 *
		 * @param aggrDefns
		 * @return
		 */
		private Map populateBindingNameMap(List aggrDefns) {
			Map nameMap = new HashMap();
			for (int i = 0; i < aggrDefns.size(); i++) {
				IAggrInfo aggrDefn = (IAggrInfo) aggrDefns.get(i);
				nameMap.put(aggrDefn.getName(), aggrDefn);
			}
			return nameMap;
		}

		/**
		 *
		 * @param expr
		 * @return
		 * @throws BirtException
		 */
		private boolean hasDataSetRowReference(IScriptExpression expr) throws BirtException {
			return !ExpressionUtil.extractColumnExpressions(expr.getText(), ExpressionUtil.DATASET_ROW_INDICATOR)
					.isEmpty();
		}

		/**
		 *
		 * @param aggrReferences
		 * @param exprs
		 * @param aggrMap
		 * @return
		 * @throws DataException
		 */
		private boolean popAggrRefFromExprs(Set aggrReferences, List exprs, Map aggrMap, IAggrInfo aggrInfo)
				throws DataException {
			boolean[] result = new boolean[exprs.size()];
			for (int i = 0; i < exprs.size(); i++) {
				result[i] = this.popAggrRefFromBaseExpr(aggrReferences, (IBaseExpression) exprs.get(i), aggrMap);
			}

			boolean base = false;

			for (int i = 0; i < result.length; i++) {
				if (isConstantExpr((IBaseExpression) exprs.get(i))) {
					continue;
				}
				base = result[i];
				break;
			}

			for (int i = 0; i < result.length; i++) {
				if (isConstantExpr((IBaseExpression) exprs.get(i))) {
					continue;
				}

				if (result[i] != base) {
					throw new DataException(ResourceConstants.INVALID_NESTED_AGGR_GROUP, aggrInfo.getName());
				}
			}
			return result.length == 0 ? false : base;
		}

		/**
		 *
		 * @param aggrReferences
		 * @param expr
		 * @param aggrMap
		 * @return
		 * @throws DataException
		 */
		private boolean popAggrRefFromBaseExpr(Set aggrReferences, IBaseExpression expr, Map aggrMap)
				throws DataException {
			try {
				boolean result = false;
				if (expr instanceof IScriptExpression) {
					result = popAggrRefFromScriptExpr(aggrReferences, (IScriptExpression) expr, aggrMap);

				} else if (expr instanceof IConditionalExpression) {
					IConditionalExpression ce = (IConditionalExpression) expr;

					result = popAggrRefFromScriptExpr(aggrReferences, ce.getExpression(), aggrMap)
							|| popAggrRefFromBaseExpr(aggrReferences, ce.getOperand1(), aggrMap)
							|| popAggrRefFromBaseExpr(aggrReferences, ce.getOperand2(), aggrMap);
				} else if (expr instanceof IExpressionCollection) {
					IExpressionCollection ce = (IExpressionCollection) expr;
					Object[] exprs = ce.getExpressions().toArray();
					for (int i = 0; i < exprs.length; i++) {
						if (popAggrRefFromBaseExpr(aggrReferences, (IBaseExpression) exprs[i], aggrMap)) {
							result = true;
							break;
						}
					}
				}
				return result;
			} catch (BirtException be) {
				throw DataException.wrap(be);
			}
		}

		/**
		 * Populate the aggregation references, return whether the aggregation should be
		 * calculated on OVERALL level, which is indicated by reference to "dataSetRow"
		 * java script object.
		 *
		 * @param aggrReferences
		 * @param expr
		 * @param aggrMap
		 * @return
		 * @throws DataException
		 */
		private boolean popAggrRefFromScriptExpr(Set aggrReferences, IScriptExpression expr, Map aggrMap)
				throws DataException {
			try {
				List usedRowReferences = ExpressionUtil.extractColumnExpressions(expr.getText());
				boolean result = this.hasDataSetRowReference(expr);
				for (int i = 0; i < usedRowReferences.size(); i++) {
					Object o = aggrMap.get(((IColumnBinding) usedRowReferences.get(i)).getResultSetColumnName());
					if (o != null) {
						aggrReferences.add(o);
					} else {
						if (this.getBinding(
								((IColumnBinding) usedRowReferences.get(i)).getResultSetColumnName()) == null) {
							return false;
						}
						result = popAggrRefFromBaseExpr(aggrReferences,
								this.getBinding(((IColumnBinding) usedRowReferences.get(i)).getResultSetColumnName())
										.getExpression(),
								aggrMap) || result;
					}
				}
				return result;
			} catch (BirtException be) {
				throw DataException.wrap(be);
			}
		}

		/**
		 *
		 * @param expr
		 * @return
		 * @throws DataException
		 */
		private boolean isConstantExpr(IBaseExpression expr) throws DataException {
			if (expr == null) {
				return true;
			}
			if (!(expr instanceof IScriptExpression)) {
				return false;
			}
			try {
				return ExpressionUtil.extractColumnExpressions(((IScriptExpression) expr).getText()).isEmpty()
						&& (!hasDataSetRowReference((IScriptExpression) expr));
			} catch (BirtException e) {
				throw DataException.wrap(e);
			}
		}

		@Override
		public DataSetRuntime getDataSetRuntime() {
			return ServiceForQueryResults.this.getDataSetRuntime();
		}
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.impl.IQueryService#execSubquery(org.eclipse.birt
	 * .data.engine.odi.IResultIterator, java.lang.String,
	 * org.mozilla.javascript.Scriptable)
	 */
	@Override
	public IQueryResults execSubquery(IResultIterator iterator, IQueryExecutor parentExecutor, String subQueryName,
			Scriptable subScope) throws DataException {
		return queryService.execSubquery(iterator, parentExecutor, subQueryName, subScope);
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IQueryService#close()
	 */
	@Override
	public void close() {
		if (queryExecutor != null) {
			queryExecutor.close();
			queryExecutor = null;
		}
	}

	// ----------------related with column binding-----------------

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.impl.IServiceForQueryResults#
	 * validateQueryColumBinding()
	 */
	@Override
	public void validateQuery() throws DataException {
		this.exprManager.validateColumnBinding();
		this.validateFilters();
		this.validateSorts();
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.impl.IQueryService#getBaseExpression(java.lang.
	 * String)
	 */
	@Override
	public IBaseExpression getBindingExpr(String exprName) throws DataException {
		return this.exprManager.getExpr(exprName);
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.impl.IQueryService#getAutoBindingExpr(java.lang.
	 * String)
	 */
	@Override
	public IScriptExpression getAutoBindingExpr(String exprName) {
		return this.exprManager.getAutoBindingExpr(exprName);
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.impl.IServiceForQueryResults#getAllBindingExprs(
	 * )
	 */
	@Override
	public List getAllBindingExprs() {
		return this.exprManager.getBindingExprs();
	}

	/*
	 * @see org.eclipse.birt.data.engine.impl.IServiceForQueryResults#
	 * getAllAutoBindingExprs()
	 */
	@Override
	public Map getAllAutoBindingExprs() {
		return this.exprManager.getAutoBindingExprMap();
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.impl.IServiceForQueryResults#initAutoBinding()
	 */
	@Override
	public void initAutoBinding() throws DataException {
		if (!needAutoBinding()) {
			return;
		}
		IResultClass metaData = queryExecutor.getOdiResultClass();
		if (metaData == null) {
			// Failed to fetch meta data during query preparation
			return;
		}
		int columnCount = metaData.getFieldCount();
		for (int i = 0; i < columnCount; i++) {
			int colIndex = i + 1;
			try {
				String colName = metaData.getFieldName(colIndex);
				String colAlias = metaData.getFieldAlias(colIndex);
				if (isTempColumn(colName)) {
					continue;
				}

				Class odiDataType = metaData.getFieldValueClass(colIndex);
				ScriptExpression baseExpr = new ScriptExpression(ExpressionUtil.createJSDataSetRowExpression(colName),
						DataTypeUtil.toApiDataType(odiDataType));
				CompiledExpression compiledExpr = ExpressionCompilerUtil.compile(baseExpr.getText(),
						session.getEngineContext().getScriptContext());
				baseExpr.setHandle(compiledExpr);
				this.exprManager.addAutoBindingExpr(colName, baseExpr);
				if (colAlias != null) {
					this.exprManager.addAutoBindingExpr(colAlias, baseExpr);
				}
			} catch (BirtException e) {
				// impossible, ignore
			}
		}

	}

	public static boolean isTempColumn(String name) {
		return (name.matches("\\Q_{$TEMP_GROUP_\\E\\d*\\Q$}_\\E") || name.matches("\\Q_{$TEMP_SORT_\\E\\d*\\Q$}_\\E")
				|| name.matches("\\Q_{$TEMP_FILTER_\\E\\d*\\Q$}_\\E"));
	}

	/**
	 * @return
	 */
	private boolean needAutoBinding() {
		if (this.queryDefn instanceof IQueryDefinition) {
			return ((IQueryDefinition) queryDefn).needAutoBinding();
		}

		return false;
	}

	/**
	 *
	 * @param filters
	 * @throws DataException
	 */
	private void validateFilters() throws DataException {
		for (int i = 0; i < this.queryDefn.getFilters().size(); i++) {
			IFilterDefinition filter = (IFilterDefinition) this.queryDefn.getFilters().get(i);
			if (hasRowNumRefExpr(filter.getExpression())) {
				throw new DataException(ResourceConstants.FILTER_EXPR_CONTAIN_ROW_NUM);
			}
		}
	}

	/**
	 *
	 * @param sorts
	 * @throws DataException
	 */
	private void validateSorts() throws DataException {
		for (int i = 0; i < this.queryDefn.getSorts().size(); i++) {
			ISortDefinition sort = (ISortDefinition) this.queryDefn.getSorts().get(i);
			if (hasRowNumRefExpr(sort.getExpression())) {
				throw new DataException(ResourceConstants.SORT_EXPR_CONTAIN_ROW_NUM);
			}
		}
	}

	/**
	 *
	 * @param expr
	 * @return
	 * @throws BirtException
	 * @throws DataException
	 */
	private boolean hasRowNumRefExpr(IBaseExpression expr) throws DataException {
		if (expr instanceof IScriptExpression) {
			return hasRowNumRefExpr((IScriptExpression) expr);
		} else if (expr instanceof IConditionalExpression) {
			IConditionalExpression ce = (IConditionalExpression) expr;
			return hasRowNumRefExpr(ce.getExpression()) || hasRowNumRefExpr(ce.getOperand1())
					|| hasRowNumRefExpr(ce.getOperand2());
		}
		return false;
	}

	/**
	 *
	 * @param expr
	 * @return
	 * @throws BirtException
	 * @throws DataException
	 */
	private boolean hasRowNumRefExpr(IScriptExpression expr) throws DataException {
		try {
			if (expr == null || expr.getText() == null || BaseExpression.constantId.equals(expr.getScriptId())) {
				return false;
			}
			if (expr.getText().matches(".*\\Qrow.__rownum\\E.*")) {
				return true;
			}
			return findRowNumReferenceInBindings(ExpressionUtil.extractColumnExpressions(expr.getText()));
		} catch (BirtException e) {
			throw new DataException(e.getLocalizedMessage());
		}
	}

	/**
	 *
	 * @param bindingNames
	 * @param bindings
	 * @return
	 * @throws DataException
	 */
	private boolean findRowNumReferenceInBindings(List bindingNames) throws DataException {
		for (int i = 0; i < bindingNames.size(); i++) {
			IBinding binding = (IBinding) this.queryDefn.getBindings()
					.get(((IColumnBinding) bindingNames.get(i)).getResultSetColumnName());
			if (binding == null) {
				return false;
			}
			IBaseExpression expr = binding.getExpression();
			if (hasRowNumRefExpr(expr)) {
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.impl.IServiceForQueryResults#getStartingRawID()
	 */
	@Override
	public int getStartingRawID() throws DataException {
		return this.startingRawId;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.impl.IServiceForQueryResults#getQueryExecutor()
	 */
	@Override
	public IQueryExecutor getQueryExecutor() throws DataException {
		return this.queryExecutor;
	}

	@Override
	public DataEngineSession getSession() {
		return session;
	}
}
