/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.ui.preview;

import org.eclipse.birt.report.designer.ui.preview.parameter.AllParameterTests;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * All test cases.
 * 
 */

public class AllTests extends TestCase {

	/**
	 * All cases.
	 * 
	 * @throws Exception
	 */

	public void test() throws Exception {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(AllParameterTests.class);
	}
}
