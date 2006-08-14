
package org.eclipse.birt.report.tests.chart;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 *
 *  All test cases for tests chart
 */
public class AllTests 
{
	public static Test suite( )
	{
		TestSuite suite = new TestSuite( "Test for org.eclipse.birt.report.tests.chart"); //$NON-NLS-1$
		// acceptance test case
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.AreaChart_3D.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.LineChart_3D.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.OverlayAreaChart_2D.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.OverlayAreaChart_2DWithDepth.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.OverlayLineChart_2D.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.OverlayLineChart_2DWithDepth.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.PercentStackedAreaChart_2D.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.PercentStackedAreaChart_2DWithDepth.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.PercentStackedBarChart_2D.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.PercentStackedBarChart_2DWithDepth.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.PercentStackedLineChart_2D.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.PercentStackedLineChart_2DWithDepth.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.PieChart_2D.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.PieChart_2DWithDepth.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.ScatterChart.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.SideBySideBarChart_2D.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.SideBySideBarChart_2DWithDepth.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.StackedAreaChart_2D.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.StackedAreaChart_2DWithDepth.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.StackedBarChart_2D.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.StackedBarChart_2DWithDepth.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.StackedBarChart_3D.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.StackedLineChart_2D.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.StackedLineChart_2DWithDepth.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.StandardMeterChart.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.StockChart.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.acceptance.SuperimposedMeterChart.class );
		
		// smoke test case
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.Create3DBarChart_1.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.Create3DBarChart_2.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.Create3DLineChart_1.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.Create3DLineChart_2.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateAreaChart_1.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateAreaChart_2.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateAreaChart_3.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateBarChart_1.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateBarChart_2.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateBarChart_3.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateBarChart_4.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateCFAreaChart_1.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateCFAreaChart_2.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateCFBarChart_1.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateCFBarChart_2.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateCFLineChart_1.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateCFLineChart_2.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateCFLineChart_3.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateCFLineChart_4.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateCFStockChart_1.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateLineChart_1.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateLineChart_2.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateLineChart_3.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateMeterChart_1.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateMeterChart_2.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateMeterChart_3.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreatePieChart_1.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreatePieChart_2.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreatePieChart_3.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateScatterChart_1.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateScatterChart_2.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.CreateStockChart_1.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.LabelStaggering_1.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.LegendTitle_1.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.LegendTitle_2.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.smoke.MarkerShape_1.class );
		
		//regression test case
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_101039.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_101855.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_101868.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_102455.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_103447.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_103787.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_103960.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_103961.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_104472.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_104627.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_106126.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_109641.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_110760.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_113536.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_118797.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_119411.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_119776.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_119805.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_120557.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_120919.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_121292.class );
		//To Do: chart script
//		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_121383.class );
		
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_121954.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_122343.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_122371.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_122392.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_122396.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_122807.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_123202.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_123208.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_123554.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_123561.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_143105.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_144519.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_76910.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_76963.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_78040.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_78433.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_94138.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_98257.class );
		
		//2006-08-11
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_115965.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_116800.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_118188.class );
//		To Do: chart script
//		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_121813.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_121816.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_121828.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_121829.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_121831.class );
//		To Do: chart script	
//		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_121836.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_121847.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_131285.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_128582.class );
		suite.addTestSuite( org.eclipse.birt.report.tests.chart.regression.Regression_130073.class );
		return suite;
		
		/*
		AreaChart_3D st1 = new AreaChart_3D( );
		
		OverlayAreaChart_2D st3 = new OverlayAreaChart_2D( );
		OverlayAreaChart_2DWithDepth st4 = new OverlayAreaChart_2DWithDepth( );
		OverlayLineChart_2D st5 = new OverlayLineChart_2D( );
		OverlayLineChart_2DWithDepth st6 = new OverlayLineChart_2DWithDepth( );
		PercentStackedAreaChart_2D st7 = new PercentStackedAreaChart_2D( );
		PercentStackedAreaChart_2DWithDepth st8 = new PercentStackedAreaChart_2DWithDepth( );
		PercentStackedBarChart_2D st9 = new PercentStackedBarChart_2D( );
		PercentStackedBarChart_2DWithDepth st10 = new PercentStackedBarChart_2DWithDepth( );
		PercentStackedLineChart_2D st11 = new PercentStackedLineChart_2D( );
		PercentStackedLineChart_2DWithDepth st12 = new PercentStackedLineChart_2DWithDepth( );
		PieChart_2D st13 = new PieChart_2D( );
		PieChart_2DWithDepth st14 = new PieChart_2DWithDepth( );
		ScatterChart st15 = new ScatterChart( );
		SideBySideBarChart_2D st16 = new SideBySideBarChart_2D( );
		SideBySideBarChart_2DWithDepth st17 = new SideBySideBarChart_2DWithDepth( );
		StackedAreaChart_2D st18 = new StackedAreaChart_2D( );
		StackedAreaChart_2DWithDepth st19 = new StackedAreaChart_2DWithDepth( );
		StackedBarChart_2D st20 = new StackedBarChart_2D( );
		StackedBarChart_2DWithDepth st21 = new StackedBarChart_2DWithDepth( );
		StackedBarChart_3D st22 = new StackedBarChart_3D( );
		StackedLineChart_2D st23 = new StackedLineChart_2D( );
		StackedLineChart_2DWithDepth st24 = new StackedLineChart_2DWithDepth( );
		StandardMeterChart st25 = new StandardMeterChart( );
		StockChart st26 = new StockChart( );
		SuperimposedMeterChart st27 = new SuperimposedMeterChart( );
		*/
	}
}
