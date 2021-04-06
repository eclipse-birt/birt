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

package org.eclipse.birt.chart.tests.engine.util;

import junit.framework.TestCase;

import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.impl.LabelImpl;
import org.eclipse.birt.chart.util.ChartUtil;

import com.ibm.icu.util.Calendar;

public class ChartUtilTest extends TestCase {

	/**
	 * Construct and initialize any objects that will be used in multiple tests.
	 * Currently Empty.
	 */
	protected void setUp() throws Exception {

	}

	/**
	 * Collect and empty any objects that are used in multiple tests. Currently
	 * Empty.
	 */
	protected void tearDown() throws Exception {

	}

	/**
	 * Test whether the given color definition is transparent.
	 * 
	 */
	public void testIsColorTransparent() {
		assertFalse(ChartUtil.isColorTransparent(ColorDefinitionImpl.BLUE()));
		assertTrue(ChartUtil.isColorTransparent(ColorDefinitionImpl.TRANSPARENT()));
	}

	/**
	 * Test whether the given label defines a shadow.
	 * 
	 */
	public void testIsShadowDefined() {
		Label label = LabelImpl.create();
		assertFalse(ChartUtil.isShadowDefined(label));
		label.setShadowColor(ColorDefinitionImpl.BLACK());
		assertTrue(ChartUtil.isShadowDefined(label));
	}

	/**
	 * Test whether the given left double value is greater than the given right
	 * value within a small precision.
	 * 
	 */
	public void testMathGT() {
		assertTrue(ChartUtil.mathGT(1.0 + 1.0 * 1E-9, 1.0));
		assertFalse(ChartUtil.mathGT(1.0 + 1.0 * 1E-11, 1.0));
	}

	/**
	 * Test whether the given left double value is less than the given right value
	 * within a small precision
	 */
	public void testMathLT() {
		assertTrue(ChartUtil.mathLT(1.0, 1.0 + 1.0 * 1E-9));
		assertFalse(ChartUtil.mathLT(1.0, 1.0 + 1.0 * 1E-11));
	}

	/**
	 * Test whether the given two double values are equal within a small precision.
	 * 
	 */
	public void testMathEqual() {
		assertFalse(ChartUtil.mathEqual(1.0 + 1.0 * 1E-9, 1.0));
		assertTrue(ChartUtil.mathEqual(1.0 + 1.0 * 1E-11, 1.0));
		assertTrue(ChartUtil.mathEqual(1.0, 1.0 + 1.0 * 1E-11));
	}

	/**
	 * Test the quadrant for given angle in degree.
	 */
	public void testGetQuadrant() {
		assertEquals(-1, ChartUtil.getQuadrant(0));
		assertEquals(-2, ChartUtil.getQuadrant(90));
		assertEquals(-3, ChartUtil.getQuadrant(180));
		assertEquals(-4, ChartUtil.getQuadrant(270));
		assertEquals(1, ChartUtil.getQuadrant(45));
		assertEquals(2, ChartUtil.getQuadrant(-200));
		assertEquals(3, ChartUtil.getQuadrant(-100));
		assertEquals(4, ChartUtil.getQuadrant(-45));
	}

	/**
	 * Test {@link ChartUtil#compareVersion(String, String)}.
	 */
	public void testCompareVersion() {
		assertEquals(0, ChartUtil.compareVersion("2", "2")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(0, ChartUtil.compareVersion("2", "2.")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(0, ChartUtil.compareVersion("2", "2.0")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(1, ChartUtil.compareVersion("3", "2")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(1, ChartUtil.compareVersion("3.", "2")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(-16, ChartUtil.compareVersion("3.1", "3.17")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(-15, ChartUtil.compareVersion("3.2", "3.17")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(0, ChartUtil.compareVersion("3.1.", "3.1.0")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(2, ChartUtil.compareVersion("3.1.2", "3.1")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(-1, ChartUtil.compareVersion("3.1.2", "3.1.3")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(1, ChartUtil.compareVersion("3.1.1.5", "3.1")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(0, ChartUtil.compareVersion("3.1.1.5", "3.1.1.5.0")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(-1, ChartUtil.compareVersion("3.1.1.5", "3.1.1.6")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void testFormatDouble() {
		assertEquals("0.000000000", ChartUtil.formatDouble(0d));//$NON-NLS-1$
		assertEquals("1.000000000", ChartUtil.formatDouble(1d));//$NON-NLS-1$
		assertEquals("0.100000000", ChartUtil.formatDouble(0.1d));//$NON-NLS-1$
		assertEquals("0.000000010", ChartUtil.formatDouble(0.00000001d));//$NON-NLS-1$
		assertEquals("0.000000000", ChartUtil.formatDouble(0.00000000001d));//$NON-NLS-1$
	}

	public void testStringValue() {
		assertEquals(null, ChartUtil.stringValue(null));
		assertEquals("1", ChartUtil.stringValue(1));//$NON-NLS-1$
		assertEquals("0.00100001", ChartUtil.stringValue(0.00100001));//$NON-NLS-1$
		// TODO test case only works under pacific time zone
		Calendar calendar = Calendar.getInstance();
		calendar.set(2015, 0, 22, 11, 56, 10);
		calendar.set(Calendar.MILLISECOND, 125);
		assertEquals("2015-01-22 11:56:10.125-0800", //$NON-NLS-1$
				ChartUtil.stringValue(calendar));
	}
}
