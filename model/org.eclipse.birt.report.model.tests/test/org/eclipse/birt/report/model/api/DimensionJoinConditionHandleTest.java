/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import java.util.List;

import org.eclipse.birt.report.model.api.elements.structures.DimensionCondition;
import org.eclipse.birt.report.model.api.elements.structures.DimensionJoinCondition;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.elements.interfaces.ITabularCubeModel;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test <code>DimensionJoinConditionHandle</code>.
 */

public class DimensionJoinConditionHandleTest extends BaseTestCase {

	/**
	 * Test dropAndClear method in DimensionHandle.
	 *
	 * @throws Exception
	 */

	public void testDropAndClear() throws Exception {
		openDesign("DimensionJoinConditionHandleTest_2.xml");//$NON-NLS-1$

		TabularCubeHandle cube = (TabularCubeHandle) designHandle.findCube("Customer Cube"); //$NON-NLS-1$

		PropertyHandle propHandle = cube.getPropertyHandle(ITabularCubeModel.DIMENSION_CONDITIONS_PROP);

		List propList = propHandle.getListValue();
		assertEquals(3, propList.size());

		// delete the first dimension condition and then let the
		// 'CachedMemberRef' in the backref of hierarchy is invalid
		propHandle.drop(0);
		assertEquals(2, propHandle.getListValue().size());
		// add then drop the dimension and its content hierarchy
		DimensionHandle dimensionHandle = cube.getDimension("Group");//$NON-NLS-1$
		dimensionHandle.dropAndClear();

		save();
		assertTrue(compareFile("DimensionJoinConditionHandleTest_golden.xml")); //$NON-NLS-1$

	}

	/**
	 * Test equals method in DimensionJoinConditionHandle.
	 *
	 * @throws Exception
	 */
	public void testEquals() throws Exception {
		openDesign("DimensionJoinConditionHandleTest.xml");//$NON-NLS-1$

		TabularCubeHandle cube = (TabularCubeHandle) designHandle.findCube("testCube"); //$NON-NLS-1$

		DimensionCondition condition = new DimensionCondition();
		DimensionConditionHandle structHandle = cube.addDimensionCondition(condition);

		DimensionJoinConditionHandle joinConditionHandle = structHandle.addJoinCondition(new DimensionJoinCondition());
		joinConditionHandle.setCubeKey("addCubeKey"); //$NON-NLS-1$
		joinConditionHandle.setHierarchyKey("addHierarchyKey"); //$NON-NLS-1$

		DimensionJoinConditionHandle joinConditionHandle2 = structHandle.addJoinCondition(new DimensionJoinCondition());
		joinConditionHandle2.setCubeKey("addCubeKey"); //$NON-NLS-1$
		joinConditionHandle2.setHierarchyKey("addHierarchyKey"); //$NON-NLS-1$

		assertFalse(joinConditionHandle.equals(joinConditionHandle2));

		assertTrue(joinConditionHandle.equals(structHandle.getJoinConditions().get(0)));

	}
}
