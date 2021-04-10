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

package org.eclipse.birt.report.designer.ui.views.attributes;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Class of test suite for view.attributes
 * 
 */
public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.eclipse.birt.report.designer.ui.views.attributes");
		// $JUnit-BEGIN$
		suite.addTestSuite(TabPageGeneratorTest.class);

		// $JUnit-END$
		return suite;
	}

}