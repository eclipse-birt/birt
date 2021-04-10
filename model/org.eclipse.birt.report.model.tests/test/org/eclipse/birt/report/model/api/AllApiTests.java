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

package org.eclipse.birt.report.model.api;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests cases in extension package.
 */

public class AllApiTests {

	/**
	 * @return the test
	 */

	public static Test suite() {
		TestSuite test = new TestSuite();

		// add all test classes here
		test.addTestSuite(ActionHandleTest.class);
		test.addTestSuite(APICompatibleTest.class);
		test.addTestSuite(AutoTextHandleTest.class);
		test.addTestSuite(BoundDataColumnsUtilTest.class);
		test.addTestSuite(CascadingParameterGroupTest.class);
		test.addTestSuite(CellHandleTest.class);
		test.addTestSuite(ClientsDerivedIteratorTest.class);
		test.addTestSuite(ColorHandleTest.class);
		test.addTestSuite(ColorUtilTest.class);
		test.addTestSuite(CompatibilityUtilTest.class);
		test.addTestSuite(ComplexValueHandleTest.class);
		test.addTestSuite(ComponentsInGridHandleTest.class);
		test.addTestSuite(CustomColorHandleTest.class);
		test.addTestSuite(DefaultSearchFileAlgorithmTest.class);
		test.addTestSuite(DesignElementHandleTest.class);
		test.addTestSuite(DesignEngineTest.class);
		test.addTestSuite(DimensionHandleTest.class);
		test.addTestSuite(ElementExporterTest.class);
		test.addTestSuite(ElementFactoryTest.class);
		test.addTestSuite(FactoryElementHandleTest.class);
		test.addTestSuite(FactoryPropertyHandleTest.class);
		test.addTestSuite(FontHandleTest.class);
		test.addTestSuite(GridColumnHandleTest.class);
		test.addTestSuite(GridItemHandleTest.class);
		test.addTestSuite(GroupElementHandleTest.class);
		test.addTestSuite(GroupHandleTest.class);
		test.addTestSuite(GroupPropertyHandleTest.class);
		test.addTestSuite(ImageHandleTest.class);
		test.addTestSuite(JoinConditionHandleTest.class);
		test.addTestSuite(JointDataSetHandleTest.class);
		test.addTestSuite(LabelHandleTest.class);
		test.addTestSuite(LayoutTableTest.class);
		test.addTestSuite(MasterPageHandleTest.class);
		test.addTestSuite(ModuleUtilTest.class);
		test.addTestSuite(ParameterValidationUtilTest.class);
		test.addTestSuite(PropertyHandleTest.class);
		test.addTestSuite(PropertySortingTest.class);
		test.addTestSuite(ReportDesignHandleTest.class);
		test.addTestSuite(ReportElementHandleTest.class);
		test.addTestSuite(ReportItemHandleTest.class);
		test.addTestSuite(ScalarParameterHandleTest.class);
		test.addTestSuite(ScriptLibHandleTest.class);
		test.addTestSuite(SessionHandleTest.class);
		test.addTestSuite(SimpleMasterPageHandleTest.class);
		test.addTestSuite(SlotHandleTest.class);
		test.addTestSuite(StructureFactoryTest.class);
		test.addTestSuite(StructureHandleTest.class);
		test.addTestSuite(StyleHandleTest.class);
		test.addTestSuite(TableColumnBandTest.class);
		test.addTestSuite(TableItemHandleTest.class);
		test.addTestSuite(TranslationHandleTest.class);
		test.addTestSuite(UserPropertyHandleTest.class);
		test.addTestSuite(ComputedColumnHandleTest.class);
		test.addTestSuite(MemberValueHandleTest.class);
		test.addTestSuite(ReportItemDataRefTest.class);
		test.addTestSuite(MultiViewHandleTest.class);
		test.addTestSuite(ReportDesignCacheTest.class);
		test.addTestSuite(HighlightRuleHandleTest.class);
		test.addTestSuite(ExpressionTest.class);
		test.addTestSuite(ColumnHandleTest.class);
		test.addTestSuite(StyleUtilTest.class);

		return test;
	}
}
