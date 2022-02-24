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

import org.eclipse.birt.report.model.api.SlotHandle;

/**
 * @author xzhang
 * 
 *         TODO To change the template for this generated type comment go to
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public class DeleteCommandTest extends CmdBaseTestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		// create row, cell
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

	public void testDeleteEmptyCell() {
		// can not delete an empty cell
		DeleteCommand deleteCmd = new DeleteCommand(firstCell);
		assertFalse(deleteCmd.canExecute());

	}

	public void testDeleteCell() {
		addDataItems();
		assertEquals(1, firstCell.getContent().getCount());
		DeleteCommand deleteCmd = new DeleteCommand(firstCell);
		assertTrue(deleteCmd.canExecute());
		deleteCmd.execute();
		assertEquals(0, firstCell.getContent().getCount());

	}

	public void testDeleteFirstRow() {
		// delete a table row.
		assertEquals(2, table.getDetail().getCount());
		DeleteCommand deleteCmd = new DeleteCommand(firstRow);
		assertTrue(deleteCmd.canExecute());
		deleteCmd.execute();
		assertEquals(1, table.getDetail().getCount());
		assertEquals(secondRow, table.getDetail().get(0));
	}

	public void testDeleteSecondRow() {
		// delete secondRow(the first row in the table.detail).
		assertEquals(2, table.getDetail().getCount());
		assertEquals(firstRow, table.getDetail().get(1));
		DeleteCommand deleteCmd = new DeleteCommand(secondRow);
		assertTrue(deleteCmd.canExecute());
		deleteCmd.execute();
		assertEquals(1, table.getDetail().getCount());
		assertEquals(firstRow, table.getDetail().get(0));

	}

	public void testDeleteColumn() {
		createColumn();
		assertEquals(2, firstRow.getCells().getCount());
		assertNotNull(adapter.getColumn(1));
		DeleteCommand deleteCmd = new DeleteCommand(firstColumn);
		assertTrue(deleteCmd.canExecute());
		deleteCmd.execute();
		assertNull(adapter.getColumn(1));
	}

	public void testDeleteDataSet() {
		SlotHandle parent = getReportDesignHandle().getDataSets();
		createDataSet();
		assertEquals(1, parent.getCount());
		assertEquals(dataSet, parent.get(0));

		DeleteCommand deleteCmd = new DeleteCommand(dataSet);
		assertTrue(deleteCmd.canExecute());
		deleteCmd.execute();
		assertEquals(0, parent.getCount());
	}

	public void testDeleteDataSource() {

		SlotHandle parent = getReportDesignHandle().getDataSources();
		assertEquals(0, parent.getCount());

		createDataSource();
		createDataSet();
		assertEquals(1, parent.getCount());
		assertEquals(dataSource, parent.get(0));
		assertEquals(dataSource, dataSet.getDataSource());

		DeleteCommand deleteCmd = new DeleteCommand(dataSource);
		assertTrue(deleteCmd.canExecute());
		deleteCmd.execute();
		assertEquals(0, parent.getCount());

		assertNull(dataSet.getDataSource());
	}
}
