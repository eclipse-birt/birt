/*******************************************************************************
 * Copyright (c) 2017 Actuate Corporation.
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

package org.eclipse.birt.core.format;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test cases in format package
 */

public class AllFormatTests {

	/**
	 * @return the test
	 */

	public static Test suite() {
		TestSuite test = new TestSuite();

		test.addTestSuite(DateFormatterTest.class);
		test.addTestSuite(NumberFormatterTest.class);
		test.addTestSuite(StringFormatterTest.class);

		return test;
	}
}
