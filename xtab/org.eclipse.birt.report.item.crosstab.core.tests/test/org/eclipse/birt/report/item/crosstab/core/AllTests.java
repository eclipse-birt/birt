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

package org.eclipse.birt.report.item.crosstab.core;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.item.crosstab.core.de.AllDeTests;
import org.eclipse.birt.report.item.crosstab.core.parser.AllParserTests;
import org.eclipse.birt.report.item.crosstab.core.re.AllReTests;
import org.eclipse.birt.report.item.crosstab.core.util.AllUtilTests;

/**
 * 
 * All tests.
 *
 */

public class AllTests {
	/**
	 * @return the test
	 */

	public static Test suite() {
		TestSuite test = new TestSuite();

		test.addTest(AllDeTests.suite());
		test.addTest(AllParserTests.suite());
		test.addTest(AllReTests.suite());
		test.addTest(AllUtilTests.suite());
		// add all test classes here

		return test;
	}
}
