/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.internal.datafeed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.aggregate.IAggregateFunction;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.emf.common.util.EList;

import com.ibm.icu.text.Collator;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * Wraps an implementation specific host resultset into a chart specific
 * resultset that may be subsequently bound to individual series associated with
 * a chart instance.
 */
public final class ResultSetWrapper {

	/**
	 * An internally maintained list containing all rows of raw resultset data
	 */
	final List rawResultSet;

	/**
	 * An internally maintained list containing all working resultset data
	 */
	final List workingResultSet;

	/**
	 * The column expressions associated with the resultset
	 */
	final String[] saExpressionKeys;

	/**
	 * The data types associated with each column in the resultset
	 */
	final int[] iaDataTypes;

	/**
	 * A reusable instance that indicates no group breaks
	 */
	private static final int[] NO_GROUP_BREAKS = new int[0];

	/**
	 * The group breaks associated with all rows of data
	 */
	private int[] iaGroupBreaks = NO_GROUP_BREAKS;

	/**
	 * A lookup table internally used to locate a numeric column index using the
	 * associated expression
	 */
	private final GroupingLookupHelper htLookup;

	private final GroupKey[] oaGroupKeys;

	/**
	 * The start interval value is used as an benchmark to compute numeric interval
	 * on specified interval range.
	 * 
	 * @since BIRT 2.3
	 */
	private static final double BASE_START_INTERVAL_VALUE = 0;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine/datafeed"); //$NON-NLS-1$

	/**
	 * The default constructor that allows creation of a resultset wrapper
	 * 
	 * @param hmLookup    The map of expressions associated with each column in the
	 *                    resultset
	 * @param liResultSet A list of rows that represent the actual resultset data
	 *                    content. Each row contains an Object[]
	 * @param groupKeys   An array of orthogonal grouping keys, if it's null or
	 *                    zero-length, no orthogonal grouping used.
	 */
	public ResultSetWrapper(GroupingLookupHelper hmLookup, List liResultSet, GroupKey[] groupKeys) {
		this(hmLookup, liResultSet, groupKeys, NO_GROUP_BREAKS);
	}

	/**
	 * @param hmLookup
	 * @param liResultSet
	 * @param groupKeys
	 * @param groupBreaks
	 */
	public ResultSetWrapper(GroupingLookupHelper hmLookup, List liResultSet, GroupKey[] groupKeys, int[] groupBreaks) {
		htLookup = hmLookup;
		rawResultSet = liResultSet;

		workingResultSet = new ArrayList();
		workingResultSet.addAll(rawResultSet);

		Collection<String> stExpressionKeys = hmLookup.getExpressions();
		saExpressionKeys = stExpressionKeys.toArray(new String[stExpressionKeys.size()]);

		iaDataTypes = new int[saExpressionKeys.length];

		oaGroupKeys = groupKeys;
		iaGroupBreaks = groupBreaks;

		initializeMeta();
	}

	/**
	 * Apply sorting and Grouping of chart.
	 * 
	 * @param sdBase           base series definition.
	 * @param sdValue          value series definition.
	 * @param aggregationExp
	 * @param saExpressionKeys
	 * @throws ChartException
	 */
	public void applyWholeSeriesSortingNGrouping(SeriesDefinition sdBase, SeriesDefinition sdValue,
			String[] aggregationExp, String[] saExpressionKeys) throws ChartException {
		applyValueSeriesGroupingNSorting(sdValue, aggregationExp, saExpressionKeys);
		applyBaseSeriesSortingAndGrouping(sdBase, aggregationExp, saExpressionKeys);
	}

	/**
	 * Apply value series grouping and sorting, it only do grouping/sorting, don't
	 * do aggregation, aggregation will be done in doing base series
	 * grouping/sorting.
	 * 
	 * @param sdValue        value series definition.
	 * @param aggregationExp
	 * @throws ChartException
	 * @since 2.3
	 */
	public void applyValueSeriesGroupingNSorting(SeriesDefinition sdValue, String[] aggregationExp,
			String[] saExpressionKeys) throws ChartException {
		generateGroupBreaks(sdValue);
		sortYGrouping(sdValue, aggregationExp, saExpressionKeys);
	}

	/**
	 * Generate group breaks with current row data.
	 * 
	 * @param sdValue the value series definition.
	 * @since 2.3
	 */
	public void generateGroupBreaks(SeriesDefinition sdValue) {
		iaGroupBreaks = findGroupBreaks(workingResultSet,
				(oaGroupKeys != null && oaGroupKeys.length > 0) ? oaGroupKeys[0] : null, sdValue.getQuery());
	}

	GroupingLookupHelper getLookupHelper() {
		return htLookup;
	}

	/**
	 * The class is used to sort data by Y grouping sort key.
	 * 
	 * @since BIRT 2.3
	 */
	private static class YGroupingSorter implements Comparator {

		private boolean ascending;
		private Collator collator;

		void sort(List aggregateData, SortOption so, Collator collator) {
			if (so == null) {
				return;
			}

			this.ascending = so == SortOption.ASCENDING_LITERAL;
			this.collator = collator;

			Collections.sort(aggregateData, this);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public final int compare(Object o1, Object o2) {
			Object obj1 = ((Object[]) o1)[2];
			Object obj2 = ((Object[]) o2)[2];

			if (obj1 == null && obj2 == null) {
				return 0;
			}
			if (obj1 == null && obj2 != null) {
				return ascending ? -1 : 1;
			}
			if (obj1 != null && obj2 == null) {
				return ascending ? 1 : -1;
			}

			int ct;
			if (obj1 instanceof String) {
				if (collator == null) {
					ct = ((String) obj1).compareTo((String) obj2);
				} else {
					ct = collator.compare(obj1.toString(), obj2.toString());
				}
			} else {
				ct = ((Comparable) obj1).compareTo(obj2);
			}

			return ascending ? ct : -ct;
		}
	}

	/**
	 * Sort Y grouping if the sort key of Y grouping doesn't equal the Y grouping
	 * expression.
	 * 
	 * @param sdValue
	 * @param aggregationExp
	 * @param saExpressionKeys
	 * @throws ChartException
	 * @since BIRT 2.3
	 */
	private void sortYGrouping(SeriesDefinition sdValue, String[] aggregationExp, String[] saExpressionKeys)
			throws ChartException {
		// 1. Check if sort key is value series expression.
		if (iaGroupBreaks == null || iaGroupBreaks.length == 0 || !sdValue.isSetSorting()
				|| sdValue.getQuery() == null) {
			return;
		}

		if (sdValue.getSortKey() == null || sdValue.getSortKey().getDefinition() == null) {
			// If sort key is null, consider it as grouping expression, no need
			// to sort again.
			return;
		}

		int ySortExprIndex = getLookupHelper().getYSortExprIndex();
		if (ySortExprIndex < 0) {
			// The sort key isn't value series expression, return directly.
			return;
		}

		// Get the aggregation function.
		final int iOrthogonalSeriesCount = saExpressionKeys.length;
		int[] iaColumnIndexes = new int[iOrthogonalSeriesCount];
		for (int i = 0; i < iOrthogonalSeriesCount; i++) {
			iaColumnIndexes[i] = getLookupHelper().findIndex(saExpressionKeys[i], aggregationExp[i]);
		}
		int relativeIndex = 0;
		for (int i = 0; i < iaColumnIndexes.length; i++) {
			if (ySortExprIndex == iaColumnIndexes[i]) {
				relativeIndex = i;
				break;
			}
		}

		IAggregateFunction iafa = null;
		try {
			iafa = PluginSettings.instance().getAggregateFunction(aggregationExp[relativeIndex]);
		} catch (Exception e) {
			;
		}

		if (iafa == null) {
			// If no aggregation function, use Sum as default to sort series.
			iafa = PluginSettings.instance().getAggregateFunction("Sum"); //$NON-NLS-1$
		}

		// 2. Compute aggregation on specified expression.
		List sortGroupsList = new ArrayList();
		for (int i = 0; i <= iaGroupBreaks.length; i++) {

			int startIndex = -1;
			int endIndex = -1;
			if (i == 0) // First group.
			{
				startIndex = 0;
				endIndex = iaGroupBreaks[i];
			} else if (i == iaGroupBreaks.length) // Last group.
			{
				startIndex = iaGroupBreaks[i - 1];
				endIndex = workingResultSet.size();
			} else {
				startIndex = iaGroupBreaks[i - 1];
				endIndex = iaGroupBreaks[i];
			}
			Object[] sortValue = new Object[3];
			sortValue[0] = Integer.valueOf(startIndex);
			sortValue[1] = Integer.valueOf(endIndex);

			// Initialize aggregation function.
			try {
				iafa.initialize();

				for (int j = startIndex; j < endIndex; j++) {
					Object[] oaTuple = (Object[]) workingResultSet.get(j);
					iafa.accumulate(oaTuple[ySortExprIndex]);
				}
				sortValue[2] = iafa.getAggregatedValue();
			} catch (IllegalArgumentException e) {
				// The sort key is not number, can not use SUM, just select first value of
				// current group as sort value.
				Object[] oaTuple = (Object[]) workingResultSet.get(startIndex);
				sortValue[2] = oaTuple[ySortExprIndex];
			}

			sortGroupsList.add(sortValue);
		}

		// 3. Sort by aggregation value.

		YGroupingSorter ygs = new YGroupingSorter();
		ygs.sort(sortGroupsList, sdValue.getSorting(), createCollator(sdValue));

		// 4. Reset row orders and group breaks.
		List newResultSet = new ArrayList();
		int[] newGroupBreaks = new int[sortGroupsList.size() - 1];
		int j = 0;
		for (Iterator iter = sortGroupsList.iterator(); iter.hasNext();) {
			Object[] sortValue = (Object[]) iter.next();
			int startIndex = ((Integer) sortValue[0]).intValue();
			int endIndex = ((Integer) sortValue[1]).intValue();
			for (int i = startIndex; i < endIndex; i++) {
				newResultSet.add(workingResultSet.get(i));
			}

			if (j < (sortGroupsList.size() - 1)) {
				newGroupBreaks[j++] = newResultSet.size();
			}
		}

		workingResultSet.clear();
		workingResultSet.addAll(newResultSet);
		iaGroupBreaks = newGroupBreaks;
	}

	/**
	 * Internally called to setup the structure of the resultset and initialize any
	 * metadata associated with it
	 */
	private void initializeMeta() {
		final Iterator it = workingResultSet.iterator();
		final int iColumnCount = iaDataTypes.length;
		final boolean[] boaFound = new boolean[iColumnCount];
		Object[] oaTuple;
		boolean bAllDone;

		// TODO seems buggy here
		while (it.hasNext()) {
			oaTuple = (Object[]) it.next();
			for (int i = 0; i < iColumnCount; i++) {
				bAllDone = true;
				if (oaTuple[i] == null) {
					continue;
				}

				boaFound[i] = true;
				if (oaTuple[i] instanceof Number) // DYNAMICALLY DETERMINE
				// DATA TYPE
				{
					iaDataTypes[i] = IConstants.NUMERICAL;
				} else if (oaTuple[i] instanceof String) // DYNAMICALLY
				// DETERMINE DATA TYPE
				{
					iaDataTypes[i] = IConstants.TEXT;
				} else if (oaTuple[i] instanceof Date || oaTuple[i] instanceof Calendar) // DYNAMICALLY
				// DETERMINE DATA
				// TYPE
				{
					iaDataTypes[i] = IConstants.DATE_TIME;
				} else if (oaTuple[i] instanceof Boolean) {
					iaDataTypes[i] = IConstants.BOOLEAN;
				} else if (oaTuple[i] instanceof Object[]) {
					iaDataTypes[i] = IConstants.ARRAY;
				}

				for (int j = 0; j < iColumnCount; j++) {
					if (!boaFound[j]) {
						bAllDone = false;
						break;
					}
				}

				if (bAllDone) {
					return;
				}
			}
		}

		logger.log(ILogger.WARNING, Messages.getString("exception.resultset.data.type.retrieval.failed" //$NON-NLS-1$
		));
	}

	/**
	 * Groups rows of data as specified in the grouping criteria for the series
	 * definition
	 * 
	 * @throws ChartException
	 */
	public void applyBaseSeriesSortingAndGrouping(SeriesDefinition sdBase, String[] aggregationExp,
			String[] saExpressionKeys) throws ChartException {
		boolean needBaseGrouping = true;
		boolean needBaseSorting = true;

		// VALIDATE SERIES GROUPING
		final SeriesGrouping sg = sdBase.getGrouping();
		if (sg == null || !sg.isEnabled()) {
			needBaseGrouping = false;
			;
		}

		if (htLookup.getBaseSortExprIndex() < 0) {
			needBaseSorting = false;
		}

		if (needBaseSorting) {
			if (!needBaseGrouping) {
				doBaseSorting(sdBase);
				return;
			}
		} else if (!needBaseGrouping) {
			return;
		}

		// Apply base series sorting.
		final Series seBaseDesignTime = sdBase.getDesignTimeSeries();
		final Query q = (Query) seBaseDesignTime.getDataDefinition().get(0);
		final int iSortColumnIndex = htLookup.findIndexOfBaseSeries(q.getDefinition());

		SortOption so = null;
		if (!sdBase.isSetSorting()) {
			if (needBaseGrouping) {
				logger.log(ILogger.WARNING, Messages.getString("warn.unspecified.sorting", //$NON-NLS-1$
						new Object[] { sdBase }, ULocale.getDefault()));

				so = SortOption.ASCENDING_LITERAL;
			}
		} else {
			so = sdBase.getSorting();
		}

		GroupingSorter gs = new GroupingSorter();
		gs.sort(workingResultSet, iSortColumnIndex, so, iaGroupBreaks, createCollator(sdBase));

		// LOOKUP AGGREGATE FUNCTION
		final int iOrthogonalSeriesCount = saExpressionKeys.length;
		IAggregateFunction[] iafa = new IAggregateFunction[iOrthogonalSeriesCount];
		try {
			for (int i = 0; i < iOrthogonalSeriesCount; i++) {
				iafa[i] = PluginSettings.instance().getAggregateFunction(aggregationExp[i]);
				iafa[i].initialize();
			}
		} catch (ChartException pex) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_BINDING, pex);
		}

		int[] iaColumnIndexes = new int[iOrthogonalSeriesCount];
		for (int i = 0; i < iOrthogonalSeriesCount; i++) {
			iaColumnIndexes[i] = getLookupHelper().findIndex(saExpressionKeys[i], aggregationExp[i]);
		}

		final DataType dtGrouping = sg.getGroupType();
		if (dtGrouping == DataType.NUMERIC_LITERAL) {
			groupNumerically(workingResultSet, iSortColumnIndex, iaColumnIndexes, iaGroupBreaks, null,
					sg.getGroupingInterval(), iafa);
		} else if (dtGrouping == DataType.DATE_TIME_LITERAL) {
			groupDateTime(workingResultSet, iSortColumnIndex, iaColumnIndexes, iaGroupBreaks, null,
					(long) sg.getGroupingInterval(), sg.getGroupingUnit(), iafa);
		} else if (dtGrouping == DataType.TEXT_LITERAL) {
			groupTextually(workingResultSet, iSortColumnIndex, iaColumnIndexes, iaGroupBreaks, null,
					(long) sg.getGroupingInterval(), sg.getGroupingUnit(), iafa);

		}

		// Sort final row data again by actual sort expression on base series.
		doBaseSorting(sdBase);

		// re-initialize meta since Aggregation could change data
		// type(text->count)
		initializeMeta();
	}

	private void doBaseSorting(SeriesDefinition sd) {
		int iBaseSortColumnIndex = htLookup.getBaseSortExprIndex();
		if (iBaseSortColumnIndex >= 0) {
			GroupingSorter gs = new GroupingSorter();
			gs.sort(workingResultSet, iBaseSortColumnIndex, sd.getSorting(), iaGroupBreaks, createCollator(sd));
		}
	}

	private void groupNumerically(List resultSet, int iBaseColumnIndex, int[] iaColumnIndexes, int[] iaBreaks,
			NumberDataElement ndeBaseReference, double iGroupingInterval, IAggregateFunction[] iafa)
			throws ChartException {
		final int iOrthogonalSeriesCount = iaColumnIndexes.length;

		int iStartIndex = 0, iEndIndex;
		int totalGroupCount = iaBreaks == null ? 1 : (iaBreaks.length + 1);
		int totalRowCount = resultSet.size();

		for (int k = 0; k < totalGroupCount; k++) {
			if (k == totalGroupCount - 1) {
				iEndIndex = totalRowCount;
			} else {
				iEndIndex = iaBreaks[k];
			}

			NumberDataElement baseReference = ndeBaseReference;

			if (baseReference == null) {
				// ASSIGN IT TO THE FIRST TYPLE'S GROUP EXPR VALUE
				Number obj = (Number) ((Object[]) resultSet.get(iStartIndex))[iBaseColumnIndex];
				baseReference = NumberDataElementImpl.create(obj == null ? 0 : obj.doubleValue());
			}

			Object[] oaTuple, oaSummarizedTuple = null;
			int iGroupIndex = 0, iLastGroupIndex = 0;
			boolean bFirst = true, bGroupBreak = false;
			double dBaseReference = BASE_START_INTERVAL_VALUE;
			List trashList = new ArrayList();
			double dLastReference = dBaseReference;

			for (int j = iStartIndex; j < iEndIndex; j++) {
				oaTuple = (Object[]) resultSet.get(j);

				if (oaTuple[iBaseColumnIndex] != null) {
					if (iGroupingInterval == 0) {
						if (((Number) oaTuple[iBaseColumnIndex]).doubleValue() != dLastReference) {
							iGroupIndex++;
						}
					} else {
						iGroupIndex = (int) Math
								.floor(Math.abs((((Number) oaTuple[iBaseColumnIndex]).doubleValue() - dBaseReference)
										/ iGroupingInterval));
					}

					dLastReference = ((Number) oaTuple[iBaseColumnIndex]).doubleValue();
				} else {
					if (iGroupingInterval == 0) {
						if (!Double.isNaN(dLastReference)) {
							iGroupIndex++;
						}
					} else {
						// Treat null value as 0.
						iGroupIndex = (int) Math.floor(Math.abs(dBaseReference / iGroupingInterval));
					}

					dLastReference = Double.NaN;
				}

				if (!bFirst) {
					bGroupBreak = (iLastGroupIndex != iGroupIndex);
				}

				if (bGroupBreak || bFirst) {
					if (oaSummarizedTuple != null) // FIRST ROW IN GROUP
					{
						// bGroupBreak == true and not first row.
						for (int i = 0; i < iOrthogonalSeriesCount; i++) {
							// Save aggregation value into previous tople.
							oaSummarizedTuple[iaColumnIndexes[i]] = iafa[i].getAggregatedValue();
							iafa[i].initialize(); // RESET
						}

						// reset base reference
						Number obj = (Number) oaTuple[iBaseColumnIndex];
						baseReference = NumberDataElementImpl.create(obj == null ? 0 : obj.doubleValue());
						dLastReference = baseReference.getValue();
					} else {
						// FIRST ROW IN RS
						bFirst = false;
					}

					// Start a new tuple.
					oaSummarizedTuple = oaTuple;
				} else {
					// The value of base column is same, so the j'th row is duplicate row.
					trashList.add(Integer.valueOf(j));
				}

				for (int i = 0; i < iOrthogonalSeriesCount; i++) {
					try {
						// Aggregate value.
						iafa[i].accumulate(oaTuple[iaColumnIndexes[i]]);
					} catch (IllegalArgumentException uiex) {
						throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION, uiex);
					}
				}
				iLastGroupIndex = iGroupIndex;
			}

			if (oaSummarizedTuple != null) // LAST ROW IN GROUP
			{
				for (int i = 0; i < iOrthogonalSeriesCount; i++) {
					oaSummarizedTuple[iaColumnIndexes[i]] = iafa[i].getAggregatedValue();
					iafa[i].initialize(); // reset
				}
			}

			for (int i = 0; i < trashList.size(); i++) {
				resultSet.remove(((Integer) trashList.get(i)).intValue() - i);
			}

			int groupChange = trashList.size();
			trashList.clear();

			// update group breaks due to base data changes
			if (iaBreaks != null && iaBreaks.length > 0 && groupChange > 0) {
				for (int j = k; j < iaBreaks.length; j++) {
					iaBreaks[j] -= groupChange;
				}
			}

			iStartIndex = iEndIndex - groupChange;
			totalRowCount -= groupChange;
		}
	}

	private void groupDateTime(List resultSet, int iBaseColumnIndex, int[] iaColumnIndexes, int[] iaBreaks,
			CDateTime ndeBaseReference, long iGroupingInterval, GroupingUnitType groupingUnit,
			IAggregateFunction[] iafa) throws ChartException {
		final int iOrthogonalSeriesCount = iaColumnIndexes.length;

		int cunit = GroupingUtil.groupingUnit2CDateUnit(groupingUnit);

		int iStartIndex = 0, iEndIndex;
		int totalGroupCount = iaBreaks == null ? 1 : (iaBreaks.length + 1);
		int totalRowCount = resultSet.size();

		for (int k = 0; k < totalGroupCount; k++) {
			if (k == totalGroupCount - 1) // Last group.
			{
				iEndIndex = totalRowCount;
			} else {
				iEndIndex = iaBreaks[k];
			}

			CDateTime baseReference = ndeBaseReference;

			if (baseReference == null) {
				Object obj = ((Object[]) resultSet.get(iStartIndex))[iBaseColumnIndex];

				// ASSIGN IT TO THE FIRST TYPLE'S GROUP EXPR VALUE
				if (obj instanceof CDateTime) {
					baseReference = (CDateTime) obj;
				} else {
					// set as the smallest Date.
					baseReference = new CDateTime(0);
				}
			}

			baseReference.clearBelow(cunit);

			Object[] oaTuple, oaSummarizedTuple = null;
			int iGroupIndex = 0, iLastGroupIndex = 0;
			boolean bFirst = true, bGroupBreak = false;
			List trashList = new ArrayList();

			for (int j = iStartIndex; j < iEndIndex; j++) {
				oaTuple = (Object[]) resultSet.get(j);
				CDateTime dCurrentValue = null;
				if (oaTuple[iBaseColumnIndex] != null) {
					Object obj = oaTuple[iBaseColumnIndex];

					// ASSIGN IT TO THE FIRST TYPLE'S GROUP EXPR VALUE
					if (obj instanceof CDateTime) {
						dCurrentValue = (CDateTime) obj;
					} else {
						dCurrentValue = new CDateTime(0);
					}

					dCurrentValue.clearBelow(cunit);
				} else {
					// Treat null value as the smallest date.
					dCurrentValue = new CDateTime(0);
				}

				// Save the approximate date in the runtime data, so they could
				// be grouped by units in renderer
				oaTuple[iBaseColumnIndex] = dCurrentValue;

				double diff = CDateTime.computeDifference(dCurrentValue, baseReference, cunit, true);
				if (diff != 0) {
					iGroupIndex = iGroupingInterval == 0 ? iGroupIndex + 1
							: (int) Math.floor(Math.abs(diff / iGroupingInterval));
				}

				if (!bFirst) {
					bGroupBreak = (iLastGroupIndex != iGroupIndex);
				}

				if (bGroupBreak || bFirst) {
					if (oaSummarizedTuple != null) // FIRST ROW IN GROUP
					{
						for (int i = 0; i < iOrthogonalSeriesCount; i++) {
							oaSummarizedTuple[iaColumnIndexes[i]] = iafa[i].getAggregatedValue();
							iafa[i].initialize(); // RESET
						}

						// reset base reference
						Object obj = oaTuple[iBaseColumnIndex];

						// ASSIGN IT TO THE FIRST TYPLE'S GROUP EXPR VALUE
						if (obj instanceof CDateTime) {
							baseReference = (CDateTime) obj;
						} else {
							// set as the smallest Date.
							baseReference = new CDateTime(0);
						}

						baseReference.clearBelow(cunit);
						iGroupIndex = 0;
					} else {
						// FIRST ROW IN RS
						bFirst = false;
					}
					oaSummarizedTuple = oaTuple;
				} else {
					trashList.add(Integer.valueOf(j));
				}

				for (int i = 0; i < iOrthogonalSeriesCount; i++) {
					try {
						iafa[i].accumulate(oaTuple[iaColumnIndexes[i]]);
					} catch (IllegalArgumentException uiex) {
						throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION, uiex);
					}
				}
				iLastGroupIndex = iGroupIndex;
			}

			if (oaSummarizedTuple != null) // LAST ROW IN GROUP
			{
				for (int i = 0; i < iOrthogonalSeriesCount; i++) {
					oaSummarizedTuple[iaColumnIndexes[i]] = iafa[i].getAggregatedValue();
					iafa[i].initialize(); // reset
				}
			}

			for (int i = 0; i < trashList.size(); i++) {
				resultSet.remove(((Integer) trashList.get(i)).intValue() - i);
			}

			int groupChange = trashList.size();
			trashList.clear();

			// update group breaks due to base data changes
			if (iaBreaks != null && iaBreaks.length > 0 && groupChange > 0) {
				for (int j = k; j < iaBreaks.length; j++) {
					iaBreaks[j] -= groupChange;
				}
			}

			iStartIndex = iEndIndex - groupChange;
			totalRowCount -= groupChange;
		}
	}

	private void groupTextually(List resultSet, int iBaseColumnIndex, int[] iaColumnIndexes, int[] iaBreaks,
			String ndeBaseReference, long iGroupingInterval, GroupingUnitType groupingUnit, IAggregateFunction[] iafa)
			throws ChartException {
		final int iOrthogonalSeriesCount = iaColumnIndexes.length;

		int iStartIndex = 0, iEndIndex;
		int totalGroupCount = iaBreaks == null ? 1 : (iaBreaks.length + 1);
		int totalRowCount = resultSet.size();

		// NOTE: Here, the 'totalGroupCount' variable actually indicates how many series
		// will be generated, the value of 'totalGroupCount' is related with Y
		// grouping. If Y grouping is set, its count will more than 1, else
		// there is only one series count.
		for (int k = 0; k < totalGroupCount; k++) {
			if (k == totalGroupCount - 1) // Last series.
			{
				iEndIndex = totalRowCount;
			} else {
				iEndIndex = iaBreaks[k];
			}

			String baseReference = ndeBaseReference;

			if (baseReference == null) {
				// ASSIGN IT TO THE FIRST TYPLE'S GROUP EXPR VALUE
				baseReference = ChartUtil.stringValue(((Object[]) resultSet.get(iStartIndex))[iBaseColumnIndex]);
			}

			Object[] oaTuple, oaSummarizedTuple = null;
			int iGroupIndex = 0, iLastGroupIndex = 0, iGroupCounter = 0;
			boolean bFirst = true, bGroupBreak = false;
			List trashList = new ArrayList();

			for (int j = iStartIndex; j < iEndIndex; j++) {
				oaTuple = (Object[]) resultSet.get(j);

				if (oaTuple[iBaseColumnIndex] != null) {
					String dBaseValue = String.valueOf(oaTuple[iBaseColumnIndex]);

					if (!GroupingUtil.isMatchedGroupingString(dBaseValue, baseReference, groupingUnit,
							(int) iGroupingInterval)) {
						iGroupCounter++;
						baseReference = dBaseValue;
						if (groupingUnit == GroupingUnitType.STRING_PREFIX_LITERAL) {
							iGroupIndex++;
						}
					}

					// The interval of string prefix case indicates the number of prefix, don't mean
					// interval range.
					if (groupingUnit != GroupingUnitType.STRING_PREFIX_LITERAL) {
						if (iGroupCounter > iGroupingInterval) {
							iGroupIndex++;
						}
					}
				} else {
					// current value is null, check last value.
					if (baseReference != null) {
						iGroupCounter++;
						baseReference = null;
					}

					if (iGroupCounter > iGroupingInterval) {
						iGroupIndex++;
					}
				}

				if (!bFirst) {
					bGroupBreak = (iLastGroupIndex != iGroupIndex);
				}

				if (bGroupBreak) {
					// reset group counter
					iGroupCounter = 0;
				}

				if (bGroupBreak || bFirst) {
					if (oaSummarizedTuple != null) // FIRST ROW IN GROUP
					{
						for (int i = 0; i < iOrthogonalSeriesCount; i++) {
							oaSummarizedTuple[iaColumnIndexes[i]] = iafa[i].getAggregatedValue();
							iafa[i].initialize(); // RESET
						}

						// reset base reference
						baseReference = ChartUtil.stringValue(oaTuple[iBaseColumnIndex]);
						iGroupIndex = 0;
					} else {
						// FIRST ROW IN RS
						bFirst = false;
					}
					oaSummarizedTuple = oaTuple;
				} else {
					trashList.add(Integer.valueOf(j));
				}

				for (int i = 0; i < iOrthogonalSeriesCount; i++) {
					try {
						iafa[i].accumulate(oaTuple[iaColumnIndexes[i]]);
					} catch (IllegalArgumentException uiex) {
						throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION, uiex);
					}
				}
				iLastGroupIndex = iGroupIndex;
			}

			if (oaSummarizedTuple != null) // LAST ROW IN GROUP
			{
				for (int i = 0; i < iOrthogonalSeriesCount; i++) {
					oaSummarizedTuple[iaColumnIndexes[i]] = iafa[i].getAggregatedValue();
					iafa[i].initialize(); // reset
				}
			}

			for (int i = 0; i < trashList.size(); i++) {
				resultSet.remove(((Integer) trashList.get(i)).intValue() - i);
			}

			int groupChange = trashList.size();
			trashList.clear();

			// update group breaks due to base data changes
			if (iaBreaks != null && iaBreaks.length > 0 && groupChange > 0) {
				for (int j = k; j < iaBreaks.length; j++) {
					iaBreaks[j] -= groupChange;
				}
			}

			iStartIndex = iEndIndex - groupChange;
			totalRowCount -= groupChange;
		}
	}

	/**
	 * Returns a pre-computed group count associated with the resultset wrapper
	 * instance
	 * 
	 * @return A pre-computed group count associated with the resultset wrapper
	 *         instance
	 */
	public int getGroupCount() {
		if (iaGroupBreaks == null) {
			return 1;
		} else {
			return iaGroupBreaks.length + 1;
		}
	}

	/**
	 * Returns the row count in specified group.
	 * 
	 * @param iGroupIndex
	 * @return
	 */
	public int getGroupRowCount(int iGroupIndex) {
		int startRow = (iGroupIndex <= 0) ? 0 : iaGroupBreaks[iGroupIndex - 1];
		int endRow = (iGroupIndex > iaGroupBreaks.length - 1) ? getRowCount() : iaGroupBreaks[iGroupIndex];

		return endRow - startRow;
	}

	/**
	 * Returns a pre-computed column count associated with the resultset wrapper
	 * instance
	 * 
	 * @return A pre-computed column count associated with the resultset wrapper
	 *         instance
	 */
	public int getColumnCount() {
		return saExpressionKeys.length;
	}

	/**
	 * Returns the number of rows of data associated with the resultset wrapper
	 * instance
	 * 
	 * @return The number of rows of data associated with the resultset wrapper
	 *         instance
	 */
	public int getRowCount() {
		return workingResultSet.size();
	}

	/**
	 * Extracts the group's key value that remains unchanged for a given group
	 * 
	 * @param iGroupIndex    The group index for which the key is requested
	 * @param sExpressionKey The expression column that holds the group key value
	 * 
	 * @return The group key value associated with the requested group index
	 */
	public Object getGroupKey(int iGroupIndex, String sExpressionKey, String aggExp) {
		final int iColumnIndex = htLookup.findIndex(sExpressionKey, aggExp);
		if (iColumnIndex < 0) {
			return IConstants.UNDEFINED_STRING;
		}

		final int iRow = (iGroupIndex <= 0) ? 0 : iaGroupBreaks[iGroupIndex - 1];

		if (iRow >= 0 && iRow < workingResultSet.size()) {
			return ((Object[]) workingResultSet.get(iRow))[iColumnIndex];
		}
		return null; // THERE WAS NO DATA
	}

	/**
	 * Extracts the group's key value that remains unchanged for a given group
	 * 
	 * @param iGroupIndex  The group index for which the key is requested
	 * @param iColumnIndex The column index from which the group key value is to be
	 *                     extracted
	 * 
	 * @return The group key value associated with the requested group index
	 */
	public Object getGroupKey(int iGroupIndex, int iColumnIndex) {
		final int iRow = (iGroupIndex <= 0) ? 0 : iaGroupBreaks[iGroupIndex - 1];

		if (iRow >= 0 && iRow < workingResultSet.size()) {
			return ((Object[]) workingResultSet.get(iRow))[iColumnIndex];
		}
		return null; // THERE WAS NO DATA
	}

	/**
	 * Creates an instance of a resultset subset that uses references to dynamically
	 * compute a subset of the original resultset instance rather than duplicate a
	 * copy of the original resultset data content
	 * 
	 * @param iGroupIndex    The group number for which a subset is requested
	 * @param sExpressionKey A single expression column for which a subset is
	 *                       requested
	 * 
	 * @return An instance of the resultset subset
	 */
	public ResultSetDataSet getSubset(int iGroupIndex, String sExpressionKey, String aggExp) {
		return new ResultSetDataSet(this, new int[] { htLookup.findIndex(sExpressionKey, aggExp) },
				(iGroupIndex <= 0) ? 0 : iaGroupBreaks[iGroupIndex - 1],
				(iGroupIndex >= iaGroupBreaks.length - 1) ? getRowCount() : iaGroupBreaks[iGroupIndex]);
	}

	/**
	 * Creates an instance of a resultset subset that uses references to dynamically
	 * compute a subset of the original resultset instance rather than duplicate a
	 * copy of the original resultset data content
	 * 
	 * @param iGroupIndex      The group number for which a subset is requested
	 * @param elExpressionKeys The expression columns for which a subset is
	 *                         requested
	 * 
	 * @return An instance of the resultset subset
	 */
	public ResultSetDataSet getSubset(int iGroupIndex, EList elExpressionKeys, String aggExp) {
		final int n = elExpressionKeys.size();
		String[] sExpressionKey = new String[n];
		for (int i = 0; i < n; i++) {
			sExpressionKey[i] = ((Query) elExpressionKeys.get(i)).getDefinition();
		}
		final int[] iaColumnIndexes = htLookup.findBatchIndex(sExpressionKey, aggExp);

		return new ResultSetDataSet(this, iaColumnIndexes, (iGroupIndex <= 0) ? 0 : iaGroupBreaks[iGroupIndex - 1],
				(iGroupIndex > iaGroupBreaks.length - 1) ? getRowCount() : iaGroupBreaks[iGroupIndex]);
	}

	/**
	 * Creates an instance of a resultset subset that uses references to dynamically
	 * compute a subset of the original resultset instance rather than duplicate a
	 * copy of the original resultset data content
	 * 
	 * @param iGroupIndex     The group number for which a subset is requested
	 * @param sExpressionKeys The expression columns for which a subset is requested
	 * @param aggExp          The aggregation function name.
	 * @param isValueSeries   indicates if the sub-dataset is retrieved for value
	 *                        series.
	 * 
	 * @return An instance of the resultset subset
	 */
	public ResultSetDataSet getSubset(int iGroupIndex, String[] sExpressionKeys, String aggExp, boolean isValueSeries) {
		if (sExpressionKeys == null) {
			return null;
		}

		final int[] iaColumnIndexes = htLookup.findBatchIndex(sExpressionKeys, aggExp);
		if (isValueSeries) {
			return new VSResultSetDataSet(this, iaColumnIndexes,
					(iGroupIndex <= 0) ? 0 : iaGroupBreaks[iGroupIndex - 1],
					(iGroupIndex > iaGroupBreaks.length - 1) ? getRowCount() : iaGroupBreaks[iGroupIndex]);
		}
		return new ResultSetDataSet(this, iaColumnIndexes, (iGroupIndex <= 0) ? 0 : iaGroupBreaks[iGroupIndex - 1],
				(iGroupIndex > iaGroupBreaks.length - 1) ? getRowCount() : iaGroupBreaks[iGroupIndex]);
	}

	/**
	 * Creates an instance of a resultset subset that uses references to dynamically
	 * compute a subset of the original resultset instance rather than duplicate a
	 * copy of the original resultset data content
	 * 
	 * @param iGroupIndex  The group number for which a subset is requested
	 * @param iColumnIndex A single column (defined by the index) for which the
	 *                     subset is requested
	 * 
	 * @return An instance of the resultset subset
	 */
	public ResultSetDataSet getSubset(int iGroupIndex, int iColumnIndex) {
		return new ResultSetDataSet(this, new int[] { iColumnIndex },
				(iGroupIndex <= 0) ? 0 : iaGroupBreaks[iGroupIndex - 1],
				(iGroupIndex >= iaGroupBreaks.length) ? getRowCount() : iaGroupBreaks[iGroupIndex]);
	}

	/**
	 * Creates an instance of a resultset subset that uses references to dynamically
	 * compute a subset of the original resultset instance rather than duplicate a
	 * copy of the original resultset data content
	 * 
	 * @param elExpressions The expression columns for which a resultset subset is
	 *                      being requested
	 * 
	 * @return The resultset subset containing the requested columns and all rows of
	 *         the resultset
	 */
	public ResultSetDataSet getSubset(EList elExpressions, String aggExp) throws ChartException {
		final int n = elExpressions.size();
		String[] sExpression = new String[n];
		for (int i = 0; i < n; i++) {
			sExpression[i] = ((Query) elExpressions.get(i)).getDefinition();
		}
		final int[] iaColumnIndexes = htLookup.findBatchIndex(sExpression, aggExp);
		return new ResultSetDataSet(this, iaColumnIndexes, 0, getRowCount());
	}

	/**
	 * Creates an instance of a resultset subset that uses references to dynamically
	 * compute a subset of the original resultset instance rather than duplicate a
	 * copy of the original resultset data content
	 * 
	 * @param sExpressionKey A single expression column for which a resultset subset
	 *                       is being requested
	 * 
	 * @return The resultset subset containing the requested column and all rows of
	 *         the resultset
	 */
	public ResultSetDataSet getSubset(String sExpressionKey, String aggExp) {
		return new ResultSetDataSet(this, new int[] { htLookup.findIndex(sExpressionKey, aggExp) }, 0, getRowCount());
	}

	/**
	 * Creates an instance of a resultset subset that uses references to dynamically
	 * compute a subset of the original resultset instance rather than duplicate a
	 * copy of the original resultset data content
	 * 
	 * @param sExpressionKeys The expression columns for which a resultset subset is
	 *                        being requested
	 * @param aggExp          The aggregation function name.
	 * @param isValueSeries   indicates if the sub-dataset is retrieved for value
	 *                        series.
	 * 
	 * @return The resultset subset containing the requested columns and all rows of
	 *         the resultset
	 * 
	 */
	public ResultSetDataSet getSubset(String[] sExpressionKeys, String aggExp, boolean isValueSeries)
			throws ChartException {
		if (sExpressionKeys == null) {
			return null;
		}

		final int[] iaColumnIndexes = htLookup.findBatchIndex(sExpressionKeys, aggExp);
		if (isValueSeries) {
			return new VSResultSetDataSet(this, iaColumnIndexes, 0, getRowCount());
		}
		return new ResultSetDataSet(this, iaColumnIndexes, 0, getRowCount());
	}

	/**
	 * Creates an instance of a resultset subset that uses references to dynamically
	 * compute a subset of the original resultset instance rather than duplicate a
	 * copy of the original resultset data content
	 * 
	 * @param iColumnIndex A single column for which a resultset subset is being
	 *                     requested
	 * 
	 * @return The resultset subset containing the requested column (specified by
	 *         index) and all rows of the resultset
	 */
	public ResultSetDataSet getSubset(int iColumnIndex) {
		return new ResultSetDataSet(this, new int[] { iColumnIndex }, 0, getRowCount());
	}

	/**
	 * Returns the values for given column and compute index arrays.
	 * 
	 * @param iColumnIndex
	 * @param sorting
	 * @param isCategory
	 * @return an array have two return objects, first is the base value list,
	 *         second is the index map for all grouped subset.
	 */
	public Object[] getMergedGroupingBaseValues(int iColumnIndex, SortOption sorting, boolean isCategory) {
		int groupCount = getGroupCount();

		final List idxList = new ArrayList(groupCount);

		Object oValue;
		ResultSetDataSet rsd;

		final List baseValue = new ArrayList();
		List<Integer> idx;

		for (int k = 0; k < groupCount; k++) {
			rsd = getSubset(k, iColumnIndex);

			idx = new ArrayList<Integer>();

			if (k == 0) {
				// if it's the first group, just add all values.
				int i = 0;
				while (rsd.hasNext()) {
					oValue = rsd.next()[0];

					baseValue.add(oValue);
					idx.add(Integer.valueOf(i++));
				}
			} else {
				while (rsd.hasNext()) {
					oValue = rsd.next()[0];

					boolean matched = false;
					int insertPoint = -1;

					// compare to existing base values and find an available
					// position. Only when sort enabled or axis type is
					// category.

					// Do sorting
					int size = (sorting != null || isCategory) ? baseValue.size() : 0;
					for (int j = 0; j < size; j++) {
						Object ov = baseValue.get(j);

						int cprt = compareObjects(oValue, ov);
						if (cprt == 0) {
							if (!idx.contains(Integer.valueOf(j))) {
								idx.add(Integer.valueOf(j));
								matched = true;
								break;
							} else if (sorting != null) {
								insertPoint = j + 1;
							}
						} else if (cprt < 0) {
							if (sorting == SortOption.DESCENDING_LITERAL) {
								insertPoint = j + 1;
							}
						} else if (cprt > 0) {
							if (sorting == SortOption.ASCENDING_LITERAL) {
								insertPoint = j + 1;
							}
						}

					}

					if (!matched) {
						if (sorting != null && insertPoint == -1) {
							// convert position to first since no value is
							// greater/less than current value.
							insertPoint = 0;
						}

						if (insertPoint == -1 || insertPoint >= baseValue.size()) {
							// if no existing position available, append to the
							// end.
							baseValue.add(oValue);
							idx.add(Integer.valueOf(baseValue.size() - 1));
						} else {
							// insert and adjust existing indices.
							baseValue.add(insertPoint, oValue);

							// adjust current group index and add new position.
							for (int i = 0; i < idx.size(); i++) {
								int x = idx.get(i);

								if (x >= insertPoint) {
									idx.set(i, Integer.valueOf(x + 1));
								}
							}
							idx.add(Integer.valueOf(insertPoint));

							// adjust computed group indices.
							for (Iterator itr = idxList.iterator(); itr.hasNext();) {
								List gidx = (List) itr.next();
								for (int i = 0; i < gidx.size(); i++) {
									int x = ((Integer) gidx.get(i)).intValue();

									if (x >= insertPoint) {
										gidx.set(i, Integer.valueOf(x + 1));
									}
								}
							}
						}
					}
				}
			}
			idxList.add(idx);
		}

		// align all index array to equal length, fill empty value with -1;
		int maxLen = baseValue.size();
		for (Iterator itr = idxList.iterator(); itr.hasNext();) {
			List lst = (List) itr.next();
			if (lst.size() < maxLen) {
				int inc = maxLen - lst.size();
				for (int i = 0; i < inc; i++) {
					lst.add(Integer.valueOf(-1));
				}
			}
		}

		return new Object[] { baseValue, idxList };
	}

	/**
	 * Returns the data type of specified column.
	 * 
	 * @param iColumnIndex
	 * @return
	 */
	public int getColumnDataType(int iColumnIndex) {
		return iaDataTypes[iColumnIndex];
	}

	/**
	 * Returns the iterator of associated resultset.
	 * 
	 * @return
	 */
	public Iterator iterator() {
		if (workingResultSet != null) {
			return workingResultSet.iterator();
		}
		return null;
	}

	/**
	 * Internally walks through the resultset and computes the group breaks cached
	 * for subsequent use
	 * 
	 * @param bGrouped Indicates if the resultset contains the group key
	 * 
	 * @return Row indexes containing changing group key values
	 */
	private int[] findGroupBreaks(List resultSet, GroupKey groupKey, Query query) {
		if (groupKey == null || groupKey.getKey() == null) {
			return NO_GROUP_BREAKS;
		}

		// For previous chart version(before2.3M3), the seriesGrouping argument
		// may be null, so here needs to check null case.
		boolean groupingIntervalEnabled = false;
		if (query != null && query.getGrouping() != null) {
			groupingIntervalEnabled = true;
		}

		GroupKey newGroupKey = groupKey;
		if (groupingIntervalEnabled && groupKey.getDirection() == null) {
			newGroupKey = new GroupKey(groupKey.getKey(), SortOption.ASCENDING_LITERAL);
			newGroupKey.setKeyIndex(groupKey.getKeyIndex());
		}
		// TODO support multiple keys for a single orthogonal series
		Collections.sort(resultSet, new TupleComparator(new GroupKey[] { newGroupKey }));

		final int iColumnIndex = newGroupKey.getKeyIndex();

		final List<Integer> alBreaks = new ArrayList<Integer>(8);
		boolean bFirst = true;
		Object oValue, oPreviousValue = null;
		int iRowIndex = 0;

		if (groupingIntervalEnabled && query != null) {
			SeriesGrouping seriesGrouping = query.getGrouping();
			// Reset grouped data by series grouping setting.
			resetGroupedData(resultSet, iColumnIndex, seriesGrouping);

			final Iterator it = resultSet.iterator();
			int intervalCount = 0;
			int lastGroupIndex = 0;
			double groupInterval = seriesGrouping.getGroupingInterval();

			while (it.hasNext()) {
				oValue = ((Object[]) it.next())[iColumnIndex];
				if (oValue == null) {
					continue;
				}

				iRowIndex++;
				if (bFirst) {
					bFirst = false;
					oPreviousValue = oValue;
					if (seriesGrouping.getGroupType() == DataType.NUMERIC_LITERAL) {
						lastGroupIndex = (int) Math.floor(Math
								.abs((((Number) oValue).doubleValue() - BASE_START_INTERVAL_VALUE) / groupInterval));
					}
					continue;
				}

				if (compareObjects(oPreviousValue, oValue, seriesGrouping) != 0) {
					if (seriesGrouping.getGroupType() == DataType.NUMERIC_LITERAL) {
						// Calculate interval range for numeric case, it may be decimal interval.
						if (groupInterval == 0) {
							alBreaks.add(Integer.valueOf(iRowIndex - 1));
						} else {
							int groupIndex = (int) Math.floor(Math.abs(
									(((Number) oValue).doubleValue() - BASE_START_INTERVAL_VALUE) / groupInterval));

							if (lastGroupIndex != groupIndex) {
								alBreaks.add(Integer.valueOf(iRowIndex - 1));

							}

							lastGroupIndex = groupIndex;
						}
					} else if (seriesGrouping.getGroupType() == DataType.DATE_TIME_LITERAL) {
						int cunit = GroupingUtil.groupingUnit2CDateUnit(seriesGrouping.getGroupingUnit());
						double diff = CDateTime.computeDifference(((CDateTime) oValue).getUnitStart(cunit),
								((CDateTime) oPreviousValue).getUnitStart(cunit), cunit, true);
						if (diff != 0) {
							int groupingInterval = (int) seriesGrouping.getGroupingInterval();
							if (groupingInterval == 0) {
								alBreaks.add(Integer.valueOf(iRowIndex - 1));
							} else {
								if ((int) Math.floor(Math.abs(diff / groupingInterval)) > 0) {
									alBreaks.add(Integer.valueOf(iRowIndex - 1));
								}
							}
						}
					} else {
						if (seriesGrouping.getGroupingUnit() == GroupingUnitType.STRING_PREFIX_LITERAL) {
							alBreaks.add(Integer.valueOf(iRowIndex - 1));
						} else {
							if (intervalCount == (int) seriesGrouping.getGroupingInterval()) {
								alBreaks.add(Integer.valueOf(iRowIndex - 1));
								intervalCount = 0;
							} else {
								intervalCount++;
							}
						}
					}
				}
				oPreviousValue = oValue;
			}
		} else {
			final Iterator it = resultSet.iterator();
			while (it.hasNext()) {
				oValue = ((Object[]) it.next())[iColumnIndex];
				iRowIndex++;
				if (bFirst) {
					bFirst = false;
					oPreviousValue = oValue;
					continue;
				}
				if (compareObjects(oPreviousValue, oValue) != 0) {
					alBreaks.add(Integer.valueOf(iRowIndex - 1));
				}
				oPreviousValue = oValue;
			}
		}

		final int[] ia = new int[alBreaks.size()];
		for (int i = 0; i < alBreaks.size(); i++) {
			ia[i] = alBreaks.get(i).intValue();
		}
		return ia;
	}

	/**
	 * Reset value of grouped column by grouping setting.
	 * 
	 * @param resultSet      row data list.
	 * @param columnIndex    grouped column index.
	 * @param seriesGrouping series grouping setting.
	 */
	private void resetGroupedData(List resultSet, int columnIndex, SeriesGrouping seriesGrouping) {
		if (seriesGrouping.getGroupType() == DataType.DATE_TIME_LITERAL) {
			CDateTime baseReference = null;
			for (Iterator iter = resultSet.iterator(); iter.hasNext();) {
				Object[] oaTuple = (Object[]) iter.next();

				Object obj = oaTuple[columnIndex];

				// ASSIGN IT TO THE FIRST TYPLE'S GROUP EXPR VALUE
				if (obj instanceof CDateTime) {
					baseReference = (CDateTime) obj;
				} else {
					// set as the smallest Date.
					baseReference = new CDateTime(0);
				}

				oaTuple[columnIndex] = baseReference;
			}

		}
	}

	/**
	 * Compares two objects of the same data type
	 * 
	 * @param a Object one
	 * @param b Object two
	 * 
	 * @return The result of the comparison
	 * @since BIRT 2.3
	 */
	public static int compareObjects(Object obja, Object objb, SeriesGrouping sg) {
		Object a = obja;
		Object b = objb;
		// a == b
		if (a == null && b == null) {
			return 0;
		}

		// a < b
		else if (a == null && b != null) {
			return -1;
		}

		// a > b
		else if (a != null && b == null) {
			return 1;
		}

		DataType type = sg.getGroupType();
		if (type == DataType.TEXT_LITERAL) {
			a = GroupingUtil.getGroupedString((String) a, sg.getGroupingUnit(), (int) sg.getGroupingInterval());
			b = GroupingUtil.getGroupedString((String) b, sg.getGroupingUnit(), (int) sg.getGroupingInterval());
		}

		if (a instanceof String) {
			int iC = a.toString().compareTo(b.toString());
			if (iC != 0)
				iC = ((iC < 0) ? -1 : 1);
			return iC;
		} else if (a instanceof Number) {
			final double d1 = ((Number) a).doubleValue();
			final double d2 = ((Number) b).doubleValue();
			return (d1 == d2) ? 0 : (d1 < d2) ? -1 : 1;
		} else if (a instanceof java.util.Date) {
			final long d1 = ((java.util.Date) a).getTime();
			final long d2 = ((java.util.Date) b).getTime();
			return (d1 == d2) ? 0 : (d1 < d2) ? -1 : 1;
		} else if (a instanceof Calendar) {
			final long d1 = ((Calendar) a).getTime().getTime();
			final long d2 = ((Calendar) b).getTime().getTime();
			return (d1 == d2) ? 0 : (d1 < d2) ? -1 : 1;
		} else
		// HANDLE AS STRINGs
		{
			return compareObjects(a.toString(), b.toString());
		}
	}

	/**
	 * Compares two objects of the same data type
	 * 
	 * @param a Object one
	 * @param b Object two
	 * 
	 * @return The result of the comparison
	 */
	public static int compareObjects(Object a, Object b) {
		// a == b
		if (a == null && b == null) {
			return 0;
		}

		// a < b
		else if (a == null && b != null) {
			return -1;
		}

		// a > b
		else if (a != null && b == null) {
			return 1;
		}

		if (a instanceof String) {
			int iC = a.toString().compareTo(b.toString());
			if (iC != 0)
				iC = ((iC < 0) ? -1 : 1);
			return iC;
		} else if (a instanceof Number) {
			final double d1 = ((Number) a).doubleValue();
			final double d2 = ((Number) b).doubleValue();
			return (d1 == d2) ? 0 : (d1 < d2) ? -1 : 1;
		} else if (a instanceof java.util.Date) {
			final long d1 = ((java.util.Date) a).getTime();
			final long d2 = ((java.util.Date) b).getTime();
			return (d1 == d2) ? 0 : (d1 < d2) ? -1 : 1;
		} else if (a instanceof Calendar) {
			final long d1 = ((Calendar) a).getTime().getTime();
			final long d2 = ((Calendar) b).getTime().getTime();
			return (d1 == d2) ? 0 : (d1 < d2) ? -1 : 1;
		} else
		// HANDLE AS STRINGs
		{
			return compareObjects(a.toString(), b.toString());
		}
	}

	/**
	 * GroupingComparator
	 */
	static final class GroupingSorter implements Comparator {

		private int iSortIndex;
		private boolean ascending;
		private Collator collator;

		void sort(List resultSet, int iSortIndex, SortOption so, int[] groupBreaks, Collator collator) {
			if (so == null) {
				return;
			}

			this.iSortIndex = iSortIndex;
			this.ascending = so == SortOption.ASCENDING_LITERAL;
			this.collator = collator;

			if (groupBreaks == null || groupBreaks.length == 0) {
				Collections.sort(resultSet, this);
			} else {
				int totalCount = resultSet.size();
				int startGroupIndex = 0;
				int endGroupIndex;
				List tmpList = new ArrayList();

				// sort each group seperately
				for (int i = 0; i <= groupBreaks.length; i++) {
					if (i == groupBreaks.length) {
						endGroupIndex = totalCount;
					} else {
						endGroupIndex = groupBreaks[i];
					}

					tmpList.clear();

					// extract each group data
					for (int j = startGroupIndex; j < endGroupIndex; j++) {
						tmpList.add(resultSet.get(j));
					}

					Collections.sort(tmpList, this);

					// sort and set back
					for (int k = 0; k < tmpList.size(); k++) {
						resultSet.set(startGroupIndex + k, tmpList.get(k));
					}

					startGroupIndex = endGroupIndex;
				}
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public final int compare(Object o1, Object o2) {
			final Object[] oaTuple1 = (Object[]) o1;
			final Object[] oaTuple2 = (Object[]) o2;
			final Object oC1 = oaTuple1[iSortIndex];
			final Object oC2 = oaTuple2[iSortIndex];

			if (oC1 == null && oC2 == null) {
				return 0;
			}
			if (oC1 == null && oC2 != null) {
				return ascending ? -1 : 1;
			}
			if (oC1 != null && oC2 == null) {
				return ascending ? 1 : -1;
			}

			int ct;
			if (oC1 instanceof String) {
				if (collator == null) {
					ct = ((String) oC1).compareTo((String) oC2);
				} else {
					ct = collator.compare(oC1.toString(), oC2.toString());
				}
			} else {
				ct = ((Comparable) oC1).compareTo(oC2);
			}

			return ascending ? ct : -ct;
		}
	}

	private Collator createCollator(SeriesDefinition sd) {
		// If sort strength is ASCII(-1), then just use default compare of
		// String class to do collator, so here just return null;
		if (sd.isSetSortStrength() && sd.getSortStrength() < 0) {
			return null;
		}
		Collator c = null;
		if (sd.getSortLocale() != null) {
			c = Collator.getInstance(new ULocale(sd.getSortLocale()));
		} else {
			c = Collator.getInstance();
		}

		if (sd.isSetSortStrength()) {
			c.setStrength(sd.getSortStrength());
		}

		return c;
	}
}