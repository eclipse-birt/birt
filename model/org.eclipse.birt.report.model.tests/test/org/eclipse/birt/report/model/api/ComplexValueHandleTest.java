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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.Label;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * 
 * TestCases for ComplexValueHandle class.
 * 
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testValueIsSet()}</td>
 * <td>Tests isSet() function for properties on a label.</td>
 * <td>Return <code>true</code> if the value is set ( including the default
 * value). Otherwise, <code>false</code>.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Tests isSet() function for private style properties on a label.</td>
 * <td>Return <code>true</code> if the value is set. Otherwise,
 * <code>false</code>.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Tests isSet() function for properties on a style.</td>
 * <td>Return <code>true</code> if the value is set(( including the default
 * value). Otherwise, <code>false</code>.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Tests isSet() function for members of a highlight rule.</td>
 * <td>Return <code>true</code> if the value is set. Otherwise,
 * <code>false</code>.</td>
 * </tr>
 * 
 * 
 * </table>
 * 
 */

public class ComplexValueHandleTest extends BaseTestCase {

	/**
	 * A subclass of ComplexValueHandle for this test case.
	 */

	private FontHandle fontHandle = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

	protected void setUp() throws Exception {
		super.setUp();
		openDesign("ComplexValueHandleTest.xml"); //$NON-NLS-1$
		assertNotNull(designHandle);
	}

	/**
	 * Tests isSet methods in ComplexValueHandle.
	 * 
	 * @see org.eclipse.birt.report.model.api.ComplexValueHandle
	 * @throws SemanticException if one of values of properties is invalid.
	 */

	public void testValueIsSet() throws SemanticException {
		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("label2"); //$NON-NLS-1$
		assertNotNull(labelHandle);
		fontHandle = labelHandle.getPrivateStyle().getFontFamilyHandle();

		assertNotNull(fontHandle);
		assertFalse(fontHandle.isSet());

		// the value for a top-level element property.

		PropertyHandle propertyHandle = labelHandle.getPropertyHandle(Label.HELP_TEXT_PROP);
		assertNotNull(propertyHandle);
		assertFalse(propertyHandle.isSet());

		propertyHandle = labelHandle.getPropertyHandle(Label.TEXT_PROP);
		assertNotNull(propertyHandle);
		assertTrue(propertyHandle.isSet());

		// the value for the private style property .

		propertyHandle = labelHandle.getPropertyHandle(Style.BACKGROUND_COLOR_PROP);
		assertNotNull(propertyHandle);
		assertFalse(propertyHandle.isSet());

		propertyHandle.setStringValue("red"); //$NON-NLS-1$
		assertTrue(propertyHandle.isSet());

		// a label has a value for the font family property.

		labelHandle = (LabelHandle) designHandle.findElement("label1"); //$NON-NLS-1$
		assertNotNull(labelHandle);
		fontHandle = labelHandle.getPrivateStyle().getFontFamilyHandle();

		assertNotNull(fontHandle);
		assertTrue(fontHandle.isSet());

		// font family property handle on one style and its highlight.

		StyleHandle styleHandle = designHandle.findStyle("Style1"); //$NON-NLS-1$
		assertNotNull(styleHandle);
		fontHandle = styleHandle.getFontFamilyHandle();
		assertNotNull(fontHandle);
		assertTrue(fontHandle.isSet());

		// a property without a value

		propertyHandle = styleHandle.getPropertyHandle(Style.TEXT_INDENT_PROP);
		assertFalse(propertyHandle.isSet());

		// a property without the default value

		propertyHandle = styleHandle.getPropertyHandle(Style.STRING_FORMAT_PROP);
		assertFalse(propertyHandle.isSet());

		Iterator iterator = styleHandle.highlightRulesIterator();
		assertNotNull(iterator);

		// a highlight member in the style with no font family property set.

		HighlightRuleHandle highlightHandle = (HighlightRuleHandle) iterator.next();
		fontHandle = highlightHandle.getFontFamilyHandle();
		assertNotNull(fontHandle);
		assertFalse(fontHandle.isSet());

		fontHandle.setStringValue("song"); //$NON-NLS-1$
		assertTrue(fontHandle.isSet());

		// a highlight member in the style with font family property set.

		styleHandle = designHandle.findStyle("Style2"); //$NON-NLS-1$
		assertNotNull(styleHandle);
		iterator = styleHandle.highlightRulesIterator();
		assertNotNull(iterator);

		highlightHandle = (HighlightRuleHandle) iterator.next();
		fontHandle = highlightHandle.getFontFamilyHandle();
		assertNotNull(fontHandle);
		assertTrue(fontHandle.isSet());
	}
}