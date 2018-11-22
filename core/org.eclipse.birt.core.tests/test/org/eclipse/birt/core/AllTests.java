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

package org.eclipse.birt.core;

import org.eclipse.birt.core.script.AllScriptTests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests cases run in the build script.
 */

public class AllTests
{

	/**
	 * @return test run in build script
	 */

	public static Test suite( )
	{
		TestSuite test = new TestSuite( );
		test.addTest( AllScriptTests.suite( ) );

		return test;
	}

}
