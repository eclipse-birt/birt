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
