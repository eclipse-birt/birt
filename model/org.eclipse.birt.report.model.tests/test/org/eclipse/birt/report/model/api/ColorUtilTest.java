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

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IColorConstants;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test case for ColorUtil.
 */

public class ColorUtilTest extends BaseTestCase {

	/**
	 * Same colors as that in ColorUtil.
	 */

	static final String[][] COLORS = { { IColorConstants.MAROON, "#800000" }, //$NON-NLS-1$
			{ IColorConstants.RED, "#FF0000" }, //$NON-NLS-1$
			{ IColorConstants.ORANGE, "#ffA500" }, //$NON-NLS-1$
			{ IColorConstants.YELLOW, "#FFFF00" }, //$NON-NLS-1$
			{ IColorConstants.OLIVE, "#808000" }, //$NON-NLS-1$
			{ IColorConstants.PURPLE, "#800080" }, //$NON-NLS-1$
			{ IColorConstants.FUCHSIA, "#FF00FF" }, //$NON-NLS-1$
			{ IColorConstants.WHITE, "#FFFFFF" }, //$NON-NLS-1$
			{ IColorConstants.LIME, "#00FF00" }, //$NON-NLS-1$
			{ IColorConstants.GREEN, "#008000" }, //$NON-NLS-1$
			{ IColorConstants.NAVY, "#000080" }, //$NON-NLS-1$
			{ IColorConstants.BLUE, "#0000FF" }, //$NON-NLS-1$
			{ IColorConstants.AQUA, "#00FFFF" }, //$NON-NLS-1$
			{ IColorConstants.TEAL, "#008080" }, //$NON-NLS-1$
			{ IColorConstants.BLACK, "#000000" }, //$NON-NLS-1$
			{ IColorConstants.SILVER, "#C0C0C0" }, //$NON-NLS-1$
			{ IColorConstants.GRAY, "#808080" } //$NON-NLS-1$
	};

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Test to see all meta support colors are supported by ColorUtil. And all
	 * ColorUtil colors are also defined in rom.def.
	 * 
	 */

	public void testConsistency() {

		// 1. All meta support colors are supported by ColorUtil.

		ColorPropertyType type = (ColorPropertyType) MetaDataDictionary.getInstance()
				.getPropertyType(PropertyType.COLOR_TYPE);
		IChoiceSet colorSet = type.getChoices();
		IChoice[] colors = colorSet.getChoices();
		for (int i = 0; i < colors.length; i++) {
			int rgb = ColorUtil.parsePredefinedColor(colors[i].getName());
			assertTrue(rgb != -1);
		}

		// 2. All ColorUtil colors are also defined in rom.def

		String[][] colorTable = COLORS;
		for (int i = 0; i < colorTable.length; i++) {
			String colorName = colorTable[i][0];
			assertTrue(colorSet.contains(colorName));
		}

	}

	/**
	 * test formatRGBValue().
	 * 
	 */

	public void testFormatRGB() {
		int input = Integer.decode("#FF00FF").intValue(); //$NON-NLS-1$

		assertEquals("RGB(255,0,255)", ColorUtil.format(input, //$NON-NLS-1$
				ColorUtil.CSS_ABSOLUTE_FORMAT));

		assertEquals("RGB(100.0%,0.0%,100.0%)", ColorUtil.format(input, //$NON-NLS-1$
				ColorUtil.CSS_RELATIVE_FORMAT));

		assertEquals("#FF00FF", ColorUtil.format(input, ColorUtil.HTML_FORMAT)); //$NON-NLS-1$

		assertEquals("0xFF00FF", ColorUtil.format(input, ColorUtil.JAVA_FORMAT)); //$NON-NLS-1$

		// preference not in the list.
		assertEquals("RGB(255,0,255)", ColorUtil.format(input, Integer.MAX_VALUE)); //$NON-NLS-1$

		// filling 0 in the left.
		assertEquals("#000010", ColorUtil.format(16, ColorUtil.HTML_FORMAT)); //$NON-NLS-1$

		// if rgb < 0, it will be clipped to 0.

		assertEquals("#000000", ColorUtil.format(-1, ColorUtil.HTML_FORMAT)); //$NON-NLS-1$

		// if rgb > 0xFFFFFF, it will be clipped to 0xFFFFFF

		assertEquals("#FFFFFF", ColorUtil.format(Integer.decode("#1FFFFFF").intValue(), ColorUtil.HTML_FORMAT)); //$NON-NLS-1$//$NON-NLS-2$

		// #70 => 43.92 round to 43.9%
		// #73 => 45.09 round to 45.1%

		assertEquals("RGB(43.9%,0.0%,45.1%)", //$NON-NLS-1$
				ColorUtil.format(Integer.decode("#700073").intValue(), ColorUtil.CSS_RELATIVE_FORMAT)); //$NON-NLS-1$

		assertEquals("1.5", String.valueOf(round(1.53f))); //$NON-NLS-1$
		assertEquals("1.6", String.valueOf(round(1.55f))); //$NON-NLS-1$
		assertEquals("1.6", String.valueOf(round(1.58f))); //$NON-NLS-1$
	}

	private static float round(float value) {
		return ((int) (value * 10 + 0.5f)) / 10.0f;
	}

	public void testFormat() {

		assertEquals("RGB(255,0,255)", ColorUtil.format("#FF00FF", //$NON-NLS-1$ //$NON-NLS-2$
				ColorUtil.CSS_ABSOLUTE_FORMAT));
		assertEquals("0xFF00FF", ColorUtil.format("#FF00FF", //$NON-NLS-1$ //$NON-NLS-2$
				ColorUtil.JAVA_FORMAT));
		assertEquals("#FF00FF", ColorUtil.format("#FF00FF", //$NON-NLS-1$ //$NON-NLS-2$
				ColorUtil.HTML_FORMAT));
		assertEquals("#0000FF", ColorUtil.format("#FF", //$NON-NLS-1$ //$NON-NLS-2$
				ColorUtil.HTML_FORMAT));
		assertEquals("#FF00FF", ColorUtil.format("#F0F", //$NON-NLS-1$ //$NON-NLS-2$
				ColorUtil.HTML_FORMAT));

		assertEquals("#FF00FF", ColorUtil.format("0xFF00FF", //$NON-NLS-1$ //$NON-NLS-2$
				ColorUtil.HTML_FORMAT));
		assertEquals("0xFF00FF", ColorUtil.format("0xFF00FF", //$NON-NLS-1$ //$NON-NLS-2$
				ColorUtil.JAVA_FORMAT));
		assertEquals("RGB(255,0,255)", ColorUtil.format("0xFF00FF", //$NON-NLS-1$ //$NON-NLS-2$
				ColorUtil.CSS_ABSOLUTE_FORMAT));

		assertEquals("0xFF00FF", ColorUtil.format("RGB(255,0,255)", //$NON-NLS-1$ //$NON-NLS-2$
				ColorUtil.JAVA_FORMAT));
		assertEquals("#FF00FF", ColorUtil.format("RGB(255,0,255)", //$NON-NLS-1$ //$NON-NLS-2$
				ColorUtil.HTML_FORMAT));
		assertEquals("RGB(255,0,255)", ColorUtil.format("RGB(255,0,255)", //$NON-NLS-1$ //$NON-NLS-2$
				ColorUtil.CSS_ABSOLUTE_FORMAT));

		assertEquals("RGB(255,0,0)", ColorUtil.format("red", //$NON-NLS-1$ //$NON-NLS-2$
				ColorUtil.CSS_ABSOLUTE_FORMAT));
		assertEquals("#FF0000", ColorUtil.format("red", //$NON-NLS-1$ //$NON-NLS-2$
				ColorUtil.HTML_FORMAT));

		// #70 => 43.92 round to 43.9%
		// #73 => 45.09 round to 45.1%

		assertEquals("RGB(43.9%,0.0%,45.1%)", ColorUtil.format("#700073", ColorUtil.CSS_RELATIVE_FORMAT)); //$NON-NLS-1$ //$NON-NLS-2$

		// convert back.

		assertEquals("#700073", ColorUtil.format("RGB(43.9%,0.0%,45.1%)", ColorUtil.HTML_FORMAT)); //$NON-NLS-1$//$NON-NLS-2$

	}

	public void testIsValidCssAbsolute() {
		assertTrue(ColorUtil.isCssAbsolute("RGB(255,0,0)")); //$NON-NLS-1$
		assertTrue(ColorUtil.isCssAbsolute("RGB( 255 , 0 , 0 )")); //$NON-NLS-1$
		assertTrue(ColorUtil.isCssAbsolute("RGB(300,300,300)")); //$NON-NLS-1$

		assertFalse(ColorUtil.isCssAbsolute("   RGB(255,0,0)")); //$NON-NLS-1$
		assertFalse(ColorUtil.isCssAbsolute("RGB(255,0,0)  ")); //$NON-NLS-1$
		assertFalse(ColorUtil.isCssAbsolute("RGB(,0,0)")); //$NON-NLS-1$
		assertFalse(ColorUtil.isCssAbsolute("RGB(255,,0)")); //$NON-NLS-1$
		assertFalse(ColorUtil.isCssAbsolute("RGB(0,0)")); //$NON-NLS-1$
		assertFalse(ColorUtil.isCssAbsolute("RGB(0)")); //$NON-NLS-1$
		assertFalse(ColorUtil.isCssAbsolute("RGB(-1,0,0)")); //$NON-NLS-1$
		assertFalse(ColorUtil.isCssAbsolute("RGB(0,-1,0)")); //$NON-NLS-1$
		assertFalse(ColorUtil.isCssAbsolute("RGB(0,0,-1)")); //$NON-NLS-1$
	}

	public void testIsValidCssRelative() {
		assertTrue(ColorUtil.isCssRelative("RGB(100%,0%,0%)")); //$NON-NLS-1$
		assertTrue(ColorUtil.isCssRelative("RGB( 255% , 0% , 0% )")); //$NON-NLS-1$
		assertTrue(ColorUtil.isCssRelative("RGB( 300%,300%,300%)")); //$NON-NLS-1$
		assertTrue(ColorUtil.isCssRelative("RGB(100.001%,100.001%,100.00001%)")); //$NON-NLS-1$

		assertFalse(ColorUtil.isCssRelative("        RGB(100,0%,0%)")); //$NON-NLS-1$
		assertFalse(ColorUtil.isCssRelative("RGB(100,0%,0%)          ")); //$NON-NLS-1$
		assertFalse(ColorUtil.isCssRelative("RGB(100,0%,0%)")); //$NON-NLS-1$
		assertFalse(ColorUtil.isCssRelative("RGB(100%, 0 , 0% )")); //$NON-NLS-1$
		assertFalse(ColorUtil.isCssRelative("RGB(100%,100%,0)")); //$NON-NLS-1$
		assertFalse(ColorUtil.isCssRelative("RGB(100.001%,100%,0)")); //$NON-NLS-1$
		assertFalse(ColorUtil.isCssRelative("RGB(-1%,100%,0)")); //$NON-NLS-1$
	}

	public void testDecodeColor() {
		assertEquals(Integer.decode("#800000").intValue(), ColorUtil.parsePredefinedColor("maroon")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Integer.decode("#FF0000").intValue(), ColorUtil.parsePredefinedColor("red")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Integer.decode("#ffA500").intValue(), ColorUtil.parsePredefinedColor("orange")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(Integer.decode("#FFFF00").intValue(), ColorUtil.parsePredefinedColor("yellow")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(Integer.decode("#808000").intValue(), ColorUtil.parsePredefinedColor("olive")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Integer.decode("#800080").intValue(), ColorUtil.parsePredefinedColor("purple")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Integer.decode("#FF00FF").intValue(), ColorUtil.parsePredefinedColor("fuchsia")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Integer.decode("#FFFFFF").intValue(), ColorUtil.parsePredefinedColor("white")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(Integer.decode("#00FF00").intValue(), ColorUtil.parsePredefinedColor("lime")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(Integer.decode("#008000").intValue(), ColorUtil.parsePredefinedColor("green")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(Integer.decode("#000080").intValue(), ColorUtil.parsePredefinedColor("navy")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(Integer.decode("#0000FF").intValue(), ColorUtil.parsePredefinedColor("blue")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(Integer.decode("#00FFFF").intValue(), ColorUtil.parsePredefinedColor("aqua")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(Integer.decode("#008080").intValue(), ColorUtil.parsePredefinedColor("teal")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(Integer.decode("#000000").intValue(), ColorUtil.parsePredefinedColor("black")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(Integer.decode("#C0C0C0").intValue(), ColorUtil.parsePredefinedColor("silver")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals(Integer.decode("#808080").intValue(), ColorUtil.parsePredefinedColor("gray")); //$NON-NLS-1$//$NON-NLS-2$

	}

	public void testParseColor() {
		assertEquals(-1, ColorUtil.parseColor(" ")); //$NON-NLS-1$
		assertEquals(-1, ColorUtil.parseColor(null));
		assertEquals(Integer.decode("#FF00FF").intValue(), ColorUtil.parseColor("#FF00FF")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Integer.decode("#FF00FF").intValue(), ColorUtil.parseColor("0xFF00FF")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Integer.decode("#FF00FF").intValue(), ColorUtil.parseColor("#F0F")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Integer.decode("12").intValue(), ColorUtil.parseColor("12")); //$NON-NLS-1$ //$NON-NLS-2$

		// value > #FFFFFF, should clipped to #FFFFFF

		assertEquals(Integer.decode("#FFFFFF").intValue(), ColorUtil.parseColor("16777216")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Integer.decode("#FFFFFF").intValue(), ColorUtil.parseColor("#1FFFFFF")); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals(Integer.decode("#FF0000").intValue(), ColorUtil.parseColor("red")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Integer.decode("#008000").intValue(), ColorUtil.parseColor("green")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Integer.decode("#0000FF").intValue(), ColorUtil.parseColor("blue")); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals(Integer.decode("#00FF00").intValue(), ColorUtil.parseColor("rgb(0,255,0)")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Integer.decode("#00FF00").intValue(), ColorUtil.parseColor("rgb( 0 , 255 , 0 )")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Integer.decode("#00FF00").intValue(), ColorUtil.parseColor("rgb(0,300,0)")); //$NON-NLS-1$ //$NON-NLS-2$

		assertEquals(Integer.decode("#00FF00").intValue(), ColorUtil.parseColor("rgb(0%,100.00%,0%)")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Integer.decode("#00FF00").intValue(), ColorUtil.parseColor("rgb(0%,200.001%,0%)")); //$NON-NLS-1$ //$NON-NLS-2$
		assertEquals(Integer.decode("#00FF00").intValue(), ColorUtil.parseColor("rgb( 0 %, 100 % , 0 % )")); //$NON-NLS-1$ //$NON-NLS-2$

		// invalid integer format.
		assertEquals(-1, ColorUtil.parseColor("0!FF")); //$NON-NLS-1$
	}

	public void tesetGetPredefinedColor() {
		assertNull(ColorUtil.getPredefinedColor(123));
		assertEquals("red", ColorUtil.getPredefinedColor(Integer.valueOf("#FF0000").intValue())); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals("black", ColorUtil.getPredefinedColor(Integer.valueOf("#FFFFFF").intValue())); //$NON-NLS-1$//$NON-NLS-2$

	}

	public void testGetRGBs() {
		int[] rgb = ColorUtil.getRGBs("#FF00FE"); //$NON-NLS-1$
		assertEquals(255, rgb[0]);
		assertEquals(0, rgb[1]);
		assertEquals(254, rgb[2]);

		rgb = ColorUtil.getRGBs("0xFF00FE"); //$NON-NLS-1$
		assertEquals(255, rgb[0]);
		assertEquals(0, rgb[1]);
		assertEquals(254, rgb[2]);

		rgb = ColorUtil.getRGBs("RGB(255,0,254)"); //$NON-NLS-1$
		assertEquals(255, rgb[0]);
		assertEquals(0, rgb[1]);
		assertEquals(254, rgb[2]);

		rgb = ColorUtil.getRGBs(15);
		assertEquals(0, rgb[0]);
		assertEquals(0, rgb[1]);
		assertEquals(15, rgb[2]);
	}

	public void testFormRGB() {
		assertEquals(Integer.decode("#FFFFFF").intValue(), ColorUtil.formRGB(255, 255, 255)); //$NON-NLS-1$
		assertEquals(0, ColorUtil.formRGB(0, 0, 0));

		assertEquals(Integer.decode("#FF0000").intValue(), ColorUtil.formRGB(300, 0, 0)); //$NON-NLS-1$
		assertEquals(Integer.decode("#00FF00").intValue(), ColorUtil.formRGB(0, 300, 0)); //$NON-NLS-1$
		assertEquals(Integer.decode("#0000FF").intValue(), ColorUtil.formRGB(0, 0, 300)); //$NON-NLS-1$

		assertEquals(Integer.decode("#00FF00").intValue(), ColorUtil.formRGB(-100, 255, 0)); //$NON-NLS-1$
		assertEquals(Integer.decode("#FF0000").intValue(), ColorUtil.formRGB(255, -100, 0)); //$NON-NLS-1$
		assertEquals(Integer.decode("#00FF00").intValue(), ColorUtil.formRGB(0, 255, -100)); //$NON-NLS-1$

		assertEquals(Integer.decode("#000000").intValue(), ColorUtil.formRGB(-100, -100, -100)); //$NON-NLS-1$

	}

}