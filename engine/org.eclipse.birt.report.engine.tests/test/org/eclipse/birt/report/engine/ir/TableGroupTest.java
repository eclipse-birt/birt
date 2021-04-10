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
 * List group test
 * 
 */
public class TableGroupTest extends GroupTestCase {

	public TableGroupTest() {
		super(new TableGroupDesign());
	}

	/**
	 * Test all get/set accessors
	 * 
	 * set values of the table group
	 * 
	 * then get the values one by one to test if they work correctly
	 */
	public void testAccessor() {
		TableGroupDesign tableGroup = (TableGroupDesign) group;
		TableBandDesign header = new TableBandDesign();
		TableBandDesign footer = new TableBandDesign();

		// Set
		tableGroup.setHeader(header);
		tableGroup.setFooter(footer);

		// Get
		assertEquals(tableGroup.getHeader(), header);
		assertEquals(tableGroup.getFooter(), footer);
	}

}
