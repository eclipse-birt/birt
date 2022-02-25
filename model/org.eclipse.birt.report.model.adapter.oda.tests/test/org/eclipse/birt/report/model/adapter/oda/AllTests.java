/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.model.adapter.oda;

import org.eclipse.birt.report.model.adapter.oda.api.AllApiTests;
import org.eclipse.birt.report.model.adapter.oda.util.AllUtilTests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests cases run in the build script.
 */

public class AllTests extends TestCase {

	/**
	 * @return test run in build script
	 */
	public static Test suite() {
		TestSuite test = new TestSuite();

		// add all package tests here
		test.addTest(AllUtilTests.suite());
		test.addTest(AllApiTests.suite());

		return test;
	}

}
