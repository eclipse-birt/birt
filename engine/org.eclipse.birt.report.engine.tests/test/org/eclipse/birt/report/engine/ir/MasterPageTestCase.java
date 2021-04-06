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
 * Master Page test
 * 
 */
abstract public class MasterPageTestCase extends StyledElementTestCase {

	MasterPageDesign masterPage;

	public MasterPageTestCase(MasterPageDesign baseMasterPage) {
		super(baseMasterPage);
		this.masterPage = baseMasterPage;
	}

	/**
	 * Test all get/set accessors
	 * 
	 * set values of the master page
	 * 
	 * then get the values one by one to test if they work correctly
	 */
	public void testMasterPageAccessor() {
		DimensionType top = new DimensionType(1, DimensionType.UNITS_CM);
		DimensionType left = new DimensionType(1, DimensionType.UNITS_CM);
		DimensionType right = new DimensionType(1, DimensionType.UNITS_CM);
		DimensionType bottom = new DimensionType(1, DimensionType.UNITS_CM);
		DimensionType width = new DimensionType(1, DimensionType.UNITS_CM);
		DimensionType height = new DimensionType(1, DimensionType.UNITS_CM);

		masterPage.setMargin(top, left, bottom, right);
		masterPage.setOrientation("auto");
		masterPage.setPageSize(width, height);
		masterPage.setPageType("A4");

		// Get
		assertEquals(masterPage.getLeftMargin(), left);
		assertEquals(masterPage.getRightMargin(), right);
		assertEquals(masterPage.getTopMargin(), top);
		assertEquals(masterPage.getBottomMargin(), bottom);
		assertEquals(masterPage.getOrientation(), "auto");
		assertEquals(masterPage.getPageHeight(), height);
		assertEquals(masterPage.getPageWidth(), width);
		assertEquals(masterPage.getPageType(), "A4");
	}
}
