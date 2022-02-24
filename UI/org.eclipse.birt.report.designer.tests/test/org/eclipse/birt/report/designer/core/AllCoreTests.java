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

package org.eclipse.birt.report.designer.core;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.designer.core.runtime.ErrorStatusTest;

/**
 * The test suite for core
 */

public class AllCoreTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.eclipse.birt.report.designer.core");
		// $JUnit-BEGIN$
		suite.addTestSuite(BaseTest.class);
		suite.addTestSuite(ErrorStatusTest.class);
		suite.addTest(org.eclipse.birt.report.designer.core.model.schematic.AllTests.suite());
		suite.addTest(org.eclipse.birt.report.designer.core.commands.AllCommandTests.suite());
		suite.addTest(org.eclipse.birt.report.designer.nls.AllTests.suite());
		// $JUnit-END$
		return suite;
	}
}
