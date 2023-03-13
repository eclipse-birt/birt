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

package org.eclipse.birt.report.designer.core.commands;

import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.elements.TableRow;

/**
 * @author xzhang
 *
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class PasteCommandTest extends CmdBaseTestCase {

	/*
	 * @see TestCase#setUp()
	 */

	private CellHandle newCell = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		createFirstRow();
		createSecondRow();
		createFirstCell();
		createSecondCell();

		/***********************************************************************
		 * Note the table will be like: the secondRow is actually the first row in the
		 * table the secondCell is actually the first cell in the row
		 * |-----------------------------------| |secondRow(no cell) |
		 * |-----------------------------------| |firstRow(secondCell | firstCell)--|
		 * |-----------------------------------|
		 *
		 **********************************************************************/
	}

	private void createNewCellInSecondRow() {
		// create a new cell in secondRow
		newCell = getElementFactory().newCell();
		try {
			secondRow.getSlot(TableRow.CONTENT_SLOT).add(newCell);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("Add Cell to secondRow");
		}

	}

	public void testPasteCell() {
		assertEquals(2, firstRow.getCells().getCount());
		assertEquals(0, secondRow.getCells().getCount());
		PasteCommand pasteCmd = new PasteCommand(firstCell, secondRow, null, false);
		assertTrue(pasteCmd.canExecute());
		pasteCmd.execute();
		assertEquals(1, secondRow.getCells().getCount());
		assertEquals(firstCell, firstRow.getCells().get(1));
	}

	public void testPasteCutCell() {
		assertEquals(2, firstRow.getCells().getCount());
		assertEquals(0, secondRow.getCells().getCount());
		PasteCommand pasteCmd = new PasteCommand(firstCell, secondRow, null, true);
		assertTrue(pasteCmd.canExecute());
		pasteCmd.execute();
		assertEquals(1, secondRow.getCells().getCount());
		assertEquals(1, firstRow.getCells().getCount());
		assertEquals(secondCell, firstRow.getCells().get(0));
	}

	public void testPasteAfterCell() {
		assertEquals(2, firstRow.getCells().getCount());
		PasteCommand pasteCmd = new PasteCommand(secondCell, firstRow, firstCell, false);
		assertTrue(pasteCmd.canExecute());
		pasteCmd.execute();
		assertEquals(3, firstRow.getCells().getCount());
		assertEquals(secondCell, firstRow.getCells().get(0));
		assertEquals(firstCell, firstRow.getCells().get(2));
	}

	public void testPasteCutAfterCellInOwnRow() {
		CellHandle cell3 = getElementFactory().newCell();
		try {
			firstRow.getSlot(TableRow.CONTENT_SLOT).add(cell3);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("add the third cell");
		}
		CellHandle cell1 = secondCell;
		CellHandle cell2 = firstCell;

		assertEquals(3, firstRow.getCells().getCount());
		assertEquals(cell1, firstRow.getCells().get(0));
		assertEquals(cell2, firstRow.getCells().get(1));
		assertEquals(cell3, firstRow.getCells().get(2));
		/***********************************************************************
		 * firstRow -----cell1 -----cell2 -----cell3
		 *
		 **********************************************************************/
		// pastecut cell1 and insert before cell3
		PasteCommand pasteCmd = new PasteCommand(cell1, firstRow, cell3, true);
		assertTrue(pasteCmd.canExecute());
		pasteCmd.execute();

		// is it right??
		/***********************************************************************
		 * firstRow -----cell2 -----cell3 -----copy of cell1
		 *
		 **********************************************************************/
		assertEquals(3, firstRow.getCells().getCount());
		assertEquals(cell2, firstRow.getCells().get(0));
		assertEquals(cell3, firstRow.getCells().get(1));

	}

	public void testPasteCutAfterCellInOtherRow() {

		createNewCellInSecondRow();
		assertEquals(2, firstRow.getCells().getCount());
		assertEquals(secondCell, firstRow.getCells().get(0));
		assertEquals(1, secondRow.getCells().getCount());
		PasteCommand pasteCmd = new PasteCommand(secondCell, secondRow, newCell, true);
		assertTrue(pasteCmd.canExecute());
		pasteCmd.execute();
		assertEquals(1, firstRow.getCells().getCount());
		assertEquals(2, secondRow.getCells().getCount());
		assertEquals(newCell, secondRow.getCells().get(1));
	}

	public void testPasteCellinPosition() {

		createNewCellInSecondRow();
		// add a new cell in slot0
		assertEquals(2, firstRow.getCells().getCount());
		assertEquals(secondCell, firstRow.getCells().get(0));
		assertEquals(1, secondRow.getCells().getCount());
		assertEquals(newCell, secondRow.getCells().get(0));
		PasteCommand pasteCmd = new PasteCommand(secondCell, secondRow, 0, false);
		assertTrue(pasteCmd.canExecute());
		pasteCmd.execute();
		assertEquals(2, firstRow.getCells().getCount());
		assertEquals(secondCell, firstRow.getCells().get(0));
		assertEquals(2, secondRow.getCells().getCount());
		if (newCell == secondRow.getCells().get(0)) {
			assertTrue(false);
		}
		assertEquals(newCell, secondRow.getCells().get(1));
	}

	public void testPasteCutCellinPosition() {

		createNewCellInSecondRow();
		// add a new cell in position 0
		assertEquals(2, firstRow.getCells().getCount());
		assertEquals(secondCell, firstRow.getCells().get(0));
		assertEquals(1, secondRow.getCells().getCount());
		assertEquals(newCell, secondRow.getCells().get(0));

		PasteCommand pasteCmd = new PasteCommand(secondCell, secondRow, 1, true);
		assertTrue(pasteCmd.canExecute());
		pasteCmd.execute();

		assertEquals(1, firstRow.getCells().getCount());
		assertEquals(firstCell, firstRow.getCells().get(0));
		assertEquals(2, secondRow.getCells().getCount());
		assertEquals(newCell, secondRow.getCells().get(0));
	}

	public void testCloneCellInNullRow() {
		IDesignElement cloned = firstCell.copy();

		assertEquals(2, firstRow.getCells().getCount());
		assertEquals(0, secondRow.getCells().getCount());

		PasteCommand pasteCmd = new PasteCommand(cloned, secondRow, null);
		assertTrue(pasteCmd.canExecute());
		pasteCmd.execute();

		assertEquals(2, firstRow.getCells().getCount());
		assertEquals(1, secondRow.getCells().getCount());
	}

	public void testCloneCellInOwnRow() {
		// the cloned cell is copied to the end of the row if do not specify
		// after
		IDesignElement cloned = secondCell.copy();

		assertEquals(2, firstRow.getCells().getCount());
		assertEquals(secondCell, firstRow.getCells().get(0));
		assertEquals(firstCell, firstRow.getCells().get(1));

		PasteCommand pasteCmd = new PasteCommand(cloned, firstRow, null);
		assertTrue(pasteCmd.canExecute());
		pasteCmd.execute();

		assertEquals(3, firstRow.getCells().getCount());
		assertEquals(secondCell, firstRow.getCells().get(0));
		assertEquals(firstCell, firstRow.getCells().get(1));
	}

	public void testCloneCellAfterInOwnRow() {

		IDesignElement cloned = secondCell.copy();

		assertEquals(2, firstRow.getCells().getCount());
		assertEquals(secondCell, firstRow.getCells().get(0));
		assertEquals(firstCell, firstRow.getCells().get(1));

		PasteCommand pasteCmd = new PasteCommand(cloned, firstRow, firstCell);
		assertTrue(pasteCmd.canExecute());
		pasteCmd.execute();

		assertEquals(3, firstRow.getCells().getCount());
		assertEquals(secondCell, firstRow.getCells().get(0));
		assertEquals(firstCell, firstRow.getCells().get(2));

	}

	public void testCloneCellAfterInOtherRow() {
		createNewCellInSecondRow();

		IDesignElement cloned = firstCell.copy();

		assertEquals(2, firstRow.getCells().getCount());
		assertEquals(1, secondRow.getCells().getCount());
		assertEquals(newCell, secondRow.getCells().get(0));

		PasteCommand pasteCmd = new PasteCommand(cloned, secondRow, newCell);
		assertTrue(pasteCmd.canExecute());
		pasteCmd.execute();

		assertEquals(2, firstRow.getCells().getCount());
		assertEquals(2, secondRow.getCells().getCount());
		assertEquals(newCell, secondRow.getCells().get(1));

	}

	public void testCloneCellInHeadPosition() {
		IDesignElement cloned = secondCell.copy();

		assertEquals(2, firstRow.getCells().getCount());
		assertEquals(secondCell, firstRow.getCells().get(0));
		assertEquals(firstCell, firstRow.getCells().get(1));

		PasteCommand pasteCmd = new PasteCommand(cloned, firstRow, 0);
		assertTrue(pasteCmd.canExecute());
		pasteCmd.execute();

		assertEquals(3, firstRow.getCells().getCount());
		assertEquals(secondCell, firstRow.getCells().get(1));
		assertEquals(firstCell, firstRow.getCells().get(2));
	}

	public void testCloneCellInMiddlePosition() {
		IDesignElement cloned = secondCell.copy();

		assertEquals(2, firstRow.getCells().getCount());
		assertEquals(secondCell, firstRow.getCells().get(0));
		assertEquals(firstCell, firstRow.getCells().get(1));

		PasteCommand pasteCmd = new PasteCommand(cloned, firstRow, 1);
		assertTrue(pasteCmd.canExecute());
		pasteCmd.execute();

		assertEquals(3, firstRow.getCells().getCount());
		assertEquals(secondCell, firstRow.getCells().get(0));
		assertEquals(firstCell, firstRow.getCells().get(2));
	}

	public void testCloneCellInTailPosition() {
		IDesignElement cloned = secondCell.copy();

		assertEquals(2, firstRow.getCells().getCount());
		assertEquals(secondCell, firstRow.getCells().get(0));
		assertEquals(firstCell, firstRow.getCells().get(1));

		PasteCommand pasteCmd = new PasteCommand(cloned, firstRow, 2);
		assertTrue(pasteCmd.canExecute());
		pasteCmd.execute();

		assertEquals(3, firstRow.getCells().getCount());
		assertEquals(secondCell, firstRow.getCells().get(0));
		assertEquals(firstCell, firstRow.getCells().get(1));
	}

	public void testCloneCellInOtherPosition() {
		IDesignElement cloned = secondCell.copy();

		assertEquals(2, firstRow.getCells().getCount());
		assertEquals(secondCell, firstRow.getCells().get(0));
		assertEquals(firstCell, firstRow.getCells().get(1));

		PasteCommand pasteCmd = new PasteCommand(cloned, firstRow, 3);
		assertTrue(pasteCmd.canExecute());
		pasteCmd.execute();

		assertEquals(3, firstRow.getCells().getCount());
		assertEquals(secondCell, firstRow.getCells().get(0));
		assertEquals(firstCell, firstRow.getCells().get(1));
		// Is it the right action?
		// the inserted position turns out to be 2 automatically.
		assertNotNull(firstRow.getCells().get(2));

	}

}
