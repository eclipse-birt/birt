/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.APISamples;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyType;

import com.ibm.icu.util.ULocale;

/**
 * This is a demo usage of Model, this class generate a sample design file using
 * pure Model APIs.
 * 
 * 
 */

public class ProjectAnalysis {

	ReportDesignHandle designHandle = null;
	ElementFactory elementFactory = null;
	StructureFactory structFactory = null;
	MetaDataDictionary dict = null;

	private final static String ODA_EXTENSION_ID = "org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet"; //$NON-NLS-1$

	/**
	 * Generate the report.
	 */

	public static void main(String[] args) throws SemanticException, IOException {
		new ProjectAnalysis().buildReport();
	}

	void buildReport() throws SemanticException, IOException {
		// Create a session handle. This is used to manage all open designs.
		// Your application need create the session only once.

		SessionHandle session = DesignEngine.newSession(ULocale.ENGLISH);

		// Create a new report design.

		designHandle = session.createDesign();

		// The element factory creates instances of the various BIRT elements.

		elementFactory = designHandle.getElementFactory();
		structFactory = new StructureFactory();

		dict = MetaDataDictionary.getInstance();

		buildMasterPages();
		buildDataSources();
		buildDataSets();
		buildImages();
		buildStyles();
		buildBody();

		/*
		 * String className = this.getClass( ).getName( ); int lastDotIndex =
		 * className.lastIndexOf( "." ); //$NON-NLS-1$ className = className.substring(
		 * 0, lastDotIndex );
		 * 
		 * String outputPath = "src/" + className.replace( '.', '/' ) ; //$NON-NLS-1$
		 * //$NON-NLS-2$ File outputFolder = new File( "sample.rptdesign" ); if (
		 * !outputFolder.exists( ) && !outputFolder.mkdir( ) ) { throw new IOException(
		 * "Can not create the output folder" ); //$NON-NLS-1$ } }
		 */
		designHandle.saveAs("projectAnalysis.rptdesign"); //$NON-NLS-1$ //$NON-NLS-2$
	}

	void buildImages() throws IOException, SemanticException {
		EmbeddedImage image = StructureFactory.createEmbeddedImage();
		image.setType(DesignChoiceConstants.IMAGE_TYPE_IMAGE_JPEG);
		image.setData(load("images/abos_logo.jpg")); //$NON-NLS-1$
		image.setName("abos_logo"); //$NON-NLS-1$
		designHandle.addImage(image);
	}

	/**
	 * load file into byte array with given file name.
	 */

	public byte[] load(String fileName) throws IOException {
		InputStream is = null;

		is = new BufferedInputStream(this.getClass().getResourceAsStream(fileName));
		byte data[] = null;
		if (is != null) {
			try {
				data = new byte[is.available()];
				is.read(data);
			} catch (IOException e1) {
				throw e1;
			}
		}
		return data;
	}

	void buildDataSources() throws SemanticException {
		OdaDataSourceHandle dsHandle = elementFactory.newOdaDataSource("Data source", //$NON-NLS-1$
				"org.eclipse.birt.report.data.oda.jdbc"); //$NON-NLS-1$

		// define a user property on datasource
		UserPropertyDefn userPropDefn = new UserPropertyDefn();
		userPropDefn.setName("valid"); //$NON-NLS-1$
		userPropDefn.setType(dict.getPropertyType(PropertyType.STRING_TYPE));
		dsHandle.addUserPropertyDefn(userPropDefn);

		dsHandle.setProperty("valid", "true"); //$NON-NLS-1$ //$NON-NLS-2$

		dsHandle.setProperty("odaDriverClass", "net.sourceforge.jtds.jdbc.Driver"); //$NON-NLS-1$ //$NON-NLS-2$
		dsHandle.setProperty("odaURL", "jdbc:jtds:sqlserver://spmdb/gui");//$NON-NLS-1$ //$NON-NLS-2$
		dsHandle.setProperty("odaUser", "root"); //$NON-NLS-1$ //$NON-NLS-2$
		dsHandle.setProperty("odaPassword", "root"); //$NON-NLS-1$//$NON-NLS-2$

		designHandle.getDataSources().add(dsHandle);
	}

	void buildDataSets() throws SemanticException {
		OdaDataSetHandle dsHandle = elementFactory.newOdaDataSet("Data Set", ODA_EXTENSION_ID); //$NON-NLS-1$
		dsHandle.setDataSource("Data source");
		dsHandle.setQueryText("SELECT [Project].[Number]," + "[Project].[Manager],"
				+ "[Project].[Type],[Project].[Status]," + " [Project].[Starting_dt]," + "[Project].[Closing_dt], "
				+ "[Project].[Value]," + "[Project].[Billing]," + "[ProjectDetails].[Region],"
				+ "[ProjectDetails].[Budget]," + "[ProjectDetails].[Expense]," + "[ProjectDetails].[Completion] "
				+ "FROM [Project],[ProjectDetails]" + "WHERE "
				+ "[Project].[Number] =  [ProjectDetails].[Project_Number]");

		FilterCondition fc = new StructureFactory().createFilterCond();
		fc.setOperator(DesignChoiceConstants.FILTER_OPERATOR_EQ);

		fc.setExpr("row[\"Number\"]");
		fc.setValue1("23575");

		PropertyHandle filterHandle = dsHandle.getPropertyHandle(OdaDataSetHandle.FILTER_PROP);
		filterHandle.addItem(fc);

		designHandle.getDataSets().add(dsHandle);
	}

	void buildMasterPages() throws ContentException, NameException {
		DesignElementHandle simpleMasterPage = elementFactory.newSimpleMasterPage("Simple MasterPage"); //$NON-NLS-1$
		designHandle.getMasterPages().add(simpleMasterPage);
	}

	void buildStyles() throws SemanticException {
		StyleHandle style1 = elementFactory.newStyle("BorderTopBottom");
		style1.setProperty(StyleHandle.BORDER_BOTTOM_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		style1.setProperty(StyleHandle.BORDER_LEFT_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_NONE);
		style1.setProperty(StyleHandle.BORDER_TOP_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		style1.setProperty(StyleHandle.BORDER_BOTTOM_WIDTH_PROP, "1px");
		style1.setProperty(StyleHandle.BORDER_TOP_WIDTH_PROP, "1px");

		StyleHandle style2 = elementFactory.newStyle("BorderL");
		style2.setProperty(StyleHandle.BORDER_BOTTOM_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		style2.setProperty(StyleHandle.BORDER_LEFT_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		style2.setProperty(StyleHandle.BORDER_TOP_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		style2.setProperty(StyleHandle.BORDER_BOTTOM_WIDTH_PROP, "1px");
		style2.setProperty(StyleHandle.BORDER_LEFT_WIDTH_PROP, "1px");
		style2.setProperty(StyleHandle.BORDER_TOP_WIDTH_PROP, "1px");

		StyleHandle style3 = elementFactory.newStyle("BorderR");
		style3.setProperty(StyleHandle.BORDER_BOTTOM_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		style3.setProperty(StyleHandle.BORDER_RIGHT_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		style3.setProperty(StyleHandle.BORDER_TOP_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		style3.setProperty(StyleHandle.BORDER_BOTTOM_WIDTH_PROP, "1px");
		style3.setProperty(StyleHandle.BORDER_RIGHT_WIDTH_PROP, "1px");
		style3.setProperty(StyleHandle.BORDER_TOP_WIDTH_PROP, "1px");

		StyleHandle style4 = elementFactory.newStyle("Date");
		style4.setDateTimeFormat("MM-dd-yyyy");
		style4.setDateTimeFormatCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM);

		designHandle.getStyles().add(style1);
		designHandle.getStyles().add(style2);
		designHandle.getStyles().add(style3);
		designHandle.getStyles().add(style4);
	}

	void buildBody() throws SemanticException {
		// build grid.
		buildBodyGrid();

		// add text.
		TextItemHandle text = elementFactory.newTextItem(null);
		text.setContentType(DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML);
		text.setContent("<br></br>");
		designHandle.getBody().add(text);

		// build table
		buildBodyTable();
	}

	private void buildBodyTable() throws SemanticException {
		TableHandle table = elementFactory.newTableItem(null, 6, 1, 1, 2);

		designHandle.getBody().add(table);

		table.setProperty(StyleHandle.FONT_FAMILY_PROP, "Georgia");
		table.setProperty(StyleHandle.FONT_SIZE_PROP, DesignChoiceConstants.FONT_SIZE_SMALLER);
		table.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_CENTER);
		table.setProperty(StyleHandle.VERTICAL_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_CENTER);
		table.setWidth("100%");
		table.setProperty(TableHandle.DATA_SET_PROP, "Data Set");

		// Header - row
		RowHandle headerRow1 = (RowHandle) table.getHeader().get(0);
		headerRow1.setProperty(StyleHandle.BACKGROUND_COLOR_PROP, "#C0C0C0");

		// Header - row - cell 1
		CellHandle cell = (CellHandle) headerRow1.getCells().get(0);
		cell.setStyleName("BorderL");
		cell.setProperty(StyleHandle.PADDING_TOP_PROP, "0.5cm");

		LabelHandle label = elementFactory.newLabel(null);
		label.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		label.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_LEFT);
		label.setText("Region");
		cell.getContent().add(label);

		// Header - row - cell 2
		cell = (CellHandle) headerRow1.getCells().get(1);
		cell.setStyleName("BorderL");
		cell.setProperty(StyleHandle.PADDING_TOP_PROP, "0.5cm");

		label = elementFactory.newLabel(null);
		label.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		label.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_LEFT);
		label.setText("Budget($)");
		cell.getContent().add(label);

		// Header - row - cell 3
		cell = (CellHandle) headerRow1.getCells().get(2);
		cell.setStyleName("BorderL");
		cell.setProperty(StyleHandle.PADDING_TOP_PROP, "0.5cm");

		label = elementFactory.newLabel(null);
		label.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		label.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_LEFT);
		label.setText("Current Expenses ($)");
		cell.getContent().add(label);

		// Header - row - cell 4
		cell = (CellHandle) headerRow1.getCells().get(3);
		cell.setStyleName("BorderL");
		cell.setProperty(StyleHandle.PADDING_TOP_PROP, "0.5cm");

		label = elementFactory.newLabel(null);
		label.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		label.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_LEFT);
		label.setText("% Completion of Project");
		cell.getContent().add(label);

		// Header - row - cell 5
		cell = (CellHandle) headerRow1.getCells().get(4);
		cell.setStyleName("BorderL");
		cell.setProperty(StyleHandle.PADDING_TOP_PROP, "0.5cm");

		label = elementFactory.newLabel(null);
		label.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		label.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_LEFT);
		label.setText("Possible Expenses ($)");
		cell.getContent().add(label);

		// Header - row - cell6
		cell = (CellHandle) headerRow1.getCells().get(5);
		cell.setStyleName("BorderL");
		cell.setProperty(StyleHandle.PADDING_TOP_PROP, "0.5cm");

		label = elementFactory.newLabel(null);
		label.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		label.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_LEFT);
		label.setText("Under (Over) Budget ($)");
		cell.getContent().add(label);

		// Detail - row
		RowHandle detailRow1 = (RowHandle) table.getDetail().get(0);

		// Detail - row - cell 1
		cell = (CellHandle) detailRow1.getCells().get(0);

		DataItemHandle data = elementFactory.newDataItem(null);
		data.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_LEFT);
		data.setValueExpr("row[\"Region\"]");
		cell.getContent().add(data);

		// Detail - row - cell 2
		cell = (CellHandle) detailRow1.getCells().get(1);

		data = elementFactory.newDataItem(null);
		data.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_CENTER);
		data.setValueExpr("row[\"Budget\"]");
		data.getPrivateStyle().setNumberFormatCategory(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM);
		data.getPrivateStyle().setNumberFormat("###,###.00");

		cell.getContent().add(data);

		// Detail - row - cell 3
		cell = (CellHandle) detailRow1.getCells().get(2);

		data = elementFactory.newDataItem(null);
		data.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_CENTER);
		data.setValueExpr("row[\"Expense\"]");
		data.getPrivateStyle().setNumberFormatCategory(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM);
		data.getPrivateStyle().setNumberFormat("###,###.00");
		cell.getContent().add(data);

		// Detail - row - cell 4
		cell = (CellHandle) detailRow1.getCells().get(3);
		data = elementFactory.newDataItem(null);
		data.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_CENTER);
		data.setValueExpr("row[\"Completion\"] * 100 +\"%\"");
		cell.getContent().add(data);

		// Detail - row - cell 5
		cell = (CellHandle) detailRow1.getCells().get(4);

		data = elementFactory.newDataItem(null);
		data.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_CENTER);
		data.setValueExpr("row[\"Expense\"]/row[\"Completion\"]");
		data.getPrivateStyle().setNumberFormatCategory(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM);
		data.getPrivateStyle().setNumberFormat("###,###.00");

		cell.getContent().add(data);

		// Detail - row - cell 6
		cell = (CellHandle) detailRow1.getCells().get(5);
		data = elementFactory.newDataItem(null);
		data.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_CENTER);
		data.setValueExpr("row[\"Budget\"] - row[\"Expense\"]/row[\"Completion\"]");
		data.getPrivateStyle().setNumberFormatCategory(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM);
		data.getPrivateStyle().setNumberFormat("###,###.00");
		cell.getContent().add(data);

		// Footer - row1
		RowHandle footerRow1 = (RowHandle) table.getFooter().get(0);

		cell = (CellHandle) footerRow1.getCells().get(0);
		cell.setColumnSpan(6);
		cell.setRowSpan(1);

		TextItemHandle text = elementFactory.newTextItem(null);
		text.setContentType(DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML);
		text.setContent("<br></br>");
		cell.getContent().add(text);

		// Footer - row2
		RowHandle footerRow2 = (RowHandle) table.getFooter().get(1);
		footerRow2.setProperty(StyleHandle.BACKGROUND_COLOR_PROP, "#C0C0C0");

		// Footer - row2 - cell 1
		cell = (CellHandle) footerRow2.getCells().get(0);
		cell.setStyleName("BorderL");

		label = elementFactory.newLabel(null);
		label.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		label.setText("Total:");
		cell.getContent().add(label);

		// Footer - row2 - cell 2
		cell = (CellHandle) footerRow2.getCells().get(1);
		cell.setStyleName("BorderTopBottom");

		data = elementFactory.newDataItem(null);
		data.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		data.setValueExpr("Total.sum(row[\"Budget\"])");
		data.getPrivateStyle().setNumberFormatCategory(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM);
		data.getPrivateStyle().setNumberFormat("$###,###.00");
		cell.getContent().add(data);

		// Footer - row2 - cell 3
		cell = (CellHandle) footerRow2.getCells().get(2);
		cell.setStyleName("BorderTopBottom");

		data = elementFactory.newDataItem(null);
		data.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		data.setValueExpr("Total.sum(row[\"Expense\"])");
		data.getPrivateStyle().setNumberFormatCategory(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM);
		data.getPrivateStyle().setNumberFormat("$###,###.00");
		cell.getContent().add(data);

		// Footer - row2 - cell 4
		cell = (CellHandle) footerRow2.getCells().get(3);
		cell.setStyleName("BorderTopBottom");

		data = elementFactory.newDataItem(null);
		data.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		data.setValueExpr("Total.ave(row[\"Completion\"] * 100) + \"%\"");
		cell.getContent().add(data);

		// Footer - row2 - cell 5
		cell = (CellHandle) footerRow2.getCells().get(4);
		cell.setStyleName("BorderTopBottom");

		data = elementFactory.newDataItem(null);
		data.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		data.setValueExpr("Total.sum(row[\"Expense\"]/row[\"Completion\"])");
		data.getPrivateStyle().setNumberFormatCategory(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM);
		data.getPrivateStyle().setNumberFormat("$###,###.00");
		cell.getContent().add(data);

		// Footer - row2 - cell 6
		cell = (CellHandle) footerRow2.getCells().get(5);
		cell.setStyleName("BorderR");

		data = elementFactory.newDataItem(null);
		data.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		data.setValueExpr("Total.sum(row[\"Budget\"] - row[\"Expense\"]/row[\"Completion\"])");
		data.getPrivateStyle().setNumberFormatCategory(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM);
		data.getPrivateStyle().setNumberFormat("$###,###.00");
		cell.getContent().add(data);

	}

	void buildBodyGrid() throws SemanticException {
		GridHandle grid = elementFactory.newGridItem(null, 1, 1);
		grid.setWidth("100%"); //$NON-NLS-1$

		designHandle.getBody().add(grid);

		CellHandle cell = grid.getCell(0, 0);
		cell.setColumnSpan(1);
		cell.setRowSpan(1);
		cell.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_CENTER);

		GridHandle grid2 = elementFactory.newGridItem(null, 4, 5);
		grid2.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_LEFT);
		grid2.setWidth("80%");
		grid2.setProperty(GridHandle.DATA_SET_PROP, "Data Set");
		cell.getContent().add(grid2);

		// 1. Row1
		RowHandle row1 = (RowHandle) grid2.getRows().get(0);

		// 1.1 Row1 -- Cell1
		CellHandle row1Cell1 = (CellHandle) row1.getCells().get(0);
		ImageHandle image = elementFactory.newImage("Logo");
		image.setImageName("abos_logo");
		row1Cell1.getContent().add(image);

		// 1.2 Row1 -- Cell2
		CellHandle row1Cell2 = (CellHandle) row1.getCells().get(1);

		row1Cell2.setColumnSpan(2);
		row1Cell2.setRowSpan(1);

		TextItemHandle text = elementFactory.newTextItem(null);
		text.setProperty(StyleHandle.FONT_FAMILY_PROP, "Georgia");
		text.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_CENTER);
		text.setContentType(DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML);
		text.setContent(
				"<CENTER><STRONG><H2>ABOS Marketing<br>Project Analysis Report<br></H2></STRONG>Run Date: <value-of>new Date()</value-of></CENTER><br></br>");

		row1Cell2.getContent().add(text);

		// 2. Row2
		RowHandle row2 = (RowHandle) grid2.getRows().get(1);

		// 2.1 Row2 -- Cell1
		CellHandle row2Cell1 = (CellHandle) row2.getCells().get(0);
		row2Cell1.setProperty(StyleHandle.BORDER_LEFT_COLOR_PROP, "#808080");
		row2Cell1.setProperty(StyleHandle.BORDER_LEFT_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		row2Cell1.setProperty(StyleHandle.BORDER_LEFT_WIDTH_PROP, DesignChoiceConstants.LINE_WIDTH_THIN);
		row2Cell1.setProperty(StyleHandle.BORDER_TOP_COLOR_PROP, "#808080");
		row2Cell1.setProperty(StyleHandle.BORDER_TOP_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		row2Cell1.setProperty(StyleHandle.BORDER_TOP_WIDTH_PROP, DesignChoiceConstants.LINE_WIDTH_THIN);

		LabelHandle row2Cell1Lable = elementFactory.newLabel(null);
		row2Cell1Lable.setProperty(StyleHandle.FONT_FAMILY_PROP, "Georgia");
		row2Cell1Lable.setProperty(StyleHandle.FONT_SIZE_PROP, DesignChoiceConstants.FONT_SIZE_SMALLER);
		row2Cell1Lable.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		row2Cell1Lable.setProperty(StyleHandle.PADDING_LEFT_PROP, "1cm");
		row2Cell1Lable.setText("Project Number:");
		row2Cell1.getContent().add(row2Cell1Lable);

		// 2.2 Row2 -- Cell2
		CellHandle row2Cell2 = (CellHandle) row2.getCells().get(1);
		row2Cell2.setProperty(StyleHandle.BORDER_TOP_COLOR_PROP, "#808080");
		row2Cell2.setProperty(StyleHandle.BORDER_TOP_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		row2Cell2.setProperty(StyleHandle.BORDER_TOP_WIDTH_PROP, DesignChoiceConstants.LINE_WIDTH_THIN);

		DataItemHandle row2Cell2Data = elementFactory.newDataItem(null);
		row2Cell2Data.setProperty(StyleHandle.FONT_FAMILY_PROP, "Georgia");
		row2Cell2Data.setProperty(StyleHandle.FONT_SIZE_PROP, DesignChoiceConstants.FONT_SIZE_SMALLER);
		row2Cell2Data.setValueExpr("row[\"Number\"]");
		row2Cell2.getContent().add(row2Cell2Data);

		// 2.3 Row2 -- Cell3
		CellHandle row2Cell3 = (CellHandle) row2.getCells().get(2);
		row2Cell3.setProperty(StyleHandle.BORDER_TOP_COLOR_PROP, "#808080");
		row2Cell3.setProperty(StyleHandle.BORDER_TOP_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		row2Cell3.setProperty(StyleHandle.BORDER_TOP_WIDTH_PROP, DesignChoiceConstants.LINE_WIDTH_THIN);

		LabelHandle row2Cell3Lable = elementFactory.newLabel(null);
		row2Cell3Lable.setProperty(StyleHandle.FONT_FAMILY_PROP, "Georgia");
		row2Cell3Lable.setProperty(StyleHandle.FONT_SIZE_PROP, DesignChoiceConstants.FONT_SIZE_SMALLER);
		row2Cell3Lable.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		row2Cell3Lable.setText("Starting Date:");
		row2Cell3.getContent().add(row2Cell3Lable);

		// 2.4 Row2 -- Cell4
		CellHandle row2Cell4 = (CellHandle) row2.getCells().get(3);
		row2Cell4.setProperty(StyleHandle.BORDER_RIGHT_COLOR_PROP, "#808080");
		row2Cell4.setProperty(StyleHandle.BORDER_RIGHT_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		row2Cell4.setProperty(StyleHandle.BORDER_RIGHT_WIDTH_PROP, DesignChoiceConstants.LINE_WIDTH_THIN);
		row2Cell4.setProperty(StyleHandle.BORDER_TOP_COLOR_PROP, "#808080");
		row2Cell4.setProperty(StyleHandle.BORDER_TOP_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		row2Cell4.setProperty(StyleHandle.BORDER_TOP_WIDTH_PROP, DesignChoiceConstants.LINE_WIDTH_THIN);

		DataItemHandle row2Cell4Data = elementFactory.newDataItem(null);
		row2Cell4Data.setStyleName("Date");
		row2Cell4Data.setProperty(StyleHandle.FONT_FAMILY_PROP, "Georgia");
		row2Cell4Data.setProperty(StyleHandle.FONT_SIZE_PROP, DesignChoiceConstants.FONT_SIZE_SMALLER);
		row2Cell4Data.setValueExpr("row[\"Starting_dt\"]");
		row2Cell4.getContent().add(row2Cell4Data);

		// row3
		RowHandle row3 = (RowHandle) grid2.getRows().get(2);

		// 3.1 Row3 -- Cell1
		CellHandle row3Cell1 = (CellHandle) row3.getCells().get(0);
		row3Cell1.setProperty(StyleHandle.BORDER_LEFT_COLOR_PROP, "#808080");
		row3Cell1.setProperty(StyleHandle.BORDER_LEFT_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		row3Cell1.setProperty(StyleHandle.BORDER_LEFT_WIDTH_PROP, DesignChoiceConstants.LINE_WIDTH_THIN);

		LabelHandle row3Cell1Lable = elementFactory.newLabel(null);
		row3Cell1Lable.setProperty(StyleHandle.FONT_FAMILY_PROP, "Georgia");
		row3Cell1Lable.setProperty(StyleHandle.FONT_SIZE_PROP, DesignChoiceConstants.FONT_SIZE_SMALLER);
		row3Cell1Lable.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		row3Cell1Lable.setProperty(StyleHandle.PADDING_LEFT_PROP, "1cm");
		row3Cell1Lable.setText("Project Manager:");
		row3Cell1.getContent().add(row3Cell1Lable);

		// 3.2 Row3 -- Cell2
		CellHandle row3Cell2 = (CellHandle) row3.getCells().get(1);
		DataItemHandle row3Cell2Data = elementFactory.newDataItem(null);
		row3Cell2Data.setProperty(StyleHandle.FONT_FAMILY_PROP, "Georgia");
		row3Cell2Data.setProperty(StyleHandle.FONT_SIZE_PROP, DesignChoiceConstants.FONT_SIZE_SMALLER);
		row3Cell2Data.setValueExpr("row[\"Manager\"]");
		row3Cell2.getContent().add(row3Cell2Data);

		// 3.3 Row3 -- Cell3
		CellHandle row3Cell3 = (CellHandle) row3.getCells().get(2);
		LabelHandle row3Cell3Lable = elementFactory.newLabel(null);
		row3Cell3Lable.setProperty(StyleHandle.FONT_FAMILY_PROP, "Georgia");
		row3Cell3Lable.setProperty(StyleHandle.FONT_SIZE_PROP, DesignChoiceConstants.FONT_SIZE_SMALLER);
		row3Cell3Lable.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		row3Cell3Lable.setText("Closing Date:");
		row3Cell3.getContent().add(row3Cell3Lable);

		// 3.4 Row3 -- Cell4
		CellHandle row3Cell4 = (CellHandle) row3.getCells().get(3);
		row3Cell4.setProperty(StyleHandle.BORDER_RIGHT_COLOR_PROP, "#808080");
		row3Cell4.setProperty(StyleHandle.BORDER_RIGHT_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		row3Cell4.setProperty(StyleHandle.BORDER_RIGHT_WIDTH_PROP, DesignChoiceConstants.LINE_WIDTH_THIN);

		DataItemHandle row3Cell4Data = elementFactory.newDataItem(null);
		row3Cell4Data.setStyleName("Date");
		row3Cell4Data.setProperty(StyleHandle.FONT_FAMILY_PROP, "Georgia");
		row3Cell4Data.setProperty(StyleHandle.FONT_SIZE_PROP, DesignChoiceConstants.FONT_SIZE_SMALLER);
		row3Cell4Data.setValueExpr("row[\"Closing_dt\"]");
		row3Cell4.getContent().add(row3Cell4Data);

		// row4
		RowHandle row4 = (RowHandle) grid2.getRows().get(3);

		// 4.1 Row4 -- Cell1
		CellHandle row4Cell1 = (CellHandle) row4.getCells().get(0);
		row4Cell1.setProperty(StyleHandle.BORDER_LEFT_COLOR_PROP, "#808080");
		row4Cell1.setProperty(StyleHandle.BORDER_LEFT_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		row4Cell1.setProperty(StyleHandle.BORDER_LEFT_WIDTH_PROP, DesignChoiceConstants.LINE_WIDTH_THIN);

		LabelHandle row4Cell1Lable = elementFactory.newLabel(null);
		row4Cell1Lable.setProperty(StyleHandle.FONT_FAMILY_PROP, "Georgia");
		row4Cell1Lable.setProperty(StyleHandle.FONT_SIZE_PROP, DesignChoiceConstants.FONT_SIZE_SMALLER);
		row4Cell1Lable.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		row4Cell1Lable.setProperty(StyleHandle.PADDING_LEFT_PROP, "1cm");
		row4Cell1Lable.setText("Project Type:");
		row4Cell1.getContent().add(row4Cell1Lable);

		// 4.2 Row4 -- Cell2
		CellHandle row4Cell2 = (CellHandle) row4.getCells().get(1);
		DataItemHandle row4Cell2Data = elementFactory.newDataItem(null);
		row4Cell2Data.setProperty(StyleHandle.FONT_FAMILY_PROP, "Georgia");
		row4Cell2Data.setProperty(StyleHandle.FONT_SIZE_PROP, DesignChoiceConstants.FONT_SIZE_SMALLER);
		row4Cell2Data.setValueExpr("row[\"Type\"]");
		row4Cell2.getContent().add(row4Cell2Data);

		// 4.3 Row4 -- Cell3
		CellHandle row4Cell3 = (CellHandle) row4.getCells().get(2);
		LabelHandle row4Cell3Lable = elementFactory.newLabel(null);
		row4Cell3Lable.setProperty(StyleHandle.FONT_FAMILY_PROP, "Georgia");
		row4Cell3Lable.setProperty(StyleHandle.FONT_SIZE_PROP, DesignChoiceConstants.FONT_SIZE_SMALLER);
		row4Cell3Lable.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		row4Cell3Lable.setText("Contract Value:");
		row4Cell3.getContent().add(row4Cell3Lable);

		// 4.4 Row4 -- Cell4
		CellHandle row4Cell4 = (CellHandle) row4.getCells().get(3);
		row4Cell4.setProperty(StyleHandle.BORDER_RIGHT_COLOR_PROP, "#808080");
		row4Cell4.setProperty(StyleHandle.BORDER_RIGHT_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		row4Cell4.setProperty(StyleHandle.BORDER_RIGHT_WIDTH_PROP, DesignChoiceConstants.LINE_WIDTH_THIN);

		DataItemHandle row4Cell4Data = elementFactory.newDataItem(null);
		row4Cell4Data.setStyleName("Date");
		row4Cell4Data.setProperty(StyleHandle.FONT_FAMILY_PROP, "Georgia");
		row4Cell4Data.setProperty(StyleHandle.FONT_SIZE_PROP, DesignChoiceConstants.FONT_SIZE_SMALLER);
		row4Cell4Data.getPrivateStyle().setDateTimeFormatCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM);
		row4Cell4Data.getPrivateStyle().setDateTimeFormat("$###,###.00");
		row4Cell4Data.setValueExpr("row[\"Value\"]");

		row4Cell4.getContent().add(row4Cell4Data);

		// row5
		RowHandle row5 = (RowHandle) grid2.getRows().get(4);

		// 5.1 Row5 -- Cell1
		CellHandle row5Cell1 = (CellHandle) row5.getCells().get(0);
		row5Cell1.setProperty(StyleHandle.BORDER_BOTTOM_COLOR_PROP, "#808080");
		row5Cell1.setProperty(StyleHandle.BORDER_BOTTOM_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		row5Cell1.setProperty(StyleHandle.BORDER_BOTTOM_WIDTH_PROP, DesignChoiceConstants.LINE_WIDTH_THIN);
		row5Cell1.setProperty(StyleHandle.BORDER_LEFT_COLOR_PROP, "#808080");
		row5Cell1.setProperty(StyleHandle.BORDER_LEFT_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		row5Cell1.setProperty(StyleHandle.BORDER_LEFT_WIDTH_PROP, DesignChoiceConstants.LINE_WIDTH_THIN);

		LabelHandle row5Cell1Lable = elementFactory.newLabel(null);
		row5Cell1Lable.setProperty(StyleHandle.FONT_FAMILY_PROP, "Georgia");
		row5Cell1Lable.setProperty(StyleHandle.FONT_SIZE_PROP, DesignChoiceConstants.FONT_SIZE_SMALLER);
		row5Cell1Lable.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		row5Cell1Lable.setProperty(StyleHandle.PADDING_LEFT_PROP, "1cm");
		row5Cell1Lable.setText("Project Status:");
		row5Cell1.getContent().add(row5Cell1Lable);

		// 5.2 Row5 -- Cell2
		CellHandle row5Cell2 = (CellHandle) row5.getCells().get(1);
		row5Cell2.setProperty(StyleHandle.BORDER_BOTTOM_COLOR_PROP, "#808080");
		row5Cell2.setProperty(StyleHandle.BORDER_BOTTOM_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		row5Cell2.setProperty(StyleHandle.BORDER_BOTTOM_WIDTH_PROP, DesignChoiceConstants.LINE_WIDTH_THIN);

		DataItemHandle row5Cell2Data = elementFactory.newDataItem(null);
		row5Cell2Data.setProperty(StyleHandle.FONT_FAMILY_PROP, "Georgia");
		row5Cell2Data.setProperty(StyleHandle.FONT_SIZE_PROP, DesignChoiceConstants.FONT_SIZE_SMALLER);
		row5Cell2Data.setValueExpr("row[\"Status\"]");
		row5Cell2.getContent().add(row5Cell2Data);

		// 5.3 Row5 -- Cell3
		CellHandle row5Cell3 = (CellHandle) row5.getCells().get(2);
		row5Cell3.setProperty(StyleHandle.BORDER_BOTTOM_COLOR_PROP, "#808080");
		row5Cell3.setProperty(StyleHandle.BORDER_BOTTOM_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		row5Cell3.setProperty(StyleHandle.BORDER_BOTTOM_WIDTH_PROP, DesignChoiceConstants.LINE_WIDTH_THIN);

		LabelHandle row5Cell3Lable = elementFactory.newLabel(null);
		row5Cell3Lable.setProperty(StyleHandle.FONT_FAMILY_PROP, "Georgia");
		row5Cell3Lable.setProperty(StyleHandle.FONT_SIZE_PROP, DesignChoiceConstants.FONT_SIZE_SMALLER);
		row5Cell3Lable.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		row5Cell3Lable.setText("Expected Gross:");
		row5Cell3.getContent().add(row5Cell3Lable);

		// 5.4 Row5 -- Cell4
		CellHandle row5Cell4 = (CellHandle) row5.getCells().get(3);
		row5Cell4.setProperty(StyleHandle.BORDER_BOTTOM_COLOR_PROP, "#808080");
		row5Cell4.setProperty(StyleHandle.BORDER_BOTTOM_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		row5Cell4.setProperty(StyleHandle.BORDER_BOTTOM_WIDTH_PROP, DesignChoiceConstants.LINE_WIDTH_THIN);
		row5Cell4.setProperty(StyleHandle.BORDER_RIGHT_COLOR_PROP, "#808080");
		row5Cell4.setProperty(StyleHandle.BORDER_RIGHT_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		row5Cell4.setProperty(StyleHandle.BORDER_RIGHT_WIDTH_PROP, DesignChoiceConstants.LINE_WIDTH_THIN);

		DataItemHandle row5Cell4Data = elementFactory.newDataItem(null);
		row5Cell4Data.setStyleName("Date");
		row5Cell4Data.setProperty(StyleHandle.FONT_FAMILY_PROP, "Georgia");
		row5Cell4Data.setProperty(StyleHandle.FONT_SIZE_PROP, DesignChoiceConstants.FONT_SIZE_SMALLER);
		row5Cell4Data.getPrivateStyle().setDateTimeFormatCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM);
		row5Cell4Data.getPrivateStyle().setDateTimeFormat("$###,###.00");
		row5Cell4Data.setValueExpr("row[\"Billing\"]");

		row5Cell4.getContent().add(row5Cell4Data);
	}

}