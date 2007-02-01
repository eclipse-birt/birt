package org.eclipse.birt.report.tests.chart.regression;


import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests cases in activity package.
 */

public class AllRegressionTests
{

	/**
	 * @return the test
	 */

	public static Test suite( )
	{
		TestSuite suite = new TestSuite( );

		// add all test classes here
		suite.addTestSuite( Regression_101039.class );
		suite.addTestSuite( Regression_101855.class );
		suite.addTestSuite( Regression_101868.class );
		suite.addTestSuite( Regression_102455.class );
		suite.addTestSuite( Regression_103447.class );
		suite.addTestSuite( Regression_103787.class );
		suite.addTestSuite( Regression_103960.class );
		suite.addTestSuite( Regression_103961.class );
		suite.addTestSuite( Regression_104472.class );
		suite.addTestSuite( Regression_104627.class );
		suite.addTestSuite( Regression_106126.class );
		suite.addTestSuite( Regression_109641.class );
		suite.addTestSuite( Regression_110760.class );
		suite.addTestSuite( Regression_113536.class );
		suite.addTestSuite( Regression_118797.class );
		suite.addTestSuite( Regression_119411.class );
		suite.addTestSuite( Regression_119776.class );
		suite.addTestSuite( Regression_119805.class );
		suite.addTestSuite( Regression_120557.class );
		suite.addTestSuite( Regression_120919.class );
		suite.addTestSuite( Regression_121292.class );
		//To Do: chart script
//		suite.addTestSuite( Regression_121383.class );
		
		suite.addTestSuite( Regression_121954.class );
		suite.addTestSuite( Regression_122343.class );
		suite.addTestSuite( Regression_122371.class );
		suite.addTestSuite( Regression_122392.class );
		suite.addTestSuite( Regression_122396.class );
		suite.addTestSuite( Regression_122807.class );
		suite.addTestSuite( Regression_123202.class );
		suite.addTestSuite( Regression_123208.class );
		suite.addTestSuite( Regression_123554.class );
		suite.addTestSuite( Regression_123561.class );
		suite.addTestSuite( Regression_143105.class );
		suite.addTestSuite( Regression_144519.class );
		suite.addTestSuite( Regression_76910.class );
		suite.addTestSuite( Regression_76963.class );
		suite.addTestSuite( Regression_78040.class );
		suite.addTestSuite( Regression_78433.class );
		suite.addTestSuite( Regression_94138.class );
		suite.addTestSuite( Regression_98257.class );
		
		//2006-08-11
		suite.addTestSuite( Regression_115965.class );
		suite.addTestSuite( Regression_116800.class );
		suite.addTestSuite( Regression_118188.class );
//		To Do: chart script
//		suite.addTestSuite( Regression_121813.class );
		suite.addTestSuite( Regression_121816.class );
		suite.addTestSuite( Regression_121828.class );
		suite.addTestSuite( Regression_121829.class );
		suite.addTestSuite( Regression_121831.class );
//		To Do: chart script	
//		suite.addTestSuite( Regression_121836.class );
		suite.addTestSuite( Regression_121847.class );
		suite.addTestSuite( Regression_131285.class );
//		suite.addTestSuite( Regression_128582.class );
		suite.addTestSuite( Regression_130073.class );
		
		
		//2008-08-18
		suite.addTestSuite( Regression_109622_1.class );
		suite.addTestSuite( Regression_115433.class );
		suite.addTestSuite( Regression_122835.class );
		suite.addTestSuite( Regression_128355.class );
		suite.addTestSuite( Regression_131308.class );
		suite.addTestSuite( Regression_132513.class );
		suite.addTestSuite( Regression_132783.class );
		suite.addTestSuite( Regression_133237.class );
		suite.addTestSuite( Regression_134309.class );
		suite.addTestSuite( Regression_134455.class );
		suite.addTestSuite( Regression_134885.class );
		suite.addTestSuite( Regression_135814.class );
		suite.addTestSuite( Regression_136586.class );
		suite.addTestSuite( Regression_136837.class );
		suite.addTestSuite( Regression_136841.class );
		suite.addTestSuite( Regression_137166.class );
		suite.addTestSuite( Regression_137462.class );
		suite.addTestSuite( Regression_137655.class );
		suite.addTestSuite( Regression_137874.class );
		suite.addTestSuite( Regression_140620.class );
		suite.addTestSuite( Regression_141939.class );
		suite.addTestSuite( Regression_142685.class );
		suite.addTestSuite( Regression_142689.class );
		suite.addTestSuite( Regression_144511.class );
		suite.addTestSuite( Regression_76914.class );
		suite.addTestSuite( Regression_78746.class );
		
		suite.addTestSuite( Regression_148393.class );
		suite.addTestSuite( Regression_150475.class );
		suite.addTestSuite( Regression_152127.class );
		suite.addTestSuite( Regression_144845.class );
		suite.addTestSuite( Regression_145473.class );
		suite.addTestSuite( Regression_145710.class );
		suite.addTestSuite( Regression_145715.class );
		suite.addTestSuite( Regression_146308.class );
		suite.addTestSuite( Regression_148308.class );
		suite.addTestSuite( Regression_149936.class );
		suite.addTestSuite( Regression_150240.class );
		suite.addTestSuite( Regression_150778.class );
		suite.addTestSuite( Regression_150779_Count.class );
		suite.addTestSuite( Regression_150779_DistinctCount.class );
		suite.addTestSuite( Regression_150779_First.class );
		suite.addTestSuite( Regression_150779_Last.class );
		suite.addTestSuite( Regression_150779_Max.class );
		suite.addTestSuite( Regression_150779_Min.class );
		suite.addTestSuite( Regression_152545.class );
		suite.addTestSuite( Regression_153979.class );
		suite.addTestSuite( Regression_155185.class );
		suite.addTestSuite( Regression_155587.class );
		suite.addTestSuite( Regression_156650.class );
		suite.addTestSuite( Regression_151575.class );
		suite.addTestSuite( Regression_157608.class );

		
		return suite;
	}
}