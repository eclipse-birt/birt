/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.util;

import junit.framework.Test;
import junit.framework.TestSuite;

/*
 * Class of test suite for "util"
 *  
 */

public class AllUtilTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.eclipse.birt.report.designer.util");
		// $JUnit-BEGIN$
		suite.addTestSuite(MetricUtilityTest.class);
		suite.addTestSuite(FontManagerTest.class);
		suite.addTestSuite(ColorManagerTest.class);
		suite.addTestSuite(FixTableLayoutCalculatorTest.class);
		suite.addTestSuite(DEUtilTest.class);
		suite.addTestSuite(ImageManagerTest.class);
		suite.addTestSuite(TableBorderCollisionArbiterTest.class);
		// $JUnit-END$
		return suite;
	}
}
