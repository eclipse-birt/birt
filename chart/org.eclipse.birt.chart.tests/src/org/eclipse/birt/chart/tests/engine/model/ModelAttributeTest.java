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

package org.eclipse.birt.chart.tests.engine.model;

import org.eclipse.birt.chart.tests.engine.model.attribute.ActionTypeTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.AnchorTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.AxisTypeTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.ChartDimensionTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.ChartTypeTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.DataPointComponentTypeTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.DataTypeTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.DateFormatDetailTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.DateFormatTypeTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.DirectionTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.GroupingUnitTypeTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.HorizontalAlignmentTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.IntersectionTypeTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.LeaderLineStyleTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.LegendItemTypeTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.LineStyleTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.MarkerTypeTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.OrientationTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.PaletteTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.PositionTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.RiserTypeTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.RuleTypeTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.ScaleUnitTypeTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.SortOptionTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.StretchTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.StyleComponentTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.TickStyleTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.TriggerConditionTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.UnitsOfMeasurementTest;
import org.eclipse.birt.chart.tests.engine.model.attribute.VerticalAlignmentTest;

import junit.framework.Test;
import junit.framework.TestSuite;

public class ModelAttributeTest {
	public static Test suite() {

		TestSuite suite = new TestSuite("Test for org.eclipse.birt.chart.model.attribute"); //$NON-NLS-1$

		// $JUnit-BEGIN$
		suite.addTestSuite(ActionTypeTest.class);
		suite.addTestSuite(AnchorTest.class);
		suite.addTestSuite(AxisTypeTest.class);
		suite.addTestSuite(ChartDimensionTest.class);
		suite.addTestSuite(ChartTypeTest.class);
		suite.addTestSuite(DataPointComponentTypeTest.class);
		suite.addTestSuite(DataTypeTest.class);
		suite.addTestSuite(DateFormatDetailTest.class);
		suite.addTestSuite(DateFormatTypeTest.class);
		suite.addTestSuite(DirectionTest.class);
		suite.addTestSuite(GroupingUnitTypeTest.class);
		suite.addTestSuite(HorizontalAlignmentTest.class);
		suite.addTestSuite(IntersectionTypeTest.class);
		suite.addTestSuite(LeaderLineStyleTest.class);
		suite.addTestSuite(LegendItemTypeTest.class);
		suite.addTestSuite(LineStyleTest.class);
		suite.addTestSuite(MarkerTypeTest.class);
		suite.addTestSuite(OrientationTest.class);
		suite.addTestSuite(PositionTest.class);
		suite.addTestSuite(RuleTypeTest.class);
		suite.addTestSuite(RiserTypeTest.class);
		suite.addTestSuite(ScaleUnitTypeTest.class);
		suite.addTestSuite(SortOptionTest.class);
		suite.addTestSuite(StretchTest.class);
		suite.addTestSuite(StyleComponentTest.class);
		suite.addTestSuite(TickStyleTest.class);
		suite.addTestSuite(TriggerConditionTest.class);
		suite.addTestSuite(UnitsOfMeasurementTest.class);
		suite.addTestSuite(VerticalAlignmentTest.class);
		suite.addTestSuite(PaletteTest.class);

		// $JUnit-END$
		return suite;
	}

}
