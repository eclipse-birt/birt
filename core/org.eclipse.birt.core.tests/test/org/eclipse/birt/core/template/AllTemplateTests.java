/*******************************************************************************
 * Copyright (c) 2017 Actuate Corporation.
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

package org.eclipse.birt.core.template;

import org.eclipse.birt.core.script.CoreJavaScriptWrapperTest;
import org.eclipse.birt.core.script.NativeDateTimeSpanTest;
import org.eclipse.birt.core.script.NativeFinanceTest;
import org.eclipse.birt.core.script.NativeJavaMapTest;
import org.eclipse.birt.core.script.NativeNamedListTest;
import org.eclipse.birt.core.script.ScriptContextTest;
import org.eclipse.birt.core.script.ScriptableParametersTest;
import org.eclipse.birt.core.script.bre.BirtCompTest;
import org.eclipse.birt.core.script.bre.BirtDateTimeTest;
import org.eclipse.birt.core.script.bre.BirtDurationTest;
import org.eclipse.birt.core.script.bre.BirtMathTest;
import org.eclipse.birt.core.script.bre.BirtStrTest;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests in template package
 */

public class AllTemplateTests {

	/**
	 * @return the test
	 */

	public static Test suite() {
		TestSuite test = new TestSuite();

		test.addTestSuite(CoreJavaScriptWrapperTest.class);
		test.addTestSuite(NativeDateTimeSpanTest.class);
		test.addTestSuite(NativeFinanceTest.class);
		test.addTestSuite(NativeJavaMapTest.class);
		test.addTestSuite(NativeNamedListTest.class);
		test.addTestSuite(ScriptableParametersTest.class);
		test.addTestSuite(ScriptContextTest.class);
		test.addTestSuite(BirtCompTest.class);
		test.addTestSuite(BirtDateTimeTest.class);
		test.addTestSuite(BirtDurationTest.class);
		test.addTestSuite(BirtMathTest.class);
		test.addTestSuite(BirtStrTest.class);
		// add all test classes here

		return test;
	}
}
