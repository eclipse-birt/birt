/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseDataSetDesign;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IColumnDefinition;
import org.eclipse.birt.data.engine.api.ICombinedOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IJointDataSetDesign;
import org.eclipse.birt.data.engine.api.IOdaDataSetDesign;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IScriptDataSetDesign;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.core.security.URLSecurity;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.expression.NamedExpression;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.eclipse.birt.data.engine.script.ScriptConstants;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;

/**
 * Create concreate class of IPreparedQuery
 */
public class PreparedQueryUtil {
	private static final int BASED_ON_DATASET = 2;
	private static final int BASED_ON_PRESENTATION = 3;
	private static Logger logger = Logger.getLogger(PreparedQueryUtil.class.getName());

	/**
	 * Creates a new instance of the proper subclass based on the type of the query
	 * passed in.
	 *
	 * @param dataEngine
	 * @param queryDefn
	 * @param appContext Application context map; could be null.
	 * @return PreparedReportQuery
	 * @throws DataException
	 */
	public static IPreparedQuery newInstance(DataEngineImpl dataEngine, IQueryDefinition queryDefn, Map appContext)
			throws DataException {
		assert dataEngine != null;
		assert queryDefn != null;

		if (queryDefn.getDistinctValue()) {
			addAllBindingAsSortKey(queryDefn);
		}

		optimizeForTransientQuery(appContext, queryDefn);

		validateQuery(dataEngine, queryDefn);
		FilterPrepareUtil.prepareFilters(queryDefn, dataEngine.getContext().getScriptContext());
		IQueryContextVisitor contextVisitor = QueryContextVisitorUtil.createQueryContextVisitor(queryDefn, appContext);
		if (queryDefn.getSourceQuery() != null) {
			if (queryDefn.getSourceQuery() instanceof IQueryDefinition) {
				IBaseDataSetDesign dset = cloneDataSetDesign(
						dataEngine.getDataSetDesign(((IQueryDefinition) queryDefn.getSourceQuery()).getDataSetName()),
						appContext);
				IPreparedQuery preparedQuery = QueryPrepareUtil.preparePresentationQuery(dataEngine, queryDefn, dset,
						appContext, contextVisitor);
				if (preparedQuery != null) {
					return preparedQuery;
				}
			}

			return new PreparedIVDataExtractionQuery(dataEngine, queryDefn, appContext, contextVisitor);
		}

		IPreparedQuery preparedQuery = null;
		IBaseDataSetDesign dset = cloneDataSetDesign(dataEngine.getDataSetDesign(queryDefn.getDataSetName()),
				appContext);

		if (queryDefn.getQueryResultsID() != null && !dataEngine.getContext().isDashBoardEnabled()) {
			if (dataEngine.getSession().getEngineContext().getMode() == DataEngineContext.MODE_PRESENTATION) {
				preparedQuery = QueryPrepareUtil.preparePresentationQuery(dataEngine, queryDefn, dset, appContext,
						contextVisitor);
			}
			if (preparedQuery != null) {
				return preparedQuery;
			}

			if (dataEngine.getContext().getMode() == DataEngineContext.MODE_GENERATION
					|| dataEngine.getContext().getMode() == DataEngineContext.DIRECT_PRESENTATION) {
				return new DummyPreparedQuery(queryDefn, dataEngine.getSession(), appContext);
			}

			if (dataEngine.getContext().getMode() == DataEngineContext.MODE_PRESENTATION) {
				return new DummyPreparedQuery(queryDefn, dataEngine.getSession(), dataEngine.getContext(),
						queryDefn.getQueryExecutionHints() != null
								? queryDefn.getQueryExecutionHints().getTargetGroupInstances()
								: null);
			}
			return QueryPrepareUtil.prepareIVGenerationQuery(dataEngine, queryDefn, dset, appContext, contextVisitor);
		}

		if (dset != null) {
			FilterPrepareUtil.prepareFilters(dset.getFilters(), dataEngine.getContext().getScriptContext());
			QueryContextVisitorUtil.populateDataSet(contextVisitor, dset, appContext);

			preparedQuery = QueryPrepareUtil.prepareQuery(dataEngine, queryDefn, dset, appContext, contextVisitor);

			if (preparedQuery != null) {
				return preparedQuery;
			}
		}

		if (dset == null) {
			// In new column binding feature, when there is no data set,
			// it is indicated that a dummy data set needs to be created
			// internally. But using the dummy one, the binding expression only
			// can refer to row object and no other object can be refered such
			// as rows.
			if (queryDefn.getQueryResultsID() == null) {
				return new PreparedDummyQuery(queryDefn, dataEngine.getSession());
			}
		}

		if (dset instanceof IScriptDataSetDesign) {
			preparedQuery = new PreparedScriptDSQuery(dataEngine, queryDefn, dset, appContext, contextVisitor);
		} else if (dset instanceof IOdaDataSetDesign) {
			if (dset instanceof IIncreCacheDataSetDesign) {
				preparedQuery = new PreparedIncreCacheDSQuery(dataEngine, queryDefn, dset, appContext);
			} else {
				((BaseDataSetDesign) dataEngine.getDataSetDesign(queryDefn.getDataSetName()))
						.setQueryContextVisitor((Object) contextVisitor);
				if (dset instanceof ICombinedOdaDataSetDesign) {

					preparedQuery = new PreparedCombinedOdaDSQuery(dataEngine, queryDefn, dset, appContext,
							contextVisitor);
				} else {
					preparedQuery = new PreparedOdaDSQuery(dataEngine, queryDefn, dset, appContext, contextVisitor);
				}
			}
		} else if (dset instanceof IJointDataSetDesign) {
			preparedQuery = new PreparedJointDataSourceQuery(dataEngine, queryDefn, dset, appContext, contextVisitor);
		} else {
			preparedQuery = DataSetDesignHelper.createPreparedQueryInstance(dset, dataEngine, queryDefn, appContext);
			if (preparedQuery == null) {
				throw new DataException(ResourceConstants.UNSUPPORTED_DATASET_TYPE, dset.getName());
			}
		}

		return preparedQuery;
	}

	private static void optimizeForTransientQuery(Map appContext, IQueryDefinition query) throws DataException {
		if (appContext != null) {
			IQueryOptimizeHints hints = (IQueryOptimizeHints) appContext.get(IQueryOptimizeHints.QUERY_OPTIMIZE_HINT);
			if (hints == null || !(query instanceof QueryDefinition)) {
				return;
			}

			query.getQueryExecutionHints().setEnablePushDown(hints.enablePushDownForTransientQuery());

			Map<String, List<IFilterDefinition>> filters = hints.getFiltersInAdvance();
			if (filters != null && filters.get(query.getDataSetName()) != null) {
				query.getFilters().addAll(filters.get(query.getDataSetName()));
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static void addAllBindingAsSortKey(IQueryDefinition queryDefn) throws DataException {
		if (!(queryDefn instanceof BaseQueryDefinition)) {
			return;
		}
		Map<String, Integer> sortedBinding = new HashMap<>();
		int sortBindingIndex = 0;
		List<ISortDefinition> sorts = queryDefn.getSorts();
		if (sorts != null) {
			for (ISortDefinition sd : sorts) {
				List<String> bindingNames = ExpressionCompilerUtil.extractColumnExpression(sd.getExpression(),
						ExpressionUtil.ROW_INDICATOR);
				if (bindingNames != null && bindingNames.size() > 0) {
					for (String bindingName : bindingNames) {
						sortedBinding.put(bindingName, sortBindingIndex++);
					}
				} else if (sd.getColumn() != null) {
					sortedBinding.put(sd.getColumn(), sortBindingIndex++);
				}
			}
		}
		Collection<IBinding> bindings = queryDefn.getBindings().values();
		BaseQueryDefinition queryDefinition = ((BaseQueryDefinition) queryDefn);
		for (IBinding binding : bindings) {
			if (!sortedBinding.containsKey(binding.getBindingName())) {
				SortDefinition sd = new SortDefinition();
				sd.setExpression(ExpressionUtil.createJSRowExpression(binding.getBindingName()));
				queryDefinition.addSort(sd);
				sortedBinding.put(binding.getBindingName(), sortBindingIndex++);
			}
		}

		// If there are multiple group keys, make sure they have the same
		// sequences as sort keys, since sort is restricted by group.
		List<GroupDefinition> groups = queryDefn.getGroups();
		if (groups.size() > 1) {
			Collections.sort(groups, (a, b) -> sortedBinding.getOrDefault(getGroupColumn(a), 0)
					- sortedBinding.getOrDefault(getGroupColumn(b), 0));
		}
	}

	private static String getGroupColumn(GroupDefinition gd) {
		if (gd.getKeyColumn() != null && !gd.getKeyColumn().isEmpty()) {
			return gd.getKeyColumn();
		}
		try {
			return ExpressionUtil.getColumnBindingName(gd.getKeyExpression());
		} catch (BirtException e) {
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * validate query
	 *
	 * @param dataEngine
	 * @param queryDefn
	 * @throws DataException
	 */
	private static void validateQuery(DataEngineImpl dataEngine, IQueryDefinition queryDefn) throws DataException {
		try {
			// Need not validate query while in Presentation Mode.
			if (dataEngine.getContext().getMode() == DataEngineContext.MODE_PRESENTATION) {
				return;
			}
			String dataSetName = queryDefn.getDataSetName();
			IBaseDataSetDesign dataSet = dataEngine.getDataSetDesign(dataSetName);
			if (dataSet != null) {
				validateComputedColumns(dataSet);
			}

			validateSort(queryDefn, dataSet);
		} catch (Exception e) {
			throw new DataException(e.getLocalizedMessage(), e);
		}
		// validateSorts( queryDefn );
		// validateSummaryQuery( queryDefn );
	}

	// Just validate the sort against the column binding. No need to validate sort
	// against data set column.
	private static void validateSort(IQueryDefinition queryDefn, IBaseDataSetDesign dataSet) throws BirtException {
		Map bindings = populateValidBinding(queryDefn);
		List<ISortDefinition> toBeRemoved = new ArrayList<>();
		List<ISortDefinition> sorts = queryDefn.getSorts();
		for (ISortDefinition sort : sorts) {
			IScriptExpression sortExpr = sort.getExpression();
			if (sort.getColumn() != null) {
				continue;
			}
			if (sortExpr == null) {
				toBeRemoved.add(sort);
			} else {
				List referedColumns = ExpressionUtil.extractColumnExpressions(sortExpr.getText());
				for (int i = 0; i < referedColumns.size(); i++) {
					IColumnBinding cb = (IColumnBinding) referedColumns.get(i);

					boolean shouldBeRemoved = !bindings.containsKey(cb.getResultSetColumnName())
							&& !"__rownum".equals(cb.getResultSetColumnName());
					if (queryDefn.needAutoBinding() && shouldBeRemoved && dataSet != null) {
						List resultSetHint = dataSet.getResultSetHints();
						for (int j = 0; j < resultSetHint.size(); j++) {
							if (resultSetHint.get(j) instanceof IColumnDefinition) {
								IColumnDefinition hint = (IColumnDefinition) resultSetHint.get(j);
								if (ScriptEvalUtil.compare(hint.getColumnName(), cb.getResultSetColumnName()) == 0
										|| ScriptEvalUtil.compare(hint.getAlias(), cb.getResultSetColumnName()) == 0) {
									shouldBeRemoved = false;
									break;
								}
							}
						}

						if (shouldBeRemoved) {
							List computedColumns = dataSet.getComputedColumns();
							for (int j = 0; j < computedColumns.size(); j++) {
								if (computedColumns.get(j) instanceof IComputedColumn) {
									IComputedColumn cc = (IComputedColumn) computedColumns.get(j);
									if (ScriptEvalUtil.compare(cc.getName(), cb.getResultSetColumnName()) == 0) {
										shouldBeRemoved = false;
										break;
									}
								}
							}
						}
						System.out.println();
					}
					if (shouldBeRemoved) {
						toBeRemoved.add(sort);
						break;
					}
				}
			}
		}

		for (int i = 0; i < toBeRemoved.size(); i++) {
			IScriptExpression expr = toBeRemoved.get(i).getExpression();
			if (expr != null) {
				logger.log(Level.WARNING, "Sort Definition:" + expr.getText()
						+ " is removed because it refers to an inexist column binding.");
			} else {
				logger.log(Level.WARNING, "Empty Sort Definition is removed.");
			}
		}

		queryDefn.getSorts().removeAll(toBeRemoved);
	}

	private static Map populateValidBinding(IQueryDefinition queryDefn) {
		Map bindings = new HashMap();

		IQueryDefinition temp = queryDefn;
		while (temp != null) {
			bindings.putAll(temp.getBindings());
			if (temp.getSourceQuery() instanceof IQueryDefinition) {
				temp = (IQueryDefinition) temp.getSourceQuery();
			} else {
				temp = null;
			}
		}
		return bindings;
	}

	@SuppressWarnings("unchecked")
	public static boolean hasSortOnAggregat(IBaseQueryDefinition iBaseQueryDefinition) throws DataException {
		List<ISortDefinition> sorts = iBaseQueryDefinition.getSorts();
		Map bindings = iBaseQueryDefinition.getBindings();

		if (sorts != null) {
			for (ISortDefinition sd : sorts) {
				List<String> bindingNames = ExpressionCompilerUtil.extractColumnExpression(sd.getExpression(),
						ExpressionUtil.ROW_INDICATOR);
				if (bindingNames != null) {
					for (String bindingName : bindingNames) {
						IBinding binding = (IBinding) bindings.get(bindingName);
						if (binding != null) {
							if (binding.getAggrFunction() != null) {
								return true;
							}

							List refBindingName = ExpressionCompilerUtil.extractColumnExpression(
									binding.getExpression(), ScriptConstants.DATA_SET_BINDING_SCRIPTABLE);
							if (refBindingName.size() > 0) {
								if (existAggregationBinding(refBindingName, bindings)) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	public static boolean existAggregationBinding(Collection<String> bindingNames, Map bindings) throws DataException {
		for (String bindingName : bindingNames) {
			Object binding = bindings.get(bindingName);
			if (binding != null) {
				// Need not worry about the cycling reference here as that is already
				// handled by previous logic
				if (OlapExpressionUtil.isAggregationBinding((IBinding) binding) || existAggregationBinding(ExpressionCompilerUtil.extractColumnExpression(
						((IBinding) binding).getExpression(), ScriptConstants.DATA_SET_BINDING_SCRIPTABLE), bindings)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Check whether computed columns defined in data set are valid
	 *
	 * @param bdsd
	 * @throws DataException
	 */
	@SuppressWarnings("unchecked")
	private static void validateComputedColumns(IBaseDataSetDesign bdsd) throws DataException {
		// check whether dependency cycle exist in computed columns
		List<IComputedColumn> ccs = bdsd.getComputedColumns();
		if (ccs != null) {
			// used check whether reference cycle exists
			Set<NamedExpression> namedExpressions = new HashSet<>();
			for (IComputedColumn cc : ccs) {
				String name = cc.getName();
				if (name == null || name.equals("")) {
					throw new DataException(ResourceConstants.CUSTOM_FIELD_EMPTY);
				}
				IBaseExpression expr = cc.getExpression();
				namedExpressions.add(new NamedExpression(name, expr));
			}
			String nameInvolvedInCycle = ExpressionCompilerUtil.getFirstFoundNameInCycle(namedExpressions,
					ExpressionUtil.ROW_INDICATOR);
			if (nameInvolvedInCycle != null) {
				throw new DataException(ResourceConstants.COMPUTED_COLUMN_CYCLE, nameInvolvedInCycle);
			}
		}

	}

	/**
	 *
	 * @param dataSetDesign
	 * @param appContext
	 * @return
	 * @throws DataException
	 */
	private static IBaseDataSetDesign cloneDataSetDesign(IBaseDataSetDesign dataSetDesign, Map appContext)
			throws DataException {
		if (dataSetDesign instanceof IScriptDataSetDesign) {
			return new ScriptDataSetAdapter(dataSetDesign);
		} else if (dataSetDesign instanceof IOdaDataSetDesign) {
			return adaptOdaDataSetDesign(dataSetDesign, appContext);
		} else if (dataSetDesign instanceof IJointDataSetDesign) {
			return new JointDataSetAdapter(dataSetDesign);
		} else {
			IBaseDataSetDesign design = DataSetDesignHelper.createAdapter(dataSetDesign);
			return design;
		}
	}

	/**
	 * @param dataSetDesign
	 * @param appContext
	 * @return
	 * @throws DataException
	 */
	private static IBaseDataSetDesign adaptOdaDataSetDesign(IBaseDataSetDesign dataSetDesign, Map appContext)
			throws DataException {
		IBaseDataSetDesign adaptedDesign = null;
		URL configFileUrl = IncreCacheDataSetAdapter.getConfigFileURL(appContext);
		if (configFileUrl != null) {
			try {
				InputStream is = configFileUrl.openStream();
				ConfigFileParser parser = new ConfigFileParser(is);
				String id = dataSetDesign.getName();
				if (parser.containDataSet(id)) {
					final String mode = parser.getModeByID(id);
					if ("incremental".equalsIgnoreCase(mode)) {
						String queryTemplate = parser.getQueryTextByID(id);
						String timestampColumn = parser.getTimeStampColumnByID(id);
						String formatPattern = parser.getTSFormatByID(id);
						IncreCacheDataSetAdapter pscDataSet = new IncreCacheDataSetAdapter(
								(IOdaDataSetDesign) dataSetDesign);
						pscDataSet.setCacheMode(IIncreCacheDataSetDesign.MODE_PERSISTENT);
						pscDataSet.setConfigFileUrl(configFileUrl);
						pscDataSet.setQueryTemplate(queryTemplate);
						pscDataSet.setTimestampColumn(timestampColumn);
						pscDataSet.setFormatPattern(formatPattern);
						adaptedDesign = pscDataSet;
					} else {
						String message = (String) AccessController.doPrivileged(new PrivilegedAction<Object>() {
							@Override
							public Object run() {
								return MessageFormat.format(ResourceConstants.UNSUPPORTED_INCRE_CACHE_MODE,
										new Object[] { mode });
							}
						});

						throw new UnsupportedOperationException(message);
					}
				}
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (adaptedDesign == null) {
			if (dataSetDesign instanceof ICombinedOdaDataSetDesign) {
				adaptedDesign = new CombinedOdaDataSetAdapter((ICombinedOdaDataSetDesign) dataSetDesign);
			} else {
				adaptedDesign = new OdaDataSetAdapter((IOdaDataSetDesign) dataSetDesign);
			}
		}
		return adaptedDesign;
	}

	public static void populateSummaryBinding(IQueryDefinition queryDefn, IResultClass rsMeta) throws DataException {
		Set<String> nameSet = new HashSet<>();

		for (int i = 1; i <= rsMeta.getFieldCount(); i++) {
			nameSet.add(rsMeta.getFieldName(i));
		}
		Iterator<IBinding> bindingIt = queryDefn.getBindings().values().iterator();

		Set<String> modifiedAggrBinding = new HashSet<>();
		while (bindingIt.hasNext()) {
			IBinding binding = bindingIt.next();
			if (nameSet.contains(binding.getBindingName())) {
				if (binding.getAggrFunction() != null) {
					modifiedAggrBinding.add(binding.getBindingName());
				}
				binding.setAggrFunction(null);
				binding.getAggregatOns().clear();
				binding.getArguments().clear();
				binding.setExpression(
						new ScriptExpression(ExpressionUtil.createDataSetRowExpression(binding.getBindingName())));
			}
		}

		Iterator groups = queryDefn.getGroups().iterator();
		while (groups.hasNext()) {
			IGroupDefinition group = (IGroupDefinition) groups.next();
			Iterator filters = group.getFilters().iterator();
			List<IFilterDefinition> removedFilter = new ArrayList<>();
			while (filters.hasNext()) {
				IFilterDefinition filter = (IFilterDefinition) filters.next();
				List<String> list = ExpressionCompilerUtil.extractColumnExpression(filter.getExpression(),
						ExpressionUtil.ROW_INDICATOR);
				for (int i = 0; i < list.size(); i++) {
					if (modifiedAggrBinding.contains(list.get(i))) {
						removedFilter.add(filter);
					}
				}
			}

			if (!removedFilter.isEmpty()) {
				queryDefn.getFilters().addAll(removedFilter);
				group.getFilters().removeAll(removedFilter);
			}
		}
	}

	/**
	 * @throws DataException
	 *
	 */
	public static void mappingParentColumnBinding(IBaseQueryDefinition baseQueryDefn) throws DataException {
		IBaseQueryDefinition queryDef = baseQueryDefn;
		while (queryDef instanceof ISubqueryDefinition) {
			queryDef = queryDef.getParentQuery();
			Map parentBindings = queryDef.getBindings();
			addParentBindings(baseQueryDefn, parentBindings);
		}
	}

	/**
	 *
	 * @param parentBindings
	 * @throws DataException
	 */
	static void addParentBindings(IBaseQueryDefinition baseQueryDefn, Map parentBindings) throws DataException {
		Map<String, Boolean> aggrInfo = QueryDefinitionUtil.parseAggregations(parentBindings);
		Iterator it = parentBindings.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (!aggrInfo.get(entry.getKey())) {
				// not an aggregation
				IBinding b = (IBinding) parentBindings.get(entry.getKey());

				if (baseQueryDefn.getBindings().get(entry.getKey()) == null) {
					IBinding binding = new Binding((String) entry.getKey());
					binding.setDataType(b.getDataType());
					binding.setExpression(copyScriptExpr(b.getExpression()));
					baseQueryDefn.addBinding(binding);
				}
			}
		}
	}

	/**
	 * Colon a script expression, however do not populate the "AggregateOn" field.
	 * All the column binding that inherit from parent query by sub query should
	 * have no "AggregateOn" field, for they could not be aggregations. However, if
	 * an aggregateOn field is set to an expression without aggregation, we should
	 * also make it inheritable by sub query for the expression actually involves no
	 * aggregations.
	 *
	 * @param expr
	 * @return
	 */
	private static ScriptExpression copyScriptExpr(IBaseExpression expr) {
		if (expr == null) {
			return null;
		}
		ScriptExpression se = new ScriptExpression(((IScriptExpression) expr).getText(),
				((IScriptExpression) expr).getDataType());
		return se;
	}

}

class OdaDataSetAdapter extends DataSetAdapter implements IOdaDataSetDesign {
	protected IOdaDataSetDesign source;

	public OdaDataSetAdapter(IOdaDataSetDesign source) {
		super(source);
		this.source = (IOdaDataSetDesign) source;
	}

	@Override
	public String getExtensionID() {
		return this.source.getExtensionID();
	}

	@Override
	public String getPrimaryResultSetName() {
		return this.source.getPrimaryResultSetName();
	}

	@Override
	public Map getPrivateProperties() {
		return this.source.getPrivateProperties();
	}

	@Override
	public Map getPublicProperties() {
		return this.source.getPublicProperties();
	}

	@Override
	public String getQueryText() {
		return this.source.getQueryText();
	}

	@Override
	public int getPrimaryResultSetNumber() {
		return this.source.getPrimaryResultSetNumber();
	}

	public QuerySpecification getCombinedQuerySpecification() {
		if (this.source instanceof OdaDataSetDesign) {
			return ((OdaDataSetDesign) this.source).getCombinedQuerySpecification();
		} else {
			return null;
		}
	}
}

class CombinedOdaDataSetAdapter extends OdaDataSetAdapter implements ICombinedOdaDataSetDesign {
	protected ICombinedOdaDataSetDesign source;

	public CombinedOdaDataSetAdapter(ICombinedOdaDataSetDesign source) {
		super(source);
		this.source = source;
	}

	@Override
	public void addDataSetDesign(IOdaDataSetDesign dataSetDesign) {
	}

	@Override
	public Set<IOdaDataSetDesign> getDataSetDesigns() {
		return source.getDataSetDesigns();
	}

}

class JointDataSetAdapter extends DataSetAdapter implements IJointDataSetDesign {
	private IJointDataSetDesign source;

	public JointDataSetAdapter(IBaseDataSetDesign source) {
		super(source);
		this.source = (IJointDataSetDesign) source;
	}

	@Override
	public List getJoinConditions() {
		return this.source.getJoinConditions();
	}

	@Override
	public int getJoinType() {
		return this.source.getJoinType();
	}

	@Override
	public String getLeftDataSetDesignName() {
		return this.source.getLeftDataSetDesignName();
	}

	@Override
	public String getRightDataSetDesignName() {
		return this.source.getRightDataSetDesignName();
	}

	@Override
	public String getLeftDataSetDesignQulifiedName() {
		return this.source.getLeftDataSetDesignQulifiedName();
	}

	@Override
	public String getRightDataSetDesignQulifiedName() {
		return this.source.getRightDataSetDesignQulifiedName();
	}
}

class ScriptDataSetAdapter extends DataSetAdapter implements IScriptDataSetDesign {
	private IScriptDataSetDesign source;

	public ScriptDataSetAdapter(IBaseDataSetDesign source) {
		super(source);
		this.source = (IScriptDataSetDesign) source;
	}

	@Override
	public String getCloseScript() {
		return this.source.getCloseScript();
	}

	@Override
	public String getDescribeScript() {
		return this.source.getDescribeScript();
	}

	@Override
	public String getFetchScript() {
		return this.source.getFetchScript();
	}

	@Override
	public String getOpenScript() {
		return this.source.getOpenScript();
	}
}

/**
 *
 */
class IncreCacheDataSetAdapter extends OdaDataSetAdapter implements IIncreCacheDataSetDesign {

	/**
	 * string patterns for parsing the query.
	 */
	private static final String DATE = "\\Q${DATE}$\\E";
	private static final String TS_COLUMN = "\\Q${TIMESTAMP-COLUMN}$\\E";
	private static final String TS_FORMAT = "\\Q${TIMESTAMP-FORMAT}$\\E";

	protected URL configFileUrl;
	protected String queryTemplate;
	protected String timestampColumn;
	protected String formatPattern;
	protected int cacheMode;

	private String queryForUpdate;

	public IncreCacheDataSetAdapter(IOdaDataSetDesign source) {
		super(source);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IIncreDataSetDesign#getCacheMode()
	 */
	@Override
	public int getCacheMode() {
		return cacheMode;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.api.IIncreDataSetDesign#getConfigFilePath()
	 */
	@Override
	public URL getConfigFileUrl() {
		return configFileUrl;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.IIncreDataSetDesign#getQueryForUpdate(long)
	 */
	@Override
	public String getQueryForUpdate(long timestamp) {
		return parseQuery(timestamp);
	}

	/**
	 *
	 * @param time
	 * @return
	 */
	private String parseQuery(long time) {
		SimpleDateFormat formater = new SimpleDateFormat(formatPattern);
		String timestamp = formater.format(new Timestamp(time));
		if (queryForUpdate == null) {
			queryForUpdate = replaceIgnoreCase(queryTemplate, TS_COLUMN, timestampColumn);
			queryForUpdate = replaceIgnoreCase(queryForUpdate, TS_FORMAT, formatPattern);
		}
		return replaceIgnoreCase(queryForUpdate, DATE, timestamp);
	}

	/**
	 * replace the target substring <code>target</code> in <code>source</code> with
	 * <code>replacement</code> case insensively.
	 *
	 * @param source
	 * @param target
	 * @param replacement
	 * @return
	 */
	private String replaceIgnoreCase(String source, CharSequence target, CharSequence replacement) {
		return Pattern.compile(target.toString(), Pattern.CASE_INSENSITIVE).matcher(source)
				.replaceAll(quote(replacement.toString()));
	}

	/**
	 * Returns a literal replacement <code>String</code> for the specified
	 * <code>String</code>.
	 *
	 * @param s
	 * @return
	 */
	private static String quote(String s) {
		if ((s.indexOf('\\') == -1) && (s.indexOf('$') == -1)) {
			return s;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\\') {
				sb.append('\\');
				sb.append('\\');
			} else if (c == '$') {
				sb.append('\\');
				sb.append('$');
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.api.IIncreDataSetDesign#getTimestampColumn()
	 */
	@Override
	public String getTimestampColumn() {
		return timestampColumn;
	}

	/**
	 * @param configFilePath the configFilePath to set
	 */
	public void setConfigFileUrl(URL configFileUrl) {
		this.configFileUrl = configFileUrl;
	}

	/**
	 * @param queryTemplate the queryTemplate to set
	 */
	public void setQueryTemplate(String queryTemplate) {
		this.queryTemplate = queryTemplate;
	}

	/**
	 * @param timestampColumn the timestampColumn to set
	 */
	public void setTimestampColumn(String timestampColumn) {
		this.timestampColumn = timestampColumn;
	}

	/**
	 * @param formatPattern the formatPattern to set
	 */
	public void setFormatPattern(String formatPattern) {
		this.formatPattern = formatPattern;
	}

	/**
	 * @param cacheMode the cacheMode to set
	 */
	public void setCacheMode(int cacheMode) {
		this.cacheMode = cacheMode;
	}

	/**
	 * the specified configure value can be a path or an URL object represents the
	 * location of the configure file, but the final returned value must be an URL
	 * object or null if fails to parse it.
	 *
	 * @param appContext
	 * @return
	 * @throws DataException
	 */
	public static URL getConfigFileURL(Map appContext) throws DataException {
		if (appContext != null) {
			Object configValue = appContext.get(DataEngine.INCREMENTAL_CACHE_CONFIG);
			URL url = null;
			if (configValue instanceof URL) {
				url = (URL) configValue;
			} else if (configValue instanceof String) {
				String configPath = configValue.toString();
				try {
					url = URLSecurity.getURL(configPath);
				} catch (MalformedURLException e) {
					try {// try to use file protocol to parse configPath
						url = URLSecurity.getURL("file", "/", configPath);
					} catch (MalformedURLException e1) {
						return null;
					}
				}
			}
			return url;
		}
		return null;
	}

}
