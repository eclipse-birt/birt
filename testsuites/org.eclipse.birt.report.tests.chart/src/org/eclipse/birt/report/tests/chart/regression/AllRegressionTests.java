/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.report.tests.chart.regression;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests cases in activity package.
 */

public class AllRegressionTests {

	/**
	 * @return the test
	 */

	public static Test suite() {
		TestSuite suite = new TestSuite();

		// add all test classes here
		suite.addTestSuite(Regression_101039.class);
		suite.addTestSuite(Regression_101855.class);
		suite.addTestSuite(Regression_103787.class);
		suite.addTestSuite(Regression_104472.class);
		suite.addTestSuite(Regression_104627.class);
		suite.addTestSuite(Regression_106126.class);
		suite.addTestSuite(Regression_109641.class);
		suite.addTestSuite(Regression_113536.class);
		suite.addTestSuite(Regression_119411.class);
		suite.addTestSuite(Regression_119805.class);
		suite.addTestSuite(Regression_120557.class);
		suite.addTestSuite(Regression_120919.class);
		// To Do: chart script
//		suite.addTestSuite( Regression_121383.class );

//		suite.addTestSuite( Regression_121954.class );
		suite.addTestSuite(Regression_122396.class);
		suite.addTestSuite(Regression_122807.class);
		suite.addTestSuite(Regression_123202.class);
		suite.addTestSuite(Regression_123208.class);
		suite.addTestSuite(Regression_123554.class);
		suite.addTestSuite(Regression_143105.class);
		suite.addTestSuite(Regression_76910.class);
		suite.addTestSuite(Regression_76963.class);
//		suite.addTestSuite( Regression_78433.class );
		suite.addTestSuite(Regression_94138.class);
		suite.addTestSuite(Regression_98257.class);

		// 2006-08-11
//		To Do: chart script
//		suite.addTestSuite( Regression_121813.class );
		suite.addTestSuite(Regression_121828.class);
		suite.addTestSuite(Regression_121831.class);
//		To Do: chart script	
//		suite.addTestSuite( Regression_121836.class );
		suite.addTestSuite(Regression_131285.class);
//		suite.addTestSuite( Regression_128582.class );

		// 2008-08-18
		suite.addTestSuite(Regression_109622_1.class);
		suite.addTestSuite(Regression_115433.class);
		suite.addTestSuite(Regression_131308.class);
		suite.addTestSuite(Regression_132783.class);
		suite.addTestSuite(Regression_133237.class);
		suite.addTestSuite(Regression_134309.class);
		suite.addTestSuite(Regression_134885.class);
		suite.addTestSuite(Regression_135814.class);
		suite.addTestSuite(Regression_136841.class);
		suite.addTestSuite(Regression_137166.class);
//		suite.addTestSuite( Regression_140620.class );
		suite.addTestSuite(Regression_142685.class);
		suite.addTestSuite(Regression_142689.class);
//		suite.addTestSuite( Regression_144511.class );
//		suite.addTestSuite( Regression_76914.class );
//		suite.addTestSuite( Regression_78746.class );

		suite.addTestSuite(Regression_148393.class);
		suite.addTestSuite(Regression_152127.class);
		suite.addTestSuite(Regression_144845.class);
		suite.addTestSuite(Regression_145473.class);
		suite.addTestSuite(Regression_146308.class);
		suite.addTestSuite(Regression_148308.class);
		suite.addTestSuite(Regression_149936.class);
		suite.addTestSuite(Regression_150240.class);
		suite.addTestSuite(Regression_150779_Count.class);
		suite.addTestSuite(Regression_152545.class);
		suite.addTestSuite(Regression_155185.class);
		suite.addTestSuite(Regression_151575.class);
		suite.addTestSuite(Regression_157608.class);

		return suite;
	}
}
