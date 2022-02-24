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

package org.eclipse.birt.data.engine.executor.transform.pass;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ResultClass;
import org.eclipse.birt.data.engine.executor.ResultFieldMetadata;
import org.eclipse.birt.data.engine.executor.aggregation.AggrDefnRoundManager;
import org.eclipse.birt.data.engine.executor.aggregation.AggregationHelper;
import org.eclipse.birt.data.engine.executor.cache.SortSpec;
import org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState;
import org.eclipse.birt.data.engine.executor.transform.IExpressionProcessor;
import org.eclipse.birt.data.engine.executor.transform.OdiResultSetWrapper;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.executor.transform.TransformationConstants;
import org.eclipse.birt.data.engine.executor.transform.group.IncrementalUpdateGroupFilter;
import org.eclipse.birt.data.engine.expression.ExpressionCompiler;
import org.eclipse.birt.data.engine.impl.ComputedColumnHelper;
import org.eclipse.birt.data.engine.impl.FilterByRow;
import org.eclipse.birt.data.engine.impl.PreparedQueryUtil;
import org.eclipse.birt.data.engine.odi.IResultClass;

/**
 * The class used to process ResultSet data.
 * 
 */
class ResultSetProcessUtil extends RowProcessUtil {
	/**
	 * 
	 */
	private List cachedSort;

	private boolean groupingDone;

	/**
	 * 
	 * @param populator
	 * @param iccState
	 * @param computedColumnHelper
	 * @param filterByRow
	 * @param psController
	 */
	private ResultSetProcessUtil(ResultSetPopulator populator, ComputedColumnsState iccState,
			ComputedColumnHelper computedColumnHelper, FilterByRow filterByRow, PassStatusController psController) {
		super(populator, iccState, computedColumnHelper, filterByRow, psController);
	}

	/**
	 * 
	 * @param populator
	 * @param iccState
	 * @param computedColumnHelper
	 * @param filterByRow
	 * @param psController
	 * @param sortList
	 * @throws DataException
	 */
	public static void doPopulate(ResultSetPopulator populator, ComputedColumnsState iccState,
			ComputedColumnHelper computedColumnHelper, FilterByRow filterByRow, PassStatusController psController,
			List sortList) throws DataException {
		ResultSetProcessUtil instance = new ResultSetProcessUtil(populator, iccState, computedColumnHelper, filterByRow,
				psController);
		instance.cachedSort = sortList;
		instance.populateResultSet();

	}

	public static void doPopulateAggregation(ResultSetPopulator populator, ComputedColumnsState iccState,
			ComputedColumnHelper computedColumnHelper, FilterByRow filterByRow, PassStatusController psController,
			List sortList) throws DataException {
		ResultSetProcessUtil instance = new ResultSetProcessUtil(populator, iccState, computedColumnHelper, filterByRow,
				psController);
		instance.cachedSort = sortList;
		instance.populateAggregation();
	}

	public static void doPopulateNoUpdateAggrFiltering(ResultSetPopulator populator, ComputedColumnsState iccState,
			ComputedColumnHelper computedColumnHelper, FilterByRow filterByRow, PassStatusController psController,
			List sortList) throws DataException {
		ResultSetProcessUtil instance = new ResultSetProcessUtil(populator, iccState, computedColumnHelper, filterByRow,
				psController);
		instance.cachedSort = sortList;
		instance.groupingDone = true;
		instance.populateNoUpdateAggrFiltering();
	}

	/**
	 * 
	 * @param stopSign
	 * @throws DataException
	 */
	private void populateResultSet() throws DataException {
		// The computed columns that need multipass
		List aggCCList = prepareComputedColumns(TransformationConstants.RESULT_SET_MODEL);

		// Grouping will also be done in this method, for currently we only support
		// simple group keys
		// that is, group keys cannot contain aggregation.
		doRowFiltering();

		// TODO remove me
		populateTempComputedColumns(this.getAggrComputedColumns(aggCCList, false));
		///////////////

		List aggrDefns = this.populator.getEventHandler().getAggrDefinitions();

		prepareAggregations(aggrDefns);

		// Filter group instances.
		doGroupFiltering();

		if (needDoGroupFiltering() && psController.needDoOperation(PassStatusController.AGGR_ROW_FILTERING))
			prepareAggregations(aggrDefns);

		// Filter aggregation filters
		doAggrRowFiltering();

		// Do row sorting
		doRowSorting();

		// Do group sorting
		doGroupSorting();

		if (!groupingDone) {
			PassUtil.pass(this.populator, new OdiResultSetWrapper(populator.getResultIterator()), true);
			groupingDone = true;
		}

		clearTemporaryComputedColumns(iccState);

		populateAggregation();

		// Do 2nd phase: no update aggregation filters here.
		doNoUpdateAggrGroupFilter();

		doNoUpdateAggrRowFilter();
	}

	private void populateNoUpdateAggrFiltering() throws DataException {
		doNoUpdateAggrGroupFilter();

		doNoUpdateAggrRowFilter();
	}

	private void populateAggregation() throws DataException {
		calculateAggregationsInColumnBinding();

		/************************************/
		// TODO remove me
		// Temp code util model makes the backward comp.
		ExpressionCompiler compiler = new ExpressionCompiler();
		compiler.setDataSetMode(false);
		for (Iterator it = this.populator.getEventHandler().getColumnBindings().values().iterator(); it.hasNext();) {
			try {
				IBinding binding = (IBinding) it.next();
				compiler.compile(binding.getExpression(),
						this.populator.getSession().getEngineContext().getScriptContext());
			} catch (DataException e) {
				// do nothing
			}
		}

		/*************************************/
		//
		populateAggregationInBinding();
	}

	private void calculateAggregationsInColumnBinding() throws DataException {
		IExpressionProcessor ep = populator.getExpressionProcessor();

		Map results = populator.getEventHandler().getColumnBindings();

		DummyICCState iccState = new DummyICCState(results);

		ep.setResultIterator(populator.getResultIterator());

		while (!iccState.isFinish()) {
			ep.evaluateMultiPassExprOnCmp(iccState, false);
		}
	}

	/**
	 * 
	 * @throws DataException
	 */
	private void populateAggregationInBinding() throws DataException {
		this.populator.getExpressionProcessor().setResultIterator(this.populator.getResultIterator());
		this.populator.getResultIterator().clearAggrValueHolder();
		List aggrDefns = this.populator.getEventHandler().getAggrDefinitions();

		AggrDefnRoundManager factory = new AggrDefnRoundManager(aggrDefns);
		for (int i = 0; i < factory.getRound(); i++) {
			AggregationHelper helper = new AggregationHelper(factory.getAggrDefnManager(i), this.populator);
			this.populator.getResultIterator().addAggrValueHolder(helper);

		}
	}

	/**
	 * Class DummyICCState is used by ExpressionProcessor to calculate multipass
	 * aggregations.
	 *
	 */
	private static class DummyICCState implements IComputedColumnsState {
		private Object[] exprs;
		private Object[] names;
		private boolean[] isValueAvailable;

		/**
		 * 
		 * @param exprs
		 * @param names
		 * @throws DataException
		 */
		DummyICCState(Map columnMappings) throws DataException {
			this.exprs = columnMappings.values().toArray();
			this.names = columnMappings.keySet().toArray();
			this.isValueAvailable = new boolean[exprs.length];
			/*
			 * for( int i = 0; i < exprs.length; i ++ ) { IBinding binding =
			 * ((IBinding)exprs[i]);
			 * 
			 * if( binding.getExpression( ).getHandle( )== null ) { this.isValueAvailable[i]
			 * = false; }else { this.isValueAvailable[i] = true; }
			 * 
			 * }
			 */
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#
		 * isValueAvailable(int)
		 */
		public boolean isValueAvailable(int index) {
			return this.isValueAvailable[index];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#getName
		 * (int)
		 */
		public String getName(int index) {
			return this.names[index].toString();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#
		 * getExpression(int)
		 */
		public IBaseExpression getExpression(int index) throws DataException {
			return ((IBinding) exprs[index]).getExpression();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#
		 * setValueAvailable(int)
		 */
		public void setValueAvailable(int index) {
			this.isValueAvailable[index] = true;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#
		 * getCount()
		 */
		public int getCount() {
			return this.isValueAvailable.length;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#
		 * getComputedColumn(int)
		 */
		public IComputedColumn getComputedColumn(int index) {
			return null;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.data.engine.executor.transform.IComputedColumnsState#
		 * setModel(int)
		 */
		public void setModel(int model) {

		}

		/**
		 * 
		 * @return
		 */
		public boolean isFinish() {
			for (int i = 0; i < isValueAvailable.length; i++) {
				if (!isValueAvailable[i])
					return false;
			}
			return true;
		}
	}

	/**
	 * 
	 * @param aggrDefns
	 * @param stopSign
	 * @throws DataException
	 */
	private void prepareAggregations(List aggrDefns) throws DataException {
		boolean needGroupFiltering = this.needDoGroupFiltering();
		boolean needGroupSorting = this.needDoGroupSorting();
		boolean needRowSortOnAggregation = this.needRowSortOnAggregation();
		boolean needAggrFiltering = psController.needDoOperation(PassStatusController.AGGR_ROW_FILTERING);
		if (needPreCalculateForGroupFilterSort(needGroupFiltering, needGroupSorting) || needAggrFiltering
				|| needRowSortOnAggregation) {
			// TODO: Enhance me so that invalid computed column will not be evaluated at all
			if (needRowSortOnAggregation && this.computedColumnHelper != null)
				this.computedColumnHelper.suppressException(true);
			// ENDTODO

			if (!groupingDone) {
				PassUtil.pass(this.populator, new OdiResultSetWrapper(populator.getResultIterator()), true);
			}
			this.populator.getExpressionProcessor().setResultIterator(this.populator.getResultIterator());
			AggrDefnRoundManager factory = new AggrDefnRoundManager(aggrDefns);
			this.populator.getResultIterator().clearAggrValueHolder();
			for (int i = 0; i < factory.getRound(); i++) {
				AggregationHelper helper = new AggregationHelper(factory.getAggrDefnManager(i), this.populator);
				this.populator.getResultIterator().addAggrValueHolder(helper);
			}
			// TODO: Enhance me so that invalid computed column will not be evaluated at all
			if (this.computedColumnHelper != null)
				this.computedColumnHelper.suppressException(false);
			// ENDTODO

		}
	}

	/**
	 * Indicate whether need to pre calculate the aggregations.
	 * 
	 * @param needGroupFiltering
	 * @param needGroupSorting
	 * @return
	 */
	private boolean needPreCalculateForGroupFilterSort(boolean needGroupFiltering, boolean needGroupSorting) {
		return needGroupFiltering || needGroupSorting;
	}

	/**
	 * Indicate whether need to do group filtering.
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean needDoGroupFiltering() {
		for (int i = 0; i < this.populator.getQuery().getGrouping().length; i++) {
			List<IFilterDefinition> groupFilters = this.populator.getQuery().getGrouping()[i].getFilters();
			if (groupFilters != null && groupFilters.size() > 0) {
				for (int k = 0; k < groupFilters.size(); k++) {
					if (groupFilters.get(k).updateAggregation())
						return true;
				}
			}
		}
		return false;
	}

	private boolean needRowSortOnAggregation() throws DataException {
		return PreparedQueryUtil.hasSortOnAggregat(this.populator.getQuery().getQueryDefinition());
	}

	/**
	 * Indicate whether need to do group sorting.
	 * 
	 * @return
	 */
	private boolean needDoGroupSorting() {
		for (int i = 0; i < this.populator.getQuery().getGrouping().length; i++) {
			List groupFilters = this.populator.getQuery().getGrouping()[i].getSorts();
			if (groupFilters != null && groupFilters.size() > 0)
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param aggCCList
	 * @param stopSign
	 * @throws DataException
	 */
	private void populateTempComputedColumns(List aggCCList) throws DataException {
		if (psController.needDoOperation(PassStatusController.RESULT_SET_TEMP_COMPUTED_COLUMN_POPULATING)) {
			if (aggCCList.size() != 0 || psController.needDoOperation(PassStatusController.GROUP_ROW_FILTERING)) {
				PassUtil.pass(this.populator, new OdiResultSetWrapper(populator.getResultIterator()), true);
				this.groupingDone = true;
			}
			if (aggCCList.size() != 0) {
				computedColumnHelper.getComputedColumnList().clear();
				computedColumnHelper.getComputedColumnList().addAll(aggCCList);
				computedColumnHelper.setRePrepare(true);
				IExpressionProcessor ep = populator.getExpressionProcessor();

				ep.setResultIterator(populator.getResultIterator());

				// Populate all temp computed columns ( used for query
				// filtering,sorting )
				while (!isICCStateFinish()) {
					ep.evaluateMultiPassExprOnCmp(iccState, false);
				}
			}

			doGroupRowFilter();
		}
	}

	/**
	 * @param stopSign
	 * @throws DataException
	 */
	private void doGroupSorting() throws DataException {
		if (!this.needDoGroupSorting())
			return;

		if (!groupingDone) {
			PassUtil.pass(this.populator, new OdiResultSetWrapper(populator.getResultIterator()), true);
			groupingDone = true;
		}

		// If the aggregation value is subject to change caused by group instance filter
		// and row filter, recalculate the
		// aggregations.
		if (this.needDoGroupFiltering() || psController.needDoOperation(PassStatusController.AGGR_ROW_FILTERING))
			prepareAggregations(this.populator.getEventHandler().getAggrDefinitions());

		this.populator.getGroupProcessorManager().doGroupSorting(this.populator.getCache(),
				this.populator.getExpressionProcessor());
	}

	/**
	 * @param stopSign
	 * @throws DataException
	 */
	private void doRowSorting() throws DataException {
		this.populator.getQuery().setOrdering(this.cachedSort);

		SortSpec spec = this.populator.getGroupProcessorManager().getGroupCalculationUtil().getSortSpec();
		if (spec != null && spec.length() > 0) {
			PassUtil.pass(this.populator, new OdiResultSetWrapper(populator.getResultIterator()), true);
			this.groupingDone = true;
		}
	}

	/**
	 * @param stopSign
	 * @throws DataException
	 */
	private void doGroupFiltering() throws DataException {
		if (!this.needDoGroupFiltering())
			return;
		if (!groupingDone) {
			PassUtil.pass(this.populator, new OdiResultSetWrapper(populator.getResultIterator()), true);
			groupingDone = true;
		}

		this.populator.getGroupProcessorManager().doGroupFiltering(this.populator.getCache(),
				this.populator.getExpressionProcessor());
	}

	/**
	 * @param stopSign
	 * @throws DataException
	 */
	private void doRowFiltering() throws DataException {
		if (!psController.needDoOperation(PassStatusController.RESULT_SET_FILTERING))
			return;

		if (needRowSortOnAggregation() && this.computedColumnHelper != null)
			this.computedColumnHelper.suppressException(true);

		boolean changeMaxRows = filterByRow.getFilterList(FilterByRow.GROUP_FILTER).size()
				+ filterByRow.getFilterList(FilterByRow.AGGR_FILTER).size() > 0;
		applyFilters(FilterByRow.QUERY_FILTER, changeMaxRows);
		filterByRow.setWorkingFilterSet(FilterByRow.NO_FILTER);

		if (this.computedColumnHelper != null)
			this.computedColumnHelper.suppressException(false);
	}

	/**
	 * @param stopSign
	 * @throws DataException
	 */
	private void doAggrRowFiltering() throws DataException {
		if (!psController.needDoOperation(PassStatusController.AGGR_ROW_FILTERING))
			return;

		applyFilters(FilterByRow.AGGR_FILTER, false);
		filterByRow.setWorkingFilterSet(FilterByRow.NO_FILTER);
	}

	/**
	 * 
	 * @return
	 */
	private boolean isICCStateFinish() {
		for (int i = 0; i < iccState.getCount(); i++) {
			if (!iccState.isValueAvailable(i))
				return false;
		}
		return true;
	}

	/**
	 * @param stopSign
	 * @throws DataException
	 */
	private void doGroupRowFilter() throws DataException {
		if (!psController.needDoOperation(PassStatusController.GROUP_ROW_FILTERING))
			return;
		// Apply group row filters (Total.isTopN, Total.isBottomN..)
		filterByRow.setWorkingFilterSet(FilterByRow.GROUP_FILTER);
		PassUtil.pass(this.populator, new OdiResultSetWrapper(populator.getResultIterator()), true);

		filterByRow.setWorkingFilterSet(FilterByRow.NO_FILTER);

	}

	/**
	 * 
	 * @param iccState
	 * @param stopSign
	 * @throws DataException
	 */
	private void clearTemporaryComputedColumns(ComputedColumnsState iccState) throws DataException {
		if (!psController.needDoOperation(PassStatusController.RESULT_SET_TEMP_COMPUTED_COLUMN_POPULATING))
			return;
		iccState.setModel(TransformationConstants.ALL_MODEL);
		populator.getExpressionProcessor().clear();

		computedColumnHelper.setModel(TransformationConstants.NONE_MODEL);

		// computedColumnHelper.getComputedColumnList( ).clear( );

		// restore computed column helper to its original state. by call this
		// method the computedColumnHelper only contain user defined computed
		// columns
		// and all temporary computed columns are exclued.
		// restoreComputedColumns( iccState, computedColumnHelper );

		cleanTempColumns();
	}

	/**
	 * Clean the temporary data.
	 * 
	 * @throws DataException
	 */
	private void cleanTempColumns() throws DataException {
		IResultClass newMeta = rebuildResultClass(populator.getResultSetMetadata());
		populator.setResultSetMetadata(newMeta);
		populator.getCache().setResultClass(newMeta);
		PassUtil.pass(populator, new OdiResultSetWrapper(populator.getResultIterator()), false);

		populator.getCache().reset();
		populator.getCache().next();
		populator.getGroupProcessorManager().getGroupCalculationUtil().getGroupInformationUtil().setLeaveGroupIndex(0);
	}

	/**
	 * Build an IResultClass instance excluding temp computed columns.
	 * 
	 * @param meta
	 * @return
	 * @throws DataException
	 */
	private static IResultClass rebuildResultClass(IResultClass meta) throws DataException {
		List projectedColumns = new ArrayList();

		for (int i = 1; i <= meta.getFieldCount(); i++) {
			if (!PassUtil.isTemporaryResultSetComputedColumn(meta.getFieldName(i))) {
				ResultFieldMetadata field = new ResultFieldMetadata(0, meta.getFieldName(i), meta.getFieldAlias(i),
						meta.getFieldBindings(i), meta.getFieldValueClass(i), meta.getFieldNativeTypeName(i),
						meta.isCustomField(i), meta.getAnalysisType(i), meta.getAnalysisColumn(i),
						meta.isIndexColumn(i), meta.isCompressedColumn(i));
				field.setAlias(meta.getFieldAlias(i));

				projectedColumns.add(field);
			}
		}
		IResultClass result = new ResultClass(projectedColumns);
		return result;
	}

	private void doNoUpdateAggrRowFilter() throws DataException {
		if (filterByRow == null)
			return;

		if (!psController.needDoOperation(PassStatusController.NOUPDATE_ROW_FILTERING))
			return;

		NoUpdateFilterCalculator.applyFilters(this.populator, this.filterByRow);
	}

	private void doNoUpdateAggrGroupFilter() throws DataException {
		if (!needNoUpdateAggrGroupFiltering())
			return;

		if (!groupingDone) {
			PassUtil.pass(this.populator, new OdiResultSetWrapper(populator.getResultIterator()), true);
			groupingDone = true;
		}

		new IncrementalUpdateGroupFilter(this.populator).doFilters();

	}

	@SuppressWarnings("unchecked")
	private boolean needNoUpdateAggrGroupFiltering() {
		for (int i = 0; i < this.populator.getQuery().getGrouping().length; i++) {
			List<IFilterDefinition> groupFilters = this.populator.getQuery().getGrouping()[i].getFilters();
			if (groupFilters != null && groupFilters.size() > 0) {
				for (int k = 0; k < groupFilters.size(); k++) {
					if (!groupFilters.get(k).updateAggregation())
						return true;
				}
			}
		}
		return false;
	}
}
