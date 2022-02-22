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
package org.eclipse.birt.chart.tests.engine.model.attribute;

import org.eclipse.birt.chart.model.attribute.DataPointComponentType;

import junit.framework.TestCase;

public class DataPointComponentTypeTest extends TestCase {

	public void testConstant() {
		assertEquals(DataPointComponentType.BASE_VALUE, DataPointComponentType.BASE_VALUE_LITERAL.getValue());
		assertEquals(DataPointComponentType.ORTHOGONAL_VALUE,
				DataPointComponentType.ORTHOGONAL_VALUE_LITERAL.getValue());
		assertEquals(DataPointComponentType.SERIES_VALUE, DataPointComponentType.SERIES_VALUE_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(DataPointComponentType.BASE_VALUE_LITERAL,
				DataPointComponentType.get(DataPointComponentType.BASE_VALUE));
		assertEquals(DataPointComponentType.SERIES_VALUE_LITERAL,
				DataPointComponentType.get(DataPointComponentType.SERIES_VALUE));

		assertEquals(DataPointComponentType.BASE_VALUE_LITERAL, DataPointComponentType.get("Base_Value")); //$NON-NLS-1$
		assertEquals(DataPointComponentType.ORTHOGONAL_VALUE_LITERAL, DataPointComponentType.get("Orthogonal_Value")); //$NON-NLS-1$
		assertEquals(DataPointComponentType.SERIES_VALUE_LITERAL, DataPointComponentType.get("Series_Value")); //$NON-NLS-1$

		assertNull(DataPointComponentType.get("No Match")); //$NON-NLS-1$
	}
}
