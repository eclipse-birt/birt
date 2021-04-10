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

import junit.framework.TestCase;

/**
 * List Band test
 * 
 */
public class ListBandTest extends TestCase {

	/**
	 * Test add/getContent methods
	 * 
	 * add a random list of report item into the list band
	 * 
	 * then get the contents one by one to test if they work correctly
	 */
	public void testAddContent() {
		ListBandDesign listBand = new ListBandDesign();

		ReportItemSet set = new ReportItemSet();

		// Add
		for (int i = 0; i < set.length; i++) {
			listBand.addContent(set.getItem(i));
		}

		// Get
		assertEquals(listBand.getContentCount(), set.length);
		for (int i = 0; i < set.length; i++) {
			assertEquals(listBand.getContent(i), set.getItem(i));
		}
		assertEquals(listBand.getContents(), set.getItems());
	}

}
