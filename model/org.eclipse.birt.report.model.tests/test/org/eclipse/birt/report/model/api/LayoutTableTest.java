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

import java.io.ByteArrayOutputStream;

import org.eclipse.birt.report.model.api.elements.table.LayoutTable;
import org.eclipse.birt.report.model.api.elements.table.LayoutUtil;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * The test case of the method <code>getColumnCount</code> in
 * <code>TableItemHandle</code>.
 *
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 *
 * </table>
 *
 * @see TableItem
 */

public class LayoutTableTest extends BaseTestCase {

	private String fileName = "LayoutTableTest1.xml"; //$NON-NLS-1$
	private String filled_fileName = "LayoutTableTest2.xml"; //$NON-NLS-1$

	private String effectiveSpans_fileName = "LayoutTableTest3.xml"; //$NON-NLS-1$

	private String layout_fileName = "LayoutTableTest4.xml"; //$NON-NLS-1$

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Tests cases to fill empty cells in the table.
	 *
	 * @throws Exception
	 */

	public void testLayoutFillsEmptyCells() throws Exception {
		openDesign(filled_fileName);

		TableHandle table = (TableHandle) designHandle.findElement("My table2"); //$NON-NLS-1$
		CellHandle cell = getCellInTableSlot(table, IListingElementModel.DETAIL_SLOT, 0, 0);
		assertEquals(3, cell.getColumn());

		save();
	}

	/**
	 * Test the table layout.
	 *
	 * @throws Exception
	 *
	 */

	public void testContentLayout() throws Exception {
		openDesign(fileName);
		os = new ByteArrayOutputStream();

		TableHandle table = (TableHandle) designHandle.findElement("My table1"); //$NON-NLS-1$
		assertNotNull(table);

		LayoutTable layout = ((TableItem) table.getElement()).getLayoutModel(design);
		os.write(layout.getLayoutString().getBytes());

		table = (TableHandle) designHandle.findElement("My table2"); //$NON-NLS-1$
		assertNotNull(table);
		layout = ((TableItem) table.getElement()).getLayoutModel(design);
		os.write(layout.getLayoutString().getBytes());

		table = (TableHandle) designHandle.findElement("My table3"); //$NON-NLS-1$
		assertNotNull(table);
		layout = ((TableItem) table.getElement()).getLayoutModel(design);
		os.write(layout.getLayoutString().getBytes());

		table = (TableHandle) designHandle.findElement("My table10"); //$NON-NLS-1$
		layout = ((TableItem) table.getElement()).getLayoutModel(design);
		os.write(layout.getLayoutString().getBytes());

		table = (TableHandle) designHandle.findElement("My table11"); //$NON-NLS-1$
		layout = ((TableItem) table.getElement()).getLayoutModel(design);
		os.write(layout.getLayoutString().getBytes());

		table = (TableHandle) designHandle.findElement("My table14"); //$NON-NLS-1$
		layout = ((TableItem) table.getElement()).getLayoutModel(design);
		os.write(layout.getLayoutString().getBytes());

		table = (TableHandle) designHandle.findElement("My table16"); //$NON-NLS-1$
		layout = ((TableItem) table.getElement()).getLayoutModel(design);
		os.write(layout.getLayoutString().getBytes());

		table = (TableHandle) designHandle.findElement("My table17"); //$NON-NLS-1$
		layout = ((TableItem) table.getElement()).getLayoutModel(design);
		os.write(layout.getLayoutString().getBytes());

		table = (TableHandle) designHandle.findElement("My table20"); //$NON-NLS-1$
		layout = ((TableItem) table.getElement()).getLayoutModel(design);
		os.write(layout.getLayoutString().getBytes());

		table = (TableHandle) designHandle.findElement("My table21"); //$NON-NLS-1$
		layout = ((TableItem) table.getElement()).getLayoutModel(design);
		os.write(layout.getLayoutString().getBytes());

		table = (TableHandle) designHandle.findElement("My table22"); //$NON-NLS-1$
		layout = ((TableItem) table.getElement()).getLayoutModel(design);
		os.write(layout.getLayoutString().getBytes());

		table = (TableHandle) designHandle.findElement("My table24"); //$NON-NLS-1$
		layout = ((TableItem) table.getElement()).getLayoutModel(design);
		os.write(layout.getLayoutString().getBytes());

		table = (TableHandle) designHandle.findElement("My table25"); //$NON-NLS-1$
		layout = ((TableItem) table.getElement()).getLayoutModel(design);
		os.write(layout.getLayoutString().getBytes());

		table = (TableHandle) designHandle.findElement("My table27"); //$NON-NLS-1$
		layout = ((TableItem) table.getElement()).getLayoutModel(design);
		os.write(layout.getLayoutString().getBytes());

		table = (TableHandle) designHandle.findElement("My table28"); //$NON-NLS-1$
		layout = ((TableItem) table.getElement()).getLayoutModel(design);
		os.write(layout.getLayoutString().getBytes());

		table = (TableHandle) designHandle.findElement("My table30"); //$NON-NLS-1$
		layout = ((TableItem) table.getElement()).getLayoutModel(design);
		os.write(layout.getLayoutString().getBytes());

		table = (TableHandle) designHandle.findElement("My table31"); //$NON-NLS-1$
		layout = ((TableItem) table.getElement()).getLayoutModel(design);
		os.write(layout.getLayoutString().getBytes());

		table = (TableHandle) designHandle.findElement("My table32"); //$NON-NLS-1$
		layout = ((TableItem) table.getElement()).getLayoutModel(design);
		os.write(layout.getLayoutString().getBytes());

		table = (TableHandle) designHandle.findElement("My table33"); //$NON-NLS-1$
		layout = ((TableItem) table.getElement()).getLayoutModel(design);
		os.write(layout.getLayoutString().getBytes());

		table = (TableHandle) designHandle.findElement("My table34"); //$NON-NLS-1$
		layout = ((TableItem) table.getElement()).getLayoutModel(design);
		os.write(layout.getLayoutString().getBytes());

		os.close();

		assertTrue(compareFile("LayoutTableTest1_golden.txt")); //$NON-NLS-1$ .

	}

	/**
	 *
	 * @throws Exception
	 */

	public void testEffectiveSpans() throws Exception {
		openDesign(effectiveSpans_fileName);

		TableHandle table = (TableHandle) designHandle.findElement("My table1"); //$NON-NLS-1$

		CellHandle cell = getCellInTableSlot(table, TableItem.DETAIL_SLOT, 0, 0);
		assertEquals(1, LayoutUtil.getEffectiveRowSpan(cell));
		assertEquals(1, LayoutUtil.getEffectiveColumnSpan(cell));

		cell = getCellInTableSlot(table, TableItem.DETAIL_SLOT, 1, 0);
		assertEquals(1, LayoutUtil.getEffectiveRowSpan(cell));
		assertEquals(2, LayoutUtil.getEffectiveColumnSpan(cell));

		table = (TableHandle) designHandle.findElement("My table2"); //$NON-NLS-1$
		cell = getCellInTableSlot(table, TableItem.DETAIL_SLOT, 0, 1);
		assertEquals(1, LayoutUtil.getEffectiveRowSpan(cell));
		assertEquals(1, LayoutUtil.getEffectiveColumnSpan(cell));

		cell = getCellInTableSlot(table, TableItem.DETAIL_SLOT, 1, 0);
		assertEquals(1, LayoutUtil.getEffectiveRowSpan(cell));
		assertEquals(2, LayoutUtil.getEffectiveColumnSpan(cell));
	}

	/**
	 * Returns a cell handle for a cell in the slot of the table.
	 *
	 * @param table     the table element
	 * @param slotID    <code>TableItem.FOOTER_SLOT</code> or
	 *                  <code>TableItem.HEADER_SLOT</code> or
	 *                  <code>TableItem.DETAIL_SLOT</code>.
	 * @param rowIndex  the number of row in the slot
	 * @param cellIndex the index of cell in the row.
	 *
	 * @return a cell handle with the given information.
	 */

	private CellHandle getCellInTableSlot(TableHandle table, int slotID, int rowIndex, int cellIndex) {
		RowHandle row = (RowHandle) (table.getSlot(slotID).get(rowIndex));
		CellHandle cell = (CellHandle) (row.getSlot(TableRow.CONTENT_SLOT).get(cellIndex));
		return cell;
	}

	/**
	 * Tests cases to check whether the table layout is valid or not. The invalid
	 * table has following cases:
	 * <ul>
	 * <li>has overlapped area.
	 * <li>column counts in rows are different.
	 * <li>empty rows and columns.
	 * </ul>
	 *
	 * @throws Exception
	 */

	public void testInvalidLayout() throws Exception {
		openDesign(layout_fileName);

		TableHandle table = (TableHandle) designHandle.findElement("My table1"); //$NON-NLS-1$
		assertFalse(LayoutUtil.isValidLayout((TableItem) table.getElement(), design));

		// overlapped area is OK.

		table = (TableHandle) designHandle.findElement("My table2"); //$NON-NLS-1$
		assertTrue(LayoutUtil.isValidLayout((TableItem) table.getElement(), design));

		table = (TableHandle) designHandle.findElement("My table3"); //$NON-NLS-1$
		assertTrue(LayoutUtil.isValidLayout((TableItem) table.getElement(), design));

		table = (TableHandle) designHandle.findElement("My table4"); //$NON-NLS-1$
		assertTrue(LayoutUtil.isValidLayout((TableItem) table.getElement(), design));

		table = (TableHandle) designHandle.findElement("My table5"); //$NON-NLS-1$
		assertFalse(LayoutUtil.isValidLayout((TableItem) table.getElement(), design));
	}
}
