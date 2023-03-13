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
