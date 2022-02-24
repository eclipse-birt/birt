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
import org.eclipse.birt.chart.util.LiteralHelper;

public class LiteralHelperTest extends TestCase {

	String[] types = { "Numberic", "DateTime", "Text" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	/**
	 * Construct and initialize any objects that will be used in multiple tests.
	 * Currently Empty.
	 * 
	 */
	protected void setUp() throws Exception {
	}

	/**
	 * Collect and empty any objects that are used in multiple tests. Currently
	 * Empty.
	 * 
	 */
	protected void tearDown() throws Exception {
	}

	// Test whether toStringNameArray parses the List to the related String[].
	public void testStringParse() {
		String[] result = LiteralHelper.dataTypeSet.getNames();
		for (int i = 1; i < types.length; i++) {
			assertEquals(types[i], result[i]);
		}
	}
}
