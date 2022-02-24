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
import java.util.ArrayList;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaDateFormatSpecifierImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.model.type.impl.StockSeriesImpl;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.IDesignEngineFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import com.ibm.icu.util.ULocale;

public class StockReport {

	ReportDesignHandle reportDesignHandle = null;

	ElementFactory elementFactory = null;

	ComputedColumn cs1, cs2, cs3, cs4, cs5, cs6 = null;

	public static void main(String[] args) throws SemanticException, IOException {
		new StockReport().createReport();
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
		reportDesignHandle.saveAs(outputPath + "/" + "StockAnalysis.rptdesign");//$NON-NLS-1$//$NON-NLS-2$

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
				+ "sourcedata = new Array( new Array(6), new Array(6), new Array(6), new Array(6),"//$NON-NLS-1$
				+ "new Array(6), new Array(6), new Array(6), new Array(6));"//$NON-NLS-1$
				+ "sourcedata[0][0] = \"3/1/2005\"; "//$NON-NLS-1$
				+ "sourcedata[0][1] = 2.77;"//$NON-NLS-1$
				+ "sourcedata[0][2] = 2.73;"//$NON-NLS-1$
				+ "sourcedata[0][3] = 2.69; "//$NON-NLS-1$
				+ "sourcedata[0][4] = 2.71;"//$NON-NLS-1$
				+ "sourcedata[0][5] = 341900;"//$NON-NLS-1$

				+ "sourcedata[1][0] = \"3/2/2005\"; "//$NON-NLS-1$
				+ "sourcedata[1][1] = 2.8;"//$NON-NLS-1$
				+ "sourcedata[1][2] = 2.64;"//$NON-NLS-1$
				+ "sourcedata[1][3] = 2.6; "//$NON-NLS-1$
				+ "sourcedata[1][4] = 2.71;"//$NON-NLS-1$
				+ "sourcedata[1][5] = 249900;"//$NON-NLS-1$

				+ "sourcedata[2][0] = \"3/3/2005\"; "//$NON-NLS-1$
				+ "sourcedata[2][1] = 2.6;"//$NON-NLS-1$
				+ "sourcedata[2][2] = 2.28;"//$NON-NLS-1$
				+ "sourcedata[2][3] = 2.13; "//$NON-NLS-1$
				+ "sourcedata[2][4] = 2.59;"//$NON-NLS-1$
				+ "sourcedata[2][5] = 394800;"//$NON-NLS-1$

				+ "sourcedata[3][0] = \"3/4/2005\"; "//$NON-NLS-1$
				+ "sourcedata[3][1] = 2.87;"//$NON-NLS-1$
				+ "sourcedata[3][2] = 2.87;"//$NON-NLS-1$
				+ "sourcedata[3][3] = 2.03; "//$NON-NLS-1$
				+ "sourcedata[3][4] = 2.21;"//$NON-NLS-1$
				+ "sourcedata[3][5] = 358200;"//$NON-NLS-1$

				+ "sourcedata[4][0] = \"3/5/2005\"; "//$NON-NLS-1$
				+ "sourcedata[4][1] = 2.48;"//$NON-NLS-1$
				+ "sourcedata[4][2] = 2.26;"//$NON-NLS-1$
				+ "sourcedata[4][3] = 2.16; "//$NON-NLS-1$
				+ "sourcedata[4][4] = 2.39;"//$NON-NLS-1$
				+ "sourcedata[4][5] = 339000;"//$NON-NLS-1$

				+ "sourcedata[5][0] = \"3/6/2005\"; "//$NON-NLS-1$
				+ "sourcedata[5][1] = 2.98;"//$NON-NLS-1$
				+ "sourcedata[5][2] = 2.86;"//$NON-NLS-1$
				+ "sourcedata[5][3] = 2.04; "//$NON-NLS-1$
				+ "sourcedata[5][4] = 2.19;"//$NON-NLS-1$
				+ "sourcedata[5][5] = 221000;"//$NON-NLS-1$

				+ "sourcedata[6][0] = \"3/7/2005\"; "//$NON-NLS-1$
				+ "sourcedata[6][1] = 2.87;"//$NON-NLS-1$
				+ "sourcedata[6][2] = 2.17;"//$NON-NLS-1$
				+ "sourcedata[6][3] = 2.14; "//$NON-NLS-1$
				+ "sourcedata[6][4] = 2.62;"//$NON-NLS-1$
				+ "sourcedata[6][5] = 183600;"//$NON-NLS-1$

				+ "sourcedata[7][0] = \"3/8/2005\"; "//$NON-NLS-1$
				+ "sourcedata[7][1] = 2.8;"//$NON-NLS-1$
				+ "sourcedata[7][2] = 2.78;"//$NON-NLS-1$
				+ "sourcedata[7][3] = 2.65; "//$NON-NLS-1$
				+ "sourcedata[7][4] = 2.66;"//$NON-NLS-1$
				+ "sourcedata[7][5] = 194800;");//$NON-NLS-1$

		// Set fetch( ) in code
		dataSetHandle.setFetch("if ( i < 8 ){"//$NON-NLS-1$
				+ "row[\"Date\"] = sourcedata[i][0];"//$NON-NLS-1$
				+ "row[\"High\"] = sourcedata[i][1];"//$NON-NLS-1$
				+ "row[\"Close\"] = sourcedata[i][2];"//$NON-NLS-1$
				+ "row[\"Low\"] = sourcedata[i][3];"//$NON-NLS-1$
				+ "row[\"Open\"] = sourcedata[i][4];"//$NON-NLS-1$
				+ "row[\"Volume\"] = sourcedata[i][5];" + "i++;"//$NON-NLS-1$//$NON-NLS-2$
				+ "return true;}" + "else return false;");//$NON-NLS-1$//$NON-NLS-2$

		// Set computed columns
		cs1 = StructureFactory.createComputedColumn();
		cs1.setName("Date");//$NON-NLS-1$
		cs1.setExpression("row[\"Date\"]");//$NON-NLS-1$
		cs1.setDataType("date-time");//$NON-NLS-1$

		cs2 = StructureFactory.createComputedColumn();
		cs2.setName("High");//$NON-NLS-1$
		cs2.setExpression("row[\"High\"]");//$NON-NLS-1$
		cs2.setDataType("float");//$NON-NLS-1$

		cs3 = StructureFactory.createComputedColumn();
		cs3.setName("Close");//$NON-NLS-1$
		cs3.setExpression("row[\"Close\"]");//$NON-NLS-1$
		cs3.setDataType("float");//$NON-NLS-1$

		cs4 = StructureFactory.createComputedColumn();
		cs4.setName("Low");//$NON-NLS-1$
		cs4.setExpression("row[\"Low\"]");//$NON-NLS-1$
		cs4.setDataType("float");//$NON-NLS-1$

		cs5 = StructureFactory.createComputedColumn();
		cs5.setName("Open");//$NON-NLS-1$
		cs5.setExpression("row[\"Open\"]");//$NON-NLS-1$
		cs5.setDataType("float");//$NON-NLS-1$

		cs6 = StructureFactory.createComputedColumn();
		cs6.setName("Volume");//$NON-NLS-1$
		cs6.setExpression("row[\"Volume\"]");//$NON-NLS-1$
		cs6.setDataType("integer");//$NON-NLS-1$

		PropertyHandle computedSet = dataSetHandle.getPropertyHandle(ScriptDataSetHandle.COMPUTED_COLUMNS_PROP);
		computedSet.addItem(cs1);
		computedSet.addItem(cs2);
		computedSet.addItem(cs3);
		computedSet.addItem(cs4);
		computedSet.addItem(cs5);
		computedSet.addItem(cs6);

		reportDesignHandle.getDataSets().add(dataSetHandle);
	}

	private void createMasterPages() throws ContentException, NameException {
		DesignElementHandle simpleMasterPage = elementFactory.newSimpleMasterPage("Master Page");//$NON-NLS-1$
		try {
			simpleMasterPage.setProperty(MasterPageHandle.LEFT_MARGIN_PROP, "0.2in");//$NON-NLS-1$
			simpleMasterPage.setProperty(MasterPageHandle.RIGHT_MARGIN_PROP, "0.2in");//$NON-NLS-1$
		} catch (SemanticException e) {
			e.printStackTrace();
		}
		reportDesignHandle.getMasterPages().add(simpleMasterPage);
	}

	private void createBody() throws SemanticException {
		GridHandle mainGrid = elementFactory.newGridItem("main", 1, 3);//$NON-NLS-1$
		mainGrid.setWidth("100%");//$NON-NLS-1$
		reportDesignHandle.getBody().add(mainGrid);

		// First Grid Row
		RowHandle row1 = (RowHandle) mainGrid.getRows().get(0);
		row1.setProperty(StyleHandle.BACKGROUND_COLOR_PROP, "#FEFBE9");//$NON-NLS-1$

		// Cell (1st Row)
		CellHandle row1Cell = (CellHandle) row1.getCells().get(0);

		// Title label
		LabelHandle label = elementFactory.newLabel(null);
		label.setText("Corporation Stock");//$NON-NLS-1$
		label.setProperty(StyleHandle.FONT_SIZE_PROP, DesignChoiceConstants.FONT_SIZE_X_LARGE);
		label.setProperty(StyleHandle.FONT_FAMILY_PROP, "Arial Black");//$NON-NLS-1$
		label.setProperty(StyleHandle.COLOR_PROP, "#6E6E6E");//$NON-NLS-1$
		label.setProperty(StyleHandle.PADDING_BOTTOM_PROP, "0.5in");//$NON-NLS-1$
		label.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_CENTER);

		row1Cell.getContent().add(label);

		// Second Grid Row
		RowHandle row2 = (RowHandle) mainGrid.getRows().get(1);

		CellHandle row2Cell1 = (CellHandle) row2.getCells().get(0);
		row2Cell1.getContent().add(createStockChart());

		// Third Grid Row
		RowHandle row3 = (RowHandle) mainGrid.getRows().get(2);

		// 1st Cell (3nd Row)
		CellHandle row3Cell1 = (CellHandle) row3.getCells().get(0);
		row3Cell1.getContent().add(createVolumeChart());
	}

	private ExtendedItemHandle createStockChart() {
		ExtendedItemHandle eih = elementFactory.newExtendedItem(null, "Chart");//$NON-NLS-1$

		try {
			eih.setHeight("175pt");//$NON-NLS-1$
			eih.setWidth("450pt");//$NON-NLS-1$
			eih.setProperty(ExtendedItemHandle.DATA_SET_PROP, "Data Set");//$NON-NLS-1$
			PropertyHandle computedSet = eih.getColumnBindings();
			cs1.setExpression("dataSetRow[\"Date\"]");//$NON-NLS-1$
			computedSet.addItem(cs1);
			cs2.setExpression("dataSetRow[\"High\"]");//$NON-NLS-1$
			computedSet.addItem(cs2);
			cs3.setExpression("dataSetRow[\"Close\"]");//$NON-NLS-1$
			computedSet.addItem(cs3);
			cs4.setExpression("dataSetRow[\"Low\"]");//$NON-NLS-1$
			computedSet.addItem(cs4);
			cs5.setExpression("dataSetRow[\"Open\"]");//$NON-NLS-1$
			computedSet.addItem(cs5);
			cs6.setExpression("dataSetRow[\"Volume\"]");//$NON-NLS-1$
			computedSet.addItem(cs6);
		} catch (SemanticException e) {
			e.printStackTrace();
		}

		ChartWithAxes cwaStock = ChartWithAxesImpl.create();
		cwaStock.setType("Stock Chart");//$NON-NLS-1$
		cwaStock.setSubType("Standard Stock Chart");//$NON-NLS-1$
		cwaStock.getTitle().setVisible(false);
		cwaStock.getLegend().setVisible(false);
		cwaStock.setOrientation(Orientation.VERTICAL_LITERAL);
		cwaStock.getBlock().setBounds(BoundsImpl.create(0, 0, 450, 175));
		cwaStock.getPlot().getClientArea().getOutline().setVisible(true);
		cwaStock.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(254, 251, 233));

		Axis xAxisPrimary = cwaStock.getPrimaryBaseAxes()[0];
		xAxisPrimary.setCategoryAxis(true);
		xAxisPrimary.getTitle().getCaption().setValue("Trading Date");//$NON-NLS-1$
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.setType(AxisType.DATE_TIME_LITERAL);
		xAxisPrimary.setFormatSpecifier(JavaDateFormatSpecifierImpl.create("MM/dd/yyyy"));//$NON-NLS-1$

		Axis yAxisPrimary = cwaStock.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getTitle().getCaption().setValue("Price");//$NON-NLS-1$
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		yAxisPrimary.getScale().setMin(NumberDataElementImpl.create(2.0));
		yAxisPrimary.getScale().setMax(NumberDataElementImpl.create(3.0));
		yAxisPrimary.getScale().setStep(0.2);

		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("01/25/2005");//$NON-NLS-1$
		sd.getBaseSampleData().add(sdBase);

		OrthogonalSampleData sdOrthogonal = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sdOrthogonal.setDataSetRepresentation("H5.3 L1.3 O4.5 C3.4");//$NON-NLS-1$
		sdOrthogonal.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal);

		cwaStock.setSampleData(sd);

		Series seCategory = SeriesImpl.create();
		Query query = QueryImpl.create("row[\"Date\"]");//$NON-NLS-1$
		seCategory.getDataDefinition().add(query);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeries().add(seCategory);
		xAxisPrimary.getSeriesDefinitions().add(sdX);

		StockSeries ss = (StockSeries) StockSeriesImpl.create();
		ss.setSeriesIdentifier("Stock Price");//$NON-NLS-1$
		Query query1 = QueryImpl.create("row[\"High\"]");//$NON-NLS-1$
		Query query2 = QueryImpl.create("row[\"Low\"]");//$NON-NLS-1$
		Query query3 = QueryImpl.create("row[\"Open\"]");//$NON-NLS-1$
		Query query4 = QueryImpl.create("row[\"Close\"]");//$NON-NLS-1$
		ArrayList list = new ArrayList();
		list.add(query1);
		list.add(query2);
		list.add(query3);
		list.add(query4);
		ss.getDataDefinition().addAll(list);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().update(ColorDefinitionImpl.create(168, 225, 253));
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(ss);

		try {
			// Add chart instance to IReportItem
			eih.getReportItem().setProperty("chart.instance", cwaStock);//$NON-NLS-1$
		} catch (ExtendedElementException e) {
			e.printStackTrace();
		}

		return eih;
	}

	private ExtendedItemHandle createVolumeChart() {
		ExtendedItemHandle eih = elementFactory.newExtendedItem(null, "Chart");//$NON-NLS-1$

		try {
			eih.setHeight("175pt");//$NON-NLS-1$
			eih.setWidth("450pt");//$NON-NLS-1$
			eih.setProperty(ExtendedItemHandle.DATA_SET_PROP, "Data Set");//$NON-NLS-1$
			PropertyHandle computedSet = eih.getColumnBindings();
			cs1.setExpression("dataSetRow[\"Date\"]");//$NON-NLS-1$
			computedSet.addItem(cs1);
			cs2.setExpression("dataSetRow[\"High\"]");//$NON-NLS-1$
			computedSet.addItem(cs2);
			cs3.setExpression("dataSetRow[\"Close\"]");//$NON-NLS-1$
			computedSet.addItem(cs3);
			cs4.setExpression("dataSetRow[\"Low\"]");//$NON-NLS-1$
			computedSet.addItem(cs4);
			cs5.setExpression("dataSetRow[\"Open\"]");//$NON-NLS-1$
			computedSet.addItem(cs5);
			cs6.setExpression("dataSetRow[\"Volume\"]");//$NON-NLS-1$
			computedSet.addItem(cs6);
		} catch (SemanticException e) {
			e.printStackTrace();
		}

		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setType("Bar Chart");//$NON-NLS-1$
		cwaBar.setSubType("Side-by-side");//$NON-NLS-1$
		cwaBar.getTitle().setVisible(false);
		cwaBar.getLegend().setVisible(false);
		cwaBar.setOrientation(Orientation.VERTICAL_LITERAL);
		cwaBar.getBlock().setBounds(BoundsImpl.create(0, 0, 450, 175));
		cwaBar.getPlot().getClientArea().getOutline().setVisible(true);
		cwaBar.getPlot().getClientArea().setBackground(ColorDefinitionImpl.create(254, 251, 233));

		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setCategoryAxis(true);
		xAxisPrimary.getTitle().getCaption().setValue("Trading Date");//$NON-NLS-1$
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());
		xAxisPrimary.setType(AxisType.DATE_TIME_LITERAL);
		xAxisPrimary.setFormatSpecifier(JavaDateFormatSpecifierImpl.create("MM/dd/yyyy"));//$NON-NLS-1$

		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getTitle().getCaption().setValue("Volume");//$NON-NLS-1$
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(ColorDefinitionImpl.GREY());

		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("01/25/2005");//$NON-NLS-1$
		sd.getBaseSampleData().add(sdBase);

		OrthogonalSampleData sdOrthogonal = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sdOrthogonal.setDataSetRepresentation("5");//$NON-NLS-1$
		sdOrthogonal.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal);

		cwaBar.setSampleData(sd);

		Series seCategory = SeriesImpl.create();
		Query query = QueryImpl.create("row[\"Date\"]");//$NON-NLS-1$
		seCategory.getDataDefinition().add(query);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeries().add(seCategory);
		xAxisPrimary.getSeriesDefinitions().add(sdX);

		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		Query query2 = QueryImpl.create("row[\"Volume\"]");//$NON-NLS-1$
		bs.getDataDefinition().add(query2);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette().update(ColorDefinitionImpl.create(168, 225, 253));
		sdY.getSeries().add(bs);
		yAxisPrimary.getSeriesDefinitions().add(sdY);

		DataPointComponent dpc = DataPointComponentImpl.create(DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("###,###"));//$NON-NLS-1$
		bs.getDataPoint().getComponents().clear();
		bs.getDataPoint().getComponents().add(dpc);
		bs.getLabel().setVisible(true);

		try {
			// Add chart instance to IReportItem
			eih.getReportItem().setProperty("chart.instance", cwaBar);//$NON-NLS-1$
		} catch (ExtendedElementException e) {
			e.printStackTrace();
		}

		return eih;
	}
}
