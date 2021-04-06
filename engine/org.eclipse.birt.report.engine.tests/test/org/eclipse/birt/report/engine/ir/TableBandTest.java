/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
