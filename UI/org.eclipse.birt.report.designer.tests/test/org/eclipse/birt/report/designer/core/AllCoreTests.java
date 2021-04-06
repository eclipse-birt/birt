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