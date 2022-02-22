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

package org.eclipse.birt.chart.reportitem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemHelper;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IInputParameterBinding;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.emf.common.util.EList;

/**
 * The class is responsible to add group bindings of chart on query definition.
 *
 * @since BIRT 2.3
 */
public class ChartBaseQueryHelper extends AbstractChartBaseQueryGenerator {
	/**
	 * Constructor of the class.
	 *
	 * @param chart
	 * @param handle
	 * @param modelAdapter
	 */
	public ChartBaseQueryHelper(ReportItemHandle handle, Chart cm, IModelAdapter modelAdapter) {
		// The default behavior is to wrap complex expression as new binding
		this(handle, cm, modelAdapter, true);
	}

	/**
	 *
	 * @param handle
	 * @param cm
	 * @param modelAdapter
	 * @param bCreateBindingForExpression indicates if query definition should
	 *                                    create a new binding for the complex
	 *                                    expression. If the expression is simply a
	 *                                    binding name, always do not add the new
	 *                                    binding.
	 */
	public ChartBaseQueryHelper(ReportItemHandle handle, Chart cm, IModelAdapter modelAdapter,
			boolean bCreateBindingForExpression) {
		super(handle, cm, bCreateBindingForExpression, modelAdapter);
	}

	@Override
	public IDataQueryDefinition createBaseQuery(IDataQueryDefinition parent) throws ChartException {
		BaseQueryDefinition query = createQueryDefinition(parent);

		if (query == null) {
			return null;
		}

		generateExtraBindings(query);

		return query;
	}

	protected BaseQueryDefinition createQueryDefinition(IDataQueryDefinition parent) throws ChartException {
		BaseQueryDefinition query = null;

		BaseQueryDefinition parentQuery = null;
		if (parent instanceof BaseQueryDefinition) {
			parentQuery = (BaseQueryDefinition) parent;
		}

		DataSetHandle dsHandle = fReportItemHandle.getDataSet();

		if (dsHandle == null) {
			// dataset reference error
			String dsName = (String) fReportItemHandle.getProperty(ReportItemHandle.DATA_SET_PROP);
			if (dsName != null && dsName.length() > 0) {
				throw new ChartException(ChartReportItemPlugin.ID, ChartException.DATA_BINDING,
						new EngineException(MessageConstants.UNDEFINED_DATASET_ERROR, dsName));
			}
			// we has data set name defined, so test if we have column
			// binding here.

			if (parent instanceof ICubeQueryDefinition) {
				return null;
				// return createSubQuery(item, null);
			}

			if (ChartReportItemUtil.canScaleShared(fReportItemHandle, fChartModel)) {
				// Add min/max binding to parent query since it's global min/max
				addMinMaxBinding(ChartItemUtil.getBindingHolder(fReportItemHandle), parentQuery);
			}

			// we have column binding, create a sub query.
			query = createSubQuery(fReportItemHandle, parentQuery);
		} else {
			// The report item has a data set definition, must create a query
			// for it.
			query = new QueryDefinition(parentQuery);

			((QueryDefinition) query).setIsSummaryQuery(needSummaryQuery());

			((QueryDefinition) query).setDataSetName(dsHandle.getQualifiedName());

			// bind the query with parameters
			((QueryDefinition) query).getInputParamBindings()
					.addAll(createParamBindings(fReportItemHandle.paramBindingsIterator()));

			Iterator<?> iter = getAllUsedBindings(fReportItemHandle);
			while (iter.hasNext()) {
				ComputedColumnHandle binding = (ComputedColumnHandle) iter.next();
				addColumnBinding(query, binding);
			}

			addSortAndFilter(fReportItemHandle, query);
		}
		return query;
	}

	protected Iterator<?> getAllUsedBindings(ReportItemHandle handle) {
		return ChartReportItemHelper.instance().getAllUsedBindings(fChartModel, handle);
	}

	protected void addColumnBinding(IBaseQueryDefinition transfer, ComputedColumnHandle columnBinding)
			throws ChartException {
		try {
			transfer.addBinding(this.modelAdapter.adaptBinding(columnBinding));
		} catch (AdapterException | DataException ex) {
			throw new ChartException(ChartReportItemPlugin.ID, ChartException.DATA_BINDING, ex);
		}
	}

	private void addMinMaxBinding(ReportItemHandle handle, BaseQueryDefinition query) throws ChartException {
		// Add min/max bindings for the query expression in first value
		// series, so share the scale later
		try {
			String queryExp = getExpressionOfValueSeries();

			ComputedColumn ccMin = StructureFactory.newComputedColumn(handle, ChartReportItemConstants.NAME_QUERY_MIN);
			ccMin.setAggregateFunction(DesignChoiceConstants.AGGREGATION_FUNCTION_MIN);
			ccMin.setExpression(queryExp);
			addColumnBinding(query, handle.addColumnBinding(ccMin, false));

			ComputedColumn ccMax = StructureFactory.newComputedColumn(handle, ChartReportItemConstants.NAME_QUERY_MAX);
			ccMax.setAggregateFunction(DesignChoiceConstants.AGGREGATION_FUNCTION_MAX);
			ccMax.setExpression(queryExp);
			addColumnBinding(query, handle.addColumnBinding(ccMax, false));
		} catch (SemanticException e) {
			throw new ChartException(ChartReportItemPlugin.ID, ChartException.DATA_BINDING, e);
		}
	}

	protected BaseQueryDefinition createSubQuery(ReportItemHandle handle, BaseQueryDefinition parentQuery)
			throws ChartException {
		BaseQueryDefinition query = null;
		// sub query must be defined in a transform
		if (parentQuery == null) {
			// no parent query exits, so create a empty query for it.
			query = new QueryDefinition(null);
		} else {
			// create a sub query
			query = new SubqueryDefinition(ChartReportItemConstants.NAME_SUBQUERY + handle.getElement().getID(),
					parentQuery);
			parentQuery.getSubqueries().add(query);
		}

		Iterator iter = getAllUsedBindings(handle);
		while (iter.hasNext()) {
			ComputedColumnHandle binding = (ComputedColumnHandle) iter.next();
			addColumnBinding(query, binding);
		}

		addSortAndFilter(handle, query);

		return query;
	}

	protected void addSortAndFilter(ReportItemHandle handle, BaseQueryDefinition query) {
		if (handle instanceof ExtendedItemHandle) {
			query.getFilters().addAll(createFilters(modelAdapter, ((ExtendedItemHandle) handle).filtersIterator()));
		} else if (handle instanceof TableHandle) {
			query.getFilters().addAll(createFilters(modelAdapter, ((TableHandle) handle).filtersIterator()));
		}
	}

	/**
	 * create a filter array given a filter condition handle iterator
	 *
	 * @param modelAdapter
	 * @param iter         the iterator
	 * @return filter array
	 */
	protected static List<IFilterDefinition> createFilters(IModelAdapter modelAdapter,
			Iterator<FilterConditionHandle> iter) {
		List<IFilterDefinition> filters = new ArrayList<>();
		if (iter != null) {

			while (iter.hasNext()) {
				FilterConditionHandle filterHandle = iter.next();
				IFilterDefinition filter = modelAdapter.adaptFilter(filterHandle);
				filters.add(filter);
			}
		}
		return filters;
	}

	/**
	 * Creates input parameter bindings
	 *
	 * @param iter parameter bindings iterator
	 * @return a list of input parameter bindings
	 * @throws ChartException
	 */
	protected List<IInputParameterBinding> createParamBindings(Iterator<ParamBindingHandle> iter)
			throws ChartException {
		List<IInputParameterBinding> list = new ArrayList<>();
		if (iter != null) {
			while (iter.hasNext()) {
				ParamBindingHandle modelParamBinding = iter.next();
				List<ScriptExpression> exprs = ChartReportItemUtil.newExpression(modelAdapter, modelParamBinding);
				for (ScriptExpression expr : exprs) {
					list.add(new InputParameterBinding(modelParamBinding.getParamName(), expr));
				}
			}
		}
		return list;
	}

	private String getExpressionOfValueSeries() {
		SeriesDefinition ySd;
		if (fChartModel instanceof ChartWithAxes) {
			Axis yAxis = ((ChartWithAxes) fChartModel).getAxes().get(0).getAssociatedAxes().get(0);
			ySd = yAxis.getSeriesDefinitions().get(0);
		} else {
			ySd = ((ChartWithoutAxes) fChartModel).getSeriesDefinitions().get(0).getSeriesDefinitions().get(0);
		}
		Query query = ySd.getDesignTimeSeries().getDataDefinition().get(0);
		return query.getDefinition();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.reportitem.AbstractChartBaseQueryGenerator#
	 * createBaseQuery(java.util.List)
	 */
	@Override
	public IDataQueryDefinition createBaseQuery(List columns) {
		throw new UnsupportedOperationException("Don't be implemented in the class."); //$NON-NLS-1$
	}

	/**
	 * Returns all query expression definitions on chart.
	 *
	 * @param chart
	 * @return query list
	 * @since 2.3
	 */
	public static List<Query> getAllQueryExpressionDefinitions(Chart chart) {
		List<Query> queryList = new ArrayList<>();
		if (chart instanceof ChartWithAxes) {
			Axis xAxis = ((ChartWithAxes) chart).getAxes().get(0);
			// Add base series query
			queryList.addAll(getQueries(xAxis.getSeriesDefinitions()));

			EList<Axis> axisList = xAxis.getAssociatedAxes();
			for (int i = 0; i < axisList.size(); i++) {
				EList<SeriesDefinition> sds = axisList.get(i).getSeriesDefinitions();

				// Add Y grouping query.
				Query q = sds.get(0).getQuery();
				if (q != null) {
					queryList.add(q);
				}

				// Add value series querys.
				queryList.addAll(getQueries(sds));

			}
		} else if (chart instanceof ChartWithoutAxes) {
			SeriesDefinition sdBase = ((ChartWithoutAxes) chart).getSeriesDefinitions().get(0);
			queryList.addAll(sdBase.getDesignTimeSeries().getDataDefinition());

			Query q = sdBase.getSeriesDefinitions().get(0).getQuery();
			if (q != null) {
				queryList.add(q);
			}

			queryList.addAll(getQueries(sdBase.getSeriesDefinitions()));
		}
		return queryList;
	}

	/**
	 * Returns queries of series definition.
	 *
	 * @param seriesDefinitions
	 * @return
	 */
	private static List<Query> getQueries(EList<SeriesDefinition> seriesDefinitions) {
		List<Query> querys = new ArrayList<>();
		for (Iterator<SeriesDefinition> iter = seriesDefinitions.iterator(); iter.hasNext();) {
			querys.addAll(iter.next().getDesignTimeSeries().getDataDefinition());
		}
		return querys;
	}

	/**
	 * processes a table/list group
	 */
	public static IGroupDefinition handleGroup(GroupHandle handle, IBaseQueryDefinition query,
			IModelAdapter modelAdapter) {
		GroupDefinition groupDefn = new GroupDefinition(handle.getName());
		groupDefn.setKeyExpression(modelAdapter.adaptExpression(ChartReportItemUtil.getExpression(handle)));
		String interval = handle.getInterval();
		if (interval != null) {
			groupDefn.setInterval(parseInterval(interval));
		}
		// inter-range
		groupDefn.setIntervalRange(handle.getIntervalRange());
		// inter-start-value
		groupDefn.setIntervalStart(handle.getIntervalBase());
		// sort-direction
		String direction = handle.getSortDirection();
		if (direction != null) {
			groupDefn.setSortDirection(parseSortDirection(direction));
		}

		groupDefn.getSorts().addAll(createSorts(handle, modelAdapter));
		groupDefn.getFilters().addAll(createFilters(modelAdapter, handle));
		query.getGroups().add(groupDefn);

		return groupDefn;
	}

	/**
	 * converts interval string values to integer values
	 */
	private static int parseInterval(String interval) {
		if (DesignChoiceConstants.INTERVAL_YEAR.equals(interval)) {
			return IGroupDefinition.YEAR_INTERVAL;
		}
		if (DesignChoiceConstants.INTERVAL_MONTH.equals(interval)) {
			return IGroupDefinition.MONTH_INTERVAL;
		}
		if (DesignChoiceConstants.INTERVAL_WEEK.equals(interval)) //
		{
			return IGroupDefinition.WEEK_INTERVAL;
		}
		if (DesignChoiceConstants.INTERVAL_QUARTER.equals(interval)) {
			return IGroupDefinition.QUARTER_INTERVAL;
		}
		if (DesignChoiceConstants.INTERVAL_DAY.equals(interval)) {
			return IGroupDefinition.DAY_INTERVAL;
		}
		if (DesignChoiceConstants.INTERVAL_HOUR.equals(interval)) {
			return IGroupDefinition.HOUR_INTERVAL;
		}
		if (DesignChoiceConstants.INTERVAL_MINUTE.equals(interval)) {
			return IGroupDefinition.MINUTE_INTERVAL;
		}
		if (DesignChoiceConstants.INTERVAL_PREFIX.equals(interval)) {
			return IGroupDefinition.STRING_PREFIX_INTERVAL;
		}
		if (DesignChoiceConstants.INTERVAL_SECOND.equals(interval)) {
			return IGroupDefinition.SECOND_INTERVAL;
		}
		if (DesignChoiceConstants.INTERVAL_INTERVAL.equals(interval)) {
			return IGroupDefinition.NUMERIC_INTERVAL;
		}
		return IGroupDefinition.NO_INTERVAL;
	}

	/**
	 * @param direction "asc" or "desc" string
	 * @return integer value defined in <code>ISortDefn</code>
	 */
	private static int parseSortDirection(String direction) {
		if ("asc".equals(direction)) { //$NON-NLS-1$
			return ISortDefinition.SORT_ASC;
		}
		if ("desc".equals(direction)) { //$NON-NLS-1$
			return ISortDefinition.SORT_DESC;
		}
		assert false;
		return 0;
	}

	/**
	 * create filter array given a GroupHandle
	 *
	 * @param modelAdapter
	 * @param group        the GroupHandle
	 * @return filter array
	 */
	private static List<IFilterDefinition> createFilters(IModelAdapter modelAdapter, GroupHandle group) {
		return createFilters(modelAdapter, group.filtersIterator());
	}

	/**
	 * create all sort conditions given a sort key handle iterator
	 *
	 * @param iter the iterator
	 * @return sort array
	 */
	public static List<ISortDefinition> createSorts(Iterator<SortKeyHandle> iter, IModelAdapter modelAdapter) {
		List<ISortDefinition> sorts = new ArrayList<>();
		if (iter != null) {
			while (iter.hasNext()) {
				sorts.add(modelAdapter.adaptSort(iter.next()));
			}
		}
		return sorts;
	}

	/**
	 * create sort array by giving GroupHandle
	 *
	 * @param group the GroupHandle
	 * @return the sort array
	 */
	private static List<ISortDefinition> createSorts(GroupHandle group, IModelAdapter modelAdapter) {
		return createSorts(group.sortsIterator(), modelAdapter);
	}

	protected boolean needSummaryQuery() {
		// Check if it is summary table.
		if (fReportItemHandle instanceof TableHandle) {
			return ((TableHandle) fReportItemHandle).isSummaryTable();
		}
		return false;
	}
}
