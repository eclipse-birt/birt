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

/**
 * List Item test
 *
 */
public class ListItemTest extends ReportItemTestCase {

	public ListItemTest() {
		super(new ListItemDesign());
	}

	/**
	 * Test all get/set accessorss
	 *
	 * set values of the list item
	 *
	 * then get the values one by one to test if they work correctly
	 */

	public void testAccessor() {
		ListItemDesign list = new ListItemDesign();
		ListBandDesign detail = new ListBandDesign();
		ListBandDesign header = new ListBandDesign();
		ListBandDesign footer = new ListBandDesign();

		// Set
		list.setDetail(detail);
		list.setHeader(header);
		list.setFooter(footer);

		// Get
		assertEquals(list.getDetail(), detail);
		assertEquals(list.getHeader(), header);
		assertEquals(list.getFooter(), footer);

	}

	/**
	 * Test add/getGroup methods
	 *
	 * add a random list of list groups into the list item
	 *
	 * then get the groups one by one to test if they work correctly
	 */
	public void testAddGroup() {
		ListItemDesign list = new ListItemDesign();
		ListGroupDesign[] groups = new ListGroupDesign[(new Random()).nextInt(5) + 1];

		// Add
		for (int i = 0; i < groups.length; i++) {
			groups[i] = new ListGroupDesign();
			list.addGroup(groups[i]);
		}

		// Get
		assertEquals(list.getGroupCount(), groups.length);
		for (int i = 0; i < groups.length; i++) {
			assertEquals(list.getGroup(i), groups[i]);
		}
	}

}
