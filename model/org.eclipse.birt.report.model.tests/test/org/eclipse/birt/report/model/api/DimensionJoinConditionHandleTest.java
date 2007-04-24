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

import org.eclipse.birt.report.model.api.elements.structures.DimensionCondition;
import org.eclipse.birt.report.model.api.elements.structures.DimensionJoinCondition;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Test <code>DimensionJoinConditionHandle</code>.
 */

public class DimensionJoinConditionHandleTest extends BaseTestCase
{

	/**
	 * Test equals method in DimensionJoinConditionHandle.
	 * 
	 * @throws Exception
	 */
	public void testEquals( ) throws Exception
	{
		openDesign( "DimensionJoinConditionHandleTest.xml" );//$NON-NLS-1$

		TabularCubeHandle cube = (TabularCubeHandle) designHandle
				.findCube( "testCube" ); //$NON-NLS-1$

		DimensionCondition condition = new DimensionCondition( );
		DimensionConditionHandle structHandle = cube
				.addDimensionCondition( condition );

		DimensionJoinConditionHandle joinConditionHandle = structHandle
				.addJoinCondition( new DimensionJoinCondition( ) );
		joinConditionHandle.setCubeKey( "addCubeKey" ); //$NON-NLS-1$
		joinConditionHandle.setHierarchyKey( "addHierarchyKey" ); //$NON-NLS-1$

		DimensionJoinConditionHandle joinConditionHandle2 = structHandle
				.addJoinCondition( new DimensionJoinCondition( ) );
		joinConditionHandle2.setCubeKey( "addCubeKey" ); //$NON-NLS-1$
		joinConditionHandle2.setHierarchyKey( "addHierarchyKey" ); //$NON-NLS-1$

		assertFalse( joinConditionHandle.equals( joinConditionHandle2 ) );

		assertTrue( joinConditionHandle.equals( structHandle
				.getJoinConditions( ).get( 0 ) ) );

	}
}
