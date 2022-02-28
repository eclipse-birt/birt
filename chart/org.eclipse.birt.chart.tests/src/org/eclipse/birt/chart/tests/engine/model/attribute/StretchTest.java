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

import org.eclipse.birt.chart.model.attribute.Stretch;

import junit.framework.TestCase;

public class StretchTest extends TestCase {

	public void testConstant() {
		assertEquals(Stretch.HORIZONTAL, Stretch.HORIZONTAL_LITERAL.getValue());
		assertEquals(Stretch.VERTICAL, Stretch.VERTICAL_LITERAL.getValue());
		assertEquals(Stretch.BOTH, Stretch.BOTH_LITERAL.getValue());
	}

	public void testGet() {
		assertEquals(Stretch.HORIZONTAL_LITERAL, Stretch.get(Stretch.HORIZONTAL));
		assertEquals(Stretch.VERTICAL_LITERAL, Stretch.get(Stretch.VERTICAL));

		assertEquals(Stretch.HORIZONTAL_LITERAL, Stretch.get("Horizontal")); //$NON-NLS-1$
		assertEquals(Stretch.VERTICAL_LITERAL, Stretch.get("Vertical")); //$NON-NLS-1$
		assertEquals(Stretch.BOTH_LITERAL, Stretch.get("Both")); //$NON-NLS-1$

		assertNull(Stretch.get("No Match")); //$NON-NLS-1$
	}
}
