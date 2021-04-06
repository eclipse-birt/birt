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

package org.eclipse.birt.report.model.css;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests cases in css package.
 */

public class AllCssTests {

	/**
	 * @return the test
	 */

	public static Test suite() {
		TestSuite test = new TestSuite();

		// add all test classes here
		test.addTestSuite(CssParserTest.class);
		test.addTestSuite(ImportCssTest.class);
		test.addTestSuite(StyleSheetLoaderTest.class);

		return test;
	}
}
