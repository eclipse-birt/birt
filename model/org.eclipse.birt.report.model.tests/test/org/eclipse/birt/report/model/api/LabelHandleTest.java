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

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Test case for LabelHandle.
 *
 */

public class LabelHandleTest extends BaseTestCase {

	/**
	 * Input design file.
	 */

	private final static String INPUT_FILE = "LabelHandleTest.xml"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		openDesign(INPUT_FILE);
	}

	/**
	 * Test getDisplayText().
	 */

	public void testGetDisplayText() {
		LabelHandle label1 = (LabelHandle) designHandle.findElement("label1"); //$NON-NLS-1$
		ModuleOption options = new ModuleOption();
		design.setOptions(options);

		options.setLocale(ULocale.US);
		assertEquals("en_US", label1.getDisplayText()); //$NON-NLS-1$

		options.setLocale(new ULocale("en")); //$NON-NLS-1$
		assertEquals("en", label1.getDisplayText()); //$NON-NLS-1$

		LabelHandle label2 = (LabelHandle) designHandle.findElement("label2"); //$NON-NLS-1$
		assertEquals("label2", label2.getDisplayText()); //$NON-NLS-1$

		LabelHandle label3 = (LabelHandle) designHandle.findElement("label3"); //$NON-NLS-1$
		assertEquals("label3", label3.getDisplayText()); //$NON-NLS-1$

		LabelHandle label4 = (LabelHandle) designHandle.findElement("label4"); //$NON-NLS-1$
		options.setLocale(ULocale.US);
		assertEquals("label4", label4.getDisplayText()); //$NON-NLS-1$
	}

	/**
	 * test getProperties and setProperties
	 *
	 * @throws SemanticException
	 *
	 */

	public void testSetGetProperties() throws SemanticException {
		ElementFactory factory = new ElementFactory(design);

		// literal string property value is not trimed.

		LabelHandle label = factory.newLabel("label1"); //$NON-NLS-1$
		label.setText("  Hello,\n  There is...   "); //$NON-NLS-1$
		assertEquals("  Hello,\n  There is...   ", label.getText()); //$NON-NLS-1$

		// Treats "" is as same as null.

		label.setText(""); //$NON-NLS-1$
		assertEquals("", label.getText()); //$NON-NLS-1$

		// get font size default value.

		label.setProperty(StyleHandle.FONT_SIZE_PROP, ""); //$NON-NLS-1$
		assertEquals(null, label.getStringProperty("medium")); //$NON-NLS-1$

		// Set height to a big number 10E(32)
		label.setHeight("100000000000000000000000000000000pt"); //$NON-NLS-1$
		assertEquals("100000000000000000000000000000000pt", label.getHeight().getStringValue()); //$NON-NLS-1$

		// Label5 take the font-style value from report selector.
		LabelHandle label5 = factory.newLabel("label5"); //$NON-NLS-1$
		designHandle.getBody().add(label5);
		assertEquals("normal", label5.getProperty(StyleHandle.FONT_STYLE_PROP)); //$NON-NLS-1$
		SharedStyleHandle reportSelector = factory.newStyle("report"); //$NON-NLS-1$
		reportSelector.setFontStyle(DesignChoiceConstants.FONT_STYLE_ITALIC);
		designHandle.getStyles().add(reportSelector);
		assertEquals(DesignChoiceConstants.FONT_STYLE_ITALIC, label5.getProperty(StyleHandle.FONT_STYLE_PROP));
	}

	/**
	 * Test get message of label.
	 *
	 */

	public void testGetLocalizationMessage() {
		LabelHandle handle10 = (LabelHandle) designHandle.findElement("label10");//$NON-NLS-1$
		assertEquals("", handle10.getDisplayText()); //$NON-NLS-1$
		assertNull(handle10.getRoot().getMessage(handle10.getTextKey()));

		LabelHandle handle11 = (LabelHandle) designHandle.findElement("label11");//$NON-NLS-1$
		assertEquals("label in i18n", handle11.getDisplayText());//$NON-NLS-1$
		assertEquals("label in i18n", handle11.getRoot().getMessage(handle11.getTextKey()));//$NON-NLS-1$

		LabelHandle handle12 = (LabelHandle) designHandle.findElement("label12");//$NON-NLS-1$
		assertEquals("", handle12.getRoot().getMessage(handle12.getTextKey()));//$NON-NLS-1$

		LabelHandle handle15 = (LabelHandle) designHandle.findElement("label15");//$NON-NLS-1$
		assertEquals("label_localized", handle15.getDisplayText());//$NON-NLS-1$
		assertNull(handle15.getRoot().getMessage(handle15.getTextKey()));// $NON-NLS-1$

		LabelHandle handle16 = (LabelHandle) designHandle.findElement("label16");//$NON-NLS-1$
		assertEquals("actuate", handle16.getDisplayText());//$NON-NLS-1$
		assertEquals("actuate", handle16.getRoot().getMessage(handle16.getTextKey()));//$NON-NLS-1$
	}

	/**
	 * Test get message of label which extends from library.
	 *
	 * @throws Exception
	 */
	public void testBaseIdLocalizationMessage() throws Exception {
		if (designHandle != null) {
			designHandle.close();
		}

		if (libraryHandle != null) {
			libraryHandle.close();
		}

		openDesign("LabelHandleTest_1.xml");//$NON-NLS-1$
		LabelHandle handle = (LabelHandle) designHandle.findElement("NewLabel");//$NON-NLS-1$
		assertEquals("actuate", handle.getDisplayText());//$NON-NLS-1$

	}

}
