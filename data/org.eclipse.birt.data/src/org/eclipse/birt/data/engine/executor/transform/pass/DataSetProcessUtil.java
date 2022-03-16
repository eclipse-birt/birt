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
import java.util.List;

import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.aggregation.AggrDefnManager;
import org.eclipse.birt.data.engine.executor.aggregation.AggrInfo;
import org.eclipse.birt.data.engine.executor.aggregation.AggregationHelper;
import org.eclipse.birt.data.engine.executor.transform.OdiResultSetWrapper;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.executor.transform.TransformationConstants;
import org.eclipse.birt.data.engine.expression.ExpressionCompiler;
import org.eclipse.birt.data.engine.impl.ComputedColumnHelper;
import org.eclipse.birt.data.engine.impl.FilterByRow;
import org.eclipse.birt.data.engine.odi.IAggrInfo;

/**
 * The class used to populate DataSet data.
 *
 */
class DataSetProcessUtil extends RowProcessUtil {
	/**
	 *
	 * @param populator
	 * @param iccState
	 * @param computedColumnHelper
	 * @param filterByRow
	 * @param psController
	 */
	private DataSetProcessUtil(ResultSetPopulator populator, ComputedColumnsState iccState,
			ComputedColumnHelper computedColumnHelper, FilterByRow filterByRow, PassStatusController psController) {
		super(populator, iccState, computedColumnHelper, filterByRow, psController);
	}

	/**
	 * Populate the data set data of an IResultIterator instance.
	 *
	 * @param populator
	 * @param iccState
	 * @param computedColumnHelper
	 * @param filterByRow
	 * @param psController
	 * @param stopSign
	 * @throws DataException
	 */
	public static void doPopulate(ResultSetPopulator populator, ComputedColumnsState iccState,
			ComputedColumnHelper computedColumnHelper, FilterByRow filterByRow, PassStatusController psController)
			throws DataException {
		DataSetProcessUtil instance = new DataSetProcessUtil(populator, iccState, computedColumnHelper, filterByRow,
				psController);
		instance.populateDataSet();
	}

	/**
	 *
	 * @param stopSign
	 * @throws DataException
	 */
	private void populateDataSet() throws DataException {
		int originalMaxRows = this.populator.getQuery().getMaxRows();

		boolean changeMaxRows = filterByRow == null ? false
				: filterByRow.getFilterList(FilterByRow.QUERY_FILTER).size()
						+ filterByRow.getFilterList(FilterByRow.GROUP_FILTER).size() > 0;
		if (changeMaxRows) {
			this.populator.getQuery().setMaxRows(0);
		}

		if (this.computedColumnHelper != null) {
			this.computedColumnHelper.setModel(TransformationConstants.NONE_MODEL);
		}
		doDataSetFilter(changeMaxRows);

		List aggCCList = prepareComputedColumns(TransformationConstants.DATA_SET_MODEL);

		// All the calculated computed columns (none aggregation ones) need not been
		// re-calculated
		// during the calculation of aggregation computed columns.
		if (this.computedColumnHelper != null) {
			computedColumnHelper.setModel(TransformationConstants.NONE_MODEL);
		}
		populateAggrCCs(this.getAggrComputedColumns(aggCCList, true));
		setStateForAggregationComputedColumns();
		removeAvailableComputedColumns();

		// Begin populate computed columns with aggregations.
		// TODO:remove me
		populateComputedColumns(this.getAggrComputedColumns(aggCCList, false));
		if (filterByRow != null && filterByRow.isFilterSetExist(FilterByRow.DATASET_AGGR_FILTER)) {
			doDataSetAggrFilter(changeMaxRows);
			populateAggrCCs(this.getAggrComputedColumns(aggCCList, true));
		}

		this.populator.getQuery().setMaxRows(originalMaxRows);
	}

	private void setStateForAggregationComputedColumns() throws DataException {
		if (iccState != null) {
			for (int i = 0; i < iccState.getCount(); i++) {
				if (iccState.getComputedColumn(i).getAggregateFunction() != null) {
					iccState.setValueAvailable(i);
				}
			}
		}
	}

	/**
	 *
	 * @param aggrComputedColumns
	 * @param stopSign
	 * @throws DataException
	 */
	private void populateAggrCCs(List aggrComputedColumns) throws DataException {
		if (aggrComputedColumns.size() == 0) {
			return;
		}
		ExpressionCompiler compiler = new ExpressionCompiler();
		compiler.setDataSetMode(true);

		List aggrInfos = new ArrayList();
		List aggrNames = new ArrayList();
		for (int i = 0; i < aggrComputedColumns.size(); i++) {
			ComputedColumn cc = (ComputedColumn) aggrComputedColumns.get(i);
			List args = cc.getAggregateArgument();

			IBaseExpression[] exprs = null;
			int offset = 0;
			if (cc.getExpression() != null) {
				exprs = new IBaseExpression[args.size() + 1];
				offset = 1;
				exprs[0] = cc.getExpression();
			} else {
				exprs = new IBaseExpression[args.size()];
			}

			for (int j = offset; j < args.size() + offset; j++) {
				exprs[j] = (IBaseExpression) args.get(j - offset);
			}

			for (int j = 0; j < exprs.length; j++) {
				if (exprs[j] instanceof IScriptExpression) {
					IScriptExpression scriptExpr = (IScriptExpression) exprs[j];
					if (scriptExpr.getText() == null) {
						continue;
					}
				}
				compiler.compile(exprs[j], this.populator.getSession().getEngineContext().getScriptContext());
			}

			if (cc.getAggregateFilter() != null) {
				compiler.compile(cc.getAggregateFilter(),
						this.populator.getSession().getEngineContext().getScriptContext());
			}
			IAggrFunction aggrFunction = AggregationManager.getInstance().getAggregation(cc.getAggregateFunction());
			IAggrInfo aggrInfo = new AggrInfo(cc.getName(), 0, aggrFunction, exprs, cc.getAggregateFilter());
			aggrInfos.add(aggrInfo);
			aggrNames.add(cc.getName());
		}

		// All the computed column aggregations should only have one round.

		if (!psController.needDoOperation(PassStatusController.DATA_SET_FILTERING)) {
			PassUtil.pass(populator, new OdiResultSetWrapper(populator.getResultIterator()), false);
		}

		AggregationHelper helper = new AggregationHelper(new AggrDefnManager(aggrInfos), this.populator);

		AggrComputedColumnHelper ccHelper = new AggrComputedColumnHelper(helper, aggrNames);
		this.populator.getQuery().getFetchEvents().add(0, ccHelper);

		PassUtil.pass(populator, new OdiResultSetWrapper(populator.getResultIterator()), false);

		this.populator.getQuery().getFetchEvents().remove(0);

	}

	/**
	 *
	 * @param changeMaxRows
	 * @param stopSign
	 * @throws DataException
	 */
	private void doDataSetFilter(boolean changeMaxRows) throws DataException {
		if (!psController.needDoOperation(PassStatusController.DATA_SET_FILTERING)) {
			return;
		}

		applyFilters(FilterByRow.DATASET_FILTER, changeMaxRows);
	}

	/**
	 *
	 * @param changeMaxRows
	 * @param stopSign
	 * @throws DataException
	 */
	private void doDataSetAggrFilter(boolean changeMaxRows) throws DataException {
		if (!psController.needDoOperation(PassStatusController.DATASET_AGGR_ROW_FILTERING)) {
			return;
		}

		applyFilters(FilterByRow.DATASET_AGGR_FILTER, changeMaxRows);
	}

	/**
	 *
	 * @param aggCCList
	 * @param stopSign
	 * @throws DataException
	 */
	private void populateComputedColumns(List aggCCList) throws DataException {
		if (!psController.needDoOperation(PassStatusController.DATA_SET_COMPUTED_COLUMN_POPULATING)) {
			return;
		}
		// if no group pass has been made, made one.
		if (!psController.needDoOperation(PassStatusController.DATA_SET_FILTERING)) {
			PassUtil.pass(this.populator, new OdiResultSetWrapper(populator.getResultIterator()), false);
		}
		computedColumnHelper.getComputedColumnList().clear();
		computedColumnHelper.getComputedColumnList().addAll(aggCCList);
		computedColumnHelper.setModel(TransformationConstants.DATA_SET_MODEL);
		iccState.setModel(TransformationConstants.DATA_SET_MODEL);
		// If there are computed columns cached in iccState, then begin
		// multipass.
		if (iccState.getCount() > 0) {
			ComputedColumnCalculator.populateComputedColumns(this.populator,
					new OdiResultSetWrapper(this.populator.getResultIterator()), iccState, computedColumnHelper);
		}
		computedColumnHelper.setModel(TransformationConstants.NONE_MODEL);
	}

	/**
	 * Remove all available computed column if it has been pre-calculated by
	 * computerColumnHelper.
	 */
	private void removeAvailableComputedColumns() {
		if (iccState != null) {
			for (int i = 0; i < iccState.getCount(); i++) {
				if (iccState.isValueAvailable(i)) {
					for (int k = 0; k < this.populator.getQuery().getFetchEvents().size(); k++) {
						if (this.populator.getQuery().getFetchEvents().get(k) instanceof ComputedColumnHelper) {
							ComputedColumnHelper helper = (ComputedColumnHelper) this.populator.getQuery()
									.getFetchEvents().get(k);
							helper.removeAvailableComputedColumn(iccState.getComputedColumn(i));
							break;
						}
					}
				}
			}
		}
	}
}
