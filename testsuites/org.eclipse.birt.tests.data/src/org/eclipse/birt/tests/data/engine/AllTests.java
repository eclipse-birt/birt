
package org.eclipse.birt.tests.data.engine;

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

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.tests.data.engine.api.MultiPassTest;
import org.eclipse.birt.tests.data.engine.api.MultiPass_FilterTest;
import org.eclipse.birt.tests.data.engine.api.MultiPass_SortTest;

/**
 * Test suit for data engine.
 */
public class AllTests {

	/**
	 * Run all test cases here
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.eclipse.birt.data.engine");

		suite.addTestSuite(MultiPass_FilterTest.class);
		// remove because of deprecated feature
		// suite.addTestSuite( MultiPass_NestedQueryTest.class );
		suite.addTestSuite(MultiPass_SortTest.class);
		suite.addTestSuite(MultiPassTest.class);

		return suite;
	}

}
