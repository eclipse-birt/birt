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

package org.eclipse.birt.chart.reportitem.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.PluginSettings.DefaultAggregations;
import org.eclipse.emf.common.util.EList;

public final class QueryUIHelper {

	private static final String BASE_SERIES = Messages.getString("QueryHelper.Text.CategroySeries"); //$NON-NLS-1$

	private static final String ORTHOGONAL_SERIES = Messages.getString("QueryHelper.Text.ValueSeries"); //$NON-NLS-1$

	private static final String X_SERIES = Messages.getString("QueryHelper.Text.XSeries"); //$NON-NLS-1$

	private static final String Y_SERIES = Messages.getString("QueryHelper.Text.YSeries"); //$NON-NLS-1$

	interface QueryValidator {

		void validate(List<String> problems);
	}

	private final Chart cm;
	private IDataServiceProvider dataProvider;

	public QueryUIHelper(Chart cm) {
		this.cm = cm;
	}

	public void enableDataTypeValidator(IDataServiceProvider dataProvider) {
		this.dataProvider = dataProvider;
	}

	public Collection<String> validate() {
		List<String> problems = new ArrayList<>();
		final QueryValidator[] qsqa = cm instanceof ChartWithAxes ? getValidators((ChartWithAxes) cm)
				: getValidators((ChartWithoutAxes) cm);
		for (int i = 0; i < qsqa.length; i++) {
			qsqa[i].validate(problems);
		}
		return problems;
	}

	/**
	 *
	 * @param cwa
	 */
	QueryValidator[] getValidators(ChartWithAxes cwa) {
		final List<QueryValidator> alSeriesQueries = new ArrayList<>(4);
		final Axis axPrimaryBase = cwa.getPrimaryBaseAxes()[0];
		EList<SeriesDefinition> elSD = axPrimaryBase.getSeriesDefinitions();
		if (elSD.size() != 1) {
			return alSeriesQueries.toArray(new QueryDefineValidator[alSeriesQueries.size()]);
		}

		// DON'T CARE ABOUT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES
		// DEFINITION
		SeriesDefinition bsd = elSD.get(0); // ONLY ONE MUST EXIST

		// PROJECT THE QUERY ASSOCIATED WITH THE BASE SERIES EXPRESSION
		final Series seBase = bsd.getDesignTimeSeries();
		EList<Query> elBaseSeries = seBase.getDataDefinition();
		int[] bDataIndex = seBase.getDefinedDataDefinitionIndex();
		Query[] qua = new Query[bDataIndex.length];
		QueryDefineValidator sqd = new QueryDefineValidator(X_SERIES, qua);
		for (int i = 0; i < bDataIndex.length; i++) {
			if (i < elBaseSeries.size()) {
				qua[i] = elBaseSeries.get(bDataIndex[i]);
			} else {
				qua[i] = QueryImpl.create(""); //$NON-NLS-1$
				elBaseSeries.add(qua[i]);
			}
		}
		alSeriesQueries.add(sqd);

		// PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL SERIES'
		// QUERIES
		final Axis[] axaOrthogonal = cwa.getOrthogonalAxes(axPrimaryBase, true);
		for (int j = 0; j < axaOrthogonal.length; j++) {
			elSD = axaOrthogonal[j].getSeriesDefinitions();
			List<Query> queries = new ArrayList<>(elSD.size());
			// DON'T CARE ABOUT SERIES DEFINITION QUERIES
			for (int k = 0; k < elSD.size(); k++) {
				SeriesDefinition vsd = elSD.get(k);
				Series seOrthogonal = vsd.getDesignTimeSeries();
				EList<Query> elOrthogonalSeries = seOrthogonal.getDataDefinition();
				int[] oDataIndex = seOrthogonal.getDefinedDataDefinitionIndex();
				qua = new Query[oDataIndex.length];
				sqd = new QueryDefineValidator(Y_SERIES, qua);
				for (int i = 0; i < oDataIndex.length; i++) {
					if (oDataIndex[i] < elOrthogonalSeries.size()) {
						qua[i] = elOrthogonalSeries.get(oDataIndex[i]);
					} else {
						qua[i] = QueryImpl.create(""); //$NON-NLS-1$
						elOrthogonalSeries.add(qua[i]);
					}
				}
				alSeriesQueries.add(sqd);
				// Validate the first query for complex series data case, such
				// as Bubble
				queries.add(elOrthogonalSeries.get(oDataIndex[0]));
			}

			if (dataProvider != null) {
				alSeriesQueries.add(new DataTypeValidator(bsd, elSD, queries));
			}
		}
		return alSeriesQueries.toArray(new QueryValidator[alSeriesQueries.size()]);
	}

	/**
	 *
	 * @param cwoa
	 */
	QueryValidator[] getValidators(ChartWithoutAxes cwoa) {
		final List<QueryValidator> alSeriesQueries = new ArrayList<>(4);
		EList<SeriesDefinition> elSD = cwoa.getSeriesDefinitions();
		if (elSD.size() != 1) {
			return alSeriesQueries.toArray(new QueryDefineValidator[alSeriesQueries.size()]);
		}

		// DON'T CARE ABOUT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES
		// DEFINITION
		SeriesDefinition bsd = elSD.get(0);

		// PROJECT THE QUERY ASSOCIATED WITH THE BASE SERIES EXPRESSION
		final Series seBase = bsd.getDesignTimeSeries();
		EList<Query> elBaseSeries = seBase.getDataDefinition();
		int[] bDataIndex = seBase.getDefinedDataDefinitionIndex();
		Query[] qua = new Query[bDataIndex.length];
		QueryDefineValidator sqd = new QueryDefineValidator(BASE_SERIES, qua);
		for (int i = 0; i < bDataIndex.length; i++) {
			if (i < elBaseSeries.size()) {
				qua[i] = elBaseSeries.get(bDataIndex[i]);
			} else {
				qua[i] = QueryImpl.create(""); //$NON-NLS-1$
				elBaseSeries.add(qua[i]);
			}
		}
		alSeriesQueries.add(sqd);

		// PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL SERIES
		elSD = bsd.getSeriesDefinitions(); // ALL ORTHOGONAL SERIES DEFINITIONS
		List<Query> queries = new ArrayList<>(elSD.size());
		for (int k = 0; k < elSD.size(); k++) {
			SeriesDefinition vsd = elSD.get(k);
			Series seOrthogonal = vsd.getDesignTimeSeries();
			EList<Query> elOrthogonalSeries = seOrthogonal.getDataDefinition();
			int[] oDataIndex = seOrthogonal.getDefinedDataDefinitionIndex();
			qua = new Query[oDataIndex.length];
			sqd = new QueryDefineValidator(ORTHOGONAL_SERIES, qua);
			for (int i = 0; i < oDataIndex.length; i++) {
				if (oDataIndex[i] < elOrthogonalSeries.size()) {
					qua[i] = elOrthogonalSeries.get(oDataIndex[i]);
				} else {
					qua[i] = QueryImpl.create(""); //$NON-NLS-1$
					elOrthogonalSeries.add(qua[i]);
				}
			}
			alSeriesQueries.add(sqd);
			// Validate the first query for complex series data case, such as
			// Bubble
			queries.add(elOrthogonalSeries.get(oDataIndex[0]));
		}
		if (dataProvider != null) {
			alSeriesQueries.add(new DataTypeValidator(bsd, elSD, queries));
		}
		return alSeriesQueries.toArray(new QueryValidator[alSeriesQueries.size()]);
	}

	/**
	 * SeriesQueries
	 */
	private static final class QueryDefineValidator implements QueryValidator {

		private final String sSeriesType;

		private final Query[] qua;

		QueryDefineValidator(String sSeriesType, Query[] qua) {
			this.sSeriesType = sSeriesType;
			this.qua = qua;
		}

		@Override
		public void validate(List<String> al) {
			if (qua.length == 0) {
				al.add(Messages.getString("QueryHelper.NoDataDefinitionFor", sSeriesType)); //$NON-NLS-1$
			} else {
				Object seriesName = ((Series) qua[0].eContainer()).getSeriesIdentifier();
				String nameExt = ""; //$NON-NLS-1$
				if (seriesName != null && seriesName.toString().length() > 0) {
					nameExt = "(" + seriesName.toString() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
				}

				for (int i = 0; i < qua.length; i++) {
					if (!qua[i].isDefined()) {

						al.add(Messages.getString("QueryHelper.dataDefnUndefined", //$NON-NLS-1$
								sSeriesType + nameExt));
					}
				}
			}
		}
	}

	private final class DataTypeValidator implements QueryValidator {

		private final SeriesDefinition bsd;
		private final List<SeriesDefinition> vsds;
		private final List<Query> queries;

		DataTypeValidator(SeriesDefinition bsd, List<SeriesDefinition> vsds, List<Query> queries) {
			this.bsd = bsd;
			this.vsds = vsds;
			this.queries = queries;
		}

		@Override
		public void validate(List<String> al) {
			int size = vsds.size();
			if (size > 1) {
				DataType firstDataType = null;
				for (int i = 0; i < size; i++) {
					// If query is null, do not need to check data type
					if (!queries.get(i).isDefined()) {
						return;
					}
					String aggFun = null;
					try {
						aggFun = ChartUtil.getFullAggregateExpression(vsds.get(i), bsd, queries.get(i));
					} catch (ChartException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					DataType dataType = null;
					if (DefaultAggregations.COUNT.equals(aggFun) || DefaultAggregations.DISTINCT_COUNT.equals(aggFun)) {
						dataType = DataType.NUMERIC_LITERAL;
					} else {
						dataType = dataProvider.getDataType(queries.get(i).getDefinition());
					}
					// Ignore the computed expression
					if (dataType == null) {
						continue;
					}
					if (firstDataType == null) {
						firstDataType = dataType;
					} else if (firstDataType != dataType) {
						String errorMessage = cm instanceof ChartWithAxes
								? Messages.getString("QueryHelper.InconsitentDataTypesForAxis") //$NON-NLS-1$
								: Messages.getString("QueryHelper.InconsitentDataTypesForValueSeries"); //$NON-NLS-1$
						al.add(errorMessage);
					}
				}
			}
		}
	}

	/**
	 * Returns query definitions of chart.
	 *
	 * @param cm
	 * @return query definition
	 * @see ChartUIConstants#QUERY_CATEGORY
	 * @see ChartUIConstants#QUERY_OPTIONAL
	 * @see ChartUIConstants#QUERY_VALUE
	 * @since 2.3
	 */
	public static Map<String, Query[]> getQueryDefinitionsMap(Chart cm) {
		if (cm instanceof ChartWithAxes) {
			return getQueryDefinitionsMap((ChartWithAxes) cm);
		} else if (cm instanceof ChartWithoutAxes) {
			return getQueryDefinitionsMap((ChartWithoutAxes) cm);
		}
		return Collections.emptyMap();
	}

	/**
	 * Returns query definitions of axes chart.
	 *
	 * @param cm
	 * @return
	 * @since 2.3
	 */
	static Map<String, Query[]> getQueryDefinitionsMap(ChartWithAxes cwa) {
		Map<String, Query[]> queryMap = new HashMap<>();

		final Axis axPrimaryBase = cwa.getPrimaryBaseAxes()[0];
		EList<SeriesDefinition> elSD = axPrimaryBase.getSeriesDefinitions();

		SeriesDefinition bsd = elSD.get(0); // ONLY ONE MUST

		// PROJECT THE QUERY ASSOCIATED WITH THE BASE SERIES EXPRESSION
		final Series seBase = bsd.getDesignTimeSeries();
		EList<Query> elBaseSeries = seBase.getDataDefinition();
		Query categoryQuery = elBaseSeries.get(0); // Only first.
		if (categoryQuery != null) {
			queryMap.put(ChartUIConstants.QUERY_CATEGORY, new Query[] { categoryQuery });
		}

		// PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL SERIES'
		// QUERIES
		Series seOrthogonal;
		EList<Query> elOrthogonalSeries;
		List<Query> yOptionQueryList = new ArrayList<>();
		List<Query> valueQueryList = new ArrayList<>();

		final Axis[] axaOrthogonal = cwa.getOrthogonalAxes(axPrimaryBase, true);
		for (Axis axis : axaOrthogonal) {
			elSD = axis.getSeriesDefinitions();
			for (SeriesDefinition sdef : elSD) {
				Query yOptionQuery = sdef.getQuery();
				if (yOptionQuery != null) {
					yOptionQueryList.add(yOptionQuery);
				}

				seOrthogonal = sdef.getDesignTimeSeries();
				elOrthogonalSeries = seOrthogonal.getDataDefinition();
				for (Query q : elOrthogonalSeries) {
					if (q != null) {
						valueQueryList.add(q);
					}
				}
			}
		}

		if (yOptionQueryList.size() > 0) {
			Query[] q = {};
			queryMap.put(ChartUIConstants.QUERY_OPTIONAL, yOptionQueryList.toArray(q));
		}

		if (valueQueryList.size() > 0) {
			Query[] q = {};
			queryMap.put(ChartUIConstants.QUERY_VALUE, valueQueryList.toArray(q));
		}

		return queryMap;
	}

	/**
	 * Returns query definitions of non-axes chart.
	 *
	 * @param cm
	 * @return
	 */
	static Map<String, Query[]> getQueryDefinitionsMap(ChartWithoutAxes cwoa) {
		Map<String, Query[]> queryMap = new HashMap<>();

		EList<SeriesDefinition> elSD = cwoa.getSeriesDefinitions();

		SeriesDefinition bsd = elSD.get(0);

		// PROJECT THE QUERY ASSOCIATED WITH THE BASE SERIES EXPRESSION
		final Series seBase = bsd.getDesignTimeSeries();
		EList<Query> elBaseSeries = seBase.getDataDefinition();
		Query categoryQuery = elBaseSeries.get(0);
		if (categoryQuery != null) {
			queryMap.put(ChartUIConstants.QUERY_CATEGORY, new Query[] { categoryQuery });
		}

		// PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL SERIES

		List<Query> yOptionQueryList = new ArrayList<>();
		List<Query> valueQueryList = new ArrayList<>();

		Series seOrthogonal;
		EList<Query> elOrthogonalSeries;
		elSD = bsd.getSeriesDefinitions(); // ALL ORTHOGONAL SERIES DEFINITIONS
		for (SeriesDefinition sdef : elSD) {

			Query yOptionQuery = sdef.getQuery();
			if (yOptionQuery != null) {
				yOptionQueryList.add(yOptionQuery);
			}

			seOrthogonal = sdef.getDesignTimeSeries();
			elOrthogonalSeries = seOrthogonal.getDataDefinition();
			for (Query q : elOrthogonalSeries) {
				if (q != null) {
					valueQueryList.add(q);
				}
			}

		}

		if (yOptionQueryList.size() > 0) {
			Query[] q = {};
			queryMap.put(ChartUIConstants.QUERY_OPTIONAL, yOptionQueryList.toArray(q));
		}

		if (valueQueryList.size() > 0) {
			Query[] q = {};
			queryMap.put(ChartUIConstants.QUERY_VALUE, valueQueryList.toArray(q));
		}

		return queryMap;
	}
}
