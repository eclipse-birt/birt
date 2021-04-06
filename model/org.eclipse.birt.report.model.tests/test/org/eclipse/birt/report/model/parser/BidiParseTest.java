/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  IBM Corporation  - Bidi properties test
 *******************************************************************************/

package org.eclipse.birt.report.model.parser;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.HighlightRuleHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TOCHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.core.NameSpace;
import org.eclipse.birt.report.model.core.StyleElement;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.interfaces.IReportDesignModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test cases for Bidi properties parsing, writing and referring.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * <tr>
 * <td>{@link #testParser()}</td>
 * <td>Test whether all style and non-style Bidi properties can be read.</td>
 * <td>All Bidi property values should be right.</td>
 * </tr>
 * <tr>
 * <td>{@link #testTable()}</td>
 * <td>Test getting the style property value set in element's style.</td>
 * <td>The property value should be from this element's style.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the style property value set in element's private style.
 * </td>
 * <td>The property value should be from this element's private style.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the style property value that is set on the local style,
 * including predefined style, of element's parent.</td>
 * <td>The property value should be from parent's local style.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the style property value that is set on the non-local style
 * of element's parent.</td>
 * <td>The property value should be null.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the style property value that is set on the predefined style
 * of one element's container/slot combination.</td>
 * <td>The property value should be from the predefined style of this element's
 * container/slot combination.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the style property value that is set on the predefined style
 * of the slot in which one element's.</td>
 * <td>The property value should be from the predefined style of the slot in
 * which this element's.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the style property value that is set on the non-predefined
 * style of one element's container.</td>
 * <td>The property value should be from the non-predefined style of one
 * element's container.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the style property value that is set on the predefined style
 * of one element's container.</td>
 * <td>The property value should be from the predefined style of one element's
 * container.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the non-style property value that is set on one element's
 * container.</td>
 * <td>The property value should be null.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test getting the style property value that is not set on the
 * non-predefined style, but set on one element's container.</td>
 * <td>The property value should be from the non-predefined style of this
 * element's container.</td>
 * </tr>
 * <tr>
 * <td>{@link #testWriter()}</td>
 * <td>Test writer.</td>
 * <td>The output file should be same as golden file.</td>
 * </tr>
 * <tr>
 * <td>{@link #testReadHighlightRules()}</td>
 * <td>Test highlight 'bidiTextDirection' property. Get the member value to see
 * if they're identical to those defined in the input file.</td>
 * <td>The member values should be identical to those in the input file</td>
 * </tr>
 * <tr>
 * <td>{@link #testWriteHighlightRules()}</td>
 * <td>Test highlight 'bidiTextDirection' property. Set the member value to see
 * if they're identical to those defined in the golden file.</td>
 * <td>Output file matches the golden file.</td>
 * </tr>
 * <tr>
 * <td>{@link #testSessionDefault()}</td>
 * <td>Test searching property with session default value.</td>
 * <td>The property value should be from session.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Test searching property with metadata default value.</td>
 * <td>The property value should be from metadata.</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td>Clear session default value.</td>
 * <td>The property value should be from metadata.</td>
 * </tr>
 * <tr>
 * <td>{@link #testErrorValidation()}</td>
 * <td>Test semantic errors with the design file input.</td>
 * <td>The errors are collected, such as the wrong choice names.</td>
 * </tr>
 * </table>
 * 
 */

public class BidiParseTest extends BaseTestCase {
	static final String IN_FILE_NAME = "BidiParseTest.xml"; //$NON-NLS-1$
	static final String IN_FILE_NAME_1 = "BidiParseTest_1.xml"; //$NON-NLS-1$
	static final String INVALID_FILE_NAME = "BidiParseTest_invalid.xml"; //$NON-NLS-1$
	static final String GOLDEN_FILE_NAME = "BidiParseTest_golden.xml"; //$NON-NLS-1$
	static final String GOLDEN_FILE_NAME_1 = "BidiParseTest_golden_1.xml"; //$NON-NLS-1$

	static final String ORIENTATION = IReportDesignModel.BIDI_ORIENTATION_PROP;
	static final String DIRECTION = Style.TEXT_DIRECTION_PROP;
	static final String LTR = DesignChoiceConstants.BIDI_DIRECTION_LTR;
	static final String RTL = DesignChoiceConstants.BIDI_DIRECTION_RTL;

	private static String[][] testStyleData = { //
			{ "Bidi-Style-LTR", LTR }, //$NON-NLS-1$
			{ "Bidi-Style-RTL", RTL }, //$NON-NLS-1$
			{ "Empty-Style", null }, //$NON-NLS-1$
			{ "table-detail", RTL } }; //$NON-NLS-1$

	private static Object[][] testElementData = { //
			{ "testLabel", true, true }, //$NON-NLS-1$
			{ "testText", false, false }, //$NON-NLS-1$
			{ "testExtendedItem", true, false }, //$NON-NLS-1$
			{ "testTextData", true, true }, //$NON-NLS-1$
			{ "testGrid", true, true }, //$NON-NLS-1$
			{ "testGridLabel", true, true }, //$NON-NLS-1$
			{ "testGridLabelLTR", false, false }, //$NON-NLS-1$
			{ "testGridLabelRTL", true, false }, //$NON-NLS-1$
			{ "testTable", false, false }, //$NON-NLS-1$
			{ "testTableLabel", false, false }, //$NON-NLS-1$
			{ "testTableLabelLTR", false, false }, //$NON-NLS-1$
			{ "testTableLabelRTL", true, false }, //$NON-NLS-1$
			{ "testFooterLabel", false, false }, //$NON-NLS-1$
			{ "testFooterLabelRTL", true, false }, //$NON-NLS-1$
			{ "testList", true, false }, //$NON-NLS-1$
			{ "testListText", true, false }, //$NON-NLS-1$
			{ "testListTextLTR", false, false }, //$NON-NLS-1$
			{ "testListTextRTL", true, false } }; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Test parsing and getting Bidi properties.
	 * 
	 * @throws Exception
	 */

	public void testParser() throws Exception {
		openDesign(IN_FILE_NAME);

		// Test getting bidiTextDirection from style element.
		for (int n = testStyleData.length; n-- > 0;) {
			testStyleElementProperty(testStyleData[n][0], testStyleData[n][1]);
		}
		NameSpace ns = design.getNameHelper().getNameSpace(Module.STYLE_NAME_SPACE);
		assertEquals(testStyleData.length, ns.getCount());

		// Test getting non-style property value set in element itself.
		for (int n = testElementData.length; n-- > 0;) {
			testPropertyFromSelector((String) testElementData[n][0], (Boolean) testElementData[n][1],
					(Boolean) testElementData[n][2]);
		}
		// bidiLayoutOrientation property test
		assertEquals(RTL, design.getProperty(design, ORIENTATION));
		assertTrue(designHandle.isDirectionRTL());
	}

	/**
	 * Tests the 'bidiTextDirection' style in Table.
	 * 
	 * @throws Exception any exception caught
	 */

	public void testTable() throws Exception {
		openDesign(IN_FILE_NAME);

		// Test getting the style property value that is set on the predefined
		// or custom style of one element's slot.

		TableItem table = (TableItem) design.findElement("testTable"); //$NON-NLS-1$
		testPropertyFromSelector(table, false, false, false, "Bidi-Style-LTR"); //$NON-NLS-1$

		DesignElement element = table.getSlot(TableItem.DETAIL_SLOT).getContent(0);
		testPropertyFromSelector(element, true, false, false, "table-detail"); //$NON-NLS-1$

		element = table.getSlot(TableItem.HEADER_SLOT).getContent(0);
		testPropertyFromSelector(element, false, false, true, "Bidi-Style-LTR"); //$NON-NLS-1$

		element = table.getSlot(TableItem.HEADER_SLOT).getContent(1);
		testPropertyFromSelector(element, true, false, false, "Bidi-Style-RTL"); //$NON-NLS-1$

		element = table.getSlot(TableItem.FOOTER_SLOT).getContent(0);
		testPropertyFromSelector(element, false, false, true, "Bidi-Style-LTR"); //$NON-NLS-1$

		element = table.getSlot(TableItem.COLUMN_SLOT).getContent(0);
		testPropertyFromSelector(element, false, false, true, "Bidi-Style-LTR"); //$NON-NLS-1$

		table.setStyle(null);
		testPropertyFromSelector(element, true, true, true, "table-detail"); //$NON-NLS-1$
		assertNull(table.getFactoryProperty(design, DIRECTION));

		table.setStyleName("Bidi-Style-RTL"); //$NON-NLS-1$
		testPropertyFromSelector(element, true, false, true, "Bidi-Style-RTL"); //$NON-NLS-1$
		assertNotNull(table.getFactoryProperty(design, DIRECTION));

		table.setStyleName("Bidi-Style-LTR"); //$NON-NLS-1$
		testPropertyFromSelector(element, false, false, true, "Bidi-Style-LTR"); //$NON-NLS-1$
	}

	private void testStyleElementProperty(String name, String value) {
		StyleElement style = design.findStyle(name);
		assertNotNull(style);
		assertEquals(value, style.getStringProperty(design, DIRECTION));
	}

	private void testPropertyFromSelector(String name, boolean rtl, boolean inherited) {
		testPropertyFromSelector(designHandle.findElement(name), rtl, inherited);
	}

	private void testPropertyFromSelector(DesignElementHandle deh, boolean rtl, boolean inherited) {
		assertNotNull(deh);
		assertNull(deh.getStringProperty(ORIENTATION));
		assertEquals(deh.isDirectionRTL(), rtl);
		assertEquals(deh.getStringProperty(DIRECTION) == null, inherited);
	}

	private void testPropertyFromSelector(DesignElement element, boolean rtl, boolean inherited,
			boolean factoryPropIsNull, String styleName) {
		assertNotNull(element);
		DesignElementHandle deh = element.getHandle(design);
		assertNotNull(deh);
		testPropertyFromSelector(deh, rtl, inherited);

		StyleElement style = design.findStyle(styleName);
		assertNotNull(style);
		assertEquals(rtl, RTL.equals(style.getStringProperty(design, DIRECTION)));

		assertEquals(element.getFactoryProperty(design, DIRECTION) == null, factoryPropIsNull);
	}

	/**
	 * Test writer.
	 * 
	 * @throws Exception any exception caught
	 */
	public void testWriter() throws Exception {
		openDesign(IN_FILE_NAME);

		DesignElementHandle handle = designHandle.findElement("testLabel"); //$NON-NLS-1$
		handle.setStyleName("Bidi-Style-LTR"); //$NON-NLS-1$

		handle = designHandle.findElement("testText"); //$NON-NLS-1$
		StyleHandle style = handle.getPrivateStyle();
		style.setTextDirection(RTL);

		design.setProperty(ORIENTATION, LTR);

		save();
		assertTrue(compareFile(GOLDEN_FILE_NAME));
	}

	/**
	 * Get the member value of highlight rule to see if they are identical to those
	 * specified in the input file.
	 * 
	 * @throws Exception any exception during writing the design file.
	 */

	public void testReadHighlightRules() throws Exception {
		openDesign(IN_FILE_NAME_1);

		StyleHandle styleHandle = designHandle.findStyle("My-Style"); //$NON-NLS-1$
		Iterator highlightHandles = styleHandle.highlightRulesIterator();
		assertNotNull(highlightHandles);

		HighlightRuleHandle highlightHandle = (HighlightRuleHandle) highlightHandles.next();
		assertNotNull(highlightHandle);
		assertEquals(RTL, highlightHandle.getTextDirection());

		// the second highlight rule
		highlightHandle = (HighlightRuleHandle) highlightHandles.next();
		assertNotNull(highlightHandle);
		assertNull(highlightHandle.getTextDirection());
	}

	/**
	 * Test highlight writer.
	 * 
	 * @throws Exception any exception during reading/writing the design file.
	 */

	public void testWriteHighlightRules() throws Exception {
		openDesign(IN_FILE_NAME_1);

		StyleHandle styleHandle = designHandle.findStyle("My-Style"); //$NON-NLS-1$
		Iterator highlightHandles = styleHandle.highlightRulesIterator();
		assertNotNull(highlightHandles);

		HighlightRuleHandle highlightHandle = (HighlightRuleHandle) highlightHandles.next();

		// invalid choice value
		try {
			highlightHandle.setTextDirection("nochoice"); //$NON-NLS-1$
			fail();
		} catch (PropertyValueException e) {
			assertEquals(e.getErrorCode(), PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND);
		}
		highlightHandle.setTextDirection(LTR);

		// the second highlight rule.

		highlightHandle = (HighlightRuleHandle) highlightHandles.next();
		assertNotNull(highlightHandle);
		highlightHandle.setTextDirection(RTL);
		assertEquals(RTL, highlightHandle.getTextDirection());

		save();

		assertTrue(compareFile(GOLDEN_FILE_NAME_1));
	}

	/**
	 * Test property search with Session Default.
	 * 
	 * @throws DesignFileException
	 * @throws PropertyValueException
	 */
	public void testSessionDefault() throws DesignFileException, PropertyValueException {
		openDesign(IN_FILE_NAME);

		DesignElementHandle grid = designHandle.findElement("testGrid"); //$NON-NLS-1$
		assertNotNull(grid);

		// Session default value
		design.getSession().setDefaultValue(DIRECTION, LTR);

		assertEquals(LTR, grid.getStringProperty(DIRECTION));
		assertFalse(grid.isDirectionRTL());

		DesignElementHandle list = designHandle.findElement("testList"); //$NON-NLS-1$
		assertNotNull(list);
		// List overrides the default
		assertEquals(RTL, list.getStringProperty(DIRECTION));
		assertTrue(list.isDirectionRTL());

		// Remove session default value
		design.getSession().setDefaultValue(DIRECTION, null);

		assertTrue(grid.isDirectionRTL());
		assertNull(grid.getStringProperty(DIRECTION));
		assertTrue(list.isDirectionRTL());
		assertEquals(RTL, list.getStringProperty(DIRECTION));
	}

	/**
	 * Tests the semantic errors of the Style, such as the font-size and width are
	 * non-negative.
	 * 
	 * @throws Exception
	 */

	public void testErrorValidation() throws Exception {
		openDesign(IN_FILE_NAME);

		DesignElementHandle text = designHandle.findElement("testText"); //$NON-NLS-1$
		assertNotNull(text);
		assertNotNull(designHandle.getProperty(ORIENTATION));

		Object oldValue = text.getProperty(DIRECTION);
		text.setProperty(DIRECTION, null);

		try {
			text.setProperty(DIRECTION, LTR + "."); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, e.getErrorCode());
			testPropertyFromSelector(text, true, true);
		}
		try {
			text.setProperty(DIRECTION, RTL + "_"); //$NON-NLS-1$
			fail();
		} catch (SemanticException e) {
			assertEquals(PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, e.getErrorCode());
			testPropertyFromSelector(text, true, true);
		}
		text.setProperty(DIRECTION, LTR.toUpperCase());
		testPropertyFromSelector(text, false, false);

		text.setProperty(DIRECTION, RTL.toUpperCase());
		testPropertyFromSelector(text, true, false);

		if (oldValue != null)
			text.setProperty(DIRECTION, oldValue);

		// Verify opening report design with invalid direction values fails.
		try {
			openDesign(INVALID_FILE_NAME);
			fail();
		} catch (DesignFileException e) {
			return;
		}
		assertTrue(false);
	}

	/**
	 * Tests the 'bidiTextDirection' style in TOC.
	 * 
	 * @throws Exception
	 */
	public void testTOC() throws Exception {
		openDesign(IN_FILE_NAME);
		LabelHandle label = designHandle.getElementFactory().newLabel(null);

		testPropertyFromSelector(label, false, true);

		designHandle.getBody().add(label);

		testPropertyFromSelector(label, designHandle.isDirectionRTL(), true);
		label.addTOC("label toc expression"); //$NON-NLS-1$

		TOCHandle tocHandle = label.getTOC();

		assertNull(tocHandle.getTextDirection());

		CommandStack stack = designHandle.getCommandStack();
		stack.startTrans(null);
		StyleHandle style = designHandle.getElementFactory().newStyle("newTocStyle"); //$NON-NLS-1$
		style.setTextDirection(LTR);
		designHandle.getStyles().add(style);
		tocHandle.setStyleName(style.getName());
		stack.commit();
		assertEquals(style.getTextDirection(), tocHandle.getTextDirection());

		stack.undo();

		assertNull(tocHandle.getTextDirection());

		// add style and set toc style
		designHandle.getStyles().add(style);
		tocHandle.setStyleName(style.getName());
		assertEquals(style.getTextDirection(), tocHandle.getTextDirection());

		stack.undo();

		assertNull(tocHandle.getTextDirection());
	}
}
