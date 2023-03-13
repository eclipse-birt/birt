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

package org.eclipse.birt.report.model.core;

import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests methods on the structure.
 */

public class StructureTest extends BaseTestCase {

	/**
	 * Tests equals method.
	 */

	public void testComputedColumnsEquals() {
		ComputedColumn column1 = StructureFactory.createComputedColumn();
		ComputedColumn column2 = StructureFactory.createComputedColumn();

		column1.setName("name"); //$NON-NLS-1$
		column1.setExpression("expression"); //$NON-NLS-1$
		column1.setDataType(DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL);

		column2.setName("name"); //$NON-NLS-1$
		column2.setExpression("expression"); //$NON-NLS-1$
		column2.setDataType(DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL);

		assertTrue(column1.equals(column2));

		column1.setDataType(null);

		assertFalse(column1.equals(column2));

		column2.setDataType(null);

		assertTrue(column1.equals(column2));
	}

	/**
	 * Test new property aggregrateOn expression.
	 */

	public void testAggregrateOn() {
		ComputedColumn column1 = StructureFactory.createComputedColumn();
		column1.setName("name"); //$NON-NLS-1$
		column1.setExpression("expression"); //$NON-NLS-1$
		column1.setDataType(DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL);
		column1.setAggregrateOn(ComputedColumn.AGGREGRATEON_MEMBER);

		assertEquals("name", column1.getName());//$NON-NLS-1$
		assertEquals(ComputedColumn.AGGREGRATEON_MEMBER, column1.getAggregrateOn());

	}
}
