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

package org.eclipse.birt.report.designer.core.model.schematic;

import java.util.Iterator;

import org.eclipse.birt.report.designer.core.model.ReportItemtHandleAdapter;
import org.eclipse.birt.report.designer.testutil.BaseTestCase;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.core.StyledElement;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.swt.SWT;

/**
 * Tests for TableHandleAdapter
 */
public class TableHandleAdapterTest extends BaseTestCase {

	private TableHandle table;
	private TableHandleAdapter adapter;

	private static final String TEST_TABLE_NAME = "Table";

	protected void setUp() throws Exception {
		super.setUp();
		table = getElementFactory().newTableItem(TEST_TABLE_NAME);
		adapter = new TableHandleAdapter(table, null);
	}

	private void createTestTable() {
		RowHandle row = getElementFactory().newTableRow();
		CellHandle cell = getElementFactory().newCell();

		try {
			cell.setColumn(1);
			row.getSlot(TableRow.CONTENT_SLOT).add(cell);
			table.getHeader().add(row);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("error add cell to table");
		}

		TableGroupHandle group = getElementFactory().newTableGroup();
		try {
			row = getElementFactory().newTableRow();
			cell = getElementFactory().newCell();
			cell.setColumn(1);
			row.getSlot(TableRow.CONTENT_SLOT).add(cell);

			cell = getElementFactory().newCell();
			cell.setColumn(1);
			row.getSlot(TableRow.CONTENT_SLOT).add(cell);

			group.getFooter().add(row);
			table.getGroups().add(group);

		} catch (Exception ex) {
			ex.printStackTrace();
			fail("error add cell to table");
		}

		ColumnHandle column = getElementFactory().newTableColumn();
		row = getElementFactory().newTableRow();
		cell = getElementFactory().newCell();

		try {
			table.getColumns().add(column);
			cell.setColumn(1);
			row.getSlot(TableRow.CONTENT_SLOT).add(cell);
			table.getDetail().add(row);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("error add cell to table");
		}

		row = getElementFactory().newTableRow();
		cell = getElementFactory().newCell();
		try {
			cell.setColumn(1);
			row.getSlot(TableRow.CONTENT_SLOT).add(cell);
			table.getFooter().add(row);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("error add cell to table");
		}
	}

	public void testGetBackGroundColor() {

		StyleHandle styleHandle = getElementFactory().newStyle(null);

		// table.setStyleElement(styleHandle.getElement());
		((StyledElement) table.getElement()).setStyle((StyleElement) styleHandle.getElement());

		styleHandle = table.getStyle();

		ReportItemtHandleAdapter adapter = new TableHandleAdapter(table, null);

		int color = adapter.getBackgroundColor(adapter.getHandle());
		// if the back ground was not set , return 0xffffff
		// assertEquals( 0xffffff, color );
		// if the back ground was not set , return system default background color
		assertEquals(SWT.COLOR_LIST_BACKGROUND, color);

		try {
			styleHandle.setProperty(Style.BACKGROUND_COLOR_PROP, "0xff1234");
		} catch (SemanticException e) {
			fail("error set background color");
			e.printStackTrace();
		}

		color = adapter.getBackgroundColor(adapter.getHandle());
		assertEquals(0xff1234, color);
	}

	public void testGetForeGroundColor() {
		StyleHandle styleHandle = getElementFactory().newStyle(null);

		// table.setStyleElement(styleHandle.getElement());
		((StyledElement) table.getElement()).setStyle((StyleElement) styleHandle.getElement());

		styleHandle = table.getStyle();

		ReportItemtHandleAdapter adapter = new TableHandleAdapter(table, null);

		int color = adapter.getForegroundColor(adapter.getHandle());
		// if the foreground was not set , return 0x000000
		assertEquals(0x00, color);

		try {
			styleHandle.setProperty("color", "0xff1234");
		} catch (SemanticException e) {
			fail("error set  color");
			e.printStackTrace();
		}

		adapter = new TableHandleAdapter(table, null);

		color = adapter.getForegroundColor(adapter.getHandle());
		assertEquals(0xff1234, color);
	}

	public void testGetChildren() {
		createTestTable();
		assertFalse(adapter.getChildren().isEmpty());
		for (Iterator it = adapter.getChildren().iterator(); it.hasNext();) {
			assertTrue(it.next() instanceof CellHandle);
		}
	}

	public void testGetRows() {
		createTestTable();
		assertFalse(adapter.getRows().isEmpty());
		for (Iterator it = adapter.getRows().iterator(); it.hasNext();) {
			assertTrue(it.next() instanceof RowHandle);
		}
	}

	public void testGetRow() {
		createTestTable();
		assertNotNull(adapter.getRow(2));
		assertNull("how can you get row?", adapter.getRow(100));
	}

	public void testGetColumns() {
		createTestTable();
		assertFalse(adapter.getColumns().isEmpty());
		for (Iterator it = adapter.getColumns().iterator(); it.hasNext();) {
			Object obj = it.next();
			assertTrue(obj instanceof ColumnHandle);
		}
	}

	public void testGetColumn() {
		createTestTable();
		assertNotNull(adapter.getColumn(1));
		assertNull("how can you get this column", adapter.getColumn(2));
	}

	public void testGetCell() {
		createTestTable();
		assertNotNull(adapter.getCell(1, 1));
		assertNotNull(adapter.getCell(2, 1));
		assertNull("how can you get this cell", adapter.getCell(1, 0));
		assertNull("how can you get this cell", adapter.getCell(1, 2));
		assertNull("how can you get this cell", adapter.getCell(100, 1));
	}

	private ElementFactory getElementFactory() {
		return getReportDesignHandle().getElementFactory();
	}

}
