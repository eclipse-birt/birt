/*************************************************************************************
 * Copyright (c) 2022 Remain Software.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Remain Software - Initial implementation.
 ************************************************************************************/
package org.eclipse.birt.report.context;

import org.eclipse.birt.report.exception.ViewerException;
import org.eclipse.birt.report.viewer.util.BaseTestCase;
import org.junit.Test;

/**
 *
 * Test the VBA.
 *
 */
public class ViewerAttributeBeanTest extends BaseTestCase {

	/**
	 * Extensions with invalid characters are not allowed.
	 *
	 * @throws ViewerException
	 */
	@Test
	public void testCheckExtensionAllowedForRPTDocument() throws ViewerException {
		ViewerAttributeBean.checkExtensionAllowedForRPTDocument("report");
		ViewerAttributeBean.checkExtensionAllowedForRPTDocument("report.pdf");
	}

	/**
	 * Extensions with invalid characters are not allowed.
	 *
	 * @throws ViewerException
	 */
	@Test
	public void testDisallowEmptyExtension() throws ViewerException {

		try {
			ViewerAttributeBean.checkExtensionAllowedForRPTDocument("report.");
		} catch (Exception e) {
			return;
		}

		fail("invalid extension accepted");
	}

	/**
	 * Extensions with invalid characters are not allowed.
	 *
	 * @throws ViewerException
	 */
	@Test
	public void testCheckExtensionAllowedForRPTDocument3() throws ViewerException {

		try {
			ViewerAttributeBean.checkExtensionAllowedForRPTDocument("./file.jsp/.");
		} catch (Exception e) {
			return;
		}

		fail("invalid extension accepted");
	}

	/**
	 * Extensions with invalid characters are not allowed.
	 *
	 * @throws ViewerException
	 */
	@Test
	public void testDocumentIsDirectory() throws ViewerException {

		try {
			ViewerAttributeBean.checkExtensionAllowedForRPTDocument("./file.jsp/");
		} catch (Exception e) {
			return;
		}
		fail("invalid extension accepted");
	}

	/**
	 * Extensions with invalid characters are not allowed.
	 *
	 * @throws ViewerException
	 */
	@Test
	public void testValidDirectoryAndFile() throws ViewerException {
		try {
			ViewerAttributeBean.checkExtensionAllowedForRPTDocument("./file/hello.jsp/.test/blok.pdf");
		} catch (Exception e) {
			fail("valid extension not accepted");
		}
	}
}
