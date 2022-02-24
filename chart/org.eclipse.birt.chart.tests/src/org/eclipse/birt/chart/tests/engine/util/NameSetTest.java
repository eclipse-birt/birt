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

package org.eclipse.birt.chart.tests.engine.util;

import junit.framework.TestCase;
import org.eclipse.birt.chart.util.NameSet;

public class NameSetTest extends TestCase {

	String[] set = { "Name 1", "Name 2", "Name 3" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	NameSet nameSet;

	/**
	 * Construct and initialize any objects that will be used in multiple tests.
	 */
	protected void setUp() throws Exception {
		super.setUp();
		nameSet = new NameSet("-", "-", set); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Construct and initialize any objects that will be used in multiple tests.
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		nameSet = null;
	}

	/**
	 * Test the original name array.
	 *
	 */
	public void testGetNames() {
		assertEquals(set, nameSet.getNames());
	}

	/**
	 * Test the display name array.
	 *
	 */
	public void testGetDisplayNames() {
		String[] a = nameSet.getDisplayNames();
		for (int i = 0; i < 3; i++) {
			assertEquals("!-" + set[i] + "-!", a[i]); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * Test the returned index by given name.
	 *
	 */
	public void testGetNameIndex() {
		assertEquals(0, nameSet.getNameIndex("Name 1")); //$NON-NLS-1$
		assertEquals(2, nameSet.getNameIndex("Name 3")); //$NON-NLS-1$
		assertEquals(-1, nameSet.getNameIndex("Not Found")); //$NON-NLS-1$
	}

	/**
	 * Test the returned index by given name.
	 *
	 */
	public void testGetSafeNameIndex() {
		assertEquals(0, nameSet.getSafeNameIndex("Name 1")); //$NON-NLS-1$
		assertEquals(2, nameSet.getSafeNameIndex("Name 3")); //$NON-NLS-1$
		assertEquals(0, nameSet.getSafeNameIndex("Not Found")); //$NON-NLS-1$
	}

	/**
	 * Test the display name by the original name.
	 *
	 */
	public void testGetDisplayNameByName() {
		assertEquals("!-Name 1-!", nameSet.getDisplayNameByName("Name 1")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(nameSet.getDisplayNameByName("Not Found")); //$NON-NLS-1$
	}

	/**
	 * Test the original name by the display name.
	 *
	 */
	public void testGetNameByDisplayName() {
		assertEquals("Name 1", nameSet.getNameByDisplayName("!-Name 1-!")); //$NON-NLS-1$ //$NON-NLS-2$
		assertNull(nameSet.getNameByDisplayName("Not Found")); //$NON-NLS-1$
	}

}
