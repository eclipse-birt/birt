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

package org.eclipse.birt.report.model.api;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * TestCases for FontHandle class. FontHandle should be got from the specific
 * ElementHandle that contains an font family property.
 * 
 * <p>
 * 
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testParse()}</td>
 * <td>The font name is a CSS constant.</td>
 * <td>Gets the font name and display name correctly.</td>
 * </tr>
 * 
 * 
 * <tr>
 * <td></td>
 * <td>The font name property is a list of font names separated by commas.</td>
 * <td>The font entry can be returned correctly.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>The font weight is a predefined CSS constant.</td>
 * <td>The font weight can be set and gotten correctly.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>The font weight is an invalid value.</td>
 * <td>Throws <code>PropertyValueException</code> with error code
 * CHOICE_NOT_FOUND.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Gets a font handle from the DataSource element.</td>
 * <td>FontHandle is null.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testSetValue()}</td>
 * <td>Sets the font name as a localized string.</td>
 * <td>Output file matches golden file.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Sets the standard CSS font name.</td>
 * <td>Output file matches golden file.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Sets the font name property is a list of font names separated by commas.
 * </td>
 * <td>Output file matches golden file.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Sets the standard CSS font weight.</td>
 * <td>Output file matches golden file.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Sets a font name to a structure like a highlightRule.</td>
 * <td>Output file matches golden file.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Sets a font weight to a structure like a highlightRule.</td>
 * <td>Output file matches golden file.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testCSSFontList()}</td>
 * <td>Checks the list of standard CSS font constants.</td>
 * <td>The order and names of the list is right.</td>
 * </tr>
 * 
 * </table>
 * 
 */

public class FontHandleTest extends BaseTestCase {

	private FontHandle fontHandle = null;

	protected void setUp() throws Exception {
		super.setUp();
		openDesign("FontHandleTest.xml"); //$NON-NLS-1$
		assertNotNull(designHandle);
	}

	/**
	 * Tests get font family when parse a design file.
	 * 
	 * <p>
	 * 
	 * Test Cases:
	 * 
	 * <ul>
	 * <li>The font name is a localized string.
	 * <li>The font name property is a list of font names separated by commas.
	 * <li>The font weight is a predefined CSS constant.
	 * <li>The font weight is an invalid value.
	 * </ul>
	 * 
	 * @throws SemanticException if values of font properties are invalid.
	 */

	public void testParse() throws SemanticException {
		StyleHandle myStyle = designHandle.findStyle("My-Style"); //$NON-NLS-1$
		assertNotNull(myStyle);
		fontHandle = myStyle.getFontFamilyHandle();

		// tests with the font name as CSS-compatible string.

		assertEquals(DesignChoiceConstants.FONT_FAMILY_CURSIVE, fontHandle.getStringValue());

		// tests with the font as a display string.

		ThreadResources.setLocale(TEST_LOCALE);

		assertEquals("\u8fde\u4f53", fontHandle.getDisplayValue()); //$NON-NLS-1$
		assertEquals(DesignChoiceConstants.FONT_FAMILY_CURSIVE, fontHandle.getStringValue());

		ThreadResources.setLocale(ULocale.ENGLISH);

		assertEquals("Cursive", fontHandle //$NON-NLS-1$
				.getDisplayValue());

		// tests with the font entry at a given position, should be null.

		StyleHandle style1 = designHandle.findStyle("Style1"); //$NON-NLS-1$
		assertNotNull(style1);
		fontHandle = style1.getFontFamilyHandle();

		// tests with the font entry at a given position.

		assertEquals("\"Time New Roman\", \"Arial\"", fontHandle.getStringValue()); //$NON-NLS-1$

		String[] names = fontHandle.getFontFamilies();
		assertEquals("\"Time New Roman\"", names[0]); //$NON-NLS-1$
		assertEquals("\"Arial\"", names[1]); //$NON-NLS-1$

		// tests sets/gets CSS constant names.

		fontHandle.setValue(DesignChoiceConstants.FONT_FAMILY_SANS_SERIF);

		assertEquals(DesignChoiceConstants.FONT_FAMILY_SANS_SERIF, fontHandle.getStringValue());

		// read a font handle from a structure like highlight.

		Iterator highlightHandles = style1.highlightRulesIterator();
		assertNotNull(highlightHandles);

		HighlightRuleHandle highlightHandle = (HighlightRuleHandle) highlightHandles.next();
		assertNotNull(highlightHandle);

		// no font family has been defined for style1, the value should be null.

		FontHandle fontHandle = highlightHandle.getFontFamilyHandle();
		assertNotNull(fontHandle);

		assertNull(fontHandle.getStringValue());
		assertNull(fontHandle.getFontFamilies());

		StyleHandle style2 = designHandle.findStyle("Style2"); //$NON-NLS-1$
		assertNotNull(style2);

		// font handle with a default value.

		fontHandle = null;
		fontHandle = style2.getFontFamilyHandle();
		assertNotNull(fontHandle);

		// tests with the font entry at a given position.

		names = fontHandle.getFontFamilies();
		assertEquals(1, names.length);
		assertEquals("serif", names[0]); //$NON-NLS-1$

		// tests with the font weight.

		assertEquals(DesignChoiceConstants.FONT_WEIGHT_LIGHTER, style2.getFontWeight());
		style2.setFontWeight(DesignChoiceConstants.FONT_WEIGHT_100);
		assertEquals(DesignChoiceConstants.FONT_WEIGHT_100, style2.getFontWeight());

		try {
			style2.setFontWeight("450"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(e.getErrorCode(), PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND);
		}

		// read a font handle from a structure like highlight.

		highlightHandles = style2.highlightRulesIterator();
		assertNotNull(highlightHandles);

		highlightHandle = (HighlightRuleHandle) highlightHandles.next();
		assertNotNull(highlightHandle);

		fontHandle = highlightHandle.getFontFamilyHandle();
		assertEquals("\"Arial\"", fontHandle.getStringValue()); //$NON-NLS-1$
		assertEquals("\"Arial\"", fontHandle.getDisplayValue()); //$NON-NLS-1$

		// no second highlight rule, must be null.

		highlightHandle = (HighlightRuleHandle) highlightHandles.next();
		assertNull(highlightHandle);

		// finds a design element that do not support font family property.

		OdaDataSourceHandle dataSource = (OdaDataSourceHandle) designHandle.findDataSource("myDataSource"); //$NON-NLS-1$
		assertNotNull(dataSource);

		// font handle must be null.

		fontHandle = ApiTestUtil.getFontProperty(dataSource);
		assertNull(fontHandle);
	}

	/**
	 * Tests set font family.
	 * 
	 * <p>
	 * 
	 * Test Cases:
	 * 
	 * <ul>
	 * <li>Sets the font name as a localized string.
	 * <li>Sets the font name property is a list of font names separated by commas.
	 * <li>Sets the standard CSS font name.
	 * <li>Sets the standard CSS font weight.
	 * </ul>
	 * 
	 * @throws Exception if values of font properties are invalid or the output file
	 *                   cannot be saved on the storage.
	 */

	public void testSetValue() throws Exception {
		StyleHandle myStyle = designHandle.findStyle("My-Style"); //$NON-NLS-1$
		assertNotNull(myStyle);
		fontHandle = myStyle.getFontFamilyHandle();

		ThreadResources.setLocale(new ULocale("am", "ET")); //$NON-NLS-1$ //$NON-NLS-2$

		fontHandle.setValue(DesignChoiceConstants.FONT_FAMILY_SANS_SERIF);

		// tests with the font entry at a given position, should be null.

		StyleHandle style1 = designHandle.findStyle("Style1"); //$NON-NLS-1$
		assertNotNull(style1);
		fontHandle = style1.getFontFamilyHandle();

		// tests sets/gets CSS constant names.

		fontHandle.setValue(DesignChoiceConstants.FONT_FAMILY_SANS_SERIF);

		assertEquals(DesignChoiceConstants.FONT_FAMILY_SANS_SERIF, fontHandle.getStringValue());

		ThreadResources.setLocale(TEST_LOCALE);

		// tests sets/gets user string names.

		fontHandle.setValue("\u8fde\u4f53"); //$NON-NLS-1$

		assertEquals(DesignChoiceConstants.FONT_FAMILY_CURSIVE, fontHandle.getStringValue());

		StyleHandle style2 = designHandle.findStyle("Style2"); //$NON-NLS-1$
		assertNotNull(style2);
		fontHandle = style2.getFontFamilyHandle();

		// tests with the font entry at a given position.

		fontHandle.setValue("cursive, sans-serif, hello"); //$NON-NLS-1$

		// tests with the font weight.

		style2.setFontWeight(DesignChoiceConstants.FONT_WEIGHT_100);
		assertEquals(DesignChoiceConstants.FONT_WEIGHT_100, style2.getFontWeight());

		// write a font family and weight property to a structure like
		// highlight.

		Iterator highlightHandles = style2.highlightRulesIterator();
		assertNotNull(highlightHandles);

		HighlightRuleHandle highlightHandle = (HighlightRuleHandle) highlightHandles.next();
		assertNotNull(highlightHandle);

		fontHandle = highlightHandle.getFontFamilyHandle();
		fontHandle.setValue(DesignChoiceConstants.FONT_FAMILY_FANTASY);
		highlightHandle.setFontWeight(DesignChoiceConstants.FONT_WEIGHT_900);

	}

	/**
	 * Tests to get the list of standard CSS font constants.
	 * 
	 * <p>
	 * 
	 * Test Cases:
	 * 
	 * <ul>
	 * <li>Checks the list of standard CSS font constants.
	 * </ul>
	 * 
	 */

	public void testCSSFontList() {
		StyleHandle myStyle = designHandle.findStyle("My-Style"); //$NON-NLS-1$
		assertNotNull(myStyle);
		fontHandle = myStyle.getFontFamilyHandle();

		// tests with the list of standard CSS font constants.

		IChoice[] standardChoices = fontHandle.getCSSFontFamilies();

		assertEquals(5, standardChoices.length);

		assertEquals(standardChoices[0].getName(), DesignChoiceConstants.FONT_FAMILY_SERIF);
		assertEquals(standardChoices[1].getName(), DesignChoiceConstants.FONT_FAMILY_SANS_SERIF);
		assertEquals(standardChoices[2].getName(), DesignChoiceConstants.FONT_FAMILY_CURSIVE);
		assertEquals(standardChoices[3].getName(), DesignChoiceConstants.FONT_FAMILY_FANTASY);
		assertEquals(standardChoices[4].getName(), DesignChoiceConstants.FONT_FAMILY_MONOSPACE);

	}

	/**
	 * Tests to get the definition of font properties.
	 * 
	 * <p>
	 * 
	 * Test Cases:
	 * 
	 * <ul>
	 * <li>Gets the property definition of a font property</li>
	 * <li>Gets the member reference of a font property</li>
	 * </ul>
	 * 
	 */

	public void testDefn() {
		StyleHandle myStyle = designHandle.findStyle("Style2"); //$NON-NLS-1$
		fontHandle = myStyle.getFontFamilyHandle();

		assertEquals(Style.FONT_FAMILY_PROP, fontHandle.getPropertyDefn().getName());

		Iterator iter = myStyle.highlightRulesIterator();
		HighlightRuleHandle handle = (HighlightRuleHandle) iter.next();

		fontHandle = handle.getFontFamilyHandle();

		assertEquals(Style.HIGHLIGHT_RULES_PROP, fontHandle.getPropertyDefn().getName());
		assertEquals(Style.FONT_FAMILY_PROP, fontHandle.getContext().getPropDefn().getName());

	}
}
