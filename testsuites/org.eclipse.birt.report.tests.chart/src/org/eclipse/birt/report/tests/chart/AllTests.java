
package org.eclipse.birt.report.tests.chart;

import junit.framework.Test;
import junit.framework.TestSuite;

//import org.eclipse.birt.report.tests.chart.acceptance.AllAcceptanceTests;
import org.eclipse.birt.report.tests.chart.regression.AllRegressionTests;
//import org.eclipse.birt.report.tests.chart.smoke.AllSmokeTests;
import org.eclipse.birt.report.tests.chart.test.imageCompare;

/**
 *
 * All test cases for tests chart
 */
public class AllTests {
	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.eclipse.birt.report.tests.chart"); //$NON-NLS-1$

		// regression test case
		suite.addTest(AllRegressionTests.suite());

		// acceptance test case
//		suite.addTest( AllAcceptanceTests.suite( ) );

		// smoke test case
//		suite.addTest( AllSmokeTests.suite( ) );

		// produce the image compare html
		suite.addTestSuite(imageCompare.class);

//		
		return suite;
		/*
		 * AreaChart_3D st1 = new AreaChart_3D( );
		 * 
		 * OverlayAreaChart_2D st3 = new OverlayAreaChart_2D( );
		 * OverlayAreaChart_2DWithDepth st4 = new OverlayAreaChart_2DWithDepth( );
		 * OverlayLineChart_2D st5 = new OverlayLineChart_2D( );
		 * OverlayLineChart_2DWithDepth st6 = new OverlayLineChart_2DWithDepth( );
		 * PercentStackedAreaChart_2D st7 = new PercentStackedAreaChart_2D( );
		 * PercentStackedAreaChart_2DWithDepth st8 = new
		 * PercentStackedAreaChart_2DWithDepth( ); PercentStackedBarChart_2D st9 = new
		 * PercentStackedBarChart_2D( ); PercentStackedBarChart_2DWithDepth st10 = new
		 * PercentStackedBarChart_2DWithDepth( ); PercentStackedLineChart_2D st11 = new
		 * PercentStackedLineChart_2D( ); PercentStackedLineChart_2DWithDepth st12 = new
		 * PercentStackedLineChart_2DWithDepth( ); PieChart_2D st13 = new PieChart_2D(
		 * ); PieChart_2DWithDepth st14 = new PieChart_2DWithDepth( ); ScatterChart st15
		 * = new ScatterChart( ); SideBySideBarChart_2D st16 = new
		 * SideBySideBarChart_2D( ); SideBySideBarChart_2DWithDepth st17 = new
		 * SideBySideBarChart_2DWithDepth( ); StackedAreaChart_2D st18 = new
		 * StackedAreaChart_2D( ); StackedAreaChart_2DWithDepth st19 = new
		 * StackedAreaChart_2DWithDepth( ); StackedBarChart_2D st20 = new
		 * StackedBarChart_2D( ); StackedBarChart_2DWithDepth st21 = new
		 * StackedBarChart_2DWithDepth( ); StackedBarChart_3D st22 = new
		 * StackedBarChart_3D( ); StackedLineChart_2D st23 = new StackedLineChart_2D( );
		 * StackedLineChart_2DWithDepth st24 = new StackedLineChart_2DWithDepth( );
		 * StandardMeterChart st25 = new StandardMeterChart( ); StockChart st26 = new
		 * StockChart( ); SuperimposedMeterChart st27 = new SuperimposedMeterChart( );
		 */
	}
}
