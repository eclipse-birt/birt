/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.util;

import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.ColumnBindingUtil;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;

/**
 * Test cases for ColumnBindingUtil class.
 *
 */

public class ColumnBindingUtilTest extends BaseTestCase {

	public void testAddColumnBinding() throws Exception {
		createDesign();
		TableHandle table = designHandle.getElementFactory().newTableItem(null);
		designHandle.getBody().add(table);
		PropertyHandle propHandle = table.getPropertyHandle(IReportItemModel.BOUND_DATA_COLUMNS_PROP);

		ComputedColumn column = StructureFactory.createComputedColumn();
		column.setName("column"); //$NON-NLS-1$
		column.setDisplayName("Column Name"); //$NON-NLS-1$
		column.setDataType(DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME);
		column.setExpression("test expression"); //$NON-NLS-1$
		column.setAggregateOn("testAggregate"); //$NON-NLS-1$
		column.setFilterExpression("test filter expression"); //$NON-NLS-1$

		// Test adding new column binding.
		ComputedColumnHandle boundColumn = ColumnBindingUtil.addColumnBinding(table, column);
		assertEquals(1, propHandle.getItems().size());
		assertNotNull(boundColumn);
		assertEquals(column, boundColumn.getStructure());

		// Test adding the same column binding with a different name.
		// Expects that the column is not added and the exist one is returned.
		ComputedColumn copiedColumn = (ComputedColumn) column.copy();
		copiedColumn.setName("new column"); //$NON-NLS-1$
		ComputedColumnHandle newBoundColumn = ColumnBindingUtil.addColumnBinding(table, copiedColumn);
		assertEquals(1, propHandle.getItems().size());
		assertEquals(boundColumn.getContext(), newBoundColumn.getContext());
		assertEquals("column", newBoundColumn.getName()); //$NON-NLS-1$

		// Test adding columns with different properties and the same name.
		// Expects a new column binding added with a new unique name.
		copiedColumn = (ComputedColumn) column.copy();
		copiedColumn.setDataType(DesignChoiceConstants.COLUMN_DATA_TYPE_STRING);
		assertEquals("column_1", ColumnBindingUtil.addColumnBinding(table, //$NON-NLS-1$
				copiedColumn).getName());
		assertEquals(2, propHandle.getItems().size());

		copiedColumn = (ComputedColumn) column.copy();
		copiedColumn.setFilterExpression("new filter expression"); //$NON-NLS-1$
		assertEquals("column_2", ColumnBindingUtil.addColumnBinding(table, //$NON-NLS-1$
				copiedColumn).getName());
		assertEquals(3, propHandle.getItems().size());

		try {
			ColumnBindingUtil.addColumnBinding(table, StructureFactory.createComputedColumn());
			fail();
		} catch (SemanticException e) {
			assertTrue(e instanceof PropertyValueException);
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED, e.getErrorCode());
		}
	}
}
