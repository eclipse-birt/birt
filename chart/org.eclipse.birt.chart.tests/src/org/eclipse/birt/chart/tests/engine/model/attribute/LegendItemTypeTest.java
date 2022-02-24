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

import junit.framework.TestCase;
import org.eclipse.birt.chart.model.attribute.LegendItemType;

public class LegendItemTypeTest extends TestCase {

	public void testConstant() {
		assertEquals(LegendItemType.SERIES, LegendItemType.SERIES_LITERAL.getValue());
		assertEquals(LegendItemType.CATEGORIES, LegendItemType.CATEGORIES_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(LegendItemType.SERIES_LITERAL, LegendItemType.get(LegendItemType.SERIES));

		assertEquals(LegendItemType.SERIES_LITERAL, LegendItemType.get("Series")); //$NON-NLS-1$
		assertEquals(LegendItemType.CATEGORIES_LITERAL, LegendItemType.get("Categories")); //$NON-NLS-1$

		assertNull(LegendItemType.get("No Match")); //$NON-NLS-1$
	}
}
