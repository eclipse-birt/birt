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
 * base class of Report Item test case
 *
 */
abstract public class ReportItemTestCase extends StyledElementTestCase {

	public ReportItemTestCase(ReportItemDesign e) {
		super(e);
	}

	/**
	 * Test all get/set accessors in base class
	 *
	 * set values of the element
	 *
	 * then get the values one by one to test if they work correctly
	 */
	public void testBaseItem() {
		ReportItemDesign e = (ReportItemDesign) element;
		DimensionType h = createDimension();
		DimensionType w = createDimension();
		DimensionType x = createDimension();
		DimensionType y = createDimension();
		Expression bookmark = Expression.newConstant("");

		// Set
		e.setHeight(h);
		e.setWidth(w);
		e.setX(x);
		e.setY(y);
		e.setBookmark(bookmark);

		// Get
		assertEquals(e.getHeight(), h);
		assertEquals(e.getWidth(), w);
		assertEquals(e.getX(), x);
		assertEquals(e.getY(), y);
		assertEquals(e.getBookmark(), bookmark);
	}

	private DimensionType createDimension() {
		return new DimensionType(1, DimensionType.UNITS_CM);
	}

}
