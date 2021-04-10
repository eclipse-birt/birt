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
import org.eclipse.birt.chart.model.attribute.SortOption;

public class SortOptionTest extends TestCase {

	public void testConstant() {
		assertEquals(SortOption.ASCENDING, SortOption.ASCENDING_LITERAL.getValue());
		assertEquals(SortOption.DESCENDING, SortOption.DESCENDING_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(SortOption.ASCENDING_LITERAL, SortOption.get(SortOption.ASCENDING));

		assertEquals(SortOption.ASCENDING_LITERAL, SortOption.get("Ascending")); //$NON-NLS-1$
		assertEquals(SortOption.DESCENDING_LITERAL, SortOption.get("Descending")); //$NON-NLS-1$

		assertNull(SortOption.get("No Match")); //$NON-NLS-1$
	}
}