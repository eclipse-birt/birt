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
 * Column test
 * 
 */
public class ColumnTest extends StyledElementTestCase {

	public ColumnTest() {
		super(new ColumnDesign());
	}

	/**
	 * Test all get/set accessorss
	 * 
	 * set values of the column
	 * 
	 * then get the values one by one to test if they work correctly
	 */

	public void testAccessor() {
		ColumnDesign column = (ColumnDesign) element;
		DimensionType width = new DimensionType(5.0, DimensionType.UNITS_CM);

		// Set
		column.setWidth(width);

		// Get
		assertEquals(column.getWidth(), width);
	}
}
