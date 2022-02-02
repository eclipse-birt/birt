/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;

/**
 * The implementation of <code>IGroupedDataResultSet</code> for chart.
 * 
 * @since BIRT 2.3
 */
public class BIRTGroupedQueryResultSetEvaluator extends BaseGroupedQueryResultSetEvaluator {

	private IQueryResultSet fQueryResultSet;

	/**
	 * Constructor.
	 * 
	 * @param resultSet
	 * @param hasAggregation
	 * @param cm
	 * @param handle
	 * @throws ChartException
	 */
	public BIRTGroupedQueryResultSetEvaluator(IQueryResultSet resultSet, boolean hasAggregation, Chart cm,
			ExtendedItemHandle handle) throws ChartException {
		super(resultSet.getResultIterator(), hasAggregation, cm, handle);

		fQueryResultSet = resultSet;
	}

	/**
	 * Constructor.
	 * 
	 * @param resultSet
	 * @param hasAggregation
	 * @param isSubQuery
	 * @param cm
	 * @throws ChartException
	 * @since 2.3
	 */
	public BIRTGroupedQueryResultSetEvaluator(IQueryResultSet resultSet, boolean hasAggregation, boolean isSubQuery,
			Chart cm, ExtendedItemHandle handle) throws ChartException {
		super(resultSet.getResultIterator(), hasAggregation, isSubQuery, cm, handle);
		fQueryResultSet = resultSet;
	}

	public BIRTGroupedQueryResultSetEvaluator(IResultIterator resultIterator, boolean hasAggregation,
			boolean isSubQuery, Chart cm, ExtendedItemHandle handle) throws ChartException {
		super(resultIterator, hasAggregation, isSubQuery, cm, handle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#close()
	 */
	public void close() {
		if (fQueryResultSet != null) {
			fQueryResultSet.close();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#evaluate(java.lang
	 * .String)
	 */
	public Object evaluate(String expression) {
		if (fQueryResultSet == null) {
			return super.evaluate(expression);
		}
		try {
			exprCodec.decode(expression);
			if (exprCodec.isConstant()) {
				return exprCodec.getExpression();
			}
			return fQueryResultSet.evaluate(exprCodec.getType(), exprCodec.getExpression());
		} catch (BirtException e) {
			sLogger.log(e);
		}
		return null;
	}
}
