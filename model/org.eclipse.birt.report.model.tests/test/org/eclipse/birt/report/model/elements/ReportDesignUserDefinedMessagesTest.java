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

package org.eclipse.birt.report.model.elements;

import java.util.List;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ErrorDetail;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.parser.DesignParserException;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Test to get a user defined message.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse: *
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testGetMessage()}</td>
 * <td>ULocale is ENGLISH, translated text is defined in translations, keyed by
 * "en".</td>
 * <td>Translated text for "en" is returned.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>locale is CHINA, translated text is defined in translations, keyed by
 * "zh_CN".</td>
 * <td>Translated text for "zh_CN" is returned.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>locale is FRANCE, translated text is not defined.</td>
 * <td>return null.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetTranslations()}</td>
 * <td>6 translations is defined in design file.</td>
 * <td>list returned contains 6 translations.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>design file defined no translations.</td>
 * <td>return null.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testFindTranslation()}</td>
 * <td>Find a translation, locale is null.</td>
 * <td>return should be the translation without a locale.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Find a translation, locale is "zh_CN".</td>
 * <td>return should be the translation with the loclae "zh_CN".</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Find a translation, the resource key is not defined.</td>
 * <td>return should be null.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetTranslationResourceKeys()}</td>
 * <td>Get an string array containing all the resource keys defined.</td>
 * <td>The array returned containing all the resource keyes defined for the
 * design.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testTranslationSemanticCheck()}</td>
 * <td>All the locale names must be unique within a resource, and each
 * translation has a resource-key.</td>
 * <td>Errors are found.</td>
 * </tr>
 * <p>
 */

public class ReportDesignUserDefinedMessagesTest extends BaseTestCase {

	/**
	 * test getMessage().
	 * <p>
	 * 1. locale is ENGLISH, translated text is defined in translations, keyed by
	 * "en".
	 * <p>
	 * 2. locale is CHINA, translated text is defined in translations, keyed by
	 * "zh_CN"
	 * <p>
	 * 3. locale is FRANCE, translated text is not defined.
	 * <p>
	 * 
	 * @throws DesignFileException
	 */
	public void testGetMessage() throws DesignFileException {
		// 1. English
		openDesign("ReportDesignUserDefinedMessagesTest.xml", ULocale.ENGLISH); //$NON-NLS-1$

		// get the title of the design
		String titleID = design.getStringProperty(design, ReportDesign.TITLE_ID_PROP);
		assertEquals("ResourceKey.ReportDesign.Title", titleID); //$NON-NLS-1$

		String title = design.getMessage(titleID);
		assertEquals("EN: My Sample design.", title); //$NON-NLS-1$

		String descriptionID = design.getStringProperty(design, ReportDesign.DESCRIPTION_ID_PROP);
		String description = design.getMessage(descriptionID);
		assertEquals("Sample report(en)", description); //$NON-NLS-1$

		// 2. China
		openDesign("ReportDesignUserDefinedMessagesTest.xml", TEST_LOCALE); //$NON-NLS-1$

		title = design.getMessage(titleID);
		assertEquals("ja_JP:\u7B80\u5355\u62A5\u8868.", title); //$NON-NLS-1$

		description = design.getMessage(descriptionID);
		assertEquals("\u5B9E\u4F8B\u62A5\u8868", description); //$NON-NLS-1$

		// 3. Default.
		// translation for zh_CN not defined.
		openDesign("ReportDesignUserDefinedMessagesTest.xml", TEST_LOCALE); //$NON-NLS-1$

		title = design.getMessage("ResourceKey.testKey1"); //$NON-NLS-1$
		assertEquals("default translation1", title); //$NON-NLS-1$

		designHandle.setIncludeResource("message"); //$NON-NLS-1$

		// en_US
		ModuleOption options = new ModuleOption();
		design.setOptions(options);

		options.setLocale(ULocale.US);

		// In default && en_US
		assertEquals("en_US for A", designHandle.getMessage("A")); //$NON-NLS-1$//$NON-NLS-2$

		// Only in en_US
		assertEquals("en_US for D", designHandle.getMessage("D")); //$NON-NLS-1$//$NON-NLS-2$

		// Only in en
		assertEquals("en for C", designHandle.getMessage("C")); //$NON-NLS-1$ //$NON-NLS-2$

		// Only in default
		assertEquals("default for B", designHandle.getMessage("B")); //$NON-NLS-1$ //$NON-NLS-2$

		// not found
		assertNull(designHandle.getMessage("non-exsit-key")); //$NON-NLS-1$
	}

	/**
	 * Get a message.
	 * 
	 * @throws DesignFileException
	 */

	public void testGetMessage2() throws DesignFileException {
		openDesign("ReportDesignUserDefinedMessagesTest.xml", ULocale.ENGLISH); //$NON-NLS-1$
		String text = design.getMessage("ResourceKey.testKey2", new ULocale("en", "AU")); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		assertEquals("text en_AU", text); //$NON-NLS-1$

		text = design.getMessage("ResourceKey.testKey2", new ULocale("en", "US")); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		assertEquals("text en_US", text); //$NON-NLS-1$

		text = design.getMessage("ResourceKey.testKey2", new ULocale("en", "GB")); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		assertEquals("text en", text); //$NON-NLS-1$

		text = design.getMessage("ResourceKey.testKey2", new ULocale("fr", "CA")); //$NON-NLS-1$//$NON-NLS-2$//$NON-NLS-3$
		assertEquals("text default", text); //$NON-NLS-1$

		assertNull(design.getMessage("")); //$NON-NLS-1$
		assertNull(design.getMessage(null));
	}

	/**
	 * Test getMessageKeys().
	 * 
	 * @throws DesignFileException
	 */

	public void testGetMessageKeys() throws DesignFileException {
		openDesign("ReportDesignUserDefinedMessagesTest2.xml", ULocale.ENGLISH); //$NON-NLS-1$
		designHandle.setIncludeResource("message"); //$NON-NLS-1$

		ModuleOption options = new ModuleOption();
		design.setOptions(options);

		options.setLocale(ULocale.US);

		List keys = design.getMessageKeys();
		assertEquals(6, keys.size());

		assertTrue(keys.contains("ResourceKey.ReportDesign.Title")); //$NON-NLS-1$
		assertTrue(keys.contains("ResourceKey.ReportDesign.Description")); //$NON-NLS-1$
		assertTrue(keys.contains("A")); //$NON-NLS-1$
		assertTrue(keys.contains("B")); //$NON-NLS-1$
		assertTrue(keys.contains("C")); //$NON-NLS-1$
		assertTrue(keys.contains("D")); //$NON-NLS-1$

		designHandle.setIncludeResource("none-exsit"); //$NON-NLS-1$
		keys = designHandle.getMessageKeys();
		assertEquals(2, keys.size());

	}

	/**
	 * Test semantic check.
	 * 
	 * @throws Exception
	 * 
	 */
	public void testTranslationSemanticCheck() throws Exception {
		try {
			openDesign("ReportDesignUserDefinedMessagesTest3.xml");//$NON-NLS-1$
			fail();
		} catch (DesignFileException e) {

			List list = e.getErrorList();
			assertEquals(2, list.size());

			ErrorDetail detail = (ErrorDetail) list.get(0);

			assertEquals(DesignParserException.DESIGN_EXCEPTION_DUPLICATE_TRANSLATION_LOCALE, detail.getErrorCode());

			detail = (ErrorDetail) list.get(1);

			assertEquals(DesignParserException.DESIGN_EXCEPTION_MESSAGE_KEY_REQUIRED, detail.getErrorCode());
		}

	}
}