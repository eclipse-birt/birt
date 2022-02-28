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

package org.eclipse.birt.report.engine.util;

import java.io.File;

import junit.framework.TestCase;

/**
 * Test case
 *
 *
 */
public class FileUtilTest extends TestCase {

	public void testIsRelativePath() {
		if (File.separatorChar == '\\') {
			// Windows
			boolean b = FileUtil.isRelativePath("c:\\no\\no.txt");
			assertFalse(b);

			b = FileUtil.isRelativePath("\\no\\no.txt");
			assertTrue(b);

			b = FileUtil.isRelativePath("no\\no.txt");
			assertTrue(b);
		} else if (File.separatorChar == '/') {
			// Unix
			boolean b = FileUtil.isRelativePath("/no/no.txt");
			assertFalse(b);

			b = FileUtil.isRelativePath("no/no.txt");
			assertTrue(b);
		}
	}
}
