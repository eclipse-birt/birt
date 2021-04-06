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