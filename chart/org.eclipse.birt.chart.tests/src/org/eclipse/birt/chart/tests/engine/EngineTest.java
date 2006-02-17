/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.tests.engine;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.chart.tests.engine.aggregate.AverageTest;
import org.eclipse.birt.chart.tests.engine.aggregate.SumTest;
import org.eclipse.birt.chart.tests.engine.computation.BoundingBoxTest;
import org.eclipse.birt.chart.tests.engine.computation.ValueFormatterTest;
import org.eclipse.birt.chart.tests.engine.computation.VectorTest;
import org.eclipse.birt.chart.tests.engine.datafeed.DataSetProcessorImplTest;
import org.eclipse.birt.chart.tests.engine.datafeed.ResultSetDataSetTest;
import org.eclipse.birt.chart.tests.engine.datafeed.ResultSetWrapperTest;
import org.eclipse.birt.chart.tests.engine.datafeed.StockEntryTest;
import org.eclipse.birt.chart.tests.engine.internal.TupleComparatorTest;
import org.eclipse.birt.chart.tests.engine.model.ModelAttributeTest;
import org.eclipse.birt.chart.tests.engine.util.CDateTimeTest;
import org.eclipse.birt.chart.tests.engine.util.ChartUtilTest;
import org.eclipse.birt.chart.tests.engine.util.LiteralHelperTest;
import org.eclipse.birt.chart.tests.engine.util.NameSetTest;

public class EngineTest
{

	public static Test suite( )
	{
		TestSuite suite = new TestSuite( "Test for org.eclipse.birt.chart.engine" //$NON-NLS-1$
				+ "and org.eclipse.birt.chart.engine.extension" ); //$NON-NLS-1$

		// $JUnit-BEGIN$
		suite.addTestSuite( AverageTest.class );
		suite.addTestSuite( SumTest.class );

		suite.addTestSuite( ValueFormatterTest.class );
		suite.addTestSuite( VectorTest.class );
		suite.addTestSuite( BoundingBoxTest.class );

		suite.addTestSuite( DataSetProcessorImplTest.class );
		suite.addTestSuite( ResultSetDataSetTest.class );
		suite.addTestSuite( ResultSetWrapperTest.class );
		suite.addTestSuite( StockEntryTest.class );
		
		suite.addTestSuite(TupleComparatorTest.class);

		suite.addTest( ModelAttributeTest.suite( ) );

		suite.addTestSuite( CDateTimeTest.class );
		suite.addTestSuite( ChartUtilTest.class );
		suite.addTestSuite( LiteralHelperTest.class );
		suite.addTestSuite( NameSetTest.class );

		// $JUnit-END$
		return suite;
	}

}