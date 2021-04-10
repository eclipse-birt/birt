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
 * 
 */
public class GraphicMasterPageTest extends MasterPageTestCase {

	public GraphicMasterPageTest() {
		super(new GraphicMasterPageDesign());
	}

	/**
	 * Test add/getContent methods
	 * 
	 * add a random list of report item into the master page
	 * 
	 * then get the contents one by one to test if they work correctly
	 */

	public void testAddContent() {
		GraphicMasterPageDesign masterPage = new GraphicMasterPageDesign();
		ReportItemSet set = new ReportItemSet();

		// Add
		for (int i = 0; i < set.length; i++) {
			masterPage.addContent(set.getItem(i));
		}

		// Get
		assertEquals(masterPage.getContentCount(), set.length);
		for (int i = 0; i < set.length; i++) {
			assertEquals(masterPage.getContent(i), set.getItem(i));
		}
		assertEquals(masterPage.getContents(), set.getItems());
	}

}
