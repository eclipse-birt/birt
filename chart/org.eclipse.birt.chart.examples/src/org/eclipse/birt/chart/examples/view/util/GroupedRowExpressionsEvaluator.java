/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.examples.view.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.AbstractGroupedDataRowExpressionEvaluator;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IResultIterator;

/**
 * The class implements an evaluator to retrieve grouped row data.
 * 
 * @since 2.3
 */
public class GroupedRowExpressionsEvaluator extends AbstractGroupedDataRowExpressionEvaluator {

	private static ILogger sLogger = Logger.getLogger("org.eclipse.birt.chart.examples/trace"); //$NON-NLS-1$

	private IResultIterator fResultIterator;

	private boolean fIsGrouped = false;

	private int fGroupCount;

	private List[] faGroupBreaks;

	private int fCountOfAvaiableRows = 0;

	private boolean fHasAggregation = false;

	/**
	 * Constructor.
	 * 
	 * @param resultSet
	 * @param hasAggregation
	 * @param cm
	 * @throws ChartException
	 */
	public GroupedRowExpressionsEvaluator(IResultIterator resultIterator, boolean hasAggregation)
			throws ChartException {
		fHasAggregation = hasAggregation;

		fResultIterator = resultIterator;
		List<IGroupDefinition> groupDefinitions = fResultIterator.getQueryResults().getPreparedQuery()
				.getReportQueryDefn().getGroups();
		if (groupDefinitions != null && groupDefinitions.size() > 0) {
			fIsGrouped = true;
			fGroupCount = groupDefinitions.size();

			faGroupBreaks = new List[groupDefinitions.size()];
			for (int i = 0; i < faGroupBreaks.length; i++) {
				faGroupBreaks[i] = new ArrayList();
			}
		}
	}

	/**
	 * Get list of group breaks, the group level is base on 0th index, 0 index means
	 * outermost group.
	 * 
	 * @param groupLevel
	 * @return
	 */
	private List getGroupBreaksList(int groupLevel) {
		if (faGroupBreaks == null || groupLevel < 0 || groupLevel > (faGroupBreaks.length - 1)) {
			return new ArrayList();
		}

		return faGroupBreaks[groupLevel];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IGroupedDataResultSet#getGroupBreaks(int)
	 */
	public int[] getGroupBreaks(int groupLevel) {
		Object[] breaksArray = getGroupBreaksList(groupLevel).toArray();
		int[] breaks = new int[breaksArray.length];
		for (int i = 0; i < breaksArray.length; i++) {
			breaks[i] = ((Integer) breaksArray[i]).intValue();
		}
		return breaks;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#close()
	 */
	public void close() {
		try {
			fResultIterator.close();
		} catch (BirtException e) {
			sLogger.log(e);
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
		try {
			// Here, the expression should be binding name.
			return fResultIterator.getValue(expression);
		} catch (BirtException e) {
			sLogger.log(e);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#evaluateGlobal(
	 * java.lang.String)
	 */
	public Object evaluateGlobal(String expression) {
		return evaluate(expression);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#first()
	 */
	public boolean first() {
		try {
			fCountOfAvaiableRows = 0;

			if (!fIsGrouped) {
				if (fResultIterator.next()) {
					return true;
				}
			} else {
				if (findFirst()) {
					return true;
				}
			}
		} catch (BirtException e) {
			sLogger.log(e);
		}
		return false;
	}

	/**
	 * Find the first row position.
	 * 
	 * @return
	 * @throws BirtException
	 */
	private boolean findFirst() throws BirtException {
		if (!fResultIterator.next()) {
			return false;
		}

		int groupLevel = fResultIterator.getStartingGroupLevel();
		if (groupLevel == 0) // It means the start of current row data.
		{
			return true;
		} else {
			return findFirst();
		}
	}

	/**
	 * Find next available row position. If it has grouped-enabled, should ignore
	 * non-grouped/non-aggregation row.
	 * 
	 * @return
	 * @throws BirtException
	 */
	private boolean findNext() throws BirtException {
		while (fResultIterator.next()) {
			int startIndex = fResultIterator.getStartingGroupLevel();
			if (startIndex > 0 && startIndex <= fGroupCount) {
				fCountOfAvaiableRows++;
				// Add break point to current grouping.
				getGroupBreaksList(startIndex - 1).add(Integer.valueOf(fCountOfAvaiableRows));
				// Also the sub-groupings of current grouping should be
				// added the break point.
				for (int i = startIndex; i < fGroupCount; i++) {
					getGroupBreaksList(i).add(Integer.valueOf(fCountOfAvaiableRows));
				}

				return true;
			}

			if (!fHasAggregation) {
				fCountOfAvaiableRows++;
				return true;
			}
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#next()
	 */
	public boolean next() {
		try {
			if (!fIsGrouped) {
				if (fResultIterator.next()) {
					fCountOfAvaiableRows++;
					return true;
				}
			} else {
				return findNext();
			}
		} catch (BirtException e) {
			sLogger.log(e);
		}
		return false;
	}
}
