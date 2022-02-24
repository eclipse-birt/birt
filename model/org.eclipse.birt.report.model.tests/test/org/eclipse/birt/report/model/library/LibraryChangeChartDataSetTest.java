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

package org.eclipse.birt.report.model.library;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.MemberHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests change data set of report item.
 */

public class LibraryChangeChartDataSetTest extends BaseTestCase {

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		openDesign("DesignWithChartLibrary.xml"); //$NON-NLS-1$
	}

	/**
	 * Tests change dataset of table.
	 *
	 * @throws SemanticException
	 */

	public void testChangeTableDataSet() throws SemanticException {
		TableHandle tableHandle = (TableHandle) designHandle.findElement("NewTable");//$NON-NLS-1$
		assertNotNull(tableHandle);

		int count = getColumnBindingsCount(tableHandle.columnBindingsIterator());
		assertEquals(7, count);

		// if the column has the same filter expression as the original column,
		// the column will not be added.
		ComputedColumn column = StructureFactory.newComputedColumn(tableHandle, "test"); //$NON-NLS-1$
		List<ComputedColumn> columns = tableHandle.getListProperty(ReportItemHandle.BOUND_DATA_COLUMNS_PROP);

		column.setFilterExpression(columns.get(6).getFilterExpression());
		column.setExpression(columns.get(6).getExpression());
		tableHandle.addColumnBinding(column, false);

		columns = tableHandle.getListProperty(ReportItemHandle.BOUND_DATA_COLUMNS_PROP);

		assertEquals(7, columns.size());

		// if the column does not have the same filter expression as the orginal
		// column, the column will be added.
		column.setFilterExpression("new expression"); //$NON-NLS-1$
		tableHandle.addColumnBinding(column, false);
		columns = tableHandle.getListProperty(ReportItemHandle.BOUND_DATA_COLUMNS_PROP);

		assertEquals(8, columns.size());

		DataSetHandle newDsHandle = (DataSetHandle) designHandle.getElementByID(6);
		assertNotNull(newDsHandle);
		try {
			tableHandle.setDataSet(newDsHandle);

		} catch (SemanticException e) {
			fail("can't set data set " + e.getMessage());//$NON-NLS-1$
		}

		addColumnBindings(tableHandle, newDsHandle);
		Iterator iterator = tableHandle.columnBindingsIterator();
		count = getColumnBindingsCount(iterator);

		assertEquals(5, count);
	}

	/**
	 * Gets count of column bindings.
	 *
	 * @param iterator column bindings iterator.
	 * @return count of column bindings
	 */

	private int getColumnBindingsCount(Iterator iterator) {
		int count = 0;
		while (iterator != null && iterator.hasNext()) {
			Object obj = iterator.next();
			if (obj != null) {
				++count;
			}
		}
		return count;
	}

	/**
	 * Tests change dataset of chart.
	 *
	 * @throws SemanticException
	 *
	 */

	public void testChangeChartDataSet() throws SemanticException {
		ExtendedItemHandle itemHandle = (ExtendedItemHandle) designHandle.findElement("NewTestingMatrix"); //$NON-NLS-1$
		assertNotNull(itemHandle);

		int count = getColumnBindingsCount(itemHandle.columnBindingsIterator());
		assertEquals(5, count);

		DataSetHandle newDsHandle = (DataSetHandle) designHandle.getElementByID(7);
		assertNotNull(newDsHandle);
		try {
			itemHandle.setDataSet(newDsHandle);

		} catch (SemanticException e) {
			fail("can't set data set " + e.getMessage());//$NON-NLS-1$
		}

		addColumnBindings(itemHandle, newDsHandle);
		Iterator iterator = itemHandle.columnBindingsIterator();
		count = getColumnBindingsCount(iterator);

		assertEquals(7, count);

	}

	/**
	 * Add column binding for report item.
	 *
	 * @param itemHandle report item handle
	 * @param dsHandle   data set handle
	 * @throws SemanticException
	 */

	private void addColumnBindings(ReportItemHandle itemHandle, DataSetHandle dsHandle) throws SemanticException {
		itemHandle.getColumnBindings().clearValue();

		CachedMetaDataHandle meta = dsHandle.getCachedMetaDataHandle();
		MemberHandle resultSet = meta.getResultSet();

		if (resultSet.getListValue() != null) {
			for (int i = 0; i < resultSet.getListValue().size(); i++) {
				ResultSetColumnHandle resultSetColumn = (ResultSetColumnHandle) resultSet.getAt(i);
				ComputedColumn column = StructureFactory.newComputedColumn(itemHandle, resultSetColumn.getColumnName());
				column.setDataType(resultSetColumn.getDataType());
				column.setExpression("row[" + resultSetColumn.getColumnName() + "]");//$NON-NLS-1$ //$NON-NLS-2$

				itemHandle.addColumnBinding(column, false);
			}
		}
	}

}
