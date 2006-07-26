
package org.eclipse.birt.tests.data.engine;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.birt.tests.data.engine.acceptance.ColumnAliasTest;
import org.eclipse.birt.tests.data.engine.acceptance.ComputedColumnTest;
import org.eclipse.birt.tests.data.engine.acceptance.DataSourceTest;
import org.eclipse.birt.tests.data.engine.acceptance.FilterTest;
import org.eclipse.birt.tests.data.engine.acceptance.InputParameterTest;
import org.eclipse.birt.tests.data.engine.acceptance.ParameterInt;
import org.eclipse.birt.tests.data.engine.acceptance.testPassThruContext;
import org.eclipse.birt.tests.data.engine.api.FeaturesTest;
import org.eclipse.birt.tests.data.engine.api.MultiPassTest;
import org.eclipse.birt.tests.data.engine.api.MultiPass_FilterTest;
import org.eclipse.birt.tests.data.engine.api.MultiPass_NestedQueryTest;
import org.eclipse.birt.tests.data.engine.api.MultiPass_SortTest;
import org.eclipse.birt.tests.data.engine.api.ReportDoc_ComputedColumnTest;
import org.eclipse.birt.tests.data.engine.api.ReportDoc_FilterTest;
import org.eclipse.birt.tests.data.engine.api.ReportDoc_Gen_PreModeTest;
import org.eclipse.birt.tests.data.engine.api.ReportDoc_GroupTest;
import org.eclipse.birt.tests.data.engine.api.ReportDoc_ParameterTest;
import org.eclipse.birt.tests.data.engine.smoke.DsAggregationTest;
import org.eclipse.birt.tests.data.engine.smoke.DsCombinationTest;
import org.eclipse.birt.tests.data.engine.smoke.DsComputeColumnTest;
import org.eclipse.birt.tests.data.engine.smoke.DsFilterTest;
import org.eclipse.birt.tests.data.engine.smoke.DsGroupTest;
import org.eclipse.birt.tests.data.engine.smoke.DsParameterTest;
import org.eclipse.birt.tests.data.engine.smoke.DsSortTest;

public class AllTests extends TestCase
{

	public AllTests( String name )
	{
		super( name );
	}

	public static Test suite( )
	{
		TestSuite suite = new TestSuite( "Test for org.eclipse.birt.tests.data" );
		// API
		suite.addTestSuite( FeaturesTest.class );
		suite.addTestSuite( MultiPass_FilterTest.class );
		suite.addTestSuite( MultiPass_NestedQueryTest.class );
		suite.addTestSuite( MultiPass_SortTest.class );
		suite.addTestSuite( MultiPassTest.class );
		suite.addTestSuite( ReportDoc_ComputedColumnTest.class );
		suite.addTestSuite( ReportDoc_FilterTest.class );
		suite.addTestSuite( ReportDoc_Gen_PreModeTest.class );
		suite.addTestSuite( ReportDoc_GroupTest.class );
		suite.addTestSuite( ReportDoc_ParameterTest.class );

		// Acceptance
		suite.addTestSuite( ColumnAliasTest.class );
		suite.addTestSuite( ComputedColumnTest.class );
		suite.addTestSuite( DataSourceTest.class );
		suite.addTestSuite( FilterTest.class );
		suite.addTestSuite( InputParameterTest.class );
		suite.addTestSuite( ParameterInt.class );
		suite.addTestSuite( testPassThruContext.class );

		// Smoke
		suite.addTestSuite( DsAggregationTest.class );
		suite.addTestSuite( DsCombinationTest.class );
		suite.addTestSuite( DsComputeColumnTest.class );
		suite.addTestSuite( DsFilterTest.class );
		suite.addTestSuite( DsGroupTest.class );
		suite.addTestSuite( DsParameterTest.class );
		suite.addTestSuite( DsSortTest.class );
		return suite;
	}
}
