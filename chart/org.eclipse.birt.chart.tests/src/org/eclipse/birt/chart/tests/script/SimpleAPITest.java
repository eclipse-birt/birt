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

package org.eclipse.birt.chart.tests.script;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.chart.tests.script.component.AxisTest;
import org.eclipse.birt.chart.tests.script.component.CategoryTest;
import org.eclipse.birt.chart.tests.script.component.LegendTest;
import org.eclipse.birt.chart.tests.script.component.MarkerLineTest;
import org.eclipse.birt.chart.tests.script.component.MarkerRangeTest;
import org.eclipse.birt.chart.tests.script.component.ValueSeriesTest;
import org.eclipse.birt.chart.tests.script.data.SeriesGroupingTest;
import org.eclipse.birt.chart.tests.script.scale.LinearScaleTest;
import org.eclipse.birt.chart.tests.script.scale.ScaleTest;
import org.eclipse.birt.chart.tests.script.scale.TimeScaleTest;
import org.eclipse.birt.chart.tests.script.series.BarSeriesTest;
import org.eclipse.birt.chart.tests.script.series.PieSeriesTest;
import org.eclipse.birt.chart.tests.script.series.SeriesTypeTest;
import org.eclipse.birt.chart.tests.script.series.StackableSeriesTest;

public class SimpleAPITest {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.eclipse.birt.chart.script.api");//$NON-NLS-1$

		// $JUnit-BEGIN$

		// Package org.eclipse.birt.chart.tests.script
		suite.addTestSuite(ChartTest.class);
		suite.addTestSuite(ChartWithAxesTest.class);
		suite.addTestSuite(ChartWithoutAxesTest.class);

		// Package org.eclipse.birt.chart.tests.script.scale
		suite.addTestSuite(ScaleTest.class);
		suite.addTestSuite(LinearScaleTest.class);
		suite.addTestSuite(TimeScaleTest.class);

		// Package org.eclipse.birt.chart.tests.script.component
		suite.addTestSuite(LegendTest.class);
		suite.addTestSuite(MarkerLineTest.class);
		suite.addTestSuite(MarkerRangeTest.class);
		suite.addTestSuite(AxisTest.class);
		suite.addTestSuite(CategoryTest.class);
		suite.addTestSuite(ValueSeriesTest.class);

		// Package org.eclipse.birt.chart.tests.script.series
		suite.addTestSuite(SeriesTypeTest.class);
		suite.addTestSuite(StackableSeriesTest.class);
		suite.addTestSuite(BarSeriesTest.class);
		suite.addTestSuite(PieSeriesTest.class);

		// Package org.eclipse.birt.chart.tests.script.data
		suite.addTestSuite(SeriesGroupingTest.class);

		// $JUnit-END$
		return suite;
	}

}
