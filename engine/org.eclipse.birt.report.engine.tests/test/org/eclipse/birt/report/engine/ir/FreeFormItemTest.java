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
 * Container Object test
 * 
 */
public class FreeFormItemTest extends ReportItemTestCase {

	public FreeFormItemTest() {
		super(new FreeFormItemDesign());
	}

	/**
	 * Test add/getItem methods
	 * 
	 * add a random list of report item into the freeform item
	 * 
	 * then get the items one by one to test if it works correctly
	 */
	public void testAddItem() {
		FreeFormItemDesign form = new FreeFormItemDesign();
		ReportItemSet set = new ReportItemSet();

		// Add
		for (int i = 0; i < set.length; i++) {
			form.addItem(set.getItem(i));
		}

		// Get
		assertEquals(form.getItemCount(), set.length);
		for (int i = 0; i < set.length; i++) {
			assertEquals(form.getItem(i), set.getItem(i));
		}
		assertEquals(form.getItems(), set.getItems());
	}

}
