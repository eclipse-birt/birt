package org.eclipse.birt.tests.data.engine;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.eclipse.birt.tests.data.engine.api.*;
import org.eclipse.birt.tests.data.engine.acceptance.*;
import org.eclipse.birt.tests.data.engine.smoke.*;

public class AllTests extends TestCase{

	public AllTests(String name){
		super(name);
	}
	
	protected void setUp( ) 
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown( ) 
	{

	}

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.eclipse.birt.tests.data");
		//API
		suite.addTestSuite(FeaturesTest.class);
		suite.addTestSuite(MultiPass_FilterTest.class);
		suite.addTestSuite(MultiPass_NestedQueryTest.class);
		suite.addTestSuite(MultiPass_SortTest.class);
		suite.addTestSuite(MultiPassTest.class);
		suite.addTestSuite(ReportDoc_ComputedColumnTest.class);
		suite.addTestSuite(ReportDoc_FilterTest.class);
		suite.addTestSuite(ReportDoc_Gen_PreModeTest.class);
		suite.addTestSuite(ReportDoc_GroupTest.class);
		suite.addTestSuite(ReportDoc_ParameterTest.class);

		//Acceptance
		suite.addTestSuite(ColumnAliasTest.class);
		suite.addTestSuite(ComputedColumnTest.class);
		suite.addTestSuite(DataSourceTest.class);
		suite.addTestSuite(FilterTest.class);
		suite.addTestSuite(InputParameterTest.class);
		suite.addTestSuite(ParameterInt.class);
		suite.addTestSuite(testPassThruContext.class);	
		
		//Smoke
		suite.addTestSuite(DsAggregationTest.class);
		suite.addTestSuite(DsCombinationTest.class);
		suite.addTestSuite(DsComputeColumnTest.class);
		suite.addTestSuite(DsFilterTest.class);
		suite.addTestSuite(DsGroupTest.class);
		suite.addTestSuite(DsParameterTest.class);
		suite.addTestSuite(DsSortTest.class);				
		return suite;
	}
}
