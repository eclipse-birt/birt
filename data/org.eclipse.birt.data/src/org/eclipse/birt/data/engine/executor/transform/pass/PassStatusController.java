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

package org.eclipse.birt.data.engine.executor.transform.pass;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.transform.FilterUtil;
import org.eclipse.birt.data.engine.executor.transform.ResultSetPopulator;
import org.eclipse.birt.data.engine.executor.transform.TransformationConstants;
import org.eclipse.birt.data.engine.impl.ComputedColumnHelper;
import org.eclipse.birt.data.engine.impl.FilterByRow;

/**
 * A class which is used to control pass process.
 */
class PassStatusController {
	/**
	 * 
	 */
	public static final int DATA_SET_FILTERING = 1;
	public static final int DATA_SET_COMPUTED_COLUMN_POPULATING = 2;
	public static final int RESULT_SET_FILTERING = 3;
	public static final int RESULT_SET_TEMP_COMPUTED_COLUMN_POPULATING = 4;
	public static final int GROUP_ROW_FILTERING = 5;
	public static final int AGGR_ROW_FILTERING = 6;
	public static final int DATASET_AGGR_ROW_FILTERING = 7;
	public static final int NOUPDATE_ROW_FILTERING = 8;

	private boolean hasDataSetFilters;
	private boolean hasDataSetCC;
	private boolean hasResultSetFilters;
	private boolean hasResultSetTempCC;
	private boolean hasGroupRowFilters;
	private boolean hasAggrRowFilters;
	private boolean hasDataSetAggrFilter;
	private boolean needMultipassProcessing;
	private boolean hasNoUpdateRowFilters;

	// private boolean hasAggregationInResultSetCC;
	private boolean hasAggregationInDataSetCC;

	/**
	 * 
	 * @param populator
	 * @param filterByRow
	 * @param computedColumnHelper
	 * @throws DataException
	 */
	PassStatusController(ResultSetPopulator populator, FilterByRow filterByRow,
			ComputedColumnHelper computedColumnHelper) throws DataException {
		this.hasDataSetFilters = filterByRow == null ? false : filterByRow.isFilterSetExist(FilterByRow.DATASET_FILTER);
		this.hasDataSetAggrFilter = filterByRow == null ? false
				: filterByRow.isFilterSetExist(FilterByRow.DATASET_AGGR_FILTER);
		this.hasDataSetCC = computedColumnHelper == null ? false
				: computedColumnHelper.isComputedColumnExist(TransformationConstants.DATA_SET_MODEL);
		this.hasResultSetFilters = filterByRow == null ? false : filterByRow.isFilterSetExist(FilterByRow.QUERY_FILTER);
		this.hasAggrRowFilters = filterByRow == null ? false : filterByRow.isFilterSetExist(FilterByRow.AGGR_FILTER);
		this.hasResultSetTempCC = computedColumnHelper == null ? false
				: computedColumnHelper.isComputedColumnExist(TransformationConstants.RESULT_SET_MODEL);
		this.hasGroupRowFilters = filterByRow == null ? false : filterByRow.isFilterSetExist(FilterByRow.GROUP_FILTER);
		this.hasNoUpdateRowFilters = filterByRow == null ? false
				: filterByRow.isFilterSetExist(FilterByRow.NOUPDATE_ROW_FILTER);

		// If there are aggregations in Computed Columns, then the group
		// filtering should not
		// be supported for that the aggregation result would be affected by
		// group filtering.
		if (computedColumnHelper != null) {
			computedColumnHelper.setModel(TransformationConstants.DATA_SET_MODEL);
			hasAggregationInDataSetCC = hasAggregationsInComputedColumns(computedColumnHelper, populator);

			/*
			 * computedColumnHelper.setModel( TransformationConstants.RESULT_SET_MODEL);
			 * hasAggregationInResultSetCC = ResultSetPopulatorUtil
			 * .hasAggregationsInComputedColumns(computedColumnHelper, populator);
			 */
			computedColumnHelper.setModel(TransformationConstants.NONE_MODEL);
		}

		// If there are some aggregations in computed columns, or there are
		// some
		// multipass filters
		// then dealing with those aggregations/multipass filters. Else
		// start
		// population directly

		needMultipassProcessing = hasAggregationInDataSetCC || FilterUtil.hasMultiPassFilters(filterByRow)
				|| (populator.getQuery().getGrouping() != null && populator.getQuery().getGrouping().length > 0)
				|| (populator.getQuery().getOrdering() != null && populator.getQuery().getOrdering().length > 0)
				|| this.hasAggrRowFilters || this.hasNoUpdateRowFilters;
	}

	/**
	 * 
	 * @param operType
	 * @return
	 */
	boolean needDoOperation(int operType) {
		switch (operType) {
		case DATA_SET_FILTERING:
			return this.hasDataSetFilters;
		case DATA_SET_COMPUTED_COLUMN_POPULATING:
			return this.hasDataSetCC && (this.hasAggregationInDataSetCC || (!this.hasDataSetFilters));
		case RESULT_SET_FILTERING:
			return this.hasResultSetFilters;
		case RESULT_SET_TEMP_COMPUTED_COLUMN_POPULATING:
			return this.hasResultSetTempCC;
		case GROUP_ROW_FILTERING:
			return this.hasGroupRowFilters;
		case AGGR_ROW_FILTERING:
			return this.hasAggrRowFilters;
		case DATASET_AGGR_ROW_FILTERING:
			return this.hasDataSetAggrFilter;
		case NOUPDATE_ROW_FILTERING:
			return this.hasNoUpdateRowFilters;
		default:
			return false;
		}
	}

	/**
	 * 
	 * @return
	 */
	boolean needMultipassProcessing() {
		return this.needMultipassProcessing;
	}

	/**
	 * Return whether there are aggregations in the query.
	 * 
	 * @throws DataException
	 */
	static boolean hasAggregationsInComputedColumns(ComputedColumnHelper ccHelper, ResultSetPopulator rsp)
			throws DataException {
		if (ccHelper == null)
			return false;

		List expressionList = new ArrayList();

		List list = ccHelper.getComputedColumnList();

		for (int j = 0; j < list.size(); j++) {
			expressionList.add(((IComputedColumn) list.get(j)).getExpression());
			if (((IComputedColumn) list.get(j)).getAggregateFunction() != null)
				return true;
		}
		return rsp.getExpressionProcessor().hasAggregateExpr(expressionList);
	}
}