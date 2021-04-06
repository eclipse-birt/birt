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