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

/**
 * Cell test
 * 
 */
public class CellTest extends StyledElementTestCase {

	public CellTest() {
		super(new CellDesign());
	}

	/**
	 * Test all get/set accessors
	 * 
	 * set values of the cell
	 * 
	 * then get the values one by one to test if they work correctly
	 */
	public void testAccessor() {
		CellDesign cell = new CellDesign();

		// Set
		cell.setColSpan(1);
		cell.setColumn(2);
		cell.setRowSpan(3);
		String drop = "Drop";
		cell.setDrop(drop);

		// Get
		assertEquals(cell.getColSpan(), 1);
		assertEquals(cell.getColumn(), 2);
		assertEquals(cell.getRowSpan(), 3);
		assertEquals(cell.getDrop(), drop);

	}

	/**
	 * Test add/getContent methods
	 * 
	 * add a random list of report items into the cell
	 * 
	 * then get the contents one by one to test if they work correctly
	 */
	public void testAddContend() {

		CellDesign cell = new CellDesign();
		ReportItemSet set = new ReportItemSet();

		// Add
		for (int i = 0; i < set.length; i++) {
			cell.addContent(set.getItem(i));
		}

		// Get
		assertEquals(cell.getContentCount(), set.length);
		for (int i = 0; i < set.length; i++) {
			assertEquals(cell.getContent(i), set.getItem(i));
		}
	}
}
