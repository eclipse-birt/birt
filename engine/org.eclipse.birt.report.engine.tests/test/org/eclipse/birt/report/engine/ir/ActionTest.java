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

import junit.framework.TestCase;

/**
 * Action test
 *
 */
public class ActionTest extends TestCase {

	// Test constants
	public void testConstant() {
		assertEquals(ActionDesign.ACTION_HYPERLINK, 1);
		assertEquals(ActionDesign.ACTION_BOOKMARK, 2);
		assertEquals(ActionDesign.ACTION_DRILLTHROUGH, 3);
	}

	/**
	 * Test get/setBookmark methods
	 *
	 * set an expression as a bookmark
	 *
	 * then get the bookmark and check the action type to test if they work
	 * correctly
	 */

	public void testBookmark() {
		ActionDesign action = new ActionDesign();

		Expression bookmark = Expression.newConstant("");
		// Set
		action.setBookmark(bookmark);

		// Get
		assertEquals(action.getActionType(), ActionDesign.ACTION_BOOKMARK);
		assertEquals(action.getBookmark(), bookmark);

	}

	/**
	 * Test get/setHyperlink methods
	 *
	 * set an expression as a hyperlink
	 *
	 * then get the hyperlink and check the action type to test if they work
	 * correctly
	 */

	public void testHyprrlink() {
		ActionDesign action = new ActionDesign();
		// Set
		Expression hyperlink = Expression.newConstant("");
		action.setHyperlink(hyperlink);

		// Get
		assertEquals(action.getActionType(), ActionDesign.ACTION_HYPERLINK);
		assertEquals(action.getHyperlink(), hyperlink);

	}

	public void testDrillThrough() {
		ActionDesign action = new ActionDesign();
		DrillThroughActionDesign drillThrough = new DrillThroughActionDesign();

		action.setDrillThrough(drillThrough);

		assertEquals(action.getActionType(), ActionDesign.ACTION_DRILLTHROUGH);
		assertEquals(action.getDrillThrough(), drillThrough);
	}
}
