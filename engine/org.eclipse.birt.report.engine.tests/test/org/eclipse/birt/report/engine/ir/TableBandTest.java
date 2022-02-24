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

package org.eclipse.birt.report.engine.ir;

import java.util.Random;

import junit.framework.TestCase;

/**
 * Band used in a TableItem.
 * 
 */
public class TableBandTest extends TestCase {

	/**
	 * Test add/getRow methods
	 * 
	 * add a random list of rows into the tree bandl
	 * 
	 * then get the rows one by one to test if they work correctly
	 */
	public void testAddRow() {
		TableBandDesign tableBand = new TableBandDesign();

		RowDesign[] rows = new RowDesign[(new Random()).nextInt(10) + 1];

		// Add
		for (int i = 0; i < rows.length; i++) {
			rows[i] = new RowDesign();
			tableBand.addRow(rows[i]);
		}

		// Get
		assertEquals(tableBand.getRowCount(), rows.length);
		for (int i = 0; i < rows.length; i++) {
			assertEquals(tableBand.getRow(i), rows[i]);
		}

	}
}
