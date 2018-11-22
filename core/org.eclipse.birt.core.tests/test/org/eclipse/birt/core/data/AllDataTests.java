/*******************************************************************************
 * Copyright (c) 2017 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.data;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests for data package
 */

public class AllDataTests
{

	/**
	 * @return the test
	 */

	public static Test suite( )
	{
		TestSuite test = new TestSuite( );

		test.addTestSuite( DataTypeUtilTest.class );
		test.addTestSuite( DateUtilTest.class );
		test.addTestSuite( DateUtilThreadTest.class );
		test.addTestSuite( ExpressionParserUtilityTest.class );
		test.addTestSuite( ExpressionUtilTest.class );
		// add all test classes here

		return test;
	}
}
