package org.eclipse.birt.report.tests.model.APISamples;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.MapRuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.SimpleMasterPageHandle;
import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.elements.TableGroup;

import com.ibm.icu.util.ULocale;

/**
 * 
 * This is a demo of how to use Model API to create report design. <br>
 * The generated report contains data source, dataset, report parameter, table,
 * style and extends elements from library
 * 
 */
public class Sample2 {

	ReportDesignHandle designHandle = null;
	LibraryHandle libraryHandle = null;
	ElementFactory designFactory = null;
	ElementFactory libraryFactory = null;
	StructureFactory structFactory = null;

	public static void main(String[] args) throws SemanticException, IOException, DesignFileException {
		new Sample2().buildReport();
	}

	void buildReport() throws SemanticException, IOException, DesignFileException {
		SessionHandle session = DesignEngine.newSession(ULocale.ENGLISH);
		designHandle = session.createDesign();
		libraryHandle = session.createLibrary();
		designFactory = designHandle.getElementFactory();
		libraryFactory = libraryHandle.getElementFactory();
		structFactory = new StructureFactory();

		buildMasterPages();
		buildDataSource();
		buildDataSet();
		buildParameter();
		buildBody();

		designHandle.saveAs("Sample2.rptdesign");

	}

	void buildDataSource() throws SemanticException {

		OdaDataSourceHandle dsHandle = designFactory.newOdaDataSource("Data Source",
				"org.eclipse.birt.report.data.oda.jdbc");
		dsHandle.setProperty("odaDriverClass", "net.sourceforge.jtds.jdbc.Driver");
		dsHandle.setProperty("odaURL", "jdbc:jtds:sqlserver://spmdb:1433/gui");
		dsHandle.setProperty("odaUser", "sa");
		dsHandle.setProperty("odaPassword", "sa");

		designHandle.getDataSources().add(dsHandle);

	}

	void buildDataSet() throws SemanticException {

		OdaDataSetHandle dsHandle = designFactory.newOdaDataSet("ds",
				"org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet");
		dsHandle.setDataSource("Data Source");
		dsHandle.setQueryText("Select \"Transaction\".trans_id," + "\"Transaction\".trans_amt," + "Trans_desc.trans_ty,"
				+ "Account.account_nm," + "\"Transaction\".trand_dt," + "\"Transaction\".account_id"
				+ "FROM \"Transaction\" , Trans_desc , Account" + "Where\"Transaction\".account_id = Account.account_id"
				+ "and \"Transaction\".trans_cd = Trans_desc.trans_cd)");

		designHandle.getDataSets().add(dsHandle);

	}

	void buildParameter() throws SemanticException {
		ScalarParameterHandle param = designFactory.newScalarParameter("month");
		param.setValueType(DesignChoiceConstants.PARAM_VALUE_TYPE_DYNAMIC);
		param.setDataType(DesignChoiceConstants.PARAM_TYPE_DATETIME);
		param.setAllowBlank(false);
		param.setControlType(DesignChoiceConstants.PARAM_CONTROL_LIST_BOX);
		param.setDataSetName("ds");
		param.setValueExpr("row[\"trand_dt\"]");
		param.setMustMatch(false);
		param.setFixedOrder(true);
		param.setCategory("Unformatted");

		designHandle.getParameters().add(param);

	}

	void buildMasterPages() throws SemanticException, IOException {

		SimpleMasterPageHandle masterpage = (SimpleMasterPageHandle) designFactory
				.newSimpleMasterPage("Simple MasterPage");
		// Grid in master page header
		GridHandle grid = designFactory.newGridItem(null, 2, 1);
		grid.setProperty("marginBottom", "1cm");
		grid.setProperty("textAlign", "right");
		grid.setProperty("verticalAlign", "baseline");
		grid.setProperty("width", "100%");
		ColumnHandle column0 = (ColumnHandle) grid.getColumns().get(0);
		column0.setProperty("width", "4.26in");
		ColumnHandle column1 = (ColumnHandle) grid.getColumns().get(1);
		column1.setProperty("width", "1.625in");

		CellHandle cell0 = grid.getCell(0, 0);
		TextItemHandle text = designFactory.newTextItem("masterpage text");
		text.setProperty("fontSize", "14pt");
		text.setProperty("fontWeight", "bold");
		text.setProperty("display", "block");
		text.setProperty("contentType", "plain");
		text.setContent("Created by: Actuate");
		cell0.getContent().add(text);

		CellHandle cell1 = grid.getCell(0, 1);
		EmbeddedImage image = structFactory.createEmbeddedImage();
		image.setType(DesignChoiceConstants.IMAGE_TYPE_IMAGE_JPEG);
		image.setData(load("images/actuate_logo.jpg")); //$NON-NLS-1$
		image.setName("actuate_logo"); //$NON-NLS-1$
		designHandle.addImage(image);

		ImageHandle image1 = designFactory.newImage("Logo");
		image1.setImageName("actuate_logo");
		cell1.getContent().add(image1);

		// Text in master page footer
		TextItemHandle text1 = designFactory.newTextItem("pageheader text");
		text1.setContentType(DesignChoiceConstants.TEXT_CONTENT_TYPE_HTML);
		text1.setContent("<value-of>new Date()</value-of>");

		masterpage.getPageHeader().add(grid);
		masterpage.getPageFooter().add(text1);

		designHandle.getMasterPages().add(masterpage);

	}

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

	void buildBody() throws SemanticException, DesignFileException {

		buildLibrary();
		buildTheme();

		designHandle.includeLibrary("library", "library");
		// Extends label in library
		LabelHandle label = (LabelHandle) libraryHandle.findElement("label");
		LabelHandle r_label = (LabelHandle) designFactory.newElementFrom(label, "report_label");
		r_label.setProperty("fontSize", "x-large");
		r_label.setProperty("fontWeight", "bold");
		r_label.setProperty("marginBottom", "0.5cm");
		r_label.setProperty("paddingBottom", "0cm");
		r_label.setProperty("textAlign", "center");

		designHandle.getBody().add(r_label);

		// Specify theme for the report
		designHandle.setThemeName("theme1");

		buildStyles();
		buildTable();
		buildGroups();

	}

	void buildStyles() throws SemanticException {
		// New a style for table detail
		SharedStyleHandle style = designFactory.newStyle("table-detail");
		style.setBorderBottomStyle("solid");
		style.setProperty("borderBottomColor", "#000000");
		style.setProperty("borderBottomStyle", "solid");
		style.setProperty("borderBottomWidth", "thin");
		style.setProperty("borderLeftColor", "#000000");
		style.setProperty("borderLeftStyle", "solid");
		style.setProperty("borderLeftWidth", "thin");
		style.setProperty("borderRightColor", "#000000");
		style.setProperty("borderRightStyle", "solid");
		style.setProperty("borderRightWidth", "thin");
		style.setProperty("borderTopColor", "#000000");
		style.setProperty("borderTopStyle", "solid");
		style.setProperty("borderTopWidth", "thin");

		// new a styel for table footer
		SharedStyleHandle style2 = designFactory.newStyle("table-footer");
		style.setBorderBottomStyle("solid");
		style.setProperty("borderBottomColor", "#000000");
		style.setProperty("borderBottomStyle", "solid");
		style.setProperty("borderBottomWidth", "thin");
		style.setProperty("borderLeftColor", "#000000");
		style.setProperty("borderLeftStyle", "solid");
		style.setProperty("borderLeftWidth", "thin");
		style.setProperty("borderRightColor", "#000000");
		style.setProperty("borderRightStyle", "solid");
		style.setProperty("borderRightWidth", "thin");
		style.setProperty("borderTopColor", "#000000");
		style.setProperty("borderTopStyle", "solid");
		style.setProperty("borderTopWidth", "thin");

		designHandle.getStyles().add(style);
		designHandle.getStyles().add(style2);

	}

	void buildTable() throws SemanticException {

		// Add a 3*7 table
		TableHandle table = designFactory.newTableItem("table", 7);
		table.setWidth("100%");
		table.setDataSet(designHandle.findDataSet("ds"));

		// table header
		RowHandle tableheader = (RowHandle) table.getHeader().get(0);

		// cell0 in table header
		CellHandle cell = (CellHandle) tableheader.getCells().get(0);
		cell.setColumnSpan(2);
		cell.setRowSpan(1);

		// cell1 in table header
		cell = (CellHandle) tableheader.getCells().get(1);
		cell.setColumnSpan(5);
		cell.setRowSpan(1);

		// table detail
		RowHandle tabledetail = (RowHandle) table.getDetail().get(0);

		// Add a data to cell 0 in table detail
		cell = (CellHandle) tabledetail.getCells().get(0);
		DataItemHandle data = designFactory.newDataItem("data");
		data.setValueExpr("row[\"trand_dt\"]");
		data.setStyleName("month");
		cell.getContent().add(data);

		// Add a data to cell 1 in table detail
		cell = (CellHandle) tabledetail.getCells().get(1);
		data = designFactory.newDataItem("data");
		data.setValueExpr("row[\"trand_dt\"]");
		data.setStyleName("day");
		cell.getContent().add(data);

		// Add a data to cell 2 in table detail
		cell = (CellHandle) tabledetail.getCells().get(2);
		data = designFactory.newDataItem("data");
		data.setValueExpr("row[\"trans_id\"]");
		cell.getContent().add(data);

		// Add a data to cell 3 in table detail
		cell = (CellHandle) tabledetail.getCells().get(3);
		data = designFactory.newDataItem("data");
		data.setValueExpr("row[\"trand_ty\"]");
		cell.getContent().add(data);

		// Add a data to cell 4 in table detail
		cell = (CellHandle) tabledetail.getCells().get(4);
		data = designFactory.newDataItem("data");
		data.setValueExpr("if (row[\"trans_amt\"]>=0)" + "row[\"trans_amt\"];" + "else null");
		cell.getContent().add(data);

		// Add a data to cell 5 in table detail
		cell = (CellHandle) tabledetail.getCells().get(5);
		data = designFactory.newDataItem("data");
		data.setValueExpr("if (row[\"trans_amt\"]&lt;=0)" + "row[\"trans_amt\"];" + "else null;");
		cell.getContent().add(data);

		// Add a data to cell 6 in table detail
		cell = (CellHandle) tabledetail.getCells().get(6);
		data = designFactory.newDataItem("data");
		data.setValueExpr("row[\"trans_amt\"]");
		data.setStyleName("TransactionType");
		cell.getContent().add(data);

		// table footer
		RowHandle tablefooter = (RowHandle) table.getFooter().get(0);

		// Add a label to cell0 in table footer
		cell = (CellHandle) tablefooter.getCells().get(0);
		cell.setColumnSpan(4);
		cell.setRowSpan(1);

		LabelHandle label1 = designFactory.newLabel("label");
		label1.setProperty("fontWeight", "bold");
		label1.setText("Overall Balance:");

		cell.getContent().add(label1);

		// Add a data to cell1 in table footer
		cell = (CellHandle) tablefooter.getCells().get(1);
		cell.setColumnSpan(2);
		cell.setRowSpan(1);

		DataItemHandle data1 = designFactory.newDataItem("data");
		data1.setProperty("textAlign", "left");
		data1.setValueExpr("Total.sum(row[\"trans_amt\"])");

		cell.getContent().add(data1);

		// Add a data to cell2 in table footer
		cell = (CellHandle) tablefooter.getCells().get(2);

		DataItemHandle data2 = designFactory.newDataItem("data");
		data1.setStyleName("TransactionType");
		data1.setValueExpr("Total.sum(row[\"trans_amt\"])");

		cell.getContent().add(data2);

		designHandle.getBody().add(table);

	}

	void buildGroups() throws SemanticException {

		// Group1 "AccountName"
		TableGroupHandle group1 = designFactory.newTableGroup();
		group1.setName("AccountName");
		group1.setInterval("interval");
		group1.setIntervalRange(3.0);
		group1.setSortDirection("asc");
		group1.setKeyExpr("row[\"account_id\"]");
		group1.setTocExpression("Account Name:" + " " + "row[\"account_id\"]");

		// Group1 header
		RowHandle groupheader1 = (RowHandle) group1.getHeader().get(0);
		groupheader1.setProperty("fontWeight", "bold");

		// Add a label to cell0 in group1 header
		CellHandle cell = (CellHandle) groupheader1.getCells().get(0);
		cell.setColumnSpan(2);
		cell.setRowSpan(1);
		cell.setProperty("borderBottomColor", "#000000");
		cell.setProperty("borderBottomStyle", "solid");
		cell.setProperty("borderBottomWidth", "thin");
		cell.setProperty("borderLeftColor", "#000000");
		cell.setProperty("borderLeftStyle", "solid");
		cell.setProperty("borderLeftWidth", "thin");
		cell.setProperty("borderRightColor", "#000000");
		cell.setProperty("borderRightStyle", "solid");
		cell.setProperty("borderRightWidth", "thin");
		cell.setProperty("borderTopColor", "#000000");
		cell.setProperty("borderTopStyle", "solid");
		cell.setProperty("borderTopWidth", "thin");
		cell.setProperty("paddingTop", "0cm");

		LabelHandle label = designFactory.newLabel("label");
		label.setProperty("fontWeight", "bold");
		label.setText("Account Name:");

		cell.getContent().add(label);

		// Add a data to cell1 in group1 header
		cell = (CellHandle) groupheader1.getCells().get(1);
		cell.setColumnSpan(5);
		cell.setRowSpan(1);
		cell.setProperty("borderBottomColor", "#000000");
		cell.setProperty("borderBottomStyle", "solid");
		cell.setProperty("borderBottomWidth", "thin");
		cell.setProperty("borderLeftColor", "#000000");
		cell.setProperty("borderLeftStyle", "solid");
		cell.setProperty("borderLeftWidth", "thin");
		cell.setProperty("borderRightColor", "#000000");
		cell.setProperty("borderRightStyle", "solid");
		cell.setProperty("borderRightWidth", "thin");
		cell.setProperty("borderTopColor", "#000000");
		cell.setProperty("borderTopStyle", "solid");
		cell.setProperty("borderTopWidth", "thin");
		cell.setProperty("paddingTop", "0cm");

		DataItemHandle data = designFactory.newDataItem("data");
		data.setProperty("marginTop", "0.5pt");
		data.setValueExpr("row[\"account_id\"]");

		cell.getContent().add(data);

		// Group2 "year"
		TableGroupHandle group2 = designFactory.newTableGroup();
		group2.setName("year");
		group2.setInterval("year");
		group2.setIntervalRange(1.0);
		group2.setSortDirection("asc");
		group2.setKeyExpr("row[\"trand_dt\"]");
		group2.setTocExpression("Year:" + " " + "row[\"trand_dt\"].getFullYear()");

		// Group2 header
		RowHandle groupheader2 = (RowHandle) group2.getHeader().get(0);
		groupheader2.setProperty("fontWeight", "bold");
		groupheader2.setProperty("backgroundColor", "#C0C0C0");

		// Add a label to cell0 in group2 header
		cell = (CellHandle) groupheader2.getCells().get(0);
		cell.setColumnSpan(2);
		cell.setRowSpan(1);
		cell.setProperty("borderBottomColor", "#000000");
		cell.setProperty("borderBottomStyle", "solid");
		cell.setProperty("borderBottomWidth", "thin");
		cell.setProperty("borderLeftColor", "#000000");
		cell.setProperty("borderLeftStyle", "solid");
		cell.setProperty("borderLeftWidth", "thin");
		cell.setProperty("borderRightColor", "#000000");
		cell.setProperty("borderRightStyle", "solid");
		cell.setProperty("borderRightWidth", "thin");
		cell.setProperty("borderTopColor", "#000000");
		cell.setProperty("borderTopStyle", "solid");
		cell.setProperty("borderTopWidth", "thin");
		cell.setProperty("paddingTop", "0cm");

		label = designFactory.newLabel("label");
		label.setProperty("fontWeight", "bold");
		label.setText("Year:");

		cell.getContent().add(label);

		// Add a data to cell1 in group2 header
		cell = (CellHandle) groupheader2.getCells().get(1);
		cell.setColumnSpan(5);
		cell.setRowSpan(1);
		cell.setProperty("borderBottomColor", "#000000");
		cell.setProperty("borderBottomStyle", "solid");
		cell.setProperty("borderBottomWidth", "thin");
		cell.setProperty("borderLeftColor", "#000000");
		cell.setProperty("borderLeftStyle", "solid");
		cell.setProperty("borderLeftWidth", "thin");
		cell.setProperty("borderRightColor", "#000000");
		cell.setProperty("borderRightStyle", "solid");
		cell.setProperty("borderRightWidth", "thin");
		cell.setProperty("borderTopColor", "#000000");
		cell.setProperty("borderTopStyle", "solid");
		cell.setProperty("borderTopWidth", "thin");
		cell.setProperty("paddingTop", "0cm");

		data = designFactory.newDataItem("data");
		data.setStyleName("year");
		data.setValueExpr("row[\"trand_dt\"]");

		cell.getContent().add(data);

		// group1 footer
		RowHandle groupfooter2 = (RowHandle) group2.getFooter().get(0);

		// Add a label to cell0 in group2 footer
		cell = (CellHandle) groupfooter2.getCells().get(0);
		cell.setColumnSpan(4);
		cell.setRowSpan(1);
		cell.setProperty("borderBottomColor", "#000000");
		cell.setProperty("borderBottomStyle", "solid");
		cell.setProperty("borderBottomWidth", "thin");
		cell.setProperty("borderLeftColor", "#000000");
		cell.setProperty("borderLeftStyle", "solid");
		cell.setProperty("borderLeftWidth", "thin");
		cell.setProperty("borderRightColor", "#000000");
		cell.setProperty("borderRightStyle", "solid");
		cell.setProperty("borderRightWidth", "thin");
		cell.setProperty("borderTopColor", "#000000");
		cell.setProperty("borderTopStyle", "solid");
		cell.setProperty("borderTopWidth", "thin");
		cell.setProperty("paddingTop", "0cm");

		label = designFactory.newLabel("label");
		label.setProperty("fontWeight", "bold");
		label.setText("Annual Balance:");

		cell.getContent().add(label);

		// Add a data to cell1 in group2 footer
		cell = (CellHandle) groupfooter2.getCells().get(1);
		cell.setColumnSpan(2);
		cell.setRowSpan(1);
		cell.setProperty("borderBottomColor", "#000000");
		cell.setProperty("borderBottomStyle", "solid");
		cell.setProperty("borderBottomWidth", "thin");
		cell.setProperty("borderLeftColor", "#000000");
		cell.setProperty("borderLeftStyle", "solid");
		cell.setProperty("borderLeftWidth", "thin");
		cell.setProperty("borderRightColor", "#000000");
		cell.setProperty("borderRightStyle", "solid");
		cell.setProperty("borderRightWidth", "thin");
		cell.setProperty("borderTopColor", "#000000");
		cell.setProperty("borderTopStyle", "solid");
		cell.setProperty("borderTopWidth", "thin");

		data = designFactory.newDataItem("data");
		data.setProperty("textAlign", "left");
		data.setValueExpr("Total.sum(row[\"trans_amt\"])");

		// Add a data to cell2 in group2 footer
		cell = (CellHandle) groupfooter2.getCells().get(2);
		cell.setProperty("borderBottomColor", "#000000");
		cell.setProperty("borderBottomStyle", "solid");
		cell.setProperty("borderBottomWidth", "thin");
		cell.setProperty("borderLeftColor", "#000000");
		cell.setProperty("borderLeftStyle", "solid");
		cell.setProperty("borderLeftWidth", "thin");
		cell.setProperty("borderRightColor", "#000000");
		cell.setProperty("borderRightStyle", "solid");
		cell.setProperty("borderRightWidth", "thin");
		cell.setProperty("borderTopColor", "#000000");
		cell.setProperty("borderTopStyle", "solid");
		cell.setProperty("borderTopWidth", "thin");

		data = designFactory.newDataItem("data");
		data.setStyleName("TransactionType");
		data.setValueExpr("Total.sum(row[\"trans_amt\"])");

		// Group3 "month"
		TableGroupHandle group3 = designFactory.newTableGroup();
		group3.setName("month");
		group3.setInterval("month");
		group3.setIntervalRange(1.0);
		group3.setSortDirection("asc");
		group3.setKeyExpr("row[\"trand_dt\"]");
		group3.setTocExpression("Month:" + " " + "(row[\"trand_dt\"].getMonth()+1)");

		SortKey sort = structFactory.createSortKey();
		sort.setKey("row[\"trand_dt\"].getMonth()");
		FilterCondition filter = structFactory.createFilterCond();
		filter.setOperator("le");
		filter.setExpr("row[\"trand_dt\"].getMonth()");
		filter.setValue1("params[\"month\"].getMonth()");

		group3.getPropertyHandle(TableGroup.SORT_PROP).addItem(sort);
		group3.getPropertyHandle(TableGroup.FILTER_PROP).addItem(filter);

		// Group3 header
		RowHandle groupheader3 = (RowHandle) group3.getHeader().get(0);
		groupheader3.setProperty("fontWeight", "bold");
		groupheader3.setProperty("backgroundColor", "#008040");
		groupheader3.setProperty("color", "#FFFFFF");

		// Add a label to cell0 in group2 header
		cell = (CellHandle) groupheader3.getCells().get(0);
		cell.setProperty("borderBottomColor", "#000000");
		cell.setProperty("borderBottomStyle", "solid");
		cell.setProperty("borderBottomWidth", "thin");
		cell.setProperty("borderLeftColor", "#000000");
		cell.setProperty("borderLeftStyle", "solid");
		cell.setProperty("borderLeftWidth", "thin");
		cell.setProperty("borderRightColor", "#000000");
		cell.setProperty("borderRightStyle", "solid");
		cell.setProperty("borderRightWidth", "thin");
		cell.setProperty("borderTopColor", "#000000");
		cell.setProperty("borderTopStyle", "solid");
		cell.setProperty("borderTopWidth", "thin");
		cell.setProperty("paddingTop", "0cm");

		label = designFactory.newLabel("label");
		label.setText("Month");

		cell.getContent().add(label);

		// Add a label to cell1 in group3 header
		cell = (CellHandle) groupheader3.getCells().get(1);
		cell.setProperty("borderBottomColor", "#000000");
		cell.setProperty("borderBottomStyle", "solid");
		cell.setProperty("borderBottomWidth", "thin");
		cell.setProperty("borderLeftColor", "#000000");
		cell.setProperty("borderLeftStyle", "solid");
		cell.setProperty("borderLeftWidth", "thin");
		cell.setProperty("borderRightColor", "#000000");
		cell.setProperty("borderRightStyle", "solid");
		cell.setProperty("borderRightWidth", "thin");
		cell.setProperty("borderTopColor", "#000000");
		cell.setProperty("borderTopStyle", "solid");
		cell.setProperty("borderTopWidth", "thin");

		label = designFactory.newLabel("label");
		label.setText("Day");

		cell.getContent().add(label);

		// Add a label to cell2 in group3 header
		cell = (CellHandle) groupheader3.getCells().get(2);
		cell.setProperty("borderBottomColor", "#000000");
		cell.setProperty("borderBottomStyle", "solid");
		cell.setProperty("borderBottomWidth", "thin");
		cell.setProperty("borderLeftColor", "#000000");
		cell.setProperty("borderLeftStyle", "solid");
		cell.setProperty("borderLeftWidth", "thin");
		cell.setProperty("borderRightColor", "#000000");
		cell.setProperty("borderRightStyle", "solid");
		cell.setProperty("borderRightWidth", "thin");
		cell.setProperty("borderTopColor", "#000000");
		cell.setProperty("borderTopStyle", "solid");
		cell.setProperty("borderTopWidth", "thin");

		label = designFactory.newLabel("label");
		label.setText("Transaction Code");

		cell.getContent().add(label);

		// Add a label to cell3 in group3 header
		cell = (CellHandle) groupheader3.getCells().get(3);
		cell.setProperty("borderBottomColor", "#000000");
		cell.setProperty("borderBottomStyle", "solid");
		cell.setProperty("borderBottomWidth", "thin");
		cell.setProperty("borderLeftColor", "#000000");
		cell.setProperty("borderLeftStyle", "solid");
		cell.setProperty("borderLeftWidth", "thin");
		cell.setProperty("borderRightColor", "#000000");
		cell.setProperty("borderRightStyle", "solid");
		cell.setProperty("borderRightWidth", "thin");
		cell.setProperty("borderTopColor", "#000000");
		cell.setProperty("borderTopStyle", "solid");
		cell.setProperty("borderTopWidth", "thin");

		label = designFactory.newLabel("label");
		label.setText("Desc");

		cell.getContent().add(label);

		// Add a label to cell4 in group3 header
		cell = (CellHandle) groupheader3.getCells().get(4);
		cell.setProperty("borderBottomColor", "#000000");
		cell.setProperty("borderBottomStyle", "solid");
		cell.setProperty("borderBottomWidth", "thin");
		cell.setProperty("borderLeftColor", "#000000");
		cell.setProperty("borderLeftStyle", "solid");
		cell.setProperty("borderLeftWidth", "thin");
		cell.setProperty("borderRightColor", "#000000");
		cell.setProperty("borderRightStyle", "solid");
		cell.setProperty("borderRightWidth", "thin");
		cell.setProperty("borderTopColor", "#000000");
		cell.setProperty("borderTopStyle", "solid");
		cell.setProperty("borderTopWidth", "thin");

		label = designFactory.newLabel("label");
		label.setText("Credit");

		cell.getContent().add(label);

		// Add a label to cell5 in group3 header
		cell = (CellHandle) groupheader3.getCells().get(5);
		cell.setProperty("borderBottomColor", "#000000");
		cell.setProperty("borderBottomStyle", "solid");
		cell.setProperty("borderBottomWidth", "thin");
		cell.setProperty("borderLeftColor", "#000000");
		cell.setProperty("borderLeftStyle", "solid");
		cell.setProperty("borderLeftWidth", "thin");
		cell.setProperty("borderRightColor", "#000000");
		cell.setProperty("borderRightStyle", "solid");
		cell.setProperty("borderRightWidth", "thin");
		cell.setProperty("borderTopColor", "#000000");
		cell.setProperty("borderTopStyle", "solid");
		cell.setProperty("borderTopWidth", "thin");

		label = designFactory.newLabel("label");
		label.setText("Debit");

		cell.getContent().add(label);

		// Add a label to cell6 in group3 header
		cell = (CellHandle) groupheader3.getCells().get(6);
		cell.setProperty("borderBottomColor", "#000000");
		cell.setProperty("borderBottomStyle", "solid");
		cell.setProperty("borderBottomWidth", "thin");
		cell.setProperty("borderLeftColor", "#000000");
		cell.setProperty("borderLeftStyle", "solid");
		cell.setProperty("borderLeftWidth", "thin");
		cell.setProperty("borderRightColor", "#000000");
		cell.setProperty("borderRightStyle", "solid");
		cell.setProperty("borderRightWidth", "thin");
		cell.setProperty("borderTopColor", "#000000");
		cell.setProperty("borderTopStyle", "solid");
		cell.setProperty("borderTopWidth", "thin");

		label = designFactory.newLabel("label");
		label.setText("Transaction Type");

		cell.getContent().add(label);

		// group3 footer
		RowHandle groupfooter3 = (RowHandle) group3.getFooter().get(0);

		// Add a label to cell0 in group3 footer
		cell = (CellHandle) groupfooter3.getCells().get(0);
		cell.setColumnSpan(4);
		cell.setRowSpan(1);
		cell.setProperty("borderBottomColor", "#000000");
		cell.setProperty("borderBottomStyle", "solid");
		cell.setProperty("borderBottomWidth", "thin");
		cell.setProperty("borderLeftColor", "#000000");
		cell.setProperty("borderLeftStyle", "solid");
		cell.setProperty("borderLeftWidth", "thin");
		cell.setProperty("borderRightColor", "#000000");
		cell.setProperty("borderRightStyle", "solid");
		cell.setProperty("borderRightWidth", "thin");
		cell.setProperty("borderTopColor", "#000000");
		cell.setProperty("borderTopStyle", "solid");
		cell.setProperty("borderTopWidth", "thin");

		label = designFactory.newLabel("label");
		label.setProperty("fontWeight", "bold");
		label.setText("Monthly Balance:");

		cell.getContent().add(label);

		// Add a data to cell1 in group3 footer
		cell = (CellHandle) groupfooter3.getCells().get(1);
		cell.setColumnSpan(2);
		cell.setRowSpan(1);
		cell.setProperty("borderBottomColor", "#000000");
		cell.setProperty("borderBottomStyle", "solid");
		cell.setProperty("borderBottomWidth", "thin");
		cell.setProperty("borderLeftColor", "#000000");
		cell.setProperty("borderLeftStyle", "solid");
		cell.setProperty("borderLeftWidth", "thin");
		cell.setProperty("borderRightColor", "#000000");
		cell.setProperty("borderRightStyle", "solid");
		cell.setProperty("borderRightWidth", "thin");
		cell.setProperty("borderTopColor", "#000000");
		cell.setProperty("borderTopStyle", "solid");
		cell.setProperty("borderTopWidth", "thin");

		data = designFactory.newDataItem("data");
		data.setProperty("textAlign", "left");
		data.setValueExpr("Total.sum(row[\"trans_amt\"])");

		// Add a data to cell2 in group3 footer
		cell = (CellHandle) groupfooter3.getCells().get(2);
		cell.setProperty("borderBottomColor", "#000000");
		cell.setProperty("borderBottomStyle", "solid");
		cell.setProperty("borderBottomWidth", "thin");
		cell.setProperty("borderLeftColor", "#000000");
		cell.setProperty("borderLeftStyle", "solid");
		cell.setProperty("borderLeftWidth", "thin");
		cell.setProperty("borderRightColor", "#000000");
		cell.setProperty("borderRightStyle", "solid");
		cell.setProperty("borderRightWidth", "thin");
		cell.setProperty("borderTopColor", "#000000");
		cell.setProperty("borderTopStyle", "solid");
		cell.setProperty("borderTopWidth", "thin");

		data = designFactory.newDataItem("data");
		data.setStyleName("TransactionType");
		data.setValueExpr("Total.sum(row[\"trans_amt\"])");

		SlotHandle groups = ((TableHandle) designHandle.findElement("table")).getGroups();
		groups.add(group1);
		groups.add(group2);
		groups.add(group3);
	}

	private void buildLibrary() throws SemanticException {

		// Add a label to the library
		libraryHandle.setName("library");
		LabelHandle label = libraryFactory.newLabel("label");
		label.setText("Personal Finacial Report");
		libraryHandle.getComponents().add(label);

	}

	void buildTheme() throws SemanticException {

		// Add a theme to the library
		ThemeHandle theme = libraryFactory.newTheme("theme1");
		libraryHandle.getThemes().add(theme);

		// Add four styles to the theme
		// style "year"
		SharedStyleHandle year = libraryFactory.newStyle("year");
		year.setDateTimeFormat("yyyy");
		year.setDateTimeFormatCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM);

		// style "Month"
		SharedStyleHandle month = libraryFactory.newStyle("month");
		month.setDateTimeFormat("MM");
		month.setDateTimeFormatCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM);

		// style "day"
		SharedStyleHandle day = libraryFactory.newStyle("day");
		day.setDateTimeFormat("dd");
		day.setDateTimeFormatCategory(DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM);

		// style "TransactionType"
		SharedStyleHandle transaction = libraryFactory.newStyle("TransactionType");

		MapRule map1 = structFactory.createMapRule();
		SimpleValueHandle value1 = (SimpleValueHandle) map1.getProperty(libraryHandle.getModule(),
				MapRule.DISPLAY_MEMBER);
		MapRuleHandle maprule1 = (MapRuleHandle) map1.getHandle(value1);
		maprule1.setTestExpression("row[\"trans_amt\"]");
		maprule1.setOperator(DesignChoiceConstants.MAP_OPERATOR_LT);
		maprule1.setValue1("0");
		maprule1.setDisplay("D");
		PropertyHandle prophandle = transaction.getPropertyHandle(SharedStyleHandle.MAP_RULES_PROP);
		prophandle.addItem(maprule1);

		MapRule map2 = structFactory.createMapRule();
		SimpleValueHandle value2 = (SimpleValueHandle) map2.getProperty(libraryHandle.getModule(),
				MapRule.DISPLAY_MEMBER);
		MapRuleHandle maprule2 = (MapRuleHandle) map1.getHandle(value2);
		maprule2.setTestExpression("row[\"trans_amt\"]");
		maprule2.setOperator(DesignChoiceConstants.MAP_OPERATOR_GE);
		maprule2.setValue1("0");
		maprule2.setDisplay("C");
		prophandle.addItem(maprule2);

		// Add four styles to the theme
		theme.getStyles().add(year);
		theme.getStyles().add(month);
		theme.getStyles().add(day);
		theme.getStyles().add(transaction);

	}

}