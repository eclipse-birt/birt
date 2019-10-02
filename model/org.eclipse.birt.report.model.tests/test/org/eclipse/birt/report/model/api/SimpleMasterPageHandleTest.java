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

import java.util.List;

import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.report.model.elements.SimpleMasterPage;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test SimpleMasterPage related classes, including
 * <code>SimpleMasterPage</code>,<code>SimpleMasterPageState</code>&
 * <code>SimpleMasterPageHandle</code>. <p> <table border="1" cellpadding="2"
 * cellspacing="2" style="border-collapse: collapse" bordercolor="#111111"> <th
 * width="20%">Method</th> <th width="40%">Test Case</th> <th
 * width="40%">Expected</th>
 * 
 * <tr> <td>testProperties</td> <td>Test all additional propertis comparing with
 * MasterPage on a simple master page.</td> <td>Parse the design file, get
 * property values, they should be identical to what stored in the file. Then,
 * make some changes to these properties, get them again, they should be
 * identical to those after the modification.</td> </tr>
 * 
 * <tr> <td>testSlots</td> <td>Test the two slots of simple master page</td>
 * <td>Get the element count of page header and page footer, they should be the
 * same as how many elements defined in the design file; then check the exact
 * element in the slot, they should be identical to what is defined in the slot.
 * </td> </tr>
 * 
 * <tr> <td>testSemanticErros</td> <td>Create another design file contains some
 * errors on simple master page </td> <td>Parse the design file, check the
 * syntax error count, they should be equals to what are designed; then check
 * each syntax error code, they should be the same as what are design too.</td>
 * </tr>
 * 
 * <tr> <td>testWriteSimpleMasterPage</td> <td>Open a design file, make some
 * changes, then save it back to an output file</td> <td>Compare the output file
 * with a golden file, they should be identical</td> </tr>
 * 
 * </table>
 * 
 */
public class SimpleMasterPageHandleTest extends BaseTestCase {

	private final String INPUT_FILE_NAME = "SimpleMasterPageHandleTest.xml"; //$NON-NLS-1$
	private final String GOLDEN_FILE_NAME = "SimpleMasterPageHandleTest_golden.xml"; //$NON-NLS-1$
	private final String ERROR_INPUT_FILE_NAME = "SimpleMasterPageHandleTest_1.xml"; //$NON-NLS-1$

	SimpleMasterPageHandle mHandle = null;
	SimpleMasterPage page = null;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		openDesign(INPUT_FILE_NAME);
	}

	/**
	 * Test all the properties of a simple master page.
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */
	public void testProperties() throws Exception {
		mHandle = (SimpleMasterPageHandle) designHandle
				.findMasterPage("Page1"); //$NON-NLS-1$
		assertNotNull(mHandle);
		assertTrue(mHandle.showHeaderOnFirst());
		assertFalse(mHandle.showFooterOnLast());
		assertTrue(mHandle.isFloatingFooter());

		mHandle.setShowHeaderOnFirst(false);
		mHandle.setShowFooterOnLast(true);
		mHandle.setFloatingFooter(true);
		assertFalse(mHandle.showHeaderOnFirst());
		assertTrue(mHandle.showFooterOnLast());
		assertTrue(mHandle.isFloatingFooter());

		String expectedDisplayValue = new NumberFormatter(mHandle.getModule().getLocale()).format(0.5) + "in";
		assertEquals(expectedDisplayValue, mHandle.getHeaderHeight().getDisplayValue());
		assertEquals("in", mHandle.getHeaderHeight().getDefaultUnit());
		assertEquals(expectedDisplayValue, mHandle.getFooterHeight().getDisplayValue());
		assertEquals("in", mHandle.getFooterHeight().getDefaultUnit());
	}

	/**
	 * Test all the page header and footer slot in a simple master page.
	 * 
	 * @throws Exception
	 *             if the test fails.
	 */
	public void testSlots() throws Exception {
		mHandle = (SimpleMasterPageHandle) designHandle
				.findMasterPage("Page1"); //$NON-NLS-1$
		SlotHandle slot = mHandle.getPageHeader();
		assertEquals(1, slot.getCount());
		assertEquals("text1", slot.get(0).getName()); //$NON-NLS-1$
		slot = mHandle.getPageFooter();
		assertEquals(1, slot.getCount());
		assertEquals("free-form1", slot.get(0).getName()); //$NON-NLS-1$

	}

	/**
	 * Parse an input xml file with some error on simple master page to see if
	 * these semantic errors are reported correctly.
	 * 
	 * @throws Exception
	 *             if test fails.
	 */
	public void testSemanticErrors() throws Exception {
		try {
			openDesign(ERROR_INPUT_FILE_NAME);
		} catch (DesignFileException ex) {
			List<ErrorDetail> list = ex.getErrorList();
			assertEquals(2, list.size());
			assertEquals("Error.ContentException.SLOT_IS_FULL", list.get(0).getErrorCode()); //$NON-NLS-1$
			assertEquals("Error.ContentException.SLOT_IS_FULL", list.get(1).getErrorCode()); //$NON-NLS-1$
		}
	}

	/**
	 * Open a design file which contains one or more simple master pages, make
	 * some modifications, then save it and compare it with another golden file.
	 * 
	 * @throws Exception
	 *             if test fails.
	 */
	public void testWriterSimpleMasterPage() throws Exception {
		mHandle = (SimpleMasterPageHandle) designHandle
				.findMasterPage("Page1"); //$NON-NLS-1$
		mHandle.setShowFooterOnLast(true);
		mHandle.setShowHeaderOnFirst(true);
		mHandle.setFloatingFooter(false);

		mHandle.getPageFooter().drop(0);

		save();
		assertTrue(compareFile(GOLDEN_FILE_NAME));
	}
}
