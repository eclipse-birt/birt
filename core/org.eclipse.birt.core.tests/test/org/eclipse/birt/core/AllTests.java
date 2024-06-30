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

package org.eclipse.birt.core;

import org.eclipse.birt.core.script.AllScriptTests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests cases run in the build script.
 */

public class AllTests {

	/**
	 * @return test run in build script
	 */

	public static Test suite() {
		TestSuite test = new TestSuite();
		test.addTest(AllScriptTests.suite());

		return test;
	}

}
