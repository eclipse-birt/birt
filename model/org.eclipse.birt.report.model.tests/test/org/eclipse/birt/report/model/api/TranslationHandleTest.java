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

import org.eclipse.birt.report.model.api.command.CustomMsgException;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test case for TranslationHandle.
 * 
 */
public class TranslationHandleTest extends BaseTestCase {

	private final static String INPUT_FILE = "TranslationHandleTest.xml"; //$NON-NLS-1$
	private final static String GOLDEN_FILE = "TranslationHandleTest.golden.xml"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		openDesign(INPUT_FILE);
	}

	/**
	 * 
	 * @throws CustomMsgException
	 */
	public void testGetSet() throws CustomMsgException {
		List translations = designHandle.getTranslations();
		assertTrue(translations.size() == 4);

		TranslationHandle transHandle = (TranslationHandle) translations.get(0);
		assertEquals("text default", transHandle.getText()); //$NON-NLS-1$
		assertEquals(null, transHandle.getLocale());
		assertEquals("ResourceKey.testKey1", transHandle.getResourceKey()); //$NON-NLS-1$

		try {
			// "en" already exsits.
			transHandle.setLocale("en"); //$NON-NLS-1$
			fail();
		} catch (CustomMsgException e) {
			assertEquals(CustomMsgException.DESIGN_EXCEPTION_DUPLICATE_LOCALE, e.getErrorCode());
		}

		transHandle.setLocale("en_AF"); //$NON-NLS-1$
		assertEquals("en_AF", transHandle.getLocale()); //$NON-NLS-1$

		transHandle.setText("text for AF"); //$NON-NLS-1$
		assertEquals("text for AF", transHandle.getText()); //$NON-NLS-1$

		transHandle = (TranslationHandle) translations.get(1);
		assertEquals("text en", transHandle.getText()); //$NON-NLS-1$
		assertEquals("en", transHandle.getLocale()); //$NON-NLS-1$
		assertEquals("ResourceKey.testKey1", transHandle.getResourceKey()); //$NON-NLS-1$

	}

	/**
	 * 
	 * 
	 * @throws Exception
	 */
	public void testWriter() throws Exception {
		// 1. change text and locale.

		List translations = designHandle.getTranslations();
		TranslationHandle transHandle = (TranslationHandle) translations.get(0);
		transHandle.setLocale("en_AF"); //$NON-NLS-1$
		transHandle.setText("text for AF"); //$NON-NLS-1$

		// 2. add a translation

		designHandle.addTranslation("ResourceKey.testKey2", "en", "ABC"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		designHandle.addTranslation("ResourceKey.testKey2", null, "DEFAULT"); //$NON-NLS-1$ //$NON-NLS-2$

		save();
		assertTrue(compareFile(GOLDEN_FILE));
	}

}
