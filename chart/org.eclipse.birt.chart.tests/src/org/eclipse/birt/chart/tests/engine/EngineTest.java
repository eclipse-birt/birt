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
import org.eclipse.birt.chart.tests.engine.computation.ValueFormatterTest;
import org.eclipse.birt.chart.tests.engine.datafeed.DataSetProcessorImplTest;
import org.eclipse.birt.chart.tests.engine.datafeed.ResultSetWrapperTest;
import org.eclipse.birt.chart.tests.engine.datafeed.ResultSetDataSetTest;
import org.eclipse.birt.chart.tests.engine.datafeed.StockEntryTest;
import org.eclipse.birt.chart.tests.engine.exception.ChartExceptionTest;
import org.eclipse.birt.chart.tests.engine.model.ModelAttributeTest;
import org.eclipse.birt.chart.tests.engine.util.CDateTimeTest;
import org.eclipse.birt.chart.tests.engine.util.ChartUtilTest;
import org.eclipse.birt.chart.tests.engine.util.LUDecompositionTest;
import org.eclipse.birt.chart.tests.engine.util.QRDecompositionTest;
import org.eclipse.birt.chart.tests.engine.util.NameSetTest;
import org.eclipse.birt.chart.tests.engine.util.MatrixTest;

public class EngineTest {
	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.eclipse.birt.chart.engine"
						+ "and org.eclipse.birt.chart.engine.extension");

		//$JUnit-BEGIN$
		suite.addTestSuite(AverageTest.class);
		suite.addTestSuite(SumTest.class);

		suite.addTestSuite(ValueFormatterTest.class);

		suite.addTestSuite(DataSetProcessorImplTest.class);
		suite.addTestSuite(ResultSetDataSetTest.class);
		suite.addTestSuite(ResultSetWrapperTest.class);
		suite.addTestSuite(StockEntryTest.class);

		suite.addTestSuite(ChartExceptionTest.class);
		
		suite.addTest(ModelAttributeTest.suite());

		suite.addTestSuite(CDateTimeTest.class);
		suite.addTestSuite(ChartUtilTest.class);
		suite.addTestSuite(LUDecompositionTest.class);
		suite.addTestSuite(QRDecompositionTest.class);
		suite.addTestSuite(NameSetTest.class);
		suite.addTestSuite(MatrixTest.class);

		//$JUnit-END$
		return suite;
	}

}