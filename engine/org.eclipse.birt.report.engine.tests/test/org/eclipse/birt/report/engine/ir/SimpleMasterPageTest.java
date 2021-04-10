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
public class SimpleMasterPageTest extends MasterPageTestCase {
	public SimpleMasterPageTest() {
		super(new SimpleMasterPageDesign());
	}

	/**
	 * Test add/getContent methods
	 * 
	 * add a random list of report item into the master page
	 * 
	 * then get the contents one by one to test if they work correctly
	 */

	public void testAddFooter() {
		SimpleMasterPageDesign masterPage = new SimpleMasterPageDesign();
		ReportItemSet set = new ReportItemSet();

		// Add
		for (int i = 0; i < set.length; i++) {
			masterPage.addFooter(set.getItem(i));
		}

		// Get
		assertEquals(masterPage.getFooterCount(), set.length);
		for (int i = 0; i < set.length; i++) {
			assertEquals(masterPage.getFooter(i), set.getItem(i));
		}
		assertEquals(masterPage.getFooters(), set.getItems());
	}

	public void testAddHeader() {
		SimpleMasterPageDesign masterPage = new SimpleMasterPageDesign();
		ReportItemSet set = new ReportItemSet();

		// Add
		for (int i = 0; i < set.length; i++) {
			masterPage.addHeader(set.getItem(i));
		}

		// Get
		assertEquals(masterPage.getHeaderCount(), set.length);
		for (int i = 0; i < set.length; i++) {
			assertEquals(masterPage.getHeader(i), set.getItem(i));
		}
		assertEquals(masterPage.getHeaders(), set.getItems());
	}

}
