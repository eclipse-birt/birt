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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * The test case of <code>GridItem</code> parser and writer.
 * <p>
 * <code>TableColumn</code>,<code>TableRow</code> and <code>Cell</code> are also
 * tested in this test case.
 *
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 *
 * <tr>
 * <td>{@link #testMethods()}</td>
 * <td>Test column slot of GridItem after parsing design file</td>
 * <td>The column number in the slot is 2. And the column number is 4.</td>
 * </tr>
 *
 * <tr>
 * <td></td>
 * <td>Test row slot of GridItem after parsing design file</td>
 * <td>The row number in the slot is 2.</td>
 * </tr>
 *
 * </table>
 */

public class GridItemHandleTest extends BaseTestCase {

	private String fileName = "GridItemHandleTest.xml"; //$NON-NLS-1$

	/*
	 * @see TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		openDesign(fileName);
	}

	/**
	 * Tests the column count on grids that have column definition or not.
	 *
	 * @throws Exception
	 *
	 */

	public void testMethods() throws Exception {

		assertEquals(0, design.getErrorList().size());

		GridHandle gridHandle = (GridHandle) designHandle.findElement("My grid"); //$NON-NLS-1$

		assertNotNull(gridHandle);

		// Test column properties
		assertEquals(4, gridHandle.getColumnCount());

		SlotHandle slotHandle = gridHandle.getColumns();
		assertEquals(2, slotHandle.getCount());

		// Test on row properties.

		slotHandle = gridHandle.getRows();
		assertEquals(2, slotHandle.getCount());

		// Test column properties

		gridHandle = (GridHandle) designHandle.findElement("My grid 1"); //$NON-NLS-1$

		assertEquals(5, gridHandle.getColumnCount());

		slotHandle = gridHandle.getColumns();
		assertEquals(0, slotHandle.getCount());

	}

	/**
	 * Test copy , paste , insert , shift table row.
	 *
	 * @throws Exception
	 */
	public void testRowCopyPasteAction() throws Exception {
		GridHandle gridHandle = (GridHandle) designHandle.findElement("My grid"); //$NON-NLS-1$
		GridHandle gridHandle2 = (GridHandle) designHandle.findElement("My grid 1");//$NON-NLS-1$
		RowOperationParameters parameters1 = new RowOperationParameters(0, -1, 0);
		RowOperationParameters parameters2 = new RowOperationParameters(0, -1, 1);
		RowOperationParameters parameters3 = new RowOperationParameters(0, -1, 100);
		RowOperationParameters parameters4 = new RowOperationParameters(0, -1, -100);

		// Test canCopy method.

		assertTrue(gridHandle.canCopyRow(parameters1));
		assertTrue(gridHandle.canCopyRow(parameters2));

		// slotid out of range.

		assertFalse(gridHandle.canCopyRow(parameters3));
		assertFalse(gridHandle.canCopyRow(parameters4));

		IDesignElement clonedData = gridHandle.copyRow(parameters1);
		TableRow clonedRow = (TableRow) clonedData.getHandle(design).getElement();

		Cell cell = (Cell) clonedRow.getContentsSlot().get(0);
		Object obj = cell.getSlot(0).getContents().get(0);
		assertTrue(obj instanceof Label);
		assertNull(clonedRow.getContainer());

		try {
			gridHandle.copyRow(parameters3);
			fail("fail to copy row"); //$NON-NLS-1$
		} catch (SemanticException e) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_ROW_COPY_FORBIDDEN, e.getErrorCode());
		}

		// Test canPaste method.

		assertTrue(gridHandle.canPasteRow(clonedData, parameters1));

		// slotid is out of range.

		assertFalse(gridHandle.canPasteRow(clonedData, parameters3));
		assertFalse(gridHandle.canPasteRow(clonedData, parameters4));

		gridHandle.pasteRow(clonedData, parameters2);
		save();
		assertTrue(compareFile("GridRowCopy_golden_1.xml")); //$NON-NLS-1$

		try {
			gridHandle2.pasteRow(null, parameters1);
			fail("fail to paste table row in grid because copied row is null"); //$NON-NLS-1$
		} catch (IllegalArgumentException e) {
			assertEquals("empty row to paste.", e.getMessage()); //$NON-NLS-1$
		}

		clonedData = (IDesignElement) clonedData.clone();

		// Test canInsert method.

		assertTrue(gridHandle2.canInsertRow(parameters1));

		// different column count.

		assertFalse(gridHandle2.canInsertRow(parameters2));

		// slotid is out of range.

		assertFalse(gridHandle2.canInsertRow(parameters3));

		try {
			gridHandle2.insertRow(parameters2);
			fail("fail to insert table row"); //$NON-NLS-1$
		} catch (SemanticException e) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_ROW_INSERT_FORBIDDEN, e.getErrorCode());
		}

		gridHandle2.insertRow(parameters1);

		gridHandle2.insertRow(parameters1);
		save();
		assertTrue(compareFile("GridRowCopy_golden_2.xml")); //$NON-NLS-1$

		// Test canShift method.

		parameters2.setSourceIndex(0);
		assertTrue(gridHandle.canShiftRow(parameters2));
		parameters1.setSourceIndex(1);
		assertTrue(gridHandle.canShiftRow(parameters1));

		// shift the same table row.

		parameters1.setSourceIndex(0);
		assertFalse(gridHandle.canShiftRow(parameters1));

		try {
			gridHandle.shiftRow(parameters1);
			fail("fail to shift table row"); //$NON-NLS-1$
		} catch (SemanticException e) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_ROW_SHIFT_FORBIDDEN, e.getErrorCode());
		}

		parameters1.setSourceIndex(1);
		gridHandle.shiftRow(parameters1);
		save();
		assertTrue(compareFile("GridRowCopy_golden_3.xml")); //$NON-NLS-1$

		// Test canInsertAndPaste method.

		clonedData = (IDesignElement) clonedData.clone();
		assertTrue(gridHandle.canInsertAndPasteRow(clonedData, parameters2));

		// slotid out of range.

		assertFalse(gridHandle.canInsertAndPasteRow(clonedData, parameters3));

		try {
			gridHandle.insertAndPasteRow(clonedData, parameters3);
			fail("fail to insert and paste table row"); //$NON-NLS-1$
		} catch (SemanticException e) {
			assertEquals(SemanticError.DESIGN_EXCEPTION_ROW_INSERTANDPASTE_FORBIDDEN, e.getErrorCode());
		}

		gridHandle.insertAndPasteRow(clonedData, parameters2);
		save();
		assertTrue(compareFile("GridRowCopy_golden_4.xml")); //$NON-NLS-1$

	}

	/**
	 * Tests getCell().
	 *
	 */
	public void testGetCell() {
		assertEquals(0, design.getErrorList().size());

		GridHandle gridHandle = (GridHandle) designHandle.findElement("My grid"); //$NON-NLS-1$

		assertNotNull(gridHandle);

		// test getCell( int, int ), the gird is 2r*4c

		gridHandle = (GridHandle) designHandle.findElement("My grid"); //$NON-NLS-1$
		assertEquals(2, gridHandle.getRows().getCount());
		assertEquals(4, gridHandle.getColumnCount());
		SlotHandle rows = gridHandle.getRows();
		RowHandle row1 = (RowHandle) rows.get(0);
		RowHandle row2 = (RowHandle) rows.get(1);

		CellHandle cell_11 = (CellHandle) row1.getCells().get(0);
		assertEquals(1, cell_11.getColumnSpan());
		assertEquals(1, cell_11.getRowSpan());
		CellHandle cell_12 = (CellHandle) row1.getCells().get(1);
		assertEquals(3, cell_12.getColumnSpan());
		assertEquals(1, cell_12.getRowSpan());
		CellHandle cell_21 = (CellHandle) row2.getCells().get(0);
		assertEquals(4, cell_21.getColumnSpan());
		assertEquals(1, cell_21.getRowSpan());

		assertNull(gridHandle.getCell(1, 5));
		assertNull(gridHandle.getCell(3, 1));
		assertNull(gridHandle.getCell(3, 5));
		assertEquals(cell_11, gridHandle.getCell(1, 1));
		assertEquals(cell_12, gridHandle.getCell(1, 2));
		assertEquals(cell_12, gridHandle.getCell(1, 3));
		assertEquals(cell_12, gridHandle.getCell(1, 4));
		assertEquals(cell_21, gridHandle.getCell(2, 1));
		assertEquals(cell_21, gridHandle.getCell(2, 2));
		assertEquals(cell_21, gridHandle.getCell(2, 3));
		assertEquals(cell_21, gridHandle.getCell(2, 4));

		//
		gridHandle = (GridHandle) designHandle.findElement("My grid 1"); //$NON-NLS-1$
		assertEquals(2, gridHandle.getRows().getCount());
		assertEquals(5, gridHandle.getColumnCount());
		rows = gridHandle.getRows();
		row1 = (RowHandle) rows.get(0);
		row2 = (RowHandle) rows.get(1);

		cell_11 = (CellHandle) row1.getCells().get(0);
		assertEquals(1, cell_11.getColumnSpan());
		assertEquals(2, cell_11.getRowSpan());
		cell_12 = (CellHandle) row1.getCells().get(1);
		assertEquals(4, cell_12.getColumnSpan());
		assertEquals(1, cell_12.getRowSpan());
		cell_21 = (CellHandle) row2.getCells().get(0);
		assertEquals(2, cell_21.getColumnSpan());
		assertEquals(1, cell_21.getRowSpan());

		assertNull(gridHandle.getCell(1, 6));
		assertNull(gridHandle.getCell(3, 1));
		assertNull(gridHandle.getCell(3, 6));
		assertEquals(cell_11, gridHandle.getCell(1, 1));
		assertEquals(cell_12, gridHandle.getCell(1, 2));
		assertEquals(cell_12, gridHandle.getCell(1, 3));
		assertEquals(cell_12, gridHandle.getCell(1, 4));
		assertEquals(cell_12, gridHandle.getCell(1, 5));
		assertEquals(cell_11, gridHandle.getCell(2, 1));
		assertEquals(cell_21, gridHandle.getCell(2, 2));
		assertEquals(cell_21, gridHandle.getCell(2, 3));
		assertEquals(null, gridHandle.getCell(2, 4));
		assertEquals(null, gridHandle.getCell(2, 5));
	}
}
