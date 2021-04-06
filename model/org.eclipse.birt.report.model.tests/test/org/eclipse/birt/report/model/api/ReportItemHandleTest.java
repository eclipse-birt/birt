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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.TOC;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.ReportItem;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Test ReportItemHandle.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse: *
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testDataSet()}</td>
 * <td>check free-form element which contains attribute data-set</td>
 * <td>dataset name is myDataSet</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>check list element which doesn't contain attribute data-set</td>
 * <td>null</td>
 * </tr>
 * 
 * <tr>
 * <td>testReadVisibilityRules()</td>
 * <td>Gets visibility rules in elements and tests whether values match with
 * those defined the design file.</td>
 * <td>Returned values match with the design file. If "format" values are not
 * defined, the default value "all" is used.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>The number of visibility rules in elements.</td>
 * <td>The number is 2.</td>
 * </tr>
 * 
 * <tr>
 * <td>testWriteVisibilityRules</td>
 * <td>The default format value in the visibility rule.</td>
 * <td>The default value can be written out to the design file.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Sets "format" and "valueExpr" properties of a visibility rule.</td>
 * <td>"format" and "valueExpr" can be written out and the output file matches
 * with the golden file.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testCssProperties()}</td>
 * <td>Tests the special "vertical-align" property.</td>
 * <td>If the property is defined on the row, cell, elements in cells can get
 * the "vertical-align" value.</td>
 * </tr>
 * 
 * </table>
 */

public class ReportItemHandleTest extends BaseTestCase {

	private DesignElement element;
	private InnerReportItemHandle innerHandle;
	private String fileName = "ReportItemHandleTest.xml"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * 
	 */

	class InnerReportItemHandle extends ReportItemHandle {

		InnerReportItemHandle(ReportDesign design, DesignElement element) {
			super(design, element);
		}
	}

	/**
	 * test getDataSet().
	 * <p>
	 * Test Cases:
	 * 
	 * <ul>
	 * <li>check free-form element which contains attribute data-set</li>
	 * <li>check list element which doesn't contain attribute data-set</li>
	 * </ul>
	 * 
	 * Excepted:
	 * <ul>
	 * <li>dataset name is myDataSet</li>
	 * <li>null</li>
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testDataSet() throws Exception {
		openDesign(fileName);

		// the data set is not null referenced by name
		element = design.findElement("free form"); //$NON-NLS-1$
		assertNotNull(element);
		innerHandle = new InnerReportItemHandle(design, element);
		DesignElementHandle itemHandle = innerHandle.getDataSet();
		assertNotNull(itemHandle);
		String name = innerHandle.getDataSet().getElement().getName();
		assertEquals("myDataSet", name); //$NON-NLS-1$

		// the data set is null referenced by name
		element = design.findElement("my list"); //$NON-NLS-1$
		assertNotNull(element);
		innerHandle = new InnerReportItemHandle(design, element);
		itemHandle = innerHandle.getDataSet();
		assertNotNull(itemHandle);

		DataSetHandle handle = designHandle.findDataSet("myDataSet"); //$NON-NLS-1$
		ListHandle list = (ListHandle) element.getHandle(design);
		list.setDataSet(handle);
		assertEquals(handle, list.getDataSet());

		list.setDataSet((DataSetHandle) null);
		assertNull(list.getDataSet());
	}

	/**
	 * Tests 'addTOC' , 'getTOC' , 'setExpression' , 'getExpression' method.
	 * 
	 * @throws Exception
	 */

	public void testTOC() throws Exception {
		openDesign(fileName);

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("bodyLabel");//$NON-NLS-1$

		TOC toc = StructureFactory.createTOC("toc"); //$NON-NLS-1$
		TOCHandle tocHandle = labelHandle.addTOC(toc);
		assertNotNull(tocHandle);
		assertEquals("toc", tocHandle.getExpression());//$NON-NLS-1$

		// private style

		tocHandle.setProperty(DesignChoiceConstants.CHOICE_FONT_WEIGHT, DesignChoiceConstants.FONT_WEIGHT_BOLD);

		// shared style

		StyleHandle style = designHandle.getElementFactory().newStyle("style");//$NON-NLS-1$
		style.setCanShrink(true);
		style.setFontWeight(DesignChoiceConstants.FONT_WEIGHT_NORMAL);
		designHandle.getStyles().add(style);

		// check shared style

		assertEquals("toc", labelHandle.getStringProperty(ReportItem.TOC_PROP));//$NON-NLS-1$
		tocHandle.setStyleName(style.getName());

		assertEquals("style", tocHandle.getStyleName());//$NON-NLS-1$

	}

	/**
	 * Tests 'setStringProperty' method.
	 * 
	 * @throws Exception
	 */

	public void testSetTOCProperty() throws Exception {
		openDesign(fileName);

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("bodyLabel");//$NON-NLS-1$
		labelHandle.setStringProperty(IReportItemModel.TOC_PROP, "toc");//$NON-NLS-1$

		assertEquals("toc", labelHandle.getStringProperty(IReportItemModel.TOC_PROP));//$NON-NLS-1$
	}

	/**
	 * Tests addcolumnBinding method.
	 * 
	 * @throws SemanticException
	 * @throws DesignFileException
	 */

	public void testColumnBinding() throws SemanticException {
		createDesign();

		TableHandle tableHandle = designHandle.getElementFactory().newTableItem("new table"); //$NON-NLS-1$
		designHandle.getBody().add(tableHandle);

		tableHandle = (TableHandle) designHandle.findElement("new table"); //$NON-NLS-1$

		ComputedColumn col = StructureFactory.createComputedColumn();
		col.setName(null);
		col.setExpression("dataSetRow[\"CUSTOMERNUMBER\"]"); //$NON-NLS-1$

		// add null on table.

		assertNull(tableHandle.addColumnBinding(null, true));

		// add empty name of computed column on table.

		try {
			tableHandle.addColumnBinding(col, true);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED, e.getErrorCode());
		}

		// add computed column on table.

		col.setName("CUSTOMERNUMBER");//$NON-NLS-1$
		tableHandle.addColumnBinding(col, true);

		// add duplicate name on table row data

		ComputedColumn col2 = StructureFactory.createComputedColumn();
		col2.setName("CUSTOMERNUMBER"); //$NON-NLS-1$
		col2.setExpression("dataSetRow[\"CUSTOMERNUMBER\"]"); //$NON-NLS-1$

		try {
			tableHandle.addColumnBinding(col2, true);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS, e.getErrorCode());
		}

		col = StructureFactory.createComputedColumn();
		col.setName("CUSTOMERNUMBER"); //$NON-NLS-1$
		col.setExpression("dataSetRow[\"CUSTOMERNUMBER_TEST\"]"); //$NON-NLS-1$

		try {
			tableHandle.addColumnBinding(col, true);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS, e.getErrorCode());
		}

		col = StructureFactory.createComputedColumn();
		col.setName("CUSTOMERNUMBER_TEST"); //$NON-NLS-1$
		col.setExpression("dataSetRow[\"CUSTOMERNUMBER\"]"); //$NON-NLS-1$

		tableHandle.addColumnBinding(col, true);

		col = (ComputedColumn) ((ComputedColumnHandle) tableHandle.columnBindingsIterator().next()).getStructure();

		// replace
		PropertyHandle propertyHandle = tableHandle.getPropertyHandle(IReportItemModel.BOUND_DATA_COLUMNS_PROP);

		ComputedColumn newCol = StructureFactory.createComputedColumn();
		newCol.setName(null);
		newCol.setExpression("dataSetRow[\"CUSTOMER\"]"); //$NON-NLS-1$

		// use empty name of computed column to replace old one.

		try {
			propertyHandle.replaceItem(col, newCol);
			fail();
		} catch (SemanticException e) {
			assertTrue(e instanceof PropertyValueException);
		}

		// replace duplicate name on table row data

		newCol.setName("CUSTOMERNUMBER");//$NON-NLS-1$
		try {
			propertyHandle.replaceItem(col, newCol);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS, e.getErrorCode());
		}

		// add computed column on table.

		newCol.setName("Number");//$NON-NLS-1$
		propertyHandle.replaceItem(col, newCol);

		// clear all bound column.

		tableHandle.clearProperty(IReportItemModel.BOUND_DATA_COLUMNS_PROP);
		assertNull(tableHandle.getListProperty(IReportItemModel.BOUND_DATA_COLUMNS_PROP));

		// add group bound column with group name aggregateOn.

		GroupHandle groupHandle = designHandle.getElementFactory().newTableGroup();
		groupHandle.setName("NewGroup");//$NON-NLS-1$
		tableHandle.getGroups().add(groupHandle);

		ComputedColumn groupCol = StructureFactory.createComputedColumn();
		groupCol.setName("data item1");//$NON-NLS-1$
		groupCol.setExpression("Total.sum(row[\"CUSTOMERNUMBER\"] , null , null )"); //$NON-NLS-1$
		groupCol.setAggregateOn("NewGroup");//$NON-NLS-1$
		groupCol.setDataType(DesignChoiceConstants.PARAM_TYPE_STRING);

		// add bound column with all aggregateOn.

		tableHandle.addColumnBinding(groupCol, false);

		ComputedColumn tableCol = StructureFactory.createComputedColumn();
		tableCol.setName("data item2");//$NON-NLS-1$
		tableCol.setExpression("Total.sum(row[\"CUSTOMERNUMBER\"] , null , null )"); //$NON-NLS-1$
		tableCol.setAggregateOn("All");//$NON-NLS-1$
		tableCol.setDataType(DesignChoiceConstants.PARAM_TYPE_STRING);

		tableHandle.addColumnBinding(tableCol, false);

		List boundList = tableHandle.getListProperty(IReportItemModel.BOUND_DATA_COLUMNS_PROP);
		assertEquals(2, boundList.size());
		assertEquals("data item1", ((ComputedColumn) boundList.get(0)).getName());//$NON-NLS-1$
		assertEquals("NewGroup", ((ComputedColumn) boundList.get(0)).getAggregateOn());//$NON-NLS-1$
		assertEquals("data item2", ((ComputedColumn) boundList.get(1)).getName());//$NON-NLS-1$
		assertEquals("All", ((ComputedColumn) boundList.get(1)).getAggregateOn());//$NON-NLS-1$
	}

	/**
	 * Test to read hide rules.
	 * 
	 * @throws Exception if open the design file with errors.
	 */

	public void testReadVisibilityRules() throws Exception {
		openDesign(fileName);

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("bodyLabel"); //$NON-NLS-1$
		Iterator rules = labelHandle.visibilityRulesIterator();

		// checks with the first visibility rule.

		StructureHandle structHandle = (StructureHandle) rules.next();
		assertNotNull(structHandle);

		MemberHandle memberHandle = structHandle.getMember(HideRule.FORMAT_MEMBER);
		assertEquals(DesignChoiceConstants.FORMAT_TYPE_PDF, memberHandle.getStringValue());
		memberHandle = structHandle.getMember(HideRule.VALUE_EXPR_MEMBER);
		assertEquals("pdf, 10 people", memberHandle.getStringValue()); //$NON-NLS-1$

		// the second visibility rule

		structHandle = (StructureHandle) rules.next();
		assertNotNull(structHandle);

		memberHandle = structHandle.getMember(HideRule.FORMAT_MEMBER);
		assertEquals(DesignChoiceConstants.FORMAT_TYPE_ALL, memberHandle.getStringValue());
		memberHandle = structHandle.getMember(HideRule.VALUE_EXPR_MEMBER);
		assertEquals("excel, 10 people", memberHandle.getStringValue()); //$NON-NLS-1$

		// no third, must be null.

		structHandle = (StructureHandle) rules.next();
		assertNull(structHandle);

		// tests visibility on the data item.

		DataItemHandle dataHandle = (DataItemHandle) designHandle.findElement("bodyData"); //$NON-NLS-1$
		rules = dataHandle.visibilityRulesIterator();

		structHandle = (StructureHandle) rules.next();
		assertNotNull(structHandle);

		// if no format attribute, use the default value.

		memberHandle = structHandle.getMember(HideRule.FORMAT_MEMBER);
		assertEquals(DesignChoiceConstants.FORMAT_TYPE_ALL, memberHandle.getStringValue());

		memberHandle = structHandle.getMember(HideRule.VALUE_EXPR_MEMBER);
		assertNull(memberHandle.getStringValue());

		// the second visibility rule for the data item

		structHandle = (StructureHandle) rules.next();
		assertNotNull(structHandle);

		memberHandle = structHandle.getMember(HideRule.FORMAT_MEMBER);
		assertEquals(DesignChoiceConstants.FORMAT_TYPE_PDF, memberHandle.getStringValue());

		// if no expression, should be empty string

		memberHandle = structHandle.getMember(HideRule.VALUE_EXPR_MEMBER);
		assertNull(memberHandle.getStringValue());

	}

	/**
	 * Tests to write hide rules to the design file.
	 * 
	 * @throws Exception if open/write the design file with IO errors.
	 */

	public void testWriteVisibilityRules() throws Exception {
		openDesign(fileName);

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("bodyLabel"); //$NON-NLS-1$
		Iterator rules = labelHandle.visibilityRulesIterator();

		// sets with the first visibility rule.

		StructureHandle structHandle = (StructureHandle) rules.next();
		assertNotNull(structHandle);
		MemberHandle memberHandle = structHandle.getMember(HideRule.FORMAT_MEMBER);

		memberHandle.setValue(DesignChoiceConstants.FORMAT_TYPE_REPORTLET);

		// invalid choice value.

		try {
			memberHandle.setValue("noformat"); //$NON-NLS-1$
		} catch (PropertyValueException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, e.getErrorCode());
		}

		memberHandle = structHandle.getMember(HideRule.VALUE_EXPR_MEMBER);
		memberHandle.setValue("10*20"); //$NON-NLS-1$

		DataItemHandle dataHandle = (DataItemHandle) designHandle.findElement("bodyData"); //$NON-NLS-1$
		rules = dataHandle.visibilityRulesIterator();

		// get the second visibility rule handle.

		structHandle = (StructureHandle) rules.next();
		structHandle = (StructureHandle) rules.next();
		assertNotNull(structHandle);

		memberHandle = structHandle.getMember(HideRule.FORMAT_MEMBER);
		memberHandle.setValue(DesignChoiceConstants.FORMAT_TYPE_REPORTLET);

		memberHandle = structHandle.getMember(HideRule.VALUE_EXPR_MEMBER);
		memberHandle.setValue("bodyData 2nd rule."); //$NON-NLS-1$

		// no expression originally, now add one expression.

		structHandle = (StructureHandle) rules.next();
		assertNull(structHandle);
	}

	/**
	 * Tests common properties on a report item.
	 * 
	 * 
	 * @throws Exception
	 */

	public void testProperties() throws Exception {
		openDesign(fileName, ULocale.ENGLISH);

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("bodyLabel"); //$NON-NLS-1$

		labelHandle.setWidth("15in"); //$NON-NLS-1$
		assertEquals("15in", labelHandle.getWidth().getStringValue()); //$NON-NLS-1$

		try {
			labelHandle.setWidth(-15);
			labelHandle.setProperty(StyleHandle.MARGIN_TOP_PROP, "-10pt"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertTrue(e instanceof PropertyValueException);
		}

		labelHandle.setHeight("5in"); //$NON-NLS-1$
		assertEquals("5in", labelHandle.getHeight().getStringValue()); //$NON-NLS-1$

		labelHandle.setX(".5in"); //$NON-NLS-1$
		assertEquals("0.5in", labelHandle.getX().getStringValue()); //$NON-NLS-1$

		labelHandle.setY("5.38in"); //$NON-NLS-1$
		assertEquals("5.38in", labelHandle.getY().getStringValue()); //$NON-NLS-1$

	}

	/**
	 * Tests the undo operation for invalid style name.
	 * 
	 * @throws Exception if any exception
	 */

	public void testUndoInvalidStyle() throws Exception {
		openDesign(fileName);

		TextItemHandle textHandle = (TextItemHandle) designHandle.findElement("myText"); //$NON-NLS-1$
		textHandle.setStyleName("My-Style"); //$NON-NLS-1$

		designHandle.getCommandStack().undo();

		assertEquals("unknownStyle", textHandle //$NON-NLS-1$
				.getStringProperty(ReportItem.STYLE_PROP));
		assertEquals(null, textHandle.getElementProperty(ReportItem.STYLE_PROP));
		assertEquals(null, textHandle.getStyle());
	}

	/**
	 * Tests the undo operation for invalid DataSet name.
	 * 
	 * @throws Exception if any exception
	 */

	public void testUndoInvalidDataSet() throws Exception {
		openDesign(fileName);

		TextItemHandle textHandle = (TextItemHandle) designHandle.findElement("myText"); //$NON-NLS-1$
		textHandle.setProperty(ReportItem.DATA_SET_PROP, "myDataSet"); //$NON-NLS-1$

		designHandle.getCommandStack().undo();

		assertEquals("unknownDataSet", textHandle //$NON-NLS-1$
				.getStringProperty(ReportItem.DATA_SET_PROP));
		assertEquals(null, textHandle.getElementProperty(ReportItem.DATA_SET_PROP));
		assertEquals(null, textHandle.getDataSet());
	}

	/**
	 * Tests the function for adding bound data columns.
	 * 
	 * @throws Exception
	 */

	public void testBoundDataColumns() throws Exception {

		openDesign(fileName);

		TextItemHandle textHandle = (TextItemHandle) designHandle.findElement("myText"); //$NON-NLS-1$

		ComputedColumn tmpComputedColumn = new ComputedColumn();
		tmpComputedColumn.setName("new column1"); //$NON-NLS-1$
		tmpComputedColumn.setExpression("new column1 expr"); //$NON-NLS-1$
		textHandle.addColumnBinding(tmpComputedColumn, false);

		assertEquals(1, textHandle.getListProperty(IReportItemModel.BOUND_DATA_COLUMNS_PROP).size());

		tmpComputedColumn = new ComputedColumn();
		tmpComputedColumn.setName("new column1"); //$NON-NLS-1$
		tmpComputedColumn.setExpression("new column expr"); //$NON-NLS-1$

		// with the same name different expression

		try {
			textHandle.addColumnBinding(tmpComputedColumn, false);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS, e.getErrorCode());
		}

		// with the same name different expression

		try {
			textHandle.addColumnBinding(tmpComputedColumn, true);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS, e.getErrorCode());
		}

		// duplidate name and expression value.

		tmpComputedColumn.setExpression("new column1 expr"); //$NON-NLS-1$
		try {
			textHandle.addColumnBinding(tmpComputedColumn, true);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS, e.getErrorCode());
		}

		textHandle.addColumnBinding(tmpComputedColumn, false);

		assertEquals(1, textHandle.getListProperty(IReportItemModel.BOUND_DATA_COLUMNS_PROP).size());

		// with the same expression, different name

		tmpComputedColumn.setName("new column2"); //$NON-NLS-1$

		textHandle.addColumnBinding(tmpComputedColumn, false);
		assertEquals(1, textHandle.getListProperty(IReportItemModel.BOUND_DATA_COLUMNS_PROP).size());

		textHandle.addColumnBinding(tmpComputedColumn, true);
		assertEquals(2, textHandle.getListProperty(IReportItemModel.BOUND_DATA_COLUMNS_PROP).size());

		Iterator iter1 = textHandle.columnBindingsIterator();
		ComputedColumnHandle columnHandle1 = (ComputedColumnHandle) iter1.next();
		assertEquals("new column1", columnHandle1.getName()); //$NON-NLS-1$
		ComputedColumnHandle columnHandle2 = (ComputedColumnHandle) iter1.next();
		assertEquals("new column2", columnHandle2.getName()); //$NON-NLS-1$

		try {
			columnHandle2.setName("new column1"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS, e.getErrorCode());
		}

		columnHandle2.setName("new column3"); //$NON-NLS-1$
		assertEquals("new column3", columnHandle2.getName()); //$NON-NLS-1$

		// it is ok to set name with dot.

		columnHandle2.setName("table1.column3"); //$NON-NLS-1$
		assertEquals("table1.column3", columnHandle2.getName()); //$NON-NLS-1$

	}

	/**
	 * Tests removedColumnBindings().
	 * 
	 * @throws Exception
	 */

	public void testRemovedColumnBindings() throws Exception {
		openDesign("ReportItemHandleTest_2.xml"); //$NON-NLS-1$

		TableHandle table = (TableHandle) designHandle.findElement("myTable1"); //$NON-NLS-1$
		assertEquals(14, table.getColumnBindings().getListValue().size());

		List bindingNameList = new ArrayList(2);
		bindingNameList.add("CITY"); //$NON-NLS-1$
		bindingNameList.add("nobindingitem"); //$NON-NLS-1$
		table.removedColumnBindings(bindingNameList);

		assertEquals(12, table.getColumnBindings().getListValue().size());
	}

	/**
	 * tests getAvailableBindings().
	 * 
	 * @throws DesignFileException
	 */
	public void testGetAvailableBindings() throws DesignFileException {

		openDesign("ReportItemHandleTest_3.xml"); //$NON-NLS-1$

		// test for TableOne. TableOne is the top level table, it is in body
		// slot.
		// It has two bindings defined.
		TableHandle tableone = (TableHandle) designHandle.findElement("tableone"); //$NON-NLS-1$
		assertNotNull(tableone);

		Map one = new HashMap();
		one.put("TableOneCol1", "\"TableOneCol1\"");
		one.put("TableOneCol2", "\"TableOneCol2\"");

		List tableOneBindings = new ArrayList();
		for (Iterator itr = tableone.getAvailableBindings(); itr.hasNext();) {
			tableOneBindings.add(itr.next());
		}

		assertTrue(tableOneBindings.size() == 2);

		// test if the returned bindings are the expected ones.
		for (int i = 0; i < tableOneBindings.size(); i++) {
			ComputedColumnHandle binding = (ComputedColumnHandle) tableOneBindings.get(i);
			assertTrue(one.containsKey(binding.getName()));
			assertEquals(one.get(binding.getName()), binding.getExpression());
		}

		// test for the tableTwo. TableTwo is a sub table inside the tableOne.
		// It does not have any binding defined local. So it's available
		// bindings should all get from tableOne.
		TableHandle tableTwo = (TableHandle) designHandle.findElement("tabletwo"); //$NON-NLS-1$

		assertNotNull(tableone);

		Map two = new HashMap();
		two.putAll(one);

		List tableTwoBindings = new ArrayList();
		for (Iterator itr = tableTwo.getAvailableBindings(); itr.hasNext();) {
			tableTwoBindings.add(itr.next());
		}

		assertTrue(tableTwoBindings.size() == 2);

		// test if the returned bindings are the expected ones.
		for (int i = 0; i < tableTwoBindings.size(); i++) {
			ComputedColumnHandle binding = (ComputedColumnHandle) tableOneBindings.get(i);
			assertTrue(two.containsKey(binding.getName()));
			assertEquals(two.get(binding.getName()), binding.getExpression());
		}

		// test for tableThree. TableThree is inside tableTwo. It is the third
		// level inner table. It have one binding defined local. it should get
		// binding from both of tableTwo and tableOne.
		TableHandle tableThree = (TableHandle) designHandle.findElement("tablethree"); //$NON-NLS-1$

		assertNotNull(tableone);
		Map three = new HashMap();
		three.putAll(one);
		three.put("TableThreeCol1", "\"TableThreeCol1\"");

		List tableThreeBindings = new ArrayList();
		for (Iterator itr = tableThree.getAvailableBindings(); itr.hasNext();) {
			tableThreeBindings.add(itr.next());
		}

		assertTrue(tableThreeBindings.size() == 3);

		// test if the returned bindings are the expected ones.
		for (int i = 0; i < tableThreeBindings.size(); i++) {
			ComputedColumnHandle binding = (ComputedColumnHandle) tableThreeBindings.get(i);
			assertTrue(three.containsKey(binding.getName()));
			assertEquals(three.get(binding.getName()), binding.getExpression());
		}

		// test for the Chart. It is in tableOne header slot. It has two
		// bindings defined local. It also should inheirate bindings from
		// tableOne.
		ExtendedItemHandle newchart = (ExtendedItemHandle) designHandle.findElement("NewChart");
		assertNotNull(newchart);

		Map chart = new HashMap();
		chart.putAll(one);
		chart.put("ChartCol1", "\"ChartCol1\"");
		chart.put("ChartCol2", "\"ChartCol2\"");

		List chartBindingList = new ArrayList();
		for (Iterator itr = newchart.getAvailableBindings(); itr.hasNext();) {
			chartBindingList.add(itr.next());
		}
		assertTrue(chartBindingList.size() == 4);

		// test if the returned bindings are the expected ones.
		for (int i = 0; i < chartBindingList.size(); i++) {
			ComputedColumnHandle binding = (ComputedColumnHandle) chartBindingList.get(i);
			assertTrue(chart.containsKey(binding.getName()));
			assertEquals(chart.get(binding.getName()), binding.getExpression());
		}
	}

}