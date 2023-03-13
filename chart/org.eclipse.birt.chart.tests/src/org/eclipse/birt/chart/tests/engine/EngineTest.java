/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.chart.tests.engine;

import org.eclipse.birt.chart.tests.engine.aggregate.AverageTest;
import org.eclipse.birt.chart.tests.engine.aggregate.SumTest;
import org.eclipse.birt.chart.tests.engine.computation.BoundingBoxTest;
import org.eclipse.birt.chart.tests.engine.computation.MonthDateFormatTest;
import org.eclipse.birt.chart.tests.engine.computation.RectangleTest;
import org.eclipse.birt.chart.tests.engine.computation.ScaleContextTest;
import org.eclipse.birt.chart.tests.engine.computation.ValueFormatterTest;
import org.eclipse.birt.chart.tests.engine.computation.VectorTest;
import org.eclipse.birt.chart.tests.engine.datafeed.DataSetProcessorImplTest;
import org.eclipse.birt.chart.tests.engine.datafeed.ResultSetDataSetTest;
import org.eclipse.birt.chart.tests.engine.datafeed.ResultSetWrapperTest;
import org.eclipse.birt.chart.tests.engine.datafeed.StockDataSetProcessorImplTest;
import org.eclipse.birt.chart.tests.engine.datafeed.StockEntryTest;
import org.eclipse.birt.chart.tests.engine.internal.MatrixTest;
import org.eclipse.birt.chart.tests.engine.internal.PolygonTest;
import org.eclipse.birt.chart.tests.engine.internal.SortKeyTest;
import org.eclipse.birt.chart.tests.engine.internal.TupleComparatorTest;
import org.eclipse.birt.chart.tests.engine.model.ModelAttributeTest;
import org.eclipse.birt.chart.tests.engine.util.ChartExpressionUtilTest;
import org.eclipse.birt.chart.tests.engine.util.ChartUtilTest;
import org.eclipse.birt.chart.tests.engine.util.FittingCalculatorTest;
import org.eclipse.birt.chart.tests.engine.util.FractionTest;
import org.eclipse.birt.chart.tests.engine.util.LiteralHelperTest;
import org.eclipse.birt.chart.tests.engine.util.NameSetTest;
import org.eclipse.birt.chart.tests.engine.util.TriggerSupportMatrixTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class EngineTest {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.eclipse.birt.chart.engine" //$NON-NLS-1$
				+ "and org.eclipse.birt.chart.engine.extension"); //$NON-NLS-1$

		// $JUnit-BEGIN$
		suite.addTestSuite(AverageTest.class);
		suite.addTestSuite(SumTest.class);

		suite.addTestSuite(ValueFormatterTest.class);
		suite.addTestSuite(VectorTest.class);
		suite.addTestSuite(BoundingBoxTest.class);
		suite.addTestSuite(PolygonTest.class);
		suite.addTestSuite(RectangleTest.class);
		suite.addTestSuite(ScaleContextTest.class);
		suite.addTestSuite(MonthDateFormatTest.class);

		suite.addTestSuite(DataSetProcessorImplTest.class);
		suite.addTestSuite(ResultSetDataSetTest.class);
		suite.addTestSuite(ResultSetWrapperTest.class);
		suite.addTestSuite(StockEntryTest.class);
		suite.addTestSuite(StockDataSetProcessorImplTest.class);

		suite.addTestSuite(TupleComparatorTest.class);
		suite.addTestSuite(SortKeyTest.class);
		suite.addTestSuite(PolygonTest.class);
		suite.addTestSuite(MatrixTest.class);

		suite.addTest(ModelAttributeTest.suite());

		suite.addTestSuite(ChartUtilTest.class);
		suite.addTestSuite(ChartExpressionUtilTest.class);
		suite.addTestSuite(LiteralHelperTest.class);
		suite.addTestSuite(FittingCalculatorTest.class);
		suite.addTestSuite(FractionTest.class);
		suite.addTestSuite(NameSetTest.class);
		suite.addTestSuite(TriggerSupportMatrixTest.class);

		// $JUnit-END$
		return suite;
	}

}
