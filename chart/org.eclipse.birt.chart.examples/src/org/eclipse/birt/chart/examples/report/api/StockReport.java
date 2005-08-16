/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
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
import org.eclipse.birt.report.model.api.elements.structures.ColumnHint;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;

public class StockReport {

	ReportDesignHandle reportDesignHandle = null;

	ElementFactory elementFactory = null;

	StructureFactory structFactory = null;

	MetaDataDictionary dict = null;

	public static void main(String[] args) throws SemanticException,
			IOException {
		new StockReport().createReport();
	}

	void createReport() throws SemanticException, IOException {
		//A session handle for all open reports
		SessionHandle session = DesignEngine.newSession(null);

		//Create a new report
		reportDesignHandle = session.createDesign();

		//Element factory is used to create instances of BIRT elements.
		elementFactory = reportDesignHandle.getElementFactory();

		structFactory = new StructureFactory();

		dict = MetaDataDictionary.getInstance();

		createMasterPages();
		createDataSources();
		createDataSets();
		createStyles();
		createBody();

		String outputPath = "output";
		File outputFolder = new File(outputPath);
		if (!outputFolder.exists() && !outputFolder.mkdir()) {
			throw new IOException("Can not create the output folder");
		}
		reportDesignHandle.saveAs(outputPath + "/" + "StockAnalysis.rptdesign");
	}

	private void createDataSources() throws SemanticException {
		ScriptDataSourceHandle dataSourceHandle = elementFactory
				.newScriptDataSource("Data Source");
		reportDesignHandle.getDataSources().add(dataSourceHandle);
	}

	private void createDataSets() throws SemanticException {
		//Data Set
		ScriptDataSetHandle dataSetHandle = elementFactory
				.newScriptDataSet("Data Set");
		dataSetHandle.setDataSource("Data Source");

		//Set open( ) in code
		dataSetHandle
				.setOpen("i=0;"
						+ "sourcedata = new Array( new Array(6), new Array(6), new Array(6), new Array(6),"
						+ "new Array(6), new Array(6), new Array(6), new Array(6));"
						+ "sourcedata[0][0] = \"3/1/2005\"; "
						+ "sourcedata[0][1] = 2.77;"
						+ "sourcedata[0][2] = 2.73;"
						+ "sourcedata[0][3] = 2.69; "
						+ "sourcedata[0][4] = 2.71;"
						+ "sourcedata[0][5] = 341900;"

						+ "sourcedata[1][0] = \"3/2/2005\"; "
						+ "sourcedata[1][1] = 2.8;"
						+ "sourcedata[1][2] = 2.64;"
						+ "sourcedata[1][3] = 2.6; "
						+ "sourcedata[1][4] = 2.71;"
						+ "sourcedata[1][5] = 249900;"

						+ "sourcedata[2][0] = \"3/3/2005\"; "
						+ "sourcedata[2][1] = 2.6;"
						+ "sourcedata[2][2] = 2.28;"
						+ "sourcedata[2][3] = 2.13; "
						+ "sourcedata[2][4] = 2.59;"
						+ "sourcedata[2][5] = 394800;"

						+ "sourcedata[3][0] = \"3/4/2005\"; "
						+ "sourcedata[3][1] = 2.87;"
						+ "sourcedata[3][2] = 2.87;"
						+ "sourcedata[3][3] = 2.03; "
						+ "sourcedata[3][4] = 2.21;"
						+ "sourcedata[3][5] = 358200;"

						+ "sourcedata[4][0] = \"3/5/2005\"; "
						+ "sourcedata[4][1] = 2.48;"
						+ "sourcedata[4][2] = 2.26;"
						+ "sourcedata[4][3] = 2.16; "
						+ "sourcedata[4][4] = 2.39;"
						+ "sourcedata[4][5] = 339000;"

						+ "sourcedata[5][0] = \"3/6/2005\"; "
						+ "sourcedata[5][1] = 2.98;"
						+ "sourcedata[5][2] = 2.86;"
						+ "sourcedata[5][3] = 2.04; "
						+ "sourcedata[5][4] = 2.19;"
						+ "sourcedata[5][5] = 221000;"

						+ "sourcedata[6][0] = \"3/7/2005\"; "
						+ "sourcedata[6][1] = 2.87;"
						+ "sourcedata[6][2] = 2.17;"
						+ "sourcedata[6][3] = 2.14; "
						+ "sourcedata[6][4] = 2.62;"
						+ "sourcedata[6][5] = 183600;"

						+ "sourcedata[7][0] = \"3/8/2005\"; "
						+ "sourcedata[7][1] = 2.8;"
						+ "sourcedata[7][2] = 2.78;"
						+ "sourcedata[7][3] = 2.65; "
						+ "sourcedata[7][4] = 2.66;"
						+ "sourcedata[7][5] = 194800;");

		//Set fetch( ) in code
		dataSetHandle.setFetch("if ( i < 8 ){"
				+ "row[\"Date\"] = sourcedata[i][0];"
				+ "row[\"High\"] = sourcedata[i][1];"
				+ "row[\"Close\"] = sourcedata[i][2];"
				+ "row[\"Low\"] = sourcedata[i][3];"
				+ "row[\"Open\"] = sourcedata[i][4];"
				+ "row[\"Volume\"] = sourcedata[i][5];" + "i++;"
				+ "return true;}" + "else return false;");

		//Set Output Columns in Data Set
		ColumnHint ch1 = StructureFactory.createColumnHint();
		ch1.setProperty("columnName", "Date");

		ColumnHint ch2 = StructureFactory.createColumnHint();
		ch2.setProperty("columnName", "High");

		ColumnHint ch3 = StructureFactory.createColumnHint();
		ch3.setProperty("columnName", "Close");

		ColumnHint ch4 = StructureFactory.createColumnHint();
		ch4.setProperty("columnName", "Low");

		ColumnHint ch5 = StructureFactory.createColumnHint();
		ch5.setProperty("columnName", "Open");

		ColumnHint ch6 = StructureFactory.createColumnHint();
		ch6.setProperty("columnName", "Volume");

		PropertyHandle columnHint = dataSetHandle
				.getPropertyHandle(ScriptDataSetHandle.COLUMN_HINTS_PROP);
		columnHint.addItem(ch1);
		columnHint.addItem(ch2);
		columnHint.addItem(ch3);
		columnHint.addItem(ch4);
		columnHint.addItem(ch5);
		columnHint.addItem(ch6);

		//Set Preview Results columns in Data Set
		ResultSetColumn rs1 = StructureFactory.createResultSetColumn();
		rs1.setColumnName("Date");
		rs1.setPosition(new Integer(1));
		rs1.setDataType("date-time");

		ResultSetColumn rs2 = StructureFactory.createResultSetColumn();
		rs2.setColumnName("High");
		rs2.setPosition(new Integer(2));
		rs2.setDataType("float");

		ResultSetColumn rs3 = StructureFactory.createResultSetColumn();
		rs3.setColumnName("Close");
		rs3.setPosition(new Integer(3));
		rs3.setDataType("float");

		ResultSetColumn rs4 = StructureFactory.createResultSetColumn();
		rs4.setColumnName("Low");
		rs4.setPosition(new Integer(4));
		rs4.setDataType("float");

		ResultSetColumn rs5 = StructureFactory.createResultSetColumn();
		rs5.setColumnName("Open");
		rs5.setPosition(new Integer(5));
		rs5.setDataType("float");

		ResultSetColumn rs6 = StructureFactory.createResultSetColumn();
		rs6.setColumnName("Volume");
		rs6.setPosition(new Integer(6));
		rs6.setDataType("integer");

		PropertyHandle resultSet = dataSetHandle
				.getPropertyHandle(ScriptDataSetHandle.RESULT_SET_PROP);
		resultSet.addItem(rs1);
		resultSet.addItem(rs2);
		resultSet.addItem(rs3);
		resultSet.addItem(rs4);
		resultSet.addItem(rs5);
		resultSet.addItem(rs6);

		reportDesignHandle.getDataSets().add(dataSetHandle);
	}

	private void createMasterPages() throws ContentException, NameException {
		DesignElementHandle simpleMasterPage = elementFactory
				.newSimpleMasterPage("Master Page");
		try {
			simpleMasterPage.setProperty(MasterPageHandle.LEFT_MARGIN_PROP,
					"0.2in");
			simpleMasterPage.setProperty(MasterPageHandle.RIGHT_MARGIN_PROP,
					"0.2in");
		} catch (SemanticException e) {
			e.printStackTrace();
		}
		reportDesignHandle.getMasterPages().add(simpleMasterPage);
	}

	private void createStyles() throws SemanticException {
		StyleHandle borderStyle = elementFactory.newStyle("Border");
		borderStyle.setProperty(StyleHandle.BORDER_BOTTOM_STYLE_PROP,
				DesignChoiceConstants.LINE_STYLE_SOLID);
		borderStyle.setProperty(StyleHandle.BORDER_LEFT_STYLE_PROP,
				DesignChoiceConstants.LINE_STYLE_SOLID);
		borderStyle.setProperty(StyleHandle.BORDER_TOP_STYLE_PROP,
				DesignChoiceConstants.LINE_STYLE_SOLID);
		borderStyle.setProperty(StyleHandle.BORDER_RIGHT_STYLE_PROP,
				DesignChoiceConstants.LINE_STYLE_SOLID);
		borderStyle.setProperty(StyleHandle.BORDER_BOTTOM_WIDTH_PROP, "1px");
		borderStyle.setProperty(StyleHandle.BORDER_LEFT_WIDTH_PROP, "1px");
		borderStyle.setProperty(StyleHandle.BORDER_TOP_WIDTH_PROP, "1px");
		borderStyle.setProperty(StyleHandle.BORDER_RIGHT_WIDTH_PROP, "1px");
		borderStyle
				.setProperty(StyleHandle.BORDER_BOTTOM_COLOR_PROP, "#808080");
		borderStyle.setProperty(StyleHandle.BORDER_LEFT_COLOR_PROP, "#808080");
		borderStyle.setProperty(StyleHandle.BORDER_RIGHT_COLOR_PROP, "#808080");
		borderStyle.setProperty(StyleHandle.BORDER_TOP_COLOR_PROP, "#808080");

		StyleHandle textStyle = elementFactory.newStyle("Text");
		textStyle.setProperty(StyleHandle.FONT_WEIGHT_PROP,
				DesignChoiceConstants.FONT_WEIGHT_BOLD);
		textStyle.setProperty(StyleHandle.FONT_SIZE_PROP,
				DesignChoiceConstants.FONT_SIZE_SMALL);
		textStyle.setProperty(StyleHandle.COLOR_PROP, "#808080");
		textStyle.setProperty(StyleHandle.TEXT_ALIGN_PROP,
				DesignChoiceConstants.TEXT_ALIGN_CENTER);

		reportDesignHandle.getStyles().add(borderStyle);
		reportDesignHandle.getStyles().add(textStyle);
	}

	private void createBody() throws SemanticException {
		GridHandle mainGrid = elementFactory.newGridItem("main", 2, 3);
		mainGrid.setWidth("100%");
		reportDesignHandle.getBody().add(mainGrid);

		//First Grid Row
		RowHandle row1 = (RowHandle) mainGrid.getRows().get(0);
		row1.setProperty(StyleHandle.BACKGROUND_COLOR_PROP, "#FEFBE9");

		//Cell (1st Row)
		CellHandle row1Cell = (CellHandle) row1.getCells().get(0);
		row1Cell.setColumnSpan(2);

		//Title label
		LabelHandle label = elementFactory.newLabel(null);
		label.setText("Corporation Stock");
		label.setProperty(StyleHandle.FONT_SIZE_PROP,
				DesignChoiceConstants.FONT_SIZE_X_LARGE);
		label.setProperty(StyleHandle.FONT_FAMILY_PROP, "Arial Black");
		label.setProperty(StyleHandle.COLOR_PROP, "#6E6E6E");
		label.setProperty(StyleHandle.PADDING_BOTTOM_PROP, "0.5in");
		label.setProperty(StyleHandle.TEXT_ALIGN_PROP,
				DesignChoiceConstants.TEXT_ALIGN_CENTER);

		row1Cell.getContent().add(label);

		row1.getCells().drop(1);

		//Second Grid Row
		RowHandle row2 = (RowHandle) mainGrid.getRows().get(1);

		//1st Cell (2nd Row)
		CellHandle row2Cell1 = (CellHandle) row2.getCells().get(0);
		row2Cell1.getContent().add(createStockChart());

		//2nd Cell (2nd Row)
		CellHandle row2Cell2 = (CellHandle) row2.getCells().get(1);
		row2Cell2.setProperty(StyleHandle.VERTICAL_ALIGN_PROP, 
				DesignChoiceConstants.VERTICAL_ALIGN_TOP);
		
		//Stock Grid
		GridHandle grid1 = elementFactory.newGridItem("stock", 2, 5);
		grid1.setWidth("100%");

		RowHandle grid1Row = (RowHandle) grid1.getRows().get(0);
		CellHandle cell = (CellHandle) grid1Row.getCells().get(0);
		cell.setColumnSpan(2);

		label = elementFactory.newLabel(null);
		label.setText("Weekly Price Summary");
		label.setStyleName("Text");
		label.setProperty(StyleHandle.FONT_SIZE_PROP,
				DesignChoiceConstants.FONT_SIZE_MEDIUM);

		cell.getContent().add(label);
		grid1Row.getCells().drop(1);

		grid1Row = (RowHandle) grid1.getRows().get(1);

		cell = (CellHandle) grid1Row.getCells().get(0);
		cell.setStyleName("Border");
		label = elementFactory.newLabel(null);
		label.setText("High:");
		label.setStyleName("Text");
		label.setProperty(StyleHandle.FONT_WEIGHT_PROP,
				DesignChoiceConstants.FONT_WEIGHT_LIGHTER);
		cell.getContent().add(label);

		cell = (CellHandle) grid1Row.getCells().get(1);
		cell.setStyleName("Border");
		cell.setProperty(StyleHandle.BACKGROUND_COLOR_PROP, "#FEFBE9");
		DataItemHandle data = elementFactory.newDataItem(null);
		data.setProperty(DataItemHandle.DATA_SET_PROP, "Data Set");
		data.setValueExpr("Total.max(row[\"High\"])");
		data.setStyleName("Text");
		data.getPrivateStyle().setNumberFormatCategory(
				DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM);
		data.getPrivateStyle().setNumberFormat("$###,##0.00");
		cell.getContent().add(data);

		grid1Row = (RowHandle) grid1.getRows().get(2);

		cell = (CellHandle) grid1Row.getCells().get(0);
		cell.setStyleName("Border");
		label = elementFactory.newLabel(null);
		label.setText("Low:");
		label.setStyleName("Text");
		label.setProperty(StyleHandle.FONT_WEIGHT_PROP,
				DesignChoiceConstants.FONT_WEIGHT_LIGHTER);
		cell.getContent().add(label);

		cell = (CellHandle) grid1Row.getCells().get(1);
		cell.setStyleName("Border");
		cell.setProperty(StyleHandle.BACKGROUND_COLOR_PROP, "#FEFBE9");
		data = elementFactory.newDataItem(null);
		data.setValueExpr("Total.min(row[\"Low\"])");
		data.setStyleName("Text");
		data.getPrivateStyle().setNumberFormatCategory(
				DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM);
		data.getPrivateStyle().setNumberFormat("$###,##0.00");
		data.setProperty(DataItemHandle.DATA_SET_PROP, "Data Set");
		cell.getContent().add(data);

		grid1Row = (RowHandle) grid1.getRows().get(3);

		cell = (CellHandle) grid1Row.getCells().get(0);
		cell.setStyleName("Border");
		label = elementFactory.newLabel(null);
		label.setText("Ave. Open");
		label.setStyleName("Text");
		label.setProperty(StyleHandle.FONT_WEIGHT_PROP,
				DesignChoiceConstants.FONT_WEIGHT_LIGHTER);
		cell.getContent().add(label);

		cell = (CellHandle) grid1Row.getCells().get(1);
		cell.setStyleName("Border");
		cell.setProperty(StyleHandle.BACKGROUND_COLOR_PROP, "#FEFBE9");
		data = elementFactory.newDataItem(null);
		data.setValueExpr("Total.Ave(row[\"Open\"])");
		data.setStyleName("Text");
		data.getPrivateStyle().setNumberFormatCategory(
				DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM);
		data.getPrivateStyle().setNumberFormat("$###,##0.00");
		data.setProperty(DataItemHandle.DATA_SET_PROP, "Data Set");
		cell.getContent().add(data);

		grid1Row = (RowHandle) grid1.getRows().get(4);

		cell = (CellHandle) grid1Row.getCells().get(0);
		cell.setStyleName("Border");
		label = elementFactory.newLabel(null);
		label.setText("Ave. Close");
		label.setStyleName("Text");
		label.setProperty(StyleHandle.FONT_WEIGHT_PROP,
				DesignChoiceConstants.FONT_WEIGHT_LIGHTER);
		cell.getContent().add(label);

		cell = (CellHandle) grid1Row.getCells().get(1);
		cell.setStyleName("Border");
		cell.setProperty(StyleHandle.BACKGROUND_COLOR_PROP, "#FEFBE9");
		data = elementFactory.newDataItem(null);
		data.setValueExpr("Total.Ave(row[\"Close\"])");
		data.setStyleName("Text");
		data.getPrivateStyle().setNumberFormatCategory(
				DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM);
		data.getPrivateStyle().setNumberFormat("$###,##0.00");
		data.setProperty(DataItemHandle.DATA_SET_PROP, "Data Set");
		cell.getContent().add(data);

		row2Cell2.getContent().add(grid1);

		//Third Grid Row
		RowHandle row3 = (RowHandle) mainGrid.getRows().get(2);

		//1st Cell (3nd Row)
		CellHandle row3Cell1 = (CellHandle) row3.getCells().get(0);
		row3Cell1.getContent().add(createVolumeChart());

		//2nd Cell (3nd Row)
		CellHandle row3Cell2 = (CellHandle) row3.getCells().get(1);
		row3Cell2.setProperty(StyleHandle.VERTICAL_ALIGN_PROP, 
				DesignChoiceConstants.VERTICAL_ALIGN_TOP);

		//Volume Grid
		GridHandle grid2 = elementFactory.newGridItem("volume", 2, 5);
		grid2.setWidth("100%");

		RowHandle grid2Row = (RowHandle) grid2.getRows().get(0);
		cell = (CellHandle) grid2Row.getCells().get(0);
		cell.setColumnSpan(2);
		label = elementFactory.newLabel(null);
		label.setText("Weekly Volume Summary");
		label.setStyleName("Text");
		label.setProperty(StyleHandle.FONT_SIZE_PROP,
				DesignChoiceConstants.FONT_SIZE_MEDIUM);
		cell.getContent().add(label);
		grid2Row.getCells().drop(1);	
		
		grid2Row = (RowHandle) grid2.getRows().get(1);

		cell = (CellHandle) grid2Row.getCells().get(0);
		cell.setStyleName("Border");
		label = elementFactory.newLabel(null);
		label.setText("High:");
		label.setStyleName("Text");
		label.setProperty(StyleHandle.FONT_WEIGHT_PROP,
				DesignChoiceConstants.FONT_WEIGHT_LIGHTER);
		cell.getContent().add(label);

		cell = (CellHandle) grid2Row.getCells().get(1);
		cell.setStyleName("Border");
		cell.setProperty(StyleHandle.BACKGROUND_COLOR_PROP, "#FEFBE9");
		data = elementFactory.newDataItem(null);
		data.setValueExpr("Total.max(row[\"Volume\"])");
		data.setStyleName("Text");
		data.getPrivateStyle().setNumberFormatCategory(
				DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM);
		data.getPrivateStyle().setNumberFormat("###,###");
		data.setProperty(DataItemHandle.DATA_SET_PROP, "Data Set");
		cell.getContent().add(data);

		grid2Row = (RowHandle) grid2.getRows().get(2);

		cell = (CellHandle) grid2Row.getCells().get(0);
		cell.setStyleName("Border");
		label = elementFactory.newLabel(null);
		label.setText("Low:");
		label.setStyleName("Text");
		label.setProperty(StyleHandle.FONT_WEIGHT_PROP,
				DesignChoiceConstants.FONT_WEIGHT_LIGHTER);
		cell.getContent().add(label);

		cell = (CellHandle) grid2Row.getCells().get(1);
		cell.setStyleName("Border");
		cell.setProperty(StyleHandle.BACKGROUND_COLOR_PROP, "#FEFBE9");
		data = elementFactory.newDataItem(null);
		data.setValueExpr("Total.min(row[\"Volume\"])");
		data.setStyleName("Text");
		data.getPrivateStyle().setNumberFormatCategory(
				DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM);
		data.getPrivateStyle().setNumberFormat("###,###");
		data.setProperty(DataItemHandle.DATA_SET_PROP, "Data Set");
		cell.getContent().add(data);

		grid2Row = (RowHandle) grid2.getRows().get(3);

		cell = (CellHandle) grid2Row.getCells().get(0);
		cell.setStyleName("Border");
		label = elementFactory.newLabel(null);
		label.setText("Total Volume:");
		label.setStyleName("Text");
		label.setProperty(StyleHandle.FONT_WEIGHT_PROP,
				DesignChoiceConstants.FONT_WEIGHT_LIGHTER);
		cell.getContent().add(label);

		cell = (CellHandle) grid2Row.getCells().get(1);
		cell.setStyleName("Border");
		cell.setProperty(StyleHandle.BACKGROUND_COLOR_PROP, "#FEFBE9");
		data = elementFactory.newDataItem(null);
		data.setValueExpr("Total.sum(row[\"Volume\"])");
		data.setStyleName("Text");
		data.getPrivateStyle().setNumberFormatCategory(
				DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM);
		data.getPrivateStyle().setNumberFormat("###,###");
		data.setProperty(DataItemHandle.DATA_SET_PROP, "Data Set");
		cell.getContent().add(data);

		grid2Row = (RowHandle) grid2.getRows().get(4);

		cell = (CellHandle) grid2Row.getCells().get(0);
		cell.setStyleName("Border");
		label = elementFactory.newLabel(null);
		label.setText("Ave Volume:");
		label.setStyleName("Text");
		label.setProperty(StyleHandle.FONT_WEIGHT_PROP,
				DesignChoiceConstants.FONT_WEIGHT_LIGHTER);
		cell.getContent().add(label);

		cell = (CellHandle) grid2Row.getCells().get(1);
		cell.setStyleName("Border");
		cell.setProperty(StyleHandle.BACKGROUND_COLOR_PROP, "#FEFBE9");
		data = elementFactory.newDataItem(null);
		data.setValueExpr("Total.ave(row[\"Volume\"])");
		data.setStyleName("Text");
		data.getPrivateStyle().setNumberFormatCategory(
				DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM);
		data.getPrivateStyle().setNumberFormat("###,###");
		data.setProperty(DataItemHandle.DATA_SET_PROP, "Data Set");
		cell.getContent().add(data);

		row3Cell2.getContent().add(grid2);

	}

	private ExtendedItemHandle createStockChart() {
		ExtendedItemHandle eih = elementFactory.newExtendedItem(null, "Chart");

		try {
			eih.setHeight("175pt");
			eih.setWidth("450pt");
			eih.setProperty(ExtendedItemHandle.DATA_SET_PROP, "Data Set");
		} catch (SemanticException e) {
			e.printStackTrace();
		}

		ChartWithAxes cwaStock = ChartWithAxesImpl.create();
		cwaStock.setType("Stock Chart");
		cwaStock.setSubType("Standard Stock Chart");
		cwaStock.getTitle().setVisible(false);
		cwaStock.getLegend().setVisible(false);
		cwaStock.setOrientation(Orientation.VERTICAL_LITERAL);
		cwaStock.getBlock().setBounds(BoundsImpl.create(0, 0, 450, 175));
		cwaStock.getPlot().getClientArea().getOutline().setVisible(true);
		cwaStock.getPlot().getClientArea().setBackground(
				ColorDefinitionImpl.create(254, 251, 233));

		Axis xAxisPrimary = cwaStock.getPrimaryBaseAxes()[0];
		xAxisPrimary.setCategoryAxis(true);
		xAxisPrimary.getTitle().getCaption().setValue("Trading Date");
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.GREY());
		xAxisPrimary.setType(AxisType.DATE_TIME_LITERAL);
		xAxisPrimary.setFormatSpecifier(JavaDateFormatSpecifierImpl
				.create("MM/dd/yyyy"));

		Axis yAxisPrimary = cwaStock.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getTitle().getCaption().setValue("Price");
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.GREY());
		yAxisPrimary.getScale().setMin(NumberDataElementImpl.create(2.0));
		yAxisPrimary.getScale().setMax(NumberDataElementImpl.create(3.0));
		yAxisPrimary.getScale().setStep(0.2);

		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("01/25/2005");
		sd.getBaseSampleData().add(sdBase);

		OrthogonalSampleData sdOrthogonal = DataFactory.eINSTANCE
				.createOrthogonalSampleData();
		sdOrthogonal.setDataSetRepresentation("H5.3 L1.3 O4.5 C3.4");
		sdOrthogonal.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal);

		cwaStock.setSampleData(sd);

		Series seCategory = SeriesImpl.create();
		Query query = QueryImpl.create("row[\"Date\"]");
		seCategory.getDataDefinition().add(query);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeries().add(seCategory);
		xAxisPrimary.getSeriesDefinitions().add(sdX);

		StockSeries ss = (StockSeries) StockSeriesImpl.create();
		ss.setSeriesIdentifier("Stock Price");
		Query query1 = QueryImpl.create("row[\"High\"]");
		Query query2 = QueryImpl.create("row[\"Low\"]");
		Query query3 = QueryImpl.create("row[\"Open\"]");
		Query query4 = QueryImpl.create("row[\"Close\"]");
		ArrayList list = new ArrayList();
		list.add(query1);
		list.add(query2);
		list.add(query3);
		list.add(query4);
		ss.getDataDefinition().addAll(list);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette()
				.update(ColorDefinitionImpl.create(168, 225, 253));
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		sdY.getSeries().add(ss);

		ChartReportItemImpl crii;
		try {
			//Add ChartReportItemImpl to ExtendedItemHandle
			crii = (ChartReportItemImpl) eih.getReportItem();
			//Add chart instance to ChartReportItemImpl
			crii.setProperty("chart.instance", cwaStock);
		} catch (ExtendedElementException e) {
			e.printStackTrace();
		}

		return eih;
	}

	private ExtendedItemHandle createVolumeChart() {
		ExtendedItemHandle eih = elementFactory.newExtendedItem(null, "Chart");

		try {
			eih.setHeight("175pt");
			eih.setWidth("450pt");
			eih.setProperty(ExtendedItemHandle.DATA_SET_PROP, "Data Set");
		} catch (SemanticException e) {
			e.printStackTrace();
		}

		ChartWithAxes cwaBar = ChartWithAxesImpl.create();
		cwaBar.setType("Bar Chart");
		cwaBar.setSubType("Side-by-side");
		cwaBar.getTitle().setVisible(false);
		cwaBar.getLegend().setVisible(false);
		cwaBar.setOrientation(Orientation.VERTICAL_LITERAL);
		cwaBar.getBlock().setBounds(BoundsImpl.create(0, 0, 450, 175));
		cwaBar.getPlot().getClientArea().getOutline().setVisible(true);
		cwaBar.getPlot().getClientArea().setBackground(
				ColorDefinitionImpl.create(254, 251, 233));

		Axis xAxisPrimary = cwaBar.getPrimaryBaseAxes()[0];
		xAxisPrimary.setCategoryAxis(true);
		xAxisPrimary.getTitle().getCaption().setValue("Trading Date");
		xAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		xAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		xAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.GREY());
		xAxisPrimary.setType(AxisType.DATE_TIME_LITERAL);
		xAxisPrimary.setFormatSpecifier(JavaDateFormatSpecifierImpl
				.create("MM/dd/yyyy"));

		Axis yAxisPrimary = cwaBar.getPrimaryOrthogonalAxis(xAxisPrimary);
		yAxisPrimary.getTitle().getCaption().setValue("Volume");
		yAxisPrimary.getMajorGrid().getLineAttributes().setStyle(
				LineStyle.DOTTED_LITERAL);
		yAxisPrimary.getMajorGrid().getLineAttributes().setVisible(true);
		yAxisPrimary.getMajorGrid().getLineAttributes().setColor(
				ColorDefinitionImpl.GREY());

		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("01/25/2005");
		sd.getBaseSampleData().add(sdBase);

		OrthogonalSampleData sdOrthogonal = DataFactory.eINSTANCE
				.createOrthogonalSampleData();
		sdOrthogonal.setDataSetRepresentation("5");
		sdOrthogonal.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal);

		cwaBar.setSampleData(sd);

		Series seCategory = SeriesImpl.create();
		Query query = QueryImpl.create("row[\"Date\"]");
		seCategory.getDataDefinition().add(query);

		SeriesDefinition sdX = SeriesDefinitionImpl.create();
		sdX.getSeries().add(seCategory);
		xAxisPrimary.getSeriesDefinitions().add(sdX);

		BarSeries bs = (BarSeries) BarSeriesImpl.create();
		Query query2 = QueryImpl.create("row[\"Volume\"]");
		bs.getDataDefinition().add(query2);

		SeriesDefinition sdY = SeriesDefinitionImpl.create();
		sdY.getSeriesPalette()
				.update(ColorDefinitionImpl.create(168, 225, 253));
		sdY.getSeries().add(bs);
		yAxisPrimary.getSeriesDefinitions().add(sdY);
		
		DataPointComponent dpc = DataPointComponentImpl.create( DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("###,###") );
		bs.getDataPoint().getComponents().clear();
		bs.getDataPoint().getComponents().add(dpc);
		bs.getLabel().setVisible(true);

		ChartReportItemImpl crii;
		try {
			//Add ChartReportItemImpl to ExtendedItemHandle
			crii = (ChartReportItemImpl) eih.getReportItem();
			//Add chart instance to ChartReportItemImpl
			crii.setProperty("chart.instance", cwaBar);
		} catch (ExtendedElementException e) {
			e.printStackTrace();
		}

		return eih;
	}
}