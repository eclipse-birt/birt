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
