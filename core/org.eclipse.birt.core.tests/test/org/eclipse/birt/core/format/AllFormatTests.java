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

package org.eclipse.birt.core.format;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test cases in format package
 */

public class AllFormatTests
{

	/**
	 * @return the test
	 */

	public static Test suite( )
	{
		TestSuite test = new TestSuite( );

		test.addTestSuite( DateFormatterTest.class );
		test.addTestSuite( NumberFormatterTest.class );
		test.addTestSuite( StringFormatterTest.class );

		return test;
	}
}
