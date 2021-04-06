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

package org.eclipse.birt.report.model.util;

import junit.framework.TestCase;

/**
 * Tests the CssPropertyUtil class.
 */

public class CssPropertyUtilTest extends TestCase {

	/**
	 * Tests some kinds of URL input.
	 *
	 */

	public void testGetURL() {
		assertEquals(null, CssPropertyUtil.getURLValue(null));
		assertEquals("c:/test", CssPropertyUtil.getURLValue("URL( c:/test )")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals("c:/test", CssPropertyUtil.getURLValue("url( c:/test )")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals("c:/test", CssPropertyUtil.getURLValue("URL( \"c:/test\" )")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals("c:/test", CssPropertyUtil.getURLValue("URL( \'c:/test\' )")); //$NON-NLS-1$//$NON-NLS-2$
		assertEquals("c:/test", CssPropertyUtil.getURLValue("URL( \' c:/test   \' )")); //$NON-NLS-1$//$NON-NLS-2$
	}
}
