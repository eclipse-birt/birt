/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b>
 * <p>
 * getRelativePath error when base is root.
 * <p>
 * example: URIUtil.getRelativePath( "c:\\", "c:\\test.library" ) will return
 * "../test.library", it should return "test.library" or "./test.library". if
 * base is not root, the result is correct, for example,
 * URIUtil.getRelativePath( "c:\\a\\", "c:\\a\\test.library" ) return
 * "test.library".
 * <p>
 * <b>Test description:</b>
 * <p>
 * Test the example in description
 * <p>
 */
public class Regression_160808 extends BaseTestCase {

	public void test_regression_160808() {
		assertEquals("test.library", URIUtil.getRelativePath("/c:/", //$NON-NLS-1$ //$NON-NLS-2$
				"/c:/test.library")); //$NON-NLS-1$
	}
}
