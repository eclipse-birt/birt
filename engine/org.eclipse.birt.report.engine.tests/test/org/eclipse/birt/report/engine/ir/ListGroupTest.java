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
 * List group test
 *
 */
public class ListGroupTest extends GroupTestCase {

	public ListGroupTest() {
		super(new ListGroupDesign());
	}

	/**
	 * Test all get/set accessorss
	 *
	 * set values of the list group
	 *
	 * then get the values one by one to test if they work correctly
	 */

	public void testAccessor() {
		ListGroupDesign listGroup = new ListGroupDesign();
		ListBandDesign header = new ListBandDesign();
		ListBandDesign footer = new ListBandDesign();

		// Set
		listGroup.setHeader(header);
		listGroup.setFooter(footer);

		// Get
		assertEquals(listGroup.getHeader(), header);
		assertEquals(listGroup.getFooter(), footer);
	}

}
