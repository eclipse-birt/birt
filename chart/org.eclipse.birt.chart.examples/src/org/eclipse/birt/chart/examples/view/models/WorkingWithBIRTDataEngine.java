/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.examples.view.models;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.chart.examples.ChartExamplesPlugin;
import org.eclipse.birt.chart.examples.view.util.GroupedRowExpressionsEvaluator;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.attribute.IntersectionType;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.TickStyle;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.data.Constants;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.OdaDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

import com.ibm.icu.util.ULocale;

/**
 * The example demonstrates how chart works with ODA/DtE(BIRT data engine) to
 * get grouped/aggregated data set, the ODA/DtE is responsible to execute data
 * query and grouping/aggregation, it returns a grouped data set to chart and
 * chart retrieve data without grouping/aggregation by chart-self.
 * <p>
 * In the example, it only uses flat file as data source, actually it supports
 * any JDBC data source, referring to Data Engine to get detail information.
 * 
 * @since 2.3
 */
public class WorkingWithBIRTDataEngine {
	private final Map<String, String> expressionMap = new HashMap<String, String>();

	/**
	 * Create runtime chart model and bind data.
	 * 
	 * @throws ChartException
	 */
	public static final Chart createWorkingWithBIRTDataEngine() throws ChartException {
		WorkingWithBIRTDataEngine wwbde = new WorkingWithBIRTDataEngine();
		String[] expressions = new String[2];
		expressions[0] = ExpressionUtil.createRowExpression(FlatFileDataSource.COLUMN_COUNTRY);
		expressions[1] = ExpressionUtil.createRowExpression(FlatFileDataSource.COLUMN_CUSTOMERNUMBER);

		ChartWithAxes cwaBar = wwbde.createChartModel(expressions);

		cwaBar = wwbde.bindData(cwaBar, expressions);

		return cwaBar;
	}

	/**
	 * Create chart model.
	 * 
	 * @param expressions expressions are used to set category series and value
	 *                    series.
	 * @return
	 */
	private ChartWithAxes createChartModel(String[] expressions) {
		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setType("Bar Chart"); //$NON-NLS-1$
		cwaBar.setSubType("Side-by-side"); //$NON-NLS-1$
		// Plot
		cwaBar.getBlock().setBackground(ColorDefinitionImpl.WHITE());
		cwaBar.getBlock().getOutline().setVisible(true);
		Plot p = cwaBar.getPlot();
		p.getClientArea().setBackground(ColorDefinitionImpl.create(255, 255, 225));

		// Title
		cwaBar.getTitle().getLabel().getCaption().setValue("Working with BIRT Data Engine"); //$NON-NLS-1$

		// Legend
		Legend lg = cwaBar.getLegend();
		lg.setItemType(LegendItemType.CATEGORIES_LITERAL);

		// X-Axis
		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];

		xAxisPrimary.setType(AxisType.TEXT_LITERAL);
		xAxisPrimary.getMajorGrid().setTickStyle(TickStyle.BELOW_LITERAL);
		xAxisPrimary.getOrigin().setType(IntersectionType.MIN_LITERAL);

		// Y-Axis
		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getMajorGrid().setTickStyle(TickStyle.LEFT_LITERAL);
		yAxisPrimary.setType(AxisType.LINEAR_LITERAL);
		yAxisPrimary.getLabel().getCaption().getFont().setRotation(90);
		yAxisPrimary.getTitle().setVisible(true);
		yAxisPrimary.getTitle().getCaption().setValue("Customer Amount"); //$NON-NLS-1$
		yAxisPrimary.getTitle().getCaption().setColor(ColorDefinitionImpl.GREEN());

		// X-Series
		Series seCategory = SeriesImpl.create();
		// seCategory.setDataSet( categoryValues );

		// Set category expression.
		seCategory.getDataDefinition().add(QueryImpl.create(expressions[0]));

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeriesPalette().shift(0);

		// Set default grouping.
		SeriesGrouping grouping = sdX.getGrouping();
		grouping.setEnabled(true);
		grouping.setGroupType(DataType.TEXT_LITERAL);
		grouping.setGroupingUnit(GroupingUnitType.STRING_LITERAL);
		grouping.setGroupingInterval(0);
		grouping.setAggregateExpression("Count"); // Set Count aggregation. //$NON-NLS-1$

		xAxisPrimary.getSeriesDefinitions().add(sdX);
		sdX.getSeries().add(seCategory);

		// Y-Series
		BarSeries bs1 = (BarSeries) BarSeriesImpl.create();
		bs1.getDataDefinition().add(QueryImpl.create(expressions[1]));
		bs1.getLabel().setVisible(true);
		bs1.setLabelPosition(Position.INSIDE_LITERAL);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(bs1);
		return cwaBar;
	}

	/**
	 * Binds data into chart model.
	 * 
	 * @param cwaBar
	 * @return
	 * @throws ChartException
	 */
	private ChartWithAxes bindData(ChartWithAxes cwaBar, String[] expressions) throws ChartException {

		RunTimeContext context = new RunTimeContext();
		context.setULocale(ULocale.getDefault());

		IDataRowExpressionEvaluator evaluator;
		try {
			// Create row expression evaluator for chart doing data binding.
			evaluator = prepareRowExpressionEvaluator(cwaBar, expressions);

			// Binding data.
			Generator.instance().bindData(evaluator, cwaBar, context);
		} catch (BirtException e) {
			throw new ChartException(ChartExamplesPlugin.ID, ChartException.DATA_BINDING, e);
		}
		return cwaBar;
	}

	/**
	 * Uses BIRT data engine to do query and wraps data with
	 * <code>IDataRowExpressionEvaluator</code> for chart doing data binding.
	 * 
	 * @return
	 * @throws BirtException
	 */
	private IDataRowExpressionEvaluator prepareRowExpressionEvaluator(ChartWithAxes chart, String[] expressions)
			throws BirtException {

		// Initialize data source and data set.
		OdaDataSourceDesign odaDataSource = newDataSource();
		OdaDataSetDesign odaDataSet = newDataSet(odaDataSource);

		// Create query definition.
		QueryDefinition query = createQueryDefinition(chart, odaDataSet);

		// Create data engine and execute query.
		DataEngine dataEngine = newDataEngine();
		dataEngine.defineDataSource(odaDataSource);
		dataEngine.defineDataSet(odaDataSet);
		IPreparedQuery preparedQuery = dataEngine.prepare(query);

		IQueryResults queryResults = preparedQuery.execute(null);

		// Create row expression evaluator.
		return new GroupedRowExpressionsEvaluator(queryResults.getResultIterator(), true) {
			public Object evaluate(String expression) {
				String bindingName = expressionMap.get(expression);
				if (bindingName != null)
					return super.evaluate(bindingName);
				return super.evaluate(expression);
			}
		};
	}

	/**
	 * Create query definition.
	 * 
	 * @param chart
	 * @param odaDataSet
	 * @return
	 * @throws ChartException
	 */
	private QueryDefinition createQueryDefinition(Chart chart, OdaDataSetDesign odaDataSet) throws ChartException {

		QueryDefinition queryDefn = new QueryDefinition();
		queryDefn.setDataSetName(odaDataSet.getName());

		try {
			initDefaultBindings(queryDefn);

			SeriesDefinition baseSD = ChartUtil.getBaseSeriesDefinitions(chart).get(0);
			SeriesDefinition orthSD = ChartUtil.getAllOrthogonalSeriesDefinitions(chart).get(0);
			String categoryExpr = baseSD.getDesignTimeSeries().getDataDefinition().get(0).getDefinition();

			// Add group definitions and aggregation binding.
			String groupName = "Group_Country"; //$NON-NLS-1$
			GroupDefinition gd = new GroupDefinition(groupName);
			gd.setKeyExpression(categoryExpr);
			gd.setInterval(IGroupDefinition.NO_INTERVAL);
			gd.setIntervalRange(0);
			queryDefn.addGroup(gd);

			// Add expression bindings.
			Binding colBinding = new Binding(categoryExpr);
			colBinding.setExpression(new ScriptExpression(categoryExpr));
			queryDefn.addBinding(colBinding);

			String valueBinding = ChartUtil.generateBindingNameOfValueSeries(
					orthSD.getDesignTimeSeries().getDataDefinition().get(0), orthSD, baseSD, true);
			colBinding = new Binding(valueBinding);
			colBinding.setExpression(null);
			colBinding.setAggrFunction(IBuildInAggregation.TOTAL_COUNT_FUNC);
			colBinding.addAggregateOn(groupName);
			colBinding.addArgument(
					new ScriptExpression(orthSD.getDesignTimeSeries().getDataDefinition().get(0).getDefinition()));

			expressionMap.put(ExpressionUtil.createRowExpression(valueBinding), valueBinding);
			queryDefn.addBinding(colBinding);
		} catch (DataException e) {
			throw new ChartException(ChartExamplesPlugin.ID, ChartException.DATA_BINDING, e);
		}

		return queryDefn;
	}

	/**
	 * Initialize default column bindings for original columns.
	 * 
	 * @param queryDefn
	 * @throws DataException
	 */
	private void initDefaultBindings(QueryDefinition queryDefn) throws DataException {

		Binding colBinding = new Binding(FlatFileDataSource.COLUMN_COUNTRY);
		colBinding.setExpression(
				new ScriptExpression(ExpressionUtil.createDataSetRowExpression(FlatFileDataSource.COLUMN_COUNTRY)));
		queryDefn.addBinding(colBinding);

		colBinding = new Binding(FlatFileDataSource.COLUMN_CUSTOMERNUMBER);
		colBinding.setExpression(new ScriptExpression(
				ExpressionUtil.createDataSetRowExpression(FlatFileDataSource.COLUMN_CUSTOMERNUMBER)));
		queryDefn.addBinding(colBinding);
	}

	/**
	 * Create a new data engine.
	 * 
	 * @return
	 * @throws BirtException
	 */
	private DataEngine newDataEngine() throws BirtException {
		DataEngineContext context = DataEngineContext.newInstance(DataEngineContext.DIRECT_PRESENTATION,
				new ScriptContext(), (IDocArchiveReader) null, (IDocArchiveWriter) null, (ClassLoader) null);
		// context.setTmpdir( this.getTempDir( ) );
		DataEngine myDataEngine = DataEngine.newDataEngine(context);
		return myDataEngine;
	}

	/**
	 * Create a new data set.
	 * 
	 * @param dataSourceDesign
	 * @return
	 */
	private OdaDataSetDesign newDataSet(OdaDataSourceDesign dataSourceDesign) {
		OdaDataSetDesign dataSet = new OdaDataSetDesign("Data Set1"); //$NON-NLS-1$

		dataSet.setDataSource(dataSourceDesign.getName());
		dataSet.setExtensionID(FlatFileDataSource.DATA_SET_TYPE);
		dataSet.setQueryText(FlatFileDataSource.QUERY);
		return dataSet;
	}

	/**
	 * Create a new data source.
	 * 
	 * @return
	 * @throws BirtException
	 */
	private OdaDataSourceDesign newDataSource() throws BirtException {
		OdaDataSourceDesign dataSource = new OdaDataSourceDesign("Data Source1"); //$NON-NLS-1$
		dataSource.setExtensionID(FlatFileDataSource.DATA_SOURCE_TYPE);
		dataSource.addPrivateProperty("HOME", FlatFileDataSource.HOME); //$NON-NLS-1$
		dataSource.addPrivateProperty("CHARSET", FlatFileDataSource.CHARSET); //$NON-NLS-1$
		dataSource.addPrivateProperty("DELIMTYPE", //$NON-NLS-1$
				FlatFileDataSource.DELIMTYPE);
		dataSource.addPrivateProperty("INCLTYPELINE", //$NON-NLS-1$
				FlatFileDataSource.INCLTYPELINE);
		dataSource.addPrivateProperty(Constants.ODA_PROP_CONFIGURATION_ID,
				dataSource.getExtensionID() + Constants.ODA_PROP_CONFIG_KEY_SEPARATOR + dataSource.getName());

		return dataSource;
	}

	/**
	 * Resource of flat file data.
	 */
	static class FlatFileDataSource {

		static final String DATA_SOURCE_TYPE = "org.eclipse.datatools.connectivity.oda.flatfile"; //$NON-NLS-1$
		static final String DATA_SET_TYPE = "org.eclipse.datatools.connectivity.oda.flatfile.dataSet"; //$NON-NLS-1$
		static final String CHARSET = "UTF-8"; //$NON-NLS-1$
		static final String DELIMTYPE = "TAB"; //$NON-NLS-1$
		static final String INCLTYPELINE = "NO"; //$NON-NLS-1$
		static String HOME = null;
		static {
			Bundle bundle = Platform.getBundle(ChartExamplesPlugin.ID);
			Path path = new Path("data"); //$NON-NLS-1$
			URL fileURL = FileLocator.find(bundle, path, null);

			try {
				HOME = FileLocator.toFileURL(fileURL).getPath();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		static final String DATA_FILE = "customers_data.csv"; //$NON-NLS-1$

		static final String COLUMN_COUNTRY = "COUNTRY"; //$NON-NLS-1$

		static final String COLUMN_CUSTOMERNUMBER = "CUSTOMERNUMBER"; //$NON-NLS-1$

		static final String QUERY = "select \"CUSTOMERNUMBER\", \"CUSTOMERNAME\", \"CONTACTLASTNAME\", \"CONTACTFIRSTNAME\", \"PHONE\", \"ADDRESSLINE1\", \"ADDRESSLINE2\", \"CITY\", \"STATE\", \"POSTALCODE\", \"COUNTRY\", \"SALESREPEMPLOYEENUMBER\", \"CREDITLIMIT\" from " //$NON-NLS-1$
				+ DATA_FILE
				+ ": {\"CUSTOMERNUMBER\",\"CUSTOMERNUMBER\",STRING;\"CUSTOMERNAME\",\"CUSTOMERNAME\",STRING;\"CONTACTLASTNAME\",\"CONTACTLASTNAME\",STRING;\"CONTACTFIRSTNAME\",\"CONTACTFIRSTNAME\",STRING;\"PHONE\",\"PHONE\",STRING;\"ADDRESSLINE1\",\"ADDRESSLINE1\",STRING;\"ADDRESSLINE2\",\"ADDRESSLINE2\",STRING;\"CITY\",\"CITY\",STRING;\"STATE\",\"STATE\",STRING;\"POSTALCODE\",\"POSTALCODE\",STRING;\"COUNTRY\",\"COUNTRY\",STRING;\"SALESREPEMPLOYEENUMBER\",\"SALESREPEMPLOYEENUMBER\",STRING;\"CREDITLIMIT\",\"CREDITLIMIT\",STRING}"; //$NON-NLS-1$
	}

}
