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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * 
 * 
 * TestCases for ColorHandle.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testGetColor()}</td>
 * <td>Gets the RGB and CSS compatible colors in a style.</td>
 * <td>Values matches with those in the design file.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Gets the RGB and CSS compatible colors in a highlight.</td>
 * <td>Values matches with those in the design file.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Gets a color of a style with the default value of black.</td>
 * <td>ColorHandle is not null and the color is black.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Gets a color of a style without the default value.</td>
 * <td>ColorHandle is null.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSetColor()}</td>
 * <td>Sets colors in the integer and string to a style.</td>
 * <td>The value is set and the golden file matches with the output file.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Sets colors in the integer and string to a highlight.</td>
 * <td>The value is set and the golden file matches with the output file.</td>
 * </tr>
 * 
 * 
 * <tr>
 * <td>{@link #testSetColor()}</td>
 * <td>locale is CHINA, color value is css "red"</td>
 * <td>The return should be Chinese "��"</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>locale is CHINA, color value is "#FF00FF", preference is
 * CSS_ABSOLUTE_FORMAT</td>
 * <td>The return should be "RGB(255,0,255)"</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>locale is CHINA, color value is "#FF00FF", preference is
 * CSS_RELATIVE_FORMAT</td>
 * <td>The return should be "RGB(255%,0%,255%)"</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>locale is CHINA, color value is "#FF00FF", preference is HTML_FORMAT</td>
 * <td>The return should be "#FF00FF"</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>locale is CHINA, color value is "#FF00FF", preference is JAVA_FORMAT</td>
 * <td>The return should be "0xFF00FF"</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>locale is ENGLISH, color value is css "red", preference is JAVA_FORMAT
 * </td>
 * <td>The return should be "red"</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>input is integer value for "#FF00FF", radix is 10. preference is
 * CSS_RELATIVE_FORMAT</td>
 * <td>The return should be "RGB(255%,0%,255%)"</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>input is integer value for "#FF00FF", radix is 10. preference is
 * HTML_FORMAT</td>
 * <td>The return should be "#FF00FF"</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>input is integer value for "#FF00FF", radix is 10. preference is
 * JAVA_FORMAT</td>
 * <td>The return should be "0xFF00FF"</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>input is integer value for "#FF00FF", radix is 10. preference is is not
 * in the allowed list</td>
 * <td>The return should be default as CSS_ABSOLUTE_FORMAT</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetCSSColors()}</td>
 * <td>call getColors()</td>
 * <td>The return should be the choice array containing all the predefined css
 * colors.</td>
 * </tr>
 * 
 */

public class ColorHandleTest extends BaseTestCase {

	/**
	 * The temporary color handle for the unit test.
	 */

	private ColorHandle colorHandle = null;
	private ColorHandle colorHandle1 = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

	protected void setUp() throws Exception {
		super.setUp();
		openDesign("ColorHandleTest.xml", TEST_LOCALE); //$NON-NLS-1$

		colorHandle = designHandle.findStyle("My-Style").getColor(); //$NON-NLS-1$
		colorHandle1 = designHandle.findStyle("My-Style1").getColor(); //$NON-NLS-1$

	}

	/**
	 * 
	 * test setStringValue(), setIntValue() and setRGB().
	 * 
	 * @throws Exception if the rgb value of the color is invalid or the output file
	 *                   cannot be saved.
	 */

	public void testSetColor() throws Exception {
		// #FF00FF
		colorHandle.setRGB(16711935);
		assertEquals(16711935, colorHandle.getRGB());

		colorHandle.setValue("#FF00FF"); //$NON-NLS-1$
		assertEquals(16711935, colorHandle.getRGB());

		colorHandle.setValue("16711935"); //$NON-NLS-1$
		assertEquals(16711935, colorHandle.getRGB());
		assertEquals("RGB(255,0,255)", colorHandle.getCssValue()); //$NON-NLS-1$

		colorHandle.setValue(new Integer(16711925));
		assertEquals("#FF00F5", colorHandle.getStringValue()); //$NON-NLS-1$

		colorHandle.setValue("red"); //$NON-NLS-1$
		assertEquals("red", colorHandle.getCssValue()); //$NON-NLS-1$
		assertEquals("red", colorHandle.getStringValue()); //$NON-NLS-1$
		assertEquals(16711680, colorHandle.getRGB());
		assertEquals("\u7ea2\u8272", colorHandle.getDisplayValue()); //$NON-NLS-1$

		colorHandle.setValue("REd"); //$NON-NLS-1$
		assertEquals("red", colorHandle.getCssValue()); //$NON-NLS-1$
		assertEquals("red", colorHandle.getStringValue()); //$NON-NLS-1$
		assertEquals("red", colorHandle.getValue()); //$NON-NLS-1$

		ThreadResources.setLocale(TEST_LOCALE);

		// red for Chinese.

		colorHandle.setStringValue("\u7ea2\u8272"); //$NON-NLS-1$
		assertEquals("red", colorHandle.getCssValue()); //$NON-NLS-1$
		assertEquals("\u7ea2\u8272", colorHandle.getDisplayValue()); //$NON-NLS-1$

		colorHandle.setStringValue("myColor1"); //$NON-NLS-1$
		assertEquals("myColor1", colorHandle.getStringValue()); //$NON-NLS-1$

		assertEquals("sheng lan", colorHandle.getDisplayValue()); //$NON-NLS-1$

		// red for China

		colorHandle.setValue("\u7ea2\u8272"); //$NON-NLS-1$
		assertEquals("red", colorHandle.getCssValue()); //$NON-NLS-1$

		// tests writing a color to a structure like a highlight rule.

		Style style1 = (Style) design.findStyle("My-Style2"); //$NON-NLS-1$
		StyleHandle style2Handle = style1.handle(design);

		Iterator highlightHandles = style2Handle.highlightRulesIterator();
		assertNotNull(highlightHandles);
		HighlightRuleHandle highlightHandle = (HighlightRuleHandle) highlightHandles.next();
		assertNotNull(highlightHandle);

		colorHandle = highlightHandle.getColor();
		assertNotNull(colorHandle);
		colorHandle.setRGB(1193046);

		colorHandle = highlightHandle.getBackgroundColor();
		assertNotNull(colorHandle);

		colorHandle.setValue("myColor2"); //$NON-NLS-1$

	}

	/**
	 * test getCSSCompatibleValue() and getRGB() and getStringValue().
	 * 
	 * @throws SemanticException if the the value of the color is invalid.
	 */

	public void testGetColor() throws SemanticException {
		// red
		assertEquals(16711680, colorHandle.getRGB());
		assertEquals("red", colorHandle.getStringValue()); //$NON-NLS-1$

		// #FF00FF
		assertEquals(16711935, colorHandle1.getRGB());

		assertEquals("red", colorHandle.getCssValue()); //$NON-NLS-1$
		colorHandle.setValue("#FF00FF"); //$NON-NLS-1$
		assertEquals("RGB(255,0,255)", colorHandle.getCssValue()); //$NON-NLS-1$
		assertEquals("#FF00FF", colorHandle.getStringValue()); //$NON-NLS-1$

		Style style1 = (Style) design.findStyle("My-Style2"); //$NON-NLS-1$
		StyleHandle style2Handle = style1.handle(design);

		// has the default value in black.

		colorHandle = style2Handle.getColor();
		assertNotNull(colorHandle);
		assertEquals("black", colorHandle.getCssValue()); //$NON-NLS-1$

		// tests reading a color from a structure like a highlight rule.

		Iterator highlightHandles = style2Handle.highlightRulesIterator();
		assertNotNull(highlightHandles);
		HighlightRuleHandle highlightHandle = (HighlightRuleHandle) highlightHandles.next();
		assertNotNull(highlightHandle);

		colorHandle = highlightHandle.getColor();
		assertNotNull(colorHandle);

		assertEquals("yellow", colorHandle.getCssValue()); //$NON-NLS-1$

		colorHandle = highlightHandle.getBackgroundColor();
		assertNotNull(colorHandle);

		assertEquals("RGB(18,52,86)", colorHandle.getCssValue()); //$NON-NLS-1$
		assertEquals(1193046, colorHandle.getRGB());

		colorHandle = highlightHandle.getBorderBottomColor();
		assertNotNull(colorHandle);

		// value from custom color pallete.
		colorHandle = designHandle.findStyle("My-Style3").getColor(); //$NON-NLS-1$
		assertEquals("myColor1", colorHandle.getStringValue()); //$NON-NLS-1$
		assertEquals(1193210, colorHandle.getRGB());

	}

	/**
	 * test getColors().
	 * 
	 */

	public void testGetCSSColors() {
		List colors = colorHandle.getCSSColors();
		assertEquals(17, colors.size());
	}
}