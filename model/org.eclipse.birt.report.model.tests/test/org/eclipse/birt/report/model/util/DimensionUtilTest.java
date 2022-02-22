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

package org.eclipse.birt.report.model.util;

import javax.print.attribute.ResolutionSyntax;

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.DimensionUtil;

import junit.framework.TestCase;

/**
 * Test the method in <code>DimensionUtil</code>.
 */

public class DimensionUtilTest extends TestCase {

	/**
	 * Tests whether unit is absolute.
	 */

	public void testIsAbsoluteUnit() {
		assertTrue(DimensionUtil.isAbsoluteUnit(DesignChoiceConstants.UNITS_CM));
		assertFalse(DimensionUtil.isAbsoluteUnit(DesignChoiceConstants.UNITS_EM));
		assertFalse(DimensionUtil.isAbsoluteUnit(DesignChoiceConstants.UNITS_EX));
		assertTrue(DimensionUtil.isAbsoluteUnit(DesignChoiceConstants.UNITS_IN));
		assertTrue(DimensionUtil.isAbsoluteUnit(DesignChoiceConstants.UNITS_MM));
		assertTrue(DimensionUtil.isAbsoluteUnit(DesignChoiceConstants.UNITS_PC));
		assertFalse(DimensionUtil.isAbsoluteUnit(DesignChoiceConstants.UNITS_PERCENTAGE));
		assertTrue(DimensionUtil.isAbsoluteUnit(DesignChoiceConstants.UNITS_PT));
		assertFalse(DimensionUtil.isAbsoluteUnit(DesignChoiceConstants.UNITS_PX));
	}

	/**
	 * Tests the unit conversion method.
	 */

	public void testConvertToForAbsoluteUnits() {
		// * => in
		DimensionValue value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_CM,
				DesignChoiceConstants.UNITS_IN);
		assertEquals(39.37, value.getMeasure(), 0.01);

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_IN, DesignChoiceConstants.UNITS_IN);
		assertEquals(100, value.getMeasure(), 0.01);

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_MM, DesignChoiceConstants.UNITS_IN);
		assertEquals(3.94, value.getMeasure(), 0.01);

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_PC, DesignChoiceConstants.UNITS_IN);
		assertEquals(16.67, value.getMeasure(), 0.01);

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_PT, DesignChoiceConstants.UNITS_IN);
		assertEquals(1.39, value.getMeasure(), 0.01);

		// * => cm

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_CM, DesignChoiceConstants.UNITS_CM);
		assertEquals(100, value.getMeasure(), 0.01);

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_MM, DesignChoiceConstants.UNITS_CM);
		assertEquals(10, value.getMeasure(), 0.01);

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_IN, DesignChoiceConstants.UNITS_CM);
		assertEquals(254, value.getMeasure(), 0.01);

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_PT, DesignChoiceConstants.UNITS_CM);
		assertEquals(3.53, value.getMeasure(), 0.01);

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_PC, DesignChoiceConstants.UNITS_CM);
		assertEquals(42.33, value.getMeasure(), 0.01);

		// * => mm

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_CM, DesignChoiceConstants.UNITS_MM);
		assertEquals(1000, value.getMeasure(), 0.01);

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_MM, DesignChoiceConstants.UNITS_MM);
		assertEquals(100, value.getMeasure(), 0.01);

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_IN, DesignChoiceConstants.UNITS_MM);
		assertEquals(2540, value.getMeasure(), 0.01);

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_PT, DesignChoiceConstants.UNITS_MM);
		assertEquals(35.28, value.getMeasure(), 0.01);

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_PC, DesignChoiceConstants.UNITS_MM);
		assertEquals(423.33, value.getMeasure(), 0.01);

		// * => pt

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_CM, DesignChoiceConstants.UNITS_PT);
		assertEquals(2834.65, value.getMeasure(), 0.01);

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_MM, DesignChoiceConstants.UNITS_PT);
		assertEquals(283.46, value.getMeasure(), 0.01);

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_IN, DesignChoiceConstants.UNITS_PT);
		assertEquals(7200, value.getMeasure(), 0.01);

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_PT, DesignChoiceConstants.UNITS_PT);
		assertEquals(100, value.getMeasure(), 0.01);

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_PC, DesignChoiceConstants.UNITS_PT);
		assertEquals(1200, value.getMeasure(), 0.01);

		// * => pc

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_CM, DesignChoiceConstants.UNITS_PC);
		assertEquals(236.22, value.getMeasure(), 0.01);

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_MM, DesignChoiceConstants.UNITS_PC);
		assertEquals(23.62, value.getMeasure(), 0.01);

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_IN, DesignChoiceConstants.UNITS_PC);
		assertEquals(600, value.getMeasure(), 0.01);

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_PT, DesignChoiceConstants.UNITS_PC);
		assertEquals(8.33, value.getMeasure(), 0.01);

		value = DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_PC, DesignChoiceConstants.UNITS_PC);
		assertEquals(100, value.getMeasure(), 0.01);

	}

	/**
	 * Tests the convertTo(double, string, string, double, string) method for
	 * relative units.
	 */

	public void testConvertToForRelativeUnits() {
		int dpi = 72;
		assertEquals(0.0,
				DimensionUtil.convertTo(null, DesignChoiceConstants.UNITS_CM, DesignChoiceConstants.UNITS_MM, 10, dpi),
				0.01);

		// test relative units, such as 'em', 'ex', '%'.

		// 10em to cm(base size is 10) -- 100pt to cm
		Object value = "10em"; //$NON-NLS-1$
		assertEquals(
				DimensionUtil.convertTo(value, DesignChoiceConstants.UNITS_IN, DesignChoiceConstants.UNITS_CM, 10,
						DesignChoiceConstants.UNITS_PT, dpi),
				DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_PT, DesignChoiceConstants.UNITS_CM)
						.getMeasure(),
				0.01);

		// 30ex to cm -- 30*10/3(100)pt to cm
		value = "30ex"; //$NON-NLS-1$
		assertEquals(
				DimensionUtil.convertTo(value, DesignChoiceConstants.UNITS_IN, DesignChoiceConstants.UNITS_CM, 10,
						DesignChoiceConstants.UNITS_PT, dpi),
				DimensionUtil.convertTo(100, DesignChoiceConstants.UNITS_PT, DesignChoiceConstants.UNITS_CM)
						.getMeasure(),
				0.01);

		// 50% to cm -- 5pt to cm
		value = "50%"; //$NON-NLS-1$
		assertEquals(
				DimensionUtil.convertTo(value, DesignChoiceConstants.UNITS_IN, DesignChoiceConstants.UNITS_CM, 10,
						DesignChoiceConstants.UNITS_PT, dpi),
				DimensionUtil.convertTo(5, DesignChoiceConstants.UNITS_PT, DesignChoiceConstants.UNITS_CM).getMeasure(),
				0.01);

		// 144px to cm -- 144/dpi(2)in to cm
		value = "144px"; //$NON-NLS-1$
		assertEquals(
				DimensionUtil.convertTo(value, DesignChoiceConstants.UNITS_IN, DesignChoiceConstants.UNITS_CM, 10,
						DesignChoiceConstants.UNITS_PT, dpi),
				DimensionUtil.convertTo(2, DesignChoiceConstants.UNITS_IN, DesignChoiceConstants.UNITS_CM).getMeasure(),
				0.01);
	}

	/**
	 * Tests for dimension merging.
	 */
	public void testMerge() throws Exception {
		DimensionValue in = DimensionValue.parse("1in");
		DimensionValue cm = DimensionValue.parse("1cm");
		DimensionValue mm = DimensionValue.parse("10mm");
		DimensionValue percentage = DimensionValue.parse("25%");
		DimensionValue px = DimensionValue.parse("10px");
		DimensionValue ex = DimensionValue.parse("10ex");

		// Merge between same absolute unit.
		assertEquals("2in", DimensionUtil.mergeDimension(in, in).toString());
		assertEquals("2cm", DimensionUtil.mergeDimension(cm, cm).toString());
		assertEquals("20mm", DimensionUtil.mergeDimension(mm, mm).toString());
		// Merge between different absolute units.
		assertEquals("2cm", DimensionUtil.mergeDimension(cm, mm).toString());
		assertEquals("20mm", DimensionUtil.mergeDimension(mm, cm).toString());
		// Merge between same relative unit.
		assertEquals("50%", DimensionUtil.mergeDimension(percentage, percentage).toString());
		assertEquals("20px", DimensionUtil.mergeDimension(px, px).toString());
		assertEquals("20ex", DimensionUtil.mergeDimension(ex, ex).toString());
		// Merge between absolute unit and pixel
		assertEquals("2in", DimensionUtil.mergeDimension(in, px, 10).toString());
		if (ResolutionSyntax.DPI > 0) {
			// Try default dpi value
			assertEquals(DimensionUtil.mergeDimension(px, in, ResolutionSyntax.DPI).toString(),
					DimensionUtil.mergeDimension(px, in).toString());
		}
		// Merge between different relative unit.
		assertNull(DimensionUtil.mergeDimension(percentage, ex));
		assertNull(DimensionUtil.mergeDimension(percentage, px));
		assertNull(DimensionUtil.mergeDimension(px, ex));
		// Merge between absolute unit and relative unit.
		assertNull(DimensionUtil.mergeDimension(cm, percentage));
		assertNull(DimensionUtil.mergeDimension(cm, ex));
		assertNull(DimensionUtil.mergeDimension(mm, percentage));
		assertNull(DimensionUtil.mergeDimension(mm, ex));
	}
}
