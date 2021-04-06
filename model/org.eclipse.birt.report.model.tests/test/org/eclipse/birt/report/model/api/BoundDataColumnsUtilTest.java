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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests functions about the column binding.
 * 
 */

public class BoundDataColumnsUtilTest extends BaseTestCase {

	/**
	 * Test the specified element factory method for bound data column.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testUniqueColumnNameWithList() throws Exception {
		openDesign("BoundDataColumnsUtilTest_1.xml"); //$NON-NLS-1$

		// Table/List has column binding .

		ListHandle list = (ListHandle) designHandle.findElement("MyList1"); //$NON-NLS-1$
		Iterator boundColumns = list.columnBindingsIterator();
		ComputedColumnHandle column = (ComputedColumnHandle) boundColumns.next();

		// existed names on group

		try {
			column.setName("COLUMN_2"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS, e.getErrorCode());
		}

		// existed names on nested table

		column.setName("COLUMN_4"); //$NON-NLS-1$
		designHandle.getCommandStack().undo();

		// InnerData2 has column binding.

		DataItemHandle data2 = (DataItemHandle) designHandle.findElement("InnerData2"); //$NON-NLS-1$

		boundColumns = data2.columnBindingsIterator();
		column = (ComputedColumnHandle) boundColumns.next();

		// "Column_5" not exist in list and InnerData2

		column.setName("COLUMN_5"); //$NON-NLS-1$

	}

	/**
	 * Test column binding following new rule.That is: if the column binding is
	 * added to the data, only check column bindings on data
	 * 
	 * <ul>
	 * <li>Table/List only need to check its own unique name.</li>
	 * </ul>
	 * 
	 * <ul>
	 * <li>data item has column binding, use its own binding.</li>
	 * </ul>
	 * 
	 * @throws SemanticException
	 * @throws DesignFileException
	 */

	public void testAddColumnBinding() throws SemanticException, DesignFileException {
		openDesign("ReportItemHandleTest_1.xml"); //$NON-NLS-1$
		TableHandle tableHandle = (TableHandle) designHandle.getElementByID(7);

		// Table/List only need to check its own unique name.

		ComputedColumn column = StructureFactory.createComputedColumn();
		column.setName("CITY");//$NON-NLS-1$
		column.setExpression("row[\"CITY\"]");//$NON-NLS-1$
		try {
			tableHandle.addColumnBinding(column, false);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS, e.getErrorCode());
		}

		column = StructureFactory.createComputedColumn();
		column.setName("NEWCITY");//$NON-NLS-1$
		column.setExpression("row[\"NEWCITY\"]");//$NON-NLS-1$
		tableHandle.addColumnBinding(column, false);

		// data item has column binding, use its own binding.

		DataItemHandle withBindingItem = (DataItemHandle) designHandle.getElementByID(24);

		column = StructureFactory.createComputedColumn();
		column.setName("NewADDRESSLINE1");//$NON-NLS-1$
		column.setExpression("row[\"NewADDRESSLINE1\"]");//$NON-NLS-1$

		try {
			withBindingItem.addColumnBinding(column, false);
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS, e.getErrorCode());
		}

		column = StructureFactory.createComputedColumn();
		column.setName("CITY");//$NON-NLS-1$
		column.setExpression("row[\"CITY\"]");//$NON-NLS-1$
		withBindingItem.addColumnBinding(column, false);

	}

	/**
	 * Test the specified element factory method for bound data column.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testUniqueColumnNameWithTable() throws Exception {
		openDesign("BoundDataColumnsUtilTest.xml"); //$NON-NLS-1$

		TableHandle table = (TableHandle) designHandle.findElement("MyTable1"); //$NON-NLS-1$
		Iterator boundColumns = table.columnBindingsIterator();
		ComputedColumnHandle column = (ComputedColumnHandle) boundColumns.next();

		// existed names on group

		try {
			column.setName("COLUMN_2"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS, e.getErrorCode());
		}

		// existed names on nested table

		column.setName("COLUMN_4"); //$NON-NLS-1$

		designHandle.getCommandStack().undo();

		// InnerData3 has own column binding.

		DataItemHandle data3 = (DataItemHandle) designHandle.findElement("InnerData3"); //$NON-NLS-1$

		boundColumns = data3.columnBindingsIterator();
		column = (ComputedColumnHandle) boundColumns.next();
		column.setName("COLUMN_1"); //$NON-NLS-1$

	}

	/**
	 * Test cases are:
	 * 
	 * <ul>
	 * <li>
	 * <li>
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testRemoveUnusedColumns() throws Exception {
		openDesign("BoundDataColumnsUtilTest_2.xml"); //$NON-NLS-1$

		ListHandle myList = (ListHandle) designHandle.findElement("MyList1"); //$NON-NLS-1$
		myList.removedUnusedColumnBindings();

		DataItemHandle data = (DataItemHandle) designHandle.findElement("InnerData3"); //$NON-NLS-1$
		data.removedUnusedColumnBindings();

		myList = (ListHandle) designHandle.findElement("MyList2"); //$NON-NLS-1$
		myList.removedUnusedColumnBindings();

		TableHandle myTable = (TableHandle) designHandle.findElement("MyTable1"); //$NON-NLS-1$
		myTable.removedUnusedColumnBindings();

		ScalarParameterHandle param1 = (ScalarParameterHandle) designHandle.findParameter("MyParam1"); //$NON-NLS-1$
		param1.removedUnusedColumnBindings();

		// column binding refers to another column binding.

		myTable = (TableHandle) designHandle.findElement("MyTable2"); //$NON-NLS-1$
		myTable.removedUnusedColumnBindings();

		myTable = (TableHandle) designHandle.findElement("MyTable3"); //$NON-NLS-1$
		myTable.removedUnusedColumnBindings();

		save();
		assertTrue(compareFile("BoundDataColumnsUtilTest_golden_2.xml")); //$NON-NLS-1$
	}
}
