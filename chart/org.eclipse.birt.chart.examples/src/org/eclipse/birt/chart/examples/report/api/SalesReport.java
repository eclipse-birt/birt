/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.examples.report.api;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl;
import org.eclipse.birt.chart.model.attribute.impl.GradientImpl;
import org.eclipse.birt.chart.model.attribute.impl.JavaNumberFormatSpecifierImpl;
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
import org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.impl.PieSeriesImpl;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.IDesignEngine;
import org.eclipse.birt.report.model.api.IDesignEngineFactory;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.ScriptDataSetHandle;
import org.eclipse.birt.report.model.api.ScriptDataSourceHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;

import com.ibm.icu.util.ULocale;

public class SalesReport {

	ReportDesignHandle reportDesignHandle = null;

	ElementFactory elementFactory = null;

	ComputedColumn cs1, cs2, cs3 = null;

	public static void main(String[] args) throws SemanticException, IOException {
		new SalesReport().createReport();
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
		createStyles();
		createBody();

		String outputPath = "output";//$NON-NLS-1$
		File outputFolder = new File(outputPath);
		if (!outputFolder.exists() && !outputFolder.mkdir()) {
			throw new IOException("Can not create the output folder");//$NON-NLS-1$
		}
		reportDesignHandle.saveAs(outputPath + "/" + "SalesReport.rptdesign");//$NON-NLS-1$//$NON-NLS-2$

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
				+ "sourcedata = new Array( new Array(3), new Array(3), new Array(3), "//$NON-NLS-1$
				+ "new Array(3), new Array(3), new Array(3));"//$NON-NLS-1$
				+ "sourcedata[0][0] = 10; "//$NON-NLS-1$
				+ "sourcedata[0][1] = \"Chris Kwai\";"//$NON-NLS-1$
				+ "sourcedata[0][2] = 2413;"//$NON-NLS-1$

				+ "sourcedata[1][0] = 10; "//$NON-NLS-1$
				+ "sourcedata[1][1] = \"Ice Bella\";"//$NON-NLS-1$
				+ "sourcedata[1][2] = 2304;"//$NON-NLS-1$

				+ "sourcedata[2][0] = 10; "//$NON-NLS-1$
				+ "sourcedata[2][1] = \"Nola Dicci\";"//$NON-NLS-1$
				+ "sourcedata[2][2] = 1998;"//$NON-NLS-1$

				+ "sourcedata[3][0] = 11; "//$NON-NLS-1$
				+ "sourcedata[3][1] = \"Chris Kwai\";"//$NON-NLS-1$
				+ "sourcedata[3][2] = 2087;"//$NON-NLS-1$

				+ "sourcedata[4][0] = 11; "//$NON-NLS-1$
				+ "sourcedata[4][1] = \"Ice Bella\";"//$NON-NLS-1$
				+ "sourcedata[4][2] = 2502;"//$NON-NLS-1$

				+ "sourcedata[5][0] = 11; "//$NON-NLS-1$
				+ "sourcedata[5][1] = \"Nola Dicci\";"//$NON-NLS-1$
				+ "sourcedata[5][2] = 2011;");//$NON-NLS-1$

		// Set fetch( ) in code
		dataSetHandle.setFetch("if ( i < 6 ){"//$NON-NLS-1$
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

	private void createStyles() throws SemanticException {
		StyleHandle labelStyle = elementFactory.newStyle("Label");//$NON-NLS-1$
		labelStyle.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		labelStyle.setProperty(StyleHandle.FONT_FAMILY_PROP, "Arial Black");//$NON-NLS-1$
		labelStyle.setProperty(StyleHandle.COLOR_PROP, "#008000");//$NON-NLS-1$

		StyleHandle dataStyle = elementFactory.newStyle("Data");//$NON-NLS-1$
		dataStyle.setProperty(StyleHandle.FONT_WEIGHT_PROP, DesignChoiceConstants.FONT_WEIGHT_BOLD);
		dataStyle.setProperty(StyleHandle.FONT_FAMILY_PROP, "Century");//$NON-NLS-1$
		dataStyle.setProperty(StyleHandle.COLOR_PROP, "#009B9B");//$NON-NLS-1$

		reportDesignHandle.getStyles().add(labelStyle);
		reportDesignHandle.getStyles().add(dataStyle);
	}

	private byte[] load(String fileName) throws IOException {
		InputStream is = null;

		is = new BufferedInputStream(this.getClass().getResourceAsStream(fileName));
		byte data[] = null;
		if (is != null) {
			try {
				data = new byte[is.available()];
				is.read(data);
			} catch (IOException e1) {
				throw e1;
			} finally {
				if (is != null) {
					is.close();
				}
			}
		}
		return data;
	}

	private void createBody() throws SemanticException {

		// Grid 1
		GridHandle grid1 = elementFactory.newGridItem("grid1", 3, 1);//$NON-NLS-1$
		grid1.setWidth("100%");//$NON-NLS-1$
		reportDesignHandle.getBody().add(grid1);

		// First Grid Row
		RowHandle row = (RowHandle) grid1.getRows().get(0);

		// Cell (1st Row)
		CellHandle cell = (CellHandle) row.getCells().get(0);
		cell.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_CENTER);
		try {
			EmbeddedImage image1 = StructureFactory.createEmbeddedImage();
			image1.setType(DesignChoiceConstants.IMAGE_TYPE_IMAGE_JPEG);
			image1.setData(load("logo1.jpg"));//$NON-NLS-1$
			image1.setName("logo1");//$NON-NLS-1$
			reportDesignHandle.addImage(image1);
			ImageHandle imageHandle = elementFactory.newImage("handle");//$NON-NLS-1$
			imageHandle.setSource(DesignChoiceConstants.IMAGE_REF_TYPE_EMBED);
			imageHandle.setImageName("logo1");//$NON-NLS-1$
			cell.getContent().add(imageHandle);
		} catch (IOException e) {
			e.printStackTrace();
		}

		cell = (CellHandle) row.getCells().get(1);
		cell.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_CENTER);
		try {
			EmbeddedImage image2 = StructureFactory.createEmbeddedImage();
			image2.setType(DesignChoiceConstants.IMAGE_TYPE_IMAGE_JPEG);
			image2.setData(load("logo2.jpg"));//$NON-NLS-1$
			image2.setName("logo2");//$NON-NLS-1$
			reportDesignHandle.addImage(image2);
			ImageHandle imageHandle2 = elementFactory.newImage("imageHandle2");//$NON-NLS-1$
			imageHandle2.setSource(DesignChoiceConstants.IMAGE_REF_TYPE_EMBED);
			imageHandle2.setImageName("logo2");//$NON-NLS-1$
			cell.getContent().add(imageHandle2);
		} catch (IOException e) {
			e.printStackTrace();
		}

		cell = (CellHandle) row.getCells().get(2);
		cell.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_CENTER);
		try {
			EmbeddedImage image3 = StructureFactory.createEmbeddedImage();
			image3.setType(DesignChoiceConstants.IMAGE_TYPE_IMAGE_JPEG);
			image3.setData(load("logo3.jpg"));//$NON-NLS-1$
			image3.setName("logo3");//$NON-NLS-1$
			reportDesignHandle.addImage(image3);
			ImageHandle imageHandle3 = elementFactory.newImage("imageHandle3");//$NON-NLS-1$
			imageHandle3.setSource(DesignChoiceConstants.IMAGE_REF_TYPE_EMBED);
			imageHandle3.setImageName("logo3");//$NON-NLS-1$
			cell.getContent().add(imageHandle3);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Grid 2
		GridHandle grid2 = elementFactory.newGridItem("grid2", 2, 1);//$NON-NLS-1$
		grid2.setWidth("100%");//$NON-NLS-1$
		reportDesignHandle.getBody().add(grid2);

		// First Grid Row
		row = (RowHandle) grid2.getRows().get(0);

		// Cell (1st Row)
		cell = (CellHandle) row.getCells().get(0);
		cell.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_RIGHT);
		cell.setProperty(StyleHandle.VERTICAL_ALIGN_PROP, DesignChoiceConstants.VERTICAL_ALIGN_MIDDLE);

		TableHandle table = elementFactory.newTableItem(null, 3, 1, 1, 1);
		table.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_CENTER);
		table.setWidth("80%");//$NON-NLS-1$
		table.setProperty(TableHandle.DATA_SET_PROP, "Data Set");//$NON-NLS-1$

		PropertyHandle computedSet = table.getColumnBindings();
		cs1.setExpression("dataSetRow[\"Month\"]");//$NON-NLS-1$
		computedSet.addItem(cs1);
		cs2.setExpression("dataSetRow[\"Product\"]");//$NON-NLS-1$
		computedSet.addItem(cs2);
		cs3.setExpression("dataSetRow[\"Amount\"]");//$NON-NLS-1$
		computedSet.addItem(cs3);

		// Table sorter
		SortKey key = StructureFactory.createSortKey();
		key.setKey("row[\"Month\"]");//$NON-NLS-1$
		key.setDirection("asc");//$NON-NLS-1$
		PropertyHandle sort = table.getPropertyHandle(TableHandle.SORT_PROP);
		sort.addItem(key);

		// Header
		RowHandle header = (RowHandle) table.getHeader().get(0);

		CellHandle tcell = (CellHandle) header.getCells().get(0);
		LabelHandle label = elementFactory.newLabel(null);
		label.setText("Product");//$NON-NLS-1$
		label.setStyleName("Label");//$NON-NLS-1$
		tcell.getContent().add(label);

		tcell = (CellHandle) header.getCells().get(1);
		label = elementFactory.newLabel(null);
		label.setText("Month");//$NON-NLS-1$
		label.setStyleName("Label");//$NON-NLS-1$
		tcell.getContent().add(label);

		tcell = (CellHandle) header.getCells().get(2);
		label = elementFactory.newLabel(null);
		label.setText("Amount");//$NON-NLS-1$
		label.setStyleName("Label");//$NON-NLS-1$
		tcell.getContent().add(label);

		// Table Group
		TableGroupHandle group = elementFactory.newTableGroup();
		group.setKeyExpr("row[\"Product\"]");//$NON-NLS-1$
		table.getGroups().add(group);

		RowHandle groupHeader = elementFactory.newTableRow(3);
		tcell = (CellHandle) groupHeader.getCells().get(0);
		tcell.setDrop(DesignChoiceConstants.DROP_TYPE_DETAIL);
		DataItemHandle data = elementFactory.newDataItem(null);
		data.setStyleName("Data");//$NON-NLS-1$
		data.setResultSetColumn(cs2.getName());
		tcell.getContent().add(data);
		group.getHeader().add(groupHeader);

		RowHandle groupFooter = elementFactory.newTableRow(3);
		tcell = (CellHandle) groupFooter.getCells().get(0);
		tcell.setProperty(StyleHandle.BORDER_BOTTOM_COLOR_PROP, "#FF8000");//$NON-NLS-1$
		tcell.setProperty(StyleHandle.BORDER_BOTTOM_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		tcell.setProperty(StyleHandle.BORDER_BOTTOM_WIDTH_PROP, DesignChoiceConstants.LINE_WIDTH_THIN);
		tcell = (CellHandle) groupFooter.getCells().get(1);
		tcell.setProperty(StyleHandle.BORDER_BOTTOM_COLOR_PROP, "#FF8000");//$NON-NLS-1$
		tcell.setProperty(StyleHandle.BORDER_BOTTOM_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		tcell.setProperty(StyleHandle.BORDER_BOTTOM_WIDTH_PROP, DesignChoiceConstants.LINE_WIDTH_THIN);
		tcell = (CellHandle) groupFooter.getCells().get(2);
		tcell.setProperty(StyleHandle.BORDER_BOTTOM_COLOR_PROP, "#FF8000");//$NON-NLS-1$
		tcell.setProperty(StyleHandle.BORDER_BOTTOM_STYLE_PROP, DesignChoiceConstants.LINE_STYLE_SOLID);
		tcell.setProperty(StyleHandle.BORDER_BOTTOM_WIDTH_PROP, DesignChoiceConstants.LINE_WIDTH_THIN);
		group.getFooter().add(groupFooter);

		// Detail
		RowHandle detail = (RowHandle) table.getDetail().get(0);
		tcell = (CellHandle) detail.getCells().get(1);
		data = elementFactory.newDataItem(null);
		data.setStyleName("Data");//$NON-NLS-1$
		data.setResultSetColumn(cs1.getName());
		tcell.getContent().add(data);

		tcell = (CellHandle) detail.getCells().get(2);
		data = elementFactory.newDataItem(null);
		data.setStyleName("Data");//$NON-NLS-1$
		data.setResultSetColumn(cs3.getName());
		tcell.getContent().add(data);

		cell.getContent().add(table);

		cell = (CellHandle) row.getCells().get(1);
		cell.setProperty(StyleHandle.TEXT_ALIGN_PROP, DesignChoiceConstants.TEXT_ALIGN_CENTER);
		cell.getContent().add(createPieChart());
	}

	private ExtendedItemHandle createPieChart() {
		ExtendedItemHandle eih = elementFactory.newExtendedItem(null, "Chart");//$NON-NLS-1$

		try {
			eih.setHeight("288pt");//$NON-NLS-1$
			eih.setWidth("252pt");//$NON-NLS-1$
			eih.setProperty(ExtendedItemHandle.DATA_SET_PROP, "Data Set");//$NON-NLS-1$
			PropertyHandle computedSet = eih.getColumnBindings();
			cs1.setExpression("dataSetRow[\"Month\"]");//$NON-NLS-1$
			computedSet.addItem(cs1);
			cs2.setExpression("dataSetRow[\"Product\"]");//$NON-NLS-1$
			computedSet.addItem(cs2);
			cs3.setExpression("dataSetRow[\"Amount\"]");//$NON-NLS-1$
			computedSet.addItem(cs3);
		} catch (SemanticException e) {
			e.printStackTrace();
		}

		ChartWithoutAxes cwoaPie = ChartWithoutAxesImpl.create();
		cwoaPie.setType("Pie Chart");//$NON-NLS-1$
		cwoaPie.setSubType("Standard Pie Chart");//$NON-NLS-1$
		cwoaPie.getTitle().setVisible(false);
		cwoaPie.getBlock().setBounds(BoundsImpl.create(0, 0, 252, 288));
		cwoaPie.getBlock().getOutline().setVisible(true);
		cwoaPie.getBlock().setBackground(GradientImpl.create(ColorDefinitionImpl.create(204, 254, 204),
				ColorDefinitionImpl.create(254, 226, 240), -35, false));
		cwoaPie.getPlot().getClientArea().setBackground(ColorDefinitionImpl.TRANSPARENT());
		cwoaPie.getLegend().setBackground(ColorDefinitionImpl.TRANSPARENT());
		cwoaPie.getLegend().getClientArea().setBackground(ColorDefinitionImpl.TRANSPARENT());

		SampleData sd = DataFactory.eINSTANCE.createSampleData();
		BaseSampleData sdBase = DataFactory.eINSTANCE.createBaseSampleData();
		sdBase.setDataSetRepresentation("Category-A, Category-B");//$NON-NLS-1$
		sd.getBaseSampleData().add(sdBase);

		OrthogonalSampleData sdOrthogonal = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sdOrthogonal.setDataSetRepresentation("4,12");//$NON-NLS-1$
		sdOrthogonal.setSeriesDefinitionIndex(0);
		sd.getOrthogonalSampleData().add(sdOrthogonal);

		cwoaPie.setSampleData(sd);

		Series seCategory = SeriesImpl.create();
		Query query = QueryImpl.create("row[\"Product\"]");//$NON-NLS-1$
		seCategory.getDataDefinition().add(query);

		SeriesDefinition series = SeriesDefinitionImpl.create();
		series.getSeries().add(seCategory);
		cwoaPie.getSeriesDefinitions().add(series);

		PieSeries ps = (PieSeries) PieSeriesImpl.create();
		Query query2 = QueryImpl.create("row[\"Amount\"]");//$NON-NLS-1$
		ps.getDataDefinition().add(query2);

		SeriesDefinition seGroup = SeriesDefinitionImpl.create();
		Query query1 = QueryImpl.create("row[\"Month\"]");//$NON-NLS-1$
		seGroup.setQuery(query1);
		series.getSeriesPalette().shift(-2);
		series.getSeriesDefinitions().add(seGroup);
		seGroup.getSeries().add(ps);

		DataPointComponent dpc = DataPointComponentImpl.create(DataPointComponentType.ORTHOGONAL_VALUE_LITERAL,
				JavaNumberFormatSpecifierImpl.create("###,###"));//$NON-NLS-1$
		ps.getDataPoint().getComponents().clear();
		ps.getDataPoint().getComponents().add(dpc);
		ps.getLabel().setVisible(true);

		try {
			// Add chart instance to IReportItem
			eih.getReportItem().setProperty("chart.instance", cwoaPie);//$NON-NLS-1$
		} catch (ExtendedElementException e) {
			e.printStackTrace();
		}

		return eih;
	}
}