/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.internal.datafeed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.AbstractGroupedDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.IActionEvaluator;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.IGroupedDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.emf.common.util.EList;

import com.ibm.icu.util.ULocale;

/**
 * Helper to lookup the index of each data definition in the evaluator data.
 */

public class GroupingLookupHelper {

	public static enum ValueSeriesExprBuilder {
		OLDER_STYLE {

			@Override
			public String buildExpr(Query orthQuery, SeriesDefinition orthSd, SeriesDefinition categorySd)
					throws ChartException {
				return orthQuery.getDefinition();
			}
		},
		TRANSFORMED {

			@Override
			public String buildExpr(Query orthQuery, SeriesDefinition orthSd, SeriesDefinition categorySd)
					throws ChartException {
				if (orthQuery.getDefinition() != null && orthQuery.getDefinition().trim().length() > 0) {
					return ChartUtil.createValueSeriesRowFullExpression(orthQuery, orthSd, categorySd);
				} else {
					return null;
				}
			}
		};

		abstract public String buildExpr(Query orthQuery, SeriesDefinition orthSD, SeriesDefinition categorySD)
				throws ChartException;

		public String[] buildExpr(List<Query> querys, SeriesDefinition orthSD, SeriesDefinition categorySD)
				throws ChartException {
			int size = querys.size();

			String[] exprs = new String[querys.size()];
			for (int i = 0; i < size; i++) {
				exprs[i] = buildExpr(querys.get(i), orthSD, categorySD);
			}
			return exprs;
		}
	}

	private final ValueSeriesExprBuilder valueSeriesExprBuilder;

	private final IDataRowExpressionEvaluator idre;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine/trace"); //$NON-NLS-1$

	private Map<String, Integer> lhmAggExp = ChartUtil.newHashMap();

	private List<String> lstAll = new ArrayList<String>(8);

	private String strBaseAggExp = null;

	private int iLookup = 0;

	private ULocale locale;

	/** The expression index of sort expression on base series. */
	private int fBaseSortExprIndex = -1;

	/**
	 * The expression index of sort key on Y grouping.
	 * 
	 * @since BIRT 2.3
	 **/
	private int fYSortExprIndex = -1;

	/**
	 * Constructor. Finds all data expressions and aggregation expressions in the
	 * chart model in the order and restore them in the lookup list
	 * 
	 * @param cm     chart model
	 * @param iae    IActionEvaluator to get the expressions in triggers
	 * @param locale
	 * @throws ChartException
	 */
	public GroupingLookupHelper(Chart cm, IActionEvaluator iae, RunTimeContext rtc, IDataRowExpressionEvaluator idre)
			throws ChartException {
		this.locale = rtc.getULocale();
		this.idre = idre;
		if (idre instanceof AbstractGroupedDataRowExpressionEvaluator && !rtc.isSharingQuery()) {
			this.valueSeriesExprBuilder = ValueSeriesExprBuilder.TRANSFORMED;
		} else {
			this.valueSeriesExprBuilder = ValueSeriesExprBuilder.OLDER_STYLE;
		}

		if (cm instanceof ChartWithAxes) {
			initRowExpressions((ChartWithAxes) cm, iae);
		} else if (cm instanceof ChartWithoutAxes) {
			initRowExpressions((ChartWithoutAxes) cm, iae);
		}
	}

	/**
	 * Constructor. Restore all expressions in the lookup list.
	 * 
	 * @param dataExps data expressions collection
	 * @param aggExps  aggregation expressions collection
	 * 
	 */
	public GroupingLookupHelper(Collection<String> dataExps, Collection<String> aggExps) {
		this.valueSeriesExprBuilder = ValueSeriesExprBuilder.OLDER_STYLE;
		this.idre = null;
		Iterator<String> dataIterator = dataExps.iterator();
		Iterator<String> aggIterator = aggExps.iterator();
		while (dataIterator.hasNext() && aggIterator.hasNext()) {
			String dataExp = dataIterator.next();
			String aggExp = aggIterator.next();

			lstAll.add(dataExp);
			lhmAggExp.put(generateKey(dataExp, aggExp), Integer.valueOf(iLookup++));
		}
	}

	/**
	 * Gets the list for all data expressions. Only for lookup, and can't be changed
	 * directly.
	 * 
	 * @return the list for all data expressions
	 */
	public List<String> getExpressions() {
		return lstAll;
	}

	/**
	 * Indicates if evaluated expressions include sort keys
	 * 
	 * @return true means sort keys needed
	 */
	private boolean needSortKeys() {
		return !(idre instanceof IGroupedDataRowExpressionEvaluator);
	}

	private String generateKey(String dataExp, String aggExp) {
		if (aggExp == null || "".equals(aggExp)) //$NON-NLS-1$
		{
			return dataExp;
		}
		return dataExp + "_" + aggExp; //$NON-NLS-1$
	}

	/**
	 * Finds the index of base series according to the data expression .
	 * 
	 * @param dataExp the data expression to lookup
	 * @return the index of the data expression in the evaluator data
	 */
	public int findIndexOfBaseSeries(String dataExp) {
		return findIndex(dataExp, ""); //$NON-NLS-1$
	}

	/**
	 * Finds the index of orthogonal series according to the combination of data
	 * expression and aggregation expression.
	 * 
	 * @param dataExp data expression
	 * @param aggExp  aggregation expression. If it's null, will use aggregation
	 *                expression of base series instead
	 * @return the index in the evaluator data
	 */
	public int findIndex(String dataExp, String aggExp) {
		Object value = null;
		if (aggExp == null) {
			value = lhmAggExp.get(generateKey(dataExp, this.strBaseAggExp));
		} else {
			value = lhmAggExp.get(generateKey(dataExp, aggExp));

		}
		return value instanceof Integer ? ((Integer) value).intValue() : -1;
	}

	/**
	 * Finds the index according to the combination of data expression and
	 * aggregation expression in a batch. Note that all data expressions must match
	 * the same aggregation expression.
	 * 
	 * @param dataExpArray data expression array
	 * @param aggExp       aggregation expression
	 * @return the index array in the evaluator data
	 */
	public int[] findBatchIndex(String[] dataExpArray, String aggExp) {
		int[] indexArray = new int[dataExpArray.length];
		for (int i = 0; i < indexArray.length; i++) {
			indexArray[i] = this.findIndex(dataExpArray[i], aggExp);
		}
		return indexArray;
	}

	private boolean addDataExpOfBaseSeries(String dataExp) {
		boolean result = addDataExp(dataExp, ""); //$NON-NLS-1$
		return result;
	}

	private boolean addDataExp(String dataExp, String aggExp) {
		if (dataExp != null && dataExp.trim().length() > 0) {
			String key = generateKey(dataExp, aggExp);
			if (!lhmAggExp.containsKey(key)) {
				lhmAggExp.put(key, Integer.valueOf(iLookup++));
				lstAll.add(dataExp);
			}
			return true;
		}
		return false;
	}

	private void addLookupForBaseSeries(SeriesDefinition baseSD) throws ChartException {
		final Query qBaseSeriesDefinition = baseSD.getQuery();
		String sExpression = qBaseSeriesDefinition.getDefinition();
		if (sExpression != null && sExpression.trim().length() > 0) {
			// Ignore expression for base series definition
			logger.log(ILogger.WARNING,
					Messages.getString("dataprocessor.log.baseSeriesDefn3", sExpression, ULocale.getDefault())); //$NON-NLS-1$
		}

		// PROJECT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES EXPRESSION
		final Series seBase = baseSD.getDesignTimeSeries();
		EList<Query> elBaseSeries = seBase.getDataDefinition();
		if (elBaseSeries.size() != 1) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_BINDING,
					"dataprocessor.exception.FoundDefnAssociatedWithX", //$NON-NLS-1$
					new Object[] { String.valueOf(elBaseSeries.size()) }, Messages.getResourceBundle(this.locale));
		}

		String baseSeriesExpression = elBaseSeries.get(0).getDefinition();
		if (!addDataExpOfBaseSeries(baseSeriesExpression)) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_BINDING,
					"dataprocessor.exception.DefinitionUnspecified", //$NON-NLS-1$
					Messages.getResourceBundle(this.locale));
		}

		// Set base sort key index if it equals base series expression.
		String sortKey = getSortKey(baseSD);
		if (baseSD.isSetSorting() && sortKey == null) {
			sortKey = baseSeriesExpression;
		}
		if (sortKey != null && sortKey.equals(baseSeriesExpression)
				&& baseSD.eIsSet(DataPackage.eINSTANCE.getSeriesDefinition_Sorting())) {
			fBaseSortExprIndex = findIndexOfBaseSeries(baseSeriesExpression);
		}
	}

	private void addLookupForOrthogonalSeries(SeriesDefinition baseSD, EList<SeriesDefinition> lstOrthogonalSDs,
			IActionEvaluator iae) throws ChartException {
		for (int k = 0; k < lstOrthogonalSDs.size(); k++) {
			SeriesDefinition orthoSD = lstOrthogonalSDs.get(k);
			Query qOrthogonalSeriesDefinition = orthoSD.getQuery();
			if (qOrthogonalSeriesDefinition == null) {
				return;
			}

			String strOrthoAgg = getOrthogonalAggregationExpression(orthoSD);

			addDataExp(qOrthogonalSeriesDefinition.getDefinition(), strOrthoAgg);

			// Get sort key of Y grouping.
			String ySortKey = getSortKey(orthoSD);

			Series seOrthogonal = orthoSD.getDesignTimeSeries();
			EList<Query> elOrthogonalSeries = seOrthogonal.getDataDefinition();
			if (elOrthogonalSeries.isEmpty()) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_BINDING,
						"dataprocessor.exception.DefnExpMustAssociateY", //$NON-NLS-1$
						new Object[] { String.valueOf(k), seOrthogonal }, Messages.getResourceBundle(this.locale));
			}

			boolean bAnyQueries = false;
			for (int i = 0; i < elOrthogonalSeries.size(); i++) {
				Query qOrthogonalSeries = elOrthogonalSeries.get(i);
				if (qOrthogonalSeries == null) // NPE PROTECTION
				{
					continue;
				}

				if (addDataExp(valueSeriesExprBuilder.buildExpr(qOrthogonalSeries, orthoSD, baseSD), strOrthoAgg)) {
					bAnyQueries = true;

					// Set base sort index if it equals the value series
					// expression.
					if (fBaseSortExprIndex < 0) {
						String sortExpr = getSortKey(baseSD);
						if (sortExpr != null && sortExpr.equals(qOrthogonalSeries.getDefinition())
								&& baseSD.eIsSet(DataPackage.eINSTANCE.getSeriesDefinition_Sorting())) {
							fBaseSortExprIndex = findIndex(
									valueSeriesExprBuilder.buildExpr(qOrthogonalSeries, orthoSD, baseSD), strOrthoAgg);
						}
					}

					// Get Y sort expression index.
					if (fYSortExprIndex < 0) {
						if (ySortKey != null && ySortKey.equals(qOrthogonalSeries.getDefinition())
								&& orthoSD.eIsSet(DataPackage.eINSTANCE.getSeriesDefinition_Sorting())) {
							fYSortExprIndex = findIndex(
									valueSeriesExprBuilder.buildExpr(qOrthogonalSeries, orthoSD, baseSD), strOrthoAgg);
						}
					}
				}
			}

			if (fYSortExprIndex < 0 && qOrthogonalSeriesDefinition.getDefinition() != null
					&& qOrthogonalSeriesDefinition.getDefinition().trim().length() > 0) {
				if (needSortKeys()) {
					addDataExp(ySortKey, ""); //$NON-NLS-1$
				}
				fYSortExprIndex = findIndexOfBaseSeries(ySortKey);
			}

			if (!bAnyQueries) {
				throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_BINDING,
						"dataprocessor.exception.AtLeastOneDefnExpMustAssociateY", //$NON-NLS-1$
						new Object[] { String.valueOf(k), seOrthogonal }, Messages.getResourceBundle(this.locale));
			}

			// Add orthogonal series trigger expressions.
			String[] triggerExprs = DataProcessor.getSeriesTriggerExpressions(seOrthogonal, iae, baseSD, orthoSD);
			if (triggerExprs != null) {
				for (int t = 0; t < triggerExprs.length; t++) {
					addDataExp(triggerExprs[t], strOrthoAgg);
				}
			}
		}
	}

	private void initRowExpressions(ChartWithoutAxes cwoa, IActionEvaluator iae) throws ChartException {
		EList<SeriesDefinition> elSD = cwoa.getSeriesDefinitions();
		if (elSD.size() != 1) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.DATA_BINDING,
					"dataprocessor.exception.CannotDecipher", //$NON-NLS-1$
					Messages.getResourceBundle(this.locale));
		}

		// PROJECT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES DEFINITION
		SeriesDefinition baseSD = elSD.get(0);
		this.strBaseAggExp = getBaseAggregationExpression(baseSD);
		addLookupForBaseSeries(baseSD);

		// PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL SERIES
		addLookupForOrthogonalSeries(baseSD, baseSD.getSeriesDefinitions(), iae);

		addCommonSortKey(baseSD);
	}

	private void initRowExpressions(ChartWithAxes cwa, IActionEvaluator iae) throws ChartException {
		final Axis axPrimaryBase = cwa.getPrimaryBaseAxes()[0];
		EList<SeriesDefinition> elSD = axPrimaryBase.getSeriesDefinitions();
		if (elSD.size() != 1) {
			throw new ChartException(ChartEnginePlugin.ID, ChartException.GENERATION,
					"dataprocessor.exception.CannotDecipher2", //$NON-NLS-1$
					Messages.getResourceBundle(this.locale));
		}

		// PROJECT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES DEFINITION
		SeriesDefinition baseSD = elSD.get(0);
		this.strBaseAggExp = getBaseAggregationExpression(baseSD);
		addLookupForBaseSeries(baseSD);

		// PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL SERIES
		final Axis[] axaOrthogonal = cwa.getOrthogonalAxes(axPrimaryBase, true);
		for (int j = 0; j < axaOrthogonal.length; j++) {
			addLookupForOrthogonalSeries(baseSD, axaOrthogonal[j].getSeriesDefinitions(), iae);
		}

		// If base sort expression is not base series or value series, directly
		// add the expression.
		addCommonSortKey(baseSD);
	}

	/**
	 * Add common sort expression by the specified expression.
	 * 
	 * @param baseSD
	 */
	private void addCommonSortKey(SeriesDefinition baseSD) {
		if (fBaseSortExprIndex < 0 && baseSD.eIsSet(DataPackage.eINSTANCE.getSeriesDefinition_Sorting())) {
			String sortExpr = getSortKey(baseSD);
			if (sortExpr != null) {
				if (needSortKeys()) {
					addDataExp(sortExpr, ""); //$NON-NLS-1$
				}
				fBaseSortExprIndex = findIndexOfBaseSeries(sortExpr);
			}
		}
	}

	/**
	 * Returns sort key of series definition.
	 * 
	 * @param sd
	 * @return
	 */
	private String getSortKey(SeriesDefinition sd) {
		if (sd == null || sd.getSortKey() == null) {
			return null;
		}

		return sd.getSortKey().getDefinition();
	}

	/**
	 * Simply gets aggregation expressions for the series definitions. If grouping
	 * is not enabled, return null
	 * 
	 * @param sd series definition
	 * @return aggregation expressions for the series definitions, or null if
	 *         grouping is disabled.
	 */
	static String getBaseAggregationExpression(SeriesDefinition sd) {
		SeriesGrouping grouping = sd.getGrouping();
		if (grouping != null && grouping.isEnabled()) {
			return grouping.getAggregateExpression();
		}
		return null;
	}

	/**
	 * Gets aggregation expressions of orthogonal series definition. If base series
	 * doesn't enable grouping, return null. If its own grouping is null, return the
	 * one of base grouping, otherwise, return its own.
	 * 
	 * @param orthoSD orthogonal series definition
	 * @return If base series doesn't enable grouping, return null. If its own
	 *         grouping is null, return the one of base grouping, otherwise, return
	 *         its own.
	 */
	public String getOrthogonalAggregationExpression(SeriesDefinition orthoSD) {
		String strOrthoAgg = null;
		SeriesGrouping grouping = orthoSD.getGrouping();
		if (grouping != null && grouping.isEnabled()) {
			// Set own group
			strOrthoAgg = grouping.getAggregateExpression();
		}

		if ((strOrthoAgg == null || strOrthoAgg.length() == 0) && this.strBaseAggExp != null) {
			strOrthoAgg = strBaseAggExp;
		}
		return strOrthoAgg;
	}

	/**
	 * Returns sort expression of base series, <code>-1</code> means no sort
	 * expression is set for base series.
	 * 
	 * @return
	 * @since 2.3
	 */
	int getBaseSortExprIndex() {
		return fBaseSortExprIndex;
	}

	/**
	 * Returns sort expression of Y grouping, <code>-1</code> means no sort
	 * expression is set for Y grouping.
	 * 
	 * @return
	 * @since BIRT 2.3
	 */
	int getYSortExprIndex() {
		return fYSortExprIndex;
	}

	/**
	 * @return Returns the valueSeriesExprBuilder.
	 */
	public final ValueSeriesExprBuilder getValueSeriesExprBuilder() {
		return valueSeriesExprBuilder;
	}
}
