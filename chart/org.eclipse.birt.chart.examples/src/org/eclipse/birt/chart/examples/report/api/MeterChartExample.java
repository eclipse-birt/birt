/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.examples.report.api;

import java.io.File;
import java.io.IOException;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.DialChartImpl;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.impl.DialSeriesImpl;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.IDesignEngineFactory;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;

import com.ibm.icu.util.ULocale;

public class MeterChartExample {

	ReportDesignHandle reportDesignHandle = null;

	ElementFactory elementFactory = null;

	ComputedColumn cs1, cs2, cs3 = null;

	public static void main(String[] args) throws SemanticException, IOException {
		new MeterChartExample().createReport();
	}

	private IDesignEngine getDesignEngine() {
		DesignConfig config = new DesignConfig();
		try {
			Platform.startup(config);
		} catch (BirtException e) {
			e.printStackTrace();
		}

		Object factory = Platform.createFactoryObject(IDesignEngineFactory.EXTENSION_DESIGN_ENGINE_FACTORY);

		return ((IDesignEngineFactory) factory).createDesignEngine(config);
	}

	void createReport() throws SemanticException, IOException {
		// A session handle for all open reports
		SessionHandle session = getDesignEngine().newSessionHandle((ULocale) null);

		// Create a new report
		reportDesignHandle = session.createDesign();

		// Element factory is used to create instances of BIRT elements.
		elementFactory = reportDesignHandle.getElementFactory();

		createMasterPages();
		createDataSources();
		createDataSets();
		createBody();

		String outputPath = "output";//$NON-NLS-1$
		File outputFolder = new File(outputPath);
		if (!outputFolder.exists() && !outputFolder.mkdir()) {
			throw new IOException("Can not create the output folder");//$NON-NLS-1$
		}
		reportDesignHandle.saveAs(outputPath + "/" + "MeterChartExample.rptdesign");//$NON-NLS-1$//$NON-NLS-2$

		Platform.shutdown();
	}

	private void createDataSources() throws SemanticException {
		ScriptDataSourceHandle dataSourceHandle = elementFactory.newScriptDataSource("Data Source");//$NON-NLS-1$
		reportDesignHandle.getDataSources().add(dataSourceHandle);
	}

	private void createDataSets() throws SemanticException {
		// Data Set
		ScriptDataSetHandle dataSetHandle = elementFactory.newScriptDataSet("Data Set");//$NON-NLS-1$
		dataSetHandle.setDataSource("Data Source");//$NON-NLS-1$

		// Set open( ) in code
		dataSetHandle.setOpen("i=0;"//$NON-NLS-1$
				+ "sourcedata = new Array( new Array(3), new Array(3), new Array(3),  new Array(3));"//$NON-NLS-1$

				+ "sourcedata[0][0] = 10; "//$NON-NLS-1$
				+ "sourcedata[0][1] = \"Ice Bella\";"//$NON-NLS-1$
				+ "sourcedata[0][2] = 304;"//$NON-NLS-1$

				+ "sourcedata[1][0] = 10; "//$NON-NLS-1$
				+ "sourcedata[1][1] = \"Nola Dicci\";"//$NON-NLS-1$
				+ "sourcedata[1][2] = 258;"//$NON-NLS-1$

				+ "sourcedata[2][0] = 11; "//$NON-NLS-1$
				+ "sourcedata[2][1] = \"Ice Bella\";"//$NON-NLS-1$
				+ "sourcedata[2][2] = 202;"//$NON-NLS-1$

				+ "sourcedata[3][0] = 11; "//$NON-NLS-1$
				+ "sourcedata[3][1] = \"Nola Dicci\";"//$NON-NLS-1$
				+ "sourcedata[3][2] = 181;");//$NON-NLS-1$

		// Set fetch( ) in code
		dataSetHandle.setFetch("if ( i < 4 ){"//$NON-NLS-1$
				+ "row[\"Month\"] = sourcedata[i][0];"//$NON-NLS-1$
				+ "row[\"Product\"] = sourcedata[i][1];"//$NON-NLS-1$
				+ "row[\"Amount\"] = sourcedata[i][2];"//$NON-NLS-1$
				+ "i++;"//$NON-NLS-1$
				+ "return true;}" + "else return false;");//$NON-NLS-1$//$NON-NLS-2$

		// Set computed columns
		cs1 = StructureFactory.createComputedColumn();
		cs1.setName("Month");//$NON-NLS-1$
		cs1.setExpression("row[\"Month\"]");//$NON-NLS-1$
		cs1.setDataType("integer");//$NON-NLS-1$

		cs2 = StructureFactory.createComputedColumn();
		cs2.setName("Product");//$NON-NLS-1$
		cs2.setExpression("row[\"Product\"]");//$NON-NLS-1$
		cs2.setDataType("string");//$NON-NLS-1$

		cs3 = StructureFactory.createComputedColumn();
		cs3.setName("Amount");//$NON-NLS-1$
		cs3.setExpression("row[\"Amount\"]");//$NON-NLS-1$
		cs3.setDataType("integer");//$NON-NLS-1$

		PropertyHandle computedSet = dataSetHandle.getPropertyHandle(ScriptDataSetHandle.COMPUTED_COLUMNS_PROP);
		computedSet.addItem(cs1);
		computedSet.addItem(cs2);
		computedSet.addItem(cs3);

		reportDesignHandle.getDataSets().add(dataSetHandle);
	}

	private void createMasterPages() throws ContentException, NameException {
		DesignElementHandle simpleMasterPage = elementFactory.newSimpleMasterPage("Master Page");//$NON-NLS-1$
		reportDesignHandle.getMasterPages().add(simpleMasterPage);
	}

	private void createBody() throws SemanticException {
		ExtendedItemHandle eih = elementFactory.newExtendedItem(null, "Chart");//$NON-NLS-1$

		try {
			eih.setHeight("288pt");//$NON-NLS-1$
			eih.setWidth("252pt");//$NON-NLS-1$
			eih.setProperty(ExtendedItemHandle.DATA_SET_PROP, "Data Set");//$NON-NLS-1$
		} catch (SemanticException e) {
			e.printStackTrace();
		}

		PropertyHandle computedSet = eih.getColumnBindings();
		cs1.setExpression("dataSetRow[\"Month\"]");//$NON-NLS-1$
		computedSet.addItem(cs1);
		cs2.setExpression("dataSetRow[\"Product\"]");//$NON-NLS-1$
		computedSet.addItem(cs2);
		cs3.setExpression("dataSetRow[\"Amount\"]");//$NON-NLS-1$
		computedSet.addItem(cs3);

		reportDesignHandle.getBody().add(eih);

		try {
			// Add chart instance to IReportItem
			eih.getReportItem().setProperty("chart.instance", createMeterChart());//$NON-NLS-1$
		} catch (ExtendedElementException e) {
			e.printStackTrace();
		}
	}

	private Chart createMeterChart() {
		DialChart dChart = (DialChart) DialChartImpl.create();
		dChart.setDialSuperimposition(true);
		dChart.setType("Meter Chart");//$NON-NLS-1$
		dChart.setSubType("Superimposed Meter Chart");//$NON-NLS-1$
		dChart.getBlock().setBounds(BoundsImpl.create(0, 0, 252, 288));
		dChart.getLegend().setItemType(LegendItemType.SERIES_LITERAL);

		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("A, B, C");//$NON-NLS-1$
		sd.getBaseSampleData().add(sdBase);

		OrthogonalSampleData sdOrthogonal = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sdOrthogonal.setDataSetRepresentation("5, 4, 12");//$NON-NLS-1$
		sdOrthogonal.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal);

		dChart.setSampleData(sd);

		Series seCategory = SeriesImpl.create();
		Query query1 = QueryImpl.create("row[\"Product\"]");//$NON-NLS-1$
		seCategory.getDataDefinition().add(query1);

		SeriesDefinition series = SeriesDefinitionImpl.create();
		series.getSeries().add(seCategory);
		dChart.getSeriesDefinitions().add(series);

		DialSeries seDial = (DialSeries) DialSeriesImpl.create();
		Query query2 = QueryImpl.create("row[\"Amount\"]");//$NON-NLS-1$
		seDial.getDataDefinition().add(query2);

		SeriesDefinition seGroup = SeriesDefinitionImpl.create();
		Query query3 = QueryImpl.create("row[\"Month\"]");//$NON-NLS-1$
		seGroup.setQuery(query3);
		seGroup.getSeriesPalette().shift(-2);
		series.getSeriesDefinitions().add(seGroup);
		seGroup.getSeries().add(seDial);

		return dChart;
	}
}
