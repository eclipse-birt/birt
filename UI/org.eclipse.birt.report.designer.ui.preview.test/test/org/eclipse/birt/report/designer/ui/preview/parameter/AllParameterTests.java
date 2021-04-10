/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.preview.parameter;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests all cases in parameter package.
 * 
 */

public class AllParameterTests extends TestCase {

	/**
	 * Tests all.
	 * 
	 * @throws Exception
	 */
	public void test() throws Exception {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(ParameterCreationTest.class);
		suite.addTestSuite(ParameterTest.class);
	}
}
