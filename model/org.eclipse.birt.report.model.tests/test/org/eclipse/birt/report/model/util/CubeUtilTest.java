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

import org.eclipse.birt.report.model.api.util.CubeUtil;

import junit.framework.TestCase;

/**
 * Tests for CubeUtil.
 */
public class CubeUtilTest extends TestCase {

	/**
	 * Tests spliLevelName method.
	 */
	public void testSplitLevelName() {
		String levelName = null;

		// input is null
		String[] results = CubeUtil.splitLevelName(levelName);
		assertEquals(2, results.length);
		assertNull(results[0]);
		assertNull(results[1]);

		// input is empty
		levelName = " "; //$NON-NLS-1$
		results = CubeUtil.splitLevelName(levelName);
		assertEquals(2, results.length);
		assertNull(results[0]);
		assertEquals(levelName, results[1]);

		// input is not empty and contains no '/'
		levelName = "testLevel"; //$NON-NLS-1$
		results = CubeUtil.splitLevelName(levelName);
		assertEquals(2, results.length);
		// assertEquals( "", results[0] ); //$NON-NLS-1$
		assertNull(results[0]);
		assertEquals(levelName, results[1]);

		// input contains one '/' and '/' is the first letter in the string
		levelName = "/testLevel"; //$NON-NLS-1$
		results = CubeUtil.splitLevelName(levelName);
		assertEquals(2, results.length);
		assertEquals("", results[0]); //$NON-NLS-1$
		assertEquals(levelName.substring(1), results[1]);

		// input contains one '/' and is not the first letter
		levelName = "testDimension/testLevel"; //$NON-NLS-1$
		results = CubeUtil.splitLevelName(levelName);
		assertEquals(2, results.length);
		assertEquals("testDimension", results[0]); //$NON-NLS-1$
		assertEquals("testLevel", results[1]); //$NON-NLS-1$

		// input contains more than one '/'
		levelName = "test/testDimension/testLevel"; //$NON-NLS-1$
		results = CubeUtil.splitLevelName(levelName);
		assertEquals(2, results.length);
		assertEquals("test/testDimension", results[0]); //$NON-NLS-1$
		assertEquals("testLevel", results[1]); //$NON-NLS-1$

	}

	/**
	 * Tests the getFullLevelName in CubeUtil.
	 */
	public void testGetFullLevelName() {
		String dimensionName = null;
		String levelName = null;

		// if dimension name is null return levelName
		assertEquals(levelName, CubeUtil.getFullLevelName(dimensionName, levelName));

		// if dimension name is empty, return levelName too
		dimensionName = " "; //$NON-NLS-1$
		levelName = "levelName"; //$NON-NLS-1$
		assertEquals(levelName, CubeUtil.getFullLevelName(dimensionName, levelName));

		// if level name is null, or empty, return null
		levelName = null;
		dimensionName = "dimensionName"; //$NON-NLS-1$
		assertNull(CubeUtil.getFullLevelName(dimensionName, levelName));
		levelName = " "; //$NON-NLS-1$
		assertNull(CubeUtil.getFullLevelName(dimensionName, levelName));

		// if dimension name and level name is not empty, then get full name
		levelName = "levelName"; //$NON-NLS-1$
		assertEquals("dimensionName/levelName", CubeUtil.getFullLevelName(dimensionName, levelName)); //$NON-NLS-1$

	}
}
