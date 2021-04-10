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

import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Test cases for custom color handle.
 */

public class CustomColorHandleTest extends BaseTestCase {

	static final String INPUT_FILE_NAME = "CustomColorHandleTest.xml"; //$NON-NLS-1$
	static final String OUTPUT_FILE_NAME = "CustomColorHandleTest_out.xml"; //$NON-NLS-1$
	static final String GOLDEN_FILE_NAME = "CustomColorHandleTest_golden.xml"; //$NON-NLS-1$
	static final String SEMANTIC_CHECK_FILE_NAME = "CustomColorHandleTest1.xml"; //$NON-NLS-1$

	/*
	 * @see BaseTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		openDesign(INPUT_FILE_NAME, ULocale.ENGLISH);
	}

	/**
	 * Test internal logic of getRGB(). The result of it based on color value.
	 * 
	 * @throws Exception
	 */

	public void testGetRGB() throws Exception {
		Iterator iter = designHandle.customColorsIterator();

		CustomColorHandle handle1 = (CustomColorHandle) iter.next();
		CustomColorHandle handle2 = (CustomColorHandle) iter.next();
		CustomColorHandle handle3 = (CustomColorHandle) iter.next();

		assertEquals("custom blue", handle1.getName()); //$NON-NLS-1$

		assertEquals("#0000FE", handle1.getColor()); //$NON-NLS-1$
		assertEquals(254, handle1.getRGB());

		// 1. css color
		handle1.setColor("red"); //$NON-NLS-1$ -*
		assertEquals(16711680, handle1.getRGB());

		// color name with UpperCases.

		handle1.setColor("GREEN"); //$NON-NLS-1$
		assertEquals(32768, handle1.getRGB());

		handle1.setColor("GrEEN"); //$NON-NLS-1$
		assertEquals(32768, handle1.getRGB());

		// 2. css relative

		handle1.setColor("RGB( 100.0%, 0.0%, 0.0%)"); //$NON-NLS-1$
		assertEquals(16711680, handle1.getRGB());

		// over 100.0%, clip to 100.0%
		handle1.setColor("RGB( 200.%, 0.0%, 0.0%)"); //$NON-NLS-1$
		assertEquals(-1, handle1.getRGB());

		// "200." not valid
		handle1.setColor("RGB( 200.%, 0.0%, 0.0%)"); //$NON-NLS-1$
		assertEquals(-1, handle1.getRGB());

		// 3. css absolute
		handle1.setColor("RGB( 255, 0, 0)"); //$NON-NLS-1$
		assertEquals(16711680, handle1.getRGB());

		handle1.setColor("RGB( 255, 0, 0)"); //$NON-NLS-1$
		assertEquals(16711680, handle1.getRGB());

		// over 255, clip to 255
		handle1.setColor("RGB( 400, 0, 0)"); //$NON-NLS-1$
		assertEquals(16711680, handle1.getRGB());

		// not valid
		handle1.setColor("RGB( 255, , 0)"); //$NON-NLS-1$
		assertEquals(-1, handle1.getRGB());

		// 4. HTML-style
		handle1.setColor("#FF0000"); //$NON-NLS-1$
		assertEquals(16711680, handle1.getRGB());

		// maximum value an integer can represent 2E(31) - 1
		handle1.setColor("#7FFFFFFF"); //$NON-NLS-1$
		assertEquals(16777215, handle1.getRGB());

		// over flow, integer can not represent.
		handle1.setColor("#80000000"); //$NON-NLS-1$
		assertEquals(-1, handle1.getRGB());

		// 3-digit representation, representing same value as #FF0000
		handle1.setColor("#F00"); //$NON-NLS-1$
		assertEquals(16711680, handle1.getRGB());

		// not 6-digit or 3-digit representations. keep the value.
		handle1.setColor("#F0"); //$NON-NLS-1$
		assertEquals(240, handle1.getRGB());

		// 5. Java-style
		handle1.setColor("0xFF0000"); //$NON-NLS-1$
		assertEquals(16711680, handle1.getRGB());

		// maximum value an integer can represent 2E(31) - 1
		// over 0xFFFFFF, clipped to 0xFFFFFF

		handle1.setColor("0x7FFFFFFF"); //$NON-NLS-1$
		assertEquals(16777215, handle1.getRGB());

		// over flow, integer can not represent.
		handle1.setColor("0x80000000"); //$NON-NLS-1$
		assertEquals(-1, handle1.getRGB());

		// not 6-digit or 3-digit representations. keep the value.
		handle1.setColor("0xF0"); //$NON-NLS-1$
		assertEquals(240, handle1.getRGB());

		// decimal format
		handle1.setColor("123456"); //$NON-NLS-1$
		assertEquals(123456, handle1.getRGB());

		// maximum value an integer can represent 2E(31) - 1
		// over 0xFFFFFF, clipped to 0xFFFFFF
		handle1.setColor("2147483647"); //$NON-NLS-1$
		assertEquals(Integer.decode("#FFFFFF").intValue(), handle1.getRGB()); //$NON-NLS-1$

		// over flow, integer can not represent.
		handle1.setColor("2147483648"); //$NON-NLS-1$
		assertEquals(-1, handle1.getRGB());

		assertEquals("custom red", handle2.getName()); //$NON-NLS-1$

		// 6. css color
		assertEquals(16711680, handle3.getRGB());

	}

	/**
	 * Tests get/set Color method.
	 * 
	 * @throws Exception
	 */
	public void testGetSet() throws Exception {
		Iterator iter = designHandle.customColorsIterator();

		CustomColorHandle handle1 = (CustomColorHandle) iter.next();

		assertEquals("custom blue", handle1.getName()); //$NON-NLS-1$
		assertEquals("#0000FE", handle1.getColor()); //$NON-NLS-1$
		assertEquals(254, handle1.getRGB());
		assertEquals("Color1", handle1.getDisplayName()); //$NON-NLS-1$
		assertEquals("custom-color1", handle1.getDisplayNameID()); //$NON-NLS-1$

		handle1.setName("cusblue"); //$NON-NLS-1$
		handle1.setColor("#000FD"); //$NON-NLS-1$
		handle1.setDisplayName("Color1_DisplayName"); //$NON-NLS-1$
		handle1.setDisplayNameID("Color1_DisplayName_ID"); //$NON-NLS-1$

		assertEquals("cusblue", handle1.getName()); //$NON-NLS-1$
		assertEquals("#000FD", handle1.getColor()); //$NON-NLS-1$
		assertEquals(253, handle1.getRGB());
		assertEquals("Color1_DisplayName", handle1.getDisplayName()); //$NON-NLS-1$
		assertEquals("Color1_DisplayName_ID", handle1.getDisplayNameID()); //$NON-NLS-1$

	}

	/**
	 * Tests save color handle method
	 * 
	 * @throws Exception
	 */
	public void testWriter() throws Exception {
		Iterator iter = designHandle.customColorsIterator();
		CustomColorHandle handle1 = (CustomColorHandle) iter.next();
		CustomColorHandle handle2 = (CustomColorHandle) iter.next();

		handle1.setName("cusblue"); //$NON-NLS-1$
		handle1.setColor("#000FD"); //$NON-NLS-1$
		handle1.setDisplayName("Color1_DisplayName"); //$NON-NLS-1$
		handle1.setDisplayNameID("Color1_DisplayName_ID"); //$NON-NLS-1$

		handle2.setColor("green"); //$NON-NLS-1$

		save();
		assertTrue(compareFile(GOLDEN_FILE_NAME));
	}

	/**
	 * @throws DesignFileException
	 */
	public void testSemanticErrors() throws DesignFileException {
		openDesign(SEMANTIC_CHECK_FILE_NAME, ULocale.ENGLISH);
		List errors = design.getErrorList();

		assertEquals(2, errors.size());
	}
}