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

import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.impl.ComputedColumnHelper;
import org.eclipse.birt.data.engine.impl.DataEngineSession;
import org.eclipse.birt.data.engine.impl.ExprManagerUtil;
import org.eclipse.birt.data.engine.impl.FilterByRow;

/**
 * The abstract class defines the common behavior of DataSetProcessUtil and
 * ResultSetProcessUtil.
 *
 */
abstract class RowProcessUtil {
	/**
	 * 
	 */
	protected ComputedColumnsState iccState;
	protected ComputedColumnHelper computedColumnHelper;
	protected FilterByRow filterByRow;
	protected PassStatusController psController;
	protected ResultSetPopulator populator;
	protected DataEngineSession session;

	/**
	 * 
	 * @param populator
	 * @param iccState
	 * @param computedColumnHelper
	 * @param filterByRow
	 * @param psController
	 */
	protected RowProcessUtil(ResultSetPopulator populator, ComputedColumnsState iccState,
			ComputedColumnHelper computedColumnHelper, FilterByRow filterByRow, PassStatusController psController) {
		this.iccState = iccState;
		this.computedColumnHelper = computedColumnHelper;
		this.filterByRow = filterByRow;
		this.psController = psController;
		this.populator = populator;
		this.session = populator.getSession();
	}

	/**
	 * 
	 * @param model
	 * @return
	 * @throws DataException
	 */
	protected List prepareComputedColumns(int model) throws DataException {
		initializeICCState(model);

		List aggCCList = new ArrayList();
		List simpleCCList = new ArrayList();
		if (computedColumnHelper != null) {
			computedColumnHelper.setModel(model);
			List l = computedColumnHelper.getComputedColumnList();
			for (int i = 0; i < l.size(); i++) {
				if (this.populator.getExpressionProcessor().hasAggregation(((ComputedColumn) l.get(i)).getExpression())
						|| ((ComputedColumn) l.get(i)).getAggregateFunction() != null) {
					aggCCList.add(l.get(i));
				} else {
					simpleCCList.add(l.get(i));
				}
			}
			computedColumnHelper.getComputedColumnList().clear();
			computedColumnHelper.getComputedColumnList().addAll(simpleCCList);
			computedColumnHelper.setRePrepare(true);
		}

		return aggCCList;
	}

	/**
	 * 
	 * @param computedColumns
	 * @param isNew
	 * @return
	 */
	protected List getAggrComputedColumns(List computedColumns, boolean isNew) {
		List result = new ArrayList();
		for (int i = 0; i < computedColumns.size(); i++) {
			if (isNew) {
				if (((ComputedColumn) computedColumns.get(i)).getAggregateFunction() != null) {
					result.add(computedColumns.get(i));
				}
			} else {
				if (((ComputedColumn) computedColumns.get(i)).getAggregateFunction() == null) {
					result.add(computedColumns.get(i));
				}
			}
		}
		return result;
	}

	/**
	 * 
	 * @param model
	 * @throws DataException
	 */
	private void initializeICCState(int model) throws DataException {
		if (iccState != null) {
			iccState.setModel(model);

			for (int i = 0; i < iccState.getCount(); i++) {
				if (!this.populator.getExpressionProcessor()
						.hasAggregation(iccState.getComputedColumn(i).getExpression())
						&& !ExprManagerUtil.parseAggregation(iccState.getComputedColumn(i),
								computedColumnHelper.getComputedColumnList())) {
					iccState.setValueAvailable(i);
				}

			}
		}
	}

	/**
	 * 
	 * @param filterType
	 * @param changeMaxRows
	 * @param stopSign
	 * @throws DataException
	 */
	protected void applyFilters(int filterType, boolean changeMaxRows) throws DataException {
		if (filterByRow != null && filterByRow.isFilterSetExist(filterType)) {
			int max = populator.getQuery().getMaxRows();

			if (changeMaxRows) {
				populator.getQuery().setMaxRows(0);
			}
			filterByRow.setWorkingFilterSet(filterType);
			FilterCalculator.applyFilters(this.populator, this.filterByRow);
			populator.getQuery().setMaxRows(max);
		}

		if (filterByRow != null) {
			filterByRow.setWorkingFilterSet(FilterByRow.NO_FILTER);
		}
	}
}
