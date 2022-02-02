/*******************************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.AbstractGroupedDataRowExpressionEvaluator;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.emf.common.util.EList;

/**
 * The class defines basic functions and sub-class must override evaluate
 * method.
 * 
 * @since BIRT 2.3
 */
public class BaseGroupedQueryResultSetEvaluator extends AbstractGroupedDataRowExpressionEvaluator {
	protected static final ILogger sLogger = Logger.getLogger("org.eclipse.birt.chart.reportitem/trace"); //$NON-NLS-1$

	protected IResultIterator fResultIterator;

	protected List<IGroupDefinition> fGroupDefinitions;

	protected boolean fIsGrouped = false;

	protected int fGroupCount;

	/** The boolean array indicates if related group of index is used by chart. */
	protected boolean[] faEnabledGroups;

	protected List<Integer>[] faGroupBreaks;

	protected int fCountOfAvaiableRows = 0;

	/**
	 * The field indicates if there is summary aggregate is set on chart, if no
	 * summary aggregate is set, the count of available row should be one by one
	 * row, it will not ignore detail rows.
	 */
	protected boolean fHasSummaryAggregation = false;

	protected final ExpressionCodec exprCodec = ChartModelHelper.instance().createExpressionCodec();

	protected ExtendedItemHandle itemHandle = null;

	/**
	 * Constructor.
	 * 
	 * @param resultIterator
	 * @param hasSummaryAggregation
	 * @param cm
	 * @param handle
	 * @throws ChartException
	 */
	@SuppressWarnings("unchecked")
	public BaseGroupedQueryResultSetEvaluator(IResultIterator resultIterator, boolean hasSummaryAggregation, Chart cm,
			ExtendedItemHandle handle) throws ChartException {
		fHasSummaryAggregation = hasSummaryAggregation;
		itemHandle = handle;
		fResultIterator = resultIterator;
		fGroupDefinitions = fResultIterator.getQueryResults().getPreparedQuery().getReportQueryDefn().getGroups();
		if (fGroupDefinitions != null && fGroupDefinitions.size() > 0) {
			fIsGrouped = true;
			fGroupCount = fGroupDefinitions.size();

			faGroupBreaks = new List[fGroupDefinitions.size()];
			for (int i = 0; i < faGroupBreaks.length; i++) {
				faGroupBreaks[i] = new ArrayList<Integer>();
			}

			updateEnabledGroupIndexes(cm, fGroupDefinitions);
		}
	}

	/**
	 * Constructor.
	 * 
	 * @param resultIterator
	 * @param hasAggregation
	 * @param isSubQuery
	 * @param cm
	 * @throws ChartException
	 * @since 2.3
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BaseGroupedQueryResultSetEvaluator(IResultIterator resultIterator, boolean hasAggregation,
			boolean isSubQuery, Chart cm, ExtendedItemHandle handle) throws ChartException {
		fHasSummaryAggregation = hasAggregation;
		itemHandle = handle;
		fResultIterator = resultIterator;
		if (isSubQuery) {
			List subQuerys = new ArrayList();
			// Get all sub queries defined on detail item.
			Collection c = fResultIterator.getQueryResults().getPreparedQuery().getReportQueryDefn().getSubqueries();
			if (c != null && !c.isEmpty()) {
				subQuerys.addAll(c);
			}

			// Get all sub queries defined on group item.
			List groups = fResultIterator.getQueryResults().getPreparedQuery().getReportQueryDefn().getGroups();
			if (groups != null) {
				for (Iterator iter = groups.iterator(); iter.hasNext();) {
					c = ((IGroupDefinition) iter.next()).getSubqueries();
					if (c != null && !c.isEmpty()) {
						subQuerys.addAll(c);
					}
				}
			}

			// Iterator all sub queries and find chart sub query to get group definitions.
			for (int i = 0; i < subQuerys.size(); i++) {
				if ((ChartReportItemConstants.NAME_SUBQUERY + handle.getElement().getID())
						.equals(((ISubqueryDefinition) subQuerys.get(i)).getName())) {
					fGroupDefinitions = ((ISubqueryDefinition) subQuerys.get(i)).getGroups();
					break;
				}
			}

			if (fGroupDefinitions == null) {
				fGroupDefinitions = fResultIterator.getQueryResults().getPreparedQuery().getReportQueryDefn()
						.getGroups();
			}
		} else {
			fGroupDefinitions = fResultIterator.getQueryResults().getPreparedQuery().getReportQueryDefn().getGroups();
		}

		if (fGroupDefinitions != null && fGroupDefinitions.size() > 0) {
			fIsGrouped = true;
			fGroupCount = fGroupDefinitions.size();
			faGroupBreaks = new List[fGroupDefinitions.size()];
			for (int i = 0; i < faGroupBreaks.length; i++) {
				faGroupBreaks[i] = new ArrayList<Integer>();
			}

			updateEnabledGroupIndexes(cm, fGroupDefinitions);
		}
	}

	/**
	 * Get list of group breaks, the group level is base on 0th index, 0 index means
	 * outermost group.
	 * 
	 * @param groupLevel
	 * @return
	 */
	protected List<Integer> getGroupBreaksList(int groupLevel) {
		if (faGroupBreaks == null || groupLevel < 0 || groupLevel > (faGroupBreaks.length - 1)) {
			return new ArrayList<Integer>();
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
			exprCodec.decode(expression);
			if (exprCodec.isConstant()) {
				return exprCodec.getExpression();
			}
			return fResultIterator.getValue(exprCodec.getExpression());
		} catch (BirtException e) {
			sLogger.log(e);
			return e;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#evaluateGlobal(
	 * java.lang.String)
	 */
	@SuppressWarnings("deprecation")
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
	protected boolean findFirst() throws BirtException {
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
	 * Check if the group between start index and end index is used by chart.
	 * 
	 * @param startIndex
	 * @param endIndex
	 * @return
	 */
	private boolean usedByChart(int startIndex, int endIndex) {
		for (int i = startIndex; i <= endIndex; i++) {
			if (faEnabledGroups[i])
				return true;
		}
		return false;
	}

	/**
	 * Find next available row position. If it has grouped-enabled, should ignore
	 * non-grouped/non-aggregation row.
	 * 
	 * @return
	 * @throws BirtException
	 */
	protected boolean findNext() throws BirtException {
		while (fResultIterator.next()) {
			int startIndex = fResultIterator.getStartingGroupLevel();
			if (startIndex > 0 && startIndex <= fGroupCount) {
				if (usedByChart(startIndex - 1, fGroupCount - 1)) // it means the group is
				// used by chart.
				{
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
			}

			// If it has no summary aggregate, we should not ignore detail rows,
			// it still increase count one by one row.
			if (!fHasSummaryAggregation) {
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

	/**
	 * Updates using state of groups, if category expression and Y optional
	 * expression have related group on specified GroupDefinition, set
	 * <code>true</code> value to that item of group indexes array.
	 * 
	 * @param cm               current chart model.
	 * @param groupDefinitions grouping definition.
	 * @throws ChartException
	 */
	protected void updateEnabledGroupIndexes(Chart cm, List<IGroupDefinition> groupDefinitions) throws ChartException {
		faEnabledGroups = new boolean[fGroupCount];
		boolean isSharing = isSharing();
		// Check the category expression.
		EList<SeriesDefinition> baseSDs = ChartUtil.getBaseSeriesDefinitions(cm);
		for (SeriesDefinition sd : baseSDs) {
			if (sd.getDesignTimeSeries().getDataDefinition().isEmpty() || sd.getGrouping() == null
					|| !sd.getGrouping().isEnabled()) {
				continue;
			}

			Query q = sd.getDesignTimeSeries().getDataDefinition().get(0);
			String expr = q.getDefinition();
			int index = getGroupIndex(expr, groupDefinitions, true);

			if (index >= 0) {
				faEnabledGroups[index] = true;

				if (isSharing && sd.getGrouping() != null) {
					IGroupDefinition gd = groupDefinitions.get(index);
					setGroupingInterval(sd.getGrouping(), gd);
				}
			}
		}

		// Check the Y optional expression.
		List<SeriesDefinition> orthoSDs = ChartUtil.getAllOrthogonalSeriesDefinitions(cm);
		for (SeriesDefinition sd : orthoSDs) {
			Query q = sd.getQuery();
			if (q == null || q.getDefinition() == null || "".equals(q.getDefinition().trim())) //$NON-NLS-1$
			{
				continue;
			}

			String expr = q.getDefinition();
			int index = getGroupIndex(expr, groupDefinitions, false);
			if (index >= 0) {
				faEnabledGroups[index] = true;
				if (isSharing && sd.getQuery() != null) {
					IGroupDefinition gd = groupDefinitions.get(index);
					setGroupingInterval(sd.getQuery().getGrouping(), gd);
				}
			}
		}
	}

	/**
	 * @param sd
	 * @param gd
	 */
	private void setGroupingInterval(SeriesGrouping sg, IGroupDefinition gd) {
		GroupingUnitType gut = GroupingUnitType.get(ChartItemUtil.convertToChartGroupUnit(gd.getInterval()));
		if (gut != null && sg != null) {
			sg.setGroupingUnit(gut);
			sg.setGroupingInterval(gd.getIntervalRange());
		}
	}

	/**
	 * Returns the index of specified expression on GroupDefinition.
	 * 
	 * @param expr             specified expression.
	 * @param groupDefinitions list of <code>GroupDefinition</code>
	 * @param isCategory
	 * @return
	 * @throws ChartException
	 */
	private int getGroupIndex(String expr, List<IGroupDefinition> groupDefinitions, boolean isCategory)
			throws ChartException {
		if (expr == null) {
			return -1;
		}

		exprCodec.decode(expr);
		Set<String> bindingNameSet = exprCodec.getRowBindingNameSet();

		boolean isJavaScript = ExpressionCodec.JAVASCRIPT.equals(exprCodec.getType());
		// Check if the expression is group expression.
		if (isCategory) {
			// Category uses revised order to seek group index to ensure getting correct
			// index when
			// category and optional Y have same expressions to group.
			for (int i = groupDefinitions.size() - 1; i >= 0; i--)

			{
				IGroupDefinition gd = groupDefinitions.get(i);
				String exprGroupKey = gd.getKeyExpression();
				if (isJavaScript) {
					// If specified expr contains group expression, the expr is
					// used as group key.
					if (expr.indexOf(exprGroupKey) >= 0) {
						return i;
					}
				}

				if (!bindingNameSet.isEmpty()) {
					Set<String> grpBindings = exprCodec.getRowBindingNameSet(exprGroupKey);

					for (String grpBinding : grpBindings) {
						if (bindingNameSet.contains(grpBinding)) {
							return i;
						}
					}
				}
			}
		} else {
			for (int i = 0; i < groupDefinitions.size(); i++)

			{
				IGroupDefinition gd = groupDefinitions.get(i);
				String exprGroupKey = gd.getKeyExpression();
				if (isJavaScript) {
					// If specified expr contains group expression, the expr is
					// used as group key.
					if (expr.indexOf(exprGroupKey) >= 0) {
						return i;
					}
				}

				if (!bindingNameSet.isEmpty()) {
					Set<String> grpBindings = exprCodec.getRowBindingNameSet(exprGroupKey);

					for (String grpBinding : grpBindings) {
						if (bindingNameSet.contains(grpBinding)) {
							return i;
						}
					}
				}
			}
		}
		return -1;
	}

	private boolean isSharing() {
		if (itemHandle == null) {
			return false;
		}
		return ChartItemUtil.isChartInheritGroups(itemHandle)
				|| (ChartItemUtil.getReportItemReference(itemHandle) != null);
	}

	/*
	 * Returns if group is enabled in each group-level.
	 */
	public boolean[] getGroupStatus() {
		return faEnabledGroups;
	}
}
