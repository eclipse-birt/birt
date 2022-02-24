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
public class FlowMoveChildCmdTest extends CmdBaseTestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		createFirstRow();
		createSecondRow();
		createFirstCell();
		createSecondCell();
		/***********************************************************************
		 * Note the table will be like: the secondRow is actually the first row in the
		 * table the secondCell is actually the first cell in the row
		 * |-----------------------------------| |secondRow( no cell) |
		 * |-----------------------------------| |firstRow(secondCell | firstCell)--|
		 * |-----------------------------------|
		 * 
		 **********************************************************************/
	}

	public void testMoveRowCmd() {
		SlotHandle container = table.getDetail();
		assertEquals(secondRow, table.getDetail().get(0));
		assertEquals(firstRow, table.getDetail().get(1));
		FlowMoveChildCommand moveCmd = new FlowMoveChildCommand(firstRow, secondRow, container);
		moveCmd.execute();
		assertEquals(secondRow, container.get(1));
		assertEquals(firstRow, container.get(0));
	}

	public void testMoveCellCmd() {
		assertEquals(secondCell, firstRow.getCells().get(0));
		assertEquals(firstCell, firstRow.getCells().get(1));
		FlowMoveChildCommand moveCmd = new FlowMoveChildCommand(firstCell, secondCell, firstRow);
		moveCmd.execute();
		assertEquals(secondCell, firstRow.getCells().get(1));
		assertEquals(firstCell, firstRow.getCells().get(0));
	}
}
