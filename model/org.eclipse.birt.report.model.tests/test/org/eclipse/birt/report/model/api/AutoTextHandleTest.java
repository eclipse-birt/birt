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

import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test case for AutoTextHandle.
 *
 */

public class AutoTextHandleTest extends BaseTestCase {

	/**
	 * Input , output and golden design file.
	 */

	private final static String INPUT_FILE = "AutoTextHandleTest.xml"; //$NON-NLS-1$
	private final static String OUTPUT_FILE = "AutoTextHandleTest_golden.xml";//$NON-NLS-1$
	private final static String INPUT_FILE_ONE = "AutoTextHandleTest_1.xml";//$NON-NLS-1$
	private final static String INPUT_FILE_TWO = "AutoTextHandleTest_2.xml";//$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @see junit.framework.TestCase#setUp()
	 */

	@Override
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * get value from xml file and test if the <code>AutoTextHandle</code> can
	 * control AutoText element
	 *
	 * Test getAutoTextType()
	 *
	 * @throws Exception
	 */
	public void testAutoTextValue() throws Exception {
		openDesign(INPUT_FILE_ONE);
		AutoTextHandle autoText = (AutoTextHandle) designHandle.findElement("sf1"); //$NON-NLS-1$

		String autotext = autoText.getAutoTextType();
		assertEquals(DesignChoiceConstants.AUTO_TEXT_PAGE_NUMBER, autotext);

		String displayName = autoText.getDisplayLabel(DesignElement.FULL_LABEL);
		assertEquals("Page Number", displayName); //$NON-NLS-1$

		autoText.setAutoTextType(DesignChoiceConstants.AUTO_TEXT_TOTAL_PAGE);
		displayName = autoText.getDisplayLabel();
		assertEquals("Total Page", displayName); //$NON-NLS-1$
		assertEquals("Total Page", autoText.getDisplayLabel(DesignElement.FULL_LABEL)); //$NON-NLS-1$

		assertEquals("test", autoText.getPageVariable()); //$NON-NLS-1$
	}

	/**
	 * create a new autotext item and add it under footer/header of
	 * simple-master-page
	 *
	 * Test setAutoTextType( String )
	 *
	 * @throws Exception
	 */
	public void testAddNewAutoText() throws Exception {
		openDesign(INPUT_FILE);
		ElementFactory factory = new ElementFactory(design);
		AutoTextHandle sf = factory.newAutoText("sf1"); //$NON-NLS-1$

		sf.setAutoTextType(DesignChoiceConstants.AUTO_TEXT_PAGE_VARIABLE);

		SimpleMasterPageHandle smpHandle = (SimpleMasterPageHandle) designHandle.findMasterPage("My Page");//$NON-NLS-1$

		smpHandle.addElement(sf, 0);

		SharedStyleHandle reportSelector = factory.newStyle("myStyle"); //$NON-NLS-1$
		reportSelector.setNumberFormat("hexingjie");//$NON-NLS-1$
		reportSelector.setNumberFormatCategory(DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY);
		designHandle.getStyles().add(reportSelector);

		sf.setStyleName("myStyle");//$NON-NLS-1$
		sf.setPageVariable("test"); //$NON-NLS-1$

		save();
		assertTrue(compareFile(OUTPUT_FILE));
	}

	/**
	 * Tests the display value of the auto text. Added for bugzilla 280232
	 *
	 * @throws Exception
	 */
	public void testAutoTextDisplayValue() throws Exception {
		openDesign(INPUT_FILE_TWO);

		AutoTextHandle autoText = (AutoTextHandle) designHandle.findElement("sf1");
		String displayName = autoText.getDisplayLabel(DesignElement.FULL_LABEL);

		assertEquals(DesignChoiceConstants.AUTO_TEXT_PAGE_VARIABLE, autoText.getAutoTextType());
		assertEquals("Report Variable(a)", displayName);

		autoText = (AutoTextHandle) designHandle.findElement("sf2");
		displayName = autoText.getDisplayLabel(DesignElement.FULL_LABEL);

		assertEquals(DesignChoiceConstants.AUTO_TEXT_PAGE_VARIABLE, autoText.getAutoTextType());
		assertEquals("Page Variable(b)", displayName);
	}
}
