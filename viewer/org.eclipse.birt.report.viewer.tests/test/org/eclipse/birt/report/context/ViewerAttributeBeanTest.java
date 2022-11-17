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

import static org.junit.Assert.fail;

import org.eclipse.birt.report.exception.ViewerException;
import org.junit.Test;

/**
 *
 * Test the VBA.
 *
 */
public class ViewerAttributeBeanTest {


	/**
	 * Extensions with invalid characters are not allowed.
	 *
	 * @throws ViewerException
	 */
	@Test
	public void testCheckExtensionAllowedForRPTDocument() throws ViewerException {

		ViewerAttributeBean.checkExtensionAllowedForRPTDocument("report");
		ViewerAttributeBean.checkExtensionAllowedForRPTDocument("report.pdf");
		ViewerAttributeBean.checkExtensionAllowedForRPTDocument("report.");
		try {
			ViewerAttributeBean.checkExtensionAllowedForRPTDocument("report.pdf/");
		} catch (Exception e) {
			return;
		}

		fail("invalid extension accepted");
	}
}
