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

import junit.framework.TestCase;

/**
 * Page Setup test
 *
 */
public class PageSetupTest extends TestCase {

	/**
	 * Test add/getPageSequenceDesign methods
	 *
	 * set the page sequence
	 *
	 * then get it to test if they work correctly
	 */
	public void testPageSequence() {
		PageSetupDesign pageSetup = new PageSetupDesign();

		PageSequenceDesign pageSequence1 = new PageSequenceDesign();
		pageSequence1.setName("seq1");
		PageSequenceDesign pageSequence2 = new PageSequenceDesign();
		pageSequence2.setName("seq2");

		// Add
		pageSetup.addPageSequence(pageSequence1);
		pageSetup.addPageSequence(pageSequence2);

		// Get
		assertEquals(pageSetup.getPageSequenceCount(), 2);
		assertEquals(pageSetup.getPageSequence(0), pageSequence1);
		assertEquals(pageSetup.getPageSequence(1), pageSequence2);
		assertEquals(pageSetup.findPageSequence("seq1"), pageSequence1);
		assertEquals(pageSetup.findPageSequence("seq2"), pageSequence2);
	}

	/**
	 * Test add/getMasterPage methods
	 *
	 * add a random list of master pages item into the page setup
	 *
	 * then get the master pages one by one to test if they work correctly
	 */
	public void testAddMasterPage() {
		PageSetupDesign pageSetup = new PageSetupDesign();
		MasterPageDesign[] masterPages = new MasterPageDesign[(new Random()).nextInt(5) + 1];

		// Add
		for (int i = 0; i < masterPages.length; i++) {
			// We do not support GraphicMasterPageDesign now. So use simple master page
			// masterPages[i] = new GraphicMasterPageDesign( );
			masterPages[i] = new SimpleMasterPageDesign();
			masterPages[i].setName("page_" + i);
			pageSetup.addMasterPage(masterPages[i]);
		}

		// Get
		assertEquals(pageSetup.getMasterPageCount(), masterPages.length);
		for (int i = 0; i < masterPages.length; i++) {
			assertEquals(pageSetup.getMasterPage(i), masterPages[i]);
			assertEquals(pageSetup.findMasterPage("page_" + i), masterPages[i]);
		}
	}
}
