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

package org.eclipse.birt.report.designer.internal.ui.util;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test suit for org.eclipse.birt.report.designer.internal.ui.util
 */

public class AllUIUtilTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.eclipse.birt.report.designer.internal.ui.util");
		// $JUnit-BEGIN$
		suite.addTestSuite(UIUtilUITest.class);
		suite.addTestSuite(UIUtilTest.class);
		// $JUnit-END$
		return suite;
	}
}
