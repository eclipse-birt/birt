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

package org.eclipse.birt.report.model.util;

import junit.framework.TestCase;

/**
 * Test class for VersionUtil.
 */

public class VersionUtilTest extends TestCase {

	/**
	 * Tests <code>compareVersion(String, String)</code>.
	 */

	public void testParseVersion() {
		assertEquals(1000000, VersionUtil.parseVersion("1")); //$NON-NLS-1$
		assertEquals(1000000, VersionUtil.parseVersion("1.")); //$NON-NLS-1$
		assertEquals(11223344, VersionUtil.parseVersion("11.22.33.44")); //$NON-NLS-1$

		try {
			VersionUtil.parseVersion("a"); //$NON-NLS-1$
			fail();
		} catch (Exception e) {
			// Exception is expetced.
		}

		try {
			VersionUtil.parseVersion("1.a"); //$NON-NLS-1$
			fail();
		} catch (Exception e) {
			// Exception is expetced.
		}

		try {
			VersionUtil.parseVersion("1a"); //$NON-NLS-1$
			fail();
		} catch (Exception e) {
			// Exception is expetced.
		}

		try {
			VersionUtil.parseVersion("2.100.1"); //$NON-NLS-1$
			fail();
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}

}
