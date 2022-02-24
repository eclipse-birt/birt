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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests handle methods for row, column and cell.
 * 
 */

public class ComponentsInGridHandleTest extends BaseTestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		openDesign("ComponentsInGridHandleTest.xml"); //$NON-NLS-1$
	}

	/**
	 * 
	 * @throws Exception
	 */

	public void testHandle() throws Exception {
		GridHandle grid = (GridHandle) designHandle.findElement("My grid"); //$NON-NLS-1$
		assertNotNull(grid);

		// test columns

		SlotHandle columns = grid.getColumns();
		assertEquals(2, columns.getCount());

		ColumnHandle column = (ColumnHandle) columns.get(0);
		assertEquals(2.5, column.getWidth().getMeasure(), 0.1);
		assertEquals(3, column.getRepeatCount());
		assertEquals("My-Style", column.getStyle().getName()); //$NON-NLS-1$

		column.setRepeatCount(5);
		assertEquals(5, column.getRepeatCount());

		// Test row properties

		SlotHandle rows = grid.getRows();
		assertEquals(2, rows.getCount());

		RowHandle row = (RowHandle) rows.get(0);
		assertEquals(5, row.getHeight().getMeasure(), 1);

		assertEquals("This is bookmark for section.", row.getBookmark()); //$NON-NLS-1$

		row.setBookmark("hello, new bookmark"); //$NON-NLS-1$

		assertEquals("hello, new bookmark", row.getBookmark()); //$NON-NLS-1$

		assertEquals("My-Style", row.getStyle().getName()); //$NON-NLS-1$

		// Test cell properties

		SlotHandle cells = row.getCells();
		assertEquals(2, cells.getCount());

		CellHandle cell = (CellHandle) cells.get(1);
		assertEquals(3, cell.getColumnSpan());
		assertEquals(1, cell.getRowSpan());
		assertEquals("all", cell.getDrop()); //$NON-NLS-1$
		assertEquals("1.5mm", cell.getHeight().getStringValue()); //$NON-NLS-1$
		assertEquals("2mm", cell.getWidth().getStringValue()); //$NON-NLS-1$
		assertEquals("red", cell.getPrivateStyle().getBackgroundColor().getStringValue()); //$NON-NLS-1$

		cell.setColumnSpan(2);
		cell.setRowSpan(2);
		cell.setDrop(DesignChoiceConstants.DROP_TYPE_DETAIL);

		assertEquals(2, cell.getColumnSpan());
		assertEquals(2, cell.getRowSpan());
		assertEquals(DesignChoiceConstants.DROP_TYPE_DETAIL, cell.getDrop());

		SlotHandle content = cell.getContent();
		LabelHandle label = (LabelHandle) content.get(0);
		assertEquals("address", label.getName()); //$NON-NLS-1$

	}

}
