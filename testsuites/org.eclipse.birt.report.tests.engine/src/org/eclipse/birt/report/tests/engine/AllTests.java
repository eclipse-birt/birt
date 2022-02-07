/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 ******************************************************************************/

package org.eclipse.birt.report.tests.engine;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.tests.engine.api.AllApiTests;

public class AllTests {

	public static Test suite() {
		TestSuite test = new TestSuite();
		test.addTest(AllApiTests.suite());
		return test;
	}

}
