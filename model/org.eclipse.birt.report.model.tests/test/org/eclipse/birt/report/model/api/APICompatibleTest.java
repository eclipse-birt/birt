/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.core.UserPropertyDefn;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.Action;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.ParamBinding;
import org.eclipse.birt.report.model.api.elements.structures.ResultSetColumn;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.api.simpleapi.IExpressionType;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests api compatibility.
 */

public class APICompatibleTest extends BaseTestCase {

	/**
	 * Supports the obsolete setValueExpr() method.
	 * 
	 * @throws IOException
	 * 
	 * @throws Exception
	 */

	public void testDataValueExpr() throws Exception {
		createDesign();

		DataItemHandle data = designHandle.getElementFactory().newDataItem("data1"); //$NON-NLS-1$

		designHandle.getBody().add(data);
		data.setValueExpr("row[\"column1\"] + row[\"column2\"]"); //$NON-NLS-1$

		TableHandle table = designHandle.getElementFactory().newTableItem("table1", 2); //$NON-NLS-1$
		data = designHandle.getElementFactory().newDataItem("data2"); //$NON-NLS-1$
		data.setValueExpr("row[\"value1\"] + row[\"value2\"]"); //$NON-NLS-1$

		table.getCell(TableHandle.HEADER_SLOT, -1, 1, 1).getContent().add(data);

		designHandle.getBody().add(table);

		save();
//		assertTrue( compareFile( "DataCompatibleValueExpr_golden.xml" ) ); //$NON-NLS-1$
		assertTrue(compareDesignModel("DataCompatibleValueExpr_golden.xml", new String[] { "id" }));
	}

	/**
	 * Supports the misc setting expression methods.
	 * 
	 * @throws Exception
	 */

	public void testBoundColumnWriterExpression() throws Exception {
		createDesign();

		DataItemHandle data = designHandle.getElementFactory().newDataItem("data1"); //$NON-NLS-1$

		designHandle.getBody().add(data);
		Action action = new Action();
		data.setAction(action);
		ActionHandle actionHandle = data.getActionHandle();
		actionHandle.setLinkType(DesignChoiceConstants.ACTION_LINK_TYPE_BOOKMARK_LINK);
		actionHandle.setTargetBookmark("row[\"actionBookMark\"]"); //$NON-NLS-1$

		TableHandle table = designHandle.getElementFactory().newTableItem("table1", 2); //$NON-NLS-1$
		ImageHandle image = designHandle.getElementFactory().newImage("image1"); //$NON-NLS-1$
		image.setBookmark("row[\"bookmark1\"] + row[\"bookmark2\"]"); //$NON-NLS-1$
		image.setValueExpression("row[\"image1\"] + row[\"valueExpr\"]"); //$NON-NLS-1$

		table.getCell(TableHandle.HEADER_SLOT, -1, 1, 1).getContent().add(image);

		TableHandle nestedTable = designHandle.getElementFactory().newTableItem("table2", 2); //$NON-NLS-1$
		ParamBinding paramBinding = new ParamBinding();
		paramBinding.setParamName("binding1"); //$NON-NLS-1$
		paramBinding.setExpression("row[\"value1\"]"); //$NON-NLS-1$
		nestedTable.getPropertyHandle(TableHandle.PARAM_BINDINGS_PROP).addItem(paramBinding);

		ColumnHandle column = (ColumnHandle) nestedTable.getColumns().get(0);
		HideRule hideRule = new HideRule();
		hideRule.setExpression("row[\"hide1Expr\"]"); //$NON-NLS-1$
		hideRule.setFormat(DesignChoiceConstants.FORMAT_TYPE_REPORTLET);
		column.getPropertyHandle(ColumnHandle.VISIBILITY_PROP).addItem(hideRule);

		table.getCell(TableHandle.DETAIL_SLOT, -1, 1, 2).getContent().add(nestedTable);

		designHandle.getBody().add(table);

		TextDataHandle textData = designHandle.getElementFactory().newTextData("textData1"); //$NON-NLS-1$
		textData.setValueExpr("row[\"textData1ValueExpr\"]"); //$NON-NLS-1$
		GridHandle grid = designHandle.getElementFactory().newGridItem("grid1", 2, 2); //$NON-NLS-1$
		grid.getCell(2, 2).getContent().add(textData);

		table.getCell(TableHandle.FOOTER_SLOT, -1, 1, 1).getContent().add(grid);
		table.getCell(TableHandle.FOOTER_SLOT, -1, 1, 1).setOnCreate("row[\"onCreateValueExpr\"] + 1");//$NON-NLS-1$

		ListHandle list = designHandle.getElementFactory().newList("list1"); //$NON-NLS-1$
		FilterCondition filter = new FilterCondition();
		filter.setExpr("row[\"filter1ValueExpr\"]"); //$NON-NLS-1$
		filter.setValue1("row[\"filter1Value1\"]"); //$NON-NLS-1$
		filter.setValue2("row[\"filter1Value2\"]"); //$NON-NLS-1$
		list.getPropertyHandle(ListHandle.FILTER_PROP).addItem(filter);

		SortKey sort = new SortKey();
		sort.setKey("row[\"sort1Key\"]"); //$NON-NLS-1$
		list.getPropertyHandle(ListHandle.SORT_PROP).addItem(sort);

		designHandle.getBody().add(list);

		ScalarParameterHandle param = designHandle.getElementFactory().newScalarParameter("param1"); //$NON-NLS-1$
		param.setValueExpr("row[\"param1ValueExpr\"]"); //$NON-NLS-1$
		param.setLabelExpr("row[\"param1LabelExpr\"]"); //$NON-NLS-1$

		designHandle.getParameters().add(param);

		param = designHandle.getElementFactory().newScalarParameter("param2"); //$NON-NLS-1$
		param.setValueExpr("param2ValueExpr"); //$NON-NLS-1$
		param.setLabelExpr("param2LabelExpr"); //$NON-NLS-1$
		designHandle.getParameters().add(param);

		textData = designHandle.getElementFactory().newTextData("textData2"); //$NON-NLS-1$

		designHandle.getBody().add(textData);

		ComputedColumn boundColumn = new ComputedColumn();
		boundColumn.setName("New Column"); //$NON-NLS-1$
		boundColumn.setExpression("row[\"textData2ValueExpr\"]"); //$NON-NLS-1$

		textData.addColumnBinding(boundColumn, true);
		textData.setValueExpr("row[\"New Column\"]"); //$NON-NLS-1$

		save();
//		assertTrue( compareFile( "CompatibleExpression_golden.xml" ) ); //$NON-NLS-1$
		assertTrue(compareDesignModel("CompatibleExpression_golden.xml", new String[] { "id" }));
	}

	/**
	 * Supports the misc setting expression methods.
	 * 
	 * @throws Exception
	 */

	public void testBoundColumnWriterNoExpression() throws Exception {
		createDesign();
		design.getVersionManager().setVersion("1"); //$NON-NLS-1$

		DataItemHandle data = designHandle.getElementFactory().newDataItem("data1"); //$NON-NLS-1$

		data.setTocExpression("row[\"value1\"]"); //$NON-NLS-1$
		data.setValueExpr("row[\"valueExpr\"]");//$NON-NLS-1$
		TableHandle table = designHandle.getElementFactory().newTableItem("table1", 2); //$NON-NLS-1$

		table.getCell(TableHandle.HEADER_SLOT, -1, 1, 1).getContent().add(data);

		designHandle.getBody().add(table);

		FilterCondition filter = new FilterCondition();
		filter.setExpr("row[\"filter1ValueExpr\"]"); //$NON-NLS-1$
		filter.setValue1("row[\"filter1Value1\"]"); //$NON-NLS-1$
		filter.setValue2("row[\"filter1Value2\"]"); //$NON-NLS-1$
		table.getPropertyHandle(ListHandle.FILTER_PROP).addItem(filter);

		save();
		// assertTrue( compareFile( "CompatibleExpression_golden_1.xml" ) );
		// //$NON-NLS-1$
		assertTrue(compareDesignModel("CompatibleExpression_golden_1.xml", new String[] { "id" }));
	}

	/**
	 * Supports the misc setting expression methods.
	 * 
	 * @throws Exception
	 */

	public void testBoundColumnWithGroup() throws Exception {
		createDesign();

		TableHandle table = designHandle.getElementFactory().newTableItem("table1", 3); //$NON-NLS-1$
		designHandle.getBody().add(table);

		TableGroupHandle group = designHandle.getElementFactory().newTableGroup();
		group.getFooter().add(designHandle.getElementFactory().newTableRow(3));
		table.getGroups().add(group);

		group = designHandle.getElementFactory().newTableGroup();
		group.getFooter().add(designHandle.getElementFactory().newTableRow(3));
		table.getGroups().add(group);

		DataItemHandle data = designHandle.getElementFactory().newDataItem("data1"); //$NON-NLS-1$
		data.setValueExpr("row[\"valueData\"]"); //$NON-NLS-1$

		table.getCell(GroupHandle.FOOTER_SLOT, 2, 1, 1).getContent().add(data);

		data = designHandle.getElementFactory().newDataItem("data1"); //$NON-NLS-1$
		data.setValueExpr("row[\"valueData\"]"); //$NON-NLS-1$

		table.getCell(GroupHandle.FOOTER_SLOT, 1, 1, 1).getContent().add(data);

		save();

		saveOutputFile("CompatibleExpression_out_2.xml");
//		assertTrue( compareFile( "CompatibleExpression_golden_2.xml" ) ); //$NON-NLS-1$		
		assertTrue(compareDesignModel("CompatibleExpression_golden_2.xml", new String[] { "id" }));
	}

	/**
	 * Bugzilla 156977. Result set property is replaced by result hints property.
	 * 
	 * @throws Exception
	 */

	public void testScriptResultSet() throws Exception {
		createDesign();

		ScriptDataSetHandle ds = designHandle.getElementFactory().newScriptDataSet("dataSet1"); //$NON-NLS-1$

		PropertyHandle ph = ds.getPropertyHandle(ScriptDataSetHandle.RESULT_SET_PROP);

		assertNotNull(ph);

		for (int i = 0; i < 2; i++) {
			ResultSetColumn rsc = StructureFactory.createResultSetColumn();
			rsc.setPosition(new Integer(i + 1));
			rsc.setColumnName("COLUMN_" + i); //$NON-NLS-1$
			rsc.setDataType(DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL);

			ph.addItem(rsc);
		}
	}

	/**
	 * Backward TOC expression. Change toc expression to toc structure. since
	 * 3.2.10. Another backward is to change the expression new syntax.
	 * 
	 * @throws Exception
	 */

	public void testTOCExpression() throws Exception {
		createDesign();

		LabelHandle labelHandle = designHandle.getElementFactory().newLabel("label1"); //$NON-NLS-1$
		designHandle.getBody().add(labelHandle);

		labelHandle.setTocExpression("toc1"); //$NON-NLS-1$
		assertEquals("toc1", labelHandle.getTocExpression()); //$NON-NLS-1$

		labelHandle.setTocExpression("toc2"); //$NON-NLS-1$
		assertEquals("toc2", labelHandle.getTocExpression()); //$NON-NLS-1$

		// should have no class cast exception

		TOC tmpTOC = (TOC) labelHandle.getProperty(LabelHandle.TOC_PROP);
		assertEquals("toc2", tmpTOC.toString()); //$NON-NLS-1$
	}

	/**
	 * Backward CachedRowCount method. Since 3.2.11.
	 * 
	 * @throws Exception
	 */

	public void testCachedRowCount() throws Exception {
		createDesign();

		OdaDataSetHandle dsHandle = designHandle.getElementFactory().newOdaDataSet("dataSet", //$NON-NLS-1$
				"org.eclipse.birt.report.data.oda.jdbc.JdbcSelectDataSet"); //$NON-NLS-1$
		designHandle.getDataSets().add(dsHandle);

		assertEquals(0, dsHandle.getCachedRowCount());

		dsHandle.setCachedRowCount(10);
		assertEquals(10, dsHandle.getCachedRowCount());
	}

	/**
	 * Backward allowNull and allowBlank property. Now it is isRequired property
	 * since the version 3.2.10.
	 * 
	 * @throws Exception
	 */

	public void testScalarParamAllowProps() throws Exception {
		createDesign();

		ScalarParameterHandle param = designHandle.getElementFactory().newScalarParameter("param1"); //$NON-NLS-1$
		designHandle.getParameters().add(param);

		param.setAllowBlank(false);
		assertEquals(false, param.allowBlank());
		assertEquals(true, param.isRequired());

		param.setAllowBlank(true);
		assertEquals(true, param.allowBlank());
		assertEquals(false, param.isRequired());

		param.setAllowNull(false);
		assertEquals(false, param.allowNull());
		assertEquals(true, param.isRequired());

		param.setAllowNull(true);
		assertEquals(true, param.allowNull());
		assertEquals(false, param.isRequired());

	}

	/**
	 * Backward for aggregate properties on ComputedColumn since the version 3.2.11.
	 * 
	 * @throws Exception
	 */

	public void testComputedColumnAggregates() throws Exception {
		ComputedColumn column = StructureFactory.createComputedColumn();
		column.setName("column 1"); //$NON-NLS-1$
		column.setAggregateOn("aggregate on 1"); //$NON-NLS-1$
		assertEquals("aggregate on 1", column.getAggregateOn()); //$NON-NLS-1$
		column.setExpression("expression1"); //$NON-NLS-1$
		createDesign();

		DataItemHandle data = designHandle.getElementFactory().newDataItem("data1"); //$NON-NLS-1$
		ComputedColumnHandle columnHandle = data.addColumnBinding(column, false);
		columnHandle.setAggregateOn("new aggregate on"); //$NON-NLS-1$
		assertEquals("new aggregate on", columnHandle.getAggregateOn()); //$NON-NLS-1$

		columnHandle.setProperty(ComputedColumn.AGGREGATEON_MEMBER, "new aggregate on1"); //$NON-NLS-1$
		List values = (List) columnHandle.getProperty(ComputedColumn.AGGREGATEON_MEMBER);
		assertTrue("new aggregate on1".equals(values.get(0))); //$NON-NLS-1$
	}

	/**
	 * API compatibility for the include resource of Module since the version
	 * 3.2.16.
	 * 
	 * @throws Exception
	 */

	public void testCompatibleIncludeResource() throws Exception {
		createDesign();

		PropertyHandle propHandle = designHandle.getPropertyHandle(ReportDesignHandle.INCLUDE_RESOURCE_PROP);
		propHandle.addItem("resource1"); //$NON-NLS-1$
		propHandle.addItem("resource2"); //$NON-NLS-1$

		// the value is a list with two items.

		assertEquals("resource1; resource2", designHandle.getIncludeResource()); //$NON-NLS-1$

		designHandle.setIncludeResource("new_resource"); //$NON-NLS-1$

		// the value is a list with a single item.

		assertEquals("new_resource", designHandle.getIncludeResource()); //$NON-NLS-1$

	}

	/**
	 * Test API backward compatibility for StyleRule.value1, FilterCondition.valu1
	 * and FilterConditionElement.value1.
	 * 
	 * @throws Exception
	 */

	public void testValue1List() throws Exception {
		// use the same file in ExpressionTest

		openDesign("CompatibleValue1Test.xml"); //$NON-NLS-1$

		StyleHandle tmpStyle = designHandle.findStyle("My-Style"); //$NON-NLS-1$
		Iterator iter1 = tmpStyle.highlightRulesIterator();
		HighlightRuleHandle tmpHighlight = (HighlightRuleHandle) iter1.next();

		List<String> tmpValues = (List<String>) tmpHighlight.getValue1List();
		assertEquals("[hi_value1, hi_value2]", tmpValues.toString()); //$NON-NLS-1$

		iter1 = tmpStyle.mapRulesIterator();
		MapRuleHandle tmpMap = (MapRuleHandle) iter1.next();

		tmpValues = (List<String>) tmpMap.getValue1List(); // $NON-NLS-1$
		assertEquals("[map_value1, map_value2]", tmpValues.toString()); //$NON-NLS-1$

		ExtendedItemHandle testTable = (ExtendedItemHandle) designHandle.findElement("testTable"); //$NON-NLS-1$
		assertNotNull(testTable);

		// test filter properties

		List valueList = testTable.getListProperty("filter"); //$NON-NLS-1$
		FilterConditionElementHandle filter = (FilterConditionElementHandle) valueList.get(0);
		tmpValues = (List<String>) filter.getValue1List();
		assertEquals("[filter_value1, filter_value2]", tmpValues.toString()); //$NON-NLS-1$

		TableHandle table1 = (TableHandle) designHandle.findElement("table1"); //$NON-NLS-1$
		FilterConditionHandle filter1 = (FilterConditionHandle) table1.filtersIterator().next();
		filter1.setValue1("ship"); //$NON-NLS-1$

		assertEquals("ship", filter1.getValue1()); //$NON-NLS-1$

	}

	/**
	 * Backward to avoid class Exception.
	 * 
	 * @throws Exception
	 */

	public void testComputedColumnFilterExpression() throws Exception {
		ComputedColumn column = StructureFactory.createComputedColumn();
		column.setName("column 1"); //$NON-NLS-1$
		column.setFilterExpression("value1"); //$NON-NLS-1$

		assertEquals("value1", column.getFilterExpression()); //$NON-NLS-1$

	}

	/**
	 * Backward for the default value list.
	 * 
	 * @throws Exception
	 */

	public void testScalarParamDefaultValue() throws Exception {
		createDesign();

		ScalarParameterHandle param = designHandle.getElementFactory().newScalarParameter(null);

		param.setDefaultValue("default value 1"); //$NON-NLS-1$
		List<Expression> tmpValues = param.getDefaultValueList();
		assertTrue(tmpValues.get(0) instanceof Expression);
	}

	/**
	 * Backward for the default value of the user property when its type is
	 * expression.
	 * 
	 * @throws Exception
	 */
	public void testUserPropertyDefaultValue() throws Exception {
		UserPropertyDefn defn = new UserPropertyDefn();
		defn.setName("TestProperty"); //$NON-NLS-1$
		defn.setType(MetaDataDictionary.getInstance().getPropertyType(PropertyType.EXPRESSION_TYPE_NAME));
		assertTrue(defn.allowExpression());
		defn.setDefault("Test"); //$NON-NLS-1$
		assertTrue(defn.getDefault() instanceof Expression);
		Expression defaultValue = (Expression) defn.getDefault();
		assertEquals("Test", defaultValue.getExpression());
		assertEquals(IExpressionType.CONSTANT, defaultValue.getType());
	}

	/**
	 * Backward for the default value of the pushDown property of ReportItem
	 */
	public void testReportItemPushDownValue() throws Exception {
		createDesign();
		LabelHandle label = designHandle.getElementFactory().newLabel(null);
		designHandle.getBody().add(label);

		assertFalse(label.pushDown());

		label.setPushDown(true);
		assertTrue(label.pushDown());

		design.getVersionManager().setVersion("3.2.20"); //$NON-NLS-1$
		label.setProperty(IReportItemModel.PUSH_DOWN_PROP, null);
		assertEquals(label.getPropertyDefn(IReportItemModel.PUSH_DOWN_PROP).getDefault(), label.pushDown());

		label.setPushDown(false);
		assertFalse(label.pushDown());
	}
}
