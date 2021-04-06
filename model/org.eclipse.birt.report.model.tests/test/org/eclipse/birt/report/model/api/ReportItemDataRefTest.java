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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.util.BaseTestCase;

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
 * 
 * </table>
 */

public class ReportItemDataRefTest extends BaseTestCase {

	private static final String FILE_NAME = "ReportItemDataRefTest.xml"; //$NON-NLS-1$
	private static final String FILE_NAME_1 = "ReportItemDataRefTest_1.xml"; //$NON-NLS-1$

	/**
	 * Tests the function for adding bound data columns.
	 * 
	 * @throws Exception
	 */

	public void testDataBindingRef() throws Exception {
		openDesign("ReportItemHandleTest_2.xml"); //$NON-NLS-1$

		DataItemHandle data1 = (DataItemHandle) designHandle.findElement("myData1"); //$NON-NLS-1$

		Iterator columns = data1.columnBindingsIterator();
		ComputedColumnHandle column = (ComputedColumnHandle) columns.next();
		verifyColumnValues(column);

		DataItemHandle data2 = (DataItemHandle) designHandle.findElement("myData2"); //$NON-NLS-1$
		columns = data2.columnBindingsIterator();
		column = (ComputedColumnHandle) columns.next();
		verifyColumnValues(column);

		assertEquals("myData1", data2.getDataBindingReferenceName()); //$NON-NLS-1$

		DataItemHandle newData = (DataItemHandle) designHandle.findElement("myData3"); //$NON-NLS-1$
		newData.setDataBindingReference(data2);

		columns = newData.columnBindingsIterator();
		column = (ComputedColumnHandle) columns.next();
		verifyColumnValues(column);

		// set binding reference to a no-name element
		DataItemHandle noNameData = designHandle.getElementFactory().newDataItem(null);
		assertNull(noNameData.getName());
		try {
			newData.setDataBindingReference(noNameData);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, e.getErrorCode());
		}

		try {
			newData.setDataBindingReference(newData);
			fail();
		} catch (SemanticException e) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_CIRCULAR_ELEMENT_REFERNECE, e.getErrorCode());
		}

		try {
			data1.setDataBindingReference(data2);
			fail();
		} catch (SemanticException e) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_CIRCULAR_ELEMENT_REFERNECE, e.getErrorCode());
		}

		// parameter biding in both data and table, should get value from the
		// table

		Iterator paramBindings = data2.paramBindingsIterator();
		ParamBindingHandle paramBinding = (ParamBindingHandle) paramBindings.next();
		assertEquals("table value1", paramBinding.getExpression()); //$NON-NLS-1$

		TableHandle table2 = (TableHandle) designHandle.findElement("myTable2"); //$NON-NLS-1$
		Iterator filters = table2.filtersIterator();
		FilterConditionHandle filter = (FilterConditionHandle) filters.next();
		assertEquals("table 1 filter expression", filter.getExpr()); //$NON-NLS-1$

		Iterator sorts = table2.sortsIterator();
		SortKeyHandle sort = (SortKeyHandle) sorts.next();
		assertEquals("table 1 name", sort.getKey()); //$NON-NLS-1$
	}

	/**
	 * Tests getDataBindingType() and getAvailableDataBindingReferenceList.
	 * 
	 * @throws Exception
	 */

	public void testgetAvailableDataBindingReferenceList() throws Exception {
		openDesign("ReportItemHandleBindingDataTypeTest.xml"); //$NON-NLS-1$

		TextItemHandle text = (TextItemHandle) designHandle.findElement("myText"); //$NON-NLS-1$
		assertEquals(ReportItemHandle.DATABINDING_TYPE_DATA, text.getDataBindingType());

		List<ReportItemHandle> tmpList = text.getAvailableCubeBindingReferenceList();
		assertEquals(5, tmpList.size());

		// make sure the last one has no name.
		assertNull(tmpList.get(4).getName());

		assertEquals(4, text.getNamedCubeBindingReferenceList().size());

		tmpList = text.getAvailableDataSetBindingReferenceList();
		assertEquals(7, tmpList.size());

		// make sure the last one has no name.
		assertNull(tmpList.get(6).getName());

		tmpList = text.getAvailableDataBindingReferenceList();
		assertEquals(10, tmpList.size());

		assertEquals(6, text.getNamedDataSetBindingReferenceList().size());

		ListHandle list = (ListHandle) designHandle.findElement("my list"); //$NON-NLS-1$
		assertEquals(ReportItemHandle.DATABINDING_TYPE_DATA, list.getDataBindingType());

		ExtendedItemHandle extendedItem = (ExtendedItemHandle) designHandle.findElement("ex1"); //$NON-NLS-1$
		assertEquals(ReportItemHandle.DATABINDING_TYPE_DATA, extendedItem.getDataBindingType());

		TableHandle table = (TableHandle) designHandle.findElement("table"); //$NON-NLS-1$
		assertEquals(ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF, table.getDataBindingType());

		DataItemHandle data = (DataItemHandle) designHandle.findElement("data1"); //$NON-NLS-1$
		assertEquals(ReportItemHandle.DATABINDING_TYPE_NONE, data.getDataBindingType());

		// cannot contain self and elements that refer to self.

		PropertyHandle propHandle = list.getPropertyHandle(ReportItemHandle.DATA_BINDING_REF_PROP);
		List handleList = propHandle.getReferenceableElementList();
		assertEquals(7, handleList.size());

		assertEquals("myText", ((DesignElementHandle) handleList.get(0)) //$NON-NLS-1$
				.getName());
		assertEquals("ex1", ((DesignElementHandle) handleList.get(1)) //$NON-NLS-1$
				.getName());
		assertEquals("table", ((DesignElementHandle) handleList.get(2)) //$NON-NLS-1$
				.getName());
		assertEquals("table2", ((DesignElementHandle) handleList.get(3)) //$NON-NLS-1$
				.getName());
		assertEquals("data1", ((DesignElementHandle) handleList.get(4)) //$NON-NLS-1$
				.getName());
		assertEquals("table5", ((DesignElementHandle) handleList.get(5)) //$NON-NLS-1$
				.getName());
		assertEquals("data2", ((DesignElementHandle) handleList.get(6)) //$NON-NLS-1$
				.getName());

	}

	private void verifyColumnValues(ComputedColumnHandle column) {
		assertEquals("CUSTOMERNUMBER", column.getName()); //$NON-NLS-1$
		assertEquals("dataSetRow[\"CUSTOMERNUMBER\"]", column.getExpression()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER, column.getDataType());
	}

	/**
	 * Tests the property search algorithm for data groups that have data binding
	 * reference.
	 * 
	 * @throws Exception
	 */

	public void testPropsOfDataGroupRef() throws Exception {
		openDesign(FILE_NAME);

		TableHandle table2 = (TableHandle) designHandle.findElement("myTable2"); //$NON-NLS-1$
		TableGroupHandle group2 = (TableGroupHandle) table2.getGroups().get(0);
		assertEquals("row[\"CUSTOMERNAME\"]", group2.getKeyExpr()); //$NON-NLS-1$
		assertEquals("group1", group2.getName()); //$NON-NLS-1$
		assertEquals("group1", group2.getDisplayLabel()); //$NON-NLS-1$

		Iterator iter1 = group2.filtersIterator();
		FilterConditionHandle filter = (FilterConditionHandle) iter1.next();
		assertEquals("table 1 filter expression", filter.getExpr()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.FILTER_OPERATOR_LT, filter.getOperator());

		iter1 = group2.sortsIterator();
		SortKeyHandle sort = (SortKeyHandle) iter1.next();
		assertEquals("table 1 name", sort.getKey()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.SORT_DIRECTION_ASC, sort.getDirection());

		TableHandle table1 = (TableHandle) designHandle.findElement("myTable1"); //$NON-NLS-1$

		TableGroupHandle group1 = (TableGroupHandle) table1.getGroups().get(0);
		group1.setKeyExpr("the new expression"); //$NON-NLS-1$
		assertEquals("the new expression", group2.getKeyExpr()); //$NON-NLS-1$

		group1.setName("newGroup1"); //$NON-NLS-1$
		assertEquals("newGroup1", group2.getDisplayLabel()); //$NON-NLS-1$

		// list refers to the table

		ListHandle list1 = (ListHandle) designHandle.findElement("myList1"); //$NON-NLS-1$

		ListGroupHandle listGroup = (ListGroupHandle) list1.getGroups().get(0);
		assertEquals("the new expression", listGroup.getKeyExpr()); //$NON-NLS-1$
		assertEquals("newGroup1", listGroup.getName()); //$NON-NLS-1$
		assertEquals("newGroup1", listGroup.getDisplayLabel()); //$NON-NLS-1$

		iter1 = listGroup.filtersIterator();
		filter = (FilterConditionHandle) iter1.next();
		assertEquals("table 1 filter expression", filter.getExpr()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.FILTER_OPERATOR_LT, filter.getOperator());

	}

	/**
	 * Tests the command to add, remove and move the group element.
	 * 
	 * @throws Exception
	 */

	public void testAddandRemoveDataGroup() throws Exception {
		openDesign(FILE_NAME);

		TableHandle table1 = (TableHandle) designHandle.findElement("myTable1"); //$NON-NLS-1$

		GroupHandle newGroup = designHandle.getElementFactory().newTableGroup();
		table1.addElement(newGroup, TableHandle.GROUP_SLOT);

		save();
		assertTrue(compareFile("DataGroupAdded_golden.xml")); //$NON-NLS-1$

		newGroup.drop();
		save();
		assertTrue(compareFile("DataGroupDropped_golden.xml")); //$NON-NLS-1$

		designHandle.getCommandStack().undo();
		save();
		assertTrue(compareFile("DataGroupUndoDrop_golden.xml")); //$NON-NLS-1$

		newGroup.getContainerSlotHandle().shift(newGroup, 0);
		save();
		assertTrue(compareFile("DataGroupShiftPosition_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests canEdit(), canDrop() methods for the shared data group. canContain()
	 * should be true always.
	 * 
	 * @throws Exception
	 */

	public void testCanMumbleForDataGroup() throws Exception {
		openDesign(FILE_NAME);

		TableHandle table2 = (TableHandle) designHandle.findElement("myTable2"); //$NON-NLS-1$
		TableGroupHandle group2 = (TableGroupHandle) table2.getGroups().get(0);

		assertFalse(group2.canDrop());

		assertTrue(group2.canContain(GroupHandle.HEADER_SLOT, ReportDesignConstants.ROW_ELEMENT));

		assertFalse(table2.canContain(TableHandle.GROUP_SLOT, designHandle.getElementFactory().newTableGroup()));

		assertFalse(table2.canContain(TableHandle.GROUP_SLOT, group2.getDefn().getName()));

		assertTrue(group2.canContain(GroupHandle.FOOTER_SLOT, ReportDesignConstants.ROW_ELEMENT));
	}

	/**
	 * Cases:
	 * 
	 * <ul>
	 * <li>table refers to the table
	 * <li>list refers to the table
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testEstablishDataGroup() throws Exception {
		openDesign("DataGroupRef_2.xml"); //$NON-NLS-1$

		TableHandle table2 = (TableHandle) designHandle.findElement("myTable2"); //$NON-NLS-1$

		TableHandle table1 = (TableHandle) designHandle.findElement("myTable1"); //$NON-NLS-1$

		ListHandle list1 = (ListHandle) designHandle.findElement("myList1"); //$NON-NLS-1$

		CommandStack cmdStack = designHandle.getCommandStack();
		cmdStack.startTrans(null);
		table2.setDataBindingReference(table1);
		list1.setDataBindingReference(table1);
		cmdStack.commit();

		save();
		assertTrue(compareFile("SetDataGroupRef_golden.xml")); //$NON-NLS-1$

		designHandle.getCommandStack().undo();
		save();
		assertTrue(compareFile("SetDataGroupRefUndo_golden.xml")); //$NON-NLS-1$

		designHandle.getCommandStack().redo();
		save();
		assertTrue(compareFile("SetDataGroupRefRedo_golden.xml")); //$NON-NLS-1$

		cmdStack.startTrans(null);

		table2.setDataBindingReference(null);
		list1.setDataBindingReference(null);
		cmdStack.commit();

		save();
		assertTrue(compareFile("SetDataGroupRefNull_golden.xml")); //$NON-NLS-1$

		designHandle.getCommandStack().undo();
		save();
		assertTrue(compareFile("SetDataGroupRefNullUndo_golden.xml")); //$NON-NLS-1$

		cmdStack.startTrans(null);

		TableHandle table3 = designHandle.getElementFactory().newTableItem("myTable3");//$NON-NLS-1$
		table2.setDataBindingReference(table3);
		list1.setDataBindingReference(table3);
		cmdStack.commit();

		save();
		assertTrue(compareFile("SetDataGroupRefInvalid_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * @throws Exception
	 */

	public void testParseInconsistentDataGroup() throws Exception {
		openDesign("DataGroupRef_3.xml"); //$NON-NLS-1$

		assertEquals(3, designHandle.getWarningList().size());

		List warnings = designHandle.getWarningList();
		assertEquals(
				"The data binding reference of the element Table(\"myTable3\") has different number of groups with element Table(\"myTable1\") it refers to.", //$NON-NLS-1$
				((ErrorDetail) warnings.get(0)).getMessage());

		assertEquals(
				"The data binding reference of the element Table(\"myTable2\") has different number of groups with element Table(\"myTable1\") it refers to.", //$NON-NLS-1$
				((ErrorDetail) warnings.get(1)).getMessage());

		assertEquals(
				"The data binding reference of the element List(\"myList1\") has different number of groups with element Table(\"myTable1\") it refers to.", //$NON-NLS-1$
				((ErrorDetail) warnings.get(2)).getMessage());

		save();
		assertTrue(compareFile("ParseInconsistentDataGroup_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests get data binding of the container of the element.
	 * 
	 * @throws Exception
	 */
	public void testGetDataBindingOfContainer() throws Exception {
		openDesign("DataBindingOfContainerTest.xml"); //$NON-NLS-1$

		TableHandle table = (TableHandle) designHandle.getElementByID(9);

		List list = table.getAvailableDataSetBindingReferenceList();

		assertEquals(0, list.size());

		ExtendedItemHandle handle = (ExtendedItemHandle) designHandle.getElementByID(28);

		try {
			table.setDataBindingReference(handle);
			fail();
		} catch (SemanticError e) {

			assertEquals(SemanticError.DESIGN_EXCEPTION_INVALID_DATA_BINDING_REF, e.getErrorCode());
		}

	}

	/**
	 * Tests the properties and group structure when the referred table is removed
	 * by calling dropAndClear.
	 * 
	 * @throws Exception
	 */
	public void testDropAndClear() throws Exception {
		openDesign(FILE_NAME);

		TableHandle table1 = (TableHandle) designHandle.findElement("myTable1"); //$NON-NLS-1$
		table1.dropAndClear();

		save();
		assertTrue(compareFile("ReportItemDataRefTest_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Tests the handle for the localization of column bindings. If the two items
	 * have different element type, the binding with aggregation will not be added.
	 * 
	 * @throws Exception
	 */
	public void testLocalizeColumnBindingWithAggregation() throws Exception {
		openDesign(FILE_NAME_1);
		TableHandle table1 = (TableHandle) designHandle.findElement("myTable1"); //$NON-NLS-1$
		table1.dropAndClear();

		save();
		assertTrue(compareFile("ReportItemDataRefTest_golden_1.xml")); //$NON-NLS-1$
	}

	public void testDataBindingRefWithDifferentFilterType() throws Exception {
		openDesign("ReportItemDataRefTest_2.xml"); //$NON-NLS-1$
		DesignElementHandle element = designHandle.getBody().get(1);
		Object value = element.getProperty("filter"); //$NON-NLS-1$
		assertNull(value);
	}
}