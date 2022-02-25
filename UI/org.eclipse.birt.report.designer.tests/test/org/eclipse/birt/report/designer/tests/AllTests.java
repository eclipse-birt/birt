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

package org.eclipse.birt.report.designer.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 */

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.eclipse.birt.report.designer.tests");
		// $JUnit-BEGIN$
		suite.addTest(org.eclipse.birt.report.designer.core.AllCoreTests.suite());
		suite.addTest(org.eclipse.birt.report.designer.ui.AllUITests.suite());
		suite.addTest(org.eclipse.birt.report.designer.util.AllUtilTests.suite());
		suite.addTest(org.eclipse.birt.report.designer.internal.ui.util.AllUIUtilTests.suite());
		// $JUnit-END$
		return suite;
	}
}
