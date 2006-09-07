package org.eclipse.birt.tests.data.engine;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.birt.tests.data.engine.acceptance.ColumnAliasTest;
import org.eclipse.birt.tests.data.engine.acceptance.DataSourceTest;
import org.eclipse.birt.tests.data.engine.acceptance.FilterTest;
import org.eclipse.birt.tests.data.engine.acceptance.InputParameterTest;
import org.eclipse.birt.tests.data.engine.acceptance.ParameterInt;
import org.eclipse.birt.tests.data.engine.api.FeaturesTest;
import org.eclipse.birt.tests.data.engine.api.MultiPassTest;
import org.eclipse.birt.tests.data.engine.api.MultiPass_FilterTest;
import org.eclipse.birt.tests.data.engine.api.MultiPass_NestedQueryTest;
import org.eclipse.birt.tests.data.engine.api.MultiPass_SortTest;
import org.eclipse.birt.tests.data.engine.regression.Reg_101568;
import org.eclipse.birt.tests.data.engine.regression.Reg_101810;
import org.eclipse.birt.tests.data.engine.regression.Reg_101856;
import org.eclipse.birt.tests.data.engine.regression.Reg_101864;
import org.eclipse.birt.tests.data.engine.regression.Reg_102128;
import org.eclipse.birt.tests.data.engine.regression.Reg_103346;
import org.eclipse.birt.tests.data.engine.regression.Reg_103802;
import org.eclipse.birt.tests.data.engine.regression.Reg_104204;
import org.eclipse.birt.tests.data.engine.regression.Reg_104611;
import org.eclipse.birt.tests.data.engine.regression.Reg_107415;
import org.eclipse.birt.tests.data.engine.regression.Reg_108248;
import org.eclipse.birt.tests.data.engine.regression.Reg_110566;
import org.eclipse.birt.tests.data.engine.regression.Reg_113200;
import org.eclipse.birt.tests.data.engine.regression.Reg_114470;
import org.eclipse.birt.tests.data.engine.regression.Reg_114898;
import org.eclipse.birt.tests.data.engine.regression.Reg_115989;
import org.eclipse.birt.tests.data.engine.regression.Reg_116772;
import org.eclipse.birt.tests.data.engine.regression.Reg_117274;
import org.eclipse.birt.tests.data.engine.regression.Reg_117641;
import org.eclipse.birt.tests.data.engine.regression.Reg_122066;
import org.eclipse.birt.tests.data.engine.regression.Reg_122309;
import org.eclipse.birt.tests.data.engine.regression.Reg_122860;
import org.eclipse.birt.tests.data.engine.regression.Reg_123389;
import org.eclipse.birt.tests.data.engine.regression.Reg_123545;
import org.eclipse.birt.tests.data.engine.regression.Reg_145508;
import org.eclipse.birt.tests.data.engine.regression.Reg_76549;
import org.eclipse.birt.tests.data.engine.regression.Reg_79009;
import org.eclipse.birt.tests.data.engine.regression.Reg_79012;
import org.eclipse.birt.tests.data.engine.regression.Reg_79182;
import org.eclipse.birt.tests.data.engine.regression.Reg_79505;
import org.eclipse.birt.tests.data.engine.regression.Reg_93220;
import org.eclipse.birt.tests.data.engine.regression.Regression_103152;
import org.eclipse.birt.tests.data.engine.regression.Regression_112288;
import org.eclipse.birt.tests.data.engine.regression.Regression_117437;
import org.eclipse.birt.tests.data.engine.regression.Regression_117686;
import org.eclipse.birt.tests.data.engine.regression.Regression_121166;
import org.eclipse.birt.tests.data.engine.regression.Regression_123153;
import org.eclipse.birt.tests.data.engine.regression.Regression_123930;
import org.eclipse.birt.tests.data.engine.regression.Regression_124065;
import org.eclipse.birt.tests.data.engine.regression.Regression_124448;
import org.eclipse.birt.tests.data.engine.regression.Regression_124593;
import org.eclipse.birt.tests.data.engine.regression.Regression_128354;
import org.eclipse.birt.tests.data.engine.regression.Regression_131668;
import org.eclipse.birt.tests.data.engine.regression.Regression_132519;
import org.eclipse.birt.tests.data.engine.regression.Regression_132805;
import org.eclipse.birt.tests.data.engine.regression.Regression_134948;
import org.eclipse.birt.tests.data.engine.regression.Regression_135490;
import org.eclipse.birt.tests.data.engine.regression.Regression_136259;
import org.eclipse.birt.tests.data.engine.regression.Regression_136296;
import org.eclipse.birt.tests.data.engine.regression.Regression_136551;
import org.eclipse.birt.tests.data.engine.regression.Regression_136966;
import org.eclipse.birt.tests.data.engine.regression.Regression_137080;
import org.eclipse.birt.tests.data.engine.regression.Regression_137149;
import org.eclipse.birt.tests.data.engine.regression.Regression_138273;
import org.eclipse.birt.tests.data.engine.regression.Regression_138777;
import org.eclipse.birt.tests.data.engine.regression.Regression_139365;
import org.eclipse.birt.tests.data.engine.regression.Regression_141600;
import org.eclipse.birt.tests.data.engine.regression.Regression_142122;
import org.eclipse.birt.tests.data.engine.regression.Regression_142939;
import org.eclipse.birt.tests.data.engine.regression.Regression_143105;
import org.eclipse.birt.tests.data.engine.regression.Regression_143816;
import org.eclipse.birt.tests.data.engine.regression.Regression_144179;
import org.eclipse.birt.tests.data.engine.regression.Regression_145508;
import org.eclipse.birt.tests.data.engine.regression.Regression_96025;
import org.eclipse.birt.tests.data.engine.regression.xml.Regression_119999;
import org.eclipse.birt.tests.data.engine.regression.xml.Regression_120036;
import org.eclipse.birt.tests.data.engine.regression.xml.Regression_123157;
import org.eclipse.birt.tests.data.engine.smoke.DsGroupTest;
import org.eclipse.birt.tests.data.engine.smoke.DsParameterTest;
import org.eclipse.birt.tests.data.engine.smoke.DsSortTest;

public class AllTests extends TestCase {

	public AllTests(String name) {
		super(name);
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.eclipse.birt.tests.data");
		// API
		suite.addTestSuite(FeaturesTest.class);
		suite.addTestSuite(MultiPass_FilterTest.class);
		suite.addTestSuite(MultiPass_NestedQueryTest.class);
		suite.addTestSuite(MultiPass_SortTest.class);
		suite.addTestSuite(MultiPassTest.class);
		// TODO:suite.addTestSuite( ReportDoc_ComputedColumnTest.class );
		// TODO:suite.addTestSuite( ReportDoc_FilterTest.class );
		// TODO:suite.addTestSuite( ReportDoc_Gen_PreModeTest.class );
		// TODO:suite.addTestSuite( ReportDoc_GroupTest.class );
		// TODO:suite.addTestSuite( ReportDoc_ParameterTest.class );
		suite.addTestSuite(Reg_145508.class);

		// Acceptance
		suite.addTestSuite(ColumnAliasTest.class);
		// TODO:suite.addTestSuite( ComputedColumnTest.class );
		suite.addTestSuite(DataSourceTest.class);
		suite.addTestSuite(FilterTest.class);
		suite.addTestSuite(InputParameterTest.class);
		suite.addTestSuite(ParameterInt.class);
		// TODO:suite.addTestSuite( testPassThruContext.class );

		// Smoke
		// TODO:suite.addTestSuite( DsAggregationTest.class );
		// TODO:suite.addTestSuite( DsCombinationTest.class );
		// TODO:suite.addTestSuite( DsComputeColumnTest.class );
		// TODO:suite.addTestSuite( DsFilterTest.class );
		suite.addTestSuite(DsGroupTest.class);
		suite.addTestSuite(DsParameterTest.class);
		suite.addTestSuite(DsSortTest.class);

		// Regression

		suite.addTestSuite(Reg_101568.class);
		suite.addTestSuite(Reg_101810.class);
		suite.addTestSuite(Reg_101864.class);
		suite.addTestSuite(Reg_104204.class);
		suite.addTestSuite(Reg_104611.class);
		suite.addTestSuite(Reg_107415.class);
		suite.addTestSuite(Reg_108248.class);
		suite.addTestSuite(Reg_110566.class);
		suite.addTestSuite(Reg_113200.class);
		suite.addTestSuite(Reg_114470.class);
		suite.addTestSuite(Reg_117274.class);
		suite.addTestSuite(Reg_122860.class);
		suite.addTestSuite(Reg_123389.class);
		suite.addTestSuite(Reg_123545.class);
		suite.addTestSuite(Reg_76549.class);
		suite.addTestSuite(Reg_79009.class);
		suite.addTestSuite(Reg_79012.class);
		suite.addTestSuite(Reg_79182.class);
		suite.addTestSuite(Reg_93220.class);
		suite.addTestSuite(Reg_101856.class);
		suite.addTestSuite(Reg_102128.class);
		suite.addTestSuite(Reg_103346.class);
		suite.addTestSuite(Reg_103802.class);
		suite.addTestSuite(Reg_114898.class);
		suite.addTestSuite(Reg_115989.class);
		suite.addTestSuite(Reg_116772.class);
		suite.addTestSuite(Regression_117437.class);
		suite.addTestSuite(Reg_117641.class);
		suite.addTestSuite(Reg_122066.class);
		suite.addTestSuite(Reg_122309.class);
		suite.addTestSuite(Reg_79505.class);

		suite.addTestSuite(Regression_141600.class);
		suite.addTestSuite(Regression_137080.class);
		// TODO: suite.addTestSuite( Regression_136542.class );
		suite.addTestSuite(Regression_103152.class);
		suite.addTestSuite(Regression_145508.class);

		suite.addTestSuite(Regression_123930.class);
		suite.addTestSuite(Regression_124065.class);
		suite.addTestSuite(Regression_131668.class);
		suite.addTestSuite(Regression_132519.class);
		suite.addTestSuite(Regression_136551.class);
		suite.addTestSuite(Regression_138273.class);
		suite.addTestSuite(Regression_138777.class);
		suite.addTestSuite(Regression_139365.class);
		suite.addTestSuite(Regression_142122.class);
		suite.addTestSuite(Regression_142939.class);
		suite.addTestSuite(Regression_137149.class);

		suite.addTestSuite(Regression_112288.class);
		suite.addTestSuite(Regression_136966.class);
		suite.addTestSuite(Regression_136296.class);
		suite.addTestSuite(Regression_135490.class);
		suite.addTestSuite(Regression_136259.class);
		suite.addTestSuite(Regression_132805.class);
		suite.addTestSuite(Regression_124593.class);
		suite.addTestSuite(Regression_128354.class);
		suite.addTestSuite(Regression_117686.class);
		suite.addTestSuite(Regression_123153.class);
		suite.addTestSuite(Regression_96025.class);
		suite.addTestSuite(Regression_121166.class);

		suite.addTestSuite(Regression_143105.class);
		suite.addTestSuite(Regression_143105.class);
		suite.addTestSuite(Regression_143816.class);
		suite.addTestSuite(Regression_144179.class);
		suite.addTestSuite(Regression_124448.class);
		suite.addTestSuite(Regression_134948.class);
		// xml package
		suite.addTestSuite(Regression_119999.class);
		suite.addTestSuite(Regression_120036.class);
		suite.addTestSuite(Regression_123157.class);
		return suite;
	}
}
