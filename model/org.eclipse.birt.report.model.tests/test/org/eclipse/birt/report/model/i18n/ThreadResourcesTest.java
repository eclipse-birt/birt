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
package org.eclipse.birt.report.model.i18n;

import com.ibm.icu.util.ULocale;

import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test for ThreadResources. The message files are named like
 * "Messages.properties", "Messages_xx.properties".
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testGetULocale()}</td>
 * <td>Get current thread's locale.</td>
 * <td>The locale should be the one set by the current thread.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetMessage()}</td>
 * <td>Given the key, get a localized message from the corresponding resource
 * bundle.</td>
 * <td>The name returned should be the same as defined in the corresponding
 * message files. The locale of the message is the one set by the caller's
 * thread.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetMessage2()}</td>
 * <td>Get a message that has placeholders. Number of the input parameters is
 * exactly the same as defined in Message file.</td>
 * <td>The placeholders in the message should be replaced by the input
 * parameters. The locale of the message is the one set by the caller's
 * thread.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Number of the input parameters is less than that defined in Message file.
 * Input be String[]{"a", "b"} for a pattern like "{0},{1},{2}"</td>
 * <td>Only the first two placeholders are filled in.</td>
 * </tr>
 * 
 * <tr>
 * <td></td>
 * <td>Number of the input parameters is more than that defined in Message
 * file.Input be String[]{"a", "b", "c" } for a pattern like "{0},{1}"</td>
 * <td>Only first two of the input String is filled in.</td>
 * </tr>
 * 
 * </table>
 */
public class ThreadResourcesTest extends BaseTestCase {

	// TODO: Needs to be auto-tested on multi-thread environment.

	/**
	 * Test getLocale().
	 */

	public void testGetULocale() {
		ThreadResources.setLocale(ULocale.ENGLISH);
		assertEquals(ULocale.ENGLISH, ThreadResources.getLocale());

		ThreadResources.setLocale(TEST_LOCALE);
		assertEquals(TEST_LOCALE, ThreadResources.getLocale());
	}

	/**
	 * Test getMessage(). The thread-locale should be set before any call to
	 * getMessage().
	 */

	public void testGetMessage() {
		ThreadResources.setLocale(ULocale.ENGLISH);

		String msg = ModelMessages.getMessage("Choices.colors.maroon"); //$NON-NLS-1$
		assertEquals("Maroon", msg); //$NON-NLS-1$

		msg = ModelMessages.getMessage("Element.ReportDesign"); //$NON-NLS-1$
		assertEquals("Report Design", msg); //$NON-NLS-1$

		ThreadResources.setLocale(TEST_LOCALE);
		msg = ModelMessages.getMessage("Element.ReportDesign"); //$NON-NLS-1$
		assertEquals("\u62a5\u8868", msg); //$NON-NLS-1$

	}

	/**
	 * Test getMessage() with parameters.
	 * <p>
	 * 1. Number of the input parameters is exactly the same as defined in Message
	 * file.
	 * <p>
	 * 2. Number of the input parameters is less than that defined in Message file.
	 * <p>
	 * 3. Number of the input parameters is more than that defined in Message file.
	 */

	public void testGetMessage2() {
		ThreadResources.setLocale(ULocale.ENGLISH);

		String msg = ModelMessages.getMessage("Error.Msg001", new String[] { "Element", "NameSpace" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		assertEquals("Element not found in NameSpace.", msg); //$NON-NLS-1$

		msg = ModelMessages.getMessage("Error.Msg001", new String[] {}); //$NON-NLS-1$

		assertEquals("{0} not found in {1}.", msg); //$NON-NLS-1$

		msg = ModelMessages.getMessage("Error.Msg001", new String[] { "Element", "NameSpace", "Extra_Param" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		assertEquals("Element not found in NameSpace.", msg); //$NON-NLS-1$
	}

}
