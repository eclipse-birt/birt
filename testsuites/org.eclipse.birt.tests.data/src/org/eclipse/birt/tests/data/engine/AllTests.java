
package org.eclipse.birt.tests.data.engine;

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

import junit.framework.Test;
import junit.framework.TestSuite;


import org.eclipse.birt.tests.data.engine.api.DateUtilTest;
import org.eclipse.birt.tests.data.engine.api.FeaturesTest;
import org.eclipse.birt.tests.data.engine.api.MultiPassTest;
import org.eclipse.birt.tests.data.engine.api.MultiPass_FilterTest;
import org.eclipse.birt.tests.data.engine.api.MultiPass_NestedQueryTest;
import org.eclipse.birt.tests.data.engine.api.MultiPass_SortTest;

/**
 * Test suit for data engine.
 */
public class AllTests
{

	/**
	 * @return
	 */
	public static Test suite( )
	{
		TestSuite suite = new TestSuite(
				"Test for org.eclipse.birt.data.engine" );

		/* Regression test cases */
		
		 // API
		
		 suite.addTestSuite( FeaturesTest.class );
		 suite.addTestSuite( MultiPass_FilterTest.class );
		 suite.addTestSuite( MultiPass_NestedQueryTest.class );
		 suite.addTestSuite( MultiPass_SortTest.class );
		 suite.addTestSuite( MultiPassTest.class );
		 suite.addTestSuite( DateUtilTest.class );
		
		

		return suite;
	}

}
