
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

import org.eclipse.birt.tests.data.engine.DtEsmoke.AllSmokeTests;
import org.eclipse.birt.tests.data.engine.acceptance.ColumnAliasTest;
import org.eclipse.birt.tests.data.engine.acceptance.DataSourceTest;
import org.eclipse.birt.tests.data.engine.acceptance.FilterTest;
import org.eclipse.birt.tests.data.engine.acceptance.InputParameterTest;
import org.eclipse.birt.tests.data.engine.acceptance.ParameterInt;
import org.eclipse.birt.tests.data.engine.api.DateUtilTest;
import org.eclipse.birt.tests.data.engine.api.FeaturesTest;
import org.eclipse.birt.tests.data.engine.api.MultiPassTest;
import org.eclipse.birt.tests.data.engine.api.MultiPass_FilterTest;
import org.eclipse.birt.tests.data.engine.api.MultiPass_NestedQueryTest;
import org.eclipse.birt.tests.data.engine.api.MultiPass_SortTest;
import org.eclipse.birt.tests.data.engine.regression.Regression_101568;
import org.eclipse.birt.tests.data.engine.regression.Regression_101810;
import org.eclipse.birt.tests.data.engine.regression.Regression_101856;
import org.eclipse.birt.tests.data.engine.regression.Regression_101864;
import org.eclipse.birt.tests.data.engine.regression.Regression_102128;
import org.eclipse.birt.tests.data.engine.regression.Regression_103152;
import org.eclipse.birt.tests.data.engine.regression.Regression_103346;
import org.eclipse.birt.tests.data.engine.regression.Regression_103802;
import org.eclipse.birt.tests.data.engine.regression.Regression_104204;
import org.eclipse.birt.tests.data.engine.regression.Regression_104611;
import org.eclipse.birt.tests.data.engine.regression.Regression_107415;
import org.eclipse.birt.tests.data.engine.regression.Regression_108248;
import org.eclipse.birt.tests.data.engine.regression.Regression_110566;
import org.eclipse.birt.tests.data.engine.regression.Regression_112288;
import org.eclipse.birt.tests.data.engine.regression.Regression_113200;
import org.eclipse.birt.tests.data.engine.regression.Regression_114470;
import org.eclipse.birt.tests.data.engine.regression.Regression_115989;
import org.eclipse.birt.tests.data.engine.regression.Regression_116772;
import org.eclipse.birt.tests.data.engine.regression.Regression_117274;
import org.eclipse.birt.tests.data.engine.regression.Regression_117641;
import org.eclipse.birt.tests.data.engine.regression.Regression_117686;
import org.eclipse.birt.tests.data.engine.regression.Regression_121166;
import org.eclipse.birt.tests.data.engine.regression.Regression_122066;
import org.eclipse.birt.tests.data.engine.regression.Regression_122309;
import org.eclipse.birt.tests.data.engine.regression.Regression_122860;
import org.eclipse.birt.tests.data.engine.regression.Regression_123153;
import org.eclipse.birt.tests.data.engine.regression.Regression_123389;
import org.eclipse.birt.tests.data.engine.regression.Regression_123545;
import org.eclipse.birt.tests.data.engine.regression.Regression_123930;
import org.eclipse.birt.tests.data.engine.regression.Regression_124065;
import org.eclipse.birt.tests.data.engine.regression.Regression_124448;
import org.eclipse.birt.tests.data.engine.regression.Regression_124593;
import org.eclipse.birt.tests.data.engine.regression.Regression_128354;
import org.eclipse.birt.tests.data.engine.regression.Regression_131668;
import org.eclipse.birt.tests.data.engine.regression.Regression_132515;
import org.eclipse.birt.tests.data.engine.regression.Regression_132519;
import org.eclipse.birt.tests.data.engine.regression.Regression_132805;
import org.eclipse.birt.tests.data.engine.regression.Regression_133079;
import org.eclipse.birt.tests.data.engine.regression.Regression_134464;
import org.eclipse.birt.tests.data.engine.regression.Regression_134948;
import org.eclipse.birt.tests.data.engine.regression.Regression_135490;
import org.eclipse.birt.tests.data.engine.regression.Regression_136044;
import org.eclipse.birt.tests.data.engine.regression.Regression_136259;
import org.eclipse.birt.tests.data.engine.regression.Regression_136296;
import org.eclipse.birt.tests.data.engine.regression.Regression_136551;
import org.eclipse.birt.tests.data.engine.regression.Regression_136966;
import org.eclipse.birt.tests.data.engine.regression.Regression_137080;
import org.eclipse.birt.tests.data.engine.regression.Regression_137149;
import org.eclipse.birt.tests.data.engine.regression.Regression_137464;
import org.eclipse.birt.tests.data.engine.regression.Regression_138273;
import org.eclipse.birt.tests.data.engine.regression.Regression_138777;
import org.eclipse.birt.tests.data.engine.regression.Regression_139365;
import org.eclipse.birt.tests.data.engine.regression.Regression_140705;
import org.eclipse.birt.tests.data.engine.regression.Regression_142091;
import org.eclipse.birt.tests.data.engine.regression.Regression_142122;
import org.eclipse.birt.tests.data.engine.regression.Regression_142939;
import org.eclipse.birt.tests.data.engine.regression.Regression_143105;
import org.eclipse.birt.tests.data.engine.regression.Regression_143816;
import org.eclipse.birt.tests.data.engine.regression.Regression_144847;
import org.eclipse.birt.tests.data.engine.regression.Regression_145463;
import org.eclipse.birt.tests.data.engine.regression.Regression_145508;
import org.eclipse.birt.tests.data.engine.regression.Regression_146165;
import org.eclipse.birt.tests.data.engine.regression.Regression_146520;
import org.eclipse.birt.tests.data.engine.regression.Regression_146548;
import org.eclipse.birt.tests.data.engine.regression.Regression_146967;
import org.eclipse.birt.tests.data.engine.regression.Regression_147237;
import org.eclipse.birt.tests.data.engine.regression.Regression_147403;
import org.eclipse.birt.tests.data.engine.regression.Regression_147496;
import org.eclipse.birt.tests.data.engine.regression.Regression_147703;
import org.eclipse.birt.tests.data.engine.regression.Regression_148392;
import org.eclipse.birt.tests.data.engine.regression.Regression_148669;
import org.eclipse.birt.tests.data.engine.regression.Regression_148757;
import org.eclipse.birt.tests.data.engine.regression.Regression_148951;
import org.eclipse.birt.tests.data.engine.regression.Regression_149654;
import org.eclipse.birt.tests.data.engine.regression.Regression_150822;
import org.eclipse.birt.tests.data.engine.regression.Regression_153036;
import org.eclipse.birt.tests.data.engine.regression.Regression_155262;
import org.eclipse.birt.tests.data.engine.regression.Regression_156338;
import org.eclipse.birt.tests.data.engine.regression.Regression_158947;
import org.eclipse.birt.tests.data.engine.regression.Regression_159385;
import org.eclipse.birt.tests.data.engine.regression.Regression_159398;
import org.eclipse.birt.tests.data.engine.regression.Regression_76549;
import org.eclipse.birt.tests.data.engine.regression.Regression_78978;
import org.eclipse.birt.tests.data.engine.regression.Regression_79009;
import org.eclipse.birt.tests.data.engine.regression.Regression_79012;
import org.eclipse.birt.tests.data.engine.regression.Regression_79182;
import org.eclipse.birt.tests.data.engine.regression.Regression_79505;
import org.eclipse.birt.tests.data.engine.regression.Regression_93220;
import org.eclipse.birt.tests.data.engine.regression.Regression_96025;
import org.eclipse.birt.tests.data.engine.regression.flatfile.Regression_105755;
import org.eclipse.birt.tests.data.engine.regression.flatfile.Regression_152210;
import org.eclipse.birt.tests.data.engine.regression.flatfile.Regression_155376;
import org.eclipse.birt.tests.data.engine.regression.flatfile.Regression_160807;
import org.eclipse.birt.tests.data.engine.regression.xml.Regression_117447;
import org.eclipse.birt.tests.data.engine.regression.xml.Regression_119127;
import org.eclipse.birt.tests.data.engine.regression.xml.Regression_119999;
import org.eclipse.birt.tests.data.engine.regression.xml.Regression_120036;
import org.eclipse.birt.tests.data.engine.regression.xml.Regression_123157;
import org.eclipse.birt.tests.data.engine.regression.xml.Regression_144931;
import org.eclipse.birt.tests.data.engine.regression.xml.Regression_146769;
import org.eclipse.birt.tests.data.engine.regression.xml.Regression_147809;
import org.eclipse.birt.tests.data.engine.regression.xml.Regression_153658;
import org.eclipse.birt.tests.data.engine.regression.xml.Regression_154423;
import org.eclipse.birt.tests.data.engine.smoke.DsGroupTest;
import org.eclipse.birt.tests.data.engine.smoke.DsParameterTest;
import org.eclipse.birt.tests.data.engine.smoke.DsSortTest;

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
		 suite.addTestSuite( Regression_101568.class );
		 suite.addTestSuite( Regression_101810.class );
		 suite.addTestSuite( Regression_101856.class );
		 suite.addTestSuite( Regression_101864.class );
		 suite.addTestSuite( Regression_102128.class );
		 suite.addTestSuite( Regression_103152.class );
		 /*
		 * Duplicated cases
		 *
		 * When group interval do not match the type error message should be
		 * displayed suite.addTestSuite( Regression_103292.class );
		 *
		 */
		
		 suite.addTestSuite( Regression_103346.class );
		 suite.addTestSuite( Regression_103802.class );
		 suite.addTestSuite( Regression_104204.class );
		 suite.addTestSuite( Regression_104611.class );
		 suite.addTestSuite( Regression_107415.class );
		 suite.addTestSuite( Regression_108248.class );
		 suite.addTestSuite( Regression_110566.class );
		 suite.addTestSuite( Regression_112288.class );
		 suite.addTestSuite( Regression_113200.class );
		 suite.addTestSuite( Regression_114470.class );
		 suite.addTestSuite( Regression_115989.class );
		 suite.addTestSuite( Regression_116772.class );
		 suite.addTestSuite( Regression_117274.class );
		 /*
		 * Build files do not include the oda.xml project case Regression
		 117437
		 * needs to be considered obsolete
		 */
		 // suite.addTestSuite( Regression_117437.class );
		 suite.addTestSuite( Regression_117641.class );
		 suite.addTestSuite( Regression_117686.class );
		 suite.addTestSuite( Regression_121166.class );
		 suite.addTestSuite( Regression_122066.class );
		 suite.addTestSuite( Regression_122309.class );
		 suite.addTestSuite( Regression_122860.class );
		 suite.addTestSuite( Regression_123153.class );
		 suite.addTestSuite( Regression_123389.class );
		 suite.addTestSuite( Regression_123545.class );
		 suite.addTestSuite( Regression_123930.class );
		 suite.addTestSuite( Regression_124065.class );
		 suite.addTestSuite( Regression_124448.class );
		 suite.addTestSuite( Regression_124593.class );
		 suite.addTestSuite( Regression_128354.class );
		 suite.addTestSuite( Regression_131668.class );
		 suite.addTestSuite( Regression_132515.class );
		 suite.addTestSuite( Regression_132519.class );
		 suite.addTestSuite( Regression_132805.class );
		 suite.addTestSuite( Regression_133079.class );
		 suite.addTestSuite( Regression_134464.class );
		
		 suite.addTestSuite( Regression_134948.class );
		 suite.addTestSuite( Regression_135490.class );
		 suite.addTestSuite( Regression_136044.class );
		 suite.addTestSuite( Regression_136259.class );
		 suite.addTestSuite( Regression_136296.class );
		 suite.addTestSuite( Regression_136551.class );
		 suite.addTestSuite( Regression_136966.class );
		 suite.addTestSuite( Regression_137080.class );
		 suite.addTestSuite( Regression_137149.class );
		 suite.addTestSuite( Regression_137464.class );
		 suite.addTestSuite( Regression_138273.class );
		 suite.addTestSuite( Regression_138777.class );
		 suite.addTestSuite( Regression_139365.class );
		 suite.addTestSuite( Regression_140705.class );
		 /*Do not support relative path in rptdesign*/
		//suite.addTestSuite( Regression_141600.class );
		 suite.addTestSuite( Regression_142091.class );
		 suite.addTestSuite( Regression_142122.class );
		 suite.addTestSuite( Regression_142939.class );
		 suite.addTestSuite( Regression_143105.class );
		 suite.addTestSuite( Regression_143816.class );
		
		 /*
		 * Case 144179 is Obsolete, it's a UI bug, has been reviewed by
		 MingXia
		 * Wu
		 */
		 // suite.addTestSuite( Regression_144179.class );
		 suite.addTestSuite( Regression_144847.class );
		 suite.addTestSuite( Regression_145463.class );
		 suite.addTestSuite( Regression_145508.class );
		 suite.addTestSuite( Regression_146165.class );
		 suite.addTestSuite( Regression_146520.class );
		 suite.addTestSuite( Regression_146548.class );
		 suite.addTestSuite( Regression_146967.class );
		 suite.addTestSuite( Regression_147237.class );
		 suite.addTestSuite( Regression_147403.class );
		 suite.addTestSuite( Regression_147496.class );
		 suite.addTestSuite( Regression_147703.class );
		 suite.addTestSuite( Regression_148392.class );
		 suite.addTestSuite( Regression_148669.class );
		 suite.addTestSuite( Regression_148757.class );
		 suite.addTestSuite( Regression_148951.class );
		 suite.addTestSuite( Regression_149654.class );
		 suite.addTestSuite( Regression_150822.class );
		 suite.addTestSuite( Regression_153036.class );
		 suite.addTestSuite( Regression_155262.class );
		 suite.addTestSuite( Regression_156338.class );
		 suite.addTestSuite( Regression_158947.class );
		 suite.addTestSuite( Regression_159385.class );
		 suite.addTestSuite( Regression_159398.class );
		 suite.addTestSuite( Regression_76549.class );
		 suite.addTestSuite( Regression_78978.class );
		 suite.addTestSuite( Regression_79009.class );
		 suite.addTestSuite( Regression_79012.class );
		 suite.addTestSuite( Regression_79182.class );
		
		 suite.addTestSuite( Regression_79505.class );
		 suite.addTestSuite( Regression_93220.class );
		 suite.addTestSuite( Regression_96025.class );
		
		 // // XML package
		 suite.addTestSuite( Regression_119999.class );
		 suite.addTestSuite( Regression_123157.class );
		 suite.addTestSuite( Regression_117447.class );
		 suite.addTestSuite( Regression_119127.class );
		 suite.addTestSuite( Regression_120036.class );
		 suite.addTestSuite( Regression_146769.class );
		 suite.addTestSuite( Regression_144931.class );
		 suite.addTestSuite( Regression_153658.class );
		 suite.addTestSuite( Regression_147809.class );
		 suite.addTestSuite( Regression_154423.class );
		
		 // flat file package
		 suite.addTestSuite( Regression_160807.class );
		 suite.addTestSuite( Regression_155376.class );
		 suite.addTestSuite( Regression_152210.class );
		 suite.addTestSuite( Regression_105755.class );
		
		 // API
		
		 suite.addTestSuite( FeaturesTest.class );
		 suite.addTestSuite( MultiPass_FilterTest.class );
		 suite.addTestSuite( MultiPass_NestedQueryTest.class );
		 suite.addTestSuite( MultiPass_SortTest.class );
		 suite.addTestSuite( MultiPassTest.class );
		 suite.addTestSuite( DateUtilTest.class );
		 // TODO:suite.addTestSuite( ReportDoc_ComputedColumnTest.class );
		 // TODO:suite.addTestSuite( ReportDoc_FilterTest.class );
		 // TODO:suite.addTestSuite( ReportDoc_Gen_PreModeTest.class );
		 // TODO:suite.addTestSuite( ReportDoc_GroupTest.class );
		 // TODO:suite.addTestSuite( ReportDoc_ParameterTest.class );
		
		 // Acceptance
		 suite.addTestSuite( ColumnAliasTest.class );
		 // TODO:suite.addTestSuite( ComputedColumnTest.class );
		 suite.addTestSuite( DataSourceTest.class );
		 suite.addTestSuite( FilterTest.class );
		 suite.addTestSuite( InputParameterTest.class );
		 suite.addTestSuite( ParameterInt.class );
		 // TODO:suite.addTestSuite( testPassThruContext.class );
		
		 // Smoke
		 // TODO:suite.addTestSuite( DsAggregationTest.class );
		 // TODO:suite.addTestSuite( DsCombinationTest.class );
		 // TODO:suite.addTestSuite( DsComputeColumnTest.class );
		 // TODO:suite.addTestSuite( DsFilterTest.class );
		 suite.addTestSuite( DsGroupTest.class );
		 suite.addTestSuite( DsParameterTest.class );
		 suite.addTestSuite( DsSortTest.class );
		
		// // DtESmokeTest
		suite.addTest( AllSmokeTests.suite( ) );

		return suite;
	}

}
